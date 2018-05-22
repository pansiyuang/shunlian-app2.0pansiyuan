package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ConsultHistoryEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IConsultHistoryView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/3.
 */

public class ConsultHistoryPresenter extends BasePresenter<IConsultHistoryView> {

    private String mRefund_id;
    private Call<BaseEntity<CommonEntity>> mChatIdCall;

    public ConsultHistoryPresenter(Context context, IConsultHistoryView iView, String refund_id) {
        super(context, iView);
        mRefund_id = refund_id;

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
        if (mChatIdCall != null && mChatIdCall.isExecuted()) {
            mChatIdCall.cancel();
        }
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        refundHistory(mRefund_id);
    }

    private void refundHistory(String refund_id) {
        Map<String, String> map = new HashMap<>();
        map.put("refund_id", refund_id);
        sortAndMD5(map);

        Call<BaseEntity<ConsultHistoryEntity>> baseEntityCall = getApiService().refundHistory(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ConsultHistoryEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ConsultHistoryEntity> entity) {
                super.onSuccess(entity);
                iView.consultHistory(entity.data);
            }
        });

    }

    /**
     * 获取商家聊天id
     *
     * @param shopId
     */
    public void getUserId(String shopId) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shopId);
        sortAndMD5(map);

        mChatIdCall = getAddCookieApiService().getUserId(map);
        getNetData(false, mChatIdCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getUserId(commonEntity.user_id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

}
