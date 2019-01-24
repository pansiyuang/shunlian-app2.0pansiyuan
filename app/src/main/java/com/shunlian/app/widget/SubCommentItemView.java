package com.shunlian.app.widget;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.utils.GlideUtils;

/**
 * Created by Administrator on 2018/3/14.
 */

public class SubCommentItemView extends FrameLayout {

    private MyImageView civ_head;
    private MyTextView mtv_name;
    private MyTextView mtv_content;
    private MyTextView mtv_time;
    private MyTextView mtv_more_count;
    private TextView tv_reply;
    private TextView tv_verify;
    private LinearLayout ll_zan;
    private TextView tv_zan;
    private LottieAnimationView mAnimationView;
    private FindCommentListEntity.ItemComment mEntity;
    private OnSubCallBack mCallBack;

    public SubCommentItemView(@NonNull Context context) {
        this(context, null);
    }

    public SubCommentItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SubCommentItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.item_comment_sub, null);
        addView(view);
        civ_head = (MyImageView) view.findViewById(R.id.civ_head);
        mtv_name = (MyTextView) view.findViewById(R.id.mtv_name);
        mtv_time = (MyTextView) view.findViewById(R.id.mtv_time);
        mtv_content = (MyTextView) view.findViewById(R.id.mtv_content);
        tv_reply = view.findViewById(R.id.tv_reply);
        tv_verify = view.findViewById(R.id.tv_verify);
        tv_zan = view.findViewById(R.id.tv_zan);
        mtv_more_count = (MyTextView) view.findViewById(R.id.mtv_more_count);
        mAnimationView = view.findViewById(R.id.animation_zan);
        ll_zan = view.findViewById(R.id.ll_zan);

        ll_zan.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.OnPraise(mEntity.id, mAnimationView);
            }
        });
        tv_reply.setOnClickListener(v -> {
            if (mCallBack != null) {
                mCallBack.OnReply();
            }
        });
    }


    public SubCommentItemView setName(CharSequence sequence) {
        if (mtv_name != null) {
            mtv_name.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setTime(CharSequence sequence) {
        if (mtv_time != null) {
            mtv_time.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setContent(CharSequence sequence) {
        if (mtv_content != null) {
            mtv_content.setText(sequence);
        }
        return this;
    }

    public SubCommentItemView setHeadPic(String sequence) {
        if (civ_head != null) {
            GlideUtils.getInstance().loadCircleHeadImage(getContext(), civ_head, sequence);
        }
        return this;
    }

    public SubCommentItemView setMoreCount(boolean isShow, String count) {
        if (mtv_more_count != null) {
            if (!TextUtils.isEmpty(count)) {
                String format = "共%s条回复>";
                mtv_more_count.setText(String.format(format, count));
            }
            mtv_more_count.setVisibility(isShow ? VISIBLE : GONE);
        }
        return this;
    }

    public void setCommentData(FindCommentListEntity.ItemComment itemComment) {
        mEntity = itemComment;
        if (mEntity.is_self == 1) {
            tv_reply.setText("删除");
        } else {
            tv_reply.setText("回复");
        }
        tv_zan.setText(String.valueOf(mEntity.like_count));

        mAnimationView.setAnimation("praise.json");
        mAnimationView.loop(false);
        mAnimationView.setImageAssetsFolder("images/");
        if ("1".equals(itemComment.like_status)) {
            mAnimationView.setProgress(1f);
            tv_zan.setTextColor(getContext().getResources().getColor(R.color.pink_color));
            ll_zan.setClickable(false);
        } else {
            mAnimationView.setProgress(0f);
            tv_zan.setTextColor(getContext().getResources().getColor(R.color.value_343434));
            ll_zan.setClickable(true);
        }

        if (itemComment.check_is_show == 0) { //审核按钮是否显示，0不显示任何按钮，1显示审核按钮，2显示撤回按钮
            tv_verify.setVisibility(View.GONE);
        } else if (itemComment.check_is_show == 1) {
            tv_verify.setVisibility(View.VISIBLE);
            tv_verify.setText("审核");
        } else if (itemComment.check_is_show == 2) {
            tv_verify.setVisibility(View.VISIBLE);
            tv_verify.setText("撤回");
        }
    }

    public void setOnCallBack(OnSubCallBack callBack) {
        mCallBack = callBack;
    }

    public interface OnSubCallBack {

        void OnPraise(String commentId, LottieAnimationView lottieAnimationView);

        void OnReply();

        void OnDel();
    }
}
