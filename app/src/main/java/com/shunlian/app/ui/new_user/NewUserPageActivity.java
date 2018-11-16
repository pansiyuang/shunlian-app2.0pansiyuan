package com.shunlian.app.ui.new_user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
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
import com.shunlian.app.utils.GlideUtils;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class NewUserPageActivity extends BaseActivity implements INewUserPageView ,UserBuyGoodsDialog.CartDelGoodListen ,ShareGoodDialogUtil.OnShareBlogCallBack {
    private StringBuffer orderGoodsIds = new StringBuffer();//提交订单的id

    private  List<AdUserEntity.AD> adList;
    private List<NewUserGoodsEntity.Goods> goodsList;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    /**
     * 最大0元够数量
     */
    private static final int MAX_COUNT = 3;
    private int CURRENT_NUM = 0;

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

    @BindView(R.id.img_share)
    ImageView img_share;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.show_title_info)
    LinearLayout show_title_info;

    @BindView(R.id.line_user_buy)
    LinearLayout line_user_buy;

    private String[] titles = {"新人专享", "精选商品"};
    private String[] titlesOld = {"精选商品"};
    private List<BaseFragment> goodsFrags;
    private boolean isDefault = true;
    private  NewUserPagePresenter mPresenter;
    private  UserBuyGoodsDialog userBuyGoodsDialog;
    private NewUserGoodsFrag userGoodFragFrist;
    private NewUserGoodsFrag userGoodFragEnd;
    private ShareInfoParam shareInfoParam;
    public static void startAct(Context context, String memberId) {
        Intent intent = new Intent(context, NewUserPageActivity.class);
        intent.putExtra("member_id", memberId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_new_user_layout;
    }
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        shareGoodDialogUtil = new ShareGoodDialogUtil(this);
        ImmersionBar.with(NewUserPageActivity.this).fitsSystemWindows(true)
                .statusBarColor(R.color.pink_color)
                .statusBarDarkFont(false, 0)
                .init();
        goodsList = new ArrayList<>();
        userBuyGoodsDialog = new UserBuyGoodsDialog(this);
        userBuyGoodsDialog.setCartDelGoodListen(this);
        mPresenter = new NewUserPagePresenter(this, this);

        setTabMode(isDefault);
        mPresenter.adlist();
    }

    public static void startAct(Activity activity) {
        Intent intent = new Intent(activity, NewUserPageActivity.class);
         activity.startActivity(intent);
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
            if(CURRENT_NUM==0){
                Common.staticToast("购物车没有商品信息");
                return;
            }
            ConfirmNewUserOrderAct.startAct(NewUserPageActivity.this, ConfirmOrderAct.TYPE_CART);
        });
        img_share.setOnClickListener(v -> {
           shareGoodDialogUtil.shareGoodDialog(shareInfoParam,false,false);
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
        goodsFrags = new ArrayList<>();
        if(isNew) {
            line_user_buy.setVisibility(View.VISIBLE);
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
            line_user_buy.setVisibility(View.GONE);
            show_title_info.setVisibility(View.VISIBLE);
            ll_left.setVisibility(View.GONE);
            userGoodFragEnd = NewUserGoodsFrag.getInstance(titlesOld[0], "2",isNew);
            goodsFrags.add(userGoodFragEnd);
        }
        viewpager.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titlesOld));
        viewpager.setOffscreenPageLimit(goodsFrags.size());
    }


    public void setTabMode(boolean isDefault) {
        if (isDefault) {
            tv_left.setTextColor(getColorResouce(R.color.pink_color));
            tv_right.setTextColor(getColorResouce(R.color.value_484848));
            line_left.setVisibility(View.VISIBLE);
            line_right.setVisibility(View.GONE);
            viewpager.setCurrentItem(0);
        } else {
            tv_right.setTextColor(getColorResouce(R.color.pink_color));
            tv_left.setTextColor(getColorResouce(R.color.value_484848));
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
                getOrderIds();
                ConfirmNewUserOrderAct.startAct(this,  ConfirmOrderAct.TYPE_CART);
            }

            CURRENT_NUM = this.goodsList.size();
            updateCartNum();

        }
    }

    /**
     * 结算id
     */
    public void getOrderIds() {
        orderGoodsIds.setLength(0);
        if (goodsList != null && goodsList.size() != 0) {
            for (int i = 0; i < goodsList.size(); i++) {
                orderGoodsIds.append(goodsList.get(i).id);
                if (i != goodsList.size() - 1) {
                    orderGoodsIds.append(",");
                }
            }
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
    }
    @Override
    public void delGood(NewUserGoodsEntity.Goods goods) {
      mPresenter.deletecart(goods.id);
    }

    @Override
    public void shareSuccess(String blogId, String goodsId) {

    }
}
