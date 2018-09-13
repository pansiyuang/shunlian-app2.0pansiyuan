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
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.TurnTableAdapter;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.bean.TurnTablePopEntity;
import com.shunlian.app.presenter.TurnTablePresenter;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITurnTableView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRecyclerView;
import com.shunlian.app.widget.MyWebView;
import com.shunlian.app.widget.TurnTableDialog;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.widget.luckWheel.WheelSurfView;
import com.shunlian.app.wxapi.WXEntryActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/31.
 */

public class LuckWheelPanActivity extends BaseActivity implements ITurnTableView, TurnTableDialog.OnShareCallBack {

    @BindView(R.id.wheelPan)
    WheelSurfView wheelPan;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.recycler_list)
    MyRecyclerView recycler_list;

    @BindView(R.id.text_switcher)
    TextSwitcher text_switcher;

    private TurnTablePresenter mPresenter;
    private int loadCount = 0;
    private HttpDialog httpDialog;
    private List<Bitmap> mListBitmap;
    private List<TurnTableEntity.Trophy> trophyList;
    private List<TurnTableEntity.MyPrize> myPrizeList;
    private int luckPosition = 0;
    private TurnTableAdapter mAdapter;
    private TurnTableDialog turnTableDialog;
    private Dialog dialog_ad;
    private LuckDrawEntity currentLuckDraw;
    private String currentTmtId;
    private String currentRuleUrl;
    private boolean isFirstLoad = true;

    private int index = 0;//textview上下滚动下标
    private Handler handler = new Handler();
    private boolean isFlipping = false; // 是否启用预警信息轮播
    private List<String> mWarningTextList = new ArrayList<>();

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
                if (turnTableDialog == null) {
                    turnTableDialog = new TurnTableDialog(LuckWheelPanActivity.this, currentLuckDraw);
                } else {
                    turnTableDialog.setLuckDrawEntity(currentLuckDraw);
                }
                turnTableDialog.setCallBack(LuckWheelPanActivity.this);
                turnTableDialog.show();

                mPresenter.getTurnTableData();
            }

            @Override
            public void rotating(ValueAnimator valueAnimator) {
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
            initDialogs(currentRuleUrl);
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
            dialog_ad.setContentView(R.layout.dialog_rule);
            MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
            MyImageView miv_ad = dialog_ad.findViewById(R.id.miv_ad);
            MyWebView mwv_rule = dialog_ad.findViewById(R.id.mwv_rule);
            mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
            mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
            mwv_rule.loadUrl(url);
            miv_ad.setImageResource(R.mipmap.image_renwu_dazhuanpan);
            miv_close.setOnClickListener(view -> dialog_ad.dismiss());
            dialog_ad.setCancelable(false);
        }
        dialog_ad.show();
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
        if (isFirstLoad) {
            currentRuleUrl = turnTableEntity.rule;
            tv_title.setText(turnTableEntity.turnTable.title);
            trophyList.clear();
            trophyList.addAll(turnTableEntity.turnTable.list);
            addAllTurnTables(trophyList);

            if (isEmpty(turnTableEntity.prizeScroll)) {
                text_switcher.setVisibility(View.GONE);
                return;
            }else{
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
                    text_switcher.setText(mWarningTextList.get(0));
                    index = 0;
                }, 1000);
                text_switcher.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom));
                text_switcher.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_out_top));
                startFlipping();
            }
        }
        myPrizeList.clear();
        myPrizeList.addAll(turnTableEntity.myPrize);
        if (mAdapter == null) {
            mAdapter = new TurnTableAdapter(this, myPrizeList);
            recycler_list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        isFirstLoad = false;
    }

    @Override
    public void getLuckDraw(LuckDrawEntity luckDrawEntity) {
        currentLuckDraw = luckDrawEntity;
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
            turnTableDialog = new TurnTableDialog(LuckWheelPanActivity.this, turnTablePopEntity.show, turnTablePopEntity.list.meg, turnTablePopEntity.list.thumb);
        } else {
            turnTableDialog.setPopupData(turnTablePopEntity.show, turnTablePopEntity.list.meg, turnTablePopEntity.list.thumb);
        }
        turnTableDialog.setCallBack(this);
        turnTableDialog.show();
    }

    @Override
    public void getShareImg(String shareImg) {
        ShareInfoParam shareInfoParam = new ShareInfoParam();
        shareInfoParam.photo = shareImg;
        WXEntryActivity.startAct(this, "shareFriend", shareInfoParam);
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
            if (currentLuckDraw != null) {
                mPresenter.turntableAddAddress(addressId, currentLuckDraw.tmt_id);
            } else if (!isEmpty(currentTmtId)) {
                mPresenter.turntableAddAddress(addressId, currentTmtId);
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
    public void onShare() {
        mPresenter.turntableShareImg();
    }
}
