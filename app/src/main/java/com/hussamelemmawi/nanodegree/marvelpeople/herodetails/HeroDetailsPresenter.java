package com.hussamelemmawi.nanodegree.marvelpeople.herodetails;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataRepository;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.LoaderProvider;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 22/11/16.
 * <p>
 * Presenter holds the logic fot the app and handling User interactions.
 * Listens to User interactions incoming heroFrom {@link HeroDetailsFragment}
 * <p>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the ComicsPresenter (if it fails, it emits a compiler error).  It uses
 * {@link ApplicationModule} and {@link DataRepositoryModule} to do so.
 */
public class HeroDetailsPresenter implements HeroDetailsContract.Presenter<HeroDetailsFragment>,
  LoaderManager.LoaderCallbacks<Cursor> {

  public final static int HERO_DETAILS_LOADER = 2;

  private HeroDetailsContract.View mView;

  private final DataRepository mDataRepository;

  private Context mContext;

  private int mHeroId;

  private Hero mHero;

  @NonNull
  private LoaderManager mLoaderManager;

  @NonNull
  private LoaderProvider mLoaderProvider;


  /**
   * {@code @Inject} to request a dependency (for a constructor here)
   */
  @Inject
  public HeroDetailsPresenter(Context context, DataRepository dataRepository) {
    mContext = context;
    mDataRepository = checkNotNull(dataRepository);
  }

  @Override
  public void bind(HeroDetailsFragment what) {
    mView = what;
    mLoaderProvider = new LoaderProvider(mContext);
    mLoaderManager = ((HeroDetailsFragment) mView).getActivity().getSupportLoaderManager();
  }

  @Override
  public void unbind(HeroDetailsFragment what) {
    mView = null;
  }

  @Override
  public void start() {
    if (mLoaderManager.getLoader(HERO_DETAILS_LOADER) == null) {
      mLoaderManager.initLoader(HERO_DETAILS_LOADER, null, this);
    }
    mLoaderManager.restartLoader(HERO_DETAILS_LOADER, null, this);
  }

  @Override
  public void initializePresenter(@NonNull int heroId) {
    mHeroId = heroId;
  }

  @Override
  public void shareHero() {
    String extraText = "Hero Name: " + mHero.getName() + "\n" + mHero.getDescription() +
      " \n\n " + mContext.getString(R.string.marvel_hash_tag);

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
    shareIntent.setType("text/plain");
    ((HeroDetailsFragment) mView).getActivity().startActivity(Intent.createChooser(shareIntent,
      ((HeroDetailsFragment) mView).getActivity().getString(R.string.share_via)));
  }

  @Override
  public void saveToFavorite() {
    mDataRepository.markAsFavoriteHero(mHeroId);
  }

  @Override
  public void removeFromFavorite() {
    mDataRepository.markAsUnFavoriteHero(mHeroId);
  }

  @Override
  public void setHeroAsWallpaper() {
    mView.setAsWallpaperDone(mHero.getName());
  }

  @Override
  public void openHeroDetailsFromWeb(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(mHero.getDetailsUrl()));
    context.startActivity(intent);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return mLoaderProvider.createHeroLoader(mHeroId);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (data != null) {
      if (data.moveToNext()) {
        // Bind Data into views
        mHero = Hero.getHeroFrom(data);
        mView.showHero(mHero);

      } else {
        mView.showNoHeroDataAvailable();
      }
    } else {
      mView.showNoHeroDataAvailable();
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }
}
