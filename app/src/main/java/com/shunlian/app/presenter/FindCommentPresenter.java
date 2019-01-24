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

    public void sendComment(String content, String pid, String article_id, String level) {
        Map<String, String> map = new HashMap<>();
        map.put("discovery_id", article_id);
        map.put("content", content);
        map.put("reply_comment_id", pid);
        map.put("level", level);//0一级评论，直接针对文章进行评论，1二级评论，对一级评论进行评论，2三级评论，对二级评论进行评论
        sortAndMD5(map);

        Call<BaseEntity<FindCommentListEntity.ItemComment>> baseEntityCall = getAddCookieApiService().sendComment(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FindCommentListEntity.ItemComment>>() {
            @Override
            public void onSuccess(BaseEntity<FindCommentListEntity.ItemComment> entity) {
                super.onSuccess(entity);
                refreshItem(entity.data, entity.message);
            }
        });
    }

    protected abstract void refreshItem(FindCommentListEntity.ItemComment insert_item, String message);

    public void delComment(String comment_id) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_id", comment_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delComment(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                delSuccess();
            }
        });
    }

    protected abstract void delSuccess();
}
