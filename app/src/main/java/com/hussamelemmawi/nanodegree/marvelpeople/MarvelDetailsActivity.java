package com.hussamelemmawi.nanodegree.marvelpeople;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;

import com.hussamelemmawi.nanodegree.marvelpeople.comicsdetails.ComicDetailsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.herodetails.HeroDetailsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.util.ActivityUtil;

/**
 * Created by hussamelemmawi on 22/11/16.
 * <p>
 * This activity holds one of {@link HeroDetailsFragment} or {@link ComicDetailsFragment}
 * for showing a detailed view.
 */

public class MarvelDetailsActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_marvel_details);
    setupWindowTransition();

    // To make the status bar transparent.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
          View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Filters.MarvelMode
    int whichFragment = getIntent().getExtras().getInt(getString(R.string.fragment_key));

    switch (whichFragment) {
      case Filters.MarvelMode.HEROES: {
        // This activity only loads on phone mode.
        // So it handling the fragment loading and adding heroFrom here.
        HeroDetailsFragment heroDetailsFragment =
          (HeroDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (heroDetailsFragment == null) {
          heroDetailsFragment = HeroDetailsFragment.newInstance();
          ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
            heroDetailsFragment, HeroDetailsFragment.TAG, R.id.fragment_container);
        }
        break;
      }
      case Filters.MarvelMode.COMICS: {
        // This activity only loads on phone mode.
        // So it handling the fragment loading and adding heroFrom here.
        ComicDetailsFragment comicDetailsFragment =
          (ComicDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (comicDetailsFragment == null) {
          comicDetailsFragment = ComicDetailsFragment.newInstance();
          ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
            comicDetailsFragment, ComicDetailsFragment.TAG, R.id.fragment_container);
        }
        break;
      }
    }
  }

  private void setupWindowTransition() {
    Fade fade = new Fade();
    fade.setDuration(500);
    getWindow().setEnterTransition(fade);
    getWindow().setReturnTransition(fade);
  }
}
