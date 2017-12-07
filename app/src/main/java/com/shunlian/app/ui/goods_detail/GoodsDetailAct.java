package com.shunlian.app.ui.goods_detail;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoodsDetailView;
import com.shunlian.app.widget.FootprintDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.RollNumView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailAct extends SideslipBaseActivity implements IGoodsDetailView, View.OnClickListener {

    private GoodsDeatilFrag goodsDeatilFrag;
    public static final String FRAG_GOODS = GoodsDeatilFrag.class.getName();
    public static final String FRAG_COMMENT = CommentFrag.class.getName();
    public static final int GOODS_ID = 0;
    public static final int COMMENT_ID = 1;
    public static final int DETAIL_ID = 2;
    public static int CURRENT_ID = GOODS_ID;

    @BindView(R.id.rnview)
    RollNumView rnview;

    @BindView(R.id.mrl_add_car)
    MyRelativeLayout mrl_add_car;

    @BindView(R.id.rl_rootview)
    RelativeLayout rl_rootview;

    @BindView(R.id.miv_is_fav)
    MyImageView miv_is_fav;

    @BindView(R.id.mtv_buy_immediately)
    MyTextView mtv_buy_immediately;

    @BindView(R.id.mll_share)
    MyLinearLayout mll_share;

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.miv_close_share)
    MyImageView miv_close_share;

    @BindView(R.id.mll_goods)
    MyLinearLayout mll_goods;

    @BindView(R.id.mtv_goods)
    MyTextView mtv_goods;

    @BindView(R.id.view_goods)
    View view_goods;

    @BindView(R.id.mll_detail)
    MyLinearLayout mll_detail;

    @BindView(R.id.mtv_detail)
    MyTextView mtv_detail;

    @BindView(R.id.view_detail)
    View view_detail;

    @BindView(R.id.mll_comment)
    MyLinearLayout mll_comment;

    @BindView(R.id.mtv_comment)
    MyTextView mtv_comment;

    @BindView(R.id.view_comment)
    View view_comment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mll_item)
    MyLinearLayout mll_item;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.sv_mask)
    ScrollView sv_mask;
    private PathMeasure mPathMeasure;
    private boolean isStopAnimation;

    private float[] mCurrentPosition = new float[2];
    private MyImageView myImageView;
    private GoodsDetailPresenter goodsDetailPresenter;
    private String store_id;
    private GoodsDeatilEntity.Sku sku;
    private int goodsCount;
    private int bannerHeight;
    public int offset;
    private Map<String,BaseFragment> fragments;
    private CommentFrag commentFrag;
    private FootprintEntity mFootprintEntity;
    private FootprintDialog footprintDialog;
    private String goodsId;
    private int num;
    private boolean isAddcart = false;//是否加入购物车
    private boolean isNowBuy = false;//是否立即购买
    private String favId;

    public static void startAct(Context context,String goodsId){
        Intent intent = new Intent(context,GoodsDetailAct.class);
        intent.putExtra("goodsId",goodsId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_goods_detail;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mrl_add_car.setOnClickListener(this);
        miv_more.setOnClickListener(this);
        miv_close_share.setOnClickListener(this);
        mll_goods.setOnClickListener(this);
        mll_detail.setOnClickListener(this);
        mll_comment.setOnClickListener(this);
        miv_close.setOnClickListener(this);
        mtv_buy_immediately.setOnClickListener(this);
        miv_is_fav.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        defToolbar();
        goodsId = getIntent().getStringExtra("goodsId");
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        offset = toolbarParams.height;
        goodsDetailPresenter = new GoodsDetailPresenter(this, this, goodsId);
        fragments = new HashMap();
        goodsFrag();

        rnview.setMode(RollNumView.Mode.UP);
        rnview.setTextColor(Color.WHITE);
        rnview.setTextSize(10);
        rnview.setNumber(0);

        bannerHeight = DeviceInfoUtil.getDeviceWidth(this)
                - offset - ImmersionBar.getStatusBarHeight(this);
    }
    public void defToolbar(){
        immersionBar.titleBar(toolbar,false)
                .statusBarDarkFont(true, 0.2f)
                .addTag(GoodsDetailAct.class.getName())
                .init();
    }

    public void goodsFrag(){
        if (goodsDeatilFrag == null) {
            goodsDeatilFrag = new GoodsDeatilFrag();
            fragments.put(FRAG_GOODS,goodsDeatilFrag);
        }else {
            goodsDeatilFrag = (GoodsDeatilFrag) fragments.get(FRAG_GOODS);
        }
        mll_goods.setVisibility(View.VISIBLE);
        mll_goods.setEnabled(true);
        mll_detail.setVisibility(View.VISIBLE);
        mll_detail.setEnabled(true);
        switchContent(goodsDeatilFrag);
    }

    public void commentFrag(String id){
        if (commentFrag == null) {
            commentFrag = new CommentFrag();
            Bundle bundle = new Bundle();
            bundle.putString("goodsId",goodsId);
            commentFrag.setArguments(bundle);
            fragments.put(FRAG_COMMENT,commentFrag);
        }else {
            commentFrag = (CommentFrag) fragments.get(FRAG_COMMENT);
        }
        mll_goods.setVisibility(View.INVISIBLE);
        mll_goods.setEnabled(false);
        mll_detail.setVisibility(View.INVISIBLE);
        mll_detail.setEnabled(false);
        mtv_comment.setTextColor(getResources().getColor(R.color.new_text));
        view_comment.setVisibility(View.INVISIBLE);
        switchContent(commentFrag);
        commentFrag.setPresenter(goodsDetailPresenter,id);
        goodsDetailPresenter.commentList(GoodsDetailPresenter.COMMENT_EMPTY_CODE,
                GoodsDetailPresenter.COMMENT_FAILURE_CODE,true,goodsId,
                "ALL","1",String.valueOf(CommentFrag.pageSize),id);
    }

    public void setBgColor(int position, int totalDy) {
        ImmersionBar immersionBar = ImmersionBar.with(this)
                .addViewSupportTransformColor(toolbar, R.color.white);
        if (totalDy <= bannerHeight) {
            if (totalDy <= 0){
                totalDy = 0;
            }
            float alpha = (float) totalDy / bannerHeight;
            immersionBar.statusBarAlpha(alpha)
                    .addTag(GoodsDetailAct.class.getName())
                    .init();
            mll_item.setAlpha(alpha);

            float v = 1.0f - alpha * 2;
            if (v <= 0){
                v = alpha * 2 - 1;
                setImg(2,1);
            }else {
                setImg(1,2);
            }
            miv_close.setAlpha(v);
            miv_is_fav.setAlpha(v);
            miv_more.setAlpha(v);
        } else {
            setToolbar();
        }
    }

    public void setToolbar(){
        setImg(2,1);
        immersionBar.statusBarAlpha(1.0f)
                .addTag(GoodsDetailAct.class.getName())
                .init();
        mll_item.setAlpha(1.0f);
        miv_close.setAlpha(1.0f);
        miv_is_fav.setAlpha(1.0f);
        miv_more.setAlpha(1.0f);
    }
    /*
    替换fragment内容
     */
    public void switchContent(Fragment show) {
        if (show != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (!show.isAdded()) {
                ft.add(R.id.mfl_content, show);
            } else {
                ft.show(show);
            }

            if (fragments != null && fragments.size() > 0) {
                Iterator<String> keys = fragments.keySet().iterator();
                while (keys.hasNext()){
                    String next = keys.next();
                    BaseFragment baseFragment = fragments.get(next);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            ft.hide(baseFragment);
                        }
                    }
                }
            }
            ft.commit();
        }
    }

    private void setImg(int status,int oldStatus){
        if (status != oldStatus) {
            if (status == 1) {
                miv_close.setImageResource(R.mipmap.icon_more_fanhui);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
                }else {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
                }
                miv_more.setImageResource(R.mipmap.icon_more_gengduo);
            } else {
                miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_n);
                }else {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_h);
                }

                miv_more.setImageResource(R.mipmap.icon_more_n);
            }
        }
    }


    @Override
    public void showFailureView(int rquest_code) {
        if (rquest_code == GoodsDetailPresenter.COMMENT_FAILURE_CODE){
            if (commentFrag!= null)
                commentFrag.loadFailure();
        }
    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    /**
     * 商品详情数据
     */
    @Override
    public void goodsDetailData(GoodsDeatilEntity goodsDeatilEntity) {
        goodsDeatilFrag.setGoodsDetailData(goodsDeatilEntity);
        GoodsDeatilEntity.StoreInfo store_info = goodsDeatilEntity.store_info;
        if (store_info != null){
            store_id = store_info.store_id;
        }
    }


    /**
     * 是否喜爱该商品
     *
     * @param is_fav
     */
    @Override
    public void isFavorite(String is_fav) {
        favId = is_fav;
        if (goodsDeatilFrag != null){
            int item = goodsDeatilFrag.currentFirstItem;
            if (item > 0){
                if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)){
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_n);
                }else {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_h);
                }
            }else {
                if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)){
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
                }else {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
                }
            }
        }else {
            if (TextUtils.isEmpty(is_fav) || "0".equals(is_fav)){
                miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
            }else {
                miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
            }
        }

    }

    /**
     * 添加购物车
     *
     * @param msg
     */
    @Override
    public void addCart(String msg) {
        addCartAnim();
    }

    /**
     * 足迹列表
     *
     * @param footprintEntity
     */
    @Override
    public void footprintList(FootprintEntity footprintEntity) {
        mFootprintEntity = footprintEntity;
        if (footprintDialog == null) {
            footprintDialog = new FootprintDialog(this, mFootprintEntity);
        }
        footprintDialog.show();
    }

    /**
     * 评价列表数据
     *
     * @param entity
     */
    @Override
    public void commentListData(CommentListEntity entity) {
        if ("1".equals(entity.list.page)) {
            commentFrag.setCommentList(entity);
        }else {
            commentFrag.setCommentMoreList(entity);
        }
    }

    /*
   显示足迹列表
    */
    public void showFootprintList() {
        if (mFootprintEntity == null)
            goodsDetailPresenter.footprint();
    }

    /**
     * 请求关注店铺
     */
    public void followStore(){
        goodsDetailPresenter.followStore(store_id);
    }

    /**
     * 请求取消关注店铺
     */
    public void delFollowStore(){
        goodsDetailPresenter.delFollowStore(store_id);
    }

    @Override
    public void onClick(View v) {
//        if (FastClickListener.isClickable(this)){
//            return;
//        }
        switch (v.getId()){
            case R.id.mrl_add_car:
                isAddcart = true;
                if (sku == null){
                    goodsDeatilFrag.showParamDialog();
                }else {
                    goodsDetailPresenter.addCart(goodsId,sku.id,String.valueOf(goodsCount));
                }
                break;
            case R.id.miv_more:
                moreAnim();
                break;
            case R.id.miv_close_share:
                moreHideAnim();
                break;
            case R.id.mll_goods:
                if (mll_item.getAlpha() <= 0.2f){
                    return;
                }
                listTop();
                break;
            case R.id.mll_detail:
                if (mll_item.getAlpha() <= 0.2f){
                    return;
                }
                setToolbar();
                setTabBarStatue(DETAIL_ID);
                goodsDeatilFrag.setScrollPosition(2,offset);
                break;
            case R.id.mll_comment:
                if (mll_item.getAlpha() <= 0.2f){
                    return;
                }
                setToolbar();
                setTabBarStatue(COMMENT_ID);
                goodsDeatilFrag.setScrollPosition(1,offset);
                break;
            case R.id.miv_close:
                backOrder();
                break;
            case R.id.mtv_buy_immediately:
                isNowBuy = true;
                if (sku == null){
                    goodsDeatilFrag.showParamDialog();
                }else {
                    ConfirmOrderAct.startAct(this,goodsId,String.valueOf(goodsCount),sku.id);
                }
                break;
            case R.id.miv_is_fav:
                if (TextUtils.isEmpty(favId) || "0".equals(favId)){
                    goodsDetailPresenter.goodsFavAdd(goodsId);
                }else {
                    goodsDetailPresenter.goodsFavRemove(favId);
                }
                break;
        }
    }

    public void listTop() {
        setTabBarStatue(GOODS_ID);
        mll_item.setAlpha(0);
        immersionBar.statusBarAlpha(0f).addTag(GoodsDetailAct.class.getName()).init();
        goodsDeatilFrag.setScrollPosition(0,offset);
    }

    private void backOrder(){
        BaseFragment baseFragment = fragments.get(FRAG_COMMENT);
        if (baseFragment != null) {
            boolean visible = baseFragment.isVisible();
            if (visible) {
                setTabBarStatue(CURRENT_ID);
                goodsFrag();
            } else {
                finish();
            }
        }else {
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backOrder();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setTabBarStatue(int statue){
        //statue == 0 商品
        //statue == 1 评价
        //statue == 2 详情
        CURRENT_ID = statue;
        mtv_goods.setTextColor(statue == 0 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_goods.setVisibility(statue == 0 ? View.VISIBLE : View.INVISIBLE);

        mtv_comment.setTextColor(statue == 1 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_comment.setVisibility(statue == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_detail.setTextColor(statue == 2 ? getResources().getColor(R.color.pink_color)
                : getResources().getColor(R.color.new_text));
        view_detail.setVisibility(statue == 2 ? View.VISIBLE : View.INVISIBLE);

    }

    private void moreHideAnim() {
        immersionBar.getTag(GoodsDetailAct.class.getName()).init();
        mll_share.setVisibility(View.GONE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1);

        animation.setDuration(250);
        mll_share.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                sv_mask.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void moreAnim() {
        sv_mask.setVisibility(View.VISIBLE);
        immersionBar.statusBarColor(R.color.white).init();
        mll_share.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,-1,Animation.RELATIVE_TO_SELF,0);

        animation.setDuration(250);
        mll_share.setAnimation(animation);
    }

    private void addCartAnim() {
        if (isStopAnimation){
            return;
        }
        isStopAnimation = true;
        myImageView = new MyImageView(this);
        myImageView.setWHProportion(50,50);
        myImageView.setImageResource(R.mipmap.icon_login_logo);
        rl_rootview.addView(myImageView);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myImageView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        layoutParams.bottomMargin = TransformUtil.dip2px(this, 6);
        myImageView.setLayoutParams(layoutParams);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(100);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                parabolaAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        myImageView.setAnimation(alphaAnimation);
    }



    private void parabolaAnimation() {
        int[] startPoint = new int[2];
        myImageView.getLocationInWindow(startPoint);

        int[] endPoint = new int[2];
        rnview.getLocationInWindow(endPoint);

        // 开始绘制贝塞尔曲线
        Path path = new Path();
        // 移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startPoint[0], startPoint[1] - myImageView.getMeasuredHeight());
        // 使用二阶贝塞尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startPoint[0] - endPoint[0]) / 2 + endPoint[0], startPoint[1] - 250,
                endPoint[0] - rnview.getMeasuredHeight() / 4,
                endPoint[1]- myImageView.getMeasuredHeight() - rnview.getMeasuredHeight() / 2);

        mPathMeasure = new PathMeasure(path, false);
        // 属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        PropertyValuesHolder propertyValuesHolder1 = PropertyValuesHolder.ofFloat("parabola",0, mPathMeasure.getLength());

        PropertyValuesHolder propertyValuesHolder = PropertyValuesHolder.ofFloat("scale",1.0f,0.5f);

        ValueAnimator valueAnimator1 = ValueAnimator.ofPropertyValuesHolder(propertyValuesHolder,propertyValuesHolder1);
        valueAnimator1.setDuration(600);

        // 匀速线性插值器
        valueAnimator1.setInterpolator(new LinearInterpolator());
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (myImageView == null){
                    return;
                }
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue("parabola");
                // 获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                // mCurrentPosition此时就是中间距离点的坐标值
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
//                System.out.println("mCurrentPosition[0]=="+mCurrentPosition[0]);
//                System.out.println("mCurrentPosition[1]=="+mCurrentPosition[1]);
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                myImageView.setX(mCurrentPosition[0]);
                myImageView.setY(mCurrentPosition[1]);
                float scale = (float) animation.getAnimatedValue("scale");
                myImageView.setScaleX(scale);
                myImageView.setScaleY(scale);
            }
        });

        // 开始执行动画
        valueAnimator1.start();
        valueAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                if (rl_rootview != null) {
                    rl_rootview.removeView(myImageView);
                }
                myImageView = null;
                isStopAnimation = false;
                numAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void numAnimation(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.2f,0.9f,1.0f);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.setDuration(600);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = (float) animation.getAnimatedValue();
                if (rnview != null) {
                    rnview.setScaleX(scale);
                    rnview.setScaleY(scale);
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                num++;
                if (rnview != null) {
                    rnview.setNumber(num - 1);
                    rnview.setTargetNumber(num);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myImageView != null){
            myImageView.clearAnimation();
        }

        if (rnview != null){
            rnview.clearAnimation();
        }
    }

    /**
     * 选择商品信息
     * @param sku
     * @param count
     */
    public void selectGoodsInfo(GoodsDeatilEntity.Sku sku, int count) {
        this.sku = sku;
        goodsCount = count;
        if (isAddcart){
            isAddcart = false;
            goodsDetailPresenter.addCart(goodsId,sku.id,String.valueOf(goodsCount));
        }

        if (isNowBuy){
            isNowBuy = false;
            ConfirmOrderAct.startAct(this,goodsId,String.valueOf(goodsCount),sku.id);
        }
    }
}
