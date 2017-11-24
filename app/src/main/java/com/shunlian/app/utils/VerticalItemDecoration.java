package com.shunlian.app.utils;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/11/24.
 */

public class VerticalItemDecoration extends RecyclerView.ItemDecoration {

    private int space;
    private int topMargin;
    private int bottomMargin;

    public VerticalItemDecoration(int space, int topMargin, int bottomMargin){

        this.space = space;
        this.topMargin = topMargin;
        this.bottomMargin = bottomMargin;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
        outRect.top = topMargin;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            int lastPosition = manager.findLastVisibleItemPosition();
            if (lastPosition + 1 == manager.getItemCount()){
                outRect.bottom = bottomMargin;
            }
        }
    }
}
