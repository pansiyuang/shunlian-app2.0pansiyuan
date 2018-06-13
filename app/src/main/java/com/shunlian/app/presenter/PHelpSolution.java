package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.bean.HelpcenterSolutionEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IHelpSolutionView;
import com.shunlian.app.view.IHelpTwoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PHelpSolution extends BasePresenter<IHelpSolutionView> {
    private String id;

    public PHelpSolution(Context context,String id, IHelpSolutionView iView) {
        super(context, iView);
        this.id=id;
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<HelpcenterSolutionEntity>> baseEntityCall = getApiService().helpcenterSolution(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpcenterSolutionEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpcenterSolutionEntity> entity) {
                super.onSuccess(entity);
                HelpcenterSolutionEntity data = entity.data;
                if (data != null) {
                    iView.setApiData(data);
                }
            }
        });
    }

    public void isSolved(final String isSolved){
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("status", isSolved);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().helpcenterSolve(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                if ("1".equals(isSolved)) {
                    Common.staticToast(entity.message);
                    iView.initFeedback(true);
                }else {
                    iView.initFeedback(false);
                }
            }
        });
    }

    public void submitFeedback(String content){
        Map<String, String> map = new HashMap<>();
        map.put("content", content);
        map.put("question_id",id);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().helpcenterFeedback(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
               iView.callFeedback();
            }

        });
    }

    public void getUserId() {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", "-1");
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().getUserId(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
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
