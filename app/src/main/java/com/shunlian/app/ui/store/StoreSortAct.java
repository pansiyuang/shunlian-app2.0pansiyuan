package com.shunlian.app.ui.store;

import android.app.Activity;
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
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.StoreSortView;

import java.util.List;

import butterknife.BindView;

public class StoreSortAct extends BaseActivity implements View.OnClickListener, StoreSortView {
    @BindView(R.id.rv_sort)
    RecyclerView rv_sort;

    private String storeId;
    private StoreSortPresenter storeSortPresenter;
    private StoreSortAdapter storeSortAdapter;

    public static void startAct(Context context, String storeId) {
        Intent intent = new Intent(context, StoreSortAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }

    public static void startActForResult(Activity activity, String storeId, int requestCode) {
        Intent intent = new Intent(activity, StoreSortAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        activity.startActivityForResult(intent, requestCode);
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
        storeSortAdapter = new StoreSortAdapter(this, false, storeCategoriesEntity.data);
        rv_sort.setAdapter(storeSortAdapter);
        storeSortAdapter.setOnItemClickListener((view, parentPosition, childPosition) -> {
            List<StoreCategoriesEntity.MData> mDataList = storeCategoriesEntity.data;
            String parentId = null, childId = null, keyword = null;
            if (mDataList.get(parentPosition).children != null) {
                List<StoreCategoriesEntity.MData.Children> childrenList = mDataList.get(parentPosition).children;
                StoreCategoriesEntity.MData.Children children = childrenList.get(childPosition);
                parentId = children.parent_id;
                childId = children.id;
                keyword = children.name;
            }
            Intent intent = new Intent();
            intent.putExtra("parentSrc", parentId);
            intent.putExtra("childSrc", childId);
            intent.putExtra("keyword", keyword);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
