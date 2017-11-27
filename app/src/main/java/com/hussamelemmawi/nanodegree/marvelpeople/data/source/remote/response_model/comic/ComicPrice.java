package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.comic;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class ComicPrice {

  @SerializedName("type")
  private String type;

  @SerializedName("price")
  private float price;

  public String getType() {
    return type;
  }

  public float getPrice() {
    return price;
  }
}
