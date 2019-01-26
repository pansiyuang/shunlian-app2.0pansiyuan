package com.shunlian.app.widget;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.ui.discover_new.comment.CommentListAct;
import com.shunlian.app.utils.GlideUtils;

public class SubBlogCommentItemView extends FrameLayout {

    private MyImageView civ_head;
    private TextView mtv_name;
    private TextView mtv_content;
    private TextView tv_total_comment;
    private BigImgEntity.CommentItem currentItem;
    private String currentBlogId;

    public SubBlogCommentItemView(Context context) {
        this(context, null);
    }

    public SubBlogCommentItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubBlogCommentItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_discovery_comment, null);
        addView(view);
        civ_head = view.findViewById(R.id.civ_head);
        mtv_name = view.findViewById(R.id.tv_nickname);
        mtv_content = view.findViewById(R.id.tv_content);
        tv_total_comment = view.findViewById(R.id.tv_total_comment);
        tv_total_comment.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(currentBlogId)) {
                CommentListAct.startAct((Activity) getContext(), currentBlogId);
            }
        });
    }

    public void setCommentData(BigImgEntity.CommentItem itemComment, String blogId) {
        currentItem = itemComment;
        currentBlogId = blogId;
        if (!TextUtils.isEmpty(currentItem.nickname)) {
            mtv_name.setText(currentItem.nickname + "说：");
        }
        if (!TextUtils.isEmpty(currentItem.content)) {
            mtv_content.setText(currentItem.content);
        }
        GlideUtils.getInstance().loadCircleAvar(getContext(), civ_head, currentItem.avatar);
    }

    public void setNumShow(boolean isShow, int reply_count) {
        if (isShow) {
            tv_total_comment.setText(String.format("查看全部%d条评论", reply_count));
            tv_total_comment.setVisibility(VISIBLE);
        } else {
            tv_total_comment.setVisibility(GONE);
        }
    }
}
