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
import com.shunlian.app.view.ICommentView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
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
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = new File(filePath.get(i).imgPath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            LogUtil.httpLogW("file:" + file.getName());
            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new ProgressListener() {
                @Override
                public void onProgress(int progress, String tag) {
                    LogUtil.httpLogW("tag:" + tag + "    progress:" + progress);
                    iView.uploadProgress(progress, tag);
                }

                @Override
                public void onDetailProgress(long written, long total, String tag) {

                }
            }, filePath.get(i).imgPath);
            params.put("file[]\"; filename=\"" + file.getName(), uploadFileRequestBody);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain"), uploadPath);
        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(params, body);
        getNetData(true,call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
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
                    if (entity.code == 1000) {
                        iView.CommentSuccess();
                    } else {
                        iView.CommentFail(entity.message);
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
                    if (entity.code == 1000) {
                        iView.CommentSuccess();
                    } else {
                        iView.CommentFail(entity.message);
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
                    if (entity.code == 1000) {
                        iView.CommentSuccess();
                    } else {
                        iView.CommentFail(entity.message);
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
