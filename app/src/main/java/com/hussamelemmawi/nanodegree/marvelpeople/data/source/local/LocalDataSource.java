package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataSource;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.PersistenceContract.ComicEntry;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.PersistenceContract.HeroEntry;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 18/11/16.
 */

public class LocalDataSource implements DataSource.Local {

  private ContentResolver mContentResolver;

  public LocalDataSource(@NonNull Context context) {
    checkNotNull(context);
    mContentResolver = context.getContentResolver();
  }

  @Override
  public void saveHeroIntoDatabase(@NonNull Hero hero) {
    checkNotNull(hero);
    ContentValues values = DataContentValues.heroFrom(hero);
    mContentResolver.insert(HeroEntry.buildHeroesUri(), values);
  }

  @Override
  public void markAsFavoriteHero(int heroId) {
    ContentValues values = new ContentValues();
    values.put(HeroEntry.COLUMN_FAVORITE, 1);
    mContentResolver.update(HeroEntry.buildHeroesUriWith(heroId), values, null, null);
  }

  @Override
  public void markAsUnFavoriteHero(int heroId) {
    ContentValues values = new ContentValues();
    values.put(HeroEntry.COLUMN_FAVORITE, 0);
    mContentResolver.update(HeroEntry.buildHeroesUriWith(heroId), values, null, null);
  }

  @Override
  public int getLastHeroId() {
    Cursor cursor =
      mContentResolver.query(HeroEntry.buildHeroesUri(), null, null, null, null);
    int lastHeroId = 0;
    if (cursor != null) {
      lastHeroId = cursor.getCount();
      cursor.close();
    }
    return lastHeroId;
  }

  @Override
  public void deleteAllHeroes() {
    mContentResolver.delete(
      HeroEntry.buildHeroesUri(),
      HeroEntry.COLUMN_FAVORITE + " = ? ",
      new String[]{"0"});
  }

  @Override
  public void saveComicIntoDatabase(@NonNull Comic comic) {
    checkNotNull(comic);
    ContentValues values = DataContentValues.comicFrom(comic);
    mContentResolver.insert(ComicEntry.buildComicsUri(), values);
  }

  @Override
  public void markAsFavoriteComic(int comicId) {
    ContentValues values = new ContentValues();
    values.put(ComicEntry.COLUMN_FAVORITE, 1);
    mContentResolver.update(ComicEntry.buildComicsUriWith(comicId), values, null, null);
  }

  @Override
  public void markAsUnFavoriteComic(int comicId) {
    ContentValues values = new ContentValues();
    values.put(ComicEntry.COLUMN_FAVORITE, 0);
    mContentResolver.update(ComicEntry.buildComicsUriWith(comicId), values, null, null);
  }

  @Override
  public int getLastComicId() {
    Cursor cursor =
      mContentResolver.query(ComicEntry.buildComicsUri(), null, null, null, null);
    int lastComicId = 0;
    if (cursor != null) {
      lastComicId = cursor.getCount();
      cursor.close();
    }
    return lastComicId;
  }

  @Override
  public void deleteAllComics() {
    mContentResolver.delete(
      ComicEntry.buildComicsUri(),
      ComicEntry.COLUMN_FAVORITE + " = ? ",
      new String[]{"0"});
  }
}
