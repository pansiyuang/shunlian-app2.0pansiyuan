//package com.shunlian.app.presenter;
//
//import android.content.Context;
//
//import com.shunlian.app.bean.BaseEntity;
//import com.shunlian.app.bean.EmptyEntity;
//import com.shunlian.app.listener.SimpleNetDataCallback;
//import com.shunlian.app.view.IProfitExtractView;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import retrofit2.Call;
//
///**
// * Created by Administrator on 2018/4/16.
// */
//
//public class ProfitExtractPresenter extends BasePresenter<IProfitExtractView> {
//
//    public ProfitExtractPresenter(Context context, IProfitExtractView iView) {
//        super(context, iView);
//    }
//
//    /**
//     * 加载view
//     */
//    @Override
//    public void attachView() {
//
//    }
//
//    /**
//     * 卸载view
//     */
//    @Override
//    public void detachView() {
//
//    }
//
//    /**
//     * 处理网络请求
//     */
//    @Override
//    public void initApi() {
//
//        Map<String,String> map = new HashMap<>();
//        sortAndMD5(map);
//
//        Call<BaseEntity<EmptyEntity>> baseEntityCall = getApiService().withdrawProfit(map);
//        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
//            @Override
//            public void onSuccess(BaseEntity<EmptyEntity> entity) {
//                super.onSuccess(entity);
//                iView.extractSuccess();
//            }
//        });
//
//    }
//}
