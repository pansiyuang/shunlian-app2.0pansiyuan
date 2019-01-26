package com.shunlian.app.presenter;

import android.animation.Animator;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.FindCommentListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
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
    private Call<BaseEntity<FindCommentListEntity>> baseEntityCall;
    private LottieAnimationView mAnimationView;
    private String currentLevel;

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
        currentPage = 1;
        allPage = 1;
        isLoading = false;
        if (baseEntityCall != null) baseEntityCall.cancel();
        if (adapter != null) {
            adapter.unbind();
            adapter = null;
        }
        if (mItemComments != null) {
            mItemComments.clear();
            mItemComments = null;
        }
    }

    @Override
    protected void initApi() {
        currentPage = 1;
        allPage = 1;
        isLoading = false;
        mItemComments.clear();
        requestData(true, 0);
    }

    public void initData() {
        initApi();
    }

    public void requestData(boolean isShow, int failureCode) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(page_size));
        map.put("discovery_id", mArticle_id);
        sortAndMD5(map);
        baseEntityCall = getApiService().findcommentList(map);
        getNetData(0, failureCode, isShow, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FindCommentListEntity>>() {
            @Override
            public void onSuccess(BaseEntity<FindCommentListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                FindCommentListEntity data = entity.data;
                FindCommentListEntity.Pager pager = data.pager;
                if ("1".equals(pager.page)) {
                    if (!isEmpty(data.top_list)) {
                        hotCommentCount = data.top_list.size();
                        mItemComments.addAll(data.top_list);
                    }
                }
                currentPage = Integer.parseInt(pager.page);
                allPage = Integer.parseInt(pager.allPage);
                mItemComments.addAll(data.list);
                comment_type = data.comment_type;
                setCommentList(currentPage, allPage);
                iView.setCommentAllCount(data.total);
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
            adapter = new FindCommentListAdapter(context, mItemComments, hotCommentCount, comment_type);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(() -> onRefresh());

            adapter.setPointFabulousListener(new FindCommentListAdapter.OnPointFabulousListener() {
                @Override
                public void onPointFabulous(int position, int childPosition, LottieAnimationView lottieAnimationView) {
                    mAnimationView = lottieAnimationView;
                    currentTouchItem = position;
                    FindCommentListEntity.ItemComment itemComment;
                    if (childPosition != -1) {
                        itemComment = mItemComments.get(position).reply_list.get(childPosition);
                    } else {
                        itemComment = mItemComments.get(position);
                    }
                    pointFabulous(itemComment.id, childPosition);
                }

                @Override
                public void onReply(int position, int childPosition) {
                    if (childPosition == -1) {
                        currentLevel = "1";
                        itemComment = mItemComments.get(position);
                    } else {
                        currentLevel = "2";
                        itemComment = mItemComments.get(position).reply_list.get(childPosition);
                    }
                    iView.showorhideKeyboard("@".concat(itemComment.nickname));
                }

                @Override
                public void onDel(int position, int childPosition) {
                    currentTouchItem = position;
                    if (childPosition == -1) {
                        itemComment = mItemComments.get(position);
                    } else {
                        itemComment = mItemComments.get(position).reply_list.get(childPosition);
                    }
                    iView.delPrompt();
                }

                @Override
                public void onVerify(int position, int childPosition) {
                    currentTouchItem = position;
                    if (childPosition == -1) {
                        itemComment = mItemComments.get(position);
                    } else {
                        itemComment = mItemComments.get(position).reply_list.get(childPosition);
                    }
                    verifyComment(itemComment.id);
                }

                @Override
                public void onRejected(int position, int childPosition) {
                    currentTouchItem = position;
                    if (childPosition == -1) {
                        itemComment = mItemComments.get(position);
                    } else {
                        itemComment = mItemComments.get(position).reply_list.get(childPosition);
                    }
                    retractComment(itemComment.id);
                }
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage, allPage);
        isEmpty();
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

    public void pointFabulous(String item_id, int childPosition) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_id", item_id);
        map.put("like_status", "1");
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().pointFabulous(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                setPointFabulous(childPosition);
            }
        });
    }

    private void setPointFabulous(int childPosition) {
        if (mAnimationView != null && !mAnimationView.isAnimating()) {
            mAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    FindCommentListEntity.ItemComment itemComment;
                    if (childPosition == -1) {
                        itemComment = mItemComments.get(currentTouchItem);
                    } else {
                        itemComment = mItemComments.get(currentTouchItem).reply_list.get(childPosition);
                    }
                    itemComment.like_count++;
                    itemComment.like_status = "1";
                    adapter.notifyDataSetChanged();
                    currentTouchItem = -1;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimationView.playAnimation();
        }
    }

    public void sendComment(String content, String level) {
        String pid = "";
        if (itemComment != null) {
            pid = itemComment.id;
        }
        sendComment(content, pid, mArticle_id, level);
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item, String message) {
//        if (!getIsAllType()) {
//            Common.staticToasts(context, message, R.mipmap.icon_common_duihao);
//            return;
//        }
        Common.staticToasts(context, getStringResouce(R.string.send_success), R.mipmap.icon_common_duihao);
//        if (currentTouchItem != -1) {
//            mItemComments.remove(currentTouchItem);
//            mItemComments.add(currentTouchItem, insert_item);
//        } else {
//        }
        if ("0".equals(insert_item.level)) {
            mItemComments.add(0, insert_item);
        } else {
            for (FindCommentListEntity.ItemComment item : mItemComments) {
                if (item.id.equals(insert_item.reply_parent_comment_id)) {
                    item.reply_list.add(0, insert_item);
                    item.reply_count++;
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
        currentTouchItem = -1;
        itemComment = null;
        isEmpty();
        currentLevel = "0";
    }


    @Override
    protected void delSuccess(String commentId, String parentId, List<FindCommentListEntity.ItemComment> itemComments, int replyCount,int replyStatus) {
        if (isEmpty(parentId) || "0".equals(parentId)) {
            for (FindCommentListEntity.ItemComment comment : mItemComments) {
                if (commentId.equals(comment.id)) {
                    if (comment.reply_count != 0) {
                        comment.status = 3;
                    } else {
                        mItemComments.remove(comment);
                    }
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            currentTouchItem = -1;
            itemComment = null;
            isEmpty();
        } else {
            for (FindCommentListEntity.ItemComment comment : mItemComments) {
                if (parentId.equals(comment.id)) {
                    comment.reply_list = itemComments;
                    comment.reply_count = replyCount;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        Common.staticToasts(context,"删除成功",R.mipmap.icon_common_duihao);
    }

    @Override
    protected void verifySuccess(String commentId, String parentCommentId) {
        if (isEmpty(parentCommentId) || "0".equals(parentCommentId)) {
            for (FindCommentListEntity.ItemComment comment : mItemComments) {
                if (commentId.equals(comment.id)) {
                    comment.check_is_show = 2;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (parentCommentId.equals(itemComment.id)) {
                    List<FindCommentListEntity.ItemComment> list = itemComment.reply_list;
                    for (FindCommentListEntity.ItemComment item : list) {
                        if (commentId.equals(item.id)) {
                            item.check_is_show = 2;
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    break;
                }
            }
        }
        Common.staticToasts(context,"审核成功",R.mipmap.icon_common_duihao);
    }

    @Override
    protected void retractComment(String commentId, String parentCommentId) {
        if (isEmpty(parentCommentId) || "0".equals(parentCommentId)) {
            for (FindCommentListEntity.ItemComment comment : mItemComments) {
                if (commentId.equals(comment.id)) {
                    comment.check_is_show = 1;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (parentCommentId.equals(itemComment.id)) {
                    List<FindCommentListEntity.ItemComment> list = itemComment.reply_list;
                    for (FindCommentListEntity.ItemComment item : list) {
                        if (commentId.equals(item.id)) {
                            item.check_is_show = 1;
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
                    break;
                }
            }
        }
        Common.staticToasts(context,"撤回成功",R.mipmap.icon_common_duihao);
    }


    public void delComment() {
        delComment(itemComment.id);
    }

    public String getCommentLevel() {
        if (isEmpty(currentLevel)) {
            return "0";
        }
        return currentLevel;
    }

    /**
     * 评论类型，true是all，否则精选
     *
     * @return
     */
    private boolean getIsAllType() {
        if ("all".equals(comment_type))
            return true;
        else
            return false;
    }

    private void isEmpty() {
        if (isEmpty(mItemComments)) {
            iView.showDataEmptyView(100);
        } else {
            iView.showDataEmptyView(0);
        }
    }
}
