package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.presenter.PBalanceMain;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class BalanceResultAct extends BaseActivity implements View.OnClickListener, IBalanceMain {
    @BindView(R.id.mtv_finish)
    MyTextView mtv_finish;

    @BindView(R.id.mtv_account)
    MyTextView mtv_account;

    @BindView(R.id.mtv_amount)
    MyTextView mtv_amount;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private PBalanceMain pBalanceMain;
    private BalanceInfoEntity data;

    public static void startAct(Context context,String amount,String account,String accountType,String error) {
        Intent intent = new Intent(context, BalanceResultAct.class);
        intent.putExtra("amount", amount);
        intent.putExtra("account", account);
        intent.putExtra("accountType", accountType);
        intent.putExtra("error", error);
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
            case R.id.miv_close:
                if (data != null)
                    BalanceXQAct.startAct(getBaseContext(), data,true);
                finish();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_finish.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BalanceXQAct.startAct(getBaseContext(), data,true);
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        GradientDrawable copyBackground = (GradientDrawable) mtv_finish.getBackground();
        copyBackground.setColor(getColorResouce(R.color.pink_color));
        if (isEmpty(getIntent().getStringExtra("error"))){
            mtv_desc.setText(getStringResouce(R.string.balance_tixianzhanghu));
            mtv_account.setText(getIntent().getStringExtra("accountType")+"("+getIntent().getStringExtra("account")+")");
            mtv_amount.setText(getIntent().getStringExtra("amount"));
        }else {
            mtv_desc.setText(getStringResouce(R.string.balance_shibaiyuanyin));
            mtv_amount.setText(getIntent().getStringExtra("amount"));
            mtv_account.setText(getIntent().getStringExtra("error"));
        }
        pBalanceMain = new PBalanceMain(this, this);
    }

    @Override
    public void setApiData(BalanceInfoEntity data) {
        this.data=data;
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
