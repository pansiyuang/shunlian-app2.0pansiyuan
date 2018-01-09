package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.IPlatformInterventionRequestView;
import com.shunlian.app.view.IView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/8.
 */

public class PlatformInterventionPresenter extends BasePresenter<IPlatformInterventionRequestView> {
    public PlatformInterventionPresenter(Context context, IPlatformInterventionRequestView iView) {
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

    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = new File(filePath.get(i).imgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            LogUtil.httpLogW("file:" + file.getName());
            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new ProgressListener() {
                @Override
                public void onProgress(int progress, String tag) {
                    LogUtil.httpLogW("tag:" + tag + "    progress:" + progress);
//                    iView.uploadProgress(progress, tag);
                }

                @Override
                public void onDetailProgress(long written, long total, String tag) {

                }
            }, filePath.get(i).imgPath);
            params.put("file[]\"; filename=\"" + file.getName(), uploadFileRequestBody);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain"), uploadPath);
        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(params, body);
        getNetData(true, call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                UploadPicEntity uploadPicEntity = entity.data;
                if (uploadPicEntity != null) {
                    iView.uploadImg(uploadPicEntity);
                }
                super.onSuccess(entity);
            }
        });
    }

    public void platformRequest(String refundId, String uesrStatus, String remark, String evidenceImgs, boolean isEdit) {
        Map<String, String> map = new HashMap<>();
        map.put("refund_id", refundId);
        map.put("user_status", uesrStatus);

        if (!TextUtils.isEmpty(remark)) {
            map.put("remark", remark);
        }
        if (!TextUtils.isEmpty(evidenceImgs)) {
            map.put("evidence_img", evidenceImgs);
        }
        if (isEdit) {
            map.put("is_edit", "1");
        } else {
            map.put("is_edit", "0");
        }
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().callPlat(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    if (1000 == entity.code) {
                        iView.requestSuccess(entity.message);
                    } else {
                        iView.requestFail(entity.message);
                    }
                    super.onSuccess(entity);
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
