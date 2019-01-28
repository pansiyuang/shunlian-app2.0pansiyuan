package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommentToolBottomDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.tv_copy)
    TextView tv_copy;

    @BindView(R.id.tv_reply)
    TextView tv_reply;

    @BindView(R.id.tv_del)
    TextView tv_del;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    private Unbinder bind;
    private FindCommentListEntity.ItemComment currentComment;
    private OnToolListener mListener;

    public CommentToolBottomDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        initView();
    }

    public CommentToolBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        setCanceledOnTouchOutside(true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_comment_tool, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        tv_copy.setOnClickListener(this);
        tv_reply.setOnClickListener(this);
        tv_del.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    public void setCommentData(FindCommentListEntity.ItemComment commentData) {
        currentComment = commentData;
        if (currentComment == null) {
            return;
        }
        if (currentComment.is_self == 1) {
            tv_del.setText("删除");
            tv_del.setTextColor(getContext().getResources().getColor(R.color.value_484848));
        } else {
            tv_del.setText("审核");
            tv_del.setTextColor(getContext().getResources().getColor(R.color.pink_color));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_copy:
                if (TextUtils.isEmpty(currentComment.content)) {
                    return;
                }
                Common.copyText(getContext(), currentComment.content);
                dismiss();
                break;
            case R.id.tv_reply:
                if (currentComment.is_self == 0) {
                    if (mListener != null) {
                        mListener.onReply();
                    }
                }
                dismiss();
                break;
            case R.id.tv_del:
                if (mListener == null) {
                    return;
                }
                if (tv_del.getText().equals("删除")) {
                    mListener.onDel();
                } else if (tv_del.getText().equals("审核")) {
                    mListener.onVerify();
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public void setOnToolListener(OnToolListener listener) {
        this.mListener = listener;
    }

    public interface OnToolListener {
        void onReply();

        void onDel();

        void onVerify();

        void onRejected();
    }
}
