package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model;

import com.google.gson.annotations.SerializedName;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.common.MarvelUrl;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.common.Thumbnail;

import java.util.List;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class MarvelResponseResult {

  @SerializedName("id")
  private int id;

  @SerializedName("urls")
  private List<MarvelUrl> urls;

  @SerializedName("thumbnail")
  private Thumbnail thumbnail;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<MarvelUrl> getUrls() {
    return urls;
  }

  public void setUrls(List<MarvelUrl> urls) {
    this.urls = urls;
  }

  public Thumbnail getThumbnail() {
    return thumbnail;
  }

  public void setThumbnail(Thumbnail thumbnail) {
    this.thumbnail = thumbnail;
  }

}
