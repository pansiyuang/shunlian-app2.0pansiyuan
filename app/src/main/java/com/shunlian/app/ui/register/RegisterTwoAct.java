package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.presenter.RegisterTwoPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IRegisterTwoView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.VerificationCodeInput;

import butterknife.BindView;

public class RegisterTwoAct extends BaseActivity implements View.OnClickListener, IRegisterTwoView {
    public static String TYPE_FIND_PSW = "find_password";
    public static String TYPE_REGIST = "regist";
    private String currentType;

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

    @BindView(R.id.btn_complete)
    Button btn_complete;

    private String nickname;
    private boolean isHiddenPwd;
    private boolean isHiddenRPwd;
    private CountDownTimer countDownTimer;
    private String smsCode;
    private String codeId;
    private String unique_sign;
    private RegisterTwoPresenter registerTwoPresenter;
    private String phone;
    private String mCode;

    public static void startAct(Context context, String smsCode, String phone, String codeId, String unique_sign, String type) {
        Intent intent = new Intent(context, RegisterTwoAct.class);
        intent.putExtra("phone", phone);
        intent.putExtra("smsCode", smsCode);
        intent.putExtra("codeId", codeId);
        intent.putExtra("unique_sign", unique_sign);
        intent.putExtra("type", type);
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
        btn_complete.setOnClickListener(this);

        input_code.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String content) {
                mCode = content;
                if (!smsCode.equals(content)){
                    Common.staticToast(getString(R.string.RegisterTwoAct_shyzmcw));
                }else {
                    setEdittextFocusable(true,et_pwd);
                }
            }
        });

        et_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (checkCode(et_pwd)){
                    return false;
                }
                return false;
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
                if (checkCode(et_rpwd))
                    return false;
                if (isEtPwdEmpty(et_pwd,et_rpwd)){
                    return false;
                }
                String pwd = et_pwd.getText().toString();
                if (!Common.regularPwd(pwd)){
                    Common.staticToast(getString(R.string.RegisterTwoAct_mmzh));
                    setEdittextFocusable(true,et_pwd);
                    setEdittextFocusable(false,et_rpwd);
                    return false;
                }else {
                    setEdittextFocusable(true,et_rpwd);
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

        et_nickname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (checkCode(et_nickname))
                    return false;
                if (isEtPwdEmpty(et_pwd,et_nickname)){
                    return false;
                }

                if (isEtPwdEmpty(et_rpwd,et_nickname)){
                    return false;
                }else {
                    String pwd = et_pwd.getText().toString();
                    String rpwd = et_rpwd.getText().toString();
                    if (!pwd.equals(rpwd)){
                        Common.staticToast(getString(R.string.RegisterTwoAct_mmbyz));
                        setEdittextFocusable(true,et_rpwd);
                        setEdittextFocusable(false,et_nickname);
                        return false;
                    }else {
                        setEdittextFocusable(true, et_nickname);
                    }
                }
                return false;
            }
        });

        et_nickname.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                byte[] bytes = s.toString().getBytes();
                if (bytes.length > 24){
                    Common.staticToast(getString(R.string.RegisterTwoAct_ncszgc));
                    et_nickname.setText(nickname);
                    et_nickname.setSelection(nickname.length());
                }else {
                    nickname = s.toString();
                }
            }
        });
    }

    private boolean checkCode(EditText editText) {
        if (TextUtils.isEmpty(mCode)){
            Common.staticToast(getString(R.string.RegisterTwoAct_sjyzm));
            setEdittextFocusable(false,editText);
            return true;
        }
        return false;
    }

    private boolean isEtPwdEmpty(EditText active ,EditText passive){
        String pwd = active.getText().toString();
        if (TextUtils.isEmpty(pwd)){
            Common.staticToast(getString(R.string.RegisterTwoAct_mmbnwk));
            setEdittextFocusable(true,active);
            setEdittextFocusable(false,passive);
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        phone = getIntent().getStringExtra("phone");
        smsCode = getIntent().getStringExtra("smsCode");
        codeId = getIntent().getStringExtra("codeId");
        unique_sign = getIntent().getStringExtra("unique_sign");
        currentType = getIntent().getStringExtra("type");

        tv_phone.setText(phone);

        if (TYPE_FIND_PSW.equals(currentType)) {
            et_nickname.setVisibility(View.GONE);
        }

        countDown();

        registerTwoPresenter = new RegisterTwoPresenter(this, this);

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
                tv_time.setText(getString(R.string.LoginPswFrg_cxhq));
                tv_time.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hidden_psw:
                if (isHiddenPwd) {//隐藏
                    isHiddenPwd = false;
                    isShowPwd(et_pwd, false);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                } else {
                    isHiddenPwd = true;
                    isShowPwd(et_pwd, true);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.iv_hidden_rpsw:
                if (isHiddenRPwd) {
                    isHiddenRPwd = false;
                    isShowPwd(et_rpwd, false);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                } else {
                    isHiddenRPwd = true;
                    isShowPwd(et_rpwd, true);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.tv_time:
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
                break;
            case R.id.btn_complete:
                if (TYPE_FIND_PSW.equals(currentType)) {
                    registerTwoPresenter.findPsw(phone.replaceAll(" ", ""), et_pwd.getText().toString(), et_rpwd.getText().toString(), smsCode);
                } else if (TYPE_REGIST.equals(currentType)) {
                    registerTwoPresenter.register(phone.replaceAll(" ", ""), smsCode, codeId, et_pwd.getText().toString(), nickname, "");
                } else {
                    registerTwoPresenter.register(phone.replaceAll(" ", ""), smsCode, codeId, et_pwd.getText().toString(), nickname, unique_sign);
                }
                break;
        }
    }

    private void isShowPwd(EditText editText, boolean isShow) {
        if (isShow) {//显示
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
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


    @Override
    public void resetPsw(String message) {
        Common.staticToast(message);
    }

    @Override
    public void registerFinish(RegisterFinishEntity entity) {
        SharedPrefUtil.saveSharedPrfString("token", entity.token);

    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

}
