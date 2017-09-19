package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hussamelemmawi on 19/11/16.
 */

public class DbHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;

  public static final String DATABASE_NAME = "marvelpeople.db";

  private static final String TEXT_TYPE = " TEXT";

  private static final String INTEGER_TYPE = " INTEGER";

  private static final String COMMA_SEP = ",";

  private static final String SQL_CREATE_HEROES_ENTRIES =
    "CREATE TABLE " + PersistenceContract.HeroEntry.TABLE_NAME + " (" +
      PersistenceContract.HeroEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
      PersistenceContract.HeroEntry.COLUMN_HERO_ID + INTEGER_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_THUMBNAIL_PATH + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_THUMBNAIL_EXTENSION + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_DETAIL_URL + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.HeroEntry.COLUMN_FAVORITE + INTEGER_TYPE + " DEFAULT 0 " +
      " )";

  private static final String SQL_CREATE_COMICS_ENTRIES =
    "CREATE TABLE " + PersistenceContract.ComicEntry.TABLE_NAME + " (" +
      PersistenceContract.ComicEntry._ID + INTEGER_TYPE + " PRIMARY KEY," +
      PersistenceContract.ComicEntry.COLUMN_COMIC_ID + INTEGER_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_THUMBNAIL_PATH + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_THUMBNAIL_EXTENSION + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_DETAIL_URL + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_PRICE + TEXT_TYPE + COMMA_SEP +
      PersistenceContract.ComicEntry.COLUMN_FAVORITE + INTEGER_TYPE + " DEFAULT 0 " +
      " )";

  public DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_HEROES_ENTRIES);
    db.execSQL(SQL_CREATE_COMICS_ENTRIES);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + PersistenceContract.HeroEntry.TABLE_NAME);
    db.execSQL("DROP TABLE IF EXISTS " + PersistenceContract.ComicEntry.TABLE_NAME);
    onCreate(db);
  }
}
