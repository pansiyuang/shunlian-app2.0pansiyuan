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
import android.widget.TextView;

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
import com.shunlian.app.widget.BlogGoodsShareDialog;
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
    private String from, froms, mBlogId;
    private Dialog dialog;
    private BlogGoodsShareDialog blogGoodsShareDialog;
    private ShareInfoParam mShareInfoParam;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private HotVideoBlogPresenter hotVideoBlogPresenter;


    public DiscoverGoodsAdapter(Context context, String blogId, List<GoodsDeatilEntity.Goods> lists, boolean isCode, String from, String froms, Dialog dialog) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        this.isCode = isCode;
        this.from = from;
        this.froms = froms;
        this.mBlogId = blogId;
        this.dialog = dialog;
        shareGoodDialogUtil = new ShareGoodDialogUtil(context);
        hotVideoBlogPresenter = new HotVideoBlogPresenter(context, this);
    }

    public DiscoverGoodsAdapter(Context context, String blogId, List<GoodsDeatilEntity.Goods> lists, boolean isCode, String from, String froms, BlogGoodsShareDialog activity) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        this.isCode = isCode;
        this.from = from;
        this.froms = froms;
        this.mBlogId = blogId;
        this.blogGoodsShareDialog = activity;
        if (context != null) {
            shareGoodDialogUtil = new ShareGoodDialogUtil(context);
            hotVideoBlogPresenter = new HotVideoBlogPresenter(context, this);
        }
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
            if (goods == null)
                return;
            GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, goods.thumb, 4);
            viewHolder.ntv_title.setText(goods.title);
            viewHolder.ntv_price.setText(getString(R.string.common_yuan) + goods.price);
            viewHolder.ntv_price1.setText(getString(R.string.common_yuan) + goods.price);
            viewHolder.ntv_priceM1.setText(getString(R.string.common_yuan) + goods.market_price);
            viewHolder.ntv_priceM1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            viewHolder.ntv_priceM.setText(getString(R.string.common_yuan) + goods.market_price);
            viewHolder.ntv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            if (isCode) {
                viewHolder.ntv_code.setVisibility(View.VISIBLE);
                viewHolder.ntv_price1.setVisibility(View.VISIBLE);
                viewHolder.ntv_priceM1.setVisibility(View.VISIBLE);
                viewHolder.ntv_priceM.setVisibility(View.GONE);
                viewHolder.ntv_price.setVisibility(View.GONE);
                viewHolder.miv_share.setVisibility(View.GONE);
                viewHolder.tv_share_text.setVisibility(View.GONE);
                viewHolder.ntv_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mShareInfoParam = new ShareInfoParam();
                        mShareInfoParam.goods_id = goods.goods_id;
                        if(hotVideoBlogPresenter!=null) {
                            hotVideoBlogPresenter.getShareInfo(hotVideoBlogPresenter.goods, goods.goods_id);
                         }
                    }
                });
            } else {
                viewHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mShareInfoParam = new ShareInfoParam();
                        mShareInfoParam.goods_id = goods.goods_id;
                        mShareInfoParam.blogId = mBlogId;
                        if (shareGoodDialogUtil != null) {
                            shareGoodDialogUtil.setOnShareBlogCallBack((blogId, goodsId) -> hotVideoBlogPresenter.goodsShare("blog_goods", blogId, goodsId));
                        }
                        if(hotVideoBlogPresenter!=null) {
                            hotVideoBlogPresenter.getShareInfo(hotVideoBlogPresenter.goods, goods.goods_id);
                        }
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
        if(mShareInfoParam==null){
            mShareInfoParam = new ShareInfoParam();
        }
        if (mShareInfoParam != null) {
            mShareInfoParam.isShowTiltle = false;
            mShareInfoParam.userName = baseEntity.data.userName;
            mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
            mShareInfoParam.shareLink = baseEntity.data.shareLink;
            mShareInfoParam.title = baseEntity.data.title;
            mShareInfoParam.desc = baseEntity.data.desc;
            mShareInfoParam.price = baseEntity.data.price;
            mShareInfoParam.share_buy_earn = baseEntity.data.share_buy_earn;
            mShareInfoParam.img = baseEntity.data.img;
            mShareInfoParam.little_word = baseEntity.data.little_word;
            mShareInfoParam.time_text = baseEntity.data.time_text;
            mShareInfoParam.is_start = baseEntity.data.is_start;
            mShareInfoParam.market_price = baseEntity.data.market_price;
            mShareInfoParam.voucher = baseEntity.data.voucher;
            if (shareGoodDialogUtil != null&&!isCode) {
                shareGoodDialogUtil.shareGoodDialog(mShareInfoParam,true,true);
                shareGoodDialogUtil.setShareGoods();
            }else if(shareGoodDialogUtil != null&&isCode){
                shareGoodDialogUtil.setShareInfoParam(mShareInfoParam);
                shareGoodDialogUtil.createGoodCode(true, false);
            }
             if (dialog != null) {
                 dialog.dismiss();
              }
             if (blogGoodsShareDialog != null) {
                 blogGoodsShareDialog.finish();
              }
        }

    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {

    }


    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.tv_share_text)
        TextView tv_share_text;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_price1)
        NewTextView ntv_price1;

        @BindView(R.id.ntv_priceM1)
        NewTextView ntv_priceM1;

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
