package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.presenter.PBalancePaySetOne;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IBalancePaySetOne;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class BalanceVerifyPhoneAct extends BaseActivity implements View.OnClickListener,IBalancePaySetOne, TextWatcher {

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.mtv_timer)
    MyTextView mtv_timer;

    @BindView(R.id.mtv_next)
    MyTextView mtv_next;

    private CountDownTimer countDownTimer;
    private PBalancePaySetOne pBalancePaySetOne;
    private GradientDrawable background;
    private String code;
    public static final int FORGETCODE=66;
    public static final int FORGETCODES=88;

    public static void startAct(Context context,boolean isPaySet,boolean isAli,boolean isForget) {
        Intent intent = new Intent(context, BalanceVerifyPhoneAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isPaySet",isPaySet);
        intent.putExtra("isAli",isAli);
        intent.putExtra("isForget", isForget);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_verify_phone;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==FORGETCODE&&resultCode==FORGETCODES) {
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_timer:
                pBalancePaySetOne.getCode();
                break;
            case R.id.mtv_next:
                pBalancePaySetOne.checkCode(code);
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
        et_code.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        background = (GradientDrawable) mtv_next.getBackground();
        mtv_phone.setText(Constant.MOBILE);
        pBalancePaySetOne=new PBalancePaySetOne(this,this);
    }

    @Override
    public void getCodeCall() {
        if (countDownTimer==null){
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_timer.setText("("+(int) Math.floor(l / 1000) + "s)");
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
        if (getIntent().getBooleanExtra("isForget",false)){
            BalancePaySetTwoAct.startActForResult(this,"","sets",key
                    ,getIntent().getBooleanExtra("isPaySet",false),getIntent().getBooleanExtra("isAli",false),true);
        }else {
            BalancePaySetTwoAct.startAct(this,"","sets",key
                    ,getIntent().getBooleanExtra("isPaySet",false),getIntent().getBooleanExtra("isAli",false));
        }
   }

    @Override
    public void bindAlipayCall(String account_number) {

    }

    @Override
    public void setApiData(BalanceInfoEntity data) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        code=editable.toString();
        if (TextUtils.isEmpty(code)){
            mtv_next.setClickable(false);
            background.setColor(getColorResouce(R.color.color_value_6c));
        }else {
            mtv_next.setClickable(true);
            background.setColor(getColorResouce(R.color.pink_color));
        }
    }
}
