package com.hussamelemmawi.nanodegree.marvelpeople.di.component;

import com.hussamelemmawi.nanodegree.marvelpeople.comics.ComicsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.comicsdetails.ComicDetailsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;
import com.hussamelemmawi.nanodegree.marvelpeople.herodetails.HeroDetailsFragment;
import com.hussamelemmawi.nanodegree.marvelpeople.heroes.HeroesFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hussamelemmawi on 10/03/17.
 */
@Singleton
@Component(modules = {ApplicationModule.class, DataRepositoryModule.class})
public interface ApplicationComponent {
  // fragments
  void inject(HeroesFragment heroesFragment);
  void inject(HeroDetailsFragment heroDetailsFragment);
  void inject(ComicsFragment comicsFragment);
  void inject(ComicDetailsFragment comicDetailsFragment);
}
