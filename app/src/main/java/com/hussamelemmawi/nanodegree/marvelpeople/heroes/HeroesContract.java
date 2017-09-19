package com.hussamelemmawi.nanodegree.marvelpeople.heroes;

import com.hussamelemmawi.nanodegree.marvelpeople.BasePresenter;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;

import java.util.List;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * This specifies the contract between the view {@link HeroesFragment}
 * and the presenter {@link HeroesPresenter}.
 */
interface HeroesContract {

  interface View {

    void showHeroes(List<Hero> heroes);

    void showLoadingHeroesError();

    void setLoadingIndicator(final boolean active);

    void setFetchingNewPageIndicator(boolean active);

    void showFilteringPopUpMenu();

    void nothingToRefresh();

    void showNoFavoriteHeroes();

    void slideUpFab();

    void slideDownFab();
  }

  interface Presenter<T> extends BasePresenter<T> {

    void loadHeroes(boolean forceUpdate);

    void getNextPage();

    void setFilteringType(int filteringType);

    boolean getFilteringIsFavorite();
  }
}
