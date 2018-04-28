package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.DetailOrderRecordAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DetailOrderRecordEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IDetailOrderRecordView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DetailOrderRecordPresenter extends BasePresenter<IDetailOrderRecordView> {

    public final String page_size = "20";
    private List<DetailOrderRecordEntity.Item> items = new ArrayList<>();
    private DetailOrderRecordAdapter adapter;

    public DetailOrderRecordPresenter(Context context, IDetailOrderRecordView iView) {
        super(context, iView);
        initApi();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {

    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {

        paging(true);
    }

    public void paging(boolean isShow) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<DetailOrderRecordEntity>> estimateDetail = getAddCookieApiService()
                .getEstimateDetail(getRequestBody(map));

        getNetData(isShow, estimateDetail, new SimpleNetDataCallback<BaseEntity<DetailOrderRecordEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DetailOrderRecordEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                DetailOrderRecordEntity data = entity.data;
                currentPage = Integer.parseInt(data.pager.page);
                allPage = Integer.parseInt(data.pager.total_page);
                items.addAll(data.list);
                setAdapter();
                currentPage++;

            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });

    }

    private void setAdapter() {
        if (adapter == null) {
            adapter = new DetailOrderRecordAdapter(context, items);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(() -> onRefresh());
        } else {
            adapter.notifyDataSetChanged();
        }

        adapter.setPageLoading(currentPage, allPage);

        if (isEmpty(items)){
            iView.showDataEmptyView(100);
        }else {
            iView.showDataEmptyView(0);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                paging(false);
            }
        }
    }
}
