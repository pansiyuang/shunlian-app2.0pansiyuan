package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.PunishEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IPunishView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PunishPresenter extends BasePresenter<IPunishView> {

    private String mId;

    public PunishPresenter(Context context, IPunishView iView, String id) {
        super(context, iView);
        mId = id;
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
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("id",mId);
        sortAndMD5(map);
        Call<BaseEntity<PunishEntity>> punish = getApiService().punish(map);
        getNetData(true,punish,new SimpleNetDataCallback<BaseEntity<PunishEntity>>(){
            @Override
            public void onSuccess(BaseEntity<PunishEntity> entity) {
                super.onSuccess(entity);
                iView.setData(entity.data);
            }
        });
    }
}
