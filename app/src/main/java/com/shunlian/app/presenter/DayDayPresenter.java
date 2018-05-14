package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.DayDayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class DayDayPresenter extends BasePresenter<DayDayView> {
    private int babyPage = 1;//当前页数
    private final int count = 20;//获取数量
    private String babyId = "";
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<ActivityListEntity.MData.Good.MList> babyDatas = new ArrayList<>();

    public DayDayPresenter(Context context, DayDayView iView) {
        super(context, iView);
    }

    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            initApiData(babyId, babyPage, count);
        }
    }

    public void resetBaby(String id) {
        babyPage = 1;
        babyId = id;
        babyIsLoading = true;
        babyDatas.clear();
        initApiData(id, babyPage, count);
    }

    public void initApiData(String id,int page,int pageSize) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("page", String.valueOf(page));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<ActivityListEntity>> baseEntityCall = getApiService().activityList(map);

        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ActivityListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ActivityListEntity> entity) {
                super.onSuccess(entity);
                ActivityListEntity data = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.datas.goods.allPage);
                babyDatas.addAll(data.datas.goods.list);
                iView.getApiData(data, babyAllPage,babyPage,babyDatas);
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
    /**
     * 设置提醒
     */
    public void settingRemind(String id, String goods_id, final int position){
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("goods_id",goods_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .actRemindMe(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                babyDatas.get(position).remind_status="1";
                iView.activityState(position);
                Common.staticToasts(context,context.getString(R.string.day_set_remind),context.getString(R.string.day_jiangzaikaiqiang),R.mipmap.icon_common_duihao);
            }
        });
    }

    /**
     * 取消提醒
     */
    public void cancleRemind(String id,String goods_id,final int position){
        Map<String,String> map = new HashMap<>();
        map.put("id",id);
        map.put("goods_id",goods_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .cancleRemind(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                babyDatas.get(position).remind_status="0";
                iView.activityState(position);
                Common.staticToasts(context,context.getString(R.string.day_cancel_remind),R.mipmap.icon_common_tanhao);
            }
        });
    }
    @Override
    protected void initApi() {

    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
}
