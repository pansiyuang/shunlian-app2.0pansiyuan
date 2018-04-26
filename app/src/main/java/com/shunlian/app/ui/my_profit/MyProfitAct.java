package com.shunlian.app.ui.my_profit;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.MyProfitEntity;
import com.shunlian.app.bean.SalesChartEntity;
import com.shunlian.app.presenter.MyProfitPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.h5.H5Act;
import com.shunlian.app.ui.sale_data.SaleDetailAct;
import com.shunlian.app.ui.sale_data.SplineChart06View;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMyProfitView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MyProfitAct extends BaseActivity implements IMyProfitView {

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.mtv_growth_value)
    MyTextView mtv_growth_value;

    @BindView(R.id.mtv_request_code)
    MyTextView mtv_request_code;

    @BindView(R.id.civ_head)
    CircleImageView civ_head;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.mtv_already_extract_m)
    MyTextView mtv_already_extract_m;

    @BindView(R.id.mtv_surplus_extract_m)
    MyTextView mtv_surplus_extract_m;

    @BindView(R.id.mtv_immediate_cash)
    MyTextView mtv_immediate_cash;

    @BindView(R.id.mtv_estimate_profit)
    MyTextView mtv_estimate_profit;

    @BindView(R.id.mtv_today_profit)
    MyTextView mtv_today_profit;

    @BindView(R.id.mtv_month_profit)
    MyTextView mtv_month_profit;

    @BindView(R.id.mtv_meritocrat_profit)
    MyTextView mtv_meritocrat_profit;

    @BindView(R.id.chart_view)
    SplineChart06View chart_view;

    @BindView(R.id.mtv_week_reward)
    MyTextView mtv_week_reward;

    @BindView(R.id.mtv_month_reward)
    MyTextView mtv_month_reward;

    @BindView(R.id.miv_balance)
    MyImageView miv_balance;

    @BindView(R.id.miv_plus_role_code)
    MyImageView miv_plus_role_code;

    @BindView(R.id.llayout_assertionProfit)
    LinearLayout llayout_assertionProfit;

    @BindView(R.id.llayout_7day)
    LinearLayout llayout_7day;

    @BindView(R.id.llayout_30day)
    LinearLayout llayout_30day;

    @BindView(R.id.llayout_60day)
    LinearLayout llayout_60day;

    @BindView(R.id.mtv_30day)
    MyTextView mtv_30day;

    @BindView(R.id.line_30day)
    View line_30day;

    @BindView(R.id.mtv_7day)
    MyTextView mtv_7day;

    @BindView(R.id.line_7day)
    View line_7day;

    @BindView(R.id.mtv_60day)
    MyTextView mtv_60day;

    @BindView(R.id.line_60day)
    View line_60day;

    @BindView(R.id.miv_toolbar_help)
    MyImageView miv_toolbar_help;

    @BindView(R.id.miv_month)
    MyImageView miv_month;

    @BindView(R.id.miv_week)
    MyImageView miv_week;

    @BindView(R.id.llayout_week_reward)
    LinearLayout llayout_week_reward;

    @BindView(R.id.llayout_month_reward)
    LinearLayout llayout_month_reward;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;

    private String available_profit;//可提现金额
    private MyProfitPresenter presenter;
    private String profit_help_url;
    private float monthReward;//月奖励收益
    private float weekReward;//周奖励收益
    private boolean isWeekAnimRuning = false;
    private boolean isMonthAnimRuning = false;
    private float availableProfit;//可提现金额


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyProfitAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_myprofit;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_balance.setOnClickListener(this);
        miv_toolbar_help.setOnClickListener(this);
        mtv_immediate_cash.setOnClickListener(this);
        llayout_week_reward.setOnClickListener(this);
        llayout_month_reward.setOnClickListener(this);
        llayout_assertionProfit.setOnClickListener(this);

        llayout_7day.setOnClickListener(this);
        llayout_30day.setOnClickListener(this);
        llayout_60day.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("我的收益");
        gone(mrlayout_toolbar_more);
        visible(miv_toolbar_help);

        GradientDrawable cash_background = (GradientDrawable) mtv_immediate_cash.getBackground();
        int i = TransformUtil.dip2px(this, 0.5f);
        int radius = TransformUtil.dip2px(this, 9);
        cash_background.setStroke(i,getColorResouce(R.color.pink_color));
        cash_background.setCornerRadius(radius);

        presenter = new MyProfitPresenter(this, this);
        translateAnimation(miv_week);
        translateAnimation(miv_month);
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
     * 设置用户信息
     *
     * @param userInfo
     */
    @Override
    public void setUserInfo(MyProfitEntity.UserInfo userInfo) {
        mtv_growth_value.setText("成长值：" + userInfo.grow_num);
        mtv_request_code.setText("邀请码：" + userInfo.invite_code);
        GlideUtils.getInstance().loadImage(this, civ_head, userInfo.avatar);
        mtv_nickname.setText(userInfo.nickname);
        int role_code = Common.plusRoleCode(userInfo.plus_role_code);
        if (role_code != 0){
            miv_plus_role_code.setImageResource(role_code);
        }
    }

    /**
     * 设置收益信息
     *
     * @param profitInfo
     */
    @Override
    public void setProfitInfo(MyProfitEntity.ProfitInfo profitInfo) {
        available_profit = profitInfo.available_profit;
        mtv_surplus_extract_m.setText(profitInfo.available_profit);
        mtv_already_extract_m.setText(profitInfo.withdrawed_profit);
        mtv_week_reward.setText(profitInfo.week_reward);
        mtv_month_reward.setText(profitInfo.month_reward);

        mtv_estimate_profit.setText(profitInfo.estimate_profit);
        mtv_today_profit.setText(profitInfo.today_profit);
        mtv_month_profit.setText(profitInfo.month_profit);
        mtv_meritocrat_profit.setText(profitInfo.meritocrat_profit);
        profit_help_url = profitInfo.profit_help_url;

        weekReward = Float.parseFloat(profitInfo.week_reward);
        monthReward = Float.parseFloat(profitInfo.month_reward);
        availableProfit = Float.parseFloat(profitInfo.available_profit);
        if (weekReward > 0){
            miv_week.setImageResource(R.mipmap.zhoujiangli_n);
        }else {
            miv_week.setImageResource(R.mipmap.zhoujiangli_h);
        }

        if (monthReward > 0){
            miv_month.setImageResource(R.mipmap.yuejiangli_n);
        }else {
            miv_month.setImageResource(R.mipmap.yuejiangli_h);
        }
    }

    /**
     * 收益折线图
     *
     * @param charts
     */
    @Override
    public void setProfitCharts(SalesChartEntity charts) {
        chart_view.init(charts);
    }

    /**
     * 领取月/周奖励
     *
     * @param type 1周奖励 2月奖励
     */
    @Override
    public void receiveReward(String type) {
        if ("1".equals(type)){
            isWeekAnimRuning = true;
            miv_week.setImageResource(R.mipmap.zhoujiangli_h);
            energySphereClick(miv_week, Color.parseColor("#FFBCCA"));
            mtv_week_reward.setText("0.00");
        }else {
            isMonthAnimRuning = true;
            miv_month.setImageResource(R.mipmap.yuejiangli_h);
            energySphereClick(miv_month, Color.parseColor("#FEE8DD"));
            mtv_month_reward.setText("0.00");
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.miv_balance:
                SaleDetailAct.startAct(this,SaleDetailAct.REWARD_DETAIL);
                break;
            case R.id.mtv_immediate_cash:
                if (availableProfit >= 100) {
                    ProfitExtractAct.startAct(this, availableProfit+"");
                }else {
                    Common.staticToast("您的收益满100方可提现");
                }
                break;
            case R.id.llayout_assertionProfit:
                //订单详情
                DetailOrderRecordAct.startAct(this);
                break;

            case R.id.llayout_7day:
                dayState(7);
                if (presenter != null){
                    presenter.profitCharts(presenter._7day);
                }
                break;
            case R.id.llayout_30day:
                dayState(30);
                if (presenter != null){
                    presenter.profitCharts(presenter._30day);
                }
                break;
            case R.id.llayout_60day:
                dayState(60);
                if (presenter != null){
                    presenter.profitCharts(presenter._60day);
                }
                break;
            case R.id.miv_toolbar_help:
                if (!TextUtils.isEmpty(profit_help_url)) {
                    H5Act.startAct(this, profit_help_url, H5Act.MODE_SONIC);
                }
                break;
            case R.id.llayout_week_reward:
                if (weekReward > 0 && !isWeekAnimRuning) {
                    presenter.receiveReward("1");
                }
                break;
            case R.id.llayout_month_reward:
                if (monthReward > 0 && !isMonthAnimRuning) {
                    presenter.receiveReward("2");
                }
                break;
        }
    }

    private void dayState(int day){
        mtv_7day.setTextColor(day == 7 ? getColorResouce(R.color.pink_color)
                :getColorResouce(R.color.new_text));
        line_7day.setVisibility(day == 7?View.VISIBLE:View.INVISIBLE);

        mtv_30day.setTextColor(day == 30 ? getColorResouce(R.color.pink_color)
                :getColorResouce(R.color.new_text));
        line_30day.setVisibility(day == 30?View.VISIBLE:View.INVISIBLE);

        mtv_60day.setTextColor(day == 60 ? getColorResouce(R.color.pink_color)
                :getColorResouce(R.color.new_text));
        line_60day.setVisibility(day == 60 ? View.VISIBLE:View.INVISIBLE);
    }

    private void translateAnimation(View view){
        int toSelf = Animation.RELATIVE_TO_SELF;
        TranslateAnimation ta = new TranslateAnimation(toSelf,0,toSelf,0,
                toSelf,0,toSelf,0.15f);
        ta.setDuration(1000);
        ta.setRepeatCount(Animation.INFINITE);
        ta.setRepeatMode(Animation.REVERSE);
        view.startAnimation(ta);
    }

    /*
    点击能力球动画
     */
    private void energySphereClick(View view,int color){
        //添加view 并指定位置
        int[] week_pos = new int[2];
        view.getLocationOnScreen(week_pos);
        int width = view.getWidth();
        View v = new View(this);
        int i = TransformUtil.dip2px(this, 12);
        ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(i,i);
        v.setLayoutParams(vl);
        v.setX(week_pos[0]+width / 2);
        v.setY(week_pos[1]);
        OvalShape os = new OvalShape();
        os.resize(i,i);
        ShapeDrawable drawable = new ShapeDrawable(os);
        drawable.getPaint().setColor(color);
        drawable.getPaint().setAntiAlias(false);
        v.setBackgroundDrawable(drawable);
        v.setAlpha(0.8f);
        rlayout_root.addView(v);

        //获取view的目标位置
        int[] extractPos = new int[2];
        mtv_surplus_extract_m.getLocationOnScreen(extractPos);
        TextPaint paint = mtv_surplus_extract_m.getPaint();
        String text = mtv_surplus_extract_m.getText().toString();
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);
        int w = (rect.right - rect.left)/2;
        int h = (rect.bottom - rect.top);

        //设置view的动画路径
        Path path = new Path();
        path.moveTo(v.getX(),v.getY());
        path.lineTo(extractPos[0]+w,extractPos[1]-(h+h));

        PathMeasure mPathMeasure = new PathMeasure(path, false);
        PropertyValuesHolder pvh = PropertyValuesHolder.ofFloat("line",
                0, mPathMeasure.getLength());
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("scale",
                1.0f,3.0f);
        ValueAnimator va = ValueAnimator.ofPropertyValuesHolder(pvh,pvh1);
        va.setDuration(200);
        va.setInterpolator(new LinearInterpolator());
        float[] mCurrentPosition = new float[2];
        va.addUpdateListener((animation)->{
            float value = (Float) animation.getAnimatedValue("line");
            mPathMeasure.getPosTan(value, mCurrentPosition, null);
            v.setX(mCurrentPosition[0]);
            v.setY(mCurrentPosition[1]);

            float scale = (float) animation.getAnimatedValue("scale");
            v.setScaleX(scale);
        });
        va.start();
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                alphaView(v);
                if (view == miv_week){
                    isWeekAnimRuning = false;
                    availableProfit +=weekReward;
                    mtv_surplus_extract_m.setText(""+availableProfit);
                }else if (view == miv_month){
                    isMonthAnimRuning = false;
                    availableProfit +=monthReward;
                    mtv_surplus_extract_m.setText(""+availableProfit);
                }
            }
        });
    }

    private void alphaView(View view){
        ValueAnimator va = ValueAnimator.ofFloat(0.8f,0.0f);
        va.setDuration(50);
        va.addUpdateListener((animation)->{
            float value = (float) animation.getAnimatedValue();
            view.setAlpha(value);
        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                gone(view);
            }
        });
        va.start();
    }

    @Override
    protected void onDestroy() {
        miv_week.clearAnimation();
        miv_month.clearAnimation();
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
        }
    }
}
