package com.shunlian.app.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.LoginPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ILoginView;
import com.shunlian.app.widget.VerificationCodeInput;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InputVerfiCodeAct extends BaseActivity implements View.OnClickListener, VerificationCodeInput.Listener, ILoginView {
    private CountDownTimer countDownTimer;
    private String currentPhone;
    private String currentVerfiCode;
    private LoginPresenter loginPresenter;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @BindView(R.id.et_rpwd)
    EditText et_rpwd;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.btn_complete)
    Button btn_complete;


    public static void startAct(Context context, String phoneNum) {
        Intent intent = new Intent(context, InputVerfiCodeAct.class);
        intent.putExtra("phoneNum", phoneNum);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_two;
    }


    private void initViews() {
        et_pwd.setVisibility(View.GONE);
        et_rpwd.setVisibility(View.GONE);
        et_nickname.setVisibility(View.GONE);
        btn_complete.setText(getString(R.string.LoginPswFrg_dl));

        tv_phone.setText(currentPhone);

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv_time.setText((int) Math.floor(l / 1000) + "s");
                tv_time.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tv_time.setText(getResources().getString(R.string.LoginPswFrg_cxhq));
                tv_time.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        currentPhone = getIntent().getStringExtra("phoneNum");
        loginPresenter = new LoginPresenter(this, this, LoginPresenter.TYPE_MOBILE);
        initViews();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_time.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        input_code.setOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_time:
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
                break;
            case R.id.btn_complete:
                if (TextUtils.isEmpty(currentVerfiCode) || currentVerfiCode.length() < 4) {
                    Common.staticToast(getResources().getString(R.string.LoginPswFrg_qsryzm));
                    return;
                }
                loginPresenter.LoginMobile(currentPhone.replaceAll(" ", ""), currentVerfiCode);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        countDownTimer = null;
    }

    @Override
    public void onComplete(String content) {
        currentVerfiCode = content;
    }

    @Override
    public void login(String content) {
        //登陆成功啦
        Common.staticToast(content);
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }
}
