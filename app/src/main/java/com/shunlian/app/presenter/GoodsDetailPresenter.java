package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommentAdapter;
import com.shunlian.app.adapter.ProbablyLikeAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ProbablyLikeEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGoodsDetailView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {


    private String goods_id;
    public static final int COMMENT_FAILURE_CODE = 400;//评论网络请求失败码
    public static final int COMMENT_EMPTY_CODE = 420;//评论数据为空码
    public static final String pageSize = "20";//评价每页数量
    private String type = "ALL";
    private String act_id;
    private ShareInfoParam shareInfoParam;
    private String mayBeBuyGoodsId;
    private final GoodsDetailAct mDetailAct;
    private Call<BaseEntity<GoodsDeatilEntity>> mGoodsDataCall;
    private Call<BaseEntity<ProbablyLikeEntity>> mMayBeBuyCall;
    private Call<BaseEntity<CateEntity>> mAddCarCall;
    private Call<BaseEntity<FootprintEntity>> mFootprintCall;
    private Call<BaseEntity<CommonEntity>> mGoodsFavCall;
    private Call<BaseEntity<CommonEntity>> mGoodsFavRemoveCall;
    private Call<BaseEntity<CommentListEntity>> mCommentCall;
    private Call<BaseEntity<CommonEntity>> mChatIdCall;

    /*************评价相关************************/
    private List<CommentListEntity.Data> mCommentLists = new ArrayList<>();
    private CommentAdapter mCommentAdapter;
    private boolean isClickHead = false;//是否点击头部
    private int praisePosition;//点赞位置
    private ProbablyLikeAdapter mLikeAdapter;
    private Call<BaseEntity<EmptyEntity>> mGoodsWantCall;

    public GoodsDetailPresenter(Context context, IGoodsDetailView iView, String goods_id) {
        super(context, iView);
        mDetailAct = (GoodsDetailAct) context;
        this.goods_id = goods_id;
        initApi();
        mayBeBuy();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        mGoodsDataCall = getSaveCookieApiService().goodsDetail(requestBody);
        getNetData(0,100,true, mGoodsDataCall,
                new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity> entity) {
                super.onSuccess(entity);
                shareInfoParam = new ShareInfoParam();
                GoodsDeatilEntity data = entity.data;
                //LogUtil.augusLogW("dfdfd---"+data.credit);
                if (!TextUtils.isEmpty(data.credit)&&Integer.parseInt(data.credit)>0){
                    Common.staticToasts(context,String.format(getStringResouce(R.string.common_gongxinin),data.credit),R.mipmap.icon_jifen);
                }
                if (data != null) {
                    iView.goodsDetailData(data);
                    iView.isFavorite(data.is_fav);
                    iView.goodsOffShelf(data.status);//是否下架
                    if (!isEmpty(data.stock) && Integer.parseInt(data.stock) <= 0)
                        iView.stockDeficiency(data.stock);//是否有库存
                    if (!"0".equals(data.status)){//非下架商品
                        GoodsDeatilEntity.TTAct tt_act = data.tt_act;
                        if (tt_act != null){
                            act_id = tt_act.id;
                            iView.activityState(tt_act.sale,tt_act.remind_status);
                            shareInfoParam.start_time = tt_act.start_time;
                            shareInfoParam.act_label = "天天特惠";
                        }
                    }
                    GoodsDeatilEntity.SpecailAct common_activity = data.common_activity;
                    if (common_activity != null){
                        iView.specailAct();
                    }

                    /**************分享****************/
                    shareInfoParam.title = data.title;
                    shareInfoParam.img = data.pics.get(0);
                    shareInfoParam.goodsPrice = data.price;
                    shareInfoParam.desc = data.introduction;
                    shareInfoParam.downloadPic = data.pics;
                    shareInfoParam.goods_id = goods_id;
                    if (data.user_info != null){
                        shareInfoParam.shareLink = data.user_info.share_url;
                        shareInfoParam.userAvatar = data.user_info.avatar;
                        shareInfoParam.userName = data.user_info.nickname;
                    }
                    /**************分享****************/

                    if (data.common_activity != null){
                        shareInfoParam.start_time = data.common_activity.start_time;
                        shareInfoParam.act_label = data.is_preferential;
                    }

                    if ("1".equals(data.type)){
                        iView.superiorProduct();
                    }else if ("2".equals(data.type)){
                        iView.groupBuy();
                    }
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                ((GoodsDetailAct)context).finish();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                iView.showFailureView(0);
            }
        });
    }

    /**
     * 刷新整页数据
     */
    public void refreshDetail(){
//        goods_id = "134";
        initApi();
    }

    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().addMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                DefMessageEvent event = new DefMessageEvent();
                event.followStoreState = 1;
                EventBus.getDefault().post(event);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /**
     * 取消关注店铺
     * @param storeId
     */
    public void delFollowStore(String storeId){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("storeId",storeId);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService().delMark(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                DefMessageEvent event = new DefMessageEvent();
                event.followStoreState = 0;
                EventBus.getDefault().post(event);
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    /**
     * 添加购物车
     * @param goods_id
     * @param sku_id
     * @param qty
     */
    public void addCart(String goods_id,String sku_id,String qty){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("sku_id",sku_id);
        map.put("qty",qty);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        mAddCarCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true, mAddCarCall,new SimpleNetDataCallback<BaseEntity<CateEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                iView.addCart(entity.message);
            }
        });

    }

    /**
     * 足迹
     */
    public void footprint(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        mFootprintCall = getAddCookieApiService().footPrint(requestBody);
        getNetData(mFootprintCall,new SimpleNetDataCallback<BaseEntity<FootprintEntity>>(){
            @Override
            public void onSuccess(BaseEntity<FootprintEntity> entity) {
                super.onSuccess(entity);
                FootprintEntity data = entity.data;
                iView.footprintList(data);
            }
        });
    }

    /**
     * 添加收藏
     * @param goods_id
     */
    public void goodsFavAdd(String goods_id){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        mGoodsFavCall = getAddCookieApiService().goodsfavorite(requestBody);
        getNetData(mGoodsFavCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.isFavorite(entity.data.fid);
                Common.staticToast(entity.message);
            }
        });
    }

    /**
     * 移除收藏
     * @param ids
     */
    public void goodsFavRemove(String ids){
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("ids",ids);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        mGoodsFavRemoveCall = getAddCookieApiService().goodsfavoriteRemove(requestBody);
        getNetData(mGoodsFavRemoveCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.isFavorite(entity.data.fid);
                Common.staticToast(entity.message);
            }
        });
    }

    /**
     * 评价列表
     * @param goods_id
     * @param type
     * @param page
     * @param id
     */
    public void commentList(int emptyCode, int failureCode, final boolean isShow,
                            String goods_id, String type, final String page,String id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("type",type);
        map.put("page",page);
        map.put("pageSize",pageSize);
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        sortAndMD5(map);
        mCommentCall = getApiService().commentList(map);
        getNetData(emptyCode,failureCode,isShow, mCommentCall,new
                SimpleNetDataCallback<BaseEntity<CommentListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommentListEntity> entity) {
                super.onSuccess(entity);
                isLoading = false;
                CommentListEntity.ListData list = entity.data.list;
                currentPage = Integer.parseInt(list.page);
                allPage = Integer.parseInt(list.allPage);
                handleCommentData(entity.data);
                currentPage++;
            }

            @Override
            public void onFailure() {
                super.onFailure();
                isLoading = false;
                if (mCommentAdapter != null){
                    mCommentAdapter.loadFailure();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                isLoading = false;
            }
        });
    }

    /**
     * 处理评价数据
     * @param data
     */
    private void handleCommentData(CommentListEntity data) {
        CommentListEntity.ListData list = data.list;
        if (currentPage == 1){
            mCommentLists.clear();
        }
        int size = mCommentLists.size();
        mCommentLists.addAll(list.data);
        if (mCommentAdapter == null){
            mCommentAdapter = new CommentAdapter(context,!isEmpty(list.data),mCommentLists);
            iView.setCommentAdapter(mCommentAdapter);
            //上拉加载
            mCommentAdapter.setOnReloadListener(() -> onRefresh());
            //加载分类数据
            mCommentAdapter.setCommentTypeListener(requestType -> {
                isClickHead = true;
                type = requestType;
                commentList(COMMENT_EMPTY_CODE,COMMENT_FAILURE_CODE,false,
                        goods_id,type,"1",null);
            });

            mCommentAdapter.setCommentPraiseListener((comment_id, position) -> {
                setCommentPraise(comment_id);
                praisePosition = position;
            });
        }
        boolean isClear = false;
        if (!isClickHead){
            isClear = true;
        }else {
            isClear = false;
        }
        mCommentAdapter.setLabel(data.label,isClear);
        mCommentAdapter.setPageLoading(currentPage,allPage);
        if (!isEmpty(list.data) && list.data.size() > 10){
            mCommentAdapter.notifyItemRangeChanged(size,Integer.parseInt(pageSize));
        }else {
            mCommentAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            isLoading = true;
            if (currentPage <= allPage){
                commentList(COMMENT_EMPTY_CODE,COMMENT_FAILURE_CODE,false,
                        goods_id,type,String.valueOf(currentPage),null);
            }
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {
        if (mGoodsDataCall != null && mGoodsDataCall.isExecuted()){
            mGoodsDataCall.cancel();
            mGoodsDataCall = null;
        }
        if (mMayBeBuyCall != null && mMayBeBuyCall.isExecuted()){
            mMayBeBuyCall.cancel();
            mMayBeBuyCall = null;
        }
        if (mAddCarCall != null && mAddCarCall.isExecuted()){
            mAddCarCall.cancel();
            mAddCarCall = null;
        }
        if (mFootprintCall != null && mFootprintCall.isExecuted()){
            mFootprintCall.cancel();
            mFootprintCall = null;
        }
        if (mGoodsFavCall != null && mGoodsFavCall.isExecuted()){
            mGoodsFavCall.cancel();
            mGoodsFavCall = null;
        }
        if (mGoodsFavRemoveCall != null && mGoodsFavRemoveCall.isExecuted()){
            mGoodsFavRemoveCall.cancel();
            mGoodsFavRemoveCall = null;
        }
        if (mCommentCall != null && mCommentCall.isExecuted()){
            mCommentCall.cancel();
            mCommentCall = null;
        }
        if (mChatIdCall != null && mChatIdCall.isExecuted()){
            mChatIdCall.cancel();
            mChatIdCall = null;
        }
        if (mGoodsWantCall != null && mGoodsWantCall.isExecuted()){
            mGoodsWantCall.cancel();
            mGoodsWantCall = null;
        }

        if (mCommentLists != null){
            mCommentLists.clear();
            mCommentLists = null;
        }

        if (mCommentAdapter != null){
            mCommentAdapter.unbind();
            mCommentAdapter = null;
        }
        if (mLikeAdapter != null){
            mLikeAdapter.unbind();
            mLikeAdapter = null;
        }
    }

    /**
     * 点赞
     */
    public void setCommentPraise(String comment_id){
        Map<String,String> map = new HashMap<>();
        map.put("comment_id",comment_id);
        sortAndMD5(map);
        Call<BaseEntity<CommonEntity>> baseEntityCall = getAddCookieApiService().commentPraise(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                if (mCommentAdapter != null)
                    mCommentAdapter.setPraiseTotal(entity.data.praise_total,praisePosition);
            }
        });
    }

    /**
     * 领取优惠券
     * @param voucherId
     */
    public void getVoucher(String voucherId) {
        if (!Common.isAlreadyLogin()){
            Common.goGoGo(context,"login");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("voucher_id", voucherId);
        sortAndMD5(map);
        Call<BaseEntity<GoodsDeatilEntity.Voucher>> baseEntityCall = getApiService().getVoucher(getRequestBody(map));
        getNetData(true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity.Voucher>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity.Voucher> entity) {
                super.onSuccess(entity);
                Common.staticToast(entity.message);
                iView.refreshVoucherState(entity.data);
            }
        });

    }

    /**
     * 设置提醒
     */
    public void settingRemind(){
        Map<String,String> map = new HashMap<>();
        map.put("id",act_id);
        map.put("goods_id",goods_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .actRemindMe(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.activityState("0","1");
                Common.staticToasts(context, Common.getResources()
                        .getString(R.string.day_set_remind),R.mipmap.icon_common_duihao);
            }
        });
    }

    /**
     * 取消提醒
     */
    public void cancleRemind(){
        Map<String,String> map = new HashMap<>();
        map.put("id",act_id);
        map.put("goods_id",goods_id);
        sortAndMD5(map);

        Call<BaseEntity<EmptyEntity>> baseEntityCall = getAddCookieApiService()
                .cancleRemind(getRequestBody(map));
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                iView.activityState("0","0");
                Common.staticToasts(context, Common.getResources()
                        .getString(R.string.day_cancel_remind),R.mipmap.icon_common_tanhao);
            }
        });
    }

    /**
     * 可能还想买
     */
    public void mayBeBuy(){
        Map<String,String> map = new HashMap<>();
        map.put("from","goods");
        map.put("ref_id",goods_id);
        sortAndMD5(map);
        mMayBeBuyCall = getApiService().mayBeBuy(getRequestBody(map));
        getNetData(false, mMayBeBuyCall,new
                SimpleNetDataCallback<BaseEntity<ProbablyLikeEntity>>(){
                    @Override
                    public void onSuccess(BaseEntity<ProbablyLikeEntity> entity) {
                        super.onSuccess(entity);
                        if (!isEmpty(entity.data.may_be_buy_list)){
                            mLikeAdapter = new ProbablyLikeAdapter
                                    (context,entity.data.may_be_buy_list,false);
                            iView.setAdapter(mLikeAdapter);
                            mLikeAdapter.setOnItemClickListener((v, p)->{
                                mDetailAct.moreHideAnim();
                                ProbablyLikeEntity.MayBuyList mayBuyList = entity.
                                        data.may_be_buy_list.get(p);
                                mayBeBuyGoodsId = mayBuyList.id;
                            });
                        }else {
                            iView.showDataEmptyView(100);
                        }
                    }
                });
    }

    /**
     * 复制链接
     */
    public void copyText(boolean isToast) {
        if (shareInfoParam == null)return;
        Common.copyText(context,shareInfoParam.shareLink,shareInfoParam.desc,isToast);
    }

    /**
     * 分享数据
     * @return
     */
    public ShareInfoParam getShareInfoParam() {
        return shareInfoParam;
    }

    public void setShareInfoParam(ShareInfoParam shareInfoParam) {
        this.shareInfoParam = shareInfoParam;
    }

    /**
     * 获取商家聊天id
     * @param shopId
     */
    public void getUserId(String shopId) {
        Map<String, String> map = new HashMap<>();
        map.put("shop_id", shopId);
        sortAndMD5(map);

        mChatIdCall = getAddCookieApiService().getUserId(map);
        getNetData(false, mChatIdCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                CommonEntity commonEntity = entity.data;
                iView.getUserId(commonEntity.user_id);
            }

            @Override
            public void onErrorCode(int code, String message) {
                Common.staticToast(message);
                super.onErrorCode(code, message);
            }
        });
    }

    /**
     * 可能想买的商品
     */
    public void mayBeBuyGoods() {
        if (!isEmpty(mayBeBuyGoodsId)) {
            Common.goGoGo(context, "goods", mayBeBuyGoodsId);
            mayBeBuyGoodsId = null;
        }
    }

    /**
     * 还想要
     */
    public void goodsWant(){
        Map<String,String> map = new HashMap<>();
        map.put("id",goods_id);
        sortAndMD5(map);
        mGoodsWantCall = getApiService().goodsWant(map);
        getNetData(true, mGoodsWantCall,new SimpleNetDataCallback<BaseEntity<EmptyEntity>>(){
            @Override
            public void onSuccess(BaseEntity<EmptyEntity> entity) {
                super.onSuccess(entity);
                Common.staticToast("已抢购~好物补货通知您！");
            }
        });
    }
}
