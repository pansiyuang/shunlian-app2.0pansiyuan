package com.shunlian.app.presenter;

import android.content.Context;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.ExperienceDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ExchangDetailEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.UseCommentEntity;
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
        currentPage=1;
        allPage = 1;
        isLoading = true;
        currentPosition = -1;
        if (adapter != null){
            mCommentLists.clear();
//            adapter.unbind();
            adapter=null;
            mCommentLists=null;
        }
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
            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                String delete_enable = "0";
                @Override
                public void onItemClick(View view, int position) {
                    currentPosition = position;
                    if (position > 0){
                        FindCommentListEntity.ItemComment itemComment = mCommentLists.get(position - 2);
                        delete_enable = itemComment.delete_enable;
                        if ("1".equals(delete_enable)) {//删除
                            iView.delPrompt();
                        } else {
                            if (getIsAllType())
                                iView.showorhideKeyboard("@".concat(itemComment.nickname));
                        }
                    }else if (position == 0){
                        iView.showorhideKeyboard("@".concat(mExperienceInfo.nickname));
                    }

                }
            });

            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    onRefresh();
                }
            });
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
    }

    /**
     * 点赞
     * @param experienceId
     * @param had_like
     */
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
                currentPosition = -1;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /**
     * 对评论点赞
     * @param comment_id
     * @param had_like
     */
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
                        currentPosition = -1;
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

    /**
     * 发布评价
     * @param content
     */
    public void sendExperience(String content){
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        if (currentPosition > 0){
            FindCommentListEntity.ItemComment itemComment = mCommentLists.get(currentPosition - 2);
            map.put("pid",itemComment.item_id);
        }
        map.put("content",content);
        map.put("from_page","list");
        sortAndMD5(map);

        Call<BaseEntity<UseCommentEntity>> comment = getAddCookieApiService()
                .createComment(getRequestBody(map));
        getNetData(true,comment,new SimpleNetDataCallback<BaseEntity<UseCommentEntity>>(){
            @Override
            public void onSuccess(BaseEntity<UseCommentEntity> entity) {
                super.onSuccess(entity);
                FindCommentListEntity.ItemComment insert_item = entity.data.insert_item;
                if (insert_item != null && isEmpty(insert_item.content)){//开通审核
                    Common.staticToasts(context, entity.message, R.mipmap.icon_common_duihao);
                    return;
                }
                //直接发布
                Common.staticToasts(context, getStringResouce(R.string.send_success),
                        R.mipmap.icon_common_duihao);
                if (currentPosition > 1){
                    mCommentLists.remove(currentPosition - 2);
                    mCommentLists.add(currentPosition - 2,insert_item);
                    adapter.notifyDataSetChanged();
                }else {
                    mCommentLists.add(0, insert_item);
                    adapter.notifyDataSetChanged();
                }
                currentPosition = -1;
            }
        });
    }

    /**
     * 评论类型，true是all，否则精选
     * @return
     */
    private boolean getIsAllType(){
        if ("0".equals(mExperienceInfo.check_comment))
            return true;
        else
            return false;
    }

    /**
     * 删除评论
     */
    public void delComment() {
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        FindCommentListEntity.ItemComment itemComment = mCommentLists.get(currentPosition - 2);
        map.put("comment_id",itemComment.item_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .deleteComment(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                mCommentLists.remove(currentPosition - 2);
                adapter.notifyDataSetChanged();
                currentPosition = -1;
            }
        });

    }
}
