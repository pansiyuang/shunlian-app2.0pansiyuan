package com.shunlian.app.ui.new_login_register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.BuildConfig;
import com.shunlian.app.R;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DispachJump;
import com.shunlian.app.eventbus_bean.SelectMemberID;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.RegisterAndBindPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.ui.register.SelectRecommendAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IRegisterAndBindView;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SelectAccountDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;

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

    @BindView(R.id.mtv_select_id)
    MyTextView mtv_select_id;

    @BindView(R.id.rlayout_pwd)
    RelativeLayout rlayout_pwd;

    @BindView(R.id.met_pwd)
    EditText met_pwd;

    @BindView(R.id.mtv_find_pwd)
    MyTextView mtv_find_pwd;

    @BindView(R.id.miv_eyes_tip)
    MyImageView miv_eyes_tip;

    @BindView(R.id.mtv_def_login)
    MyTextView mtv_def_login;

    private RegisterAndBindPresenter mPresenter;
    private final String visity_specialist = "查看导购专员";
    private boolean isRuning1 = false;
    private boolean isRuning2 = false;
    private boolean isRuning3 = false;
    private boolean isRuning4 = false;
    private String mRecommenderId;//推荐人id
    private String mSelectMember_id;//选择导购员的member_id
    private int mFlag;
    private boolean isRefereesIdRight;
    private boolean iSMobileRight;
    private String mMobile;
    private String mUniqueSign;
    private String mMember_id;
    private DispachJump mJump;
    private boolean isHiddenPwd = true;

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

    private void isShowPwd(EditText editText, boolean isShow) {
        if (isShow) {//显示
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        editText.setSelection(editText.getText().length());
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_eyes_tip.setOnClickListener(view -> {
            miv_eyes_tip.setImageResource(isHiddenPwd?R.mipmap.icon_login_eyes_h:R.mipmap.icon_login_eyes_n);
            isShowPwd(met_pwd,isHiddenPwd);
            isHiddenPwd = !isHiddenPwd;
        });
        met_id.setOnTouchListener((v, event) ->{
            if (!isRuning1){
                isRuning1 = true;
                runAnimation("导购专员ID",R.id.rlayout_id,met_id);
            }
            return false;
        });

        met_mobile.setOnTouchListener((v, event) ->{
            if (!isRuning2){
                isRuning2 = true;
                String tip = "";
                if (mFlag == RegisterAndBindingAct.FLAG_PWD_LOGIN){
                    tip = "账号ID";
                }else {
                    tip = "手机号";
                }
                runAnimation(tip,R.id.rlayout_mobile,met_mobile);
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

        met_pwd.setOnTouchListener((v, event) ->{
            if (!isRuning4){
                isRuning4 = true;
                runAnimation("密码",R.id.rlayout_pwd,met_pwd);
            }
            return false;
        });

        //推荐人输入框验证
        setOnFocusChangeListener(met_id,1);
        //手机号输入框验证
        addTextChangedListener(met_mobile,2);
        //setOnFocusChangeListener(met_mobile,2);
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
                        break;
                    case 2:
                        if (mFlag != RegisterAndBindingAct.FLAG_PWD_LOGIN) {
                            if (s.length() >= 11) {
                                checkMobileAPI();
                            } else {
                                iSMobileRight(false);
                            }
                        }
                        break;
                    case 3:
                        if (mFlag != RegisterAndBindingAct.FLAG_LOGIN &&
                                mFlag != RegisterAndBindingAct.FLAG_FIND_PWD)
                            checkPicCode(s);
                        break;
                }
            }
        });
    }

    /**
     *
     * @param editText
     * @param state 1监听输入推荐人  2 监听输入手机号
     */
    private void setOnFocusChangeListener(EditText editText,int state){
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus){
                switch (state){
                    case 1:
                        checkReferees();
                        break;
                    case 2:
                        checkMobileAPI();
                        break;
                }
            }
        });
    }

    /**
     * 检验推荐人id
     */
    private void checkReferees() {
        //失去焦点时检验推荐人id是否正确
        if (rlayout_id != null && rlayout_id.getVisibility() == View.VISIBLE){
            String refereesId = met_id.getText().toString().trim();
            if (mPresenter != null)
                mPresenter.checkRefereesId(refereesId);
        }
    }

    /**
     * 接口验证手机号
     */
    private void checkMobileAPI() {
        String mobile = met_mobile.getText().toString().trim();
        if (mobile.length() == 11){
            if (mPresenter != null){
                if (mFlag == RegisterAndBindingAct.FLAG_LOGIN){
                    //登录验证手机号
                    mPresenter.checkMobile(mobile,"1");
                }else if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){
                    //注册验证手机号
                    mPresenter.checkMobile(mobile,"0");
                }else {
                    //微信登录验证手机号
                    mPresenter.checkMobile(mobile,"2");
                }
            }
        }else {
            iSMobileRight(false);
        }
    }

    /**
     * 检验手机图形验证码
     * @param s
     */
    private void checkPicCode(CharSequence s) {
        if (s.length()>=4 && mPresenter != null){
            String mobile = "";
            if (mFlag == RegisterAndBindingAct.FLAG_BIND_ID){
                mobile = mMobile;
            }else {
               mobile = met_mobile.getText().toString().trim();
            }
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
        mMember_id = arguments.getString("member_id");
        showStatus(mFlag);

        mPresenter = new RegisterAndBindPresenter(baseActivity, this);
        mPresenter.initApi();

        //如果有推荐人，直接填写推荐人
        String member_id = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(member_id)){
            met_id.setText(member_id);
            met_id.setEnabled(false);
            setDispatchFocusable(2);
            mtv_select_id.setText(visity_specialist);
            if (mPresenter != null)
                mPresenter.checkRefereesId(member_id);
        }

        if (BuildConfig.DEBUG){
            visible(mtv_def_login);
        }
    }

    public void resetPage(int flag, String mobile, String unique_sign, String member_id) {
        this.mMobile = mobile;
        this.mUniqueSign = unique_sign;
        this.mMember_id = member_id;
        //LogUtil.zhLogW(this.mFlag+"====<>========"+flag);
        if (this.mFlag != flag && flag == RegisterAndBindingAct.FLAG_REGISTER){
            met_mobile.setText("");
            setDispatchFocusable(1);
            miv_tip.setVisibility(View.INVISIBLE);
        }
        this.mFlag = flag;
        showStatus(mFlag);
        resetCode();
    }

    private void showStatus(int flag) {
        ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(false);
        met_mobile.setHint("请输入手机号");
        switch (flag) {
            case RegisterAndBindingAct.FLAG_REGISTER://注册
                mtv_tip.setText("注册");
                visible(rlayout_id,rlayout_pic_code);
                gone(mbtn_login,rlayout_pwd);

                break;
            case RegisterAndBindingAct.FLAG_BIND_ID://绑定id
                mtv_tip.setText("绑定导购专员ID");
                visible(rlayout_pic_code);
                gone(rlayout_mobile,mbtn_login,rlayout_pwd);

                break;
            case RegisterAndBindingAct.FLAG_BIND_MOBILE://绑定手机号
                mtv_tip.setText("绑定手机号");
                visible(rlayout_pic_code);
                gone(rlayout_id,mbtn_login,rlayout_pwd);

                break;
            case RegisterAndBindingAct.FLAG_BIND_MOBILE_ID://绑定手机号和id
                mtv_tip.setText("绑定");
                visible(rlayout_pic_code);
                gone(mbtn_login,rlayout_pwd);

                break;
            case RegisterAndBindingAct.FLAG_PWD_LOGIN://密码登录
                mtv_tip.setText("账号密码登录");
                visible(rlayout_pwd,mbtn_login);
                gone(rlayout_id,rlayout_pic_code);
                ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(true);
                RelativeLayout.LayoutParams
                        lp = (RelativeLayout.LayoutParams) mbtn_login.getLayoutParams();
                lp.addRule(RelativeLayout.BELOW,R.id.rlayout_pwd);
                mbtn_login.setLayoutParams(lp);
                met_mobile.setHint("请输入账号ID");

                break;
            case RegisterAndBindingAct.FLAG_FIND_PWD://找回密码
                mtv_tip.setText("找回密码");
                gone(rlayout_id,rlayout_pwd);
                visible(mbtn_login,rlayout_pic_code);
                ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(false);
                RelativeLayout.LayoutParams
                        lp2 = (RelativeLayout.LayoutParams) mbtn_login.getLayoutParams();
                lp2.addRule(RelativeLayout.BELOW,R.id.rlayout_pic_code);
                mbtn_login.setLayoutParams(lp2);
                mbtn_login.setText("下一步");

                break;
            default://默认登录
                mtv_tip.setText("手机号登录");
                gone(rlayout_id,rlayout_pwd);
                visible(mbtn_login,rlayout_pic_code);
                ((RegisterAndBindingAct) baseActivity).isShowRegisterBtn(true);
                RelativeLayout.LayoutParams
                        lp1 = (RelativeLayout.LayoutParams) mbtn_login.getLayoutParams();
                lp1.addRule(RelativeLayout.BELOW,R.id.rlayout_pic_code);
                mbtn_login.setLayoutParams(lp1);

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecommenderId(SelectMemberID memberID){
        this.mRecommenderId = memberID.code;
        this.mSelectMember_id = memberID.member_id;
        met_id.setText(mRecommenderId);
        isRefereesIdRight = true;
        setDispatchFocusable(2);
    }

    @OnClick(R.id.mtv_select_id)
    public void selectId() {
        if (visity_specialist.equals(mtv_select_id.getText())){
            SelectRecommendAct.startAct(baseActivity,met_id.getText().toString().trim(),true);
        }else {
            SelectRecommendAct.startAct(baseActivity,mSelectMember_id,false);
        }
    }

    @OnClick(R.id.miv_pic_code)
    public void resetCode(){
        if (mPresenter != null){
            mPresenter.getCode();
        }
        met_pic_code.setText("");
    }

    @OnClick(R.id.mtv_find_pwd)
    public void findPWD(){
        ((RegisterAndBindingAct)baseActivity).findPWD();
    }

    @OnClick(R.id.mbtn_login)
    public void btnLogin(){

        String mobile = met_mobile.getText().toString().trim();
        String picCode = met_pic_code.getText().toString().trim();
        String pwd = met_pwd.getText().toString().trim();

        if (mFlag == RegisterAndBindingAct.FLAG_PWD_LOGIN){//密码登录
            if (isEmpty(mobile)){
                Common.staticToast("账号不能为空喔~");
                return;
            }

            if (isEmpty(pwd)){
                Common.staticToast("请输入密码");
                return;
            }
            if (mPresenter != null)
                mPresenter.loginPwd(mobile, pwd);
            return;
        }

        //验证码登录
        if (isEmpty(mobile)){
            Common.staticToast("手机号不能为空喔~");
            return;
        }
        if (!iSMobileRight){
            Common.staticToast("手机号错误");
            setDispatchFocusable(2);
            return;
        }
        if (isEmpty(picCode)){
            Common.staticToast("请输入验证码");
            return;
        }

        if (mPresenter != null)
            mPresenter.sendSmsCode(mobile, picCode);
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
        isRuning4 = false;
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
        } else if (getActivity()!=null&&!getActivity().isFinishing()){
            Common.staticToast(getString(R.string.RegisterOneAct_hqyzmsb));
        }
    }

    @Override
    public void iSMobileRight(boolean isRight) {
        iSMobileRight = isRight;
        visible(miv_tip);
        if (isRight){
            miv_tip.setImageResource(R.mipmap.correct_g);
        }else {
            miv_tip.setImageResource(R.mipmap.credit_error);
            setDispatchFocusable(2);
        }
    }

    @Override
    public void isRefereesId(boolean isRight) {
        isRefereesIdRight = isRight;
        if (!isRight){
            setDispatchFocusable(1);
        }
    }

    @Override
    public void smsCode(String message) {
        if (isEmpty(message) && mPresenter != null){
            resetCode();
        }
        String refereesId = met_id.getText().toString().trim();
        String mobile = met_mobile.getText().toString().trim();
        String picCode = met_pic_code.getText().toString().trim();

        if (!isEmpty(message)){
            Common.staticToast(message);
            Common.hideKeyboard(met_pic_code);
            if (mFlag == RegisterAndBindingAct.FLAG_LOGIN) {
                //登录
                ((RegisterAndBindingAct)baseActivity).twoFrag("",mobile,picCode,
                        mUniqueSign,mMember_id,mFlag);

            }else if (mFlag == RegisterAndBindingAct.FLAG_REGISTER){
                if (!isRefereesIdRight())return;
                if (!isMobileRight())return;
                //注册
                ((RegisterAndBindingAct)baseActivity).twoFrag(refereesId,mobile,picCode,
                        "",mMember_id,mFlag);

            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE_ID){
                if (!isRefereesIdRight())return;
                if (!isMobileRight())return;
                //绑定
                ((RegisterAndBindingAct)baseActivity).twoFrag(refereesId,mobile,picCode,
                        mUniqueSign,mMember_id,mFlag);

            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_MOBILE){
                if (!isMobileRight())return;
                //绑定手机
                ((RegisterAndBindingAct)baseActivity).twoFrag("",mobile,picCode,
                        mUniqueSign,mMember_id,mFlag);

            }else if (mFlag == RegisterAndBindingAct.FLAG_BIND_ID){
                if (!isRefereesIdRight())return;
                //绑定id
                ((RegisterAndBindingAct)baseActivity).twoFrag(refereesId,mMobile,picCode,
                        mUniqueSign,mMember_id,mFlag);

            }else if (mFlag == RegisterAndBindingAct.FLAG_FIND_PWD){
                //找回密码
                ((RegisterAndBindingAct)baseActivity).twoFrag("",mobile,picCode,
                        mUniqueSign,mMember_id,mFlag);
            }
        }
    }

    /**
     * 导购专员id不对跳转导购专员输入框
     * @return
     */
    private boolean isRefereesIdRight(){
        if (!isRefereesIdRight){
            Common.staticToast("导购专员id错误");
            setDispatchFocusable(1);
            return false;
        }
        return true;
    }

    /**
     * 手机号不对跳转手机号输入框
     * @return
     */
    private boolean isMobileRight(){
        if (!iSMobileRight){
            Common.staticToast("手机号错误");
            setDispatchFocusable(2);
            return false;
        }
        return true;
    }

    /**
     *
     * @param state 1导购获取焦点，2手机号获取焦点，3验证码获取焦点
     */
    private void setDispatchFocusable(int state){
        if (state == 1){
            setEdittextFocusable(true,met_id);
        }else if (state == 2){
            setEdittextFocusable(true,met_mobile);
        }else if (state == 3){
            setEdittextFocusable(true,met_pic_code);
        }
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

        if (Constant.JPUSH != null && !"login".equals(Constant.JPUSH.get(0))) {
            Common.goGoGo(baseActivity, Constant.JPUSH.get(0), Constant.JPUSH.get(1), Constant.JPUSH.get(2)
                    ,Constant.JPUSH.get(3),Constant.JPUSH.get(4),Constant.JPUSH.get(5),Constant.JPUSH.get(6),Constant.JPUSH.get(7)
                    ,Constant.JPUSH.get(8),Constant.JPUSH.get(9),Constant.JPUSH.get(10),Constant.JPUSH.get(11),Constant.JPUSH.get(12));
        }

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


    @OnClick(R.id.mtv_def_login)
    public void defLogin(){
        SelectAccountDialog selectAccountDialog=new SelectAccountDialog(this);
        selectAccountDialog.setCanceledOnTouchOutside(true);
        selectAccountDialog.setDefLoginListener((account, pwd) -> {
            if (mPresenter != null)
                mPresenter.loginPwd(account, pwd);
        });
        selectAccountDialog.show();
    }
}
