package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.IReturnRequestView;

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

    public void applyRefund(String refundId, String ogId, String qty, String amount,
                            String type, String reasonId, String remark,
                            String images, boolean isEdit) {
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
                    RefundDetailEntity.RefundDetail refundDetail = entity.data;
                    iView.applyRefundSuccess(refundDetail.refund_id);
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.applyRefundFail(message);
                    super.onErrorCode(code, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        if (isEmpty(filePath)) {
            return;
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
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
                super.onSuccess(entity);
            }
        });
    }
}
