package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IDiscoverTiezi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PDiscoverTiezi extends BasePresenter<IDiscoverTiezi> {
    private int pageSize=20;
    private boolean isFirst=true;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    private boolean babyIsLoading;
    private String circle_id;
    private List<DiscoveryTieziEntity.Mdata.Hot> mDatas = new ArrayList<>();
    private ShareInfoParam shareInfoParam;

    public PDiscoverTiezi(Context context, IDiscoverTiezi iView,String circle_id) {
        super(context, iView);
        this.circle_id=circle_id;
        getApiData(babyPage,circle_id);
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
    public void refreshBaby() {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getApiData(babyPage,circle_id);
        }
    }

    public void getApiData(int page,String circle_id){
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("circle_id", circle_id);
        map.put("pageSize", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<DiscoveryTieziEntity>> baseEntityCall = getApiService().discoveryList(map);
        getNetData(isFirst,baseEntityCall, new SimpleNetDataCallback<BaseEntity<DiscoveryTieziEntity>>() {
            @Override
            public void onSuccess(BaseEntity<DiscoveryTieziEntity> entity) {
                super.onSuccess(entity);
                DiscoveryTieziEntity data =entity.data;
                babyIsLoading = false;
                babyPage++;
                babyAllPage = Integer.parseInt(data.list.total_page);
                mDatas.addAll(data.list.new_inv);
                iView.setApiData(data.list,mDatas);
                isFirst=false;

                if (shareInfoParam == null)
                    shareInfoParam = new ShareInfoParam();
                shareInfoParam.shareLink = data.share_url;
                GoodsDeatilEntity.UserInfo user_info = data.user_info;
                if (user_info != null){
                    shareInfoParam.userAvatar = user_info.avatar;
                    shareInfoParam.userName = user_info.nickname;
                }
                DiscoveryTieziEntity.Mdata.TopicDetail topicDetail = data.list.topicDetail;
                shareInfoParam.title = topicDetail.title;
                shareInfoParam.desc = topicDetail.content;
                shareInfoParam.img = topicDetail.img;
                shareInfoParam.thumb_type = "1";
            }
        });
    }

    public ShareInfoParam getShareInfoParam() {
        return shareInfoParam;
    }
}
