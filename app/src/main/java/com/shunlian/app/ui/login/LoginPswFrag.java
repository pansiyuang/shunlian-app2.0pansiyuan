package com.shunlian.app.ui.login;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.widget.MyImageView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginPswFrag extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private boolean isHidden = true;
    private MyImageView iv_hidden_psw;
    private EditText edt_psw;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_login_psw, container, false);
        initView();
        return rootView;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        super.initListener();
        iv_hidden_psw.setOnClickListener(this);
        edt_psw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (edt_psw.getText().toString().isEmpty()) {
                    iv_hidden_psw.setVisibility(View.INVISIBLE);
                    hiddenPsw(false);
                } else {
                    iv_hidden_psw.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView() {
        iv_hidden_psw = (MyImageView) rootView.findViewById(R.id.iv_hidden_psw);
        edt_psw = (EditText) rootView.findViewById(R.id.edt_psw);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hidden_psw:
                hiddenPsw(isHidden);
                int index = edt_psw.getText().toString().length();
                if (index > 0) {
                    edt_psw.setSelection(index);
                }
                break;
        }
    }

    public void hiddenPsw(boolean b) {
        if (b) {
            edt_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_n);
            isHidden = false;
        } else {
            edt_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            iv_hidden_psw.setImageResource(R.mipmap.icon_login_eyes_h);
            isHidden = true;
        }
    }
}
