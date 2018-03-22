package com.shunlian.app.presenter;

import android.content.Context;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.FindCommentListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IFindCommentListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FindCommentListPresenter extends FindCommentPresenter<IFindCommentListView> {

    private final int page_size = 10;
    private List<FindCommentListEntity.ItemComment> mItemComments = new ArrayList<>();
    private String mArticle_id;
    private int hotCommentCount = 0;
    private FindCommentListAdapter adapter;
    private int currentTouchItem = -1;
    private FindCommentListEntity.ItemComment itemComment;
    private String comment_type;

    public FindCommentListPresenter(Context context, IFindCommentListView iView, String article_id) {
        super(context, iView);
        mArticle_id = article_id;
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
        requestData(true, 0);
    }

    private void requestData(boolean isShow, int failureCode) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(page_size));
        map.put("article_id", mArticle_id);
        sortAndMD5(map);
        Call<BaseEntity<FindCommentListEntity>> baseEntityCall = getApiService().findcommentList(map);
        getNetData(0, failureCode, isShow, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FindCommentListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<FindCommentListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                FindCommentListEntity data = entity.data;
                if ("1".equals(data.page)) {
                    if (!isEmpty(data.top_list)) {
                        hotCommentCount = data.top_list.size();
                        mItemComments.addAll(data.top_list);
                    }
                }
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                mItemComments.addAll(data.comment_list);
                comment_type = data.comment_type;
                setCommentList(currentPage, allPage);
                iView.setCommentAllCount(data.count);
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });
    }

    private void setCommentList(int currentPage, int allPage) {
        if (adapter == null) {
            adapter = new FindCommentListAdapter(context, mItemComments, hotCommentCount,comment_type);
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
                    position = adapter.getPosition(position);
                    currentTouchItem = position;
                    itemComment = mItemComments.get(position);
                    if ("1".equals(itemComment.delete_enable)) {//删除
                        iView.delPrompt();
                    } else {
                        if (getIsAllType())
                            iView.showorhideKeyboard("@".concat(itemComment.nickname));
                    }
                }
            });


            adapter.setPointFabulousListener(new FindCommentListAdapter.OnPointFabulousListener() {
                @Override
                public void onPointFabulous(int position) {
                    position = adapter.getPosition(position);
                    currentTouchItem = position;
                    FindCommentListEntity.ItemComment itemComment = mItemComments.get(position);
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
                requestData(false, 0);
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
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().pointFabulous(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                setPointFabulous(entity.data.new_likes);
            }
        });
    }

    private void setPointFabulous(String new_likes) {
        FindCommentListEntity.ItemComment itemComment = mItemComments.get(currentTouchItem);
        itemComment.likes = new_likes;
        itemComment.had_like = "0".equals(itemComment.had_like) ? "1" : "0";
        adapter.notifyDataSetChanged();
    }


    public void sendComment(String content) {
        String pid = "";
        if (itemComment != null) {
            pid = itemComment.item_id;
        }
        sendComment(content, pid, mArticle_id, "list");
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item, String message) {
        if (!getIsAllType()){
            Common.staticToasts(context, message, R.mipmap.icon_common_duihao);
            return;
        }
        Common.staticToasts(context, "发布成功", R.mipmap.icon_common_duihao);
        if (currentTouchItem != -1) {
            mItemComments.remove(currentTouchItem);
            mItemComments.add(currentTouchItem, insert_item);
        } else {
            mItemComments.add(hotCommentCount, insert_item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void delSuccess() {
        mItemComments.remove(currentTouchItem);
        adapter.notifyDataSetChanged();
    }


    public void delComment() {
        delComment(itemComment.item_id);
    }

    /**
     * 评论类型，true是all，否则精选
     * @return
     */
    private boolean getIsAllType(){
        if ("all".equals(comment_type))
            return true;
        else
            return false;
    }
}
