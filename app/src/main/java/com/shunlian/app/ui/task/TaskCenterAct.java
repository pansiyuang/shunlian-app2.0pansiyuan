package com.shunlian.app.ui.task;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.DayGiveEggEntity;
import com.shunlian.app.bean.NewEggDetailEntity;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.eventbus_bean.GoldEggsTaskEvent;
import com.shunlian.app.eventbus_bean.ShareInfoEvent;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.DowntimeLayout;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.MyWebView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.ObtainGoldenEggsTip;
import com.shunlian.app.widget.SignGoldEggsLayout;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/8/30.
 */

public class TaskCenterAct extends BaseActivity implements ITaskCenterView {
    public List<TaskHomeEntity.SignDaysBean> sign_days;
    @BindView(R.id.mtv_eggs_count)
    MyTextView mtv_eggs_count;

    @BindView(R.id.miv_golden_eggs)
    MyImageView miv_golden_eggs;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.mtv_new_task)
    MyTextView mtvNewTask;

    @BindView(R.id.view_new_task)
    View viewNewTask;

    @BindView(R.id.llayout_new_task)
    LinearLayout llayoutNewTask;

    @BindView(R.id.mtv_day_task)
    MyTextView mtvDayTask;

    @BindView(R.id.tv_sign_unit)
    MyTextView tv_sign_unit;

    @BindView(R.id.view_day_task)
    View viewDayTask;

    @BindView(R.id.llayout_day_task)
    LinearLayout llayoutDayTask;

    @BindView(R.id.recy_view)
    RecyclerView recyView;

    @BindView(R.id.animation_view)
    LottieAnimationView animation_view;

    @BindView(R.id.mtv_sign_state)
    MyTextView mtvSignState;

    @BindView(R.id.mtv_sign_day)
    MyTextView mtvSignDay;

    @BindView(R.id.rlayout_sign)
    RelativeLayout rlayoutSign;

    @BindView(R.id.sgel)
    SignGoldEggsLayout sgel;

    @BindView(R.id.dtime_layout)
    DowntimeLayout dtime_layout;

    @BindView(R.id.oget)
    ObtainGoldenEggsTip oget;

    @BindView(R.id.miv_sign_rule)
    MyImageView miv_sign_rule;

    @BindView(R.id.miv_airbubble)
    MyImageView miv_airbubble;

    @BindView(R.id.mtv_user)
    MyTextView mtv_user;

    int pick_color;

    int v48;

    private TaskCenterPresenter mPresenter;
    private Dialog dialog_rule, dialog_qr;
    private String mAdUrl;
    private int mPicWidth;
    public static final String TASK_AD_KEY = "task_ad_key";

    /**
     * 常见问题url
     */
    private String question;
    public static void startAct(Context context) {
        Intent intent = new Intent(context, TaskCenterAct.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_task_center;
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (dtime_layout != null)
            dtime_layout.setOnClickListener(() -> {
                if (dtime_layout != null) {
                    if (dtime_layout.isClickable() && mPresenter != null) {
                        //领取金蛋
                        mPresenter.goldegglimit();
                    }
                }
            });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setSignStyle();
        immersionBar.statusBarView(R.id.view_state).init();
        EventBus.getDefault().register(this);
        mPresenter = new TaskCenterPresenter(this, this);
        setGoldEggsAnim("eggs_not_hatch.json");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        recyView.setNestedScrollingEnabled(false);

        pick_color = getColorResouce(R.color.pink_color);
        v48 = getColorResouce(R.color.value_484848);

        mPicWidth = DeviceInfoUtil.getDeviceWidth(this)
                - TransformUtil.dip2px(this,24);

        GradientDrawable gd = new GradientDrawable();
        int i = TransformUtil.dip2px(this, 1);
        gd.setStroke(i,getColorResouce(R.color.white_ash));
        gd.setCornerRadius(i*10);
        gd.setColor(getColorResouce(R.color.transparent));
        mtv_user.setBackgroundDrawable(gd);
    }

    /**
     * 签到
     */
    @OnClick(R.id.rlayout_sign)
    public void signPostSend() {
        GoldEggsTaskEvent event = new GoldEggsTaskEvent();
        if(sign_days!=null&&sign_days.size()>3) {
            event.isClickSign = true;
            event.sign_date = sign_days.get(2).date;
            EventBus.getDefault().post(event);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mPresenter != null) mPresenter.attachView();
    }

    /**
     * 金蛋明细
     */
    @OnClick(R.id.mtv_eggs_count)
    public void goldenEggsDetail() {
        EggDetailAct.startAct(this);
    }

    /**
     * 使用金蛋去首页
     */
    @OnClick(R.id.mtv_user)
    public void userEggs() {
        Common.goGoGo(this, "home");
    }

    @OnClick(R.id.miv_show_order)
    public void showOrder() {
        if (mPresenter != null) {
            mPresenter.share();
        }
    }

    /**
     * 常见问题
     */
    @OnClick(R.id.mtv_question)
    public void question() {
        if(!TextUtils.isEmpty(question)) {
            H5X5Act.startAct(this, question, H5X5Act.MODE_SONIC);
        }
//        if (dialog_qr != null)
//            dialog_qr.show();
    }

    /**
     * 签到规则
     */
    @OnClick(R.id.miv_sign_rule)
    public void rule() {
        if (dialog_rule != null)
            dialog_rule.show();
    }

    @OnClick({R.id.animation_view, R.id.miv_golden_eggs})
    public void goldEggs() {
        if (dtime_layout != null && !dtime_layout.isClickable()&&miv_airbubble != null) {
            miv_airbubble.setPivotX(0.0f);
            miv_airbubble.setPivotY(miv_airbubble.getMeasuredHeight());
            ValueAnimator va = ValueAnimator.ofFloat(0.0f, 1.0f,//0.5秒
                    1.0f, 1.0f, 1.0f, 1.0f,//1秒
                    1.0f, 1.0f, 1.0f, 1.0f,//1秒
                    1.0f, 0.0f);//0.5秒
            va.setDuration(3000);
            va.setInterpolator(new LinearInterpolator());
            va.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                if (miv_airbubble != null) {
                    miv_airbubble.setAlpha(value);
                    miv_airbubble.setScaleX(value);
                    miv_airbubble.setScaleY(value);
                }
            });
            va.start();
        }else{
            mPresenter.goldegglimit();
        }
    }

    /**
     * 新手任务列表
     */
    @OnClick(R.id.llayout_new_task)
    public void newTaskList() {
        if (mPresenter != null) {
            mPresenter.current_task_state = TaskCenterPresenter.NEW_USER_TASK;
            mPresenter.cacheTaskList();
            stateChange(1);
        }
    }

    /**
     * 日常任务列表
     */
    @OnClick(R.id.llayout_day_task)
    public void dayTaskList() {
        if (mPresenter != null) {
            mPresenter.current_task_state = TaskCenterPresenter.DAILY_TASK;
            mPresenter.cacheTaskList();
            stateChange(2);
        }
    }

    private void stateChange(int state) {

        mtvNewTask.setTextColor(state == 1 ? pick_color : v48);
        viewNewTask.setVisibility(state == 1 ? View.VISIBLE : View.INVISIBLE);

        mtvDayTask.setTextColor(state == 2 ? pick_color : v48);
        viewDayTask.setVisibility(state == 2 ? View.VISIBLE : View.INVISIBLE);

    }


    private void setSignStyle() {
        GradientDrawable topDrawable = new GradientDrawable();
        topDrawable.setColor(getColorResouce(R.color.pink_color));
        int i = TransformUtil.dip2px(this, 5);
        float[] topRad = {i, i, i, i, 0, 0, 0, 0};
        topDrawable.setCornerRadii(topRad);
        mtvSignState.setBackgroundDrawable(topDrawable);

        GradientDrawable bottomDrawable = new GradientDrawable();
        bottomDrawable.setColor(getColorResouce(R.color.white));
        float[] bottomRad = {0, 0, 0, 0, i, i, i, i};
        bottomDrawable.setCornerRadii(bottomRad);
        rlayoutSign.setBackgroundDrawable(bottomDrawable);

    }

    private void setGoldEggsAnim(String filename) {
        AssetManager assets = null;
        InputStream is = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                gone(miv_golden_eggs);
                visible(animation_view);
                animation_view.setAnimation(filename);//在assets目录下的动画json文件名。
                animation_view.loop(true);//设置动画循环播放
                animation_view.setImageAssetsFolder("eggs/");//assets目录下的子目录，存放动画所需的图片
                animation_view.playAnimation();//播放动画
            } else {
                visible(miv_golden_eggs);
                gone(animation_view);
                assets = getAssets();
                is = assets.open("eggs/img_1.png");
                miv_golden_eggs.setImageBitmap(BitmapFactory.decodeStream(is));
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

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 金蛋数量
     *
     * @param count
     */
    @Override
    public void setGoldEggsCount(String count) {
        if (mtv_eggs_count != null)
        mtv_eggs_count.setText(count);
    }

    /**
     * ；连续签到天数
     *
     * @param num
     */
    @Override
    public void setSignContinueNum(String num) {
        if (mtvSignDay != null)
        mtvSignDay.setText(num);
    }

    /**
     * @param second      倒计时秒数
     * @param maxProgress 最大进度
     */
    @Override
    public void obtainDownTime(String second, String maxProgress, String task_status) {
        if ("0".equals(task_status)) {
            setGoldEggsAnim("eggs_hatch.json");
            if (mPresenter != null) mPresenter.getTaskList();
            if (dtime_layout != null) {
                dtime_layout.setSecond(1, 1);
                dtime_layout.startDownTimer();
            }
        } else {
            setGoldEggsAnim("eggs_not_hatch.json");
            if (dtime_layout != null && !isEmpty(second) && !isEmpty(maxProgress)) {
                dtime_layout.setSecond(Long.parseLong(second), Long.parseLong(maxProgress));
                dtime_layout.startDownTimer();
                dtime_layout.setDownTimeComplete(() -> {
                    setGoldEggsAnim("eggs_hatch.json");
                    if (mPresenter != null) mPresenter.getTaskList();
                    if (mPresenter != null &&
                            mPresenter.current_task_state == TaskCenterPresenter.DAILY_TASK)
                        mPresenter.updateItem(0, "0");
                });
            }
        }
    }

    /**
     * 限时领金蛋弹窗
     *
     * @param num
     */
    public void showGoldEggsNum(String num) {
        if (oget != null) {
            //领取金蛋
            oget.setTopTextView("恭喜获得");
            oget.setEggsCount(num);
            oget.show(4000);
        }
    }

    @Override
    public void popAd(String url, TaskHomeEntity.AdUrlBean pop_ad_url) {
        if (isEmpty(url))return;
        String cache_url = SharedPrefUtil.getCacheSharedPrf(TASK_AD_KEY, "");
        if (!cache_url.equals(url)){
            adDialog(url,pop_ad_url);
            SharedPrefUtil.saveCacheSharedPrf(TASK_AD_KEY,url);
        }
    }

    @Override
    public void dayGiveEgg(DayGiveEggEntity dayGiveEggEntity) {

    }

    @Override
    public void getEggDetail(int allPage, int page, List<NewEggDetailEntity.In> list) {

    }

    private String re = "(w=|h=)(\\d+)";
    private Pattern p = Pattern.compile(re);
    /**
     * 广告图
     *
     * @param url
     * @param urlBean
     */
    @Override
    public void setPic(String url, TaskHomeEntity.AdUrlBean urlBean, List<TaskHomeEntity.AdUrlRollBean> adUrlRollBean) {
        if(adUrlRollBean!=null&&adUrlRollBean.size()>0){
              visible(kanner);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < adUrlRollBean.size(); i++) {
                strings.add(adUrlRollBean.get(i).ad_pic_url);
                if (i >= adUrlRollBean.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(baseAct, adUrlRollBean.get(position).ad_url.type, adUrlRollBean.get(position).ad_url.item_id);
                        }
                    });
                }
            }
        }else{
             gone(kanner);
        }
//            if (Pattern.matches(".*(w=\\d+&h=\\d+).*", url))
//        }{
//                Matcher m = p.matcher(url);
//                int w = 0;
//                int h = 0;
//                if (m.find()) {
//                    w = Integer.parseInt(m.group(2));
//                } else {
//                    w = 672;
//                }
//                if (m.find()) {
//                    h = Integer.parseInt(m.group(2));
//                } else {
//                    h = 200;
//                }
//
//                int i = (int) (mPicWidth * h * 1.0f / w);
//                int radius = TransformUtil.dip2px(this, 5f);
//                GlideUtils.getInstance().loadBitmapSync(this, url, new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        Bitmap source = BitmapUtil.scaleBitmap(resource, mPicWidth, i);
//                        if (source != null) {
//                            Bitmap bitmap = BitmapUtil.roundCropBitmap(source, radius);
//                            mivPic.setImageBitmap(bitmap);
//                        }
//                    }
//                });
//            }

//            mivPic.setOnClickListener(v -> {
//                if (urlBean != null) {
//                    Common.goGoGo(this, urlBean.type, urlBean.item_id);
//                }
//            });
//        } else {
//            gone(mivPic);
//        }
//        mAdUrl = url;
    }


    /**
     * @param question 常见问题
     * @param rule     签到规则
     */
    @Override
    public void setTip(String question, String rule) {
        this.question = question;
//        initQRDialog(question);
        initRuleDialog(rule);
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        if (recyView != null) {
            recyView.setAdapter(adapter);
        }
    }

    /**
     * 关闭新手任务列表
     */
    @Override
    public void closeNewUserList(boolean isClose) {
        if (isClose) {
            gone(llayoutNewTask);
            if (llayoutDayTask != null)
                llayoutDayTask.setEnabled(false);
            if (mtvDayTask != null)
                mtvDayTask.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else {
            visible(llayoutNewTask);
        }
    }

    /**
     * 签到
     */
    @Override
    public void setSignData(List<TaskHomeEntity.SignDaysBean> list,String sign_continue_num) {
        if (sgel != null)
            sign_days = list;
            sgel.setData(list);

            //判断连续签到次数和当前是否签到
        if(list!=null&&list.size()>3){
            if (mtvSignDay != null&&!list.get(2).sign_status.equals("1")){
                mtvSignDay.setText("签到");
                mtvSignState.setText("未签到");
            }else {
                mtvSignState.setText("已签到");
                mtvSignDay.setText(sign_continue_num);
            }
        }

    }

    @Override
    public void setMid(List<TaskHomeEntity.GoldEgg> list) {

    }

    /**
     * 签到成功
     *
     * @param signEggEntity
     */
    @Override
    public void signEgg(SignEggEntity signEggEntity) {
        initDialog(signEggEntity);
        mtvSignState.setText("已签到");
        if (mtv_eggs_count != null) mtv_eggs_count.setText(signEggEntity.gold_egg);
        if (mtvSignDay != null) mtvSignDay.setText(signEggEntity.sign_continue_num);
        if (sgel != null) sgel.signSuccess();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dispatchEvent(GoldEggsTaskEvent event) {
        if (event.isClickSign && mPresenter != null) {//点击了签到
            mPresenter.signEgg(event.sign_date);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareSuccess(ShareInfoEvent event) {
        if (oget != null && event.isShareSuccess && mPresenter != null
                && "income".equals(event.type)) {
            oget.setEggsCount(event.eggs_count);
            oget.show(4000);
            if (mPresenter.current_task_state == TaskCenterPresenter.DAILY_TASK)
                mPresenter.updateItem(mPresenter.getUpdatePosition(), "1");
        }
    }



    /*
    常见问题弹窗
     */
    public void initQRDialog(String url) {
        if (isEmpty(url))
            return;
        dialog_qr = new Dialog(this, R.style.popAd);
        dialog_qr.setContentView(R.layout.dialog_rule);
        MyImageView miv_close = dialog_qr.findViewById(R.id.miv_close);
        MyImageView miv_ad = dialog_qr.findViewById(R.id.miv_ad);
        MyWebView mwv_rule = dialog_qr.findViewById(R.id.mwv_rule);
        mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
        mwv_rule.loadUrl(url);
//        mwv_rule.loadData("ddddddfsdfsfsfsdfsfsdfd","text/html", "UTF-8");

        miv_ad.setImageResource(R.mipmap.image_renwu_changjianwenti);
        miv_close.setOnClickListener(view -> dialog_qr.dismiss());
        dialog_qr.setCancelable(false);
    }

    /*
    签到规则弹窗
     */
    public void initRuleDialog(String url) {
        if (isEmpty(url))
            return;
        dialog_rule = new Dialog(this, R.style.popAd);
        dialog_rule.setContentView(R.layout.dialog_rule);
        MyImageView miv_close = dialog_rule.findViewById(R.id.miv_close);
        MyImageView miv_ad = dialog_rule.findViewById(R.id.miv_ad);
        MyWebView mwv_rule = dialog_rule.findViewById(R.id.mwv_rule);
        mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
        mwv_rule.loadUrl(url);
//        mwv_rule.loadData("ddddddfsdfsfsfsdfsfsdfd","text/html", "UTF-8");

        miv_ad.setImageResource(R.mipmap.image_renwu_qiandaoguize);
        miv_close.setOnClickListener(view -> dialog_rule.dismiss());
        dialog_rule.setCancelable(false);
    }

    /**
     * 签到成功弹窗
     *
     * @param data
     */
    public void initDialog(SignEggEntity data) {
//        if (Dialog dialog_ad == null) {
        Dialog dialog_ad = new Dialog(this, R.style.popAd);
        dialog_ad.setContentView(R.layout.dialog_sign_egg);
        NewTextView ntv_hint = dialog_ad.findViewById(R.id.ntv_hint);
        NewTextView ntv_sure = dialog_ad.findViewById(R.id.ntv_sure);
        if (!isEmpty(data.ad_pic_url)) {
            MyImageView miv_ad = dialog_ad.findViewById(R.id.miv_ad);
            miv_ad.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(this, miv_ad, data.ad_pic_url);
            miv_ad.setOnClickListener(view -> {
                Common.goGoGo(TaskCenterAct.this, data.url.type, data.url.item_id);
                dialog_ad.dismiss();
            });
        }
        ntv_sure.setOnClickListener(view -> dialog_ad.dismiss());
        ntv_hint.setText(String.format(getStringResouce(R.string.mission_gongxininhuode), data.gold_num));
        dialog_ad.setCancelable(false);
//        }
        dialog_ad.show();
    }


    public void adDialog(String url, TaskHomeEntity.AdUrlBean urlBean) {
        Dialog dialog_ad = new Dialog(this, R.style.popAd);
        dialog_ad.setContentView(R.layout.ad_task);
        LinearLayout ll_root = dialog_ad.findViewById(R.id.ll_root);
        ViewGroup.LayoutParams layoutParams = ll_root.getLayoutParams();
        layoutParams.width = DeviceInfoUtil.getDeviceWidth(this);
        layoutParams.height = DeviceInfoUtil.getDeviceHeight(this);
        ll_root.setLayoutParams(layoutParams);
        MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
        ImageView miv_pic = dialog_ad.findViewById(R.id.miv_pic);
        if (URLUtil.isNetworkUrl(url)) {
            if (Pattern.matches(".*(w=\\d+&h=\\d+).*", url)) {
                Matcher m = p.matcher(url);
                int w = 0;
                int h = 0;
                if (m.find()) {
                    w = Integer.parseInt(m.group(2));
                } else {
                    w = 600;
                }
                if (m.find()) {
                    h = Integer.parseInt(m.group(2));
                } else {
                    h = 690;
                }

                int r_w = TransformUtil.countRealWidth(this, 600);
                int i = (int) (r_w * h * 1.0f / w);
                GlideUtils.getInstance().loadOverrideImage(this, miv_pic, url,r_w,i);

                /*int radius = TransformUtil.dip2px(this, 4f);
                GlideUtils.getInstance().loadBitmapSync(this, url, new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap source = BitmapUtil.scaleBitmap(resource, r_w, i);
                        if (source != null) {
                            Bitmap bitmap = BitmapUtil.roundCropBitmap(source, radius);
                            miv_pic.setImageBitmap(bitmap);
                        }
                    }
                });*/
            }
        }

        miv_pic.setOnClickListener(v -> {
            if (dialog_ad != null)dialog_ad.dismiss();
            if (urlBean != null) {
                Common.goGoGo(this, urlBean.type, urlBean.item_id);
            }
        });
        miv_close.setOnClickListener(view -> dialog_ad.dismiss());
        dialog_ad.setCancelable(false);
        dialog_ad.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (dtime_layout != null){
            dtime_layout.cancelDownTimer();
        }
    }

    @Override
    protected void onDestroy() {
        if (dtime_layout != null) {
            dtime_layout.detachView();
        }
        if (sgel != null) sgel.detachView();
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
        EventBus.getDefault().unregister(this);
    }
}
