package com.shunlian.app.ui.balance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.presenter.PBalanceTX;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IBalanceTX;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.gridpasswordview.GridPasswordView;

import butterknife.BindView;

import static com.shunlian.app.ui.balance.BalanceTXAct.PASSWORDERROR;
import static com.shunlian.app.ui.balance.BalanceTXAct.PASSWORDLOCK;

public class BalanceXQAct extends BaseActivity implements View.OnClickListener, IBalanceTX {
    @BindView(R.id.mtv_yueminxi)
    MyTextView mtv_yueminxi;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.mtv_tixian)
    MyTextView mtv_tixian;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_tishi)
    MyTextView mtv_tishi;

    @BindView(R.id.mtv_account)
    MyTextView mtv_account;

    @BindView(R.id.miv_close)
    MyImageView miv_close;


    private BalanceInfoEntity balanceInfoEntity;
    private PromptDialog promptDialog;
    private boolean isBack;
    private GradientDrawable copyBackground;
    private PBalanceTX pBalanceTX;
    private GridPasswordView gpv_customUi;
    private Dialog dialog_pay;
    private MyTextView mtv_amount;
    private String balance;

    public static void startAct(Context context, BalanceInfoEntity balanceInfoEntity, boolean isBack) {
        Intent intent = new Intent(context, BalanceXQAct.class);
        intent.putExtra("balance", balanceInfoEntity);
        intent.putExtra("isBack", isBack);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_xq;
    }
    public void initDialog() {
        if (gpv_customUi!=null)
            gpv_customUi.clearPassword();
        if (dialog_pay == null) {
            dialog_pay = new Dialog(this, R.style.Mydialog);
            dialog_pay.setContentView(R.layout.dialog_pay_password);
            MyImageView miv_close = (MyImageView) dialog_pay.findViewById(R.id.miv_close);
            mtv_amount = (MyTextView) dialog_pay.findViewById(R.id.mtv_amount);
            MyTextView mtv_fee = (MyTextView) dialog_pay.findViewById(R.id.mtv_fee);
            mtv_fee.setVisibility(View.GONE);
            gpv_customUi = (GridPasswordView) dialog_pay.findViewById(R.id.gpv_customUi);
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
                    pBalanceTX.tiXian(psw, balance, "");
                }
            });
        }
        mtv_amount.setText(getStringResouce(R.string.common_yuan) + balance);
        dialog_pay.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_yueminxi:
                BalanceDetailAct.startAct(getBaseContext());
                break;
            case R.id.mtv_tixian:
                if (balanceInfoEntity.havePayAccount) {
                    if (Constant.ISBALANCE){
                        BalanceTXAct.startAct(this);
                    }else {
                        initDialog();
                    }
                } else {
                    initHintDialog();
                }
                break;
            case R.id.miv_close:
                if (isBack)
                    BalanceMainAct.startAct(getBaseContext(), true);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isBack)
            BalanceMainAct.startAct(getBaseContext(), true);
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
        }
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.balance_ninhaimeiyoutianjia), getStringResouce(R.string.balance_qutianjia), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (balanceInfoEntity.is_set_password){
                    BalancePaySetTwoAct.startAct(getBaseContext(), "", "bindPay", "",true,false);
                }else {
                    BalanceVerifyPhoneAct.startAct(getBaseContext(),false,false,false);
                }
                promptDialog.dismiss();
            }
        }, getStringResouce(R.string.errcode_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }).show();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_yueminxi.setOnClickListener(this);
        mtv_tixian.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        pBalanceTX = new PBalanceTX(this, this);
        if (Constant.ISBALANCE){
            mtv_account.setText(getStringResouce(R.string.balance_zhanghuyue));
            mtv_title.setText(getStringResouce(R.string.balance_yuexiangqing));
            mtv_yueminxi.setVisibility(View.VISIBLE);
        }else {
            mtv_account.setText(getStringResouce(R.string.balance_zhanghushouyi));
            mtv_title.setText(getStringResouce(R.string.balance_shouyitixian));
            mtv_yueminxi.setVisibility(View.GONE);
        }
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBack = getIntent().getBooleanExtra("isBack", false);
        balanceInfoEntity = (BalanceInfoEntity) getIntent().getSerializableExtra("balance");
        if (balanceInfoEntity != null) {
            balance=balanceInfoEntity.balance;
            mtv_count.setText(getStringResouce(R.string.common_yuan) + balance);
            copyBackground = (GradientDrawable) mtv_tixian.getBackground();
            if (!TextUtils.isEmpty(balance) && Float.parseFloat(balance) > 0) {
                copyBackground.setColor(getColorResouce(R.color.pink_color));
                mtv_tixian.setClickable(true);
            } else {
                copyBackground.setColor(getColorResouce(R.color.color_value_6c));
                mtv_tixian.setClickable(false);
            }
            if (Constant.ISBALANCE){
                String free= String.valueOf(Float.parseFloat(balanceInfoEntity.limit_amount)+Float.parseFloat(balanceInfoEntity.profit));
                String content = String.format(getStringResouce(R.string.balance_wenxintishi),free
                        ,balanceInfoEntity.rate_name);
                SpannableStringBuilder tishiBuilder = Common.changeColors(content,
                        getColorResouce(R.color.pink_color), "度为" + free
                        ,"分按"+balanceInfoEntity.rate_name);
                mtv_tishi.setText(tishiBuilder);
            }else {
                mtv_tishi.setText(getStringResouce(R.string.balance_wenxintishis));
            }
        }
    }

    @Override
    public void setApiData(CommonEntity data) {

    }
    public void initHintDialog(String title, String left,String right) {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
        }
        promptDialog.setSureAndCancleListener(title, right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                if (right.equals(getStringResouce(R.string.balance_chongxinshuru))) {
                    initDialog();
                }
            }
        }, left, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                BalanceVerifyPhoneAct.startAct(getBaseContext(),false,false,true);
            }
        }).show();
    }

    @Override
    public void tiXianCallback(CommonEntity data, int code, String message) {
        if (data != null) {
            BalanceResultAct.startAct(this, data.amount, data.account, data.account_type,data.error);
        } else {
            String right,left;
            gpv_customUi.clearPassword();
            dialog_pay.dismiss();
            if (code == PASSWORDLOCK) {
                right = getStringResouce(R.string.errcode_cancel);
                left = getStringResouce(R.string.balance_zhaohuimima);
                initHintDialog(message, left,right);
            } else if (code==PASSWORDERROR){
                right = getStringResouce(R.string.balance_chongxinshuru);
                left = getStringResouce(R.string.balance_wangjimima);
                initHintDialog(message, left,right);
            }
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
