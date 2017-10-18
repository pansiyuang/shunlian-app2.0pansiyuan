package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyImageView;

import butterknife.BindView;

public class SelectRecommendAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    public static void startAct(Context context){
        Intent intent = new Intent(context,SelectRecommendAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_recommend;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_close:
                finish();
                break;
        }
    }
}
