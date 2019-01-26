package com.shunlian.app.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;

public class BlogCommentSendPopwindow {
    private EditText inputComment;
    private TextView tvSend;
    private PopupWindow popupWindow;
    private Context mContext;
    private OnPopClickListener mListener;

    public BlogCommentSendPopwindow(Context context) {
        mContext = context;
    }

//    private void popupInputMethodWindow() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                Common.showKeyboard(inputComment);
//            }
//        }.start();
//    }

    @SuppressLint("WrongConstant")
    public void show() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_send, null);
        inputComment = view.findViewById(R.id.edt_content);
        tvSend = view.findViewById(R.id.tv_send);
        popupWindow = new PopupWindow(view, RecyclerView.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor((v, event) -> false);
        popupWindow.setFocusable(true);
        // 设置点击窗口外边窗口消失
        popupWindow.setOutsideTouchable(true);

        // 设置弹出窗体需要软键盘
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow().getAttributes();
        params.alpha = 0.4f;
        ((Activity) mContext).getWindow().setAttributes(params);
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.update();
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams params1 = ((Activity) mContext).getWindow().getAttributes();
            params1.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(params1);
        });
        tvSend.setOnClickListener(v -> {
            String content = inputComment.getText().toString();
            if (TextUtils.isEmpty(content)) {
                Common.staticToast("评论不能为空");
                return;
            }
            if (mListener != null) {
                mListener.onSendClick(content);
            }
        });
       Common.showKeyboard(inputComment);
    }

    public void dismiss() {
        inputComment.setText("");
        inputComment.clearFocus();
        inputComment.setFocusable(false);
        inputComment.setFocusableInTouchMode(false);
        Common.hideKeyboard(inputComment);
        popupWindow.dismiss();
    }

    public void setOnPopClickListener(OnPopClickListener listener) {
        this.mListener = listener;
    }

    public interface OnPopClickListener {
        void onSendClick(String content);
    }
}
