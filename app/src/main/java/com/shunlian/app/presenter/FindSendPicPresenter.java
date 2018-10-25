package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BlogDraftEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.view.ISelectPicVideoView;

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
 * Created by zhanghe on 2018/10/22.
 */

public class FindSendPicPresenter extends BasePresenter<ISelectPicVideoView> {

    public FindSendPicPresenter(Context context, ISelectPicVideoView iView) {
        super(context, iView);
        initApi();
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
    public void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<BlogDraftEntity>> entityCall = getApiService().getDraft(map);
        getNetData(true,entityCall,new SimpleNetDataCallback<BaseEntity<BlogDraftEntity>>(){
            @Override
            public void onSuccess(BaseEntity<BlogDraftEntity> entity) {
                super.onSuccess(entity);
                iView.resetDraft(entity.data);
            }
        });

    }

    public void uploadPic(List<ImageVideo> filePath, final String uploadPath) {
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


    public void uploadVideo(String videoPath){
        File file = new File(videoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().uploadVideo(part);

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                String url = entity.data.name;
                iView.uploadViodeSuccess(url,videoPath);
            }
        });
    }

    public void publish(String text, String pics, String video,
                        String activity_id, String place,
                        String related_goods, String draft) {
        Map<String, String> map = new HashMap<>();
        map.put("text", text);
        if (!isEmpty(pics)) {
            map.put("type", "1");
            map.put("pics", pics);
        } else if (!isEmpty(video)) {
            map.put("type", "2");
            map.put("video", video);
        }
        if (!isEmpty(activity_id)) {
            map.put("activity_id", activity_id);
        }
        if (!isEmpty(place)) {
            map.put("place", place);
        }
        if (!isEmpty(related_goods)) {
            map.put("related_goods", related_goods);
        }
        if (!isEmpty(draft)) {
            map.put("draft", draft);
        }
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().pubishBlog(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.publishSuccess();
            }
        });
    }


}
