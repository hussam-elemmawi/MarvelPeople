package com.hussamelemmawi.nanodegree.marvelpeople.comicsdetails;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.hussamelemmawi.nanodegree.marvelpeople.R;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataRepository;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.LoaderProvider;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;

import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 03/03/17.
 * <p>
 * * Presenter holds the logic fot the app and handling User interactions.
 * Listens to User interactions incoming heroFrom {@link ComicDetailsFragment}
 * <p>
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the ComicsPresenter (if it fails, it emits a compiler error).  It uses
 * {@link ApplicationModule} and {@link DataRepositoryModule} to do so.
 */

public class ComicDetailsPresenter implements ComicDetailsContract.Presenter<ComicDetailsFragment>,
  LoaderManager.LoaderCallbacks<Cursor> {


  public final static int COMIC_DETAILS_LOADER = 6;

  private ComicDetailsContract.View mView;

  private final DataRepository mDataRepository;

  private Context mContext;

  private int mComicId;

  private Comic mComic;

  @NonNull
  private LoaderManager mLoaderManager;

  @NonNull
  private LoaderProvider mLoaderProvider;

  /**
   * {@code @Inject} to request a dependency (for a constructor here)
   */
  @Inject
  public ComicDetailsPresenter(Context context, DataRepository dataRepository) {
    mContext = context;
    mDataRepository = checkNotNull(dataRepository);
  }

  @Override
  public void bind(ComicDetailsFragment what) {
    mView = what;
    mLoaderProvider = new LoaderProvider(mContext);
    mLoaderManager = ((ComicDetailsFragment) mView).getActivity().getSupportLoaderManager();
  }

  @Override
  public void unbind(ComicDetailsFragment what) {
    mView = null;
  }

  @Override
  public void start() {
    if (mLoaderManager.getLoader(COMIC_DETAILS_LOADER) == null) {
      mLoaderManager.initLoader(COMIC_DETAILS_LOADER, null, this);
    }
  }

  @Override
  public void initializePresenter(@NonNull int comicId) {
    mComicId = comicId;
  }

  @Override
  public void shareComic(AppCompatActivity activity) {
    String extraText = "Comic Name: " + mComic.getName() + "\n" + mComic.getDescription() +
      " \n\n " + activity.getString(R.string.marvel_hash_tag);

    Intent shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
    shareIntent.setType("text/plain");
    activity.startActivity(Intent.createChooser(shareIntent, activity.getString(R.string.share_via)));
  }

  @Override
  public void saveToFavorite() {
    mDataRepository.markAsFavoriteComic(mComicId);
  }

  @Override
  public void removeFromFavorite() {
    mDataRepository.markAsUnFavoriteComic(mComicId);
  }

  @Override
  public void setComicAsWallpaper() {
    mView.setAsWallpaperDone(mComic.getName());
  }

  @Override
  public void openComicDetailsFromWeb(Context context) {
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(Uri.parse(mComic.getDetailsUrl()));
    context.startActivity(intent);
  }

  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    return mLoaderProvider.createComicLoader(mComicId);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    if (data != null) {
      if (data.moveToNext()) {
        // Bind Data into views
        mComic = Comic.getComicFrom(data);
        mView.showComic(mComic);

      } else {
        mView.showNoComicDataAvailable();
      }
    } else {
      mView.showNoComicDataAvailable();
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {

  }

  private void setHeroId(int heroId) {
    mComicId = heroId;
  }
}
