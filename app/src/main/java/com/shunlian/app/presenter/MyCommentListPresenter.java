package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMyCommentListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentListPresenter extends BasePresenter<IMyCommentListView> {

    public static final int pageSize = 20;
    public static final int ALL = 0;
    public static final int APPEND = 1;

    public MyCommentListPresenter(Context context, IMyCommentListView iView) {
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
        myCommentListAll();
    }

    public void myCommentListAll(){
        myCommentList(100,100,true,ALL,1);
    }

    public void myCommentListAppend(){
        myCommentList(100,100,true,APPEND,1);
    }

    public void myCommentList(int empty,int failureCode,boolean isLoading,int status,int page){
        Map<String,String> map = new HashMap<>();
        map.put("status",String.valueOf(status));
        map.put("page",String.valueOf(page));
        map.put("page_size",String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<MyCommentListEntity>> baseEntityCall = getApiService().myCommentList(map);

        getNetData(empty,failureCode,isLoading,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<MyCommentListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<MyCommentListEntity> entity) {
                super.onSuccess(entity);

            }
        });
    }
}
