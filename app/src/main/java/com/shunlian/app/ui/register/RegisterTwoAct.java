package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    public static void startAct(Context context, String phone) {
        Intent intent = new Intent(context, RegisterTwoAct.class);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        String phone = getIntent().getStringExtra("phone");
        tv_phone.setText(phone);
    }
}
