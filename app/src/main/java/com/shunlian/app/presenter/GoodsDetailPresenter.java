package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGoodsDetailView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {


    private String goods_id;

    public GoodsDetailPresenter(Context context, IGoodsDetailView iView, String goods_id) {
        super(context, iView);
        this.goods_id = goods_id;
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<GoodsDeatilEntity>> baseEntityCall = getApiService().goodsDetail(requestBody);
        getNetData(0,0,true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity> entity) {
                super.onSuccess(entity);
                GoodsDeatilEntity data = entity.data;
                if (data != null) {
                    iView.goodsDetailData(data);
                    iView.isFavorite(data.is_fav);
                }
            }
        });
    }

    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /**
     * 取消关注店铺
     * @param storeId
     */
    public void delFollowStore(String storeId){
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void addCart(String goods_id,String sku_id,String qty){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("sku_id",sku_id);
        map.put("qty",qty);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.addCart(entity.message);
            }
        });

    }

    public void footprint(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<FootprintEntity>> baseEntityCall = getAddCookieApiService().footPrint(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<FootprintEntity>>(){
            @Override
            public void onSuccess(BaseEntity<FootprintEntity> entity) {
                super.onSuccess(entity);
                FootprintEntity data = entity.data;
                iView.footprintList(data);
            }
        });
    }

    /**
     * 添加收藏
     * @param goods_id
     */
    public void goodsFavAdd(String goods_id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> goodsfavorite = getAddCookieApiService().goodsfavorite(requestBody);
        getNetData(goodsfavorite,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.isFavorite(entity.data.fid);
                Common.staticToast(entity.message);
            }
        });
    }

    /**
     * 移除收藏
     * @param ids
     */
    public void goodsFavRemove(String ids){
        Map<String,String> map = new HashMap<>();
        map.put("ids",ids);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> goodsfavorite = getAddCookieApiService().goodsfavoriteRemove(requestBody);
        getNetData(goodsfavorite,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.isFavorite(entity.data.fid);
                Common.staticToast(entity.message);
            }
        });
    }

    /**
     * 评价列表
     * @param goods_id
     * @param type
     * @param page
     * @param pageSize
     * @param id
     */
    public void commentList(String goods_id, String type, String page, String pageSize, String id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("type",type);
        map.put("page",page);
        map.put("pageSize",pageSize);
        if (!TextUtils.isEmpty(id)) {
            map.put("id", "1");
        }
        sortAndMD5(map);
        Call<BaseEntity<CommentListEntity>> baseEntityCall = getApiService().commentList(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommentListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommentListEntity> entity) {
                super.onSuccess(entity);
                iView.commentListData(entity.data);
            }
        });
    }
    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
