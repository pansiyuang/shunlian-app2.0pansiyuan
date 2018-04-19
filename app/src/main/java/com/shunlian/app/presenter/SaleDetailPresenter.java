package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.DataDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.SaleDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.sale_data.SaleDetailAct;
import com.shunlian.app.view.ISaleDetailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/13.
 */

public class SaleDetailPresenter extends BasePresenter<ISaleDetailView> {

    private final String page_size = "20";
    private List<SaleDetailEntity.Item> dataItems = new ArrayList<>();
    private DataDetailAdapter adapter;
    public final String type1 = "1";
    public final String type2 = "2";
    public final String type3 = "3";
    public String curType = type3;
    private int mType;
    private String path_name1 = "";
    private String path_name2 = "";


    public SaleDetailPresenter(Context context, ISaleDetailView iView, int type) {
        super(context, iView);
        mType = type;
//        销售详情 和奖励明细 myprofit/rewarddetail   salesdata/salesDetail
        if (mType == SaleDetailAct.SALE_DETAIL){
            path_name1 = "salesdata";
            path_name2 = "salesDetail";
        }else if (mType == SaleDetailAct.REWARD_DETAIL){
            path_name1 = "myprofit";
            path_name2 = "rewarddetail";
        }
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
        currentPage = 1;
        allPage = 1;
        dataItems.clear();
        paging(true);
    }

    private void paging(boolean isShow) {
        Map<String,String> map = new HashMap<>();
        map.put("type",curType);
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",page_size);
        sortAndMD5(map);

        Call<BaseEntity<SaleDetailEntity>> baseEntityCall = getAddCookieApiService()
                .salesDetail(path_name1,path_name2,getRequestBody(map));

        getNetData(isShow,baseEntityCall,new SimpleNetDataCallback<BaseEntity<SaleDetailEntity>>(){
            @Override
            public void onSuccess(BaseEntity<SaleDetailEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;

                SaleDetailEntity data = entity.data;
                SaleDetailEntity.Page pager = data.pager;
                currentPage = Integer.parseInt(pager.page);
                allPage = Integer.parseInt(pager.total_page);
                dataItems.addAll(data.list);
                if (currentPage == 1){
                    if (mType == SaleDetailAct.REWARD_DETAIL){
                        iView.setTotalSale_Profit(null,data.total);
                    }else {
                        iView.setTotalSale_Profit(data.total_sales, data.total_profit);
                    }
                }
                setData();
                currentPage++;
            }
        });
    }

    private void setData() {
        if (adapter == null) {
            adapter = new DataDetailAdapter(context, dataItems,mType);
            iView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            if (currentPage <= allPage){
                isLoading = true;
                paging(false);
            }
        }
    }
}
