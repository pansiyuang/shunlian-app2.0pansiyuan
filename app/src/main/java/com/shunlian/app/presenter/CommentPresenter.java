package com.shunlian.app.presenter;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.JoinGoodsEntity;
import com.shunlian.app.bean.UploadCmtPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ICommentView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void uploadPic(String picPath) {
        File file = new File(picPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        Call<BaseEntity<UploadCmtPicEntity>> call = getAddCookieApiService().uploadPic(body);
        getNetData(true, call, new SimpleNetDataCallback<BaseEntity<UploadCmtPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadCmtPicEntity> entity) {
                super.onSuccess(entity);
            }
        });
    }

    public void appendComment(String commentId, String content, String images) {
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
            Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().appendComment(requestBody);
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    if (entity.code == 1000) {
                        iView.appendCommentSuccess();
                    } else {
                        iView.appendCommentFail(entity.message);
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
            getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
                @Override
                public void onSuccess(BaseEntity<EmptyEntity> entity) {
                    if (entity.code == 1000) {
                        iView.changeCommentSuccess();
                    } else {
                        iView.changeCommtFail(entity.message);
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
