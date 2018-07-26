package com.shunlian.app.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.UserCouponListPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IUserCouponListView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/23.
 */

public class UserCouponListAct extends BaseActivity implements IUserCouponListView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private UserCouponListPresenter mPresenter;
    private String voucher_id;


    public static void startAct(Context context,String id){
        Intent intent = new Intent(context,UserCouponListAct.class);
        intent.putExtra("voucher_id",id);
        context.startActivity(intent);
    }


    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_user_couponlist;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.pink_color);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        voucher_id = getIntent().getStringExtra("voucher_id");

        mPresenter = new UserCouponListPresenter(this,this,voucher_id);

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        recy_view.setAdapter(adapter);
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
}
