package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.RefundAfterSaleAdapter;
import com.shunlian.app.bean.RefundListEntity;
import com.shunlian.app.presenter.RefundListPresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IRefundListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundAfterSaleAct extends BaseActivity implements IRefundListView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private RefundListPresent refundListPresent;
    private LinearLayoutManager manager;
    private RefundAfterSaleAdapter afterSaleAdapter;
    private List<RefundListEntity.RefundList> refundLists = new ArrayList<>();


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

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void refundList(List<RefundListEntity.RefundList> refundLists, int page, int allPage) {
        if (isEmpty(refundLists)){
            this.refundLists.addAll(refundLists);
        }
        if (afterSaleAdapter == null) {
            afterSaleAdapter = new RefundAfterSaleAdapter(this, true, refundLists);
            recy_view.setAdapter(afterSaleAdapter);
            afterSaleAdapter.setPageLoading(page,allPage);
        }else {
            afterSaleAdapter.notifyDataSetChanged();
            afterSaleAdapter.setPageLoading(page,allPage);
        }
    }
}
