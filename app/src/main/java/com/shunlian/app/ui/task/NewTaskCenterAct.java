package com.shunlian.app.ui.task;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.NewEggDetailAdapter;
import com.shunlian.app.adapter.SignAdapter;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.DayGiveEggEntity;
import com.shunlian.app.bean.NewEggDetailEntity;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.TaskDownTimerView;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.CompileScrollView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.X5WebView;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.tencent.smtt.sdk.WebSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2019/1/5.
 */

public class NewTaskCenterAct extends BaseActivity implements ITaskCenterView {

    @BindView(R.id.ntv_sign)
    public NewTextView ntv_sign;

    @BindView(R.id.recy_view)
    RecyclerView recyView;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.csv_out)
    CompileScrollView csv_out;

    @BindView(R.id.view_eggdetail)
    View view_eggdetail;

    @BindView(R.id.view_eggdetails)
    View view_eggdetails;

    @BindView(R.id.mllayout_mid)
    MyLinearLayout mllayout_mid;

    @BindView(R.id.rv_sign)
    RecyclerView rv_sign;

    @BindView(R.id.ntv_eggnum)
    NewTextView ntv_eggnum;

    @BindView(R.id.ntv_titleOne)
    NewTextView ntv_titleOne;

    @BindView(R.id.ntv_content)
    NewTextView ntv_content;

    @BindView(R.id.miv_chose)
    MyImageView miv_chose;

    @BindView(R.id.mrlayout_goGet)
    MyRelativeLayout mrlayout_goGet;

    @BindView(R.id.animation_view)
    LottieAnimationView animation_view;

    @BindView(R.id.miv_golden_eggs)
    MyImageView miv_golden_eggs;

    @BindView(R.id.miv_one)
    MyImageView miv_one;

    @BindView(R.id.miv_two)
    MyImageView miv_two;

    @BindView(R.id.miv_get)
    MyImageView miv_get;

    @BindView(R.id.miv_gets)
    MyImageView miv_gets;

    @BindView(R.id.ntv_titleOnes)
    NewTextView ntv_titleOnes;

    @BindView(R.id.ntv_contents)
    NewTextView ntv_contents;

    @BindView(R.id.ntv_titleOness)
    NewTextView ntv_titleOness;

    @BindView(R.id.ntv_task)
    NewTextView ntv_task;

    @BindView(R.id.ddp_downTime)
    TaskDownTimerView ddp_downTime;

    @BindView(R.id.miv_signDetail)
    MyImageView miv_signDetail;

    @BindView(R.id.miv_left)
    MyImageView miv_left;

    @BindView(R.id.miv_mid)
    MyImageView miv_mid;
    //   气泡
    @BindView(R.id.lLayout_toast)
    LinearLayout lLayout_toast;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_info)
    TextView tv_info;

    LinearLayoutManager linearLayoutManager;
    NewEggDetailAdapter eggDetailAdapter;
    private boolean isStop, isCrash;
    private boolean isPause = true;
    private Runnable runnableA, runnableB, runnableC;
    private Timer outTimer;
    private int mposition, size;
    private TaskCenterPresenter mPresenter;
    private SignAdapter signAdapter;
    private Dialog dialog_rule, dialog_detail;
    private String miss_eggs, is_remind;
    private boolean isReceive;//是否可以领取金蛋
    private static Handler handler;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewTaskCenterAct.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void beginToast() {
        if (isPause) {
            mposition = 0;
            isStop = false;
            if (mPresenter != null)
                mPresenter.getBubble();
            isPause = false;
        }
    }

    public void stopToast() {
        if (!isCrash) {
            isPause = true;
            isStop = true;
            if (lLayout_toast != null) {
                LogUtil.augusLogW("mposition:gone");
                lLayout_toast.setVisibility(View.GONE);
            }
            if (outTimer != null) {
                LogUtil.augusLogW("mposition:cancel");
                outTimer.cancel();
            }
            if (handler != null) {
                LogUtil.augusLogW("mposition:remove");
                if (runnableA != null) {
                    handler.removeCallbacks(runnableA);
                }
                if (runnableB != null) {
                    handler.removeCallbacks(runnableB);
                }
                if (runnableC != null) {
                    handler.removeCallbacks(runnableC);
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (ddp_downTime != null) {
            ddp_downTime.cancelDownTimer();
        }
        stopToast();
    }

    public void startTimer() {
        if (handler == null) {
            handler = new Handler();
        }
        runnableA = () -> {
            if (!isStop) {
                LogUtil.augusLogW("mposition：delayed");
                mposition = 0;
                if (mPresenter != null)
                    mPresenter.getBubble();
            }
        };
        handler.postDelayed(runnableA, ((Constant.BUBBLE_SHOW + Constant.BUBBLE_DUR) * size + 1) * 1000);
    }
    //   气泡

    public void startToast(final List<BubbleEntity.Content> datas) {
        if (outTimer != null) {
            outTimer.cancel();
        }
        outTimer = new Timer();
        try {
            outTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mposition < datas.size()) {
                        runnableB = () -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && baseAct.isDestroyed()) {
//                                throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
                            } else if (mposition < datas.size() && lLayout_toast != null
                                    && miv_icon != null && tv_info != null && !baseAct.isFinishing()) {
                                LogUtil.augusLogW("mposition:" + mposition);
                                lLayout_toast.setVisibility(View.VISIBLE);
                                GlideUtils.getInstance().loadCircleAvar(baseAct, miv_icon, datas.get(mposition).avatar);
                                tv_info.setText(datas.get(mposition).text);
                                lLayout_toast.setOnClickListener(v -> {
                                    if (datas.get(mposition).url != null)
                                        Common.goGoGo(baseAct, datas.get(mposition).url.type,
                                                datas.get(mposition).url.item_id);
                                });
                            }
                        };
                        if (handler == null) {
                            if (!isCrash) {
                                isCrash = true;
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                mHandler.postDelayed(() -> isCrash = false,
                                        ((Constant.BUBBLE_SHOW + Constant.BUBBLE_DUR) * size + 2) * 1000);
                            }
                        } else {
                            handler.post(runnableB);
                            runnableC = () -> {
                                if (!isStop && lLayout_toast != null) {
                                    lLayout_toast.setVisibility(View.GONE);
                                    mposition++;
                                }
                            };
                            handler.postDelayed(runnableC, Constant.BUBBLE_SHOW * 1000);
                        }
                    }
                }
            }, 0, (Constant.BUBBLE_SHOW + Constant.BUBBLE_DUR) * 1000);
        } catch (Exception e) {

        }
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_new_task_center;
    }

    @Override
    protected void initListener() {
        super.initListener();
        csv_out.setOnScrollListener((y, oldy) -> {
            if (y > 30) {
                view_eggdetail.setAlpha(1);
                view_eggdetails.setAlpha(0);
            } else if (y > 0) {
                float alpha = ((float) y) / 30;
                view_eggdetail.setAlpha(alpha);
                view_eggdetails.setAlpha(1 - alpha);
            } else {
                view_eggdetail.setAlpha(0);
                view_eggdetails.setAlpha(1);
                csv_out.setFocusable(false);
            }
        });


        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null)
                    visible(customView.findViewById(R.id.view_line));
                if ((int) tab.getTag() == TaskCenterPresenter.NEW_USER_TASK) {
                    if (mPresenter != null) {
                        mPresenter.current_task_state = TaskCenterPresenter.NEW_USER_TASK;
                        mPresenter.cacheTaskList();
                    }
                } else if ((int) tab.getTag() == TaskCenterPresenter.DAILY_TASK) {
                    if (mPresenter != null) {
                        mPresenter.current_task_state = TaskCenterPresenter.DAILY_TASK;
                        mPresenter.cacheTaskList();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                if (customView != null)
                    customView.findViewById(R.id.view_line).setVisibility(View.INVISIBLE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        miv_chose.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        immersionBar.statusBarView(R.id.view_state).init();
        mPresenter = new TaskCenterPresenter(this, this);
        setGoldEggsAnim("eggs_not_hatch.json", miv_golden_eggs,
                animation_view, true, "eggs/img_1.png");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        recyView.setNestedScrollingEnabled(false);

        view_eggdetail.setAlpha(0);
        view_eggdetails.setAlpha(1);
    }

    @Override
    protected void onResume() {
        beginToast();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mPresenter != null) mPresenter.attachView();
    }

    public void setGoldEggsAnim(String filename, MyImageView miv_denglong,
                                LottieAnimationView animation_view, boolean isLoop, String photo) {
        AssetManager assets = null;
        InputStream is = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gone(miv_denglong);
                visible(animation_view);
                animation_view.setAnimation(filename);//在assets目录下的动画json文件名。
                animation_view.loop(isLoop);//设置动画循环播放
                animation_view.setImageAssetsFolder("eggs/");//assets目录下的子目录，存放动画所需的图片
                animation_view.playAnimation();//播放动画
            } else {
                visible(miv_denglong);
                gone(animation_view);
                assets = getAssets();
                is = assets.open(photo);
                miv_denglong.setImageBitmap(BitmapFactory.decodeStream(is));
            }
        } catch (Exception e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (assets != null) assets.close();
        }
    }


    private View getTabView(int position) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);


        MyImageView imageView = new MyImageView(this);
        layout.addView(imageView);
        imageView.setWHProportion(220, 48);
        if (TaskCenterPresenter.NEW_USER_TASK == position) {
            imageView.setImageResource(R.mipmap.img_task_xinshourenwu);
            imageView.setId(R.id.iv_task_new);
        } else if (TaskCenterPresenter.DAILY_TASK == position) {
            imageView.setImageResource(R.mipmap.img_task_richangrenwu);
            imageView.setId(R.id.iv_task);
        }

        View view = new View(this);
        layout.addView(view);
        LinearLayout.LayoutParams viewParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        int i = TransformUtil.dip2px(this, 2);
        viewParams.width = i * 32;
        viewParams.height = i;
        viewParams.topMargin = i * 4;
        view.setLayoutParams(viewParams);
        view.setBackgroundResource(R.mipmap.btn_sel);
        view.setId(R.id.view_line);
        view.setVisibility(View.INVISIBLE);
        return layout;
    }


    /**
     * 签到成功弹窗
     *
     * @param data
     */
    public void initDialog(SignEggEntity data) {
        Dialog dialog_commond = new Dialog(baseAct, R.style.popAd);
        dialog_commond.setContentView(R.layout.dialog_sign);

        MyImageView miv_close = dialog_commond.findViewById(R.id.miv_close);
        MyImageView miv_photo = dialog_commond.findViewById(R.id.miv_photo);
        NewTextView ntv_detail = dialog_commond.findViewById(R.id.ntv_detail);
        NewTextView ntv_from = dialog_commond.findViewById(R.id.ntv_from);

        GlideUtils.getInstance().loadImageZheng(baseAct, miv_photo, data.ad_pic_url);

        ntv_from.setText(String.format(getStringResouce(R.string.mission_gongxininhuode), data.gold_num));

        miv_close.setOnClickListener(view -> dialog_commond.dismiss());

        if (data.url != null && "nojump".equals(data.url.type)) {
            miv_close.setVisibility(View.GONE);
            ntv_detail.setText("确定");
        }
        ntv_detail.setOnClickListener(view -> {
            if (data.url != null && !"nojump".equals(data.url.type))
                Common.goGoGo(baseAct, data.url.type, data.url.item_id);
            dialog_commond.dismiss();
        });

        dialog_commond.setCancelable(false);
        dialog_commond.show();
    }

    /*
    金蛋明细
     */
    public void initDetailDialog(int allPage, int page, List<NewEggDetailEntity.In> list) {
        if (dialog_detail == null) {
            dialog_detail = new Dialog(this, R.style.popAd);
            dialog_detail.setContentView(R.layout.dialog_egg_detail);
            MyImageView miv_close = dialog_detail.findViewById(R.id.miv_close);
            RecyclerView rv_detail = dialog_detail.findViewById(R.id.rv_detail);
            NetAndEmptyInterface nei_empty = dialog_detail.findViewById(R.id.nei_empty);

            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText(getString(R.string.mission_zanwumingxi))
                    .setButtonText(null);

            rv_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (linearLayoutManager != null) {
                        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                        if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                            if (mPresenter != null) {
                                mPresenter.refreshBaby();
                            }
                        }
                    }
                }
            });

            miv_close.setOnClickListener(view -> dialog_detail.dismiss());
            dialog_detail.setCancelable(false);

            if (isEmpty(list)) {
                visible(nei_empty);
                gone(rv_detail);
            } else {
                visible(rv_detail);
                gone(nei_empty);
                eggDetailAdapter = new NewEggDetailAdapter(dialog_detail.getContext(), false, list);
                linearLayoutManager = new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false);
                rv_detail.setLayoutManager(linearLayoutManager);
                rv_detail.setAdapter(eggDetailAdapter);
            }
        } else {
            eggDetailAdapter.notifyDataSetChanged();
        }
        dialog_detail.show();
        if (eggDetailAdapter != null)
            eggDetailAdapter.setPageLoading(page, allPage);

    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.miv_chose:
                Common.goGoGo(this,"taskGoldenEggTurnTable");
                break;
        }
    }

    /*
       签到规则弹窗
        */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    public void initRuleDialog(String url) {
        if (isEmpty(url))
            return;
        dialog_rule = new Dialog(this, R.style.popAd);
        dialog_rule.setContentView(R.layout.dialog_rule_new);
        MyImageView miv_close = dialog_rule.findViewById(R.id.miv_close);
        X5WebView mwv_rule = dialog_rule.findViewById(R.id.mwv_rule);
        mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
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

        miv_close.setOnClickListener(view -> dialog_rule.dismiss());
        dialog_rule.setCancelable(false);
    }

    /*
 提示弹窗
  */
    public void initHintialog(String type, String title, String desc) {
        Dialog dialog_hint = new Dialog(this, R.style.popAd);
        dialog_hint.setContentView(R.layout.dialog_egg_hint);
        NewTextView ntv_btn = dialog_hint.findViewById(R.id.ntv_btn);
        NewTextView ntv_desc = dialog_hint.findViewById(R.id.ntv_desc);
        NewTextView ntv_title = dialog_hint.findViewById(R.id.ntv_title);
        MyLinearLayout mllayout_goget = dialog_hint.findViewById(R.id.mllayout_goget);
        ntv_desc.setText(desc);

        switch (type) {
            case "remind":
                ntv_title.setBackgroundResource(R.mipmap.image_renwu_cg02);
                ntv_title.setTextSize(18);
                ntv_title.setText(title);
                ntv_desc.setText(desc);
                ntv_desc.setVisibility(View.VISIBLE);
                ntv_btn.setText("准点提醒");
                ntv_btn.setOnClickListener(view -> {
                    mPresenter.setRemind();
                    dialog_hint.dismiss();
                });
                break;
            case "goget":
                ntv_title.setBackgroundResource(R.mipmap.image_renwu_cg03);
                mllayout_goget.setVisibility(View.VISIBLE);
                ntv_title.setTextSize(18);
                ntv_title.setText(title);
                ntv_btn.setOnClickListener(view -> dialog_hint.dismiss());
                break;
            case "getok":
                ntv_title.setBackgroundResource(R.mipmap.image_renwu_cg01);
                ntv_title.setText(title);
                ntv_desc.setText(desc);
                ntv_desc.setVisibility(View.VISIBLE);
                ntv_btn.setOnClickListener(view -> {
                    if ("0".equals(is_remind)) {
                        initHintialog("remind", "在过去的几天里错过了"
                                + miss_eggs + "个金蛋", "现在设置准点提醒，让您“蛋”无虚发! ");
                    }
                    dialog_hint.dismiss();
                });
                break;
        }
        dialog_hint.setCancelable(false);
        dialog_hint.show();
    }

    @Override
    public void setBubble(BubbleEntity data) {
        size = 2;
        if (!isEmpty(data.list)) {
            size = data.list.size();
            startToast(data.list);
        }
        startTimer();
    }

    /**
     * 金蛋数量
     *
     * @param count
     */
    @Override
    public void setGoldEggsCount(String count) {
        ntv_eggnum.setText(count);
    }

    /**
     * ；连续签到天数
     *
     * @param num
     */
    @Override
    public void setSignContinueNum(String num) {

    }

    @OnClick({R.id.animation_view, R.id.miv_golden_eggs, R.id.miv_get})
    public void goldEggs() {
        if (mPresenter != null && isReceive) {
            mPresenter.goldegglimit();
        } else {
           initBubble(miv_left,false);
        }
    }

    /**
     * @param second      倒计时秒数
     * @param maxProgress 最大进度
     * @param task_status
     */
    @Override
    public void obtainDownTime(String second, String maxProgress, String task_status) {
        if ("0".equals(task_status)) {
            isReceive = true;
            setGoldEggsAnim("eggs_hatch.json", miv_golden_eggs,
                    animation_view, true, "eggs/img_1.png");
            if (mPresenter != null) mPresenter.getTaskList();
            visible(miv_get);
            ddp_downTime.setVisibility(View.INVISIBLE);
        } else {
            isReceive = false;
            setGoldEggsAnim("eggs_not_hatch.json", miv_golden_eggs,
                    animation_view, true, "eggs/img_1.png");
            gone(miv_get);
            visible(ddp_downTime);
            String times = isEmpty(second) ? "0" : second;
            ddp_downTime.setDownTime(Integer.parseInt(times));
            ddp_downTime.startDownTimer();
            ddp_downTime.setDownTimerListener(() -> {
                setGoldEggsAnim("eggs_hatch.json", miv_golden_eggs,
                        animation_view, true, "eggs/img_1.png");
                isReceive = true;
                if (mPresenter != null) mPresenter.getTaskList();
                if (mPresenter != null &&
                        mPresenter.current_task_state == TaskCenterPresenter.DAILY_TASK)
                    mPresenter.updateItem(0, "0");
            });
        }
    }

    /**
     * 广告图
     *
     * @param url
     * @param urlBean
     * @param adUrlRollBean
     */
    @Override
    public void setPic(String url, TaskHomeEntity.AdUrlBean urlBean,
                       List<TaskHomeEntity.AdUrlRollBean> adUrlRollBean) {
        if (adUrlRollBean != null && adUrlRollBean.size() > 0) {
            visible(kanner);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < adUrlRollBean.size(); i++) {
                strings.add(adUrlRollBean.get(i).ad_pic_url);
                if (i >= adUrlRollBean.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(position ->
                            Common.goGoGo(baseAct, adUrlRollBean.get(position).ad_url.type,
                                    adUrlRollBean.get(position).ad_url.item_id));
                }
            }
        } else {
            gone(kanner);
        }
    }

    /**
     * 签到规则
     */
    @OnClick(R.id.miv_signDetail)
    public void rule() {
        if (dialog_rule != null)
            dialog_rule.show();
    }

    /**
     * @param question 常见问题
     * @param rule     签到规则
     */
    @Override
    public void setTip(String question, String rule) {
        initRuleDialog(rule);
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {
        if (666 == request_code) {
            size = 2;
            startTimer();
        }
    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        if (recyView != null) {
            recyView.setAdapter(adapter);
        }
    }

    /**
     * 关闭新手任务列表
     *
     * @param isClose
     */
    @Override
    public void closeNewUserList(boolean isClose) {
        if (tab_layout != null) {
            tab_layout.removeAllTabs();
            if (isClose) {//只有日常任务
                TabLayout.Tab tab = tab_layout.newTab();
                tab.setCustomView(getTabView(TaskCenterPresenter.DAILY_TASK));
                tab.setTag(TaskCenterPresenter.DAILY_TASK);
                tab_layout.addTab(tab);
            } else {//新手任务和日常任务
                for (int i = 0; i < 2; i++) {
                    TabLayout.Tab tab = tab_layout.newTab();
                    int tag = 0;
                    if (i == 0) {
                        tag = TaskCenterPresenter.NEW_USER_TASK;
                    } else {
                        tag = TaskCenterPresenter.DAILY_TASK;
                    }
                    tab.setCustomView(getTabView(tag));
                    tab.setTag(tag);
                    tab_layout.addTab(tab);
                }
            }
        }
    }

    /**
     * 签到主页
     *
     * @param list
     * @param sign_continue_num
     */
    @Override
    public void setSignData(List<TaskHomeEntity.SignDaysBean> list, String sign_continue_num) {
        ntv_sign.setText("已签到" + sign_continue_num + "天");
        ntv_sign.setClickable(false);
        if (signAdapter == null) {
            signAdapter = new SignAdapter(baseAct, false, list);
            GridLayoutManager manager = new GridLayoutManager(this, 7);
            rv_sign.setLayoutManager(manager);
            rv_sign.setAdapter(signAdapter);
        } else {
            signAdapter.notifyDataSetChanged();
        }
    }

    public void initBubble(MyImageView miv_bubble,boolean isAnchorCenter) {
        if (miv_bubble!=null){
            float pivotX = 0.0f;
            float pivotY = 0.0f;
            if (isAnchorCenter){
                pivotX = miv_bubble.getMeasuredWidth() *1.0f / 2.0f;
                pivotY = miv_bubble.getMeasuredHeight() *1.0f / 2.0f;
            }
            miv_bubble.setPivotX(pivotX);
            miv_bubble.setPivotY(pivotY);
            ValueAnimator va = ValueAnimator.ofFloat(0.0f, 1.0f,//0.5秒
                    1.0f, 1.0f, 1.0f, 1.0f,//1秒
                    1.0f, 1.0f, 1.0f, 1.0f,//1秒
                    1.0f, 0.0f);//0.5秒
            va.setDuration(3000);
            va.setInterpolator(new LinearInterpolator());
            va.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                if (miv_bubble != null) {
                    miv_bubble.setAlpha(value);
                    miv_bubble.setScaleX(value);
                    miv_bubble.setScaleY(value);
                }
            });
            va.start();
        }
    }

    @Override
    public void setMid(List<TaskHomeEntity.GoldEgg> list) {
        if (isEmpty(list))
            return;
        int width = (Common.getScreenWidth(baseAct) - TransformUtil.dip2px(baseAct, 34)) / 3;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mllayout_mid.getLayoutParams();
        layoutParams.height = (width * 98) / 108;
        ntv_titleOne.setText(list.get(0).title);
        GlideUtils.getInstance().loadImageZheng(baseAct, miv_one, list.get(0).icon_url);
        ntv_titleOnes.setText(list.get(1).title);
        GlideUtils.getInstance().loadImageZheng(baseAct, miv_two, list.get(1).icon_url);
        ntv_titleOness.setText(list.get(2).title);
        ntv_contents.setText(list.get(2).content);
        ntv_task.setVisibility(View.GONE);
        mrlayout_goGet.setOnClickListener(view -> csv_out.fullScroll(ScrollView.FOCUS_DOWN));

        if (!isEmpty(list.get(1).over_task) && !isEmpty(list.get(1).all_task)
                && Integer.parseInt(list.get(1).over_task) > 0) {

            if (list.get(1).over_task.equals(list.get(1).all_task)) {
                miv_gets.setVisibility(View.VISIBLE);
                mrlayout_goGet.setOnClickListener(view -> initBubble(miv_mid,true));
                if ("0".equals(list.get(1).status)) {
                    initHintialog("goget", "恭喜您完成今日全部任务", "");
                    miv_gets.setImageResource(R.mipmap.icon_lingjiang);
                    miv_gets.setOnClickListener(view -> {
                        if (mPresenter != null)
                        mPresenter.everyDayGiveEgg();
                    });
                } else {
                    miv_gets.setImageResource(R.mipmap.icon_yilingjiang);
                    miv_gets.setOnClickListener(view -> initBubble(miv_mid,true));
                }
            } else {
                ntv_content.setText(list.get(1).content);
            }
            ntv_task.setVisibility(View.VISIBLE);
            ntv_task.setText(list.get(1).over_task + "/" + list.get(1).all_task);
        } else {
            ntv_content.setText(list.get(1).content);
        }
        GlideUtils.getInstance().loadImageZheng(baseAct, miv_chose, list.get(2).icon_url);
    }

    /**
     * 签到成功
     *
     * @param signEggEntity
     */
    @Override
    public void signEgg(SignEggEntity signEggEntity) {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            if (mPresenter != null) {
                mPresenter.initApis();
                initDialog(signEggEntity);
            }
        }, 2 * 1000);
        ntv_sign.setText("已签到" + signEggEntity.sign_continue_num + "天");
        ntv_sign.setClickable(false);
        if (ntv_eggnum != null) ntv_eggnum.setText(signEggEntity.gold_egg);
        if (signAdapter != null && signAdapter.miv_denglong != null)
            setGoldEggsAnim("signning.json", signAdapter.miv_denglong,
                    signAdapter.animation_view, false, "eggs/sign_two.png");
//        if (mtvSignDay != null) mtvSignDay.setText(signEggEntity.sign_continue_num);
//        if (sgel != null) sgel.signSuccess();
    }


    /**
     * 签到
     */
    @OnClick(R.id.ntv_sign)
    public void signPostSend() {
        mPresenter.signEgg("");
    }


    /**
     * 签到
     */
    @OnClick({R.id.view_eggdetails, R.id.view_eggdetail})
    public void getdetails() {
        if (dialog_detail == null) {
            mPresenter.getEggDetail();
        } else {
            dialog_detail.show();
        }
    }


    /**
     * 限时领金蛋弹窗
     *
     * @param got_eggs
     */

    @Override
    public void showGoldEggsNum(String got_eggs, String isRemind, String missEgg) {
        is_remind = isRemind;
        miss_eggs = missEgg;
        initHintialog("getok", "+" + got_eggs, "恭喜你！成功领取" + got_eggs + "个金蛋");
    }

    /**
     * 广告弹窗
     *
     * @param url
     * @param pop_ad_url
     */
    @Override
    public void popAd(String url, TaskHomeEntity.AdUrlBean pop_ad_url) {

    }

    @Override
    public void dayGiveEgg(DayGiveEggEntity dayGiveEggEntity) {
        mPresenter.initApis();
        initHintialog("getok", dayGiveEggEntity.title, dayGiveEggEntity.content);
    }

    @Override
    public void getEggDetail(int allPage, int page, List<NewEggDetailEntity.In> list) {
        initDetailDialog(allPage, page, list);
    }

    @Override
    protected void onDestroy() {
        if (ddp_downTime != null) {
            ddp_downTime.cancelDownTimer();
        }
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }
}
