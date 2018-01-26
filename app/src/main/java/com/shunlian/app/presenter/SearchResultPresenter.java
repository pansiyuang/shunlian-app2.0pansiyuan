package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.ICollectionSearchResultView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/25.
 */

public class SearchResultPresenter extends BasePresenter<ICollectionSearchResultView> {


    private String mKeyword;
    private String mType;
    private final int page_size = 10;

    public SearchResultPresenter(Context context, ICollectionSearchResultView iView,
                                 String keyword, String type) {
        super(context, iView);
        mKeyword = keyword;
        mType = type;
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

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        Map<String,String> map = new HashMap<>();
        map.put("key_words",mKeyword);
        map.put("type",mType);
        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(page_size));
        sortAndMD5(map);

        if ("goods".equals(mType)){
            Call<BaseEntity<CollectionGoodsEntity>> baseEntityCall = getAddCookieApiService()
                    .collectionGoodsSearch(getRequestBody(map));
            getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CollectionGoodsEntity>>(){
                @Override
                public void onSuccess(BaseEntity<CollectionGoodsEntity> entity) {
                    super.onSuccess(entity);

                }
            });
        }else {

        }
    }
}
