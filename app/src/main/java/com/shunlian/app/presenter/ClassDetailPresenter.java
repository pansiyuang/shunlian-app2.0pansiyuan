package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IClassDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/5/4.
 */

public class ClassDetailPresenter extends BasePresenter<IClassDetailView> {

    private String mId;
    private ShareInfoParam shareInfoParam;

    public ClassDetailPresenter(Context context, IClassDetailView iView, String id) {
        super(context, iView);
        mId = id;
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
        Map<String,String> map = new HashMap<>();
        map.put("classesId",mId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> classesshare = getApiService().classesshare(map);
        getNetData(false,classesshare,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                shareInfoParam = new ShareInfoParam();
                shareInfoParam.shareLink = entity.data.share_url;
                shareInfoParam.title = entity.data.title;
                shareInfoParam.desc = entity.data.desc;
            }
        });
    }

    public ShareInfoParam getShareInfoParam() {
        return shareInfoParam;
    }
}
