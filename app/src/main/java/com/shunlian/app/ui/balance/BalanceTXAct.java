package com.shunlian.app.ui.balance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.presenter.PBalanceTX;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IBalanceTX;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.gridpasswordview.GridPasswordView;

import butterknife.BindView;

public class BalanceTXAct extends BaseActivity implements View.OnClickListener, IBalanceTX, TextWatcher {
    public static final int PASSWORDERROR = 1401;
    public static final int PASSWORDLOCK = 1402;
    @BindView(R.id.mtv_account)
    MyTextView mtv_account;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.mtv_tixian)
    MyTextView mtv_tixian;
    @BindView(R.id.mtv_keti)
    MyTextView mtv_keti;
    @BindView(R.id.mtv_quanbuketi)
    MyTextView mtv_quanbuketi;
    @BindView(R.id.mtv_hint)
    MyTextView mtv_hint;
    private PBalanceTX pBalanceTX;
    private float free_amount, rate;
    private String mAmount, amounts, balance, fee, account, accountType;
    private GradientDrawable copyBackground;
    private Dialog dialog_pay;
    private MyTextView mtv_amount, mtv_fee;
    private PromptDialog promptDialog;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, BalanceTXAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_tx;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_quanbuketi:
                et_amount.setText(amounts);
                moneyChange(amounts);
                et_amount.setSelection(amounts.length());
                break;
            case R.id.mtv_tixian:
                initDialog();
                break;
        }
    }

    public void initDialog() {
        if (dialog_pay == null) {
            dialog_pay = new Dialog(this, R.style.Mydialog);
            dialog_pay.setContentView(R.layout.dialog_pay_password);
            MyImageView miv_close = (MyImageView) dialog_pay.findViewById(R.id.miv_close);
            mtv_amount = (MyTextView) dialog_pay.findViewById(R.id.mtv_amount);
            mtv_fee = (MyTextView) dialog_pay.findViewById(R.id.mtv_fee);
            GridPasswordView gpv_customUi = (GridPasswordView) dialog_pay.findViewById(R.id.gpv_customUi);
            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_pay.dismiss();
                }
            });
            gpv_customUi.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                @Override
                public void onTextChanged(String psw) {
                }

                @Override
                public void onInputFinish(String psw) {
                    pBalanceTX.tiXian(psw, balance, account);
                }
            });
        }
        mtv_amount.setText(getStringResouce(R.string.common_yuan) + balance);
        mtv_fee.setText(String.format(getStringResouce(R.string.balance_ewaikouchu), fee));
        dialog_pay.show();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_quanbuketi.setOnClickListener(this);
        mtv_tixian.setOnClickListener(this);
        et_amount.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_tixian.setClickable(false);
        copyBackground = (GradientDrawable) mtv_tixian.getBackground();
        copyBackground.setColor(getColorResouce(R.color.color_value_6c));
        pBalanceTX = new PBalanceTX(this, this);
    }

    @Override
    public void setApiData(CommonEntity data) {
        SpannableStringBuilder tishiBuilder = Common.changeColorAndSize(data.account_type + " (" + data.account_number+")"
                , data.account_type, 15, getColorResouce(R.color.new_text));
        account = data.account_number;
        accountType = data.account_type;
        mtv_account.setText(tishiBuilder);
        mAmount = String.format(getStringResouce(R.string.balance_ketixian), data.amount);
        mtv_keti.setText(mAmount);
        free_amount = Float.parseFloat(data.free_amount);
        rate = Float.parseFloat(data.rate);
        amounts = data.amount;
        mtv_hint.setText(data.free_amount_des);
    }

    @Override
    public void tiXianCallback(CommonEntity data, int code, String message) {
        if (data != null) {
            BalanceResultAct.startAct(this, data.amount, data.account, accountType);
        } else {
            String right;
            if (code == PASSWORDLOCK) {
                right = getStringResouce(R.string.errcode_cancel);
            } else {
                right = getStringResouce(R.string.balance_chongxinshuru);
            }
            initHintDialog(message, right);
        }
    }

    public void initHintDialog(String title, String right) {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
        }
        promptDialog.setSureAndCancleListener(title, getStringResouce(R.string.balance_zhaohuimima), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }, right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                if (!right.equals(getStringResouce(R.string.balance_chongxinshuru))) {
                    initDialog();
                }
            }
        }).show();
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
        balance = editable.toString();
        if (balance.startsWith("."))
            balance = "0." + balance.substring(1);
        moneyChange(balance);
    }

    public void moneyChange(String money) {
        if (TextUtils.isEmpty(money)) {
            mtv_keti.setText(mAmount);
            mtv_tixian.setClickable(false);
            copyBackground.setColor(getColorResouce(R.color.color_value_6c));
        } else {
            float amout = Float.parseFloat(money);
            String desc;
            if (amout > Float.parseFloat(amounts)) {
                mtv_tixian.setClickable(false);
                copyBackground.setColor(getColorResouce(R.color.color_value_6c));
                desc = getStringResouce(R.string.balance_jinechao);
                mtv_keti.setTextColor(getColorResouce(R.color.pink_color));
            } else if (amout > free_amount) {
                mtv_tixian.setClickable(true);
                copyBackground.setColor(getColorResouce(R.color.pink_color));
                mtv_keti.setTextColor(getColorResouce(R.color.share_text));
                fee = Common.formatFloat((amout - free_amount) * rate);
                if (fee.startsWith("."))
                    fee = "0." + fee.substring(1);
                desc = String.format(getStringResouce(R.string.balance_tixianshouxu),
                        fee, rate * 100 + "%");
            } else {
                mtv_tixian.setClickable(true);
                copyBackground.setColor(getColorResouce(R.color.pink_color));
                mtv_keti.setTextColor(getColorResouce(R.color.share_text));
                fee = "0.00";
                desc = String.format(getStringResouce(R.string.balance_tixianshouxu),
                        fee, rate * 100 + "%");
            }
            mtv_keti.setText(desc);
        }
//        et_amount.setSelection(money.length());
    }

}