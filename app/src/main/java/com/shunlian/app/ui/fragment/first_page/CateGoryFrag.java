package com.shunlian.app.ui.fragment.first_page;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.FirstPageAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.PFirstPage;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IFirstPage;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader1;
import com.shunlian.app.widget.nestedrefresh.interf.onRefreshListener;
import com.shunlian.app.yjfk.ComplaintActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class CateGoryFrag extends BaseFragment implements IFirstPage {
    private int first = 0;
    public PFirstPage pFirstPage;
    public String cate_id, sort_type, cate_name;
    public List<GetDataEntity.MData> mDatass = new ArrayList<>();
    public List<GetDataEntity.MData> mDatasss = new ArrayList<>();
    public List<GetDataEntity.MData> mDatassss = new ArrayList<>();
    @BindView(R.id.rv_view)
    public RecyclerView rv_view;
    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;
    //    @BindView(R.id.mtv_empty)
//    MyTextView mtv_empty;
    private String channel_id, chinnel_name;
    private FirstPageAdapter firstPageAdapter;
    private GridLayoutManager gridLayoutManager;
    private boolean isFirst = false, isRefresh = false/*, isShow = true*/;
    private View rootView;
    private boolean isUp = true, isStop = false,isShow=false;
    public   NestedSlHeader1 header;
//    public MyLinearLayout mllayout_title;


    public static BaseFragment getInstance(String channel_id, String chinnel_name) {
        CateGoryFrag fragment = new CateGoryFrag();

        Bundle args = new Bundle();
        args.putSerializable("channel_id", channel_id);
        args.putString("chinnel_name", chinnel_name);
        fragment.setArguments(args);
        return fragment;
    }
//    public void setmllayout_title(MyLinearLayout mllayout_title){
//        this.mllayout_title=mllayout_title;
//    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_category, container, false);
        return rootView;
    }

    public void refresh() {
        isRefresh = true;
        mDatasss.clear();
//        pFirstPage.getContentData(channel_id, isShow);
        pFirstPage.getContentData(channel_id);
//        isShow = true;
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
//                isShow = false;
                if (header.isgoSecond){
                    ComplaintActivity.startAct(getContext());
                    refresh();
                }else {
                    refresh();
                }
            }
        });

        rv_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isShow){
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            isUp = false;
                            LogUtil.augusLogW("ppppd--ACTION_DOWN");
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isUp = false;
                            LogUtil.augusLogW("ppppd--ACTION_MOVE22");
                            break;
                        case MotionEvent.ACTION_UP:
                            isUp = true;
                            LogUtil.augusLogW("ppppd--ACTION_UP3333");
                            break;
                    }
                    judge();
                }
                return false;
            }
        });

        rv_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy != 0) {
                    if (FirstPageFrag.miv_entrys.getVisibility() == View.VISIBLE) {
                        int value = TransformUtil.dip2px(baseActivity, 72);
                        int values = TransformUtil.dip2px(baseActivity, 100);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) FirstPageFrag.miv_entrys.getLayoutParams();
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        if (dy > 0) {
                            layoutParams.setMargins(0, values, -value / 2, 0);
                            FirstPageFrag.isHides = true;
                        } else {
                            layoutParams.setMargins(0, values, 0, 0);
                            FirstPageFrag.isHides = false;
                        }
                        FirstPageFrag.miv_entrys.setLayoutParams(layoutParams);
                    }
                    if (FirstPageFrag.miv_entry.getVisibility() == View.VISIBLE) {
                        int value = TransformUtil.dip2px(baseActivity, 80);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(value, value);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        if (dy > 0) {
                            layoutParams.setMargins(0, 0, -value / 2, value*5/4);
                            FirstPageFrag.isHide = true;
                        } else {
                            layoutParams.setMargins(0, 0, 0, value*5/4);
                            FirstPageFrag.isHide = false;
                        }
                        FirstPageFrag.miv_entry.setLayoutParams(layoutParams);
                    } else if (FirstPageFrag.show_new_user_view.getVisibility() == View.VISIBLE) {
                        int valuew = TransformUtil.dip2px(baseActivity, 104);
                        int valueh = TransformUtil.dip2px(baseActivity, 112);
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(valuew, valueh);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        if (dy > 0) {
                            layoutParams.setMargins(0, 0, -valuew / 2, TransformUtil.dip2px(baseActivity, 60));
                            FirstPageFrag.isNewUserHide = true;
                        } else {
                            layoutParams.setMargins(0, 0, 0, TransformUtil.dip2px(baseActivity, 60));
                            FirstPageFrag.isNewUserHide = false;
                        }
                        FirstPageFrag.show_new_user_view.setLayoutParams(layoutParams);

                    }
                }
//                if (!FirstPageFrag.isExpand && 0 >= getScollYDistance()){
//                    FirstPageFrag.mAppbar.setExpanded(true);
//                }

                if (gridLayoutManager != null) {
                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition>first+5){
                        isShow=true;
                        FirstPageFrag.ntv_top.setText(lastPosition-first + "");
                        isStop = (1 >= (dy > 0 ? dy : -dy)||rv_view.getScrollState()==0);
                        judge();
                    }else {
                        isShow=false;
                        FirstPageFrag.mllayout_arrow.setVisibility(View.GONE);
                        FirstPageFrag.miv_arrow.setVisibility(View.GONE);
                    }
                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                        if (pFirstPage != null) {
                            pFirstPage.refreshBaby(cate_id, sort_type);
                        }
                    }
                }
//                if (dy > 80) {
//                    FirstPageFrag.mllayout_title.setAlpha(1);
//                } else if (dy > 0) {
//                    float alpha = ((float) dy) / 80;
//                    FirstPageFrag.mllayout_title.setAlpha(alpha);
//                } else {
//                    FirstPageFrag.mllayout_title.setAlpha(0);
//                }
            }
        });
    }

    public void judge() {
        if (isStop && isUp||!isFirst) {
            FirstPageFrag.mllayout_arrow.setVisibility(View.GONE);
            FirstPageFrag.miv_arrow.setVisibility(View.VISIBLE);
        } else {
            FirstPageFrag.mllayout_arrow.setVisibility(View.VISIBLE);
            FirstPageFrag.miv_arrow.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        //新增下拉刷新
         header = new NestedSlHeader1(getContext());
        lay_refresh.setRefreshHeaderView(header);
        //end
        if (getArguments() != null)
            channel_id = (String) getArguments().getSerializable("channel_id");
        chinnel_name = getArguments().getString("chinnel_name");
        if (FirstPageFrag.firstId.equals(channel_id)) {
            isFirst = true;
        } else {
            isFirst = false;
        }
//        channel_id = "15";
        pFirstPage = new PFirstPage(baseActivity, this, this);
//        pFirstPage.getContentData(channel_id, isShow);
        if (isFirst && Constant.getDataEntity != null)
            setData(Constant.getDataEntity);
        pFirstPage.getContentData(channel_id);
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

    public void setData(GetDataEntity getDataEntity) {
        //        if (getDataEntity.datas!=null&&getDataEntity.datas.size()>0){
////            mtv_empty.setVisibility(View.GONE);
////            rv_view.setVisibility(View.VISIBLE);
//            mtv_empty.setVisibility(View.VISIBLE);
//            rv_view.setVisibility(View.GONE);
        if (rv_view == null)
            return;
        if (getDataEntity != null && getDataEntity.input_keyword != null) {
            FirstPageFrag.keyWord = getDataEntity.input_keyword;
            if (!isEmpty(getDataEntity.input_keyword.keyword))
                FirstPageFrag.mtv_search.setText(getDataEntity.default_keyword);
        }
        if (lay_refresh != null)
            lay_refresh.setRefreshing(false);
        isRefresh = false;
        int size = 0;
        mDatass.clear();
        if (getDataEntity != null) {
            size = getDataEntity.datas.size();
            mDatass.addAll(getDataEntity.datas);
        }
//        if (firstPageAdapter==null){
        firstPageAdapter = new FirstPageAdapter(baseActivity, true, mDatass, isFirst, this, size, chinnel_name);
        gridLayoutManager = new GridLayoutManager(baseActivity, 2);

        if (rv_view == null)
            rv_view = (RecyclerView) rootView.findViewById(R.id.rv_view);
        rv_view.setLayoutManager(gridLayoutManager);
        rv_view.setAdapter(firstPageAdapter);

        if (!isEmpty(mDatass) && !isEmpty(mDatass.get(mDatass.size() - 1).cates)) {
            cate_id = mDatass.get(mDatass.size() - 1).cates.get(0).id;
            sort_type = mDatass.get(mDatass.size() - 1).cates.get(0).sort_type;
            cate_name = mDatass.get(mDatass.size() - 1).cates.get(0).name;
            pFirstPage.resetBaby(cate_id, sort_type);
        }

        if (isFirst) {
            FirstPageFrag.ntv_bottom.setText(getDataEntity.floor_total);
//            FirstPageFrag.ntv_bottom.setText("100");
            try {
                first=Integer.parseInt(getDataEntity.first_floor);
//                first=Integer.parseInt("8");
            }catch (Exception e){
                first=0;
            }
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
    public void setContent(GetDataEntity getDataEntity) {
        if (!isFirst || Constant.getDataEntity == null || isRefresh)
            setData(getDataEntity);
        if (isFirst)
            Constant.getDataEntity = getDataEntity;
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

    @Override
    public void setBubble(BubbleEntity data) {

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
