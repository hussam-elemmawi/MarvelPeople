package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.comic;

import com.google.gson.annotations.SerializedName;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.MarvelResponseResult;

import java.util.List;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class ComicResponseResult extends MarvelResponseResult {

  /*
    "id":1009489,
    "title":"The Amazing Spider-man",
    "description":"",
    "urls"[...],
    "thumbnail": ..,
    "prices" [...]
  */

  @SerializedName("title")
  private String title;

  @SerializedName("description")
  private String description;

  @SerializedName("prices")
  private List<ComicPrice> comicPrices;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<ComicPrice> getComicPrices() {
    return comicPrices;
  }

  public void setComicPrices(List<ComicPrice> comicPrices) {
    this.comicPrices = comicPrices;
  }
}
