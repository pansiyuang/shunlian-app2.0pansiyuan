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
    private LinearLayoutManager manager;


    public static void startAct(Context context,String voucher_id,String store_id){
        Intent intent = new Intent(context,CouponGoodsAct.class);
        intent.putExtra("store_id",store_id);
        intent.putExtra("voucher_id",voucher_id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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


    @Override
    protected void initListener() {
        super.initListener();
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null){
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount() && mPresenter != null){
                        mPresenter.onRefresh();
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

        gone(mrlayout_toolbar_more);
        mtv_toolbar_title.setText("优惠商品");

        String store_id = getIntent().getStringExtra("store_id");
        String voucher_id = getIntent().getStringExtra("voucher_id");

        mPresenter = new CouponGoodsPresenter(this,this,store_id,voucher_id);

        manager = new LinearLayoutManager(this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null){
            mPresenter.detachView();
            mPresenter = null;
        }
    }
}
