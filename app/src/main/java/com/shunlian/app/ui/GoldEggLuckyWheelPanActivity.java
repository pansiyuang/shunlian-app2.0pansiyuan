package com.shunlian.app.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.GoldRecordAdapter;
import com.shunlian.app.bean.CreditLogEntity;
import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.MyDrawRecordEntity;
import com.shunlian.app.bean.NoAddressOrderEntity;
import com.shunlian.app.bean.TaskDrawEntity;
import com.shunlian.app.presenter.GoldEggLuckyWheelPresenter;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoldEggLuckyWheelView;
import com.shunlian.app.widget.GoldEggDialog;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRecyclerView;
import com.shunlian.app.widget.ScoreRecordDialog;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.widget.luckWheel.WheelSurfView;
import com.shunlian.mylibrary.ImmersionBar;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class GoldEggLuckyWheelPanActivity extends BaseActivity implements IGoldEggLuckyWheelView, GoldEggDialog.OnDialogBtnClickListener {
    @BindView(R.id.wheelPan)
    WheelSurfView wheelPan;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.rl_title)
    RelativeLayout rl_title;

    @BindView(R.id.recycler_list)
    MyRecyclerView recycler_list;

    @BindView(R.id.text_switcher)
    TextSwitcher text_switcher;

    @BindView(R.id.tv_gold_count)
    TextView tv_gold_count;

    @BindView(R.id.tv_score_count)
    TextView tv_score_count;

    @BindView(R.id.tv_score_record)
    TextView tv_score_record;

    private GoldEggLuckyWheelPresenter mPresenter;
    private int loadCount = 0;
    private HttpDialog httpDialog;
    private List<Bitmap> mListBitmap;
    private List<GoldEggPrizeEntity.Prize> myPrizeList;
    private int luckPosition = 0;
    private GoldRecordAdapter mAdapter;
    private Dialog dialog_ad;
    private TaskDrawEntity currentTaskDraw;
    private String currentPrizeId;
    private String currentRuleUrl;
    private int consume;
    private boolean isDraw;
    private int index = 0;//textview上下滚动下标
    private Handler handler = new Handler();
    private boolean isFlipping = false, isFirstAttenion = false; // 是否启用预警信息轮播、积分提醒
    private List<String> mWarningTextList = new ArrayList<>();
    private List<MyDrawRecordEntity.DrawRecord> mReocrdList = new ArrayList<>();
    private int statusBarHeight;
    private GoldEggDialog goldEggDialog;
    private boolean isAttention;
    private long totalGoldCount, totalScoreCount;
    private String currentScore; //当前积分数量
    private PopupWindow popupWindow;
    private Timer timer;
    private boolean hasDraw; //是否有抽奖记录，来刷新积分记录列表
    private int defaultDrawType, currentDrawType;//初始化抽奖类型、当前抽奖类型
    private ScoreRecordDialog mScoreRecordDialog;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(() -> {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
            });
        }
    };

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GoldEggLuckyWheelPanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goldegg_lucky_wheel;
    }

    @Override
    protected void initData() {

        statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        RelativeLayout.LayoutParams titleLayoutParams = (RelativeLayout.LayoutParams) rl_title.getLayoutParams();
        titleLayoutParams.setMargins(0, statusBarHeight, 0, 0);
        rl_title.setLayoutParams(titleLayoutParams);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) text_switcher.getLayoutParams();
        layoutParams.setMargins(0, statusBarHeight + TransformUtil.dip2px(this, 44), 0, 0);
        text_switcher.setLayoutParams(layoutParams);

        httpDialog = new HttpDialog(this);
        goldEggDialog = new GoldEggDialog(this);
        goldEggDialog.setOnDialogBtnClickListener(this);
        mPresenter = new GoldEggLuckyWheelPresenter(this, this);
        mPresenter.getPrizeList();
        mPresenter.getDrawRecordList();
        mPresenter.getNoAddressOrder();
        mPresenter.getMyDrawRecordList();
        httpDialog.show();
        //添加滚动监听
        wheelPan.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                if (wheelPan != null) {
                    wheelPan.setEnable(false);
                }
                goldEggDialog.setShowType(currentTaskDraw);
                mPresenter.getMyDrawRecordList();
                hasDraw = true;
                if (isEmpty(currentScore)) {
                    return;
                }
                if (Long.valueOf(currentTaskDraw.credit) == 0) {
                    isDraw = false;
                    mPresenter.getPrizeList();
                }
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {
                if (wheelPan != null) {
                    wheelPan.setEnable(true);
                }
            }

            @Override
            public void rotateBefore(ImageView goImg) {
                isDraw = true;
                mPresenter.getPrizeList();
            }
        });
        myPrizeList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(linearLayoutManager);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setHasFixedSize(false);

        tv_title_right.setOnClickListener(v -> {
            if (dialog_ad != null) {
                dialog_ad.show();
            }
        });

        tv_score_record.setOnClickListener(v -> {
            showScoreRecordDialog();
        });
        setTextSwitcher();
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void initDialogs(String url) {
        if (dialog_ad == null) {
            if (isEmpty(url))
                return;
            dialog_ad = new Dialog(this, R.style.popAd);
            dialog_ad.setContentView(R.layout.dialog_gold_egg_draw_rule);
            MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
            X5WebView mwv_rule = dialog_ad.findViewById(R.id.mWebView);
            mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
//            mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
            mwv_rule.getSettings().setAppCacheMaxSize(Long.MAX_VALUE);
            mwv_rule.getSettings().setAppCachePath(Constant.CACHE_PATH_EXTERNAL);
//        h5_mwb.removeJavascriptInterface("searchBoxJavaBridge_");
//        h5_mwb.addJavascriptInterface(new SonicJavaScriptInterface(sonicSessionClient, getIntent()), "sonic");
            mwv_rule.getSettings().setAppCacheEnabled(true);
            mwv_rule.getSettings().setAllowFileAccess(true);
            //开启DOM缓存，关闭的话H5自身的一些操作是无效的
            mwv_rule.getSettings().setDomStorageEnabled(true);
            mwv_rule.getSettings().setAllowContentAccess(true);
            mwv_rule.getSettings().setDatabaseEnabled(true);
            mwv_rule.getSettings().setSavePassword(false);
            mwv_rule.getSettings().setSaveFormData(false);
            mwv_rule.getSettings().setUseWideViewPort(true);
            mwv_rule.getSettings().setLoadWithOverviewMode(true);

            mwv_rule.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            mwv_rule.getSettings().setSupportZoom(false);
            mwv_rule.getSettings().setBuiltInZoomControls(false);
            mwv_rule.getSettings().setSupportMultipleWindows(false);
            mwv_rule.getSettings().setGeolocationEnabled(true);
            mwv_rule.getSettings().setDatabasePath(this.getDir("databases", 0).getPath());
            mwv_rule.getSettings().setGeolocationDatabasePath(this.getDir("geolocation", 0)
                    .getPath());
            // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
            mwv_rule.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
            mwv_rule.loadUrl(url);
            miv_close.setOnClickListener(view -> dialog_ad.dismiss());
            mwv_rule.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedError(WebView webView, int i, String s, String s1) {
                    super.onReceivedError(webView, i, s, s1);
                }

                @Override
                public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                    super.onReceivedSslError(webView, sslErrorHandler, sslError);
                    sslErrorHandler.proceed();
                }
            });
//            dialog_ad.setCancelable(false);
        }
    }

    public void setTextSwitcher() {
        text_switcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
        text_switcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
        text_switcher.setFactory(() -> {
            TextView textView = new TextView(GoldEggLuckyWheelPanActivity.this);
            textView.setTextSize(9);//字号
            textView.setTextColor(Color.parseColor("#584C4C"));
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            textView.setSingleLine();
            textView.setMinWidth(DeviceInfoUtil.getDeviceWidth(this) / 2);
            textView.setMaxWidth(DeviceInfoUtil.getDeviceWidth(this) - TransformUtil.dip2px(this, 50));
            textView.setGravity(Gravity.CENTER);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);
            textView.setPadding(25, 0, 25, 0);
            return textView;
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (!isFlipping) {
                return;
            }
            index++;
            text_switcher.setText(mWarningTextList.get(index % mWarningTextList.size()));
            if (index == mWarningTextList.size()) {
                index = 0;
            }
            startFlipping();
        }
    };

    public void startFlipping() {
        if (mWarningTextList.size() > 1) {
            handler.removeCallbacks(runnable);
            isFlipping = true;
            handler.postDelayed(runnable, 3000);
        }
    }

    //关闭信息轮播
    public void stopFlipping() {
        if (mWarningTextList.size() > 1) {
            isFlipping = false;
            handler.removeCallbacks(runnable);
        }
    }

    public void addAllTurnTables(List<GoldEggPrizeEntity.Prize> prizes) {
        if (isEmpty(prizes)) {
            return;
        }
        loadCount = 0;
        mListBitmap = new ArrayList<>();
        loadImage(prizes);
    }

    public void loadImage(List<GoldEggPrizeEntity.Prize> prizes) {
        loadCount++;
        GlideUtils.getInstance().loadBitmapSync(this, prizes.get(loadCount - 1).image,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mListBitmap.add(resource);
//                        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_jindan));
                        if (loadCount >= prizes.size()) {
                            setWheelPanData(prizes);
                        } else {
                            loadImage(prizes);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_jindan));
                        if (loadCount >= prizes.size()) {
                            setWheelPanData(prizes);
                        } else {
                            loadImage(prizes);
                        }
                    }
                });
    }

    public void setWheelPanData(List<GoldEggPrizeEntity.Prize> prizeList) {
        String[] des = new String[prizeList.size()];
        //文字
        Integer[] colors = new Integer[prizeList.size()];
        //图标
        for (int i = 0; i < prizeList.size(); i++) {
            colors[i] = Color.parseColor("#BD9054");
            des[i] = getTrophyString(prizeList.get(i).name);
        }
        //主动旋转一下图片
        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);

        String content;
        if (totalScoreCount == 0) {
            content = consume + "金蛋一次";
        } else {
            content = consume + "积分一次";
        }
        //获取第三个视图
        LogUtil.httpLogW("prizeList:" + prizeList.size());
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmIcons(mListBitmap)
                .setmType(1)
                .setmTextColor(Color.parseColor("#80561D"))
                .setmTextSize(TransformUtil.dip2px(this, 9))
                .setmTypeNum(prizeList.size())
                .setStartContent(content)
                .build();
        wheelPan.setConfig(build);
        httpDialog.dismiss();
    }

    public String getTrophyString(String s) {
        if (isEmpty(s))
            return "";
//        if (s.length() > 6) {
//            s = s.substring(0, 6);
//        }
        char[] strings = s.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            stringBuffer.append(strings[i]);
            if (Common.isChineseCharacters(String.valueOf(strings[i]))) {
                stringBuffer.append("    ");
            } else {
                stringBuffer.append("  ");
            }
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && Activity.RESULT_OK == resultCode) {
            String addressId = data.getStringExtra("addressId");
            if (goldEggDialog != null) {
                switch (goldEggDialog.getShowType()) {
                    case 1://中奖商品
                        if (currentTaskDraw != null) {
                            mPresenter.updateOrderAddress(currentTaskDraw.draw_id, addressId);
                        }
                        break;
                    case 4: //未填写收获地址奖品
                        if (!isEmpty(currentPrizeId)) {
                            mPresenter.updateOrderAddress(currentPrizeId, addressId);
                        }
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        startFlipping();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopFlipping();
        if (wheelPan != null) {
            wheelPan.stopRotate();
        }
    }

    @Override
    public void getPrizeData(GoldEggPrizeEntity prizeEntity) {
        currentRuleUrl = prizeEntity.rule_url;
        consume = prizeEntity.consume;
        totalGoldCount = prizeEntity.gold_egg;
        currentScore = prizeEntity.credit;
        if (!isEmpty(currentScore)) {
            totalScoreCount = Long.valueOf(currentScore);
            tv_score_count.setText(String.format("积分：%d", totalScoreCount));
            visible(tv_score_count);
            showPopwindow();
        } else {
            totalScoreCount = 0;
            gone(tv_score_count);
        }
        initDialogs(currentRuleUrl);
        myPrizeList.clear();
        myPrizeList.addAll(prizeEntity.list);
        addAllTurnTables(myPrizeList);
        tv_gold_count.setText(String.format("金蛋数量：%d", totalGoldCount));

        if (!isDraw) {//假如不是抽奖
            return;
        }

        currentDrawType = SharedPrefUtil.getCacheSharedPrfInt("DrawType", 0);
        if (currentDrawType != prizeEntity.draw_type) { //清空转圈记录
            wheelPan.clearHistory(prizeEntity.list.size());
            SharedPrefUtil.saveCacheSharedPrfBoolean("isAttention", false);
        }
        SharedPrefUtil.saveCacheSharedPrfInt("DrawType", prizeEntity.draw_type);

        isAttention = SharedPrefUtil.getCacheSharedPrfBoolean("isAttention", false);
        LogUtil.httpLogW("isAttention:" + isAttention + "  currentDrawType:" + currentDrawType + "  draw_type:" + prizeEntity.draw_type);
        if (isAttention) {
            mPresenter.getTaskDraw();
        } else {
            if (!isEmpty(currentScore)) {
                if (totalScoreCount >= consume) { //积分大于等于一次消耗积分数量
                    goldEggDialog.setShowType(6, consume, totalScoreCount);
                } else if (totalScoreCount < consume && totalScoreCount > 0) {//积分小于一次消耗积分数量
                    goldEggDialog.setShowType(7, consume, totalScoreCount);
                } else if (totalScoreCount == 0) {//没有积分只能用金蛋抽奖
                    goldEggDialog.setShowType(-1, consume, totalScoreCount);
                }
            } else {
                goldEggDialog.setShowType(-1, consume, totalScoreCount);
            }
        }

//        if (currentDrawType != defaultDrawType) { //判断积分抽奖是否被关闭了
//            String luckId;
//            if (currentTaskDraw != null && !isEmpty(currentTaskDraw.id)) {
//                luckId = currentTaskDraw.id;
//                for (int i = 0; i < myPrizeList.size(); i++) {
//                    if (luckId.equals(myPrizeList.get(i).id)) {
//                        if (i == 0) {
//                            luckPosition = 1;
//                        } else {
//                            luckPosition = myPrizeList.size() + 1 - i;
//                        }
//                        wheelPan.clearHistory(myPrizeList.size());
//                        wheelPan.startRotate(luckPosition);
//                        break;
//                    }
//                }
//            }
//        }
//        defaultDrawType = prizeEntity.draw_type;
    }

    @Override
    public void getTaskDraw(TaskDrawEntity taskDrawEntity) {
        currentTaskDraw = taskDrawEntity;
//        currentDrawType = taskDrawEntity.draw_type;
        String luckId;
        if (!isEmpty(currentTaskDraw.id)) {
            luckId = currentTaskDraw.id;
            for (int i = 0; i < myPrizeList.size(); i++) {
                if (luckId.equals(myPrizeList.get(i).id)) {
                    if (i == 0) {
                        luckPosition = 1;
                    } else {
                        luckPosition = myPrizeList.size() + 1 - i;
                    }
                    wheelPan.startRotate(luckPosition);
                    break;
                }
            }
        }

        currentScore = currentTaskDraw.credit;
        if (!isEmpty(currentScore)) {
            totalScoreCount = Long.valueOf(currentScore);
            tv_score_count.setText(String.format("积分：%d", totalScoreCount));
            visible(tv_score_count);

            if (totalScoreCount < consume && totalScoreCount > 0) {
                SharedPrefUtil.saveCacheSharedPrfBoolean("isAttention", false);
            } else if (totalScoreCount == 0 && !isFirstAttenion) {
                SharedPrefUtil.saveCacheSharedPrfBoolean("isAttention", false);
                isFirstAttenion = true;
            }
        } else {
            gone(tv_score_count);
        }
        totalGoldCount = currentTaskDraw.gold_egg;
        tv_gold_count.setText(String.format("金蛋数量：%d", totalGoldCount));

//        if (currentDrawType != defaultDrawType) {
//            mPresenter.getPrizeList();
//        }
    }

    @Override
    public void getDrawRecordList(List<String> recordList) {
        try {
            if (isEmpty(recordList)) {
                text_switcher.setVisibility(View.GONE);
                return;
            } else {
                text_switcher.setVisibility(View.VISIBLE);
            }
            mWarningTextList.clear();
            mWarningTextList.addAll(recordList);
            if (mWarningTextList.size() == 1) {
                text_switcher.setText(mWarningTextList.get(0));
                index = 0;
            }
            if (mWarningTextList.size() > 1) {
                handler.postDelayed(() -> {
                    if (text_switcher != null)
                        text_switcher.setText(mWarningTextList.get(0));
                    index = 0;
                }, 1000);
                text_switcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
                text_switcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
                startFlipping();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getNoAddressData(NoAddressOrderEntity noAddressOrderEntity) {
        if (noAddressOrderEntity.status == 0) {
            return;
        }
        currentPrizeId = noAddressOrderEntity.id;
        goldEggDialog.setShowType(noAddressOrderEntity);
    }

    @Override
    public void getMyRecordList(List<MyDrawRecordEntity.DrawRecord> drawRecordList) {
        mReocrdList.clear();
        if (!isEmpty(drawRecordList)) {
            mReocrdList.addAll(drawRecordList);
        }
        if (mAdapter == null) {
            mAdapter = new GoldRecordAdapter(this, mReocrdList);
            recycler_list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void taskDrawFail() {
        goldEggDialog.setShowType(5, 0, totalScoreCount);
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void onDraw() {
        mPresenter.getTaskDraw();
    }

    @Override
    public void onSetAddress() {
        AddressListActivity.startAct(this, null);
    }

    @Override
    public void onToVisit(TaskDrawEntity.Url url) {
        Common.goGoGo(this, url.type, url.item_id);
    }

    @Override
    public void jumpTaskCenter() {
        Common.goGoGo(this, "taskSystems");
    }

    public void showPopwindow() {
        boolean isShow = SharedPrefUtil.getCacheSharedPrfBoolean("isShowScorePopWindow", false);
        if (isShow) {
            return;
        }
        if (popupWindow == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_score_draw, null);
            int width = DeviceInfoUtil.getDeviceWidth(this) - TransformUtil.dip2px(this, 18);
            popupWindow = new PopupWindow(view, width, TransformUtil.dip2px(this, 46), true);

            popupWindow.setTouchable(true);
            popupWindow.setFocusable(true);
            // 设置点击窗口外边窗口消失
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setAnimationStyle(R.style.popwindow_anim_style);
        }
        popupWindow.showAtLocation(rl_title, Gravity.TOP, 0, 0);
        SharedPrefUtil.saveCacheSharedPrfBoolean("isShowScorePopWindow", true);
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(timerTask, 5000);
    }

    public void showScoreRecordDialog() {
        if (mScoreRecordDialog == null) {
            mScoreRecordDialog = new ScoreRecordDialog(this);
        }
        if (hasDraw) {
            mScoreRecordDialog.initPage();
        }
        hasDraw = false;
        mScoreRecordDialog.show();
    }
}
