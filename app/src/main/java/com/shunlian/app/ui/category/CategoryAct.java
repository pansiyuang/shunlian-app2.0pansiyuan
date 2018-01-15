package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
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

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.shunlian.app.utils.LogUtil;
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
    private HashMap<String, Object> hashMap;
    private ObjectMapper objectMapper;
    private String currentBrandIds = "";
    private String currentArea = "";
    private String currentAttrData = "";
    private String currentFreeship = "";

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

        objectMapper = new ObjectMapper();
        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
        if (searchParam == null) {
            searchParam = new GoodsSearchParam();
        }
        presenter = new CategoryPresenter(this, this);
        presenter.getSearchGoods(searchParam);
        mGoods = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        recycle_category.setNestedScrollingEnabled(false);
        popupWindow = new CategorySortPopWindow(this);
        popupWindow.setOnSortSelectListener(this);
        popupWindow.setOnDismissListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.httpLogW("onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
//        if (!isEmpty(searchParam.brand_ids)) {
//            tv_filter.setTextColor(getColorResouce(R.color.pink_color));
//            tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.icon_sx), null);
//        } else {
//            tv_filter.setTextColor(getColorResouce(R.color.new_text));
//            tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.img_saixuan), null);
//        }
        hashMap = classToMap(searchParam);
        LogUtil.httpLogW("hashMap:" + hashMap.size());

        if (!currentBrandIds.equals(searchParam.brand_ids)) {
            presenter.getSearchGoods(searchParam);
        }
        currentBrandIds = searchParam.brand_ids;
        currentArea = searchParam.send_area;
        currentFreeship = searchParam.is_free_ship;
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
    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity) {
        if (goodsEntity.goods_list != null && goodsEntity.goods_list.size() != 0) {
            mGoods.clear();
            mGoods = goodsEntity.goods_list;
            if (singleAdapter == null) {
                singleAdapter = new SingleCategoryAdapter(this, false, mGoods, goodsEntity.ref_store);
            } else {
                singleAdapter.setData(mGoods);
            }
            if (doubleAdapter == null) {
                doubleAdapter = new DoubleCategoryAdapter(this, false, goodsEntity.goods_list, goodsEntity.ref_store);
            } else {
                doubleAdapter.setData(mGoods);
            }
            setListMode(currentMode);
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
                    presenter.getSearchGoods(searchParam);
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
        presenter.getSearchGoods(searchParam);
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
            presenter.getSearchGoods(searchParam);
            return true;
        }
        return false;
    }

    public HashMap<String, Object> classToMap(GoodsSearchParam entity) {
        hashMap = new HashMap<>();
        if (!isEmpty(entity.send_area)) {
            hashMap.put("send_area", entity.send_area);
        }
        if (!isEmpty(entity.attr_data)) {
            hashMap.put("attr_data", entity.attr_data);
            try {
                LogUtil.httpLogW("attr_data:" + objectMapper.writeValueAsString(entity.attr_data));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
