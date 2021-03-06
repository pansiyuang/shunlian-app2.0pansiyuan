package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.PersonalDataEntity;
import com.shunlian.app.bean.PunishEntity;
import com.shunlian.app.bean.ShowSignEntity;
import com.shunlian.app.bean.ShowVoucherSuspension;
import com.shunlian.app.bean.TeamCodeInfoEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IMain;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PMain extends BasePresenter<IMain> {
    public PMain(Context context, IMain iView) {
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

    public void entryInfo(){
        Map<String,String> map = new HashMap<>();
//        map.put("word",word);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().entryInfo(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.entryInfo(data);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                JPushInterface.resumePush(Common.getApplicationContext());
            }

            @Override
            public void onErrorData(BaseEntity<CommonEntity> commonEntityBaseEntity) {
                super.onErrorData(commonEntityBaseEntity);
                JPushInterface.resumePush(Common.getApplicationContext());
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                JPushInterface.resumePush(Common.getApplicationContext());
            }
        });
    }

    public void getCommond(String word){
        Map<String,String> map = new HashMap<>();
        map.put("word",word);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommondEntity>> baseEntityCall = getAddCookieApiService().parseSecretWord(requestBody);

        getNetData(false,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommondEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommondEntity> entity) {
                super.onSuccess(entity);
                CommondEntity data = entity.data;
                if (data != null) {
                    if(data.type.equals("carveUpEgg")){
                            iView.setReadPassword(data);
                    }else {
                            iView.setCommond(data);
                    }
                }
            }
        });
    }

    /**
     * 处理网络请求
     */
    public void loginUserInfo() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<PersonalDataEntity>> baseEntityCall = getApiService().personalData(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<PersonalDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<PersonalDataEntity> entity) {
                super.onSuccess(entity);
                PersonalDataEntity data = entity.data;
                SharedPrefUtil.saveSharedUserString("nickname", data.nickname);
                SharedPrefUtil.saveSharedUserString("avatar", data.avatar);
//                SharedPrefUtil.saveSharedUserString("token", data.token);
//                SharedPrefUtil.saveSharedUserString("plus_role", data.plus_role);
//                SharedPrefUtil.saveSharedUserString("refresh_token", data.refresh_token);
//                SharedPrefUtil.saveSharedUserString("member_id", data.member_id);
            }
        });
    }

    public void getSplashAD() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<AdEntity>> baseEntityCall = getApiService().splashScreen(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AdEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdEntity> entity) {
                super.onSuccess(entity);
                AdEntity data = entity.data;
                if (data != null) {
                    iView.setAD(data);
                }
            }
        });
    }

    public void isShowNewPersonPrize() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().isShowNewPersonPrize(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.isShowNew(data);
                }
            }
        });
    }

    public void isShowUserNewPersonPrize() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().isUserShowNewPersonPrize(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.isShowNew(data);
                }
            }
        });
    }
    public void getPrizeByRegister() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getPrizeByRegister(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null&&!isEmpty(data.prize)&&Float.parseFloat(data.prize)>0) {
                    iView.getPrize(data);
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.showFailureView(0);
            }

            @Override
            public void onErrorData(BaseEntity<CommonEntity> commonEntityBaseEntity) {
                super.onErrorData(commonEntityBaseEntity);
                iView.showFailureView(0);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(0);
            }
        });
    }

    public void getDiscoveryUnreadCount() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().getDiscoveryUnreadCount(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                if (data != null) {
                    iView.setDiscoveryUnreadCount(data);
                }
            }
        });
    }

    public void getUpdateInfo(String type, String version) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("version", version);
        sortAndMD5(map);

        Call<BaseEntity<UpdateEntity>> baseEntityCall = getApiService().updateappcheck(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<UpdateEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UpdateEntity> entity) {
                super.onSuccess(entity);
                UpdateEntity data = entity.data;
                if (data != null) {
                    iView.setUpdateInfo(data);
                }
            }
        });
    }

    public void isShowSign() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<ShowSignEntity>> baseEntityCall = getApiService().isShowSign(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShowSignEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ShowSignEntity> entity) {
                super.onSuccess(entity);
                ShowSignEntity data = entity.data;
                if (data != null) {
                    iView.setADs(data);
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }
        });
    }

    public void getPopAD() {
        Map<String, String> map = new HashMap<>();
//        map.put("storeId", storeId);
        sortAndMD5(map);

        Call<BaseEntity<AdEntity>> baseEntityCall = getApiService().popup(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AdEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AdEntity> entity) {
                super.onSuccess(entity);
                AdEntity data = entity.data;
                if (data != null) {
                    iView.setAD(data);
                }
                if(Common.isAlreadyLogin()) {
                    showVoucherSuspension();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
//                super.onErrorCode(code, message);
                showVoucherSuspension();
            }
        });
    }

    public void showVoucherSuspension() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<ShowVoucherSuspension>> setinfo = getAddCookieApiService().showVoucherSuspension(map);
        getNetData(false, setinfo, new SimpleNetDataCallback<BaseEntity<ShowVoucherSuspension>>() {
            @Override
            public void onSuccess(BaseEntity<ShowVoucherSuspension> entity) {
                super.onSuccess(entity);
                iView.showVoucherSuspension(entity.data);
            }
        });
    }

    public void getMenuData(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<GetMenuEntity>> baseEntityCall = getApiService().channelGetMenu(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetMenuEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetMenuEntity> entity) {
                super.onSuccess(entity);
                iView.setTab(entity.data);
            }
        });

    }

    public void getContentData(String id){
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        sortAndMD5(map);
        Call<BaseEntity<GetDataEntity>> baseEntityCall = getApiService().channelGetData(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetDataEntity> entity) {
                super.onSuccess(entity);
                iView.setContent(entity.data);
            }
        });
    }

}
