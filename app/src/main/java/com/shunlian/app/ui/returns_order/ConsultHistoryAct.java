package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ConsultHistoryAdapter;
import com.shunlian.app.bean.ConsultHistoryEntity;
import com.shunlian.app.presenter.ConsultHistoryPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IConsultHistoryView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ConsultHistoryAct extends BaseActivity implements IConsultHistoryView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private ConsultHistoryPresenter presenter;


    public static void startAct(Context context,String refund_id){
        Intent intent = new Intent(context, ConsultHistoryAct.class);
        intent.putExtra("refund_id",refund_id);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_consult_history;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        String refund_id = getIntent().getStringExtra("refund_id");
        presenter = new ConsultHistoryPresenter(this,this,refund_id);
        LinearLayoutManager manager = new LinearLayoutManager(this);
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
    public void consultHistory(ConsultHistoryEntity entity) {
        ConsultHistoryAdapter adapter = new ConsultHistoryAdapter(this,
                false, entity.history_list);
        recy_view.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }
}
