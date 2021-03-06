package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.CreatCommentEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.ICommentView;

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
 * Created by Administrator on 2017/12/13.
 */

public class CommentPresenter extends BasePresenter<ICommentView> {

    public CommentPresenter(Context context, ICommentView iView) {
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

    public void creatComment(String ordersn, String logisticsStar, String attitudeStar, String consistentStar, String goodsString) {
        Map<String, String> map = new HashMap<>();
        map.put("ordersn", ordersn);
        map.put("ship_star_level", logisticsStar);
        map.put("service_star_level", attitudeStar);
        map.put("desc_star_level", consistentStar);
        map.put("goods", goodsString);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().addComment(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    iView.CommentSuccess();
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.CommentFail(message);
                    super.onErrorCode(code, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void appendComment(String goodsStr) {
        Map<String, String> map = new HashMap<>();
        map.put("goods", goodsStr);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().appendComment(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    iView.CommentSuccess();
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.CommentFail(message);
                    super.onErrorCode(code, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeComment(String commentId, String content, String images) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_id", commentId);
        map.put("content", content);
        if (!TextUtils.isEmpty(images)) {
            map.put("images", images);
        }
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().changeComment(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    iView.CommentSuccess();
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.CommentFail(message);
                    super.onErrorCode(code, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getOrderInfo(String orderSn) {
        Map<String, String> map = new HashMap<>();
        map.put("order_sn", orderSn);
        sortAndMD5(map);
        try {
            String s = new ObjectMapper().writeValueAsString(map);
            RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), s);
            Call<BaseEntity<CreatCommentEntity>> baseEntityCall = getApiService().getOrderInfo(requestBody);
            getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CreatCommentEntity>>() {
                @Override
                public void onSuccess(BaseEntity<CreatCommentEntity> entity) {
                    CreatCommentEntity creatCommentEntity = entity.data;
                    iView.getCreatCommentList(creatCommentEntity.order_goods);
                    super.onSuccess(entity);
                }

                @Override
                public void onErrorCode(int code, String message) {
                    iView.CommentFail(message);
                    super.onErrorCode(code, message);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
