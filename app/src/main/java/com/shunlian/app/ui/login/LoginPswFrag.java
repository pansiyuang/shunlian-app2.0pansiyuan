package com.shunlian.app.ui.login;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.register.RegisterOneAct;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/10/17.
 */

public class LoginPswFrag extends BaseFragment implements View.OnClickListener {
    private View rootView;
    private boolean isHidden = true;
    private MyImageView iv_hidden_psw;
    private EditText edt_psw;

    @BindView(R.id.tv_new_regist)
    TextView tv_new_regist;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_login_psw, container, false);
        initView();
        return rootView;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_new_regist.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    private void initView() {
        iv_hidden_psw = (MyImageView) rootView.findViewById(R.id.iv_hidden_psw);
        edt_psw = (EditText) rootView.findViewById(R.id.edt_psw);
        iv_hidden_psw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_hidden_psw:
                if (isHidden) {
                    edt_psw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    isHidden = false;
                } else {
                    edt_psw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    isHidden = true;
                }
                int index = edt_psw.getText().toString().length();
                if (index > 0) {
                    edt_psw.setSelection(index);
                }
                break;
            case R.id.tv_new_regist:
                RegisterOneAct.stratAct(baseContext);
                break;
        }
    }
}
