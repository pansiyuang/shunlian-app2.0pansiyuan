package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreSortAdapter;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.presenter.StoreSortPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.StoreSortView;

import butterknife.BindView;

public class StoreSearchAct extends BaseActivity implements View.OnClickListener, StoreSortView {
    @BindView(R.id.rv_sort)
    RecyclerView rv_sort;


    private String storeId;
    private StoreSortPresenter storeSortPresenter;

    public static void startAct(Context context, String storeId) {
        Intent intent = new Intent(context, StoreSearchAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_sort;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("storeId");
        storeSortPresenter = new StoreSortPresenter(this, this, storeId);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void introduceInfo(StoreCategoriesEntity storeCategoriesEntity) {

        LinearLayoutManager firstManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_sort.setLayoutManager(firstManager);
        rv_sort.setAdapter(new StoreSortAdapter(this, false, storeCategoriesEntity.data));
    }
}
