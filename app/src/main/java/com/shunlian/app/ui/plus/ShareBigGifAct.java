package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.Calendar;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/23.
 */

public class ShareBigGifAct extends BaseActivity {

    public final int Mode_Month = 1001;
    public final int Mode_Year = 1002;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_tab1_left)
    TextView tv_tab1_left;

    @BindView(R.id.tv_tab1_right)
    TextView tv_tab1_right;

    @BindView(R.id.tv_tab2_left)
    TextView tv_tab2_left;

    @BindView(R.id.tv_tab2_middle)
    TextView tv_tab2_middle;

    @BindView(R.id.tv_tab2_right)
    TextView tv_tab2_right;

    @BindView(R.id.tv_invitation_record)
    TextView tv_invitation_record;

    @BindView(R.id.tv_store_gif)
    TextView tv_store_gif;

    @BindView(R.id.tv_invitations)
    TextView tv_invitations;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_group_money_title)
    TextView tv_group_money_title;

    @BindView(R.id.tv_group_money)
    TextView tv_group_money;

    @BindView(R.id.tv_group_count_title)
    TextView tv_group_count_title;

    @BindView(R.id.tv_group_count)
    TextView tv_group_count;

    private int screenWidth;
    private int tabOneWidth, tabTwoWidth;
    private int tabOneMode = Mode_Year;
    private int space;
    private int currentYear, currentMonth;


    public static void startAct(Context context) {
        context.startActivity(new Intent(context, ShareBigGifAct.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_share_big_gif;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.share_store_big_gif));
        tv_title_right.setText(getStringResouce(R.string.my_order));
        tv_title_right.setVisibility(View.VISIBLE);

        screenWidth = DeviceInfoUtil.getDeviceWidth(this);
        space = TransformUtil.dip2px(this, 5);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;

        showTabOneButton(Mode_Year);

        GlideUtils.getInstance().loadCircleImage(this, miv_icon, "");
        initTabsWidth();
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_tab1_right.setOnClickListener(this);
        tv_invitation_record.setOnClickListener(this);
        tv_store_gif.setOnClickListener(this);
        tv_invitations.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    private void initTabsWidth() {

        tabOneWidth = (screenWidth - TransformUtil.dip2px(this, 20)) / 2;
        tabTwoWidth = (screenWidth - TransformUtil.dip2px(this, 20)) / 3;

        tv_tab1_left.setWidth(tabOneWidth);
        tv_tab2_left.setWidth(tabTwoWidth + space);
        tv_tab2_middle.setWidth(tabTwoWidth + (2 * space));
        tv_tab2_right.setWidth(tabTwoWidth + space);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab1_right:
                if (tabOneMode == Mode_Year) {
                    showTabOneButton(Mode_Month);
                } else if (tabOneMode == Mode_Month) {
                    showTabOneButton(Mode_Year);
                }
                break;
            case R.id.tv_invitation_record:
                showTabTwoButton(1);
                break;
            case R.id.tv_store_gif:
                showTabTwoButton(2);
                break;
            case R.id.tv_invitations:
                showTabTwoButton(3);
                break;
            case R.id.tv_title_right:
                SuperProductsAct.startAct(this);
                break;
        }
        super.onClick(view);
    }

    public void showTabOneButton(int mode) {
        switch (mode) {
            case Mode_Year:
                tv_tab1_left.setText(String.format(getStringResouce(R.string.plus_vip_year_performance), currentYear));
                tv_tab1_right.setText(getStringResouce(R.string.plus_vip_month_performance));
                tv_group_money_title.setText(String.format(getStringResouce(R.string.plus_group_money), currentYear));
                tv_group_count_title.setText(getStringResouce(R.string.plus_group_count));
                tabOneMode = Mode_Year;
                break;
            case Mode_Month:
                tv_tab1_left.setText(String.format(getStringResouce(R.string.plus_vip_detail_performance), currentYear, currentMonth));
                tv_tab1_right.setText(getStringResouce(R.string.plus_vip_period_performance));
                tv_group_money_title.setText(getStringResouce(R.string.plus_group_month_money));
                tv_group_count_title.setText(getStringResouce(R.string.plus_group_month_count));
                tabOneMode = Mode_Month;
                break;
        }
    }

    public void showTabTwoButton(int position) {
        tv_tab2_left.setVisibility(View.GONE);
        tv_tab2_middle.setVisibility(View.GONE);
        tv_tab2_right.setVisibility(View.GONE);
        switch (position) {
            case 1:
                tv_tab2_left.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_tab2_middle.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_tab2_right.setVisibility(View.VISIBLE);
                break;
        }
    }
}
