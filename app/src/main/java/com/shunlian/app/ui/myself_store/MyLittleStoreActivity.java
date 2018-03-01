package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AddGoodsAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.presenter.PersonStorePresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonStoreView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class MyLittleStoreActivity extends BaseActivity implements IPersonStoreView, View.OnClickListener{
    @BindView(R.id.recycler_goods)
    RecyclerView recycler_goods;

    @BindView(R.id.miv_add)
    MyImageView miv_add;

    @BindView(R.id.tv_add_count)
    TextView tv_add_count;

    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private static final int HIDE_THRESHOLD = 60;

    private int mScrolledDistance = 0;
    private boolean mControlsVisible = true;
    public PersonStorePresent mPresenter;
    private AddGoodsAdapter mAdapter;
    private List<GoodsDeatilEntity.Goods> goodsList;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, MyLittleStoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_little_store;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mPresenter = new PersonStorePresent(this, this);
        mPresenter.getPersonDetail();

        goodsList = new ArrayList<>();
        mAdapter = new AddGoodsAdapter(this, false,false, goodsList);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycler_goods.setLayoutManager(manager);
        recycler_goods.setAdapter(mAdapter);
        recycler_goods.setNestedScrollingEnabled(false);
        recycler_goods.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this, 12), true));
    }

    @Override
    protected void initListener() {
        miv_add.setOnClickListener(this);
        recycler_goods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (firstVisibleItem == 0) {
                    if (!mControlsVisible) {
                        onShow();
                        mControlsVisible = true;
                    }
                } else {
                    if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                        onHide();
                        mControlsVisible = false;
                        mScrolledDistance = 0;
                    } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                        onShow();
                        mControlsVisible = true;
                        mScrolledDistance = 0;
                    }
                }
                if ((mControlsVisible && dy > 0) || (!mControlsVisible && dy < 0)) {
                    mScrolledDistance += dy;
                }
            }
        });
        super.initListener();
    }

    public void onShow(){
        miv_add.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    public void onHide(){
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) miv_add.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        miv_add.animate().translationY(miv_add.getHeight() + fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_add:
                AddStoreGoodsAct.startAct(this);
                break;
        }
        super.onClick(view);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void showEmptyView(boolean isShowEmpty) {
        if (isShowEmpty) {
            nei_empty.setImageResource(R.mipmap.img_xiaodian_kongyemian).setText(getStringResouce(R.string.store_empty));
            nei_empty.setButtonText(getStringResouce(R.string.add_goods));
            nei_empty.setVisibility(View.VISIBLE);
            recycler_goods.setVisibility(View.GONE);
            nei_empty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   AddStoreGoodsAct.startAct(MyLittleStoreActivity.this);
                }
            });

            rl_bottom.setVisibility(View.GONE);
            tv_title_right.setVisibility(View.GONE);
            miv_add.setVisibility(View.GONE);
            tv_title.setText(getStringResouce(R.string.customize_my_store));
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_goods.setVisibility(View.VISIBLE);

            tv_title.setText(getStringResouce(R.string.decorate_my_store));
            tv_title_right.setVisibility(View.VISIBLE);
            rl_bottom.setVisibility(View.VISIBLE);
            miv_add.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getShopDetail(PersonShopEntity personShopEntity) {
        goodsList.clear();
        if (!isEmpty(personShopEntity.goods_list)) {
            goodsList.addAll(personShopEntity.goods_list);
            mAdapter.notifyDataSetChanged();

            showEmptyView(false);
        } else {
            showEmptyView(true);
        }
    }

    @Override
    public void getFairishNums(String count) {
        tv_add_count.setText(String.format(getStringResouce(R.string.add_some_goods), count));
    }
}
