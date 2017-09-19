package com.hussamelemmawi.nanodegree.marvelpeople.heroes;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.hussamelemmawi.nanodegree.marvelpeople.Filters;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataRepository;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataSource;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.LoaderProvider;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.MarvelData;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * Presenter holds the logic fot the app and handling User interactions.
 * Listens to User interactions incoming heroFrom {@link HeroesFragment}
 * <p>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the HeroesPresenter (if it fails, it emits a compiler error).  It uses
 * {@link ApplicationModule} and {@link DataRepositoryModule} to do so.
 */
public class HeroesPresenter implements HeroesContract.Presenter<HeroesFragment>,
  DataSource.LoadDataCallback, DataSource.FetchDataCallback,
  LoaderManager.LoaderCallbacks<Cursor> {

  public static final String ACTION_DATA_UPDATE =
    "com.hussamelemmawi.nanodegree.marvelpeople.heroes.ACTION_DATA_UPDATE";

  private final DataRepository mDataRepository;

  private Context mContext;

  private HeroesContract.View mView;

  @NonNull
  private LoaderManager mLoaderManager;

  @NonNull
  private LoaderProvider mLoaderProvider;

  public final static int HEROES_LOADER = 1;

  private boolean mShowLoadingUi;

  private boolean mFirstLoad = true;

  private boolean mRequestingNextPage = false;

  @Filters.MarvelFavorite
  private int filteringType = Filters.MarvelFavorite.ALL;

  /**
   * {@code @Inject} to request a dependency (for a constructor here)
   */
  @Inject
  public HeroesPresenter(Context context, DataRepository dataRepository) {
    mContext = context;
    mDataRepository = checkNotNull(dataRepository);
  }

  @Override
  public void bind(HeroesFragment what) {
    mView = what;
    mLoaderProvider = new LoaderProvider(mContext);
    mLoaderManager = ((HeroesFragment) mView).getActivity().getSupportLoaderManager();
  }

  @Override
  public void unbind(HeroesFragment what) {
    mView = null;
  }

  @Override
  public void start() {
    loadHeroes(false);
    ((HeroesFragment) mView).getActivity().getSupportFragmentManager();
  }

  @Override
  public void loadHeroes(boolean forceUpdate) {
    // If it is the first time force loading,
    // And show the loading indicator.
    loadHeroes(forceUpdate || mFirstLoad, mFirstLoad);
    mFirstLoad = false;
  }

  /**
   * @param forceUpdate   Pass in true to refresh the data in the {@link DataSource}
   * @param showLoadingUI Pass in true to display a loading icon in the UI
   */
  private void loadHeroes(boolean forceUpdate, boolean showLoadingUI) {

    if (showLoadingUI) {
      mShowLoadingUi = true;
      mView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      // Force update force to fetch new data heroFrom server.
      // Check connectivity first.
      if (Utility.isConnected())
        mDataRepository.refreshHeroes();
      else {
        mView.showLoadingHeroesError();
      }
    }
    mDataRepository.getHeroes(this);
  }

  @Override
  public void getNextPage() {
    if (!mRequestingNextPage) {
      mRequestingNextPage = true;
      mDataRepository.requestNextHeroPage(this);
      mView.setFetchingNewPageIndicator(true);
    }
  }

  @Override
  public void setFilteringType(int filteringType) {
    // Change filtering.
    this.filteringType = filteringType;
  }

  @Override
  public boolean getFilteringIsFavorite() {
    return filteringType == Filters.MarvelFavorite.FAVORITE;
  }

  @Override
  public <D extends MarvelData> void onSuccess(List<D> data) {
    if (mLoaderManager.getLoader(HEROES_LOADER) == null) {
      mLoaderManager.initLoader(HEROES_LOADER, null, this);
    } else {
      mLoaderManager.restartLoader(HEROES_LOADER, null, this);
    }
    mView.setLoadingIndicator(false);
    mView.setFetchingNewPageIndicator(false);
    mRequestingNextPage = false;
  }

  @Override
  public void onFailure() {
    // TODO: change to show network error
    mView.showLoadingHeroesError();
    mView.setFetchingNewPageIndicator(false);
    mRequestingNextPage = false;
  }

  @Override
  public void onDataLoaded(Cursor data) {
    mDataRepository.refreshHeroesCache(data);

    if (mShowLoadingUi) {
      mView.setLoadingIndicator(false);
      mShowLoadingUi = !mShowLoadingUi;
    }
    mView.showHeroes(Hero.getHeroesFrom(data));
  }

  @Override
  public void onDataEmpty() {
    if (filteringType == Filters.MarvelFavorite.FAVORITE) {
      filteringType = Filters.MarvelFavorite.ALL;
      mView.showNoFavoriteHeroes();
    }
  }

  @Override
  public void onDataNotAvailable() {
    mView.showLoadingHeroesError();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return mLoaderProvider.createHeroesLoader(filteringType);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (data != null) {
      if (data.moveToLast()) {
        onDataLoaded(data);
      } else {
        onDataEmpty();
      }
    } else {
      onFailure();
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
  }
}