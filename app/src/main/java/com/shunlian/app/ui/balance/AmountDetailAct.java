package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AmountDetailAdapter;
import com.shunlian.app.bean.AmountDetailEntity;
import com.shunlian.app.presenter.PAmountDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IAmountDetail;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class AmountDetailAct extends BaseActivity implements View.OnClickListener, IAmountDetail {
    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.rv_content)
    RecyclerView rv_content;

    private PAmountDetail pBalanceMain;


    public static void startAct(Context context, String id) {
        Intent intent = new Intent(context, AmountDetailAct.class);
        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_amount_detail;
    }


    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pBalanceMain = new PAmountDetail(this, this, getIntent().getStringExtra("id"));
        if (Constant.ISBALANCE) {
            mtv_title.setText(getStringResouce(R.string.balance_shouzhiminxi));
            pBalanceMain.getData();
        } else {
            mtv_title.setText(getStringResouce(R.string.balance_tixianxianqing));
            pBalanceMain.getDatas();
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setApiData(List<AmountDetailEntity.Content> amountDetailEntities) {
        mtv_desc.setText(amountDetailEntities.get(0).name);
        mtv_count.setText(amountDetailEntities.get(0).value);
        amountDetailEntities.remove(0);
        rv_content.setLayoutManager(new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false));
        rv_content.setAdapter(new AmountDetailAdapter(baseAct, false, amountDetailEntities));
    }
}
