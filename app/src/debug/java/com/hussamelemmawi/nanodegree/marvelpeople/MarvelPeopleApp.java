package com.hussamelemmawi.nanodegree.marvelpeople;

import com.facebook.stetho.Stetho;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class MarvelPeopleApp extends MarvelPeopleAppBase {

  @Override
  public void onCreate() {
    super.onCreate();

    Stetho.initializeWithDefaults(this);
  }
}
