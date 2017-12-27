package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.RefundInfoEntity;
import com.shunlian.app.presenter.SelectServicePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.ISelectServiceView;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class SelectServiceActivity extends BaseActivity implements View.OnClickListener, ISelectServiceView {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    private String currentOgId;
    private SelectServicePresenter presenter;


    public static void startAct(Context context, String ogId) {
        Intent intent = new Intent(context, SelectServiceActivity.class);
        intent.putExtra("ogId", ogId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_service;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.select_service_type));
        rl_title_more.setVisibility(View.VISIBLE);

        presenter = new SelectServicePresenter(this, this);
        currentOgId = getIntent().getStringExtra("ogId");
        if (!isEmpty(currentOgId)) {
            presenter.getRefundInfo(currentOgId);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void getRefundInfo(RefundInfoEntity infoEntity) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
