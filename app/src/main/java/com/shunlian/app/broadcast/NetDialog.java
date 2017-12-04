package com.shunlian.app.broadcast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyLinearLayout;

/**
 * Created by Administrator on 2017/11/29.
 */

public class NetDialog extends Dialog {

    private static NetDialog netDialog;

    public static synchronized NetDialog getInstance(Context context) {
        if (netDialog == null) {
            netDialog = new NetDialog(context);
        }
        return netDialog;
    }

    private NetDialog(@NonNull Context context) {
        this(context, R.style.Mydialog);
    }

    private NetDialog(@NonNull final Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.popup_network);
        int height = TransformUtil.dip2px(context, 40);
        int y = TransformUtil.dip2px(context, 50);
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = height;
        lp.x = 0;
        lp.y = y;
        lp.gravity = Gravity.TOP;
        lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
//        lp.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |       //该flags描述的是窗口的模式，是否可以触摸，可以聚焦等
//                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.format = PixelFormat.TRANSLUCENT;
        win.setAttributes(lp);

        MyLinearLayout mll_rootview = (MyLinearLayout) findViewById(R.id.mll_rootview);
        mll_rootview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(intent);
                if (isShowing()) {
                    dismiss();
                }
            }
        });
    }
}
