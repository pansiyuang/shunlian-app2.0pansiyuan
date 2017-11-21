package com.shunlian.app.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by augus on 2017/11/17 0017.
 */

public class GrideItemDecoration extends RecyclerView.ItemDecoration {
    private final int spaceLeft;
    private final int spaceTop;
    private final int spaceRight;
    private final int spaceBottom;
    private boolean isPage;

    public GrideItemDecoration(int spaceLeft, int spaceTop, int spaceRight, int spaceBottom,  boolean isPage) {
        this.spaceLeft = spaceLeft;
        this.spaceTop = spaceTop;
        this.spaceRight = spaceRight;
        this.spaceBottom = spaceBottom;
        this.isPage = isPage;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = spaceLeft;
        outRect.top = spaceTop;
        outRect.right = spaceRight;
        outRect.bottom = spaceBottom;
        if (parent.getChildAdapterPosition(view) % 2 == 1) {
            outRect.right = 0;
        }

        if (isPage) {
            if (parent.getChildAdapterPosition(view) + 1 == state.getItemCount()) {
                outRect.bottom = 0;
                outRect.right = 0;
            }
        } else {
            if (parent.getChildAdapterPosition(view) + 1 == state.getItemCount()) {
                outRect.bottom = 0;
            }
            if (parent.getChildAdapterPosition(view) + 2 == state.getItemCount()) {
                outRect.bottom = 0;
            }
        }
    }
}
