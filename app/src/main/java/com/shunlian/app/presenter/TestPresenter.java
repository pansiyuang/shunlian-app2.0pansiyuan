package com.shunlian.app.presenter;

//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

import android.content.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.UserLoginEntity;
import com.shunlian.app.listener.INetDataCallback;
import com.shunlian.app.view.IView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by zhang on 2017/6/20 15 : 23.
 */

public class TestPresenter extends BasePresenter {


    private String stringEntry;

    public TestPresenter(Context context, IView iView) {
        super(context, iView);
    }

    private void goodsItem() {

        getApiService().goodsItem("");
    }

    private void myHome() {
        Call<BaseEntity<MyHomeEntity>> baseEntityCall = getAddCookieApiService().getmyHome();
        getNetData(baseEntityCall, new INetDataCallback<BaseEntity<MyHomeEntity>>() {
            @Override
            public void onSuccess(BaseEntity<MyHomeEntity> myHomeEntityBaseEntity) {
                System.out.println(myHomeEntityBaseEntity.data.toString());
            }

            @Override
            public void onFailure() {

            }

            @Override
            public void onErrorCode(int code, String message) {

            }

            @Override
            public void onErrorData(BaseEntity<MyHomeEntity> myHomeEntityBaseEntity) {

            }
        });
    }

    /**
     * 处理网络请求
     */
    @Override
    protected void initApi() {
        System.out.println("==========initApi==============");
        Map<String,String> fieldMap = new HashMap<>();
        fieldMap.put("username","13400000006");
        fieldMap.put("password","123456");
        fieldMap.put("captcha","");
        String stringEntry = null;
        try {
            stringEntry =  new ObjectMapper().writeValueAsString(fieldMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"),stringEntry);
        Call<BaseEntity<UserLoginEntity>> userLogin1 = getSaveCookieApiService().getUserLogin1(requestBody);

        getNetData(userLogin1, new INetDataCallback<BaseEntity<UserLoginEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UserLoginEntity> userLoginEntityBaseEntity) {
                System.out.println(userLoginEntityBaseEntity.data.toString());

                myHome();
            }

            @Override
            public void onFailure() {
                System.out.println("==========失败==============");
            }

            @Override
            public void onErrorCode(int code, String message) {

            }

            @Override
            public void onErrorData(BaseEntity<UserLoginEntity> userLoginEntityBaseEntity) {

            }
        });


        goodsItem();
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
}
