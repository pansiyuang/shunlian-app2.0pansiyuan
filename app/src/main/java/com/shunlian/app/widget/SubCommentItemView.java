package com.shunlian.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.circle.CircleImageView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SubCommentItemView extends FrameLayout {

    private CircleImageView civ_head;
    private MyTextView mtv_name;
    private MyTextView mtv_content;
    private MyTextView mtv_time;
    private MyTextView mtv_more_count;

    public SubCommentItemView(@NonNull Context context) {
        this(context,null);
    }

    public SubCommentItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SubCommentItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_comment_sub, null);
        addView(view);
        civ_head = (CircleImageView) view.findViewById(R.id.civ_head);
        mtv_name = (MyTextView) view.findViewById(R.id.mtv_name);
        mtv_time = (MyTextView) view.findViewById(R.id.mtv_time);
        mtv_content = (MyTextView) view.findViewById(R.id.mtv_content);
        mtv_more_count = (MyTextView) view.findViewById(R.id.mtv_more_count);
    }


    public SubCommentItemView setName(CharSequence sequence){
        if (mtv_name != null){
            mtv_name.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setTime(CharSequence sequence){
        if (mtv_time != null){
            mtv_time.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setContent(CharSequence sequence){
        if (mtv_content != null){
            mtv_content.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setHeadPic(String sequence){
        if (civ_head != null){
            GlideUtils.getInstance().loadImage(getContext(),civ_head,sequence);
        }
        return this;
    }

    public SubCommentItemView setMoreCount(boolean isShow,String count){
        if (mtv_more_count != null){
            if (!TextUtils.isEmpty(count)){
                String format = "共%s条回复>";
                mtv_more_count.setText(String.format(format,count));
            }
            mtv_more_count.setVisibility(isShow?VISIBLE:GONE);
        }
        return this;
    }
}
