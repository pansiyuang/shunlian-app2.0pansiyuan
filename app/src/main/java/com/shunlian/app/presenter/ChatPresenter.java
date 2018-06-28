package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.newchat.entity.HistoryEntity;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.upload.ProgressListener;
import com.shunlian.app.utils.upload.UploadFileRequestBody;
import com.shunlian.app.view.IChatView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/12.
 */

public class ChatPresenter extends BasePresenter<IChatView> {

    public ChatPresenter(Context context, IChatView iView) {
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
        File file = filePath.file;
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part[] files = new MultipartBody.Part[2];
        files[0] = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Map<String, String> map = new HashMap<>();
        map.put("path_name", uploadPath);
        sortAndMD5(map);

        Call<BaseEntity<UploadPicEntity>> call = getAddCookieApiService().chatupload(files, map);
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

    private RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    public String getTimeStamp(String time) {
        Map<String, String> map = new HashMap<>();
        map.put("timestamp", String.valueOf(time));
        List<String> strs = new ArrayList<>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            strs.add(next);
        }
        Collections.sort(strs);
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < strs.size(); i++) {
            String key = strs.get(i);
            String value = map.get(key);
            if (TextUtils.isEmpty(value)) {
                value = "";
            }
            sign.append(key + "=" + value);
            sign.append("&");
            if (i == strs.size() - 1) {
                sign.append("key=" + Constant.KEY);
            }
        }
        LogUtil.httpLogW("getTimeStamp:" + getStringMD5(sign.toString()));
        return getStringMD5(sign.toString());
    }

    public void getChatHistoryMessage(boolean isLoad, String userId, String platform_type, String shopId, String sendTime) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("platform_type", platform_type);
        if (!isEmpty(shopId)) {
            map.put("shop_id", shopId);
        }
        if (!isEmpty(sendTime)) {
            map.put("send_time", String.valueOf(sendTime));
        }
        sortAndMD5(map);

        Call<BaseEntity<HistoryEntity>> baseEntityCall = getAddCookieApiService().chatUserHistoryData(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HistoryEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HistoryEntity> entity) {
                super.onSuccess(entity);
                HistoryEntity historyEntity = entity.data;
                List<MsgInfo> msgInfoList = historyEntity.list;
                iView.getHistoryMsg(msgInfoList, historyEntity.last_send_time, historyEntity.surplus);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void platformChatUserHistoryData(boolean isLoad, String chatId, String mUserId, String sendTime) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", chatId);
        map.put("m_user_id", mUserId);
        if (!isEmpty(sendTime)) {
            map.put("send_time", String.valueOf(sendTime));
        }
        sortAndMD5(map);

        Call<BaseEntity<HistoryEntity>> baseEntityCall = getAddCookieApiService().platformChatUserHistoryData(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HistoryEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HistoryEntity> entity) {
                super.onSuccess(entity);
                HistoryEntity historyEntity = entity.data;
                List<MsgInfo> msgInfoList = historyEntity.list;
                iView.getHistoryMsg(msgInfoList, historyEntity.last_send_time, historyEntity.surplus);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    public void shopChatUserHistoryData(boolean isLoad, String chatId, String mUserId, String sendTime) {
        Map<String, String> map = new HashMap<>();
        map.put("user_id", chatId);
        map.put("m_user_id", mUserId);
        if (!isEmpty(sendTime)) {
            map.put("send_time", String.valueOf(sendTime));
        }
        sortAndMD5(map);

        Call<BaseEntity<HistoryEntity>> baseEntityCall = getAddCookieApiService().shopChatUserHistoryData(map);
        getNetData(isLoad, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HistoryEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HistoryEntity> entity) {
                super.onSuccess(entity);
                HistoryEntity historyEntity = entity.data;
                List<MsgInfo> msgInfoList = historyEntity.list;
                iView.getHistoryMsg(msgInfoList, historyEntity.last_send_time, historyEntity.surplus);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }
}
