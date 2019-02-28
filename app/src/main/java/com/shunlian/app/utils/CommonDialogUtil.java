package com.shunlian.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreShareBabyAdapter;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.TeamIndexEntity;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.integral_team.TeamIntegralActivity;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.WebViewProgressBar;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.dialog.CommonDialog;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zh.chartlibrary.common.DensityUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static android.os.Build.VERSION_CODES.M;

public class CommonDialogUtil {
    public Context context;
    public CommonDialog nomalBuildl;
    private TextView tvSure;
    private TextView tvCancle;
    private TextView tvMessage;

    public CommonDialog guideInfo;
    public CommonDialog inputGuide;
    public CommonDialog inputWeixin;
    public CommonDialog dialog_user_info;
    public CommonDialog dialog_user_old;
    public CommonDialog dialog_me_teach;

    public CommonDialog dialog_weixin_copy;
    public CommonDialog dialog_h5_team;
    public CommonDialog dialog_team_paste;
    public CommonDialog dialog_team_fill;
    public CommonDialog dialog_team_into;
    public CommonDialog dialog_team_fill_pass;
    public CommonDialogUtil(Context context){
        this.context = context;
    }

    //新人领取优惠劵
    public void userNewShowDialog(ICallBackResult<String> callBackResult,String defaultValue,String warn_txt) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAdLiuhai).fullWidth()
                .setView(R.layout.dialog_page_user_new);
        dialog_user_info = nomalBuild.create();
        dialog_user_info.getWindow().getAttributes().flags= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        dialog_user_info.getWindow().getAttributes().height = App.hightPixels;
        dialog_user_info.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        dialog_user_info.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
                Window window = dialog_user_info.getWindow();
                int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                uiFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                uiFlags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;

                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.WHITE);
                window.getDecorView().setSystemUiVisibility(uiFlags);
            } else {//4.4 全透明状态栏
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        dialog_user_info.show();
        MyImageView miv_close= dialog_user_info.findViewById(R.id.miv_close);
        TextView tv_new_submit = dialog_user_info.findViewById(R.id.tv_new_submit);
        TextView  ntv_user_page_price= dialog_user_info.findViewById(R.id.ntv_user_page_price);
        TextView tv_desc_text= dialog_user_info.findViewById(R.id.tv_desc_text);
        ntv_user_page_price.setText(Common.changeTextSize( context.getResources().getString(R.string.common_yuan)+"?", context.getResources().getString(R.string.common_yuan), 16));
        if(!TextUtils.isEmpty(defaultValue)){
            tv_new_submit.setText(defaultValue);
        }
        if(!TextUtils.isEmpty(warn_txt)){
            tv_desc_text.setText(warn_txt);
        }
        tv_new_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackResult.onTagClick(tv_new_submit.getText().toString());
            }
        });
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((Activity)context).isFinishing()){
                    dialog_user_info.dismiss();
                    ((Activity)context).finish();
                    return;
                }
            }
        });
    }

    //老用户领取优惠劵
    public void userOldShowDialog(ICallBackResult<String> callBackResult,String content,int code) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,280))
                .setView(R.layout.dialog_page_user_old);
        dialog_user_old = nomalBuild.create();
        dialog_user_old.setCancelable(false);
        dialog_user_old.show();
        TextView tv_messages = dialog_user_old.findViewById(R.id.tv_messages);
        TextView  tv_sure= dialog_user_old.findViewById(R.id.tv_sure);
        TextView  tv_cancel= dialog_user_old.findViewById(R.id.tv_cancel);
       ImageView image_pic= dialog_user_old.findViewById(R.id.image_pic);
        TextView  tv_title= dialog_user_old.findViewById(R.id.tv_title);

        if(code==6201){
            tv_cancel.setText("去新人专享");
            image_pic.setVisibility(View.GONE);
            tv_title.setVisibility(View.INVISIBLE);
            tv_messages.setText("您已经领取过新人专享优惠券了哦，赶紧去使用吧");
        }else{
            tv_cancel.setText("确定");
            tv_title.setVisibility(View.VISIBLE);
            image_pic.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(content)){
                tv_messages.setText(content);
            }
        }
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_user_old.dismiss();
                callBackResult.onTagClick("home");
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_user_old.dismiss();

                callBackResult.onTagClick("page");
            }
        });
    }

    //添加微信
    public void defaultEditDialog(ICallBackResult<String> callBackResult,String defaultValue) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_weixin_input);
        inputWeixin = nomalBuild.create();
        inputWeixin.setCancelable(false);
        inputWeixin.show();
        TextView tvSure = inputWeixin.findViewById(R.id.tv_sure);
        TextView  tvCancle = inputWeixin.findViewById(R.id.tv_cancel);
        EditText edit_input = inputWeixin.findViewById(R.id.edit_input);

        if(!TextUtils.isEmpty(defaultValue)){
            edit_input.setText(defaultValue);
            edit_input.setSelection(defaultValue.length());
        }
        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(edit_input);
                inputWeixin.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_input.getText())){
                    Common.staticToast("请输入微信号");
                    return;
                }
                Common.hideKeyboard(edit_input);
                inputWeixin.dismiss();
                callBackResult.onTagClick(edit_input.getText().toString());
            }
        });
    }
    //默认的
    public void defaultCommonDialog(String message,String tv_sure, View.OnClickListener sureListener,
                                String tv_cancle, View.OnClickListener cancleListener) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_common_cart);
        nomalBuildl = nomalBuild.create();
        nomalBuildl.setCancelable(false);
        nomalBuildl.show();
        tvSure = nomalBuildl.findViewById(R.id.tv_sure);
        tvCancle = nomalBuildl.findViewById(R.id.tv_cancel);
        tvMessage = nomalBuildl.findViewById(R.id.tv_message);

        tvCancle.setText(tv_cancle);
        tvSure.setText(tv_sure);
        tvMessage.setText(message);
        tvCancle.setOnClickListener(cancleListener);
        tvSure.setOnClickListener(sureListener);
    }

    //输入导购
    public void inputGuideCommonDialog(ICallBackResult<String> callBackResult) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_member_input);
        inputGuide = nomalBuild.create();
        inputGuide.setCancelable(false);
        inputGuide.show();
        TextView tvSure = inputGuide.findViewById(R.id.tv_sure);
        TextView  tvCancle = inputGuide.findViewById(R.id.tv_cancel);
         EditText edit_input = inputGuide.findViewById(R.id.edit_input);

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(edit_input);
                inputGuide.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edit_input.getText())){
                    Common.staticToast("请输入邀请码");
                    return;
                }
                Common.hideKeyboard(edit_input);
                inputGuide.dismiss();
                callBackResult.onTagClick(edit_input.getText().toString());
            }
        });
    }


    //导购信息
    public void guideInfoCommonDialog(ICallBackResult<MemberCodeListEntity.ListBean> callBackResult, MemberCodeListEntity.ListBean listBean) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,250))
                .setView(R.layout.dialog_member_info);
        guideInfo = nomalBuild.create();
        guideInfo.setCancelable(false);
        guideInfo.show();
        TextView tvSure = guideInfo.findViewById(R.id.tv_sure);
        TextView  tvCancle = guideInfo.findViewById(R.id.tv_cancel);

        ImageView img_member_head = guideInfo.findViewById(R.id.img_member_head);
        TextView  tv_member_name = guideInfo.findViewById(R.id.tv_member_name);
        TextView  tv_member_number = guideInfo.findViewById(R.id.tv_member_number);
        if(listBean!=null){
            GlideUtils.getInstance().loadCircleAvar(context,img_member_head,listBean.avatar);
            tv_member_name.setText(listBean.nickname);
            tv_member_number.setText(listBean.code);
        }

        tvCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideInfo.dismiss();
            }
        });
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideInfo.dismiss();
                callBackResult.onTagClick(listBean);
            }
        });
    }


    public void dismiss() {
        if(nomalBuildl!=null&&nomalBuildl.isShowing()){
            nomalBuildl.dismiss();
        }
    }

    //我的导师提示
    public void meTeachCommonDialog(String weixinCode,boolean isTeach, View.OnClickListener sureListener,
                                     View.OnClickListener cancleListener) {
        if(((Activity)context).isFinishing()){
            return;
        }
        if(dialog_me_teach!=null&&dialog_me_teach.isShowing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd)
                .setView(R.layout.dialog_me_teach);
        dialog_me_teach = nomalBuild.create();
        dialog_me_teach.setCancelable(false);
        dialog_me_teach.show();
        TextView tv_teach_title = dialog_me_teach.findViewById(R.id.tv_teach_title);
        TextView tv_teach_desc = dialog_me_teach.findViewById(R.id.tv_teach_desc);
        TextView tv_teach_weixin = dialog_me_teach.findViewById(R.id.tv_teach_weixin);
        TextView tv_teach_weixin_code = dialog_me_teach.findViewById(R.id.tv_teach_weixin_code);
        TextView tv_teach_copy = dialog_me_teach.findViewById(R.id.tv_teach_copy);
        TextView tv_teach_remind = dialog_me_teach.findViewById(R.id.tv_teach_remind);
        MyImageView  miv_close= dialog_me_teach.findViewById(R.id.miv_close);
        if(isTeach) {
            tv_teach_title.setText("添加社群导师");
            tv_teach_desc.setText("获取导师一对一辅导帮助！");
            tv_teach_weixin.setText("社群导师微信");
        }else{
            tv_teach_title.setText("添加官方导师");
            tv_teach_desc.setText("获取官方一手重要资料及培训辅导!");
            tv_teach_weixin.setText("官方导师微信");
        }
        if(!TextUtils.isEmpty(weixinCode)){
            tv_teach_weixin_code.setText(weixinCode);
        }
        tv_teach_remind.setOnClickListener(cancleListener);
        tv_teach_copy.setOnClickListener(sureListener);
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_me_teach.dismiss();
            }
        });
    }
    //组队满员进入
    public void teamFillCommonDialog( TeamIndexEntity.PopData2 pop_2) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context, 340))
                .setView(R.layout.dialog_team_fill);
        dialog_team_fill = nomalBuild.create();
        dialog_team_fill.setCancelable(false);
        dialog_team_fill.show();

        ImageView miv_close = dialog_team_fill.findViewById(R.id.miv_close);
        miv_close.setOnClickListener(v -> dialog_team_fill.dismiss());

        ImageView img_team_code = dialog_team_fill.findViewById(R.id.img_team_code);
        TextView tv_team_text1 = dialog_team_fill.findViewById(R.id.tv_team_text1);
        TextView tv_team_text2 = dialog_team_fill.findViewById(R.id.tv_team_text2);
        TextView tv_team_egg = dialog_team_fill.findViewById(R.id.tv_team_egg);

        tv_team_text1.setText(pop_2.text);
        tv_team_text2.setText(pop_2.text2);
        tv_team_egg.setText(HighLightKeyWordUtil.getHighBigBoldKeyWord(25,pop_2.content_egg,pop_2.content));
        if(!TextUtils.isEmpty(pop_2.qcode_pic)){
            GlideUtils.getInstance().loadImage(context,img_team_code,pop_2.qcode_pic);
        }
    }

    //组队邀请进入提示
    public void teamPasteCommonDialog(CommondEntity.TypeData type_data, View.OnClickListener sureListener) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context, 280))
                .setView(R.layout.dialog_team_paste);
        dialog_team_paste = nomalBuild.create();
        dialog_team_paste.setCancelable(false);
        dialog_team_paste.show();
        ImageView img_team_head = dialog_team_paste.findViewById(R.id.img_team_head);
        ImageView miv_close = dialog_team_paste.findViewById(R.id.miv_close);
        TextView tv_team_nick = dialog_team_paste.findViewById(R.id.tv_team_nick);
        TextView tv_team_code = dialog_team_paste.findViewById(R.id.tv_team_code);
        TextView tv_submit_open = dialog_team_paste.findViewById(R.id.tv_submit_open);
        miv_close.setOnClickListener(v -> dialog_team_paste.dismiss());
        tv_submit_open.setOnClickListener(sureListener);
        tv_team_nick.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_d8d7d7),
                type_data.nickname+"给你分享了","给你分享了"));

        tv_team_code.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_fed147),
                type_data.text,type_data.group_name));

        tv_submit_open.setText(type_data.button);
        if(!TextUtils.isEmpty(type_data.avatar)) {
            GlideUtils.getInstance().loadCircleImage(context, img_team_head, type_data.avatar);
        }else{
            img_team_head.setImageResource(R.mipmap.img_set_defaulthead);
        }
    }

    private boolean isContinue = false;
    //组队规则H5
    public void teamH5CommonDialog(String h5_url) {
        if(((Activity)context).isFinishing()){
            return;
        }
        CommonDialog.Builder nomalBuild = new CommonDialog.Builder(context, R.style.popAd).setWidth(DensityUtil.dip2px(context,300))
                .setView(R.layout.dialog_rule_h5_team);
        dialog_h5_team = nomalBuild.create();
        dialog_h5_team.setCancelable(false);
        dialog_h5_team.show();
        ImageView miv_close = dialog_h5_team.findViewById(R.id.miv_close);
        miv_close.setOnClickListener(v -> dialog_h5_team.dismiss());
        X5WebView mwv_rule = dialog_h5_team.findViewById(R.id.mwv_rule);
        WebViewProgressBar mProgressbar = dialog_h5_team.findViewById(R.id.mProgressbar);
        WebSettings webSetting = mwv_rule.getSettings();
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
        webSetting.setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        webSetting.setAppCacheEnabled(true);
        webSetting.setAllowFileAccess(true);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        webSetting.setDomStorageEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setSavePassword(false);
        webSetting.setSaveFormData(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
//        SensorsDataAPI.sharedInstance().showUpX5WebView(mwv_h5,true);
        //x5新增
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setGeolocationEnabled(true);
        webSetting.setDatabasePath(context.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(context.getDir("geolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        mwv_rule.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mwv_rule.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //显示进度条
                if (mProgressbar != null && View.VISIBLE == mProgressbar.getVisibility() && isContinue == false) {
                    mProgressbar.setVisibility(View.VISIBLE);
                    //大于80的进度的时候,放慢速度加载,否则交给自己加载
                    if (newProgress >= 80) {
                        //拦截webView自己的处理方式
                        if (isContinue) {
                            return;
                        }
                        mProgressbar.setCurProgress(100, 3000, () -> {
                                if (mProgressbar != null) {
                                    //最后加载设置100进度
                                    mProgressbar.setNormalProgress(100);
                                    AnimationSet animation = getDismissAnim(context);
                                    animation.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {
                                        }
                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            mProgressbar.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {
                                        }
                                    });
                                    mProgressbar.startAnimation(animation);
                                }
                        });
                        isContinue = true;
                    } else {
                        mProgressbar.setNormalProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        mwv_rule.loadUrl(h5_url);
        mwv_rule.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.d("TAG", "onReceivedError: "); //如果是证书问题，会打印出此条log到console
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
                Log.d("TAG", "onReceivedSslError: "); //如果是证书问题，会打印出此条log到console
            }
        });
    }
    /**
     * 获取消失的动画
     *
     * @param context
     * @return
     */
    private AnimationSet getDismissAnim(Context context) {
        AnimationSet dismiss = new AnimationSet(context, null);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
        alpha.setDuration(1000);
        dismiss.addAnimation(alpha);
        return dismiss;
    }
    //弹出分享框
    public void shareTeamWeixinCommonDialog(ICallBackResult<String> iCallBackResult) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuildShare = new CommonDialog.Builder(context).fromBottom().fullWidth()
                .setView(R.layout.dialog_share);
        CommonDialog dialog_share = nomalBuildShare.create();
        dialog_share.setCancelable(false);
        dialog_share.show();
        dialog_share.getView(R.id.mllayout_weixinpenyou).setVisibility(View.GONE);
        dialog_share.getView(R.id.mllayout_tuwenerweima).setVisibility(View.GONE);
        dialog_share.getView(R.id.mllayout_shangping).setVisibility(View.GONE);
        dialog_share.getView(R.id.line_share_title).setVisibility(View.VISIBLE);
        ((TextView)dialog_share.getView(R.id.tv_share)).setText("分享到");
        ((TextView)dialog_share.getView(R.id.tv_text_haoyou)).setText("微信好友");
        dialog_share.getWindow().getDecorView().setOnTouchListener((v, event) -> {
            dialog_share.dismiss();
            return false;
        });
        dialog_share.setOnClickListener(R.id.ntv_cancel, v -> dialog_share.dismiss());

        dialog_share.setOnClickListener(R.id.mllayout_weixinhaoyou, v -> {
            dialog_share.dismiss();
            iCallBackResult.onTagClick("微信");
        });
    }

    //复制内容提示弹框
    public void shareCopyCommonDialog(String textPassword) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuildShare = new CommonDialog.Builder(context)
                .setView(R.layout.dialog_team_weixin_copy);
        dialog_weixin_copy = nomalBuildShare.create();
        dialog_weixin_copy.setCancelable(false);
        dialog_weixin_copy.show();
       TextView tv_text_password = dialog_weixin_copy.getView(R.id.tv_text_password);

       tv_text_password.setText(textPassword);
        dialog_weixin_copy.setOnClickListener(R.id.tv_cancel, v -> dialog_weixin_copy.dismiss());

        dialog_weixin_copy.setOnClickListener(R.id.tv_sure, v -> {
            dialog_weixin_copy.dismiss();
            Common.copyTextNoToast(context,textPassword);
            SharedPrefUtil.getWechatApi((Activity) context);
        });
    }

    //组队进入没有满员
    public void teamIntoCommonDialog( TeamIndexEntity.PopData1 pop_1) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuildShare = new CommonDialog.Builder(context).setWidth(DensityUtil.dip2px(context,300))
                .setView(R.layout.dialog_team_into);
        dialog_team_into = nomalBuildShare.create();
        dialog_team_into.setCancelable(false);
        dialog_team_into.show();

        ImageView img_user_head = dialog_team_into.getView(R.id.img_user_head);
        TextView tv_user_nick = dialog_team_into.getView(R.id.tv_user_nick);
        TextView tv_content = dialog_team_into.getView(R.id.tv_content);
        TextView tv_team_egg = dialog_team_into.getView(R.id.tv_team_egg);

        if(!TextUtils.isEmpty(pop_1.avatar)){
            GlideUtils.getInstance().loadCircleAvarRound(context,img_user_head,pop_1.avatar);
        }else{
            img_user_head.setImageResource(R.mipmap.bg_guafenjindan_morentouxiang);
        }
        tv_user_nick.setText(pop_1.nickname);
        tv_content.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_fed599),
                pop_1.text,pop_1.nickname2));
        tv_team_egg.setText(HighLightKeyWordUtil.getHighBigBoldKeyWord(25,pop_1.content_egg,pop_1.content));
        dialog_team_into.setOnClickListener(R.id.miv_close, v -> {
            dialog_team_into.dismiss();
        });
        dialog_team_into.setOnClickListener(R.id.tv_submit_open, v -> {
            dialog_team_into.dismiss();
        });

    }


    //满6个人进入满员
    public void teamIntoFillCommonDialog( TeamIndexEntity.PopData3 pop_3,ICallBackResult<String> iCallBackResult) {
        if (((Activity) context).isFinishing()) {
            return;
        }
        CommonDialog.Builder nomalBuildShare = new CommonDialog.Builder(context).setWidth(DensityUtil.dip2px(context,300))
                .setView(R.layout.dialog_team_into);
        dialog_team_fill_pass = nomalBuildShare.create();
        dialog_team_fill_pass.setCancelable(false);
        dialog_team_fill_pass.show();

        ImageView img_user_head = dialog_team_fill_pass.getView(R.id.img_user_head);
        TextView tv_user_nick = dialog_team_fill_pass.getView(R.id.tv_user_nick);
        TextView tv_content = dialog_team_fill_pass.getView(R.id.tv_content);
        TextView tv_team_egg = dialog_team_fill_pass.getView(R.id.tv_team_egg);

        if(!TextUtils.isEmpty(pop_3.avatar)){
            GlideUtils.getInstance().loadCircleAvarRound(context,img_user_head,pop_3.avatar);
        }else{
            img_user_head.setImageResource(R.mipmap.bg_guafenjindan_morentouxiang);
        }
        tv_user_nick.setText(pop_3.nickname);
        tv_content.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_fed599),
                pop_3.text,pop_3.nickname2));

        tv_team_egg.setTextColor(context.getResources().getColor(R.color.white));
        tv_team_egg.setText(pop_3.content);
        dialog_team_fill_pass.setOnClickListener(R.id.miv_close, v -> {
            dialog_team_fill_pass.dismiss();
        });
        dialog_team_fill_pass.setOnClickListener(R.id.tv_submit_open, v -> {
            dialog_team_fill_pass.dismiss();
        });
        tv_team_egg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_team_fill_pass.dismiss();
                iCallBackResult.onTagClick("创建队伍");
            }
        });
    }
}
