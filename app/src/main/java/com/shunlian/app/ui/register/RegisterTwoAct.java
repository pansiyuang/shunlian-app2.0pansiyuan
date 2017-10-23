package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterTwoPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.VerificationCodeInput;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_phone)
    TextView tv_phone;

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

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.tv_complete)
    TextView tv_complete;

    private String nickname;
    private boolean isHiddenPwd;
    private boolean isHiddenRPwd;
    private CountDownTimer countDownTimer;
    private String smsCode;
    private String codeId;
    private RegisterTwoPresenter registerTwoPresenter;
    private String phone;

    public static void startAct(Context context, String smsCode, String phone, String codeId){
        Intent intent = new Intent(context,RegisterTwoAct.class);
        intent.putExtra("phone",phone);
        intent.putExtra("smsCode",smsCode);
        intent.putExtra("codeId",codeId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    @Override
    protected void initListener() {
        super.initListener();
        iv_hidden_psw.setOnClickListener(this);
        iv_hidden_rpsw.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_complete.setOnClickListener(this);

        input_code.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                if (!smsCode.equals(content)){
                    Common.staticToast("手机验证码错误");
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

        phone = getIntent().getStringExtra("phone");
        smsCode = getIntent().getStringExtra("smsCode");
        codeId = getIntent().getStringExtra("codeId");
        tv_phone.setText(phone);
        countDown();

        registerTwoPresenter = new RegisterTwoPresenter(this,null);

    }

    private void countDown() {
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv_time.setText((int) Math.floor(l / 1000) + "s");
                tv_time.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tv_time.setText("重新获取");
                tv_time.setEnabled(true);
            }
        };
        countDownTimer.start();
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
            case R.id.tv_time:
                if (countDownTimer != null){
                    countDownTimer.start();
                }
                break;
            case R.id.tv_complete:
                registerTwoPresenter.register(phone.replaceAll(" ", ""),smsCode,codeId,et_pwd.getText().toString(),nickname);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
}
