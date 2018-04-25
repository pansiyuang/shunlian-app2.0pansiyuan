package com.shunlian.app.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2018/4/24.
 */

public interface ISelectLikeView extends IView {

    void setAdapter(RecyclerView.Adapter adapter);

    void setTextTag(String tag,int count);
}
