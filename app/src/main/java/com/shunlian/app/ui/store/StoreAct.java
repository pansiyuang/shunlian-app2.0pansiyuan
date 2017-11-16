package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.adapter.StoreFirstAdapter;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.presenter.StorePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.StoreView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class StoreAct extends BaseActivity implements View.OnClickListener, StoreView {
    @BindView(R.id.store_ctLayout)
    CollapsingToolbarLayout store_ctLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mrlayout_stores)
    MyRelativeLayout mrlayout_stores;

    @BindView(R.id.store_abLayout)
    AppBarLayout store_abLayout;

    @BindView(R.id.miv_store)
    MyImageView miv_store;

    @BindView(R.id.mtv_store)
    MyTextView mtv_store;

    @BindView(R.id.line_first)
    View line_first;

    @BindView(R.id.mtv_baby)
    MyTextView mtv_baby;

    @BindView(R.id.mrlayout_baby)
    MyRelativeLayout mrlayout_baby;

    @BindView(R.id.mrlayout_discount)
    MyRelativeLayout mrlayout_discount;

    @BindView(R.id.mtv_discount)
    MyTextView mtv_discount;

    @BindView(R.id.line_baby)
    View line_baby;

    @BindView(R.id.line_discount)
    View line_discount;

    @BindView(R.id.mrlayout_new)
    MyRelativeLayout mrlayout_new;

    @BindView(R.id.mtv_new)
    MyTextView mtv_new;

    @BindView(R.id.line_new)
    View line_new;

    @BindView(R.id.rv_firstVouch)
    RecyclerView rv_firstVouch;

    @BindView(R.id.mrlayout_store)
    MyRelativeLayout mrlayout_store;

    @BindView(R.id.mllayout_first)
    MyLinearLayout mllayout_first;

    @BindView(R.id.mllayout_baby)
    MyLinearLayout mllayout_baby;

    @BindView(R.id.mllayout_discount)
    MyLinearLayout mllayout_discount;

    @BindView(R.id.rv_new)
    RecyclerView rv_new;

    @BindView(R.id.rv_baby)
    RecyclerView rv_baby;

    @BindView(R.id.rv_discount)
    RecyclerView rv_discount;

    @BindView(R.id.rv_first)
    RecyclerView rv_first;
    @BindView(R.id.miv_storeLogo)
    MyImageView miv_storeLogo;

    @BindView(R.id.mtv_attention)
    MyTextView mtv_attention;

    @BindView(R.id.mtv_storeName)
    MyTextView mtv_storeName;

    @BindView(R.id.mtv_storeScore)
    MyTextView mtv_storeScore;

    @BindView(R.id.mtv_number)
    MyTextView mtv_number;

    @BindView(R.id.mtv_babyNum)
    MyTextView mtv_babyNum;

    @BindView(R.id.mtv_discountNum)
    MyTextView mtv_discountNum;

    @BindView(R.id.mtv_newNum)
    MyTextView mtv_newNum;

    @BindView(R.id.mrlayout_bg)
    MyRelativeLayout mrlayout_bg;

    private StorePresenter storePresenter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, StoreAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_store;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mrlayout_stores.setOnClickListener(this);
        mrlayout_baby.setOnClickListener(this);
        mrlayout_discount.setOnClickListener(this);
        mrlayout_new.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        storePresenter = new StorePresenter(this, this);
        // 设置布局管理器

        List<String> item1 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            item1.add("宝贝--" + i);
        }
        List<String> item2 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            item2.add("促销--" + i);
        }
        List<String> item3 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            item3.add("新品--" + i);
        }

        List<String> item4 = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            item4.add("优惠卷--" + i);
        }
        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>(this, android.R.layout.simple_list_item_1, item3) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                holder.setText(android.R.id.text1, s);
            }
        };

        SimpleRecyclerAdapter simpleRecyclerAdapter1 = new SimpleRecyclerAdapter<String>(this, android.R.layout.simple_list_item_1, item1) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                holder.setText(android.R.id.text1, s);
            }
        };

        SimpleRecyclerAdapter simpleRecyclerAdapter2 = new SimpleRecyclerAdapter<String>(this, android.R.layout.simple_list_item_1, item2) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                holder.setText(android.R.id.text1, s);
            }
        };

        SimpleRecyclerAdapter simpleRecyclerAdapter4 = new SimpleRecyclerAdapter<String>(this, android.R.layout.simple_list_item_1, item4) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                holder.setText(android.R.id.text1, s);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_new.setLayoutManager(manager);
        rv_new.setAdapter(simpleRecyclerAdapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_baby.setLayoutManager(manager1);
        rv_baby.setAdapter(simpleRecyclerAdapter1);

        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_discount.setLayoutManager(manager2);
        rv_discount.setAdapter(simpleRecyclerAdapter2);


        LinearLayoutManager manager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_firstVouch.setLayoutManager(manager4);
        rv_firstVouch.setAdapter(simpleRecyclerAdapter4);

        store_abLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                float alpha = -((float) verticalOffset) / ((float) (store_ctLayout.getHeight() - toolbar.getHeight()));
                mrlayout_store.setAlpha(1 - alpha);
                miv_store.setAlpha(1 - alpha);
                mtv_babyNum.setAlpha(1 - alpha);
                mtv_discountNum.setAlpha(1 - alpha);
                mtv_newNum.setAlpha(1 - alpha);

                if (verticalOffset <= -(store_ctLayout.getHeight() - toolbar.getHeight())) {
                    miv_store.setVisibility(View.GONE);
                    mtv_babyNum.setVisibility(View.GONE);
                    mtv_discountNum.setVisibility(View.GONE);
                    mtv_newNum.setVisibility(View.GONE);
                } else {
                    miv_store.setVisibility(View.VISIBLE);
                    mtv_babyNum.setVisibility(View.VISIBLE);
                    mtv_discountNum.setVisibility(View.VISIBLE);
                    mtv_newNum.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void change(final String type) {
        switch (type) {
            case "first":
                mllayout_first.setVisibility(View.VISIBLE);
                mllayout_baby.setVisibility(View.GONE);
                mllayout_discount.setVisibility(View.GONE);
                rv_new.setVisibility(View.GONE);
                break;
            case "baby":
                mllayout_first.setVisibility(View.GONE);
                mllayout_baby.setVisibility(View.VISIBLE);
                mllayout_discount.setVisibility(View.GONE);
                rv_new.setVisibility(View.GONE);
                break;
            case "discount":
                mllayout_first.setVisibility(View.GONE);
                mllayout_baby.setVisibility(View.GONE);
                mllayout_discount.setVisibility(View.VISIBLE);
                rv_new.setVisibility(View.GONE);
                break;
            case "new":
                mllayout_first.setVisibility(View.GONE);
                mllayout_baby.setVisibility(View.GONE);
                mllayout_discount.setVisibility(View.GONE);
                rv_new.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mrlayout_stores:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_h);
                mtv_store.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                line_first.setVisibility(View.VISIBLE);
                line_baby.setVisibility(View.GONE);
                line_discount.setVisibility(View.GONE);
                line_new.setVisibility(View.GONE);
                change("first");
                break;
            case R.id.mrlayout_baby:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
                mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_baby.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                line_first.setVisibility(View.GONE);
                line_baby.setVisibility(View.VISIBLE);
                line_discount.setVisibility(View.GONE);
                line_new.setVisibility(View.GONE);
                change("baby");
                break;
            case R.id.mrlayout_discount:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
                mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discount.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_new.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_newNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                line_first.setVisibility(View.GONE);
                line_baby.setVisibility(View.GONE);
                line_discount.setVisibility(View.VISIBLE);
                line_new.setVisibility(View.GONE);
                change("discount");
                break;
            case R.id.mrlayout_new:
                miv_store.setImageResource(R.mipmap.icon_shop_shop_n);
                mtv_store.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_baby.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_babyNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discount.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_discountNum.setTextColor(getResources().getColor(R.color.my_gray_three));
                mtv_new.setTextColor(getResources().getColor(R.color.pink_color));
                mtv_newNum.setTextColor(getResources().getColor(R.color.pink_color));
                line_first.setVisibility(View.GONE);
                line_baby.setVisibility(View.GONE);
                line_discount.setVisibility(View.GONE);
                line_new.setVisibility(View.VISIBLE);
                change("new");
                break;
        }
    }

    @Override
    public void storeFirst(List<StoreIndexEntity.Body> bodies) {
        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_first.setLayoutManager(firstManager);
        LogUtil.augusLogW("yxf---"+bodies);
        rv_first.setAdapter(new StoreFirstAdapter(this, false, bodies));
    }

    @Override
    public void storeHeader(StoreIndexEntity.Head head) {
        GlideUtils.getInstance().loadImage(this,miv_storeLogo,head.decoration_logo);
//        GlideUtils.getInstance().l(this,mrlayout_bg,head.decoration_banner);
//        if (head.is_mark)

//        @BindView(R.id.mtv_attention)
//        MyTextView mtv_attention;
//
//        @BindView(R.id.mtv_storeName)
//        MyTextView mtv_storeName;
//
//        @BindView(R.id.mtv_storeScore)
//        MyTextView mtv_storeScore;
//
//        @BindView(R.id.mtv_number)
//        MyTextView mtv_number;
//
//        @BindView(R.id.mtv_babyNum)
//        MyTextView mtv_babyNum;
//
//        @BindView(R.id.mtv_discountNum)
//        MyTextView mtv_discountNum;
//
//        @BindView(R.id.mtv_newNum)
//        MyTextView mtv_newNum;
//
//        @BindView(R.id.mrlayout_bg)
//        MyRelativeLayout mrlayout_bg;
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }
}
