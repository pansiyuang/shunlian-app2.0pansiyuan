package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISystemMsgView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/9.
 */

public class SystemMsgPresenter extends BasePresenter<ISystemMsgView> {
    public final String page_size = "10";

    public SystemMsgPresenter(Context context, ISystemMsgView iView) {
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
    public void initApi() {

        currentPage = 1;
        isLoading = false;
        allPage = 1;
        paging(true,0);


    }

    public void paging(boolean isShow,int empty){
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",page_size);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> sysmessage = getApiService().sysmessage(map);
        getNetData(empty,empty,isShow,sysmessage,
                new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);

            }
        });
    }


}
