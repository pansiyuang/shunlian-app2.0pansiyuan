package com.shunlian.app.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/8.
 * 快速操作
 *
 * 默认全部隐藏，1 消息 2 首页 3 分享 4 个人中心 5足迹 6 我要反馈
 */

public class QuickActions extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View view;

    @BindView(R.id.mrlayout_message)
    MyRelativeLayout mrlayout_message;

    @BindView(R.id.mtv_message_count)
    MyTextView mtv_message_count;

    @BindView(R.id.mllayout_firstPage)
    MyLinearLayout mllayout_firstPage;

    @BindView(R.id.mllayout_share)
    MyLinearLayout mllayout_share;

    @BindView(R.id.mllayout_PersonalCenter)
    MyLinearLayout mllayout_PersonalCenter;

    @BindView(R.id.mllayout_printfoot)
    MyLinearLayout mllayout_printfoot;

    @BindView(R.id.mllayout_feedback)
    MyLinearLayout mllayout_feedback;

    @BindView(R.id.mllayout_content)
    MyLinearLayout mllayout_content;

    @BindViews({R.id.view_firstPage,R.id.view_share,R.id.view_PersonalCenter
            ,R.id.view_printfoot,R.id.view_feedback})
    List<View> view_line;

    private Unbinder bind;


    public QuickActions(@NonNull Context context) {
        this(context, null);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        ScrollView scrollView = new ScrollView(mContext);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView textView = new TextView(mContext);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.addView(textView);
        addView(scrollView);

        view = LayoutInflater.from(mContext).inflate(R.layout.layout_quick_actions, this, false);
        addView(view);
        bind = ButterKnife.bind(this, view);

        mrlayout_message.setOnClickListener(this);
        mllayout_firstPage.setOnClickListener(this);
        mllayout_share.setOnClickListener(this);
        mllayout_PersonalCenter.setOnClickListener(this);
        mllayout_printfoot.setOnClickListener(this);
        mllayout_feedback.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mrlayout_message:
                hide();
                break;
            case R.id.mllayout_firstPage:
                break;
            case R.id.mllayout_share:
                break;
            case R.id.mllayout_PersonalCenter:
                break;
            case R.id.mllayout_printfoot:
                break;
            case R.id.mllayout_feedback:
                break;
            default:

                break;
        }
    }


    public void setShowItem(int... showItem){
        if (showItem == null){
            hide();
            return;
        }

        if (showItem.length == 0){
            hide();
            return;
        }

        for (int i = 0; i < showItem.length; i++) {
            hideItem(showItem[i]);
        }

        int i = showItem.length * 95 + TransformUtil.dip2px(mContext,30);
        mllayout_content.setWHProportion(300,i);
        requestLayout();
    }

    private void hide(){
        setVisibility(GONE);
    }

    /**
     * 销毁快速操作
     */
    public void destoryQuickActions(){
        hide();
        if (bind != null)
            bind.unbind();
    }

    private void hideItem(int position){
        switch (position){
            case 1:
                mrlayout_message.setVisibility(VISIBLE);
                break;
            case 2:
                mllayout_firstPage.setVisibility(VISIBLE);
                view_line.get(0).setVisibility(VISIBLE);
                break;
            case 3:
                mllayout_share.setVisibility(VISIBLE);
                view_line.get(1).setVisibility(VISIBLE);
                break;
            case 4:
                mllayout_PersonalCenter.setVisibility(VISIBLE);
                view_line.get(2).setVisibility(VISIBLE);
                break;
            case 5:
                mllayout_printfoot.setVisibility(VISIBLE);
                view_line.get(3).setVisibility(VISIBLE);
                break;
            case 6:
                mllayout_feedback.setVisibility(VISIBLE);
                view_line.get(4).setVisibility(VISIBLE);
                break;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                hide();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
