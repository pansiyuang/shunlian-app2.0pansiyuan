package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;

public class BlogCommentSendDialog extends Dialog {
    private EditText inputComment;
    private TextView tvSend;
    private PopupWindow popupWindow;
    private Context mContext;
    private OnPopClickListener mListener;
    private WindowManager.LayoutParams params;

    public BlogCommentSendDialog(Context context) {
        this(context, R.style.popAd);
        mContext = context;
        init();
    }

    public BlogCommentSendDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    public void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_comment_send, null, false);
        setContentView(view);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = TransformUtil.dip2px(mContext, 49.5f);
        window.setGravity(Gravity.BOTTOM);
        window.setAttributes(attributes);
        setCanceledOnTouchOutside(true);

        inputComment = view.findViewById(R.id.edt_content);
        tvSend = view.findViewById(R.id.tv_send);

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
    }

    @Override
    public void show() {
        inputComment.setHint(Common.getRandomWord());
        showPopInput(mContext, inputComment);
        super.show();
    }

    @Override
    public void dismiss() {
        inputComment.setText("");
        hintPopInput(mContext, inputComment);
        super.dismiss();
    }

    public void showPopInput(final Context context, final View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    public void hintPopInput(final Context context, final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
        if (context instanceof Activity)
            ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

}
    public void setOnPopClickListener(OnPopClickListener listener) {
        this.mListener = listener;
    }

    public interface OnPopClickListener {
        void onSendClick(String content);
    }
}
