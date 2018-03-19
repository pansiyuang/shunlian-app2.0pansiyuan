package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.UseCommentEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
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

public class FindCommentListPresenter extends BasePresenter<IFindCommentListView> {

    private final int page_size = 10;
    private List<FindCommentListEntity.ItemComment> itemComments = new ArrayList<>();
    private String mArticle_id;

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
        requestData(true,0);
    }

    private void requestData(boolean isShow,int failureCode) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(page_size));
        map.put("article_id",mArticle_id);
        sortAndMD5(map);
        Call<BaseEntity<FindCommentListEntity>> baseEntityCall = getApiService().findcommentList(map);
        getNetData(0,failureCode,isShow,baseEntityCall,new SimpleNetDataCallback<BaseEntity<FindCommentListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<FindCommentListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                FindCommentListEntity data = entity.data;
                int hotCommentCount = 0;
                if ("1".equals(data.page)){
                    List<FindCommentListEntity.ItemComment> top_list = data.top_list;
                    if (top_list != null && top_list.size() > 0) {
                        hotCommentCount = top_list.size();
                        itemComments.addAll(top_list);
                    }
                }
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                LogUtil.zhLogW("===allPage======="+allPage);
                itemComments.addAll(data.comment_list);
                iView.setCommentList(itemComments,hotCommentCount,currentPage,allPage);
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

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            if (currentPage <= allPage){
                isLoading = true;
                requestData(false,0);
            }
        }
    }


    public void sendComment(String content,String pid){
        Map<String,String> map = new HashMap<>();
        map.put("article_id",mArticle_id);
        map.put("content",content);
        map.put("pid",pid);
        map.put("from_page","list");
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
}
