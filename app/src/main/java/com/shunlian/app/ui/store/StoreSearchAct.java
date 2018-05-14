package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.StoreBabyAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PStoreSearch;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.StoreSearchView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StoreSearchAct extends BaseActivity implements View.OnClickListener, StoreSearchView, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.rv_baby)
    RecyclerView rv_baby;

    @BindView(R.id.tv_zonghe)
    MyTextView tv_zonghe;

    @BindView(R.id.tv_xiaoliang)
    MyTextView tv_xiaoliang;

    @BindView(R.id.tv_shangxin)
    MyTextView tv_shangxin;

    @BindView(R.id.mtv_key)
    MyTextView mtv_key;

    @BindView(R.id.tv_price)
    MyTextView tv_price;

    @BindView(R.id.mtv_nomore)
    MyTextView mtv_nomore;

    @BindView(R.id.edt_goods_search)
    EditText edt_goods_search;

    @BindView(R.id.iv_price)
    MyImageView iv_price;

    @BindView(R.id.mrlayout_sort)
    MyRelativeLayout mrlayout_sort;

    @BindView(R.id.mrLayout_price)
    MyRelativeLayout mrLayout_price;
    @BindView(R.id.mll_menu)
    MyLinearLayout mll_menu;
    @BindView(R.id.mrlayout_search)
    MyRelativeLayout mrlayout_search;
    @BindView(R.id.rl_more)
    RelativeLayout rl_more;
    @BindView(R.id.quick_actions)
    QuickActions quick_actions;
    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    private String storeId, sc1, sc2, scName;
    private PStoreSearch pStoreSearch;
    private GridLayoutManager babyManager;
    private boolean isPriceUp;
    private StoreBabyAdapter storeBabyAdapter;
    private MessageCountManager messageCountManager;
    private String key = "";

    public static void startAct(Context context, String storeId, String sc1, String sc2, String scName) {
        Intent intent = new Intent(context, StoreSearchAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        intent.putExtra("sc1", sc1);
        intent.putExtra("sc2", sc2);
        intent.putExtra("scName", scName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.Store();
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
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

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_store_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mrlayout_sort:
                StoreSortAct.startAct(this, storeId, true);
                finish();
                break;
            case R.id.mtv_key:
                edt_goods_search.setVisibility(View.VISIBLE);
                mrlayout_search.setVisibility(View.GONE);
                pStoreSearch.sc1="";
                pStoreSearch.sc2="";
                break;
        }
        if (rv_baby.getScrollState() == 0) {
            babyMenu(v);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_zonghe.setOnClickListener(this);
        tv_xiaoliang.setOnClickListener(this);
        tv_shangxin.setOnClickListener(this);
        mrlayout_sort.setOnClickListener(this);
        mtv_key.setOnClickListener(this);
        mrLayout_price.setOnClickListener(this);
        rv_baby.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (babyManager != null) {
                    int lastPosition = babyManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == babyManager.getItemCount()) {
                        if (pStoreSearch != null) {
                            pStoreSearch.refreshBaby();
                        }
                    }
                }
            }
        });
        edt_goods_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    key = edt_goods_search.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        Common.staticToast("请先输入关键字...");
                    } else {
                        pStoreSearch.resetBaby("default", key);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void babyMenu(View v) {
        switch (v.getId()) {
            case R.id.tv_zonghe:
                tv_zonghe.setTextColor(getResources().getColor(R.color.pink_color));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                pStoreSearch.resetBaby("default", key);
                break;
            case R.id.tv_xiaoliang:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.pink_color));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                pStoreSearch.resetBaby("sales", key);
                break;
            case R.id.tv_shangxin:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.pink_color));
                tv_price.setTextColor(getResources().getColor(R.color.my_gray_three));
                iv_price.setVisibility(View.GONE);
                pStoreSearch.resetBaby("new", key);
                break;
            case R.id.mrLayout_price:
                tv_zonghe.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_xiaoliang.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_shangxin.setTextColor(getResources().getColor(R.color.my_gray_three));
                tv_price.setTextColor(getResources().getColor(R.color.pink_color));
                iv_price.setVisibility(View.VISIBLE);
                if (isPriceUp) {
                    iv_price.setImageResource(R.mipmap.icon_common_arrowdown);
                    pStoreSearch.resetBaby("pricedown", key);
                    isPriceUp = false;
                } else {
                    iv_price.setImageResource(R.mipmap.icon_common_arrowup);
                    pStoreSearch.resetBaby("priceup", key);
                    isPriceUp = true;
                }
                break;
        }
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        storeId = getIntent().getStringExtra("storeId");
        sc1 = getIntent().getStringExtra("sc1");
        sc2 = getIntent().getStringExtra("sc2");
        scName = getIntent().getStringExtra("scName");
        pStoreSearch = new PStoreSearch(this, this, storeId, sc1, sc2);
        if (!TextUtils.isEmpty(scName)) {
            mll_menu.setVisibility(View.VISIBLE);
            mtv_key.setText(scName + " ×");
            mrlayout_search.setVisibility(View.VISIBLE);
            edt_goods_search.setVisibility(View.GONE);
            pStoreSearch.resetBaby("default", "");
        } else {
            edt_goods_search.setVisibility(View.VISIBLE);
            mrlayout_search.setVisibility(View.GONE);
            mll_menu.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void storeBaby(List<StoreGoodsListEntity.MData> mDataList, int allPage, int page) {
        if (storeBabyAdapter == null) {
            storeBabyAdapter = new StoreBabyAdapter(this, true, mDataList);
            babyManager = new GridLayoutManager(this, 2);
            rv_baby.setLayoutManager(babyManager);
            rv_baby.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(this, 5), TransformUtil.dip2px(this, 5), true));
            rv_baby.setAdapter(storeBabyAdapter);
            storeBabyAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(StoreSearchAct.this, mDataList.get(position).id);
                }
            });
        } else {
            storeBabyAdapter.notifyDataSetChanged();
        }
        if (mDataList!=null&&mDataList.size()>0){
            rv_baby.setVisibility(View.VISIBLE);
            mll_menu.setVisibility(View.VISIBLE);
            mtv_nomore.setVisibility(View.GONE);
        }else {
            mll_menu.setVisibility(View.GONE);
            rv_baby.setVisibility(View.GONE);
            mtv_nomore.setVisibility(View.VISIBLE);
        }
        storeBabyAdapter.setPageLoading(page, allPage);
    }
}
