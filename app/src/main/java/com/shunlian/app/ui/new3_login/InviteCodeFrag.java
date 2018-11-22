package com.shunlian.app.ui.new3_login;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashSet;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/19.
 * 填写邀请码
 */

public class InviteCodeFrag extends BaseFragment implements INew3LoginView{

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_rule)
    MyTextView mtvRule;

    @BindView(R.id.invite_code)
    InviteCodeWidget invite_code;

    private VerifyPicDialog mVerifyPicDialog;
    private New3LoginPresenter presenter;
    private DispachJump mJump;
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
        View view = inflater.inflate(R.layout.frag_invite_code_new3, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        invite_code.setOnTextChangeListener(sequence -> {
            GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
            if (!isEmpty(sequence)){
                btnDrawable.setColor(getColorResouce(R.color.pink_color));
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
        EventBus.getDefault().register(this);
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));

        mtvRule.setText("邀请码规则:\n" +
                "1：可联系您的好友获取顺联动力邀请码\n" +
                "2：一个用户只能绑定一个邀请码，绑定后不得修改");

        //如果有推荐人，直接填写推荐人
        String inviteCode = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(inviteCode)){
            invite_code.setInviteCodeText(inviteCode);
        }

        mConfig = getArguments().getParcelable("config");
        presenter = new New3LoginPresenter(baseActivity,this);
    }

    public void initStatus(New3LoginAct.LoginConfig config) {
        mConfig = config;
        //如果有推荐人，直接填写推荐人
        String inviteCode = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(inviteCode)){
            invite_code.setInviteCodeText(inviteCode);
        }else {
            invite_code.setInviteCodeText("");
        }
    }


    @OnClick(R.id.mbtn_login)
    public void bindInviteCode(){
        if (invite_code == null || isEmpty(invite_code.getText())){
            return;
        }

        String code = invite_code.getText().toString();
        if (presenter != null){
            presenter.codeDetail(code);
        }
    }

    /**
     * 邀请码详情
     *
     * @param bean
     */
    @Override
    public void codeInfo(MemberCodeListEntity bean) {
        mVerifyPicDialog = new VerifyPicDialog(baseActivity);
        mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
        mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        mVerifyPicDialog.setMessage("请确认您的导购专员");
        mVerifyPicDialog.showState(2);
        mVerifyPicDialog.setMemberDetail(bean.info);
        mVerifyPicDialog.setSureAndCancleListener("确认绑定", v -> {
            String code = invite_code.getText().toString();
            if (presenter != null && mConfig != null){
                if ("4".equals(mConfig.status)){//绑定导购员
                    mConfig.invite_code = code;
                    ((New3LoginAct)baseActivity).loginSms(2,mConfig);
                }else {
                    presenter.register(mConfig.mobile, mConfig.smsCode, code, mConfig.unique_sign);
                }
            }
            mVerifyPicDialog.dismiss();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
