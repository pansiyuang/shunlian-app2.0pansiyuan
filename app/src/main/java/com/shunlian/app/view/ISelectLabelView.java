package com.shunlian.app.view;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2018/4/23.
 */

public interface ISelectLabelView extends IView {

    void setAdapter(RecyclerView.Adapter adapter);

    void success();

    void failure();
}
