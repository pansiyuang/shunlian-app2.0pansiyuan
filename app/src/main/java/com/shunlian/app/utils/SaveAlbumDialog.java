package com.shunlian.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.TextView;

import com.shunlian.app.R;

/**
 * Created by MBENBEN on 2016/8/13 10 : 46.
 * 提示框
 */
public class SaveAlbumDialog {

    private Dialog logoutDialog;
    private final Activity ctx;
    private TextView mtv_close;
    private TextView mtv_go_weChat;
    private String type="",id="";


    public SaveAlbumDialog(Activity ctx,String type,String id) {
        this.ctx = ctx;
        this.type=type;
        this.id=id;
        logoutDialog = new Dialog(ctx, R.style.Mydialog);
        logoutDialog.setContentView(R.layout.dialog_savealbum);
        initView(logoutDialog);
        setCancelable(false);
    }

    private void initView(Dialog logoutDialog) {
        mtv_close = (TextView) logoutDialog.findViewById(R.id.mtv_close);
        mtv_go_weChat = (TextView) logoutDialog.findViewById(R.id.mtv_go_weChat);
        mtv_close.setOnClickListener((v) -> release());

        mtv_go_weChat.setOnClickListener((v) -> {
            Common.openWeiXin(ctx,type,id);
            release();
        });
    }

    public void setCancelable(boolean flag) {
        if (logoutDialog != null)
            logoutDialog.setCancelable(flag);
    }

    /**
     * 是否设置键监听 flag为true 返回键不能取消dialog
     *
     * @param flag
     */
    public void isOnKeyListener(boolean flag) {
        if (logoutDialog != null) {
            if (flag) {
                logoutDialog.setOnKeyListener(keylistener);
            } else {
                logoutDialog.setOnKeyListener(null);
            }
        }
    }

    public void show() {
        if (!ctx.isFinishing() && logoutDialog != null && !logoutDialog.isShowing()) {
            logoutDialog.show();
        }
    }

    public void dismiss() {
        if (logoutDialog != null && logoutDialog.isShowing()) {
            logoutDialog.dismiss();
        }
    }

    /**
     * 释放dialog 防止内存泄漏
     */
    public void release() {
        dismiss();
        logoutDialog = null;
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
}
