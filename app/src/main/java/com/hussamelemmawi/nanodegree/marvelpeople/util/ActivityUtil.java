package com.hussamelemmawi.nanodegree.marvelpeople.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by hussamelemmawi on 18/11/16.
 * <p>
 * This provides methods to help Activities load their UI.
 */
public class ActivityUtil {

  public static void addFragmentToActivity(@NonNull FragmentManager manager,
                                           @NonNull Fragment fragment, String fragmentTag, int fragmentContainer) {

    checkNotNull(manager);
    checkNotNull(fragment);
    manager.beginTransaction().replace(fragmentContainer, fragment, fragmentTag).commit();
  }

  public static boolean isFragmentAdd(@NonNull FragmentManager manager, String fragmentTag, int fragmentContainer) {
    return manager.findFragmentById(fragmentContainer) != null
      && manager.findFragmentById(fragmentContainer).getTag().matches(fragmentTag);
  }
}

