package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class MarvelResponseModel<R extends MarvelResponseResult> {

  /*
   "code":200,
   "status":"Ok",
   "copyright":"© 2017 MARVEL",
   "attributionText":"Data provided by Marvel. © 2017 MARVEL",
   "attributionHTML":"<a href=\"http://marvel.com\">Data provided by Marvel. © 2017 MARVEL</a>",
   "etag":"d5843bd44e03fd56c4dd5007b95832d938256bea",
   ...
  */

  @SerializedName("code")
  int code;

  @SerializedName("status")
  String status;

  @SerializedName("copyright")
  String copyRight;

  @SerializedName("attributionText")
  String attributionText;

  @SerializedName("attributionHTML")
  String attributionHTML;

  @SerializedName("etag")
  String etag;

  @SerializedName("data")
  MarvelResponseData<R> data;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCopyRight() {
    return copyRight;
  }

  public void setCopyRight(String copyRight) {
    this.copyRight = copyRight;
  }

  public String getAttributionText() {
    return attributionText;
  }

  public void setAttributionText(String attributionText) {
    this.attributionText = attributionText;
  }

  public String getAttributionHTML() {
    return attributionHTML;
  }

  public void setAttributionHTML(String attributionHTML) {
    this.attributionHTML = attributionHTML;
  }

  public String getEtag() {
    return etag;
  }

  public void setEtag(String etag) {
    this.etag = etag;
  }

  public MarvelResponseData<R> getData() {
    return data;
  }

  public void setData(MarvelResponseData<R> data) {
    this.data = data;
  }

}
