package com.shunlian.app.ui.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.OrderListAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.presenter.OrderListPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IOrderListView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AllFrag extends BaseLazyFragment implements IOrderListView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    private OrderListAdapter adapter;
    private List<MyOrderEntity.Orders> ordersLists = new ArrayList<>();
    private LinearLayoutManager manager;
    private OrderListPresenter mPresenter;
    private int id;

    public static AllFrag getInstance(int id){
        AllFrag allFrag = new AllFrag();
        Bundle bundle = new Bundle();
        bundle.putInt("id",id);
        allFrag.setArguments(bundle);
        return allFrag;
    }

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_order_list,container,false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()){
                        if (mPresenter != null){
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        manager = new LinearLayoutManager(baseActivity);
        recy_view.setLayoutManager(manager);
        id = getArguments().getInt("id");
        mPresenter = new OrderListPresenter(baseActivity, this);
    }

    @Override
    public void fetchData() {
        adapter = null;
        recy_view.scrollToPosition(0);
        if (ordersLists != null){
            ordersLists.clear();
        }
        if (mPresenter != null){
            mPresenter.detachView();
        }
        requestData(id);
    }

    public void requestData(int position) {
        mPresenter.detachView();
        switch (position) {
            case 0:
                mPresenter.orderListAll();
                break;
            case 1:
                mPresenter.orderListPay();
                break;
            case 2:
                mPresenter.orderListSend();
                break;
            case 3:
                mPresenter.orderListReceive();
                break;
            case 4:
                mPresenter.orderListComment();
                break;
        }
    }

    /**
     * 订单列表
     *
     * @param orders
     * @param page
     * @param allPage
     */
    @Override
    public void orderList(List<MyOrderEntity.Orders> orders, int page, int allPage) {
        if (orders != null){
            ordersLists.addAll(orders);
        }
        if (adapter == null) {
            adapter = new OrderListAdapter(baseActivity, true, ordersLists);
            recy_view.setAdapter(adapter);
            adapter.setPageLoading(page,allPage);
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (mPresenter != null){
                        mPresenter.onRefresh();
                    }
                }
            });
        }else {
            adapter.setPageLoading(page,allPage);
            adapter.notifyDataSetChanged();
        }
        if (isEmpty(ordersLists)){
            empty();
        }else {
            recy_view.setVisibility(View.VISIBLE);
            nei_empty.setVisibility(View.GONE);
        }
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == OrderListPresenter.LOAD_CODE){
            if (adapter != null){
                adapter.loadFailure();
            }
        }else if (request_code == OrderListPresenter.OTHER_CODE){
            recy_view.setVisibility(View.GONE);
            nei_empty.setNetExecption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null){
                        requestData(id);
                    }
                }
            });
        }

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {
        if (request_code == OrderListPresenter.OTHER_CODE){
            empty();
        }
    }

    private void empty(){
        recy_view.setVisibility(View.GONE);
        nei_empty.setVisibility(View.VISIBLE);
        nei_empty.setImageResource(R.mipmap.img_empty_dingdan)
                .setText("暂无订单信息").setButtonText("");
    }
}
