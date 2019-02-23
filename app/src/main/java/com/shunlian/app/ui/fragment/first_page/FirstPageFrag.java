package com.shunlian.app.ui.fragment.first_page;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PFirstPage;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.ui.zxing_code.ZXingDemoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.HoneRedDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IFirstPage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.slide_tab.PagerSlidingTabStrip;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 首页页面
 */

public class FirstPageFrag extends BaseFragment implements View.OnClickListener, IFirstPage, MessageCountManager.OnGetMessageListener {
    public static String firstId = "";
    public static boolean isExpand = false;
    //    @BindView(R.id.mAppbar)
    public static MyImageView miv_entry;
    public static MyImageView miv_entrys;
    public static AppBarLayout mAppbar;
    public static boolean isHide = false;
    public static boolean isHides = false;
    public static boolean isNewUserHide = false;
    public static MyTextView mtv_search;
    public static GetDataEntity.KeyWord keyWord;
    //新用户的倒计时
    public static RelativeLayout show_new_user_view;
    private static Handler handler;
    public ArrayList<Fragment> fragments;
    @BindView(R.id.mll_message)
    MyLinearLayout mll_message;
    @BindView(R.id.miv_photo)
    MyImageView miv_photo;
    @BindView(R.id.miv_news)
    MyImageView miv_news;
    @BindView(R.id.miv_scan)
    MyImageView miv_scan;
    @BindView(R.id.mtv_scan)
    MyTextView mtv_scan;
    @BindView(R.id.mtv_news)
    MyTextView mtv_news;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.mllayout_title)
    MyLinearLayout mllayout_title;
    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;
    @BindView(R.id.data_coorLayout)
    CoordinatorLayout data_coorLayout;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    //   气泡
    @BindView(R.id.lLayout_toast)
    LinearLayout lLayout_toast;
    @BindView(R.id.miv_icon)
    MyImageView miv_icon;
    @BindView(R.id.tv_info)
    TextView tv_info;
    @BindView(R.id.tv_new_user_title)
    TextView tv_new_user_title;
    @BindView(R.id.tv_new_user_time)
    HoneRedDownTimerView tv_new_user_time;
    private PFirstPage pFirstPage;
    private String logoType, logoId;
    private MessageCountManager messageCountManager;
    private MainActivity mainActivity;
    private boolean isRefresh = false;
    private boolean isStop, misHide, isCrash;
    private boolean isPause = true;
    private Runnable runnableA, runnableB, runnableC;
    private Timer outTimer;
    private int mposition, size;

    public void beginToast() {
        if (isPause) {
            mposition = 0;
            isStop = false;
            if (pFirstPage != null)
                pFirstPage.getBubble();
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
        stopToast();
    }

    public void startTimer() {
        if (handler == null) {
            handler = new Handler();
        }
        runnableA = new Runnable() {
            @Override
            public void run() {
                if (!isStop) {
                    LogUtil.augusLogW("mposition：delayed");
                    mposition = 0;
                    if (pFirstPage != null)
                        pFirstPage.getBubble();
                }
            }
        };
        handler.postDelayed(runnableA, ((Constant.BUBBLE_SHOW + Constant.BUBBLE_DUR) * size + 1) * 1000);
    }

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
                        runnableB = new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && baseActivity != null && baseActivity.isDestroyed()) {
//                                throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
                                } else if (mposition < datas.size() && lLayout_toast != null && miv_icon != null && tv_info != null && !baseActivity.isFinishing()) {
                                    LogUtil.augusLogW("mposition:" + mposition);
                                    lLayout_toast.setVisibility(View.VISIBLE);
                                    GlideUtils.getInstance().loadCircleAvar(baseActivity, miv_icon, datas.get(mposition).avatar);
                                    tv_info.setText(datas.get(mposition).text);
                                    lLayout_toast.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (datas.get(mposition).url != null)
                                                Common.goGoGo(baseContext, datas.get(mposition).url.type, datas.get(mposition).url.item_id);
                                        }
                                    });
                                }
                            }
                        };
                        if (handler == null) {
                            if (!isCrash) {
                                isCrash = true;
                                Handler mHandler = new Handler(Looper.getMainLooper());
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        isCrash = false;
                                    }
                                }, ((Constant.BUBBLE_SHOW + Constant.BUBBLE_DUR) * size + 2) * 1000);
                            }
                        } else {
                            handler.post(runnableB);
                            runnableC = new Runnable() {
                                @Override
                                public void run() {
                                    if (!isStop) {
                                        if (lLayout_toast != null)
                                            lLayout_toast.setVisibility(View.GONE);
                                        mposition++;
                                    }
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
//   气泡

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.frag_first_page, container, false);
        mAppbar = (AppBarLayout) rootView.findViewById(R.id.mAppbar);
        miv_entry = (MyImageView) rootView.findViewById(R.id.miv_entry);
        miv_entrys = (MyImageView) rootView.findViewById(R.id.miv_entrys);
        show_new_user_view = rootView.findViewById(R.id.show_new_user_view);
        int value = TransformUtil.dip2px(baseActivity, 80);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(value, value);
        layoutParams.setMargins(0, 0, 0, value);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        miv_entry.setLayoutParams(layoutParams);
        int values = TransformUtil.dip2px(baseActivity, 100);
        RelativeLayout.LayoutParams layoutParamss = (RelativeLayout.LayoutParams) miv_entrys.getLayoutParams();
        layoutParamss.setMargins(0, values, 0, 0);
        layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        miv_entrys.setLayoutParams(layoutParamss);
        mtv_search = (MyTextView) rootView.findViewById(R.id.mtv_search);
        return rootView;
    }

    public void updateUserNewToast(ShowVoucherSuspension voucherSuspension) {
        if (voucherSuspension.suspensionShow.equals("1") && Common.isAlreadyLogin()) {
            tv_new_user_title.setText(voucherSuspension.suspension.prize);
            show_new_user_view.setVisibility(View.VISIBLE);
            if (miv_entry.getVisibility() == View.VISIBLE) {
                miv_entry.setVisibility(View.GONE);
            }
            if (voucherSuspension.suspension.finish > 0) {
                tv_new_user_time.setVisibility(View.VISIBLE);
                tv_new_user_time.cancelDownTimer();
                tv_new_user_time.setDownTime(voucherSuspension.suspension.finish);
                tv_new_user_time.startDownTimer();
                tv_new_user_time.setDownTimerListener(new OnCountDownTimerListener() {
                    @Override
                    public void onFinish() {
                        tv_new_user_time.cancelDownTimer();
                        show_new_user_view.setVisibility(View.GONE);
                    }
                });
            } else {
                tv_new_user_time.setVisibility(View.GONE);
            }
        } else {
            show_new_user_view.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);

    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    public void onResume() {
//        if (BuildConfig.DEBUG){
//            pFirstPage.getMenuData();
//        }
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseContext);
            if (messageCountManager.isLoad()) {
                messageCountManager.setTextCount(tv_msg_count);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        if (!misHide) {
            beginToast();
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

    @Override
    protected void initListener() {
        super.initListener();
        mll_message.setOnClickListener(this);
        miv_photo.setOnClickListener(this);
        miv_entry.setOnClickListener(this);
        miv_entrys.setOnClickListener(this);
        show_new_user_view.setOnClickListener(this);
        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (-verticalOffset >= mAppbar.getMeasuredHeight()) {
                    isExpand = false;
                    if (!Constant.IS_FIRST_SHARE)
                        ImmersionBar.with(FirstPageFrag.this).fitsSystemWindows(true)
                                .statusBarColor(R.color.pink_color)
                                .statusBarDarkFont(false, 0)
                                .init();
                    mllayout_title.setBackgroundColor(getColorResouce(R.color.pink_color));
                    miv_scan.setImageResource(R.mipmap.icon_home_saoyisao_w);
                    miv_news.setImageResource(R.mipmap.icon_home_message_w);
                    tv_msg_count.setTextColor(getColorResouce(R.color.pink_color));
                    tv_msg_count.setBackgroundResource(R.drawable.rounded_corner_white_100);
                    mtv_scan.setTextColor(getColorResouce(R.color.white));
                    mtv_news.setTextColor(getColorResouce(R.color.white));
                } else {
                    isExpand = true;
                    ImmersionBar.with(FirstPageFrag.this).fitsSystemWindows(true)
                            .statusBarColor(R.color.white)
                            .statusBarDarkFont(true, 0.2f)
                            .init();
                    mllayout_title.setBackgroundColor(getColorResouce(R.color.white));
                    miv_scan.setImageResource(R.mipmap.icon_home_saoyisao);
                    miv_news.setImageResource(R.mipmap.icon_home_message);
                    tv_msg_count.setTextColor(getColorResouce(R.color.white));
                    tv_msg_count.setBackgroundResource(R.drawable.rounded_corner_pink_100);
                    mtv_scan.setTextColor(getColorResouce(R.color.new_text));
                    mtv_news.setTextColor(getColorResouce(R.color.new_text));
                }
            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                mAppbar.setExpanded(true);
                if (mainActivity != null) {
                    mainActivity.position = arg0;
                }
                if (0 == arg0) {
                    misHide = false;
                    beginToast();
                } else {
                    misHide = true;
                    stopToast();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }


    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        pFirstPage = new PFirstPage(getContext(), this, null);
//        if (!BuildConfig.DEBUG){
//            pFirstPage.getMenuData();
//        }
        if (Constant.getMenuData != null) {
            setMenu(Constant.getMenuData);
        }
        pFirstPage.getMenuData();
        mainActivity = (MainActivity) getActivity();
//        if (mainActivity!=null&&mainActivity.adEntity!=null&&"1".equals(mainActivity.adEntity.suspensionShow)){
//            miv_entry.setVisibility(View.VISIBLE);
//            GlideUtils.getInstance().loadImageZheng(mainActivity,FirstPageFrag.miv_entry,mainActivity.adEntity.suspension.image);
//        }else {
//            miv_entry.setVisibility(View.GONE);
//        }
        nei_empty.setImageResource(R.mipmap.img_empty_wuwangluo)
                .setText(getString(R.string.common_wangluozhuangkuang))
                .setButtonText(getStringResouce(R.string.common_dianjichongshi))
                .setOnClickListener((view) -> {
                    isRefresh = true;
                    pFirstPage.getMenuData();
                });
    }

    @OnClick(R.id.mllayout_scan)
    public void scan() {
        ZXingDemoAct.startAct(baseActivity, false, 0);
//        H5X5Act.startAct(baseContext,"http://soft.imtt.qq.com/browser/tes/feedback.html",H5X5Act.MODE_SONIC);
//        H5X5Act.startAct(baseContext,"https://plus.mengtianvip.com/plus",H5X5Act.MODE_SONIC);
//        PaySuccessAct.startAct(baseActivity, "", "78", "1901234941107803Q587", false);
//        H5X5Act.startAct(baseContext,"http://mt-front.v2.shunliandongli.com",H5X5Act.MODE_SONIC);
    }

    @OnClick(R.id.mllayout_search)
    public void search() {
        if (keyWord!=null&&!isEmpty(keyWord.type)){
            SearchGoodsActivity.startAct(baseActivity, getStringResouce(R.string.first_souni).equals(mtv_search.getText().toString()) ? "" : mtv_search.getText().toString(), "FirstPageFrag",keyWord.type,keyWord.item_id);
        }else {
            SearchGoodsActivity.startAct(baseActivity, getStringResouce(R.string.first_souni).equals(mtv_search.getText().toString()) ? "" : mtv_search.getText().toString(), "FirstPageFrag");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mll_message:
                mAppbar.setExpanded(false);
                MessageActivity.startAct(getActivity());
//                H5X5Act.startAct(getActivity(),"http://front.v2.shunliandongli.com/demo?sl_debug=open", H5X5Act.MODE_SONIC);
                break;
            case R.id.miv_photo:
                Common.goGoGo(getContext(), logoType, logoId);
                break;
            case R.id.show_new_user_view:
                Common.goGoGo(getContext(), "newuser");
                break;
            case R.id.miv_entrys:
//                if (isHides) {
//                    int values = TransformUtil.dip2px(baseActivity, 100);
//                    RelativeLayout.LayoutParams layoutParamss = (RelativeLayout.LayoutParams) miv_entrys.getLayoutParams();
//                    layoutParamss.setMargins(0, values, 0, 0);
//                    layoutParamss.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                    miv_entrys.setLayoutParams(layoutParamss);
//                    isHides = false;
//                } else {
                    String token = SharedPrefUtil.getSharedUserString("token", "");
                    if (TextUtils.isEmpty(token)) {
                        Common.goGoGo(baseActivity,"login");
//                        Common.theRelayJump("taskSystems",null);
                    } else {
                        if (mainActivity != null && mainActivity.adEntitys != null && mainActivity.adEntitys.url != null )
                            Common.goGoGo(baseActivity, mainActivity.adEntitys.url.type, mainActivity.adEntitys.url.item_id);
//                    }

                }
                break;
            case R.id.miv_entry:
                if (isHide) {
                    int value = TransformUtil.dip2px(baseActivity, 80);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(value, value);
                    layoutParams.setMargins(0, 0, 0, value);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    miv_entry.setLayoutParams(layoutParams);
                    isHide = false;
                } else {
                    if (mainActivity != null && mainActivity.adEntity != null && mainActivity.adEntity.suspension != null && mainActivity.adEntity.suspension.link != null)
                        Common.goGoGo(baseActivity, mainActivity.adEntity.suspension.link.type, mainActivity.adEntity.suspension.link.item_id);
                }
                break;
        }

    }

    public void setMenu(GetMenuEntity getMenuEntiy) {
        if (getMenuEntiy == null) {
            return;
        }
        visible(data_coorLayout);
        gone(nei_empty);
        if (miv_photo != null) {
            if (getMenuEntiy.logo != null) {
                logoType = getMenuEntiy.logo.type;
                logoId = getMenuEntiy.logo.item_id;
                GlideUtils.getInstance().loadImage(getContext(), miv_photo, getMenuEntiy.logo.bg_pic, R.mipmap.img_default_home_logo);
            } else {
                miv_photo.setVisibility(View.GONE);
            }
        }
        fragments = new ArrayList<>();
        for (int i = 0; i < getMenuEntiy.datas.size(); i++) {
            fragments.add(CateGoryFrag.getInstance(getMenuEntiy.datas.get(i).id, getMenuEntiy.datas.get(i).channel_name));
            if (i >= getMenuEntiy.datas.size() - 1 && isAdded()) {
                firstId = getMenuEntiy.datas.get(0).id;
                pager.setAdapter(new MyFrPagerAdapter(getChildFragmentManager(), getMenuEntiy.datas, fragments));
                tabs.setViewPager(pager);
                setTabsValue();
            }
        }
    }

    @Override
    public void setTab(GetMenuEntity getMenuEntiy) {
        if (Constant.getMenuData == null || isRefresh)
            setMenu(getMenuEntiy);
        isRefresh = false;
        Constant.getMenuData = getMenuEntiy;
    }

    @Override
    public void setContent(GetDataEntity getDataEntity) {

    }

    @Override
    public void setGoods(List<GoodsDeatilEntity.Goods> mDatas, int page, int allPage) {

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

    @Override
    public void showFailureView(int request_code) {
        if (666 == request_code) {
            size = 2;
            startTimer();
        } else if (888 == request_code) {
            visible(nei_empty);
            gone(data_coorLayout);
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {
        if (888 == request_code) {
            visible(nei_empty);
            gone(data_coorLayout);
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        //设置加粗
        tabs.setTypeface(null, Typeface.BOLD);

        // 设置Tab是自动填充满屏幕的
        tabs.setShouldExpand(true);

        // 设置Tab的分割线的颜色
        tabs.setDividerColor(getResources().getColor(R.color.white));
        // 设置分割线的上下的间距,传入的是dp
        tabs.setDividerPaddingTopBottom(12);

        // 设置Tab底部线的高度,传入的是dp
        tabs.setUnderlineHeight(0);
        //设置Tab底部线的颜色
        tabs.setUnderlineColor(getResources().getColor(R.color.white));

        // 设置Tab 指示器Indicator的高度,传入的是dp
        tabs.setIndicatorHeight(0);
        // 设置Tab Indicator的颜色
        tabs.setIndicatorColor(getResources().getColor(R.color.white));

        // 设置Tab标题文字的大小,传入的是sp
        tabs.setTextSize(15);
        // 设置选中Tab文字的颜色
        tabs.setSelectedTextColor(getColorResouce(R.color.colorAccent));
        //设置正常Tab文字的颜色
        tabs.setTextColor(getColorResouce(R.color.new_text));

        //  设置点击Tab时的背景色
//        tabs.setTabBackground(R.drawable.background_tab);

        //是否支持动画渐变(颜色渐变和文字大小渐变)
        tabs.setFadeEnabled(true);
        // 设置最大缩放,正常状态为
        tabs.setZoomMax(0.5F);

        //设置Tab文字的左右间距,传入的是dp
        tabs.setTabPaddingLeftRight(20);
    }

}
