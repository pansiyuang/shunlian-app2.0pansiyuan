package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.CouponListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CouponListEntity;
import com.shunlian.app.bean.SaleDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICouponListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/19.
 */

public class CouponListPresenter extends BasePresenter<ICouponListView> {

    private final String page_size = "15";
    private List<CouponListEntity.VoucherList> voucherLists = new ArrayList<>();
    private CouponListAdapter adapter;

    public final String no_used = "0";
    public final String used = "1";
    public final String already_used = "-2";
    public String current_state = no_used;
    private Call<BaseEntity<CouponListEntity>> baseEntityCall;

    //优惠券状态 0未使用1|已使用|-2已过期
    public CouponListPresenter(Context context, ICouponListView iView) {
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
    public void  detachView() {
        if (baseEntityCall != null)baseEntityCall.cancel();
        if (adapter != null){
            adapter.unbind();
            adapter = null;
        }
        if (voucherLists != null){
            voucherLists.clear();
            voucherLists = null;
        }
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        voucherLists.clear();
        currentPage = 1;
        allPage = 1;
        paging(true,0);
    }

    private void paging(boolean isShow,int failureCode) {
        Map<String, String> map = new HashMap<>();
        map.put("status", current_state);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        baseEntityCall = getAddCookieApiService()
                .voucherList(getRequestBody(map));

        getNetData(0,failureCode,isShow, baseEntityCall, new
                SimpleNetDataCallback<BaseEntity<CouponListEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<CouponListEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        CouponListEntity data = entity.data;
                        SaleDetailEntity.Page pager = data.pager;
                        currentPage = Integer.parseInt(pager.page);
                        allPage = Integer.parseInt(pager.total_page);
                        voucherLists.addAll(data.voucher_list);
                        if (currentPage == 1) {
                            iView.setCouponNum(data.num_info);
                        }
                        setData();
                        currentPage++;
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        isLoading = false;
                    }
                });
    }

    private void setData() {
        if (adapter == null) {
            adapter = new CouponListAdapter(context, voucherLists);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(() -> onRefresh());
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);
        if (isEmpty(voucherLists)) {
            iView.showDataEmptyView(100);
        } else {
            iView.showDataEmptyView(0);
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                paging(false,100);
            }
        }
    }
}
