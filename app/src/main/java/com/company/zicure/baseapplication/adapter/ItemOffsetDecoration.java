package com.company.zicure.baseapplication.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by macintosh on 16/3/18.
 */

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int mSpacing;

    public ItemOffsetDecoration(int mSpacing) {
        this.mSpacing = mSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mSpacing, mSpacing, mSpacing, mSpacing);
    }
}
