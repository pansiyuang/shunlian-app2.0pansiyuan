package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity {

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.et_code1)
    EditText et_code1;

    @BindView(R.id.et_code2)
    EditText et_code2;

    @BindView(R.id.et_code3)
    EditText et_code3;

    @BindView(R.id.et_code4)
    EditText et_code4;

    public static void startAct(Context context, String phone){
        Intent intent = new Intent(context,RegisterTwoAct.class);
        intent.putExtra("phone",phone);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    @Override
    protected void initListener() {
        super.initListener();
        et_code2.setFocusable(false);
        et_code2.setFocusableInTouchMode(false);

        et_code3.setFocusable(false);
        et_code3.setFocusableInTouchMode(false);

        et_code4.setFocusable(false);
        et_code4.setFocusableInTouchMode(false);

        et_code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    et_code2.setFocusableInTouchMode(true);
                    et_code2.setFocusable(true);
                    et_code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    et_code3.setFocusableInTouchMode(true);
                    et_code3.setFocusable(true);
                    et_code3.requestFocus();
                }else {
                    et_code1.setFocusableInTouchMode(true);
                    et_code1.setFocusable(true);
                    et_code1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)){
                    et_code4.setFocusableInTouchMode(true);
                    et_code4.setFocusable(true);
                    et_code4.requestFocus();
                }else {
                    et_code2.setFocusableInTouchMode(true);
                    et_code2.setFocusable(true);
                    et_code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    et_code3.setFocusableInTouchMode(true);
                    et_code3.setFocusable(true);
                    et_code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        String phone = getIntent().getStringExtra("phone");
        tv_phone.setText(phone);
    }
}
