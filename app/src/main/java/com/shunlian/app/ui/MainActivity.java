package com.shunlian.app.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShowSignEntity;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.DiscoveryLocationEvent;
import com.shunlian.app.eventbus_bean.MeLocationEvent;
import com.shunlian.app.eventbus_bean.MessageReadSuccessEvent;
import com.shunlian.app.eventbus_bean.SuspensionRefresh;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.newchat.websocket.EasyWebsocketClient;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.coupon.CouponListAct;
import com.shunlian.app.ui.find_send.FindSendPictureTextAct;
import com.shunlian.app.ui.find_send.SelectPicVideoAct;
import com.shunlian.app.ui.fragment.NewDiscoverFrag;
import com.shunlian.app.ui.fragment.NewPersonalCenterFrag;
import com.shunlian.app.ui.fragment.ShoppingCarFrag;
import com.shunlian.app.ui.fragment.SortFrag;
import com.shunlian.app.ui.fragment.first_page.CateGoryFrag;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.ui.h5.H5PlusFrag;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.h5.H5X5Frag;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HighLightKeyWordUtil;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.sideslip.ActivityHelper;
import com.shunlian.app.view.IMain;
import com.shunlian.app.widget.CommondDialog;
import com.shunlian.app.widget.DiscoveryGuideView;
import com.shunlian.app.widget.MeGuideView;
import com.shunlian.app.widget.MyFrameLayout;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.UpdateDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity implements MessageCountManager.OnGetMessageListener, IMain {
    private static final String[] flags = {"mainPage", "myPlus", "discover", "shoppingcar", "personCenter"};
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
    public int position = 0;
    public CommonEntity data;
    @BindView(R.id.mtv_message_count)
    public MyTextView mtv_message_count;
    public AdEntity adEntity;
    public ShowSignEntity adEntitys;
    @BindView(R.id.fl_main)
    MyFrameLayout fl_main;
    @BindView(R.id.ll_tab_main_page)
    MyRelativeLayout ll_tab_main_page;
    @BindView(R.id.miv_tab_main)
    MyImageView miv_tab_main;
    @BindView(R.id.tv_tab_main)
    TextView tv_tab_main;
    @BindView(R.id.ll_tab_sort)
    MyRelativeLayout ll_tab_sort;
    @BindView(R.id.miv_tab_sort)
    MyImageView miv_tab_sort;
    @BindView(R.id.tv_tab_sort)
    TextView tv_tab_sort;
    @BindView(R.id.ll_tab_discover)
    MyRelativeLayout ll_tab_discover;
    @BindView(R.id.miv_tab_discover)
    MyImageView miv_tab_discover;
    @BindView(R.id.tv_tab_discover)
    TextView tv_tab_discover;
    @BindView(R.id.ll_tab_shopping_car)
    MyRelativeLayout ll_tab_shopping_car;
    @BindView(R.id.miv_shopping_car)
    MyImageView miv_shopping_car;
    @BindView(R.id.miv_first)
    MyImageView miv_first;
    @BindView(R.id.tv_shopping_car)
    TextView tv_shopping_car;
    @BindView(R.id.ll_tab_person_center)
    MyRelativeLayout ll_tab_person_center;
    @BindView(R.id.miv_person_center)
    MyImageView miv_person_center;
    @BindView(R.id.tv_person_center)
    TextView tv_person_center;
    @BindView(R.id.view_message)
    View view_message;
    @BindView(R.id.miv_hint)
    MyImageView miv_hint;
    //    private MainPageFrag mainPageFrag;
    private FirstPageFrag mainPageFrag;
    //    private MyPlusFrag myPlusFrag;
    private SortFrag sortFrag;
    private H5PlusFrag h5PlusFrag;
    //    private DiscoverFrag discoverFrag;
    private NewDiscoverFrag discoverFrag;
    private ShoppingCarFrag shoppingCarFrag;
    private NewPersonalCenterFrag personalCenterFrag;
    private long mExitTime;
    private FragmentManager fragmentManager;
    private int pageIndex;
    //    private String flag="default";
    private String flag;
    private Dialog dialog_ad, dialog_new;
    private MessageCountManager messageCountManager;
    private PMain pMain;
    private UpdateDialog updateDialogV;//判断是否需要跟新
    private boolean isFirst = false;
    //    private boolean  isFirst = false;
    private Handler handler;
    private CateGoryFrag cateGoryFrag;
    private MyLinearLayout mllayout_before, mllayout_after;
    private NewTextView ntv_get, ntv_aOne, ntv_check, ntv_use;
    private boolean isGetAward = false;
    private ObjectMapper objectMapper;
    private int[] currentLocation;
    private int currentImgWidth;
    private boolean isShowGuide = false;
    private boolean isShowGuideMe = false;
    @BindView(R.id.ntv_uuid)
    NewTextView ntv_uuid;
    private String currentDiscoverFlag;
    private PromptDialog promptDialog;

    public static void startAct(Context context, String flag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", flag);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startAct(Context context, String flag, String discoverFlag) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("flag", flag);
        intent.putExtra("discover_flag", discoverFlag);
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
        return R.layout.activity_main;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        /*if (isGetAward){//新人优惠券手动领取
            pMain.getPrizeByRegister();
            isGetAward=false;
        }*/
        if (!isEmpty(flag) && "nicefocusexperiencecirclematerial".contains(flag)) {
            flag = "";
        } else {
            initMessage();
        }
//        if (Common.isAlreadyLogin()){
        pMain.isShowSign();
//        }else {
//            FirstPageFrag.miv_entrys.setVisibility(View.VISIBLE);
//        }
    }

    public void initMessage() {
        pMain.getDiscoveryUnreadCount();
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        objectMapper = new ObjectMapper();
        if (SharedPrefUtil.getSharedUserBoolean("hide_first", false)) {
            miv_hint.setVisibility(View.GONE);
        } else {
            GlideUtils.getInstance().loadLocal(this, miv_hint, R.drawable.firsts_hints);
            miv_hint.setVisibility(View.VISIBLE);
        }

        if (false) {
            ntv_uuid.setVisibility(View.VISIBLE);
            ntv_uuid.setText("uuid:\n" + UUID.nameUUIDFromBytes(Build.SERIAL.getBytes()).toString().toUpperCase());
            ntv_uuid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.copyText(baseAct, UUID.nameUUIDFromBytes(Build.SERIAL.getBytes()).toString().toUpperCase());
                }
            });
        }

        pMain = new PMain(MainActivity.this, MainActivity.this);
        pMain.entryInfo();
        pMain.isShowUserNewPersonPrize();
        initMessage();
        fragmentManager = getSupportFragmentManager();
        mainPageClick();
        if (updateDialogV == null)
            updateDialogV = new UpdateDialog(this) {
                @Override
                public void initDialogFinish() {
                    if (updateDialogV.updateDialog == null) {
                        pMain.getPopAD();
//                        if (Common.isAlreadyLogin()){
                            pMain.isShowSign();
//                        }else {
//                            FirstPageFrag.miv_entrys.setVisibility(View.VISIBLE);
//                        }
                    }
                }
            };
        if (Common.isAlreadyLogin()) {
            EasyWebsocketClient.getInstance(this).initChat(); //初始化聊天
            messageCountManager = MessageCountManager.getInstance(this);
            messageCountManager.initData();
            messageCountManager.setOnGetMessageListener(this);
        }
        Common.parseClipboard(this);
//        if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))){
//            visible(ll_tab_sort);
//        }else {
//            gone(ll_tab_sort);
//        }
        loadUserInfo();
    }



    @Override
    protected void initListener() {
        super.initListener();
        ll_tab_main_page.setOnClickListener(this);
        ll_tab_sort.setOnClickListener(this);
        ll_tab_discover.setOnClickListener(this);
        ll_tab_shopping_car.setOnClickListener(this);
        ll_tab_person_center.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
//        if (Common.isPlus()) {
//            tv_tab_sort.setText(getStringResouce(R.string.main_wodedian));
//        } else {
//            tv_tab_sort.setText(getStringResouce(R.string.main_shengjiplus));
//        }
        Common.handleTheRelayJump(this);
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        if (!isEmpty(getIntent().getStringExtra("flag")))
        flag = getIntent().getStringExtra("flag");
        currentDiscoverFlag = getIntent().getStringExtra("discover_flag");
        /*if (TextUtils.isEmpty(flag)) {
            mainPageClick();
        } else {
            switch2jump(flag);
        }*/
        switch2jump(flag);
        if ("route_login".equals(flag)) {
            Common.goGoGo(this, "login");
        }
        if (discoverFrag != null) {
            discoverFrag.setCurrentPage(currentDiscoverFlag);
        }
        Common.handleTheRelayJump(this);
    }

    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                fragmentManager.beginTransaction().remove(show).commitAllowingStateLoss();
                fragmentManager.beginTransaction().add(R.id.fl_main, show).commitAllowingStateLoss();
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


    @Override
    public void onClick(View view) {
        if (MyOnClickListener.isFastClick()) {
            return;
        }
        //春节活动
//        if (view.getId() == R.id.ll_tab_main_page) {
//            miv_first.setVisibility(View.VISIBLE);
//            miv_tab_main.setVisibility(View.GONE);
//            tv_tab_main.setVisibility(View.GONE);
//            miv_first.animate().rotation(0).setDuration(0).start();
//            miv_first.animate().rotation(360).setDuration(300).start();
//        } else {
//            if (view.getId() == R.id.ll_tab_discover) {
//                if (discoverFrag != null && !discoverFrag.isVisible()) {
//                    view.animate().scaleX(0.2f).scaleY(0.2f).setDuration(0).start();
//                    view.animate().scaleX(1).scaleY(1).setDuration(300).start();
//                }
//            } else {
//                view.animate().scaleX(0.2f).scaleY(0.2f).setDuration(0).start();
//                view.animate().scaleX(1).scaleY(1).setDuration(300).start();
//            }
//        }
        //春节活动

        if (view.getId() == R.id.ll_tab_discover) {
            mtv_message_count.setVisibility(View.GONE);
            if(data!=null) {
                data.count = 0;
            }
            try {
                if (discoverFrag != null && discoverFrag.isVisible()) {

                    if (!Common.isAlreadyLogin()) {
                        Common.goGoGo(this,"login");
                        return;
                    }

                    String baseInfoStr = SharedPrefUtil.getSharedUserString("base_info", "");
                    HotBlogsEntity.BaseInfo baseInfo = objectMapper.readValue(baseInfoStr, HotBlogsEntity.BaseInfo.class);
                    FindSendPictureTextAct.SendConfig sendConfig = new FindSendPictureTextAct.SendConfig();

                    if (baseInfo.white_list == 0) {
                        sendConfig.isWhiteList = false;
                    } else {
                        sendConfig.isWhiteList = true;
                    }
                    sendConfig.memberId = baseInfo.member_id;

                    //判断跳转逻辑:非plus和非内部账号不跳转发布界面
                    if (Common.isPlus()) {
                        EventBus.getDefault().postSticky(sendConfig);
                        SelectPicVideoAct.startAct(this,
                                FindSendPictureTextAct.SELECT_PIC_REQUESTCODE,9);
                    } else {
                        if (baseInfo.is_inner == 1) {
                            EventBus.getDefault().postSticky(sendConfig);
                            SelectPicVideoAct.startAct(this,
                                    FindSendPictureTextAct.SELECT_PIC_REQUESTCODE,9);
                        } else {
                            initHintDialog();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (handler == null)
            handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (view.getId()) {
                    case R.id.ll_tab_main_page:
                        JosnSensorsDataAPI.currentPageMain = 1;
                        if (isFirst && !isEmpty(mainPageFrag.fragments) && mainPageFrag.fragments.get(position) != null) {
                            cateGoryFrag = (CateGoryFrag) mainPageFrag.fragments.get(position);
                            if (cateGoryFrag.rv_view != null) {
                                cateGoryFrag.rv_view.scrollToPosition(0);
//                                cateGoryFrag.rv_view.smoothScrollToPosition(0);
                                FirstPageFrag.mAppbar.setExpanded(true);
                            }
                        } else {
                            mainPageClick();
                        }
                        break;
                    case R.id.ll_tab_sort:
                        //myPlusClick();
                        JosnSensorsDataAPI.currentPageMain = 2;
                        sortClick();
                        break;
                    case R.id.ll_tab_discover:
                        JosnSensorsDataAPI.currentPageMain = 3;
                        discoverClick();
                        break;
                    case R.id.ll_tab_shopping_car:
                        JosnSensorsDataAPI.currentPageMain = 4;
//                        CouponMsgAct.startAct(MainActivity.this,"");
                        shoppingCarClick();
                        break;
                    case R.id.ll_tab_person_center:
                        JosnSensorsDataAPI.currentPageMain = 5;
                        miv_hint.setVisibility(View.GONE);
                        SharedPrefUtil.saveSharedUserBoolean("hide_first", true);
                        personCenterClick();
                        break;
                }
            }
            //春节活动
//        }, 300);
        }, 0);
        //春节活动
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(this);
            promptDialog.setTvCancleIsBold(false);
            promptDialog.setTvSureIsBold(false);
            promptDialog.setTvSureColor(R.color.pink_color);
            promptDialog.setTvSureBGColor(Color.WHITE);
        }
        promptDialog.setSureAndCancleListener("亲，您还不是PLUS会员哦,现在开通享7大专属权益", "立即开通", view -> {
            promptDialog.dismiss();
            H5X5Act.startAct(MainActivity.this, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
        }, getStringResouce(R.string.errcode_cancel), view -> promptDialog.dismiss()).show();
    }

    public void mainPageClick() {
        isFirst = true;
        if (mainPageFrag == null) {
            mainPageFrag = (FirstPageFrag) fragmentMap.get(flags[0]);
            if (mainPageFrag == null) {
                mainPageFrag = new FirstPageFrag();
                fragmentMap.put(flags[0], mainPageFrag);
            }
        }
        switchContent(mainPageFrag);
        pageIndex = 0;
        chageTabItem(pageIndex);
    }

    public void myPlusClick() {
        isFirst = false;
//        if (!Common.isAlreadyLogin() || !Common.isPlus()) {
//            H5Act.startAct(baseAct, Constant.PLUS_ADD, H5Act.MODE_SONIC);
//            return;
//        }
        //先判断此碎片是否第一次点击，是的话初始化碎片
        String url;
        if (!Common.isAlreadyLogin() || !Common.isPlus()) {
            url = SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD);
        } else {
            url = SharedPrefUtil.getCacheSharedPrf("plus_index", Constant.PLUS_ADD);
        }
        if (h5PlusFrag == null) {
            h5PlusFrag = (H5PlusFrag) fragmentMap.get(flags[1]);
            if (h5PlusFrag == null) {
                h5PlusFrag = (H5PlusFrag) H5PlusFrag.getInstance(url, H5X5Frag.MODE_SONIC);
                fragmentMap.put(flags[1], h5PlusFrag);
            }
        } else {
            try {
                if (!TextUtils.isEmpty(url))
                    url = java.net.URLDecoder.decode(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            h5PlusFrag.h5Url = url;
            h5PlusFrag.reFresh();
        }

        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(h5PlusFrag);
        pageIndex = 1;
        chageTabItem(pageIndex);
    }

//    public void myPlusClick() {
//        isFirst = false;
//        if (!Common.isAlreadyLogin() || !Common.isPlus()) {
//            H5Act.startAct(baseAct, Constant.PLUS_ADD, H5Act.MODE_SONIC);
//            return;
//        }
//        //先判断此碎片是否第一次点击，是的话初始化碎片
//        if (myPlusFrag == null) {
//            myPlusFrag = (MyPlusFrag) fragmentMap.get(flags[1]);
//            if (myPlusFrag == null) {
//                myPlusFrag = new MyPlusFrag();
//                fragmentMap.put(flags[1], myPlusFrag);
//            }
//        } else {
//            myPlusFrag.getPlusData();
//        }
//
//        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
//        switchContent(myPlusFrag);
//        pageIndex = 1;
//        chageTabItem(pageIndex);
//    }

    public void sortClick() {
        isFirst = false;
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (sortFrag == null) {
            sortFrag = (SortFrag) fragmentMap.get(flags[1]);
            if (sortFrag == null) {
                sortFrag = new SortFrag();
                fragmentMap.put(flags[1], sortFrag);
            }
        }

        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(sortFrag);
        pageIndex = 1;
        chageTabItem(pageIndex);
    }

    public void discoverClick() {
        isFirst = false;
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (discoverFrag == null) {
            discoverFrag = (NewDiscoverFrag) fragmentMap.get(flags[2]);
            if (discoverFrag == null) {
                discoverFrag = new NewDiscoverFrag();
                Bundle bundle = new Bundle();
                bundle.putString("flag", flag);
                discoverFrag.setArguments(bundle);
                fragmentMap.put(flags[2], discoverFrag);
            }
        } else {
//            discoverFrag.setArgument(flag);
//            if (Common.isAlreadyLogin()) {
//                discoverFrag.initMessage(data);
//            } else {
//                discoverFrag.initMessage(null);
//            }
        }
        switchContent(discoverFrag);
        pageIndex = 2;
        chageTabItem(pageIndex);
    }

    public void shoppingCarClick() {
        isFirst = false;
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(this, "login");
            return;
        }
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (shoppingCarFrag == null) {
            shoppingCarFrag = (ShoppingCarFrag) fragmentMap.get(flags[3]);
            if (shoppingCarFrag == null) {
                shoppingCarFrag = new ShoppingCarFrag();
                fragmentMap.put(flags[3], shoppingCarFrag);
            }
        } else {
            shoppingCarFrag.getShoppingCarData();
        }

        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(shoppingCarFrag);
        pageIndex = 3;
        chageTabItem(pageIndex);
    }

    public void personCenterClick() {
        isFirst = false;
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(this, "login");
            return;
        }
        //先判断此碎片是否第一次点击，是的话初始化碎片
        if (personalCenterFrag == null) {
            personalCenterFrag = (NewPersonalCenterFrag) fragmentMap.get(flags[4]);
            if (personalCenterFrag == null) {
                personalCenterFrag = new NewPersonalCenterFrag();
                fragmentMap.put(flags[4], personalCenterFrag);
            }
        } else {
            personalCenterFrag.getPersonalcenterData();
        }

        //把当前点击的碎片作为参数，表示显示当前碎片，并且隐藏其他碎片
        switchContent(personalCenterFrag);
        pageIndex = 4;
        chageTabItem(pageIndex);
    }

    private void chageTabItem(int pageIndex) {
//        双十一活动
//        RelativeLayout.LayoutParams layoutParams_discover= (RelativeLayout.LayoutParams) miv_tab_discover.getLayoutParams();
//        RelativeLayout.LayoutParams layoutParams_message_count= (RelativeLayout.LayoutParams) mtv_message_count.getLayoutParams();
//        LinearLayout.LayoutParams layoutParams_main= (LinearLayout.LayoutParams) miv_tab_main.getLayoutParams();
//        LinearLayout.LayoutParams layoutParams_sort= (LinearLayout.LayoutParams) miv_tab_sort.getLayoutParams();
//        LinearLayout.LayoutParams layoutParams_shopping_car= (LinearLayout.LayoutParams) miv_shopping_car.getLayoutParams();
//        LinearLayout.LayoutParams layoutParams_person_center= (LinearLayout.LayoutParams) miv_person_center.getLayoutParams();
//        int topOne= -TransformUtil.dip2px(baseAct,12);
//        int topTwo= TransformUtil.dip2px(baseAct,8);
//        int topThree= -TransformUtil.dip2px(baseAct,10);
//        int topFour= TransformUtil.dip2px(baseAct,6);
//
//
//        layoutParams_message_count.setMargins(0,topFour,topTwo,0);
//        miv_tab_discover.setImageResource(R.mipmap.tab_faxian_p);
//        layoutParams_discover.setMargins(0,topTwo,0,0);
//
//        miv_tab_main.setImageResource(R.mipmap.tab_shouye_p);
//        layoutParams_main.setMargins(0,topTwo,0,0);
//
//        miv_tab_sort.setImageResource(R.mipmap.tab_fenlei_p);
//        layoutParams_sort.setMargins(0,topTwo,0,0);
//
//        miv_shopping_car.setImageResource(R.mipmap.tab_gouwuche_p);
//        layoutParams_shopping_car.setMargins(0,topTwo,0,0);
//
//        miv_person_center.setImageResource(R.mipmap.tab_gerenzhongxin_p);
//        layoutParams_person_center.setMargins(0,topTwo,0,0);
//
//        switch (pageIndex) {
//            case 0:
//                miv_tab_main.setImageResource(R.mipmap.tab_01_sel);
//                layoutParams_main.setMargins(0,topOne,0,0);
//                break;
//            case 1:
//                miv_tab_sort.setImageResource(R.mipmap.tab_02_sel);
//                layoutParams_sort.setMargins(0,topOne,0,0);
//                break;
//            case 2:
//                miv_tab_discover.setImageResource(R.mipmap.tab_03_sel);
//                layoutParams_discover.setMargins(0,topOne,0,0);
//                layoutParams_message_count.setMargins(0,topThree,topFour,0);
//                break;
//            case 3:
//                miv_shopping_car.setImageResource(R.mipmap.tab_04_sel);
//                layoutParams_shopping_car.setMargins(0,topOne,0,0);
//                break;
//            case 4:
//                miv_person_center.setImageResource(R.mipmap.tab_05_sel);
//                layoutParams_person_center.setMargins(0,topOne,0,0);
//                break;
//        }
        //        双十一活动

        //春节活动
//        miv_tab_main.setImageResource(R.mipmap.tab_1_n);
//        tv_tab_main.setTextColor(getResources().getColor(R.color.tab_text_n));
//
//        miv_tab_sort.setImageResource(R.mipmap.tab_2_n);
//        tv_tab_sort.setTextColor(getResources().getColor(R.color.tab_text_n));
//
//        miv_tab_discover.setImageResource(R.mipmap.tab_3_n);
//        tv_tab_discover.setTextColor(getResources().getColor(R.color.tab_text_n));
//
//        miv_shopping_car.setImageResource(R.mipmap.tab_4_n);
//        tv_shopping_car.setTextColor(getResources().getColor(R.color.tab_text_n));
//
//        miv_person_center.setImageResource(R.mipmap.tab_5_n);
//        tv_person_center.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_tab_main.setImageResource(R.mipmap.tab_1_n_new);
        tv_tab_main.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_tab_sort.setImageResource(R.mipmap.tab_2_n_new);
        tv_tab_sort.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_tab_discover.setImageResource(R.mipmap.tab_3_n_new);
        tv_tab_discover.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_shopping_car.setImageResource(R.mipmap.tab_4_n_new);
        tv_shopping_car.setTextColor(getResources().getColor(R.color.tab_text_n));

        miv_person_center.setImageResource(R.mipmap.tab_5_n_new);
        tv_person_center.setTextColor(getResources().getColor(R.color.tab_text_n));

        //春节活动

        miv_first.setVisibility(View.GONE);
        miv_tab_main.setVisibility(View.VISIBLE);
        tv_tab_main.setVisibility(View.VISIBLE);

        //春节活动
        switch (pageIndex) {
            case 0:

//                miv_first.setVisibility(View.VISIBLE);
//                miv_tab_main.setVisibility(View.GONE);
//                tv_tab_main.setVisibility(View.GONE);
                if(data!=null&&data.count>0) {
                    mtv_message_count.setVisibility(View.VISIBLE);
                }else{
                    mtv_message_count.setVisibility(View.GONE);
                }
                miv_tab_main.setImageResource(R.mipmap.tab_1_h_new);
                tv_tab_main.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 1:
                if(data!=null&&data.count>0) {
                    mtv_message_count.setVisibility(View.VISIBLE);
                }else{
                    mtv_message_count.setVisibility(View.GONE);
                }
//                miv_tab_sort.setImageResource(R.mipmap.tab_2_h);
                miv_tab_sort.setImageResource(R.mipmap.tab_2_h_new);
                tv_tab_sort.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 2:
//                miv_tab_discover.setImageResource(R.mipmap.tab_03);
                miv_tab_discover.setImageResource(R.mipmap.tab_3_h_new);
                tv_tab_discover.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 3:
                if(data!=null&&data.count>0) {
                    mtv_message_count.setVisibility(View.VISIBLE);
                }else{
                    mtv_message_count.setVisibility(View.GONE);
                }
//                miv_shopping_car.setImageResource(R.mipmap.tab_4_h);
                miv_shopping_car.setImageResource(R.mipmap.tab_4_h_new);
                tv_shopping_car.setTextColor(getResources().getColor(R.color.pink_color));
                break;
            case 4:
                if(data!=null&&data.count>0) {
                    mtv_message_count.setVisibility(View.VISIBLE);
                }else{
                    mtv_message_count.setVisibility(View.GONE);
                }
//                miv_person_center.setImageResource(R.mipmap.tab_5_h);
                miv_person_center.setImageResource(R.mipmap.tab_5_h_new);
                tv_person_center.setTextColor(getResources().getColor(R.color.pink_color));
                break;
        }
        //春节活动

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Common.staticToast("再按一次退出顺联动力");
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    finish();
                } catch (Exception e) {
                    finish();
                } finally {
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void switch2jump(String flag) {
        try {
            switch (flag) {
                case "mainPage":
                    mainPageClick();
                    break;
                case "myplus":
                    //myPlusClick();
                    sortClick();
                    break;
                case "focus":
                case "discover":
                    discoverClick();
                    break;
                case "shoppingcar":
                    shoppingCarClick();
                    break;
                case "personCenter":
                    personCenterClick();
                    break;
                default:
                    mainPageClick();
                    break;
            }
        }catch (Exception e){

        }
    }

    @Override
    protected void onDestroy() {
        if (updateDialogV != null) {
            if (updateDialogV.updateDialog != null) {
                updateDialogV.updateDialog.dismiss();
            }
            PromptDialog promptDialog = updateDialogV.getPromptDialog();
            if (promptDialog != null) {
                promptDialog.dismiss();
                promptDialog = null;
            }
        }
        super.onDestroy();
        if (fragmentMap != null)
            fragmentMap.clear();
        mainPageFrag = null;
//        myPlusFrag = null;
        h5PlusFrag = null;
        discoverFrag = null;
        cateGoryFrag = null;
        personalCenterFrag = null;
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //防止app崩溃后造成fragment重叠
        //super.onSaveInstanceState(outState);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        //可以开始设置消息数量的数据了
    }

    @Override
    public void OnLoadFail() {
        //可以开始设置消息数量的数据了
    }


    public void initDialog(AdEntity data) {
        if (dialog_ad == null) {
            dialog_ad = new Dialog(this, R.style.popAd);
            dialog_ad.setContentView(R.layout.dialog_ad);
            MyImageView miv_close = dialog_ad.findViewById(R.id.miv_close);
            MyImageView miv_photo = dialog_ad.findViewById(R.id.miv_photo);
            GlideUtils.getInstance().loadImage(baseAct, miv_photo, data.list.ad_img);
            miv_close.setOnClickListener(view -> dialog_ad.dismiss());
            miv_photo.setOnClickListener(view -> {
                Common.goGoGo(baseAct, data.list.link.type, data.list.link.item_id);
                dialog_ad.dismiss();
            });
            dialog_ad.setCancelable(false);
        }
        dialog_ad.show();
    }

    public void initDialogs(String prize) {
        if (dialog_new == null) {
            dialog_new = new Dialog(this, R.style.popAd);
            dialog_new.setContentView(R.layout.dialog_user_new);
            MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
            ntv_get = dialog_new.findViewById(R.id.ntv_get);
            miv_close.setOnClickListener(view -> dialog_new.dismiss());
            ntv_get.setOnClickListener(view -> {
                dialog_new.dismiss();
                NewUserPageActivity.startAct(MainActivity.this, true);
            });
        }
        ntv_aOne = dialog_new.findViewById(R.id.ntv_user_bOne);
        ntv_aOne.setText(HighLightKeyWordUtil.getHighLightKeyWord(
                getResources().getColor(R.color.pink_color), prize, "现金红包"));
        Activity activity = ActivityHelper.getActivity();
        if (activity != null && activity instanceof MainActivity){
            dialog_new.show();
        }
    }


    @Override
    public void setAD(AdEntity data) {
        adEntity = data;
        if ("1".equals(data.suspensionShow) && mainPageFrag != null) {
            FirstPageFrag.miv_entry.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImageZheng(this, FirstPageFrag.miv_entry, data.suspension.image);
//            GlideUtils.getInstance().loadImageZheng(this, FirstPageFrag.miv_entry, "http://i.imgur.com/GP1m9.png");//apng图片不支持
//            GlideUtils.getInstance().loadImageZheng(this, FirstPageFrag.miv_entry, "https://upload-images.jianshu.io/upload_images/2625875-9a044086b7de0a45.gif");
        } else {
            FirstPageFrag.miv_entry.setVisibility(View.GONE);
        }
        if ("1".equals(data.show) && !SharedPrefUtil.getCacheSharedPrf("ad_id", "").equals(data.list.ad_sn)) {
            initDialog(data);
            SharedPrefUtil.saveCacheSharedPrf("ad_id", data.list.ad_sn);
        }
        CommondDialog commondDialog = new CommondDialog(this);
        commondDialog.parseCommond();
    }

    @Override
    public void setADs(ShowSignEntity data) {
        adEntitys=data;
        if ("1".equals(data.is_show) && mainPageFrag != null) {
            FirstPageFrag.miv_entrys.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImageChang(this, FirstPageFrag.miv_entrys, data.pic);
//            GlideUtils.getInstance().loadImageZheng(this, FirstPageFrag.miv_entry, "http://i.imgur.com/GP1m9.png");//apng图片不支持
//            GlideUtils.getInstance().loadImageZheng(this, FirstPageFrag.miv_entry, "https://upload-images.jianshu.io/upload_images/2625875-9a044086b7de0a45.gif");
        } else {
            FirstPageFrag.miv_entrys.setVisibility(View.GONE);
        }
    }


    @Override
    public void setContent(GetDataEntity data) {

    }

    @Override
    public void setTab(GetMenuEntity data) {

    }

    @Override
    public void setCommond(CommondEntity data) {
    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {
    }

    @Override
    public void entryInfo(CommonEntity data) {
        Constant.EMAIL = data.ducha_email;
        SharedPrefUtil.saveSharedUserString("plus_role", data.is_plus);
        SharedPrefUtil.saveCacheSharedPrf("is_open", data.is_open);
        SharedPrefUtil.saveCacheSharedPrf("plus_url", data.url);
        SharedPrefUtil.saveCacheSharedPrf("plus_index", data.url_index);
        if ("0".equals(data.push_on)) {//接收推送，1是，0否
            if (!JPushInterface.isPushStopped(Common.getApplicationContext())) {
                JPushInterface.stopPush(Common.getApplicationContext());
            }
        } else {
            JPushInterface.resumePush(Common.getApplicationContext());
        }
    }

    @Override
    public void isShowNew(CommonEntity data) {
        if ("1".equals(data.show) && !isEmpty(data.prize))
            initDialogs(data.prize);
    }

    @Override
    public void getPrize(CommonEntity data) {
        if (dialog_new != null) {
            mllayout_before.setVisibility(View.GONE);
            mllayout_after.setVisibility(View.VISIBLE);
            SpannableStringBuilder spannableStringBuilders = Common.changeTextSize(data.prize + getStringResouce(R.string.new_yuan), getStringResouce(R.string.new_yuan), 24);
            ntv_aOne.setText(spannableStringBuilders);
            ntv_get.setVisibility(View.GONE);
            ntv_use.setVisibility(View.VISIBLE);
            ntv_check.setVisibility(View.VISIBLE);
            ntv_use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Common.goGoGo(baseAct, data.type, data.item_id);
                    dialog_new.dismiss();
                }
            });
            ntv_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CouponListAct.startAct(baseAct);
                    dialog_new.dismiss();
                }
            });
        }
    }

    @Override
    public void setDiscoveryUnreadCount(CommonEntity data) {
        this.data = data;
        if (mtv_message_count != null) {
            if (data.count > 99) {
                mtv_message_count.setVisibility(View.VISIBLE);
                mtv_message_count.setText("99+");
            } else if (data.count <= 0) {
                mtv_message_count.setVisibility(View.GONE);
            } else {
                mtv_message_count.setVisibility(View.VISIBLE);
                mtv_message_count.setText(String.valueOf(data.count));
            }
        }
        if (discoverFrag != null) {
//            discoverFrag.initMessage(data);
        }
    }

    @Override
    public void showVoucherSuspension(ShowVoucherSuspension voucherSuspension) {
        if(mainPageFrag!=null){
            mainPageFrag.updateUserNewToast(voucherSuspension);
        }
    }

    @Override
    public void showFailureView(int request_code) {
        if (0 == request_code && dialog_new != null)
            dialog_new.dismiss();
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(DiscoveryLocationEvent event) {
        isShowGuide = SharedPrefUtil.getCacheSharedPrfBoolean("showGuide", false);
        currentLocation = event.location;
        currentImgWidth = event.imgWidth;
        if (currentLocation != null && currentImgWidth != 0 && !isShowGuide) {
            showGuideView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageReadSuccessEvent(MessageReadSuccessEvent event) {
        if(event.isSuccess){
            initMessage();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshMeData(MeLocationEvent event) {
        isShowGuideMe = SharedPrefUtil.getCacheSharedPrfBoolean("showGuideMe", false);
        currentLocation = event.location;
        currentImgWidth = event.imgWidth;
        if (currentLocation != null && currentImgWidth != 0 && !isShowGuideMe) {
            showGuideViewMe();
        }
    }

    public void showGuideViewMe() {
        currentLocation[0] = currentLocation[0] + currentImgWidth / 2;
        currentLocation[1] = currentLocation[1] + currentImgWidth / 2;
        MeGuideView guide_view = new MeGuideView(this);
        guide_view.setOnClickListener(v -> {
            guide_view.setVisibility(View.GONE);
        });
        guide_view.setImageLocation(currentLocation);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        guide_view.setLayoutParams(layoutParams);
        decorView.addView(guide_view);
        SharedPrefUtil.saveCacheSharedPrfBoolean("showGuideMe", true);
    }

    public void showGuideView() {
        int[] location2 = new int[2];
        miv_tab_discover.getLocationInWindow(location2);
        int bottomImgWidth = miv_tab_discover.getWidth();
        location2[0] = location2[0] + bottomImgWidth / 2;
        location2[1] = location2[1] + bottomImgWidth / 2;

        currentLocation[0] = currentLocation[0] + currentImgWidth / 2;
        currentLocation[1] = currentLocation[1] + currentImgWidth / 2;

        DiscoveryGuideView guide_view = new DiscoveryGuideView(this);
        guide_view.setOnClickListener(v -> {
            guide_view.setVisibility(View.GONE);
        });
        guide_view.setImageLocation(currentLocation, location2);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        guide_view.setLayoutParams(layoutParams);
        decorView.addView(guide_view);

        SharedPrefUtil.saveCacheSharedPrfBoolean("showGuide", true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && pMain != null) {
            pMain.isShowUserNewPersonPrize();
            pMain.showVoucherSuspension();
        }
    }

    /**
     * 选择商品信息
     *
     * @param sku
     * @param count
     */
    public void selectGoodsInfo(GoodsDeatilEntity.Sku sku, int count, boolean isAddcart,String goodId) {
        //LogUtil.zhLogW("=goodsCount=========="+goodsCount);
        if (isAddcart) {
            pMain.addCart(goodId, sku == null ? "" : sku.id, String.valueOf(count));
        }else {
            ConfirmOrderAct.startAct(this, goodId,
                    String.valueOf(count),
                    sku == null ? "" : sku.id,
                    ConfirmOrderAct.TYPE_GOODS_DETAIL);
        }
    }

    @Override
    public void addCart(String msg) {
        Common.staticToastAct(this,"添加购物车成功");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void suspensionRefresh(SuspensionRefresh suspensionRefresh) {
        if (suspensionRefresh.isRefresh && pMain != null) {
            pMain.showVoucherSuspension();
        }
    }
    /**
     * 处理网络请求
     */
    public void loadUserInfo() {
        if (!Common.isAlreadyLogin()) {
            return;
        }
        if (pMain!=null)
            pMain.loginUserInfo();
    }
}
