package com.shunlian.app.newchat.util;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.mylibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/15.
 */

public class ChatDialog extends AppCompatActivity {

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_date)
    TextView tv_date;

    private Window window;
    private ImmersionBar immersionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyCompat();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_toast);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(layoutParams);
        window.setWindowAnimations(R.style.ChatDialogAnim);
    }

    private void applyCompat() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true,1).init();

        window = getWindow();
        window.setGravity(Gravity.TOP);
        setFinishOnTouchOutside(true);
    }
}
