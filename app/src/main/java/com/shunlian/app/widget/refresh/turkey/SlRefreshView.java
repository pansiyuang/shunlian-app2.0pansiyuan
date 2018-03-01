package com.shunlian.app.widget.refresh.turkey;

import android.content.Context;
import android.util.AttributeSet;

import com.shunlian.app.widget.refreshlayout.FooterView;
import com.shunlian.app.widget.refreshlayout.RefreshLayout;


/**
 * 顺联火鸡下拉刷新的layout
 * Created by zhouweilong on 2016/10/24.
 */

public class SlRefreshView extends RefreshLayout {
    public SlRefreshView(Context context) {
        super(context);
    }

    public SlRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    public void init() {
        SlHeader header = new SlHeader(getContext());
        FooterView footer = new FooterView(getContext());

        addHeader(header);
        addFooter(footer);
        setOnHeaderListener(header);
        setOnFooterListener(footer);
    }
}
