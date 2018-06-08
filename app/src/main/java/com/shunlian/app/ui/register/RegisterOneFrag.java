package com.shunlian.app.ui.register;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.presenter.RegisterOnePresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IRegisterOneView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.PhoneTextWatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


public class RegisterOneFrag extends BaseFragment implements View.OnClickListener, IRegisterOneView {

    @BindView(R.id.et_id)
    EditText et_id;

    @BindView(R.id.tv_select)
    TextView tv_select;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.miv_code)
    MyImageView miv_code;

    @BindView(R.id.miv_logo)
    MyImageView miv_logo;

    @BindView(R.id.view_title)
    View view_title;

    @BindView(R.id.sv_content)
    ScrollView sv_content;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private String id;//推荐人id
    private RegisterOnePresenter onePresenter;
    private boolean isCheckCode;
    private boolean isCheckMobile;

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_register_one, null);
        return view;
    }

    @Override
    protected void initListener() {
        super.initListener();
        et_phone.addTextChangedListener(new PhoneTextWatcher(et_phone));
        miv_close.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        miv_code.setOnClickListener(this);
        sv_content.setOnTouchListener((v, event) -> true);
        et_phone.setOnTouchListener((v, event) -> {
            if (isEtIdEmpty()) {
                return false;
            } else {
                if (TextUtils.isEmpty(id)) {
                    id = et_id.getText().toString();
                }
                onePresenter.checkCode(id);
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

        et_code.setOnTouchListener((v, event) -> {
            if (isEtIdEmpty())
                return false;
            if (isEtPhoneEmpty())
                return false;
            return false;
        });
        et_code.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                String phone = et_phone.getText().toString().replaceAll(" ", "");
                if (!TextUtils.isEmpty(s) && s.length() >= 4) {
                    et_code.setSelection(s.length());
                    String code = et_code.getText().toString();
                    onePresenter.sendSmsCode(phone, code);
                }
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        GradientDrawable background = (GradientDrawable) tv_select.getBackground();
        background.setColor(getColorResouce(R.color.bg_gray_two));
        setEdittextFocusable(true,et_id);
        visible(miv_logo);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            visible(view_title);
        }else {
            gone(view_title);
        }
        //返回键扩大点击范围
        int i = TransformUtil.dip2px(baseActivity, 20);
        TransformUtil.expandViewTouchDelegate(miv_close,i,i,i,i);
        onePresenter = new RegisterOnePresenter(baseActivity, this);
    }

    private boolean isEtPhoneEmpty() {
        String phone = et_phone.getText().toString().replaceAll(" ", "");
        if (TextUtils.isEmpty(phone)) {
            Common.staticToast(getString(R.string.RegisterOneAct_sjhbnwk));
            setEdittextFocusable(true, et_phone);
            setEdittextFocusable(false, et_code);
            return true;
        }else if (!TextUtils.isEmpty(phone) && !phone.startsWith("1") && phone.length() < 11){
            Common.staticToast(getString(R.string.RegisterOneAct_sjhbzq));
            setEdittextFocusable(true, et_phone);
            setEdittextFocusable(false, et_code);
            return true;
        }
        return false;
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

    @Override
    public void onClick(View v) {
        if (MyOnClickListener.isFastClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.tv_select:
                SelectRecommendAct.startAct(baseActivity);
                break;
            case R.id.miv_code:
                onePresenter.getCode();
                break;
            case R.id.miv_close:
                baseActivity.finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getRecommenderId(String id){
        this.id = id;
        et_id.setText(id);
        setEdittextFocusable(true, et_phone);
    }

    @Override
    public void setCode(byte[] bytes) {
        if (bytes != null && miv_code != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            miv_code.setImageBitmap(bitmap);
            et_code.setText("");
        } else {
            Common.staticToast(getString(R.string.RegisterOneAct_hqyzmsb));
        }
    }

    @Override
    public void smsCode(String smsCode) {
        if (!TextUtils.isEmpty(smsCode) && isCheckCode) {
            if (!isCheckMobile){
                return;
            }
            if (TextUtils.isEmpty(id)) {
                id = et_id.getText().toString();
            }
            //Common.staticToast(smsCode);
            String phone = et_phone.getText().toString();
            String code = et_code.getText().toString();
            ((RegisterAct)baseActivity).addRegisterTwo(phone,smsCode,id,code,
                    null,RegisterTwoFrag.TYPE_REGIST);
        } else {
            onePresenter.getCode();
            et_code.setText("");
        }
    }

    @Override
    public void checkCode(boolean isSuccess) {
        isCheckCode = isSuccess;
        if (!isSuccess) {
            setEdittextFocusable(true, et_id);
        }else {
            setEdittextFocusable(true, et_phone);
        }
    }

    @Override
    public void checkMobile(boolean isSuccess) {
        isCheckMobile = isSuccess;
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void onDestroyView() {
        Common.hideKeyboard(et_phone);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    /**
     * 刷新验证码
     */
    public void refreshCode() {
        if (onePresenter != null){
            onePresenter.getCode();
        }
    }
}
