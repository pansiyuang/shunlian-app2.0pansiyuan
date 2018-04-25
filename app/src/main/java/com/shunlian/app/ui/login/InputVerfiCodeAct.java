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
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.presenter.LoginPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.ILoginView;
import com.shunlian.app.widget.VerificationCodeInput;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InputVerfiCodeAct extends BaseActivity implements View.OnClickListener, VerificationCodeInput.Listener, ILoginView {
    private CountDownTimer countDownTimer;
    private String currentPhone;
    private String currentVerfiCode;
    private String picCode;
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
    private String jumpType;


    public static void startAct(Context context, String phoneNum, String vCode) {
        Intent intent = new Intent(context, InputVerfiCodeAct.class);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("vCode", vCode);
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
        EventBus.getDefault().register(this);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        currentPhone = getIntent().getStringExtra("phoneNum");
        picCode = getIntent().getStringExtra("vCode");
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
        if (FastClickListener.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_time:
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
                loginPresenter.sendSmsCode(currentPhone.replaceAll(" ", ""), picCode);
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
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onComplete(String content) {
        currentVerfiCode = content;
    }

    @Subscribe(sticky =true)
    public void eventBus(DispachJump jump){
        jumpType = jump.jumpType;
    }

    @Override
    public void login(LoginFinishEntity content) {
        //登陆成功啦
        SharedPrefUtil.saveSharedPrfString("token", content.token);
        SharedPrefUtil.saveSharedPrfString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedPrfString("member_id", content.member_id);
        if (content.tag!=null)
        SharedPrefUtil.saveSharedPrfStringss("tags", new HashSet<>(content.tag));
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);
        JpushUtil.setJPushAlias();
        if (!isEmpty(jumpType)){
            Common.goGoGo(this,jumpType);
        }
        finish();
    }

    @Override
    public void getSmsCode(String code) {
        currentVerfiCode = code;
    }

    @Override
    public void loginFail(String erroMsg) {
        Common.staticToast(erroMsg);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }
}
