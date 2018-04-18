package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.presenter.PBalanceMain;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.Serializable;

import butterknife.BindView;

public class BalanceXQAct extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.mtv_yueminxi)
    MyTextView mtv_yueminxi;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.mtv_tixian)
    MyTextView mtv_tixian;

    @BindView(R.id.mtv_tishi)
    MyTextView mtv_tishi;


    private BalanceInfoEntity balanceInfoEntity;
    private PromptDialog promptDialog;

    public static void startAct(Context context,BalanceInfoEntity balanceInfoEntity) {
        Intent intent = new Intent(context, BalanceXQAct.class);
        intent.putExtra("balance", balanceInfoEntity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
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
                if (balanceInfoEntity.havePayAccount){
                    BalanceTXAct.startAct(this);
                }else {
                    initHintDialog();
                }
                break;
        }
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
        }
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.balance_ninhaimeiyoutianjia), getStringResouce(R.string.errcode_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }, getStringResouce(R.string.balance_qutianjia), new View.OnClickListener() {
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
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        balanceInfoEntity= (BalanceInfoEntity) getIntent().getSerializableExtra("balance");
        if (balanceInfoEntity!=null){
            mtv_count.setText(getStringResouce(R.string.common_yuan)+balanceInfoEntity.balance);
            if (!TextUtils.isEmpty(balanceInfoEntity.balance)&&Float.parseFloat(balanceInfoEntity.balance)>0){
                GradientDrawable copyBackground = (GradientDrawable) mtv_tixian.getBackground();
                copyBackground.setColor(getColorResouce(R.color.pink_color));
                mtv_tixian.setClickable(true);
            }else {
                mtv_tixian.setClickable(false);
            }
            String content=String.format(getStringResouce(R.string.balance_wenxintishi),balanceInfoEntity.limit_amount
            ,balanceInfoEntity.quota,balanceInfoEntity.profit,balanceInfoEntity.total);
            SpannableStringBuilder tishiBuilder = Common.changeColors(content,
                    getColorResouce(R.color.pink_color),"现有"+balanceInfoEntity.limit_amount
                    ,"还剩"+balanceInfoEntity.quota,"还有"+balanceInfoEntity.profit,"度为"+balanceInfoEntity.total);
            mtv_tishi.setText(tishiBuilder);
        }
    }
}
