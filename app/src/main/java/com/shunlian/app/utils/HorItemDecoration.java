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

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhang on 2017/3/17 09 : 09.
 */

public class HorItemDecoration extends RecyclerView.ItemDecoration {

    private final int space;
    private int leftMargin;
    private int rightMargin;

    public HorItemDecoration(int space,int leftMargin,int rightMargin){
        this.space = space;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
       outRect.right = space;
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
}
