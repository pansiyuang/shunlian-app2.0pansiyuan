package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ReplysetEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IChatInputView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/7/18.
 */

public class ChatInputPresenter extends BasePresenter<IChatInputView> {

    public ChatInputPresenter(Context context, IChatInputView iView) {
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

    public void getReplyList(String joinId, String type) {
        LogUtil.httpLogW("joinId:" + joinId + "   type:" + type);
        Map<String, String> map = new HashMap<>();
        map.put("join_id", joinId);
        map.put("type", type);
        sortAndMD5(map);
        Call<BaseEntity<ReplysetEntity>> baseEntityCall = getApiService().replysetList(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ReplysetEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ReplysetEntity> entity) {
                super.onSuccess(entity);
                ReplysetEntity replysetEntity = entity.data;
                iView.getReplysetList(replysetEntity.list);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
