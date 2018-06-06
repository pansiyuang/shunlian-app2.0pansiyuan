package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class AlipayMyAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mtv_account)
    MyTextView mtv_account;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.mtv_tianjiazhi)
    MyTextView mtv_tianjiazhi;

    @BindView(R.id.mrlayout_zhifubao)
    MyRelativeLayout mrlayout_zhifubao;

    @BindView(R.id.mllayout_add)
    MyLinearLayout mllayout_add;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private boolean isBack,needPaySet;

    public static void startAct(Context context,boolean isHave, boolean needPaySet, boolean isBack, String account_number) {
        Intent intent = new Intent(context, AlipayMyAct.class);
        intent.putExtra("needPaySet", needPaySet);//是否需要设置支付密码
        intent.putExtra("isHave", isHave);
        intent.putExtra("isBack", isBack);
        intent.putExtra("account_number", account_number);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_alipay_my;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isBack)
            BalanceMainAct.startAct(this,true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_close:
                if (isBack)
                    BalanceMainAct.startAct(this, true);
                finish();
                break;
            case R.id.mrlayout_zhifubao:
                AlipayDetailAct.startAct(this);
                break;
            case R.id.mtv_tianjiazhi:
                if (needPaySet){
                    BalancePaySetOneAct.startAct(this,false,true);
//                    BalanceVerifyPhoneAct.startAct(this,true,true,false);
                }else {
                    BalancePaySetTwoAct.startAct(this, "", "bindPay", "",false,true);
                }
                break;
        }
    }


    @Override
    protected void initListener() {
        super.initListener();
        mrlayout_zhifubao.setOnClickListener(this);
        mtv_tianjiazhi.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBack=getIntent().getBooleanExtra("isBack", false);
        needPaySet=getIntent().getBooleanExtra("needPaySet", false);
        if (getIntent().getBooleanExtra("isHave", false)) {
            mrlayout_zhifubao.setVisibility(View.VISIBLE);
            mtv_desc.setVisibility(View.VISIBLE);
            mtv_account.setText(String.format(getStringResouce(R.string.balance_zhifubao),
                    getIntent().getStringExtra("account_number")));
        } else {
            mllayout_add.setVisibility(View.VISIBLE);
        }
    }
}
