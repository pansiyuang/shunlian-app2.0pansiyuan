package com.shunlian.app.ui.new_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.eventbus_bean.UserPaySuccessEvent;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.presenter.MyPagePresenter;
import com.shunlian.app.presenter.NewUserGoodsPresenter;
import com.shunlian.app.presenter.NewUserPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.discover_new.AttentionMemberActivity;
import com.shunlian.app.ui.discover_new.CommonBlogFrag;
import com.shunlian.app.ui.discover_new.DiscoverMsgActivity;
import com.shunlian.app.ui.discover_new.FansListActivity;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.ui.setting.AutographAct;
import com.shunlian.app.ui.setting.PersonalDataAct;
import com.shunlian.app.ui.zxing_code.ZXingDemoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.UserBuyGoodsDialog;
import com.shunlian.app.view.IMyPageView;
import com.shunlian.app.view.INewUserGoodsView;
import com.shunlian.app.view.INewUserPageView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.Kanner;
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
    private boolean isEvent =false;
    private  CommonLazyPagerAdapter commonLazyPagerAdapter;
    /**
     * 最大0元够数量
     */
    public static final int MAX_COUNT = 3;
    public static int CURRENT_NUM = 0;

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tv_left)
    TextView tv_left;

    @BindView(R.id.tv_right)
    TextView tv_right;

    @BindView(R.id.line_left)
    View line_left;

    @BindView(R.id.line_right)
    View line_right;

    @BindView(R.id.ll_left)
    RelativeLayout ll_left;

    @BindView(R.id.ll_right)
    RelativeLayout ll_right;

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

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.show_title_info)
    LinearLayout show_title_info;

    @BindView(R.id.line_user_buy)
    LinearLayout line_user_buy;
    public static boolean isNew = false;

    private String[] titles = {"新人专享", "精选商品"};
    private String[] titlesOld = {"精选商品"};
    private List<BaseFragment> goodsFrags;
    private boolean isDefault = true;
    private  NewUserPagePresenter mPresenter;
    private  UserBuyGoodsDialog userBuyGoodsDialog;
    private NewUserGoodsFrag userGoodFragFrist;
    private NewUserGoodsFrag userGoodFragEnd;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshPayData(UserPaySuccessEvent event) {
        if (event!=null && event.isSuccess&&!event.isFragmet) {
            CURRENT_NUM=0;
            isEvent = true;
            mPresenter.adlist();
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
        beginToast();
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
        outTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mposition < datas.size()) {
                    runnableB = new Runnable() {
                        @Override
                        public void run() {
                            if (mposition < datas.size()) {
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
    }

//    public static void startAct(Context context, String memberId) {
//        Intent intent = new Intent(context, NewUserPageActivity.class);
//        intent.putExtra("member_id", memberId);
//        context.startActivity(intent);
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_new_user_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        EventBus.getDefault().register(this);
        shareInfoParam = new ShareInfoParam();
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
         commonLazyPagerAdapter = new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titlesOld);
        viewpager.setAdapter(commonLazyPagerAdapter);
        setTabMode(isDefault);
        mPresenter.adlist();
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, NewUserPageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        ll_left.setOnClickListener(v -> {
            isDefault = true;
            setTabMode(isDefault);
        });
        ll_right.setOnClickListener(v -> {
            isDefault = false;
            setTabMode(isDefault);
        });
        tv_buy_num.setOnClickListener(v -> mPresenter.cartlist(false));
        tv_go_pay.setOnClickListener(v -> {
            goBuyUserGood();
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
                if (position == 0) {
                    isDefault = true;
                    setTabMode(isDefault);
                } else {
                    isDefault = false;
                    setTabMode(isDefault);
                }
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
        if(this.isNew) {
            tv_head.setText("新人专享");
            tv_right.setText("精选商品");
            tv_right.setTextColor(getColorResouce(R.color.text_gray2));
            show_title_info.setVisibility(View.VISIBLE);
            for (int i = 0; i < titles.length; i++) {
                if (i == 0) {
                    userGoodFragFrist = NewUserGoodsFrag.getInstance(titles[i], "1",isNew);
                    goodsFrags.add(userGoodFragFrist);
                } else {
                    userGoodFragEnd = NewUserGoodsFrag.getInstance(titles[i], "2",isNew);
                    goodsFrags.add(userGoodFragEnd);
                }
            }
        }else{
            setTabMode(true);
            tv_head.setText("邀粉专区");
            tv_right.setText("商品列表");
            show_title_info.setVisibility(View.VISIBLE);
            ll_left.setVisibility(View.GONE);
            userGoodFragEnd = NewUserGoodsFrag.getInstance(titlesOld[0], "1",isNew);
            userGoodFragEnd.updateTypeUser("1",isNew);
            goodsFrags.add(userGoodFragEnd);
        }
        commonLazyPagerAdapter.notifyDataSetChanged();
        if(isEvent){
            EventBus.getDefault().post(new UserPaySuccessEvent(true,true));
        }
    }


    public void setTabMode(boolean isDefault) {
        if (isDefault) {
            tv_show_num.setTextColor(getColorResouce(R.color.pink_color));
            tv_left.setTextColor(getColorResouce(R.color.pink_color));
            tv_right.setTextColor(getColorResouce(R.color.value_484848));
            line_left.setVisibility(View.VISIBLE);
            line_right.setVisibility(View.GONE);
            viewpager.setCurrentItem(0);
        } else {
            tv_right.setTextColor(getColorResouce(R.color.pink_color));
            tv_left.setTextColor(getColorResouce(R.color.value_484848));
            tv_show_num.setTextColor(getColorResouce(R.color.value_484848));
            line_right.setVisibility(View.VISIBLE);
            line_left.setVisibility(View.GONE);
            viewpager.setCurrentItem(1);
        }
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
                ConfirmOrderAct.startAct(this);
            }, "再逛逛", view -> promptDialog.dismiss());
        }else{
            ConfirmOrderAct.startAct(this);
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
    public void initCartNum(int currentNum){
        this.CURRENT_NUM = currentNum;
        updateCartNum();
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
        shareInfoParam.desc =baseEntity.data.desc;
        shareInfoParam.img =baseEntity.data.imgdefalt;
        shareInfoParam.special_img_url =baseEntity.data.imgdefalt;
        shareInfoParam.isSpecial = true;
        shareGoodDialogUtil.shareGoodDialog(shareInfoParam,false,false);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
