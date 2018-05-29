package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.ProbablyLikeAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ProbablyLikeEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IPaySuccessView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.PUT;

/**
 * Created by Administrator on 2018/4/28.
 */

public class PaySuccessPresenter extends BasePresenter<IPaySuccessView>{

    private String mPay_sn;
    private boolean isPlus=false;

    public PaySuccessPresenter(Context context, IPaySuccessView iView, String pay_sn,boolean isPlus) {
        super(context, iView);
        mPay_sn = pay_sn;
        this.isPlus=isPlus;
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
        map.put("pay_sn",mPay_sn);
        sortAndMD5(map);
        Call<BaseEntity<ProbablyLikeEntity>> baseEntityCall;
        if (isPlus){
            baseEntityCall= getApiService().productorderPayresult(map);
        }else {
            baseEntityCall = getApiService().probablyLike(map);
        }
        getNetData(true,baseEntityCall,new
                SimpleNetDataCallback<BaseEntity<ProbablyLikeEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<ProbablyLikeEntity> entity) {
                        super.onSuccess(entity);
                        if (!isEmpty(entity.data.may_be_buy_list)){
                            ProbablyLikeAdapter adapter = new ProbablyLikeAdapter
                                    (context,entity.data.may_be_buy_list,isPlus);

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
