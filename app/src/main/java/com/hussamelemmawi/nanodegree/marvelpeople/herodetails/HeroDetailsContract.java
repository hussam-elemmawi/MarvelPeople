package com.hussamelemmawi.nanodegree.marvelpeople.herodetails;

import android.content.Context;
import android.support.annotation.NonNull;

import com.hussamelemmawi.nanodegree.marvelpeople.BasePresenter;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;

/**
 * Created by hussamelemmawi on 22/11/16.
 * <p>
 * This specifies the contract between the view {@link HeroDetailsFragment}
 * and the presenter {@link HeroDetailsPresenter}.
 */
interface HeroDetailsContract {

  interface View {

    void showHero(Hero hero);

    void showFilteringPopUpMenu();

    void setAsWallpaperDone(String heroName);

    void showNoHeroDataAvailable();
  }

  interface Presenter<T> extends BasePresenter<T> {

    void initializePresenter(@NonNull int heroId);

    void shareHero();

    void saveToFavorite();

    void removeFromFavorite();

    void setHeroAsWallpaper();

    void openHeroDetailsFromWeb(Context context);
  }
}
