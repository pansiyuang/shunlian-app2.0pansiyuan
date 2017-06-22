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
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.UploadCmtPicEntity;
import com.shunlian.app.bean.UserInfo;
import com.shunlian.app.bean.UserLoginEntity;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by zhang on 2017/4/13 16 : 06.
 * 网络请求接口
 */

public interface ApiService {


    @Headers("Content-type:application/json; charset=utf-8")
    @POST("user/login.json")
    Call<BaseEntity<UserLoginEntity>> getUserLogin1(@Body UserInfo info);



    @GET("my/home.json")
    Call<BaseEntity<MyHomeEntity>> getmyHome();





    @Multipart
    @POST("My/comment.uploadCmtPic.json")
    Call<BaseEntity<UploadCmtPicEntity>> uploadPic1(@Query("ordersn") String ordersn,@Part MultipartBody.Part files);




    @Multipart
    @POST("My/Sign.step2.json")
    Call<BaseEntity<UploadCmtPicEntity>> uploadPics(@Part List<MultipartBody.Part> files);
}
