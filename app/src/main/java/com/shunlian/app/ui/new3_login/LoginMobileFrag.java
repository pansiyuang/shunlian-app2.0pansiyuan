package com.shunlian.app.ui.new3_login;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.new_login_register.LoginEntryAct;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/17.
 * 手机号登录
 */
public class LoginMobileFrag extends BaseFragment implements INew3LoginView{


    @BindView(R.id.mobile)
    AccountControlsWidget mobile;

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

    @BindView(R.id.mtv_mobileLoginTip)
    MyTextView mtv_mobileLoginTip;

    private New3LoginPresenter presenter;
    private New3LoginAct.LoginConfig mConfig;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_login_mobile_new3, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mobile.setOnTextChangeListener(s -> {
            if (!s.toString().startsWith("1")){
                showMobileTip("请输入正确手机号");
            }else {
                mtv_tip.setVisibility(View.INVISIBLE);
            }

            if (s.length()<=0)mtv_tip.setVisibility(View.INVISIBLE);

            GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
            String mobile_text = mobile.getText().toString();
            if (mobile_text.length() == 11){
                btnDrawable.setColor(getColorResouce(R.color.pink_color));
                if (presenter != null){
                    presenter.checkMobile(mobile_text);
                }
            }else {
                btnDrawable.setColor(Color.parseColor("#ECECEC"));
            }

        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));
        presenter = new New3LoginPresenter(baseActivity,this);
        presenter.loginInfoTip();
        mConfig = getArguments().getParcelable("config");
    }

    public void initStatus(New3LoginAct.LoginConfig config) {
        mConfig = config;
    }

    @OnClick(R.id.mbtn_login)
    public void next(){
        if (MyOnClickListener.isFastClick()){
            return;
        }
        String mobile = this.mobile.getText().toString();
        if (isEmpty(mobile) || mobile.length() != 11) return;
        if (mConfig != null && !isEmpty(mConfig.status) &&
                ("2".equals(mConfig.status) || "3".equals(mConfig.status))
                && mConfig.isMobileRegister){
            return;
        }
        if (presenter != null){
            presenter.sendSmsCode(mobile,"");
        }
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement() {
        H5X5Act.startAct(baseActivity, InterentTools.H5_HOST
                + LoginEntryAct.TERMS_OF_SERVICE, H5X5Act.MODE_SONIC);
    }

    /**
     * 手机号是否正确
     * @param b
     */
    @Override
    public void iSMobileRight(boolean b,String msg,String shareid) {
        if (b){
            if (mConfig != null) {
                mConfig.isMobileRegister = "1".equals(msg);
                mConfig.invite_code = shareid;
                if (!isEmpty(mConfig.status) &&
                        ("2".equals(mConfig.status) || "3".equals(mConfig.status))
                        && mConfig.isMobileRegister){
                    showMobileTip("该手机号已注册");
                }
            }
        }else {
            showMobileTip(msg);
        }
    }

    /**
     * 显示网络请求失败的界面
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {}

    /**
     * 显示空数据界面
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {}
    /**
     * 短信验证码
     * @param showPictureCode 1显示图像验证码 0 1都要跳界面
     * @param error 错误信息
     */
    @Override
    public void smsCode(int showPictureCode,String error) {
        if (showPictureCode == 0){
            String mobile = this.mobile.getText().toString();
            if (mConfig != null) {
                mConfig.mobile = mobile;
            }
            ((New3LoginAct)baseActivity).loginSms(2,mConfig);
        }
    }

    @Override
    public void setLoginInfoTip(New3LoginInfoTipEntity data) {
        if (data != null && mtv_mobileLoginTip != null){
            if (!isEmpty(data.login_title)) {
                mtv_mobileLoginTip.setText(data.login_title);
                visible(mtv_mobileLoginTip);
            }else {
                gone(mtv_mobileLoginTip);
            }
        }
    }

    /**
     * 显示手机号提示
     * @param tip
     */
    private void showMobileTip(String tip) {
        visible(mtv_tip);
        mtv_tip.setText(tip);
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));
    }
}
