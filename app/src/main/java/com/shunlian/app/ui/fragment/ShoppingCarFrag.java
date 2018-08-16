package com.shunlian.app.ui.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DisabledGoodsAdapter;
import com.shunlian.app.adapter.ProbabyLikeGoodsAdapter;
import com.shunlian.app.adapter.RecommendAdapter;
import com.shunlian.app.adapter.ShopCarStoreAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ProbabyLikeGoodsEntity;
import com.shunlian.app.bean.RecommendEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.presenter.ShopCarPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.ui.confirm_order.MegerOrderActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.view.IShoppingCarView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.RecyclerDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 购物车界面
 */

public class ShoppingCarFrag extends BaseFragment implements IShoppingCarView, View.OnClickListener, ShopCarStoreAdapter.OnEnableChangeListener {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.rl_price)
    RelativeLayout rl_price;

    @BindView(R.id.ll_total_edit)
    LinearLayout ll_total_edit;

    @BindView(R.id.miv_total_select)
    MyImageView miv_total_select;

    @BindView(R.id.tv_total_price)
    TextView tv_total_price;

    @BindView(R.id.tv_total_discount)
    TextView tv_total_discount;

    @BindView(R.id.tv_edit_freight)
    TextView tv_edit_freight;

    @BindView(R.id.btn_total_complete)
    Button btn_total_complete;

    @BindView(R.id.tv_toal_favorite)
    TextView tv_toal_favorite;

    @BindView(R.id.tv_toal_del)
    TextView tv_toal_del;

    @BindView(R.id.expand_shoppingcar)
    ExpandableListView expand_shoppingcar;

    @BindView(R.id.line_total)
    View line_total;

    @BindView(R.id.rl_total)
    RelativeLayout rl_total;

    private HashMap<String, Boolean> editMap; //用来记录店铺分组编辑状态
    private View rootView;
    private View footView;
    private View emptyView;
    private View probabyView;
    private View recommendView;
    private ShopCarPresenter shopCarPresenter;
    private ShopCarStoreAdapter shopCarStoreAdapter;
    private ProbabyLikeGoodsAdapter goodsAdapter;
    private RecommendAdapter recommendAdapter;
    private ShoppingCarEntity mCarEntity;
    private Unbinder mUnbinder;
    private FooterHolderView footerHolderView;
    private ProbabyFooterHolderView probabyHolderView;
    private EmptyHolderView emptyHolderView;
    private RecommendGoodsHolderView recommendGoodsHolderView;
    private String isCheckAll; //用来记录是否全选了
    private StringBuffer orderGoodsIds = new StringBuffer();//提交订单的id
    private String disGoodsIds;//失效订单的id
    private RecyclerDialog recyclerDialog;
    private List<ProbabyLikeGoodsEntity.Goods> probabyGoods;
    private List<RecommendEntity.Goods> recommenGoods;

    private int scrollPos;
    private int scrollTop;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        rootView = inflater.inflate(R.layout.frag_shoppingcar, container, false);
        footView = inflater.inflate(R.layout.foot_shoppingcar_disable, container, false);
        probabyView = inflater.inflate(R.layout.foot_probayby_goods, container, false);
        emptyView = inflater.inflate(R.layout.shoppingcar_empty, container, false);
        recommendView = inflater.inflate(R.layout.foot_recommend_goods, container, false);
        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    protected void initViews() {
        super.initViews();
        miv_close.setVisibility(View.GONE);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title.setText(baseContext.getResources().getText(R.string.shopping_car));
        tv_title_right.setText(baseContext.getResources().getText(R.string.edit));

        footerHolderView = new FooterHolderView(footView);
        probabyHolderView = new ProbabyFooterHolderView(probabyView);
        emptyHolderView = new EmptyHolderView(emptyView);
        recommendGoodsHolderView = new RecommendGoodsHolderView(recommendView);

        probabyGoods = new ArrayList<>();
        recommenGoods = new ArrayList<>();

        footView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT)); //添加这句话 防止报错
        probabyView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        emptyView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        recommendView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));

        expand_shoppingcar.addFooterView(footView);
        expand_shoppingcar.addFooterView(probabyView);
        expand_shoppingcar.addFooterView(emptyView);
        expand_shoppingcar.addFooterView(recommendView);
        recyclerDialog = new RecyclerDialog(baseContext);
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        editMap = new HashMap<>();
        shopCarPresenter = new ShopCarPresenter(baseContext, this);
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_title_right.setOnClickListener(this);
        btn_total_complete.setOnClickListener(this);
        miv_total_select.setOnClickListener(this);
        tv_toal_favorite.setOnClickListener(this);
        tv_toal_del.setOnClickListener(this);
        expand_shoppingcar.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // scrollPos记录当前可见的List顶端的一行的位置
                    scrollPos = expand_shoppingcar.getFirstVisiblePosition();
                }
                if (!isEmpty(mCarEntity.enabled)) {
                    View v = expand_shoppingcar.getChildAt(0);
                    scrollTop = (v == null) ? 0 : v.getTop();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public void getShoppingCarData() {
        if (shopCarPresenter != null && !MyOnClickListener.isFastRequest()) {
            shopCarPresenter.initShopData();
        }
    }

    @Override
    public void showFailureView(int rquest_code) {
    }

    @Override
    public void showDataEmptyView(int rquest_code) {
    }


    @Override
    public void OnShoppingCarEntity(ShoppingCarEntity shoppingCarEntity) {
        this.mCarEntity = shoppingCarEntity;
        String totalPrice = getString(R.string.total);
        tv_total_price.setText(String.format(totalPrice, mCarEntity.total_amount));

        String discountStr = getString(R.string.already_discount);
        tv_total_discount.setText(String.format(discountStr, mCarEntity.total_reduce));

        String totalCount = getString(R.string.balance_accounts);
        btn_total_complete.setText(String.format(totalCount, mCarEntity.total_count));

        shopCarStoreAdapter = new ShopCarStoreAdapter(baseContext, mCarEntity.enabled, this);
        expand_shoppingcar.setAdapter(shopCarStoreAdapter);
        shopCarStoreAdapter.setOnEnableChangeListener(this);

        //默认展开
        if (!isEmpty(mCarEntity.enabled)) {
            for (int i = 0; i < mCarEntity.enabled.size(); i++) {
                expand_shoppingcar.expandGroup(i);
                editMap.put(mCarEntity.enabled.get(i).store_id, false);
            }
        }

        //屏蔽父布局点击事件
        expand_shoppingcar.setOnGroupClickListener((expandableListView, view, i, l) -> true);

        if (!isEmpty(mCarEntity.disabled)) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            footerHolderView.recycle_disable.setNestedScrollingEnabled(false);
            footerHolderView.recycle_disable.setLayoutManager(linearLayoutManager);
            footerHolderView.recycle_disable.setAdapter(new DisabledGoodsAdapter(baseContext, false, mCarEntity.disabled));
            footerHolderView.ll_rootView.setVisibility(View.VISIBLE);
        } else {
            footerHolderView.ll_rootView.setVisibility(View.GONE);
        }

        isCheckAll = getCheckAll();
        disGoodsIds = mCarEntity.disabled_ids;
        if ("1".equals(isCheckAll)) {
            miv_total_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        } else {
            miv_total_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }

        getOrderIds(mCarEntity.checked_cartId);//拼接商品id

        if (isEmpty(mCarEntity.enabled) && isEmpty(mCarEntity.disabled)) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
        }

        if (!isEmpty(mCarEntity.enabled)) {
            expand_shoppingcar.setSelectionFromTop(scrollPos, scrollTop);
        }
    }

    @Override
    public void onResume() {
        if (!isHidden()) {
            rl_price.setVisibility(View.VISIBLE);
            ll_total_edit.setVisibility(View.GONE);
            tv_title_right.setText(getString(R.string.edit));
            getShoppingCarData();
        }
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_title_right:
                if (getString(R.string.edit).equals(tv_title_right.getText())) {
                    setEditMode(true);
                } else {
                    setEditMode(false);
                }
                shopCarStoreAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_toal_del:
                if (TextUtils.isEmpty(orderGoodsIds.toString())) {
                    return;
                }
                shopCarPresenter.cartRemove(orderGoodsIds.toString());
                break;
            case R.id.tv_toal_favorite://收藏
                if (TextUtils.isEmpty(orderGoodsIds.toString())) {
                    return;
                }
                shopCarPresenter.removetofav(orderGoodsIds.toString());
                break;
            case R.id.btn_total_complete: //结算
                if (TextUtils.isEmpty(orderGoodsIds.toString())) {
                    return;
                }
                ConfirmOrderAct.startAct(baseContext, orderGoodsIds.toString(), ConfirmOrderAct.TYPE_CART);
                break;
            case R.id.miv_total_select:
                if ("1".equals(isCheckAll)) {
                    shopCarPresenter.checkCartGoods("0", "0");
                } else {
                    shopCarPresenter.checkCartGoods("0", "1"); //storeId= 0为设置全选接口传参
                }
                break;
        }
    }

    public void setEditMode(boolean edit) {
        if (mCarEntity == null || mCarEntity.enabled == null || mCarEntity.enabled.size() == 0) {
            return;
        }
        for (ShoppingCarEntity.Enabled enable : mCarEntity.enabled) {
            enable.isEditAll = edit;
            enable.isEditGood = edit;
            editMap.put(enable.store_id, edit); //修改所有店铺状态
        }

        if (!edit) {
            Common.hideKeyboard(tv_title_right);
            rl_price.setVisibility(View.VISIBLE);
            ll_total_edit.setVisibility(View.GONE);
            tv_title_right.setText(getString(R.string.edit));
        } else {
            rl_price.setVisibility(View.GONE);
            ll_total_edit.setVisibility(View.VISIBLE);
            tv_title_right.setText(getString(R.string.RegisterTwoAct_finish));
        }
    }

    public void showVouchersDialog(int position) {
        if (!isEmpty(mCarEntity.enabled.get(position).store_voucher)) {
            List<GoodsDeatilEntity.Voucher> voucherList = mCarEntity.enabled.get(position).store_voucher;
            recyclerDialog.setVoucheres(voucherList);
            recyclerDialog.setOnVoucherCallBack(new RecyclerDialog.OnVoucherCallBack() {
                @Override
                public void onVoucherSelect(GoodsDeatilEntity.Voucher voucher) {
                    shopCarPresenter.getVoucher(voucher.voucher_id);
                }
            });
            recyclerDialog.show();
        }
    }

    //修改商品数量
    @Override
    public void OnChangeCount(String goodsId, int count) {
        shopCarPresenter.editCar(goodsId, String.valueOf(count), null, null, null);
    }

    //修改商品属性
    @Override
    public void OnChangeSku(String goodsId, String skuId) {
        shopCarPresenter.editCar(goodsId, null, skuId, null, null);
    }

    //修改商品选择
    @Override
    public void OnChangeCheck(String goodsId, String isCheck) {
        shopCarPresenter.editCar(goodsId, null, null, null, isCheck);
    }

    //修改商品编辑
    @Override
    public void OnChangeEdit(String storeId, boolean isEdit) {
        try {
            editMap.put(storeId, isEdit);
            shopCarStoreAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnGoodsDel(String goodsId) {
        shopCarPresenter.cartRemove(goodsId);
    }

    //商品选择活动
    @Override
    public void OnChangePromotion(String goodsId, String promoId) {
        shopCarPresenter.editCar(goodsId, null, null, promoId, null);
    }

    //店铺勾选
    @Override
    public void OnStoreCheck(String storeId, String isSelect) {
        shopCarPresenter.checkCartGoods(storeId, isSelect);
    }

    @Override
    public void OnMegerOrder(String needId) {
        MegerOrderActivity.startAct(baseContext, needId);
    }

    @Override
    public void OnEditEntity(ShoppingCarEntity shoppingCarEntity) {
        this.mCarEntity = shoppingCarEntity;
        String totalPrice = getString(R.string.total);
        tv_total_price.setText(String.format(totalPrice, mCarEntity.total_amount));

        String discountStr = getString(R.string.already_discount);
        tv_total_discount.setText(String.format(discountStr, mCarEntity.total_reduce));

        String totalCount = getString(R.string.balance_accounts);
        btn_total_complete.setText(String.format(totalCount, mCarEntity.total_count));

        shopCarStoreAdapter.setEnables(mCarEntity.enabled, editMap);
        shopCarStoreAdapter.notifyDataSetChanged();

        isCheckAll = getCheckAll();
        if ("1".equals(isCheckAll)) {
            miv_total_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        } else {
            miv_total_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }

        getOrderIds(mCarEntity.checked_cartId);//拼接商品id
        disGoodsIds = mCarEntity.disabled_ids;

        if (mCarEntity.disabled == null || mCarEntity.disabled.size() == 0) {
            footerHolderView.ll_rootView.setVisibility(View.GONE);
        } else {
            footerHolderView.ll_rootView.setVisibility(View.VISIBLE);
        }

        if (isEmpty(mCarEntity.enabled) && isEmpty(mCarEntity.disabled)) {
            showEmptyView(true);
        } else {
            showEmptyView(false);
        }
    }

    @Override
    public void OnGetVoucher(GoodsDeatilEntity.Voucher voucher) {
        //领取成功
        Common.staticToast("领取成功");
        if (recyclerDialog != null) {
            if ("1".equals(voucher.is_get)) {
                recyclerDialog.getVoucherSuccess(voucher.voucher_id, voucher.is_get);
            }
        }
    }

    @Override
    public void OnGetProbabyGoods(List<ProbabyLikeGoodsEntity.Goods> goodsList) {
        probabyGoods.clear();
        if (!isEmpty(goodsList)) {
            probabyGoods.addAll(goodsList);
            if (goodsAdapter == null) {
                goodsAdapter = new ProbabyLikeGoodsAdapter(getActivity(), probabyGoods);
                probabyHolderView.recycler_list.setAdapter(goodsAdapter);
            } else {
                goodsAdapter.notifyDataSetChanged();
            }
            probabyHolderView.ll_rootView.setVisibility(View.VISIBLE);
        } else {
            probabyHolderView.ll_rootView.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnGetWantGoodsList(List<RecommendEntity.Goods> goodsList) {
        recommenGoods.clear();
        if (!isEmpty(goodsList)) {
            recommenGoods.addAll(goodsList);
            if (recommendAdapter == null) {
                recommendAdapter = new RecommendAdapter(getActivity(), recommenGoods);
                recommendGoodsHolderView.recycler_list.setAdapter(recommendAdapter);
            } else {
                recommendAdapter.notifyDataSetChanged();
            }
            recommendGoodsHolderView.ll_rootView.setVisibility(View.VISIBLE);
            recommendAdapter.setOnItemClickListener((view, position) -> {
                GoodsDetailAct.startAct(getActivity(), recommenGoods.get(position).id);
            });
        } else {
            recommendGoodsHolderView.ll_rootView.setVisibility(View.GONE);
        }
    }

    public void getOrderIds(List<String> orderList) {
        orderGoodsIds.setLength(0);
        if (orderList != null && orderList.size() != 0) {
            for (int i = 0; i < orderList.size(); i++) {
                orderGoodsIds.append(orderList.get(i));
                if (i != orderList.size() - 1) {
                    orderGoodsIds.append(",");
                }
            }
        }
    }

    private void showEmptyView(boolean isEmpty) {
        if (!isEmpty) {
            line_total.setVisibility(View.VISIBLE);
            rl_total.setVisibility(View.VISIBLE);
            tv_title_right.setVisibility(View.VISIBLE);
            emptyHolderView.ll_rootView.setVisibility(View.GONE);
        } else {
            line_total.setVisibility(View.GONE);
            rl_total.setVisibility(View.GONE);
            tv_title_right.setVisibility(View.GONE);
            probabyGoods.clear();
            probabyHolderView.ll_rootView.setVisibility(View.GONE);
            emptyHolderView.ll_rootView.setVisibility(View.VISIBLE);
        }
    }

    public class FooterHolderView implements View.OnClickListener {
        @BindView(R.id.tv_clear_disable)
        TextView tv_clear_disable;

        @BindView(R.id.recycle_disable)
        RecyclerView recycle_disable;

        @BindView(R.id.foot_disable)
        LinearLayout foot_disable;

        @BindView(R.id.ll_rootView)
        LinearLayout ll_rootView;


        public FooterHolderView(View view) {
            mUnbinder = ButterKnife.bind(this, view);
            tv_clear_disable.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_clear_disable:
                    if (TextUtils.isEmpty(disGoodsIds)) {
                        return;
                    }
                    shopCarPresenter.cartRemove(disGoodsIds);
                    break;
            }
        }
    }

    public class ProbabyFooterHolderView {
        RecyclerView recycler_list;
        LinearLayout ll_rootView;

        public ProbabyFooterHolderView(View view) {
            recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
            ll_rootView = view.findViewById(R.id.ll_rootView);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
            recycler_list.setNestedScrollingEnabled(false);
            recycler_list.setLayoutManager(manager);
        }
    }

    public class RecommendGoodsHolderView {
        RecyclerView recycler_list;
        LinearLayout ll_rootView;

        public RecommendGoodsHolderView(View view) {
            recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
            ll_rootView = view.findViewById(R.id.ll_rootView);
            GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
            recycler_list.setNestedScrollingEnabled(false);
            recycler_list.setLayoutManager(manager);
        }
    }

    public class EmptyHolderView {
        NetAndEmptyInterface nei_empty;
        LinearLayout ll_rootView;

        public EmptyHolderView(View view) {
            nei_empty = (NetAndEmptyInterface) view.findViewById(R.id.nei_empty);
            ll_rootView = view.findViewById(R.id.ll_rootView);
        }
    }

    public String getCheckAll() {
        if (mCarEntity == null || mCarEntity.enabled == null || mCarEntity.enabled.size() == 0) {
            return "0";
        }
        for (int i = 0; i < mCarEntity.enabled.size(); i++) {
            if ("0".equals(mCarEntity.enabled.get(i).all_check)) {
                return "0";
            }
        }
        return "1";
    }


    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
