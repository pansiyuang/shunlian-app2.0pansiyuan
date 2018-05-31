package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.bean.PlusMemberEntity;
import com.shunlian.app.presenter.ShareBigGifPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IShareBifGifView;
import com.shunlian.app.widget.MyImageView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/23.
 */

public class MyPlusAct extends BaseActivity implements IShareBifGifView {

    public final int Mode_Month = 1001;
    public final int Mode_Year = 1002;

    @BindView(R.id.seekbar_plus)
    SeekBar seekbar_plus;

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

    @BindView(R.id.tv_sales_type)
    TextView tv_sales_type;

    @BindView(R.id.tv_sales_date)
    TextView tv_sales_date;

    @BindView(R.id.tv_earn_money)
    TextView tv_earn_money;

    @BindView(R.id.tv_member_count)
    TextView tv_member_count;

    @BindView(R.id.miv_invite)
    MyImageView miv_invite;

    @BindView(R.id.flayout_content)
    FrameLayout flayout_content;

    private int screenWidth;
    private int tabOneWidth, tabTwoWidth;
    private int tabOneMode = Mode_Year;
    private int space;
    private int currentYear, currentMonth;
    private ShareBigGifPresenter mPresenter;
    private FragmentManager fragmentManager;
    private Map<String, BaseFragment> fragmentMap = new HashMap<>();
    private final String[] flags = {"record", "gif", "invitations"};
    private InvitationRecordFrag invitationRecordFrag;
    private StoreGifFrag storeGifFrag;
    private InvitationsFrag invitationsFrag;
    private String invitationsUrl;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyPlusAct.class));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_plus;
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
        initTabsWidth();
        initFragments();
        mPresenter = new ShareBigGifPresenter(this, this);
        mPresenter.getPlusData(tabOneMode);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_tab1_right.setOnClickListener(this);
        tv_invitation_record.setOnClickListener(this);
        tv_store_gif.setOnClickListener(this);
        tv_invitations.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        miv_invite.setOnClickListener(this);
    }

    private void initTabsWidth() {

        tabOneWidth = (screenWidth - TransformUtil.dip2px(this, 20)) / 2;
        tabTwoWidth = (screenWidth - TransformUtil.dip2px(this, 20)) / 3;

        tv_tab1_left.setWidth(tabOneWidth);
        tv_tab2_left.setWidth(tabTwoWidth + space);
        tv_tab2_middle.setWidth(tabTwoWidth + (2 * space));
        tv_tab2_right.setWidth(tabTwoWidth + space);
    }

    public void initFragments() {
        fragmentManager = getSupportFragmentManager();
        recordClick();
    }

    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                fragmentManager.beginTransaction().remove(show).commitAllowingStateLoss();
                fragmentManager.beginTransaction().add(R.id.flayout_content, show).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().show(show).commitAllowingStateLoss();
            }
            if (fragmentMap != null && fragmentMap.size() > 0) {
                Set<String> keySet = fragmentMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BaseFragment baseFragment = fragmentMap.get(key);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            fragmentManager.beginTransaction().hide(baseFragment).commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }

    public void recordClick() {
        if (invitationRecordFrag == null) {
            invitationRecordFrag = (InvitationRecordFrag) fragmentMap.get(flags[0]);
            if (invitationRecordFrag == null) {
                invitationRecordFrag = new InvitationRecordFrag();
                fragmentMap.put(flags[0], invitationRecordFrag);
            }
        }
        switchContent(invitationRecordFrag);
    }

    public void gifClick() {
        if (storeGifFrag == null) {
            storeGifFrag = (StoreGifFrag) fragmentMap.get(flags[1]);
            if (storeGifFrag == null) {
                storeGifFrag = new StoreGifFrag();
                fragmentMap.put(flags[1], storeGifFrag);
            }
        }
        switchContent(storeGifFrag);
    }

    public void invitationsClick() {
        if (invitationsFrag == null) {
            invitationsFrag = (InvitationsFrag) fragmentMap.get(flags[2]);
            if (invitationsFrag == null) {
                invitationsFrag = InvitationsFrag.getInstance(invitationsUrl);
                fragmentMap.put(flags[2], invitationsFrag);
            }
        }
        switchContent(invitationsFrag);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_tab1_right:
                mPresenter.getPlusData(tabOneMode);
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
                PlusOrderAct.startAct(this);
                break;
            case R.id.miv_invite:
                GifBagListAct.startAct(this);
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
                recordClick();
                tv_tab2_left.setVisibility(View.VISIBLE);
                break;
            case 2:
                gifClick();
                tv_tab2_middle.setVisibility(View.VISIBLE);
                break;
            case 3:
                invitationsClick();
                tv_tab2_right.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void getPlusData(PlusDataEntity plusDataEntity) {   //小店级别：0=普通会员，1=plus店主，2=销售主管，3=销售经理
        PlusDataEntity.BaseInfo baseInfo = plusDataEntity.base_info;
        GlideUtils.getInstance().loadCircleImage(this, miv_icon, baseInfo.avatar);
        tv_sales_type.setText(baseInfo.role_desc);
        tv_sales_date.setText("有效期:" + baseInfo.expire_time);
        seekbar_plus.setProgress(12);
        tv_earn_money.setText("赚" + baseInfo.invite_reward + "奖励");
        seekbar_plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        invitationsUrl = baseInfo.invite_strategy;
        showTabOneButton(tabOneMode);

        if (baseInfo.role >= 3) {
            tv_member_count.setVisibility(View.VISIBLE);
        } else {
            tv_member_count.setVisibility(View.GONE);
        }
    }

    @Override
    public void getPlusMember(List<PlusMemberEntity.PlusMember> plusMembers) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
