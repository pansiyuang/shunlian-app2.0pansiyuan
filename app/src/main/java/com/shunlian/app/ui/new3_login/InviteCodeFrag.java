package com.shunlian.app.ui.new3_login;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/11/19.
 * 填写邀请码
 */

public class InviteCodeFrag extends BaseFragment {

    @BindView(R.id.mbtn_login)
    MyButton mbtnLogin;

    @BindView(R.id.mtv_rule)
    MyTextView mtvRule;
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
        View view = inflater.inflate(R.layout.frag_invite_code_new3, null);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        GradientDrawable btnDrawable = (GradientDrawable) mbtnLogin.getBackground();
        btnDrawable.setColor(Color.parseColor("#ECECEC"));

        mtvRule.setText("邀请码规则:\n" +
                "1：可联系您的好友获取顺联动力邀请码\n" +
                "2：一个用户只能绑定一个邀请码，绑定后不得修改");
    }


    @OnClick(R.id.mbtn_login)
    public void bindInviteCode(){
        showCode();
    }

    private void showCode() {
        mVerifyPicDialog = new VerifyPicDialog(baseActivity);
        mVerifyPicDialog.setTvSureColor(R.color.value_007AFF);
        mVerifyPicDialog.setTvSureBgColor(Color.WHITE);
        mVerifyPicDialog.setMessage("请确认您的导购专员");
        mVerifyPicDialog.showState(2);
        mVerifyPicDialog.setSureAndCancleListener("确认绑定", v -> {
            String verifyText = mVerifyPicDialog.getVerifyText();
            System.out.println("====verifyText========="+verifyText);
            mVerifyPicDialog.dismiss();
        }, "取消", v -> mVerifyPicDialog.dismiss()).show();
    }
}
