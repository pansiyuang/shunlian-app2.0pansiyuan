package com.shunlian.app.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SaturdayHotGoodsAdapter;
import com.shunlian.app.adapter.TurnTableAdapter;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.bean.TurnTablePopEntity;
import com.shunlian.app.presenter.TurnTablePresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITurnTableView;
import com.shunlian.app.widget.DrawRecordDialog;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRecyclerView;
import com.shunlian.app.widget.TurnTableDialog;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.widget.luckWheel.WheelSurfView;
import com.shunlian.app.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.utils.Log;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/31.
 */

public class LuckWheelPanActivity extends BaseActivity implements ITurnTableView, TurnTableDialog.OnShareCallBack, TurnTableAdapter.OnItemGetHeightListener {

    @BindView(R.id.wheelPan)
    WheelSurfView wheelPan;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_draw_time)
    TextView tv_draw_time;

    @BindView(R.id.tv_draw_record)
    TextView tv_draw_record;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.recycler_hot_goods)
    RecyclerView recycler_hot_goods;

    @BindView(R.id.text_switcher)
    TextSwitcher text_switcher;

    @BindView(R.id.ll_rootView)
    LinearLayout ll_rootView;

    private TurnTablePresenter mPresenter;
    private int loadCount = 0;
    private HttpDialog httpDialog;
    private List<Bitmap> mListBitmap;
    private List<TurnTableEntity.Trophy> trophyList;
    private List<TurnTableEntity.MyPrize> myPrizeList;
    private int luckPosition = 0;
    private TurnTableAdapter mAdapter;
    private SaturdayHotGoodsAdapter hotGoodsAdapter;
    private TurnTableDialog turnTableDialog;
    private Dialog dialog_ad;
    private LuckDrawEntity currentLuckDraw;
    private String currentTmtId;
    private String currentRuleUrl;
    private boolean isFirstLoad = true;
    private boolean hasDraw;
    private DrawRecordDialog mDialog;

    private int index = 0;//textview上下滚动下标
    private Handler handler = new Handler();
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();
    private List<TurnTableEntity.HotGoods> hotGoodsList = new ArrayList<>();
    private Timer mTimer;
    private int currentItemHeight;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, LuckWheelPanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lucky_wheel;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("大转盘规则");
        tv_title_right.setTextColor(getColorResouce(R.color.value_B78D70));

        httpDialog = new HttpDialog(this);
        mPresenter = new TurnTablePresenter(this, this);
        mPresenter.getTurnTableData();
        httpDialog.show();
        //添加滚动监听
        wheelPan.setRotateListener(new RotateListener() {
            @Override
            public void rotateEnd(int position, String des) {
                try{
                    if (wheelPan != null) {
                        wheelPan.setEnable(false);
                    }
                    hasDraw = true;
                    if (turnTableDialog == null) {
                        turnTableDialog = new TurnTableDialog(LuckWheelPanActivity.this, currentLuckDraw);
                    } else {
                        turnTableDialog.setLuckDrawEntity(currentLuckDraw);
                    }
                    turnTableDialog.setCallBack(LuckWheelPanActivity.this);
                    turnTableDialog.show();

                    mPresenter.getTurnTableData();
                }catch (Exception e){

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
                mPresenter.luckDrawData();
            }
        });
        trophyList = new ArrayList<>();
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
        tv_draw_record.setOnClickListener(v -> {
            showDrawRecordDialog();
        });

        setTextSwitcher();

        GridLayoutManager hotManager = new GridLayoutManager(this, 2);
        recycler_hot_goods.setLayoutManager(hotManager);
        hotGoodsAdapter = new SaturdayHotGoodsAdapter(this, hotGoodsList);
        recycler_hot_goods.setAdapter(hotGoodsAdapter);
        recycler_hot_goods.setNestedScrollingEnabled(false);
        hotGoodsAdapter.setOnItemClickListener((view, position) -> {
            TurnTableEntity.HotGoods hotGoods = hotGoodsList.get(position);
            GoodsDetailAct.startAct(LuckWheelPanActivity.this, hotGoods.id);
        });
        recycler_hot_goods.addItemDecoration(new GridSpacingItemDecoration(2, TransformUtil.dip2px(this, 5), false));
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
            dialog_ad.setContentView(R.layout.dialog_rule);
            MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
            MyImageView miv_ad = dialog_ad.findViewById(R.id.miv_ad);
            X5WebView mwv_rule = dialog_ad.findViewById(R.id.mwv_rule);
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
            mwv_rule.setFocusable(true);
            mwv_rule.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            mwv_rule.loadUrl(url);
            miv_ad.setImageResource(R.mipmap.image_renwu_dazhuanpan);
            miv_close.setOnClickListener(view -> dialog_ad.dismiss());
//            dialog_ad.setCancelable(false);
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
        }
    }

    public void setTextSwitcher() {
        text_switcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
        text_switcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
        text_switcher.setFactory(() -> {
            TextView textView = new TextView(LuckWheelPanActivity.this);
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

    @Override
    public void getTurnData(TurnTableEntity turnTableEntity) {
        if (tv_title == null)
            return;
        if (isFirstLoad) {
            currentRuleUrl = turnTableEntity.rule;
            initDialogs(currentRuleUrl);
            tv_title.setText(turnTableEntity.turnTable.title);
            tv_draw_time.setText(String.format("剩余次数%d", turnTableEntity.how_many));
            trophyList.clear();
            trophyList.addAll(turnTableEntity.turnTable.list);
            addAllTurnTables(trophyList);

            if (isEmpty(turnTableEntity.prizeScroll)) {
                text_switcher.setVisibility(View.INVISIBLE);
                return;
            } else {
                text_switcher.setVisibility(View.VISIBLE);
            }
            mWarningTextList.clear();
            mWarningTextList.addAll(turnTableEntity.prizeScroll);
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
        }
        myPrizeList.clear();
        hotGoodsList.clear();
        myPrizeList.addAll(turnTableEntity.prize_list);
        hotGoodsList.addAll(turnTableEntity.hot_list);
        hotGoodsAdapter.notifyDataSetChanged();
        if (mAdapter == null) {
            mAdapter = new TurnTableAdapter(this, myPrizeList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setOnItemGetHeightListener(this);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        isFirstLoad = false;
    }

    @Override
    public void getLuckDraw(LuckDrawEntity luckDrawEntity) {
        currentTmtId = null;//抽奖后把popwindow的中奖Id置空
        currentLuckDraw = luckDrawEntity;
        tv_draw_time.setText(String.format("剩余次数%d", currentLuckDraw.how_many));
        String luckId;
        if (!isEmpty(luckDrawEntity.id)) {
            luckId = luckDrawEntity.id;
            for (int i = 0; i < trophyList.size(); i++) {
                if (luckId.equals(trophyList.get(i).id)) {
                    if (i == 0) {
                        luckPosition = 1;
                    } else {
                        luckPosition = trophyList.size() + 1 - i;
                    }
                    wheelPan.startRotate(luckPosition);
                    break;
                }
            }
        }
    }

    @Override
    public void getTurnPop(TurnTablePopEntity turnTablePopEntity) {
        if (0 == turnTablePopEntity.show) {
            return;
        }
        currentTmtId = turnTablePopEntity.list.tmt_id;
        if (turnTableDialog == null) {
            turnTableDialog = new TurnTableDialog(LuckWheelPanActivity.this, turnTablePopEntity);
        } else {
            turnTableDialog.setPopupData(turnTablePopEntity);
        }
        turnTableDialog.setCallBack(this);
        turnTableDialog.show();
    }

    @Override
    public void getShareImg(String shareImg) {
        ShareInfoParam shareInfoParam = new ShareInfoParam();
        shareInfoParam.photo = shareImg;
        WXEntryActivity.startAct(this, "shareFriend", shareInfoParam,0);
    }

    public void addAllTurnTables(List<TurnTableEntity.Trophy> trophyList) {
        if (isEmpty(trophyList)) {
            return;
        }
        loadCount = 0;
        mListBitmap = new ArrayList<>();
        loadImage(trophyList);
    }

    public void loadImage(List<TurnTableEntity.Trophy> trophyList) {
        loadCount++;
        GlideUtils.getInstance().loadBitmapSync(this, trophyList.get(loadCount - 1).thumb,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mListBitmap.add(resource);
//                        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_jindan));
                        if (loadCount >= trophyList.size()) {
                            setWheelPanData(trophyList);
                        } else {
                            loadImage(trophyList);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_jindan));
                        if (loadCount >= trophyList.size()) {
                            setWheelPanData(trophyList);
                        } else {
                            loadImage(trophyList);
                        }
                    }
                });
    }

    public void setWheelPanData(List<TurnTableEntity.Trophy> trophyList) {
        String[] des = new String[trophyList.size()];
        //文字
        Integer[] colors = new Integer[trophyList.size()];
        //图标
        for (int i = 0; i < trophyList.size(); i++) {
            colors[i] = Color.parseColor("#BD9054");
            des[i] = getTrophyString(trophyList.get(i).trophy_name);
        }
        //主动旋转一下图片
        mListBitmap = WheelSurfView.rotateBitmaps(mListBitmap);

        //获取第三个视图
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmIcons(mListBitmap)
                .setmType(1)
                .setmTextColor(Color.parseColor("#80561D"))
                .setmTextSize(TransformUtil.dip2px(this, 10))
                .setmTypeNum(trophyList.size())
                .build();
        wheelPan.setConfig(build);

        httpDialog.dismiss();

        mPresenter.luckDrawPopup();
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
            if (!isEmpty(currentTmtId)) {
                mPresenter.turntableAddAddress(addressId, currentTmtId);
            } else if (currentLuckDraw != null) {
                mPresenter.turntableAddAddress(addressId, currentLuckDraw.tmt_id);
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
    protected void onRestart() {
        startScorll();
        super.onRestart();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopFlipping();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (wheelPan != null) {
            wheelPan.stopRotate();
        }
    }

    @Override
    public void onShare() {
        mPresenter.turntableShareImg();
    }

    @Override
    public void cancelDraw() {
        if (turnTableDialog != null) {
            turnTableDialog.showAttentionData();
            turnTableDialog.show();
        }
    }

    public void showDrawRecordDialog() {
        if (mDialog == null) {
            mDialog = new DrawRecordDialog(this);
        }
        if (hasDraw) {
            mDialog.initPage();
        }
        hasDraw = false;
        mDialog.show();
    }

    public void startScorll() {
        mTimer = new Timer();
        if (currentItemHeight == 0) {
            return;
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (recycler_list!=null){
                        if (isSlideToBottom(recycler_list)) {
                            recycler_list.smoothScrollToPosition(0);
                            return;
                        }
                        recycler_list.smoothScrollBy(0, currentItemHeight * 5, new LinearInterpolator());
                    }
                });
            }
        }, 100, 2000);
    }

    public boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    @Override
    public void getItemHeight(int height) {
        currentItemHeight = height;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TransformUtil.dip2px(this, 25) + 6 * currentItemHeight);
        ll_rootView.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams rl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5 * currentItemHeight);
        recycler_list.setLayoutParams(rl);
        startScorll();
    }
}
