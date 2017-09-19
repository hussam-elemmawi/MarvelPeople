package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.hussamelemmawi.nanodegree.marvelpeople.BuildConfig;

/**
 * Created by hussamelemmawi on 19/11/16.
 * <p>
 * This is the contract class for using with the {@link DataProvider}.
 */
public final class PersistenceContract {

  public static final String CONTENT_AUTHORITY = BuildConfig.APPLICATION_ID;
  private static final String SEPARATOR = "/";
  public static final String CONTENT_HERO_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + SEPARATOR + HeroEntry.TABLE_NAME;
  public static final String CONTENT_HERO_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + SEPARATOR + HeroEntry.TABLE_NAME;
  public static final String CONTENT_COMIC_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + SEPARATOR + ComicEntry.TABLE_NAME;
  public static final String CONTENT_COMIC_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + SEPARATOR + ComicEntry.TABLE_NAME;
  public static final String VND_ANDROID_CURSOR_ITEM_VND = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".";
  private static final String CONTENT_SCHEME = "content://";
  public static final Uri BASE_CONTENT_URI = Uri.parse(CONTENT_SCHEME + CONTENT_AUTHORITY);
  private static final String VND_ANDROID_CURSOR_DIR_VND = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".";

  private PersistenceContract() {
  }

  public static abstract class HeroEntry implements BaseColumns {
    public static final String TABLE_NAME = "hero";

    public static final String COLUMN_HERO_ID = "hid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_THUMBNAIL_PATH = "thumbnail_path";
    public static final String COLUMN_THUMBNAIL_EXTENSION = "thumbnail_ex";
    public static final String COLUMN_DETAIL_URL = "detail_url";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final Uri CONTENT_HERO_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static String[] HERO_COLUMNS = new String[]{
      HeroEntry._ID,
      HeroEntry.COLUMN_HERO_ID,
      HeroEntry.COLUMN_NAME,
      HeroEntry.COLUMN_DESCRIPTION,
      HeroEntry.COLUMN_THUMBNAIL_PATH,
      HeroEntry.COLUMN_THUMBNAIL_EXTENSION,
      HeroEntry.COLUMN_DETAIL_URL,
      HeroEntry.COLUMN_FAVORITE};

    public static Uri buildHeroesUriWith(long id) {
      return ContentUris.withAppendedId(CONTENT_HERO_URI, id);
    }

    public static Uri buildHeroesUriWith(String id) {
      return CONTENT_HERO_URI.buildUpon().appendPath(id).build();
    }

    public static Uri buildHeroesUri() {
      return CONTENT_HERO_URI.buildUpon().build();
    }
  }

  public static abstract class ComicEntry implements BaseColumns {
    public static final String TABLE_NAME = "comic";

    public static final String COLUMN_COMIC_ID = "cid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_THUMBNAIL_PATH = "thumbnail_path";
    public static final String COLUMN_THUMBNAIL_EXTENSION = "thumbnail_ex";
    public static final String COLUMN_DETAIL_URL = "detail_url";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final Uri CONTENT_COMIC_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

    public static String[] COMIC_COLUMNS = new String[]{
      ComicEntry._ID,
      ComicEntry.COLUMN_COMIC_ID,
      ComicEntry.COLUMN_NAME,
      ComicEntry.COLUMN_DESCRIPTION,
      ComicEntry.COLUMN_THUMBNAIL_PATH,
      ComicEntry.COLUMN_THUMBNAIL_EXTENSION,
      ComicEntry.COLUMN_DETAIL_URL,
      ComicEntry.COLUMN_PRICE,
      ComicEntry.COLUMN_FAVORITE};

    public static Uri buildComicsUriWith(long id) {
      return ContentUris.withAppendedId(CONTENT_COMIC_URI, id);
    }

    public static Uri buildComicsUriWith(String id) {
      return CONTENT_COMIC_URI.buildUpon().appendPath(id).build();
    }

    public static Uri buildComicsUri() {
      return CONTENT_COMIC_URI.buildUpon().build();
    }
  }
}
