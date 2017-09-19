package com.hussamelemmawi.nanodegree.marvelpeople.data.source;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.MarvelData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * This is the only interface to communicate with any presenter which is outside the data layer.
 */

public class DataRepository implements DataSource {

  // Make a reference for the two available data source, local and remote.
  private final DataSource.Remote mRemoteDataSource;
  private final DataSource.Local mLocalDataSource;

  private ArrayList<Hero> mCachedHeroes;
  private boolean mHeroesCacheIsDirty = false;

  private ArrayList<Comic> mCachedComics;
  private boolean mComicsCacheIsDirty = false;

  /**
   * {@code @Inject} to request a dependency (for a constructor here)
   */
  @Inject
  public DataRepository(DataSource.Remote remoteDataSource, DataSource.Local localDataSource) {

    mRemoteDataSource = checkNotNull(remoteDataSource);
    mLocalDataSource = checkNotNull(localDataSource);
  }

  @Override
  public void getHeroes(@NonNull final FetchDataCallback callback) {
    // If already saved in cache and cache is fresh.
    // Return data heroFrom cache
    if (mCachedHeroes != null && !mHeroesCacheIsDirty) {
      callback.onSuccess(mCachedHeroes);
      Log.d("debug", "Loading hero from cache");
      return;
    }
    // Else get data heroFrom remote and refresh the cache.
    if (mHeroesCacheIsDirty) {
      getHeroesFromRemoteDataSource(callback, 0);
    } else {
      // Get data heroFrom local data storage
      // Loader syncs with Presenter and updates cache
      callback.onSuccess(null);
    }
  }

  @Override
  public void requestNextHeroPage(FetchDataCallback callback) {
    int lastHeroId = mLocalDataSource.getLastHeroId();
    getHeroesFromRemoteDataSource(callback, lastHeroId);
  }

  public void refreshHeroes() {
    mHeroesCacheIsDirty = true;
  }

  @Override
  public void markAsFavoriteHero(int heroId) {
    mLocalDataSource.markAsFavoriteHero(heroId);
  }

  @Override
  public void markAsUnFavoriteHero(int heroId) {
    mLocalDataSource.markAsUnFavoriteHero(heroId);
  }

  @Override
  public void getComics(@NonNull FetchDataCallback callback) {
    // If already saved in cache and cache is fresh.
    // Return data heroFrom cache
    if (mCachedComics != null && !mComicsCacheIsDirty) {
      callback.onSuccess(mCachedComics);
      Log.d("debug", "Loading comic from cache");
      return;
    }
    // Else get data heroFrom remote and refresh the cache.
    if (mComicsCacheIsDirty) {
      getComicsFromRemoteDataSource(callback, 0);
    } else {
      // Get data heroFrom local data storage
      // Loader syncs with Presenter and updates cache
      callback.onSuccess(null);
    }
  }

  @Override
  public void requestNextComicPage(FetchDataCallback callback) {
    int lastComicId = mLocalDataSource.getLastComicId();
    getComicsFromRemoteDataSource(callback, lastComicId);
  }

  public void refreshComics() {
    mComicsCacheIsDirty = true;
  }

  @Override
  public void markAsFavoriteComic(int comicId) {
    mLocalDataSource.markAsFavoriteComic(comicId);
  }

  @Override
  public void markAsUnFavoriteComic(int comicId) {
    mLocalDataSource.markAsUnFavoriteComic(comicId);
  }

  private void getHeroesFromRemoteDataSource(@NonNull final FetchDataCallback callback, final int lastHeroId) {
    mRemoteDataSource.getHeroes(new FetchDataCallback() {
      @Override
      public <D extends MarvelData> void onSuccess(List<D> heroes) {
        if (lastHeroId == 0) {
          // So, this is a call from the beginning
          // Delete all old heroes and save the new ones.
          mLocalDataSource.deleteAllHeroes();
        }
        refreshLocalDataSource(heroes);
        // Passing null as loaders will syncs when its is available.
        // Loaders will also refresh the Cache.
        callback.onSuccess(null);
      }

      @Override
      public void onFailure() {
        callback.onFailure();
      }
    }, lastHeroId);
  }

  private void getComicsFromRemoteDataSource(@NonNull final FetchDataCallback callback, final int lastComicId) {
    mRemoteDataSource.getComics(new FetchDataCallback() {
      @Override
      public <D extends MarvelData> void onSuccess(List<D> comics) {
        if (lastComicId == 0) {
          // So, this is a call from the beginning
          // Delete all old heroes and save the new ones.
          mLocalDataSource.deleteAllComics();
        }
        refreshLocalDataSource(comics);
        // Passing null as loaders will syncs when its is available.
        // Loaders will also refresh the Cache.
        callback.onSuccess(null);
      }

      @Override
      public void onFailure() {
        callback.onFailure();
      }
    }, lastComicId);
  }

  private <D extends MarvelData> void refreshLocalDataSource(List<D> data) {
    if (data.get(0) instanceof Hero) {
      for (MarvelData hero : data) {
        mLocalDataSource.saveHeroIntoDatabase((Hero) hero);
      }
    } else if (data.get(0) instanceof Comic) {
      for (MarvelData comic : data) {
        mLocalDataSource.saveComicIntoDatabase((Comic) comic);
      }
    }
  }

  public void refreshHeroesCache(Cursor data) {
    if (mCachedHeroes == null) {
      mCachedHeroes = new ArrayList<>();
    }
    mCachedHeroes.clear();
    List<Hero> heroes = Hero.getHeroesFrom(data);
    for (Hero hero : heroes) {
      mCachedHeroes.add(hero);
    }
    mHeroesCacheIsDirty = false;
  }

  public void refreshComicsCache(Cursor data) {
    if (mCachedComics == null) {
      mCachedComics = new ArrayList<>();
    }
    mCachedComics.clear();
    List<Comic> comics = Comic.getComicsFrom(data);
    for (Comic comic : comics) {
      mCachedComics.add(comic);
    }
    mComicsCacheIsDirty = false;
  }
}