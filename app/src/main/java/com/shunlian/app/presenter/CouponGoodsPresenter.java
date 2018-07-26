package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.CouponGoodsListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.StageGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICouponGoodsView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/26.
 */

public class CouponGoodsPresenter extends BasePresenter<ICouponGoodsView> {

    private String mStore_id;
    private String mVoucher_id;

    private List<StageGoodsListEntity.ItemGoods> itemGoodsList = new ArrayList<>();
    private CouponGoodsListAdapter adapter;
    private int totalCount;
    private Call<BaseEntity<StageGoodsListEntity>> baseEntityCall;

    public CouponGoodsPresenter(Context context, ICouponGoodsView iView,
                                String store_id, String voucher_id) {
        super(context, iView);
        mStore_id = store_id;
        mVoucher_id = voucher_id;
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
        if (baseEntityCall != null)baseEntityCall.cancel();
        if (adapter != null){
            adapter.unbind();
            adapter = null;
        }
        if (itemGoodsList != null){
            itemGoodsList.clear();
            itemGoodsList = null;
        }
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        itemGoodsList.clear();
        currentPage = 1;
        allPage = 1;
        pageing(true);
    }

    public void pageing(boolean isShowLoad) {
        Map<String, String> map = new HashMap<>();
        map.put("voucher_id",mVoucher_id);
        if (isEmpty(mStore_id))
            mStore_id = "0";
        map.put("store_id", mStore_id);
        map.put("page", currentPage + "");
        map.put("page_size", page_size);
        sortAndMD5(map);

        baseEntityCall = getApiService().voucherRelatedStore(map);

        getNetData(isShowLoad, baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<StageGoodsListEntity>>() {

                    @Override
                    public void onSuccess(BaseEntity<StageGoodsListEntity> entity) {
                        super.onSuccess(entity);
                        StageGoodsListEntity data = entity.data;
                        StageGoodsListEntity.PagerBean pager = data.pager;
                        if (pager != null) {
                            currentPage = Integer.parseInt(pager.page);
                            allPage = Integer.parseInt(pager.total_page);
                            totalCount = Integer.parseInt(pager.count);
                        }
                        setData(data);
                        currentPage++;
                    }
                });
    }

    private void setData(StageGoodsListEntity data) {
        if (!isEmpty(data.goods_list)) {
            itemGoodsList.addAll(data.goods_list);
            if (adapter == null) {
                adapter = new CouponGoodsListAdapter(context, itemGoodsList, data.voucher_info,totalCount);
                iView.setAdapter(adapter);
                adapter.setOnReloadListener(() -> onRefresh());
            }else {
                adapter.notifyDataSetChanged();
            }
            adapter.setPageLoading(currentPage,allPage);
        }
    }


    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                pageing(false);
            }
        }
    }
}
