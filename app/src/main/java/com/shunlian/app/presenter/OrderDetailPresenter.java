package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.confirm_order.SearchOrderResultActivity;
import com.shunlian.app.ui.my_comment.SuccessfulTradeAct;
import com.shunlian.app.ui.order.AllFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.OrderdetailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class OrderDetailPresenter extends BasePresenter<OrderdetailView> {
    private String order_id;
    private OrderdetailEntity orderdetailEntity;

    public OrderDetailPresenter(Context context, OrderdetailView iView, String order_id) {
        super(context, iView);
        this.order_id = order_id;
        initApiData();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {

    }

    public void initApiData(){
        Map<String, String> map = new HashMap<>();
        map.put("order_id", order_id);
        sortAndMD5(map);
        Call<BaseEntity<OrderdetailEntity>> baseEntityCall = getApiService().orderdetail(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<OrderdetailEntity>>() {
            @Override
            public void onSuccess(BaseEntity<OrderdetailEntity> entity) {
                super.onSuccess(entity);
                orderdetailEntity= entity.data;
                iView.setOrder(orderdetailEntity);
            }
        });
    }
    /**
     * 取消订单
     */
    public void cancleOrder(String order_id,int reason){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        map.put("reason",String.valueOf(reason));
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().cancleOrder(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                AllFrag.isRefreshItem = true;
                SearchOrderResultActivity.isRefreshItem = true;
                initApiData();
            }
        });
    }

    /**
     * 提醒发货
     * @param order_id
     */
    public void remindseller(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().remindseller(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
            }
        });
    }

    /**
     * 延长收货
     * @param order_id
     */
    public void postpone(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().postpone(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                AllFrag.isRefreshItem = true;
                SearchOrderResultActivity.isRefreshItem = true;
                initApiData();
            }
        });
    }

    /**
     * 确认收货
     * @param order_id
     */
    public void confirmreceipt(String order_id){
        Map<String,String> map = new HashMap<>();
        map.put("order_id",order_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().confirmreceipt(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null){
                    Common.staticToast(data.message);
                }
                AllFrag.isRefreshItem = true;
                SearchOrderResultActivity.isRefreshItem = true;

                ArrayList<ReleaseCommentEntity> entities = new ArrayList<>();
                for (int i = 0; i < orderdetailEntity.order_goods.size(); i++) {
                    OrderdetailEntity.Good bean = orderdetailEntity.order_goods.get(i);
                    ReleaseCommentEntity commentEntity = new ReleaseCommentEntity(order_id, bean.thumb, bean.title, bean.price, bean.goods_id);
                    commentEntity.order_sn = orderdetailEntity.order_sn;
                    entities.add(commentEntity);
                }
                SuccessfulTradeAct.startAct(context,entities,order_id);
                initApiData();
            }
        });
    }

    /**
     * 获取商家客服Id
     * @param storeId
     */

    public void getUserId(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getUserId(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getUserId(commonEntity.user_id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
