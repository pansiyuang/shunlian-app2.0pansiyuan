package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IMainPageView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/2/1.
 */

public class MainPagePresenter extends BasePresenter<IMainPageView> {

    private final int goods_page = 10;

    private final int brand_page = 8;

    private int brandPage = 1;
    private int brandAllPage = 1;
    private boolean isLoadingBrand = false;
    private boolean isGoods = true;
    private boolean isBrand = true;


    public MainPagePresenter(Context context, IMainPageView iView) {
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
        pageData(true);
    }

    private void pageData(boolean isShowLoading) {
        Map<String,String> map = new HashMap<>();
        map.put("brand_page",String.valueOf(brandPage));//品牌页码
        map.put("brand_page_size",String.valueOf(brand_page));//品牌页容量
        map.put("goods_page",String.valueOf(currentPage));//精选商品页码
        map.put("goods_page_size",String.valueOf(goods_page));//精选商品页容量
        sortAndMD5(map);

        Call<BaseEntity<MainPageEntity>> baseEntityCall = getApiService().firstPage(map);
        getNetData(isShowLoading,baseEntityCall,new SimpleNetDataCallback<BaseEntity<MainPageEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MainPageEntity> entity) {
                super.onSuccess(entity);
                MainPageEntity data = entity.data;

                banner_day(data);
                if (isBrand) {
                    //brand
                    brand(data);
                }
                if (isGoods) {
                    //goods
                    goods(data);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                isLoadingBrand = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                isLoadingBrand = false;
            }
        });
    }

    private void goods(MainPageEntity data) {
        currentPage = Integer.parseInt(data.recommend_goods.page);
        allPage = Integer.parseInt(data.recommend_goods.total);
        iView.moreGoods(data.recommend_goods);
        currentPage++;
    }

    private void brand(MainPageEntity data) {
        MainPageEntity.RecommendBrands recommend_brands = data.recommend_brands;
        brandPage = Integer.parseInt(recommend_brands.page);
        brandAllPage = Integer.parseInt(recommend_brands.total);
        iView.moreBrands(recommend_brands);
        brandPage++;
    }

    private void banner_day(MainPageEntity data) {
        iView.banner(data.carousel);
        iView.daySpecialAndNewGoods(data.every_day_special,data.new_goods);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            if (currentPage <= allPage){
                isLoading = true;
                isGoods = true;
                isBrand = false;
                pageData(false);
            }
        }
    }

    /**
     * 换一批
     */
    public void onBatch() {
        if (!isLoadingBrand){
            if (brandPage <= brandAllPage){
                isLoadingBrand = true;
                isGoods = false;
                isBrand = true;
                pageData(true);
            }
        }
    }
}
