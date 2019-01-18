package com.shunlian.app.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.GoldRecordAdapter;
import com.shunlian.app.bean.GoldEggPrizeEntity;
import com.shunlian.app.bean.MyDrawRecordEntity;
import com.shunlian.app.bean.NoAddressOrderEntity;
import com.shunlian.app.bean.TaskDrawEntity;
import com.shunlian.app.presenter.GoldEggLuckyWheelPresenter;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoldEggLuckyWheelView;
import com.shunlian.app.widget.GoldEggDialog;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRecyclerView;
import com.shunlian.app.widget.MyWebView;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.widget.luckWheel.WheelSurfView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private int goldEggCount;
    private boolean isFirstLoad = true;

    private int index = 0;//textview上下滚动下标
    private Handler handler = new Handler();
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();
    private List<MyDrawRecordEntity.DrawRecord> mReocrdList = new ArrayList<>();
    private int statusBarHeight;
    private GoldEggDialog goldEggDialog;
    private boolean isAttention;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, GoldEggLuckyWheelPanActivity.class);
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
                goldEggDialog.setShowType(currentTaskDraw);
                mPresenter.getMyDrawRecordList();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {
            }

            @Override
            public void rotateBefore(ImageView goImg) {
                isAttention = SharedPrefUtil.getCacheSharedPrfBoolean("isAttention", false);
                if (isAttention) {
                    mPresenter.getTaskDraw();
                } else {
                    goldEggDialog.setShowType(-1, goldEggCount);
                }
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

        setTextSwitcher();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void initDialogs(String url) {
        if (dialog_ad == null) {
            if (isEmpty(url))
                return;
            dialog_ad = new Dialog(this, R.style.popAd);
            dialog_ad.setContentView(R.layout.dialog_gold_egg_draw_rule);
            MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
            MyWebView mwv_rule = dialog_ad.findViewById(R.id.mWebView);
            mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
            mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
            mwv_rule.loadUrl(url);
            miv_close.setOnClickListener(view -> dialog_ad.dismiss());
            dialog_ad.setCancelable(false);
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
                        if (loadCount >= myPrizeList.size()) {
                            setWheelPanData(myPrizeList);
                        } else {
                            loadImage(myPrizeList);
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

        //获取第三个视图
        WheelSurfView.Builder build = new WheelSurfView.Builder()
                .setmColors(colors)
                .setmDeses(des)
                .setmIcons(mListBitmap)
                .setmType(1)
                .setmTextColor(Color.parseColor("#80561D"))
                .setmTextSize(TransformUtil.dip2px(this, 9))
                .setmTypeNum(prizeList.size())
                .setStartContent(goldEggCount + "金蛋一次")
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
    }

    @Override
    public void getPrizeData(GoldEggPrizeEntity prizeEntity) {
        if (isFirstLoad) {
            currentRuleUrl = prizeEntity.rule_url;
            goldEggCount = prizeEntity.consume;
            initDialogs(currentRuleUrl);
            myPrizeList.clear();
            myPrizeList.addAll(prizeEntity.list);
            addAllTurnTables(myPrizeList);
        }
        myPrizeList.clear();
        myPrizeList.addAll(prizeEntity.list);
        isFirstLoad = false;
    }

    @Override
    public void getTaskDraw(TaskDrawEntity taskDrawEntity) {
        currentTaskDraw = taskDrawEntity;
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
    }

    @Override
    public void getDrawRecordList(List<String> recordList) {
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
        goldEggDialog.setShowType(5, 0);
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
}
