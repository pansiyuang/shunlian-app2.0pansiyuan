package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.new_user.NewUserPageActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ICollectionGoodsView;
import com.shunlian.app.view.INewUserGoodsView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NewUserGoodsPresenter extends BasePresenter<INewUserGoodsView> {
    private final int page_size = 10;
    private String cate_id = "0";
    private String is_cut = "0";
    public static final int DISPLAY_NET_FAIL = 100;//显示网络请求失败
    public static final int NOT_DISPLAY_NET_FAIL = 200;//显示网络请求失败
    private Call<BaseEntity<NewUserGoodsEntity>> userGoodsListCall;
    private Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> goodsSkuCall;

    private String type = "1";
    public NewUserGoodsPresenter(Context context, INewUserGoodsView iView,String type) {
        super(context, iView);
        this.type = type;
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
        if (userGoodsListCall != null)userGoodsListCall.cancel();
        if (goodsSkuCall != null)goodsSkuCall.cancel();
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        userGoodsList(DISPLAY_NET_FAIL,true, type);
    }

    public void refreshData(){
        currentPage = 1;
        allPage = 1;
        userGoodsList(DISPLAY_NET_FAIL,false, type);
    }


    private void userGoodsList(int netFail ,boolean isShowLoading, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(page_size));
        sortAndMD5(map);

        userGoodsListCall = getAddCookieApiService()
                .usergoodslist(map);

        getNetData(0,netFail,isShowLoading, userGoodsListCall, new SimpleNetDataCallback<BaseEntity<NewUserGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NewUserGoodsEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                NewUserGoodsEntity data = entity.data;
                currentPage = Integer.parseInt(data.page);
                LogUtil.zhLogW("currentPage====="+currentPage);
                if(Integer.parseInt(data.count)<page_size){
                    allPage = currentPage;
                }else{
                    allPage = currentPage+1;
                }
                iView.userGoodsList(currentPage, allPage, data.list);
                if(currentPage==1){
                    iView.refreshFinish();
                    if(type.equals("1")) {
                        ((NewUserPageActivity) context).initCartNum(data.cartTotal);
                    }
                }
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
    public void getGoodsSku(final String goods_id,int postion) {
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
                iView.showGoodsSku(goods,postion);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                userGoodsList(NOT_DISPLAY_NET_FAIL,false, type);
            }
        }
    }

    /**
     * 添加购物车
     * @param goods_id
     * @param sku_id
     * @param qty
     */
    public void addCart(String goods_id,String sku_id,String qty,int postion){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goodsId",goods_id);
        if(!TextUtils.isEmpty(sku_id))
        map.put("skuId",sku_id);
//        map.put("qty",qty);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().newuseraddCart(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CateEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                iView.addCartSuccess(entity.data.cartTotal,postion);
            }
        });

    }
}
