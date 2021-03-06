package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.SubmitLogisticsInfoEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ISubmitLogisticsInfoView;

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
 * Created by Administrator on 2018/1/4.
 */

public class SubmitLogisticsInfoPresenter extends BasePresenter<ISubmitLogisticsInfoView> {

    private String refund_id;
    private String is_edit = "N";//是否编辑提交，是Y，否N

    public SubmitLogisticsInfoPresenter(Context context, ISubmitLogisticsInfoView iView, String refund_id) {
        super(context, iView);
        this.refund_id = refund_id;
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
    protected void initApi() {
    }

    public void getLogisticsShipInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("refund_id", refund_id);
        sortAndMD5(map);
        Call<BaseEntity<SubmitLogisticsInfoEntity>> logisticsShipInfo = getApiService().getLogisticsShipInfo(map);
        getNetData(true, logisticsShipInfo, new SimpleNetDataCallback<BaseEntity<SubmitLogisticsInfoEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SubmitLogisticsInfoEntity> entity) {
                super.onSuccess(entity);

                SubmitLogisticsInfoEntity data = entity.data;
                iView.setLogisticsName(data.express_com);
                iView.setLogisticsCode(data.express_sn);
                iView.setRefundMemo(data.memo);
                iView.setRefundPics(data.pics, true);
                is_edit = "Y";
            }
        });
    }

    /**
     * 提交物流信息
     */
    public void submitLogisticsInfo(String express_com, String express_sn, String memo, String pics) {
        Map<String, String> map = new HashMap<>();
        map.put("express_com", express_com);
        map.put("refund_id", refund_id);
        map.put("express_sn", express_sn);
        map.put("memo", memo);
        if (pics != null)
            map.put("pics", pics);
        map.put("is_edit", is_edit);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .addLogisticsShipInfo(getRequestBody(map));

        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.submitSuccess();
            }
        });

    }


    public void uploadPic(List<ImageEntity> filePath, final String uploadPath) {
        if (isEmpty(filePath)) {
            return;
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
            if (file == null)continue;
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
}
