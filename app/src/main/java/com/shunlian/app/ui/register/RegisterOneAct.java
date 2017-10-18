package com.shunlian.app.ui.register;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

public class RegisterOneAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.tv_select)
    TextView tv_select;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.miv_code)
    MyImageView miv_code;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_one;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                System.out.println("beforeTextChanged=="+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("onTextChanged=="+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("afterTextChanged=="+s);
            }
        });

        et_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    return;
                }
                if ("1234".equals(s.toString())){
                    RegisterTwoAct.startAct(RegisterOneAct.this, phone);
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_close:
                finish();
                break;
            case R.id.tv_select:
                SelectRecommendAct.startAct(this);
                break;
        }
    }
}
