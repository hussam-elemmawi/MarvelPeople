package com.hussamelemmawi.nanodegree.marvelpeople.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by hussamelemmawi on 24/11/16.
 */

public class TwoThreeFrameLayout extends FrameLayout {

    public TwoThreeFrameLayout(Context context) {
        super(context);
    }

    public TwoThreeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoThreeFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int threeTwoHeight = MeasureSpec.getSize(widthMeasureSpec) * 3 / 2;
        int threeTwoHeightSpec =
                MeasureSpec.makeMeasureSpec(threeTwoHeight, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, threeTwoHeightSpec);
    }

}
