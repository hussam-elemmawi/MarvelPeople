package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.common;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class Thumbnail {
  @SerializedName("path")
  private String path;

  @SerializedName("extension")
  private String extension;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }
}
