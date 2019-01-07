//package com.shunlian.app.ui.new_login_register;
//
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.TranslateAnimation;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.shunlian.app.R;
//import com.shunlian.app.presenter.RegisterAndBindPresenter;
//import com.shunlian.app.ui.BaseFragment;
//import com.shunlian.app.utils.Common;
//import com.shunlian.app.view.IRegisterAndBindView;
//import com.shunlian.app.widget.MyButton;
//import com.shunlian.app.widget.MyImageView;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
///**
// * Created by zhanghe on 2018/8/7.
// */
//
//public class FindPwdFrag extends BaseFragment implements IRegisterAndBindView{
//
//    @BindView(R.id.rlayout_root)
//    RelativeLayout rlayout_root;
//
//    @BindView(R.id.rlayout_pwd)
//    RelativeLayout rlayout_pwd;
//
//    @BindView(R.id.met_pwd)
//    EditText met_pwd;
//
//    @BindView(R.id.rlayout_confirm_pwd)
//    RelativeLayout rlayout_confirm_pwd;
//
//    @BindView(R.id.met_confirm_pwd)
//    EditText met_confirm_pwd;
//
//    @BindView(R.id.miv_tip2)
//    MyImageView miv_tip2;
//
//    @BindView(R.id.miv_tip1)
//    MyImageView miv_tip1;
//
//    @BindView(R.id.mbtn_login)
//    MyButton mbtn_login;
//
//    private boolean isRuning1 = false;
//    private boolean isRuning2 = false;
//
//    private boolean isHiddenPwd = true;
//    private boolean isHiddenRPwd = true;
//
//    private String mMobile;
//    private String mSmsCode;
//    private RegisterAndBindPresenter presenter;
//
//    /**
//     * 设置布局id
//     *
//     * @param inflater
//     * @param container
//     * @return
//     */
//    @Override
//    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
//        View view = inflater.inflate(R.layout.find_pwd_layout, null);
//        return view;
//    }
//
//
//    @Override
//    protected void initListener() {
//        super.initListener();
//        miv_tip1.setOnClickListener(this);
//        miv_tip2.setOnClickListener(this);
//
//        met_pwd.setOnTouchListener((v, event) ->{
//            if (!isRuning1){
//                isRuning1 = true;
//                runAnimation("新密码",R.id.rlayout_pwd,met_pwd);
//            }
//            return false;
//        });
//
//        met_confirm_pwd.setOnTouchListener((v, event) ->{
//            if (!isRuning2){
//                isRuning2 = true;
//                runAnimation("确认密码",R.id.rlayout_confirm_pwd,met_confirm_pwd);
//            }
//            return false;
//        });
//    }
//
//    /**
//     * 初始化数据
//     */
//    @Override
//    protected void initData() {
//        Bundle arguments = getArguments();
//        mMobile = arguments.getString("mobile");
//        mSmsCode = arguments.getString("smsCode");
//        presenter = new RegisterAndBindPresenter(baseActivity,this);
//    }
//
//    public void resetPage(String mobile, String smsCode) {
//        this.mMobile = mobile;
//        this.mSmsCode = smsCode;
//        met_pwd.setText("");
//        met_confirm_pwd.setText("");
//    }
//
//    @OnClick(R.id.mbtn_login)
//    public void goLogin(){
//        String pwd = met_pwd.getText().toString().trim();
//        String rpwd = met_confirm_pwd.getText().toString().trim();
//        if (!pwd.equals(rpwd)){
//            Common.staticToast("密码不一致");
//            return;
//        }
//        if (presenter != null){
//            presenter.findPsw(mMobile,pwd,rpwd,mSmsCode);
//        }
//    }
//
//
//    private void runAnimation(String text,int subject,EditText view) {
//        TextView tv = new TextView(baseActivity);
//        tv.setTextColor(getColorResouce(R.color.color_value_6c));
//        tv.setTextSize(14);
//        tv.setText(text);
//        rlayout_root.addView(tv);
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tv.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.ALIGN_TOP,subject);
//        layoutParams.addRule(RelativeLayout.ALIGN_LEFT,subject);
//        tv.setLayoutParams(layoutParams);
//
//
//        TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
//                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
//                0,Animation.RELATIVE_TO_SELF,-1);
//        ta.setFillAfter(true);
//        ta.setDuration(200);
//        tv.setAnimation(ta);
//        ta.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                view.setHint("");
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    private void isShowPwd(EditText editText, boolean isShow) {
//        if (isShow) {//显示
//            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//        } else {
//            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        }
//        editText.setSelection(editText.getText().length());
//    }
//
//    @Override
//    public void onClick(View view) {
//        super.onClick(view);
//        switch (view.getId()){
//            case R.id.miv_tip1:
//                miv_tip1.setImageResource(isHiddenPwd?R.mipmap.icon_login_eyes_h:R.mipmap.icon_login_eyes_n);
//                isShowPwd(met_pwd,isHiddenPwd);
//                isHiddenPwd = !isHiddenPwd;
//                break;
//            case R.id.miv_tip2:
//                miv_tip2.setImageResource(isHiddenRPwd?R.mipmap.icon_login_eyes_h:R.mipmap.icon_login_eyes_n);
//                isShowPwd(met_confirm_pwd,isHiddenRPwd);
//                isHiddenRPwd = !isHiddenRPwd;
//                break;
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        isRuning1 = false;
//        isRuning2 = false;
//    }
//
//    @Override
//    public void findPwdSuccess(String message) {
//        Common.staticToast(message);
//        LoginEntryAct.startAct(baseActivity);
//        baseActivity.finish();
//    }
//
//    /**
//     * 显示网络请求失败的界面
//     *
//     * @param request_code
//     */
//    @Override
//    public void showFailureView(int request_code) {
//
//    }
//
//    /**
//     * 显示空数据界面
//     *
//     * @param request_code
//     */
//    @Override
//    public void showDataEmptyView(int request_code) {
//
//    }
//
//}
