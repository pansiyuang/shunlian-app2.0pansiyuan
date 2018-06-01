package com.shunlian.app.ui.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.presenter.LoginPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.ILoginView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.VerificationCodeInput;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/18.
 */

public class InputVerfiCodeFrag extends BaseFragment implements View.OnClickListener,
        VerificationCodeInput.Listener, ILoginView {

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

    @BindView(R.id.rlayout_pwd)
    RelativeLayout rlayout_pwd;

    @BindView(R.id.rlayout_reset_pwd)
    RelativeLayout rlayout_reset_pwd;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.btn_complete)
    Button btn_complete;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.llayout_clause)
    LinearLayout llayout_clause;

    private String jumpType;


    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_register_two, null);
        return view;
    }

    protected void initViews() {
        visible(llayout_clause);
        gone(rlayout_pwd,et_nickname,rlayout_reset_pwd);
        btn_complete.setText(getString(R.string.LoginPswFrg_dl));

        tv_phone.setText(currentPhone);

        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                if (tv_time != null) {
                    tv_time.setText((int) Math.floor(l / 1000) + "s");
                    tv_time.setEnabled(false);
                }
            }

            @Override
            public void onFinish() {
                if (tv_time != null) {
                    tv_time.setText(getResources().getString(R.string.LoginPswFrg_cxhq));
                    tv_time.setEnabled(true);
                }
            }
        };
        countDownTimer.start();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        currentPhone = arguments.getString("phoneNum");
        picCode = arguments.getString("vCode");
        loginPresenter = new LoginPresenter(baseActivity, this, LoginPresenter.TYPE_MOBILE);
        initViews();
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        btn_complete.setOnClickListener(this);
        input_code.setOnCompleteListener(this);
    }

    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
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
            case R.id.miv_close:
                ((LoginAct)baseActivity).backPage();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        SharedPrefUtil.saveSharedPrfString("plus_role", content.plus_role);
        SharedPrefUtil.saveSharedPrfString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedPrfString("member_id", content.member_id);
        if (content.tag!=null)
        SharedPrefUtil.saveSharedPrfStringss("tags", new HashSet<>(content.tag));
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        JpushUtil.setJPushAlias();
        if (!isEmpty(jumpType)){
            Common.goGoGo(baseActivity,jumpType);
        }
        baseActivity.finish();
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