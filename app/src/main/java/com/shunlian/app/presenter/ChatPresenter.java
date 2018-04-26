package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.IChatUpLoadImgView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatUpLoadImgPresenter extends BasePresenter<IChatUpLoadImgView> {

    public ChatUpLoadImgPresenter(Context context, IChatUpLoadImgView iView) {
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

    public void uploadPic(ImageEntity filePath, final String uploadPath, String tagId, final ImageMessage imageMessage) {
        Map<String, RequestBody> params = new HashMap<>();
        File file = filePath.file;
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new ProgressListener() {
            @Override
            public void onProgress(int progress, String tag) {
            }

            @Override
            public void onDetailProgress(long written, long total, String tag) {

            }
        }, file.getAbsolutePath());
        params.put("file[]\"; filename=\"" + file.getName(), uploadFileRequestBody);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain"), uploadPath);
        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(params, body);
        getNetData(false, call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                if (uploadPicEntity != null) {
                    iView.uploadImg(uploadPicEntity, tagId, imageMessage);
                }
            }
        });
    }
}
