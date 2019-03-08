package com.shunlian.app.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.circle.RoundRectImageView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.widget.popmenu.PopMenu;
import com.shunlian.app.widget.popmenu.PopMenuItem;
import com.shunlian.app.widget.popmenu.PopMenuItemCallback;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.shunlian.mylibrary.ImmersionBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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
    private Context mContext;
    private View mActionView;
    private String dirName;
    private Unbinder bind;
    private int topMargin;
    private int rightMargin;
    private int px;
    private PromptDialog promptDialog;
    private HttpDialog httpDialog;
    private PopMenu mPopMenu;
    private ShareInfoParam mShareInfoParam;
    private String shareType = "", shareId = "";
    private Handler mHandler;
    private OnShareBlogCallBack mCallBack;
    //    private String tag="";


    public QuickActions(@NonNull Context context) {
        this(context, null);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickActions(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
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
        httpDialog = new HttpDialog(mContext);
        mHandler = new Handler(mContext.getMainLooper());
        px = TransformUtil.dip2px(mContext, 30);
        topMargin = px;
        rightMargin = px / 2;
    }

    public static boolean saveImageToShare(Context context, Bitmap bmp,boolean isFriend) {

        if (!Common.hasSD()) {
            Common.staticToast("没有sd卡");
            return false;
        }
        LogUtil.httpLogW("剩余内存:" + Common.getSDFreeSize());
        if (Common.getSDFreeSize() < 1024 * 5) {
            Common.staticToast("内存不足");
            return false;
        }
        // 首先保存图片
        File appDir = new File(Constant.CACHE_PATH_EXTERNAL, "sldl");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        File fileUri = null;
        try {
            fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            if (isSuccess) {
                // 其次把文件插入到系统图库
                String path = MediaStore.Images.Media
                        .insertImage(context.getContentResolver(),
                                file.getAbsolutePath(), fileName, null);
                if (TextUtils.isEmpty(path)) return false;
                // 最后通知图库更新
//                fileUri = new File(path);
//                context.sendBroadcast(new
//                        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(fileUri)));
                ShareInfoParam shareInfoParam = new ShareInfoParam();
                shareInfoParam.photo = path;
                WXEntryActivity.startAct(context, isFriend?"shareFriend":"shareCircle", shareInfoParam,0);
                return true;
            } else {
                return false;
            }
        }/* catch (FileNotFoundException e) {
            e.printStackTrace();
        } */ catch (Exception e) {
            e.printStackTrace();
        } finally {
            appDir = null;
            file = null;
            fileUri = null;
            fos = null;
        }
        return false;
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
                Common.goGoGo(getContext(), "message");
                hide();
                break;
            case R.id.mllayout_firstPage:
                Common.goGoGo(getContext(), "");
                hide();
                break;
            case R.id.mllayout_share:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(mContext, "login");
                    reset();
                    /*final PromptDialog promptDialog = new PromptDialog((Activity) mContext);
                    promptDialog.setTvSureColor(R.color.white);
                    promptDialog.setTvSureBg(R.color.pink_color);
                    promptDialog.setSureAndCancleListener("请先登录顺联APP，参与分享呦~", "确定",
                            (view) -> {
                                Common.goGoGo(mContext,"login");
                                reset();
                                promptDialog.dismiss();
                            }, "取消", (view) -> promptDialog.dismiss()).show();*/
                    return;
                }
//                mllayout_content.setVisibility(GONE);
                if(shareItemCallBack!=null){
                    shareItemCallBack.shareItem();
                    return;
                }
                if (mPopMenu!=null&&!mPopMenu.isShowing()) {
                    mPopMenu.show();
                }
                break;
            case R.id.mllayout_PersonalCenter:
                Common.goGoGo(getContext(), "personCenter");
                hide();
                break;
            case R.id.mllayout_car:
                Common.goGoGo(getContext(), "shoppingcar");
                hide();
                break;
            case R.id.mllayout_feedback:
                Common.goGoGo(getContext(), "feedback");
                hide();
                break;
            case R.id.mllayout_help:
                Common.goGoGo(getContext(), "help");
                hide();
                break;
            case R.id.mllayout_search:
                Common.goGoGo(getContext(), "search");
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
            showItem(showItemPos[i], showItemPos[0]);
        }

        int totalHeight = showItemPos.length * 90 + 60;

        int[] realWH = TransformUtil.countRealWH(getContext(), 300, totalHeight);
        RelativeLayout.LayoutParams layoutParams = (LayoutParams)
                mllayout_content.getLayoutParams();
        layoutParams.width = realWH[0];
        layoutParams.height = realWH[1];
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

    private void showItem(int position, int first) {
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

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            boolean b = inRangeOfView(mllayout_content, ev);
            if (!b)
                hide();
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getX() < x || ev.getX() > (x + view.getWidth())
                || ev.getY() < y || ev.getY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }

    public void shareInfo(ShareInfoParam shareInfoParam) {
        mShareInfoParam = shareInfoParam;
    }

    /**
     * 消息数量
     *
     * @param count
     */
    public void setMessageCount(String count) {
        if (!TextUtils.isEmpty(count)) {
            mtv_message_count.setVisibility(VISIBLE);
            mtv_message_count.setText(count);
        }
    }

    /**
     * 发现详情页
     */
    public void findDetail(String articleId) {
        if (!TextUtils.isEmpty(articleId)) {
            topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext);
        } else {
            topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px;
        }
        rightMargin = px / 6;
        setShowItem(1, 2, 3, 6, 7, 8);
        if (TextUtils.isEmpty(articleId)) {
            shareStyle2Dialog(false, 2);
        } else {
            shareStyle2Dialog(false, 2, "article", articleId);
        }

    }

    /**
     * 店铺
     */
    public void shop() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px / 10;
        rightMargin = px / 6;
        setShowItem(1, 3, 4, 6, 8);
        shareStyle2Dialog(false, 1);
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
        setShowItem(2, 3, 4, 6, 7);
    }

    /**
     * 售后
     */
    public void afterSale() {
        setShowItem(1, 2, 3, 6, 7);
    }

    /**
     * 活动
     */
    public void activity() {
        setShowItem(1, 2, 3, 4, 5, 6);
    }

    /**
     * 专题页
     */
    public void special(OnShareItemCallBack shareItemCallBack ) {
//        tag="special";
        this.shareItemCallBack  = shareItemCallBack;
        setShowItem(1, 2, 3, 6, 7, 8);
//        shareSpecialDialog();
    }

    private OnShareItemCallBack shareItemCallBack;
    /**
     * 只能分享微信和复制链接
     */
    public void shareSpecialDialog() {
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("图文分享", getResources().getDrawable(R.mipmap.img_tuwenfenxiang)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam,0);
                                hide();
                                break;
                            case 1:
                                mllayout_content.setVisibility(VISIBLE);
                                GlideUtils.getInstance().savePicture(getContext(), mShareInfoParam.special_img_url);
                                hide();
                                break;
                            case 2:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText(true);
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
     * 订单页
     */
    public void order() {
        setShowItem(1, 2, 3, 6, 7);
    }

    /**
     * 支付成功
     */
    public void paySuccess() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px / 10;
        rightMargin = px / 6;
        setShowItem(1, 2, 3, 6, 7);
    }

    /**
     * 发现评论列表
     */
    public void findCommentList() {
        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px - px / 10;
        setShowItem(1, 2, 3, 6, 7);
    }

    /**
     * 帮助
     */
    public void help() {
        setShowItem(1, 2, 3, 4, 6);
    }

    /**
     * 要分享的帮助
     */
    public void shareHelp() {
        setShowItem(1, 2, 3, 4, 6, 8);
        shareStyle1Dialog();
    }

    /**
     * 课堂详情分享
     */
    public void classDetailShare() {
//        topMargin = ImmersionBar.getStatusBarHeight((Activity) mContext) + px ;
//        rightMargin = px / 6;
        setShowItem(1, 2, 3, 4, 6, 8);
        shareStyle1Dialog();
    }

    /**
     * 搜索
     */
    public void search() {
        setShowItem(1, 3, 4, 5, 6, 7);
    }

    /**
     * 店铺二级
     */
    public void Store() {
        setShowItem(1, 3, 4, 6, 7);
    }

    public void createCode(String shareLink, String title, String desc, String price, String goodsId, String thumb,
                           boolean isSuperiorProduct, String from, String froms) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(mContext, "login");
        } else {
            Dialog dialog_new = new Dialog(mContext, R.style.popAd);
            dialog_new.setContentView(R.layout.share_goods_new);
            Window window = dialog_new.getWindow();
//        //设置边框距离
            window.getDecorView().setPadding(TransformUtil.dip2px(mContext, 30),
                    0,
                    TransformUtil.dip2px(mContext, 30),
                    TransformUtil.dip2px(mContext, 12));
            //设置dialog位置
//            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            //设置宽高
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
            MyLinearLayout mllayout_wexin = dialog_new.findViewById(R.id.mllayout_wexin);
            MyLinearLayout mllayout_save = dialog_new.findViewById(R.id.mllayout_save);
            MyImageView miv_user_head = dialog_new.findViewById(R.id.miv_user_head);
            MyTextView mtv_nickname = dialog_new.findViewById(R.id.mtv_nickname);
            mtv_nickname.setText("来自" + from + "的分享");
            GlideUtils.getInstance().loadCircleAvar(mContext,miv_user_head,froms);
            MyImageView miv_code = (MyImageView) dialog_new.findViewById(R.id.miv_code);
            int i = TransformUtil.dip2px(mContext, 92.5f);
            Bitmap qrImage = BitmapUtil.createQRImage(shareLink, null, i);
            miv_code.setImageBitmap(qrImage);


            MyTextView mtv_title = (MyTextView) dialog_new.findViewById(R.id.mtv_title);
            mtv_title.setText(title);

            MyTextView mtv_desc = (MyTextView) dialog_new.findViewById(R.id.mtv_desc);
            if (!TextUtils.isEmpty(desc)) {
                mtv_desc.setVisibility(View.VISIBLE);
                mtv_desc.setText(desc);
            } else {
                mtv_desc.setVisibility(View.GONE);
            }

            MyTextView mtv_price = (MyTextView) dialog_new.findViewById(R.id.mtv_price);
            mtv_price.setText("￥" + price);

//            MyTextView mtv_time = (MyTextView) dialog_new.findViewById(R.id.mtv_time);
//            MyTextView mtv_act_label = (MyTextView) dialog_new.findViewById(R.id.mtv_act_label);

            MyTextView mtv_goodsID = (MyTextView) dialog_new.findViewById(R.id.mtv_goodsID);
            mtv_goodsID.setText("商品编号:" + goodsId + "(搜索可直达)");

//            LinearLayout llayout_day = (LinearLayout) dialog_new.findViewById(R.id.llayout_day);

//            if (TextUtils.isEmpty(mShareInfoParam.start_time)) {
//                llayout_day.setVisibility(View.GONE);
//            } else {
//                mtv_time.setText(mShareInfoParam.start_time);
//                mtv_act_label.setText(mShareInfoParam.act_label);
//            }

            //显示优品图标
            MyTextView miv_SuperiorProduct = (MyTextView) dialog_new.findViewById(R.id.mtv_SuperiorProduct);
            if (isSuperiorProduct) {
                miv_SuperiorProduct.setVisibility(View.VISIBLE);
                miv_SuperiorProduct.setText("自营");
            } else {
                miv_SuperiorProduct.setVisibility(View.GONE);
            }

            MyImageView miv_goods_pic = (MyImageView) dialog_new.findViewById(R.id.miv_goods_pic);
            int width = Common.getScreenWidth((Activity) mContext) - TransformUtil.dip2px(mContext, 120);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) miv_goods_pic.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
            GlideUtils.getInstance().loadImageZheng(mContext, miv_goods_pic, thumb);

            miv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog_new.dismiss();
                }
            });

            mllayout_wexin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveshareGoodsPic(true,shareLink, title, desc, price, goodsId, thumb, isSuperiorProduct, false, from, froms);
                }
            });
            mllayout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveshareGoodsPic(true,shareLink, title, desc, price, goodsId, thumb, isSuperiorProduct, true, from, froms);
                }
            });
            dialog_new.setCancelable(false);
            dialog_new.show();
        }
    }

    public CommonDialog nomalBuildl;
//    发现对话框
    public void shareDiscoverDialog(String blodId,String shareLink, String title, String desc, String price, String goodsId, String thumb,
                                    boolean isSuperiorProduct, String from, String froms) {

        mShareInfoParam = new ShareInfoParam();
        mShareInfoParam.shareLink = shareLink;
        mShareInfoParam.title = title;
        mShareInfoParam.desc = desc;
        mShareInfoParam.img = thumb;

        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(mContext,R.style.popAd).fromBottom()
                .setView(R.layout.dialog_share);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        nomalBuildl.setOnClickListener(R.id.ntv_cancel, new OnClickListener() {
            @Override
            public void onClick(View v) {
                nomalBuildl.dismiss();
                if (!TextUtils.isEmpty(blodId) && mCallBack != null) {
                    mCallBack.shareSuccess(blodId, goodsId);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinhaoyou, new OnClickListener() {
            @Override
            public void onClick(View v) {
                WXEntryActivity.startAct(getContext(),
                        "shareFriend", mShareInfoParam,0);
                nomalBuildl.dismiss();
                if (!TextUtils.isEmpty(blodId) && mCallBack != null) {
                    mCallBack.shareSuccess(blodId, goodsId);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_weixinpenyou, new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveshareGoodsPic(false,shareLink, title, desc, price, goodsId, thumb, isSuperiorProduct, false, from, froms);
                nomalBuildl.dismiss();
                if (!TextUtils.isEmpty(blodId) && mCallBack != null) {
                    mCallBack.shareSuccess(blodId, goodsId);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_tuwenerweima, new OnClickListener() {
            @Override
            public void onClick(View v) {
                createCode(shareLink, title, desc, price, goodsId, thumb,
                isSuperiorProduct, from, froms);
                nomalBuildl.dismiss();
                if (!TextUtils.isEmpty(blodId) && mCallBack != null) {
                    mCallBack.shareSuccess(blodId, goodsId);
                }
            }
        });
        nomalBuildl.setOnClickListener(R.id.mllayout_shangping, new OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(true);
                nomalBuildl.dismiss();
                if (!TextUtils.isEmpty(blodId) && mCallBack != null) {
                    mCallBack.shareSuccess(blodId, goodsId);
                }
            }
        });

//        Dialog dialog_new = new Dialog(mContext, R.style.popAd);
//        dialog_new.setContentView(R.layout.dialog_share);
//        Window window = dialog_new.getWindow();
////        //设置边框距离
//        window.getDecorView().setPadding(TransformUtil.dip2px(mContext, 12),
//                0,
//                TransformUtil.dip2px(mContext, 12),
//                TransformUtil.dip2px(mContext, 12));
//        //设置dialog位置
////            window.setGravity(Gravity.BOTTOM);
//        WindowManager.LayoutParams lp = window.getAttributes();
//        //设置宽高
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//        window.setAttributes(lp);
//
//        NewTextView ntv_cancel = dialog_new.findViewById(R.id.ntv_cancel);
//        MyLinearLayout mllayout_tuwenerweima = dialog_new.findViewById(R.id.mllayout_tuwenerweima);
//        MyLinearLayout mllayout_weixinhaoyou = dialog_new.findViewById(R.id.mllayout_weixinhaoyou);
//        MyLinearLayout mllayout_weixinpenyou = dialog_new.findViewById(R.id.mllayout_weixinpenyou);
//        MyLinearLayout mllayout_shangping = dialog_new.findViewById(R.id.mllayout_shangping);
//
//        dialog_new.setCancelable(false);
//        dialog_new.show();
//
//        ntv_cancel.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog_new.dismiss();
//            }
//        });
//
//        mllayout_tuwenerweima.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createCode(shareLink, title, desc, price, goodsId, thumb,
//                        isSuperiorProduct, from, froms);
//                dialog_new.dismiss();
//            }
//        });
//        mllayout_weixinhaoyou.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                WXEntryActivity.startAct(getContext(),
//                        "shareFriend", mShareInfoParam);
//                dialog_new.dismiss();
//            }
//        });
//        mllayout_weixinpenyou.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                saveshareGoodsPic(false,shareLink, title, desc, price, goodsId, thumb, isSuperiorProduct, false, from, froms);
//                dialog_new.dismiss();
//            }
//        });
//        mllayout_shangping.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                copyText(true);
//                dialog_new.dismiss();
//            }
//        });
    }

//    public void dealHideBack(){
//        switch (tag){
//            case "special":
//
//                break;
//        }
//    }

    /**
     * 只能分享微信和复制链接
     */
    public void shareStyle1Dialog() {
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam,0);
                                hide();
                                break;
                            case 1:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText(true);
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
    public void shareStyle2Dialog(boolean isShow, int textPicState, String type, String id) {
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem(textPicState == 4 ? "保存二维码" : "图文分享",
                        textPicState == 4 ? getResources().getDrawable(R.mipmap.icon_erweima) :
                                getResources().getDrawable(R.mipmap.img_tuwenfenxiang)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setIsfull(textPicState == 1)
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                Constant.SHARE_TYPE = type;
                                Constant.SHARE_ID = id;
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam,0);
                                hide();
                                break;
                            case 1:
                                copyText(false);
                                shareType = type;
                                shareId = id;
                                mllayout_content.setVisibility(VISIBLE);
                                if (textPicState == 1)//店铺分享
                                    saveshareShopPic();
                                else if (textPicState == 2)//发现分享
                                    savePicAndTxt();
                                else if (textPicState == 3) {//优品分享
                                    mShareInfoParam.isSuperiorProduct = true;
                                    saveshareGoodsPic();
                                } else if (textPicState == 4) {//plus分享
                                    savesharePLUS();
                                }
                                setVisibility(INVISIBLE);
                                break;
                            case 2:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText(true);
                                hide();
                                break;
                        }
                    }

                    @Override
                    public void onClickClose(View view) {
                        mllayout_content.setVisibility(VISIBLE);
                        hide();
                    }

                    @Override
                    public void onHideCallback(Activity mActivity) {
                        if (textPicState == 1) {
                            ImmersionBar.with(mActivity).shareSever(0.0f).init();
                        } else {
                            super.onHideCallback(mActivity);
                        }
                    }
                }).build();
        if (isShow) {
            removeAllViews();
            if (!mPopMenu.isShowing()) {
                mPopMenu.show();
            }
        }
    }

    /**
     * 分享微信和复制链接，图文分享
     */
    public void shareStyle2Dialog(boolean isShow, int textPicState) {
        mPopMenu = new PopMenu.Builder().attachToActivity((Activity) mContext)
                .addMenuItem(new PopMenuItem("微信", getResources().getDrawable(R.mipmap.icon_weixin)))
                .addMenuItem(new PopMenuItem(textPicState == 4 ? "保存二维码" : "图文分享",
                        textPicState == 4 ? getResources().getDrawable(R.mipmap.icon_erweima) :
                                getResources().getDrawable(R.mipmap.img_tuwenfenxiang)))
                .addMenuItem(new PopMenuItem("复制链接", getResources().getDrawable(R.mipmap.icon_lianjie)))
                .setIsfull(textPicState == 1)
                .setOnItemClickListener(new PopMenuItemCallback() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position) {
                            case 0:
                                mllayout_content.setVisibility(VISIBLE);
                                WXEntryActivity.startAct(getContext(),
                                        "shareFriend", mShareInfoParam,0);
                                hide();
                                break;
                            case 1:
                                copyText(false);
                                mllayout_content.setVisibility(VISIBLE);
                                if (textPicState == 1)//店铺分享
                                    saveshareShopPic();
                                else if (textPicState == 2)//发现分享
                                    savePicAndTxt();
                                else if (textPicState == 3) {//优品分享
                                    mShareInfoParam.isSuperiorProduct = true;
                                    saveshareGoodsPic();
                                } else if (textPicState == 4) {//plus分享
                                    savesharePLUS();
                                }
                                setVisibility(INVISIBLE);
                                break;
                            case 2:
                                mllayout_content.setVisibility(VISIBLE);
                                copyText(true);
                                hide();
                                break;
                        }
                    }

                    @Override
                    public void onClickClose(View view) {
                        mllayout_content.setVisibility(VISIBLE);
                        hide();
                    }

                    @Override
                    public void onHideCallback(Activity mActivity) {
                        if (textPicState == 1) {
                            ImmersionBar.with(mActivity).shareSever(0.0f).init();
                        } else {
                            super.onHideCallback(mActivity);
                        }
                    }
                }).build();
        if (isShow) {
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

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自" + mShareInfoParam.userName + "的分享");

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


        MyTextView mtv_shop_name = (MyTextView) inflate.findViewById(R.id.mtv_shop_name);
        mtv_shop_name.setText(mShareInfoParam.shop_name);

        CircleImageView miv_shop_head = (CircleImageView) inflate.findViewById(R.id.civ_shop_head);

        GlideUtils.getInstance().loadBitmapSync(getContext(), mShareInfoParam.userAvatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);

                        if (TextUtils.isEmpty(mShareInfoParam.shop_logo)) {
                            miv_shop_head.setVisibility(GONE);
                            savePic(inflate);
                        } else {
                            shopPic(inflate, miv_shop_head);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);

                        if (TextUtils.isEmpty(mShareInfoParam.shop_logo)) {
                            miv_shop_head.setVisibility(GONE);
                            savePic(inflate);
                        } else {
                            shopPic(inflate, miv_shop_head);
                        }
                    }
                });
    }

    private void shopPic(View inflate, CircleImageView miv_shop_head) {
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
                        reset();
                    }
                });
    }

    /**
     * 保存商品分享图new
     */
    public void saveshareGoodsPic(boolean isFriend,String shareLink, String title, String desc, String price, String goodsId,
                                  String thumb, boolean isSuperiorProduct, boolean isShow, String from, String froms) {
        removeAllViews();
        setVisibility(INVISIBLE);
        final View inflate = LayoutInflater.from(mContext)
                .inflate(R.layout.share_goods, this, false);

        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;

        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自" + from + "的分享");

        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        int i = TransformUtil.dip2px(mContext, 92.5f);
        Bitmap qrImage = BitmapUtil.createQRImage(shareLink, null, i);
        miv_code.setImageBitmap(qrImage);


        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (!TextUtils.isEmpty(desc)) {
            mtv_desc.setVisibility(VISIBLE);
            mtv_desc.setText(desc);
        } else {
            mtv_desc.setVisibility(GONE);
        }

        MyTextView mtv_price = (MyTextView) inflate.findViewById(R.id.mtv_price);
        mtv_price.setText("￥" + price);

        MyTextView mtv_time = (MyTextView) inflate.findViewById(R.id.mtv_time);
        MyTextView mtv_act_label = (MyTextView) inflate.findViewById(R.id.mtv_act_label);

        MyTextView mtv_goodsID = (MyTextView) inflate.findViewById(R.id.mtv_goodsID);
        mtv_goodsID.setText("商品编号:" + goodsId + "(搜索可直达)");

        LinearLayout llayout_day = (LinearLayout) inflate.findViewById(R.id.llayout_day);

//        if (TextUtils.isEmpty(mShareInfoParam.start_time)) {
        llayout_day.setVisibility(GONE);
//        } else {
//            mtv_time.setText(mShareInfoParam.start_time);
//            mtv_act_label.setText(mShareInfoParam.act_label);
//        }

        //显示优品图标
        MyImageView miv_SuperiorProduct = (MyImageView) inflate.findViewById(R.id.miv_SuperiorProduct);
        if (isSuperiorProduct) {
            miv_SuperiorProduct.setVisibility(VISIBLE);
        } else {
            miv_SuperiorProduct.setVisibility(GONE);
        }


        MyImageView miv_goods_pic = (MyImageView) inflate.findViewById(R.id.miv_goods_pic);
        GlideUtils.getInstance().loadBitmapSync(mContext, froms,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);
                        goodsPic(isFriend,inflate, miv_goods_pic, thumb, isShow);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                        goodsPic(isFriend,inflate, miv_goods_pic, thumb, isShow);
                    }
                });
    }

    private void goodsPic(boolean isFriend,View inflate, MyImageView miv_goods_pic, String thumb, boolean isShow) {
        GlideUtils.getInstance().loadBitmapSync(mContext, thumb,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_goods_pic.setImageBitmap(resource);
                        inflate.postDelayed(() -> {
                            Bitmap bitmapByView = getBitmapByView(inflate);
                            if (isShow) {
                                boolean isSuccess = BitmapUtil.saveImageToAlbumn(mContext, bitmapByView,false,false);
                                if (isSuccess) {
//                                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext, shareType, shareId);
//                                dialog.show();
                                    Common.staticToast(mContext.getString(R.string.operate_tupianyibaocun));
                                } else {
                                    Common.staticToast(mContext.getString(R.string.operate_tupianbaocunshibai));
                                }
                            } else {
                                boolean isSuccess = saveImageToShare(mContext, bitmapByView,isFriend);
                                if (!isSuccess)
                                    Common.staticToast("分享失败");
                            }
                            reset();
                        }, 100);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        if (isShow) {
                            Common.staticToast(mContext.getString(R.string.operate_tupianbaocunshibai));
                        } else {
                            Common.staticToast("分享失败");
                        }
                        reset();
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

        if (mContext instanceof GoodsDetailAct &&
                ImmersionBar.hasNavigationBar((Activity) mContext)) {
            ((GoodsDetailAct) mContext).setFullScreen(true);
        }

        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;

        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自" + mShareInfoParam.userName + "的分享");

        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        int i = TransformUtil.dip2px(getContext(), 92.5f);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        miv_code.setImageBitmap(qrImage);


        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(mShareInfoParam.title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (!TextUtils.isEmpty(mShareInfoParam.desc)) {
            mtv_desc.setVisibility(VISIBLE);
            mtv_desc.setText(mShareInfoParam.desc);
        } else {
            mtv_desc.setVisibility(GONE);
        }

        MyTextView mtv_price = (MyTextView) inflate.findViewById(R.id.mtv_price);
        mtv_price.setText("￥" + mShareInfoParam.goodsPrice);

        MyTextView mtv_time = (MyTextView) inflate.findViewById(R.id.mtv_time);
        MyTextView mtv_act_label = (MyTextView) inflate.findViewById(R.id.mtv_act_label);

        MyTextView mtv_goodsID = (MyTextView) inflate.findViewById(R.id.mtv_goodsID);
        mtv_goodsID.setText("商品编号:" + mShareInfoParam.goods_id + "(搜索可直达)");

        LinearLayout llayout_day = (LinearLayout) inflate.findViewById(R.id.llayout_day);

//        if (TextUtils.isEmpty(mShareInfoParam.start_time)) {
//            llayout_day.setVisibility(GONE);
//        } else {
//            mtv_time.setText(mShareInfoParam.start_time);
//            mtv_act_label.setText(mShareInfoParam.act_label);
//        }

        //显示优品图标
        MyImageView miv_SuperiorProduct = (MyImageView) inflate.findViewById(R.id.miv_SuperiorProduct);
        if (mShareInfoParam.isSuperiorProduct) {
            miv_SuperiorProduct.setVisibility(VISIBLE);
        } else {
            miv_SuperiorProduct.setVisibility(GONE);
        }

        //下载图片
        if (mShareInfoParam.downloadPic != null && mShareInfoParam.downloadPic.size() > 0) {
            DownLoadImageThread thread = new DownLoadImageThread(mContext, mShareInfoParam.downloadPic);
            thread.start();
        }

        MyImageView miv_goods_pic = (MyImageView) inflate.findViewById(R.id.miv_goods_pic);
        GlideUtils.getInstance().loadBitmapSync(getContext(), mShareInfoParam.userAvatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);
                        goodsPic(inflate, miv_goods_pic);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                        goodsPic(inflate, miv_goods_pic);
                    }
                });
    }

    private void goodsPic(View inflate, MyImageView miv_goods_pic) {
        GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_goods_pic.setImageBitmap(resource);
                        inflate.postDelayed(() -> {
                            Bitmap bitmapByView = getBitmapByView(inflate);
                            boolean isSuccess = BitmapUtil.saveImageToAlbumn(getContext(), bitmapByView,false,false);
                            if (isSuccess) {
                                if (mContext instanceof GoodsDetailAct) {
                                    ((GoodsDetailAct) mContext).moreHideAnim();
                                    if (mContext instanceof GoodsDetailAct &&
                                            ImmersionBar.hasNavigationBar((Activity) mContext)) {
                                        ((GoodsDetailAct) mContext).setFullScreen(false);
                                    }
                                }
                                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext, shareType, shareId);
                                dialog.show();
                            } else {
                                Common.staticToast("分享失败");
                            }
                            reset();
                        }, 100);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Common.staticToast("分享失败");
                        reset();
                    }
                });
    }

    /**
     * 保存发现分享图
     */
    public void saveshareFindPic() {
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

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自" + mShareInfoParam.userName + "的分享");

        int i = TransformUtil.countRealWidth(getContext(), 320);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        miv_code.setImageBitmap(qrImage);


        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(mShareInfoParam.title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (TextUtils.isEmpty(mShareInfoParam.desc)) {
            mtv_desc.setVisibility(GONE);
        } else {
            mtv_desc.setVisibility(VISIBLE);
            mtv_desc.setText(mShareInfoParam.desc);
        }

        RoundRectImageView miv_bigpic = (RoundRectImageView) inflate.findViewById(R.id.miv_bigpic);
        MyImageView miv_smallpic = (MyImageView) inflate.findViewById(R.id.miv_smallpic);

        int[] ints = TransformUtil.countRealWH(mContext, 640, 300);
        ViewGroup.LayoutParams layoutParams = miv_bigpic.getLayoutParams();
        layoutParams.width = ints[0];
        layoutParams.height = ints[1];

        GlideUtils.getInstance().loadBitmapSync(getContext(), mShareInfoParam.userAvatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);
                        findPic(inflate, miv_bigpic, miv_smallpic);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                        findPic(inflate, miv_bigpic, miv_smallpic);
                    }
                });
    }

    private void findPic(View inflate, RoundRectImageView miv_bigpic, MyImageView miv_smallpic) {
        GlideUtils.getInstance().loadBitmapSync(mContext, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        if ("1".equals(mShareInfoParam.thumb_type)) {//显示大图
                            miv_bigpic.setVisibility(VISIBLE);
                            miv_smallpic.setVisibility(GONE);
                            miv_bigpic.setImageBitmap(resource);
                        } else {
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
                        reset();
                    }
                });
    }

    /**
     * 保存plus分享图
     */
    public void savesharePLUS() {
        removeAllViews();
        setVisibility(INVISIBLE);
        final View inflate = LayoutInflater.from(getContext())
                .inflate(R.layout.share_plus, this, false);
        ViewGroup.LayoutParams layoutParams1 = inflate.getLayoutParams();
        layoutParams1.width = ViewGroup.LayoutParams.MATCH_PARENT;//
        layoutParams1.height = ViewGroup.LayoutParams.MATCH_PARENT;
        inflate.setLayoutParams(layoutParams1);
        removeAllViews();
        addView(inflate);

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);
        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText(mShareInfoParam.userName);

        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        int i = TransformUtil.countRealWidth(getContext(), 160);
        Bitmap bitmap_logo = BitmapFactory.decodeResource(getResources(), R.mipmap.img_plus_logo);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, bitmap_logo, i);
        miv_code.setImageBitmap(qrImage);

        GlideUtils.getInstance().loadBitmapSync(getContext(), mShareInfoParam.userAvatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource,
                                                GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);
                        inflate.postDelayed(() -> savePic(inflate), 200);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                        inflate.postDelayed(() -> savePic(inflate), 200);
                    }
                });
    }


    public void copyText(boolean isToast) {
        LogUtil.httpLogW("title:" + mShareInfoParam.title + "   desc:" + mShareInfoParam.desc);
        Common.copyText(getContext(), mShareInfoParam.shareLink, mShareInfoParam.isCopyTitle ? mShareInfoParam.title : mShareInfoParam.desc, isToast);
    }

    public Bitmap getBitmapByView(View view) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                    view.getMeasuredHeight(), Bitmap.Config.RGB_565);
            final Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
        }catch (Exception e){
        }
        return bitmap;
    }

    private void reset() {
        removeAllViews();
        addView(mActionView);
        hide();
    }

    private void savePic(View inflate) {
        inflate.postDelayed(() -> {
            Bitmap bitmapByView = getBitmapByView(inflate);
            boolean isSuccess = BitmapUtil.saveImageToAlbumn(getContext(), bitmapByView,false,false);
            if (isSuccess) {
                LogUtil.httpLogW("图片保存成功");
                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) mContext, shareType, shareId);
                dialog.show();
            } else {
                Common.staticToast("分享失败");
            }
            reset();
        }, 100);
    }

    public void savePicAndTxt() {
        if (TextUtils.isEmpty(mShareInfoParam.thumb_type)) {
            return;
        }
        LogUtil.httpLogW("thumb_type:" + mShareInfoParam.thumb_type);
        switch (mShareInfoParam.thumb_type) {
            case "0"://大图小图模式
            case "1":
                saveshareFindPic();
                break;
            case "2":
                savePics();
                break;
            case "3":
                if (!TextUtils.isEmpty(mShareInfoParam.video_url)) {
                    readToDownLoad(mShareInfoParam.video_url);
                }
                break;
        }
    }

    public void savePics() {
        try {
            if (mShareInfoParam.downloadPic == null && mShareInfoParam.downloadPic.size() == 0) {
                return;
            }
            DownLoadImageThread thread = new DownLoadImageThread(getContext(), mShareInfoParam.downloadPic);
            thread.start();
            Common.copyText(getContext(), mShareInfoParam.shareLink, mShareInfoParam.title, false);
            if (promptDialog == null) {
                promptDialog = new PromptDialog((Activity) getContext());
            }
            promptDialog.setSureAndCancleListener(getResources().getString(R.string.discover_articledewenzi),
                    getResources().getString(R.string.discover_articletupian), "", getResources().getString(R.string.discover_quweixinfenxiang), v -> {
                        Common.openWeiXin(getContext(), shareType, shareId);
                        promptDialog.dismiss();
                    }, getResources().getString(R.string.errcode_cancel), v -> promptDialog.dismiss());
            promptDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readToDownLoad(String videoUrl) {
        File file = new File(dirName);
        // 文件夹不存在时创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 下载后的文件名
        int i = videoUrl.lastIndexOf("/"); // 取的最后一个斜杠后的字符串为名
        String fileName = dirName + videoUrl.substring(i, videoUrl.length());
        File file1 = new File(fileName);

        if (file1.exists()) {
            saveVideoDialog();
        } else {
            mHandler.post(() -> {
                if (httpDialog != null && !httpDialog.isShowing()) {
                    httpDialog.show();
                }
            });
            new Thread(() -> downLoadVideo(fileName)).start();
        }
    }

    public void downLoadVideo(String fileName) {
        try {
            URL url = new URL(mShareInfoParam.video_url);
            // 打开连接
            URLConnection conn = url.openConnection();
            // 打开输入流
            InputStream is = conn.getInputStream();
            // 创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            // 写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完成后关闭流
            saveVideoFile(fileName);
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveVideoFile(String fileDir) {
        //strDir视频路径
        Uri localUri = Uri.parse("file://" + fileDir);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        getContext().sendBroadcast(localIntent);

        saveVideoDialog();
    }

    public void saveVideoDialog() {
        mHandler.post(() -> {
            if (httpDialog != null && httpDialog.isShowing()) {
                httpDialog.dismiss();
            }
            Common.copyText(getContext(), mShareInfoParam.shareLink, mShareInfoParam.title, false);
            if (promptDialog == null) {
                promptDialog = new PromptDialog((Activity) getContext());
            }
            promptDialog.setSureAndCancleListener(getResources().getString(R.string.discover_articlevideo),
                    getResources().getString(R.string.discover_articleurl), "", getResources().getString(R.string.discover_quweixinfenxiang), v -> {
                        Common.openWeiXin(getContext(), shareType, shareId);
                        promptDialog.dismiss();
                    }, getResources().getString(R.string.errcode_cancel), v -> promptDialog.dismiss());
            promptDialog.show();
        });
    }

    public void setOnShareBlogCallBack(OnShareBlogCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnShareBlogCallBack {
        void shareSuccess(String blogId,String goodsId);
    }

    public interface OnShareItemCallBack {
        void shareItem();
    }
}
