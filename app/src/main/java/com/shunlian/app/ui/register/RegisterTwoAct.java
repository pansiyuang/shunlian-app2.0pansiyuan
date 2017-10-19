package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity implements View.OnClickListener {

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

    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @BindView(R.id.et_rpwd)
    EditText et_rpwd;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.iv_hidden_psw)
    MyImageView iv_hidden_psw;

    @BindView(R.id.iv_hidden_rpsw)
    MyImageView iv_hidden_rpsw;

    private String nickname;
    private boolean isHiddenPwd;
    private boolean isHiddenRPwd;

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

        setEdittextFocusable(false,et_code2,et_code3,et_code4);
        iv_hidden_psw.setOnClickListener(this);
        iv_hidden_rpsw.setOnClickListener(this);

        et_code1.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!TextUtils.isEmpty(s)){
                    setEdittextFocusable(true,et_code2);
                }
            }
        });

        et_code2.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!TextUtils.isEmpty(s)){
                    setEdittextFocusable(true,et_code3);
                }else {
                    setEdittextFocusable(true,et_code1);
                }
            }
        });

        et_code3.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!TextUtils.isEmpty(s)){
                    setEdittextFocusable(true,et_code4);
                }else {
                    setEdittextFocusable(true,et_code2);
                }
            }
        });

        et_code4.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (TextUtils.isEmpty(s)){
                    setEdittextFocusable(true,et_code3);
                }
            }
        });

        et_pwd.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_psw.setVisibility(View.VISIBLE);
                }else {
                    iv_hidden_psw.setVisibility(View.INVISIBLE);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        et_rpwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String pwd = et_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)){
                    Common.staticToast("密码不能为空");
                    setEdittextFocusable(true,et_pwd);
                    return false;
                }
                if (!Common.regularPwd(pwd)){
                    Common.staticToast("密码只能由字母和数字组合");
                    et_pwd.setText("");
                    setEdittextFocusable(true,et_pwd);
                    return false;
                }
                return false;
            }
        });

        et_rpwd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_rpsw.setVisibility(View.VISIBLE);
                }else {
                    iv_hidden_rpsw.setVisibility(View.INVISIBLE);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_rpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        et_nickname.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String pwd = et_pwd.getText().toString();
                String rpwd = et_rpwd.getText().toString();
                if (TextUtils.isEmpty(rpwd)){
                    Common.staticToast("密码不能为空");
                    setEdittextFocusable(true,et_rpwd);
                    return;
                }
                if (!pwd.equals(rpwd)){
                    Common.staticToast("两次输入的密码不一致");
                    et_rpwd.setText("");
                    setEdittextFocusable(true,et_rpwd);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                byte[] bytes = s.toString().getBytes();
                if (bytes.length > 24){
                    Common.staticToast("昵称设置过长");
                    et_nickname.setText(nickname);
                    et_nickname.setSelection(nickname.length());
                }else {
                    nickname = s.toString();
                }
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

    private void setEdittextFocusable(boolean focusable,EditText... editText){
        for (int i = 0; i < editText.length; i++) {
            editText[i].setFocusable(focusable);
            editText[i].setFocusableInTouchMode(focusable);
            if (focusable){
                editText[i].requestFocus();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_hidden_psw:
                if (isHiddenPwd){//隐藏
                    isHiddenPwd = false;
                    isShowPwd(et_pwd,false);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                }else {
                    isHiddenPwd = true;
                    isShowPwd(et_pwd,true);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.iv_hidden_rpsw:
                if (isHiddenRPwd){
                    isHiddenRPwd = false;
                    isShowPwd(et_rpwd,false);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                }else {
                    isHiddenRPwd = true;
                    isShowPwd(et_rpwd,true);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
        }
    }

    private void isShowPwd(EditText editText,boolean isShow) {
        if (isShow){//显示
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }
}
