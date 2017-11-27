package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote;

import android.support.annotation.NonNull;
import android.util.Log;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataSource;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.DataSource.FetchDataCallback;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Comic;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.local.model.Hero;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.MarvelResponseModel;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.comic.ComicPrice;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.comic.ComicResponseResult;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.common.MarvelUrl;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.hero.HeroResponseResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hussamelemmawi on 18/11/16.
 */

public class RemoteDataSource implements DataSource.Remote {
    private MarvelApi mMarvelApi;

    public RemoteDataSource() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUtility.MARVEL_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMarvelApi = retrofit.create(MarvelApi.class);
    }

    @Override
    public void getHeroes(@NonNull final FetchDataCallback callback, int fromHeroId) {
        Call<MarvelResponseModel<HeroResponseResult>> call = mMarvelApi.listOfHeroesResponse(ApiUtility.getRequestQueryParam(fromHeroId));
        call.enqueue(new Callback<MarvelResponseModel<HeroResponseResult>>() {
            @Override
            public void onResponse(Call<MarvelResponseModel<HeroResponseResult>> call, Response<MarvelResponseModel<HeroResponseResult>> response) {
                callback.onSuccess(mapHeroResponseResultsIntoHeroes(response.body().getData().getResults()));
            }

            @Override
            public void onFailure(Call<MarvelResponseModel<HeroResponseResult>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    @Override
    public void getComics(@NonNull final FetchDataCallback callback, int fromComicId) {
        Call<MarvelResponseModel<ComicResponseResult>> call = mMarvelApi.listOfComicsResponse(ApiUtility.getRequestQueryParam(fromComicId));
        call.enqueue(new Callback<MarvelResponseModel<ComicResponseResult>>() {
            @Override
            public void onResponse(Call<MarvelResponseModel<ComicResponseResult>> call, Response<MarvelResponseModel<ComicResponseResult>> response) {
                callback.onSuccess(mapComicResponseResultsIntoComics(response.body().getData().getResults()));
            }

            @Override
            public void onFailure(Call<MarvelResponseModel<ComicResponseResult>> call, Throwable t) {
                callback.onFailure();
            }
        });
    }

    private List<Hero> mapHeroResponseResultsIntoHeroes(List<HeroResponseResult> heroResponseResultList) {
        List<Hero> heroList = new ArrayList<>();
        Hero.Builder builder;
        for (HeroResponseResult hrr : heroResponseResultList) {
            builder = new Hero.Builder(hrr.getId(), hrr.getName())
                    .description(hrr.getDescription())
                    .thumbnailPath(hrr.getThumbnail().getPath())
                    .thumbnailExtention(hrr.getThumbnail().getExtension());

            for (MarvelUrl marvelUrl : hrr.getUrls()) {
                if (marvelUrl.getType().equals("detail")) {
                    builder.detailsUrl(marvelUrl.getUrl());
                }
            }
            heroList.add(builder.build());
        }
        return heroList;
    }

    private List<Comic> mapComicResponseResultsIntoComics(List<ComicResponseResult> comicResponseResultList) {
        List<Comic> comicList = new ArrayList<>();

        Comic.Builder builder;
        for (ComicResponseResult crr : comicResponseResultList) {
            builder = new Comic.Builder(crr.getId(), crr.getTitle())
                    .description(crr.getDescription())
                    .thumbnailPath(crr.getThumbnail().getPath())
                    .thumbnailExtention(crr.getThumbnail().getExtension());

            for (MarvelUrl marvelUrl : crr.getUrls()) {
                if (marvelUrl.getType().equals("detail")) {
                    builder.detailsUrl(marvelUrl.getUrl());
                }
            }

            for (ComicPrice comicPrice : crr.getComicPrices()) {
                if (comicPrice.getType().equals("printPrice")) {
                    builder.price(comicPrice.getPrice());
                    Log.d("debug", "" + comicPrice.getPrice());
                }
            }
            comicList.add(builder.build());
        }
        return comicList;
    }
}
