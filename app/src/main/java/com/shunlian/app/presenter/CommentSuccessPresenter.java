package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentSuccessEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICommentSuccessView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/18.
 */

public class CommentSuccessPresenter extends BasePresenter<ICommentSuccessView> {

    private Call<BaseEntity<CommentSuccessEntity>> baseEntityCall;

    public CommentSuccessPresenter(Context context, ICommentSuccessView iView) {
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
        if (baseEntityCall != null)baseEntityCall.cancel();
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        baseEntityCall = getApiService().mixed_list(map);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommentSuccessEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommentSuccessEntity> entity) {
                super.onSuccess(entity);
                iView.otherCommentList(entity.data.comment,entity.data.append);
            }
        });

    }

}
