package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.StoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class StorePresenter extends BasePresenter<StoreView> {
    private final int count = 20;//获取数量
    private String storeId;
    private int babyPage = 1;//当前页数
    private int discountPage = 1;//当前页数
    private String babySort = "default";//评价类型
    private String discountType = "";//评价类型
    private int promotionId;//评价类型
    private int babyAllPage = 0;
    private int discountAllPage = 0;
    private boolean babyIsLoading;
    private boolean discountIsLoading;
    private boolean isDiscountSecond;
    private List<StoreGoodsListEntity.MData> babyDatas = new ArrayList<>();
    private List<StorePromotionGoodsListOneEntity.MData> discountDatas = new ArrayList<>();

    public StorePresenter(Context context, StoreView iView, String storeId) {
        super(context, iView);
        this.storeId = storeId;
        initApi();
    }


    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            initBaby(storeId, babyPage, babySort, count);
        }
    }

    public void refreshDiscountOne() {
        if (!discountIsLoading && discountPage <= discountAllPage) {
            discountIsLoading = true;
            isDiscountSecond=true;
            initDiscountOne(storeId, promotionId, discountType, discountPage, count);
        }
    }

    public void resetBaby(String sort) {
        babyPage = 1;
        babySort = sort;
        babyIsLoading = true;
        babyDatas.clear();
        initBaby(storeId, babyPage, babySort, count);
    }

    public void resetDiscountOne(int id,String type) {
        discountPage = 1;
        promotionId = id;
        discountIsLoading = true;
        discountType=type;
        discountDatas.clear();
        isDiscountSecond=false;
        initDiscountOne(storeId, promotionId, discountType, discountPage, count);
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<StoreIndexEntity>> baseEntityCall = getApiService().storeIndex(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreIndexEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreIndexEntity> entity) {
                super.onSuccess(entity);
                StoreIndexEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("StoreIndexEntity:" + data);
                    iView.storeHeader(data.head);
                    iView.storeVoucher(data.voucher);
                    if (data.body != null) {
                        typeOneHandle(data.body);
                    }
                }
            }
        });
    }

    public void initBaby(String storeId, final int page, String sort, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("page", String.valueOf(page));
        map.put("sort", sort);
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<StoreGoodsListEntity>> baseEntityCall = getApiService().storeGoodsList(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreGoodsListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreGoodsListEntity> entity) {
                super.onSuccess(entity);
                StoreGoodsListEntity data = entity.data;
                if (data != null && data.datas != null && data.datas.size() > 0) {
                    LogUtil.httpLogW("StoreGoodsListEntity:" + data);
                    babyIsLoading = false;
                    babyPage++;
                    babyAllPage = Integer.parseInt(data.allPage);
                    babyDatas.addAll(data.datas);
                    iView.storeBaby(babyDatas, babyAllPage, babyPage);
                }
            }
        });
    }

    public void initNew(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<StoreNewGoodsListEntity>> baseEntityCall = getApiService().storeNewGoodsList(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StoreNewGoodsListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StoreNewGoodsListEntity> entity) {
                super.onSuccess(entity);
                StoreNewGoodsListEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("StoreNewGoodsListEntity:" + data);
                    storeNewHandle(data.data);
                }
            }
        });
    }

    public void initDiscount(final String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<StorePromotionGoodsListEntity>> baseEntityCall = getApiService().storePromotionGoodsList(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StorePromotionGoodsListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StorePromotionGoodsListEntity> entity) {
                super.onSuccess(entity);
                StorePromotionGoodsListEntity data = entity.data;
                if (data != null) {
                    LogUtil.httpLogW("StorePromotionGoodsListEntity:" + data);
                    iView.storeDiscountMenu(data.lable);
                    if ("combo".equals(data.lable.get(0).type)) {
                        initDiscountTwo(storeId, Integer.parseInt(data.lable.get(0).promotionId), data.lable.get(0).type);
                    } else {
                        initDiscountOne(storeId, Integer.parseInt(data.lable.get(0).promotionId), data.lable.get(0).type, 1, count);
                    }
                }
            }
        });
    }

    public void initDiscountOne(String storeId, int promotionId, String type, int page, int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("promotionId", String.valueOf(promotionId));
        map.put("type", type);
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<StorePromotionGoodsListOneEntity>> baseEntityCall = getApiService().storePromotionGoodsListOne(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StorePromotionGoodsListOneEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StorePromotionGoodsListOneEntity> entity) {
                super.onSuccess(entity);
                StorePromotionGoodsListOneEntity data = entity.data;
                if (data != null && data.list != null && data.list.remark != null) {
                    LogUtil.httpLogW("StorePromotionGoodsListOneEntity:" + data);
                    if (!isDiscountSecond){
                        storeDiscountHandle(data.list.goods, data.list.remark);
                    }else {
                        storeDiscountHandles(data.list.goods);
                    }

                }
            }
        });
    }

    public void initDiscountTwo(String storeId, int promotionId, String type) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        map.put("promotionId", String.valueOf(promotionId));
        map.put("type", type);
        sortAndMD5(map);

        Call<BaseEntity<StorePromotionGoodsListTwoEntity>> baseEntityCall = getApiService().storePromotionGoodsListTwo(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<StorePromotionGoodsListTwoEntity>>() {
            @Override
            public void onSuccess(BaseEntity<StorePromotionGoodsListTwoEntity> entity) {
                super.onSuccess(entity);
                StorePromotionGoodsListTwoEntity data = entity.data;
                if (data != null&&data.list!=null) {
                    LogUtil.httpLogW("StorePromotionGoodsListTwoEntity:" + data);
                    iView.storeDiscountTwo(data.list.goods);
                }
            }
        });
    }

    public void storeNewHandle(List<StoreNewGoodsListEntity.Data> dataList) {
        List<StoreNewGoodsListEntity.Data> cbodys = new ArrayList<>();
        cbodys.addAll(dataList);
        int y = 0;
        for (int b = 0; b < dataList.size(); b++) {
            List<StoreNewGoodsListEntity.Data> bbodys = new ArrayList<>();
            for (int c = 0; c < dataList.get(b).data.size(); c = c + 2) {
                StoreNewGoodsListEntity.Data bbody = new StoreNewGoodsListEntity.Data();
                StoreNewGoodsListEntity.Data.MData ldata = new StoreNewGoodsListEntity.Data.MData();
                StoreNewGoodsListEntity.Data.MData rdata = new StoreNewGoodsListEntity.Data.MData();
                if (c == 0) {
                    bbody.day = dataList.get(b).day;
                }
                ldata.id = dataList.get(b).data.get(c).id;
                ldata.title = dataList.get(b).data.get(c).title;
                ldata.thumb = dataList.get(b).data.get(c).thumb;
                ldata.price = dataList.get(b).data.get(c).price;
                ldata.type = dataList.get(b).data.get(c).type;
                ldata.store_id = dataList.get(b).data.get(c).store_id;
                ldata.item_id = dataList.get(b).data.get(c).item_id;
                ldata.market_price = dataList.get(b).data.get(c).market_price;
                ldata.update_time = dataList.get(b).data.get(c).update_time;
                ldata.whole_thumb = dataList.get(b).data.get(c).whole_thumb;
                if ((c + 1) < dataList.get(b).data.size()) {
                    rdata.id = dataList.get(b).data.get(c + 1).id;
                    rdata.title = dataList.get(b).data.get(c + 1).title;
                    rdata.thumb = dataList.get(b).data.get(c + 1).thumb;
                    rdata.price = dataList.get(b).data.get(c + 1).price;
                    rdata.type = dataList.get(b).data.get(c + 1).type;
                    rdata.store_id = dataList.get(b).data.get(c + 1).store_id;
                    rdata.item_id = dataList.get(b).data.get(c + 1).item_id;
                    rdata.market_price = dataList.get(b).data.get(c + 1).market_price;
                    rdata.update_time = dataList.get(b).data.get(c + 1).update_time;
                    rdata.whole_thumb = dataList.get(b).data.get(c + 1).whole_thumb;
                }
                bbody.mDatal = ldata;
                bbody.mDatar = rdata;
                bbodys.add(bbody);
                if (c >= dataList.get(b).data.size() - 2) {
                    int x = b + y;
                    cbodys.remove(x);
                    cbodys.addAll(x, bbodys);
                    y = y + bbodys.size() - 1;
                    if (b >= dataList.size() - 1) {
                        iView.storeNew(cbodys);
                    }
                }
            }
        }
    }

    public void storeDiscountHandles(StorePromotionGoodsListOneEntity.Lists.Good good) {
        for (int c = 0; c < good.data.size(); c = c + 2) {
            StorePromotionGoodsListOneEntity.MData cData = new StorePromotionGoodsListOneEntity.MData();
            StorePromotionGoodsListOneEntity.Lists.Good.Data ldata = new StorePromotionGoodsListOneEntity.Lists.Good.Data();
            StorePromotionGoodsListOneEntity.Lists.Good.Data rdata = new StorePromotionGoodsListOneEntity.Lists.Good.Data();
            cData.allPage = good.allPage;
            cData.type = "3";
            ldata.id = good.data.get(c).id;
            ldata.title = good.data.get(c).title;
            ldata.thumb = good.data.get(c).thumb;
            ldata.price = good.data.get(c).price;
            ldata.type = good.data.get(c).type;
            ldata.store_id = good.data.get(c).store_id;
            ldata.item_id = good.data.get(c).item_id;
            ldata.market_price = good.data.get(c).market_price;
            ldata.whole_thumb = good.data.get(c).whole_thumb;
            ldata.giftGoodsName = good.data.get(c).giftGoodsName;
            if ((c + 1) < good.data.size()) {
                rdata.id = good.data.get(c + 1).id;
                rdata.title = good.data.get(c + 1).title;
                rdata.thumb = good.data.get(c + 1).thumb;
                rdata.price = good.data.get(c + 1).price;
                rdata.type = good.data.get(c + 1).type;
                rdata.store_id = good.data.get(c + 1).store_id;
                rdata.item_id = good.data.get(c + 1).item_id;
                rdata.market_price = good.data.get(c + 1).market_price;
                rdata.whole_thumb = good.data.get(c + 1).whole_thumb;
                rdata.giftGoodsName = good.data.get(c + 1).giftGoodsName;
            }
            cData.ldata = ldata;
            cData.rdata = rdata;
            discountDatas.add(cData);
            if (c >= good.data.size() - 2) {
                discountIsLoading = false;
                discountPage++;
                discountAllPage = Integer.parseInt(discountDatas.get(0).allPage);
                iView.storeDiscountOne(discountDatas, discountAllPage, discountPage);
            }
        }
    }

    public void storeDiscountHandle(StorePromotionGoodsListOneEntity.Lists.Good good, List<String> remarks) {
        if (remarks.size() > 0) {
            for (int m = 0; m < remarks.size(); m++) {
                StorePromotionGoodsListOneEntity.MData mData = new StorePromotionGoodsListOneEntity.MData();
                mData.type = "2";
                mData.mRemark = remarks.get(m);
                mData.allPage = good.allPage;
                discountDatas.add(mData);
                if (m >= remarks.size() - 1) {
                    storeDiscountHandles(good);
                }
            }
        } else {
            storeDiscountHandles(good);
        }
    }

    public void typeTwoHandle(List<StoreIndexEntity.Body> cbodys) {
        List<StoreIndexEntity.Body> ebodys = new ArrayList<>();
        for (int e = 0; e < cbodys.size(); e++) {
            if ("2".equals(cbodys.get(e).block_module_type)) {
                StoreIndexEntity.Body ebody = new StoreIndexEntity.Body();
                ebody.name = String.valueOf(e);
                ebody.data = cbodys.get(e).data;
                ebody.title = cbodys.get(e).title;
                ebodys.add(ebody);
            }
            if (e >= cbodys.size() - 1) {
                if (ebodys.size() > 0) {
                    List<StoreIndexEntity.Body> fbodys = new ArrayList<>();
                    fbodys.addAll(cbodys);
                    int z = 0;
                    for (int f = 0; f < ebodys.size(); f++) {
                        List<StoreIndexEntity.Body> gbodys = new ArrayList<>();
                        for (int g = 0; g < ebodys.get(f).data.size(); g++) {
                            StoreIndexEntity.Body fbody = new StoreIndexEntity.Body();
                            StoreIndexEntity.Body.Datas adata = new StoreIndexEntity.Body.Datas();
                            if (g == 0) {
                                fbody.title = ebodys.get(f).title;
                            }
                            fbody.block_module_type = "2";
                            adata.id = ebodys.get(f).data.get(g).id;
                            adata.title = ebodys.get(f).data.get(g).title;
                            adata.thumb = ebodys.get(f).data.get(g).thumb;
                            adata.sales = ebodys.get(f).data.get(g).sales;
                            adata.price = ebodys.get(f).data.get(g).price;
                            adata.type = ebodys.get(f).data.get(g).type;
                            adata.store_id = ebodys.get(f).data.get(g).store_id;
                            adata.item_id = ebodys.get(f).data.get(g).item_id;
                            adata.url = ebodys.get(f).data.get(g).url;
                            adata.whole_thumb = ebodys.get(f).data.get(g).whole_thumb;
                            adata.label = ebodys.get(f).data.get(g).label;
                            fbody.ldata = adata;
                            gbodys.add(fbody);
                            if (g >= ebodys.get(f).data.size() - 1) {
                                int p = Integer.parseInt(ebodys.get(f).name) + z;
                                fbodys.remove(p);
                                fbodys.addAll(p, gbodys);
                                z = z + gbodys.size() - 1;
                                if (f >= ebodys.size() - 1) {
                                    iView.storeFirst(fbodys);
                                }
                            }
                        }
                    }
                } else {
                    iView.storeFirst(cbodys);
                }
            }
        }
    }

    public void typeOneHandle(List<StoreIndexEntity.Body> bodies) {
        List<StoreIndexEntity.Body> abodys = new ArrayList<>();
        for (int a = 0; a < bodies.size(); a++) {
            if ("1".equals(bodies.get(a).block_module_type)) {
                StoreIndexEntity.Body abody = new StoreIndexEntity.Body();
                abody.name = String.valueOf(a);
                abody.data = bodies.get(a).data;
                abody.title = bodies.get(a).title;
                abodys.add(abody);
            }
            if (a >= bodies.size() - 1) {
                if (abodys.size() > 0) {
                    List<StoreIndexEntity.Body> cbodys = new ArrayList<>();
                    cbodys.addAll(bodies);
                    int y = 0;
                    for (int b = 0; b < abodys.size(); b++) {
                        List<StoreIndexEntity.Body> bbodys = new ArrayList<>();
                        for (int c = 0; c < abodys.get(b).data.size(); c = c + 2) {
                            StoreIndexEntity.Body bbody = new StoreIndexEntity.Body();
                            StoreIndexEntity.Body.Datas ldata = new StoreIndexEntity.Body.Datas();
                            StoreIndexEntity.Body.Datas rdata = new StoreIndexEntity.Body.Datas();
                            if (c == 0) {
                                bbody.title = abodys.get(b).title;
                            }
                            bbody.block_module_type = "1";
                            ldata.id = abodys.get(b).data.get(c).id;
                            ldata.title = abodys.get(b).data.get(c).title;
                            ldata.thumb = abodys.get(b).data.get(c).thumb;
                            ldata.sales = abodys.get(b).data.get(c).sales;
                            ldata.price = abodys.get(b).data.get(c).price;
                            ldata.type = abodys.get(b).data.get(c).type;
                            ldata.store_id = abodys.get(b).data.get(c).store_id;
                            ldata.item_id = abodys.get(b).data.get(c).item_id;
                            ldata.url = abodys.get(b).data.get(c).url;
                            ldata.whole_thumb = abodys.get(b).data.get(c).whole_thumb;
                            if ((c + 1) < abodys.get(b).data.size()) {
                                rdata.id = abodys.get(b).data.get(c + 1).id;
                                rdata.title = abodys.get(b).data.get(c + 1).title;
                                rdata.thumb = abodys.get(b).data.get(c + 1).thumb;
                                rdata.sales = abodys.get(b).data.get(c + 1).sales;
                                rdata.price = abodys.get(b).data.get(c + 1).price;
                                rdata.type = abodys.get(b).data.get(c + 1).type;
                                rdata.store_id = abodys.get(b).data.get(c + 1).store_id;
                                rdata.label = abodys.get(b).data.get(c + 1).label;
                                rdata.item_id = abodys.get(b).data.get(c + 1).item_id;
                                rdata.url = abodys.get(b).data.get(c + 1).url;
                                rdata.whole_thumb = abodys.get(b).data.get(c + 1).whole_thumb;
                            }
                            bbody.ldata = ldata;
                            bbody.rdata = rdata;
                            bbodys.add(bbody);
                            if (c >= abodys.get(b).data.size() - 2) {
                                int x = Integer.parseInt(abodys.get(b).name) + y;
                                cbodys.remove(x);
                                cbodys.addAll(x, bbodys);
                                y = y + bbodys.size() - 1;
                                if (b >= abodys.size() - 1) {
                                    typeTwoHandle(cbodys);
                                }
                            }
                        }
                    }
                } else {
                    typeTwoHandle(bodies);
                }
            }
        }
    }

    /**
     * 关注店铺
     *
     * @param storeId
     */
    public void followStore(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.storeFocus();
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
     *
     * @param storeId
     */
    public void delFollowStore(String storeId) {
        Map<String, String> map = new HashMap<>();
        map.put("storeId", storeId);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.storeFocus();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
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
