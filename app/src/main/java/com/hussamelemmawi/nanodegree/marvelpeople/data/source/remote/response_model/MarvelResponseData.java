package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class MarvelResponseData<R extends MarvelResponseResult> {

  /*
   "offset":0,
   "limit":20,
   "total":12,
   "count":12,
   "results":[  ]
  */

  @SerializedName("offset")
  int offset;

  @SerializedName("limit")
  int limit;

  @SerializedName("total")
  int total;

  @SerializedName("count")
  int count;

  @SerializedName("results")
  List<R> results;

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public List<R> getResults() {
    return results;
  }

  public void setResults(List<R> results) {
    this.results = results;
  }
}
