package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;

/**
 * Created by Administrator on 2018/11/7.
 */

public class SaveImgDialog {
    private Activity act;
    private Dialog saveImgDialog;
    private MyImageView miv_wechat;
    private TextView tv_cancel;

    public SaveImgDialog(Activity activity) {
        this.act = activity;
        saveImgDialog = new Dialog(activity, R.style.Mydialog);
        saveImgDialog.setContentView(R.layout.dialog_save_imgs);
        initView(saveImgDialog);
        setCancelable(false);
    }

    private void initView(Dialog dialog) {
        miv_wechat = dialog.findViewById(R.id.mtv_close);
        tv_cancel = dialog.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener((v) -> release());

        miv_wechat.setOnClickListener((v) -> {
            Common.openWeiXin(act, "", "");
            release();
        });
    }

    public void setCancelable(boolean flag) {
        if (saveImgDialog != null)
            saveImgDialog.setCancelable(flag);
    }

    public void show() {
        if (!act.isFinishing() && saveImgDialog != null && !saveImgDialog.isShowing()) {
            saveImgDialog.show();
        }
    }

    public void dismiss() {
        if (saveImgDialog != null && saveImgDialog.isShowing()) {
            saveImgDialog.dismiss();
        }
    }

    /**
     * 释放dialog 防止内存泄漏
     */
    public void release() {
        dismiss();
        saveImgDialog = null;
    }
}
