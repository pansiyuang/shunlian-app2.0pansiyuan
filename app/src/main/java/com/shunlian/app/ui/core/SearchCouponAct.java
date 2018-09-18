package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CouponsAdapter;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.view.IGetCoupon;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class SearchCouponAct extends BaseActivity implements View.OnClickListener, IGetCoupon {

    @BindView(R.id.tv_search_cancel)
    MyTextView tv_search_cancel;

    @BindView(R.id.edt_goods_search)
    EditText edt_goods_search;

    @BindView(R.id.rv_search)
    RecyclerView rv_search;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;


    private PGetCoupon pGetCoupon;
    private CouponsAdapter couponsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String key;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchCouponAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_search_cancel:
                finish();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_search_coupon;
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pGetCoupon != null) {
                            pGetCoupon.refreshBaby("", key);
                        }
                    }
                }
            }
        });
        tv_search_cancel.setOnClickListener(this);
        edt_goods_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    key = edt_goods_search.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        Common.staticToast("请先输入关键字...");
                    } else {
                        pGetCoupon.resetBaby("", key);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        nei_empty.setImageResource(R.mipmap.img_empty_youhuiquan).setText(getString(R.string.first_dianpuyouhui));
        nei_empty.setButtonText(null);
        pGetCoupon = new PGetCoupon(this, this);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nei_empty);
        gone(rv_search);
    }

    @Override
    public void setpingData(VouchercenterplEntity vouchercenterplEntity) {

    }

    @Override
    public void setdianData(List<VouchercenterplEntity.MData> mData, String page, String total) {
        if (mData == null || mData.size() <= 0) {
            visible(nei_empty);
            gone(rv_search);
        } else {
            gone(nei_empty);
            visible(rv_search);
        }
        if (couponsAdapter == null) {
            couponsAdapter = new CouponsAdapter(baseAct, true, mData, pGetCoupon);
            linearLayoutManager = new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false);
            rv_search.setLayoutManager(linearLayoutManager);
            rv_search.setAdapter(couponsAdapter);
            rv_search.addItemDecoration(new MVerticalItemDecoration(baseAct, 10, 10, 0));
        } else {
            couponsAdapter.notifyDataSetChanged();
        }
        couponsAdapter.setPageLoading(Integer.parseInt(page), Integer.parseInt(total));
    }

    @Override
    public void getCouponCallBack(boolean isCommon, int position, String isGet) {
        if ("1".equals(isGet)){
            pGetCoupon.mDatas.get(position).if_get = "1";
            couponsAdapter.notifyItemChanged(position);
        }
    }

}
