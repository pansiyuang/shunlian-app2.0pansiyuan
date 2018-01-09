package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DoubleCategoryAdapter;
import com.shunlian.app.adapter.SingleCategoryAdapter;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.presenter.CategoryPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICategoryView;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

import static com.shunlian.app.adapter.DoubleCategoryAdapter.BANANER_LAYOUT;
import static com.shunlian.app.utils.TransformUtil.expandViewTouchDelegate;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CategoryAct extends SideslipBaseActivity implements ICategoryView, OnClickListener {

    public static final int MODE_SINGLE = 1;
    public static final int MODE_DOUBLE = 2;

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.miv_change_mode)
    MyImageView miv_change_mode;

    private CategoryPresenter presenter;
    private SingleCategoryAdapter singleAdapter;
    private DoubleCategoryAdapter doubleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private VerticalItemDecoration verticalItemDecoration;
    private GridSpacingItemDecoration spacingItemDecoration;
    private int currentMode = MODE_SINGLE;

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_category;
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, CategoryAct.class);
        context.startActivity(intent);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        presenter = new CategoryPresenter(this, this);
        presenter.getSearchGoods();

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        verticalItemDecoration = new VerticalItemDecoration(TransformUtil.dip2px(this, 0.5f), 0, 0, getColorResouce(R.color.value_ECECEC));
        spacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(this, 5f), false);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //通过获取adapter来获取当前item的itemviewtype
                int type = recycle_category.getAdapter().getItemViewType(position);
                if (type == BANANER_LAYOUT) {
                    return 1;
                } else {
                    return gridLayoutManager.getSpanCount();
                }
            }
        });
        recycle_category.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_change_mode.setOnClickListener(this);
        expandViewTouchDelegate(miv_change_mode, TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f));
    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity) {
        if (goodsEntity.goods_list != null && goodsEntity.goods_list.size() != 0) {
            singleAdapter = new SingleCategoryAdapter(this, false, goodsEntity.goods_list, goodsEntity.ref_store);
            doubleAdapter = new DoubleCategoryAdapter(this, false, goodsEntity.goods_list, goodsEntity.ref_store);
            setListMode(currentMode);
        }
    }

    public void setListMode(int mode) {
        if (mode == MODE_SINGLE) {
            recycle_category.setLayoutManager(linearLayoutManager);
            recycle_category.removeItemDecoration(spacingItemDecoration);
            recycle_category.addItemDecoration(verticalItemDecoration);
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_yilei));
            if (singleAdapter != null) {
                recycle_category.setAdapter(singleAdapter);
            }
        } else if (mode == MODE_DOUBLE) {
            recycle_category.setLayoutManager(gridLayoutManager);
            recycle_category.removeItemDecoration(verticalItemDecoration);
            recycle_category.addItemDecoration(spacingItemDecoration);
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_lianglei));
            if (doubleAdapter != null) {
                recycle_category.setAdapter(doubleAdapter);
            }
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_change_mode:
                if (currentMode == MODE_SINGLE) {
                    currentMode = MODE_DOUBLE;
                } else {
                    currentMode = MODE_SINGLE;
                }
                setListMode(currentMode);
                break;
        }
        super.onClick(view);
    }
}
