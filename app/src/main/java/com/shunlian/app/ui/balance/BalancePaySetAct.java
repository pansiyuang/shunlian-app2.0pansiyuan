package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;

import butterknife.BindView;

public class BalancePaySetAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mrlayout_xiugai)
    MyRelativeLayout mrlayout_xiugai;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.mrlayout_zhaohui)
    MyRelativeLayout mrlayout_zhaohui;

    @BindView(R.id.mrlayout_mimashezhi)
    MyRelativeLayout mrlayout_mimashezhi;

    private boolean isBack;


    public static void startAct(Context context, boolean isSet,boolean isBack) {
        Intent intent = new Intent(context, BalancePaySetAct.class);
        intent.putExtra("isSet", isSet);
        intent.putExtra("isBack", isBack);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_pay_set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_close:
                if (isBack)
                BalanceMainAct.startAct(this,true);
                finish();
                break;
            case R.id.mrlayout_xiugai:
                BalancePaySetTwoAct.startAct(this,"","modify","",false);
                break;
            case R.id.mrlayout_zhaohui:
                BalanceVerifyPhoneAct.startAct(this,false);
                break;
            case R.id.mrlayout_mimashezhi:
                BalancePaySetOneAct.startAct(this);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isBack)
            BalanceMainAct.startAct(this, true);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mrlayout_xiugai.setOnClickListener(this);
        mrlayout_zhaohui.setOnClickListener(this);
        mrlayout_mimashezhi.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBack=getIntent().getBooleanExtra("isBack", false);
        if (getIntent().getBooleanExtra("isSet", false)) {
            mrlayout_xiugai.setVisibility(View.VISIBLE);
            mrlayout_zhaohui.setVisibility(View.VISIBLE);
        } else {
            mrlayout_mimashezhi.setVisibility(View.VISIBLE);
        }
    }
}
