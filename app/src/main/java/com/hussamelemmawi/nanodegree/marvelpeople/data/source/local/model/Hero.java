package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hussamelemmawi on 18/11/16.
 */

public class Hero extends MarvelData {
  @NonNull
  private final int mId;

  @NonNull
  private final String mName;

  @NonNull
  private final String mDescription;

  @NonNull
  private final String mThumbnailPath;

  @NonNull
  private final String mThumbnailExtension;

  @NonNull
  private final String mDetailsUrl;

  private final boolean mFavorite;


  public Hero(Builder builder) {
    this.mId = builder.mId;
    this.mName = builder.mName;
    this.mDescription = builder.mDescription;
    this.mThumbnailPath = builder.mThumbnailPath;
    this.mThumbnailExtension = builder.mThumbnailExtension;
    this.mDetailsUrl = builder.mDetailsUrl;
    this.mFavorite = builder.mFavorite;
  }

  @NonNull
  public int getId() {
    return mId;
  }

  @NonNull
  public String getName() {
    return mName;
  }

  @NonNull
  public String getDescription() {
    return mDescription;
  }

  @NonNull
  public String getThumbnailPath() {
    return mThumbnailPath;
  }

  @NonNull
  public String getThumbnailExtension() {
    return mThumbnailExtension;
  }

  @NonNull
  public String getDetailsUrl() {
    return mDetailsUrl;
  }

  public boolean isFavorite() {
    return mFavorite;
  }

  public static List<Hero> getHeroesFrom(Cursor cursor) {
    ArrayList<Hero> heroes = new ArrayList<>(cursor.getCount());
    int id;
    String name;
    String desc;
    String thumbnailPath;
    String thumbnailEx;
    String detailsUrl;
    boolean favorite;
    Hero hero;

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      id = cursor.getInt(1);
      name = cursor.getString(2);
      desc = cursor.getString(3);
      thumbnailPath = cursor.getString(4);
      thumbnailEx = cursor.getString(5);
      detailsUrl = cursor.getString(6);
      favorite = cursor.getInt(7) == 1;

      hero = new Hero.Builder(id, name)
        .description(desc)
        .thumbnailPath(thumbnailPath)
        .thumbnailExtention(thumbnailEx)
        .detailsUrl(detailsUrl)
        .favorite(favorite)
        .build();
      heroes.add(hero);
      cursor.moveToNext();
    }
    return heroes;
  }

  public static Hero getHeroFrom(Cursor cursor) {
    int id;
    String name;
    String desc;
    String thumbnailPath;
    String thumbnailEx;
    String detailsUrl;
    boolean favorite;

    Hero hero;

    id = cursor.getInt(1);
    name = cursor.getString(2);
    desc = cursor.getString(3);
    thumbnailPath = cursor.getString(4);
    thumbnailEx = cursor.getString(5);
    detailsUrl = cursor.getString(6);
    favorite = cursor.getInt(7) == 1;

    hero = new Hero.Builder(id, name)
      .description(desc)
      .thumbnailPath(thumbnailPath)
      .thumbnailExtention(thumbnailEx)
      .detailsUrl(detailsUrl)
      .favorite(favorite)
      .build();
    return hero;
  }


  public static class Builder {

    private int mId;

    private String mName;

    private String mDescription;

    private String mThumbnailPath;

    private String mThumbnailExtension;

    private String mDetailsUrl;

    private boolean mFavorite;

    public Builder(int id, String name) {
      this.mId = id;
      this.mName = name;
      mDetailsUrl = "https://google.com";
    }

    public Builder description(String desc) {
      this.mDescription = desc;
      return this;
    }

    public Builder thumbnailPath(String path) {
      this.mThumbnailPath = path;
      return this;
    }

    public Builder thumbnailExtention(String ex) {
      this.mThumbnailExtension = ex;
      return this;
    }

    public Builder detailsUrl(String url) {
      this.mDetailsUrl = url;
      return this;
    }

    public Builder favorite(boolean favorite) {
      this.mFavorite = favorite;
      return this;
    }

    public Hero build() {
      return new Hero(this);
    }
  }
}
