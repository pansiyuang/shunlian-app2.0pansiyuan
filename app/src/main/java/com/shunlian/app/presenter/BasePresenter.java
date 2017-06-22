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

import com.shunlian.app.listener.INetDataCallback;
import com.shunlian.app.service.ApiService;
import com.shunlian.app.service.InterentTools;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by zhang on 2017/4/22 16 : 32.
 */

public class BasePresenter{

    public BasePresenter(){

    }



    private Retrofit getRetrofit(){
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .build();

        return tools.getRetrofit();
    }

    /**
     * 需要保存cookie
     * @return
     */
    private Retrofit getSaveCookieRetrofit(){
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .addReceivedCookiesInterceptor()
                .build();


        return tools.getRetrofit();
    }

    /**
     * 需要携带cookie
     * @return
     */
    private Retrofit getAddCookieRetrofit(){
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .addCookiesInterceptor()
                .build();

        return tools.getRetrofit();
    }


    protected ApiService getApiService(){
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 需要保存cookie调用这个
     * @return
     */
    protected ApiService getSaveCookieApiService(){
        ApiService apiService = getSaveCookieRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 需要携带cookie调这个
     * @return
     */
    protected ApiService getAddCookieApiService(){
        ApiService apiService = getAddCookieRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 请求网络数据
     * @param tCall
     * @param callback
     * @param <T>
     */
    protected <T> void getNetData(Call<T> tCall, final INetDataCallback<T> callback){
        if (tCall == null || callback == null)
            return;

        tCall.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                T body = response.body();
                if (body != null){
                    callback.onSuccess(body);
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                callback.onFailure();
            }
        });
    }
}
