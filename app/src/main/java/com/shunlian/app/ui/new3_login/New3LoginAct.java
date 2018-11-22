package com.shunlian.app.ui.new3_login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyImageView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/11/16.
 * 第三版登录 包括（账号密码登录   短信验证登录）
 */
public class New3LoginAct extends BaseActivity{

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private FragmentManager mFragmentManager;
    private Map<String, BaseFragment> fragments;
    /*******账号密码登录*********/
    public static final String ACCOUNT_PWD_LOGIN = LoginPwdFrag.class.getName();
    /*******手机号登录*输入手机号界面********/
    public static final String MOBILE_1_LOGIN = LoginMobileFrag.class.getName();
    /*******手机号登录*输入短信验证码界面********/
    public static final String MOBILE_2_LOGIN = VerifyMobileFrag.class.getName();
    /*********邀请码****************/
    public static final String INVITE_CODE = InviteCodeFrag.class.getName();
    private LoginPwdFrag mLoginPwdFrag;
    private LoginMobileFrag mLoginMobileFrag;
    private VerifyMobileFrag mVerifyMobileFrag;
    private InviteCodeFrag mInviteCodeFrag;
    private int mCurrentPage = 1;
    private LoginConfig mConfig;

    public static void startAct(Context context, LoginConfig config){
        context.startActivity(new Intent(context,New3LoginAct.class)
                .putExtra("config",config));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_login_new3;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(v -> {
           if (mCurrentPage == 1){
                finish();
           }else {
               loginSms(1, mConfig);
           }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mConfig = getIntent().getParcelableExtra("config");
        if (mConfig != null){
            fragments = new HashMap<>();
            mFragmentManager = getSupportFragmentManager();
            if (!isEmpty(mConfig.status))
            {
                loginSms(1, mConfig);
            }
            else

                switch (mConfig.login_mode){
                    case PASSWORD_TO_LOGIN:
                        //密码登录
                        loginPwd();
                        break;
                    case SMS_TO_LOGIN:
                        //短信登录
                        loginSms(1, mConfig);
                        break;
                    case BIND_INVITE_CODE:
                        //绑定推荐人
                        loginInviteCode(mConfig);
                        break;
                }
        }
    }

    /**
     * 密码登录
     */
    public void loginPwd(){
        mLoginPwdFrag = (LoginPwdFrag) fragments.get(ACCOUNT_PWD_LOGIN);
        if (mLoginPwdFrag == null){
            mLoginPwdFrag = new LoginPwdFrag();
            fragments.put(ACCOUNT_PWD_LOGIN,mLoginPwdFrag);
        }
        switchContent(mLoginPwdFrag);
    }

    /**
     * 短信登录
     */
    public void loginSms(int page, LoginConfig config){
        if (page == 1){
            mCurrentPage = 1;
            mLoginMobileFrag = (LoginMobileFrag) fragments.get(MOBILE_1_LOGIN);
            if (mLoginMobileFrag == null){
                mLoginMobileFrag = new LoginMobileFrag();
                fragments.put(MOBILE_1_LOGIN,mLoginMobileFrag);
                Bundle bundle = new Bundle();
                bundle.putParcelable("config", this.mConfig);
                mLoginMobileFrag.setArguments(bundle);
            }else {
                mLoginMobileFrag.initStatus(this.mConfig);
            }
            switchContent(mLoginMobileFrag);
        }else if (page == 2){
            mCurrentPage = 2;
            mVerifyMobileFrag = (VerifyMobileFrag) fragments.get(MOBILE_2_LOGIN);
            if (mVerifyMobileFrag == null){
                mVerifyMobileFrag = new VerifyMobileFrag();
                fragments.put(MOBILE_2_LOGIN,mVerifyMobileFrag);
                Bundle bundle = new Bundle();
                bundle.putParcelable("config", config);
                mVerifyMobileFrag.setArguments(bundle);
            }else {
                mVerifyMobileFrag.initStatus(config);
            }
            switchContent(mVerifyMobileFrag);
        }
    }

    /**
     * 邀请码
     */
    public void loginInviteCode(LoginConfig config){
        if (config.login_mode == LoginConfig.LOGIN_MODE.BIND_INVITE_CODE){
            mCurrentPage = 1;
        }else {
            mCurrentPage = 3;
        }
        mInviteCodeFrag = (InviteCodeFrag) fragments.get(INVITE_CODE);
        if (mInviteCodeFrag == null){
            mInviteCodeFrag = new InviteCodeFrag();
            fragments.put(INVITE_CODE,mInviteCodeFrag);
            Bundle bundle = new Bundle();
            bundle.putParcelable("config", config);
            mInviteCodeFrag.setArguments(bundle);
        }else {
            mInviteCodeFrag.initStatus(config);
        }
        switchContent(mInviteCodeFrag);
    }

    /*
    替换fragment内容
     */
    private void switchContent(Fragment show) {
        if (mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        if (show != null) {
            if (!show.isAdded()) {
                mFragmentManager.beginTransaction()
                        .add(R.id.frame_rootView, show)
                        .commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction()
                        .show(show)
                        .commitAllowingStateLoss();
            }

            if (fragments != null && fragments.size() > 0) {
                Iterator<String> keys = fragments.keySet().iterator();
                while (keys.hasNext()) {
                    String next = keys.next();
                    BaseFragment baseFragment = fragments.get(next);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            mFragmentManager.beginTransaction()
                                    .hide(baseFragment)
                                    .commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mCurrentPage == 1){
            super.onBackPressed();
        }else {
            loginSms(1, mConfig);
        }
    }

    /**
     * 登录配置信息
     */
    public static class LoginConfig implements Parcelable {

        //登录状态
        public String status;//微信登录状态 0和3绑定手机号需要推荐人  2绑定手机号不需要推荐人  4绑定推荐人
        public String unique_sign;//微信登录openid
        public String member_id;
        public String mobile;//手机号
        public String smsCode;//短信验证码
        public boolean isMobileRegister;//手机号是否注册
        public boolean showPictureCode;//是否显示图像验证码
        public String invite_code;//邀请码
        public LOGIN_MODE login_mode;//登录模式

        public enum LOGIN_MODE{

            /**
             * 密码登录
             */
            PASSWORD_TO_LOGIN,

            /**
             * 短信登录
             */
            SMS_TO_LOGIN,
            /**
             * 绑定邀请码
             */
            BIND_INVITE_CODE
        }

        public LoginConfig() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.status);
            dest.writeString(this.unique_sign);
            dest.writeString(this.member_id);
            dest.writeString(this.mobile);
            dest.writeString(this.smsCode);
            dest.writeByte(this.isMobileRegister ? (byte) 1 : (byte) 0);
            dest.writeByte(this.showPictureCode ? (byte) 1 : (byte) 0);
            dest.writeString(this.invite_code);
            dest.writeInt(this.login_mode == null ? -1 : this.login_mode.ordinal());
        }

        protected LoginConfig(Parcel in) {
            this.status = in.readString();
            this.unique_sign = in.readString();
            this.member_id = in.readString();
            this.mobile = in.readString();
            this.smsCode = in.readString();
            this.isMobileRegister = in.readByte() != 0;
            this.showPictureCode = in.readByte() != 0;
            this.invite_code = in.readString();
            int tmpLogin_mode = in.readInt();
            this.login_mode = tmpLogin_mode == -1 ? null : LOGIN_MODE.values()[tmpLogin_mode];
        }

        public static final Creator<LoginConfig> CREATOR = new Creator<LoginConfig>() {
            @Override
            public LoginConfig createFromParcel(Parcel source) {
                return new LoginConfig(source);
            }

            @Override
            public LoginConfig[] newArray(int size) {
                return new LoginConfig[size];
            }
        };
    }
}
