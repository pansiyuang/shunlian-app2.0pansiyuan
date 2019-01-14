package com.shunlian.app.ui.task;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.NewEggDetailAdapter;
import com.shunlian.app.adapter.SignAdapter;
import com.shunlian.app.bean.DayGiveEggEntity;
import com.shunlian.app.bean.NewEggDetailEntity;
import com.shunlian.app.bean.SignEggEntity;
import com.shunlian.app.bean.TaskHomeEntity;
import com.shunlian.app.presenter.TaskCenterPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.TaskDownTimerView;
import com.shunlian.app.view.ITaskCenterView;
import com.shunlian.app.widget.CompileScrollView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyWebView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    @BindView(R.id.miv_airbubble)
    MyImageView miv_airbubble;

    LinearLayoutManager linearLayoutManager;
    NewEggDetailAdapter eggDetailAdapter;
    private TaskCenterPresenter mPresenter;
    private SignAdapter signAdapter;
    private Dialog dialog_rule, dialog_detail;
    private String miss_eggs, is_remind;
    private boolean isReceive;//是否可以领取金蛋

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewTaskCenterAct.class);
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
                if ((int)tab.getTag() == TaskCenterPresenter.NEW_USER_TASK){
                    if (mPresenter != null) {
                        mPresenter.current_task_state = TaskCenterPresenter.NEW_USER_TASK;
                        mPresenter.cacheTaskList();
                    }
                }else if ((int)tab.getTag() == TaskCenterPresenter.DAILY_TASK){
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
            public void onTabReselected(TabLayout.Tab tab) {}
        });
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
        layout.setGravity(Gravity.CENTER);


        ImageView imageView = new ImageView(this);
        layout.addView(imageView);
        if (TaskCenterPresenter.NEW_USER_TASK == position) {
            imageView.setImageResource(R.mipmap.img_task_xinshourenwu);
            imageView.setId(R.id.iv_task_new);
        } else if (TaskCenterPresenter.DAILY_TASK == position){
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

        ntv_detail.setOnClickListener(view -> {
            if (data.url != null)
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

    /*
   签到规则弹窗
    */
    public void initRuleDialog(String url) {
        if (isEmpty(url))
            return;
        dialog_rule = new Dialog(this, R.style.popAd);
        dialog_rule.setContentView(R.layout.dialog_rule_new);
        MyImageView miv_close = dialog_rule.findViewById(R.id.miv_close);
        MyWebView mwv_rule = dialog_rule.findViewById(R.id.mwv_rule);
        mwv_rule.getSettings().setJavaScriptEnabled(true);   //加上这句话才能使用javascript方法
        mwv_rule.setMaxHeight(TransformUtil.dip2px(this, 380));
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
                    }else {
                        dialog_hint.dismiss();
                    }
                });
                break;
        }
        dialog_hint.setCancelable(false);
        dialog_hint.show();
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
        if (mPresenter != null && isReceive){
            mPresenter.goldegglimit();
        }else {
            if (miv_airbubble != null) {
                miv_airbubble.setPivotX(0.0f);
                miv_airbubble.setPivotY(0.0f);
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
            }
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
            }else {//新手任务和日常任务
                for (int i = 0; i < 2; i++) {
                    TabLayout.Tab tab = tab_layout.newTab();
                    int tag = 0;
                    if (i == 0){
                        tag = TaskCenterPresenter.NEW_USER_TASK;
                    }else {
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
        ntv_task.setVisibility(View.GONE);
        mrlayout_goGet.setClickable(true);

        if (!isEmpty(list.get(1).over_task) && !isEmpty(list.get(1).all_task)
                && Integer.parseInt(list.get(1).over_task) > 0) {

            if (list.get(1).over_task.equals(list.get(1).all_task)) {
                miv_gets.setVisibility(View.VISIBLE);
                mrlayout_goGet.setClickable(false);
                if ("0".equals(list.get(1).status)) {
                    miv_gets.setImageResource(R.mipmap.icon_lingjiang);
                    miv_gets.setOnClickListener(view -> {

                    });
                } else {
                    miv_gets.setImageResource(R.mipmap.icon_yilingjiang);
                    miv_gets.setOnClickListener(view -> {
                        miv_mid.setPivotX(0.0f);
                        miv_mid.setPivotY(miv_mid.getMeasuredHeight());
                        ValueAnimator va = ValueAnimator.ofFloat(0.0f, 1.0f,//0.5秒
                                1.0f, 1.0f, 1.0f, 1.0f,//1秒
                                1.0f, 1.0f, 1.0f, 1.0f,//1秒
                                1.0f, 0.0f);//0.5秒
                        va.setDuration(3000);
                        va.setInterpolator(new LinearInterpolator());
                        va.addUpdateListener(animation -> {
                            float value = (float) animation.getAnimatedValue();
                            if (miv_mid != null) {
                                miv_mid.setAlpha(value);
                                miv_mid.setScaleX(value);
                                miv_mid.setScaleY(value);
                            }
                        });
                        va.start();
                    });
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPresenter != null){
                    mPresenter.initApis();
                    initDialog(signEggEntity);
                }
            }
        }, 2 * 1000);
        ntv_sign.setText("已签到" + signEggEntity.sign_continue_num + "天");
        ntv_sign.setClickable(false);
        if (ntv_eggnum != null) ntv_eggnum.setText(signEggEntity.gold_egg);
        if (signAdapter != null && signAdapter.miv_denglong != null)
            setGoldEggsAnim("signning.json", signAdapter.miv_denglong,
                    signAdapter.animation_view, false, "eggs/sign_two.png");

        miss_eggs = signEggEntity.miss_eggs;
        is_remind = signEggEntity.is_remind;
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
    @OnClick(R.id.mrlayout_goGet)
    public void gets() {
        csv_out.fullScroll(ScrollView.FOCUS_DOWN);
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
    public void showGoldEggsNum(String got_eggs) {
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
        initHintialog("getok",dayGiveEggEntity.title,dayGiveEggEntity.content);
    }

    @Override
    public void getEggDetail(int allPage, int page, List<NewEggDetailEntity.In> list) {
        initDetailDialog(allPage, page, list);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ddp_downTime != null) {
            ddp_downTime.cancelDownTimer();
        }
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
