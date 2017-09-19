package com.hussamelemmawi.nanodegree.marvelpeople.di.module;

import android.content.Context;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataRepository;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.LocalDataSource;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.RemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hussamelemmawi on 21/11/16.
 * <p>
 * {@code @Module}for the classes whose methods provide dependencies
 * {@code @Provides} for the methods within {@code @Module} classes
 */
@Module
public class DataRepositoryModule {

  @Singleton
  @Provides
  DataRepository provideDataRepository(Context context) {
    return new DataRepository(new RemoteDataSource(), new LocalDataSource(context));
  }
}
