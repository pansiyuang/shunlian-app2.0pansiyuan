package com.shunlian.app.presenter;

import android.content.Context;
import android.view.KeyEvent;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BubbleEntity;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.bean.GetMenuEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.fragment.first_page.CateGoryFrag;
import com.shunlian.app.view.IFirstPage;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/10/20.
 */

public class PFirstPage extends BasePresenter<IFirstPage> {
    private int pageSize = 20;
    private int babyPage = 1;//当前页数
    private int babyAllPage = 0;
    public boolean babyIsLoading,isRest=false;
    private CateGoryFrag cateGoryFrag;

    public PFirstPage(Context context, IFirstPage iView, CateGoryFrag cateGoryFrag) {
        super(context, iView);
        this.cateGoryFrag=cateGoryFrag;
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

    public void getBubble() {
        Map<String, String> map = new HashMap<>();
        map.put("position", "0");
        sortAndMD5(map);
        Call<BaseEntity<BubbleEntity>> baseEntityCall = getApiService().getBubble(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<BubbleEntity>>() {
            @Override
            public void onSuccess(BaseEntity<BubbleEntity> entity) {
                super.onSuccess(entity);
                BubbleEntity data = entity.data;
                if (data != null) {
                    iView.setBubble(data);
                }else {
                    iView.showFailureView(666);
                }
            }

            @Override
            public void onErrorData(BaseEntity<BubbleEntity> bubbleEntityBaseEntity) {
                super.onErrorData(bubbleEntityBaseEntity);
                iView.showFailureView(666);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(666);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code,message);
                iView.showFailureView(666);
            }

        });
    }

    public void resetBaby(String cate_id,String sort_type) {
        isRest=true;
        babyPage = 1;
        babyIsLoading = true;
        getSearchGoods(cate_id,sort_type);
    }

    public void refreshBaby( String cate_id,String sort_type) {
        if (!babyIsLoading && babyPage <= babyAllPage) {
            babyIsLoading = true;
            getSearchGoods(cate_id,sort_type);
        }
    }

    public void getSearchGoods(String cate_id,String sort_type) {
        Map<String, String> map = new HashMap<>();
        map.put("cid", cate_id);
        map.put("sort_type", sort_type);
        map.put("page", String.valueOf(babyPage));
        map.put("page_size", String.valueOf(pageSize));
        sortAndMD5(map);

        Call<BaseEntity<SearchGoodsEntity>> searchGoodsCallback = getAddCookieApiService().getSearchGoods(getRequestBody(map));
        getNetData(false, searchGoodsCallback, new SimpleNetDataCallback<BaseEntity<SearchGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<SearchGoodsEntity> entity) {
                super.onSuccess(entity);
                SearchGoodsEntity searchGoodsEntity = entity.data;
                babyPage++;
                babyAllPage = Integer.parseInt(searchGoodsEntity.total_page);
                if (isRest){
                    if (cateGoryFrag.mDatasss != null && cateGoryFrag.mDatasss.size() > 0){
                        cateGoryFrag.mDatass.removeAll(cateGoryFrag.mDatasss);
                        cateGoryFrag.mDatasss.clear();
                    }
                    isRest=false;
                }
                iView.setGoods(searchGoodsEntity.goods_list, babyPage, babyAllPage);
            }
        });
    }

    public void getMenuData(){
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);

        Call<BaseEntity<GetMenuEntity>> baseEntityCall = getApiService().channelGetMenu(map);
        getNetData(true, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetMenuEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetMenuEntity> entity) {
                super.onSuccess(entity);
                iView.setTab(entity.data);
                if (entity.data==null)
                    iView.showDataEmptyView(888);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.showFailureView(888);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(888);
            }

            @Override
            public void onErrorData(BaseEntity<GetMenuEntity> getMenuEntityBaseEntity) {
                super.onErrorData(getMenuEntityBaseEntity);
                iView.showFailureView(888);
            }
        });

    }

    public void getContentData(String id){
        Map<String, String> map = new HashMap<>();
        map.put("id",id);
        sortAndMD5(map);
        Call<BaseEntity<GetDataEntity>> baseEntityCall = getApiService().channelGetData(map);
        getNetData(false, baseEntityCall, new SimpleNetDataCallback<BaseEntity<GetDataEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GetDataEntity> entity) {
                super.onSuccess(entity);
                iView.setContent(entity.data);
            }

            @Override
            public void onErrorData(BaseEntity<GetDataEntity> getDataEntityBaseEntity) {
                super.onErrorData(getDataEntityBaseEntity);
                iView.setContent(null);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.setContent(null);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                iView.setContent(null);
            }
        });
    }

}
