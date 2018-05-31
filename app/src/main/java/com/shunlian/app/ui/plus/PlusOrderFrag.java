package com.shunlian.app.ui.plus;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PlusOrderAdapter;
import com.shunlian.app.bean.PlusOrderEntity;
import com.shunlian.app.presenter.PlusOrderPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IPlusOrderView;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/28.
 */

public class PlusOrderFrag extends BaseLazyFragment implements IPlusOrderView {

    RecyclerView recycler_list;
    SlRefreshView refreshview;

    public static PlusOrderFrag getInstance(String fromType) {
        PlusOrderFrag plusOrderFrag = new PlusOrderFrag();
        Bundle bundle = new Bundle();
        bundle.putString("from_type", fromType);
        plusOrderFrag.setArguments(bundle);
        return plusOrderFrag;
    }

    private PlusOrderPresenter mPresenter;
    private String fromType;
    private List<PlusOrderEntity.PlusOrder> orderList;
    private PlusOrderAdapter mAdapter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_refresh_list, container, false);
        refreshview = (SlRefreshView) view.findViewById(R.id.refreshview);
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        return view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onFragmentFirstVisible() {

        fromType = getArguments().getString("from_type");

        mPresenter = new PlusOrderPresenter(getActivity(), this);
        mPresenter.getOrderList(fromType, true);
        orderList = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);

        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        ((DefaultItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);

        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getOrderList(fromType, true);
            }

            @Override
            public void onLoadMore() {
            }
        });

        super.onFragmentFirstVisible();
    }

    @Override
    public void getPlusOrderList(int page, int totalPage, List<PlusOrderEntity.PlusOrder> plusOrderList) {
        refreshview.stopRefresh(true);

        if (page == 1) {
            orderList.clear();
        }

        if (!isEmpty(plusOrderList)) {
            orderList.addAll(plusOrderList);
        }

        if (mAdapter == null) {
            mAdapter = new PlusOrderAdapter(getActivity(), orderList);
            mAdapter.setOnItemClickListener((view, position) -> {
                PlusOrderEntity.PlusOrder plusOrder = orderList.get(position);
                PlusOrderDetailAct.startAct(getActivity(), plusOrder.product_order_id);
            });
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
