package com.shunlian.app.ui.setting.change_user;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.presenter.ChangeUserPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IChangeUserView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.VerificationCodeInput;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SettingPwdFrag extends BaseFragment implements IChangeUserView, View.OnClickListener {

    @BindView(R.id.mbtn_save)
    MyButton mbtn_save;

    @BindView(R.id.rlayout_pwd)
    RelativeLayout rlayout_pwd;

    @BindView(R.id.rlayout_reset_pwd)
    RelativeLayout rlayout_reset_pwd;

    @BindView(R.id.mtv_phone)
    MyTextView mtv_phone;

    @BindView(R.id.tv_time)
    MyTextView tv_time;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.et_pwd)
    MyEditText et_pwd;

    @BindView(R.id.iv_hidden_psw)
    MyImageView iv_hidden_psw;

    @BindView(R.id.et_rpwd)
    MyEditText et_rpwd;

    @BindView(R.id.iv_hidden_rpsw)
    MyImageView iv_hidden_rpsw;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    public static final int CONFIRM_CODE = 1;//验证码验证
    public static final int BIND_CODE = 2;//绑定手机号
    public static final int SET_PWD_CODE = 3;//设置密码
    private CountDownTimer countDownTimer;
    private ChangeUserPresenter presenter;
    private int state;
    private boolean isHiddenPwd;
    private boolean isHiddenRPwd;
    private String mSmsCode;
    private String mMobile;
    private String mKey;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.act_settingpwd, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
        iv_hidden_psw.setOnClickListener(this);
        iv_hidden_rpsw.setOnClickListener(this);
        input_code.setOnCompleteListener(content->mSmsCode = content);


        et_pwd.setOnTouchListener((v,event)->{
            setEdittextFocusable(true,et_pwd);
            return false;
        });


        et_pwd.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_psw.setVisibility(View.VISIBLE);
                    GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                    background.setColor(getColorResouce(R.color.pink_color));
                }else {
                    GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                    background.setColor(getColorResouce(R.color.color_value_6c));
                    iv_hidden_psw.setVisibility(View.INVISIBLE);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        et_rpwd.setOnTouchListener((v,event)->{
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
        );

        et_rpwd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_rpsw.setVisibility(View.VISIBLE);
                    GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                    background.setColor(getColorResouce(R.color.pink_color));
                }else {
                    GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
                    background.setColor(getColorResouce(R.color.color_value_6c));
                    iv_hidden_rpsw.setVisibility(View.INVISIBLE);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_rpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        Bundle arguments = getArguments();
        state = arguments.getInt("state", -1);
        mMobile = arguments.getString("mobile");
        mKey = arguments.getString("key");

        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);
        presenter = new ChangeUserPresenter(baseActivity, this);

        reset();
    }

    private void reset() {
        if (state == BIND_CODE) {//绑定手机
            mbtn_save.setText("绑定");
            mtv_phone.setText(mMobile);
            gone(rlayout_pwd, rlayout_reset_pwd);
        } else if (state == CONFIRM_CODE) {//验证码验证
            mbtn_save.setText("验证");
            gone(rlayout_pwd, rlayout_reset_pwd);
            presenter.getMobile();
        } else {//设置密码
            visible(rlayout_pwd, rlayout_reset_pwd);
            mbtn_save.setText("完成");
            et_pwd.setText("");
            et_rpwd.setText("");
            GradientDrawable background = (GradientDrawable) mbtn_save.getBackground();
            background.setColor(getColorResouce(R.color.color_value_6c));
            presenter.getMobile();
        }
        input_code.clearAll();
        countDown();
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setMobile(String mobile) {
        mtv_phone.setText(mobile);
    }

    @Override
    public void key(String key) {
        ((ModifyAct)baseActivity).modifyOnePage(true,false,key);
    }

    @Override
    public void modifyKey(String key) {
        mKey = key;
    }

    @Override
    public void bindMobileSuccess() {
        // TODO: 2018/4/25 绑定成功
        /***登录成功后去个人中心***/
        MainActivity.startAct(baseActivity,"route_login");//先到首页再路由到登录界面
        DispachJump dispachJump = new DispachJump();
        dispachJump.jumpType = dispachJump.personal;
        EventBus.getDefault().postSticky(dispachJump);
        baseActivity.finish();
    }

    @Override
    public void modifyPwdSuccess() {
        // TODO: 2018/4/25 修改密码成功
        /***登录成功后去个人中心***/
        MainActivity.startAct(baseActivity,"route_login");//先到首页再路由到登录界面
        DispachJump dispachJump = new DispachJump();
        dispachJump.jumpType = dispachJump.personal;
        EventBus.getDefault().postSticky(dispachJump);
        baseActivity.finish();
    }

    @OnClick(R.id.tv_time)
    public void downTime(){
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        if (presenter != null){
            presenter.sendSmsCode(mKey);
        }
    }

    @OnClick(R.id.mbtn_save)
    public void clickBtn(){
        switch (state){
            case BIND_CODE:
                if (!isEmpty(mSmsCode)){
                    presenter.changeMobile(mKey,mSmsCode);
                }
                break;
            case CONFIRM_CODE:
                if (presenter != null && !isEmpty(mSmsCode)){
                    presenter.checkSmsCode(mSmsCode);
                }else {
                    Common.staticToast("请输入短信验证码");
                }
                break;
            case SET_PWD_CODE:
                String s = et_pwd.getText().toString();
                String s1 = et_rpwd.getText().toString();
                if (!s.equals(s1)){
                    Common.staticToast("密码不一致");
                }else {
                    presenter.changePassword(s,mSmsCode);
                }
                break;
        }
    }

    private void countDown() {
        if (countDownTimer == null){
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
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            case R.id.miv_close:
                // TODO: 2018/6/1 待定
                if (state == SET_PWD_CODE) {
                    ((ModifyAct) baseActivity).modifyOnePage(false, true, null);
                }else {
                    ((ModifyAct) baseActivity).modifyOnePage(false, false, null);
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
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public void setArgument(int state, String mobile, String key) {
        this.state = state;
        this.mMobile = mobile;
        this.mKey = key;
        reset();
    }
}
