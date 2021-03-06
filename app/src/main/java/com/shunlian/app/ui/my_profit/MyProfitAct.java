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
import android.text.ClipboardManager;
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
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.balance.BalanceDetailAct;
import com.shunlian.app.ui.balance.BalanceMainAct;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.sale_data.SaleDetailAct;
import com.shunlian.app.ui.sale_data.SplineChart06View;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IMyProfitView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/16.
 */

public class MyProfitAct extends BaseActivity implements IMyProfitView {

    public final static String ASTERISK = "****";
    public final static String KEY = "profit_isShow";

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.mtv_growth_value)
    MyTextView mtv_growth_value;

    @BindView(R.id.mtv_request_code)
    MyTextView mtv_request_code;

    @BindView(R.id.civ_head)
    MyImageView civ_head;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.mtv_already_extract_m)
    MyTextView mtv_already_extract_m;

    @BindView(R.id.mtv_surplus_extract_m)
    MyTextView mtv_surplus_extract_m;

    @BindView(R.id.mtv_immediate_cash)
    MyTextView mtv_immediate_cash;

    @BindView(R.id.mtv_detail)
    MyTextView mtv_detail;

    @BindView(R.id.mtv_estimate_profit)
    MyTextView mtv_estimate_profit;

//    @BindView(R.id.mtv_meritocrat_profit)
//    MyTextView mtv_meritocrat_profit;
    @BindView(R.id.mtv_today_profit)
    MyTextView mtv_today_profit;

    @BindView(R.id.mtv_month_profit)
    MyTextView mtv_month_profit;

    @BindView(R.id.chart_view)
    SplineChart06View chart_view;

    @BindView(R.id.mtv_week_reward)
    MyTextView mtv_week_reward;

    @BindView(R.id.mtv_month_reward)
    MyTextView mtv_month_reward;

    @BindView(R.id.miv_balance)
    MyImageView miv_balance;

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

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_week)
    MyImageView miv_week;

    @BindView(R.id.llayout_week_reward)
    LinearLayout llayout_week_reward;

    @BindView(R.id.llayout_month_reward)
    LinearLayout llayout_month_reward;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;

    @BindView(R.id.llayout_appoint_sale)
    LinearLayout llayout_appoint_sale;

    @BindView(R.id.mtv_appoint_child_sale)
    MyTextView mtv_appoint_child_sale;

    @BindView(R.id.mtv_appoint_grand_child_sale)
    MyTextView mtv_appoint_grand_child_sale;

    @BindView(R.id.mtv_appoint_consume_child_sale)
    MyTextView mtv_appoint_consume_sale;

    @BindView(R.id.mtv_date)
    MyTextView mtv_date;

    @BindView(R.id.mtv_profitTip)
    MyTextView mtv_profitTip;

    @BindView(R.id.miv_isShow_data)
    MyImageView miv_isShow_data;

    @BindView(R.id.llayout_reward)
    LinearLayout llayout_reward;

    @BindView(R.id.miv_PhotoFrame)
    MyImageView miv_PhotoFrame;

    @BindView(R.id.miv_grade)
    MyImageView miv_grade;

    @BindView(R.id.llayout_order_profit)
    LinearLayout llayout_order_profit;

    @BindView(R.id.miv_order_profit)
    MyImageView miv_order_profit;

    @BindView(R.id.mtv_order_profit)
    MyTextView mtv_order_profit;

    @BindView(R.id.llayout_nickname)
    LinearLayout llayout_nickname;

    private MyProfitPresenter presenter;
    private String profit_help_url;
    private float monthReward;//月奖励收益
    private float weekReward;//周奖励收益
    private float orderProfit;//订单收益
    private boolean isWeekAnimRuning = false;
    private boolean isMonthAnimRuning = false;
    private boolean isOrderAnimRuning = false;
    //private float availableProfit;//可提现金额
    private boolean isShowData = true;
    private MyProfitEntity.ProfitInfo mProfitInfo;
    private boolean isBack;


    public static void startAct(Context context, boolean isBack) {
        Intent intent = new Intent(context, MyProfitAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("isBack", isBack);
        context.startActivity(intent);
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
        miv_close.setOnClickListener(this);
        miv_toolbar_help.setOnClickListener(this);
        mtv_immediate_cash.setOnClickListener(this);
        mtv_detail.setOnClickListener(this);
        llayout_week_reward.setOnClickListener(this);
        llayout_order_profit.setOnClickListener(this);
        llayout_month_reward.setOnClickListener(this);
        mtv_request_code.setOnClickListener(this);
        llayout_assertionProfit.setOnClickListener(this);

        llayout_7day.setOnClickListener(this);
        llayout_30day.setOnClickListener(this);
        llayout_60day.setOnClickListener(this);

        chart_view.setOnSaleChartListener((data, date) -> {
            if (isEmpty(data)) {
                gone(llayout_appoint_sale);
                return;
            }
            visible(llayout_appoint_sale);
            mtv_date.setText(date);
            for (int i = 0; i < data.size(); i++) {
                switch (i) {
                    case 0:
                        if (isEmpty(data.get(i))) {
                            gone(mtv_appoint_child_sale);
                        } else {
                            visible(mtv_appoint_child_sale);
                            mtv_appoint_child_sale.setText(data.get(i));
                        }
                        break;
                    case 1:
                        if (isEmpty(data.get(i))) {
                            gone(mtv_appoint_grand_child_sale);
                        } else {
                            visible(mtv_appoint_grand_child_sale);
                            mtv_appoint_grand_child_sale.setText(data.get(i));
                        }
                        break;
                    case 2:
                        if (isEmpty(data.get(i))) {
                            gone(mtv_appoint_consume_sale);
                        } else {
                            visible(mtv_appoint_consume_sale);
                            mtv_appoint_consume_sale.setText(data.get(i));
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isBack)
            MainActivity.startAct(this, "personCenter");
        super.onBackPressed();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        isBack = getIntent().getBooleanExtra("isBack", false);
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText(getStringResouce(R.string.my_profit));
        gone(mrlayout_toolbar_more);
        visible(miv_toolbar_help);

        int deviceWidth = DeviceInfoUtil.getDeviceWidth(this);
        if (deviceWidth >= Constant.DRAWING_WIDTH) {
            ViewGroup.LayoutParams layoutParams = chart_view.getLayoutParams();
            int[] ints = TransformUtil.countRealWH(this, 720, 320);
            layoutParams.width = ints[0];
            layoutParams.height = ints[1];
            chart_view.setLayoutParams(layoutParams);
        }

        GradientDrawable cash_background = (GradientDrawable) mtv_immediate_cash.getBackground();
        GradientDrawable detail_background = (GradientDrawable) mtv_detail.getBackground();
        int i = TransformUtil.dip2px(this, 0.5f);
        int radius = TransformUtil.dip2px(this, 9);
        cash_background.setStroke(i, getColorResouce(R.color.pink_color));
        cash_background.setCornerRadius(radius);
        detail_background.setStroke(i, getColorResouce(R.color.pink_color));
        detail_background.setCornerRadius(radius);

        presenter = new MyProfitPresenter(this, this);
        translateAnimation(miv_week);
        translateAnimation(miv_month);
        translateAnimation(miv_order_profit);

        //扩大眼睛点击范围
        int dp = TransformUtil.dip2px(this, 30);
        TransformUtil.expandViewTouchDelegate(miv_isShow_data,dp,dp,dp,dp);
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
        if (isEmpty(userInfo.grow_num)){
            mtv_growth_value.setVisibility(View.INVISIBLE);
        }else {
            visible(mtv_growth_value);
            mtv_growth_value.setText(String.format
                    (getStringResouce(R.string.growth_value), userInfo.grow_num));
        }
        mtv_request_code.setText(String.format
                (getStringResouce(R.string.invitation_code),userInfo.invite_code));
        int plus_role_code = Integer.parseInt(userInfo.plus_role_code);
        //plus_role_code = "3";
        if (plus_role_code == 1) {//店主 1=plus店主，2=销售主管，>=3 销售经理
            visible(miv_PhotoFrame, miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_dianzhu);
            miv_grade.setImageResource(R.mipmap.img_plus_dianzhude);
        } else if (plus_role_code >= 3) {//经理
            visible(miv_PhotoFrame, miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_jingli);
            miv_grade.setImageResource(R.mipmap.img_plus_xiaoshoujingli);
        } else if (plus_role_code == 2) {//主管
            visible(miv_PhotoFrame, miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_zhuguan);
            miv_grade.setImageResource(R.mipmap.img_plus_xiaoshouzhuguan);
        } else {//没有级别
            gone(miv_grade);
            miv_PhotoFrame.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams
                    lp = (RelativeLayout.LayoutParams) llayout_nickname.getLayoutParams();
            lp.topMargin = 0;
        }
        GlideUtils.getInstance().loadCircleHeadImage(this, civ_head, userInfo.avatar);
        mtv_nickname.setText(userInfo.nickname);
    }

    /**
     * 设置收益信息
     *
     * @param profitInfo
     */
    @Override
    public void setProfitInfo(MyProfitEntity.ProfitInfo profitInfo) {
        mProfitInfo = profitInfo;
        isShowData = SharedPrefUtil.getCacheSharedPrfBoolean(KEY, true);
        changeState();
        profit_help_url = profitInfo.profit_help_url;

        weekReward = Float.parseFloat(isEmpty(profitInfo.week_reward) ? "0" : profitInfo.week_reward);
        monthReward = Float.parseFloat(isEmpty(profitInfo.month_reward) ? "0" : profitInfo.month_reward);
        orderProfit = Float.parseFloat(isEmpty(profitInfo.order_profit) ? "0" : profitInfo.order_profit);
        /*availableProfit = Float.parseFloat(isEmpty(profitInfo.available_profit)
                ? "0" : profitInfo.available_profit);*/

        /*weekReward = 10;
        monthReward = 10;
        orderProfit = 10;*/

        if (weekReward > 0) {//周补贴
            miv_week.setImageResource(R.mipmap.zhoubutie);
            visible(llayout_week_reward);
        } else {
            gone(llayout_week_reward);
        }

        if (monthReward > 0) {//推广补贴
            miv_month.setImageResource(R.mipmap.tuiguangbutie);
            visible(llayout_month_reward);
        } else {
            gone(llayout_month_reward);
        }

        if (orderProfit > 0) {//订单收益
            miv_order_profit.setImageResource(R.mipmap.dingdan_n);
            visible(llayout_order_profit);
        } else {
            gone(llayout_order_profit);
        }

        if (llayout_week_reward.getVisibility() == View.GONE &&
                llayout_month_reward.getVisibility() == View.GONE &&
                llayout_order_profit.getVisibility() == View.GONE) {
            gone(llayout_reward);
        }else {
            visible(llayout_reward);
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
     * @param type 1周补贴 2推广补贴
     * @param available_profit
     */
    @Override
    public void receiveReward(String type, String available_profit) {
        if ("1".equals(type)) {
            isWeekAnimRuning = true;
            miv_week.setImageResource(R.mipmap.zhoubutie);
            energySphereClick(miv_week, Color.parseColor("#FFBCCA"),available_profit);
            mtv_week_reward.setText("0.00");
        } else if ("2".equals(type)) {
            isMonthAnimRuning = true;
            miv_month.setImageResource(R.mipmap.tuiguangbutie);
            energySphereClick(miv_month, Color.parseColor("#FEE8DD"),available_profit);
            mtv_month_reward.setText("0.00");
        } else {
            isOrderAnimRuning = true;
            miv_order_profit.setImageResource(R.mipmap.dingdan_n);
            energySphereClick(miv_order_profit, Color.parseColor("#02CC91"),available_profit);
            mtv_order_profit.setText("0.00");
        }
    }

    /**
     * 收益说明
     *
     * @param tip
     */
    @Override
    public void setProfitTip(String tip) {
        if (mtv_profitTip != null)
            mtv_profitTip.setText(tip);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.miv_close:
                if (isBack)
                    MainActivity.startAct(this, "personCenter");
                finish();
                break;
            case R.id.miv_balance:
                SaleDetailAct.startAct(this, SaleDetailAct.REWARD_DETAIL);
                break;
            case R.id.mtv_immediate_cash:
//                if (availableProfit >= 100) {
                    Constant.ISBALANCE = false;
                    BalanceMainAct.startAct(this, false);
//                    ProfitExtractAct.startAct(this, availableProfit+"");
//                } else {
//                    Common.staticToast("您的收益满100方可提现");
//                }
                break;
            case R.id.mtv_detail:
                Constant.ISBALANCE = false;
                BalanceDetailAct.startAct(this);
                break;
            case R.id.llayout_assertionProfit:
                //订单详情
                DetailOrderRecordAct.startAct(this);
                break;

            case R.id.llayout_7day:
                dayState(7);
                if (presenter != null) {
                    presenter.profitCharts(presenter._7day);
                }
                break;
            case R.id.llayout_30day:
                dayState(30);
                if (presenter != null) {
                    presenter.profitCharts(presenter._30day);
                }
                break;
            case R.id.llayout_60day:
                dayState(60);
                if (presenter != null) {
                    presenter.profitCharts(presenter._60day);
                }
                break;
            case R.id.miv_toolbar_help:
                if (!TextUtils.isEmpty(profit_help_url)) {
                    H5X5Act.startAct(this, profit_help_url, H5X5Act.MODE_SONIC);
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
            case R.id.llayout_order_profit:
                if (orderProfit > 0 && !isOrderAnimRuning) {
                    presenter.receiveReward("3");
                }
                break;
            case R.id.mtv_request_code:
                String content = mtv_request_code.getText().toString();
                String sub = content.substring(content.indexOf("：") + 1, content.length());
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(sub);
                Common.staticToast("复制成功");
                break;
        }
    }

    private void dayState(int day) {
        mtv_7day.setTextColor(day == 7 ? getColorResouce(R.color.pink_color)
                : getColorResouce(R.color.new_text));
        line_7day.setVisibility(day == 7 ? View.VISIBLE : View.INVISIBLE);

        mtv_30day.setTextColor(day == 30 ? getColorResouce(R.color.pink_color)
                : getColorResouce(R.color.new_text));
        line_30day.setVisibility(day == 30 ? View.VISIBLE : View.INVISIBLE);

        mtv_60day.setTextColor(day == 60 ? getColorResouce(R.color.pink_color)
                : getColorResouce(R.color.new_text));
        line_60day.setVisibility(day == 60 ? View.VISIBLE : View.INVISIBLE);
    }

    private void translateAnimation(View view) {
        int toSelf = Animation.RELATIVE_TO_SELF;
        TranslateAnimation ta = new TranslateAnimation(toSelf, 0, toSelf, 0,
                toSelf, 0, toSelf, 0.15f);
        ta.setDuration(1000);
        ta.setRepeatCount(Animation.INFINITE);
        ta.setRepeatMode(Animation.REVERSE);
        view.startAnimation(ta);
    }

    /*
    点击能力球动画
     */
    private void energySphereClick(View view, int color,String available_profit) {
        //添加view 并指定位置
        int[] week_pos = new int[2];
        view.getLocationOnScreen(week_pos);
        int width = view.getWidth();
        View v = new View(this);
        int i = TransformUtil.dip2px(this, 12);
        ViewGroup.LayoutParams vl = new ViewGroup.LayoutParams(i, i);
        v.setLayoutParams(vl);
        v.setX(week_pos[0] + width / 2);
        v.setY(week_pos[1]);
        OvalShape os = new OvalShape();
        os.resize(i, i);
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
        paint.getTextBounds(text, 0, text.length(), rect);
        int w = (rect.right - rect.left) / 2;
        int h = (rect.bottom - rect.top);

        //设置view的动画路径
        Path path = new Path();
        path.moveTo(v.getX(), v.getY());
        path.lineTo(extractPos[0] + w, extractPos[1] - (h + h));

        PathMeasure mPathMeasure = new PathMeasure(path, false);
        PropertyValuesHolder pvh = PropertyValuesHolder.ofFloat("line",
                0, mPathMeasure.getLength());
        PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("scale",
                1.0f, 3.0f);
        ValueAnimator va = ValueAnimator.ofPropertyValuesHolder(pvh, pvh1);
        va.setDuration(200);
        va.setInterpolator(new LinearInterpolator());
        float[] mCurrentPosition = new float[2];
        va.addUpdateListener((animation) -> {
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
                if (view == miv_week) {
                    isWeekAnimRuning = false;
                    mtv_surplus_extract_m.setText(available_profit);
                    gone(llayout_week_reward);
                } else if (view == miv_month) {
                    isMonthAnimRuning = false;
                    mtv_surplus_extract_m.setText(available_profit);
                    gone(llayout_month_reward);
                } else if (view == miv_order_profit) {
                    isOrderAnimRuning = false;
                    mtv_surplus_extract_m.setText(available_profit);
                    gone(llayout_order_profit);
                }
                if (llayout_week_reward.getVisibility() == View.GONE &&
                        llayout_month_reward.getVisibility() == View.GONE &&
                        llayout_order_profit.getVisibility() == View.GONE) {
                    gone(llayout_reward);
                }else {
                    visible(llayout_reward);
                }
            }
        });
    }

    private void alphaView(View view) {
        ValueAnimator va = ValueAnimator.ofFloat(0.8f, 0.0f);
        va.setDuration(50);
        va.addUpdateListener((animation) -> {
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

    @OnClick(R.id.miv_isShow_data)
    public void isShowData() {
        isShowData = !isShowData;
        changeState();
        SharedPrefUtil.saveCacheSharedPrfBoolean(KEY, isShowData);
    }

    private void changeState() {
        if (mProfitInfo == null)return;
        miv_isShow_data.setImageResource(!isShowData ? R.mipmap.img_plus_guanbi_n : R.mipmap.img_guanbi_h);
        mtv_surplus_extract_m.setText(!isShowData ? ASTERISK : mProfitInfo.available_profit);
        mtv_already_extract_m.setText(!isShowData ? ASTERISK : mProfitInfo.withdrawed_profit);
        mtv_week_reward.setText(!isShowData ? ASTERISK : mProfitInfo.week_reward);
        mtv_order_profit.setText(!isShowData ? ASTERISK : mProfitInfo.order_profit);
        mtv_month_reward.setText(!isShowData ? ASTERISK : mProfitInfo.month_reward);
        mtv_estimate_profit.setText(!isShowData ? ASTERISK : mProfitInfo.estimate_profit);
        mtv_today_profit.setText(!isShowData ? ASTERISK : mProfitInfo.today_profit);
        mtv_month_profit.setText(!isShowData ? ASTERISK : mProfitInfo.month_profit);
    }

    @Override
    protected void onDestroy() {
        miv_week.clearAnimation();
        miv_month.clearAnimation();
        miv_order_profit.clearAnimation();
        if (chart_view != null)chart_view.destroyDrawingCache();
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
