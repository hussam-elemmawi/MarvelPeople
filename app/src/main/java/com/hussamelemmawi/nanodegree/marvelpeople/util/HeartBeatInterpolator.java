package com.hussamelemmawi.nanodegree.marvelpeople.util;


import android.view.animation.Interpolator;

/**
 * Created by hussamelemmawi on 23/04/17.
 */

public class HeartBeatInterpolator implements Interpolator {

  @Override
  public float getInterpolation(float input) {
    if (input >= 0 && input < 0.4f) {
      return (float) (8 * Math.pow((1.226 * input), 2));
    } else if (input >= 0.4f && input < 0.6f) {
      return (float) (8 * Math.pow((1.226 * input - 0.564719), 2));
    } else if (input >= 0.6f && input <= 1) {
      return (float) (1 / (8 * Math.pow((1.226 * input), 2)));
    } else {
      return 0;
    }
  }
}
