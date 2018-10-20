package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.shunlian.app.adapter.SelectGoodsAdapter;
import com.shunlian.app.bean.AddGoodsEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class SelectGoodsPresenter extends BasePresenter {

    private final String page_size = "20";
    private List<GoodsDeatilEntity.Goods> goodsLists = new ArrayList<>();
    private SelectGoodsAdapter adapter;

    public SelectGoodsPresenter(Context context, IView iView) {
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

    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        getValidGoods(true);
    }

    public void getValidGoods(boolean isShowLoading) {
        Map<String, String> map = new HashMap<>();
        map.put("from", "ALL");
        map.put("page", String.valueOf(currentPage));
        map.put("page_size", page_size);
        sortAndMD5(map);
        Call<BaseEntity<AddGoodsEntity>> baseEntityCall = getApiService().validGoods(map);
        getNetData(isShowLoading, baseEntityCall, new SimpleNetDataCallback<BaseEntity<AddGoodsEntity>>() {
            @Override
            public void onSuccess(BaseEntity<AddGoodsEntity> entity) {
                super.onSuccess(entity);
                AddGoodsEntity addGoodsEntity = entity.data;
                isLoading = false;
                setData(addGoodsEntity.list);
                currentPage = Integer.parseInt(entity.data.page);
                allPage = Integer.parseInt(entity.data.total_page);
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
            }
        });
    }

    private void setData(List<GoodsDeatilEntity.Goods> list) {
        if (!isEmpty(list)) {
            goodsLists.addAll(list);
        }
        if (adapter == null) {
            adapter = new SelectGoodsAdapter(context,true, goodsLists);
            if (iView != null)
                iView.setAdapter(adapter);

            adapter.setOnItemClickListener((view, position) -> {
                GoodsDeatilEntity.Goods goods = goodsLists.get(position);
                Intent intent = new Intent();
                intent.putExtra("goods",goods);
                ((Activity)context).setResult(Activity.RESULT_OK,intent);
                ((Activity)context).finish();
            });
        } else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(currentPage,allPage);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading) {
            if (currentPage <= allPage) {
                isLoading = true;
                getValidGoods(false);
            }
        }
    }
}
