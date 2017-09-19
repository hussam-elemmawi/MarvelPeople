package com.hussamelemmawi.nanodegree.marvelpeople;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.hussamelemmawi.nanodegree.marvelpeople.comics.ComicsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.heroes.HeroesFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.util.ActivityUtil;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * This is the main entry point for the app. it holds {@link HeroesFragment}
 * or {@link ComicsFragment}.
 */

public class MarvelActivity extends AppCompatActivity {

  private DrawerLayout mDrawerLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_marvel);
    setupWindowTransition();
    // Set up the toolbar.
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar ab = getSupportActionBar();
    ab.setHomeAsUpIndicator(R.drawable.ic_menu);
    ab.setDisplayHomeAsUpEnabled(true);

    // Set up the navigation drawer.
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
    initializeHandelingNavigationActions();

    createHeroesView();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // Open the navigation drawer when the home icon is selected from the toolbar.
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
      mDrawerLayout.closeDrawer(GravityCompat.START);
    } else
      super.onBackPressed();
  }

  private void setupWindowTransition() {
    Fade fade = new Fade();
    fade.setDuration(500);
    getWindow().setExitTransition(fade);
    getWindow().setReenterTransition(fade);
  }

  private void initializeHandelingNavigationActions() {
    final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
          case R.id.list_navigation_heroes: {
            createHeroesView();
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
            return true;
          }
          case R.id.list_navigation_comics: {
            createComicsView();
            mDrawerLayout.closeDrawer(GravityCompat.START, true);
            return true;
          }
          default:
            return false;
        }
      }
    });
  }

  private void createHeroesView() {
    if (!ActivityUtil.isFragmentAdd(getSupportFragmentManager(), HeroesFragment.TAG, R.id.fragment_container))
      ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
        HeroesFragment.newInstance(), HeroesFragment.TAG, R.id.fragment_container);
  }

  private void createComicsView() {
    if (!ActivityUtil.isFragmentAdd(getSupportFragmentManager(), ComicsFragment.TAG, R.id.fragment_container))
      ActivityUtil.addFragmentToActivity(getSupportFragmentManager(),
        ComicsFragment.newInstance(), ComicsFragment.TAG, R.id.fragment_container);
  }
}