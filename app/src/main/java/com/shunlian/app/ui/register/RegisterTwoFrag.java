package com.shunlian.app.ui.register;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.RegisterTwoPresenter;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.ui.my_profit.SexSelectAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRegisterTwoView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.VerificationCodeInput;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterTwoFrag extends BaseFragment implements View.OnClickListener, IRegisterTwoView {
    public static String TYPE_FIND_PSW = "find_password";
    public static String TYPE_REGIST = "regist";
    private String currentType;
    private String pictureCode;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.et_pwd)
    EditText et_pwd;

    @BindView(R.id.et_rpwd)
    EditText et_rpwd;

    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.iv_hidden_psw)
    MyImageView iv_hidden_psw;

    @BindView(R.id.iv_hidden_rpsw)
    MyImageView iv_hidden_rpsw;

    @BindView(R.id.input_code)
    VerificationCodeInput input_code;

    @BindView(R.id.tv_time)
    TextView tv_time;

    @BindView(R.id.btn_complete)
    Button btn_complete;

    @BindView(R.id.view_title)
    View view_title;

    @BindView(R.id.llayout_agreement)
    LinearLayout llayout_agreement;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_agree)
    MyImageView miv_agree;

    private String nickname;
    private boolean isHiddenPwd;
    private boolean isHiddenRPwd;
    private CountDownTimer countDownTimer;
    private String smsCode;
    private String codeId;
    private String unique_sign;
    private RegisterTwoPresenter registerTwoPresenter;
    private String phone;
    private String mCode;
    private boolean isAgree;


    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_register_two, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_hidden_psw.setOnClickListener(this);
        iv_hidden_rpsw.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        llayout_agreement.setOnClickListener(this);
        btn_complete.setOnClickListener(this);

        input_code.setOnCompleteListener(content -> {
            mCode = content;
            setEdittextFocusable(true,et_pwd);
        });

        et_pwd.setOnTouchListener((v, event) -> {
            if (checkCode(et_pwd)){
                return false;
            }
            return false;
        });


        et_pwd.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_psw.setVisibility(View.VISIBLE);
                }else {
                    iv_hidden_psw.setVisibility(View.INVISIBLE);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        et_rpwd.setOnTouchListener((v, event) -> {
            if (checkCode(et_rpwd))
                return false;
            if (isEtPwdEmpty(et_pwd,et_rpwd)){
                return false;
            }
            String pwd = et_pwd.getText().toString();
            if (!Common.regularPwd(pwd)){
                Common.staticToast(getString(R.string.RegisterTwoAct_mmzh));
                setEdittextFocusable(true,et_pwd);
                setEdittextFocusable(false,et_rpwd);
                return false;
            }else {
                setEdittextFocusable(true,et_rpwd);
            }
            return false;
        });

        et_rpwd.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    iv_hidden_rpsw.setVisibility(View.VISIBLE);
                }else {
                    iv_hidden_rpsw.setVisibility(View.INVISIBLE);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                    et_rpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });

        et_nickname.setOnTouchListener((v, event) -> {
            if (checkCode(et_nickname))
                return false;
            if (isEtPwdEmpty(et_pwd,et_nickname)){
                return false;
            }

            if (isEtPwdEmpty(et_rpwd,et_nickname)){
                return false;
            }else {
                String pwd = et_pwd.getText().toString();
                String rpwd = et_rpwd.getText().toString();
                if (!pwd.equals(rpwd)){
                    Common.staticToast(getString(R.string.RegisterTwoAct_mmbyz));
                    setEdittextFocusable(true,et_rpwd);
                    setEdittextFocusable(false,et_nickname);
                    return false;
                }else {
                    setEdittextFocusable(true, et_nickname);
                }
            }
            return false;
        });

        et_nickname.addTextChangedListener(new SimpleTextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                boolean nicknameLegitimate = Common.isNicknameLegitimate(s.toString());
                if (nicknameLegitimate){
                    nickname = s.toString();
                }else {
                    et_nickname.setText(nickname);
                    et_nickname.setSelection(nickname.length());
                }
            }
        });
    }

    private boolean checkCode(EditText editText) {
        if (TextUtils.isEmpty(mCode)){
            Common.staticToast(getString(R.string.RegisterTwoAct_sjyzm));
            setEdittextFocusable(false,editText);
            return true;
        }
        return false;
    }

    private boolean isEtPwdEmpty(EditText active ,EditText passive){
        String pwd = active.getText().toString();
        if (TextUtils.isEmpty(pwd)){
            Common.staticToast(getString(R.string.RegisterTwoAct_mmbnwk));
            setEdittextFocusable(true,active);
            setEdittextFocusable(false,passive);
            return true;
        }
        return false;
    }

    @Override
    protected void initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visible(view_title);
        }else {
            gone(view_title);
        }
        countDown();
        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i*2,i*2,i*2,i*2);
        TransformUtil.expandViewTouchDelegate(miv_agree,i,i,i,i);

        Bundle arguments = getArguments();
        phone = arguments.getString("phone");
        smsCode = arguments.getString("smsCode");
        codeId = arguments.getString("codeId");
        unique_sign = arguments.getString("unique_sign");
        currentType = arguments.getString("type");
        pictureCode = arguments.getString("pictureCode");
        if (!TextUtils.isEmpty(unique_sign)){
            btn_complete.setText(getString(R.string.SelectRecommendAct_bind));
            gone(llayout_agreement);
        }
        tv_phone.setText(this.phone);
        if (TYPE_FIND_PSW.equals(currentType)) {
            gone(llayout_agreement,et_nickname);
        }
        GradientDrawable completeBG = (GradientDrawable) btn_complete.getBackground();
        completeBG.setColor(getColorResouce(R.color.pink_color));
        btn_complete.setEnabled(true);

        if (TYPE_REGIST.equals(currentType)){//显示协议
            visible(llayout_agreement);
            completeBG.setColor(getColorResouce(R.color.color_value_6c));
            btn_complete.setEnabled(false);
        }
        registerTwoPresenter = new RegisterTwoPresenter(baseActivity, this);
    }

    public void setArgument(String phone,String smsCode,String codeId,
                             String pictureCode,String unique_sign,String currentType){
        this.phone = phone;
        this.smsCode = smsCode;
        this.codeId = codeId;
        this.pictureCode = pictureCode;
        this.currentType = currentType;
        this.unique_sign = unique_sign;

        GradientDrawable completeBG = (GradientDrawable) btn_complete.getBackground();
        completeBG.setColor(getColorResouce(R.color.pink_color));
        btn_complete.setEnabled(true);

        if (!TextUtils.isEmpty(unique_sign)){
            btn_complete.setText(getString(R.string.SelectRecommendAct_bind));
            gone(llayout_agreement);
        }
        tv_phone.setText(this.phone);
        if (TYPE_FIND_PSW.equals(currentType)) {
            gone(llayout_agreement,et_nickname);
        }

        if (TYPE_REGIST.equals(currentType)){
            visible(llayout_agreement);
            completeBG.setColor(getColorResouce(R.color.color_value_6c));
            btn_complete.setEnabled(false);
        }
        input_code.clearAll();
        et_pwd.setText("");
        et_rpwd.setText("");
        et_nickname.setText("");
        countDown();
    }

    private void countDown() {
        if (countDownTimer == null) {
            countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                @Override
                public void onTick(long l) {
                    tv_time.setText((int) Math.floor(l / 1000) + "s");
                    tv_time.setEnabled(false);
                }

                @Override
                public void onFinish() {
                    tv_time.setText(getString(R.string.LoginPswFrg_cxhq));
                    tv_time.setEnabled(true);
                }
            };
        }
        countDownTimer.cancel();
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        if (MyOnClickListener.isFastClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.miv_close:
                ((RegisterAct)baseActivity).addRegisterOne();
                break;
            case R.id.iv_hidden_psw:
                if (isHiddenPwd) {//隐藏
                    isHiddenPwd = false;
                    isShowPwd(et_pwd, false);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
                } else {
                    isHiddenPwd = true;
                    isShowPwd(et_pwd, true);
                    iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.iv_hidden_rpsw:
                if (isHiddenRPwd) {
                    isHiddenRPwd = false;
                    isShowPwd(et_rpwd, false);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_h);
                } else {
                    isHiddenRPwd = true;
                    isShowPwd(et_rpwd, true);
                    iv_hidden_rpsw.setImageResource(R.mipmap.icon_login_eyes_n);
                }
                break;
            case R.id.tv_time:
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
                registerTwoPresenter.sendSmsCode(phone.replaceAll(" ", ""),pictureCode);
                break;
            case R.id.btn_complete:
                String pwd = et_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)){
                    Common.staticToast(getString(R.string.RegisterTwoAct_mmbnwk));
                    return;
                }
                if (TextUtils.isEmpty(nickname)&&!TYPE_FIND_PSW.equals(currentType)){
                    Common.staticToast(getString(R.string.RegisterTwoAct_qsznc));
                    return;
                }
                LogUtil.httpLogW("TYPE_FIND_PSW："+TYPE_FIND_PSW);
                if (TYPE_FIND_PSW.equals(currentType)) {
                    registerTwoPresenter.findPsw(phone.replaceAll(" ", ""),
                            et_pwd.getText().toString(), et_rpwd.getText().toString(), mCode);
                } else if (TYPE_REGIST.equals(currentType)) {
                    registerTwoPresenter.register(phone.replaceAll(" ", ""),
                            mCode, codeId, et_pwd.getText().toString(), nickname, "");
                } else {
                    registerTwoPresenter.register(phone.replaceAll(" ", ""),
                            mCode, codeId, et_pwd.getText().toString(), nickname, unique_sign);
                }
                break;
            case R.id.llayout_agreement:
                H5X5Act.startAct(baseActivity, InterentTools.H5_HOST
                        +RegisterAct.REGISTRATION_AGREEMENT,H5X5Act.MODE_SONIC);
                break;
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    @Override
    public void resetPsw(String message) {
        Common.staticToast(message);
        baseActivity.finish();
    }

    @Override
    public void registerFinish(BaseEntity<RegisterFinishEntity> entity) {
        if (TYPE_FIND_PSW.equals(currentType)){//找回密码
            LoginAct.startAct(baseActivity);
        }else if (TYPE_REGIST.equals(currentType)){//注册
            saveUserInfo(entity);
        }else {//绑定
            saveUserInfo(entity);
        }
        baseActivity.finish();
    }

    private void saveUserInfo(BaseEntity<RegisterFinishEntity> entity) {
        Common.staticToast(entity.message);
        RegisterFinishEntity content = entity.data;
        SharedPrefUtil.saveSharedUserString("token", content.token);
        SharedPrefUtil.saveSharedUserString("refresh_token", content.refresh_token);
        SharedPrefUtil.saveSharedUserString("member_id", content.member_id);
        SharedPrefUtil.saveSharedUserString("plus_role", content.plus_role);
        CrashReport.setUserId(content.member_id);
        EasyWebsocketClient.getInstance(baseActivity).initChat(); //初始化聊天
//        if (content.tag!=null)
//            SharedPrefUtil.saveSharedUserStringss("tags", new HashSet<>(content.tag));
        JpushUtil.setJPushAlias();

        if (!"1".equals(content.is_tag)){
            SexSelectAct.startAct(baseActivity);
        }else {
            Common.goGoGo(baseActivity,"mainPage");
        }
    }

    @Override
    public void smsCode(String smsCode) {

    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @OnClick(R.id.miv_agree)
    public void agree(){
        GradientDrawable completeBG = (GradientDrawable) btn_complete.getBackground();
        if (isAgree){
            miv_agree.setImageResource(R.mipmap.img_balance_xieyi_n);
            completeBG.setColor(getColorResouce(R.color.color_value_6c));
        }else {
            miv_agree.setImageResource(R.mipmap.img_balance_xieyi_h);
            completeBG.setColor(getColorResouce(R.color.pink_color));
        }
        btn_complete.setEnabled(!isAgree);
        isAgree = !isAgree;
    }
}
