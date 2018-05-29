package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusOrderAct extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.vp_order)
    ViewPager vp_order;


    private String[] titles = {"全部", "待发货", "已发货"};
    private String[] fromList = {"all", "1", "2"};
    private List<BaseFragment> orderFrags;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, PlusOrderAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_plus_order;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.plus_order));

        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }

        orderFrags = new ArrayList<>();
        for (String from : fromList) {
            orderFrags.add(PlusOrderFrag.getInstance(from));
        }
        vp_order.setOffscreenPageLimit(orderFrags.size());
        vp_order.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), orderFrags, titles));
        tab_layout.setupWithViewPager(vp_order);
    }
}
