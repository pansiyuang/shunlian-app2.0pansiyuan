package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.ProbablyLikeAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ProbablyLikeEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.ISuccessfulTradeView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2018/4/28.
 */

public class SuccessfulTradePresenter extends BasePresenter<ISuccessfulTradeView> {

    private String mOrder_id;

    public SuccessfulTradePresenter(Context context, ISuccessfulTradeView iView, String order_id) {
        super(context, iView);
        mOrder_id = order_id;
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
        map.put("from","order");
        map.put("ref_id",mOrder_id);
        sortAndMD5(map);
        Call<BaseEntity<ProbablyLikeEntity>> baseEntityCall = getApiService()
                .mayBeBuy(getRequestBody(map));
        getNetData(true,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<ProbablyLikeEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<ProbablyLikeEntity> entity) {
                        super.onSuccess(entity);
                        if (!isEmpty(entity.data.may_be_buy_list)){
                            ProbablyLikeAdapter adapter = new ProbablyLikeAdapter
                                    (context,entity.data.may_be_buy_list,false);
                            iView.setAdapter(adapter);
                            adapter.setOnItemClickListener((v,p)->{
                                ProbablyLikeEntity.MayBuyList mayBuyList = entity.
                                        data.may_be_buy_list.get(p);
                                Common.goGoGo(context,"goods",mayBuyList.id);
                            });
                        }else {
                            iView.showDataEmptyView(100);
                        }
                    }
                });
    }
}
