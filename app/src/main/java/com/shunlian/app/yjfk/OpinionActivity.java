package com.shunlian.app.yjfk;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.shunlianyoupin.ShunlianyoupinAct;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.activity.DayDayAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/16.
 */

public class OpinionActivity extends BaseActivity implements IOpinionbackView {
    @BindView(R.id.miv_ads)
    MyImageView miv_ads;
    @BindView(R.id.mll_root)
    MyLinearLayout mll_root;
    @BindView(R.id.mll_qtyj)
    MyLinearLayout mll_qtyj;
    @BindView(R.id.mtv_youxiang)
    MyTextView mtv_youxiang;
    @BindView(R.id.mwv_h5)
    X5WebView mwv_h5;
    @BindView(R.id.ksts)
    MyLinearLayout ksts;
    @BindView(R.id.gnty)
    MyLinearLayout gnty;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, OpinionActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_pinion_main;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        OpinionPresenter opinionPresenter = new OpinionPresenter(this,this);
        opinionPresenter.initOpinionfeedback();
//        ntv_uuid.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.day_haohuotaiduo));
//        ntv_uuid.setButtonText(null);
        mll_qtyj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Opinion1Activity.startAct(OpinionActivity.this);
            }
        });
        mtv_youxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.copyText(OpinionActivity.this, mtv_youxiang.getText()+"");
            }
        });
        ksts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaintActivity.startAct(OpinionActivity.this);
            }
        });
        gnty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void getOpinionfeedback(OpinionfeedbackEntity entity) {
//        WebSettings webSetting = mwv_h5.getSettings();
//        webSetting.setJavaScriptEnabled(true);
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
//        webSetting.setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
//        webSetting.setAppCacheEnabled(true);
//        mwv_h5.getSettings().setAllowFileAccess(true);
//        mwv_h5.getSettings().setDomStorageEnabled(true);
//        mwv_h5.getSettings().setAllowContentAccess(true);
//        mwv_h5.getSettings().setDatabaseEnabled(true);
//        mwv_h5.getSettings().setSavePassword(false);
//        mwv_h5.getSettings().setSaveFormData(false);
//        mwv_h5.getSettings().setUseWideViewPort(true);
//        mwv_h5.getSettings().setLoadWithOverviewMode(true);
//        mwv_h5.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        mwv_h5.getSettings().setSupportZoom(false);
//        mwv_h5.getSettings().setBuiltInZoomControls(false);
//        mwv_h5.getSettings().setSupportMultipleWindows(false);
//        mwv_h5.getSettings().setGeolocationEnabled(true);
//        mwv_h5.getSettings().setDatabasePath(this.getDir("databases", 0).getPath());
//        mwv_h5.getSettings().setGeolocationDatabasePath(this.getDir("geolocation", 0)
//                .getPath());
//        mwv_h5.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
//
//        mwv_h5.loadDataWithBaseURL(null,entity.announcemeny_content,"text/html", "utf-8",null);

        mwv_h5.getSettings().setDefaultTextEncodingName("UTF-8");
        mwv_h5.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        //web_view.loadData(map.get("NEWS_CONTENT"), "text/html", "UTF-8") ; 有时会遇到乱码问题 具体好像与sdk有关系
        mwv_h5.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
        mwv_h5.getSettings().setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
//        h5_mwb.removeJavascriptInterface("searchBoxJavaBridge_");
//        h5_mwb.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, getIntent()), "sonic");
        mwv_h5.getSettings().setAppCacheEnabled(true);
        mwv_h5.getSettings().setAllowFileAccess(true);
        //开启DOM缓存，关闭的话H5自身的一些操作是无效的
        mwv_h5.getSettings().setDomStorageEnabled(true);
        mwv_h5.getSettings().setAllowContentAccess(true);
        mwv_h5.getSettings().setDatabaseEnabled(true);
        mwv_h5.getSettings().setSavePassword(false);
        mwv_h5.getSettings().setSaveFormData(false);
        mwv_h5.getSettings().setUseWideViewPort(true);
        mwv_h5.getSettings().setLoadWithOverviewMode(true);

        mwv_h5.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        mwv_h5.getSettings().setSupportZoom(false);
        mwv_h5.getSettings().setBuiltInZoomControls(false);
        mwv_h5.getSettings().setSupportMultipleWindows(false);
        mwv_h5.getSettings().setGeolocationEnabled(true);
        mwv_h5.getSettings().setDatabasePath(this.getDir("databases", 0).getPath());
        mwv_h5.getSettings().setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
//            mwv_h5.setWebViewClient(new WebViewClient());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        mwv_h5.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        String contentHtml = "<header><meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no'></header>"+
                entity.announcement_content;
        mwv_h5.loadDataWithBaseURL(null, contentHtml, "text/html", "UTF-8", null);
        GlideUtils.getInstance().loadImage(this, miv_ads, entity.ads.get(0).image);
        miv_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Common.goGoGo(OpinionActivity.this,entity.ads.get(0).link.type);
            }
        });
    }

    @Override
    public void uploadImg(UploadPicEntity uploadPicEntity) {

    }

    @Override
    public void setRefundPics(List<String> relativePath, boolean b) {

    }

    @Override
    public void submitSuccess(String message) {

    }

    @Override
    public void submitSuccess1(BaseEntity<Opinionfeedback1Entity> data) {

    }

    @Override
    public void getcomplaintTypes(ComplaintTypesEntity entity) {

    }

    @Override
    public void getcomplaintList(List<ComplanintListEntity.Lists> entity, int allPage, int page) {

    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {
//        switch (request_code){
//            case 2:
//                visible(ntv_uuid);
//                gone(mll_root);
//        }

    }
}
