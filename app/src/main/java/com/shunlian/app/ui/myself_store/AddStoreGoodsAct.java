package com.shunlian.app.ui.myself_store;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonLazyPagerAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.AddGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.category.BrandListAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IAddGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.ui.discover.ExperiencePublishActivity.FROM_EXPERIENCE_PUBLISH;

/**
 * Created by Administrator on 2018/2/27.
 */

public class AddStoreGoodsAct extends BaseActivity implements IAddGoodsView, View.OnClickListener, ViewPager.OnPageChangeListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_goods_search)
    TextView tv_goods_search;

    @BindView(R.id.tv_brand)
    TextView tv_brand;

    @BindView(R.id.tab_layout)
    TabLayout tab_layout;

    @BindView(R.id.vp_goods)
    ViewPager vp_goods;

    @BindView(R.id.tv_add_goods)
    TextView tv_add_goods;

    @BindView(R.id.tv_selectAll)
    TextView tv_selectAll;

    @BindView(R.id.tv_store_add)
    TextView tv_store_add;

    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;

    private String[] titles = {"全部", "推荐", "订单", "收藏", "购物车"};
    private static String[] fromList = {"ALL", "RECOMMEND", "ORDERS", "COLLECT", "SPCAR"};
    private static List<BaseFragment> goodsFrags;
    public static List<GoodsDeatilEntity.Goods> currentGoodsList;
    private int currentCount;
    private AddGoodsPresenter mPresenter;
    private StringBuffer currentGoodsIds = new StringBuffer();
    private String currentFlag = "store_goods";
    private PromptDialog promptDialog;
    private String currentFrom;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AddStoreGoodsAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_addstoregoods;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.add_goods));
        tv_title_right.setText(getStringResouce(R.string.SelectRecommendAct_sure));

        currentFrom = getIntent().getStringExtra("currentFrom");

        if (FROM_EXPERIENCE_PUBLISH.equals(currentFrom)) { //用于判别是否是发布心得
            showBottomLayout(false);
            tv_title_right.setVisibility(View.VISIBLE);
        }
        //设置tablayout标签的显示方式
        tab_layout.setTabMode(TabLayout.MODE_FIXED);
        //循环注入标签
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        for (String tab : titles) {
            tab_layout.addTab(tab_layout.newTab().setText(tab));
        }

        goodsFrags = new ArrayList<>();
        for (String from : fromList) {
            goodsFrags.add(AllGoodsFrag.getInstance(from));
        }
        vp_goods.setAdapter(new CommonLazyPagerAdapter(getSupportFragmentManager(), goodsFrags, titles));
        tab_layout.setupWithViewPager(vp_goods);

        vp_goods.setOffscreenPageLimit(5);
        mPresenter = new AddGoodsPresenter(this, this);
        mPresenter.getFairishNums();

        currentGoodsList = new ArrayList<>();
    }

    @Override
    protected void initListener() {
        tv_brand.setOnClickListener(this);
        tv_store_add.setOnClickListener(this);
        tv_goods_search.setOnClickListener(this);
        vp_goods.addOnPageChangeListener(this);
        tv_title_right.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void getFairishNums(String count, boolean isJump) {
        currentCount = Integer.valueOf(count);
        tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods), count));
        if (isJump) {
            MyLittleStoreActivity.startAct(this);
            finish();
        }
    }

    public String getCurrentFrom() {
        return currentFrom;
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_goods_search:
                clearSelectData();
                SearchGoodsActivity.startAct(this, "", currentFlag);
                break;
            case R.id.tv_brand:
                BrandListAct.startAct(this);
                break;
            case R.id.tv_store_add:
                if (currentGoodsList.size() == 0) {
                    Common.staticToast("请选择你要添加的商品");
                    return;
                }
                for (int i = 0; i < currentGoodsList.size(); i++) {
                    currentGoodsIds.append(currentGoodsList.get(i).goods_id);
                    if (i != currentGoodsList.size() - 1) {
                        currentGoodsIds.append(",");
                    }
                }
                mPresenter.addStoreGoods(currentGoodsIds.toString(), true);
                break;
            case R.id.tv_title_right:
                if (isEmpty(currentGoodsList)) {
                    Common.staticToast("请选择要选择的商品");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("goods", currentGoodsList.get(0));
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        super.onClick(view);
    }

    public static void upDateIndexPage(String from, int index) {
        int pageIndex = 0;
        for (int i = 0; i < fromList.length; i++) {
            if (fromList[i].equals(from)) {
                pageIndex = i;
                break;
            }
        }
        for (int i = 0; i < goodsFrags.size(); i++) {
            if (pageIndex == i) {
                continue;
            }
            ((AllGoodsFrag) goodsFrags.get(i)).updateIndex(index);
        }
    }

    public void clearSelectData() {
        if (currentGoodsList.size() == 0) {
            return;
        }
        currentGoodsList.clear();
        for (int i = 0; i < goodsFrags.size(); i++) {
            ((AllGoodsFrag) goodsFrags.get(i)).clearSelectData();
        }
    }

    public boolean canAddGoods() {
        if (currentCount <= 0) {
            if (promptDialog == null) {
                checkSelectDialog();
            }
            promptDialog.show();
            return false;
        }
        return true;
    }

    public void updateAddGoodsCount() {
        currentCount++;
        tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods), currentCount));
    }

    public void updateDelGoodsCount() {
        currentCount--;
        tv_add_goods.setText(String.format(getStringResouce(R.string.add_some_goods), currentCount));

    }

    public void checkSelectDialog() {
        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener("", "您的店铺商品数量已经达到上限，删除后才可以添加", getStringResouce(R.string.SelectRecommendAct_sure), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        }, getStringResouce(R.string.errcode_cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        });
    }

    public void showBottomLayout(boolean isShow) {
        if (isShow) {
            ll_bottom.setVisibility(View.VISIBLE);
            tv_add_goods.setVisibility(View.VISIBLE);
            vp_goods.setPadding(0, 0, 0, TransformUtil.dip2px(this, 48));
        } else {
            ll_bottom.setVisibility(View.GONE);
            tv_add_goods.setVisibility(View.GONE);
            vp_goods.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (FROM_EXPERIENCE_PUBLISH.equals(currentFrom)) {//用于判别是否是发布心得
            showBottomLayout(false);
            return;
        }
        AllGoodsFrag frag = (AllGoodsFrag) goodsFrags.get(position);
        showBottomLayout(frag.isShowEmpty());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
