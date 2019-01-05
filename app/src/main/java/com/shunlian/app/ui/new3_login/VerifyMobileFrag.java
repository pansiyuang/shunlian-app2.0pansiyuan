package com.shunlian.app.ui.new3_login;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.VerificationCodeInput;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;

import java.util.HashSet;

import butterknife.BindView;

import static com.shunlian.app.ui.new3_login.New3LoginAct.LoginConfig.LOGIN_MODE.BIND_INVITE_CODE;
import static com.shunlian.app.ui.new3_login.New3LoginAct.LoginConfig.LOGIN_MODE.SMS_TO_LOGIN;

/**
 * Created by zhanghe on 2018/11/17.
 * 验证手机号
 */

public class VerifyMobileFrag extends BaseFragment implements INew3LoginView {

    @BindView(R.id.mtv_mobile)
    MyTextView mtv_mobile;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.mtv_reset)
    MyTextView mtv_reset;

    @BindView(R.id.mtv_smsLoginTip)
    MyTextView mtv_smsLoginTip;

    @BindView(R.id.miv_avatar)
    ImageView miv_avatar;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_invite_code)
    MyTextView mtv_invite_code;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;


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
            if (presenter != null && mConfig != null) {
                presenter.checkSmsCode(mConfig.mobile, content);
            }
        });

        mtv_reset.setOnClickListener(v -> {
            if (presenter != null && mConfig != null) {
                presenter.sendSmsCode(mConfig.mobile, "");
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        presenter = new New3LoginPresenter(baseActivity, this);
        presenter.loginInfoTip();
        mConfig = getArguments().getParcelable("config");
        dispatchApi();
    }

    public void initStatus(New3LoginAct.LoginConfig config) {
        mConfig = config;
        if (input_code != null) {
            input_code.clearAll();
        }
        dispatchApi();
    }

    private void dispatchApi() {
        String inviteCode = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(inviteCode) && presenter != null){
            presenter.codeDetail(inviteCode);
        }
        if (mConfig != null&&mtv_mobile!=null) {
            if (!isEmpty(mConfig.mobile) && mConfig.mobile.length() == 11){
                String temp = "";//130 0756 2706
                for (int i = 0; i < mConfig.mobile.length(); i++) {
                    temp += mConfig.mobile.charAt(i);
                    if (i == 2 || i == 6){
                        temp += " ";
                    }
                }
                mtv_mobile.setText(temp);
            }else {
                mtv_mobile.setText(mConfig.mobile);
            }
            countDown();
            if (mConfig.login_mode == BIND_INVITE_CODE && presenter != null) {//绑定邀请码
                presenter.sendSmsCode(mConfig.mobile, "");
            }
        }
    }

    private void countDown() {
        String format = "(%ds)";
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_reset.setText(String.format(format, (int) Math.floor(l / 1000)));
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
        if (mVerifyPicDialog == null) {
            mVerifyPicDialog = new VerifyPicDialog(baseActivity);
            mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
            mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        mVerifyPicDialog.setImagViewCode(bitmap);
        mVerifyPicDialog.setSureAndCancleListener("确认", v -> {
            String verifyText = mVerifyPicDialog.getVerifyText();
            if (presenter != null && mConfig != null) {
                presenter.checkPictureCode(mConfig.mobile,verifyText);
            }
        }, "取消", v -> mVerifyPicDialog.dismiss()).show();
    }

    @Override
    public void setLoginInfoTip(New3LoginInfoTipEntity data) {
        if (data != null && mtv_smsLoginTip != null){
            if (!isEmpty(data.login_title)) {
                mtv_smsLoginTip.setText(data.login_title);
                visible(mtv_smsLoginTip);
            }else {
                gone(mtv_smsLoginTip);
            }
        }
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
            if (presenter != null && mConfig != null){
                presenter.checkSmsCode(mConfig.mobile,mSmsCode);
            }
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
    public void smsCode(int showPictureCode,String error) {
        if (showPictureCode == 0) {
            input_code.clearAll();
            countDown();
        }
    }

    /**
     * 邀请码详情
     *
     * @param bean
     */
    @Override
    public void codeInfo(MemberCodeListEntity bean, String error) {
        if (bean != null && bean.info != null){
            visible(rlayout_root);
            MemberCodeListEntity.ListBean info = bean.info;
            mtv_invite_code.setText("邀请码："+info.code);
            mtv_title.setText(info.nickname+"邀请了您加入顺联动力");
            GlideUtils.getInstance().loadCircleHeadImage(baseActivity,miv_avatar,info.avatar);
        }
    }

    /**
     * 检查短信验证码
     * @param showPictureCode 1显示图像验证码
     * @param error 错误信息
     */
    @Override
    public void checkSmsCode(int showPictureCode,String error) {
        if (showPictureCode == 1 && presenter != null) {
            presenter.getPictureCode();//获取图形验证码
        }else if (showPictureCode == 2){
            input_code.clearAll();//短信验证码输入错误清空输入内容
        } else if (showPictureCode == 0 && mConfig != null) {
            if (isEmpty(mConfig.status) && mConfig.isMobileRegister && mConfig.login_mode == SMS_TO_LOGIN) {
                presenter.loginMobile(mConfig.mobile, mSmsCode);//登录
            }else if ("2".equals(mConfig.status)) {
                if (presenter != null) {//绑定手机号
                    presenter.register(mConfig.mobile, mSmsCode, "", mConfig.unique_sign);
                }
            }else if (mConfig.login_mode == BIND_INVITE_CODE) {
                if (presenter != null) {//绑定导购员
                    presenter.bindShareid(mConfig.member_id, mConfig.invite_code, mConfig.mobile, mSmsCode);
                }
            }else if ("0".equals(mConfig.status) || "3".equals(mConfig.status)
                    || (!mConfig.isMobileRegister && isEmpty(mConfig.status))) {//绑定手机号和导购员
                mConfig.smsCode = mSmsCode;
                if ("0".equals(mConfig.invite_code)){
                    ((New3LoginAct) baseActivity).loginInviteCode(mConfig);
                }else {//微信绑定有上级的手机号，不需要手动绑定上级
                    if (presenter != null) {
                        presenter.register(mConfig.mobile, mSmsCode, "", mConfig.unique_sign);
                    }
                }
            }
        }
    }

    @Override
    public void loginMobileSuccess(LoginFinishEntity content) {
        //登陆成功啦
        SharedPrefUtil.saveSharedUserString("token", content.token);
        SharedPrefUtil.saveSharedUserString("avatar", content.avatar);
        SharedPrefUtil.saveSharedUserString("plus_role", content.plus_role);
        SharedPrefUtil.saveSharedUserString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", content.member_id);
        SharedPrefUtil.saveSharedUserString("nickname", content.nickname);
        SensorsDataAPI.sharedInstance().login(SharedPrefUtil.getSharedUserString("member_id", ""));
        CrashReport.setUserId(content.member_id);
        if (content.tag != null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(content.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

        EasyWebsocketClient.getInstance(getActivity()).initChat(); //初始化聊天
        MessageCountManager.getInstance(getActivity()).initData();

        Common.handleTheRelayJump(baseActivity);

        if (!"1".equals(content.is_tag)) {
            SexSelectAct.startAct(baseActivity);
        }
        baseActivity.finish();
    }
}
