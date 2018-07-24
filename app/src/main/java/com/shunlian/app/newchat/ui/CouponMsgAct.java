package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.VoucherEntity;
import com.shunlian.app.presenter.AssignVoucherPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IAssignVoucherView;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/4.
 * 领取优惠券
 */

public class CouponMsgAct extends BaseActivity implements IAssignVoucherView {

    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_store_name)
    TextView tv_store_name;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.tv_plus)
    TextView tv_plus;

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_use)
    TextView tv_use;

    @BindView(R.id.miv_pick)
    MyImageView miv_pick;

    private AssignVoucherPresenter mPresenter;
    private String currentCouponId;

    public static void startAct(Context context, String couponId) {
        Intent intent = new Intent(context, CouponMsgAct.class);
        intent.putExtra("coupon_id", couponId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_coupon_msg;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentCouponId = getIntent().getStringExtra("coupon_id");
        mPresenter = new AssignVoucherPresenter(this, this);
        mPresenter.getVoucherDetai(currentCouponId);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getVoucherDetail(VoucherEntity voucherEntity) {
        GlideUtils.getInstance().loadCircleImage(this, miv_icon, voucherEntity.store_label);
        tv_store_name.setText(voucherEntity.store_name);
        tv_price.setText(String.valueOf(voucherEntity.denomination));
        tv_plus.setText(voucherEntity.use_condition);
        tv_date.setText("有效期:" + voucherEntity.limit_data);
    }
}
