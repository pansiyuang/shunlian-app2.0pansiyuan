package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GoodsSearchParam;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.category.CategoryAct;
import com.shunlian.app.ui.category.CategoryFiltrateAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.CategoryFiltrateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class CategoryFiltratePresenter extends BasePresenter<CategoryFiltrateView> {
    public static final int FILTRATE_REQUEST_CODE = 1001;
    public boolean isSecond,isEmpty=true;
    private String cid, keyword;
    private Activity context;

    public CategoryFiltratePresenter(Context context, CategoryFiltrateView iView, String cid, String keyword) {
        super(context, iView);
        this.cid = cid;
        this.keyword = keyword;
        this.context= (Activity) context;
    }

    public void initApiData(){
        Map<String, String> map = new HashMap<>();
        map.put("cid", cid);
        map.put("keyword", keyword);
        sortAndMD5(map);
        Call<BaseEntity<GetListFilterEntity>> baseEntityCall = getApiService().getListFilter(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetListFilterEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetListFilterEntity> entity) {
                super.onSuccess(entity);
                iView.getListFilter(entity.data);
                initGps();
            }
        });
    }
    @Override
    protected void initApi() {

    }

    public void initGps() {
        Location location = Common.getGPS(context);
        if (location != null) {
            Map<String, String> map = new HashMap<>();
            map.put("lng", String.valueOf(location.getLongitude()));
            map.put("lat", String.valueOf(location.getLatitude()));
            sortAndMD5(map);

            RequestBody requestBody = getRequestBody(map);
            Call<BaseEntity<DistrictGetlocationEntity>> baseEntityCall = getAddCookieApiService().districtGetlocation(requestBody);
            getNetData(isSecond, baseEntityCall, new SimpleNetDataCallback<BaseEntity<DistrictGetlocationEntity>>() {
                @Override
                public void onSuccess(BaseEntity<DistrictGetlocationEntity> entity) {
                    super.onSuccess(entity);
                    DistrictGetlocationEntity data = entity.data;
                    if (data != null) {
                        LogUtil.httpLogW("DistrictGetlocationEntity:" + data);
                        iView.getGps(data);
                        isSecond = true;
                    }
                }
            });
        }
    }

    public void dealRecommendBrand(GetListFilterEntity getListFilterEntity) {
        if (Constant.BRAND_IDS==null){
            Constant.BRAND_IDS = new ArrayList<>();
        }
        List<GetListFilterEntity.Recommend> recommends = new ArrayList<>();
        for (int i = 0; i < 11 || i < getListFilterEntity.recommend_brand_list.size(); i++) {
            GetListFilterEntity.Recommend recommend = getListFilterEntity.recommend_brand_list.get(i);
            recommends.add(recommend);
            if (i >= getListFilterEntity.recommend_brand_list.size() - 1 || i >= 10) {
                if (getListFilterEntity.recommend_brand_list.size() > 10) {
                    recommends.add(getListFilterEntity.recommend_brand_list.get(0));
                }
                iView.initPingpai(recommends);
            }
        }
    }

    public void dealBrandIds(GoodsSearchParam goodsSearchParam) {
        String brand_ids = "";
        if (Constant.BRAND_IDS!=null&&Constant.BRAND_IDS.size() > 0) {
            for (int m = 0; m < Constant.BRAND_IDS.size(); m++) {
                if (m >= Constant.BRAND_IDS.size() - 1) {
                    brand_ids += Constant.BRAND_IDS.get(m);
                    goodsSearchParam.brand_ids = brand_ids;
                    dealBrandAttrs(goodsSearchParam);
                } else {
                    brand_ids += Constant.BRAND_IDS.get(m) + ",";
                }
            }
        } else {
            dealBrandAttrs(goodsSearchParam);
        }
    }

    private void dealBrandAttrs(GoodsSearchParam goodsSearchParam) {
        if (Constant.BRAND_ATTRNAME!=null&&Constant.BRAND_ATTRNAME.size() > 0) {
            List<GoodsSearchParam.Attr> attrs = new ArrayList<>();
            for (int n = 0; n < Constant.BRAND_ATTRNAME.size(); n++) {
                GoodsSearchParam.Attr attr = new GoodsSearchParam.Attr();
                String name = Constant.BRAND_ATTRNAME.get(n);
                attr.attr_name = name;
                if (Constant.BRAND_ATTRS.get(name)!=null&&Constant.BRAND_ATTRS.get(name).size()>0){
                    isEmpty=false;
                    attr.attr_vals = Constant.BRAND_ATTRS.get(name);
                }
                attrs.add(attr);
                if (n >= Constant.BRAND_ATTRNAME.size() - 1) {
                    if (!isEmpty){
                        goodsSearchParam.attr_data = attrs;
                    }else if(goodsSearchParam.attr_data!=null){
                        goodsSearchParam.attr_data.clear();
                    }
                    Constant.SEARCHPARAM=goodsSearchParam;
                    if (Constant.REBRAND_IDS==null){
                        Constant.REBRAND_IDS=new ArrayList<>();
                    }else {
                        Constant.REBRAND_IDS.clear();
                    }
                    Constant.REBRAND_IDS.addAll(Constant.BRAND_IDS);
                    if (Constant.REBRAND_ATTRS==null){
                        Constant.REBRAND_ATTRS=new HashMap<>();
                    }else {
                        Constant.REBRAND_ATTRS.clear();
                    }
                    Constant.REBRAND_ATTRS.putAll(Constant.BRAND_ATTRS);
                    Intent intent = new Intent();
                    intent.putExtra("searchparam",Constant.SEARCHPARAM);
                    context.setResult(Activity.RESULT_OK, intent);
                    context.finish();
                }
            }
        }else {
            Constant.SEARCHPARAM=goodsSearchParam;
            Intent intent = new Intent();
            intent.putExtra("searchparam",Constant.SEARCHPARAM);
            context.setResult(Activity.RESULT_OK, intent);
            context.finish();
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
