package com.shunlian.app.ui.fragment.first_page;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.FirstPageAdapter;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.PFirstPage;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.view.IFirstPage;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CateGoryFrag extends BaseFragment implements IFirstPage, View.OnClickListener {
    public PFirstPage pFirstPage;
    public String cate_id;
    public List<GetDataEntity.MData> mDatass = new ArrayList<>();
    public List<GetDataEntity.MData> mDatasss = new ArrayList<>();
    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;
    @BindView(R.id.rv_view)
    public RecyclerView rv_view;
    //    @BindView(R.id.mtv_empty)
//    MyTextView mtv_empty;
    private String channel_id;
    private FirstPageAdapter firstPageAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isFirst = false,isShow=true;


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

    public void refresh(){
        mDatasss.clear();
        pFirstPage.getContentData(channel_id,isShow);
        isShow=true;
    }
    @Override
    protected void initListener() {
        super.initListener();
        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                isShow=false;
               refresh();
            }
        });

        rv_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayoutManager != null) {
                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                        if (pFirstPage != null) {
                            pFirstPage.refreshBaby(cate_id);
                        }
                    }
                }
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
        if (FirstPageFrag.firstId.equals(channel_id)) {
            isFirst = true;
        } else {
            isFirst = false;
        }
//        channel_id = "15";
        pFirstPage = new PFirstPage(baseActivity, this, this);
        pFirstPage.getContentData(channel_id,isShow);
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

    @Override
    public void setContent(GetDataEntity getDataEntity) {
//        if (getDataEntity.datas!=null&&getDataEntity.datas.size()>0){
////            mtv_empty.setVisibility(View.GONE);
////            rv_view.setVisibility(View.VISIBLE);
//            mtv_empty.setVisibility(View.VISIBLE);
//            rv_view.setVisibility(View.GONE);
        if (lay_refresh!=null)
            lay_refresh.setRefreshing(false);
        int size=0;
        mDatass.clear();
        if (getDataEntity!=null){
            size=getDataEntity.datas.size();
            mDatass.addAll(getDataEntity.datas);
        }
//        if (firstPageAdapter==null){
        firstPageAdapter = new FirstPageAdapter(baseActivity, true, mDatass, isFirst, this,size);
        gridLayoutManager = new GridLayoutManager(baseActivity, 2);
        rv_view.setLayoutManager(gridLayoutManager);
        rv_view.setAdapter(firstPageAdapter);

        if (!isEmpty(mDatass)&&!isEmpty(mDatass.get(mDatass.size()-1).cates))
        pFirstPage.resetBaby(mDatass.get(mDatass.size()-1).cates.get(0).id);
//        }else {
////            mtv_empty.setVisibility(View.VISIBLE);
////            rv_view.setVisibility(View.GONE);
//            mtv_empty.setVisibility(View.GONE);
//            rv_view.setVisibility(View.VISIBLE);
//        }

//        }else {
//            LogUtil.augusLogW("lll");
//            firstPageAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void setGoods(List<GoodsDeatilEntity.Goods> mDatas, int page, int allPage) {
        if (rv_view!=null&&rv_view.getScrollState() == 0) {
            firstPageAdapter.setPageLoading(page, allPage);
            for (int i = 0; i < mDatas.size(); i++) {
                GetDataEntity.MData mData = new GetDataEntity.MData();
                mData.module = "moreGoods";
                mData.moreGoods = mDatas.get(i);
                mDatasss.add(mData);
                if (i >= mDatas.size() - 1) {
                    mDatass.addAll(mDatasss);
                    firstPageAdapter.notifyDataSetChanged();
                }
            }
            if (mDatas.size() <= 0) {
                firstPageAdapter.notifyDataSetChanged();
            }
        }

    }
}
