package com.shunlian.app.newchat.ui;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddGoodsAdapter;
import com.shunlian.app.presenter.GoodsListPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.ui.myself_store.AddStoreGoodsAct;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SelectGoodsFragmet extends BaseLazyFragment {

    RecyclerView recy_view;
    NetAndEmptyInterface emptyInterface;

    public static SelectGoodsFragmet getInstance(String from) {
        SelectGoodsFragmet selectGoodsFragmet = new SelectGoodsFragmet();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        selectGoodsFragmet.setArguments(bundle);
        return selectGoodsFragmet;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_all_goods, container, false);
        recy_view = (RecyclerView) view.findViewById(R.id.recy_view);
        emptyInterface = (NetAndEmptyInterface) view.findViewById(R.id.nei_empty);
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        ((DefaultItemAnimator) recy_view.getItemAnimator()).setSupportsChangeAnimations(false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onFragmentFirstVisible() {

//        fromType = ((AddStoreGoodsAct) getActivity()).getCurrentFrom();
//
//        manager = new GridLayoutManager(baseActivity, 2);
//        recy_view.setLayoutManager(manager);
//        recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseContext, 12), true));
//
//        goodsList = new ArrayList<>();
//        mAdapter = new AddGoodsAdapter(baseActivity, true, true, goodsList, fromType);
//        recy_view.setAdapter(mAdapter);
//        currentFrom = getArguments().getString("from");
//        mPresenter = new GoodsListPresenter(baseActivity, this);
//        mPresenter.getValidGoods(currentFrom, true);
//
//        mAdapter.setOnItemClickListener(this);
        super.onFragmentFirstVisible();
    }
}
