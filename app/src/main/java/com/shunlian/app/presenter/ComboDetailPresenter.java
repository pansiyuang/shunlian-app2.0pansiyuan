package com.shunlian.app.presenter;

import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.view.IComboDetailView;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by Administrator on 2017/12/21.
 */

public class ComboDetailPresenter extends BasePresenter<IComboDetailView> {

    public ComboDetailPresenter(Context context, IComboDetailView iView) {
        super(context, iView);
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

    }

    /**
     * 套餐详情
     * @param combo_id
     * @param goods_id
     */
    public void getcombodetail(String combo_id, String goods_id){
        Map<String,String> map = new HashMap<>();
        map.put("combo_id",combo_id);
        map.put("goods_id",goods_id);
        sortAndMD5(map);

        Call<BaseEntity<ComboDetailEntity>> getcombodetail = getAddCookieApiService().getcombodetail(getRequestBody(map));
        getNetData(true,getcombodetail,new SimpleNetDataCallback<BaseEntity<ComboDetailEntity>>(){
            @Override
            public void onSuccess(BaseEntity<ComboDetailEntity> entity) {
                super.onSuccess(entity);
                iView.comboDetailData(entity.data);
            }
        });
    }
}
