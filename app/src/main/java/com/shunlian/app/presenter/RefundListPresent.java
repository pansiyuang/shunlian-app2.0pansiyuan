package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RefundListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IRefundListView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundListPresent extends BasePresenter<IRefundListView> {

    private final int PAGE_SIZE = 10;

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
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(PAGE_SIZE));
        sortAndMD5(map);
        Call<BaseEntity<RefundListEntity>> baseEntityCall = getApiService().refundList(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<RefundListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RefundListEntity> entity) {
                super.onSuccess(entity);
                RefundListEntity data = entity.data;
                List<RefundListEntity.RefundList> refund_list = data.refund_list;
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                iView.refundList(refund_list,currentPage,allPage);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            isLoading = true;
            if (currentPage <= allPage){
                currentPage ++;
                initApi();
            }
        }
    }
}
