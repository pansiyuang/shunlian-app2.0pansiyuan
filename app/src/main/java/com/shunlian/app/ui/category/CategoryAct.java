package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DoubleCategoryAdapter;
import com.shunlian.app.adapter.SingleCategoryAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.presenter.CategoryPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ICategoryView;
import com.shunlian.app.widget.CategorySortPopWindow;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.TransformUtil.expandViewTouchDelegate;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CategoryAct extends SideslipBaseActivity implements ICategoryView, OnClickListener, CategorySortPopWindow.OnSortSelectListener, PopupWindow.OnDismissListener, TextView.OnEditorActionListener {

    public static final int MODE_SINGLE = 1;
    public static final int MODE_DOUBLE = 2;

    @BindView(R.id.recycle_category)
    RecyclerView recycle_category;

    @BindView(R.id.miv_change_mode)
    MyImageView miv_change_mode;

    @BindView(R.id.miv_general_sort)
    MyImageView miv_general_sort;

    @BindView(R.id.tv_filter)
    TextView tv_filter;

    @BindView(R.id.edt_keyword)
    EditText edt_keyword;

    @BindView(R.id.tv_general_sort)
    TextView tv_general_sort;

    @BindView(R.id.tv_sales_volume)
    TextView tv_sales_volume;

    @BindView(R.id.view_line)
    View view_line;

    private CategoryPresenter presenter;
    private SingleCategoryAdapter singleAdapter;
    private DoubleCategoryAdapter doubleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private GoodsSearchParam searchParam;
    private CategorySortPopWindow popupWindow;
    private int currentSortPosition = -1;
    private String currentSort;
    private boolean sortBySales;
    private int currentMode = MODE_SINGLE;
    private List<GoodsDeatilEntity.Goods> mGoods;
    private SearchGoodsEntity.RefStore mRefStore;
    private HashMap<String, Object> hashMap;
    private String currentBrandIds = "";
    private String currentArea = "";
    private String currentFreeship = "";
    private String currentMaxPrice = "";
    private String currentMinPrice = "";
    private List<GoodsSearchParam.Attr> currentAttrData = new ArrayList<>();
    private boolean hasChange;
    private int totalPage;
    private int currentPage;

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_category;
    }

    public static void startAct(Context context, GoodsSearchParam param) {
        Intent intent = new Intent(context, CategoryAct.class);
        intent.putExtra("param", param);
        context.startActivity(intent);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
        if (searchParam == null) {
            searchParam = new GoodsSearchParam();
        }
        presenter = new CategoryPresenter(this, this);
        presenter.getSearchGoods(searchParam, true);
        mGoods = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        singleAdapter = new SingleCategoryAdapter(this, true, mGoods, mRefStore);
        doubleAdapter = new DoubleCategoryAdapter(this, true, mGoods, mRefStore);

        recycle_category.setNestedScrollingEnabled(false);
        popupWindow = new CategorySortPopWindow(this);
        popupWindow.setOnSortSelectListener(this);
        popupWindow.setOnDismissListener(this);

        setListMode(currentMode);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
        hashMap = classToMap(searchParam);
        hasChange = false;

        if (hashMap.size() == 0) {
            tv_filter.setTextColor(getColorResouce(R.color.new_text));
            tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.img_saixuan), null);
        } else {
            tv_filter.setTextColor(getColorResouce(R.color.pink_color));
            tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.icon_sx), null);

            if (isEmpty(searchParam.brand_ids)) {
                searchParam.brand_ids = "";
            }
            if (isEmpty(searchParam.send_area)) {
                searchParam.send_area = "";
            }
            if (isEmpty(searchParam.is_free_ship)) {
                searchParam.is_free_ship = "";
            }
            if (isEmpty(searchParam.max_price)) {
                searchParam.max_price = "";
            }
            if (isEmpty(searchParam.min_price)) {
                searchParam.min_price = "";
            }

            if (!currentBrandIds.equals(searchParam.brand_ids)) {
                currentBrandIds = searchParam.brand_ids;
                hasChange = true;
            }
            if (!currentArea.equals(searchParam.send_area)) {
                currentArea = searchParam.send_area;
                hasChange = true;
            }
            if (!currentFreeship.equals(searchParam.is_free_ship)) {
                currentFreeship = searchParam.is_free_ship;
                hasChange = true;
            }
            if (!currentMaxPrice.equals(searchParam.max_price)) {
                currentMaxPrice = searchParam.max_price;
                hasChange = true;
            }
            if (!currentMinPrice.equals(searchParam.min_price)) {
                currentMinPrice = searchParam.min_price;
                hasChange = true;
            }
            if (!isEmpty(searchParam.attr_data)) {
                hasChange = true;
            }
            if (!isEmpty(searchParam.attr_data) && currentAttrData.size() != searchParam.attr_data.size()) {
                currentAttrData = searchParam.attr_data;
                hasChange = true;
            }
        }
        if (hasChange) {
            presenter.getSearchGoods(searchParam, true);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_change_mode.setOnClickListener(this);
        tv_filter.setOnClickListener(this);
        tv_general_sort.setOnClickListener(this);
        tv_sales_volume.setOnClickListener(this);
        edt_keyword.setOnEditorActionListener(this);
        expandViewTouchDelegate(tv_filter, TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f));
        expandViewTouchDelegate(miv_change_mode, TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f));

        recycle_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (currentMode == MODE_SINGLE) {
                    if (linearLayoutManager != null) {
                        int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                        if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                            if (presenter != null) {
                                if (currentPage > totalPage) {
                                    presenter.resetCurrentPage();
                                }
                                presenter.onRefresh();
                            }
                        }
                    }
                } else if (currentMode == MODE_DOUBLE) {
                    if (gridLayoutManager != null) {
                        int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                        if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                            if (presenter != null) {
                                if (currentPage > totalPage) {
                                    presenter.resetCurrentPage();
                                }
                                presenter.onRefresh();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity, int page, int allPage) {
        currentPage = page;
        totalPage = allPage;
        if (currentPage == 1) {
            mGoods.clear();
            mRefStore = goodsEntity.ref_store;
            singleAdapter.setStoreData(mRefStore);
            doubleAdapter.setStoreData(mRefStore);
        }
        if (goodsEntity.goods_list != null && goodsEntity.goods_list.size() != 0) {
            mGoods.addAll(goodsEntity.goods_list);
        }
        if (currentMode == MODE_SINGLE) {
            singleAdapter.setStoreData(mRefStore);
            if (goodsEntity.goods_list.size() <= CategoryPresenter.PAGE_SIZE) {
                singleAdapter.notifyDataSetChanged();
            } else {
                singleAdapter.notifyItemInserted(CategoryPresenter.PAGE_SIZE);
            }
            singleAdapter.setPageLoading(page, allPage);
        } else if (currentMode == MODE_DOUBLE) {
            if (goodsEntity.goods_list.size() <= CategoryPresenter.PAGE_SIZE) {
                doubleAdapter.notifyDataSetChanged();
            } else {
                doubleAdapter.notifyItemInserted(CategoryPresenter.PAGE_SIZE);
            }
            doubleAdapter.setPageLoading(page, allPage);
        }
    }

    public Drawable getRightDrawable(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public void setListMode(int mode) {
        if (mode == MODE_SINGLE) {
            recycle_category.setLayoutManager(linearLayoutManager);
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_yilei));
            if (singleAdapter != null) {
                recycle_category.setAdapter(singleAdapter);
            }
        } else if (mode == MODE_DOUBLE) {
            recycle_category.setLayoutManager(gridLayoutManager);
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_lianglei));
            if (doubleAdapter != null) {
                recycle_category.setAdapter(doubleAdapter);
            }
        }
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
            case R.id.miv_change_mode:
                if (currentMode == MODE_SINGLE) {
                    currentMode = MODE_DOUBLE;
                } else {
                    currentMode = MODE_SINGLE;
                }
                setListMode(currentMode);
                break;
            case R.id.tv_filter:
                String keyword = edt_keyword.getText().toString();
                CategoryFiltrateAct.startAct(CategoryAct.this, keyword, searchParam.cid, searchParam.sort_type);
                break;
            case R.id.tv_general_sort:
                popupWindow.initData(currentSortPosition);
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(view_line);
                    miv_general_sort.setRotation(180);
                }
                break;
            case R.id.tv_sales_volume:
                if (!sortBySales) {
                    setSalesMode(true);
                    setSortMode(false);
                    tv_general_sort.setText(getStringResouce(R.string.general_sort));
                    currentSortPosition = -1;
                    searchParam.sort_type = "sales_desc";
                    presenter.getSearchGoods(searchParam, true);
                }
                break;
        }
        super.onClick(view);
    }

    public void setSortMode(boolean b) {
        if (b) {
            tv_general_sort.setTextColor(getColorResouce(R.color.pink_color));
            miv_general_sort.setImageDrawable(getDrawableResouce(R.mipmap.icon_sanjiao));
        } else {
            miv_general_sort.setImageDrawable(getDrawableResouce(R.mipmap.icon_sanjiao_gray));
            tv_general_sort.setTextColor(getColorResouce(R.color.new_text));
        }
    }

    public void setSalesMode(boolean b) {
        if (b) {
            tv_sales_volume.setTextColor(getColorResouce(R.color.pink_color));
        } else {
            tv_sales_volume.setTextColor(getColorResouce(R.color.new_text));
        }
        tv_sales_volume.setEnabled(!b);
        sortBySales = b;
    }

    @Override
    public void OnItemSelect(int position, String sortString) {
        if (currentSortPosition == position) {
            return;
        }
        currentSortPosition = position;
        tv_general_sort.setText(sortString);
        setSortMode(true);
        setSalesMode(false);
        switch (currentSortPosition) {
            case 0:
                currentSort = "";
                break;
            case 1:
                currentSort = "price_desc";
                break;
            case 2:
                currentSort = "price_asc";
                break;
            case 3:
                currentSort = "view_desc";
                break;
        }
        searchParam.sort_type = currentSort;
        presenter.getSearchGoods(searchParam, true);
    }

    @Override
    public void onDismiss() {
        miv_general_sort.setRotation(0);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String str = edt_keyword.getText().toString();
            searchParam.keyword = str;
            Common.hideKeyboard(edt_keyword);
            presenter.getSearchGoods(searchParam, true);
            return true;
        }
        return false;
    }

    public HashMap<String, Object> classToMap(GoodsSearchParam entity) {
        if (hashMap == null) {
            hashMap = new HashMap<>();
        } else {
            hashMap.clear();
        }
        if (!isEmpty(entity.send_area)) {
            hashMap.put("send_area", entity.send_area);
        }
        if (!isEmpty(entity.brand_ids)) {
            hashMap.put("brand_ids", entity.brand_ids);
        }
        if ("Y".equals(entity.is_free_ship)) {
            hashMap.put("is_free_ship", entity.is_free_ship);
        }
        if (!isEmpty(entity.max_price)) {
            hashMap.put("max_price", entity.max_price);
        }
        if (!isEmpty(entity.min_price)) {
            hashMap.put("min_price", entity.min_price);
        }
        if (!isEmpty(entity.attr_data)) {
            hashMap.put("attr_data", entity.attr_data);
        }
        return hashMap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.BRAND_IDS = null;//筛选品牌id
        Constant.BRAND_IDSBEFORE = null;//筛选品牌id,记录用
        Constant.BRAND_ATTRS = null;//筛选属性
        Constant.BRAND_ATTRNAME = null;//筛选属性名

        Constant.SEARCHPARAM = null;//搜索参数
        Constant.REBRAND_IDS = null;//筛选品牌id(重新赋值用)
        Constant.REBRAND_ATTRS = null;//筛选属性(重新赋值用)
        Constant.LISTFILTER = null;//列表属性(重新赋值用)
        Constant.DINGWEI = null;

    }
}
