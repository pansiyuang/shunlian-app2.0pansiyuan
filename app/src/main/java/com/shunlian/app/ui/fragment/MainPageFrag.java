package com.shunlian.app.ui.fragment;

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
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
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
 *
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
        items.add("购物车");
        items.add("确认订单");
        items.add("选择收获地址");
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
                        StoreAct.startAct(baseContext);
                        break;
                    case 4:
                        break;
                    case 5:
                        AddressListActivity.startAct(baseContext,"");
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
