package com.shunlian.app.newchat.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.websocket.MemberStatus;
import com.shunlian.app.widget.MyTextView;

/**
 * Created by Administrator on 2018/4/18.
 */

public class SwitchStatusDialog implements View.OnClickListener {
    private TextView tvSure;
    private TextView tvCancle;
    private TextView tvMessage;
    private Dialog dialog;
    private Context mContext;
    private OnButtonClickListener mListener;

    public SwitchStatusDialog(Context context) {
        mContext = context;
        dialog = new Dialog(context, R.style.Mydialog);
        dialog.setContentView(R.layout.dialog_common);
        initView(dialog);
        setCancelable(false);
        tvSure.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        tvCancle.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
    }

    private void initView(Dialog dialog) {
        tvSure = (TextView) dialog.findViewById(R.id.tv_sure);
        tvCancle = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvMessage = (TextView) dialog.findViewById(R.id.tv_message);

        tvSure.setText(mContext.getResources().getString(R.string.ready_to_switch));
        tvCancle.setText(mContext.getResources().getString(R.string.no_switch));
        tvSure.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
    }

    public void setCancelable(boolean flag) {
        if (dialog != null)
            dialog.setCancelable(flag);
    }

    /**
     * @param selferStatus 当前身份
     * @param toStatus     聊天对象身份
     * @param switchStatus 切换身份
     * @return
     */
    public SwitchStatusDialog setDialogMessage(MemberStatus selferStatus, MemberStatus toStatus, MemberStatus switchStatus) {
        String str = mContext.getResources().getString(R.string.switch_status);
        tvMessage.setText(String.format(str, getMemberStatus(selferStatus), getMemberStatus(toStatus), getMemberStatus(switchStatus)));
        return this;
    }

    public String getMemberStatus(MemberStatus memberStatus) {
        if (memberStatus == MemberStatus.Seller) {
            return "商家";
        } else if (memberStatus == MemberStatus.Admin) {
            return "客服";
        } else {
            return "成员";
        }
    }

    public SwitchStatusDialog setOnButtonClickListener(OnButtonClickListener listener) {
        this.mListener = listener;
        return this;
    }

    public void show() {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }


    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sure:
                if (mListener != null) {
                    mListener.OnClickSure();
                }
                break;
            case R.id.tv_cancel:
                if (mListener != null) {
                    mListener.OnClickCancle();
                }
                break;
        }
    }

    public interface OnButtonClickListener {

        void OnClickSure();

        void OnClickCancle();
    }
}
