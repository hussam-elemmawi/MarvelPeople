package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.ContentValues;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;

/**
 * Created by hussamelemmawi on 19/11/16.
 */

public class DataContentValues {

  public static ContentValues heroFrom(Hero hero) {
    ContentValues values = new ContentValues();
    values.put(PersistenceContract.HeroEntry.COLUMN_HERO_ID, hero.getId());
    values.put(PersistenceContract.HeroEntry.COLUMN_NAME, hero.getName());
    values.put(PersistenceContract.HeroEntry.COLUMN_DESCRIPTION, hero.getDescription());
    values.put(PersistenceContract.HeroEntry.COLUMN_THUMBNAIL_PATH, hero.getThumbnailPath());
    values.put(PersistenceContract.HeroEntry.COLUMN_THUMBNAIL_EXTENSION, hero.getThumbnailExtension());
    values.put(PersistenceContract.HeroEntry.COLUMN_DETAIL_URL, hero.getDetailsUrl());
    return values;
  }

  public static ContentValues comicFrom(Comic comic) {
    ContentValues values = new ContentValues();
    values.put(PersistenceContract.ComicEntry.COLUMN_COMIC_ID, comic.getId());
    values.put(PersistenceContract.ComicEntry.COLUMN_NAME, comic.getName());
    values.put(PersistenceContract.ComicEntry.COLUMN_DESCRIPTION, comic.getDescription());
    values.put(PersistenceContract.ComicEntry.COLUMN_THUMBNAIL_PATH, comic.getThumbnailPath());
    values.put(PersistenceContract.ComicEntry.COLUMN_THUMBNAIL_EXTENSION, comic.getThumbnailExtension());
    values.put(PersistenceContract.ComicEntry.COLUMN_DETAIL_URL, comic.getDetailsUrl());
    values.put(PersistenceContract.ComicEntry.COLUMN_PRICE, comic.getPrice());
    return values;
  }
}
