package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.R;
import com.shunlian.app.adapter.GuanzhuAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGuanzhuView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GuanzhuPresenter extends BasePresenter<IGuanzhuView> {

    private List<GuanzhuEntity.DynamicListBean> mListBeans = new ArrayList<>();
    private GuanzhuAdapter adapter;
    private final String page_size = "10";
    private int operationId = 0;
    private int currentPosition;

    public GuanzhuPresenter(Context context, IGuanzhuView iView) {
        super(context, iView);
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
        mListBeans.clear();
        if (adapter != null){
//            adapter.unbind();
            adapter = null;
            mListBeans = null;
        }
    }

    @Override
    public void initApi() {
        currentPage = 1;
        allPage = 1;
        mListBeans.clear();
        request(true,0);
    }

    private void request(final boolean isShow, int failure) {
        Map<String,String> map = new HashMap<>();
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",page_size);
        sortAndMD5(map);
        Call<BaseEntity<GuanzhuEntity>> baseEntityCall = getApiService().foucsHome(map);
        getNetData(0,0,isShow,baseEntityCall,
                new SimpleNetDataCallback<BaseEntity<GuanzhuEntity>>(){
            @Override
            public void onSuccess(BaseEntity<GuanzhuEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                GuanzhuEntity data = entity.data;
                currentPage = Integer.parseInt(data.page);
                allPage = Integer.parseInt(data.total_page);
                mListBeans.addAll(data.dynamic_list);
                setGuanzhuList(currentPage,allPage);
                if (currentPage == 1){
                    iView.refreshFinish();
                }
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

    private void setGuanzhuList(int currentPage, int allPage) {
        if (adapter == null) {
            adapter = new GuanzhuAdapter(context, mListBeans);
            iView.setAdapter(adapter);
            adapter.setOnFollowShopListener((position)-> {
                operationId = position;
                //是否关注，1是，0否
                GuanzhuEntity.DynamicListBean dynamicListBean = mListBeans.get(position);
                String store_id = dynamicListBean.store_id;
                if ("1".equals(dynamicListBean.has_follow)){
                    delFollowStore(store_id);
                }else {
                    followStore(store_id);
                }
            });

            adapter.setOnItemClickListener((view,position)-> {
                operationId = position;
                GuanzhuEntity.DynamicListBean dynamicListBean = mListBeans.get(position);
                if (!"new_sales".equals(dynamicListBean.type)){//文章
                    ArticleH5Act.startAct(context, dynamicListBean.id, ArticleH5Act.MODE_SONIC);
                }
            });

            adapter.setOnShareLikeListener((position,isShare)-> {
                currentPosition = position;
                if (isShare){//分享

                }else {//点赞
                    GuanzhuEntity.DynamicListBean dynamicListBean = mListBeans.get(position);
                    if ("1".equals(dynamicListBean.has_like)){
                        articleUnLike(dynamicListBean.id);
                    }else {
                        articleLike(dynamicListBean.id);
                    }
                }
            });
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
        if (isEmpty(mListBeans)){
            iView.showDataEmptyView(0);
        }else {
            iView.showDataEmptyView(1);
        }
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
    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                followResult(true);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /*
    关注结果，true是已关注，false未关注
     */
    private void followResult(boolean b) {
        GuanzhuEntity.DynamicListBean dy = mListBeans.get(operationId);
        if (b){
            dy.has_follow = "1";
        }else {
            dy.has_follow = "0";
        }
        adapter.notifyDataSetChanged();
    }


    /**
     * 取消关注店铺
     * @param storeId
     */
    public void delFollowStore(String storeId){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                followResult(false);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }


    public void articleLike(final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().userLike(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                changeLikeNum(entity.data.new_likes);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(getStringResouce(R.string.discover_failure_zan));
            }
        });
    }

    private void changeLikeNum(String new_likes) {
        GuanzhuEntity.DynamicListBean dy = mListBeans.get(currentPosition);
        dy.likes = new_likes;
        dy.has_like = "1".equals(dy.has_like)?"0":"1";
        adapter.notifyDataSetChanged();
    }

    public void articleUnLike(final String articleId) {
        Map<String, String> map = new HashMap<>();
        map.put("id", articleId);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getApiService().userUnLike(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                changeLikeNum(entity.data.new_likes);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(getStringResouce(R.string.discover_cacle_failure_zan));
            }
        });
    }
}
