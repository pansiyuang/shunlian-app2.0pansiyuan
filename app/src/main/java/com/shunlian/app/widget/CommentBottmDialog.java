package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.ui.discover_new.comment.CommentRejectedAct;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CommentBottmDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_pass)
    TextView tv_pass;

    @BindView(R.id.tv_rejected)
    TextView tv_rejected;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    private Unbinder bind;
    private FindCommentListEntity.ItemComment currentComment;
    private OnPassListener mListener;

    public CommentBottmDialog(Context context) {
        this(context, R.style.MyDialogStyleBottom);
        initView();
    }

    public CommentBottmDialog(Context context, int themeResId) {
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
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_comment_bottom, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        tv_pass.setOnClickListener(this);
        tv_rejected.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    public void setCommentData(FindCommentListEntity.ItemComment commentData) {
        currentComment = commentData;
        if (currentComment == null) {
            return;
        }
        tv_title.setText(String.format("审核“%s”发表的评论", currentComment.nickname));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pass:
                if (mListener != null) {
                    mListener.onPass();
                }
                dismiss();
                break;
            case R.id.tv_rejected:
                CommentRejectedAct.startAct(getContext(), currentComment.id);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }

    public void setOnPassListener(OnPassListener listener) {
        this.mListener = listener;
    }

    public interface OnPassListener {
        void onPass();
    }
}
