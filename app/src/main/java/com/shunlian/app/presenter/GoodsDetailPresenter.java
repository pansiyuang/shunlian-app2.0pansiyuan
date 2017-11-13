package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IGoodsDetailView;

import java.util.ArrayList;
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
        final Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<GoodsDeatilEntity>> baseEntityCall = getApiService().goodsDetail(requestBody);
            getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity>>(){
                @Override
                public void onSuccess(BaseEntity<GoodsDeatilEntity> entity) {
                    super.onSuccess(entity);
                    GoodsDeatilEntity data = entity.data;
                    if (data != null){
                        ArrayList<String> pics = data.pics;
                        if (pics != null && pics.size() > 0)
                            iView.banner(pics);

                        String title = data.title;
                        String price = data.price;
                        String market_price = data.market_price;
                        String free_shipping = data.free_shipping;
                        String area = data.area;
                        GoodsDeatilEntity.GoodsData goods_data = data.goods_data;
                        String sales = null;
                        if (goods_data != null) {
                            sales = goods_data.sales;
                        }
                        iView.goodsInfo(title,price,market_price,free_shipping,sales,area);

                        iView.smallLabel(data.is_new,data.is_explosion,data.is_hot,data.is_recommend);
                        iView.voucher(data.voucher);
                        iView.shopInfo(data.store_info);
                    }


                }
            });

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
