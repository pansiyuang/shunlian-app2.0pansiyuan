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

import com.shunlian.app.bean.*;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.newchat.entity.ChatMemberEntity;
import com.shunlian.app.newchat.entity.HistoryEntity;
import com.shunlian.app.newchat.entity.ReplysetEntity;
import com.shunlian.app.newchat.entity.ServiceEntity;
import com.shunlian.app.newchat.entity.StoreMessageEntity;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.newchat.entity.SystemMessageEntity;
import com.shunlian.app.ui.new3_login.New3LoginEntity;
import com.shunlian.app.ui.new3_login.New3LoginInfoTipEntity;

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
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zhang on 2017/4/13 16 : 06.
 * 网络请求接口
 */

public interface ApiService {
    int SC_ACCEPTED = 202;
    int SC_BAD_GATEWAY = 502;
    int SC_BAD_REQUEST = 400;
    int SC_CONFLICT = 409;
    int SC_CONTINUE = 100;
    int SC_CREATED = 201;
    int SC_EXPECTATION_FAILED = 417;
    int SC_FAILED_DEPENDENCY = 424;
    int SC_FORBIDDEN = 403;
    int SC_GATEWAY_TIMEOUT = 504;
    int SC_GONE = 410;
    int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
    int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
    int SC_INSUFFICIENT_STORAGE = 507;
    int SC_INTERNAL_SERVER_ERROR = 500;
    int SC_LENGTH_REQUIRED = 411;
    int SC_LOCKED = 423;
    int SC_METHOD_FAILURE = 420;
    int SC_METHOD_NOT_ALLOWED = 405;
    int SC_MOVED_PERMANENTLY = 301;
    int SC_MOVED_TEMPORARILY = 302;
    int SC_MULTIPLE_CHOICES = 300;
    int SC_MULTI_STATUS = 207;
    int SC_NON_AUTHORITATIVE_INFORMATION = 203;
    int SC_NOT_ACCEPTABLE = 406;
    int SC_NOT_FOUND = 404;
    int SC_NOT_IMPLEMENTED = 501;
    int SC_NOT_MODIFIED = 304;
    int SC_NO_CONTENT = 204;
    int SC_OK = 200;
    int SC_PARTIAL_CONTENT = 206;
    int SC_PAYMENT_REQUIRED = 402;
    int SC_PRECONDITION_FAILED = 412;
    int SC_PROCESSING = 102;
    int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
    int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    int SC_REQUEST_TIMEOUT = 408;
    int SC_REQUEST_TOO_LONG = 413;
    int SC_REQUEST_URI_TOO_LONG = 414;
    int SC_RESET_CONTENT = 205;
    int SC_SEE_OTHER = 303;
    int SC_SERVICE_UNAVAILABLE = 503;
    int SC_SWITCHING_PROTOCOLS = 101;
    int SC_TEMPORARY_REDIRECT = 307;
    int SC_UNAUTHORIZED = 401;
    int SC_UNPROCESSABLE_ENTITY = 422;
    int SC_UNSUPPORTED_MEDIA_TYPE = 415;
    int SC_USE_PROXY = 305;

    @GET("user/getQrCard")
    Call<BaseEntity<GetQrCardEntity>> userGetQrCard(@QueryMap Map<String, String> map);

    @GET("member/checkin/respond")
    Call<BaseEntity<CheckInRespondEntity>> checkinRespond(@QueryMap Map<String, String> map);

    @GET("member/checkin/status")
    Call<BaseEntity<CheckInStateEntity>> checkinStatus(@QueryMap Map<String, String> map);

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
    Call<BaseEntity<UploadPicEntity>> uploadPic1(@Query("ordersn") String ordersn, @Part MultipartBody.Part files);

    @Multipart
    @POST("My/Sign.step2.json")
    Call<BaseEntity<UploadPicEntity>> uploadPics(@Part List<MultipartBody.Part> files);

    /**
     * 2.0正式接口
     */


    /**
     *银联支付成功验证
     * @param url
     * @return
     */
    @GET
    Call<BaseEntity<EmptyEntity>> payVerify(@Url String url);

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
     * 检查短信验证码
     * @param requestBody
     * @return
     */
    @POST("member/common/checkMobileCode")
    Call<BaseEntity<EmptyEntity>> checkNew3LoginSmsCode(@Body RequestBody requestBody);

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
    @POST("member/common/sendSmsCode")
    Call<ResponseBody> sendSms(@Body RequestBody requestBody);

    /**
     * 登录
     *
     * @param requestBody
     * @return
     */
    @POST("member/login/index")
    Call<BaseEntity<LoginFinishEntity>> login(@Body RequestBody requestBody);

    /**
     * 验证验证码
     *
     * @param requestBody
     * @return
     */
    @POST("member/Common/checkCode")
    Call<BaseEntity<EmptyEntity>> checkCode(@Body RequestBody requestBody);

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
     * 新注册
     *
     * @param requestBody
     * @return
     */
    @POST("member/register/index")
    Call<BaseEntity<LoginFinishEntity>> new_register(@Body RequestBody requestBody);

    /**
     * 微信登录
     *
     * @param requestBody
     * @return
     */
    @POST("member/oauth/wechat")
    Call<BaseEntity<LoginFinishEntity>> wxLogin(@Body RequestBody requestBody);

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
    @POST("member/userinfo/findPwd")
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
     * 检验手机号是否注册
     * @param map
     * @return
     */
    @GET("member/register/checkMobileStatus")
    Call<BaseEntity<New3LoginEntity>> checkMobileNews(@QueryMap Map<String, String> map);

    /**
     * 商品详情
     *
     * @return
     */
    @GET("goods/detail")
    Call<BaseEntity<GoodsDeatilEntity>> goodsDetail(@QueryMap Map<String, String> map);

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
     * 您可能还想买
     */
    @GET("cart/getMemberProbablyLike")
    Call<BaseEntity<ProbabyLikeGoodsEntity>> getProbablyLikeList(@QueryMap Map<String, String> map);

    /**
     * 你可能想买
     */
    @GET("cart/wanthotgoods")
    Call<BaseEntity<RecommendEntity>> getWantHotGoodsList(@QueryMap Map<String, String> map);

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
     * 新人专享
     * @param body
     * @return
     */
    @POST("newexclusive/buy")
    Call<BaseEntity<ConfirmOrderEntity>> newexclusive(@Body RequestBody body);

    /**
     * 修改购物车
     *
     * @param body
     * @return
     */
    @POST("cart/edit")
    Call<BaseEntity<ShoppingCarEntity>> carEdit(@Body RequestBody body);

    /**
     * 购物车多选
     *
     * @param body
     * @return
     */
    @POST("cart/checkcartgoods")
    Call<BaseEntity<ShoppingCarEntity>> checkCartGoods(@Body RequestBody body);

    /**
     * 优惠券领取
     *
     * @param body
     * @return
     */
    @POST("voucher/getVoucher")
    Call<BaseEntity<GoodsDeatilEntity.Voucher>> getVoucher(@Body RequestBody body);


    /**
     * 购物车商品转入收藏夹
     *
     * @param body
     * @return
     */
    @POST("cart/removetofav")
    Call<BaseEntity<ShoppingCarEntity>> removetofav(@Body RequestBody body);

    /**
     * 购物车删除商品
     *
     * @param body
     * @return
     */
    @POST("cart/remove")
    Call<BaseEntity<ShoppingCarEntity>> cartRemove(@Body RequestBody body);


    /**
     * 购物车店铺全选删除
     *
     * @param body
     * @return
     */
    @POST("cart/multyremove")
    Call<BaseEntity<ShoppingCarEntity>> multyremove(@Body RequestBody body);

    /**
     * 购物车进入确认订单页
     *
     * @param body
     * @return
     */
    @POST("order/confirm")
    Call<BaseEntity<ConfirmOrderEntity>> orderConfirm(@Body RequestBody body);

    /**
     * 收藏商品
     *
     * @param body
     * @return
     */
    @POST("member/goodsfavorite/add")
    Call<BaseEntity<CommonEntity>> goodsfavorite(@Body RequestBody body);

    /**
     * 移除收藏
     *
     * @param body
     * @return
     */
    @POST("member/goodsfavorite/remove")
    Call<BaseEntity<CommonEntity>> goodsfavoriteRemove(@Body RequestBody body);

    /**
     * 评价列表
     *
     * @param map
     * @return
     */
    @GET("comment/list")
    Call<BaseEntity<CommentListEntity>> commentList(@QueryMap Map<String, String> map);

    /**
     * 省市区数据
     *
     * @param map
     * @return
     */
    @GET("district/all")
    Call<BaseEntity<DistrictAllEntity>> districtAll(@QueryMap Map<String, String> map);

    /**
     * 根据经纬度获取省市区
     *
     * @param body
     * @return
     */
    @POST("district/getlocation")
    Call<BaseEntity<DistrictGetlocationEntity>> districtGetlocation(@Body RequestBody body);

    /**
     * 添加收货地址
     *
     * @param body
     * @return
     */
    @POST("member/address/add")
    Call<BaseEntity<EmptyEntity>> addressAdd(@Body RequestBody body);

    /**
     * 编辑收货地址
     *
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
    @POST("member/address/all")
    Call<BaseEntity<AddressDataEntity>> allAddress(@Body RequestBody body);

    /**
     * 删除收货地址
     *
     * @param body
     * @return
     */
    @POST("member/address/remove")
    Call<BaseEntity<EmptyEntity>> delAddress(@Body RequestBody body);

    /**
     * 凑单界面商店类目
     *
     * @param body
     * @return
     */
    @POST("cart/getjoingoodsstorecates")
    Call<BaseEntity<CateEntity>> megerGoodsCates(@Body RequestBody body);

    /**
     * 凑单
     *
     * @param body
     * @return
     */
    @POST("cart/joingoods")
    Call<BaseEntity<JoinGoodsEntity>> getRecommmendGoods(@Body RequestBody body);

    /**
     * 获取商品sku信息
     *
     * @param body
     * @return
     */
    @POST("goods/getgoodssku")
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
     *
     * @param map
     * @return
     */
    @GET("member/comment/list")
    Call<BaseEntity<MyCommentListEntity>> myCommentList(@QueryMap Map<String, String> map);

    /**
     * 查看营业执照
     *
     * @param body
     * @return
     */
    @POST("store/businessLicense")
    Call<BaseEntity<StoreLicenseEntity>> storeLicense(@Body RequestBody body);



    /**
     * 上传图片 单张图片+多个参数
     */
    @Multipart
    @POST("uploads/chatupload")
    Call<BaseEntity<UploadPicEntity>> chatupload(@Part MultipartBody.Part[] parts,@QueryMap Map<String, String> maps);

    /**
     * 上传图片 单张图片+多个参数
     */
    @Multipart
    @POST("uploads/uploadotherimage")
    Call<BaseEntity<UploadPicEntity>> uploadPic(@Part MultipartBody.Part[] parts,@QueryMap Map<String, String> maps);


    /**
     * 上传图片 多张图片+多个参数
     */
    @Multipart
    @POST("uploads/uploadotherimage")
    Call<BaseEntity<UploadPicEntity>> uploadPic(@Part() List<MultipartBody.Part> parts, @QueryMap Map<String, String> maps);

    /**
     * 新增评价
     *
     * @param body
     * @return
     */
    @POST("member/comment/add")
    Call<BaseEntity<EmptyEntity>> addComment(@Body RequestBody body);

    /**
     * 批量追加评价
     *
     * @param body
     * @return
     */
    @POST("member/comment/batch_append")
    Call<BaseEntity<EmptyEntity>> appendComment(@Body RequestBody body);

    /**
     * 修好好评
     *
     * @param body
     * @return
     */
    @POST("member/comment/change")
    Call<BaseEntity<EmptyEntity>> changeComment(@Body RequestBody body);

    /**
     * 打开评价页面
     *
     * @param body
     * @return
     */
    @POST("member/comment/getorderinfo")
    Call<BaseEntity<CreatCommentEntity>> getOrderInfo(@Body RequestBody body);

    /**
     * 订单物流详情
     *
     * @return
     */
    @GET("personalcenter/traces")
    Call<BaseEntity<OrderLogisticsEntity>> orderLogistics(@QueryMap Map<String, String> map);

    /**
     * 订单搜索历史
     *
     * @return
     */
    @GET("personalcenter/searchhistory")
    Call<BaseEntity<TagEntity>> searchHistory(@QueryMap Map<String, String> map);

    /**
     * 清空历史搜索记录
     *
     * @return
     */
    @GET("personalcenter/delhistory")
    Call<BaseEntity<EmptyEntity>> delHistory(@QueryMap Map<String, String> map);

    /**
     * 待评价和待追评混合列表
     *
     * @return
     */
    @GET("member/comment/mixed_list")
    Call<BaseEntity<CommentSuccessEntity>> mixed_list(@QueryMap Map<String, String> map);

    /**
     * 我的订单列表
     *
     * @param map
     * @return
     */
    @GET("personalcenter/orderlist")
    Call<BaseEntity<MyOrderEntity>> orderList(@QueryMap Map<String, String> map);

    /**
     * 礼包订单详情
     *
     * @param map
     * @return
     */
    @GET("productorder/plusorderdetail")
    Call<BaseEntity<OrderdetailEntity>> plusorderdetail(@QueryMap Map<String, String> map);


    /**
     * 订单详情
     *
     * @param map
     * @return
     */
    @GET("personalcenter/orderdetail")
    Call<BaseEntity<OrderdetailEntity>> orderdetail(@QueryMap Map<String, String> map);

    /**
     * 取消订单
     *
     * @param body
     * @return
     */
    @POST("personalcenter/cancelorder")
    Call<BaseEntity<CommonEntity>> cancleOrder(@Body RequestBody body);

    /**
     * 提醒发货
     *
     * @param body
     * @return
     */
    @POST("personalcenter/remindseller")
    Call<BaseEntity<CommonEntity>> remindseller(@Body RequestBody body);

    /**
     * 延长发货
     *
     * @param body
     * @return
     */
    @POST("personalcenter/postpone")
    Call<BaseEntity<CommonEntity>> postpone(@Body RequestBody body);

    /**
     * 刷新订单
     *
     * @param map
     * @return
     */
    @GET("personalcenter/partorderlist")
    Call<BaseEntity<MyOrderEntity.Orders>> refreshOrder(@QueryMap Map<String, String> map);

    /**
     * 确认收货
     */
    @POST("personalcenter/confirmreceipt")
    Call<BaseEntity<CommonEntity>> confirmreceipt(@Body RequestBody body);

    /**
     * 换货确认收货
     */
    @POST("member/refund/confirmReceive")
    Call<BaseEntity<CommonEntity>> confirmReceive(@Body RequestBody body);

    /**
     * 撤销售后
     */
    @POST("member/refund/cancleapply")
    Call<BaseEntity<CommonEntity>> cancleapply(@Body RequestBody body);

    /**
     * 获取支付列表
     *
     * @param map
     * @return
     */
    @GET("order/getavailablepaymethod")
    Call<BaseEntity<PayListEntity>> methodlist(@QueryMap Map<String, String> map);

    /**
     * 商品详情评价点赞
     *
     * @param body
     * @return
     */
    @POST("comment/praise")
    Call<BaseEntity<CommonEntity>> commentPraise(@Body RequestBody body);

    /**
     * 套餐详情
     *
     * @param body
     * @return
     */
    @POST("goods/getcombodetail")
    Call<BaseEntity<ComboDetailEntity>> getcombodetail(@Body RequestBody body);

    /**
     * 购买套餐
     *
     * @param body
     * @return
     */
    @POST("order/buycombo")
    Call<BaseEntity<ConfirmOrderEntity>> buycombo(@Body RequestBody body);

    /**
     * 选择服务类型
     *
     * @param body
     * @return
     */
    @POST("member/refund/getrefundinfo")
    Call<BaseEntity<RefundDetailEntity.RefundDetail.Edit>> getrefundinfo(@Body RequestBody body);

    /**
     * 售后申请列表
     *
     * @param map
     * @return
     */
    @GET("member/refund/applyList")
    Call<BaseEntity<RefundListEntity>> refundList(@QueryMap Map<String, String> map);

    /**
     * 获取用户可选原因列表
     *
     * @return
     */
    @GET("member/refund/getrefundreason")
    Call<BaseEntity<CommonEntity>> getRefundReason(@QueryMap Map<String, String> map);

    /**
     * 申请退款
     *
     * @param body
     * @return
     */
    @POST("member/refund/applyRefund")
    Call<BaseEntity<RefundDetailEntity.RefundDetail>> applyRefund(@Body RequestBody body);

    /**
     * 售后申请详情
     *
     * @param map
     * @return
     */
    @GET("member/refund/applyDetail")
    Call<BaseEntity<RefundDetailEntity>> refundDetail(@QueryMap Map<String, String> map);

    /**
     * 协商历史
     *
     * @param map
     * @return
     */
    @GET("member/refund/applyLogList")
    Call<BaseEntity<ConsultHistoryEntity>> refundHistory(@QueryMap Map<String, String> map);


    /**
     * 提交订单
     *
     * @param body
     * @return
     */
    @POST("order/checkout")
    Call<BaseEntity<PayOrderEntity>> orderCheckout(@Body RequestBody body);

    /**
     * 新人专享支付
     * @param body
     * @return
     */
    @POST("newexclusive/checkout")
    Call<BaseEntity<PayOrderEntity>> newexclusivePay(@Body RequestBody body);


    /**
     * 从订单列表去支付
     *
     * @param body
     * @return
     */
    @POST("order/payinorderlist")
    Call<BaseEntity<PayOrderEntity>> fromOrderListGoPay(@Body RequestBody body);

    /**
     * 退换货物流公司
     *
     * @param map
     * @return
     */
    @GET("member/refund/refundExpressList")
    Call<BaseEntity<LogisticsNameEntity>> refundExpressList(@QueryMap Map<String, String> map);

    /**
     * 商品搜索列表
     *
     * @param body
     * @return
     */
    @POST("goods/search")
    Call<BaseEntity<SearchGoodsEntity>> getSearchGoods(@Body RequestBody body);

    /**
     * 筛选条件
     *
     * @param body
     * @return
     */
    @POST("goods/getListFilter")
    Call<BaseEntity<GetListFilterEntity>> getListFilter(@Body RequestBody body);

    /**
     * 提交物流信息
     *
     * @param body
     * @return
     */
    @POST("member/refund/saveShipInfo")
    Call<BaseEntity<EmptyEntity>> addLogisticsShipInfo(@Body RequestBody body);

    /**
     * 获取提交的物流信息
     *
     * @param map
     * @return
     */
    @GET("member/refund/getShipInfo")
    Call<BaseEntity<SubmitLogisticsInfoEntity>> getLogisticsShipInfo(@QueryMap Map<String, String> map);

    /**
     * 分类所有数据
     *
     * @return
     */
    @GET("operatecategory/all")
    Call<BaseEntity<SortFragEntity>> categoryAll(@QueryMap Map<String, String> map);

    /**
     * 排行榜
     *
     * @return
     */
    @GET("goods/top")
    Call<BaseEntity<RankingListEntity>> rankingList(@QueryMap Map<String, String> map);

    /**
     * 优品销量排行榜
     *
     * @return
     */
    @GET("plus/index/salesRanking")
    Call<BaseEntity<WeekSaleTopEntity>> salesRanking(@QueryMap Map<String, String> map);

    /**
     * 申请平台介入
     *
     * @param body
     * @return
     */
    @POST("member/refund/callplat")
    Call<BaseEntity<EmptyEntity>> callPlat(@Body RequestBody body);

    /**
     * 热搜关键字与搜索历史
     *
     * @return
     */
    @GET("goods/hotSearch")
    Call<BaseEntity<HotSearchEntity>> hotSearch(@QueryMap Map<String, String> map);

    /**
     * 搜索关键字提示
     *
     * @return
     */
    @POST("goods/keywordSuggest")
    Call<BaseEntity<CommonEntity>> keywordSuggest(@Body RequestBody body);

    /**
     * 清空搜索历史
     *
     * @return
     */
    @POST("goods/clearSearchHistory")
    Call<BaseEntity<EmptyEntity>> clearSearchHistory(@QueryMap Map<String, String> map);

    /**
     * 天天特惠列表
     *
     * @param map
     * @return
     */
    @GET("activity/ttList")
    Call<BaseEntity<ActivityListEntity>> activityList(@QueryMap Map<String, String> map);

    /**
     * 设置提醒功能
     *
     * @param body
     * @return
     */
    @POST("activity/remindMe")
    Call<BaseEntity<EmptyEntity>> actRemindMe(@Body RequestBody body);

    /**
     * 取消提醒
     *
     * @return
     */
    @POST("activity/cancelRemind")
    Call<BaseEntity<EmptyEntity>> cancleRemind(@Body RequestBody body);

    /**
     * 收藏商品
     *
     * @param body
     * @return
     */
    @POST("member/Myfavorite/favoriteGoods")
    Call<BaseEntity<CollectionGoodsEntity>> favoriteGoods(@Body RequestBody body);

    /**
     * 收藏的店铺
     *
     * @param body
     * @return
     */
    @POST("member/Myfavorite/favoriteShop")
    Call<BaseEntity<CollectionStoresEntity>> favoriteShop(@Body RequestBody body);


    /**
     * 获取足迹日历形式信息
     *
     * @return
     */
    @POST("member/footermark/getmarkCalendar")
    Call<BaseEntity<CommonEntity>> getmarkCalendar(@Body RequestBody body);

    /**
     * 我的足迹列表
     *
     * @return
     */
    @POST("member/footermark/getmarklist")
    Call<BaseEntity<FootprintEntity>> getmarklist(@Body RequestBody body);

    /**
     * 收藏商品搜索
     *
     * @param body
     * @return
     */
    @POST("member/Myfavorite/search")
    Call<BaseEntity<CollectionGoodsEntity>> collectionGoodsSearch(@Body RequestBody body);

    /**
     * 收藏店铺搜索
     *
     * @param body
     * @return
     */
    @POST("member/Myfavorite/search")
    Call<BaseEntity<CollectionStoresEntity>> collectionStoreSearch(@Body RequestBody body);

    /**
     * 批量删除关注的商店
     *
     * @param map
     * @return
     */
    @GET("member/Myfavorite/removeFavoShop")
    Call<BaseEntity<EmptyEntity>> removeFavoShop(@QueryMap Map<String, String> map);

    /**
     * 首页
     *
     * @param map
     * @return
     */
    @GET("channel/index")
    Call<BaseEntity<MainPageEntity>> firstPage(@QueryMap Map<String, String> map);

    /**
     * 个人中心首页
     *
     * @param map
     * @return
     */
    @GET("personalcenter/home")
    Call<BaseEntity<PersonalcenterEntity>> personalcenter(@QueryMap Map<String, String> map);

    /**
     * 签到领金蛋
     *
     * @param map
     * @return
     */
    @GET("task/signForGold")
    Call<BaseEntity<SignEggEntity>> signEgg(@QueryMap Map<String, String> map);

    /**
     * 个人金蛋明细
     *
     * @param map
     * @return
     */
    @GET("task/goldEggDetail")
    Call<BaseEntity<EggDetailEntity>> eggDetail(@QueryMap Map<String, String> map);

    /**
     * 个人金蛋明细2
     *
     * @param map
     * @return
     */
    @GET("task/goldEggDetail2")
    Call<BaseEntity<NewEggDetailEntity>> eggDetail2(@QueryMap Map<String, String> map);

    /**
     * 限时提醒
     *
     * @param map
     * @return
     */
    @GET("task/setRemind")
    Call<BaseEntity<EmptyEntity>> setRemind(@QueryMap Map<String, String> map);

    /**
     * 足迹列表足迹批量删除
     *
     * @param map
     * @return
     */
    @GET("member/footermark/deleteBatch")
    Call<BaseEntity<CommonEntity>> deleteBatch(@QueryMap Map<String, String> map);

    /**
     * 可添加商品列表
     *
     * @param map
     * @return
     */
    @GET("member/Personalshop/validGoods")
    Call<BaseEntity<AddGoodsEntity>> validGoods(@QueryMap Map<String, String> map);

    /**
     * 可添加的商品数量
     *
     * @param map
     * @return
     */
    @GET("member/Personalshop/fairishNums")
    Call<BaseEntity<CommonEntity>> fairishNums(@QueryMap Map<String, String> map);

    /**
     * 小店页面
     *
     * @param map
     * @return
     */
    @GET("member/Personalshop/personShop")
    Call<BaseEntity<PersonShopEntity>> getPersonShop(@QueryMap Map<String, String> map);

    /**
     * 添加商品
     *
     * @param body
     * @return
     */
    @POST("member/Personalshop/addGoods")
    Call<BaseEntity<CommonEntity>> addGoods(@Body RequestBody body);

    /**
     * 删除商品
     *
     * @param body
     * @return
     */
    @POST("member/Personalshop/delGoods")
    Call<BaseEntity<CommonEntity>> delGoods(@Body RequestBody body);

    /**
     * 所有品牌列表
     *
     * @param map
     * @return
     */
    @GET("goods/brandlist")
    Call<BaseEntity<GetListFilterEntity>> brandlist(@QueryMap Map<String, String> map);

    /**
     * 小店展示页面
     *
     * @param map
     * @return
     */
    @GET("myshop/personShop")
    Call<BaseEntity<PersonShopEntity>> personShop(@QueryMap Map<String, String> map);

    /**
     * 精选列表
     *
     * @param map
     * @return
     */
    @GET("discovery/nice/home")
    Call<BaseEntity<ArticleEntity>> niceList(@QueryMap Map<String, String> map);

    /**
     * 精选文章点赞
     *
     * @param map
     * @return
     */
    @GET("discovery/user/like")
    Call<BaseEntity<CommonEntity>> userLike(@QueryMap Map<String, String> map);

    /**
     * 精选文章点赞
     *
     * @param map
     * @return
     */
    @GET("discovery/user/unlike ")
    Call<BaseEntity<CommonEntity>> userUnLike(@QueryMap Map<String, String> map);

    /**
     * 发现评论列表
     *
     * @param map
     * @return
     */
    @GET("discovery/comment/list")
    Call<BaseEntity<FindCommentListEntity>> findcommentList(@QueryMap Map<String, String> map);

    /**
     * 发布评论
     *
     * @return
     */
    @POST("discovery/comment/save")
    Call<BaseEntity<FindCommentListEntity.ItemComment>> sendComment(@Body RequestBody body);

    /**
     * 推荐关注
     *
     * @param map
     * @return
     */
    @GET("discovery/focus/recommendStore")
    Call<BaseEntity<FindSelectShopEntity>> recommendFollow(@QueryMap Map<String, String> map);

    /**
     * 发现评论详情
     *
     * @param map
     * @return
     */
    @GET("discovery/comment/info")
    Call<BaseEntity<CommentDetailEntity>> commentDetail(@QueryMap Map<String, String> map);

    /**
     * 发现关注
     *
     * @param map
     * @return
     */
    @GET("discovery/focus/home")
    Call<BaseEntity<GuanzhuEntity>> foucsHome(@QueryMap Map<String, String> map);

    /**
     * 发现点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/comment/like")
    Call<BaseEntity<CommonEntity>> pointFabulous(@Body RequestBody body);

    /**
     * 删除评论
     *
     * @param body
     * @return
     */
    @POST("discovery/comment/delete")
    Call<BaseEntity<FindCommentListEntity.ItemComment>> delComment(@Body RequestBody body);

    /**
     * 白名单会员撤回已审核评论
     *
     * @param body
     * @return
     */
    @POST("discovery/comment/retract")
    Call<BaseEntity<CommonEntity>> retractComment(@Body RequestBody body);

    /**
     * 导航信息
     *
     * @param map
     * @return
     */
    @GET("discovery/nice/nav")
    Call<BaseEntity<DiscoveryNavEntity>> discoveryNav(@QueryMap Map<String, String> map);

    /**
     * 换一换猜你喜欢
     *
     * @param map
     * @return
     */
    @GET("discovery/nice/otherTopics")
    Call<BaseEntity<ArticleEntity>> othersTopics(@QueryMap Map<String, String> map);

    /**
     * 心得列表
     *
     * @param map
     * @return
     */
    @GET("discovery/experience/list")
    Call<BaseEntity<ExperienceEntity>> experienceList(@QueryMap Map<String, String> map);

    /**
     * 心得点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/experience/praise")
    Call<BaseEntity<CommonEntity>> praiseExperience(@Body RequestBody body);

    /**
     * 文章搜索接口
     *
     * @param body
     * @return
     */
    @POST("discovery/nice/search")
    Call<BaseEntity<ArticleEntity>> searchArticle(@Body RequestBody body);

    /**
     * 文章详情
     *
     * @param map
     * @return
     */
    @GET("discovery/nice/detailForApp")
    Call<BaseEntity<ArticleDetailEntity>> niceDetail(@QueryMap Map<String, String> map);

    /**
     * 标签详情
     *
     * @param map
     * @return
     */
    @GET("discovery/nice/tagDetail")
    Call<BaseEntity<ArticleEntity>> tagDetail(@QueryMap Map<String, String> map);

    /**
     * 素材前台列表
     *
     * @param map
     * @return
     */
    @GET("discovery/material/list")
    Call<BaseEntity<DiscoveryMaterialEntity>> discoveryMaterial(@QueryMap Map<String, String> map);

    /**
     * 素材点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/material/praise")
    Call<BaseEntity<EmptyEntity>> discoveryPraise(@Body RequestBody body);

    /**
     * 前台话题列表
     *
     * @param map
     * @return
     */
    @GET("discovery/circle/list")
    Call<BaseEntity<DiscoveryCircleEntity>> discoveryCircle(@QueryMap Map<String, String> map);

    /**
     * 前台话题列表
     *
     * @param map
     * @return
     */
    @GET("discovery/circle/InvList")
    Call<BaseEntity<DiscoveryTieziEntity>> discoveryList(@QueryMap Map<String, String> map);

    /**
     * 前台帖子评论列表
     *
     * @param map
     * @return
     */
    @GET("discovery/circle/commentList")
    Call<BaseEntity<DiscoveryCommentListEntity>> discoveryCommentList(@QueryMap Map<String, String> map);

    /**
     * 发表心得
     *
     * @param body
     * @return
     */
    @POST("discovery/experience/create")
    Call<BaseEntity<EmptyEntity>> createExperience(@Body RequestBody body);

    /**
     * 发表帖子
     *
     * @param body
     * @return
     */
    @POST("discovery/circle/addInv")
    Call<BaseEntity<EmptyEntity>> createCircle(@Body RequestBody body);

    /**
     * 话题点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/circle/topicLike")
    Call<BaseEntity<EmptyEntity>> circleTopicLike(@Body RequestBody body);

    /**
     * 帖子点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/circle/InvLike")
    Call<BaseEntity<CommonEntity>> circleInvLike(@Body RequestBody body);

    /**
     * 帖子评论点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/circle/CommentLike")
    Call<BaseEntity<EmptyEntity>> circleCommentLike(@Body RequestBody body);

    /**
     * 心得详情
     *
     * @param map
     * @return
     */
    @GET("discovery/experience/commentList")
    Call<BaseEntity<ExchangDetailEntity>> experienceDetail(@QueryMap Map<String, String> map);

    /**
     * 发表帖子评论
     *
     * @param body
     * @return
     */
    @POST("discovery/circle/addComment")
    Call<BaseEntity<CircleAddCommentEntity>> circleAddComment(@Body RequestBody body);


    /**
     * 心得评论点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/experience/commentPraise")
    Call<BaseEntity<CommonEntity>> comment_Praise(@Body RequestBody body);

    /**
     * 发布心得评论
     *
     * @param body
     * @return
     */
    @POST("discovery/experience/createComment")
    Call<BaseEntity<UseCommentEntity>> createComment(@Body RequestBody body);

    /**
     * 心得评论删除
     *
     * @return
     */
    @POST("discovery/experience/deleteComment")
    Call<BaseEntity<EmptyEntity>> deleteComment(@Body RequestBody body);

    /**
     * 心得评论详情
     *
     * @param map
     * @return
     */
    @GET("discovery/experience/commentInfo")
    Call<BaseEntity<CommentDetailEntity>> experienceCommentDetail(@QueryMap Map<String, String> map);

    /**
     * 收藏文章列表
     *
     * @return
     */
    @POST("member/Myfavorite/favoriteArticles")
    Call<BaseEntity<ArticleEntity>> favoriteArticles(@Body RequestBody body);


    /**
     * 精选文章收藏
     *
     * @return
     */
    @GET("discovery/user/favorite")
    Call<BaseEntity<EmptyEntity>> favoriteArticle(@QueryMap Map<String, String> map);

    /**
     * 精选文章取消收藏
     *
     * @return
     */
    @GET("discovery/user/unFavorite")
    Call<BaseEntity<EmptyEntity>> unFavoriteArticle(@QueryMap Map<String, String> map);

    /**
     * 系统消息
     *
     * @param map
     * @return
     */
    @GET("message/sysmessage")
    Call<BaseEntity<SystemMsgEntity>> sysmessage(@QueryMap Map<String, String> map);

    /**
     * 获取系统消息
     *
     * @return
     */
    @GET("message/sys")
    Call<BaseEntity<SystemMessageEntity>> getSystemMessage(@QueryMap Map<String, String> map);

    /**
     * 获取小店消息
     *
     * @return
     */
    @GET("message/store")
    Call<BaseEntity<StoreMessageEntity>> getStoremMessage(@QueryMap Map<String, String> map);

    /**
     * 获取店铺会员消息列表
     *
     * @return
     */
    @GET("message/vipmsg")
    Call<BaseEntity<StoreMsgEntity>> getVipMessage(@QueryMap Map<String, String> map);

    /**
     * 获取店铺会员消息列表
     *
     * @return
     */
    @GET("message/ordermsg")
    Call<BaseEntity<StoreMsgEntity>> getOrderMessage(@QueryMap Map<String, String> map);

    /**
     * 获取消息列表
     *
     * @return
     */
    @GET("chat/message/list")
    Call<BaseEntity<ChatMemberEntity>> getMessageList(@QueryMap Map<String, String> map);

    /**
     * 消息统计
     *
     * @return
     */
    @GET("message/allcount")
    Call<BaseEntity<AllMessageCountEntity>> messageAllCount(@QueryMap Map<String, String> map);

    /**
     * 销售数据
     */
    @GET("member/salesdata/generalsales")
    Call<BaseEntity<SaleDataEntity>> salesdata(@QueryMap Map<String, String> map);

    /**
     * 销售数据折线图
     *
     * @param body
     * @return
     */
    @POST("member/salesdata/{name}")
    Call<BaseEntity<SalesChartEntity>> salesChart(@Path("name") String path_name, @Body RequestBody body);

    /**
     * 销售详情 和奖励明细 myprofit/rewarddetail   salesdata/salesDetail
     *
     * @param body
     * @return
     */
    @POST("member/{path1}/{path2}")
    Call<BaseEntity<SaleDetailEntity>> salesDetail(@Path("path1") String path_name1,
                                                   @Path("path2") String path_name2,
                                                   @Body RequestBody body);

    /**
     * 我的收益
     *
     * @param body
     * @return
     */
    @POST("member/myprofit/generalprofit")
    Call<BaseEntity<MyProfitEntity>> generalprofit(@Body RequestBody body);

    /**
     * 收益折线走势图
     *
     * @param body
     * @return
     */
    @POST("member/myprofit/profitChart")
    Call<BaseEntity<SalesChartEntity>> profitChart(@Body RequestBody body);

    /**
     * 收益提现
     *
     * @param map
     * @return
     */
    @GET("member/myprofit/withdrawProfit")
    Call<BaseEntity<CommonEntity>> withdrawProfit(@QueryMap Map<String, String> map);

    /**
     * 预估收益  详情订单记录
     *
     * @param body
     * @return
     */
    @POST("member/myprofit/getEstimateDetail")
    Call<BaseEntity<DetailOrderRecordEntity>> getEstimateDetail(@Body RequestBody body);

    /**
     * 充值订单详情
     *
     * @param body
     * @return
     */
    @POST("virtual/orderDetail")
    Call<BaseEntity<PhoneOrderDetailEntity>> virtualOrderDetail(@Body RequestBody body);

     /**
     * 支付成功查看订单详情
     *
     * @param body
     * @return
     */
    @POST("virtual/orderPayResult")
    Call<BaseEntity<PhoneOrderDetailEntity>> orderPayResult(@Body RequestBody body);


    /**
     * 充值记录
     *
     * @param body
     * @return
     */
    @POST("virtual/rechargelist")
    Call<BaseEntity<PhoneRecordEntity>> rechargelist(@Body RequestBody body);

    /**
     * 领取月奖励和周奖励
     *
     * @param map
     * @return
     */
    @GET("member/myprofit/receiveReward")
    Call<BaseEntity<CommonEntity>> receiveReward(@QueryMap Map<String, String> map);

    /**
     * 帮助首页
     *
     * @return
     */
    @GET("helpcenter/index")
    Call<BaseEntity<HelpcenterIndexEntity>> helpcenterIndex(@QueryMap Map<String, String> map);

    /**
     * 问题分类
     *
     * @return
     */
    @GET("helpcenter/questionCate")
    Call<BaseEntity<HelpcenterQuestionEntity>> helpcenterQuestionCate(@QueryMap Map<String, String> map);

    /**
     * 根据二级分类获取问题列表
     *
     * @return
     */
    @GET("helpcenter/question")
    Call<BaseEntity<HelpcenterQuestionEntity>> helpcenterQuestion(@QueryMap Map<String, String> map);

    /**
     * 解决方案
     *
     * @return
     */
    @GET("helpcenter/solution")
    Call<BaseEntity<HelpcenterSolutionEntity>> helpcenterSolution(@QueryMap Map<String, String> map);


    /**
     * 客服电话
     *
     * @return
     */
    @GET("chat/chat/getAdminInfo")
    Call<BaseEntity<CommonEntity>> getAdminInfo(@QueryMap Map<String, String> map);

    /**
     * 新手课堂
     *
     * @return
     */
    @GET("helpcenter/classes")
    Call<BaseEntity<HelpClassEntity>> helpcenterClasses(@QueryMap Map<String, String> map);

    /**
     * 帮助中心关键词搜索
     *
     * @return
     */
    @GET("helpcenter/search")
    Call<BaseEntity<HelpSearchEntity>> helpcenterSearch(@QueryMap Map<String, String> map);

    /**
     * 意见反馈
     *
     * @return
     */
    @POST("helpcenter/feedback")
    Call<BaseEntity<EmptyEntity>> helpcenterFeedback(@Body RequestBody body);

    /**
     * 是否解决
     *
     * @return
     */
    @POST("helpcenter/solve")
    Call<BaseEntity<EmptyEntity>> helpcenterSolve(@Body RequestBody body);

    /**
     * 余额明细
     *
     * @return
     */
    @POST("balance/transactionList")
    Call<BaseEntity<BalanceDetailEntity>> balanceTransactionList(@Body RequestBody body);

    /**
     * 我的收益提现明细
     *
     * @return
     */
    @POST("member/myprofit/withdrawList")
    Call<BaseEntity<WithdrawListEntity>> withdrawList(@Body RequestBody body);

    /**
     * 我的收益提现详情
     *
     * @return
     */
    @POST("member/myprofit/withdrawLogDetail")
    Call<BaseEntity<AmountDetailEntity>> withdrawLogDetail(@Body RequestBody body);

    /**
     * 提现到支付宝
     *
     * @return
     */
    @POST("balance/withdraw")
    Call<BaseEntity<CommonEntity>> balanceWithdraw(@Body RequestBody body);


    /**
     * 验证短信验证码
     *
     * @return
     */
    @POST("balance/checkCode")
    Call<BaseEntity<CommonEntity>> balanceCheckCode(@Body RequestBody body);

    /**
     * 验证用户支付密码规则有效性
     *
     * @return
     */
    @POST("balance/checkPayPasswordRuleValid")
    Call<BaseEntity<EmptyEntity>> checkPayPasswordRuleValid(@Body RequestBody body);

    /**
     * 第一次设置支付密码
     *
     * @return
     */
    @POST("balance/setPayPassword")
    Call<BaseEntity<EmptyEntity>> balanceSetPayPassword(@Body RequestBody body);

    /**
     * 修改支付密码
     *
     * @return
     */
    @POST("balance/changePayPassword")
    Call<BaseEntity<EmptyEntity>> balanceChangePayPassword(@Body RequestBody body);

    /**
     * 绑定支付宝账户
     *
     * @return
     */
    @POST("balance/bindAliPay")
    Call<BaseEntity<CommonEntity>> balanceBindAliPay(@Body RequestBody body);

    /**
     * 验证支付密码正确
     *
     * @return
     */
    @POST("balance/checkPayPassword")
    Call<BaseEntity<CommonEntity>> balanceCheckPayPassword(@Body RequestBody body);

    /**
     * 解除支付宝绑定
     *
     * @return
     */
    @POST("balance/unbindAliPay")
    Call<BaseEntity<EmptyEntity>> balanceUnbindAliPay(@Body RequestBody body);


    /**
     * 余额详情
     *
     * @return
     */
    @GET("balance/info")
    Call<BaseEntity<BalanceInfoEntity>> balanceInfo(@QueryMap Map<String, String> map);

    /**
     * 收益余额详情
     *
     * @return
     */
    @GET("member/myprofit/balanceDetail")
    Call<BaseEntity<BalanceInfoEntity>> balanceDetail(@QueryMap Map<String, String> map);

    /**
     * 余额明细详情
     *
     * @return
     */
    @GET("balance/transactionDetail")
    Call<BaseEntity<AmountDetailEntity>> amountDetails(@QueryMap Map<String, String> map);

    /**
     * 获取提现账户
     *
     * @return
     */
    @GET("balance/getWithdrawAccount")
    Call<BaseEntity<CommonEntity>> getWithdrawAccount(@QueryMap Map<String, String> map);

    /**
     * 发送短信验证码
     *
     * @return
     */
    @GET("balance/sendSmsCode")
    Call<BaseEntity<EmptyEntity>> balanceSendSmsCode(@QueryMap Map<String, String> map);

    /**
     * 获取实名信息
     *
     * @return
     */
    @GET("balance/getRealInfo")
    Call<BaseEntity<GetRealInfoEntity>> balanceGetRealInfo(@QueryMap Map<String, String> map);

    /**
     * 个人中心优惠券列表
     *
     * @param body
     * @return
     */
    @POST("voucher/all")
    Call<BaseEntity<CouponListEntity>> voucherList(@Body RequestBody body);

    /**
     * 用户画像标签
     *
     * @param body
     * @return
     */
    @POST("member/portrait/artTag")
    Call<BaseEntity<ArtTagEntity>> portraitArtTag(@Body RequestBody body);

    /**
     * 提交用户画像
     *
     * @param body
     * @return
     */
    @POST("member/portrait/addPortrait")
    Call<BaseEntity<EmptyEntity>> addPortrait(@Body RequestBody body);

    /**
     * 设置数据
     *
     * @param map
     * @return
     */
    @GET("member/accountSetting/myDatum")
    Call<BaseEntity<PersonalDataEntity>> personalData(@QueryMap Map<String, String> map);

    /**
     * 设置个人信息
     *
     * @param body
     * @return
     */
    @POST("member/accountSetting/setinfo")
    Call<BaseEntity<EmptyEntity>> setinfo(@Body RequestBody body);

    /**
     * 设置数据
     *
     * @param map
     * @return
     */
    @GET("member/accountSetting/gettaglist")
    Call<BaseEntity<PersonalDataEntity>> gettaglist(@QueryMap Map<String, String> map);

    /**
     * 获取手机号
     *
     * @param map
     * @return
     */
    @GET("personalcenter/getMobile")
    Call<BaseEntity<CommonEntity>> getMobile(@QueryMap Map<String, String> map);

    /**
     * 更换账号和密码
     *
     * @param body
     * @return
     */
    @POST("personalcenter/{path}")
    Call<BaseEntity<EmptyEntity>> userAndPwd(@Path("path") String path, @Body RequestBody body);

    /**
     * 验证短信验证码
     *
     * @param body
     * @return
     */
    @POST("personalcenter/checkCode")
    Call<BaseEntity<CommonEntity>> checkSmsCode(@Body RequestBody body);

    /**
     * 检验新手机
     *
     * @param body
     * @return
     */
    @POST("personalcenter/bindMobile")
    Call<BaseEntity<CommonEntity>> checkNewMobile(@Body RequestBody body);

    /**
     * 发送短信验证码
     *
     * @param body
     * @return
     */
    @POST("personalcenter/reSendSmsToNewMobile")
    Call<BaseEntity<CommonEntity>> sendSmsCodeToMobile(@Body RequestBody body);

    /**
     * 我要反馈
     *
     * @param body
     * @return
     */
    @POST("personalcenter/feedback")
    Call<BaseEntity<CommonEntity>> feedback(@Body RequestBody body);

    /**
     * 设置
     *
     * @param map
     * @return
     */
    @GET("personalcenter/setting")
    Call<BaseEntity<CommonEntity>> setting(@QueryMap Map<String, String> map);

    /**
     * 更新推送设置
     *
     * @param body
     * @return
     */
    @POST("personalcenter/updateSetting")
    Call<BaseEntity<EmptyEntity>> updatePushSet(@Body RequestBody body);


    /**
     * 客服商品列表
     *
     * @return
     */
    @GET("chat/goods/list")
    Call<BaseEntity<ChatGoodsEntity>> chatGoodsList(@QueryMap Map<String, String> map);

    /**
     * 获取商家客服聊天用户ID
     *
     * @return
     */
    @GET("chat/chat/getUserId")
    Call<BaseEntity<CommonEntity>> getUserId(@QueryMap Map<String, String> map);

    /**
     * 发现小红点
     *
     * @return
     */
    @GET("discovery/blogfront/getUnreadCount")
    Call<BaseEntity<CommonEntity>> getDiscoveryUnreadCount(@QueryMap Map<String, String> map);

    /**
     * 普通用户查看聊天记录
     *
     * @return
     */
    @GET("chat/chat/chatUserHistoryData")
    Call<BaseEntity<HistoryEntity>> chatUserHistoryData(@QueryMap Map<String, String> map);

    /**
     * 平台客服查看用户历史消息
     *
     * @return
     */
    @GET("chat/chat/platformChatUserHistoryData")
    Call<BaseEntity<HistoryEntity>> platformChatUserHistoryData(@QueryMap Map<String, String> map);

    /**
     * 商家客服查看用户历史消息
     *
     * @return
     */
    @GET("chat/chat/shopChatUserHistoryData")
    Call<BaseEntity<HistoryEntity>> shopChatUserHistoryData(@QueryMap Map<String, String> map);

    /**
     * 客服获取工作状态
     *
     * @return
     */
    @GET("chat/chat/getReception")
    Call<BaseEntity<CommonEntity>> getReception(@QueryMap Map<String, String> map);

    /**
     * 客服设置工作状态
     *
     * @return
     */
    @POST("chat/chat/setReception")
    Call<BaseEntity<CommonEntity>> setReception(@Body RequestBody body);

    /**
     * 客服获取正在聊天用户列表
     *
     * @return
     */
    @GET("chat/chat/getUserList")
    Call<BaseEntity<ChatMemberEntity>> getUserList(@QueryMap Map<String, String> map);

    /**
     * 转接客服列表
     *
     * @return
     */
    @GET("chat/chat/transferChatUserList")
    Call<BaseEntity<ServiceEntity>> getTransferChatUserList(@QueryMap Map<String, String> map);

    /**
     * 删除客服消息
     *
     * @return
     */
    @POST("chat/message/delete")
    Call<BaseEntity<CommonEntity>> deleteMessage(@Body RequestBody body);

    /**
     * 删除消息
     *
     * @return
     */
    @POST("message/del")
    Call<BaseEntity<CommonEntity>> messageDel(@Body RequestBody body);

    /**
     * 发现头条列表
     *
     * @return
     */
    @GET("message/head")
    Call<BaseEntity<StoreMsgEntity>> foundTopicList(@QueryMap Map<String, String> map);

    /**
     * 发现评论列表
     *
     * @return
     */
    @GET("message/comment")
    Call<BaseEntity<StoreMsgEntity>> foundCommentList(@QueryMap Map<String, String> map);


    /**
     * 首页-频道的菜单
     *
     * @return
     */
    @GET("channel/getMenu")
    Call<BaseEntity<GetMenuEntity>> channelGetMenu(@QueryMap Map<String, String> map);

    /**
     * 获取首页或频道数据
     *
     * @return
     */
    @GET("channel/getData")
    Call<BaseEntity<GetDataEntity>> channelGetData(@QueryMap Map<String, String> map);

    /**
     * 爱上新品列表
     *
     * @return
     */
    @GET("channel/channelCore")
    Call<BaseEntity<CoreNewEntity>> coreNew(@QueryMap Map<String, String> map);

    /**
     * 热销
     *
     * @return
     */
    @GET("channel/channelCore")
    Call<BaseEntity<CoreHotEntity>> coreHot(@QueryMap Map<String, String> map);

    /**
     * 品牌特卖列表
     *
     * @return
     */
    @GET("channel/channelCore")
    Call<BaseEntity<CorePingEntity>> corePing(@QueryMap Map<String, String> map);

    /**
     * 品质热推自动生成版
     *
     * @return
     */
    @GET("channel/hotPush")
    Call<BaseEntity<HotRdEntity>> hotPush(@QueryMap Map<String, String> map);

    /**
     * 平台券
     *
     * @return
     */
    @GET("channel/vouchercenterpl")
    Call<BaseEntity<VouchercenterplEntity>> vouchercenterpl(@QueryMap Map<String, String> map);

    /**
     * 领券中心
     *
     * @return
     */
    @GET("channel/vouchercenter")
    Call<BaseEntity<VouchercenterplEntity>> vouchercenter(@QueryMap Map<String, String> map);

    /**
     * 万用广告弹窗接口
     *
     * @return
     */
    @GET("adpush/commonad")
    Call<BaseEntity<AdEntity>> adpush(@QueryMap Map<String, String> map);


    /**
     * 品牌特卖详情
     *
     * @return
     */
    @GET("channel/branddetail")
    Call<BaseEntity<CorePingEntity>> branddetail(@QueryMap Map<String, String> map);

    /**
     * 热销商品分类
     *
     * @return
     */
    @GET("channel/hotGoodsCate")
    Call<BaseEntity<CoreHotEntity>> hotGoodsCate(@QueryMap Map<String, String> map);


    /**
     * 爱上新品分类上新
     *
     * @return
     */
    @GET("channel/newGoodsCate")
    Call<BaseEntity<CoreNewsEntity>> newGoodsCate(@QueryMap Map<String, String> map);


    /**
     * 小店销售周排行榜
     *
     * @return
     */
    @GET("top/weekSaleTop")
    Call<BaseEntity<WeekSaleTopEntity>> weekSaleTop(@QueryMap Map<String, String> map);

    /**
     * 猜你喜欢
     *
     * @return
     */
    @GET("order/payresult")
    Call<BaseEntity<ProbablyLikeEntity>> probablyLike(@QueryMap Map<String, String> map);

    /**
     * 礼包支付成功提示
     *
     * @return
     */
    @GET("productorder/payresult")
    Call<BaseEntity<ProbablyLikeEntity>> productorderPayresult(@QueryMap Map<String, String> map);

    /**
     * 猜你喜欢
     *
     * @param body
     * @return
     */
    @POST("goods/mayBeBuy")
    Call<BaseEntity<ProbablyLikeEntity>> mayBeBuy(@Body RequestBody body);

    /**
     * 解析分享密码
     *
     * @param body
     * @return
     */
    @POST("share/parseSecretWord")
    Call<BaseEntity<CommondEntity>> parseSecretWord(@Body RequestBody body);

    /**
     * plus入口
     *
     * @param body
     * @return
     */
    @POST("product/entryinfo")
    Call<BaseEntity<CommonEntity>> entryInfo(@Body RequestBody body);

    /**
     * 分享计数
     *
     * @param body
     * @return
     */
    @POST("share/add")
    Call<BaseEntity<CommonEntity>> shareAdd(@Body RequestBody body);


    /**
     * 闪屏广告
     *
     * @return
     */
    @GET("adpush/splashScreen")
    Call<BaseEntity<AdEntity>> splashScreen(@QueryMap Map<String, String> map);

    /**
     * 闪屏广告
     *
     * @return
     */
    @GET("Goods/getBubble")
    Call<BaseEntity<BubbleEntity>> getBubble(@QueryMap Map<String, String> map);

    /**
     * 验证新人
     *
     * @return
     */
    @GET("tasknewperson/isShowNewPersonPrize")
    Call<BaseEntity<CommonEntity>> isShowNewPersonPrize(@QueryMap Map<String, String> map);

    /**
     * 首次注册抽奖
     *
     * @return
     */
    @GET("tasknewperson/getPrizeByRegister")
    Call<BaseEntity<CommonEntity>> getPrizeByRegister(@QueryMap Map<String, String> map);

    /**
     * 弹窗广告
     *
     * @return
     */
    @GET("adpush/popup")
    Call<BaseEntity<AdEntity>> popup(@QueryMap Map<String, String> map);

    /**
     * 是否显示签到引流
     *
     * @return
     */
    @GET("task/isShowSign")
    Call<BaseEntity<ShowSignEntity>> isShowSign(@QueryMap Map<String, String> map);

    /**
     * 专题页数据
     *
     * @return
     */
    @GET("special/getdata")
    Call<BaseEntity<ShareEntity>> specialGetdata(@QueryMap Map<String, String> map);


    /**
     * 弹窗广告
     *
     * @return
     */
    @GET("adpush/updateappcheck")
    Call<BaseEntity<UpdateEntity>> updateappcheck(@QueryMap Map<String, String> map);

    /**
     * 课堂详情分享
     *
     * @return
     */
    @GET("helpcenter/classesshare")
    Call<BaseEntity<CommonEntity>> classesshare(@QueryMap Map<String, String> map);

    /**
     * 改变消息已读状态
     *
     * @param body
     * @return
     */
    @POST("message/read")
    Call<BaseEntity<EmptyEntity>> messageRead(@Body RequestBody body);

    /**
     * 优品列表
     *
     * @return
     */
    @GET("superiorspecial/getdata")
    Call<BaseEntity<SuperProductEntity>> superProductsList(@QueryMap Map<String, String> map);

    /**
     * 礼包列表
     *
     * @return
     */
    @GET("product/productlist")
    Call<BaseEntity<GifProductEntity>> getProductList(@QueryMap Map<String, String> map);

    /**
     * 礼包详情
     *
     * @return
     */
    @GET("product/productdetail")
    Call<BaseEntity<ProductDetailEntity>> getProductDetail(@QueryMap Map<String, String> map);

    /**
     * 处罚
     *
     * @return
     */
    @GET("message/punish")
    Call<BaseEntity<PunishEntity>> punish(@QueryMap Map<String, String> map);

    /**
     * plus确认订单
     *
     * @param body
     * @return
     */
    @POST("productorder/confirm")
    Call<BaseEntity<PLUSConfirmEntity>> plusConfirm(@Body RequestBody body);

    /**
     * 支付plus订单
     *
     * @param body
     * @return
     */
    @POST("productorder/checkout")
    Call<BaseEntity<PayOrderEntity>> submitPLUSOrder(@Body RequestBody body);

    /**
     * 礼包订单列表
     *
     * @return
     */
    @POST("productorder/plusorderlist")
    Call<BaseEntity<PlusOrderEntity>> getPlusOrderList(@Body RequestBody body);

    /**
     * plus信息
     *
     * @return
     */
    @GET("member/pluscenter/plusAchievement")
    Call<BaseEntity<PlusDataEntity>> getPlusData(@QueryMap Map<String, String> map);

    /**
     * 查看物流信息
     *
     * @return
     */
    @POST("personalcenter/getOppositeTraces")
    Call<BaseEntity<OrderLogisticsEntity>> getOppositeTraces(@Body RequestBody body);

    /**
     * 邀请记录
     *
     * @return
     */
    @POST("member/pluscenter/invitehistory")
    Call<BaseEntity<InvitationEntity>> inviteHistory(@Body RequestBody body);

    /**
     * 加入plus的人
     *
     * @return
     */
    @GET("product/getjoinlist")
    Call<BaseEntity<PlusMemberEntity>> getPlusMember(@QueryMap Map<String, String> map);

    /**
     * 扫二维码解析url
     * @return
     */
    @POST("share/parseUrlForApp")
    Call<BaseEntity<ScanCodeEntity>> scanCodeParseUrl(@Body RequestBody body);

    /**
     * 分享信息
     * @param map
     * @return
     */
    @GET("channel/share")
    Call<BaseEntity<ShareInfoParam>> shareInfo(@QueryMap Map<String, String> map);

    /**
     * 获取话费充值面额列表
     * @param body
     * @return
     */
    @POST("Virtual/getProductList")
    Call<BaseEntity<MoreCreditEntity>> getCreditProductList(@Body RequestBody body);

    /**
     * 手机充值
     * @param body
     * @return
     */
    @POST("Virtual/addOrder")
    Call<BaseEntity<PayOrderEntity>> phoneTopUp(@Body RequestBody body);

    /**
     * 客服快捷语列表
     *
     * @param map
     * @return
     */
    @GET("chat/chat/quickList")
    Call<BaseEntity<ReplysetEntity>> replysetList(@QueryMap Map<String, String> map);

    /**
     * 指定优惠券
     *
     * @param map
     * @return
     */
    @GET("voucher/getassignvoucher")
    Call<BaseEntity<VoucherEntity>> getAssignVoucher(@QueryMap Map<String, String> map);

    /**
     * 充值手机号列表
     * @param body
     * @return
     */
    @POST("virtual/listCard")
    Call<BaseEntity<CreditPhoneListEntity>> listCard(@Body RequestBody body);

    /**
     * 删除手机号
     * @param body
     * @return
     */
    @POST("virtual/deleteCard")
    Call<BaseEntity<EmptyEntity>> deleteCard(@Body RequestBody body);

    /**
     * 平台优惠券对应的可购买商品
     * @param map
     * @return
     */
    @GET("voucher/voucherRelatedGoodsList")
    Call<BaseEntity<StageVoucherGoodsListEntity>> stageVoucherGoodsList(@QueryMap Map<String, String> map);

    /**
     * 店铺，或者指定部分商品
     * @param map
     * @return
     */
    @GET("voucher/voucherRelatedStore")
    Call<BaseEntity<StageGoodsListEntity>> voucherRelatedStore(@QueryMap Map<String, String> map);

    /**
     * 绑定导购员id
     * @param body
     * @return
     */
    @POST("member/register/bindShareid")
    Call<BaseEntity<LoginFinishEntity>> bindShareid(@Body RequestBody body);

    /**
     * 模拟微信登录
     * @param map
     * @return
     */
    @GET("member/oauth/checkTest")
    Call<BaseEntity<WXLoginEntity>> checkTest(@QueryMap Map<String, String> map);

    /**
     *模拟微信登录
     * @param map
     * @return
     */
    @GET("member/oauth/checkTestV2")
    Call<BaseEntity<LoginFinishEntity>> checkTestV2(@QueryMap Map<String, String> map);

    /**
     * 导购员详情
     * @param map
     * @return
     */
    @GET("member/register/codeInfo")
    Call<BaseEntity<MemberCodeListEntity>> codeInfo(@QueryMap Map<String, String> map);
    /**
     * 我的导师
     * @param map
     * @return
     */
    @GET("member/follower/myTeacher")
    Call<BaseEntity<MemberTeacherEntity>> codeTeacherInfo(@QueryMap Map<String, String> map);
    /**
     * 设置永久不弹
     * @param map
     * @return
     */
    @GET("member/follower/neverPop")
    Call<BaseEntity<EmptyEntity>> neverPop(@QueryMap Map<String, String> map);
    /**
     * 我还想要
     * @param map
     * @return
     */
    @GET("goods/want")
    Call<BaseEntity<EmptyEntity>> goodsWant(@QueryMap Map<String, String> map);

    /**
     * 验证短信验证码
     * @param body
     * @return
     */
    @POST("member/common/checkMobileCode")
    Call<BaseEntity<EmptyEntity>> checkMobileCode(@Body RequestBody body);

    /**
     * 大转盘界面
     *
     * @param map
     * @return
     */
    @GET("turntable/turnTable")
    Call<BaseEntity<TurnTableEntity>> getTurnTable(@QueryMap Map<String, String> map);

    /**
     * 大转盘抽奖
     *
     * @param map
     * @return
     */
    @GET("turntable/luckDraw")
    Call<BaseEntity<LuckDrawEntity>> luckDraw(@QueryMap Map<String, String> map);

    /**
     * 进入大转盘进的弹窗
     *
     * @param map
     * @return
     */
    @GET("turntable/popup")
    Call<BaseEntity<TurnTablePopEntity>> turntablePopup(@QueryMap Map<String, String> map);

    /**
     * 添加奖品收货地址
     *
     * @param body
     * @return
     */
    @POST("turntable/addAddress")
    Call<BaseEntity<EmptyEntity>> turntableAddAddress(@Body RequestBody body);


    /**
     * 获取分享图
     *
     * @param map
     * @return
     */
    @GET("turntable/shareImg")
    Call<BaseEntity<CommonEntity>> turntableShareImg(@QueryMap Map<String, String> map);

    /**
     * 分享成功回调
     * @param body
     * @return
     */
    @POST("share/addOnOk")
    Call<BaseEntity<CommonEntity>> shareSuccessCall(@Body RequestBody body);

    /**
     * 浏览商品获得金蛋
     * @param body
     * @return
     */
    @POST("goods/getGold")
    Call<BaseEntity<CommonEntity>> getGold(@Body RequestBody body);

    /**
     * 任务首页
     * @param map
     * @return
     */
    @GET("task/home")
    Call<BaseEntity<TaskHomeEntity>> taskHome(@QueryMap Map<String,String> map);

    /**
     * 拆分百万金蛋
     * @param map
     * @return
     */
    @GET("task/everyDayGiveEgg")
    Call<BaseEntity<DayGiveEggEntity>> everyDayGiveEgg(@QueryMap Map<String,String> map);

    /**
     *任务列表
     * @param map
     * @return
     */
    @GET("task/taskList")
    Call<BaseEntity<TaskListEntity>> taskList(@QueryMap Map<String,String> map);

    /**
     * 限时领金蛋
     * @param map
     * @return
     */
    @GET("goldegglimit/getlimitgoldegg")
    Call<BaseEntity<TaskHomeEntity>> goldegglimit(@QueryMap Map<String,String> map);

    /**
     * 邀请码领金蛋
     * @param map
     * @return
     */
    @GET("tasknewperson/getGoldByCode")
    Call<BaseEntity<TaskHomeEntity>> getGoldByCode(@QueryMap Map<String,String> map);

    /**
     * 看视频领金蛋
     */
    @GET("tasknewperson/getGoldByWatchVideo")
    Call<BaseEntity<TaskHomeEntity>> getGoldByWatchVideo(@QueryMap Map<String,String> map);

    /**
     * 获取附近位置
     * @param body
     * @return
     */
    @POST("discovery/publish/getRelatedPlaces")
    Call<BaseEntity<NearAddrEntity>> getRelatedPlaces(@Body RequestBody body);

    /**
     * 获取话题
     * @param map
     * @return
     */
    @GET("discovery/blogfront/getActivitys")
    Call<BaseEntity<TopicEntity>>  getTopics(@QueryMap Map<String,String> map);

    /**
     * 发表博客
     * @return
     */
    @POST("discovery/publish/publish")
    Call<BaseEntity<CommonEntity>> pubishBlog(@Body RequestBody body);

    /**
     * 上传视频
     * @param parts
     * @return
     */
    @Multipart
    @POST("uploads/uploadVideo")
    Call<BaseEntity<CommonEntity>> uploadVideo(@Part MultipartBody.Part parts);

    /**
     *获取草稿
     * @return
     */
    @GET("discovery/publish/getdraft")
    Call<BaseEntity<BlogDraftEntity>> getDraft(@QueryMap Map<String,String> map);

    /**
     * 精选列表
     */
    @GET("discovery/blogfront/hotblogs")
    Call<BaseEntity<HotBlogsEntity>> hotblogs(@QueryMap Map<String, String> map);

    /**
     * 关注列表
     */
    @GET("discovery/blogfront/getmyfocusblogs")
    Call<BaseEntity<HotBlogsEntity>> focusblogs(@QueryMap Map<String, String> map);

    /**
     * 活动列表
     */
    @GET("discovery/blogfront/getActivitys")
    Call<BaseEntity<DiscoverActivityEntity>> getActivitys(@QueryMap Map<String, String> map);

    /**
     * 关注/取消关注
     */
    @GET("discovery/discoveryuser/focusUser")
    Call<BaseEntity<EmptyEntity>> focusUser(@QueryMap Map<String, String> map);

    /**
     * 文章点赞
     */
    @GET("discovery/discoveryuser/praise")
    Call<BaseEntity<EmptyEntity>> praiseBlog(@QueryMap Map<String, String> map);

    /**
     * 获取发表评论提示关键词
     */
    @GET("discovery/comment/getWord")
    Call<BaseEntity<CommonEntity>> getWordList(@QueryMap Map<String, String> map);

    /**
     * 周达人榜
     */
    @GET("discovery/blogfront/weekExportTopList")
    Call<BaseEntity<ExpertEntity>> weekExpertList(@QueryMap Map<String, String> map);

    /**
     * 精选达人榜
     */
    @GET("discovery/blogfront/hotExpertTopList")
    Call<BaseEntity<HotBlogsEntity>> hotExpertTopList(@QueryMap Map<String, String> map);

    /**
     * 收藏列表/自己发表的文章
     */
    @GET("discovery/discoveryuser/getblogs")
    Call<BaseEntity<HotBlogsEntity>> getblogs(@QueryMap Map<String, String> map);

    /**
     * 我的粉丝列表
     */
    @GET("discovery/discoveryuser/fansList")
    Call<BaseEntity<MemberEntity>> fansList(@QueryMap Map<String, String> map);

    /**
     * TA的粉丝列表
     */
    @GET("discovery/blogfront/fansList")
    Call<BaseEntity<MemberEntity>> taFansList(@QueryMap Map<String, String> map);

    /**
     * 我的关注列表
     */
    @GET("discovery/discoveryuser/focusList")
    Call<BaseEntity<MemberEntity>> focusList(@QueryMap Map<String, String> map);

    /**
     * TA的关注列表
     */
    @GET("discovery/blogfront/focusList")
    Call<BaseEntity<MemberEntity>> taFocusList(@QueryMap Map<String, String> map);

    /**
     * 活动详情
     */
    @GET("discovery/blogfront/getactivitydetail")
    Call<BaseEntity<HotBlogsEntity>> getActivityDetail(@QueryMap Map<String, String> map);

    /**
     * 点赞与分享
     */
    @GET("discovery/message/praisesharelist")
    Call<BaseEntity<ZanShareEntity>> getPraiseShareList(@QueryMap Map<String, String> map);

    /**
     * 关注消息
     */
    @GET("discovery/message/attention")
    Call<BaseEntity<AttentionMsgEntity>> getAttentionMsg(@QueryMap Map<String, String> map);

    /**
     * 下载消息
     */
    @GET("discovery/message/download")
    Call<BaseEntity<ZanShareEntity>> getDownloadMsg(@QueryMap Map<String, String> map);

    /**
     * 通知消息
     */
    @GET("discovery/message/notice")
    Call<BaseEntity<NoticeMsgEntity>> getNoticeMsg(@QueryMap Map<String, String> map);

    /**
     * 搜索关键词
     */
    @GET("discovery/discoveryuser/getHotSearch")
    Call<BaseEntity<TagEntity>> getHotSearch(@QueryMap Map<String, String> map);

    /**
     * 关注/取消关注
     */
    @GET("discovery/discoveryuser/downcount")
    Call<BaseEntity<EmptyEntity>> downCount(@QueryMap Map<String, String> map);

    /**
     * 搜索达人
     */
    @GET("discovery/blogfront/searchExpert")
    Call<BaseEntity<ExpertEntity>> searchExpert(@QueryMap Map<String, String> map);

    /**
     * 收藏文章
     */
    @GET("discovery/discoveryuser/addFavo")
    Call<BaseEntity<EmptyEntity>> addFavo(@QueryMap Map<String, String> map);

    /**
     * 删除文章
     */
    @GET("discovery/discoveryuser/removeBlog")
    Call<BaseEntity<EmptyEntity>> removeBlog(@QueryMap Map<String, String> map);

    /**
     * 未读消息统计
     */
    @GET("discovery/message/unreadcount")
    Call<BaseEntity<CommonEntity>> unreadcount(@QueryMap Map<String, String> map);

    /**
     * 下载文件
     */
     @Streaming
     @GET
     Call<ResponseBody> download(@Url String url);

    /**
     * 新人专享banner
     */
    @GET("newexclusive/adlist")
    Call<BaseEntity<AdUserEntity>> adlist(@QueryMap Map<String, String> map);

    /**
     * 新人专享商品
     * @return
     */
    @POST("newexclusive/goodslist")
    Call<BaseEntity<NewUserGoodsEntity>> usergoodslist(@QueryMap Map<String, String> map);

    /**
     * 新人专享添加购物车
     * @return
     */
    @POST("newexclusive/addcart")
    Call<BaseEntity<CateEntity>> newuseraddCart(@Body RequestBody body);

    /**
     * 新人专享购物车商品列表
     */
    @GET("newexclusive/cartlist")
    Call<BaseEntity<NewUserGoodsEntity>> cartlist(@QueryMap Map<String, String> map);
    /**
     * 新人专享删除购物车
     */
    @GET("newexclusive/deletecart")
    Call<BaseEntity<EmptyEntity>> deletecart(@QueryMap Map<String, String> map);
    /**
     * 购物车进入确认订单页
     *
     * @param body
     * @return
     */
    @POST("newexclusive/buy")
    Call<BaseEntity<ConfirmOrderEntity>> orderNewUserConfirm(@Body RequestBody body);

    /**
     * 分享信息
     * @param map
     * @return
     */
    @GET("newexclusive/sharepage")
    Call<BaseEntity<ShareInfoParam>> shareNewUserInfo(@QueryMap Map<String, String> map);

    /**
     * 登录界面提示信息
     * @param map
     * @return
     */
    @GET("member/login/info")
    Call<BaseEntity<New3LoginInfoTipEntity>> loginInfoTip(@QueryMap Map<String,String> map);

    /**
     * 会员集主页
     * @return
     */
    @POST("member/follower/followerList")
    Call<BaseEntity<MemberInfoEntity>> followerList(@Body RequestBody body);

    /**
     * 绑定导购员
     * @return
     */
    @POST("member/Register/bindShareidAfter")
    Call<BaseEntity<EmptyEntity>> bindShareidAfter(@Body RequestBody body);

    /**
     * 首页验证新人
     * @return
     */
    @GET("newexclusive/isShowNewPersonPrize")
    Call<BaseEntity<CommonEntity>> isUserShowNewPersonPrize(@QueryMap Map<String, String> map);

    /**
     *领取新人优惠券
     */
    @GET("newexclusive/getvoucher")
    Call<BaseEntity<UserNewDataEntity>> getvoucher(@QueryMap Map<String, String> map);

    /**
     * 新版注册
     * @return
     */
    @POST("member/register/v2")
    Call<BaseEntity<LoginFinishEntity>> newRegister(@Body RequestBody body);

    /**
     * 新版微信登录
     * @return
     */
    @POST("member/oauth/wechatV2")
    Call<BaseEntity<LoginFinishEntity>> newWXLogin(@Body RequestBody body);

    /**
     * 从微信界面过来绑定手机号验证
     * @param body
     * @return
     */
    @POST("member/oauth/checkMobileV2")
    Call<BaseEntity<CommonEntity>> checkFromWXMobile(@Body RequestBody body);

    /**
     * 绑定导购员
     * @return
     */
    @POST("member/userinfo/bindShareidV2")
    Call<BaseEntity<CommonEntity>> bindShareidV2(@Body RequestBody body);

    /**
     * 转转转获取奖品列表
     *
     * @return
     */
    @GET("task/getPrizeList")
    Call<BaseEntity<GoldEggPrizeEntity>> getPrizeList(@QueryMap Map<String, String> map);

    /**
     * 转转转抽奖
     *
     * @return
     */
    @GET("task/draw")
    Call<BaseEntity<TaskDrawEntity>> getTaskDraw(@QueryMap Map<String, String> map);

    /**
     * 转转转获取轮播中奖记录
     *
     * @return
     */
    @GET("task/getDrawRecordList")
    Call<BaseEntity<DrawRecordEntity>> getDrawRecordList(@QueryMap Map<String, String> map);

    /**
     * 转转转未填收货地址订单
     *
     * @return
     */
    @GET("task/getNoAddressOrder")
    Call<BaseEntity<NoAddressOrderEntity>> getNoAddressOrder(@QueryMap Map<String, String> map);

    /**
     * 转转转获取我的中奖记录
     *
     * @return
     */
    @GET("task/getMyDrawRecordList")
    Call<BaseEntity<MyDrawRecordEntity>> getMyDrawRecordList(@QueryMap Map<String, String> map);

    /**
     * 转转转保存收货地址
     *
     * @return
     */
    @POST("task/updateOrderAddress")
    Call<BaseEntity<CommonEntity>> updateOrderAddress(@Body RequestBody body);

    /**
     * 转转转积分消费记录
     *
     * @return
     */
    @GET("task/creditLog")
    Call<BaseEntity<CreditLogEntity>> getScoreRecord(@QueryMap Map<String, String> map);

    /**
     * 新人专享banner
     */
    @GET("newexclusive/showVoucherSuspension")
    Call<BaseEntity<ShowVoucherSuspension>> showVoucherSuspension(@QueryMap Map<String, String> map);

    /**
     * 邀请好友列表
     */
    @GET("newexclusive/inviteLog")
    Call<BaseEntity<InviteLogUserEntity>> inviteLog(@QueryMap Map<String, String> map);

    /**
     * plus免费专区 确认订单
     * @param map
     * @return
     */
    @GET("plusfree/buy")
    Call<BaseEntity<ConfirmOrderEntity>> plusfree(@QueryMap Map<String, String> map);

    /**
     * plus免费专区 订单结算
     * @param body
     * @return
     */
    @POST("plusfree/checkoutOrder")
    Call<BaseEntity<PayOrderEntity>> plusfreePay(@Body RequestBody body);

    /**
     * 检查是否有上级
     * @param map
     * @return
     */
    @GET("member/userinfo/checkBindShareidV2")
    Call<BaseEntity<CommonEntity>> checkBindShareidV2(@QueryMap Map<String, String> map);

    /**
     * 绑定导师
     * @return
     */
    @POST("member/userinfo/bindShareidV2")
    Call<BaseEntity<EmptyEntity>> bindShareidV(@Body RequestBody body);

    /**
     * 发现  评论 快捷驳回列表
     * @param map
     * @return
     */
    @GET("discovery/comment/checkSetList")
    Call<BaseEntity<CommentRejectedEntity>> checkSetList(@QueryMap Map<String,String> map);

    /**
     * 白名单会员审核评论
     * @return
     */
    @POST("discovery/comment/check")
    Call<BaseEntity<CommonEntity>> commentCheck(@Body RequestBody body);

    /**
     * 我的周六中奖记录
     * @return
     */
    @GET("turntable/myPrizes")
    Call<BaseEntity<SaturdayDrawRecordEntity>> getDrawRecord(@QueryMap Map<String,String> map);
}
