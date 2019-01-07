package com.shunlian.app.ui.new3_login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.BuildConfig;
import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SelectAccountDialog;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/16.
 * 密码登录
 */

public class LoginPwdFrag extends BaseFragment implements INew3LoginView{

    @BindView(R.id.account)
    AccountControlsWidget account;

    @BindView(R.id.password)
    PwdControlsWidget password;

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_def_login)
    MyTextView mtv_def_login;

    private New3LoginPresenter presenter;
    private DispachJump mJump;
    private VerifyPicDialog mVerifyPicDialog;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_login_pwd_new3, null);
        return view;
    }


    @Override
    protected void initListener() {
        super.initListener();
        account.setOnTextChangeListener(sequence -> changeState());

        password.setOnTextChangeListener(sequence -> changeState());

        mbtnLogin.setOnClickListener(v -> login());
    }

    private void login() {
        if (account != null && password != null){
            String accountText = account.getText().toString();
            String passwordText = password.getText().toString();
            if (isEmpty(accountText)){
                Common.staticToast("账号不能为空");
                return;
            }

            if (isEmpty(passwordText)){
                Common.staticToast("密码不能为空");
                return;
            }

            if (presenter != null){
                presenter.loginPwd(accountText,passwordText);
            }
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));
        presenter = new New3LoginPresenter(baseActivity,this);

        if (BuildConfig.DEBUG){
            visible(mtv_def_login);
        }
    }

    @OnClick(R.id.mtv_def_login)
    public void defLogin(){
        SelectAccountDialog selectAccountDialog=new SelectAccountDialog(this);
        selectAccountDialog.setCanceledOnTouchOutside(true);
        selectAccountDialog.setDefLoginListener((account, pwd) -> {
            if (presenter != null)
                presenter.loginPwd(account, pwd);
        });
        selectAccountDialog.show();
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement() {
        H5X5Act.startAct(baseActivity, InterentTools.H5_HOST
                + LoginEntryAct.TERMS_OF_SERVICE, H5X5Act.MODE_SONIC);
    }


    private void changeState(){
        if (account != null && !isEmpty(account.getText())
                && password != null && !isEmpty(password.getText())){
            if (mbtnLogin != null){
                GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
                btnDrawable.setColor(getColorResouce(R.color.pink_color));
            }
        }else {
            if (mbtnLogin != null){
                GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
                btnDrawable.setColor(Color.parseColor("#ECECEC"));
            }
        }
    }

    /**
     * 设置图形验证码
     *
     * @param bytes
     */
    @Override
    public void setCode(byte[] bytes) {
        if (mVerifyPicDialog == null) {
            mVerifyPicDialog = new VerifyPicDialog(baseActivity);
            mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
            mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mVerifyPicDialog.setImagViewCode(bitmap);
        mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
            String verifyText = mVerifyPicDialog.getVerifyText();
            if (presenter != null) {
                String accounts = account.getText().toString();
                presenter.checkPictureCode(accounts,verifyText);
            }
        }, "取消", v -> mVerifyPicDialog.dismiss()).show();
    }


    /**
     * 检验图形验证码
     * @param error
     */
    @Override
    public void checkPictureCode(String error) {
        if (isEmpty(error)){
            if (mVerifyPicDialog != null){
                mVerifyPicDialog.dismiss();
            }
            login();
        }else {
            if (mVerifyPicDialog != null){
                mVerifyPicDialog.setPicTip(error);
                if (presenter != null){
                    presenter.getPictureCode();
                }
            }
        }
    }

    /**
     * 账号密码登录
     *
     * @param content
     */
    @Override
    public void accountPwdSuccess(LoginFinishEntity content) {
        if (content == null && presenter != null){
            presenter.getPictureCode();
            return;
        }
        //登陆成功啦

        SharedPrefUtil.saveSharedUserString("token", content.token);
        SharedPrefUtil.saveSharedUserString("avatar", content.avatar);
        SharedPrefUtil.saveSharedUserString("plus_role", content.plus_role);
        SharedPrefUtil.saveSharedUserString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", content.member_id);
        SharedPrefUtil.saveSharedUserString("nickname", content.nickname);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(content.member_id);
        if (content.tag!=null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(content.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        EasyWebsocketClient.getInstance(getActivity()).initChat(); //初始化聊天
        MessageCountManager.getInstance(getActivity()).initData();

        Common.handleTheRelayJump(baseActivity);

        if (!"1".equals(content.is_tag)){
            SexSelectAct.startAct(baseActivity);
        }
        baseActivity.finish();
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
