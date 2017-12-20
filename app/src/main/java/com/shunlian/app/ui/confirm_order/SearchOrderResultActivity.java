package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.OrderListAdapter;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.presenter.SearchOrderResultPresent;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISearchResultView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SearchOrderResultActivity extends BaseActivity implements ISearchResultView, View.OnClickListener {

    @BindView(R.id.tv_order_search)
    TextView tv_order_search;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.tv_title_number)
    TextView tv_title_number;

    @BindView(R.id.recycler_result)
    RecyclerView recycler_result;

    private SearchOrderResultPresent resultPresent;
    private String currentKeyword;
    private OrderListAdapter adapter;
    private LinearLayoutManager linearLayoutManage;

    public static void startAct(Context context, String keyword) {
        Intent intent = new Intent(context, SearchOrderResultActivity.class);
        intent.putExtra("keyword", keyword);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_order_result;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        currentKeyword = getIntent().getStringExtra("keyword");
        resultPresent = new SearchOrderResultPresent(this, this);
        resultPresent.searchOrder("1", "10", "all", currentKeyword);

        linearLayoutManage = new LinearLayoutManager(this);
        recycler_result.setLayoutManager(linearLayoutManage);
        recycler_result.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initListener() {
        tv_order_search.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void getSearchResult(MyOrderEntity myOrderEntity) {
        adapter = new OrderListAdapter(this, false, myOrderEntity.orders,null);
        recycler_result.setLayoutManager(linearLayoutManage);
        recycler_result.setAdapter(adapter);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_order_search:
                SearchOrderActivity.startAct(SearchOrderResultActivity.this);
                break;
        }
    }
}
