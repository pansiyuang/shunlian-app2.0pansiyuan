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
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/16.
 * 密码登录
 */

public class LoginPwdFrag extends BaseFragment {

    @BindView(R.id.account)
    AccountControlsWidget account;

    @BindView(R.id.password)
    PwdControlsWidget password;

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

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
        account.setOnTextChangeListener(sequence -> {
            changeState();
        });

        password.setOnTextChangeListener(sequence -> {
            changeState();
        });

        mbtnLogin.setOnClickListener(v -> {
            if (account != null && password != null){
                CharSequence accountText = account.getText();
                CharSequence passwordText = password.getText();
                if (isEmpty(accountText)){
                    Common.staticToast("账号不能为空");
                    return;
                }

                if (isEmpty(passwordText)){
                    Common.staticToast("密码不能为空");
                    return;
                }


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
}
