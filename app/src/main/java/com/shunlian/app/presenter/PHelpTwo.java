package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.HelpClassEntity;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IHelpTwoView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PHelpTwo extends BasePresenter<IHelpTwoView> {
    private int pageSize=20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading=false;
    private List<HelpcenterQuestionEntity.Question> mDatas = new ArrayList<>();
    private List<HelpClassEntity.Article> datas = new ArrayList<>();

    public PHelpTwo(Context context, IHelpTwoView iView) {
        super(context, iView);
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }
    public void resetBaby(String second_cate) {
        babyPage = 1;
        babyIsLoading = true;
        mDatas.clear();
        getCatetwo(second_cate);
    }

    public void refreshBaby(String second_cate) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getCatetwo(second_cate);
        }
    }

    public void resetBabys(String cate_id) {
        babyPage = 1;
        babyIsLoading = true;
        datas.clear();
        getClass(cate_id);
    }

    public void refreshBabys(String cate_id) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getClass(cate_id);
        }
    }
    @Override
    protected void initApi() {
    }

    public void getCateOne(String id){
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<HelpcenterQuestionEntity>> baseEntityCall = getApiService().helpcenterQuestionCate(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpcenterQuestionEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpcenterQuestionEntity> entity) {
                super.onSuccess(entity);
                HelpcenterQuestionEntity data = entity.data;
                if (data != null) {
                    iView.setCateOne(data);
                }
            }
        });
    }

    public void getCatetwo(String second_cate){
        Map<String, String> map = new HashMap<>();
        map.put("second_cate", second_cate);
        map.put("page_size", String.valueOf(pageSize));
        map.put("page", String.valueOf(babyPage));
        sortAndMD5(map);

        Call<BaseEntity<HelpcenterQuestionEntity>> baseEntityCall = getApiService().helpcenterQuestion(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpcenterQuestionEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpcenterQuestionEntity> entity) {
                super.onSuccess(entity);
                HelpcenterQuestionEntity data = entity.data;
                if (data != null) {
                    babyIsLoading = false;
                    babyPage++;
                    babyAllPage = Integer.parseInt(data.total_page);
                    mDatas.addAll(data.list);
                    iView.setCateTwo(data,mDatas);
                }
            }
        });
    }

    public void getClass(String cate_id){
        Map<String, String> map = new HashMap<>();
        map.put("cate_id", cate_id);
        map.put("page_size", String.valueOf(pageSize));
        map.put("page", String.valueOf(babyPage));
        sortAndMD5(map);

        Call<BaseEntity<HelpClassEntity>> baseEntityCall = getApiService().helpcenterClasses(map);
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<HelpClassEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HelpClassEntity> entity) {
                super.onSuccess(entity);
                HelpClassEntity data = entity.data;
                if (data != null) {
                    babyIsLoading = false;
                    babyPage++;
                    babyAllPage = Integer.parseInt(data.total_page);
                    datas.addAll(data.list);
                    iView.setClass(data,datas);
                }
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
