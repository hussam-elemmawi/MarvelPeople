package com.hussamelemmawi.nanodegree.marvelpeople.util;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;

import com.hussamelemmawi.nanodegree.marvelpeople.R;

/**
 * Created by hussamelemmawi on 24/11/16.
 */

public class SelectableFloatingActionButton extends FloatingActionButton {

    private boolean fabSelected = false;

    public SelectableFloatingActionButton(Context context) {
        super(context);
    }

    public SelectableFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    public boolean isFabSelected() {
        return fabSelected;
    }

    // Making the Fab selectable
    public void setFabSelected(boolean fabSelected) {
        this.fabSelected = fabSelected;
        if (fabSelected) {
            this.setImageResource(R.drawable.ic_star_yellow_24dp);
        } else {
            this.setImageResource(R.drawable.ic_star_grey_24dp);
        }
    }
}
