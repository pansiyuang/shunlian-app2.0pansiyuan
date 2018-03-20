package com.shunlian.app.presenter;

import android.content.Context;

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

public class FindCommentDetailPresenter extends BasePresenter<IFindCommentDetailView> {

    private String mComment_id;
    private final String page_size = "10";
    private String article_id;
    private List<FindCommentListEntity.ItemComment> replyListBeans = new ArrayList<>();

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
                        replyListBeans.addAll(data.reply_list);
                        currentPage = Integer.parseInt(data.page);
                        allPage = Integer.parseInt(data.total_page);
                        iView.commentDetailList(replyListBeans, currentPage, allPage);
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            if (currentPage <= allPage){
                isLoading = true;
                request(false,0);
            }
        }
    }

    public void sendComment(String content,String pid){
        Map<String,String> map = new HashMap<>();
        map.put("article_id",article_id);
        map.put("content",content);
        map.put("pid",pid);
        map.put("from_page","detail");
        sortAndMD5(map);

        Call<BaseEntity<UseCommentEntity>> baseEntityCall = getAddCookieApiService().sendComment(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<UseCommentEntity>>(){
            @Override
            public void onSuccess(BaseEntity<UseCommentEntity> entity) {
                super.onSuccess(entity);
                iView.refreshItem(entity.data.insert_item);
            }
        });

    }

    public void delComment(String comment_id) {
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",comment_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delComment(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.delSuccess();
            }
        });
    }

    public void pointFabulous(String item_id,String opt){
        Map<String,String> map = new HashMap<>();
        map.put("item_id",item_id);
        if ("1".equals(opt)){
            map.put("opt","unlike");
        }else {
            map.put("opt","like");
        }
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().pointFabulous(map);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.setPointFabulous(entity.data.new_likes);
            }
        });
    }
}
