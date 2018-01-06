package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.OrderListAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.presenter.OrderListPresenter;
import com.shunlian.app.presenter.SearchOrderResultPresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.my_comment.SuccessfulTradeAct;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.view.ISearchResultView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SearchOrderResultActivity extends BaseActivity implements ISearchResultView {

    @BindView(R.id.tv_order_search)
    TextView tv_order_search;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_title_number)
    TextView tv_title_number;

    @BindView(R.id.recycler_result)
    RecyclerView recy_view;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private SearchOrderResultPresent mPresenter;
    private String currentKeyword;
    private OrderListAdapter adapter;
    private LinearLayoutManager linearLayoutManage;
    private List<MyOrderEntity.Orders> ordersLists = new ArrayList<>();
    private int refreshPosition;//刷新位置

    public static void startAct(Context context, String keyword) {
        Intent intent = new Intent(context, SearchOrderResultActivity.class);
        intent.putExtra("keyword", keyword);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_order_result;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        currentKeyword = getIntent().getStringExtra("keyword");
        mPresenter = new SearchOrderResultPresent(this, this,currentKeyword);

        linearLayoutManage = new LinearLayoutManager(this);
        recy_view.setLayoutManager(linearLayoutManage);
        recy_view.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManage != null) {
                    int lastPosition = linearLayoutManage.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManage.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void getSearchResult(List<MyOrderEntity.Orders> orders,int page,int allPage) {
        if (orders != null) {
            ordersLists.addAll(orders);
        }
        if (adapter == null) {
            adapter = new OrderListAdapter(this, true, ordersLists,this);
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
                    OrderDetailAct.startAct(SearchOrderResultActivity.this, orders1.id);
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

    private void empty() {
        recy_view.setVisibility(View.GONE);
        nei_empty.setVisibility(View.VISIBLE);
        nei_empty.setImageResource(R.mipmap.img_empty_dingdan)
                .setText(getString(R.string.no_order_info)).setButtonText("");
    }
    /**
     * 通知刷新列表
     *
     * @param status
     */
    @Override
    public void notifRefreshList(int status) {
        String id = ordersLists.get(refreshPosition).id;
        refreshOrder(id);
        if (status == OrderListPresenter.CONFIRM_RECEIPT){
            // TODO: 2017/12/20 确认收货界面
            SuccessfulTradeAct.startAct(this);
        }
    }

    /**
     * 刷新订单
     *
     * @param orders
     */
    @Override
    public void refreshOrder(MyOrderEntity.Orders orders) {
        ordersLists.remove(refreshPosition);
        ordersLists.add(refreshPosition,orders);
        if (adapter != null){
            adapter.notifyItemChanged(refreshPosition);
        }
        emptyPage();
    }

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
                        mPresenter.searchOrder();
                    }
                }
            });
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {
        if (request_code == OrderListPresenter.OTHER_CODE) {
            empty();
        }
    }

    @OnClick(R.id.tv_order_search)
    public void orderSearch(){
        SearchOrderActivity.startAct(SearchOrderResultActivity.this);
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