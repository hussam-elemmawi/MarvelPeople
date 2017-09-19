package com.hussamelemmawi.nanodegree.marvelpeople.data.source.remote;

import com.hussamelemmawi.nanodegree.marvelpeople.BuildConfig;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Created by hussamelemmawi on 10/02/17.
 */

public class ApiUtility {

  static final String MARVEL_BASE_URL = "https://gateway.marvel.com:443/v1/public/";

  static final String CHARACTERS_ENDPOINT = "characters";

  static final String COMICS_ENDPOINT = "comics";

  static final String LIMIT_PARAM = "limit";
  static final String OFFSET_PARAM = "offset";
  static final String TIMESTAMP_PARAM = "ts";
  static final String PUBLIC_API_KEY_PARAM = "apikey";
  static final String HASH_PARAM = "hash"; // hash = md5(ts + privateKey + publicKey) in order

  static HashMap<String, String> getRequestQueryParam(int lastHeroId) {
    String limit = "20";
    String offset = String.valueOf(lastHeroId);
    String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
    String hash = md5(getHash(timeStamp));

    HashMap<String, String> options = new HashMap<>();

    options.put(ApiUtility.LIMIT_PARAM, limit);
    options.put(ApiUtility.OFFSET_PARAM, offset);
    options.put(ApiUtility.TIMESTAMP_PARAM, timeStamp);
    options.put(ApiUtility.PUBLIC_API_KEY_PARAM, BuildConfig.MARVEL_PUBLIC_API_KEY);
    options.put(ApiUtility.HASH_PARAM, hash);

    return options;
  }

  private static String getHash(String timeStamp) {
    return timeStamp
      .concat(BuildConfig.MARVEL_PRIVATE_API_KEY)
      .concat(BuildConfig.MARVEL_PUBLIC_API_KEY);
  }

  private static String md5(final String s) {
    final String MD5 = "MD5";
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest
        .getInstance(MD5);
      digest.update(s.getBytes());
      byte messageDigest[] = digest.digest();

      // Create Hex String
      StringBuilder hexString = new StringBuilder();
      for (byte aMessageDigest : messageDigest) {
        String h = Integer.toHexString(0xFF & aMessageDigest);
        while (h.length() < 2)
          h = "0" + h;
        hexString.append(h);
      }
      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }


}
