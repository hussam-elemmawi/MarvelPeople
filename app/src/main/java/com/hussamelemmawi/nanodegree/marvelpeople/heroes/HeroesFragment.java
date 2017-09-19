package com.hussamelemmawi.nanodegree.marvelpeople.heroes;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussamelemmawi.nanodegree.marvelpeople.Filters;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelDetailsActivity;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelPeopleApp;
import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.util.ScrollChildSwipeRefreshLayout;
import com.hussamelemmawi.nanodegree.marvelpeople.util.TransitionHelper;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.saeid.fabloading.LoadingView;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * Displays a gird of Heroes. User can choose to view all or favorite ones.
 * View holders viewing logic only.
 * Pass user actions to {@link HeroesPresenter} to handle it.
 */
public class HeroesFragment extends Fragment implements HeroesContract.View {

  public static final String TAG = HeroesFragment.class.getSimpleName();
  /**
   * {@code @Inject} to request a dependency (for a field here)
   */
  @Inject
  HeroesPresenter mPresenter;

  private HeroesAdapter mGridAdapter;

  private AppCompatActivity mActivity;

  @BindView(R.id.recycler_heroes)
  RecyclerView rvHeroes;

  @BindView(R.id.scroll_up_fab)
  FloatingActionButton scrollUpFab;

  @BindView(R.id.loading_layout)
  FrameLayout loadingLayout;

  @BindView(R.id.loading_fab)
  LoadingView loadingFab;

  private StaggeredGridLayoutManager staggeredGridLayoutManager;

  public static final String ACTION_DATA_UPDATE =
    "com.hussamelemmawi.nanodegree.marvelpeople.heroes.ACTION_DATA_UPDATE";

  /* By default load first item into hero details, otherwise load incoming heroFrom widget */
  private boolean loadDefault = true;

  private boolean fabIsUp = false;

  private boolean loadNextPage = false;

  private AnimatorSet loadingIndicatorSet;

  public static HeroesFragment newInstance() {
    return new HeroesFragment();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    ((MarvelPeopleApp) context.getApplicationContext()).getApplicationComponent().inject(this);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mActivity = (AppCompatActivity) getActivity();
    mGridAdapter = new HeroesAdapter(new ArrayList<Hero>(0));
    // For widget sake!
    // Set the current hero id to the chosen heroFrom widget
    // So hero details presenter can load the right hero.
    if (mActivity.getIntent() != null) {
      if (mActivity.getIntent().getExtras() != null)
        if (mActivity.getIntent().getExtras().getString("heroId") != null) {
          loadDefault = false;
        }
    }
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_heroes, container, false);

    ButterKnife.bind(this, root);

    rvHeroes.setAdapter(mGridAdapter);

    staggeredGridLayoutManager =
      new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    rvHeroes.setLayoutManager(staggeredGridLayoutManager);

    rvHeroes.addOnScrollListener(onScrollListener);

    // Set up progress indicator
    final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
      (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
    swipeRefreshLayout.setColorSchemeColors(
      ContextCompat.getColor(getActivity(), R.color.colorPrimary),
      ContextCompat.getColor(getActivity(), R.color.colorAccent),
      ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    );
    // Set the scrolling view in the custom SwipeRefreshLayout.
    swipeRefreshLayout.setScrollUpChild(rvHeroes);

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {

        if (mPresenter.getFilteringIsFavorite()) {
          nothingToRefresh();
        } else {
          // Here passing true to force network update.
          mPresenter.loadHeroes(true);
        }
      }
    });

    scrollUpFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rvHeroes.smoothScrollToPosition(0);
      }
    });

    setHasOptionsMenu(true);

    loadingIndicatorSet = new AnimatorSet();

    prepareLoadingFab();

    return root;
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.bind(this);
    slideDownFab();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Start loading heroes into the grid
    mPresenter.start();
  }

  @Override
  public void onStop() {
    super.onStop();
    mPresenter.unbind(this);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.marvel_fargment_menu, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_filter:
        showFilteringPopUpMenu();
        break;
    }
    return true;
  }

  @Override
  public void showHeroes(List<Hero> heroes) {
    mGridAdapter.replaceData(heroes);
  }

  private void transitionToActivity(HeroViewHolder viewHolder, Hero hero) {
    final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), false,
      new Pair<>(viewHolder.heroThumbnail, getString(R.string.transition_image)));
    startDetailsActivity(pairs, hero);
  }

  private void startDetailsActivity(Pair<View, String>[] pairs, Hero hero) {
    Intent i = new Intent(getContext(), MarvelDetailsActivity.class);
    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
    i.putExtra(getString(R.string.fragment_key), Filters.MarvelMode.HEROES);
    i.putExtra("heroId", hero.getId());
    startActivity(i, transitionActivityOptions.toBundle());
  }

  @Override
  public void showLoadingHeroesError() {
    showMessage(mActivity.getString(R.string.error_cant_refresh));
  }

  @Override
  public void setLoadingIndicator(final boolean active) {

    if (getView() == null) {
      return;
    }
    final SwipeRefreshLayout srl =
      (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

    // Make sure setRefreshing() is called after the layout is done with everything else.
    srl.post(new Runnable() {
      @Override
      public void run() {
        srl.setRefreshing(active);
      }
    });
  }

  @Override
  public void setFetchingNewPageIndicator(boolean active) {
    if (active) {
      loadNextPage = true;
      animateLoadingNewPage();
    } else {
      animateStopLoadingNewPage();
      loadNextPage = false;
    }
    if (fabIsUp)
      slideDownFab();
  }

  @Override
  public void showFilteringPopUpMenu() {
    PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
    popup.getMenuInflater().inflate(R.menu.filter_heroes, popup.getMenu());

    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
          case R.id.all:
            mPresenter.setFilteringType(Filters.MarvelFavorite.ALL);
            break;
          case R.id.favorite:
            mPresenter.setFilteringType(Filters.MarvelFavorite.FAVORITE);
            break;
          default:
            mPresenter.setFilteringType(Filters.MarvelFavorite.ALL);
            break;
        }
        mPresenter.loadHeroes(false);
        return true;
      }
    });

    popup.show();
  }

  @Override
  public void nothingToRefresh() {
    showMessage(mActivity.getString(R.string.error_nothing_to_refresh));
    setLoadingIndicator(false);
  }

  @Override
  public void showNoFavoriteHeroes() {
    mPresenter.setFilteringType(Filters.MarvelFavorite.ALL);
    mPresenter.loadHeroes(false);
    showMessage(mActivity.getString(R.string.error_no_fav_heroes));
  }

  private void showMessage(String message) {
    Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void slideUpFab() {
    if (!fabIsUp) {
      ObjectAnimator obj = ObjectAnimator.ofFloat(scrollUpFab, "translationY", -128);
      obj.setDuration(1000);
      obj.setInterpolator(new FastOutSlowInInterpolator());
      obj.start();
      fabIsUp = !fabIsUp;
    }
  }

  @Override
  public void slideDownFab() {
    if (fabIsUp) {
      ObjectAnimator obj = ObjectAnimator.ofFloat(scrollUpFab, "translationY", 128);
      obj.setDuration(1000);
      obj.setInterpolator(new FastOutSlowInInterpolator());
      obj.start();
      fabIsUp = !fabIsUp;
    }
  }

  private void animateLoadingNewPage() {
    loadingLayout.setVisibility(View.VISIBLE);
    rvHeroes.setAlpha(0.4f);
    scaleFabWide();
    loadingFab.resumeAnimation();
    loadingFab.startAnimation();
  }

  private void animateStopLoadingNewPage() {
    loadingFab.pauseAnimation();
    scaleFabNarrow();
    loadingLayout.setVisibility(View.GONE);
    rvHeroes.setAlpha(1.0f);
  }

  public void scaleFabWide() {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(loadingFab, "scaleX", 1.5f);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(loadingFab, "scaleY", 1.5f);

    AnimatorSet scaleWide = new AnimatorSet();
    scaleWide.playTogether(scaleX, scaleY);
    scaleWide.setDuration(500);
    scaleWide.setInterpolator(new FastOutSlowInInterpolator());
    scaleWide.start();
  }

  public void scaleFabNarrow() {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(loadingFab, "scaleX", 0);
    ObjectAnimator scaleY = ObjectAnimator.ofFloat(loadingFab, "scaleY", 0);

    AnimatorSet scaleNarrow = new AnimatorSet();
    scaleNarrow.playTogether(scaleX, scaleY);
    scaleNarrow.setDuration(700);
    scaleNarrow.setInterpolator(new LinearOutSlowInInterpolator());
    scaleNarrow.start();
  }

  private void prepareLoadingFab() {
    int wolfy = R.drawable.marvel_1_lollipop;
    int spidy = R.drawable.marvel_2_lollipop;
    int irono = R.drawable.marvel_3_lollipop;
    int capitano = R.drawable.marvel_4_lollipop;
    loadingFab.addAnimation(Color.parseColor("#FFD200"), wolfy, LoadingView.FROM_LEFT);
    loadingFab.addAnimation(Color.parseColor("#2F5DA9"), spidy, LoadingView.FROM_TOP);
    loadingFab.addAnimation(Color.parseColor("#FF4218"), irono, LoadingView.FROM_RIGHT);
    loadingFab.addAnimation(Color.parseColor("#C7E7FB"), capitano, LoadingView.FROM_BOTTOM);
    loadingFab.setScaleX(0.0f);
    loadingFab.setScaleY(0.0f);
  }

  /**
   * Recycler view scroll listener implementation
   */
  private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
      super.onScrollStateChanged(recyclerView, newState);

      if (newState == RecyclerView.SCROLL_STATE_IDLE) { // No scrolling
        if (!loadNextPage) {
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              slideDownFab();
            }
          }, 3500);
        }
      } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) { // User dragging now
        slideUpFab();
      } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {// User left scrolling and it is slowing down
        slideUpFab();
      }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
      super.onScrolled(recyclerView, dx, dy);

      int itemCounts = staggeredGridLayoutManager.getItemCount();

      int lastVisibleItem[] = staggeredGridLayoutManager
        .findLastCompletelyVisibleItemPositions(null);

      // I'm adding 2: 1 as the indexing start with 0 so I add 1 in case reaching last item == 19
      // and items count == 20, another 1 for making sure if there is just one item inflated on a span
      // such a case, the 18th item will be the last of 20 items count
      // I added another 1 as there is some bugs and this is just a quick hack!
      if (lastVisibleItem[0] + 3 >= itemCounts) {
        mPresenter.getNextPage();
        loadNextPage = true;
      }
    }
  };

  private class HeroesAdapter extends RecyclerView.Adapter<HeroViewHolder> {

    private List<Hero> mHeroes;

    HeroesAdapter(List<Hero> heroes) {
      setList(heroes);
    }

    void replaceData(List<Hero> heroes) {
      setList(heroes);
      notifyDataSetChanged();

      // Send a Broadcast for widget to update.
      Intent broadcastIntent = new Intent(ACTION_DATA_UPDATE)
        .setPackage(getContext().getPackageName());
      getContext().sendBroadcast(broadcastIntent);
    }

    void setList(List<Hero> heros) {
      mHeroes = checkNotNull(heros);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public int getItemCount() {
      return mHeroes.size();
    }

    @Override
    public HeroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = mActivity.getLayoutInflater().inflate(R.layout.hero_row_item, parent, false);
      final HeroViewHolder vh = new HeroViewHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(final HeroViewHolder holder, final int position) {

      final Hero hero = mHeroes.get(position);
      String imageUrl = Utility.imageUrlLarge(hero.getThumbnailPath(),
        hero.getThumbnailExtension());

      Picasso.with(getContext())
        .load(imageUrl)
        .placeholder(R.drawable.grid_item_img_placeholder)
        .into(holder.heroThumbnail);

      holder.heroName.setText(hero.getName());

      if (hero.isFavorite()) {
        holder.iconFavorite.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
      } else {
        holder.iconFavorite.setBackgroundResource(R.drawable.ic_star_grey_24dp);
      }

      holder.heroThumbnail.setContentDescription(getString(R.string.comic_thumbnail));
      holder.heroThumbnail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          transitionToActivity(holder, hero);
        }
      });
    }
  }

  public class HeroViewHolder extends RecyclerView.ViewHolder {
    ImageView heroThumbnail;
    TextView heroName;
    ImageView iconFavorite;

    public HeroViewHolder(View view) {
      super(view);
      heroThumbnail = (ImageView) view.findViewById(R.id.card_thumbnail);
      heroName = (TextView) view.findViewById(R.id.card_name);
      iconFavorite = (ImageView) view.findViewById(R.id.card_favorite_icon);
    }
  }
}
