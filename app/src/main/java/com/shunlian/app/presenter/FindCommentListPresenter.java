package com.shunlian.app.presenter;

import android.animation.Animator;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.FindCommentListAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.eventbus_bean.BlogCommentEvent;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IFindCommentListView;

import org.greenrobot.eventbus.EventBus;

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
    private String currentCommentId;

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
                    mItemComments.clear();
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

    public void praiseData(String commentId, String parentId) {
        if (isEmpty(parentId) || "0".equals(parentId)) {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (commentId.equals(itemComment.id)) {
                    itemComment.like_status = "1";
                    itemComment.like_count++;
                    break;
                }
            }
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (parentId.equals(itemComment.id)) {
                    for (FindCommentListEntity.ItemComment item : itemComment.reply_list) {
                        if (commentId.equals(item.id)) {
                            item.like_status = "1";
                            item.like_count++;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void addCommentData(FindCommentListEntity.ItemComment insertItem) {
        if (isEmpty(insertItem.reply_parent_comment_id) || "0".equals(insertItem.reply_parent_comment_id)) {
            return;
        }
        for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
            if (insertItem.reply_parent_comment_id.equals(itemComment.id)) {
                itemComment.reply_list = insertItem.comment_list;
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void delCommentData(FindCommentListEntity.ItemComment delItem) {
        if (isEmpty(delItem.reply_parent_comment_id) || "0".equals(delItem.reply_parent_comment_id)) {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (delItem.comment_id.equals(itemComment.id)) {
                    itemComment.status = 3;
                    itemComment.reply_count = delItem.reply_count;
                }
            }
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (delItem.reply_parent_comment_id.equals(itemComment.id)) {
                    itemComment.reply_list = delItem.reply_result;
                    itemComment.reply_count = delItem.reply_count;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void verifyCommentData(String commentId, String parentId, int checkShow) {
        if (isEmpty(parentId) || "0".equals(parentId)) {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (commentId.equals(itemComment.id)) {
                    itemComment.check_is_show = checkShow;
                    break;
                }
            }
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mItemComments) {
                if (parentId.equals(itemComment.id)) {
                    for (FindCommentListEntity.ItemComment item : itemComment.reply_list) {
                        if (commentId.equals(item.id)) {
                            item.check_is_show = checkShow;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
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

    public void clearComment() {
        currentCommentId = "";
        currentLevel = "";
        itemComment = null;
    }

    public void sendComment(String content, String level) {
        if (itemComment != null) {
            currentCommentId = itemComment.id;
        }
        sendComment(content, currentCommentId, mArticle_id, level);
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item, String message) {
        Common.staticToasts(context, getStringResouce(R.string.send_success), R.mipmap.icon_common_duihao);
        if ("0".equals(insert_item.level)) {
            mItemComments.add(0, insert_item);
            insert_item.discovery_id = mArticle_id;
            iView.setCommentAllCount(String.valueOf(insert_item.comment_count));
            EventBus.getDefault().post(new BlogCommentEvent(BlogCommentEvent.ADD_TYPE, insert_item));
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
    protected void delSuccess(FindCommentListEntity.ItemComment item) {
        if (isEmpty(item.reply_parent_comment_id) || "0".equals(item.reply_parent_comment_id)) {
            for (FindCommentListEntity.ItemComment myComment : mItemComments) {
                item.discovery_id = mArticle_id;
                if (item.comment_id.equals(myComment.id)) {
                    if (myComment.reply_count != 0) {
                        myComment.status = 3;
                    } else {
                        mItemComments.remove(myComment);
                    }
                    break;
                }
            }
            iView.setCommentAllCount(String.valueOf(item.reply_count));
            EventBus.getDefault().post(new BlogCommentEvent(BlogCommentEvent.DEL_TYPE, item));
            adapter.notifyDataSetChanged();
            currentTouchItem = -1;
            itemComment = null;
            isEmpty();
        } else {
            for (FindCommentListEntity.ItemComment comment : mItemComments) {
                if (item.comment_id.equals(comment.id)) {
                    comment.reply_list = item.reply_result;
                    comment.reply_count = item.reply_count;
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        Common.staticToasts(context, "删除成功", R.mipmap.icon_common_duihao);
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
        Common.staticToasts(context, "审核成功", R.mipmap.icon_common_duihao);
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
        Common.staticToasts(context, "撤回成功", R.mipmap.icon_common_duihao);
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
