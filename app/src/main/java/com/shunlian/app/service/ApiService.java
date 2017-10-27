package com.shunlian.app.service;

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

import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.bean.UploadCmtPicEntity;
import com.shunlian.app.bean.UserLoginEntity;
import com.shunlian.app.bean.WXLoginEntity;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by zhang on 2017/4/13 16 : 06.
 * 网络请求接口
 */

public interface ApiService {


    /**
     * 测试
     *
     * @param requestBody
     * @return
     */
//    @Headers("Content-type:application/json; charset=utf-8")
    @POST("user/login.json")
    Call<BaseEntity<UserLoginEntity>> getUserLogin1(@Body RequestBody requestBody);

    @GET("my/home.json")
    Call<BaseEntity<MyHomeEntity>> getmyHome();

    @GET("my/home.json")
    Call getmyHome1();

    @GET("Goods/item.json")
    Call<ResponseBody> goodsItem(@Query("goodsId") String goodsId);

    //    shopId  count page usePage sort sortby
    @GET("Shop/home.json")
    Call<ResponseBody> shopHome(@Query("shopId") String shopId, @Query("count") String count);

    @GET("Shop/home.json")
    Call<ResponseBody> shopHome1(@QueryMap Map<String, String> map);

    @GET
    Call<ResponseBody> url(@Url String url);

    @GET("shop/{abc}/home.json")
    Call<ResponseBody> url1(@Path("abc") String abc);

    @Multipart
    @POST("My/comment.uploadCmtPic.json")
    Call<BaseEntity<UploadCmtPicEntity>> uploadPic1(@Query("ordersn") String ordersn, @Part MultipartBody.Part files);

    @Multipart
    @POST("My/Sign.step2.json")
    Call<BaseEntity<UploadCmtPicEntity>> uploadPics(@Part List<MultipartBody.Part> files);

    /**
     * 2.0正式接口
     */

    /**
     * 选择推荐人
     *
     * @param map
     * @return
     */
    @GET("member/register/codeList")
    Call<BaseEntity<MemberCodeListEntity>> memberCodeList(@QueryMap Map<String, String> map);

    /**
     * 发送验证码
     *
     * @param requestBody
     * @return
     */
    @POST("member/common/sendSmsCode")
    Call<BaseEntity<String>> sendSmsCode(@Body RequestBody requestBody);

    /**
     * 获取图形验证码
     *
     * @return
     */
    @GET("member/Common/vcode")
    Call<ResponseBody> graphicalCode();

    Call<ResponseBody> code();

    @POST("/member/common/sendSmsCode")
    Call<ResponseBody> sendSms(@Body RequestBody requestBody);

    @POST("/member/login/index")
    Call<ResponseBody> login(@Body RequestBody requestBody);

    @POST("/member/Common/checkCode")
    Call<ResponseBody> checkCode(@Body RequestBody requestBody);

    /**
     * 注册
     *
     * @param requestBody
     * @return
     */
    @POST("member/register/index")
    Call<BaseEntity<RegisterFinishEntity>> register(@Body RequestBody requestBody);

    /**
     * 微信登录
     *
     * @param requestBody
     * @return
     */
    @POST("member/oauth/wechat")
    Call<BaseEntity<WXLoginEntity>> wxLogin(@Body RequestBody requestBody);

    /**
     * 推荐人验证
     *
     * @param map
     * @return
     */
    @GET("member/register/checkCode")
    Call<BaseEntity<String>> checkCode(@QueryMap Map<String, String> map);

    /**
     * 找回密码
     */
    @POST("/member/userinfo/findPwd")
    Call<BaseEntity<RegisterFinishEntity>> findPsw(@Body RequestBody requestBody);
}
