package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.MyCommentAdapter;
import com.shunlian.app.adapter.WaitAppendCommentAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.my_comment.CommentDetailAct;
import com.shunlian.app.view.IMyCommentListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentListPresenter extends BasePresenter<IMyCommentListView> {

    public static final int pageSize = 10;
    public static final int ALL = 0;
    public static final int APPEND = 1;
    private int currentStatus = ALL;
    public static final int ALL_COMMENT_CODE = 100;//所有评论码
    public static final int APPEND_COMMENT_CODE = 200;//追加评论码
    private int current_code;//当前码

    public int currentPageStatus = MyCommentListPresenter.ALL;
    private MyCommentAdapter allAdapter;
    private WaitAppendCommentAdapter waitAdapter;
    private List<CommentListEntity.Data> lists = new ArrayList<>();
    private String nickname;
    private String avatar;
    private Call<BaseEntity<MyCommentListEntity>> commentListCall;

    public MyCommentListPresenter(Context context, IMyCommentListView iView) {
        super(context, iView);
        //initApi();
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
        if (commentListCall != null)commentListCall.cancel();
        if (lists != null){
            lists.clear();
            lists = null;
        }
        if (waitAdapter != null){
            waitAdapter.unbind();
            waitAdapter = null;
        }
        if (allAdapter != null){
            allAdapter.unbind();
            allAdapter = null;
        }
        isLoading = false;
        currentStatus = ALL;
        currentPage = 1;
        allPage = 1;
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        myCommentListAll();
    }

    public void myCommentListAll() {
        currentStatus = ALL;
        current_code = ALL_COMMENT_CODE;
        myCommentList(ALL_COMMENT_CODE, ALL_COMMENT_CODE, true, ALL, 1);
    }

    public void myCommentListAppend() {
        currentStatus = APPEND;
        current_code = APPEND_COMMENT_CODE;
        myCommentList(APPEND_COMMENT_CODE, APPEND_COMMENT_CODE, true, APPEND, 1);
    }

    public void myCommentList(int empty, int failureCode, final boolean isShowLoad, int status, int page) {
        Map<String, String> map = new HashMap<>();
        map.put("status", String.valueOf(status));
        map.put("page", String.valueOf(page));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);

        commentListCall = getApiService().myCommentList(map);
        getNetData(empty, failureCode, isShowLoad, commentListCall,
                new SimpleNetDataCallback<BaseEntity<MyCommentListEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<MyCommentListEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        MyCommentListEntity data = entity.data;
                        allPage = Integer.parseInt(data.max_page);
                        currentPage = Integer.parseInt(data.page);
                        nickname = data.nickname;
                        avatar = data.avatar;
                        if ("1".equals(data.page)){
                            iView.setNicknameAndAvatar(data.nickname,data.avatar);
                        }
                        setData(data.list,data.page);
                        currentPage++;
                    }

                    @Override
                    public void onErrorCode(int code, String message) {
                        super.onErrorCode(code, message);
                        isLoading = false;
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                        isLoading = false;
                        loadFailure();
                        if (isEmpty(lists)){
                            iView.showFailureView(0);
                        }
                    }
                });
    }

    private void loadFailure() {
        if (current_code == MyCommentListPresenter.ALL_COMMENT_CODE) {
            if (allAdapter != null) {
                allAdapter.loadFailure();
            }
        } else {
            if (waitAdapter != null) {
                waitAdapter.loadFailure();
            }
        }
    }

    private void setData(List<CommentListEntity.Data> list, String page) {
        if (Integer.parseInt(page) == 1) {
            this.lists.clear();
            if (currentPageStatus == MyCommentListPresenter.ALL) {
                if (waitAdapter != null){
                    waitAdapter.unbind();
                    waitAdapter = null;
                }
            } else {
                if (allAdapter != null){
                    allAdapter.unbind();
                    allAdapter = null;
                }
            }
        }
        this.lists.addAll(list);
        if (currentPageStatus == MyCommentListPresenter.ALL) {
            allComment(currentPage, allPage);
        } else {
            appendComment(currentPage, allPage);
        }

        if (isEmpty(this.lists)){
            iView.showDataEmptyView(0);
        }else {
            iView.showDataEmptyView(100);
        }
    }

    private void allComment(int currentPage, int allPage) {
        if (allAdapter == null) {
            allAdapter = new MyCommentAdapter(context, true, this.lists);
            allAdapter.setPageLoading(currentPage, allPage);
            iView.setAdapter(allAdapter);
            allAdapter.setOnReloadListener(() -> onRefresh());

            allAdapter.setOnItemClickListener((view, position) -> {
                CommentListEntity.Data data = lists.get(position);
                CommentDetailAct.startAct(context, data, nickname, avatar);
            });
        } else {
            allAdapter.setPageLoading(currentPage, allPage);
            allAdapter.notifyDataSetChanged();
        }
    }

    private void appendComment(int currentPage, int allPage) {
        if (waitAdapter == null) {
            waitAdapter = new WaitAppendCommentAdapter(context, true, this.lists);
            waitAdapter.setPageLoading(currentPage, allPage);
            iView.setAdapter(waitAdapter);
            waitAdapter.setOnReloadListener(() -> onRefresh());

            waitAdapter.setOnItemClickListener((view, position) -> {
                CommentListEntity.Data data = lists.get(position);
                CommentDetailAct.startAct(context, data, nickname, avatar);
            });
        } else {
            waitAdapter.setPageLoading(currentPage, allPage);
            waitAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                myCommentList(current_code, current_code, false, currentStatus, currentPage);
            }
        }
    }
}
