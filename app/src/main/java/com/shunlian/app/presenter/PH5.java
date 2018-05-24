package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ShareEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.AddAddressView;
import com.shunlian.app.view.IH5View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chihane.shunlian.BottomDialog;
import chihane.shunlian.DataProvider;
import chihane.shunlian.ISelectAble;
import chihane.shunlian.SelectedListener;
import chihane.shunlian.Selector;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/23.
 */

public class PH5 extends BasePresenter<IH5View> {

    public PH5(Context context, IH5View iView) {
        super(context, iView);
    }

    @Override
    protected void initApi() {

    }
    public void getShareInfo(String id) {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<ShareEntity>> baseEntityCall = getApiService().specialGetdata(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ShareEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ShareEntity> entity) {
                super.onSuccess(entity);
                ShareEntity data = entity.data;
                if (data != null) {
                    iView.setShareInfo(data);
                }
            }

            @Override
            public void onErrorData(BaseEntity<ShareEntity> shareEntityBaseEntity) {
                super.onErrorData(shareEntityBaseEntity);
                iView.setShareInfo(null);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.setShareInfo(null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.setShareInfo(null);
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
