package com.shunlian.app.ui.fragment.first_page;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.FirstPageAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.PFirstPage;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFirstPage;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CateGoryFrag extends BaseFragment implements IFirstPage {
    public PFirstPage pFirstPage;
    public String cate_id,sort_type;
    public List<GetDataEntity.MData> mDatass = new ArrayList<>();
    public List<GetDataEntity.MData> mDatasss = new ArrayList<>();
    public List<GetDataEntity.MData> mDatassss = new ArrayList<>();
    @BindView(R.id.rv_view)
    public RecyclerView rv_view;
    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;
    //    @BindView(R.id.mtv_empty)
//    MyTextView mtv_empty;
    private String channel_id;
    private FirstPageAdapter firstPageAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isFirst = false, isShow = true;
    private View rootView;


    public static BaseFragment getInstance(String channel_id) {
        CateGoryFrag fragment = new CateGoryFrag();

        Bundle args = new Bundle();
        args.putSerializable("channel_id", channel_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_category, container, false);
        return rootView;
    }

    public void refresh() {
        mDatasss.clear();
        pFirstPage.getContentData(channel_id, isShow);
        isShow = true;
    }
    public int getScollYDistance() {
        int position = gridLayoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = gridLayoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    @Override
    protected void initListener() {
        super.initListener();
        lay_refresh.setOnRefreshListener(new onRefreshListener() {
            @Override
            public void onRefresh() {
                isShow = false;
                refresh();
            }
        });


        rv_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int value= TransformUtil.dip2px(baseActivity,80);
                RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(value,value);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                if (dy>0){
                    layoutParams.setMargins(0,0,-value/2,value);
                    FirstPageFrag.isHide=true;
                }else {
                    layoutParams.setMargins(0,0,0,value);
                    FirstPageFrag.isHide=false;
                }
                FirstPageFrag.miv_entry.setLayoutParams(layoutParams);

                if (!FirstPageFrag.isExpand&&0>=getScollYDistance())
                    FirstPageFrag.mAppbar.setExpanded(true);
                if (gridLayoutManager != null) {
                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                        if (pFirstPage != null) {
                            pFirstPage.refreshBaby(cate_id,sort_type);
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
        pFirstPage.getContentData(channel_id, isShow);
    }


    @Override
    public void showFailureView(int request_code) {
        if (lay_refresh != null)
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
        if (!isEmpty(getDataEntity.default_keyword))
        FirstPageFrag.mtv_search.setText(getDataEntity.default_keyword);
        if (lay_refresh != null)
            lay_refresh.setRefreshing(false);
        int size = 0;
        mDatass.clear();
        if (getDataEntity != null) {
            size = getDataEntity.datas.size();
            mDatass.addAll(getDataEntity.datas);
        }
//        if (firstPageAdapter==null){
        firstPageAdapter = new FirstPageAdapter(baseActivity, true, mDatass, isFirst, this, size);
        gridLayoutManager = new GridLayoutManager(baseActivity, 2);

        if (rv_view == null)
            rv_view = (RecyclerView) rootView.findViewById(R.id.rv_view);
        rv_view.setLayoutManager(gridLayoutManager);
        rv_view.setAdapter(firstPageAdapter);

        if (!isEmpty(mDatass) && !isEmpty(mDatass.get(mDatass.size() - 1).cates)) {
            cate_id = mDatass.get(mDatass.size() - 1).cates.get(0).id;
            sort_type = mDatass.get(mDatass.size() - 1).cates.get(0).sort_type;
            pFirstPage.resetBaby(cate_id,sort_type);
        }

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
        firstPageAdapter.setPageLoading(page, allPage);
        if (mDatas.size() <= 0) {
            pFirstPage.babyIsLoading = false;
            firstPageAdapter.notifyDataSetChanged();
        } else {
            for (int i = 0; i < mDatas.size(); i++) {
                GetDataEntity.MData mData = new GetDataEntity.MData();
                mData.module = "moreGoods";
                mData.moreGoods = mDatas.get(i);
                mDatassss.add(mData);
                if (i >= mDatas.size() - 1) {
                    mDatasss.addAll(mDatassss);
                    mDatass.addAll(mDatassss);
                    firstPageAdapter.notifyDataSetChanged();
                    mDatassss.clear();
                    pFirstPage.babyIsLoading = false;
                }
            }
        }
    }

    public void getShareInfo(String type, String id) {
        if (pFirstPage != null) pFirstPage.getShareInfo(type, id, channel_id);
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        if (firstPageAdapter != null) {
            firstPageAdapter.shareInfo(baseEntity);
        }
    }
}
