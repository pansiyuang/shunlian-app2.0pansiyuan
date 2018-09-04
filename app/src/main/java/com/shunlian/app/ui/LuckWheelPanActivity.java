package com.shunlian.app.ui;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.TurnTableAdapter;
import com.shunlian.app.bean.LuckDrawEntity;
import com.shunlian.app.bean.TurnTableEntity;
import com.shunlian.app.bean.TurnTablePopEntity;
import com.shunlian.app.presenter.TurnTablePresenter;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITurnTableView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyRecyclerView;
import com.shunlian.app.widget.TurnTableDialog;
import com.shunlian.app.widget.luckWheel.RotateListener;
import com.shunlian.app.widget.luckWheel.WheelSurfView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/8/31.
 */

public class LuckWheelPanActivity extends BaseActivity implements ITurnTableView {

    @BindView(R.id.wheelPan)
    WheelSurfView wheelPan;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.recycler_list)
    MyRecyclerView recycler_list;

    private TurnTablePresenter mPresenter;
    private int loadCount = 0;
    private HttpDialog httpDialog;
    private List<Bitmap> mListBitmap;
    private List<TurnTableEntity.Trophy> trophyList;
    private int luckPosition = 0;
    private TurnTableAdapter mAdapter;
    private TurnTableDialog turnTableDialog;
    private LuckDrawEntity currentLuckDraw;
    private String currentTmtId;

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
                turnTableDialog.show();
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(linearLayoutManager);
        recycler_list.setNestedScrollingEnabled(false);
        recycler_list.setHasFixedSize(false);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getTurnData(TurnTableEntity turnTableEntity) {
        tv_title.setText(turnTableEntity.turnTable.title);
        trophyList.clear();
        trophyList.addAll(turnTableEntity.turnTable.list);
        addAllTurnTables(trophyList);

        if (!isEmpty(turnTableEntity.myPrize)) {
            mAdapter = new TurnTableAdapter(this, turnTableEntity.myPrize);
            recycler_list.setAdapter(mAdapter);
        }
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
        turnTableDialog.show();
    }

    @Override
    public void getShareImg(String shareImg) {

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
//                        mListBitmap.add(resource);
                        mListBitmap.add(BitmapFactory.decodeResource(getResources(), R.mipmap.img_jindan));
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
        if (s.length() > 4) {
            s = s.substring(0, 4);
        }
        char[] strings = s.toCharArray();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < strings.length; i++) {
            stringBuffer.append(strings[i]);

            if (i != strings.length - 1) {
                stringBuffer.append("    ");
            }
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && Activity.RESULT_OK == resultCode) {
            String addressId = data.getStringExtra("addressId");
            if (currentLuckDraw != null) {
                mPresenter.turntableAddAddress(addressId, currentLuckDraw.id);
            } else if (!isEmpty(currentTmtId)) {
                mPresenter.turntableAddAddress(addressId, currentTmtId);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
