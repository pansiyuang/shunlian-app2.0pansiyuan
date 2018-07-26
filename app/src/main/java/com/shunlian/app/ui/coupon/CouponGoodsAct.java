package com.shunlian.app.ui.coupon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.presenter.CouponGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ICouponGoodsView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/7/24.
 */

public class CouponGoodsAct extends BaseActivity implements ICouponGoodsView{

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;
    private CouponGoodsPresenter mPresenter;


    public static void startAct(Context context,String store_id){
        Intent intent = new Intent(context,CouponGoodsAct.class);
        intent.putExtra("store_id",store_id);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon_goods;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        gone(mrlayout_toolbar_more);
        mtv_toolbar_title.setText("优惠商品");

        String store_id = getIntent().getStringExtra("store_id");


        mPresenter = new CouponGoodsPresenter(this,this,store_id,store_id);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

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
