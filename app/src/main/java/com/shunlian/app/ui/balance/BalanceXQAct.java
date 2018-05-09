package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class BalanceXQAct extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.mtv_yueminxi)
    MyTextView mtv_yueminxi;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.mtv_tixian)
    MyTextView mtv_tixian;

    @BindView(R.id.mtv_tishi)
    MyTextView mtv_tishi;

    @BindView(R.id.miv_close)
    MyImageView miv_close;


    private BalanceInfoEntity balanceInfoEntity;
    private PromptDialog promptDialog;
    private boolean isBack;
    private GradientDrawable copyBackground;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_yueminxi:
                BalanceDetailAct.startAct(getBaseContext());
                break;
            case R.id.mtv_tixian:
                if (balanceInfoEntity.havePayAccount) {
                    BalanceTXAct.startAct(this);
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
                    BalanceVerifyPhoneAct.startAct(getBaseContext(),false,false);
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
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBack = getIntent().getBooleanExtra("isBack", false);
        balanceInfoEntity = (BalanceInfoEntity) getIntent().getSerializableExtra("balance");
        if (balanceInfoEntity != null) {
            mtv_count.setText(getStringResouce(R.string.common_yuan) + balanceInfoEntity.balance);
            copyBackground = (GradientDrawable) mtv_tixian.getBackground();
            if (!TextUtils.isEmpty(balanceInfoEntity.balance) && Float.parseFloat(balanceInfoEntity.balance) > 0) {
                copyBackground.setColor(getColorResouce(R.color.pink_color));
                mtv_tixian.setClickable(true);
            } else {
                copyBackground.setColor(getColorResouce(R.color.color_value_6c));
                mtv_tixian.setClickable(true);
            }
            String free= String.valueOf(Float.parseFloat(balanceInfoEntity.limit_amount)+Float.parseFloat(balanceInfoEntity.profit));
            String content = String.format(getStringResouce(R.string.balance_wenxintishi),free
                    ,balanceInfoEntity.rate_name);
            SpannableStringBuilder tishiBuilder = Common.changeColors(content,
                    getColorResouce(R.color.pink_color), "度为" + free
                    ,"分按"+balanceInfoEntity.rate_name);
            mtv_tishi.setText(tishiBuilder);
        }
    }
}
