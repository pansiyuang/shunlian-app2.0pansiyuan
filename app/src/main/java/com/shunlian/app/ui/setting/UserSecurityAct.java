package com.shunlian.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.ChangeUserPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.setting.change_user.ModifyAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IChangeUserView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

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

    @BindView(R.id.mtv_pwd_setting)
    MyTextView mtv_pwd_setting;

    public static void startAct(Context context, String isSetPwd) {
        context.startActivity(new Intent(context, UserSecurityAct.class)
        .putExtra("isSetPwd",isSetPwd));
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
        String mIsSetPwd = getIntent().getStringExtra("isSetPwd");
        if ("1".equals(mIsSetPwd)){
            mtv_pwd_setting.setText("已设置");
        }else {
            mtv_pwd_setting.setText("未设置");
        }
        mtv_toolbar_title.setText("设置");
        gone(mrlayout_toolbar_more);

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
                    EasyWebsocketClient.getInstance(this).logout();
                    Common.goGoGo(this, "");
                    promptDialog.dismiss();
                }, "取消", (v) -> promptDialog.dismiss()).show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.llayout_login_user:
                ModifyAct.startAct(this,false,false,null);
                break;
            case R.id.llayout_login_pwd:
                ModifyAct.startAct(this,false,true,null);
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

