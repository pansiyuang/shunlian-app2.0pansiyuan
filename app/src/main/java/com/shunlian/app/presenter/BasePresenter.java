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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.PayOrderEntity;
import com.shunlian.app.bean.RefreshTokenEntity;
import com.shunlian.app.listener.BaseContract;
import com.shunlian.app.listener.INetDataCallback;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.service.ApiService;
import com.shunlian.app.service.InterentTools;
import com.shunlian.app.ui.login.LoginAct;
import com.shunlian.app.utils.Code;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IView;
import com.shunlian.app.widget.HttpDialog;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
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
    protected ObjectMapper objectMapper;
    protected static int requestCount;//请求次数
    private HttpDialog httpDialog;
    protected boolean isLoading = false;//是否正在加载
    protected int currentPage = 1;//当前页
    protected int allPage;//总页数
    private final Resources resources;

    public BasePresenter(Context context, IV iView) {
        this.context = context;
        this.iView = iView;
        objectMapper = new ObjectMapper();
        requestCount = 0;
        resources = context.getResources();
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
    protected <T>void getNetData(final Call<BaseEntity<T>> tCall, final INetDataCallback<BaseEntity<T>> callback){
        getNetData(0,0,false,tCall,callback);
    }

    /**
     * 请求网络数据
     *
     * @param tCall
     * @param callback
     * @param <T>
     */
    protected <T>void getNetData(boolean isLoading,final Call<BaseEntity<T>> tCall, final INetDataCallback<BaseEntity<T>> callback){
        getNetData(0,0,isLoading,tCall,callback);
    }

    /**
     * 请求网络数据
     *
     * @param tCall
     * @param callback
     * @param <T>
     */
    protected <T> void  getNetData(final int emptyCode, final int failureCode,
                                   final boolean isLoading, final Call<BaseEntity<T>> tCall,
                                   final INetDataCallback<BaseEntity<T>> callback){
        if (tCall == null || callback == null)
            return;

        /*if (!NetworkUtils.isNetworkAvailable((Activity) context)){//断网提示
            return;
        }*/
        if (isLoading) {
            httpDialog = new HttpDialog(context);
            httpDialog.show();
        }
        tCall.enqueue(new Callback<BaseEntity<T>>() {
            @Override
            public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
                if (httpDialog != null){
                    httpDialog.dismiss();
                }
                BaseEntity<T> body = response.body();
                LogUtil.longW("onResponse============" + body.toString());
                if (body.code == 1000) {//请求成功
                    if (body.data instanceof EmptyEntity){
                        callback.onSuccess(body);
                    }else {
                        if (body.data != null) {
                            callback.onSuccess(body);
                        } else {
                            iView.showDataEmptyView(emptyCode);
                        }
                    }
                }else if (body.code != 1000 && interceptApi(call,body)){//目前余额支付失败时code!=100,data不为空
                    callback.onErrorData(body);
                }else {
                    callback.onErrorCode(body.code,body.message);
                    //请求错误
                    handlerCode(body.code, body.message,tCall.clone(),emptyCode,failureCode,isLoading);
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<T>> call, Throwable t) {
                if (httpDialog != null){
                    httpDialog.dismiss();
                }
                if (t != null)
                    t.printStackTrace();
                callback.onFailure();
                if (iView != null) {
                    iView.showFailureView(failureCode);
                }
            }
        });
    }

    private <T> void handlerCode(Integer code, String message, Call<BaseEntity<T>> clone,
                                 final int emptyCode,final int failureCode,final boolean isLoading) {
        if (code != Code.CODE_REFRESH_TOKEN_VALIDE) {
            Common.staticToast(message);
        }
        switch (code) {
            // TODO: 2017/10/19
            case Code.CODE_NO_LOGIN://未登录
                String token = SharedPrefUtil.getSharedPrfString("token", "");
                if (TextUtils.isEmpty(token)){
                    Common.staticToast(message);
                    goLogin();
                }else {
                    requestCount++;
                    if (requestCount >= 5){
                        clone.cancel();
                        return;
                    }
                    refreshToken(clone,emptyCode,failureCode,isLoading);
                }
                break;
            case Code.CODE_REFRESH_TOKEN_VALIDE://刷新token过期,让用户登录
                Common.staticToast(Common.getResources().getString(R.string.plase_again_login));
                goLogin();
                break;
        }
    }

    private void goLogin() {
        Common.clearLoginInfo();
        LoginAct.startAct(context);
    }


    private <T> void refreshToken(final Call<BaseEntity<T>> clone,final int emptyCode,final int failureCode,final boolean isLoading){
        String refresh_token = SharedPrefUtil.getSharedPrfString("refresh_token", "");
        String member_id = SharedPrefUtil.getSharedPrfString("member_id", "");
        Map<String,String> map = new HashMap<>();
        map.put("member_id",member_id);
        map.put("refresh_token",refresh_token);
        sortAndMD5(map);
        String stringEntry = null;
        try {
            stringEntry = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        Call<BaseEntity<RefreshTokenEntity>> baseEntityCall = getAddCookieApiService().refreshToken(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<RefreshTokenEntity>>(){
            @Override
            public void onSuccess(BaseEntity<RefreshTokenEntity> entity) {
                super.onSuccess(entity);
                RefreshTokenEntity data = entity.data;
                if (data != null) {
                    SharedPrefUtil.saveSharedPrfString("token", data.token);
                    SharedPrefUtil.saveSharedPrfString("refresh_token", data.refresh_token);
                    getNetData(emptyCode,failureCode,isLoading,clone,new SimpleNetDataCallback<BaseEntity<T>>());
                }
            }
        });
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
            if (TextUtils.isEmpty(value)){
                value = "";
            }
            sign.append(key + "=" + value);
            sign.append("&");
            if (i == strs.size() - 1) {
                sign.append("key=" + Constant.KEY);
            }
        }
        map.put("sign", getStringMD5(sign.toString()));
    }

    /**
     * 将键值对格式成 RequestBody对象并返回
     * @param map
     * @return
     */
    public RequestBody getRequestBody(Map map){
        String stringEntry = null;
        try {
            if (objectMapper == null){
                objectMapper = new ObjectMapper();
            }
            stringEntry = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), stringEntry);
        return requestBody;
    }

    private <T> boolean interceptApi(Call call, BaseEntity<T> body){
        String url = call.request().url().toString();
        LogUtil.longW("interceptApi=============url=".concat(url));
        if (!TextUtils.isEmpty(url) && (url.contains("order/checkout") || url.contains("order/payinorderlist"))){
            T data = body.data;
            if (data != null && data instanceof PayOrderEntity){
                PayOrderEntity entity = (PayOrderEntity) data;
                if ("credit".equals(entity.paytype)){//当支付类型为余额支付时，返回true
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断集合内容是否为空
     * @param list
     * @return
     */
    protected boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }

    protected boolean isEmpty(CharSequence sequence){
        return TextUtils.isEmpty(sequence);
    }

    /**
     * 获取文字资源
     *
     * @param stringResouce
     */
    protected String getStringResouce(int stringResouce) {
        return resources.getString(stringResouce);
    }

    /**
     * 获取颜色资源
     *
     *  @param colorResouce
     */
    protected int getColorResouce(int colorResouce) {
        return resources.getColor(colorResouce);
    }

    /**
     * 获取图片资源
     *
     *  @param drawableResouce
     */
    protected Drawable getDrawableResouce(int drawableResouce) {
        return resources.getDrawable(drawableResouce);
    }
}
