package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IBusinessCardView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/25.
 */

public class BusinessCardPresenter extends BasePresenter<IBusinessCardView> {


    public BusinessCardPresenter(Context context, IBusinessCardView iView) {
        super(context, iView);
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
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> qrCard = getApiService().getQrCard(map);
        getNetData(true,qrCard,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.codePath(entity.data.card_path);
            }
        });

    }
}
