package com.shunlian.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.presenter.ChangeUserPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.ui.setting.change_user.ChangeUserAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IChangeUserView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.pick_time.PickTimeView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class UserSecurityAct extends BaseActivity implements IChangeUserView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.llayout_login_user)
    LinearLayout llayout_login_user;

    @BindView(R.id.llayout_login_pwd)
    LinearLayout llayout_login_pwd;

    @BindView(R.id.mtv_user)
    MyTextView mtv_user;

    @BindView(R.id.ptv)
    PickTimeView ptv;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, UserSecurityAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_usersecurity;
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_login_user.setOnClickListener(this);
        llayout_login_pwd.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("设置");
        gone(mrlayout_toolbar_more);

        ptv.setViewType(PickTimeView.TYPE_PICK_DATE);

        ChangeUserPresenter presenter = new ChangeUserPresenter(this,this);
        presenter.getMobile();
    }

    @OnClick(R.id.mbtn_logout)
    public void logout() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureColor(R.color.new_text);
        promptDialog.setTvSureBg(R.drawable.bg_dialog_bottomr);
        promptDialog.setSureAndCancleListener("确定要退出登录吗？", "确定",
                (v) -> {
                    Common.clearLoginInfo();
                    JpushUtil.setJPushAlias();
                    Constant.JPUSH = null;
                    LoginAct.startAct(UserSecurityAct.this);
                    /***登录成功后去个人中心***/
                    DispachJump dispachJump = new DispachJump();
                    dispachJump.jumpType = dispachJump.personal;
                    EventBus.getDefault().postSticky(dispachJump);
                    promptDialog.dismiss();
                }, "取消", (v) -> promptDialog.dismiss()).show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.llayout_login_user:
                ChangeUserAct.startAct(this,false,false,null);
                break;
            case R.id.llayout_login_pwd:
                ChangeUserAct.startAct(this,false,true,null);
                break;
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
    public void setMobile(String mobile) {
        mtv_user.setText(mobile);
    }
}

