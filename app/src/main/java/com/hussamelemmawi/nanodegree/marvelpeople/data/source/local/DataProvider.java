package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by hussamelemmawi on 19/11/16.
 */

public class DataProvider extends ContentProvider {

  private static final int HERO = 10;
  private static final int HERO_ITEM = 11;
  private static final int COMIC = 20;
  private static final int COMIC_ITEM = 21;
  private static final UriMatcher sUriMatcher = buildUriMatcher();
  private DbHelper mDbHelper;

  private static UriMatcher buildUriMatcher() {
    final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    final String authority = PersistenceContract.CONTENT_AUTHORITY;

    matcher.addURI(authority, PersistenceContract.HeroEntry.TABLE_NAME, HERO);
    matcher.addURI(authority, PersistenceContract.HeroEntry.TABLE_NAME + "/*", HERO_ITEM);
    matcher.addURI(authority, PersistenceContract.ComicEntry.TABLE_NAME, COMIC);
    matcher.addURI(authority, PersistenceContract.ComicEntry.TABLE_NAME + "/*", COMIC_ITEM);

    return matcher;
  }


  @Nullable
  @Override
  public String getType(Uri uri) {
    final int match = sUriMatcher.match(uri);
    switch (match) {
      case HERO:
        return PersistenceContract.CONTENT_HERO_TYPE;
      case HERO_ITEM:
        return PersistenceContract.CONTENT_HERO_ITEM_TYPE;
      case COMIC:
        return PersistenceContract.CONTENT_COMIC_TYPE;
      case COMIC_ITEM:
        return PersistenceContract.CONTENT_COMIC_ITEM_TYPE;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
  }

  @Override
  public boolean onCreate() {
    mDbHelper = new DbHelper(getContext());
    return false;
  }

  @Nullable
  @Override
  public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    Cursor retCursor;
    switch (sUriMatcher.match(uri)) {
      case HERO: {
        retCursor = mDbHelper.getReadableDatabase().query(
          PersistenceContract.HeroEntry.TABLE_NAME,
          projection,
          selection,
          selectionArgs,
          null,
          null,
          sortOrder
        );
        break;
      }
      case HERO_ITEM: {
        String[] where = {uri.getLastPathSegment()};
        retCursor = mDbHelper.getReadableDatabase().query(
          PersistenceContract.HeroEntry.TABLE_NAME,
          projection,
          PersistenceContract.HeroEntry.COLUMN_HERO_ID + " = ?",
          where,
          null,
          null,
          sortOrder
        );
        break;
      }
      case COMIC: {
        retCursor = mDbHelper.getReadableDatabase().query(
          PersistenceContract.ComicEntry.TABLE_NAME,
          projection,
          selection,
          selectionArgs,
          null,
          null,
          sortOrder
        );
        break;
      }
      case COMIC_ITEM: {
        String[] where = {uri.getLastPathSegment()};
        retCursor = mDbHelper.getReadableDatabase().query(
          PersistenceContract.ComicEntry.TABLE_NAME,
          projection,
          PersistenceContract.ComicEntry.COLUMN_COMIC_ID + " = ?",
          where,
          null,
          null,
          sortOrder
        );
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    retCursor.setNotificationUri(getContext().getContentResolver(), uri);
    return retCursor;
  }

  @Nullable
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    Uri returnUri;

    switch (match) {
      case HERO: {
        Cursor exists = db.query(
          PersistenceContract.HeroEntry.TABLE_NAME,
          new String[]{PersistenceContract.HeroEntry.COLUMN_HERO_ID},
          PersistenceContract.HeroEntry.COLUMN_HERO_ID + " = ?",
          new String[]{values.getAsString(PersistenceContract.HeroEntry.COLUMN_HERO_ID)},
          null,
          null,
          null
        );
        if (exists.moveToLast()) {
          long _id = db.update(
            PersistenceContract.HeroEntry.TABLE_NAME, values,
            PersistenceContract.HeroEntry.COLUMN_HERO_ID + " = ?",
            new String[]{values.getAsString(PersistenceContract.HeroEntry.COLUMN_HERO_ID)}
          );
          if (_id > 0) {
            returnUri = PersistenceContract.HeroEntry.buildHeroesUriWith(_id);
          } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
          }
        } else {
          long _id = db.insert(PersistenceContract.HeroEntry.TABLE_NAME, null, values);
          if (_id > 0) {
            returnUri = PersistenceContract.HeroEntry.buildHeroesUriWith(_id);
          } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
          }
        }
        exists.close();
        break;
      }
      case COMIC: {
        Cursor exists = db.query(
          PersistenceContract.ComicEntry.TABLE_NAME,
          new String[]{PersistenceContract.ComicEntry.COLUMN_COMIC_ID},
          PersistenceContract.ComicEntry.COLUMN_COMIC_ID + " = ?",
          new String[]{values.getAsString(PersistenceContract.ComicEntry.COLUMN_COMIC_ID)},
          null,
          null,
          null
        );
        if (exists.moveToLast()) {
          long _id = db.update(
            PersistenceContract.ComicEntry.TABLE_NAME, values,
            PersistenceContract.ComicEntry.COLUMN_COMIC_ID + " = ?",
            new String[]{values.getAsString(PersistenceContract.ComicEntry.COLUMN_COMIC_ID)}
          );
          if (_id > 0) {
            returnUri = PersistenceContract.HeroEntry.buildHeroesUriWith(_id);
          } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
          }
        } else {
          long _id = db.insert(PersistenceContract.ComicEntry.TABLE_NAME, null, values);
          if (_id > 0) {
            returnUri = PersistenceContract.ComicEntry.buildComicsUriWith(_id);
          } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
          }
        }
        exists.close();
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return returnUri;
  }

  @Override
  public int bulkInsert(Uri uri, ContentValues[] values) {
    int i = 0;
    for (ContentValues value : values) {
      insert(uri, value);
      i++;
    }
    return i;
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int rowsDeleted;

    switch (match) {
      case HERO:
        rowsDeleted = db.delete(
          PersistenceContract.HeroEntry.TABLE_NAME, selection, selectionArgs);
        break;
      case COMIC:
        rowsDeleted = db.delete(
          PersistenceContract.ComicEntry.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (selection == null || rowsDeleted != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();
    final int match = sUriMatcher.match(uri);
    int rowsUpdated;

    switch (match) {
      case HERO: {
        rowsUpdated = db.update(PersistenceContract.HeroEntry.TABLE_NAME, values, selection,
          selectionArgs);
        break;
      }
      case HERO_ITEM: {
        selection = PersistenceContract.HeroEntry.COLUMN_HERO_ID + " = ?";
        String[] where = {uri.getLastPathSegment()};
        rowsUpdated = db.update(PersistenceContract.HeroEntry.TABLE_NAME, values, selection, where);
        break;
      }
      case COMIC: {
        rowsUpdated = db.update(PersistenceContract.ComicEntry.TABLE_NAME, values, selection,
          selectionArgs);
        break;
      }
      case COMIC_ITEM: {
        selection = PersistenceContract.ComicEntry.COLUMN_COMIC_ID + " = ?";
        String[] where = {uri.getLastPathSegment()};
        rowsUpdated = db.update(PersistenceContract.ComicEntry.TABLE_NAME, values, selection, where);
        break;
      }
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }
    if (rowsUpdated != 0) {
      getContext().getContentResolver().notifyChange(uri, null);
    }
    return rowsUpdated;
  }
}
