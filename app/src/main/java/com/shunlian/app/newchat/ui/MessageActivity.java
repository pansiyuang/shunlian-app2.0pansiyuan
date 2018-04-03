package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/3.
 */

public class MessageActivity extends BaseActivity {

    public static final int[] tabTitle = new int[]{R.string.sys_message, R.string.store_message};

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.miv_title_right)
    MyImageView miv_title_right;

    @Override
    protected int getLayoutId() {
        return R.layout.act_message;
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.message));
        miv_title_right.setVisibility(View.VISIBLE);
        miv_title_right.setImageResource(R.mipmap.icon_found_sousuo);

        tab_layout.addTab(tab_layout.newTab().setText(getStringResouce(tabTitle[0])));
        tab_layout.addTab(tab_layout.newTab().setText(getStringResouce(tabTitle[1])));
        tab_layout.setupWithViewPager(view_pager);
    }
}
