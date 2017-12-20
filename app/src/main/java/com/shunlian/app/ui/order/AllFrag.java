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
import com.shunlian.app.ui.my_comment.SuccessfulTradeAct;
import com.shunlian.app.view.IOrderListView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 * 禁止fragment懒加载
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
    private int refreshPosition;//刷新位置

    public static AllFrag getInstance(int id) {
        AllFrag allFrag = new AllFrag();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
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
        return inflater.inflate(R.layout.frag_order_list, container, false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
        fetchNewData();
    }
    @Override
    public void refreshData() {
        fetchNewData();
    }

    public void fetchNewData() {
        adapter = null;
        recy_view.scrollToPosition(0);
        if (ordersLists != null) {
            ordersLists.clear();
        }
        if (mPresenter != null) {
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
        if (orders != null) {
            ordersLists.addAll(orders);
        }
        if (adapter == null) {
            adapter = new OrderListAdapter(baseActivity, true, ordersLists,this);
            recy_view.setAdapter(adapter);
            adapter.setPageLoading(page, allPage);
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    if (mPresenter != null) {
                        mPresenter.onRefresh();
                    }
                }
            });

            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    MyOrderEntity.Orders orders1 = ordersLists.get(position);
                    OrderDetailAct.startAct(baseActivity, orders1.id);
                }
            });

            adapter.setRefreshOrderListener(new OrderListAdapter.RefreshOrderListener() {
                @Override
                public void onRefreshOrder(int position) {
                    refreshPosition = position;
                }
            });
        } else {
            adapter.setPageLoading(page, allPage);
            adapter.notifyDataSetChanged();
        }
        emptyPage();
    }

    private void emptyPage() {
        if (isEmpty(ordersLists)) {
            empty();
        } else {
            recy_view.setVisibility(View.VISIBLE);
            nei_empty.setVisibility(View.GONE);
        }
    }

    /**
     * 通知刷新列表
     *
     * @param status
     */
    @Override
    public void notifRefreshList(int status) {
//        if (status == OrderListPresenter.CANCLE_ORDER){
//        }
        String id = ordersLists.get(refreshPosition).id;
        refreshOrder(id);
        if (status == OrderListPresenter.CONFIRM_RECEIPT){
            // TODO: 2017/12/20 确认收货界面 
            SuccessfulTradeAct.startAct(baseActivity);
        }
    }

    /**
     * 刷新订单
     *
     * @param orders
     */
    @Override
    public void refreshOrder(MyOrderEntity.Orders orders) {
        if (id == 0){//全部，更新条目
            ordersLists.remove(refreshPosition);
            ordersLists.add(refreshPosition,orders);
            if (adapter != null){
                adapter.notifyItemChanged(refreshPosition);
            }
        }else {//非全部，status状态一样就更新，不一样就删除
            String old_status = ordersLists.get(refreshPosition).status;
            String new_status = orders.status;
            if (old_status.equals(new_status)) {//更新
                ordersLists.remove(refreshPosition);
                ordersLists.add(refreshPosition, orders);
                if (adapter != null) {
                    adapter.notifyItemChanged(refreshPosition);
                }
            } else {//删除
                ordersLists.remove(refreshPosition);
                if (isEmpty(ordersLists)) {
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    if (adapter != null) {
                        adapter.notifyItemRemoved(refreshPosition);
                    }
                }
            }
        }
        emptyPage();
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (request_code == OrderListPresenter.LOAD_CODE) {
            if (adapter != null) {
                adapter.loadFailure();
            }
        } else if (request_code == OrderListPresenter.OTHER_CODE) {
            recy_view.setVisibility(View.GONE);
            nei_empty.setNetExecption().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPresenter != null) {
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
        if (request_code == OrderListPresenter.OTHER_CODE) {
            empty();
        }
    }

    private void empty() {
        recy_view.setVisibility(View.GONE);
        nei_empty.setVisibility(View.VISIBLE);
        nei_empty.setImageResource(R.mipmap.img_empty_dingdan)
                .setText(getString(R.string.no_order_info)).setButtonText("");
    }

    /**
     * 取消订单
     * @param order_id
     */
    public void cancleOrder(String order_id,int reason) {
        if (mPresenter != null){
            mPresenter.cancleOrder(order_id,reason);
        }
    }

    /**
     * 提醒发货
     * @param order_id
     */
    public void remindseller(String order_id) {
        if (mPresenter != null){
            mPresenter.remindseller(order_id);
        }
    }

    /**
     * 延长收货
     * @param order_id
     */
    public void postpone(String order_id) {
        if (mPresenter != null){
            mPresenter.postpone(order_id);
        }
    }

    /**
     * 刷新指定订单
     * @param order_id
     */
    public void refreshOrder(String order_id){
        if (mPresenter != null){
            mPresenter.refreshOrder(order_id);
        }
    }

    /**
     * 确认收货
     * @param order_id
     */
    public void confirmreceipt(String order_id){
        if (mPresenter != null){
            mPresenter.confirmreceipt(order_id);
        }
    }
}
