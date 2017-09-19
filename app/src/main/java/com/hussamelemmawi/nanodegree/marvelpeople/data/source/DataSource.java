package com.hussamelemmawi.nanodegree.marvelpeople.data.source;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.MarvelData;

import java.util.List;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * This is the contract for {@link DataRepository}.
 */
public interface DataSource {

  void getHeroes(@NonNull FetchDataCallback callback);

  void requestNextHeroPage(FetchDataCallback callback);

  void markAsFavoriteHero(int heroId);

  void markAsUnFavoriteHero(int heroId);

  void getComics(@NonNull FetchDataCallback callback);

  void requestNextComicPage(FetchDataCallback callback);

  void markAsFavoriteComic(int comicId);

  void markAsUnFavoriteComic(int comicId);

  /**
   * Local contract
   */
  interface Local {
    void saveHeroIntoDatabase(@NonNull Hero hero);

    void markAsFavoriteHero(int heroId);

    void markAsUnFavoriteHero(int heroId);

    int getLastHeroId();

    void deleteAllHeroes();

    void saveComicIntoDatabase(@NonNull Comic comic);

    void markAsFavoriteComic(int comicId);

    void markAsUnFavoriteComic(int comicId);

    int getLastComicId();

    void deleteAllComics();
  }

  /**
   * Remote contract
   */
  interface Remote {
    void getHeroes(@NonNull FetchDataCallback callback, int fromHeroId);

    void getComics(@NonNull FetchDataCallback callback, int fromComicId);
  }

  interface FetchDataCallback {
    <D extends MarvelData> void onSuccess(List<D> data);

    void onFailure();
  }

  // An interface for handling on fetching data results.
  interface LoadDataCallback {
    void onDataLoaded(Cursor data);

    void onDataEmpty();

    void onDataNotAvailable();
  }
}
