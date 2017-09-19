package com.hussamelemmawi.nanodegree.marvelpeople.di.module;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hussamelemmawi on 21/11/16.
 * <p>
 * This is a Dagger module. We use this to pass in the Context dependency to the
 * {@link DataRepositoryComponent}.
 */
@Module
public final class ApplicationModule {

  private final Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  Context provideApplicationContext() {
    return application;
  }
}
