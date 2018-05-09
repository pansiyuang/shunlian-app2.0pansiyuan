package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.presenter.PBalancePaySetOne;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IBalancePaySetOne;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class AlipayAddAct extends BaseActivity implements View.OnClickListener, IBalancePaySetOne {

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.et_shengfengzheng)
    EditText et_shengfengzheng;

    @BindView(R.id.et_zhifubao)
    EditText et_zhifubao;

    @BindView(R.id.et_yanzhengma)
    EditText et_yanzhengma;

    @BindView(R.id.mtv_timer)
    MyTextView mtv_timer;

    @BindView(R.id.mtv_next)
    MyTextView mtv_next;

    @BindView(R.id.miv_xieyi)
    MyImageView miv_xieyi;

    @BindView(R.id.mtv_xieyi)
    MyTextView mtv_xieyi;

    private CountDownTimer countDownTimer;
    private PBalancePaySetOne pBalancePaySetOne;
    private GradientDrawable background;
    private boolean isAgree = false;

    public static void startAct(Context context,boolean isAli) {
        Intent intent = new Intent(context, AlipayAddAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isAli",isAli);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_alipay_add;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_timer:
                pBalancePaySetOne.getCode();
                break;
            case R.id.mtv_next:
                String account_name=et_shengfengzheng.getText().toString();
                String account_number=et_zhifubao.getText().toString();
                String vcode=et_yanzhengma.getText().toString();
                if (TextUtils.isEmpty(account_name)) {
                    Common.staticToast(getStringResouce(R.string.balance_qingshurushengfengzheng));
                } else if (TextUtils.isEmpty(account_number)) {
                    Common.staticToast(getStringResouce(R.string.balance_qingshuruzhifubao));
                } else if (TextUtils.isEmpty(vcode)) {
                    Common.staticToast(getStringResouce(R.string.RegisterOneAct_qsryzm));
                } else {
                    pBalancePaySetOne.bindAliPay(vcode,account_name,account_number);
                }
                break;
            case R.id.miv_xieyi:
                if (isAgree) {
                    miv_xieyi.setImageResource(R.mipmap.img_balance_xieyi_n);
                    isAgree = false;
                    mtv_next.setClickable(false);
                    background.setColor(getColorResouce(R.color.color_value_6c));
                } else {
                    miv_xieyi.setImageResource(R.mipmap.img_balance_xieyi_h);
                    background.setColor(getColorResouce(R.color.pink_color));
                    isAgree = true;
                    mtv_next.setClickable(true);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_timer.setOnClickListener(this);
        mtv_next.setOnClickListener(this);
        miv_xieyi.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        SpannableStringBuilder titleBuilder = Common.changeColor(getStringResouce(R.string.balance_woyiyuedu)+getStringResouce(R.string.balance_yonghuxieyi),
                getStringResouce(R.string.balance_yonghuxieyi), getColorResouce(R.color.pink_color));
        mtv_xieyi.setText(titleBuilder);
        background = (GradientDrawable) mtv_next.getBackground();
        background.setColor(getColorResouce(R.color.color_value_6c));
        mtv_next.setClickable(false);
        mtv_phone.setText(Constant.MOBILE);
        pBalancePaySetOne = new PBalancePaySetOne(this, this);
    }

    @Override
    public void getCodeCall() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_timer.setText("(" + (int) Math.floor(l / 1000) + "s)");
                    mtv_timer.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    mtv_timer.setText(getResources().getString(R.string.LoginPswFrg_cxhq));
                    mtv_timer.setEnabled(true);
                }
            };
        }
        countDownTimer.start();
    }

    @Override
    public void nextCall(String key) {
    }

    @Override
    public void bindAlipayCall(String account_number) {
        if (getIntent().getBooleanExtra("isAli",false)){
            AlipayMyAct.startAct(this,true,true,true,account_number);
        }else {
            BalanceTXAct.startAct(this);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
