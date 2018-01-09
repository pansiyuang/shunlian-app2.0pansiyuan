package com.shunlian.app.utils;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhang on 2017/3/17 09 : 09.
 */

public class HorItemDecoration extends RecyclerView.ItemDecoration {

    private int space; //item中间间距
    private int leftMargin;//第一个item距左边的边距
    private int rightMargin;//最后一个item距右边的边距
    private Paint mPaint;

    public HorItemDecoration(int space,int leftMargin,int rightMargin){
        this.space = space;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
    }

    public HorItemDecoration(int space,int leftMargin,int rightMargin,@ColorInt int id){
        this.space = space;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
        mPaint = new Paint();
        mPaint.setColor(id);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        outRect.left = leftMargin;
        RecyclerView.LayoutManager manager = parent.getLayoutManager();
        if (manager instanceof LinearLayoutManager){
            LinearLayoutManager manager1 = (LinearLayoutManager) manager;
            if (manager1.findLastVisibleItemPosition()+1 == manager1.getItemCount()){
                if (rightMargin != 0){
                    outRect.right = rightMargin;
                }else {
                    outRect.right = 0;
                }
            }
            if (leftMargin != 0){
                if (manager1.findFirstVisibleItemPosition() == 0){
                    outRect.left = leftMargin;
                }else {
                    outRect.left = 0;
                }
            }
        }
    }


    //绘制分割线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mPaint != null)
            drawVertical(c, parent);
    }

    //绘制纵向 item 分割线
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            if (i + 1 == childSize){
                continue;
            }
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + space;
            if (mPaint != null) {
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }
}
