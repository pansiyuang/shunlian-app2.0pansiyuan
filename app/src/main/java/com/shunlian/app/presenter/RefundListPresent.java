package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.RefundAfterSaleAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RefundListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.view.IRefundListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundListPresent extends BasePresenter<IRefundListView> {

    public static final int PAGE_SIZE = 10;
    private RefundAfterSaleAdapter afterSaleAdapter;
    private List<RefundListEntity.RefundList> refundLists = new ArrayList<>();


    public RefundListPresent(Context context, IRefundListView iView) {
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
    protected void initApi() {
        refundlist(true);
    }

    private void refundlist(boolean isShowLoading) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<RefundListEntity>> baseEntityCall = getApiService().refundList(map);
        getNetData(isShowLoading,baseEntityCall,new SimpleNetDataCallback<BaseEntity<RefundListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RefundListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                RefundListEntity data = entity.data;
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                if (!isEmpty(data.refund_list)){
                    refundLists.addAll(data.refund_list);
                }
                setAdapter();
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }
        });
    }

    private void setAdapter() {
        if (afterSaleAdapter == null) {
            afterSaleAdapter = new RefundAfterSaleAdapter(context, true, refundLists);
            iView.setAdapter(afterSaleAdapter);
            afterSaleAdapter.setPageLoading(currentPage,allPage);
            afterSaleAdapter.setOnItemClickListener((view,position)->
                    ExchangeDetailAct.startAct(context,refundLists.get(position).refund_id));
        }else {
            if (refundLists.size() <= RefundListPresent.PAGE_SIZE){
                afterSaleAdapter.notifyDataSetChanged();
            }else {
                afterSaleAdapter.notifyItemInserted(RefundListPresent.PAGE_SIZE);
            }
            afterSaleAdapter.setPageLoading(currentPage,allPage);
        }

        if (!isEmpty(refundLists))
            iView.showDataEmptyView(0);
        else
            iView.showDataEmptyView(100);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            isLoading = true;
            if (currentPage < allPage){
                refundlist(false);
            }
        }
    }
}
