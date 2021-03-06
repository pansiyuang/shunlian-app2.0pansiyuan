package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICollectionGoodsView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/22.
 */

public class CollectionGoodsPresenter extends BasePresenter<ICollectionGoodsView> {
    private final int page_size = 10;
    private String cate_id = "0";
    private String is_cut = "0";
    public static final int DISPLAY_NET_FAIL = 100;//显示网络请求失败
    public static final int NOT_DISPLAY_NET_FAIL = 200;//显示网络请求失败
    private Call<BaseEntity<CollectionGoodsEntity>> collectionGoodsListCall;
    private Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> goodsSkuCall;
    private Call<BaseEntity<CommonEntity>> goodsfavoriteCall;

    public CollectionGoodsPresenter(Context context, ICollectionGoodsView iView) {
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
        if (collectionGoodsListCall != null)collectionGoodsListCall.cancel();
        if (goodsfavoriteCall != null)goodsfavoriteCall.cancel();
        if (goodsSkuCall != null)goodsSkuCall.cancel();
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        collectionGoodsList(DISPLAY_NET_FAIL,true, cate_id, is_cut);
    }

    public void sort(){
        currentPage = 1;
        allPage = 1;
        collectionGoodsList(DISPLAY_NET_FAIL,true, cate_id, is_cut);
    }

    public void setCateOrIscut(String cate_id, String is_cut){
        if (!TextUtils.isEmpty(cate_id)){
            this.cate_id = cate_id;
        }
        if (!TextUtils.isEmpty(is_cut)){
            this.is_cut = is_cut;
        }
    }

    private void collectionGoodsList(int netFail ,boolean isShowLoading, String cate_id, String is_cut) {
        Map<String, String> map = new HashMap<>();
        map.put("cate_id", cate_id);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(page_size));
        map.put("is_cut", is_cut);
        sortAndMD5(map);

        collectionGoodsListCall = getAddCookieApiService()
                .favoriteGoods(getRequestBody(map));

        getNetData(0,netFail,isShowLoading, collectionGoodsListCall, new SimpleNetDataCallback<BaseEntity<CollectionGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CollectionGoodsEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                CollectionGoodsEntity data = entity.data;
                iView.collectionGoodsCategoryList(data.cates);
                currentPage = Integer.parseInt(data.page);
                LogUtil.zhLogW("currentPage====="+currentPage);
                allPage = Integer.parseInt(data.total_page);
                iView.collectionGoodsList(currentPage, allPage, data.goods);
                currentPage++;
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
                super.onErrorCode(code, message);
            }
        });
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

        goodsSkuCall = getAddCookieApiService()
                .getGoodsSku(getRequestBody(map));
        getNetData(true, goodsSkuCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.GoodsInfo>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.GoodsInfo> entity) {
                super.onSuccess(entity);
                GoodsDeatilEntity.Goods goods = new GoodsDeatilEntity.Goods();
                goods.goods_info = entity.data;
                goods.thumb = entity.data.thumb;
                goods.stock = entity.data.stock;
                goods.price = entity.data.price;
                goods.type = entity.data.type;
                goods.limit_min_buy = entity.data.limit_min_buy;
                goods.goods_id = goods_id;
                iView.showGoodsSku(goods);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                collectionGoodsList(NOT_DISPLAY_NET_FAIL,false, cate_id, is_cut);
            }
        }
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
        goodsfavoriteCall = getAddCookieApiService().goodsfavoriteRemove(requestBody);
        getNetData(goodsfavoriteCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess();
                Common.staticToast(entity.message);
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
}
