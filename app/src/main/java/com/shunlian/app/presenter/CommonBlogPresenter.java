package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ICommonBlogView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/10/22.
 */

public class CommonBlogPresenter extends BasePresenter<ICommonBlogView> {
    public static int PAGE_SIZE = 10;
    public String currentMemberId;
    public String currentType;

    public CommonBlogPresenter(Context context, ICommonBlogView iView) {
        super(context, iView);
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

    public void initPage() {
        currentPage = 1;
    }

    public void getBlogList(boolean isFirst, String memberId, String type) {
        currentMemberId = memberId;
        currentType = type;
        Map<String, String> map = new HashMap<>();
        if (!isEmpty(memberId)) {
            map.put("member_id", memberId);
        }
        map.put("type", type);
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", String.valueOf(PAGE_SIZE));
        sortAndMD5(map);

        Call<BaseEntity<HotBlogsEntity>> baseEntityCall = getApiService().getblogs(map);
        getNetData(isFirst, baseEntityCall, new SimpleNetDataCallback<BaseEntity<HotBlogsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<HotBlogsEntity> entity) {
                super.onSuccess(entity);
                HotBlogsEntity hotBlogsEntity = entity.data;
                isLoading = false;
                iView.getFocusblogs(hotBlogsEntity, hotBlogsEntity.pager.page, hotBlogsEntity.pager.total_page);
                currentPage = hotBlogsEntity.pager.page;
                allPage = hotBlogsEntity.pager.total_page;
                if (currentPage == 1) {
                    iView.refreshFinish();
                }
                currentPage++;
            }

            @Override
            public void onFailure() {
                isLoading = false;
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                isLoading = false;
                super.onErrorCode(code, message);
            }
        });
    }


    //1关注，2取消关注
    public void focusUser(int type, String memberId) {
        Map<String, String> map = new HashMap<>();
        if (type == 0) {
            map.put("type", "1");
        } else {
            map.put("type", "2");
        }
        map.put("member_id", memberId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().focusUser(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.focusUser(type, memberId);
                Common.staticToast(entity.message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void praiseBlos(String blogId) {
        Map<String, String> map = new HashMap<>();
        map.put("blog_id", blogId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().praiseBlog(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.praiseBlog(blogId);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void downCount(String blogId) {
        Map<String, String> map = new HashMap<>();
        map.put("blog_id", blogId);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().downCount(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<EmptyEntity>>() {
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.downCountSuccess(blogId);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void goodsShare(String type, String blogId, String id) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id", id);
        sortAndMD5(map);

        Call<BaseEntity<CommonEntity>> baseEntityCall = getSaveCookieApiService().shareSuccessCall(getRequestBody(map));
        getNetData(baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.shareGoodsSuccess(blogId, id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }


    public void sendComment(String content, String pid, String article_id, String level) {
        Map<String, String> map = new HashMap<>();
        map.put("discovery_id", article_id);
        map.put("content", content);
        map.put("reply_comment_id", pid);
        map.put("level", level);//0一级评论，直接针对文章进行评论，1二级评论，对一级评论进行评论，2三级评论，对二级评论进行评论
        sortAndMD5(map);

        Call<BaseEntity<FindCommentListEntity.ItemComment>> baseEntityCall = getAddCookieApiService().sendComment(getRequestBody(map));
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<FindCommentListEntity.ItemComment>>() {
            @Override
            public void onSuccess(BaseEntity<FindCommentListEntity.ItemComment> entity) {
                super.onSuccess(entity);
                FindCommentListEntity.ItemComment itemComment = entity.data;
                BigImgEntity.CommentItem commentItem = new BigImgEntity.CommentItem(itemComment);
                iView.replySuccess(commentItem);
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                getBlogList(false, currentMemberId, currentType);
            }
        }
    }
}
