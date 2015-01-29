package com.arongeorgel.flinggallery.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Extension of {@link android.support.v7.widget.LinearLayoutManager} and override the method, to
 * return an additional number of pixels to be drawn
 *
 * Created by arongeorgel on 30/01/2015.
 */
public class PreCachingLayoutManager extends LinearLayoutManager {
    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 600;
    private int mExtraLayoutSpace = -1;

    public PreCachingLayoutManager(Context context) {
        super(context);
    }

    public PreCachingLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setDefaultExtraLayoutSpace(int extraLayoutSpace) {
        this.mExtraLayoutSpace = extraLayoutSpace;
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        if(mExtraLayoutSpace > 0) {
            return mExtraLayoutSpace;
        }
        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}
