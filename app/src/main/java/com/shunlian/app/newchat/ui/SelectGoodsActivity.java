package com.shunlian.app.newchat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SelectGoodsActivity extends BaseActivity {
    public static List<ChatGoodsEntity.Goods> mSelectGoods = new ArrayList<>();

    private String[] titles = {"订单", "收藏", "购物车"};
    private static String[] fromList = {"order", "favorite", "cart"};

    @BindView(R.id.ll_sure)
    LinearLayout ll_sure;

    @BindView(R.id.tv_select_count)
    TextView tv_select_count;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.vp_goods)
    ViewPager vp_goods;

    private List<BaseFragment> goodsFrags;
    private String mStoreId;

    public static void startActForResult(Context context, String mStoreId, int requestCode) {
        Intent intent = new Intent(context, SelectGoodsActivity.class);
        intent.putExtra("store_id", mStoreId);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_select_goods;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mStoreId = getIntent().getStringExtra("store_id");

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }
        goodsFrags = new ArrayList<>();
        for (String from : fromList) {
            goodsFrags.add(SelectGoodsFragment.getInstance(from, mStoreId));
        }
        vp_goods.setOffscreenPageLimit(3);
        vp_goods.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        tab_layout.setupWithViewPager(vp_goods);
    }

    @Override
    protected void initListener() {
        ll_sure.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_sure:
                if (isEmpty(mSelectGoods)) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("select_goods", (Serializable) mSelectGoods);
                setResult(RESULT_OK, intent);
                finish();
                mSelectGoods.clear();
                break;
        }
        super.onClick(view);
    }

    public void updateSelectCount() {
        tv_select_count.setText(mSelectGoods.size() + "/");
    }
}
