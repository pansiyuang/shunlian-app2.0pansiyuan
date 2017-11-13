package com.shunlian.app.ui.goods_detail;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.GoodsDetailPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IGoodsDetailView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.RollNumView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailAct extends SideslipBaseActivity implements IGoodsDetailView, View.OnClickListener {

    private FragmentManager supportFragmentManager;
    private GoodsDeatilFrag goodsDeatilFrag;

    @BindView(R.id.rnview)
    RollNumView rnview;

    @BindView(R.id.mrl_add_car)
    MyRelativeLayout mrl_add_car;

    @BindView(R.id.rl_rootview)
    RelativeLayout rl_rootview;

    private PathMeasure mPathMeasure;
    private boolean isStopAnimation;

    private float[] mCurrentPosition = new float[2];
    private MyImageView myImageView;

    public static void startAct(Context context){
        Intent intent = new Intent(context,GoodsDetailAct.class);
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
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        GoodsDetailPresenter goodsDetailPresenter = new GoodsDetailPresenter(this, this, "148");
        supportFragmentManager = getSupportFragmentManager();
        goodsDeatilFrag = new GoodsDeatilFrag();
        supportFragmentManager.beginTransaction().add(R.id.mfl_content,goodsDeatilFrag).commit();

        rnview.setMode(RollNumView.Mode.UP);
        rnview.setTextColor(Color.WHITE);
        rnview.setTextSize(10);
        rnview.setNumber(0);
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }

    /**
     * 轮播
     * @param pics
     */
    @Override
    public void banner(ArrayList<String> pics) {
        goodsDeatilFrag.setBanner(pics);
    }

    /**
     * 商品的标题和价格
     * 是否包邮
     * 销售量
     * 发货地点
     */
    @Override
    public void goodsInfo(String title, String price, String market_price, String free_shipping, String sales, String address) {
        goodsDeatilFrag.goodsInfo(title, price, market_price, free_shipping, sales, address);
    }

    /**
     *
     * @param is_new 是否新品
     * @param is_explosion 是否爆款
     * @param is_hot 是否热卖
     * @param is_recommend 是否推荐
     */
    @Override
    public void smallLabel(String is_new, String is_explosion, String is_hot, String is_recommend) {
        goodsDeatilFrag.smallLabel(is_new,is_explosion,is_hot,is_recommend);
    }

    /**
     * 优惠券
     *
     * @param vouchers
     */
    @Override
    public void voucher(ArrayList<GoodsDeatilEntity.Voucher> vouchers) {
        goodsDeatilFrag.setVoucher(vouchers);
    }

    /**
     * 店铺信息
     *
     * @param infos
     */
    @Override
    public void shopInfo(GoodsDeatilEntity.StoreInfo infos) {
        goodsDeatilFrag.setShopInfo(infos);
    }

    private int num;
    @Override
    public void onClick(View v) {
//        if (FastClickListener.isClickable(this)){
//            return;
//        }
        switch (v.getId()){
            case R.id.mrl_add_car:
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
                break;
        }
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
                System.out.println("mCurrentPosition[0]=="+mCurrentPosition[0]);
                System.out.println("mCurrentPosition[1]=="+mCurrentPosition[1]);
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

    @Override
    public void paramDialog(GoodsDeatilEntity goodsDeatilEntity) {
        goodsDeatilFrag.setParamDialog(goodsDeatilEntity);
    }

    @Override
    public void attributeDialog() {

    }
}
