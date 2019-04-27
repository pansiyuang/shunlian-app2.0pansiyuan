package com.shunlian.app.yjfk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;

/**
 * Created by Administrator on 2019/4/24.
 */

public class OnTouchActivity extends BaseActivity {
    public static void startAct(Context context) {
        Intent intent = new Intent(context, OnTouchActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.ontouch;
    }

    @Override
    protected void initData() {
        Button Button1 = findViewById(R.id.Button1);
        Button1.setOnTouchListener(new MyOnTouch());
    }
}
