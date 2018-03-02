package com.shunlian.app.ui.sign;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SignGoodsAdapter;
import com.shunlian.app.adapter.StoreBabyAdapter;
import com.shunlian.app.bean.CheckInRespondEntity;
import com.shunlian.app.bean.CheckInStateEntity;
import com.shunlian.app.presenter.PSignIn;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISignInView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

public class SignInAct extends BaseActivity implements View.OnClickListener, ISignInView {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_titles)
    MyTextView mtv_titles;

    @BindView(R.id.mtv_scores)
    MyTextView mtv_scores;

    @BindView(R.id.mtv_score)
    MyTextView mtv_score;

    @BindView(R.id.mrlayout_news)
    MyRelativeLayout mrlayout_news;

    @BindView(R.id.miv_sign)
    MyImageView miv_sign;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.msv_sign)
    MyScrollView msv_sign;

    private AnimationDrawable signAnimation;
    private boolean isSign, isSigned, isFirst = true;
    private PSignIn pSignIn;
    private Handler handler;
    private SignGoodsAdapter signGoodsAdapter;


    public static void startAct(Context context) {
        Intent intent = new Intent(context, SignInAct.class);
//        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_sign_in;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_sign:
                if (!isSigned && !isSign) {
                    pSignIn.sign();
                }
                break;
        }
    }

    public void anim(final String scores) {
        final float[] alpha = {0};
        final Timer timeAnim = new Timer();
        timeAnim.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        alpha[0] = alpha[0] + 0.03f;
                        mtv_score.setAlpha(alpha[0]);
                    }
                });
            }
        }, 0, 50);
        if (handler == null) {
            handler = new Handler();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isSign = false;
                isSigned = true;
                signAnimation.stop();
                timeAnim.cancel();
                mtv_score.setAlpha(0);
                mtv_scores.setText(scores);
            }
        }, 1650);
        isSign = true;
        signAnimation.stop();
        signAnimation.start();
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_sign.setOnClickListener(this);
        msv_sign.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom){
                    if (pSignIn != null) {
                        pSignIn.refreshBaby();
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        pSignIn = new PSignIn(this, this);
        mtv_title.setText("签到");
        mtv_score.setAlpha(0);
        mrlayout_news.setVisibility(View.GONE);
        miv_sign.setBackgroundResource(R.drawable.sign_in_animation);
        signAnimation = (AnimationDrawable) miv_sign.getBackground();
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void signCallBack(CheckInRespondEntity checkInRespondEntity) {
        SpannableStringBuilder spannableStringBuilder = Common.changeTextSize("+" + checkInRespondEntity.credit, checkInRespondEntity.credit, 42);
        mtv_score.setText(spannableStringBuilder);
        anim(checkInRespondEntity.total_credit);
    }


    public void initRv(final List<CheckInStateEntity.GoodsList.MData> mDataList, int allPage, final int page){
        if (signGoodsAdapter == null) {
            signGoodsAdapter = new SignGoodsAdapter(this, true, mDataList);
            GridLayoutManager babyManager = new GridLayoutManager(this, 2);
            rv_goods.setLayoutManager(babyManager);
            rv_goods.setNestedScrollingEnabled(false);
//            rv_goods.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(this, 5), TransformUtil.dip2px(this, 5), true));
            rv_goods.setAdapter(signGoodsAdapter);
            signGoodsAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(SignInAct.this,mDataList.get(position).id);
                }
            });
        } else {
            signGoodsAdapter.notifyDataSetChanged();
        }
        signGoodsAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void setApiData(CheckInStateEntity checkInStateEntity, List<CheckInStateEntity.GoodsList.MData> mDataList) {
        if (isFirst) {
            if ("yes".equals(checkInStateEntity.today_is_singed)) {
                isSign = true;
                miv_sign.setImageResource(R.mipmap.img_33);
            }
            mtv_scores.setText(checkInStateEntity.credit1);
            mtv_titles.setText(checkInStateEntity.title);
        }
        initRv(mDataList,checkInStateEntity.goodslist.total_page,
                checkInStateEntity.goodslist.page);
        isFirst = false;
    }
}
