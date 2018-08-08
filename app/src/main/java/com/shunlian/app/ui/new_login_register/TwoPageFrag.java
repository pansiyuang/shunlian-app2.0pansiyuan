package com.shunlian.app.ui.new_login_register;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.RegisterAndBindPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IRegisterAndBindView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.VerificationCodeInput;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/24.
 */

public class TwoPageFrag extends BaseFragment implements IRegisterAndBindView{

    @BindView(R.id.mtv_mobile)
    MyTextView mtv_mobile;

    @BindView(R.id.mtv_downTime)
    MyTextView mtv_downTime;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.rlayout_nickname)
    RelativeLayout rlayout_nickname;

    @BindView(R.id.met_nickname)
    EditText met_nickname;

    @BindView(R.id.mbtn_login)
    MyButton mbtn_login;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;


    private CountDownTimer countDownTimer;
    private RegisterAndBindPresenter mPresenter;
    private String mSmsCode;//用户输入短信验证码
    private String mMobile;
    private String picCode;
    private String unique_sign;
    private String mMember_id;
    private String refereesId;
    private boolean isRuning = false;
    private int mFlag;
    private DispachJump mJump;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_two_page,null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();

        met_nickname.setOnTouchListener((v, event) ->{
            if (!isRuning){
                isRuning = true;
                runAnimation("昵称",R.id.rlayout_nickname,met_nickname);
            }
            return false;
        });

        input_code.setOnCompleteListener(content -> mSmsCode = content);
    }

    private void runAnimation(String text,int subject,EditText view) {
        TextView tv = new TextView(baseActivity);
        tv.setTextColor(getColorResouce(R.color.color_value_6c));
        tv.setTextSize(14);
        tv.setText(text);
        rlayout_root.addView(tv);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_TOP,subject);
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT,subject);
        tv.setLayoutParams(layoutParams);


        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
                0,Animation.RELATIVE_TO_SELF,-1);
        ta.setFillAfter(true);
        ta.setDuration(200);
        tv.setAnimation(ta);
        ta.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setHint("");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        mPresenter = new RegisterAndBindPresenter(baseActivity,this);
        Bundle arguments = getArguments();
        mMobile = arguments.getString("mobile");
        refereesId = arguments.getString("refereesId");
        picCode = arguments.getString("picCode");
        unique_sign = arguments.getString("unique_sign");
        mMember_id = arguments.getString("member_id");
        mFlag = arguments.getInt("flag");
        mtv_mobile.setText(mMobile);
        if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){
            visible(rlayout_nickname);
        }else {
            gone(rlayout_nickname);
        }
        if (mFlag == RegisterAndBindingAct.FLAG_FIND_PWD){
            mbtn_login.setText("下一步");
        }else {
            mbtn_login.setText("登录");
        }
        countDown();
        ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(false);
    }

    /**
     *
     * @param refereesId 导购id
     * @param mobile 手机号
     * @param picCode 图形验证码
     * @param unique_sign 微信登录code
     * @param flag 状态
     */
    public void resetPage(String refereesId, String mobile, String picCode,
                          String unique_sign,String member_id, int flag) {
        this.refereesId = refereesId;
        this.mMobile = mobile;
        this.picCode = picCode;
        this.mMember_id = member_id;
        this.mFlag = flag;
        this.unique_sign = unique_sign;
        mtv_mobile.setText(mMobile);
        if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){
            visible(rlayout_nickname);
            met_nickname.setText("");
        }else {
            gone(rlayout_nickname);
        }
        if (mFlag == RegisterAndBindingAct.FLAG_FIND_PWD){
            mbtn_login.setText("下一步");
        }else {
            mbtn_login.setText("登录");
        }
        countDown();
        input_code.clearAll();
        ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(false);
    }

    @OnClick(R.id.mtv_downTime)
    public void resetDownTime(){
        if (mPresenter != null){
            mPresenter.sendSmsCode(mMobile,picCode);
        }
    }

    @OnClick(R.id.mbtn_login)
    public void btnLogin(){
        if (!isEmpty(mSmsCode) && mPresenter != null){
            if (mFlag == RegisterAndBindingAct.FLAG_LOGIN){//登录
                mPresenter.loginMobile(mMobile,mSmsCode);
            }else if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){//注册
                String nickname = met_nickname.getText().toString().trim();
                if (isEmpty(nickname)){
                    Common.staticToast("请填写昵称");
                    return;
                }
                mPresenter.register(mMobile,mSmsCode,refereesId,nickname,unique_sign);
            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE_ID){//绑定
                mPresenter.register(mMobile,mSmsCode,refereesId,"",unique_sign);
            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE){//绑定手机号
                mPresenter.register(mMobile,mSmsCode,refereesId,"",unique_sign);
            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_ID){//绑定id
                mPresenter.bindShareid(mMember_id,refereesId,mMobile,mSmsCode);
            }else if (mFlag == RegisterAndBindingAct.FLAG_FIND_PWD){//找回密码
                mPresenter.checkMobileCode(mMobile,mSmsCode);
            }
        }else {
            Common.staticToast("请输入短信验证码");
        }
    }

    private void countDown() {
        String format = "%d秒后重新获取";
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    mtv_downTime.setText(String.format(format,(int) Math.floor(l / 1000)));
                    mtv_downTime.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    mtv_downTime.setText(getString(R.string.LoginPswFrg_cxhq));
                    mtv_downTime.setEnabled(true);
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
        isRuning = false;
        EventBus.getDefault().unregister(this);
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
    public void smsCode(String message) {
        if (!isEmpty(message) && countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    @Override
    public void checkMobileSmsCode(String message) {
        ((RegisterAndBindingAct)baseActivity).findPwd(mMobile,mSmsCode);
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
        if (content.tag!=null)
            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(content.tag));
        JpushUtil.setJPushAlias();
        //通知登录成功
        DefMessageEvent event = new DefMessageEvent();
        event.loginSuccess = true;
        EventBus.getDefault().post(event);

//        if (Constant.JPUSH != null && !"login".equals(Constant.JPUSH.get(0))) {
//            Common.goGoGo(baseActivity, Constant.JPUSH.get(0), Constant.JPUSH.get(1), Constant.JPUSH.get(2)
//                    ,Constant.JPUSH.get(3),Constant.JPUSH.get(4),Constant.JPUSH.get(5),Constant.JPUSH.get(6),Constant.JPUSH.get(7)
//                    ,Constant.JPUSH.get(8),Constant.JPUSH.get(9),Constant.JPUSH.get(10),Constant.JPUSH.get(11),Constant.JPUSH.get(12));
//        }

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
