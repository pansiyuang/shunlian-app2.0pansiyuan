package com.shunlian.app.yjfk;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/18.
 */

public class OpinionSuccess extends BaseActivity {
    @BindView(R.id.mtv_tijiao)
    MyTextView mtv_tijiao;
    @BindView(R.id.mtv_tijiao2)
    MyTextView mtv_tijiao2;
    @BindView(R.id.mtv_tousu)
    MyTextView mtv_tousu;

    private static Intent intent;

    public static void startAct(Context context, String messages, String message) {
        intent = new Intent(context, OpinionSuccess.class);
        intent.putExtra("messages",messages);
        intent.putExtra("message",message);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_success;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        String messages = intent.getStringExtra("messages");
        String message = intent.getStringExtra("message");
        mtv_tijiao.setText(message);
        mtv_tijiao2.setText(messages);
        mtv_tousu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaninRecordActivity.startAct(OpinionSuccess.this);

            }
        });
    }
}
