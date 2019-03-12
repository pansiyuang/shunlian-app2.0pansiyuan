package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MergeGoodsAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.MergeOrderEntity;
import com.shunlian.app.presenter.MegerPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.timer.DDPDownTimerView;
import com.shunlian.app.utils.timer.OnCountDownTimerListener;
import com.shunlian.app.view.IMegerView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.ParamDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/8.
 */

public class MergeOrderActivity extends BaseActivity implements IMegerView, View.OnClickListener, OnCountDownTimerListener, MergeGoodsAdapter.OnGoodsBuyOnclickListener, ParamDialog.OnGoodsBuyCallBack {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.line_title)
    View line_title;

    @BindView(R.id.tv_meger_total)
    TextView tv_meger_total;

    @BindView(R.id.tv_meger_min)
    TextView tv_meger_min;

    @BindView(R.id.tv_to_shopcar)
    TextView tv_to_shopcar;

    @BindView(R.id.tv_prom_titile)
    TextView tv_prom_titile;

    @BindView(R.id.dowTimer)
    DDPDownTimerView dowTimer;

    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout layRefresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface neiEmpty;

    @BindView(R.id.tv_general)
    TextView tvGeneral;

    @BindView(R.id.tv_sales)
    TextView tvSales;

    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.miv_price)
    MyImageView mivPrice;

    @BindView(R.id.ll_price)
    LinearLayout llPrice;

    private String currentPromId;
    private MergeGoodsAdapter mAdapter;
    private List<GoodsDeatilEntity.Goods> mGoodsList;
    public MegerPresenter megerPresenter;
    public GridLayoutManager manager;
    public String[] timeUnit = {"天", "时", "分", "秒"};
    private int currentMode = 1;
    private ParamDialog paramDialog;
    private GoodsDeatilEntity.Goods currentGoods;

    public static void startAct(Context context, String needId) {
        Intent intent = new Intent(context, MergeOrderActivity.class);
        intent.putExtra("prom_id", needId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meger_order;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        tv_title.setText("凑单");
        line_title.setVisibility(View.GONE);
        NestedSlHeader header = new NestedSlHeader(this);
        layRefresh.setRefreshHeaderView(header);
        recyclerList.setNestedScrollingEnabled(false);

        currentPromId = getIntent().getStringExtra("prom_id");
        currentPromId = "nx_66";
        if (!TextUtils.isEmpty(currentPromId)) {
            megerPresenter = new MegerPresenter(this, this);
            megerPresenter.getMegerGoods(true, currentPromId, String.valueOf(currentMode));
        }
        mGoodsList = new ArrayList<>();

        manager = new GridLayoutManager(this, 2);
        recyclerList.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this, 5), false));
        recyclerList.setLayoutManager(manager);
        dowTimer.addLabelView(timeUnit);
        dowTimer.setTimeUnitTextColor(getColorResouce(R.color.value_8f8f8f));
    }

    @Override
    protected void initListener() {
        tv_to_shopcar.setOnClickListener(this);
        tvGeneral.setOnClickListener(this);
        tvSales.setOnClickListener(this);
        llPrice.setOnClickListener(this);
        layRefresh.setOnRefreshListener(() -> {
            if (megerPresenter != null) {
                megerPresenter.initPage();
                megerPresenter.getMegerGoods(true, currentPromId, String.valueOf(currentMode));
            }
        });

        recyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (megerPresenter != null) {
                            megerPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        dowTimer.setDownTimerListener(this);
        super.initListener();
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
            case R.id.tv_to_shopcar:
                MainActivity.startAct(this, "shoppingcar");
                break;
            case R.id.tv_general:
                setSelectStatus(1);
                break;
            case R.id.tv_sales:
                setSelectStatus(2);
                break;
            case R.id.ll_price:
                if (currentMode != 3) {
                    setSelectStatus(3);
                } else {
                    setSelectStatus(4);
                }
                break;
        }
        super.onClick(view);
    }

    @Override
    public void getMegerOrder(MergeOrderEntity mergeOrderEntity) {
        if (mergeOrderEntity.page == 1) {
            dowTimer.setDownTime(mergeOrderEntity.count_down);
            dowTimer.startDownTimer();
            tv_prom_titile.setText(mergeOrderEntity.prom_title);
            tv_meger_min.setText(mergeOrderEntity.prom_text);
            tv_meger_total.setText(getStringResouce(R.string.common_yuan) + mergeOrderEntity.total);
            mGoodsList.clear();
        }
        if (!isEmpty(mergeOrderEntity.list)) {
            mGoodsList.addAll(mergeOrderEntity.list);
        }
        if (mAdapter == null) {
            mAdapter = new MergeGoodsAdapter(this, mGoodsList);
            mAdapter.setOnGoodsBuyOnclickListener(this);
            recyclerList.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setPageLoading(mergeOrderEntity.page, mergeOrderEntity.total_page);
    }

    @Override
    public void refreshFinish() {
        if (layRefresh != null) {
            layRefresh.setRefreshing(false);
        }
    }

    @Override
    public void onFinish() {

    }

    /**
     * 设置选中状态
     *
     * @param mode 排序 1综合 2销量 3 价格升序 4价格降序
     */
    public void setSelectStatus(int mode) {
        switch (mode) {
            case 1:
                tvGeneral.setTextColor(getColorResouce(R.color.pink_color));
                tvSales.setTextColor(getColorResouce(R.color.value_484848));
                tvPrice.setTextColor(getColorResouce(R.color.value_484848));
                mivPrice.setImageResource(R.mipmap.img_jiage);
                break;
            case 2:
                tvGeneral.setTextColor(getColorResouce(R.color.value_484848));
                tvSales.setTextColor(getColorResouce(R.color.pink_color));
                tvPrice.setTextColor(getColorResouce(R.color.value_484848));
                mivPrice.setImageResource(R.mipmap.img_jiage);
                break;
            case 3:
                tvGeneral.setTextColor(getColorResouce(R.color.value_484848));
                tvSales.setTextColor(getColorResouce(R.color.value_484848));
                tvPrice.setTextColor(getColorResouce(R.color.pink_color));
                mivPrice.setImageResource(R.mipmap.img_jiage_shang);
                break;
            case 4:
                tvGeneral.setTextColor(getColorResouce(R.color.value_484848));
                tvSales.setTextColor(getColorResouce(R.color.value_484848));
                tvPrice.setTextColor(getColorResouce(R.color.pink_color));
                mivPrice.setImageResource(R.mipmap.img_jiage_xia);
                break;
        }
        currentMode = mode;
    }

    @Override
    public void OnItemBuy(GoodsDeatilEntity.Goods goods) {
        megerPresenter.getGoodsSku(goods.goods_id, goods.prom_id);
        currentGoods = goods;
    }

    @Override
    public void onAddCar(GoodsDeatilEntity.Sku sku, int count) {
        megerPresenter.addCart(currentGoods.goods_id, sku.id, String.valueOf(count), currentPromId);
    }

    @Override
    public void onBuyNow(GoodsDeatilEntity.Sku sku, int count) {

    }

    @Override
    public void showGoodsSku(GoodsDeatilEntity.Goods goods) {
        if (paramDialog == null) {
            paramDialog = new ParamDialog(this, goods);
            paramDialog.setOnGoodsBuyCallBack(this, true);
        } else {
            paramDialog.setParamGoods(goods);
        }
        paramDialog.show();
    }
}
