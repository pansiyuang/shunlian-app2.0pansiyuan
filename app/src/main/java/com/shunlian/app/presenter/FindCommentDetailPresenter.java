package com.shunlian.app.presenter;

import android.content.Context;
import android.view.View;

import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FindCommentDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentDetailEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.discover.CommentDetailAct;
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

    private String mComment_id;
    private final String page_size = "10";
    private String article_id;
    private FindCommentDetailAdapter adapter;
    private FindCommentListEntity.ItemComment itemComment;
    private List<FindCommentListEntity.ItemComment> mReplyListBeans = new ArrayList<>();
    private final CommentDetailAct mDetailAct;
    private int currentTouchItem = -1;

    public FindCommentDetailPresenter(Context context, IFindCommentDetailView iView, String comment_id) {
        super(context, iView);
        mDetailAct = (CommentDetailAct) context;
        mComment_id = comment_id;
        initApi();
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    @Override
    protected void initApi() {
        request(true, 0);
    }

    private void request(boolean isShow, int failure) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_id", mComment_id);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);

        Call<BaseEntity<CommentDetailEntity>> baseEntityCall = getApiService().commentDetail(map);
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
                    pointFabulous(itemComment.item_id, itemComment.had_like);
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
        sendComment(content, pid, article_id, "detail");
    }


    public void delComment() {
        delComment(itemComment.item_id);
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item) {
        mReplyListBeans.add(1, insert_item);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void delSuccess() {
        mReplyListBeans.remove(currentTouchItem);
        adapter.notifyDataSetChanged();
    }
}
