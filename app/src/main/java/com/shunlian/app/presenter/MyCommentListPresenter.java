package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMyCommentListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentListPresenter extends BasePresenter<IMyCommentListView> {

    public static final int pageSize = 20;
    public static final int ALL = 0;
    public static final int APPEND = 1;
    private int currentStatus = ALL;
    public static final int ALL_COMMENT_CODE = 100;//所有评论码
    public static final int APPEND_COMMENT_CODE = 200;//追加评论码
    private int current_code;//当前码


    public MyCommentListPresenter(Context context, IMyCommentListView iView) {
        super(context, iView);
        initApi();
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
        Call<BaseEntity<MyCommentListEntity>> baseEntityCall = getApiService().myCommentList(map);

        getNetData(empty, failureCode, isShowLoad, baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<MyCommentListEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<MyCommentListEntity> entity) {
                        super.onSuccess(entity);
                        isLoading = false;
                        MyCommentListEntity data = entity.data;
                        allPage = Integer.parseInt(data.max_page);
                        currentPage = Integer.parseInt(data.page);
                        iView.commentList(data.list, Integer.parseInt(data.page), allPage);
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
                    }
                });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            isLoading = true;
            if (currentPage <= allPage) {
                currentPage++;
                myCommentList(current_code, current_code, false, currentStatus, currentPage);
            }
        }
    }
}
