package com.shunlian.app.presenter;

import android.content.Context;
import android.view.View;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FindCommentDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentDetailEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.UseCommentEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IFindCommentDetailView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/16.
 */

public class FindCommentDetailPresenter extends FindCommentPresenter<IFindCommentDetailView> {

    private String mExperience_id;
    private String mComment_id;
    private final String page_size = "10";
    private String article_id;
    private FindCommentDetailAdapter adapter;
    private FindCommentListEntity.ItemComment itemComment;
    private List<FindCommentListEntity.ItemComment> mReplyListBeans = new ArrayList<>();
    private int currentTouchItem = -1;

    public FindCommentDetailPresenter(Context context, IFindCommentDetailView iView,
                                      String experience_id, String comment_id) {
        super(context, iView);
        mExperience_id = experience_id;
        mComment_id = comment_id;
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        currentPage = 1;
        allPage = 1;
        isLoading = false;
        mReplyListBeans.clear();
        if (adapter != null){
//            adapter.unbind();
            adapter = null;
            mReplyListBeans = null;
        }
    }

    @Override
    protected void initApi() {
        request(true, 0);
    }

    private void request(boolean isShow, int failure) {
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(mExperience_id)){
            map.put("experience_id", mExperience_id);
        }
        map.put("comment_id", mComment_id);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<CommentDetailEntity>> baseEntityCall = null;
        if (!isEmpty(mExperience_id)){
            baseEntityCall = getApiService().experienceCommentDetail(map);
        }else {
            baseEntityCall = getApiService().commentDetail(map);
        }
        getNetData(0, failure, isShow, baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<CommentDetailEntity>>() {

                    @Override
                    public void onSuccess(BaseEntity<CommentDetailEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        CommentDetailEntity data = entity.data;
                        article_id = data.article_id;
                        mReplyListBeans.addAll(data.reply_list);
                        currentPage = Integer.parseInt(data.page);
                        allPage = Integer.parseInt(data.total_page);
                        commentDetailList(currentPage, allPage);
                        iView.setCommentAllCount(data.count);

                        currentPage++;
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        isLoading = false;
                    }
                });
    }

    private void commentDetailList(int currentPage, int allPage) {

        if (adapter == null) {
            iView.setHint("@" + mReplyListBeans.get(0).nickname);
            adapter = new FindCommentDetailAdapter(context, mReplyListBeans);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(new BaseRecyclerAdapter.OnReloadListener() {
                @Override
                public void onReload() {
                    onRefresh();
                }
            });

            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    currentTouchItem = position;
                    itemComment = mReplyListBeans.get(position);
                    if ("1".equals(itemComment.delete_enable)) {//删除评论
                        iView.delPrompt();
                    } else {
                        iView.showorhideKeyboard("@".concat(itemComment.nickname));
                    }
                }
            });

            adapter.setPointFabulousListener(new FindCommentDetailAdapter.OnPointFabulousListener() {
                @Override
                public void onPointFabulous(int position) {
                    currentTouchItem = position;
                    FindCommentListEntity.ItemComment itemComment = mReplyListBeans.get(position);
                    if (!isEmpty(mExperience_id)){
                        comment_Praise(itemComment.item_id, itemComment.had_like);
                    }else {
                        pointFabulous(itemComment.item_id, itemComment.had_like);
                    }
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                request(false, 0);
            }
        }
    }


    public void pointFabulous(String item_id, String opt) {
        Map<String, String> map = new HashMap<>();
        map.put("item_id", item_id);
        if ("1".equals(opt)) {
            map.put("opt", "unlike");
        } else {
            map.put("opt", "like");
        }
        map.put("with_last_likes", "Y");
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().pointFabulous(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity data = entity.data;
                setPointFabulous(data.new_likes, data.last_likes);
            }
        });
    }

    private void setPointFabulous(String new_likes, List<FindCommentListEntity.LastLikesBean> last_likes) {
        FindCommentListEntity.ItemComment itemComment = mReplyListBeans.get(currentTouchItem);
        itemComment.likes = new_likes;
        itemComment.had_like = "0".equals(itemComment.had_like) ? "1" : "0";
        adapter.setHeadPic(last_likes);
        adapter.notifyDataSetChanged();
    }


    public void sendComment(String content) {
        String pid = "";
        if (itemComment != null) {
            pid = itemComment.item_id;
        } else {
            pid = mReplyListBeans.get(0).item_id;
        }
        if (!isEmpty(mExperience_id)){
            sendExperience(content);
        }else {
            sendComment(content, pid, article_id, "detail");
        }
    }


    public void delComment() {
        if (!isEmpty(mExperience_id)){
            delExperienceComment();
        }else {
            delComment(itemComment.item_id);
        }
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item, String message) {
        mReplyListBeans.add(1, insert_item);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void delSuccess() {
        mReplyListBeans.remove(currentTouchItem);
        adapter.notifyDataSetChanged();
    }

    /***************************************心*********************************************/
    /***************************************得*********************************************/
    /***************************************详*********************************************/
    /***************************************情*********************************************/
    /***************************************处*********************************************/
    /***************************************理*********************************************/

    /**
     * 对心得评论点赞
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
                        CommonEntity data = entity.data;
                        setPointFabulous(data.new_likes, data.last_likes);
                    }
                });
    }


    /**
     * 删除心得评论
     */
    public void delExperienceComment() {
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        FindCommentListEntity.ItemComment itemComment = mReplyListBeans.get(currentTouchItem);
        map.put("comment_id",itemComment.item_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .deleteComment(getRequestBody(map));

        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                delSuccess();
            }
        });
    }


    /**
     * 发布心得评价
     * @param content
     */
    public void sendExperience(String content){
        Map<String,String> map = new HashMap<>();
        map.put("experience_id",mExperience_id);
        FindCommentListEntity.ItemComment itemComment = null;
        if (currentTouchItem > 0){
            itemComment = mReplyListBeans.get(currentTouchItem);
        }else {
            itemComment = mReplyListBeans.get(0);
        }
        map.put("pid",itemComment.item_id);
        map.put("content",content);
        map.put("from_page","detail");
        sortAndMD5(map);

        Call<BaseEntity<UseCommentEntity>> comment = getAddCookieApiService()
                .createComment(getRequestBody(map));
        getNetData(true,comment,new SimpleNetDataCallback<BaseEntity<UseCommentEntity>>(){
            @Override
            public void onSuccess(BaseEntity<UseCommentEntity> entity) {
                super.onSuccess(entity);
                UseCommentEntity data = entity.data;
                refreshItem(data.insert_item,entity.message);
            }
        });
    }
}
