package com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model;

import android.database.Cursor;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class Comic extends MarvelData {
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

  @NonNull
  private final float price;

  private final boolean mFavorite;


  public Comic(Comic.Builder builder) {
    this.mId = builder.mId;
    this.mName = builder.mName;
    this.mDescription = builder.mDescription;
    this.mThumbnailPath = builder.mThumbnailPath;
    this.mThumbnailExtension = builder.mThumbnailExtension;
    this.mDetailsUrl = builder.mDetailsUrl;
    this.price = builder.price;
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

  @NonNull
  public float getPrice() {
    return price;
  }

  public boolean isFavorite() {
    return mFavorite;
  }

  public static List<Comic> getComicsFrom(Cursor cursor) {
    ArrayList<Comic> Comices = new ArrayList<>(cursor.getCount());
    int id;
    String name;
    String desc;
    String thumbnailPath;
    String thumbnailEx;
    String detailsUrl;
    float price;
    boolean favorite;
    Comic Comic;

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      id = cursor.getInt(1);
      name = cursor.getString(2);
      desc = cursor.getString(3);
      thumbnailPath = cursor.getString(4);
      thumbnailEx = cursor.getString(5);
      detailsUrl = cursor.getString(6);
      price = cursor.getFloat(7);
      favorite = cursor.getInt(8) == 1;

      Comic = new Comic.Builder(id, name)
        .description(desc)
        .thumbnailPath(thumbnailPath)
        .thumbnailExtention(thumbnailEx)
        .favorite(favorite)
        .detailsUrl(detailsUrl)
        .price(price)
        .build();

      Comices.add(Comic);
      cursor.moveToNext();
    }
    return Comices;
  }

  public static Comic getComicFrom(Cursor cursor) {
    int id;
    String name;
    String desc;
    String thumbnailPath;
    String thumbnailEx;
    String detailsUrl;
    float price;
    boolean favorite;
    Comic Comic;

    id = cursor.getInt(1);
    name = cursor.getString(2);
    desc = cursor.getString(3);
    thumbnailPath = cursor.getString(4);
    thumbnailEx = cursor.getString(5);
    detailsUrl = cursor.getString(6);
    price = cursor.getFloat(7);
    favorite = cursor.getInt(8) == 1;

    Comic = new Comic.Builder(id, name)
      .description(desc)
      .thumbnailPath(thumbnailPath)
      .thumbnailExtention(thumbnailEx)
      .detailsUrl(detailsUrl)
      .price(price)
      .favorite(favorite)
      .build();

    return Comic;
  }


  public static class Builder {

    private int mId;

    private String mName;

    private String mDescription;

    private String mThumbnailPath;

    private String mThumbnailExtension;

    private String mDetailsUrl;

    private float price;

    private boolean mFavorite;

    public Builder(int id, String name) {
      this.mId = id;
      this.mName = name;
      mDetailsUrl = "https://google.com";
    }

    public Comic.Builder description(String desc) {
      this.mDescription = desc;
      return this;
    }

    public Comic.Builder thumbnailPath(String path) {
      this.mThumbnailPath = path;
      return this;
    }

    public Comic.Builder thumbnailExtention(String ex) {
      this.mThumbnailExtension = ex;
      return this;
    }

    public Comic.Builder detailsUrl(String url) {
      this.mDetailsUrl = url;
      return this;
    }

    public Comic.Builder price(float price) {
      this.price = price;
      return this;
    }

    public Comic.Builder favorite(boolean favorite) {
      this.mFavorite = favorite;
      return this;
    }

    public Comic build() {
      return new Comic(this);
    }
  }
}
