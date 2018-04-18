package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class BalanceResultAct extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.mtv_finish)
    MyTextView mtv_finish;

    @BindView(R.id.mtv_account)
    MyTextView mtv_account;

    @BindView(R.id.mtv_amount)
    MyTextView mtv_amount;


    public static void startAct(Context context,String amount,String account,String accountType) {
        Intent intent = new Intent(context, BalanceResultAct.class);
        intent.putExtra("amount", amount);
        intent.putExtra("account", account);
        intent.putExtra("accountType", accountType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_finish:
                finish();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_finish.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        GradientDrawable copyBackground = (GradientDrawable) mtv_finish.getBackground();
        copyBackground.setColor(getColorResouce(R.color.pink_color));
        mtv_account.setText(getIntent().getStringExtra("accountType")+getIntent().getStringExtra("account"));
        mtv_amount.setText(getIntent().getStringExtra("amount"));
    }
}
