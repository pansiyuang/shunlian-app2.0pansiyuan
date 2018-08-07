package com.shunlian.app.ui.new_login_register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/8/7.
 */

public class FindPwdFrag extends BaseFragment {

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;

    @BindView(R.id.rlayout_pwd)
    RelativeLayout rlayout_pwd;

    @BindView(R.id.met_pwd)
    EditText met_pwd;

    @BindView(R.id.rlayout_confirm_pwd)
    RelativeLayout rlayout_confirm_pwd;

    @BindView(R.id.met_confirm_pwd)
    EditText met_confirm_pwd;

    @BindView(R.id.miv_tip2)
    MyImageView miv_tip2;

    @BindView(R.id.miv_tip1)
    MyImageView miv_tip1;

    @BindView(R.id.mbtn_login)
    MyButton mbtn_login;

    private boolean isRuning1 = false;
    private boolean isRuning2 = false;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.find_pwd_layout, null);
        return view;
    }


    @Override
    protected void initListener() {
        super.initListener();
        met_pwd.setOnTouchListener((v, event) ->{
            if (!isRuning1){
                isRuning1 = true;
                runAnimation("昵称",R.id.rlayout_pwd,met_pwd);
            }
            return false;
        });

        met_confirm_pwd.setOnTouchListener((v, event) ->{
            if (!isRuning2){
                isRuning2 = true;
                runAnimation("昵称",R.id.rlayout_confirm_pwd,met_confirm_pwd);
            }
            return false;
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

        Bundle arguments = getArguments();
        String mobile = arguments.getString("mobile");

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isRuning1 = false;
        isRuning2 = false;
    }
}
