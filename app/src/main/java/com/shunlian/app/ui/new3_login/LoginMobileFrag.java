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
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/17.
 * 手机号登录
 */
public class LoginMobileFrag extends BaseFragment {


    @BindView(R.id.mobile)
    AccountControlsWidget mobile;

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

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
                visible(mtv_tip);
                mtv_tip.setText("请输入正确手机号");
            }else {
                mtv_tip.setVisibility(View.INVISIBLE);
            }

            if (s.length()<=0)mtv_tip.setVisibility(View.INVISIBLE);

            GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
            if (mobile.getText().length() == 11){
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
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));
    }

    @OnClick(R.id.mbtn_login)
    public void next(){
        String mobile = this.mobile.getText().toString();
        if (isEmpty(mobile)){
            Common.staticToast("请输入手机号");
            return;
        }
        ((New3LoginAct)baseActivity).loginSms(2,mobile);
    }

    @OnClick(R.id.llayout_login_agreement)
    public void loginAgreement() {
        H5X5Act.startAct(baseActivity, InterentTools.H5_HOST
                + LoginEntryAct.TERMS_OF_SERVICE, H5X5Act.MODE_SONIC);
    }
}
