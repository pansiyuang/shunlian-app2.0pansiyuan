package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.IReturnRequestView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ReturnRequestPresenter extends BasePresenter<IReturnRequestView> {

    public ReturnRequestPresenter(Context context, IReturnRequestView iView) {
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

    public void applyRefund(String refundId, String ogId, String qty, String amount, String type, String reasonId, String remark, String images, boolean isEdit) {
        Map<String, String> map = new HashMap<>();
        map.put("og_id", ogId);
        map.put("qty", qty);
        map.put("type", type);
        map.put("reason_id", reasonId);

        if (!TextUtils.isEmpty(refundId)) {
            map.put("refund_id", refundId);
        }
        if (isEdit) {
            map.put("is_edit", "1");
        } else {
            map.put("is_edit", "0");
        }
        if (!TextUtils.isEmpty(amount)) {
            map.put("amount", amount);
        }

        if (!TextUtils.isEmpty(remark)) {
            map.put("remark", remark);
        }
        if (!TextUtils.isEmpty(images)) {
            map.put("evidence_img", images);
        }
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<RefundDetailEntity.RefundDetail>> baseEntityCall = getApiService().applyRefund(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<RefundDetailEntity.RefundDetail>>() {
                @Override
                public void onSuccess(BaseEntity<RefundDetailEntity.RefundDetail> entity) {
                    if (entity.code == 1000) {
                        RefundDetailEntity.RefundDetail refundDetail = entity.data;
                        iView.applyRefundSuccess(refundDetail.refund_id);
                    } else {
                        iView.applyRefundFail(entity.message);
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

    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new ProgressListener() {
                @Override
                public void onProgress(int progress, String tag) {
                    LogUtil.httpLogW("tag:" + tag + "    progress:" + progress);
//                    iView.uploadProgress(progress, tag);
                }

                @Override
                public void onDetailProgress(long written, long total, String tag) {

                }
            },file.getAbsolutePath());
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
}
