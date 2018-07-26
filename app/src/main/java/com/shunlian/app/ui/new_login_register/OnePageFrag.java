package com.shunlian.app.ui.new_login_register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.shunlian.app.presenter.RegisterAndBindPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.register.SelectRecommendAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IRegisterAndBindView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/7/23.
 */
public class OnePageFrag extends BaseFragment implements IRegisterAndBindView {

    @BindView(R.id.mtv_tip)
    MyTextView mtv_tip;

    @BindView(R.id.met_id)
    EditText met_id;

    @BindView(R.id.rlayout_id)
    RelativeLayout rlayout_id;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;

    @BindView(R.id.rlayout_mobile)
    RelativeLayout rlayout_mobile;

    @BindView(R.id.met_mobile)
    EditText met_mobile;

    @BindView(R.id.miv_tip)
    MyImageView miv_tip;

    @BindView(R.id.rlayout_pic_code)
    RelativeLayout rlayout_pic_code;

    @BindView(R.id.met_pic_code)
    EditText met_pic_code;

    @BindView(R.id.miv_pic_code)
    MyImageView miv_pic_code;

    @BindView(R.id.mbtn_login)
    MyButton mbtn_login;

    private RegisterAndBindPresenter mPresenter;
    private boolean isRuning1 = false;
    private boolean isRuning2 = false;
    private boolean isRuning3 = false;
    private String mRecommenderId;//推荐人id
    private int mFlag;
    private boolean isRefereesIdRight;
    private boolean iSMobileRight;
    private String mMobile;
    private String mUniqueSign;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_one_page, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        met_id.setOnTouchListener((v, event) ->{
            if (!isRuning1){
                isRuning1 = true;
                runAnimation("导购员ID",R.id.rlayout_id,met_id);
            }
            return false;
        });

        met_mobile.setOnTouchListener((v, event) ->{
            if (!isRuning2){
                isRuning2 = true;
                runAnimation("手机号",R.id.rlayout_mobile,met_mobile);
            }
            return false;
        });

        met_pic_code.setOnTouchListener((v, event) ->{
            if (!isRuning3){
                isRuning3 = true;
                runAnimation("验证码",R.id.rlayout_pic_code,met_pic_code);
            }
            return false;
        });

        //推荐人输入框验证
        addTextChangedListener(met_id,1);
        //手机号输入框验证
        addTextChangedListener(met_mobile,2);
        //图形验证码输入框验证
        addTextChangedListener(met_pic_code,3);
    }

    /**
     * editText输入监听
     * @param editText
     * @param state 1监听输入推荐人  2 监听输入手机号 3 监听输入图形验证码
     */
    private void addTextChangedListener(EditText editText,int state){
        editText.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                switch (state){
                    case 1:
                        checkReferees(s);
                        break;
                    case 2:
                        checkMobileAPI(s);
                        break;
                    case 3:
                        checkPicCode(s);
                        break;
                }
            }
        });
    }

    /**
     * 检验推荐人id
     * @param s
     */
    private void checkReferees(CharSequence s) {

    }

    /**
     * 接口验证手机号
     * @param s
     */
    private void checkMobileAPI(CharSequence s) {
        if (rlayout_id.getVisibility() == View.VISIBLE){//检验推荐人id是否正确
            String refereesId = met_id.getText().toString().trim();
            if (mPresenter != null)
                mPresenter.checkRefereesId(refereesId);
        }
        if (s.length() >= 11){
            if (mPresenter != null){
                if (mFlag == RegisterAndBindingAct.FLAG_LOGIN){
                    //登录验证手机号
                    mPresenter.checkMobile(s.toString().trim(),"1");
                }else {
                    //注册验证手机号
                    mPresenter.checkMobile(s.toString().trim(),"0");
                }
            }
        }
    }

    /**
     * 检验手机图形验证码
     * @param s
     */
    private void checkPicCode(CharSequence s) {
        //如果推荐人id不正确，不允许执行下一步
        if (rlayout_id.getVisibility() == View.VISIBLE && !isRefereesIdRight)return;

        String mobile = met_mobile.getText().toString().trim();
        //如果手机号不正确，不允许执行下一步
        if (isEmpty(mobile) || (!isEmpty(mobile) && mobile.length() != 11) || !iSMobileRight){
            iSMobileRight(false);
            if (mFlag != RegisterAndBindingAct.FLAG_LOGIN)mbtn_login.setEnabled(false);
        }
        if (s.length()>=4 && mPresenter != null){
            String picCode = met_pic_code.getText().toString().trim();
            mPresenter.sendSmsCode(mobile, picCode);
        }
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        Bundle arguments = getArguments();
        mFlag = arguments.getInt("flag");
        mMobile = arguments.getString("mobile");
        mUniqueSign = arguments.getString("unique_sign");
        showStatus(mFlag);
        mPresenter = new RegisterAndBindPresenter(baseActivity, this);
    }

    public void resetPage(int mFlag,String mobile,String unique_sign) {
        this.mMobile = mobile;
        this.mUniqueSign = unique_sign;
        //LogUtil.zhLogW(this.mFlag+"====<>========"+mFlag);
        if (this.mFlag != mFlag){
            miv_tip.setVisibility(View.INVISIBLE);
            if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){
                setEdittextFocusable(true,met_id);
            }
        }
        this.mFlag = mFlag;
        showStatus(mFlag);
        resetCode();
    }

    private void showStatus(int flag) {
        switch (flag) {
            case RegisterAndBindingAct.FLAG_REGISTER://注册
                mtv_tip.setText("注册");
                visible(rlayout_id);
                gone(mbtn_login);

                break;
            case RegisterAndBindingAct.FLAG_BIND_ID://绑定id
                mtv_tip.setText("绑定导购员ID");
                gone(rlayout_mobile,mbtn_login);

                break;
            case RegisterAndBindingAct.FLAG_BIND_MOBILE://绑定手机号
                mtv_tip.setText("绑定手机号");
                gone(rlayout_id,mbtn_login);

                break;
            case RegisterAndBindingAct.FLAG_BIND_MOBILE_ID://绑定手机号和id
                mtv_tip.setText("绑定");
                gone(mbtn_login);

                break;
            default://默认登录
                mtv_tip.setText("手机号登录");
                gone(rlayout_id);
                visible(mbtn_login);

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecommenderId(String id){
        this.mRecommenderId = id;
        met_id.setText(id);
        setEdittextFocusable(true, met_mobile);
    }

    @OnClick(R.id.mtv_select_id)
    public void selectId() {
        SelectRecommendAct.startAct(baseActivity);
    }

    @OnClick(R.id.miv_pic_code)
    public void resetCode(){
        if (mPresenter != null){
            mPresenter.getCode();
        }
        met_pic_code.setText("");
    }

    @OnClick(R.id.mbtn_login)
    public void btnLogin(){
        String mobile = met_mobile.getText().toString().trim();
        String picCode = met_pic_code.getText().toString().trim();
        ((RegisterAndBindingAct)baseActivity).twoFrag("",mobile,picCode,
                null,false);
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
        isRuning3 = false;
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
    public void setCode(byte[] bytes) {
        if (bytes != null && miv_pic_code != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            miv_pic_code.setImageBitmap(bitmap);
        } else {
            Common.staticToast(getString(R.string.RegisterOneAct_hqyzmsb));
        }
    }

    @Override
    public void iSMobileRight(boolean isRight) {
        iSMobileRight = isRight;
        visible(miv_tip);
        if (isRight){
            miv_tip.setImageResource(R.mipmap.correct_g);
            //setEdittextFocusable(true,met_pic_code);
        }else {
            miv_tip.setImageResource(R.mipmap.credit_error);
        }
    }

    @Override
    public void isRefereesId(boolean isRight) {
        isRefereesIdRight = isRight;
    }

    @Override
    public void smsCode(String message) {
        if (isEmpty(message) && mPresenter != null){
            resetCode();
        }
        if (!isEmpty(message)){
            Common.staticToast(message);
            Common.hideKeyboard(met_pic_code);
            if (mFlag == RegisterAndBindingAct.FLAG_LOGIN) {//登录
                mbtn_login.setEnabled(true);
            }else if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){//注册
                String refereesId = met_id.getText().toString().trim();
                String mobile = met_mobile.getText().toString().trim();
                String picCode = met_pic_code.getText().toString().trim();
                ((RegisterAndBindingAct)baseActivity).twoFrag(refereesId,mobile,picCode,
                        null,true);
            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE_ID){//绑定
                String refereesId = met_id.getText().toString().trim();
                String mobile = met_mobile.getText().toString().trim();
                String picCode = met_pic_code.getText().toString().trim();
                ((RegisterAndBindingAct)baseActivity).twoFrag(refereesId,mobile,picCode,
                        mUniqueSign,false);
            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE){//绑定手机
                String mobile = met_mobile.getText().toString().trim();
                String picCode = met_pic_code.getText().toString().trim();
                ((RegisterAndBindingAct)baseActivity).twoFrag(null,mobile,picCode,
                        mUniqueSign,false);
            }
        }
    }
}
