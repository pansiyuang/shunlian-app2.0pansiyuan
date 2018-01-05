package com.shunlian.app.ui.category;

import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.ui.SideslipBaseActivity;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CategoryAct extends SideslipBaseActivity {

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_category;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }
}
