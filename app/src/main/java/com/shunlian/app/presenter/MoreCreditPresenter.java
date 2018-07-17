package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;

import com.shunlian.app.adapter.MoreCreditAdapter;
import com.shunlian.app.adapter.TopUpHistoryAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CreditPhoneListEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.MoreCreditEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.more_credit.MoreCreditAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMoreCreditView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pay.PayListActivity;
import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditPresenter extends BasePresenter<IMoreCreditView> {

    public String phoneNumber = "";//手机号
    private MoreCreditAdapter moreCreditAdapter;
    private List<MoreCreditEntity.ListBean> list = new ArrayList<>();
    private List<CreditPhoneListEntity.ListBean> historyLists = new ArrayList<>();
    private TopUpHistoryAdapter historyAdapter;
    private int delPosition;//删除手机号条目位置

    public MoreCreditPresenter(Context context, IMoreCreditView iView) {
        super(context, iView);
        initApi();
        topUpHistory();
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
    public void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", phoneNumber);
        sortAndMD5(map);
        Call<BaseEntity<MoreCreditEntity>> creditProductList = getAddCookieApiService()
                .getCreditProductList(getRequestBody(map));
        getNetData(true, creditProductList, new
                SimpleNetDataCallback<BaseEntity<MoreCreditEntity>>() {
                    @Override
                    public void onSuccess(BaseEntity<MoreCreditEntity> entity) {
                        super.onSuccess(entity);
                        if (!isEmpty(entity.data.card_address))
                            iView.setBelongingTo(phoneNumber,entity.data.card_address);
                        setdata(entity.data.list);
                    }

                    @Override
                    public void onErrorCode(int code, String message) {
                        super.onErrorCode(code, message);
                        if (code == 0){
                            iView.phoneError();
                        }
                    }
                });
    }

    private void setdata(List<MoreCreditEntity.ListBean> list) {
        if (!isEmpty(list)){
            this.list.clear();
            this.list.addAll(list);
            if (moreCreditAdapter == null) {
                moreCreditAdapter = new MoreCreditAdapter(context, this.list);
                iView.setAdapter(moreCreditAdapter);
                moreCreditAdapter.setOnItemClickListener((view, position) -> {
                    moreCreditAdapter.currentPos = position;
                    moreCreditAdapter.notifyDataSetChanged();
                });
            }else {
                moreCreditAdapter.notifyDataSetChanged();
            }
        }
    }

    public void topUp(String phone) {
        if (moreCreditAdapter != null && (moreCreditAdapter.currentPos >= 0
                && moreCreditAdapter.currentPos < list.size())) {
            MoreCreditEntity.ListBean listBean = list.get(moreCreditAdapter.currentPos);
            PayListActivity.startAct((Activity) context, phone, listBean.face_price,listBean.sale_price);
        }else {
            Common.staticToast("请选择充值面额");
        }
    }


    public void topUpHistory(){
        Map<String,String> map = new HashMap<>();
        map.put("type","1");
        sortAndMD5(map);
        Call<BaseEntity<CreditPhoneListEntity>>
                baseEntityCall = getAddCookieApiService().listCard(getRequestBody(map));

        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<CreditPhoneListEntity>>(){

            @Override
            public void onSuccess(BaseEntity<CreditPhoneListEntity> entity) {
                super.onSuccess(entity);
                if (!isEmpty(entity.data.list)) {
                    historyLists.addAll(entity.data.list);
                    historyAdapter = new TopUpHistoryAdapter(context,historyLists);
                    historyAdapter.setDelPhoneListener((id, position) -> {
                        delPosition = position;
                        delPhone(id);
                    });
                    historyAdapter.setOnItemClickListener((view, position) -> {
                        if (context != null){
                            CreditPhoneListEntity.ListBean listBean = historyLists.get(position);
                            ((MoreCreditAct)context).setPhone(listBean.number);
                        }
                    });
                }
                iView.setTopUpHistoryAdapter(historyAdapter);
            }
        });
    }

    /**
     * 删除手机号
     * @param id
     */
    public void delPhone(String id){
        Map<String,String> map = new HashMap<>();
        map.put("type","1");
        map.put("id",id);
        sortAndMD5(map);
        Call<BaseEntity<EmptyEntity>>
                baseEntityCall = getAddCookieApiService().deleteCard(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                historyLists.remove(delPosition);
                if (historyAdapter != null)
                    historyAdapter.notifyItemRemoved(delPosition);
                if (isEmpty(historyLists)){
                    historyAdapter.unbind();
                    historyAdapter = null;
                    iView.setTopUpHistoryAdapter(historyAdapter);
                }
            }
        });
    }
}
