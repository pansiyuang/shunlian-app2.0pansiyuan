package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.RefundListPresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IRefundListView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundAfterSaleAct extends BaseActivity implements IRefundListView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private RefundListPresent refundListPresent;
    private LinearLayoutManager manager;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,RefundAfterSaleAct.class));
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_refund_after_sale;
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
                        if (refundListPresent != null){
                            refundListPresent.onRefresh();
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
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        refundListPresent = new RefundListPresent(this,this);

        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.afterSale();
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        nei_empty.setNetExecption().setOnClickListener(v ->  {
            if (refundListPresent != null) {
                refundListPresent.onRefresh();
            }
        });
    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {
        if (request_code == 100){
            gone(recy_view);
            visible(nei_empty);
            nei_empty.setImageResource(R.mipmap.img_empty_dingdan)
                    .setText(getString(R.string.no_order_info)).setButtonText("");
        }else {
            visible(recy_view);
            gone(nei_empty);
        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }
}
