package com.shunlian.app.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.help.HelpOneAct;
import com.shunlian.app.ui.setting.feed_back.BeforeFeedBackAct;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/8.
 * 快速操作
 * <p>
 * 默认全部隐藏，1 消息 2 首页 3 分享 4 个人中心 5足迹 6 我要反馈
 */

public class QuickActions extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View view;

    //消息
    @BindView(R.id.mrlayout_message)
    MyRelativeLayout mrlayout_message;
    @BindView(R.id.mtv_message_count)
    MyTextView mtv_message_count;
    //首页
    @BindView(R.id.mllayout_firstPage)
    MyLinearLayout mllayout_firstPage;
    //分享
    @BindView(R.id.mllayout_share)
    MyLinearLayout mllayout_share;
    //个人中心
    @BindView(R.id.mllayout_PersonalCenter)
    MyLinearLayout mllayout_PersonalCenter;
    //反馈
    @BindView(R.id.mllayout_feedback)
    MyLinearLayout mllayout_feedback;
    //搜索
    @BindView(R.id.mllayout_search)
    MyLinearLayout mllayout_search;
    //购物车
    @BindView(R.id.mllayout_car)
    MyLinearLayout mllayout_car;
    //帮助中心
    @BindView(R.id.mllayout_help)
    MyLinearLayout mllayout_help;

    @BindView(R.id.mllayout_content)
    MyLinearLayout mllayout_content;

    @BindViews({R.id.view_search, R.id.view_firstPage, R.id.view_PersonalCenter
            , R.id.view_car, R.id.view_feedback, R.id.view_help, R.id.view_share})
    List<View> view_line;

    private Unbinder bind;
    private int topMargin;
    private int rightMargin;
    private int px;


    public QuickActions(@NonNull Context context) {
        this(context, null);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        view = LayoutInflater.from(mContext).inflate(R.layout.layout_quick_actions, this, false);
        addView(view);
        bind = ButterKnife.bind(this, view);

        mrlayout_message.setOnClickListener(this);
        mllayout_firstPage.setOnClickListener(this);
        mllayout_share.setOnClickListener(this);
        mllayout_PersonalCenter.setOnClickListener(this);
        mllayout_feedback.setOnClickListener(this);
        mllayout_search.setOnClickListener(this);
        mllayout_car.setOnClickListener(this);
        mllayout_help.setOnClickListener(this);

        px = TransformUtil.dip2px(mContext, 30);
        topMargin = px;
        rightMargin = px / 2;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mrlayout_message:
                MessageActivity.startAct(getContext());
                hide();
                break;
            case R.id.mllayout_firstPage:
                Common.goGoGo(getContext(),"");
                hide();
                break;
            case R.id.mllayout_share:
                break;
            case R.id.mllayout_PersonalCenter:
                Common.goGoGo(getContext(),"personCenter");
                hide();
                break;
            case R.id.mllayout_car:
                Common.goGoGo(getContext(),"shoppingcar");
                hide();
                break;
            case R.id.mllayout_feedback:
                BeforeFeedBackAct.startAct(getContext(),null);
                hide();
                break;
            case R.id.mllayout_help:
                HelpOneAct.startAct(getContext());
                hide();
                break;
            case R.id.mllayout_search:
                SearchGoodsActivity.startActivityForResult((Activity) mContext);
                hide();
                break;
            default:
                break;
        }
    }


    public void setShowItem(int... showItemPos) {
        if (showItemPos == null) {
            hide();
            return;
        }

        if (showItemPos.length == 0) {
            hide();
            return;
        }

        for (int i = 0; i < showItemPos.length; i++) {
            showItem(showItemPos[i],showItemPos[0]);
        }


        int i = showItemPos.length * 90 + px;
        mllayout_content.setWHProportion(300,i);

        RelativeLayout.LayoutParams layoutParams = (LayoutParams)
                mllayout_content.getLayoutParams();
        layoutParams.topMargin = topMargin;
        layoutParams.rightMargin = rightMargin;

        requestLayout();
    }

    private void hide() {
        setVisibility(GONE);
    }

    /**
     * 销毁快速操作
     */
    public void destoryQuickActions() {
        hide();
        if (bind != null)
            bind.unbind();
    }

    private void showItem(int position,int first) {
        switch (position) {
            case 1:
                mrlayout_message.setVisibility(VISIBLE);
                break;
            case 2:
                mllayout_search.setVisibility(VISIBLE);
                if (first != 2)
                    view_line.get(0).setVisibility(VISIBLE);
                break;
            case 3:
                mllayout_firstPage.setVisibility(VISIBLE);
                if (first != 3)
                    view_line.get(1).setVisibility(VISIBLE);
                break;
            case 4:
                mllayout_PersonalCenter.setVisibility(VISIBLE);
                if (first != 4)
                    view_line.get(2).setVisibility(VISIBLE);
                break;
            case 5:
                mllayout_car.setVisibility(VISIBLE);
                if (first != 5)
                    view_line.get(3).setVisibility(VISIBLE);
                break;
            case 6:
                mllayout_feedback.setVisibility(VISIBLE);
                if (first != 6)
                    view_line.get(4).setVisibility(VISIBLE);
                break;
            case 7:
                mllayout_help.setVisibility(VISIBLE);
                if (first != 7)
                    view_line.get(5).setVisibility(VISIBLE);
                break;
            case 8:
                mllayout_share.setVisibility(VISIBLE);
                if (first != 8)
                    view_line.get(6).setVisibility(VISIBLE);
                break;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            boolean b = inRangeOfView(mllayout_content, ev);
            if (!b)
                hide();
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + view.getWidth())
                || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }

    /**
     * 商品详情
     */
    public void goodsDetail() {
        setShowItem(1, 2, 3, 6, 7, 8);
    }

    /**
     * 店铺
     */
    public void shop() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px /10;
        rightMargin = px / 6;
        setShowItem(1, 3, 4, 6, 8);
    }

    /**
     * 凑单
     */
    public void pieceTogether() {
        setShowItem(1, 2, 4, 6, 8);
    }

    /**
     * 评价
     */
    public void comment() {
        setShowItem(1, 2, 3, 6, 7);
    }

    /**
     * 分类
     */
    public void category() {
        setShowItem(1, 3, 6, 7);
    }

    /**
     * 频道
     */
    public void channel() {
        setShowItem(1, 2, 3, 5, 6);
    }

    /**
     * 消息界面
     */
    public void message() {
        setShowItem(3,4,6,7);
    }

    /**
     * 售后
     */
    public void afterSale(){
        setShowItem(1,2,3,6,7);
    }

    /**
     * 活动
     */
    public void activity(){
        setShowItem(1,2,3,4,5,6);
    }

    /**
     * 订单页
     */
    public void order(){
        setShowItem(1,2,3,6,7);
    }

    /**
     * 发现评论列表
     */
    public void findCommentList(){
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px /10;
        setShowItem(1,2,3,6,7);
    }

    /**
     * 帮助
     */
    public void help(){
        setShowItem(1,2,3,4,6);
    }
}
