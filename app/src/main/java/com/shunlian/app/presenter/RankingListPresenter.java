package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.RankingListEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IRankingListView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RankingListPresenter extends BasePresenter <IRankingListView>{

    public String op_id;
    public String cid;
    public final int page_size = 20;

    public RankingListPresenter(Context context, IRankingListView iView, String id) {
        super(context, iView);
        op_id = id;
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
        rankingList(true);
    }

    public void getNewRankingList(String g_cid){
        cid = g_cid;
        currentPage = 1;
        allPage = 1;
        rankingList(true);
    }

    private void rankingList(boolean isShowLoading) {
        Map<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(op_id) && TextUtils.isEmpty(cid))
            map.put("op_cid",op_id);

        if (!TextUtils.isEmpty(cid))
            map.put("cid",cid);

        map.put("page",String.valueOf(currentPage));
        map.put("page_size",String.valueOf(page_size));
        sortAndMD5(map);

        Call<BaseEntity<RankingListEntity>> baseEntityCall = getApiService().rankingList(map);
        getNetData(isShowLoading,baseEntityCall,new SimpleNetDataCallback<BaseEntity<RankingListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RankingListEntity> entity) {
                isLoading = false;

                RankingListEntity.Goods goods = entity.data.goods;
                if (goods != null){
                    currentPage = Integer.parseInt(goods.page);
                    allPage = Integer.parseInt(goods.total_page);
                }
                iView.rankingGoodsList(goods,currentPage,allPage);
                if (currentPage == 1) {
                    iView.rankingCategoryList(entity.data.category);
                }
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
                rankingList(false);
            }
        }
    }
}
