package com.hussamelemmawi.nanodegree.marvelpeople;

import android.app.Application;

import com.hussamelemmawi.nanodegree.marvelpeople.di.component.ApplicationComponent;
import com.hussamelemmawi.nanodegree.marvelpeople.di.component.DaggerApplicationComponent;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.ApplicationModule;
import com.hussamelemmawi.nanodegree.marvelpeople.di.module.DataRepositoryModule;
import com.hussamelemmawi.nanodegree.marvelpeople.util.Utility;

/**
 * Created by hussamelemmawi on 19/11/16.
 * <p>
 * Even though Dagger2 allows annotating a {@link dagger.Component} as a singleton, the code itself
 * must ensure only one instance of the class is created. Therefore, we create a custom
 * {@link Application} class to store a singleton reference to the {@link ApplicationComponent}.
 */
public class MarvelPeopleAppBase extends android.app.Application {
  private ApplicationComponent applicationComponent;

  @Override
  public void onCreate() {
    super.onCreate();
    // Passing context to Utility class so it can use it for the app lifecycle.
    Utility.initializeUitlity(getApplicationContext());

    this.applicationComponent = initializeInjector();
  }

  private ApplicationComponent initializeInjector() {
    return DaggerApplicationComponent.builder()
      .dataRepositoryModule(new DataRepositoryModule())
      .applicationModule(new ApplicationModule(this)).build();
  }

  public ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }
}
