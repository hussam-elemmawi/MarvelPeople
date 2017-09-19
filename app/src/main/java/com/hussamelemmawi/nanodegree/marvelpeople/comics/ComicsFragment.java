package com.hussamelemmawi.nanodegree.marvelpeople.comics;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussamelemmawi.nanodegree.marvelpeople.Filters;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelDetailsActivity;
import com.hussamelemmawi.nanodegree.marvelpeople.MarvelPeopleApp;
import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.util.HeartBeatInterpolator;
import com.hussamelemmawi.nanodegree.marvelpeople.util.ScrollChildSwipeRefreshLayout;
import com.hussamelemmawi.nanodegree.marvelpeople.util.TransitionHelper;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 17/02/17.
 * <p>
 * Displays a gird of Comics. User can choose to view all or favorite ones.
 * View holders viewing logic only.
 * Pass user actions to {@link ComicsPresenter} to handle it.
 */

public class ComicsFragment extends Fragment implements ComicsContract.View {

  public static final String TAG = ComicsFragment.class.getSimpleName();
  /**
   * {@code @Inject} to request a dependency (for a field here)
   */
  @Inject
  ComicsPresenter mPresenter;

  private ComicsAdapter mComicsAdapter;

  private AppCompatActivity mActivity;

  @BindView(R.id.recycler_comics)
  RecyclerView rvComics;

  @BindView(R.id.comics_scroll_utility_fab)
  FloatingActionButton scrollUtilityFab;

  private LinearLayoutManager linearLayoutManager;

  private boolean fabIsUp = false;

  private boolean loadNextPage = false;

  private AnimatorSet loadingIndicatorSet;


  public static ComicsFragment newInstance() {
    return new ComicsFragment();
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
    mComicsAdapter = new ComicsAdapter(new ArrayList<Comic>(0));
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_comics, container, false);

    ButterKnife.bind(this, root);

    rvComics.setAdapter(mComicsAdapter);

    linearLayoutManager = new LinearLayoutManager(getContext());
    rvComics.setLayoutManager(linearLayoutManager);

    rvComics.addOnScrollListener(onScrollListener);

    // Set up progress indicator
    final ScrollChildSwipeRefreshLayout swipeRefreshLayout =
      (ScrollChildSwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
    swipeRefreshLayout.setColorSchemeColors(
      ContextCompat.getColor(getActivity(), R.color.colorPrimary),
      ContextCompat.getColor(getActivity(), R.color.colorAccent),
      ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
    );
    // Set the scrolling view in the custom SwipeRefreshLayout.
    swipeRefreshLayout.setScrollUpChild(rvComics);

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {

        if (mPresenter.getFilteringIsFavorite()) {
          nothingToRefresh();
        } else {
          // Here passing true to force network update.
          mPresenter.loadComics(true);
        }
      }
    });

    scrollUtilityFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        rvComics.smoothScrollToPosition(0);
      }
    });

    setHasOptionsMenu(true);

    loadingIndicatorSet = new AnimatorSet();

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
    // Start loading comics into the grid
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
  public void showComics(List<Comic> comics) {
    mComicsAdapter.replaceData(comics);
  }

  private void transitionToActivity(ComicHolder viewHolder, Comic comic) {
    final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(getActivity(), false,
      new Pair<>(viewHolder.comicThumbnail, getString(R.string.transition_image)));
    startDetailsActivity(pairs, comic);
  }

  private void startDetailsActivity(Pair<View, String>[] pairs, Comic comic) {
    Intent i = new Intent(getContext(), MarvelDetailsActivity.class);
    ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pairs);
    i.putExtra(getString(R.string.fragment_key), Filters.MarvelMode.COMICS);
    i.putExtra("comicId", comic.getId());
    startActivity(i, transitionActivityOptions.toBundle());
  }

  @Override
  public void showLoadingComicsError() {
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
      setLoadingIndicator(true);
      rvComics.setAlpha(0.3f);
    } else {
      setLoadingIndicator(false);
      rvComics.setAlpha(1.0f);
      loadNextPage = false;
    }
    if (fabIsUp)
      slideDownFab();
  }

  @Override
  public void showFilteringPopUpMenu() {
    PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
    popup.getMenuInflater().inflate(R.menu.filter_comics, popup.getMenu());

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
        mPresenter.loadComics(false);
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
  public void showNoFavoriteComics() {
    mPresenter.setFilteringType(Filters.MarvelFavorite.ALL);
    mPresenter.loadComics(false);
    showMessage(mActivity.getString(R.string.error_no_fav_comics));
  }

  private void showMessage(String message) {
    Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void slideUpFab() {
    if (!fabIsUp) {
      ObjectAnimator obj = ObjectAnimator.ofFloat(scrollUtilityFab, "translationY", -200);
      obj.setDuration(1000);
      obj.setInterpolator(new FastOutSlowInInterpolator());
      obj.start();
      fabIsUp = !fabIsUp;
    }
  }

  @Override
  public void slideDownFab() {
    if (fabIsUp) {
      ObjectAnimator obj = ObjectAnimator.ofFloat(scrollUtilityFab, "translationY", 200);
      obj.setDuration(1000);
      obj.setInterpolator(new FastOutSlowInInterpolator());
      obj.start();
      fabIsUp = !fabIsUp;
    }
  }

  private void startHeartBeatAnimation() {
    ObjectAnimator scaleX = ObjectAnimator.ofFloat(scrollUtilityFab, "scaleX", 1.0f, 1.5f);
    scaleX.setDuration(500);
    scaleX.setInterpolator(new HeartBeatInterpolator());
    scaleX.setRepeatCount(ValueAnimator.INFINITE);
    scaleX.setRepeatMode(ValueAnimator.RESTART);

    ObjectAnimator scaleY = ObjectAnimator.ofFloat(scrollUtilityFab, "scaleY", 1.0f, 1.5f);
    scaleY.setDuration(500);
    scaleY.setInterpolator(new HeartBeatInterpolator());
    scaleY.setRepeatCount(ValueAnimator.INFINITE);
    scaleY.setRepeatMode(ValueAnimator.RESTART);

    loadingIndicatorSet.playTogether(scaleX, scaleY);
    loadingIndicatorSet.start();
  }

  private void stopHeartBeatAnimation() {
    loadingIndicatorSet.cancel();
    scrollUtilityFab.setScaleX(1.0f);
    scrollUtilityFab.setScaleY(1.0f);
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

      int itemCounts = linearLayoutManager.getItemCount();

      int lastVisibleItem = linearLayoutManager
        .findLastCompletelyVisibleItemPosition();

      // I'm adding 1 as the indexing start with 0 so I add 1 in case reaching last item == 19
      if (lastVisibleItem + 1 >= itemCounts) {
        mPresenter.getNextPage();
        loadNextPage = true;
      }
    }
  };

  private class ComicsAdapter extends RecyclerView.Adapter<ComicHolder> {

    private List<Comic> mComics;

    public ComicsAdapter(List<Comic> comics) {
      setList(comics);
    }

    void replaceData(List<Comic> comics) {
      setList(comics);
      notifyDataSetChanged();
    }

    void setList(List<Comic> comics) {
      mComics = checkNotNull(comics);
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public int getItemCount() {
      return mComics.size();
    }

    @Override
    public ComicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = mActivity.getLayoutInflater().inflate(R.layout.comic_row_item, parent, false);
      final ComicHolder vh = new ComicHolder(view);
      return vh;
    }

    @Override
    public void onBindViewHolder(final ComicHolder holder, final int position) {

      final Comic comic = mComics.get(position);
      String imageUrl = Utility.imageUrlLarge(comic.getThumbnailPath(),
        comic.getThumbnailExtension());

      Picasso.with(getContext())
        .load(imageUrl)
        .placeholder(R.drawable.grid_item_img_placeholder)
        .into(holder.comicThumbnail);

      holder.comicName.setText(comic.getName());

      if (comic.isFavorite()) {
        holder.iconFavorite.setBackgroundResource(R.drawable.ic_star_yellow_24dp);
      } else {
        holder.iconFavorite.setBackgroundResource(R.drawable.ic_star_grey_24dp);
      }

      holder.comicThumbnail.setContentDescription(getString(R.string.comic_thumbnail));
      holder.comicThumbnail.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          transitionToActivity(holder, comic);
        }
      });
    }
  }

  class ComicHolder extends RecyclerView.ViewHolder {
    ImageView comicThumbnail;
    TextView comicName;
    View iconFavorite;

    ComicHolder(View view) {
      super(view);
      comicThumbnail = (ImageView) view.findViewById(R.id.card_thumbnail);
      comicName = (TextView) view.findViewById(R.id.card_name);
      iconFavorite = view.findViewById(R.id.card_favorite_icon);
    }
  }
}
