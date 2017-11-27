package com.hussamelemmawi.nanodegree.marvelpeople;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.hussamelemmawi.nanodegree.marvelpeople.Filters.MarvelFavorite.ALL;
import static com.hussamelemmawi.nanodegree.marvelpeople.Filters.MarvelFavorite.FAVORITE;
import static com.hussamelemmawi.nanodegree.marvelpeople.Filters.MarvelMode.COMICS;
import static com.hussamelemmawi.nanodegree.marvelpeople.Filters.MarvelMode.HEROES;

/**
 * Created by hussamelemmawi on 25/02/17.
 * <p>
 * Used with the filter menu option.
 */
public class Filters {

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({ALL, FAVORITE})
  public @interface MarvelFavorite {
    int ALL = 0;
    int FAVORITE = 1;
  }

  @Retention(RetentionPolicy.SOURCE)
  @IntDef({HEROES, COMICS})
  public @interface MarvelMode {
    int HEROES = 10;
    int COMICS = 11;
  }
}
