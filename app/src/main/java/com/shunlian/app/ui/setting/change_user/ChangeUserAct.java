package com.shunlian.app.ui.setting.change_user;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.text.Editable;

import com.shunlian.app.R;
import com.shunlian.app.presenter.ChangeUserPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.view.IChangeUserView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ChangeUserAct extends BaseActivity implements IChangeUserView{

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.mtv_change_user)
    MyTextView mtv_change_user;

    @BindView(R.id.mtv_prompt)
    MyTextView mtv_prompt;

    @BindView(R.id.met_phone)
    MyEditText met_phone;

    @BindView(R.id.met_code)
    MyEditText met_code;
    private ChangeUserPresenter presenter;
    private boolean isSetPwd;
    private String mKey;
    private boolean isBind;


    public static void startAct(Context context,boolean isBind,boolean isSetPwd,String key){
        Intent intent = new Intent(context,ChangeUserAct.class);
        intent.putExtra("isBind",isBind);
        intent.putExtra("isSetPwd",isSetPwd);
        intent.putExtra("key",key);
        context.startActivity(intent);
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_change_user;
    }

    @Override
    protected void initListener() {
        super.initListener();
        met_code.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length() >= 4 && presenter != null){
                    if (isBind) {
                        String phone = met_phone.getText().toString();
                        if (phone.startsWith("1")&&phone.length() == 11) {
                            presenter.checkNewMobile(phone, s.toString(), mKey);
                        }else {
                            Common.staticToast("请输入正确的手机号");
                        }
                    }
                    else
                        presenter.checkImageCode(s.toString());
                }
            }
        });

        met_phone.addTextChangedListener(new SimpleTextWatcher(){
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length() >=11){
                    setEdittextFocusable(true,met_code);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        isBind = getIntent().getBooleanExtra("isBind", false);
        isSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
        mKey = getIntent().getStringExtra("key");
        presenter = new ChangeUserPresenter(this,this);
        presenter.getCode();
        if (isBind){//绑定手机
            mtv_change_user.setText("绑定手机号");
            gone(mtv_prompt);
            setEdittextFocusable(true,met_phone);
        }else {
            visible(mtv_prompt);
            mtv_change_user.setText("更换账号");
            setEdittextFocusable(false,met_phone);
            setEdittextFocusable(true,met_code);
            presenter.getMobile();
        }

    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            met_code.setText("");
            miv_code.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void goBindNewMobile(String mobile, String key) {
        SettingPwdAct.startAct(this,SettingPwdAct.BIND_CODE,mobile,key);
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

    @OnClick(R.id.miv_code)
    public void getCode(){
        if (presenter != null){
            presenter.getCode();
        }
    }

    @Override
    public void setMobile(String mobile) {
        met_phone.setText(mobile);
    }

    @Override
    public void setCheckSuccess() {
        if (isSetPwd){
            SettingPwdAct.startAct(this,SettingPwdAct.SET_PWD_CODE,null,null);
        }else {
            SettingPwdAct.startAct(this,SettingPwdAct.CONFIRM_CODE,null,null);
        }
    }
}
