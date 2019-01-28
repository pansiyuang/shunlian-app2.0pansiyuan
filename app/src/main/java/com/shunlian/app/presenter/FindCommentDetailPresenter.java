package com.shunlian.app.presenter;

import android.animation.Animator;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.adapter.FindCommentDetailAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentDetailEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.UseCommentEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
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
    private FindCommentDetailAdapter adapter;
    private List<FindCommentListEntity.ItemComment> mReplyListBeans = new ArrayList<>();
    private List<FindCommentListEntity.ItemComment> parentList = new ArrayList<>();
    private FindCommentListEntity.ItemComment currentComment;
    private LottieAnimationView mAnimationView;
    private int currentTouchItem;
    private boolean isParent = true;
    private String currentCommentId;
    private String currentLevel;

    public FindCommentDetailPresenter(Context context, IFindCommentDetailView iView, String comment_id) {
        super(context, iView);
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
        if (adapter != null) {
            adapter.unbind();
            adapter = null;
        }
        if (mReplyListBeans != null) {
            mReplyListBeans.clear();
            mReplyListBeans = null;
        }
    }

    @Override
    protected void initApi() {
        request(true, 0);
    }

    public void initData() {
        request(true, 0);
    }

    private void request(boolean isShow, int failure) {
        if (isShow) {
            currentPage = 1;
        }
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
                        currentPage = Integer.parseInt(data.page.page);
                        allPage = Integer.parseInt(data.page.allPage);

                        if (currentPage == 1) {
                            currentComment = data.info;
                            mReplyListBeans.clear();
                            mReplyListBeans.addAll(currentComment.reply_list);
                        } else {
                            mReplyListBeans.addAll(data.info.reply_list);
                            currentComment.reply_list = mReplyListBeans;
                        }
                        commentDetailList(currentPage, allPage);
                        iView.setCommentAllCount(data.page.total);
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
            parentList.add(currentComment);
            adapter = new FindCommentDetailAdapter(context, parentList);
            iView.setAdapter(adapter);
            adapter.setOnReloadListener(() -> onRefresh());

            adapter.setPointFabulousListener(new FindCommentDetailAdapter.OnPointFabulousListener() {

                @Override
                public void onPointFabulous(boolean isP, int childPosition, LottieAnimationView lottieAnimationView) {
                    mAnimationView = lottieAnimationView;
                    currentTouchItem = childPosition;
                    isParent = isP;
                    FindCommentListEntity.ItemComment itemComment;
                    if (isParent) {
                        itemComment = currentComment;
                    } else {
                        itemComment = mReplyListBeans.get(currentTouchItem);
                    }
                    pointFabulous(itemComment.id);
                }

                @Override
                public void onReply(boolean isP, int childPosition) {
                    FindCommentListEntity.ItemComment itemComment;
                    isParent = isP;
                    if (isP) {
                        itemComment = currentComment;
                    } else {
                        itemComment = mReplyListBeans.get(childPosition);
                    }
                    iView.showorhideKeyboard("@".concat(itemComment.nickname));
                }

                @Override
                public void onDel(boolean isP, int childPosition) {
                    currentTouchItem = childPosition;
                    isParent = isP;
                    iView.delPrompt();
                }

                @Override
                public void onVerify(boolean isP, int childPosition) {
                    FindCommentListEntity.ItemComment itemComment;
                    isParent = isP;
                    if (isP) {
                        itemComment = currentComment;
                    } else {
                        itemComment = mReplyListBeans.get(childPosition);
                    }
                    verifyComment(itemComment.id);
                }

                @Override
                public void onRejected(boolean isP, int childPosition) {
                    FindCommentListEntity.ItemComment itemComment;
                    isParent = isP;
                    if (isP) {
                        itemComment = currentComment;
                    } else {
                        itemComment = mReplyListBeans.get(childPosition);
                    }
                    retractComment(itemComment.id);
                }
            });
            iView.setHint("@" + currentComment.nickname);
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

    public void rejectedComment(String commentId, String parentId) {
        if (isEmpty(commentId)) {
            return;
        }
        if (isEmpty(parentId) || "0".equals(parentId)) {
            parentList.get(0).check_is_show = 0;
        } else {
            for (FindCommentListEntity.ItemComment itemComment : parentList.get(0).reply_list) {
                itemComment.check_is_show = 0;
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void pointFabulous(String item_id) {
        Map<String, String> map = new HashMap<>();
        map.put("comment_id", item_id);
        map.put("like_status", "1");
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().pointFabulous(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                setPointFabulous();
            }
        });
    }

    private void setPointFabulous() {
        if (mAnimationView == null || mAnimationView.isAnimating()) {
            return;
        }
        mAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isParent) {
                    parentList.get(0).like_status = "1";
                    parentList.get(0).like_count++;
                    isParent = true;
                } else {
                    FindCommentListEntity.ItemComment itemComment = mReplyListBeans.get(currentTouchItem);
                    itemComment.like_count++;
                    itemComment.like_status = "1";
                    isParent = false;
                }
                adapter.notifyDataSetChanged();
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

    public void clearComment() {
        currentCommentId = mComment_id;
        currentLevel = "1";
    }

    public void sendComment(String content) {
        if (isParent) {
            currentCommentId = currentComment.id;
            currentLevel = "1";
        } else {
            currentCommentId = mReplyListBeans.get(currentTouchItem).id;
            currentLevel = "2";
        }
        sendComment(content, currentCommentId, currentComment.discovery_id, "2");
    }


    public void delComment() {
        FindCommentListEntity.ItemComment itemComment;
        if (isParent) {
            itemComment = currentComment;
        } else {
            itemComment = mReplyListBeans.get(currentTouchItem);
        }
        delComment(itemComment.id);
    }

    @Override
    protected void refreshItem(FindCommentListEntity.ItemComment insert_item, String message) {
        mReplyListBeans.add(0, insert_item);
        currentComment.reply_list = mReplyListBeans;
        adapter.notifyDataSetChanged();
        currentTouchItem = -1;
        isParent = true;
        Common.staticToasts(context, "评论成功", R.mipmap.icon_common_duihao);
    }

    @Override
    protected void delSuccess(String commentId, String parentCommentId, List<FindCommentListEntity.ItemComment> itemCommentList, int replyCount, int replyStatus) {
        if (isParent) {
            currentComment = null;
            parentList.clear();
        } else {
            if (replyStatus == 1) {
                mReplyListBeans.get(currentTouchItem).status = 3;
            } else {
                mReplyListBeans.remove(currentTouchItem);
                currentComment.reply_list = mReplyListBeans;
            }
        }
        Common.staticToasts(context, "删除成功", R.mipmap.icon_common_duihao);
        adapter.notifyDataSetChanged();
        currentTouchItem = -1;
        isParent = true;
        isEmpty();
    }

    @Override
    protected void verifySuccess(String commentId, String parentCommentId) {
        if (isParent) {
            parentList.get(0).check_is_show = 2;
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mReplyListBeans) {
                if (commentId.equals(itemComment.id)) {
                    itemComment.check_is_show = 2;
                    break;
                }
            }
        }
        Common.staticToasts(context, "审核成功", R.mipmap.icon_common_duihao);
        adapter.notifyDataSetChanged();
        currentTouchItem = -1;
        isParent = true;
    }

    @Override
    protected void retractComment(String commentId, String parentCommentId) {
        if (isParent) {
            parentList.get(0).check_is_show = 1;
        } else {
            for (FindCommentListEntity.ItemComment itemComment : mReplyListBeans) {
                if (commentId.equals(itemComment.id)) {
                    itemComment.check_is_show = 1;
                    break;
                }
            }
        }
        Common.staticToasts(context, "撤回成功", R.mipmap.icon_common_duihao);
        adapter.notifyDataSetChanged();
        currentTouchItem = -1;
        isParent = true;
    }

    private void isEmpty() {
        if (currentComment == null) {
            iView.showDataEmptyView(100);
        } else {
            iView.showDataEmptyView(0);
        }
    }
}
