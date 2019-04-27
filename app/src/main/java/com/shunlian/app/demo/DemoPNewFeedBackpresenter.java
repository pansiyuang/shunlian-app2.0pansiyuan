package com.shunlian.app.demo;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.presenter.BasePresenter;
import com.shunlian.app.view.INewFeedBackView;

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
 * Created by Administrator on 2019/4/2.
 */
class DemoNewFeedBackPresenter extends BasePresenter<INewFeedBackView> {
    public DemoNewFeedBackPresenter(Context context, INewFeedBackView iView) {
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
    public void feedback(Map<String, String> map) {
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> feedback = getAddCookieApiService().feedback(getRequestBody(map));
        getNetData(true,feedback,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.submitSuccess(entity.data.message);
            }
        });

    }
    public void uploadpic(List<ImageEntity>filePath,final String upladPath){
        if(isEmpty(filePath)){
            return;
        }
        List<MultipartBody.Part>parts= new ArrayList<>();
        for(int i =0 ;i<filePath.size();i++){
            File file = filePath.get(i).file;
            if (file==null)continue;
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part formData = MultipartBody.Part.createFormData("file[]", file.getName(), requestBody);
            parts.add(formData);

        }
        HashMap<String, String> map = new HashMap<>();
        map.put("path_name",upladPath);
        sortAndMD5(map);

        Call<BaseEntity<UploadPicEntity>> Call = getAddCookieApiService().uploadPic(parts, map);
        getNetData(true,Call,new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>(){
            public void onSuccess(BaseEntity<UploadPicEntity>entity){
                super.onSuccess(entity);
                UploadPicEntity data = entity.data;
                if(data!=null){
                    iView.uploadImg(data);
                }
                iView.setRefundPics(data.relativePath,false);
            }
        });

    }
}
