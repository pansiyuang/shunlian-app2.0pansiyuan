package com.shunlian.app.yjfk;

import android.content.Context;

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2019/4/17.
 */

public class OpinionPresenter extends BasePresenter<IOpinionbackView> {
    private int babyPage = 1;//当前页数
    private final int count = 20;//获取数量
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private List<ComplanintListEntity.Lists> babyDatas = new ArrayList<>();
    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getcomplaintList( babyPage, count);
        }
    }




    public OpinionPresenter(Context context, IOpinionbackView iView) {
        super(context, iView);
    }

    public void initOpinionfeedback() {
        HashMap<String, String> map = new HashMap<>();
//        map.put("page",String.valueOf(page));
//        map.put("pageSize",String.valueOf(pageSize));
        sortAndMD5(map);
        Call<BaseEntity<OpinionfeedbackEntity>> youlist = getApiService().getOpinionfeedback(map);
        getNetData(false, youlist, new SimpleNetDataCallback<BaseEntity<OpinionfeedbackEntity>>() {
            public void onSuccess(BaseEntity<OpinionfeedbackEntity> entity) {
                super.onSuccess(entity);
                if (entity != null && entity.data != null && !isEmpty(entity.data.ads)) {
                    OpinionfeedbackEntity data = entity.data;
                    iView.getOpinionfeedback(data);
                } else {
                    isLoading = false;
                    iView.showDataEmptyView(1);
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
                iView.showDataEmptyView(2);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                iView.showDataEmptyView(2);
            }

        });
    }

    public void uploadPic(List<ImageEntity> filePath, String uploadPath) {
        if (isEmpty(filePath)) {
            return;
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
            if (file == null) continue;
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file[]", file.getName(), requestBody);
            parts.add(part);
        }

        Map<String, String> map = new HashMap<>();
        map.put("path_name", uploadPath);
        sortAndMD5(map);

        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(parts, map);
        getNetData(true, call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                if (uploadPicEntity != null) {
                    iView.uploadImg(uploadPicEntity);
                }
                iView.setRefundPics(uploadPicEntity.relativePath, false);
            }
        });
    }

    public void submitComplaint(Map<String, String> map) {
        sortAndMD5(map);
        Call<BaseEntity<Opinionfeedback1Entity>> feedback = getAddCookieApiService().getsubmitComplaint(getRequestBody(map));

        getNetData(true, feedback, new SimpleNetDataCallback<BaseEntity<Opinionfeedback1Entity>>() {
            @Override
            public void onSuccess(BaseEntity<Opinionfeedback1Entity> entity) {
                super.onSuccess(entity);
                iView.submitSuccess1(entity);
            }
        });

    }

    public void getComplaintTypesEntity() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<ComplaintTypesEntity>> baseEntityCall = getAddCookieApiService().getcomplaintTypes(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ComplaintTypesEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ComplaintTypesEntity> entity) {
                super.onSuccess(entity);
                iView.getcomplaintTypes(entity.data);
            }


        });
    }
    public void getcomplaintList(int page,int page_size) {
        Map<String, String> map = new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("page_size",String.valueOf(page_size));
        sortAndMD5(map);
        Call<BaseEntity<ComplanintListEntity>> baseEntityCall = getAddCookieApiService().getcomplaintList(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<ComplanintListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<ComplanintListEntity> entity) {
                super.onSuccess(entity);
                ComplanintListEntity data = entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.allPage);
                babyDatas.addAll(data.list);
                iView.getcomplaintList(babyDatas,babyAllPage,Integer.parseInt(data.page));
            }


        });
    }



        @Override
        public void attachView () {

        }

        @Override
        public void detachView () {

        }

        @Override
        protected void initApi () {

        }


    }

