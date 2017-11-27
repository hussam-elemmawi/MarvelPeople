package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class MarvelUrl {

  @SerializedName("type")
  private String type;

  @SerializedName("url")
  private String url;

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }
}
