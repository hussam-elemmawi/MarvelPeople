package com.hussamelemmawi.nanodegree.marvelpeople.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by hussamelemmawi on 19/11/16.
 */

public class Utility {

    private static final String port_uncanny = "/portrait_uncanny";
    private static final String port_small = "/portrait_medium";
    private static final String port_detail = "/detail";
    private static final String dot = ".";

    private static Context mContext;

    public static void initializeUitlity(Context context) {
        mContext = context;
    }

    public static String imageUrlSmall(String basePath, String ex) {
        return basePath + port_small + dot + ex;
    }

    public static String imageUrlMedium(String basePath, String ex) {
        return basePath + port_uncanny + dot + ex;
    }

    public static String imageUrlLarge(String basePath, String ex) {
        return basePath + port_detail + dot + ex;
    }

    public static boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
