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

import com.shunlian.app.bean.AddressDataEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GetusernewsnumEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.JoinGoodsEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.RefreshTokenEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.bean.StoreLicenseEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.bean.TagEntity;
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

    /**
     * 发送短信验证码
     *
     * @param requestBody
     * @return
     */
    @POST("/member/common/sendSmsCode")
    Call<ResponseBody> sendSms(@Body RequestBody requestBody);

    /**
     * 登录
     *
     * @param requestBody
     * @return
     */
    @POST("/member/login/index")
    Call<BaseEntity<LoginFinishEntity>> login(@Body RequestBody requestBody);

    /**
     * 验证验证码
     *
     * @param requestBody
     * @return
     */
    @POST("/member/Common/checkCode")
    Call<ResponseBody> checkCode(@Body RequestBody requestBody);

    /**
     * 刷新token
     *
     * @param requestBody
     * @return
     */
    @POST("member/login/refreshToken")
    Call<BaseEntity<RefreshTokenEntity>> refreshToken(@Body RequestBody requestBody);

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

    /**
     * 检验手机号是否注册
     *
     * @param map
     * @return
     */
    @GET("member/register/checkMobile")
    Call<BaseEntity<String>> checkMobile(@QueryMap Map<String, String> map);

    /**
     * 商品详情
     *
     * @return
     */
    @POST("goods/detail")
    Call<BaseEntity<GoodsDeatilEntity>> goodsDetail(@Body RequestBody body);

    /**
     * 关注店铺
     *
     * @return
     */
    @POST("store/addMark")
    Call<BaseEntity<EmptyEntity>> addMark(@Body RequestBody body);

    /**
     * 取消关注店铺
     *
     * @return
     */
    @POST("store/delMark")
    Call<BaseEntity<EmptyEntity>> delMark(@Body RequestBody body);

    /**
     * 店铺首页
     *
     * @return
     */
    @GET("store/index")
    Call<BaseEntity<StoreIndexEntity>> storeIndex(@QueryMap Map<String, String> map);

    /**
     * 店铺宝贝
     *
     * @return
     */
    @GET("store/goodsList")
    Call<BaseEntity<StoreGoodsListEntity>> storeGoodsList(@QueryMap Map<String, String> map);

    /**
     * 店铺促销
     *
     * @return
     */
    @GET("store/promotionGoodsList")
    Call<BaseEntity<StorePromotionGoodsListEntity>> storePromotionGoodsList(@QueryMap Map<String, String> map);

    @GET("store/promotionGoodsList")
    Call<BaseEntity<StorePromotionGoodsListOneEntity>> storePromotionGoodsListOne(@QueryMap Map<String, String> map);

    @GET("store/promotionGoodsList")
    Call<BaseEntity<StorePromotionGoodsListTwoEntity>> storePromotionGoodsListTwo(@QueryMap Map<String, String> map);
    /**
     * 店铺新品
     *
     * @return
     */
    @GET("store/newGoodsList")
    Call<BaseEntity<StoreNewGoodsListEntity>> storeNewGoodsList(@QueryMap Map<String, String> map);

    /**
     * 店铺简介
     *
     * @return
     */
    @GET("store/introduce")
    Call<BaseEntity<StoreIntroduceEntity>> storeIntroduce(@QueryMap Map<String, String> map);

    /**
     * 店铺类目
     *
     * @return
     */
    @GET("store/categories")
    Call<BaseEntity<StoreCategoriesEntity>> storeCategories(@QueryMap Map<String, String> map);

    /**
     * 添加购物车
     *
     * @param body
     * @return
     */
    @POST("cart/add")
    Call<BaseEntity<CateEntity>> addCart(@Body RequestBody body);

    /**
     * 购物车首页
     */
    @POST("cart/home")
    Call<BaseEntity<ShoppingCarEntity>> storeList(@Body RequestBody body);

    /**
     * 商品详情页足迹
     *
     * @param body
     * @return
     */
    @POST("member/footermark/getsidemark")
    Call<BaseEntity<FootprintEntity>> footPrint(@Body RequestBody body);

    /**
     * 立即购买
     *
     * @param body
     * @return
     */
    @POST("order/buy")
    Call<BaseEntity<ConfirmOrderEntity>> orderBuy(@Body RequestBody body);

    /**
     * 修改购物车
     *
     * @param body
     * @return
     */
    @POST("/cart/edit")
    Call<BaseEntity<ShoppingCarEntity>> carEdit(@Body RequestBody body);

    /**
     * 购物车多选
     *
     * @param body
     * @return
     */
    @POST("/cart/checkcartgoods")
    Call<BaseEntity<ShoppingCarEntity>> checkCartGoods(@Body RequestBody body);

    /**
     * 优惠券领取
     *
     * @param body
     * @return
     */
    @POST("/voucher/getVoucher")
    Call<BaseEntity<GoodsDeatilEntity.Voucher>> getVoucher(@Body RequestBody body);


    /**
     * 购物车商品转入收藏夹
     *
     * @param body
     * @return
     */
    @POST("/cart/removetofav")
    Call<BaseEntity<ShoppingCarEntity>> removetofav(@Body RequestBody body);

    /**
     * 购物车删除商品
     *
     * @param body
     * @return
     */
    @POST("/cart/remove")
    Call<BaseEntity<ShoppingCarEntity>> cartRemove(@Body RequestBody body);


    /**
     * 购物车店铺全选删除
     *
     * @param body
     * @return
     */
    @POST("/cart/multyremove")
    Call<BaseEntity<ShoppingCarEntity>> multyremove(@Body RequestBody body);

    /**
     * 收藏商品
     *
     * @param body
     * @return
     */
    @POST("/member/goodsfavorite/add")
    Call<BaseEntity<EmptyEntity>> addFavorite(@Body RequestBody body);

    /**
     * 取消收藏商品
     *
     * @param body
     * @return
     */
    @POST("/member/goodsfavorite/remove")
    Call<BaseEntity<String>> removeFavorite(@Body RequestBody body);

    /**
     * 购物车进入确认订单页
     * @param body
     * @return
     */
    @POST("order/confirm")
    Call<BaseEntity<ConfirmOrderEntity>> orderConfirm(@Body RequestBody body);

    /**
     * 收藏商品
     * @param body
     * @return
     */
    @POST("member/goodsfavorite/add")
    Call<BaseEntity<CommonEntity>> goodsfavorite(@Body RequestBody body);

    /**
     * 移除收藏
     * @param body
     * @return
     */
    @POST("member/goodsfavorite/remove")
    Call<BaseEntity<CommonEntity>> goodsfavoriteRemove(@Body RequestBody body);

    /**
     * 评价列表
     * @param map
     * @return
     */
    @GET("comment/list")
    Call<BaseEntity<CommentListEntity>> commentList(@QueryMap Map<String,String> map);

    /**
     * 省市区数据
     * @param map
     * @return
     */
    @GET("district/all")
    Call<BaseEntity<DistrictAllEntity>> districtAll(@QueryMap Map<String, String> map);

    /**
     * 根据经纬度获取省市区
     * @param body
     * @return
     */
    @POST("district/getlocation")
    Call<BaseEntity<DistrictGetlocationEntity>> districtGetlocation(@Body RequestBody body);

    /**
     * 添加收货地址
     * @param body
     * @return
     */
    @POST("member/address/add")
    Call<BaseEntity<EmptyEntity>> addressAdd(@Body RequestBody body);

    /**
     * 编辑收货地址
     * @param body
     * @return
     */
    @POST("member/address/edit")
    Call<BaseEntity<EmptyEntity>> addressEdit(@Body RequestBody body);


    /**
     * 收货地址列表
     *
     * @param body
     * @return
     */
    @POST("/member/address/all")
    Call<BaseEntity<AddressDataEntity>> allAddress(@Body RequestBody body);

    /**
     * 删除收货地址
     *
     * @param body
     * @return
     */
    @POST("/member/address/remove")
    Call<BaseEntity<EmptyEntity>> delAddress(@Body RequestBody body);

    /**
     * 凑单界面商店类目
     *
     * @param body
     * @return
     */
    @POST("/cart/getjoingoodsstorecates")
    Call<BaseEntity<CateEntity>> megerGoodsCates(@Body RequestBody body);

    /**
     * 凑单
     *
     * @param body
     * @return
     */
    @POST("/cart/joingoods")
    Call<BaseEntity<JoinGoodsEntity>> getRecommmendGoods(@Body RequestBody body);

    /**
     * 获取商品sku信息
     *
     * @param body
     * @return
     */
    @POST("/goods/getgoodssku")
    Call<BaseEntity<GoodsDeatilEntity.GoodsInfo>> getGoodsSku(@Body RequestBody body);

    /**
     * 获取系统消息数量
     *
     * @param body
     * @return
     */
    @POST("user/getusernewsnum")
    Call<BaseEntity<GetusernewsnumEntity>> getusernewsnum(@Body RequestBody body);

    /**
     * 我的评价列表
     * @param map
     * @return
     */
    @GET("member/comment/list")
    Call<BaseEntity<MyCommentListEntity>> myCommentList(@QueryMap Map<String,String> map);

    /**
     * 查看营业执照
     * @param body
     * @return
     */
    @POST("store/businessLicense")
    Call<BaseEntity<StoreLicenseEntity>> storeLicense(@Body RequestBody body);



    /**
     * 上传图片
     */
    @Multipart
    @POST("https://v20-test.shunliandongli.com/uploads/uploadotherimage")
    Call<BaseEntity<UploadCmtPicEntity>> uploadPic(@Part MultipartBody.Part file);

    /**
     * 添加评价
     *
     * @param body
     * @return
     */
    @POST("/member/comment/append")
    Call<BaseEntity<EmptyEntity>> appendComment(@Body RequestBody body);

    /**
     * 修好好评
     *
     * @param body
     * @return
     */
    @POST("/member/comment/change")
    Call<BaseEntity<EmptyEntity>> changeComment(@Body RequestBody body);

    /**
     * 订单物流详情
     *
     * @return
     */
    @GET("/personalcenter/traces")
    Call<BaseEntity<OrderLogisticsEntity>> orderLogistics(@QueryMap Map<String, String> map);

    /**
     * 订单搜索历史
     *
     * @return
     */
    @GET("/personalcenter/searchhistory")
    Call<BaseEntity<TagEntity>> searchHistory(@QueryMap Map<String, String> map);

    /**
     * 清空历史搜索记录
     *
     * @return
     */
    @GET("/personalcenter/delhistory")
    Call<BaseEntity<EmptyEntity>> delHistory(@QueryMap Map<String, String> map);

    /**
     * 待评价和待追评混合列表
     * @return
     */
    @GET("member/comment/mixed_list")
    Call<BaseEntity<EmptyEntity>> mixed_list();

    /**
     * 个人订单列表
     * @param map
     * @return
     */
    @GET("/personalcenter/orderlist")
    Call<BaseEntity<MyOrderEntity>> orderList(@QueryMap Map<String,String> map);


    /**
     * 订单详情
     * @param map
     * @return
     */
    @GET("personalcenter/orderdetail")
    Call<BaseEntity<OrderdetailEntity>> orderdetail(@QueryMap Map<String,String> map);

}
