package com.shunlian.app.newchat.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CouponGoodsAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.VoucherEntity;
import com.shunlian.app.presenter.AssignVoucherPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IAssignVoucherView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

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

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.miv_pick)
    MyImageView miv_pick;

    @BindView(R.id.rv_coupon)
    RecyclerView rv_coupon;

    @BindView(R.id.view_dash_line)
    View view_dash_line;

    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private AssignVoucherPresenter mPresenter;
    private String currentCouponId, couponId;
    private boolean canGet = false;
    private CouponGoodsAdapter couponGoodsAdapter;
    private VoucherEntity voucherEntity;

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
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.miv_pick:
                if (canGet && !isEmpty(couponId)) {
                    mPresenter.getVoucher(couponId);
                } else  if (voucherEntity!=null){
                    Common.goGoGo(this,voucherEntity.jump_type,voucherEntity.lazy_id);
                }
                break;
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        view_dash_line.setVisibility(View.VISIBLE);
        rl_more.setVisibility(View.GONE);
        mtv_title.setText(getStringResouce(R.string.chat_lingquyouhui));
        currentCouponId = getIntent().getStringExtra("coupon_id");
        mPresenter = new AssignVoucherPresenter(this, this, currentCouponId);
        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText(getString(R.string.first_shangping))
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_pick.setOnClickListener(this);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom){
                    if (mPresenter != null) {
                        mPresenter.refreshBaby();
                    }
                }
            }
        });
//        rv_coupon.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (gridLayoutManager != null) {
//                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
//                        if (mPresenter != null) {
//                            mPresenter.refreshBaby();
//                        }
//                    }
//                }
//            }
//        });

    }

    @Override
    public void showFailureView(int request_code) {
        visible(nei_empty);
        gone(rv_coupon);
    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nei_empty);
        gone(rv_coupon);
    }


    @Override
    public void getVoucherDetail(VoucherEntity voucherEntity, List<VoucherEntity.Goods> goods) {
        if (isEmpty(goods)) {
            visible(nei_empty);
            gone(rv_coupon);
        }
        if (couponGoodsAdapter == null) {
            this.voucherEntity=voucherEntity;
            visible(rv_coupon);
            gone(nei_empty);
            GlideUtils.getInstance().loadCircleImage(this, miv_icon, voucherEntity.store_label);
            tv_store_name.setText(voucherEntity.store_name);
            tv_price.setText(Common.changeTextSize("￥" + voucherEntity.denomination, "￥", 14));
            tv_plus.setText(voucherEntity.use_condition);
            tv_date.setText("有效期:" + voucherEntity.limit_data);
            tv_use.setText(voucherEntity.desc);
            couponId = voucherEntity.id;
            canGet = "0".equals(voucherEntity.is_get);
            if (!canGet)
                miv_pick.setImageResource(R.mipmap.img_quan_hui);
            if ("1".equals(voucherEntity.is_store))
                ll_bg.setBackgroundResource(R.mipmap.img_pingtai);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            rv_coupon.setLayoutManager(gridLayoutManager);
            couponGoodsAdapter = new CouponGoodsAdapter(this, true, goods);
            rv_coupon.setAdapter(couponGoodsAdapter);
            rv_coupon.setNestedScrollingEnabled(false);
            couponGoodsAdapter.setOnItemClickListener((view, position) ->
                    GoodsDetailAct.startAct(getBaseContext(), voucherEntity.goods_list.get(position).goods_id));
        } else {
            couponGoodsAdapter.notifyDataSetChanged();
        }
        couponGoodsAdapter.setPageLoading(Integer.parseInt(voucherEntity.pager.page), Integer.parseInt(voucherEntity.pager.total_page));

    }

    @Override
    public void refreshVoucherState(GoodsDeatilEntity.Voucher voucher) {
        if ("1".equals(voucher.is_get)) {
            miv_pick.setImageResource(R.mipmap.img_quan_hui);
            canGet = false;
        }
    }
}
