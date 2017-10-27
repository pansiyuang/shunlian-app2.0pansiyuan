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

import android.app.Activity;
import android.content.Context;

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.listener.BaseContract;
import com.shunlian.app.listener.INetDataCallback;
import com.shunlian.app.service.ApiService;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.view.IView;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by zhang on 2017/4/22 16 : 32.
 */

public abstract class BasePresenter<IV extends IView> implements BaseContract {

    protected Context context;
    protected IV iView;

    public BasePresenter(Context context, IV iView) {
        this.context = context;
        this.iView = iView;

    }

    /**
     * 处理网络请求
     */
    protected abstract void initApi();


    private Retrofit getRetrofit() {
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .build();

        return tools.getRetrofit();
    }

    /*
     * 需要保存cookie
     * @return
     */
    private Retrofit getSaveCookieRetrofit() {
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .addReceivedCookiesInterceptor()
                .build();


        return tools.getRetrofit();
    }

    /*
     * 需要携带cookie
     * @return
     */
    private Retrofit getAddCookieRetrofit() {
        InterentTools tools = new InterentTools.Builder()
                .isOpenLogging(true)
                .connectTimeout()
                .addHeaderInterceptor()
                .addCookiesInterceptor()
                .build();

        return tools.getRetrofit();
    }


    protected ApiService getApiService() {
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 需要保存cookie调用这个
     *
     * @return
     */
    protected ApiService getSaveCookieApiService() {
        ApiService apiService = getSaveCookieRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 需要携带cookie调这个
     *
     * @return
     */
    protected ApiService getAddCookieApiService() {
        ApiService apiService = getAddCookieRetrofit().create(ApiService.class);
        return apiService;
    }

    /**
     * 请求网络数据
     *
     * @param tCall
     * @param callback
     * @param <T>
     */
    protected <T> void  getNetData(final Call<BaseEntity<T>> tCall, final INetDataCallback<BaseEntity<T>> callback){
        if (tCall == null || callback == null)
            return;

        if (!NetworkUtils.isNetworkAvailable((Activity) context)){
            return;
        }
        tCall.enqueue(new Callback<BaseEntity<T>>() {
            @Override
            public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
                BaseEntity<T> body = response.body();
                LogUtil.longW("onResponse============" + body.toString());
                if (body.code == 1000) {//请求成功
                    if (body.data != null) {
                        LogUtil.longW("onSuccess============");
                        callback.onSuccess(body);
                    } else {
                        iView.showDataEmptyView();
                    }
                }else {
                    callback.onErrorCode(body.code,body.message);
                    //请求错误
                    handlerCode(body.code, body.message);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<T>> call, Throwable t) {
                if (t != null)
                    t.printStackTrace();
                callback.onFailure();
                if (iView != null) {
                    iView.showFailureView();
                }
            }
        });
    }

    private void handlerCode(Integer code, String message) {
        Common.staticToast(message);
        switch (code) {
            // TODO: 2017/10/19
        }
    }


    /**
     * 处理刷新逻辑
     */
    public void onRefresh() {

    }

    private String getStringMD5(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInt = new BigInteger(1, digest.digest());
            String s = bigInt.toString(16);
//            s="a123456789012345678952145a";//a123456789012345678952145a000
            if (s.length() < 32){
                int legnth = 32 - s.length();
//                LogUtil.zhLogW("getStringMD5=========legnth=="+legnth);
//                LogUtil.zhLogW("getStringMD5=========s=="+s);
//                LogUtil.zhLogW("前getStringMD5=========s.length()=="+s.length());
                for (int i = 0; i < legnth; i++) {
                    s = "0" + s;
                }
//                LogUtil.zhLogW("后getStringMD5=========s.length()=="+s.length());
//                LogUtil.zhLogW("后getStringMD5=========s=="+s);
            }
            return s;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sortAndMD5(Map<String, String> map) {
        long time = System.currentTimeMillis() / 1000;
        map.put("timestamp", String.valueOf(time));
        List<String> strs = new ArrayList<>();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            strs.add(next);
        }
        Collections.sort(strs);
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < strs.size(); i++) {
            String key = strs.get(i);
            String value = map.get(key);
            sign.append(key + "=" + value);
            sign.append("&");
            if (i == strs.size() - 1) {
                sign.append("key=" + Constant.KEY);
            }
        }
        map.put("sign", getStringMD5(sign.toString()));
    }
}
