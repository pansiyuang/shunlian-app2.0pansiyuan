package com.shunlian.app.ui.category;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DoubleCategoryAdapter;
import com.shunlian.app.adapter.SingleCategoryAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.CategoryFiltratePresenter;
import com.shunlian.app.presenter.CategoryPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.goods_detail.SearchGoodsActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ICategoryView;
import com.shunlian.app.widget.CategorySortPopWindow;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shunlian.app.utils.TransformUtil.expandViewTouchDelegate;

/**
 * Created by Administrator on 2018/1/2.
 */

public class CategoryAct extends SideslipBaseActivity implements ICategoryView, OnClickListener, CategorySortPopWindow.OnSortSelectListener, PopupWindow.OnDismissListener, MessageCountManager.OnGetMessageListener {

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

    @BindView(R.id.tv_keyword)
    TextView tv_keyword;

    @BindView(R.id.tv_msg_count)
    TextView tv_msg_count;

    @BindView(R.id.tv_general_sort)
    TextView tv_general_sort;

    @BindView(R.id.tv_sales_volume)
    TextView tv_sales_volume;

    @BindView(R.id.view_category_line)
    View view_category_line;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private CategoryPresenter presenter;
    private SingleCategoryAdapter singleAdapter;
    private DoubleCategoryAdapter doubleAdapter;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private GoodsSearchParam searchParam;
    private CategorySortPopWindow popupWindow;
    private int currentSortPosition = 0;
    private String currentSort;
    private boolean sortBySales;
    private int currentMode = MODE_SINGLE;
    private List<GoodsDeatilEntity.Goods> mGoods;
    private SearchGoodsEntity.RefStore mRefStore;
    private HashMap<String, Object> hashMap;
    private HashMap<String, Object> currentMap = new HashMap<>();
    private String currentBrandIds = "";
    private String currentArea = "";
    private String currentFreeship = "";
    private String currentMaxPrice = "";
    private String currentMinPrice = "";
    private List<GoodsSearchParam.Attr> currentAttrData = new ArrayList<>();
    private List<String> mKeywords = new ArrayList<>();
    private boolean hasChange;
    private int totalPage;
    private int currentPage;
    private int totalMsgCount;
    private MessageCountManager messageCountManager;

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
        EventBus.getDefault().register(this);
        searchParam = (GoodsSearchParam) getIntent().getSerializableExtra("param");
        if (searchParam == null) {
            searchParam = new GoodsSearchParam();
        }

        if (!isEmpty(searchParam.keyword)) {
            tv_keyword.setText(searchParam.keyword);
        } else if (!isEmpty(searchParam.name)) {
            tv_keyword.setText(searchParam.name);
        }
//        searchParam.keyword = "";
        presenter = new CategoryPresenter(this, this);
        presenter.getSearchGoods(searchParam, true);
        mGoods = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this, 2);

        singleAdapter = new SingleCategoryAdapter(this, true, mGoods, mRefStore, mKeywords);
        doubleAdapter = new DoubleCategoryAdapter(this, true, mGoods, mRefStore, mKeywords);

        recycle_category.setNestedScrollingEnabled(false);
        popupWindow = new CategorySortPopWindow(this);
        setSortMode(true);
        popupWindow.initData(currentSortPosition);
        popupWindow.setOnSortSelectListener(this);
        popupWindow.setOnDismissListener(this);

        setListMode(currentMode);

        singleAdapter.setOnItemClickListener((view, position) -> {
            try{
                int mposition;
                if (mRefStore == null && isEmpty(mKeywords)) {
                    mposition = position;
                } else {
                    mposition = position - 1;
                }
                if (mposition < 0||mposition>=mGoods.size())
                    return;
                GoodsDeatilEntity.Goods goods = mGoods.get(mposition);
                if (!isEmpty(goods.id)) {
                    GoodsDetailAct.startAct(CategoryAct.this, goods.id);
                }
            }catch (Exception e){

            }
        });
        doubleAdapter.setOnItemClickListener((view, position) -> {
            try{
                int mposition;
                if (mRefStore == null && isEmpty(mKeywords)) {
                    mposition = position;
                } else {
                    mposition = position - 1;
                }
                if (mposition < 0||mposition>=mGoods.size())
                    return;
                GoodsDeatilEntity.Goods goods = mGoods.get(mposition);
                if (!isEmpty(goods.id)) {
                    GoodsDetailAct.startAct(CategoryAct.this, goods.id);
                }
            }catch (Exception e){

            }

        });

        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(this);
            if (messageCountManager.isLoad()) {
                totalMsgCount = messageCountManager.getAll_msg();
                if (totalMsgCount > 0) {
                    tv_msg_count.setVisibility(View.VISIBLE);
                }
                tv_msg_count.setText(String.valueOf(totalMsgCount));
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
    }

    @OnClick(R.id.rl_title_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.search();
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_change_mode.setOnClickListener(this);
        tv_filter.setOnClickListener(this);
        tv_general_sort.setOnClickListener(this);
        tv_sales_volume.setOnClickListener(this);
        tv_keyword.setOnClickListener(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    public void showEmptyView(boolean isShowEmpty) {
        if (nei_empty == null)
            return;
        if (isShowEmpty) {
            nei_empty.setImageResource(R.mipmap.img_empty_dingdan).setText("暂无商品").setButtonText(null);
            nei_empty.setVisibility(View.VISIBLE);
            recycle_category.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycle_category.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getSearchGoods(SearchGoodsEntity goodsEntity, int page, int allPage) {
        JosnSensorsDataAPI.search(searchParam.keyword, goodsEntity.goods_list != null && goodsEntity.goods_list.size() > 0,
                JosnSensorsDataAPI.isHistory, JosnSensorsDataAPI.isRecommend);
        currentPage = page;
        totalPage = allPage;
        if (currentPage == 1) {
            mGoods.clear();
            mKeywords.clear();
            if (!isEmpty(goodsEntity.keyword_list)) {
                mKeywords.addAll(goodsEntity.keyword_list);
                singleAdapter.setKeywordData(mKeywords);
                doubleAdapter.setKeywordData(mKeywords);
            }
            if (goodsEntity.ref_store != null) {
                mRefStore = goodsEntity.ref_store;
                singleAdapter.setStoreData(mRefStore);
                doubleAdapter.setStoreData(mRefStore);
            }

            if (isEmpty(goodsEntity.goods_list) && goodsEntity.ref_store == null) {
                showEmptyView(true);
            } else {
                showEmptyView(false);
            }
        }
        if (!isEmpty(goodsEntity.goods_list)) {
            mGoods.addAll(goodsEntity.goods_list);
        }
        if (currentMode == MODE_SINGLE) {
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
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_lianglei));
            if (singleAdapter != null) {
                recycle_category.setAdapter(singleAdapter);
            }
        } else if (mode == MODE_DOUBLE) {
            recycle_category.setLayoutManager(gridLayoutManager);
            miv_change_mode.setImageDrawable(getDrawableResouce(R.mipmap.img_yilei));
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
                String keyword = tv_keyword.getText().toString();
                CategoryFiltrateAct.startAct(CategoryAct.this, keyword, searchParam.cid, searchParam.sort_type);
                break;
            case R.id.tv_general_sort:
                popupWindow.initData(currentSortPosition);
                if (!popupWindow.isShowing()) {
                    showAsDropDown(popupWindow, view_category_line, 0, 0);

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
                    presenter.initPage();
                    presenter.getSearchGoods(searchParam, true);
                }
                break;
            case R.id.tv_keyword:
                String word = tv_keyword.getText().toString();
                SearchGoodsActivity.startActivityForResult(this, word);
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
        presenter.initPage();
        presenter.getSearchGoods(searchParam, true);
    }

    @Override
    public void onDismiss() {
        miv_general_sort.setRotation(0);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CategoryFiltratePresenter.FILTRATE_REQUEST_CODE && resultCode == RESULT_OK) {
            searchParam = (GoodsSearchParam) data.getSerializableExtra("searchparam");
            hasChange = false;
            HashMap<String, Object> map = classToMap(searchParam);

            if (map.size() == 0) {
                tv_filter.setTextColor(getColorResouce(R.color.new_text));
                tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.img_saixuan), null);
            }
            if (currentMap.size() != map.size()) {
                hasChange = true;
            }

            currentMap.clear();
            currentMap.putAll(map);
            if (currentMap.size() != 0) {
                tv_filter.setTextColor(getColorResouce(R.color.pink_color));
                tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.icon_sx), null);

                if (isEmpty(searchParam.brand_ids)) {
                    searchParam.brand_ids = "";
                }
                if (isEmpty(searchParam.send_area) || getStringResouce(R.string.category_dingwei).equals(searchParam.send_area)) {
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
                presenter.initPage();
                presenter.getSearchGoods(searchParam, true);
            }
        } else if (requestCode == SearchGoodsActivity.SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            String keyword = data.getStringExtra("keyword");
            if (!isEmpty(keyword) && !keyword.equals(searchParam.keyword)) {
                currentSortPosition = 0;
                presenter.initPage();
                setSortMode(true);
                setSalesMode(false);
                tv_filter.setTextColor(getColorResouce(R.color.new_text));
                tv_filter.setCompoundDrawables(null, null, getRightDrawable(R.mipmap.img_saixuan), null);

                searchParam = new GoodsSearchParam();
                initFiltrate();

                searchParam.keyword = keyword;
                tv_keyword.setText(keyword);
                presenter.getSearchGoods(searchParam, true);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        initFiltrate();
    }

    public void initFiltrate() {

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

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    public void requestData(String keyword) {
        searchParam = new GoodsSearchParam();
        initFiltrate();

        searchParam.keyword = keyword;
        tv_keyword.setText(keyword);
        presenter.initPage();
        presenter.getSearchGoods(searchParam, true);
    }

    public void showAsDropDown(final PopupWindow pw, final View anchor, final int xoff, final int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            pw.setHeight(height);
            pw.showAsDropDown(anchor, xoff, yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }
}
