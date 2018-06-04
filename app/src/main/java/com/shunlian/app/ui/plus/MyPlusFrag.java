package com.shunlian.app.ui.plus;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.PlusDataEntity;
import com.shunlian.app.bean.PlusMemberEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.ShareBigGifPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IShareBifGifView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MySplineChartView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/5/29.
 */

public class MyPlusFrag extends BaseFragment implements IShareBifGifView, View.OnClickListener {

    public final int Mode_Month = 1;
    public final int Mode_Year = 2;

    @BindView(R.id.seekbar_plus)
    SeekBar seekbar_plus;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

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

    @BindView(R.id.lLayout_toast)
    LinearLayout lLayout_toast;

    @BindView(R.id.miv_toast_icon)
    MyImageView miv_toast_icon;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.chart_view)
    MySplineChartView chart_view;

    @BindView(R.id.miv_show_chart)
    MyImageView miv_show_chart;

    @BindView(R.id.rl_tab_one)
    RelativeLayout rl_tab_one;

    @BindView(R.id.ll_close_tab1)
    LinearLayout ll_close_tab1;

    QuickActions quick_actions;
    private Unbinder bind;
    private int screenWidth;
    private int tabOneWidth, tabTwoWidth;
    private int tabOneMode = Mode_Month;
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

    private Timer outTimer;
    private Handler handler;
    private Runnable runnableA, runnableB, runnableC;
    private boolean isStop, isCrash;
    private int size, position;
    private boolean isPause = true;
    private boolean isExpand = true; //是否展开
    private ShareInfoParam mShareInfoParam = new ShareInfoParam();
    public boolean isclick=false;

    public static void startAct(Context context) {
        context.startActivity(new Intent(context, MyPlusAct.class));
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.act_my_plus, container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    public void getPlusData() {
        if (mPresenter != null) {
            mPresenter.getPlusData(tabOneMode);
            showTabTwoButton(1);
            invitationRecordFrag.getInviteHistory();
        }
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        tv_title.setText(getStringResouce(R.string.share_store_big_gif));
        tv_title_right.setText(getStringResouce(R.string.my_order));
        tv_title_right.setVisibility(View.VISIBLE);
        miv_close.setVisibility(View.GONE);

        screenWidth = DeviceInfoUtil.getDeviceWidth(getActivity());
        space = TransformUtil.dip2px(getActivity(), 5);

        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;

        showTabOneButton(Mode_Year);
        initTabsWidth();
        initFragments();
        mPresenter = new ShareBigGifPresenter(getActivity(), this);
        mPresenter.getPlusData(1);

        //分享
        quick_actions = new QuickActions(baseActivity);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);
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
        ll_close_tab1.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        beginToast();
        if (!isHidden()&&!isclick){
            isclick=false;
            getPlusData();
        }
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    private void initTabsWidth() {

        tabOneWidth = (screenWidth - TransformUtil.dip2px(getActivity(), 20)) / 2;
        tabTwoWidth = (screenWidth - TransformUtil.dip2px(getActivity(), 20)) / 3;

        tv_tab1_left.setWidth(tabOneWidth);
        tv_tab2_left.setWidth(tabTwoWidth + space);
        tv_tab2_middle.setWidth(tabTwoWidth + (2 * space));
        tv_tab2_right.setWidth(tabTwoWidth + space);
    }

    public void initFragments() {
        fragmentManager = getChildFragmentManager();
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
                if (tabOneMode == Mode_Month) {
                    tabOneMode = Mode_Year;
                } else {
                    tabOneMode = Mode_Month;
                }
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
                PlusOrderAct.startAct(getActivity());
                break;
            case R.id.miv_invite:
                visible(quick_actions);
                quick_actions.shareInfo(mShareInfoParam);
                quick_actions.shareStyle2Dialog(true, 4);
                break;
            case R.id.ll_close_tab1:
                if (isExpand) {
                    isExpand = false;
                    chart_view.setVisibility(View.GONE);
                    miv_show_chart.setImageResource(R.mipmap.img_plus_jixiao_jiantou_shang);
                } else {
                    isExpand = true;
                    chart_view.setVisibility(View.VISIBLE);
                    miv_show_chart.setImageResource(R.mipmap.img_plus_jixiao_jiantou_xia);
                }
                break;
        }
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
        PlusDataEntity.Achievement achievement = plusDataEntity.achievement;
        GlideUtils.getInstance().loadCircleImage(getActivity(), miv_icon, baseInfo.avatar);
        tv_sales_type.setText(baseInfo.role_desc);
        tv_sales_date.setText("有效期:" + baseInfo.expire_time);
        seekbar_plus.setProgress(baseInfo.upgrade_process);
        tv_earn_money.setText("赚" + baseInfo.invite_reward + "奖励");
        seekbar_plus.setOnTouchListener((view, motionEvent) -> true);
        invitationsUrl = baseInfo.invite_strategy;

        if (baseInfo.role >= 3) {  //经理及以上身份才显示数据和表格
            rl_tab_one.setVisibility(View.VISIBLE);
        } else {
            rl_tab_one.setVisibility(View.GONE);
        }

        if (baseInfo.role == 3) {//经理才显示plus会员数量
            tv_member_count.setVisibility(View.VISIBLE);
        }

        if (baseInfo.plus_num <= 0) {
            tv_member_count.setVisibility(View.GONE);
        } else if (baseInfo.plus_num > 12) {
            tv_member_count.setText("12+");
        } else {
            tv_member_count.setText(String.valueOf(baseInfo.plus_num));
        }

        tv_group_money.setText(achievement.total_sales);
        tv_group_count.setText(achievement.plus_num);

        showTabOneButton(tabOneMode);

        //分享
        mShareInfoParam.userAvatar = baseInfo.avatar;
        mShareInfoParam.userName = baseInfo.nickname;
        mShareInfoParam.title = baseInfo.share_info.title;
        mShareInfoParam.desc = baseInfo.share_info.content;
        mShareInfoParam.shareLink = baseInfo.share_info.invite_middle_page;
        mShareInfoParam.img = baseInfo.share_info.pic;

        chart_view.initView(Integer.valueOf(plusDataEntity.max_sale), Integer.valueOf(plusDataEntity.max_num), plusDataEntity.chart);
    }

    @Override
    public void getPlusMember(List<PlusMemberEntity.PlusMember> plusMembers) {
        size = 2;
        if (!isEmpty(plusMembers)) {
            startToast(plusMembers);
            startTimer();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void startTimer() {
        if (handler == null) {
            handler = new Handler();
        }
        runnableA = () -> {
            if (!isStop) {
                mPresenter.getPlusMember();
            }
        };
        handler.postDelayed(runnableA, (7 * size + 1) * 1000);
    }

    public void startToast(final List<PlusMemberEntity.PlusMember> datas) {
        if (outTimer != null) {
            outTimer.cancel();
        }
        outTimer = new Timer();
        outTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (position < datas.size()) {
                    runnableB = () -> {
                        if (position < datas.size()) {
                            lLayout_toast.setVisibility(View.VISIBLE);
                            GlideUtils.getInstance().loadCircleImage(getActivity(), miv_toast_icon, datas.get(position).avatar);
                            tv_info.setText(datas.get(position).sentence);
                            lLayout_toast.setOnClickListener(v -> {
                                //
                            });
                        }
                    };
                    if (handler == null) {
                        if (!isCrash) {
                            isCrash = true;
                            Handler mHandler = new Handler(Looper.getMainLooper());
                            mHandler.postDelayed(() -> isCrash = false, (7 * size + 2) * 1000);
                        }
                    } else {
                        handler.post(runnableB);
                        runnableC = () -> {
                            if (!isStop) {
                                lLayout_toast.setVisibility(View.GONE);
                                position++;
                            }
                        };
                        handler.postDelayed(runnableC, 5 * 1000);
                    }
                }
            }
        }, 0, 7 * 1000);
    }

    public void beginToast() {
        if (isPause) {
            position = 0;
            isStop = false;
            mPresenter.getPlusMember();
            isPause = false;
        }
    }

    public void stopToast() {
        if (!isCrash) {
            isPause = true;
            isStop = true;
            if (lLayout_toast != null) {
                LogUtil.augusLogW("position:gone");
                lLayout_toast.setVisibility(View.GONE);
            }
            if (outTimer != null) {
                LogUtil.augusLogW("position:cancel");
                outTimer.cancel();
            }
            if (handler != null) {
                LogUtil.augusLogW("position:remove");
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
        stopToast();
    }

    @Override
    public void onDestroyView() {
        if (bind != null) {
            bind.unbind();
        }
        super.onDestroyView();
    }
}
