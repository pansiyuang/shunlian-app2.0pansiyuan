package com.shunlian.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.presenter.ActivityPresenter;
import com.shunlian.app.presenter.HotVideoBlogPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SaveAlbumDialog;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IHotVideoBlogView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DiscoverGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> implements IHotVideoBlogView {
    private LayoutInflater mInflater;
    private boolean isCode;
    private QuickActions quickActions;
    private String from,froms,mBlogId;
    private ShareInfoParam  mShareInfoParam;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private HotVideoBlogPresenter hotVideoBlogPresenter;
    public DiscoverGoodsAdapter(Context context,String blogId, List<GoodsDeatilEntity.Goods> lists,boolean isCode,QuickActions quick_actions,String from,String froms) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        this.isCode=isCode;
        this.quickActions=quick_actions;
        this.from=from;
        this.froms=froms;
        this.mBlogId = blogId;
        shareGoodDialogUtil = new ShareGoodDialogUtil(context);
        hotVideoBlogPresenter = new HotVideoBlogPresenter(context,this);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SingleViewHolder(mInflater.inflate(R.layout.item_found_goods, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SingleViewHolder) {
                SingleViewHolder viewHolder = (SingleViewHolder) holder;
                GoodsDeatilEntity.Goods goods = lists.get(position);
                if (goods==null)
                    return;
                GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, goods.thumb);
                viewHolder.ntv_title.setText(goods.title);
                viewHolder.ntv_price.setText(getString(R.string.common_yuan)+goods.price);
                viewHolder.ntv_priceM.setText(getString(R.string.common_yuan)+goods.market_price);
                viewHolder.ntv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                if (isCode){
                    viewHolder.ntv_code.setVisibility(View.VISIBLE);
                    viewHolder.miv_share.setVisibility(View.GONE);
                    viewHolder.ntv_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean is=false;
//                            if (!isEmpty(goods.isSuperiorProduct)&&"1".equals(goods.isSuperiorProduct)){
//                                is=true;
//                            }else {
//                                is=false;
//                            }
//                            quickActions.createCode(goods.share_url,goods.title,goods.desc,goods.price,goods.goods_id,goods.thumb,
//                                    1==goods.isSuperiorProduct,from,froms);
                            mShareInfoParam = new ShareInfoParam();
                            mShareInfoParam.blogId =mBlogId;
                            mShareInfoParam.shareLink=goods.share_url;
                            mShareInfoParam.title =goods.title;
                            mShareInfoParam.desc =goods.desc;
                            mShareInfoParam.goods_id =goods.goods_id;
                            mShareInfoParam.price =goods.price;
                            mShareInfoParam.market_price =goods.market_price;
                            mShareInfoParam.img =goods.thumb;
                            mShareInfoParam.isSuperiorProduct =(goods.isSuperiorProduct==1?true:false);
                            mShareInfoParam.userName= SharedPrefUtil.getSharedUserString("nickname", "");
                            mShareInfoParam.userAvatar= SharedPrefUtil.getSharedUserString("avatar", "");
                            shareGoodDialogUtil.setShareInfoParam(mShareInfoParam);
                            shareGoodDialogUtil.createGoodCode(true);
                        }
                    });
                }else {
                    viewHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            shareGoodDialogUtil.setOnShareBlogCallBack(new ShareGoodDialogUtil.OnShareBlogCallBack() {
                                @Override
                                public void shareSuccess(String blogId, String goodsId) {
                                    hotVideoBlogPresenter.goodsShare("blog_goods", blogId, goodsId);
                                }
                            });
                            mShareInfoParam = new ShareInfoParam();
                            mShareInfoParam.blogId =mBlogId;
                            mShareInfoParam.shareLink=goods.share_url;
                            mShareInfoParam.title =goods.title;
                            mShareInfoParam.desc =goods.desc;
                            mShareInfoParam.goods_id =goods.goods_id;
                            mShareInfoParam.price =goods.price;
                            mShareInfoParam.market_price =goods.market_price;
                            mShareInfoParam.img =goods.thumb;
                            mShareInfoParam.isSuperiorProduct =(goods.isSuperiorProduct==1?true:false);
                            mShareInfoParam.userName= SharedPrefUtil.getSharedUserString("nickname", "");
                            mShareInfoParam.userAvatar= SharedPrefUtil.getSharedUserString("avatar", "");
                            shareGoodDialogUtil.shareGoodDialog(mShareInfoParam,true,true);
//                            quickActions.shareDiscoverDialog(mBlogId, goods.share_url, goods.title, goods.desc, goods.price, goods.goods_id, goods.thumb,
//                                    1 == goods.isSuperiorProduct, from, froms);
                        }
                    });
                }

            }
    }

    @Override
    public void focusUser(int isFocus, String memberId) {

    }

    @Override
    public void parseBlog(int isAttent, String memberId) {

    }

    @Override
    public void downCountSuccess() {

    }

    @Override
    public void shareGoodsSuccess(String blogId, String id) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {

    }


    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_code)
        NewTextView ntv_code;

        @BindView(R.id.ntv_priceM)
        NewTextView ntv_priceM;

        public SingleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
