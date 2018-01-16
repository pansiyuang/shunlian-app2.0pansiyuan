package com.shunlian.app.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.CateEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.EmptyEntity;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IGoodsDetailView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by Administrator on 2017/11/8.
 */

public class GoodsDetailPresenter extends BasePresenter<IGoodsDetailView> {


    private String goods_id;
    private int page = 1;//当前页
    public static final int COMMENT_FAILURE_CODE = 400;//评论网络请求失败码
    public static final int COMMENT_EMPTY_CODE = 420;//评论数据为空码
    public static final int pageSize = 20;//评价每页数量
    private boolean isLoading = false;
    private String type = "ALL";
    private int allPage;//总页数

    public GoodsDetailPresenter(Context context, IGoodsDetailView iView, String goods_id) {
        super(context, iView);
        this.goods_id = goods_id;
        initApi();
    }

    @Override
    protected void initApi() {
        Map<String, String> map = new HashMap<>();
        map.put("goods_id", goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<GoodsDeatilEntity>> baseEntityCall = getApiService().goodsDetail(requestBody);
        getNetData(0,0,true,baseEntityCall, new SimpleNetDataCallback<BaseEntity<GoodsDeatilEntity>>() {
            @Override
            public void onSuccess(BaseEntity<GoodsDeatilEntity> entity) {
                super.onSuccess(entity);
                GoodsDeatilEntity data = entity.data;
                if (data != null) {
                    iView.goodsDetailData(data);
                    iView.isFavorite(data.is_fav);
                    iView.goodsOffShelf(data.status);
                }
            }
        });
    }

    /**
     * 关注店铺
     * @param storeId
     */
    public void followStore(String storeId){
        if (Common.loginPrompt()){
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
        if (Common.loginPrompt()){
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
                Common.staticToast(entity.message);
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                Common.staticToast(message);
            }
        });
    }

    public void addCart(String goods_id,String sku_id,String qty){
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("sku_id",sku_id);
        map.put("qty",qty);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CateEntity>> baseEntityCall = getAddCookieApiService().addCart(requestBody);
        getNetData(true,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CateEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CateEntity> entity) {
                super.onSuccess(entity);
                iView.addCart(entity.message);
            }
        });

    }

    public void footprint(){
        Map<String,String> map = new HashMap<>();
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<FootprintEntity>> baseEntityCall = getAddCookieApiService().footPrint(requestBody);
        getNetData(baseEntityCall,new SimpleNetDataCallback<BaseEntity<FootprintEntity>>(){
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
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> goodsfavorite = getAddCookieApiService().goodsfavorite(requestBody);
        getNetData(goodsfavorite,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
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
        if (Common.loginPrompt()){
            return;
        }
        Map<String,String> map = new HashMap<>();
        map.put("ids",ids);
        sortAndMD5(map);
        RequestBody requestBody = getRequestBody(map);
        Call<BaseEntity<CommonEntity>> goodsfavorite = getAddCookieApiService().goodsfavoriteRemove(requestBody);
        getNetData(goodsfavorite,new SimpleNetDataCallback<BaseEntity<CommonEntity>>(){
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
     * @param pageSize
     * @param id
     */
    public void commentList(int emptyCode, int failureCode, final boolean isLoading,
                            String goods_id, String type, final String page, String pageSize, String id){
        Map<String,String> map = new HashMap<>();
        map.put("goods_id",goods_id);
        map.put("type",type);
        map.put("page",page);
        map.put("pageSize",pageSize);
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        sortAndMD5(map);
        Call<BaseEntity<CommentListEntity>> baseEntityCall = getApiService().commentList(map);
        getNetData(emptyCode,failureCode,isLoading,baseEntityCall,new SimpleNetDataCallback<BaseEntity<CommentListEntity>>(){
            @Override
            public void onSuccess(BaseEntity<CommentListEntity> entity) {
                super.onSuccess(entity);
                GoodsDetailPresenter.this.isLoading = false;
                CommentListEntity.ListData list = entity.data.list;
                GoodsDetailPresenter.this.page = Integer.parseInt(list.page)+1;
                GoodsDetailPresenter.this.allPage = Integer.parseInt(list.allPage);
                iView.commentListData(entity.data);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                GoodsDetailPresenter.this.isLoading = false;
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                GoodsDetailPresenter.this.isLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (!isLoading){
            isLoading = true;
            if (page <= allPage){
                commentList(COMMENT_EMPTY_CODE,COMMENT_FAILURE_CODE,false,
                        goods_id,type,String.valueOf(page),String.valueOf(pageSize),null);
            }
        }
    }

    @Override
    public void attachView() {

    }

    @Override
    public void detachView() {

    }

    public void setType(String type) {
        this.type = type;
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
                iView.praiseTotal(entity.data.praise_total);
            }
        });
    }

    /**
     * 领取优惠券
     * @param voucherId
     */
    public void getVoucher(String voucherId) {
        if (Common.loginPrompt()){
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
                Common.staticToast(context.getResources().getString(R.string.get_success));
                iView.refreshVoucherState(entity.data);
            }
        });

    }
}
