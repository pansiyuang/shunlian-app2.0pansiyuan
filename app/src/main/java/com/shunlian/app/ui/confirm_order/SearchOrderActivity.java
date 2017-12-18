package com.shunlian.app.ui.confirm_order;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.OrderListAdapter;
import com.shunlian.app.bean.MyOrderEntity;
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

    @BindView(R.id.rl_history)
    RelativeLayout rl_history;

    private OrderHistoryPresenter orderHistoryPresenter;
    private TagAdapter tagAdapter;
    private String currentKeyword;

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

        if (tagEntity == null || tagEntity.keyword == null) {
            return;
        }

        if (tagEntity.keyword.size() != 0) {
            rl_history.setVisibility(View.VISIBLE);
            tag_search_history.setVisibility(View.VISIBLE);
        } else {
            rl_history.setVisibility(View.GONE);
            tag_search_history.setVisibility(View.GONE);
        }
        tagAdapter = new TagAdapter(tagEntity.keyword) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                final String tagStr = tagEntity.keyword.get(position);
                View view = LayoutInflater.from(SearchOrderActivity.this).inflate(R.layout.item_tag_layout, tag_search_history, false);
                TextView tv = (TextView) view.findViewById(R.id.tv_history_tag);
                tv.setText(tagStr);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SearchOrderResultActivity.startAct(SearchOrderActivity.this, tagStr);
                    }
                });
                return view;
            }
        };
        tag_search_history.setAdapter(tagAdapter);
    }

    @Override
    public void delSuccess() {
        rl_history.setVisibility(View.GONE);
        tag_search_history.setVisibility(View.GONE);
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
            currentKeyword = edt_order_search.getText().toString();
            if (isEmpty(currentKeyword)) {
                Common.staticToast("请输入您要搜索的商品");
            } else {
                SearchOrderResultActivity.startAct(this, currentKeyword);
            }
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
