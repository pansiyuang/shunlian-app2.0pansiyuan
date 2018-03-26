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

import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.AddGoodsEntity;
import com.shunlian.app.bean.AddressDataEntity;
import com.shunlian.app.bean.ArticleDetailEntity;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CheckInRespondEntity;
import com.shunlian.app.bean.CheckInStateEntity;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.bean.CommentDetailEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.CommentSuccessEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.ConsultHistoryEntity;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.bean.DistrictAllEntity;
import com.shunlian.app.bean.DistrictGetlocationEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.ExperienceEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.bean.FindSelectShopEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.bean.GetQrCardEntity;
import com.shunlian.app.bean.GetusernewsnumEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.bean.HotSearchEntity;
import com.shunlian.app.bean.JoinGoodsEntity;
import com.shunlian.app.bean.LoginFinishEntity;
import com.shunlian.app.bean.LogisticsNameEntity;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.bean.MyCommentListEntity;
import com.shunlian.app.bean.MyHomeEntity;
import com.shunlian.app.bean.MyOrderEntity;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.bean.OrderdetailEntity;
import com.shunlian.app.bean.PayListEntity;
import com.shunlian.app.bean.PayOrderEntity;
import com.shunlian.app.bean.PersonShopEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.bean.RankingListEntity;
import com.shunlian.app.bean.RefreshTokenEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.RefundListEntity;
import com.shunlian.app.bean.RegisterFinishEntity;
import com.shunlian.app.bean.SearchGoodsEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.bean.StoreLicenseEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListOneEntity;
import com.shunlian.app.bean.StorePromotionGoodsListTwoEntity;
import com.shunlian.app.bean.SubmitLogisticsInfoEntity;
import com.shunlian.app.bean.TagEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.bean.UseCommentEntity;
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
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by zhang on 2017/4/13 16 : 06.
 * 网络请求接口
 */

public interface ApiService {

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
     * 上传图片
     */
    @Multipart
    @POST("https://v20-test.shunliandongli.com/uploads/uploadotherimage")
    Call<BaseEntity<UploadPicEntity>> uploadPic(@PartMap Map<String, RequestBody> params, @Part("path_name") RequestBody path_name);

    /**
     * 新增评价
     *
     * @param body
     * @return
     */
    @POST("/member/comment/add")
    Call<BaseEntity<EmptyEntity>> addComment(@Body RequestBody body);

    /**
     * 批量追加评价
     *
     * @param body
     * @return
     */
    @POST("/member/comment/batch_append")
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
     * @param body
     * @return
     */
    @POST("order/buycombo")
    Call<BaseEntity<ConfirmOrderEntity>> buycombo(@Body RequestBody body);

    /**
     * 选择服务类型
     * @param body
     * @return
     */
    @POST("/member/refund/getrefundinfo")
    Call<BaseEntity<RefundDetailEntity.RefundDetail.Edit>> getrefundinfo(@Body RequestBody body);

    /**
     * 售后申请列表
     * @param map
     * @return
     */
    @GET("member/refund/applyList")
    Call<BaseEntity<RefundListEntity>> refundList(@QueryMap Map<String,String> map);

    /**
     * 申请退款
     *
     * @param body
     * @return
     */
    @POST("/member/refund/applyRefund")
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
     * @param map
     * @return
     */
    @GET("member/refund/applyLogList")
    Call<BaseEntity<ConsultHistoryEntity>> refundHistory(@QueryMap Map<String,String> map);


    /**
     * 提交订单
     * @param body
     * @return
     */
    @POST("order/checkout")
    Call<BaseEntity<PayOrderEntity>> orderCheckout(@Body RequestBody body);


    /**
     *从订单列表去支付
     * @param body
     * @return
     */
    @POST("order/payinorderlist")
    Call<BaseEntity<PayOrderEntity>> fromOrderListGoPay(@Body RequestBody body);

    /**
     * 退换货物流公司
     * @param map
     * @return
     */
    @GET("member/refund/refundExpressList")
    Call<BaseEntity<LogisticsNameEntity>> refundExpressList(@QueryMap Map<String,String> map);

    /**
     * 商品搜索列表
     * @param body
     * @return
     */
    @POST("/goods/search")
    Call<BaseEntity<SearchGoodsEntity>> getSearchGoods(@Body RequestBody body);

    /**
     * 筛选条件
     * @param body
     * @return
     */
    @POST("goods/getListFilter")
    Call<BaseEntity<GetListFilterEntity>> getListFilter(@Body RequestBody body);

    /**
     * 提交物流信息
     * @param body
     * @return
     */
    @POST("member/refund/saveShipInfo")
    Call<BaseEntity<EmptyEntity>> addLogisticsShipInfo(@Body RequestBody body);

    /**
     * 获取提交的物流信息
     * @param map
     * @return
     */
    @GET("member/refund/getShipInfo")
    Call<BaseEntity<SubmitLogisticsInfoEntity>> getLogisticsShipInfo(@QueryMap Map<String,String> map);

    /**
     * 分类所有数据
     * @return
     */
    @GET("operatecategory/all")
    Call<BaseEntity<SortFragEntity>> categoryAll(@QueryMap Map<String,String> map);

    /**
     * 排行榜
     * @return
     */
    @GET("goods/top")
    Call<BaseEntity<RankingListEntity>> rankingList(@QueryMap Map<String,String> map);

    /**
     * 申请平台介入
     * @param body
     * @return
     */
    @POST("member/refund/callplat")
    Call<BaseEntity<EmptyEntity>> callPlat(@Body RequestBody body);

    /**
     * 热搜关键字与搜索历史
     * @return
     */
    @GET("/goods/hotSearch")
    Call<BaseEntity<HotSearchEntity>> hotSearch(@QueryMap Map<String,String> map);

    /**
     * 搜索关键字提示
     * @return
     */
    @POST("/goods/keywordSuggest")
    Call<BaseEntity<CommonEntity>> keywordSuggest(@Body RequestBody body);

    /**
     * 清空搜索历史
     * @return
     */
    @POST("/goods/clearSearchHistory")
    Call<BaseEntity<EmptyEntity>> clearSearchHistory(@QueryMap Map<String,String> map);

    /**
     * 天天特惠列表
     * @param map
     * @return
     */
    @GET("activity/ttList")
    Call<BaseEntity<ActivityListEntity>> activityList(@QueryMap Map<String,String> map);

    /**
     * 设置提醒功能
     * @param body
     * @return
     */
    @POST("activity/remindMe")
    Call<BaseEntity<EmptyEntity>> actRemindMe(@Body RequestBody body);

    /**
     * 取消提醒
     * @return
     */
    @POST("activity/cancelRemind")
    Call<BaseEntity<EmptyEntity>> cancleRemind(@Body RequestBody body);

    /**
     * 收藏商品
     * @param body
     * @return
     */
    @POST("member/Myfavorite/favoriteGoods")
    Call<BaseEntity<CollectionGoodsEntity>> favoriteGoods(@Body RequestBody body);

    /**
     * 收藏的店铺
     * @param body
     * @return
     */
    @POST("member/Myfavorite/favoriteShop")
    Call<BaseEntity<CollectionStoresEntity>> favoriteShop(@Body RequestBody body);


    /**
     * 获取足迹日历形式信息
     * @return
     */
    @POST("member/footermark/getmarkCalendar")
    Call<BaseEntity<CommonEntity>> getmarkCalendar(@Body RequestBody body);

    /**
     * 我的足迹列表
     * @return
     */
    @POST("member/footermark/getmarklist")
    Call<BaseEntity<FootprintEntity>> getmarklist(@Body RequestBody body);

    /**
     * 收藏商品搜索
     * @param body
     * @return
     */
    @POST("member/Myfavorite/search")
    Call<BaseEntity<CollectionGoodsEntity>> collectionGoodsSearch(@Body RequestBody body);

    /**
     * 收藏店铺搜索
     * @param body
     * @return
     */
    @POST("member/Myfavorite/search")
    Call<BaseEntity<CollectionStoresEntity>> collectionStoreSearch(@Body RequestBody body);

    /**
     * 批量删除关注的商店
     * @param map
     * @return
     */
    @GET("member/Myfavorite/removeFavoShop")
    Call<BaseEntity<EmptyEntity>> removeFavoShop(@QueryMap Map<String,String> map);

    /**
     * 首页
     * @param map
     * @return
     */
    @GET("channel/index")
    Call<BaseEntity<MainPageEntity>> firstPage(@QueryMap Map<String,String> map);

    /**
     * 个人中心首页
     * @param map
     * @return
     */
    @GET("/personalcenter/home")
    Call<BaseEntity<PersonalcenterEntity>> personalcenter(@QueryMap Map<String,String> map);

    /**
     * 足迹列表足迹批量删除
     * @param map
     * @return
     */
    @GET("member/footermark/deleteBatch")
    Call<BaseEntity<EmptyEntity>> deleteBatch(@QueryMap Map<String, String> map);

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
     * @param map
     * @return
     */
    @GET("goods/brandlist")
    Call<BaseEntity<GetListFilterEntity>> brandlist(@QueryMap Map<String,String> map);

    /**
     * 小店展示页面
     * @param map
     * @return
     */
    @GET("myshop/personShop")
    Call<BaseEntity<PersonShopEntity>> personShop(@QueryMap Map<String,String> map);

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
     * @param map
     * @return
     */
    @GET("discovery/nice/commentList")
    Call<BaseEntity<FindCommentListEntity>> findcommentList(@QueryMap Map<String,String> map);

    /**
     * 发布评论
     * @return
     */
    @POST("discovery/user/comment")
    Call<BaseEntity<UseCommentEntity>> sendComment(@Body RequestBody body);

    /**
     * 推荐关注
     * @param map
     * @return
     */
    @GET("discovery/focus/recommendStore")
    Call<BaseEntity<FindSelectShopEntity>> recommendFollow(@QueryMap Map<String,String> map);

    /**
     * 评论详情
     * @param map
     * @return
     */
    @GET("discovery/nice/commentDetail")
    Call<BaseEntity<CommentDetailEntity>> commentDetail(@QueryMap Map<String,String> map);

    /**
     * 发现关注
     * @param map
     * @return
     */
    @GET("discovery/focus/home")
    Call<BaseEntity<GuanzhuEntity>> foucsHome(@QueryMap Map<String,String> map);

    /**
     * 点赞
     * @param map
     * @return
     */
    @GET("discovery/user/likeCommentOrReply")
    Call<BaseEntity<CommonEntity>> pointFabulous(@QueryMap Map<String,String> map);

    /**
     * 删除评论
     * @param map
     * @return
     */
    @GET("discovery/user/deleteComment")
    Call<BaseEntity<EmptyEntity>> delComment(@QueryMap Map<String,String> map);

    /**
     * 导航信息
     * @param map
     * @return
     */
    @GET("discovery/nice/nav")
    Call<BaseEntity<DiscoveryNavEntity>> discoveryNav(@QueryMap Map<String,String> map);

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
    Call<BaseEntity<EmptyEntity>> praiseExperience(@Body RequestBody body);

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
     * @param map
     * @return
     */
    @GET("discovery/material/list")
    Call<BaseEntity<DiscoveryMaterialEntity>> discoveryMaterial(@QueryMap Map<String,String> map);

    /**
     * 素材点赞
     *
     * @param body
     * @return
     */
    @POST("discovery/material/praise")
    Call<BaseEntity<CommonEntity>> discoveryPraise(@Body RequestBody body);

    /**
     * 前台话题列表
     * @param map
     * @return
     */
    @GET("discovery/circle/list")
    Call<BaseEntity<DiscoveryCircleEntity>> discoveryCircle(@QueryMap Map<String,String> map);

    /**
     * 前台话题列表
     * @param map
     * @return
     */
    @GET("discovery/circle/InvList")
    Call<BaseEntity<DiscoveryTieziEntity>> discoveryList(@QueryMap Map<String,String> map);

    /**
     * 前台帖子评论列表
     * @param map
     * @return
     */
    @GET("discovery/circle/commentList")
    Call<BaseEntity<DiscoveryCommentListEntity>> discoveryCommentList(@QueryMap Map<String,String> map);

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
     * 心得详情
     * @param map
     * @return
     */
    @GET("discovery/experience/commentList")
    Call<BaseEntity<EmptyEntity>> experienceDetail(@QueryMap Map<String,String> map);

}
