package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

public class TestAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.tv2)
    TextView tv2;

    @BindView(R.id.tv3)
    TextView tv3;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, TestAct.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv1:
                BindingPhoneAct.startAct(this,0);
                break;
            case R.id.tv2:
                BindingPhoneAct.startAct(this,1);
                break;
            case R.id.tv3:
                BindingPhoneAct.startAct(this,2);
                break;
        }
    }
}
