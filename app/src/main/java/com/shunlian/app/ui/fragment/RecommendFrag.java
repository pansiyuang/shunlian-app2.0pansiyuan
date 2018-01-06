package com.shunlian.app.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.RecommmendAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.JoinGoodsEntity;
import com.shunlian.app.presenter.RecommmendPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.confirm_order.MegerOrderActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRecommmendView;

import java.util.List;

import butterknife.BindView;

public class RecommendFrag extends BaseFragment implements View.OnClickListener, IRecommmendView, BaseRecyclerAdapter.OnItemClickListener, RecommmendAdapter.OnGoodsBuyOnclickListener {

    @BindView(R.id.recycler_recommmend)
    RecyclerView recycler_recommmend;

    @BindView(R.id.tv_meger_tag)
    TextView tv_meger_tag;

    @BindView(R.id.tv_meger_pro)
    TextView tv_meger_pro;

    private RecommmendPresenter recommmendPresenter;
    private String currentCateId;
    private String mJoinSign;
    private List<GoodsDeatilEntity.Goods> goodsList;
    private RecommmendAdapter adapter;

    public static BaseFragment getInstance(String joinSign, String cateId) {
        RecommendFrag fragment = new RecommendFrag();

        Bundle args = new Bundle();
        args.putString("joinSign", joinSign);
        args.putString("cateId", cateId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_recommmend, container, false);
        return view;
    }

    @Override
    protected void initData() {
        currentCateId = getArguments().getString("cateId");
        mJoinSign = getArguments().getString("joinSign");
        recommmendPresenter = new RecommmendPresenter(baseContext, this);
        recommmendPresenter.getRecommmendGoods(mJoinSign, currentCateId);
        recycler_recommmend.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseContext, 5f), false));
    }

    @Override
    public void getJoinGoods(JoinGoodsEntity joinGoodsEntity) {
        goodsList = joinGoodsEntity.join_goods;
        if (goodsList == null || goodsList.size() == 0) {
            return;
        }
        adapter = new RecommmendAdapter(baseContext, false, goodsList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(baseContext, 2);
        recycler_recommmend.setNestedScrollingEnabled(false);
        recycler_recommmend.setLayoutManager(gridLayoutManager);
        recycler_recommmend.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        adapter.setOnGoodsBuyOnclickListener(this);

        tv_meger_tag.setText(joinGoodsEntity.label);
        tv_meger_pro.setText(joinGoodsEntity.title);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        String goodsId = goodsList.get(position).goods_id;
        GoodsDetailAct.startAct(baseContext, goodsId);
    }

    @Override
    public void OnItemBuy(GoodsDeatilEntity.Goods goods) {
        if (baseContext instanceof MegerOrderActivity) {
            MegerOrderActivity megerOrderActivity = (MegerOrderActivity) baseContext;
            megerOrderActivity.getGoodsInfo(goods);
        }
    }
}