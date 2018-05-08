package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.presenter.PBalanceMain;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class BalanceMainAct extends BaseActivity implements View.OnClickListener, IBalanceMain {
    @BindView(R.id.mtv_yueminxi)
    MyTextView mtv_yueminxi;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.mrlayout_licai)
    MyRelativeLayout mrlayout_licai;

    @BindView(R.id.mrlayout_tixiandao)
    MyRelativeLayout mrlayout_tixiandao;

    @BindView(R.id.mrlayout_zhifushezhi)
    MyRelativeLayout mrlayout_zhifushezhi;

    private PBalanceMain pBalanceMain;
    private BalanceInfoEntity data;
    private boolean isBack;

    public static void startAct(Context context,boolean isBack) {
        Intent intent = new Intent(context, BalanceMainAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isBack", isBack);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_close:
                if (isBack)
                MainActivity.startAct(this,"personCenter");
                finish();
                break;
            case R.id.mtv_count:
                if (data != null)
                    BalanceXQAct.startAct(getBaseContext(), data,false);
                break;
            case R.id.mtv_yueminxi:
                BalanceDetailAct.startAct(getBaseContext());
                break;
            case R.id.mrlayout_zhifushezhi:
                BalancePaySetAct.startAct(this,data.is_set_password,false);
                break;
            case R.id.mrlayout_tixiandao:
                AlipayMyAct.startAct(this,data.havePayAccount,!data.is_set_password,false,data.account_number);
                break;
            case R.id.mrlayout_licai:

                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_yueminxi.setOnClickListener(this);
        mrlayout_licai.setOnClickListener(this);
        mrlayout_tixiandao.setOnClickListener(this);
        mrlayout_zhifushezhi.setOnClickListener(this);
        mtv_count.setOnClickListener(this);
        miv_close.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBack=getIntent().getBooleanExtra("isBack", false);
        pBalanceMain = new PBalanceMain(this, this);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(BalanceInfoEntity data) {
        this.data=data;
        mtv_count.setText(data.balance);
        Constant.MOBILE=data.mobile;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isBack)
        MainActivity.startAct(this,"personCenter");
    }
}
