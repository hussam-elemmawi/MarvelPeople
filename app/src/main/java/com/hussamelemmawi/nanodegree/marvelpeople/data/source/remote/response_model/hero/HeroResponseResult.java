package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.hero;

import com.google.gson.annotations.SerializedName;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.MarvelResponseResult;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class HeroResponseResult extends MarvelResponseResult {

  /*
    "id":1009489,
    "name":"Ben Parker",
    "description":"",
    "urls"[...],
    "thumbnail": ..
  */

  @SerializedName("name")
  private String name;

  @SerializedName("description")
  private String description;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}

