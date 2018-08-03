package com.shunlian.app.ui.setting.change_user;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.presenter.ChangeUserPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IChangeUserView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ChangeUserFrag extends BaseFragment implements IChangeUserView, View.OnClickListener {

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

    @BindView(R.id.miv_close)
    MyImageView miv_close;
    private ChangeUserPresenter presenter;
    private boolean isSetPwd;
    private String mKey;
    private boolean isBind;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.act_change_user, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
        met_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length() >= 4 && presenter != null) {
                    if (isBind) {
                        String phone = met_phone.getText().toString();
                        if (phone.startsWith("1") && phone.length() == 11) {
                            presenter.checkNewMobile(phone, s.toString(), mKey);
                        } else {
                            Common.staticToast("请输入正确的手机号");
                        }
                    } else
                        presenter.checkImageCode(s.toString());
                }
            }
        });

        met_phone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (s.length() >= 11) {
                    setEdittextFocusable(true, met_code);
                }
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        isBind = arguments.getBoolean("isBind", false);
        isSetPwd = arguments.getBoolean("isSetPwd", false);
        mKey = arguments.getString("key");

        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);
        presenter = new ChangeUserPresenter(baseActivity, this);

        reset();

    }

    private void reset() {
        presenter.getCode();
        if (isBind) {//绑定手机
            mtv_change_user.setText("绑定手机号");
            gone(mtv_prompt);
            setEdittextFocusable(true, met_phone);
            met_phone.setText("");
        } else {
            visible(mtv_prompt);
            mtv_change_user.setText("更换账号");
            setEdittextFocusable(false, met_phone);
            setEdittextFocusable(true, met_code);
            presenter.getMobile();
        }
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null) {
            if (met_code != null) met_code.setText("");
            if (miv_code!=null)
            miv_code.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    }

    @Override
    public void goBindNewMobile(String mobile, String key) {
        ((ModifyAct) baseActivity).modifyTwoPage(SettingPwdFrag.BIND_CODE, mobile, key);
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
    public void getCode() {
        if (presenter != null) {
            presenter.getCode();
        }
    }

    @Override
    public void setMobile(String mobile) {
        met_phone.setText(mobile);
    }

    @Override
    public void setCheckSuccess() {
        if (isSetPwd) {
            ((ModifyAct) baseActivity).modifyTwoPage(SettingPwdFrag.SET_PWD_CODE,
                    null, null);
        } else {
            ((ModifyAct) baseActivity).modifyTwoPage(SettingPwdFrag.CONFIRM_CODE,
                    null, null);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.miv_close){
            if (isBind){
                ((ModifyAct) baseActivity).modifyOnePage(false, false, null);
            }else {
                baseActivity.finish();
            }
        }
    }

    public void setArgument(boolean isBind, boolean isSetPwd, String key) {
        this.isBind = isBind;
        this.isSetPwd = isSetPwd;
        this.mKey = key;
        reset();
    }
}
