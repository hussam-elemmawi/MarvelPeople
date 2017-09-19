package com.hussamelemmawi.nanodegree.marvelpeople.comics;

import android.support.annotation.NonNull;

import com.hussamelemmawi.nanodegree.marvelpeople.BasePresenter;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;

import java.util.List;

/**
 * Created by hussamelemmawi on 19/02/17.
 * <p>
 * This specifies the contract between the view {@link ComicsFragment}
 * and the presenter {@link ComicsPresenter}.
 */
interface ComicsContract {

  interface View {

    void showComics(List<Comic> comics);

    void showLoadingComicsError();

    void setLoadingIndicator(final boolean active);

    void setFetchingNewPageIndicator(boolean active);

    void showFilteringPopUpMenu();

    void nothingToRefresh();

    void showNoFavoriteComics();

    void slideUpFab();

    void slideDownFab();
  }

  interface Presenter<T> extends BasePresenter<T> {

    void loadComics(boolean forceUpdate);

    void getNextPage();

    void setFilteringType(int filteringType);

    boolean getFilteringIsFavorite();
  }
}
