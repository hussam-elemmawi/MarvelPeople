package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote;

import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.MarvelResponseModel;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.comic.ComicResponseResult;
import com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote.response_model.hero.HeroResponseResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public interface MarvelApi {

  /*
   Marvel Api response looks like this:
  {
   "code":200,
   "status":"Ok",
   "copyright":"© 2017 MARVEL",
   "attributionText":"Data provided by Marvel. © 2017 MARVEL",
   "attributionHTML":"<a href=\"http://marvel.com\">Data provided by Marvel. © 2017 MARVEL</a>",
   "etag":"aa88323a47481152e7bbc4f29b52ad5c0d1ec1a3",
   "data":{
      "offset":100,
      "limit":5,
      "total":1485,
      "count":5,
      "results":[
         {
            "id":1009489,
            ...
            "thumbnail":{  },
            ...
            "urls":[  ]
         },
         {  },
         {  },
         {  },
         {  }
       ]
     }
  }

  those are filed in common for any response, so I created

  - MarvelResponseModel for the top-most object
  - MarvelResponseData to hold the "data" filed
  - MarvelResponseResult to hold what in common for "results" field as a List, and other classes extending this one like
      HeroResponseResult, ComicResponseResult .. etc.
  */

  @GET(ApiUtility.CHARACTERS_ENDPOINT)
  Call<MarvelResponseModel<HeroResponseResult>> listOfHeroesResponse(@QueryMap Map<String, String> options);

  @GET(ApiUtility.COMICS_ENDPOINT)
  Call<MarvelResponseModel<ComicResponseResult>> listOfComicsResponse(@QueryMap Map<String, String> options);

}
