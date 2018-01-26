package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICollectionSearchResultView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/25.
 */

public class SearchResultPresenter extends BasePresenter<ICollectionSearchResultView> {


    private String mKeyword;
    private String mType;
    private final int page_size = 10;
    public static final int FIRST_NET_FAIL = 500;//网络错误
    public static final int OTHER_NET_FAIL = 600;//网络错误

    public SearchResultPresenter(Context context, ICollectionSearchResultView iView,
                                 String keyword, String type) {
        super(context, iView);
        mKeyword = keyword;
        mType = type;
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
        requestSearchApi(true,FIRST_NET_FAIL);
    }

    public void againRequest(){
        currentPage = 1;
        allPage = 1;
        requestSearchApi(true,FIRST_NET_FAIL);
    }

    private void requestSearchApi(boolean isShowLoading,int netFail) {
        Map<String,String> map = new HashMap<>();
        map.put("key_words",mKeyword);
        map.put("type",mType);
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(page_size));
        sortAndMD5(map);

        if ("goods".equals(mType)){//商品搜索
            Call<BaseEntity<CollectionGoodsEntity>> baseEntityCall = getAddCookieApiService()
                    .collectionGoodsSearch(getRequestBody(map));
            getNetData(0,netFail,isShowLoading,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CollectionGoodsEntity>>(){
                @Override
                public void onSuccess(BaseEntity<CollectionGoodsEntity> entity) {
                    super.onSuccess(entity);
                    isLoading = false;
                    CollectionGoodsEntity data = entity.data;
                    currentPage = Integer.parseInt(data.page);
                    LogUtil.zhLogW("currentPage====="+currentPage);
                    allPage = Integer.parseInt(data.total_page);
                    iView.collectionGoodsList(currentPage, allPage, data.goods);
                    currentPage++;
                }

                @Override
                public void onErrorCode(int code, String message) {
                    super.onErrorCode(code, message);
                    isLoading = false;
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    isLoading = false;
                }
            });
        }else {//店铺搜索
            Call<BaseEntity<CollectionStoresEntity>> baseEntityCall = getAddCookieApiService()
                    .collectionStoreSearch(getRequestBody(map));
            getNetData(0,netFail,isShowLoading,baseEntityCall,new SimpleNetDataCallback
                    <BaseEntity<CollectionStoresEntity>>(){
                @Override
                public void onSuccess(BaseEntity<CollectionStoresEntity> entity) {
                    super.onSuccess(entity);
                    isLoading = false;

                    CollectionStoresEntity data = entity.data;
                    currentPage = Integer.parseInt(data.page);
                    LogUtil.zhLogW("currentPage====="+currentPage);
                    allPage = Integer.parseInt(data.total_page);
                    iView.collectionStoresList(currentPage, allPage, data.stores);
                    currentPage++;
                }

                @Override
                public void onErrorCode(int code, String message) {
                    super.onErrorCode(code, message);
                    isLoading = false;
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    isLoading = false;
                }
            });
        }
    }

    /**
     * 获取商品属性
     *
     * @param goods_id
     */
    public void getGoodsSku(final String goods_id) {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);

        Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> goodsSku = getAddCookieApiService()
                .getGoodsSku(getRequestBody(map));
        getNetData(true, goodsSku, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.GoodsInfo>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.GoodsInfo> entity) {
                super.onSuccess(entity);
                GoodsDeatilEntity.Goods goods = new GoodsDeatilEntity.Goods();
                goods.goods_info = entity.data;
                goods.thumb = entity.data.thumb;
                goods.stock = entity.data.stock;
                goods.price = entity.data.price;
                goods.goods_id = goods_id;
                iView.showGoodsSku(goods);
            }
        });
    }

    /**
     * 添加购物车
     * @param goods_id
     * @param sku_id
     * @param qty
     */
    public void addCart(String goods_id,String sku_id,String qty){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("sku_id",sku_id);
        map.put("qty",qty);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CateEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
            }
        });

    }

    /**
     * 移除收藏
     * @param ids
     */
    public void goodsFavRemove(String ids){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("ids",ids);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> goodsfavorite = getAddCookieApiService().goodsfavoriteRemove(requestBody);
        getNetData(goodsfavorite,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess();
                Common.staticToast(entity.message);
            }
        });
    }

    /**
     * 移除收藏
     * @param ids
     */
    public void storesFavRemove(String ids){
        if (Common.loginPrompt()){
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("ids",ids);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().removeFavoShop(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess();
                Common.staticToast(entity.message);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                requestSearchApi(false,OTHER_NET_FAIL);
            }
        }
    }
}
