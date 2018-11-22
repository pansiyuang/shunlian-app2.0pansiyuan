package com.shunlian.app.ui.new3_login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.VerificationCodeInput;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/11/17.
 * 验证手机号
 */

public class VerifyMobileFrag extends BaseFragment implements INew3LoginView{

    @BindView(R.id.mtv_mobile)
    MyTextView mtv_mobile;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.mtv_reset)
    MyTextView mtv_reset;

    private VerifyPicDialog mVerifyPicDialog;
    private CountDownTimer countDownTimer;
    private New3LoginPresenter presenter;
    private DispachJump mJump;
    private String mSmsCode;
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
        View view = inflater.inflate(R.layout.frag_verify_mobile, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        input_code.setOnCompleteListener(content -> {
            mSmsCode = content;
            if (presenter != null){
                if (mConfig != null && mConfig.isMobileRegister) {
                    presenter.loginMobile(mConfig.mobile, content);
                }else {
                    presenter.checkSmsCode(mConfig.mobile,content);
                }
            }
        });

        mtv_reset.setOnClickListener(v -> {
            if (presenter != null && mConfig != null && "3".equals(mConfig.showPictureCode)){
                presenter.getPictureCode();
            }else {
                if (presenter != null && mConfig != null){
                    presenter.sendSmsCode(mConfig.mobile,"");
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        presenter = new New3LoginPresenter(baseActivity,this);
        mConfig = getArguments().getParcelable("config");
        if (mConfig != null) {
            mtv_mobile.setText(mConfig.mobile);
            if ("3".equals(mConfig.showPictureCode)){
                presenter.getPictureCode();
            }else {
                countDown();
            }
        }
    }

    public void initStatus(New3LoginAct.LoginConfig config) {
       mConfig = config;
        if (input_code != null){
            input_code.clearAll();
        }
        if (mConfig != null) {
            mtv_mobile.setText(mConfig.mobile);
            if ("3".equals(mConfig.showPictureCode)){
                presenter.getPictureCode();
            }else {
                countDown();
            }
        }
    }

    private void countDown() {
        String format = "(%ds)";
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_reset.setText(String.format(format,(int) Math.floor(l / 1000)));
                    mtv_reset.setEnabled(false);
                    mtv_reset.setTextColor(getColorResouce(R.color.value_484848));
                }

                @Override
                public void onFinish() {
                    mtv_reset.setTextColor(getColorResouce(R.color.pink_color));
                    mtv_reset.setText(getString(R.string.LoginPswFrg_cxhq));
                    mtv_reset.setEnabled(true);
                }
            };
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    /**
     * 设置图形验证码
     *
     * @param bytes
     */
    @Override
    public void setCode(byte[] bytes) {
        mVerifyPicDialog = new VerifyPicDialog(baseActivity);
        mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
        mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mVerifyPicDialog.setImagViewCode(bitmap);
        mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
            countDown();
            String verifyText = mVerifyPicDialog.getVerifyText();
            if (presenter != null && mConfig != null){
                presenter.sendSmsCode(mConfig.mobile,verifyText);
            }
        }, "取消", v -> mVerifyPicDialog.dismiss()).show();
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
    public void smsCode(String state,String message) {
        if (isEmpty(message) && mVerifyPicDialog != null){
            mVerifyPicDialog.dismiss();
        }else if (!isEmpty(message) && mVerifyPicDialog != null){
            mVerifyPicDialog.setPicTip(message);
        }
    }

    @Override
    public void checkSmsCode(String msg, String vcode_status) {
        if (isEmpty(msg) && "3".equals(vcode_status) && presenter != null){
            if (mConfig != null)mConfig.showPictureCode = vcode_status;
            presenter.getPictureCode();
        }else if (isEmpty(msg) && mConfig != null){
            if ("0".equals(mConfig.status) || "3".equals(mConfig.status)) {//绑定手机号和导购员
                mConfig.smsCode = mSmsCode;
                ((New3LoginAct) baseActivity).loginInviteCode(mConfig);
            }else if ("2".equals(mConfig.status)){
                if (presenter != null){//绑定手机号
                    presenter.register(mConfig.mobile,mSmsCode,"",mConfig.unique_sign);
                }
            }else if ("4".equals(mConfig.status)){
                if (presenter != null){//绑定导购员
                    presenter.bindShareid(mConfig.member_id,mConfig.invite_code,mConfig.mobile,mSmsCode);
                }
            }
        }
    }

    @Subscribe(sticky = true)
    public void eventBus(DispachJump jump) {
        mJump = jump;
    }

    @Override
    public void loginMobileSuccess(LoginFinishEntity content) {
        //登陆成功啦
        SharedPrefUtil.saveSharedUserString("token", content.token);
        SharedPrefUtil.saveSharedUserString("avatar", content.avatar);
        SharedPrefUtil.saveSharedUserString("plus_role", content.plus_role);
        SharedPrefUtil.saveSharedUserString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", content.member_id);
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

        if (mJump != null){
            Common.goGoGo(baseActivity,mJump.jumpType,mJump.items);
        }

        if (!"1".equals(content.is_tag)){
            SexSelectAct.startAct(baseActivity);
        }
        baseActivity.finish();
    }
}
