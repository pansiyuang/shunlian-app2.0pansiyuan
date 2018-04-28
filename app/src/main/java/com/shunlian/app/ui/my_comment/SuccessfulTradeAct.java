package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.presenter.SuccessfulTradePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.order.OrderDetailAct;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISuccessfulTradeView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/20.
 */

public class SuccessfulTradeAct extends BaseActivity implements ISuccessfulTradeView {

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private String order_id;
    private ArrayList<ReleaseCommentEntity> commentList;

    public static void startAct(Context context, ArrayList<ReleaseCommentEntity> entity, String id){
        Intent intent = new Intent(context, SuccessfulTradeAct.class);
        intent.putExtra("order_id",id);
        intent.putExtra("commentList",entity);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_successful_trade;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.pink_color);
        GridLayoutManager manager = new GridLayoutManager(this,2);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new GridSpacingItemDecoration
                (TransformUtil.dip2px(this, 5), false));

        order_id = getIntent().getStringExtra("order_id");
        commentList = (ArrayList<ReleaseCommentEntity>)
                getIntent().getSerializableExtra("commentList");
        SuccessfulTradePresenter presenter = new SuccessfulTradePresenter(this,this, order_id);
    }

    @OnClick(R.id.rl_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.order();
    }

    @OnClick(R.id.mtv_look_order)
    public void lookOrder(){
        OrderDetailAct.startAct(this,order_id);
    }

    @OnClick(R.id.mtv_comment)
    public void comment(){
        CreatCommentActivity.startAct(this,commentList,CreatCommentActivity.CREAT_COMMENT);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
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
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
    }
}
