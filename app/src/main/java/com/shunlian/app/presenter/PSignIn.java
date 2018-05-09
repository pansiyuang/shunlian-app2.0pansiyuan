package com.shunlian.app.presenter;

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CheckInRespondEntity;
import com.shunlian.app.bean.CheckInStateEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISignInView;
import com.shunlian.app.view.StoreIntroduceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PSignIn extends BasePresenter<ISignInView> {
    private int pageSize=20;
    private boolean isFirst=true;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<CheckInStateEntity.GoodsList.MData> mDatas = new ArrayList<>();

    public PSignIn(Context context, ISignInView iView) {
        super(context, iView);
        getApiData(babyPage);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {

    }
    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiData(babyPage);
        }
    }

    public void getApiData(int page){
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<CheckInStateEntity>> baseEntityCall = getApiService().checkinStatus(map);
        getNetData(isFirst,baseEntityCall, new SimpleNetDataCallback<BaseEntity<CheckInStateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CheckInStateEntity> entity) {
                super.onSuccess(entity);
                CheckInStateEntity checkInStateEntity =entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = checkInStateEntity.goodslist.total_page;
                if (checkInStateEntity.goodslist.list!=null)
                mDatas.addAll(checkInStateEntity.goodslist.list);
                iView.setApiData(checkInStateEntity,mDatas);
                isFirst=false;
            }
        });
    }
    public void sign(){
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CheckInRespondEntity>> baseEntityCall = getApiService().checkinRespond(map);
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<CheckInRespondEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CheckInRespondEntity> entity) {
                super.onSuccess(entity);
                if (entity.data!=null)
                iView.signCallBack(entity.data);
            }
        });
    }
//    /**
//     * 关注店铺
//     *
//     * @param storeId
//     */
//    public void followStore(String storeId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
//        sortAndMD5(map);
//        String stringEntry = null;
//        try {
//            stringEntry = new ObjectMapper().writeValueAsString(map);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
//        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
//        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
//            @Override
//            public void onSuccess(BaseEntity<EmptyEntity> entity) {
//                super.onSuccess(entity);
//                iView.storeFocus();
//                Common.staticToast(entity.message);
//            }
//
//            @Override
//            public void onErrorCode(int code, String message) {
//                super.onErrorCode(code, message);
//                Common.staticToast(message);
//            }
//        });
//    }
//
//    /**
//     * 取消关注店铺
//     *
//     * @param storeId
//     */
//    public void delFollowStore(String storeId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
//        sortAndMD5(map);
//        String stringEntry = null;
//        try {
//            stringEntry = new ObjectMapper().writeValueAsString(map);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
//        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
//        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
//            @Override
//            public void onSuccess(BaseEntity<EmptyEntity> entity) {
//                super.onSuccess(entity);
//                Common.staticToast(entity.message);
//                iView.storeFocus();
//            }
//
//            @Override
//            public void onErrorCode(int code, String message) {
//                super.onErrorCode(code, message);
//                Common.staticToast(message);
//            }
//        });
//    }


}
