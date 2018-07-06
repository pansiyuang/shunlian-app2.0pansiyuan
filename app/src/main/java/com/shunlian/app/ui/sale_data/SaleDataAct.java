package com.shunlian.app.ui.sale_data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberAdapter;
import com.shunlian.app.bean.SaleDataEntity;
import com.shunlian.app.bean.SalesChartEntity;
import com.shunlian.app.presenter.SaleDataPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISaleDataView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/12.
 */

public class SaleDataAct extends BaseActivity implements ISaleDataView {

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

    @BindView(R.id.mtv_sale_sum)
    MyTextView mtv_sale_sum;

    @BindView(R.id.mtv_today_vip)
    MyTextView mtv_today_vip;

    @BindView(R.id.mtv_today_order)
    MyTextView mtv_today_order;

    @BindView(R.id.mtv_total_consume)
    MyTextView mtv_total_consume;

    @BindView(R.id.mtv_grand_child_store)
    MyTextView mtv_grand_child_store;

    @BindView(R.id.mtv_child_store)
    MyTextView mtv_child_store;

    @BindView(R.id.chart_view)
    SplineChart06View chart_view;

    @BindView(R.id.view_child_store)
    View view_child_store;

    @BindView(R.id.view_grand_child_store)
    View view_grand_child_store;

    @BindView(R.id.view_total_consume)
    View view_total_consume;

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

    @BindView(R.id.llayout_30day)
    LinearLayout llayout_30day;

    @BindView(R.id.llayout_7day)
    LinearLayout llayout_7day;

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

    @BindView(R.id.llayout_vip)
    LinearLayout llayout_vip;

    @BindView(R.id.llayout_sale)
    LinearLayout llayout_sale;

    @BindView(R.id.llayout_order)
    LinearLayout llayout_order;

    @BindView(R.id.mtv_sale)
    MyTextView mtv_sale;

    @BindView(R.id.miv_sale)
    MyImageView miv_sale;

    @BindView(R.id.mtv_order)
    MyTextView mtv_order;

    @BindView(R.id.miv_order)
    MyImageView miv_order;

    @BindView(R.id.mtv_vip)
    MyTextView mtv_vip;

    @BindView(R.id.miv_vip)
    MyImageView miv_vip;

    @BindView(R.id.llayout_mark1)
    LinearLayout llayout_mark1;

    @BindView(R.id.mtv_mark1)
    MyTextView mtv_mark1;

    @BindView(R.id.llayout_mark2)
    LinearLayout llayout_mark2;

    @BindView(R.id.mtv_mark2)
    MyTextView mtv_mark2;

    @BindView(R.id.llayout_mark3)
    LinearLayout llayout_mark3;

    @BindView(R.id.mtv_this_month)
    MyTextView mtv_this_month;

    @BindView(R.id.mtv_sale_Explain)
    MyTextView mtv_sale_Explain;

    @BindView(R.id.rlayout_vip)
    RelativeLayout rlayout_vip;

    @BindView(R.id.miv_isShow_data)
    MyImageView miv_isShow_data;

    @BindView(R.id.miv_PhotoFrame)
    MyImageView miv_PhotoFrame;

    @BindView(R.id.miv_grade)
    MyImageView miv_grade;

    @BindView(R.id.llayout_nickname)
    LinearLayout llayout_nickname;

    @BindView(R.id.miv_jingying)
    MyImageView miv_jingying;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.view_line2)
    View member_role_line;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    private SaleDataPresenter presenter;
    private int currentPos;//当前所在位置，销售 订单 会员
    private boolean isShowData = true;
    public final static String ASTERISK = "****";
    public final static String KEY = "sale_isShow";
    private String mThisMonth;
    private String mTodayVip;
    private String mTodayOrder;
    private String mXiaodian;
    private String mFendian;
    private String mXiaofei;


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, SaleDataAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_saledata;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText(getStringResouce(R.string.sale_data));
        gone(mrlayout_toolbar_more);
        int deviceWidth = DeviceInfoUtil.getDeviceWidth(this);
        if (deviceWidth >= Constant.DRAWING_WIDTH) {
            ViewGroup.LayoutParams layoutParams = chart_view.getLayoutParams();
            int[] ints = TransformUtil.countRealWH(this, 720, 320);
            layoutParams.width = ints[0];
            layoutParams.height = ints[1];
            chart_view.setLayoutParams(layoutParams);
        }
        presenter = new SaleDataPresenter(this, this);

        //扩大眼睛点击范围
        int dp = TransformUtil.dip2px(this, 30);
        TransformUtil.expandViewTouchDelegate(miv_isShow_data,dp,dp,dp,dp);
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_7day.setOnClickListener(this);
        llayout_30day.setOnClickListener(this);
        llayout_60day.setOnClickListener(this);

        llayout_sale.setOnClickListener(this);
        llayout_order.setOnClickListener(this);
        llayout_vip.setOnClickListener(this);

        mtv_sale_sum.setOnClickListener(this);
        mtv_this_month.setOnClickListener(this);

        chart_view.setOnSaleChartListener((data, date) -> {
            if (isEmpty(data)){
                gone(llayout_appoint_sale);
                return;
            }
            visible(llayout_appoint_sale);
            mtv_date.setText(date);
            for (int i = 0; i < data.size(); i++) {
                switch (i){
                    case 0:
                        if (isEmpty(data.get(i))){
                            gone(mtv_appoint_child_sale);
                        }else {
                            visible(mtv_appoint_child_sale);
                            mtv_appoint_child_sale.setText(data.get(i));
                        }
                        break;
                    case 1:
                        if (isEmpty(data.get(i))){
                            gone(mtv_appoint_grand_child_sale);
                        }else {
                            visible(mtv_appoint_grand_child_sale);
                            mtv_appoint_grand_child_sale.setText(data.get(i));
                        }
                        break;
                    case 2:
                        if (isEmpty(data.get(i))){
                            gone(mtv_appoint_consume_sale);
                        }else {
                            visible(mtv_appoint_consume_sale);
                            mtv_appoint_consume_sale.setText(data.get(i));
                        }
                        break;
                }
            }
        });
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
     * 设置成长值和请求码
     *
     * @param growth_value
     * @param request_code
     */
    @Override
    public void setGrowthValue_RequestCode(String growth_value, String request_code) {
        mtv_growth_value.setText(String.format(getStringResouce(R.string.growth_value),growth_value));
        mtv_request_code.setText(String.format(getStringResouce(R.string.invitation_code),request_code));
    }

    /**
     * 设置头像和昵称
     *
     * @param head
     * @param nickname
     */
    @Override
    public void setHeadNickname(String head, String nickname) {
        GlideUtils.getInstance().loadCircleHeadImage(this, civ_head, head);
        mtv_nickname.setText(nickname);
    }

    /**
     * 设置等级
     *
     * @param plus_role_code
     */
    @Override
    public void setplusrole(String plus_role_code) {
        //plus_role_code = "2";
        int plusCode = Integer.parseInt(plus_role_code);
        if (plusCode == 1){//店主 1=plus店主，2=销售主管，>=3 销售经理
            visible(miv_PhotoFrame,miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_dianzhu);
            miv_grade.setImageResource(R.mipmap.img_plus_dianzhude);
        }else if (plusCode >= 3){//经理
            visible(miv_PhotoFrame,miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_jingli);
            miv_grade.setImageResource(R.mipmap.img_plus_xiaoshoujingli);
        }else if (plusCode == 2){//主管
            visible(miv_PhotoFrame,miv_grade);
            miv_PhotoFrame.setImageResource(R.mipmap.img_plus_shouyi_zhuguan);
            miv_grade.setImageResource(R.mipmap.img_plus_xiaoshouzhuguan);
        }else {
            gone(miv_grade);
            miv_PhotoFrame.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams
                    lp = (RelativeLayout.LayoutParams) llayout_nickname.getLayoutParams();
            lp.topMargin = 0;
        }
    }

    /**
     * 设置本月销售额，今日会员，今日销售订单
     *
     * @param this_month
     * @param today_vip
     * @param today_order
     */
    @Override
    public void setMonthVip_Order(String this_month, String today_vip, String today_order) {
        mThisMonth = this_month;
        mTodayVip = today_vip;
        mTodayOrder = today_order;

        isShowData = SharedPrefUtil.getSharedPrfBoolean(KEY,true);
        changeState();
    }

    /**
     * @param xiaodian 小店销售额
     * @param fendian 分店销售额
     * @param xiaofei 消费金额
     */
    @Override
    public void setSaleData(String xiaodian, String fendian, String xiaofei) {
        mXiaodian = xiaodian;
        mFendian = fendian;
        mXiaofei = xiaofei;

        isShowData = SharedPrefUtil.getSharedPrfBoolean(KEY,true);
        changeState();

        GradientDrawable child_store_background = (GradientDrawable) view_child_store.getBackground();
        child_store_background.setColor(getColorResouce(R.color.pink_color));
        GradientDrawable grand_child_store_background = (GradientDrawable)
                view_grand_child_store.getBackground();
        grand_child_store_background.setColor(Color.parseColor("#43D060"));
        GradientDrawable total_consume_background = (GradientDrawable) view_total_consume.getBackground();
        total_consume_background.setColor(Color.parseColor("#F9A31C"));
    }

    /**
     * 设置折线图
     *
     * @param saleChart
     */
    @Override
    public void setSaleChart(SalesChartEntity saleChart) {
        chart_view.init(saleChart);
    }

    /**
     * 设置精英导师数据
     *
     * @param master_info_new
     */
    @Override
    public void setEliteTutorData(List<SaleDataEntity.NewMemberInfo> master_info_new, String member_role_code) {
        //member_role_code = "2";
        if ("1".equals(member_role_code)){
            mtv_name.setText(getStringResouce(R.string.elite_tutor));
            miv_jingying.setImageResource(R.mipmap.img_jingyingdaoshi);
            visible(mtv_name,miv_jingying,member_role_line);
        }else if ("2".equals(member_role_code)){
            mtv_name.setText(getStringResouce(R.string.gen_guest_elite));
            miv_jingying.setImageResource(R.mipmap.img_chuangkejingying);
            visible(mtv_name,miv_jingying,member_role_line);
        }else {
            gone(mtv_name,miv_jingying,member_role_line);
        }

        if (!isEmpty(master_info_new)) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            recy_view.setLayoutManager(manager);
            recy_view.setNestedScrollingEnabled(false);
            recy_view.setAdapter(new MemberAdapter(this, master_info_new));
        }
    }

    /**
     * 销售数据说明
     *
     * @param tip
     */
    @Override
    public void setSaleTip(String tip) {
        if (mtv_sale_Explain != null)
            mtv_sale_Explain.setText(tip);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.llayout_7day:
                dayState(7);
                if (presenter != null){
                    presenter.handleRequest(currentPos,presenter._7day);
                }
                break;
            case R.id.llayout_30day:
                dayState(30);
                if (presenter != null){
                    presenter.handleRequest(currentPos,presenter._30day);
                }
                break;
            case R.id.llayout_60day:
                dayState(60);
                if (presenter != null){
                    presenter.handleRequest(currentPos,presenter._60day);
                }
                break;

            case R.id.llayout_sale:
                saleState(1);
                dayState(7);
                currentPos = 0;
                if (presenter != null){
                    presenter.salesChart(presenter.salesChart,presenter._7day);
                }
                break;
            case R.id.llayout_order:
                saleState(2);
                dayState(7);
                currentPos = 1;
                if (presenter != null){
                    presenter.salesChart(presenter.ordersChart,presenter._7day);
                }
                break;
            case R.id.llayout_vip:
                saleState(3);
                dayState(7);
                currentPos = 2;
                if (presenter != null){
                    presenter.salesChart(presenter.membersChart,presenter._7day);
                }
                break;

            case R.id.mtv_sale_sum:
            case R.id.mtv_this_month:
                SaleDetailAct.startAct(this,SaleDetailAct.SALE_DETAIL);
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

    private void saleState(int state){
        mtv_sale.setTextColor(state == 1 ? getColorResouce(R.color.white)
                :getColorResouce(R.color.share_text));
        mtv_sale.setBackgroundColor(state == 1?getColorResouce(R.color.pink_color):
                getColorResouce(R.color.value_F1F0F0));
        miv_sale.setVisibility(state == 1?View.VISIBLE:View.GONE);

        mtv_order.setTextColor(state == 2 ? getColorResouce(R.color.white)
                :getColorResouce(R.color.share_text));
        mtv_order.setBackgroundColor(state == 2?getColorResouce(R.color.pink_color):
                getColorResouce(R.color.value_F1F0F0));
        miv_order.setVisibility(state == 2?View.VISIBLE:View.GONE);

        mtv_vip.setTextColor(state == 3 ? getColorResouce(R.color.white)
                :getColorResouce(R.color.share_text));
        mtv_vip.setBackgroundColor(state == 3?getColorResouce(R.color.pink_color):
                getColorResouce(R.color.value_F1F0F0));
        miv_vip.setVisibility(state == 3 ? View.VISIBLE:View.GONE);

        gone(mtv_appoint_consume_sale);
        switch (state){
            case 1:
                mtv_mark1.setText(chart_view.key_line1 = "小店销售额");
                mtv_mark2.setText(chart_view.key_line2 = "分店销售额");
                gone(rlayout_vip);
                visible(llayout_mark3,mtv_sale_Explain,mtv_appoint_consume_sale);
                break;
            case 2:
                mtv_mark1.setText(chart_view.key_line1 = "小店销售订单");
                mtv_mark2.setText(chart_view.key_line2 = "分店销售订单");
                gone(rlayout_vip,llayout_mark3,mtv_sale_Explain,mtv_appoint_consume_sale);
                break;
            case 3:
                mtv_mark1.setText(chart_view.key_line1 = "小店会员");
                mtv_mark2.setText(chart_view.key_line2 = "PLUS会员");
                visible(rlayout_vip);
                gone(llayout_mark3,mtv_sale_Explain,mtv_appoint_consume_sale);
                break;
        }
    }

    @OnClick(R.id.miv_isShow_data)
    public void isShowData(){
        isShowData = !isShowData;
        changeState();
        SharedPrefUtil.saveSharedPrfBoolean(KEY,isShowData);
    }

    private void changeState() {
        miv_isShow_data.setImageResource(!isShowData?R.mipmap.img_plus_guanbi_n:R.mipmap.img_guanbi_h);
        mtv_sale_sum.setText(!isShowData?ASTERISK:mThisMonth);
        mtv_today_vip.setText(!isShowData?ASTERISK:mTodayVip);
        mtv_today_order.setText(!isShowData?ASTERISK:mTodayOrder);

        mtv_child_store.setText(!isShowData?ASTERISK:mXiaodian);
        mtv_grand_child_store.setText(!isShowData?ASTERISK:mFendian);
        mtv_total_consume.setText(!isShowData?ASTERISK:mXiaofei);
    }

    @Override
    protected void onDestroy() {
        if (chart_view != null)chart_view.destroyDrawingCache();
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
    }
}
