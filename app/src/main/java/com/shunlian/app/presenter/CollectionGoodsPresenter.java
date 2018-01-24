package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
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

        Call<BaseEntity<CollectionGoodsEntity>> baseEntityCall = getAddCookieApiService()
                .favoriteGoods(getRequestBody(map));

        getNetData(0,netFail,isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CollectionGoodsEntity>>() {
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
    public void getGoodsSku(String goods_id) {
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
}
