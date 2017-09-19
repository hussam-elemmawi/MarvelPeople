package com.hussamelemmawi.nanodegree.marvelpeople.comicsdetails;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hussamelemmawi.nanodegree.marvelpeople.MarvelPeopleApp;
import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.util.SelectableFloatingActionButton;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by hussamelemmawi on 17/02/17.
 * <p>
 * Displays a detailed view fot the chosen comic.
 * Pass User actions to {@link ComicDetailsPresenter} to handle it.
 */

public class ComicDetailsFragment extends Fragment implements ComicDetailsContract.View,
  AppBarLayout.OnOffsetChangedListener {

  public static final String TAG = ComicDetailsFragment.class.getSimpleName();

  /**
   * {@code @Inject} to request a dependency (for a field here)
   */
  @Inject
  ComicDetailsPresenter mPresenter;

  private AppCompatActivity mActivity;

  private View mRoot;

  @BindView(R.id.comic_image)
  ImageView comicImage;

  @BindView(R.id.name_and_desc_card_text)
  TextView nameAndDescText;

  @BindView(R.id.comic_price)
  TextView comicPrice;

  @BindView(R.id.favorite_fab)
  SelectableFloatingActionButton fab;

  @BindView(R.id.button_action_menu)
  ImageButton popUpMenu;

  @BindView(R.id.button_action_share)
  ImageButton shareButton;

  @BindView(R.id.image_progress_circle)
  MaterialProgressBar imageLoadingIndicator;

  @BindView(R.id.more_details_card)
  TextView moreDetailsView;

  private int mMaxScrollSize;

  private int comicId;

  private boolean mIsImageHidden;

  private static final int PERCENTAGE_TO_SHOW_IMAGE = 70;

  private Bitmap mComicBitmap;

  public static ComicDetailsFragment newInstance() {
    return new ComicDetailsFragment();
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

    if (mActivity.getIntent() != null) {
      if (mActivity.getIntent().getExtras() != null)
        if (mActivity.getIntent().getExtras().containsKey("comicId")) {
          comicId = mActivity.getIntent().getExtras().getInt("comicId");
        }
    }
    mPresenter.initializePresenter(comicId);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    mRoot = inflater.inflate(R.layout.fragment_comic_details, container, false);

    ButterKnife.bind(this, mRoot);
    AppBarLayout appBarLayout = (AppBarLayout) mRoot.findViewById(R.id.app_bar_layout);
    appBarLayout.addOnOffsetChangedListener(this);
    mRoot.findViewById(R.id.button_action_nav_up).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mActivity.onBackPressed();
      }
    });

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!fab.isFabSelected()) {
          mPresenter.saveToFavorite();
        } else {
          mPresenter.removeFromFavorite();
        }
        fab.setFabSelected(!fab.isFabSelected());
      }
    });

    popUpMenu.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showFilteringPopUpMenu();
      }
    });

    shareButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mPresenter.shareComic(mActivity);
      }
    });

    moreDetailsView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mPresenter.openComicDetailsFromWeb(getContext());
      }
    });

    return mRoot;
  }

  @Override
  public void onStart() {
    super.onStart();
    mPresenter.bind(this);
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
  public void showComic(Comic comic) {
    String imageUrl = Utility.imageUrlLarge(comic.getThumbnailPath(),
      comic.getThumbnailExtension());

    Target target = new Target() {
      @Override
      public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imageLoadingIndicator.setVisibility(View.GONE);
        comicImage.setImageBitmap(bitmap);
        mComicBitmap = bitmap;

        CollapsingToolbarLayout collapsingToolbarLayout =
          (CollapsingToolbarLayout) mRoot.findViewById(R.id.collapsing_toolbar);
        Drawable drawable = new BitmapDrawable(bitmap);
        collapsingToolbarLayout.setContentScrim(drawable);
      }

      @Override
      public void onBitmapFailed(Drawable errorDrawable) {
        mComicBitmap = null;
      }

      @Override
      public void onPrepareLoad(Drawable placeHolderDrawable) {
        imageLoadingIndicator.setVisibility(View.VISIBLE);
      }
    };

    Picasso.with(getContext()).load(imageUrl).into(target);

    nameAndDescText.setText(comic.getName() + ": " + comic.getDescription());
    comicPrice.setText(String.format(Locale.US, "%.2f $", comic.getPrice()));

    fab.setFabSelected(comic.isFavorite());

  }

  @Override
  public void showFilteringPopUpMenu() {
    PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.button_action_menu));
    popup.getMenuInflater().inflate(R.menu.marvel_details_menu, popup.getMenu());

    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.set_as_wallpaper_option) {
          mPresenter.setComicAsWallpaper();
        }
        return true;
      }
    });

    popup.show();
  }

  @Override
  public void setAsWallpaperDone(String comicName) {
    if (mComicBitmap != null) {
      WallpaperManager wallpaperManager = WallpaperManager.getInstance(mActivity);
      try {
        wallpaperManager.setBitmap(mComicBitmap);
      } catch (IOException e) {
        e.printStackTrace();
      }
      showMessage("Setting " + comicName + " as Wallpaper done!");
    } else {
      showMessage(mActivity.getString(R.string.error_failed_set_as_wallpaper));
    }
  }

  @Override
  public void showNoComicDataAvailable() {
    showMessage(mActivity.getString(R.string.error_no_available_comic_details));
  }

  private void showMessage(String s) {
    Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
  }

  @Override
  public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
    if (mMaxScrollSize == 0)
      mMaxScrollSize = appBarLayout.getTotalScrollRange();

    int currentScrollPercentage = (Math.abs(verticalOffset)) * 100
      / mMaxScrollSize;

    if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
      if (!mIsImageHidden) {
        mIsImageHidden = true;
        // Scale to 0, to hide it
        ViewCompat.animate(fab).scaleY(0).scaleX(0).start();
      }
    }

    if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
      if (mIsImageHidden) {
        mIsImageHidden = false;
        // Scale to 1, show the fab again
        ViewCompat.animate(fab).scaleY(1).scaleX(1).start();
      }
    }
  }
}
