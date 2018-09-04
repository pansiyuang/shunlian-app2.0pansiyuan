package com.shunlian.app.ui.task;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.eventbus_bean.GoldEggsTaskEvent;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.DowntimeLayout;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.MyWebView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.ObtainGoldenEggsTip;
import com.shunlian.app.widget.SignGoldEggsLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/8/30.
 */

public class TaskCenterAct extends BaseActivity implements ITaskCenterView {

    @BindView(R.id.mtv_eggs_count)
    MyTextView mtv_eggs_count;

    @BindView(R.id.miv_golden_eggs)
    MyImageView miv_golden_eggs;

    @BindView(R.id.miv_pic)
    MyImageView mivPic;

    @BindView(R.id.mtv_new_task)
    MyTextView mtvNewTask;

    @BindView(R.id.view_new_task)
    View viewNewTask;

    @BindView(R.id.llayout_new_task)
    LinearLayout llayoutNewTask;

    @BindView(R.id.mtv_day_task)
    MyTextView mtvDayTask;

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

    int pick_color;

    int v48;

    private TaskCenterPresenter mPresenter;
    private String ruleUrl, qrUrl;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, TaskCenterAct.class));
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

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setSignStyle();
        EventBus.getDefault().register(this);
        mPresenter = new TaskCenterPresenter(this, this);
        setGoldEggsAnim("eggs_not_hatch.json");

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyView.setLayoutManager(manager);
        recyView.setNestedScrollingEnabled(false);

        pick_color = getColorResouce(R.color.pink_color);
        v48 = getColorResouce(R.color.value_484848);
    }


    /**
     * 金蛋明细
     */
    @OnClick(R.id.rlayout_golden_eggs)
    public void goldenEggsDetail() {
        EggDetailAct.startAct(this);
    }

    /**
     * 常见问题
     */
    @OnClick(R.id.mtv_question)
    public void question() {
        initDialogs(qrUrl, true);
    }

    /**
     * 签到规则
     */
    @OnClick(R.id.miv_sign_rule)
    public void rule() {
        initDialogs(ruleUrl, false);
    }

    @OnClick(R.id.animation_view)
    public void goldEggs(){
        if (dtime_layout != null && !dtime_layout.isClickable()){
            miv_airbubble.setPivotX(0.0f);
            miv_airbubble.setPivotY(miv_airbubble.getMeasuredHeight());
            ValueAnimator va = ValueAnimator.ofFloat(0.0f,1.0f,//0.5秒
                    1.0f,1.0f,1.0f,1.0f,//1秒
                    1.0f,1.0f,1.0f,1.0f,//1秒
                    1.0f,0.0f);//0.5秒
            va.setDuration(3000);
            va.setInterpolator(new LinearInterpolator());
            va.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                miv_airbubble.setAlpha(value);
                miv_airbubble.setScaleX(value);
                miv_airbubble.setScaleY(value);
            });
            va.start();
        }
    }

    /**
     * 新手任务列表
     */
    @OnClick(R.id.llayout_new_task)
    public void newTaskList() {
        if (mPresenter != null) {
            mPresenter.current_task_state = TaskCenterPresenter.NEW_USER_TASK;
            mPresenter.getTaskList();
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
            mPresenter.getTaskList();
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
                AssetManager assets = getAssets();
                InputStream is = null;
                is = assets.open("eggs/img_1.png");
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                miv_golden_eggs.setImageBitmap(bitmap);
                bitmap.recycle();
                is.close();
                assets.close();
            }
        }catch (Exception e){

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
        mtv_eggs_count.setText(count);
    }

    /**
     * ；连续签到天数
     *
     * @param num
     */
    @Override
    public void setSignContinueNum(String num) {
        mtvSignDay.setText(num);
    }

    /**
     * @param second      倒计时秒数
     * @param maxProgress 最大进度
     */
    @Override
    public void obtainDownTime(String second, String maxProgress) {
        if (dtime_layout != null && !isEmpty(second) && !isEmpty(maxProgress)) {
            dtime_layout.setSecond(Long.parseLong(second), Long.parseLong(maxProgress));
            dtime_layout.startDownTimer();
            dtime_layout.setDownTimeComplete(() -> {
                setGoldEggsAnim("eggs_hatch.json");
                if(mPresenter != null &&
                        mPresenter.current_task_state == TaskCenterPresenter.DAILY_TASK)
                    mPresenter.updateItem(0,"0");
            });
            dtime_layout.setOnClickListener(view -> {
                if (dtime_layout != null) {
                    if (dtime_layout.isClickable() && mPresenter != null) {
                        //领取金蛋
                        mPresenter.goldegglimit();
                    }
                }
            });
        }
    }

    /**
     * 限时领金蛋弹窗
     * @param num
     */
    public void showGoldEggsNum(String num){
        if (oget != null) {
            //领取金蛋
            oget.setTopTextView("恭喜获得");
            oget.setEggsCount(num);
            oget.show(4000);
        }
    }

    /**
     * 广告图
     *
     * @param url
     * @param urlBean
     */
    @Override
    public void setPic(String url, TaskHomeEntity.AdUrlBean urlBean) {
        if (!isEmpty(url)) {
            visible(mivPic);
            GlideUtils.getInstance().loadCornerImage(this, mivPic, url,
                    TransformUtil.dip2px(this, 2.5f));
            mivPic.setOnClickListener(v -> {
                if (urlBean != null) {
                    Common.goGoGo(this, urlBean.type, urlBean.item_id);
                }
            });
        } else {
            gone(mivPic);
        }
    }

    /**
     * @param question 常见问题
     * @param rule     签到规则
     */
    @Override
    public void setTip(String question, String rule) {
        qrUrl=question;
        ruleUrl=rule;
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
    public void closeNewUserList() {
        gone(llayoutNewTask);
    }

    /**
     * 签到
     */
    @Override
    public void setSignData(List<TaskHomeEntity.SignDaysBean> list) {
        if (sgel != null)
            sgel.setData(list);
    }

    /**
     * 签到成功
     *
     * @param signEggEntity
     */
    @Override
    public void signEgg(SignEggEntity signEggEntity) {
        initDialog(signEggEntity);
        if (mtv_eggs_count != null)mtv_eggs_count.setText(signEggEntity.gold_egg);
        if (mtvSignDay != null)mtvSignDay.setText(signEggEntity.sign_continue_num);
        if (sgel != null)sgel.signSuccess();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void dispatchEvent(GoldEggsTaskEvent event) {
        if (event.isClickSign && mPresenter != null) {//点击了签到
            mPresenter.signEgg(event.sign_date);
        }
    }


    /*
    常见问题和签到明细弹窗
     */
    public void initDialogs(String url, boolean isQR) {
//        if (Dialog dialog_ad == null) {
        if (isEmpty(url))
            return;
        Dialog dialog_ad = new Dialog(this, R.style.popAd);
        dialog_ad.setContentView(R.layout.dialog_rule);
        MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
        MyImageView miv_ad = dialog_ad.findViewById(R.id.miv_ad);
        MyWebView mwv_rule = dialog_ad.findViewById(R.id.mwv_rule);
        mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
        mwv_rule.loadUrl(url);
//        mwv_rule.loadData("ddddddfsdfsfsfsdfsfsdfd","text/html", "UTF-8");
        if (isQR) {
            miv_ad.setImageResource(R.mipmap.image_renwu_changjianwenti);
        } else {
            miv_ad.setImageResource(R.mipmap.image_renwu_qiandaoguize);
        }
        miv_close.setOnClickListener(view -> dialog_ad.dismiss());
        dialog_ad.setCancelable(false);
//        }
        dialog_ad.show();
    }

    /**
     * 签到成功弹窗
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
