package com.shunlian.app.ui.fragment.first_page;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.FirstPageAdapter;
import com.shunlian.app.adapter.first_page.BannerAdapter;
import com.shunlian.app.adapter.first_page.BrandAdapter;
import com.shunlian.app.adapter.first_page.BrandTitleAdapter;
import com.shunlian.app.adapter.first_page.FirstPageGoodsAdapter;
import com.shunlian.app.adapter.first_page.GoodsTitleAdapter;
import com.shunlian.app.adapter.first_page.OnePlusTwoLayoutAdapter;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.presenter.MainPagePresenter;
import com.shunlian.app.presenter.PFirstPage;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.ui.message.SystemMsgAct;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFirstPage;
import com.shunlian.app.view.IMainPageView;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;


public class CateGoryFrag extends BaseLazyFragment implements IFirstPage, View.OnClickListener {

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.rv_view)
    RecyclerView rv_view;

    private PFirstPage pFirstPage;
    private String channel_id;
    private long currentTime = 0L;

    public static BaseFragment getInstance(String channel_id) {
        CateGoryFrag fragment = new CateGoryFrag();

        Bundle args = new Bundle();
        args.putSerializable("channel_id", channel_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.frag_category, container, false);
        return rootView;
    }

    @Override
    protected void initListener() {
        super.initListener();

        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                lay_refresh.setRefreshing(false);
//                pDiscoverQuanzi.resetBaby();
            }
        });
    }

    @Override
    protected void initData() {
        //新增下拉刷新
        NestedSlHeader header = new NestedSlHeader(getContext());
        lay_refresh.setRefreshHeaderView(header);

        //end
        channel_id = (String) getArguments().getSerializable("channel_id");
        pFirstPage = new PFirstPage(baseActivity, this);
        pFirstPage.getContentData(channel_id);
    }


    @Override
    public void showFailureView(int request_code) {
        lay_refresh.setRefreshing(false);
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setTab(GetMenuEntity getMenuEntiy) {

    }
    private void delayRunableWithDuration(long remainMiPLus, Runnable runnable) {
        long timePer = System.currentTimeMillis() - currentTime;

        LogUtil.httpLogW("---currentTime----timePer---------" + timePer + "----%--" + timePer % 350);
        long remainMi = 0;
        if (timePer % 350 != 0) {
            remainMi = 350 - timePer % 350;
        }
        LogUtil.httpLogW("---currentTime----remainMi---------" + remainMi);
        new Handler().postDelayed(runnable, remainMi + remainMiPLus);
    }
    @Override
    public void setContent(GetDataEntity getDataEntity) {
        LogUtil.augusLogW("uuu");
        rv_view.setLayoutManager(new LinearLayoutManager(baseActivity,LinearLayoutManager.VERTICAL,false));
        rv_view.setAdapter(new FirstPageAdapter(baseActivity,false,getDataEntity.datas,false));
    }
}
