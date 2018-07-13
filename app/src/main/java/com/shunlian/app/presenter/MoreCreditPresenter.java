package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.adapter.MoreCreditAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MoreCreditEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IMoreCreditView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by zhanghe on 2018/7/13.
 */

public class MoreCreditPresenter extends BasePresenter<IMoreCreditView> {

    public String phoneNumber = "";//手机号
    private MoreCreditAdapter moreCreditAdapter;

    public MoreCreditPresenter(Context context, IMoreCreditView iView) {
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
                        setdata(entity.data.list);
                    }
                });
    }

    private void setdata(List<MoreCreditEntity.ListBean> list) {
        if (!isEmpty(list)){
            if (moreCreditAdapter == null) {
                moreCreditAdapter = new MoreCreditAdapter(context, list);
                iView.setAdapter(moreCreditAdapter);
            }else {
                moreCreditAdapter.notifyDataSetChanged();
            }
        }
    }
}
