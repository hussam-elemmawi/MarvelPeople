package com.hussamelemmawi.nanodegree.marvelpeople.comicsdetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.hussamelemmawi.nanodegree.marvelpeople.BasePresenter;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;

/**
 * Created by hussamelemmawi on 03/03/17.
 * <p>
 * This specifies the contract between the view {@link ComicDetailsFragment}
 * and the presenter {@link ComicDetailsPresenter}.
 */

public interface ComicDetailsContract {

  interface View {

    void showComic(Comic comic);

    void showFilteringPopUpMenu();

    void setAsWallpaperDone(String heroName);

    void showNoComicDataAvailable();
  }

  interface Presenter<T> extends BasePresenter<T> {

    void initializePresenter(@NonNull int heroId);

    void shareComic(final AppCompatActivity activity);

    void saveToFavorite();

    void removeFromFavorite();

    void setComicAsWallpaper();

    void openComicDetailsFromWeb(Context context);
  }
}
