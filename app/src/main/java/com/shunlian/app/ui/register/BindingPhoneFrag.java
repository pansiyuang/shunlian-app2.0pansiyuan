package com.shunlian.app.ui.register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.ClearableEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class BindingPhoneFrag extends BaseFragment implements IRegisterOneView, View.OnClickListener {
    /**
     * 用户状态
     * 3 没有绑定手机号有上级
     * 2 没有绑定手机号无上级 新用户
     * 1 没有绑定手机号无上级 老直属
     */
    public final int USER_STATES = 3;
    public final int USER_STATES_NEW = 2;
    public final int USER_STATES_OLD = 1;
    private RegisterOnePresenter onePresenter;
    private int state;
    private String id;
    private String unique_sign;

    @BindView(R.id.rl_id)
    RelativeLayout rl_id;

    @BindView(R.id.et_phone)
    ClearableEditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.tv_select)
    TextView tv_select;

    @BindView(R.id.view_title)
    View view_title;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.sv_content)
    ScrollView sv_content;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.binding_phone, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        et_phone.addTextChangedListener(new PhoneTextWatcher(et_phone));
        miv_code.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        sv_content.setOnTouchListener((v, event) -> true);

        et_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                Editable text = et_id.getText();
                if (state == USER_STATES_NEW && TextUtils.isEmpty(text)) {
                    Common.staticToast(getString(R.string.RegisterOneAct_tjridbnwk));
                    return;
                }
                String phone = et_phone.getText().toString().replaceAll(" ", "");
                if (TextUtils.isEmpty(phone)) {
                    Common.staticToast(getString(R.string.RegisterOneAct_sjhbnwk));
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 4) {
                    et_code.setSelection(4);
                    String code = et_code.getText().toString();
                    onePresenter.sendSmsCode(phone, code);
                }
            }
        });

        et_phone.setOnTouchListener((v, event) -> {
            if (rl_id.getVisibility() == View.GONE){
                Common.showKeyboard(et_phone);
                setEdittextFocusable(true,et_phone);
                return true;
            }
            if (isEtIdEmpty()) {
                return false;
            } else {
                id = et_id.getText().toString();
                onePresenter.checkCode(id);
                setEdittextFocusable(true, et_phone);
            }
            return false;
        });

        et_phone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                if (!TextUtils.isEmpty(s) && !s.toString().startsWith("1")){
                    Common.staticToast(getString(R.string.RegisterOneAct_sjhbzq));
                    return;
                }
                if (!TextUtils.isEmpty(s) && s.length() >= 13) {
                    onePresenter.checkPhone(et_phone.getText().toString().replaceAll(" ",""));
                    setEdittextFocusable(true, et_code);
                }
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            visible(view_title);
        } else {
            gone(view_title);
        }
        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);

        Bundle arguments = getArguments();
        state = arguments.getInt("state", -1);
        unique_sign = arguments.getString("unique_sign");
        //LogUtil.zhLogW("=state========="+state);
        if (state == USER_STATES || state == USER_STATES_OLD) {
            gone(rl_id);
            setEdittextFocusable(true,et_phone);
        }else {
            visible(rl_id);
            setEdittextFocusable(true,et_id);
        }
        onePresenter = new RegisterOnePresenter(baseActivity, this);

        //如果有推荐人，直接填写推荐人
        String member_id = SharedPrefUtil.getSharedUserString("share_code", "");
        if (!isEmpty(member_id)){
            et_id.setText(member_id);
        }
    }

    private boolean isEtIdEmpty() {
        Editable text = et_id.getText();
        if (TextUtils.isEmpty(text)) {
            Common.staticToast(getString(R.string.RegisterOneAct_tjridbnwk));
            setEdittextFocusable(true, et_id);
            setEdittextFocusable(false, et_phone, et_code);
            return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecommenderId(String id){
        this.id = id;
        et_id.setText(id);
        setEdittextFocusable(true, et_phone);
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null&&miv_code!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            miv_code.setImageBitmap(bitmap);
        }
    }

    @Override
    public void smsCode(String smsCode) {
        if (!TextUtils.isEmpty(smsCode)) {
            if (state == USER_STATES_NEW && TextUtils.isEmpty(id)) {
                id = et_id.getText().toString();
            }
            String phone = et_phone.getText().toString().replaceAll(" ", "");
            String code = et_code.getText().toString();
            ((RegisterAct)baseActivity).addRegisterTwo(phone,smsCode,id,code,unique_sign,null);
        } else {
            Common.staticToast(getString(R.string.RegisterOneAct_sjyzmfssb));
        }
    }

    @Override
    public void checkCode(boolean isSuccess) {
        if (!isSuccess) {
            setEdittextFocusable(true, et_id);
        }else {
            if (isEmpty(et_phone.getText()))
                setEdittextFocusable(true, et_phone);
        }
    }

    @Override
    public void checkMobile(boolean isSuccess) {

    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onClick(View v) {
        if (MyOnClickListener.isFastClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.miv_code:
                onePresenter.getCode();
                break;
            case R.id.tv_select:
                SelectRecommendAct.startAct(baseActivity,"",false);
                break;
            case R.id.miv_close:
                baseActivity.finish();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        boolean focusable1 = et_id.isFocusable();
        boolean focusable = et_phone.isFocusable();
        boolean focusable2 = et_code.isFocusable();
        if (focusable) {
            Common.hideKeyboard(et_phone);
        } else if (focusable1) {
            Common.hideKeyboard(et_id);
        } else if (focusable2) {
            Common.hideKeyboard(et_code);
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
