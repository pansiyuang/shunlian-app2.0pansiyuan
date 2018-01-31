package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICollectionStoresView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/22.
 */

public class CollectionStoresPresenter extends BasePresenter<ICollectionStoresView> {
    private final int page_size = 10;
    private String cate_id = "0";
    public static final int DISPLAY_NET_FAIL = 100;//显示网络请求失败
    public static final int NOT_DISPLAY_NET_FAIL = 200;//显示网络请求失败

    public CollectionStoresPresenter(Context context, ICollectionStoresView iView) {
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
        collectionStoresList(DISPLAY_NET_FAIL,true, cate_id);
    }

    public void sort(){
        currentPage = 1;
        allPage = 1;
        collectionStoresList(DISPLAY_NET_FAIL,true, cate_id);
    }

    public void setCate(String cate_id){
        if (!TextUtils.isEmpty(cate_id)){
            this.cate_id = cate_id;
        }
    }

    private void collectionStoresList(int netFail , boolean isShowLoading, String cate_id) {
        Map<String, String> map = new HashMap<>();
        map.put("cate_id", cate_id);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(page_size));
        sortAndMD5(map);

        Call<BaseEntity<CollectionStoresEntity>> baseEntityCall = getAddCookieApiService()
                .favoriteShop(getRequestBody(map));

        getNetData(0,netFail,isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CollectionStoresEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CollectionStoresEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                CollectionStoresEntity data = entity.data;
                iView.collectionStoresCategoryList(data.cates);
                currentPage = Integer.parseInt(data.page);
                LogUtil.zhLogW("currentPage====="+currentPage);
                allPage = Integer.parseInt(data.total_page);
                iView.collectionStoresList(currentPage, allPage, data.stores);
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                collectionStoresList(NOT_DISPLAY_NET_FAIL,false, cate_id);
            }
        }
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
}
