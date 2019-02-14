package com.shunlian.app.ui.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FuWuAdapter;
import com.shunlian.app.adapter.ShangAdapter;
import com.shunlian.app.adapter.ZiChanAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.MemberTeacherEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.eventbus_bean.DiscoveryLocationEvent;
import com.shunlian.app.eventbus_bean.MeLocationEvent;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.ui.MessageActivity;
import com.shunlian.app.newchat.util.ChatManager;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PersonalcenterPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.balance.BalanceDetailAct;
import com.shunlian.app.ui.balance.BalanceMainAct;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.ui.member.MemberPageActivity;
import com.shunlian.app.ui.member.ShoppingGuideActivity;
import com.shunlian.app.ui.my_profit.DetailOrderRecordAct;
import com.shunlian.app.ui.order.MyOrderAct;
import com.shunlian.app.ui.qr_code.QrCodeAct;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.ui.setting.PersonalDataAct;
import com.shunlian.app.ui.setting.SettingAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonalView;
import com.shunlian.app.widget.CompileScrollView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.ScrollTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.CenterKanner;
import com.shunlian.mylibrary.ImmersionBar;
import com.zh.chartlibrary.common.DensityUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 个人中心页面
 */

public class NewPersonalCenterFrag extends BaseFragment implements IPersonalView, View.OnClickListener, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.view_bg)
    View view_bg;
    @BindView(R.id.csv_out)
    CompileScrollView csv_out;
    @BindView(R.id.rLayout_title)
    MyRelativeLayout rLayout_title;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    @BindView(R.id.ntv_grow)
    NewTextView ntv_grow;
    @BindView(R.id.ntv_name)
    ScrollTextView ntv_name;
    @BindView(R.id.ntv_yue)
    NewTextView ntv_yue;
    @BindView(R.id.ntv_credit)
    NewTextView ntv_credit;
    @BindView(R.id.ntv_title)
    NewTextView ntv_title;
    @BindView(R.id.ntv_left)
    NewTextView ntv_left;
    @BindView(R.id.ntv_zhifu)
    NewTextView ntv_zhifu;
    @BindView(R.id.ntv_keti)
    NewTextView ntv_keti;
    @BindView(R.id.ntv_dai)
    NewTextView ntv_dai;
    @BindView(R.id.ntv_notice)
    NewTextView ntv_notice;
    @BindView(R.id.ntv_copy)
    NewTextView ntv_copy;
    @BindView(R.id.ntv_yaoqing)
    NewTextView ntv_yaoqing;
    @BindView(R.id.ntv_desc)
    NewTextView ntv_desc;
    @BindView(R.id.ntv_asset)
    NewTextView ntv_asset;
    @BindView(R.id.ntv_service)
    NewTextView ntv_service;
    @BindView(R.id.ntv_shang)
    NewTextView ntv_shang;
    @BindView(R.id.miv_avar)
    MyImageView miv_avar;
    @BindView(R.id.miv_level)
    MyImageView miv_level;
    @BindView(R.id.miv_pluss)
    MyImageView miv_pluss;
    @BindView(R.id.miv_kefu)
    MyTextView miv_kefu;
    @BindView(R.id.miv_levels)
    MyImageView miv_levels;
    @BindView(R.id.miv_shezhi)
    MyTextView miv_shezhi;
    @BindView(R.id.miv_huiyuan)
    MyImageView miv_huiyuan;
    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;
    @BindView(R.id.rv_zichan)
    RecyclerView rv_zichan;
    @BindView(R.id.rv_fuwu)
    RecyclerView rv_fuwu;
    @BindView(R.id.rv_shang)
    RecyclerView rv_shang;
    @BindView(R.id.kanner)
    CenterKanner kanner;
    @BindView(R.id.miv_isShow_data)
    MyImageView miv_isShow_data;
    @BindView(R.id.miv_yaoqing)
    MyImageView miv_yaoqing;
    @BindView(R.id.ntv_quanbu)
    NewTextView ntv_quanbu;
    @BindView(R.id.ntv_shouyixiangqing)
    NewTextView ntv_shouyixiangqing;
    @BindView(R.id.ntv_lijitixian)
    NewTextView ntv_lijitixian;
    @BindView(R.id.ntv_tixianmingxi)
    NewTextView ntv_tixianmingxi;
    @BindView(R.id.mllayout_daifukuan)
    MyLinearLayout mllayout_daifukuan;
    @BindView(R.id.mllayout_daishouhuo)
    MyLinearLayout mllayout_daishouhuo;
    @BindView(R.id.mllayout_daifahuo)
    MyLinearLayout mllayout_daifahuo;
    @BindView(R.id.mllayout_daipingjia)
    MyLinearLayout mllayout_daipingjia;
    @BindView(R.id.mllayout_shouhuo)
    MyLinearLayout mllayout_shouhuo;
    @BindView(R.id.mtv_payNum)
    MyTextView mtv_payNum;
    @BindView(R.id.mtv_refundNum)
    MyTextView mtv_refundNum;
    @BindView(R.id.mtv_remarkNum)
    MyTextView mtv_remarkNum;
    @BindView(R.id.mtv_sendNum)
    MyTextView mtv_sendNum;
    @BindView(R.id.ntv_names)
    NewTextView ntv_names;
    @BindView(R.id.mtv_receiveNum)
    MyTextView mtv_receiveNum;
    @BindView(R.id.mrlayout_plus)
    MyRelativeLayout mrlayout_plus;
    @BindView(R.id.rl_layout_top)
    MyRelativeLayout rl_layout_top;
    @BindView(R.id.ntv_check)
    NewTextView ntv_check;

    @BindView(R.id.line_anim)
    LinearLayout line_anim;

    @BindView(R.id.mtv_hint)
    MyTextView mtv_hint;

    public PersonalcenterPresenter personalcenterPresenter;
    private PersonalcenterEntity personalcenterEntity;
    private String invite_code;
    private MessageCountManager messageCountManager;
    private PersonalcenterEntity.MyAssets myAssets;
    private boolean isShowData = true;
    public final static String ASTERISK = "****";
    public final static String KEY = "person_isShow";
    private String managerUrl;
    private PromptDialog promptDialog;
    private String currentChatUserId;

    @BindView(R.id.miv_daoshi)
    MyTextView miv_daoshi;
    private boolean isShowGuideMe=false;//是否显示过引导 false,没有显示过
    private CommonDialogUtil commonDialogUtil;
    private boolean isHidden = false;
    //    private Timer outTimer;
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_mine_new, container, false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        } else {

        }
    }

    @Override
    public void onResume() {
        if (!isHidden()) {
            getPersonalcenterData();
        }
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(baseContext);
            if (messageCountManager.isLoad()) {
                messageCountManager.setTextCount(tv_msg_count);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        isShowGuideMe = SharedPrefUtil.getCacheSharedPrfBoolean("showGuideMe", false);
        if(!isHidden&&Common.isAlreadyLogin()&&isShowGuideMe&&personalcenterPresenter!=null){
            personalcenterPresenter.codeTeacherDetail();
        }
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.isHidden = hidden;
        if (!hidden) {
//            ImmersionBar.with(this).fitsSystemWindows(true)
//                    .statusBarColor(R.color.white)
//                    .statusBarDarkFont(true, 0.2f)
//                    .init();
            ImmersionBar.with(this).titleBar(rLayout_title, false).init();
            isShowGuideMe = SharedPrefUtil.getCacheSharedPrfBoolean("showGuideMe", false);
            if(Common.isAlreadyLogin()&&isShowGuideMe&&personalcenterPresenter!=null){
                personalcenterPresenter.codeTeacherDetail();
            }
        }
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        ImmersionBar.with(this).titleBar(rLayout_title, false).init();
        view_bg.setAlpha(0);
        ntv_names.setAlpha(0);
        commonDialogUtil = new CommonDialogUtil(baseActivity);
        personalcenterPresenter = new PersonalcenterPresenter(baseContext, this);
        int picWidth = Common.getScreenWidth((Activity) baseActivity) - TransformUtil.dip2px(baseActivity, 26);
        int height = ((picWidth/2) * 80)/ 165;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) miv_yaoqing.getLayoutParams();
        params.height=height;
        params.width=picWidth/2;
        RelativeLayout.LayoutParams paramss = (RelativeLayout.LayoutParams) miv_huiyuan.getLayoutParams();
        paramss.height=height;
        paramss.width=picWidth/2;
        if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))) {
            mrlayout_plus.setVisibility(View.VISIBLE);
            line_anim.setTranslationY(DensityUtil.dip2px(baseContext, 65));
        } else {
            mrlayout_plus.setVisibility(View.GONE);
            line_anim.setTranslationY(0);
        }

        miv_daoshi.post(() -> {
            int[] location = new int[2];
            miv_daoshi.getLocationInWindow(location);
            int imgWidth = miv_daoshi.getHeight();
            EventBus.getDefault().post(new MeLocationEvent(location, imgWidth));

        });


    }


    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private float lastY;
    private double lastY_damping = 0.2;
    private boolean isDownUp= false;
    private SpringSystem springSystem;
    private Spring spring;
    private boolean isStartAnim = false;
    @Override
    protected void initListener() {
        super.initListener();
        springSystem = SpringSystem.create();
        spring = springSystem.createSpring();
        spring.setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(100, 4));
        ntv_yaoqing.setOnClickListener(this);
        view_bg.setOnClickListener(this);
        ntv_copy.setOnClickListener(this);
        miv_yaoqing.setOnClickListener(this);
        miv_shezhi.setOnClickListener(this);
        miv_kefu.setOnClickListener(this);
        ntv_yue.setOnClickListener(this);
        rl_more.setOnClickListener(this);
        rl_layout_top.setOnClickListener(this);
        ntv_shouyixiangqing.setOnClickListener(this);
        ntv_lijitixian.setOnClickListener(this);
        ntv_tixianmingxi.setOnClickListener(this);
        ntv_quanbu.setOnClickListener(this);
        mllayout_daifukuan.setOnClickListener(this);
        mllayout_daishouhuo.setOnClickListener(this);
        ntv_name.setOnClickListener(this);
        miv_avar.setOnClickListener(this);
        ntv_check.setOnClickListener(this);
        mllayout_daifahuo.setOnClickListener(this);
        mllayout_daipingjia.setOnClickListener(this);
        mllayout_shouhuo.setOnClickListener(this);
        miv_huiyuan.setOnClickListener(this);
        ntv_left.setOnClickListener(this);
        miv_daoshi.setOnClickListener(this);
        csv_out.setOnScrollListener(new CompileScrollView.OnScrollListener() {
            @Override
//            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
            public void scrollCallBack(int y, int oldy) {
                if (y > 188) {
                    view_bg.setAlpha(1);
                    ntv_names.setAlpha(1);
                } else if (y > 0) {
                    float alpha = ((float) y) / 188;
                    view_bg.setAlpha(alpha);
                    ntv_names.setAlpha(alpha);
                } else {
                    view_bg.setAlpha(0);
                    ntv_names.setAlpha(0);
                    csv_out.setFocusable(false);
                }
            }
        });
        csv_out.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try {
                    if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))) {
                        LogUtil.longW(csv_out.getScrollY() + "");
                        if (csv_out.getScrollY() == 0) {
                            int action = event.getAction();
                            switch (action) {
                                case MotionEvent.ACTION_DOWN:
                                    lastY = event.getY();
                                    isDownUp = true;
                                    break;
                                case MotionEvent.ACTION_UP:
                                    spring.removeAllListeners();
                                    isDownUp = false;
                                    lastY = 0;
                                    handler.sendEmptyMessage(0);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    if (isDownUp) {
                                        float nowY = event.getY();
                                        if (nowY - lastY > 0) {
                                            int deltaY = (int) ((nowY - lastY) * lastY_damping);
                                            if (line_anim.getTranslationY() < DensityUtil.dip2px(baseContext, 65)) {
                                                line_anim.setTranslationY(deltaY);
                                            }
                                        } else {
                                            line_anim.setTranslationY(0);
                                        }
                                    } else {
                                        lastY = event.getY();
                                        isDownUp = true;
                                    }
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        } else {
                            if (line_anim.getTranslationY() != 0 && isDownUp) {
                                spring.removeAllListeners();
                                isDownUp = false;
                                lastY = 0;
                                handler.sendEmptyMessage(0);
                            }
                            return false;
                        }
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        });
    }


    public void getPersonalcenterData() {
        if (personalcenterPresenter != null && !MyOnClickListener.isFastRequest()) {
            personalcenterPresenter.getApiData();
        }
    }

    private boolean equals(String avatar, String avatar1) {
        if (!isEmpty(avatar) && avatar.equals(avatar1)) {
            return true;
        }
        return false;
    }


    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            spring.setCurrentValue(line_anim.getTranslationY());
            spring.setEndValue(0);
            spring.addListener(new SpringListener() {
                @Override
                public void onSpringUpdate(Spring spring) {
                    double value = spring.getCurrentValue();
                    if(!isDownUp&&line_anim!=null) {
                        line_anim.setTranslationY((int) value);
                    }
                    isStartAnim = true;
                }

                @Override
                public void onSpringAtRest(Spring spring) {
                }

                @Override
                public void onSpringActivate(Spring spring) {
                }

                @Override
                public void onSpringEndStateChange(Spring spring) {
                    isStartAnim =false;
                }
            });
        }
    };

    @Override
    public void getApiData(PersonalcenterEntity personalcenterEntity) {
        if (ntv_grow==null)
            return;
        if ("1".equals(SharedPrefUtil.getCacheSharedPrf("is_open", ""))) {
            if(line_anim.getTranslationY()>0) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                }, 1000);
            }
        }
//        personalcenterEntity.note="尊敬的客户，您好：你的账户因违规操作已被暂停部分服务！暂停时间：2018.01.04 18:00至2018.04.04 18:00。" +
//                "在此期间将暂停小店锁粉和小店收益。如需帮助，请联系平台客服处理！";
        if (isEmpty(personalcenterEntity.note)) {
            gone(mtv_hint);
        } else {
            visible(mtv_hint);
            mtv_hint.setText(personalcenterEntity.note);
        }

        SharedPrefUtil.saveSharedUserString("plus_role", personalcenterEntity.plus_role);
        if (!isEmpty(personalcenterEntity.invite_code))
            SharedPrefUtil.saveSharedUserString("invite_code", personalcenterEntity.invite_code);
        this.personalcenterEntity = personalcenterEntity;
        String avatar = SharedPrefUtil.getSharedUserString("personal_avatar", "null");
        if (!equals(avatar, personalcenterEntity.avatar) || miv_avar.getDrawable() == null) {
            SharedPrefUtil.saveSharedUserString("personal_avatar", personalcenterEntity.avatar);
            GlideUtils.getInstance().loadCircleAvar(baseContext, miv_avar, personalcenterEntity.avatar);
        }
        if (!isEmpty(personalcenterEntity.plus_expire_time))
            GlideUtils.getInstance().loadImageChang(baseContext, miv_pluss, personalcenterEntity.plus_expire_time);
        if (!isEmpty(personalcenterEntity.level)) {
            Bitmap bitmap = TransformUtil.convertNewVIP(getActivity(), personalcenterEntity.level);
            miv_level.setImageBitmap(bitmap);
            miv_level.setVisibility(View.VISIBLE);
        } else {
            miv_level.setVisibility(View.GONE);
        }
        ntv_grow.setText("成长值\n"+personalcenterEntity.grow_num);
        miv_levels.setVisibility(View.GONE);
        if (!isEmpty(personalcenterEntity.plus_role)) {
            int level = Integer.parseInt(personalcenterEntity.plus_role);
            if (level > 0)
                miv_levels.setVisibility(View.VISIBLE);
            if (level == 1) {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_dianzhu);
            } else if (level == 2) {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_zhuguan);
            } else {
                miv_levels.setImageResource(R.mipmap.img_plus_phb_jingli);
            }
        }
        ntv_name.setText(personalcenterEntity.nickname);
        ntv_names.setText(personalcenterEntity.nickname);
        invite_code = personalcenterEntity.invite_code;
        ntv_yaoqing.setText("邀请码：" + invite_code);
        ntv_desc.setText(personalcenterEntity.plus_meg);
        ntv_title.setText(personalcenterEntity.team_sales);
        if (!isEmpty(personalcenterEntity.diff)&&Float.parseFloat(personalcenterEntity.diff)>0){
            ntv_left.setText("销售额还差"+personalcenterEntity.diff+"元即可获得奖励");
        }else {
            ntv_left.setText(personalcenterEntity.diff_meg);
        }
        ntv_zhifu.setText(personalcenterEntity.zhifu);
        ntv_keti.setText(personalcenterEntity.all_sl_income);
        ntv_dai.setText(personalcenterEntity.estimateProfit);
        if (!isEmpty(personalcenterEntity.notice)){
            ntv_notice.setText(personalcenterEntity.notice);
            ntv_notice.setSelected(true);
            ntv_notice.setVisibility(View.VISIBLE);
        }else {
            ntv_notice.setVisibility(View.GONE);
        }
         myAssets=personalcenterEntity.myAssets;
         if (myAssets==null)
             return;
        if (myAssets.balance!=null&&!isEmpty(myAssets.balance.title)){
            ntv_yue.setText(myAssets.balance.title);
            ntv_yue.setVisibility(View.VISIBLE);
        }else {
            ntv_yue.setVisibility(View.GONE);
        }
        if (personalcenterEntity.credit!=null&&!isEmpty(personalcenterEntity.credit.title)){
            ntv_credit.setText(personalcenterEntity.credit.title);
            ntv_credit.setText(personalcenterEntity.credit.title+"\n"+personalcenterEntity.credit.value);
            ntv_credit.setVisibility(View.VISIBLE);
            ntv_credit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (personalcenterEntity.credit.url!=null)
                    Common.goGoGo(baseActivity,personalcenterEntity.credit.url.type,personalcenterEntity.credit.url.item_id);
                }
            });
        }else {
            ntv_credit.setVisibility(View.GONE);
        }
        isShowData = SharedPrefUtil.getCacheSharedPrfBoolean(KEY, true);
        changeState();
        managerUrl = personalcenterEntity.son_manage_url;
        if (isEmpty(myAssets.items)){
            rv_zichan.setVisibility(View.GONE);
        }else {
            rv_zichan.setVisibility(View.VISIBLE);
            rv_zichan.setNestedScrollingEnabled(false);
            ZiChanAdapter ziChanAdapter=new ZiChanAdapter(baseActivity,false,myAssets.items);
            rv_zichan.setLayoutManager(new GridLayoutManager(baseActivity,3));
            rv_zichan.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Common.goGoGo(baseActivity,myAssets.items.get(position).url.type,myAssets.items.get(position).url.item_id);
                }
            });
        }
        PersonalcenterEntity.MyService myService=personalcenterEntity.myService;
        if (isEmpty(myService.items)){
            rv_fuwu.setVisibility(View.GONE);
        }else {
            rv_fuwu.setVisibility(View.VISIBLE);
            rv_fuwu.setNestedScrollingEnabled(false);
            FuWuAdapter ziChanAdapter=new FuWuAdapter(baseActivity,false,myService.items);
            rv_fuwu.setLayoutManager(new GridLayoutManager(baseActivity,4));
            rv_fuwu.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if ("myshop".equals(myService.items.get(position).url.type)) {
                        if (Common.isPlus()) {
                            Common.goGoGo(baseActivity, myService.items.get(position).url.type, myService.items.get(position).url.item_id);
                        } else {
                            initHintDialog();
                        }
                    }else{
                        Common.goGoGo(baseActivity, myService.items.get(position).url.type, myService.items.get(position).url.item_id);
                    }
                }
            });
        }
        PersonalcenterEntity.OursSchool oursSchool=personalcenterEntity.oursSchool;
        if (isEmpty(oursSchool.items)){
            rv_shang.setVisibility(View.GONE);
        }else {
            rv_shang.setVisibility(View.VISIBLE);
            rv_shang.setNestedScrollingEnabled(false);
            ShangAdapter ziChanAdapter=new ShangAdapter(baseActivity,false,oursSchool.items);
            rv_shang.setLayoutManager(new GridLayoutManager(baseActivity,4));
            rv_shang.setAdapter(ziChanAdapter);
            ziChanAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Common.goGoGo(baseActivity,oursSchool.items.get(position).url.type,oursSchool.items.get(position).url.item_id);
                }
            });
        }
        if (myService.banner_list != null && myService.banner_list.size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < myService.banner_list.size(); i++) {
                strings.add(myService.banner_list.get(i).img);
                if (i >= myService.banner_list.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setRoundBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(baseActivity, myService.banner_list.get(position).link.type, myService.banner_list.get(position).link.item_id);
                        }
                    });
                }
            }
        } else {
            kanner.setVisibility(View.GONE);
        }
        mtv_refundNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.new_refund_num) <= 0) {
            mtv_refundNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.new_refund_num) > 99) {
            mtv_refundNum.setText("99+");
        } else {
            mtv_refundNum.setText(personalcenterEntity.new_refund_num);
        }
        mtv_remarkNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.un_comment_num) <= 0) {
            mtv_remarkNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.un_comment_num) > 99) {
            mtv_remarkNum.setText("99+");
        } else {
            mtv_remarkNum.setText(personalcenterEntity.un_comment_num);
        }
        mtv_sendNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_send_num) <= 0) {
            mtv_sendNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_send_num) > 99) {
            mtv_sendNum.setText("99+");
        } else {
            mtv_sendNum.setText(personalcenterEntity.order_un_send_num);
        }
        mtv_receiveNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_receive_num) <= 0) {
            mtv_receiveNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_receive_num) > 99) {
            mtv_receiveNum.setText("99+");
        } else {
            mtv_receiveNum.setText(personalcenterEntity.order_un_receive_num);
        }
        mtv_payNum.setVisibility(View.VISIBLE);
        if (Integer.parseInt(personalcenterEntity.order_un_pay_num) <= 0) {
            mtv_payNum.setVisibility(View.GONE);
        } else if (Integer.parseInt(personalcenterEntity.order_un_pay_num) > 99) {
            mtv_payNum.setText("99+");
        } else {
            mtv_payNum.setText(personalcenterEntity.order_un_pay_num);
        }
        ntv_shang.setText(oursSchool.title);
        ntv_service.setText(myService.title);
        ntv_asset.setText(myAssets.title);
    }

    @Override
    public void getUserId(String userId) {
        currentChatUserId = userId;
        jump2Chat(userId);
    }

    public void jump2Chat(String chatUserId){
        if (!isEmpty(chatUserId)) {
            ChatMemberEntity.ChatMember chatMember = new ChatMemberEntity.ChatMember();
            chatMember.nickname = "官方客服";
            chatMember.m_user_id = chatUserId;
            chatMember.type = "1";
            ChatManager.getInstance(baseActivity).init().MemberChat2Platform(chatMember);
        } else {
            personalcenterPresenter.getUserId();
        }
    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.ntv_yaoqing:
            case R.id.ntv_copy:
                Common.copyText(getActivity(), invite_code);
                //点击查看特权
                break;
            case R.id.miv_shezhi:
                SettingAct.startAct(baseContext);
                break;
            case R.id.ntv_quanbu:
                MyOrderAct.startAct(baseContext, 1);
                break;
            case R.id.rl_more:
                MessageActivity.startAct(baseActivity);
                break;
            case R.id.miv_kefu:
                jump2Chat(currentChatUserId);
                break;
            case R.id.ntv_yue:
                Constant.ISBALANCE = true;
                BalanceMainAct.startAct(baseContext, false);
//                Common.goGoGo(baseActivity,myAssets.balance.url.type,myAssets.balance.url.item_id);
                break;
            case R.id.miv_yaoqing:
                QrCodeAct.startAct(baseContext, managerUrl);
                break;
            case R.id.mllayout_daifukuan:
                MyOrderAct.startAct(baseContext, 2);
                break;
            case R.id.mllayout_daishouhuo:
                MyOrderAct.startAct(baseContext, 4);
                break;
            case R.id.mllayout_daifahuo:
                MyOrderAct.startAct(baseContext, 3);
                break;
            case R.id.mllayout_daipingjia:
                MyOrderAct.startAct(baseContext, 5);
                break;
            case R.id.mllayout_shouhuo:
                RefundAfterSaleAct.startAct(baseContext);
                break;
            case R.id.miv_huiyuan:
                MemberPageActivity.startAct(baseContext);
                break;
            case R.id.rl_layout_top:
            case R.id.ntv_check:
//                EggDetailAct.startAct(getContext());
//                mainActivity.myPlusClick();//old
                if (Common.isPlus()) {
//                    LogUtil.augusLogW("uiui-"+SharedPrefUtil.getCacheSharedPrf("plus_index", Constant.PLUS_ADD));
                    H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_index", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
//                    H5X5Act.startAct(baseContext, "https://www.baidu.com", H5Act.MODE_SONIC);
                } else {
//                    LogUtil.augusLogW("uiui-"+SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD));
                    H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
//                    H5X5Act.startAct(baseContext, "https://www.baidu.com", H5Act.MODE_SONIC);
                }
                //点击查看特权
                break;
            case R.id.miv_avar:
                PersonalDataAct.startAct(baseActivity);

                break;
            case R.id.ntv_shouyixiangqing:
                //订单详情
                DetailOrderRecordAct.startAct(baseActivity);
                break;
            case R.id.ntv_lijitixian:
                Constant.ISBALANCE = false;
                BalanceMainAct.startAct(baseContext, false);
                break;
            case R.id.ntv_tixianmingxi:
                Constant.ISBALANCE = false;
                BalanceDetailAct.startAct(baseActivity);
                break;
            case R.id.ntv_left:
                if(personalcenterEntity!=null&&!TextUtils.isEmpty(personalcenterEntity.salesInfo_url)) {
                    H5X5Act.startAct(baseContext, personalcenterEntity.salesInfo_url, H5X5Act.MODE_SONIC);
                }
                break;
            case R.id.ntv_name:
                ntv_name.startScroll();
                break;
            case R.id.miv_daoshi:
                ShoppingGuideActivity.startAct(baseActivity,null);
                break;
        }
    }
    public SpannableStringBuilder formatNumber(String number) {
        if (isEmpty(number)) return new SpannableStringBuilder("");
        char[] chars = number.toCharArray();
        return Common.changeTextSize(number, String.valueOf(chars[chars.length - 1]), 11);
    }

    private void changeState() {
        miv_isShow_data.setImageResource(!isShowData ? R.mipmap.icon_eyes_closes : R.mipmap.icon_eyes_opens);
        if (personalcenterEntity != null) {
            ntv_title.setText(!isShowData ? ASTERISK : personalcenterEntity.team_sales);
            ntv_zhifu.setText(!isShowData ? ASTERISK : personalcenterEntity.zhifu);
            ntv_keti.setText(!isShowData ? ASTERISK : personalcenterEntity.all_sl_income);
            ntv_dai.setText(!isShowData ? ASTERISK : personalcenterEntity.estimateProfit);
        }
    }

    @OnClick(R.id.miv_isShow_data)
    public void isShowData() {
        isShowData = !isShowData;
        changeState();
        SharedPrefUtil.saveCacheSharedPrfBoolean(KEY, isShowData);
//        TaskCenterAct.startAct(baseActivity);
    }

    public void initHintDialog() {
        if (promptDialog == null) {
            promptDialog = new PromptDialog(baseActivity);
        }
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.personal_ninhaibushi), getStringResouce(R.string.personal_goumai), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                H5X5Act.startAct(baseContext, SharedPrefUtil.getCacheSharedPrf("plus_url", Constant.PLUS_ADD), H5X5Act.MODE_SONIC);
            }
        }, getStringResouce(R.string.errcode_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }).show();
    }

    @Override
    public void showFailureView(int request_code) {
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void teacherCodeInfo(MemberTeacherEntity memberTeacherEntity) {
        if(memberTeacherEntity!=null&&memberTeacherEntity.type!=null){
            if("0".equals(memberTeacherEntity.type)){
                return;
            }
            if("2".equals(memberTeacherEntity.type)&&memberTeacherEntity.follow_from!=null&&memberTeacherEntity.follow_from.weixin!=null){
                commonDialogUtil.meTeachCommonDialog(memberTeacherEntity.follow_from.weixin, true, v -> {
                    Common.staticToastAct(baseActivity,"复制成功");
                    Common.copyTextNoToast(baseActivity,memberTeacherEntity.follow_from.weixin);
                    personalcenterPresenter.neverPop();
                    handlerWenxin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handlerWenxin.sendEmptyMessage(0);
                        }
                    },50);
                }, v -> {
                    commonDialogUtil.dialog_me_teach.dismiss();
                    personalcenterPresenter.neverPop();
                });
            }else if("1".equals(memberTeacherEntity.type)&&memberTeacherEntity.system_weixin!=null&&memberTeacherEntity.system_weixin.weixin!=null){
                commonDialogUtil.meTeachCommonDialog(memberTeacherEntity.system_weixin.weixin, false, v -> {
                    Common.staticToastAct(baseActivity,"复制成功");
                    Common.copyTextNoToast(baseActivity,memberTeacherEntity.system_weixin.weixin);
                    personalcenterPresenter.neverPop();
                    handlerWenxin.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handlerWenxin.sendEmptyMessage(0);
                        }
                    },50);
                }, v -> {
                    commonDialogUtil.dialog_me_teach.dismiss();
                    personalcenterPresenter.neverPop();
                });
            }
        }
    }

    private Handler handlerWenxin = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getWechatApi();
        }
    };
        /**
         * 跳转到微信
         */
        private void getWechatApi(){
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // TODO: handle exception
                Common.staticToastAct(baseActivity,"检查到您手机没有安装微信，请安装后使用该功能");
            }
    }

    @Override
    public void OnLoadFail() {

    }
}
