package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.ExperienceDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ExchangDetailEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IExperienceDetailView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailPresenter extends BasePresenter<IExperienceDetailView> {

    private String mExperience_id;
    private final String page_size = "10";
    private ExperienceDetailAdapter adapter;
    private ExchangDetailEntity.ExperienceInfo mExperienceInfo;
    private List<FindCommentListEntity.ItemComment> mCommentLists = new ArrayList<>();
    private int currentPosition = -1;

    public ExperienceDetailPresenter(Context context, IExperienceDetailView iView,
                                     String experience_id) {
        super(context, iView);
        mExperience_id = experience_id;
        initApi();
        EventBus.getDefault().register(this);
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
        EventBus.getDefault().unregister(this);
    }

    /**
     * 处理网络请求
     */
    @Override
    public void initApi() {
        allPage = 1;
        currentPage = 1;
        isLoading = false;
        requestData(true);
    }

    private void requestData(boolean isShow) {
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",page_size);
        sortAndMD5(map);

        Call<BaseEntity<ExchangDetailEntity>> baseEntityCall = getApiService().experienceDetail(map);
        getNetData(isShow,baseEntityCall,new SimpleNetDataCallback<BaseEntity<ExchangDetailEntity>>(){

            @Override
            public void onSuccess(BaseEntity<ExchangDetailEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                ExchangDetailEntity data = entity.data;
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                mExperienceInfo = data.experience_info;
                mCommentLists.addAll(data.comment_list);
                handleData();
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }
        });
    }

    private void handleData() {
        if (adapter == null) {
            adapter = new ExperienceDetailAdapter(context, mExperienceInfo, mCommentLists);
            iView.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
    }


    public void praiseExperience(String experienceId, String had_like) {
        Map<String, String> map = new HashMap<>();
        map.put("experience_id", experienceId);
        if ("1".equals(had_like)){
            map.put("status", "2");
        }else {
            map.put("status", "1");
        }
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService()
                .praiseExperience(getRequestBody(map));
        getNetData(false, baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                mExperienceInfo.had_like = "1".equals(mExperienceInfo.had_like)?"0":"1";
                mExperienceInfo.praise_num = entity.data.new_likes;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void comment_Praise(String comment_id,String had_like){
        Map<String, String> map = new HashMap<>();
        map.put("experience_id", mExperience_id);
        if ("1".equals(had_like)){
            map.put("status", "2");
        }else {
            map.put("status", "1");
        }
        map.put("comment_id",comment_id);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService()
                .comment_Praise(getRequestBody(map));
        getNetData(true,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<CommonEntity> entity) {
                        super.onSuccess(entity);
                        FindCommentListEntity.ItemComment itemComment = mCommentLists
                                .get(currentPosition - 2);
                        itemComment.had_like = "1".equals(itemComment.had_like)?"0":"1";
                        itemComment.likes = entity.data.new_likes;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void praiseCallback(DefMessageEvent event){
        if (event.praisePosition != -1){
            currentPosition = event.praisePosition;
            if (event.praisePosition == 0){
                String had_like = mExperienceInfo.had_like;
                praiseExperience(mExperienceInfo.id,had_like);
            }else {
                FindCommentListEntity.ItemComment itemComment = mCommentLists
                        .get(event.praisePosition - 2);
                comment_Praise(itemComment.item_id,itemComment.had_like);
            }
        }
    }
}
