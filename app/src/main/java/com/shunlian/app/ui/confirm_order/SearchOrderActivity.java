package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.TagEntity;
import com.shunlian.app.presenter.OrderHistoryPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ITagView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/16.
 */

public class SearchOrderActivity extends BaseActivity implements ITagView, TextView.OnEditorActionListener, View.OnClickListener {

    @BindView(R.id.edt_order_search)
    EditText edt_order_search;

    @BindView(R.id.tv_search_cancel)
    TextView tv_search_cancel;

    @BindView(R.id.miv_search_del)
    MyImageView miv_search_del;

    @BindView(R.id.tag_search_history)
    TagFlowLayout tag_search_history;

    public OrderHistoryPresenter orderHistoryPresenter;
    public TagAdapter tagAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_order;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
    }

    @Override
    protected void initListener() {
        edt_order_search.setOnEditorActionListener(this);
        tv_search_cancel.setOnClickListener(this);
        miv_search_del.setOnClickListener(this);
        super.initListener();
    }

    @Override
    protected void onResume() {
        if (orderHistoryPresenter == null) {
            orderHistoryPresenter = new OrderHistoryPresenter(this, this);
        }
        orderHistoryPresenter.getOrderHistory();
        super.onResume();
    }

    @Override
    public void getOrderHistory(final TagEntity tagEntity) {
        tagAdapter = new TagAdapter(tagEntity.keyword) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                View view = LayoutInflater.from(SearchOrderActivity.this).inflate(R.layout.item_tag_layout, tag_search_history, false);
                TextView tv = (TextView) view.findViewById(R.id.tv_history_tag);
                tv.setText(tagEntity.keyword.get(position));
                return view;
            }
        };
        tag_search_history.setAdapter(tagAdapter);
    }

    @Override
    public void delSuccess() {
    }

    @Override
    public void delFail(String errorStr) {
        Common.staticToast(errorStr);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search_cancel:
                finish();
                break;
            case R.id.miv_search_del:
                orderHistoryPresenter.delHistory();
                break;
        }
    }
}
