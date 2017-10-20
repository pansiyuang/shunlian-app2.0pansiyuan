package com.shunlian.app.ui.register;

import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

import butterknife.BindView;

public class BindingPhoneAct extends BaseActivity {


    @BindView(R.id.rl_id)
    RelativeLayout rl_id;

    @Override
    protected int getLayoutId() {
        return R.layout.binding_phone;
    }

    @Override
    protected void initData() {
        rl_id.setVisibility(View.GONE);
    }
}
