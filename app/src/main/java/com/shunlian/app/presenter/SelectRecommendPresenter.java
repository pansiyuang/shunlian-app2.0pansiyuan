package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISelectRecommendView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/19.
 */

public class SelectRecommendPresenter extends BasePresenter<ISelectRecommendView> {


    public SelectRecommendPresenter(Context context, ISelectRecommendView iView) {
        super(context, iView);
        initApi();
    }

    @Override
    public void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("page","1");
        map.put("pageSize","9");
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity>> baseEntityCall = getApiService().memberCodeList(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<MemberCodeListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity> entity) {
                super.onSuccess(entity);
                MemberCodeListEntity data = entity.data;
                if (data != null){
                    List<MemberCodeListEntity.ListBean> list = data.list;
                    iView.selectCodeList(list);
                    iView.help(data.url);
                }
            }
        });
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }


    public void codeDetail(String id){
        Map<String,String> map = new HashMap<>();
        map.put("code",id);
        sortAndMD5(map);

        Call<BaseEntity<MemberCodeListEntity.ListBean>>
                baseEntityCall = getApiService().codeInfo(map);

        getNetData(baseEntityCall,new SimpleNetDataCallback
                <BaseEntity<MemberCodeListEntity.ListBean>>(){
            @Override
            public void onSuccess(BaseEntity<MemberCodeListEntity.ListBean> entity) {
                super.onSuccess(entity);

                iView.codeInfo(entity.data);
            }
        });
    }
}
