package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.UseCommentEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IFindCommentView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/19.
 */

public abstract class FindCommentPresenter<T extends IFindCommentView> extends BasePresenter<IFindCommentView> {

    protected T iView;

    public FindCommentPresenter(Context context, T iView) {
        super(context, iView);
        this.iView = iView;
    }

    public void sendComment(String content, String pid,String article_id,String from_page){
        Map<String,String> map = new HashMap<>();
        map.put("article_id",article_id);
        map.put("content",content);
        map.put("pid",pid);
        map.put("from_page",from_page);
        sortAndMD5(map);

        Call<BaseEntity<UseCommentEntity>> baseEntityCall = getAddCookieApiService().sendComment(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<UseCommentEntity>>(){
            @Override
            public void onSuccess(BaseEntity<UseCommentEntity> entity) {
                super.onSuccess(entity);
                refreshItem(entity.data.insert_item);
            }
        });
    }

    protected abstract void refreshItem(FindCommentListEntity.ItemComment insert_item);

    public void delComment(String comment_id) {
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",comment_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delComment(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                delSuccess();
            }
        });
    }

    protected abstract void delSuccess();
}
