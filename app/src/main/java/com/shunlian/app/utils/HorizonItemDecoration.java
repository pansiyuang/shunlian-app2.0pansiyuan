package com.shunlian.app.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/11/24.
 */

public class HorizonItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public HorizonItemDecoration(int space){

        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        if (parent.getChildAdapterPosition(view) + 1 == state.getItemCount()){
            outRect.right = 0;
        }
    }
}
