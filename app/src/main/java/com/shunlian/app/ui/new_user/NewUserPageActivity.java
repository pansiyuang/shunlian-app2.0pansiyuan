package com.shunlian.app.ui.new_user;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonPagerAdapter;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.UserNewDataEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.eventbus_bean.SuspensionRefresh;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.presenter.NewUserPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.UserBuyGoodsDialog;
import com.shunlian.app.utils.timer.HoneRedDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.INewUserPageView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class NewUserPageActivity extends BaseActivity implements INewUserPageView ,UserBuyGoodsDialog.CartDelGoodListen ,ShareGoodDialogUtil.OnShareBlogCallBack {
    private  List<AdUserEntity.AD> adList;
    private List<NewUserGoodsEntity.Goods> goodsList;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private CommonPagerAdapter commonLazyPagerAdapter;

    private CommonDialogUtil commonDialogUtil;
    /**
     * 最大0元够数量
     */
    public static final int MAX_COUNT = 3;
    public static int CURRENT_NUM = 0;
    private NewUserGoodsEntity newUserGoodsEntity;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_buy_num)
    TextView tv_buy_num;

    @BindView(R.id.tv_go_pay)
    TextView tv_go_pay;

    @BindView(R.id.tv_show_num)
    TextView tv_show_num;

    @BindView(R.id.tv_head)
    TextView tv_head;

    @BindView(R.id.img_share)
    ImageView img_share;

    @BindView(R.id.img_guize)
    ImageView img_guize;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.line_user_buy)
    LinearLayout line_user_buy;
    public static boolean isNew = false;

    @BindView(R.id.tv_new_user_title)
    TextView tv_new_user_title;
    @BindView(R.id.tv_new_user_time)
    HoneRedDownTimerView tv_new_user_time;
    @BindView(R.id.show_new_user_view)
    RelativeLayout show_new_user_view;
    @BindView(R.id.show_title_info)
    LinearLayout show_title_info;

    private String[] titlesOld = {"精选商品"};
    private List<BaseFragment> goodsFrags;
    private  NewUserPagePresenter mPresenter;
    private  UserBuyGoodsDialog userBuyGoodsDialog;
    private NewUserGoodsFrag userGoodFragFrist;
    private ShareInfoParam shareInfoParam;

    @BindView(R.id.lLayout_toast)
    LinearLayout lLayout_toast;
    @BindView(R.id.miv_icon)
    MyImageView miv_icon;
    @BindView(R.id.tv_info)
    TextView tv_info;
    private boolean isStop, isCrash;
    private boolean isPause = true;
    private Runnable runnableA, runnableB, runnableC;
    private Timer outTimer;
    private int mposition, size;
    private static Handler handler;


    public void beginToast() {
        if (isPause) {
            mposition = 0;
            isStop = false;
            mPresenter.getBubble();
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

    public void scrollListen(RecyclerView recyclerView, int dx, int dy){
        if(show_new_user_view.getVisibility()==View.VISIBLE){
            int valuew = TransformUtil.dip2px(this, 104);
            int valueh = TransformUtil.dip2px(this, 112);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(valuew, valueh);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            if (dy > 0) {
                layoutParams.setMargins(0, 0, -valuew / 2, TransformUtil.dip2px(this, 60));
            } else {
                layoutParams.setMargins(0, 0, 0, TransformUtil.dip2px(this, 60));
            }
            show_new_user_view.setLayoutParams(layoutParams);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPayData(UserPaySuccessEvent event) {
//        if (event!=null && event.isSuccess&&!event.isFragmet) {
//            CURRENT_NUM=0;
//            isEvent = true;
//            mPresenter.adlist();
//        }else if(event!=null && !event.isSuccess&&!event.isFragmet){
//            line_user_buy.setVisibility(View.VISIBLE);
//            tv_buy_num.setText("邀请记录");
//            tv_go_pay.setText("去邀请赚钱");
//        }
        if (event!=null && event.isSuccess) {
            resreshQuest();
            SuspensionRefresh eventRefresh = new SuspensionRefresh();
            eventRefresh.isRefresh = true;
            EventBus.getDefault().post(eventRefresh);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopToast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Common.isAlreadyLogin()) {
            beginToast();
        }
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
                    mPresenter.getBubble();
                }
            }
        };
        handler.postDelayed(runnableA, (7 * size + 1) * 1000);
    }

    public void startToast(final List<BubbleEntity.Content> datas) {
        if (outTimer != null) {
            outTimer.cancel();
        }
        outTimer = new Timer();
        try{
            outTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (mposition < datas.size()) {
                        runnableB = new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && baseAct.isDestroyed()) {
//                                throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
                                }else if (mposition < datas.size()) {
                                    LogUtil.augusLogW("mposition:" + mposition);
                                    lLayout_toast.setVisibility(View.VISIBLE);
                                    GlideUtils.getInstance().loadCircleAvar(baseAct,miv_icon,datas.get(mposition).avatar);
                                    tv_info.setText(datas.get(mposition).text);
                                    lLayout_toast.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (datas.get(mposition).url!=null)
                                                Common.goGoGo(baseAct,datas.get(mposition).url.type,datas.get(mposition).url.item_id);
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
                                }, (7 * size + 2) * 1000);
                            }
                        } else {
                            handler.post(runnableB);
                            runnableC = new Runnable() {
                                @Override
                                public void run() {
                                    if (!isStop) {
                                        lLayout_toast.setVisibility(View.GONE);
                                        mposition++;
                                    }
                                }
                            };
                            handler.postDelayed(runnableC, 5 * 1000);
                        }
                    }
                }
            }, 0, 7 * 1000);
        }catch (Exception e){

        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_new_user_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
//        isDialogNew = this.getIntent().getBooleanExtra("isDialogNew",false);
        shareInfoParam = new ShareInfoParam();
        commonDialogUtil = new CommonDialogUtil(this);
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        ImmersionBar.with(NewUserPageActivity.this).fitsSystemWindows(true)
                .statusBarColor(R.color.pink_color)
                .statusBarDarkFont(false, 0)
                .init();
        goodsList = new ArrayList<>();
        userBuyGoodsDialog = new UserBuyGoodsDialog(this);
        userBuyGoodsDialog.setCartDelGoodListen(this);
        mPresenter = new NewUserPagePresenter(this, this);
        goodsFrags = new ArrayList<>();
         commonLazyPagerAdapter = new CommonPagerAdapter(getSupportFragmentManager(), goodsFrags, titlesOld);
        viewpager.setAdapter(commonLazyPagerAdapter);
        if(Common.isAlreadyLogin()) {
            mPresenter.adlist();
            mPresenter.showVoucherSuspension();
        }else{
            showDialogView("");
        }
        img_guize.setOnClickListener(v -> {
            if(newUserGoodsEntity!=null&&!TextUtils.isEmpty(newUserGoodsEntity.h5_rule)){
                H5X5Act.startAct(NewUserPageActivity.this, newUserGoodsEntity.h5_rule, H5X5Act.MODE_SONIC);

            }
        });

    }

    /**
     * 刷选页面
     */
    public void resreshQuest(){
        mPresenter.adlist();
        mPresenter.showVoucherSuspension();
    }

    /**
     * 显示新人的dialog
     */
    private void showDialogView(String warn_txt){
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        commonDialogUtil.userNewShowDialog(new ICallBackResult<String>() {
            @Override
            public void onTagClick(String data) {
                if(!Common.isAlreadyLogin()){
                    Common.goGoGo(NewUserPageActivity.this, "login");
                }else if(Common.isAlreadyLogin()&&data.equals("立即领取")){
                    mPresenter.getvoucher();
                }else if(Common.isAlreadyLogin()&&data.equals("前往使用")){
                    ImmersionBar.with(NewUserPageActivity.this).fitsSystemWindows(true)
                            .statusBarColor(R.color.pink_color)
                            .statusBarDarkFont(false, 0)
                            .init();
                    if(commonDialogUtil.dialog_user_info!=null&&commonDialogUtil.dialog_user_info.isShowing()){
                        commonDialogUtil.dialog_user_info.dismiss();
                    }
                    beginToast();
                    mPresenter.adlist();
                    mPresenter.showVoucherSuspension();
                }
            }
        }, "立即领取",warn_txt);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewUserPageActivity.class);
        context.startActivity(intent);
    }
    public static void startAct(Context context,boolean isDialogNew) {
        Intent intent = new Intent(context, NewUserPageActivity.class);
        intent.putExtra("isDialogNew",isDialogNew);
        context.startActivity(intent);
    }
    @Override
    protected void initListener() {
        tv_buy_num.setOnClickListener(v -> {
            if(isNew) {
                mPresenter.cartlist(false);
            }else{
                NewInvitationActivity.startAct(NewUserPageActivity.this);
            }
          }
        );
        tv_go_pay.setOnClickListener(v -> {
            if(isNew) {
                goBuyUserGood();
            }else{
                mPresenter.getNewUserShareInfo();
            }
        });
        img_share.setOnClickListener(v -> {
            mPresenter.getNewUserShareInfo();
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        kanner.setOnItemClickL(position -> Common.goGoGo(baseAct, adList.get(position).link.type, adList.get(position).link.item_id));
        super.initListener();
    }

    public void initFrags(boolean isNew) {
        this.isNew  =  isNew;
        goodsFrags.clear();
        line_user_buy.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
        if(this.isNew) {
            tv_head.setText("新人专享");
            show_title_info.setVisibility(View.VISIBLE);
            userGoodFragFrist = NewUserGoodsFrag.getInstance(titlesOld[0], "1",isNew);
            goodsFrags.add(userGoodFragFrist);
        }else{
            tv_head.setText("邀粉专区");
            userGoodFragFrist = NewUserGoodsFrag.getInstance(titlesOld[0], "1",isNew);
            goodsFrags.add(userGoodFragFrist);
            tv_buy_num.setText("邀请记录");
            tv_go_pay.setText("去邀请赚钱");
            show_title_info.setVisibility(View.GONE);
            line_user_buy.setVisibility(View.VISIBLE);
        }
        commonLazyPagerAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void showFailureView(int request_code) {
        if (666==request_code){
            size = 2;
            startTimer();
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    public void bannerList(List<AdUserEntity.AD> adList,boolean isNew) {
        if (kanner==null)
            return;
        this.adList = adList;
        initFrags(isNew);
        List<String> strings = new ArrayList<>();
        for(AdUserEntity.AD list:adList) {
            strings.add(list.ad_img);
        }
        kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
        kanner.setBanner(strings);
    }

    @Override
    public void goodCartList(List<NewUserGoodsEntity.Goods> goodsList,boolean isGoBuy) {
        if(goodsList!=null&&goodsList.size()>0) {
            this.goodsList.clear();
            this.goodsList.addAll(goodsList);
            if(!isGoBuy) {
                userBuyGoodsDialog.showGoodsInfo(this.goodsList);
            } else{
                goBuyUserGood();
            }

            CURRENT_NUM = this.goodsList.size();
            updateCartNum();

        }
    }

    private void goBuyUserGood(){
        if(CURRENT_NUM==0){
            Common.staticToast("购物车没有商品信息");
            return;
        }
        if(MAX_COUNT>CURRENT_NUM) {
            CommonDialogUtil promptDialog = new CommonDialogUtil(this);
            promptDialog.defaultCommonDialog("你已经领取了" + CURRENT_NUM + "件商品\n还可以再免费领" + (MAX_COUNT - CURRENT_NUM) + "件商品哦", "去支付", view -> {
                promptDialog.dismiss();
                ConfirmOrderAct.startAct(this,ConfirmOrderAct.TYPE_NEW_USER_PAGE);
            }, "再逛逛", view -> promptDialog.dismiss());
        }else{
            ConfirmOrderAct.startAct(this,ConfirmOrderAct.TYPE_NEW_USER_PAGE);
        }

    }


    @Override
    public void delCart(String cid) {
        if(goodsList==null||goodsList.size()==0){
            return;
        }
        for (NewUserGoodsEntity.Goods goods:goodsList){
            if(goods.id.equals(cid)){
                userGoodFragFrist.updateCartGoods(goods);
                goodsList.remove(goods);
                break;
            }
        }
        CURRENT_NUM = this.goodsList.size();
        userBuyGoodsDialog.updateItemData(this.goodsList);
        updateCartNum();
    }

    @Override
    public void getvoucher(UserNewDataEntity userNewDataEntity) {
        if(commonDialogUtil!=null&&commonDialogUtil.dialog_user_info!=null&&commonDialogUtil.dialog_user_info.isShowing()){
            if(userNewDataEntity.isNew) {
                SuspensionRefresh event = new SuspensionRefresh();
                event.isRefresh = true;
                EventBus.getDefault().post(event);
                TextView tv_new_submit = commonDialogUtil.dialog_user_info.findViewById(R.id.tv_new_submit);
                TextView ntv_user_page_price = commonDialogUtil.dialog_user_info.findViewById(R.id.ntv_user_page_price);
                tv_new_submit.setText("前往使用");
                ntv_user_page_price.setText(getStringResouce(R.string.common_yuan) + userNewDataEntity.prize);
            }else{
                getOldMessage("您不是新用户哦，无法领取该优惠券",0);
            }
        }
    }

    @Override
    public void getOldMessage(String message,int code) {
        commonDialogUtil.userOldShowDialog(new ICallBackResult<String>() {
            @Override
            public void onTagClick(String data) {
                if(commonDialogUtil.dialog_user_old!=null&&commonDialogUtil.dialog_user_old.isShowing()){
                    commonDialogUtil.dialog_user_old.dismiss();
                }
                if(commonDialogUtil.dialog_user_info!=null&&commonDialogUtil.dialog_user_info.isShowing()){
                    commonDialogUtil.dialog_user_info.dismiss();
                }
                if(data.equals("home")){
                    MainActivity.startAct(NewUserPageActivity.this, "mainPage");
                    finish();
                }else{
                    ImmersionBar.with(NewUserPageActivity.this).fitsSystemWindows(true)
                            .statusBarColor(R.color.pink_color)
                            .statusBarDarkFont(false, 0)
                            .init();
                    mPresenter.adlist();
                    mPresenter.showVoucherSuspension();
                    beginToast();
                }
            }
        }, message, code);
    }

    @Override
    public void showVoucherSuspension(ShowVoucherSuspension voucherSuspension) {
        if(voucherSuspension.suspensionShow.equals("1")&&Common.isAlreadyLogin()){
            tv_new_user_title.setText(voucherSuspension.suspension.prize);
            show_new_user_view.setVisibility(View.VISIBLE);
            if(voucherSuspension.suspension.finish>0) {
                tv_new_user_time.setVisibility(View.VISIBLE);
                tv_new_user_time.cancelDownTimer();
                tv_new_user_time.setDownTime(voucherSuspension.suspension.finish);
                tv_new_user_time.startDownTimer();
                tv_new_user_time.setDownTimerListener(new OnCountDownTimerListener() {
                    @Override
                    public void onFinish() {
                        tv_new_user_time.cancelDownTimer();
                        show_new_user_view.setVisibility(View.GONE);
                        mPresenter.adlist();
                    }
                });
            }else{
                tv_new_user_time.setVisibility(View.GONE);
            }
        }else{
             show_new_user_view.setVisibility(View.GONE);
        }
    }

    /**
     * 更新购物车数量
     */
    public void addCartOne(){
        CURRENT_NUM++;
        updateCartNum();
    }
    /**
     * 更新购物车数量
     */
    public void initCartNum(int currentNum,int show,NewUserGoodsEntity newUserGoodsEntity){
        this.CURRENT_NUM = currentNum;
        this.newUserGoodsEntity = newUserGoodsEntity;
        updateCartNum();
        if(show!=1&&isNew){
            showDialogView(newUserGoodsEntity.warn_txt);
        }
    }
    /**
     * 更新购物车数量
     */
    public void updateCartNum(){
        tv_buy_num.setText("已购商品("+CURRENT_NUM+")");

       if(CURRENT_NUM==0){
           line_user_buy.setVisibility(View.GONE);
       }else{
           line_user_buy.setVisibility(isNew?View.VISIBLE:View.GONE);
       }
        if(!isNew) {
            tv_buy_num.setText("邀请记录");
            tv_go_pay.setText("去邀请赚钱");
            line_user_buy.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void delGood(NewUserGoodsEntity.Goods goods) {
        CommonDialogUtil promptDialog = new CommonDialogUtil(this);
            promptDialog.defaultCommonDialog("亲！您真的确定要\n删除这件商品吗？", "确定", view -> {
                promptDialog.dismiss();
                mPresenter.deletecart(goods.id);

            }, "取消", view -> promptDialog.dismiss());
    }

    @Override
    public void gotoBuy() {
        goBuyUserGood();
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {

    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        shareInfoParam.shareLink =baseEntity.data.link;
        shareInfoParam.title =baseEntity.data.title;
        shareInfoParam.desc =baseEntity.data.content;
        shareInfoParam.img =baseEntity.data.imgdefalt;
        shareInfoParam.special_img_url =baseEntity.data.imgdefalt;
        shareGoodDialogUtil.shareGoodDialog(shareInfoParam,false,false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if(tv_new_user_time!=null){
            tv_new_user_time.cancelDownTimer();
        }
        super.onDestroy();
    }
}
