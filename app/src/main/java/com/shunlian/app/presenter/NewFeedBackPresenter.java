package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.INewFeedBackView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/25.
 */

public class NewFeedBackPresenter extends BasePresenter<INewFeedBackView> {


    public NewFeedBackPresenter(Context context, INewFeedBackView iView) {
        super(context, iView);
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

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {

    }

    public void feedback(Map<String,String> map){
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> feedback = getAddCookieApiService().feedback(getRequestBody(map));

        getNetData(true,feedback,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.submitSuccess(entity.data.message);
            }
        });
    }

    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        Map<String, RequestBody> params = new HashMap<>();
        for (int i = 0; i < filePath.size(); i++) {
            LogUtil.httpLogW("I:"+i);
            File file = filePath.get(i).file;
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(requestBody, new ProgressListener() {
                @Override
                public void onProgress(int progress, String tag) {
                    LogUtil.httpLogW("tag:" + tag + "    progress:" + progress);
                    iView.uploadProgress(progress, tag);
                }

                @Override
                public void onDetailProgress(long written, long total, String tag) {

                }
            },file.getAbsolutePath());
            params.put("file[]\"; filename=\"" + file.getName(), uploadFileRequestBody);
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/plain"), uploadPath);
        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().uploadPic(params, body);
        getNetData(true,call, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                if (uploadPicEntity != null) {
                    iView.uploadImg(uploadPicEntity);
                }
                iView.setRefundPics(uploadPicEntity.relativePath,false);
            }
        });
    }
}