package com.hussamelemmawi.nanodegree.marvelpeople.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by hussamelemmawi on 04/04/17.
 */

public class ThreeTwoFrameLayout extends FrameLayout {

  public ThreeTwoFrameLayout(Context context) {
    super(context);
  }

  public ThreeTwoFrameLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ThreeTwoFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public ThreeTwoFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int threeTwoHeight = View.MeasureSpec.getSize(widthMeasureSpec) * 2 / 3;
    int threeTwoHeightSpec =
      View.MeasureSpec.makeMeasureSpec(threeTwoHeight, View.MeasureSpec.EXACTLY);

    super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
  }
}
