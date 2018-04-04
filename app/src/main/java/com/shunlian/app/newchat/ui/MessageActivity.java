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

    TextView tv_tab_title;
    TextView tv_count;
    TabLayout.Tab tab;

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

        tab = tab_layout.newTab();
        tab.setCustomView(R.layout.item_tab_point);
        tv_tab_title = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_title);
        tv_count = (TextView) tab.getCustomView().findViewById(R.id.tv_count);
        tv_count.setVisibility(View.VISIBLE);
        tv_count.setText("99+");
        tv_tab_title.setText(tabTitle[0]);
        tab_layout.addTab(tab);

        tab = tab_layout.newTab().setText(tabTitle[1]);
        tab_layout.addTab(tab);

        tab_layout.setupWithViewPager(view_pager);
    }

    @Override
    protected void initListener() {
        super.initListener();
        //添加tabLayout选中监听
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置选中时的文字颜色
                if (tab.getCustomView() != null) {
                    tv_tab_title.setTextColor(getColorResouce(R.color.pink_color));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //设置未选中时的文字颜色
                if (tab.getCustomView() != null) {
                    tv_tab_title.setTextColor(getColorResouce(R.color.new_text));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
