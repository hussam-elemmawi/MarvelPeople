package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hussamelemmawi.nanodegree.marvelpeople.Filters;
import com.hussamelemmawi.nanodegree.marvelpeople.heroes.HeroesPresenter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 19/11/16.
 * <p>
 * This class providers 2 loaders type.
 * One for loading all heroes heroFrom database for the {@link HeroesPresenter}.
 * The second for loading only one hero details for {@link com.hussamelemmawi.nanodegree.marvelpeople.herodetails.HeroDetailsPresenter}
 */
public class LoaderProvider {

  @NonNull
  private final Context mContext;

  public LoaderProvider(@NonNull Context context) {
    mContext = checkNotNull(context, "context cannot be null");
  }

  public Loader<Cursor> createHeroesLoader(@Filters.MarvelFavorite int filter) {
    String where = "";
    String[] whereArgs = {};

    if (filter == Filters.MarvelFavorite.FAVORITE) {
      where = PersistenceContract.HeroEntry.COLUMN_FAVORITE + " = ?";
      whereArgs = new String[]{Integer.toString(filter)};
    }

    return new CursorLoader(mContext,
      PersistenceContract.HeroEntry.buildHeroesUri(),
      PersistenceContract.HeroEntry.HERO_COLUMNS,
      where,
      whereArgs,
      null
    );
  }

  public Loader<Cursor> createComicsLoader(@Filters.MarvelFavorite int filter) {
    String where = "";
    String[] whereArgs = {};

    if (filter == Filters.MarvelFavorite.FAVORITE) {
      where = PersistenceContract.ComicEntry.COLUMN_FAVORITE + " = ?";
      whereArgs = new String[]{Integer.toString(filter)};
    }

    return new CursorLoader(mContext,
      PersistenceContract.ComicEntry.buildComicsUri(),
      PersistenceContract.ComicEntry.COMIC_COLUMNS,
      where,
      whereArgs,
      null
    );
  }

  public Loader<Cursor> createHeroLoader(int heroId) {
    return new CursorLoader(mContext,
      PersistenceContract.HeroEntry.buildHeroesUriWith(heroId),
      PersistenceContract.HeroEntry.HERO_COLUMNS,
      PersistenceContract.HeroEntry.COLUMN_HERO_ID + " = ?",
      new String[]{Integer.toString(heroId)},
      null
    );
  }

  public Loader<Cursor> createComicLoader(int comicId) {
    return new CursorLoader(mContext,
      PersistenceContract.ComicEntry.buildComicsUriWith(comicId),
      PersistenceContract.ComicEntry.COMIC_COLUMNS,
      PersistenceContract.ComicEntry.COLUMN_COMIC_ID + " = ?",
      new String[]{Integer.toString(comicId)},
      null
    );
  }
}
