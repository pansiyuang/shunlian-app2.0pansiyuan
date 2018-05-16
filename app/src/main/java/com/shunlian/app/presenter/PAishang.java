package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IAishang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PAishang extends BasePresenter<IAishang> {
    private int pageSize = 20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading, isRest = false;
    private List<CoreHotEntity.Hot.Goods> hotDatas = new ArrayList<>();
    private List<CoreNewsEntity.Goods> newDatas = new ArrayList<>();
    private List<HotRdEntity.MData> pushDatas = new ArrayList<>();

    public PAishang(Context context, IAishang iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void resetBaby(String type, String cate_id) {
        babyPage = 1;
        babyIsLoading = true;
        switch (type) {
            case "hot":
                hotDatas.clear();
                getCoreHots(cate_id);
                break;
            case "new":
                isRest = true;
                getCoreNews(cate_id);
                break;
//            case "push":
//                hotDatas.clear();
//                getHotRd(cate_id);
//                break;
        }
    }

    public void refreshBaby(String type, String cate_id) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            switch (type) {
                case "hot":
                    getCoreHots(cate_id);
                    break;
                case "new":
                    getCoreNews(cate_id);
                    break;
                case "push":
                    getHotRd(cate_id);
                    break;
//            case "hot":
//                break;
            }
        }
    }

    @Override
    protected void initApi() {

    }

    public void getCoreNew() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "xp");
        sortAndMD5(map);

        Call<BaseEntity<CoreNewEntity>> baseEntityCall = getApiService().coreNew(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CoreNewEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CoreNewEntity> entity) {
                super.onSuccess(entity);
                iView.setNewData(entity.data);
            }
        });
    }

    public void getHotRd(String hotId) {
        Map<String, String> map = new HashMap<>();
        map.put("hotId", hotId);
        map.put("page", String.valueOf(babyPage));
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<HotRdEntity>> baseEntityCall = getApiService().hotPush(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotRdEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotRdEntity> entity) {
                super.onSuccess(entity);
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(entity.data.allPage);
                pushDatas.addAll(entity.data.datas);
                iView.setPushData(pushDatas,entity.data);
            }
        });
    }

    public void getCorePing() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "dp");
        sortAndMD5(map);

        Call<BaseEntity<CorePingEntity>> baseEntityCall = getApiService().corePing(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CorePingEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CorePingEntity> entity) {
                super.onSuccess(entity);
                iView.setPingData(entity.data);
            }
        });
    }

    public void getCoreHot() {
        Map<String, String> map = new HashMap<>();
        map.put("type", "rp");
        sortAndMD5(map);

        Call<BaseEntity<CoreHotEntity>> baseEntityCall = getApiService().coreHot(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CoreHotEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CoreHotEntity> entity) {
                super.onSuccess(entity);
                iView.setHotData(entity.data);
            }
        });
    }

    private void getCoreHots(String cate_id) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        map.put("cate_id", cate_id);
        sortAndMD5(map);

        Call<BaseEntity<CoreHotEntity>> baseEntityCall = getApiService().hotGoodsCate(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CoreHotEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CoreHotEntity> entity) {
                super.onSuccess(entity);
                CoreHotEntity.Hot hot = entity.data.hot_goods;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(hot.total_page);
                hotDatas.addAll(hot.goods_list);
                iView.setHotsData(hotDatas, hot.page, hot.total_page);
            }
        });
    }

    private void getCoreNews(String cate_id) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        map.put("cate_id", cate_id);
        sortAndMD5(map);

        Call<BaseEntity<CoreNewsEntity>> baseEntityCall = getApiService().newGoodsCate(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CoreNewsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CoreNewsEntity> entity) {
                super.onSuccess(entity);
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(entity.data.total_page);
                if (isRest) {//此处写解决闪屏
                    newDatas.clear();
                    isRest = false;
                }
                newDatas.addAll(entity.data.goods_list);
                iView.setNewsData(newDatas, entity.data.page, entity.data.total_page);
            }
        });
    }
}
