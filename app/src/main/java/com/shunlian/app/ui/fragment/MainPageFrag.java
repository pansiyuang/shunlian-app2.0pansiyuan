package com.shunlian.app.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.presenter.TestPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.collection.MyCollectionAct;
import com.shunlian.app.ui.confirm_order.OrderLogisticsActivity;
import com.shunlian.app.ui.confirm_order.SearchOrderActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.ui.my_comment.MyCommentAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.ui.returns_order.SelectServiceActivity;
import com.shunlian.app.ui.returns_order.SubmitLogisticsInfoAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.refresh.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 首页页面
 */

public class MainPageFrag extends BaseFragment {

    @BindView(R.id.ll_special)
    MyRelativeLayout ll_special;

    @BindView(R.id.special_miaosha)
    MyImageView special_miaosha;

    @BindView(R.id.special_qingliang)
    MyImageView special_qingliang;

    @BindView(R.id.special_man)
    MyImageView special_man;

    @BindView(R.id.special_woman)
    MyImageView special_woman;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.ll_layout)
    PullToRefreshView ll_layout;

    private View rootView;


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_main, container, false);
        return rootView;
    }

    @Override
    protected void initData() {
        TestPresenter testPresenter = new TestPresenter(baseContext, null);

//        ll_special.setWHProportion(720,414);
//        special_miaosha.setWHProportion(298,414);
//        special_qingliang.setWHProportion(422,207);
//        special_man.setWHProportion(211,207);
//        special_woman.setWHProportion(211,207);

//        List<String> items = DataUtil.getListString(40, "条目");
        List<String> items = new ArrayList<>();
        items.add("登录");
        items.add("商品详情");
        items.add("店铺");
        items.add("店铺57");
        items.add("店铺58");
        items.add("我的评价");
        items.add("我的订单");
        items.add("订单物流详情");
        items.add("订单搜索历史");
        items.add("选择服务类型");
        items.add("H5");
        items.add("天天特惠");
        items.add("退换/售后");
        items.add("提交物流信息");
        items.add("列表排序");
        items.add("收藏");

        items.addAll(DataUtil.getListString(40, "条目"));


        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>(baseContext, android.R.layout.simple_list_item_1, items) {

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {
                holder.addOnClickListener(android.R.id.text1);
                holder.setText(android.R.id.text1, s);
            }
        };

        LinearLayoutManager manager = new LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false);
        recy_view.setLayoutManager(manager);
        recy_view.setAdapter(simpleRecyclerAdapter);
        simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        LoginAct.startAct(baseContext);
                        break;
                    case 1:
                        GoodsDetailAct.startAct(baseContext, "56");
//                        GoodsDetailAct.startAct(baseContext,"134");
                        break;
                    case 2:
                        StoreAct.startAct(baseContext, "26");
                        break;
                    case 3:
                        StoreAct.startAct(baseContext, "57");
                        break;
                    case 4:
                        StoreAct.startAct(baseContext, "58");
                        break;
                    case 5:
                        MyCommentAct.startAct(baseActivity);
                        break;
                    case 6:
                        MyOrderAct.startAct(baseActivity,0);
                        break;
                    case 7:
                        OrderLogisticsActivity.startAct(baseActivity, "5");
//                        OrderLogisticsActivity.startAct(baseActivity, "4");
                        break;
                    case 8:
                        SearchOrderActivity.startAct(baseActivity);
                        break;
                    case 9:
                        SelectServiceActivity.startAct(baseActivity, "404");
                        break;
                    case 10:
//                        String url = "https://pro.m.jd.com/mall/active/2PimE38Vam99eMLJWXiLTx1VgLJs/index.html";
                        String url = "https://h5.api.shunliandongli.com/v1/detail/389517.html";
                        H5Act.startActivity(baseActivity, "hhahh", url, H5Act.MODE_SONIC);
                        break;
                    case 11:
                        startActivity(new Intent(baseActivity,DayDayAct.class));
                        break;
                    case 12:
                        RefundAfterSaleAct.startAct(baseActivity);
                        break;
                    case 13:
                        SubmitLogisticsInfoAct.startAct(baseActivity,"155",SubmitLogisticsInfoAct.APPLY);
                        break;
                    case 14:
                        CategoryAct.startAct(baseActivity,null);
                        break;
                    case 15:
                        MyCollectionAct.startAct(baseActivity);
                        break;
                }
            }
        });

        ll_layout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ll_layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll_layout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

    }
}
