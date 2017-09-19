package com.hussamelemmawi.nanodegree.marvelpeople.comics;

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
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.MarvelData;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;

import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 19/02/17.
 * <p>
 * Presenter holds the logic fot the app and handling User interactions.
 * Listens to User interactions incoming ComicFrom {@link ComicsFragment}
 * <p>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the ComicsPresenter (if it fails, it emits a compiler error).  It uses
 * {@link ApplicationModule} and {@link DataRepositoryModule} to do so.
 */
public class ComicsPresenter implements ComicsContract.Presenter<ComicsFragment>,
  DataSource.LoadDataCallback, DataSource.FetchDataCallback,
  LoaderManager.LoaderCallbacks<Cursor> {

  private DataRepository mDataRepository;

  private Context mContext;

  private ComicsContract.View mView;

  @NonNull
  private LoaderManager mLoaderManager;

  @NonNull
  private LoaderProvider mLoaderProvider;

  public final static int COMICS_LOADER = 3;

  private boolean mShowLoadingUi;

  private boolean mFirstLoad = true;

  private boolean mRequestingNextPage = false;

  @Filters.MarvelFavorite
  private int filteringType = Filters.MarvelFavorite.ALL;

  /**
   * {@code @Inject} to request a dependency (for a constructor here)
   */
  @Inject
  public ComicsPresenter(Context context, DataRepository dataRepository) {
    mContext = context;
    mDataRepository = dataRepository;
  }

  @Override
  public void bind(ComicsFragment what) {
    mView = what;
    mLoaderProvider = new LoaderProvider(mContext);
    mLoaderManager = ((ComicsFragment) mView).getActivity().getSupportLoaderManager();
  }

  @Override
  public void unbind(ComicsFragment what) {
    mView = null;
  }

  @Override
  public void start() {
    loadComics(false);
  }

  @Override
  public void loadComics(boolean forceUpdate) {
    // If it is the first time force loading,
    // And show the loading indicator.
    loadComices(forceUpdate || mFirstLoad, mFirstLoad);
    mFirstLoad = false;
  }

  /**
   * @param forceUpdate   Pass in true to refresh the data in the {@link DataSource}
   * @param showLoadingUI Pass in true to display a loading icon in the UI
   */
  private void loadComices(boolean forceUpdate, boolean showLoadingUI) {

    if (showLoadingUI) {
      mShowLoadingUi = true;
      mView.setLoadingIndicator(true);
    }
    if (forceUpdate) {
      // Force update force to fetch new data comicFrom server.
      // Check connectivity first.
      if (Utility.isConnected())
        mDataRepository.refreshComics();
      else {
        mView.showLoadingComicsError();
      }
    }
    mDataRepository.getComics(this);
  }

  @Override
  public void getNextPage() {
    if (!mRequestingNextPage) {
      mRequestingNextPage = true;
      mDataRepository.requestNextComicPage(this);
      mView.setFetchingNewPageIndicator(true);
    }
  }

  @Override
  public void setFilteringType(@Filters.MarvelFavorite int filteringType) {
    // Change filtering.
    this.filteringType = filteringType;
  }

  @Override
  public boolean getFilteringIsFavorite() {
    return filteringType == Filters.MarvelFavorite.FAVORITE;
  }

  @Override
  public <D extends MarvelData> void onSuccess(List<D> data) {
    if (mLoaderManager.getLoader(COMICS_LOADER) == null) {
      mLoaderManager.initLoader(COMICS_LOADER, null, this);
    } else {
      mLoaderManager.restartLoader(COMICS_LOADER, null, this);
    }
    mView.setLoadingIndicator(false);
    mView.setFetchingNewPageIndicator(false);
    mRequestingNextPage = false;
  }

  @Override
  public void onFailure() {
    // TODO: change to show a network failure
    mView.showLoadingComicsError();
    mView.setFetchingNewPageIndicator(false);
    mRequestingNextPage = false;
  }

  @Override
  public void onDataLoaded(Cursor data) {
    mDataRepository.refreshComicsCache(data);

    if (mShowLoadingUi) {
      mView.setLoadingIndicator(false);
      mShowLoadingUi = !mShowLoadingUi;
    }
    mView.showComics(Comic.getComicsFrom(data));
  }

  @Override
  public void onDataEmpty() {
    if (filteringType == Filters.MarvelFavorite.FAVORITE) {
      filteringType = Filters.MarvelFavorite.ALL;
      mView.showNoFavoriteComics();
    }
  }

  @Override
  public void onDataNotAvailable() {
    mView.showLoadingComicsError();
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return mLoaderProvider.createComicsLoader(filteringType);
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