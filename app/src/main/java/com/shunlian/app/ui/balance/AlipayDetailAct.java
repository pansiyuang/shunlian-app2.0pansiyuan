package com.shunlian.app.ui.balance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AlipayDetailAdapter;
import com.shunlian.app.bean.GetRealInfoEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.presenter.PAlipayDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IAlipayDetail;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

public class AlipayDetailAct extends BaseActivity implements View.OnClickListener, IAlipayDetail {
    @BindView(R.id.mtv_jiechu)
    MyTextView mtv_jiechu;

    @BindView(R.id.mtv_account)
    MyTextView mtv_account;

    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    private PAlipayDetail pAlipayDetail;
    private PromptDialog promptDialog;

    public static void startAct(Context context,boolean hintFlag) {
        Intent intent = new Intent(context, AlipayDetailAct.class);
        intent.putExtra("hintFlag", hintFlag);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
        }
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.balance_quedingyao), getStringResouce(R.string.balance_wozaixiang), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }, getStringResouce(R.string.SelectRecommendAct_sure), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalancePaySetTwoAct.startAct(baseAct,"","unbind","",false,true);
                promptDialog.dismiss();
            }
        }).show();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_alipay_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_jiechu:
                initHintDialog();
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_jiechu.setOnClickListener(this);
    }
    public void initDialog() {
        Dialog dialog_hint = new Dialog(baseAct, R.style.popAd);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.dialog_hint_zhi, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
//设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        dialog_hint.setContentView(viewDialog, layoutParams);

//        dialog_hint.setContentView(R.layout.dialog_hint_zhi);
        MyTextView mtv_sure = (MyTextView) dialog_hint.findViewById(R.id.mtv_sure);
        mtv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_hint.dismiss();
            }
        });
        dialog_hint.show();
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        if (getIntent().getBooleanExtra("hintFlag", false))
            initDialog();
        pAlipayDetail = new PAlipayDetail(this, this);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setApiData(GetRealInfoEntity getRealInfoEntity) {
        mtv_account.setText(String.format(getStringResouce(R.string.balance_zhifubao), getRealInfoEntity.account_number));
        rv_content.setLayoutManager(new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false));
        rv_content.setAdapter(new AlipayDetailAdapter(baseAct, false, getRealInfoEntity.limit_memo));
        rv_content.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0));
    }
}
