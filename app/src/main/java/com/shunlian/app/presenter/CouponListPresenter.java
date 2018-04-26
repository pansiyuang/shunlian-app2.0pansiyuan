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
    public void detachView() {

    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        voucherLists.clear();
        currentPage = 1;
        allPage = 1;
        paging(true);
    }

    private void paging(boolean isShow) {
        Map<String, String> map = new HashMap<>();
        map.put("status", current_state);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<CouponListEntity>> baseEntityCall = getAddCookieApiService()
                .voucherList(getRequestBody(map));

        getNetData(isShow, baseEntityCall, new
                SimpleNetDataCallback<BaseEntity<CouponListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CouponListEntity> entity) {
                super.onSuccess(entity);
                CouponListEntity data = entity.data;
                SaleDetailEntity.Page pager = data.pager;
                currentPage = Integer.parseInt(pager.page);
                allPage = Integer.parseInt(pager.total_page);
                voucherLists.addAll(data.voucher_list);
                if (currentPage == 1){
                    iView.setCouponNum(data.num_info);
                }
                setData();
                currentPage++;
            }
        });
    }

    private void setData() {
        if (adapter == null) {
            adapter = new CouponListAdapter(context, voucherLists);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(()-> onRefresh());
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
        if (isEmpty(voucherLists)){
            iView.showDataEmptyView(100);
        }else {
            iView.showDataEmptyView(0);
        }
    }
}
