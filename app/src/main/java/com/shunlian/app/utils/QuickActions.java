package com.shunlian.app.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.circle.RoundRectImageView;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemListener;
import com.shunlian.app.wxapi.WXEntryActivity;
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
 * 默认全部隐藏，1消息、2搜索、3首页、4个人中心、5购物车、6反馈、7帮助中心、8分享
 */

public class QuickActions extends RelativeLayout implements View.OnClickListener {

    private Context mContext;
    private View mActionView;

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
    LinearLayout mllayout_content;

    @BindViews({R.id.view_search, R.id.view_firstPage, R.id.view_PersonalCenter
            , R.id.view_car, R.id.view_feedback, R.id.view_help, R.id.view_share})
    List<View> view_line;

    private Unbinder bind;
    private int topMargin;
    private int rightMargin;
    private int px;
    private PopMenu mPopMenu;
    private ShareInfoParam mShareInfoParam;


    public QuickActions(@NonNull Context context) {
        this(context, null);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mActionView = LayoutInflater.from(mContext).inflate(R.layout.layout_quick_actions, this, false);
        addView(mActionView);
        bind = ButterKnife.bind(this, mActionView);

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
                Common.goGoGo(getContext(),"message");
                hide();
                break;
            case R.id.mllayout_firstPage:
                Common.goGoGo(getContext(),"");
                hide();
                break;
            case R.id.mllayout_share:
                if (!Common.isAlreadyLogin()){
                    final PromptDialog promptDialog = new PromptDialog((Activity) mContext);
                    promptDialog.setTvSureColor(R.color.white);
                    promptDialog.setTvSureBg(R.color.pink_color);
                    promptDialog.setSureAndCancleListener("请先登录顺联APP，参与分享呦~", "确定",
                            (view) -> {
                                Common.goGoGo(mContext,"login");
                                promptDialog.dismiss();
                            }, "取消", (view) -> promptDialog.dismiss()).show();
                    return;
                }
                mllayout_content.setVisibility(GONE);
                if (!mPopMenu.isShowing()) {
                    mPopMenu.show();
                }
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
                Common.goGoGo(getContext(),"feedback");
                hide();
                break;
            case R.id.mllayout_help:
                Common.goGoGo(getContext(),"help");
                hide();
                break;
            case R.id.mllayout_search:
                Common.goGoGo(getContext(),"search");
                hide();
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

        int totalHeight = showItemPos.length * 90 + 60;

        int[] realWH = TransformUtil.countRealWH(getContext(), 300, totalHeight);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams)
                mllayout_content.getLayoutParams();
        layoutParams.width =realWH[0];
        layoutParams.height =realWH[1];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            layoutParams.topMargin = topMargin;
        else
            layoutParams.topMargin = px;

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
        if (mPopMenu != null) {
            if (mPopMenu.isShowing()) {
                mPopMenu.hide();
            }
            mPopMenu = null;
        }
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

    public void shareInfo(ShareInfoParam shareInfoParam){
        mShareInfoParam = shareInfoParam;
    }

    /**
     * 消息数量
     * @param count
     */
    public void setMessageCount(String count){
        if (!TextUtils.isEmpty(count)) {
            mtv_message_count.setVisibility(VISIBLE);
            mtv_message_count.setText(count);
        }
    }

    /**
     * 发现详情页
     */
    public void findDetail() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px ;
        rightMargin = px / 6;
        setShowItem(1, 2, 3, 6, 7, 8);
        shareStyle2Dialog(false,false);
    }

    /**
     * 店铺
     */
    public void shop() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px / 10;
        rightMargin = px / 6;
        setShowItem(1, 3, 4, 6, 8);
        shareStyle2Dialog(false,true);
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
     * 专题页
     */
    public void special(){
        setShowItem(1,2,3,6,7,8);
        shareStyle1Dialog();
    }

    /**
     * 订单页
     */
    public void order(){
        setShowItem(1,2,3,6,7);
    }

    /**
     * 支付成功
     */
    public void paySuccess(){
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px /10;
        rightMargin = px / 6;
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
    public void help() {
        setShowItem(1,2,3,4,6);
    }

    /**
     * 要分享的帮助
     */
    public void shareHelp(){
        setShowItem(1,2,3,4,6,8);
        shareStyle1Dialog();
    }

    /**
     * 课堂详情分享
     */
    public void classDetailShare(){
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px ;
        rightMargin = px / 6;
        setShowItem(1,2,3,4,6,8);
        shareStyle1Dialog();
    }

    /**
     * 搜索
     */
    public void search(){
        setShowItem(1,3,4,5,6,7);
    }

    /**
     * 店铺二级
     */
    public void Store(){
        setShowItem(1,3,4,6,7);
    }

    /**
     * 只能分享微信和复制链接
     */
    public void shareStyle1Dialog(){
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam);
                                hide();
                                break;
                            case 1:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText();
                                hide();
                                break;
                        }
                    }
                    @Override
                    public void onClickClose(View view) {
                        mllayout_content.setVisibility(VISIBLE);
                        hide();
                    }
                }).build();
    }

    /**
     * 分享微信和复制链接，图文分享
     */
    public void shareStyle2Dialog(boolean isShow,boolean isShop){
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("图文分享", getResources().getDrawable(R.mipmap.icon_erweima)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam);
                                hide();
                                break;
                            case 1:
                                mllayout_content.setVisibility(VISIBLE);
                                if (isShop)
                                    saveshareShopPic();
                                else
                                    saveshareFindPic();
                                setVisibility(INVISIBLE);
                                break;
                            case 2:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText();
                                hide();
                                break;
                        }
                    }
                    @Override
                    public void onClickClose(View view) {
                        mllayout_content.setVisibility(VISIBLE);
                        hide();
                    }
                }).build();
        if (isShow){
            removeAllViews();
            if (!mPopMenu.isShowing()) {
                mPopMenu.show();
            }
        }
    }

    /**
     * 保存店铺分享图
     */
    private void saveshareShopPic() {
        final View inflate = LayoutInflater.from(getContext())
                .inflate(R.layout.share_shop, this, false);
        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;//
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        MyImageView miv_user_head = (MyImageView) inflate.findViewById(R.id.miv_user_head);
        GlideUtils.getInstance().loadCircleImage(getContext(),
                miv_user_head,mShareInfoParam.userAvatar);

        int i = TransformUtil.countRealWidth(getContext(), 400);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        miv_code.setImageBitmap(qrImage);

        MyImageView miv_star = (MyImageView) inflate.findViewById(R.id.miv_star);
        GlideUtils.getInstance().loadBitmapSync(getContext(),
                mShareInfoParam.shop_star, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_star.setVisibility(VISIBLE);
                        miv_star.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_star.setVisibility(GONE);
                    }
                });

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自"+mShareInfoParam.userName+"的分享");

        MyTextView mtv_shop_name = (MyTextView) inflate.findViewById(R.id.mtv_shop_name);
        mtv_shop_name.setText(mShareInfoParam.shop_name);

        CircleImageView miv_shop_head = (CircleImageView) inflate.findViewById(R.id.civ_shop_head);
        if (TextUtils.isEmpty(mShareInfoParam.shop_logo)){
            miv_shop_head.setVisibility(GONE);
            savePic(inflate);
            return;
        }
        GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.shop_logo,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_shop_head.setImageBitmap(resource);
                        miv_shop_head.setVisibility(VISIBLE);
                        savePic(inflate);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Common.staticToast("分享失败");
                    }
                });
    }

    /**
     * 保存商品分享图
     */
    public void saveshareGoodsPic() {
        removeAllViews();
        setVisibility(INVISIBLE);
        final View inflate = LayoutInflater.from(getContext())
                .inflate(R.layout.share_goods, this, false);
        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;//
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        MyImageView miv_user_head = (MyImageView) inflate.findViewById(R.id.miv_user_head);
        GlideUtils.getInstance().loadCircleImage(getContext(),
                miv_user_head,mShareInfoParam.userAvatar);

        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        int i = TransformUtil.countRealWidth(getContext(), 185);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        miv_code.setImageBitmap(qrImage);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自"+mShareInfoParam.userName+"的分享");


        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(mShareInfoParam.title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (!TextUtils.isEmpty(mShareInfoParam.desc)){
            mtv_desc.setVisibility(VISIBLE);
            mtv_desc.setText(mShareInfoParam.desc);
        }else {
            mtv_desc.setVisibility(GONE);
        }

        MyTextView mtv_price = (MyTextView) inflate.findViewById(R.id.mtv_price);
        mtv_price.setText("￥"+mShareInfoParam.goodsPrice);

        MyTextView mtv_time = (MyTextView) inflate.findViewById(R.id.mtv_time);
        MyTextView mtv_act_label = (MyTextView) inflate.findViewById(R.id.mtv_act_label);

        LinearLayout llayout_day = (LinearLayout) inflate.findViewById(R.id.llayout_day);

        if (TextUtils.isEmpty(mShareInfoParam.start_time)){
            llayout_day.setVisibility(GONE);
        }else {
            mtv_time.setText(mShareInfoParam.start_time);
            mtv_act_label.setText(mShareInfoParam.act_label);
        }

        //下载图片
        DownLoadImageThread thread = new DownLoadImageThread(mContext,mShareInfoParam.downloadPic);
        thread.start();

        MyImageView miv_goods_pic = (MyImageView) inflate.findViewById(R.id.miv_goods_pic);
        GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_goods_pic.setImageBitmap(resource);
                        inflate.postDelayed(()->{
                            Bitmap bitmapByView = getBitmapByView(inflate);
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(getContext(), bitmapByView);
                            if (isSuccess) {
                                if (mContext instanceof GoodsDetailAct){
                                    ((GoodsDetailAct)mContext).moreHideAnim();
                                }
                                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext);
                                dialog.show();
                            } else {
                                Common.staticToast("分享失败");
                            }
                            reset();
                        },100);
                    }
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Common.staticToast("分享失败");
                    }
                });
    }


    /**
     * 保存发现分享图
     */
    private void saveshareFindPic() {
        removeAllViews();
        setVisibility(INVISIBLE);
        final View inflate = LayoutInflater.from(getContext())
                .inflate(R.layout.share_find, this, false);
        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;//
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        MyImageView miv_user_head = (MyImageView) inflate.findViewById(R.id.miv_user_head);
        GlideUtils.getInstance().loadCircleImage(mContext,miv_user_head,mShareInfoParam.userAvatar);

        int i = TransformUtil.countRealWidth(getContext(), 320);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        miv_code.setImageBitmap(qrImage);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自"+mShareInfoParam.userName+"的分享");

        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(mShareInfoParam.title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (TextUtils.isEmpty(mShareInfoParam.desc)){
            mtv_desc.setVisibility(GONE);
        }else {
            mtv_desc.setVisibility(VISIBLE);
            mtv_desc.setText(mShareInfoParam.desc);
        }

        RoundRectImageView miv_bigpic = (RoundRectImageView) inflate.findViewById(R.id.miv_bigpic);
        MyImageView miv_smallpic = (MyImageView) inflate.findViewById(R.id.miv_smallpic);

        int[] ints = TransformUtil.countRealWH(mContext, 640, 300);
        ViewGroup.LayoutParams layoutParams = miv_bigpic.getLayoutParams();
        layoutParams.width = ints[0];
        layoutParams.height = ints[1];

        GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        if ("1".equals(mShareInfoParam.thumb_type)){//显示大图
                            miv_bigpic.setVisibility(VISIBLE);
                            miv_smallpic.setVisibility(GONE);
                            miv_bigpic.setImageBitmap(resource);
                        }else {
                            miv_bigpic.setVisibility(GONE);
                            miv_smallpic.setVisibility(VISIBLE);
                            miv_smallpic.setImageBitmap(resource);
                        }
                        savePic(inflate);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Common.staticToast("分享失败");
                    }
                });
    }

    private void copyText() {
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        if (!TextUtils.isEmpty(mShareInfoParam.desc)) {
            sb.append(mShareInfoParam.desc);
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(mShareInfoParam.shareLink)) {
            sb.append(mShareInfoParam.shareLink);
        }
        sb.append("\n复制这条信息，打开顺联APP~");
        ClipboardManager cm = (ClipboardManager) getContext()
                .getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(sb.toString());
        Common.staticToasts(getContext(), "复制链接成功", R.mipmap.icon_common_duihao);
    }


    public Bitmap getBitmapByView(View view) {
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private void reset(){
        removeAllViews();
        addView(mActionView);
        hide();
    }

    private void savePic(View inflate) {
        inflate.postDelayed(() -> {
            Bitmap bitmapByView = getBitmapByView(inflate);
            boolean isSuccess = BitmapUtil.saveImageToAlbumn(getContext(), bitmapByView);
            if (isSuccess) {
                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext);
                dialog.show();
            } else {
                Common.staticToast("分享失败");
            }
            reset();
        }, 100);
    }
}
