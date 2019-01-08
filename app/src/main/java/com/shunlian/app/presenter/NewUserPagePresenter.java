package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.AdUserEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.UserNewDataEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMyPageView;
import com.shunlian.app.view.INewUserGoodsView;
import com.shunlian.app.view.INewUserPageView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/11/5.
 */

public class NewUserPagePresenter extends BasePresenter<INewUserPageView> {

    public NewUserPagePresenter(Context context, INewUserPageView iView) {
        super(context, iView);
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

    public void getBubble() {
        Map<String, String> map = new HashMap<>();
        map.put("position", "2");
        sortAndMD5(map);

        Call<BaseEntity<BubbleEntity>> baseEntityCall = getApiService().getBubble(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<BubbleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<BubbleEntity> entity) {
                super.onSuccess(entity);
                BubbleEntity data = entity.data;
                if (data != null) {
                    iView.setBubble(data);
                }else {
                    iView.showFailureView(666);
                }
            }

            @Override
            public void onErrorData(BaseEntity<BubbleEntity> bubbleEntityBaseEntity) {
                super.onErrorData(bubbleEntityBaseEntity);
                iView.showFailureView(666);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(666);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code,message);
                iView.showFailureView(666);
            }

        });
    }
    public void adlist() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<AdUserEntity>> setinfo = getAddCookieApiService().adlist(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<AdUserEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdUserEntity> entity) {
                super.onSuccess(entity);
                iView.bannerList(entity.data.list,entity.data.isNew);
            }
        });
    }

    public void cartlist(boolean isGoBuy) {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<NewUserGoodsEntity>> setinfo = getAddCookieApiService().cartlist(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<NewUserGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<NewUserGoodsEntity> entity) {
                super.onSuccess(entity);
                iView.goodCartList(entity.data.list,isGoBuy);
            }
        });
    }

    public void deletecart(String cid) {
        Map<String, String> map = new HashMap<>();
        map.put("cid",cid);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> setinfo = getAddCookieApiService().deletecart(map);
        getNetData(false, setinfo, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.delCart(cid);
            }
        });
    }


    public void getvoucher() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<UserNewDataEntity>> setinfo = getAddCookieApiService().getvoucher(map);
        getNetData(false, setinfo, new SimpleNetDataCallback<BaseEntity<UserNewDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UserNewDataEntity> entity) {
                super.onSuccess(entity);
                iView.getvoucher(entity.data);
            }
            @Override
            public void onErrorCode(int code, String message) {
//                super.onErrorCode(code, message);
                iView.getOldMessage(message);
            }
        });
    }

    public void showVoucherSuspension() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<ShowVoucherSuspension>> setinfo = getAddCookieApiService().showVoucherSuspension(map);
        getNetData(true, setinfo, new SimpleNetDataCallback<BaseEntity<ShowVoucherSuspension>>() {
            @Override
            public void onSuccess(BaseEntity<ShowVoucherSuspension> entity) {
                super.onSuccess(entity);
                iView.showVoucherSuspension(entity.data);
            }
        });
    }
}
