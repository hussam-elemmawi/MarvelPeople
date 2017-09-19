package com.hussamelemmawi.nanodegree.marvelpeople;

/**
 * Created by hussamelemmawi on 26/11/16.
 */

public interface BasePresenter<T>{

  void bind(T what);
  void unbind(T what);
  void start();
}
