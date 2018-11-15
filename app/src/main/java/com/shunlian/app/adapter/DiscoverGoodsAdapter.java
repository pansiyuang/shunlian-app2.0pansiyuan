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
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SaveAlbumDialog;
import com.shunlian.app.utils.TransformUtil;
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

public class DiscoverGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    private LayoutInflater mInflater;
    private boolean isCode;
    private QuickActions quickActions;
    private String from,froms,mBlogId;
    private Dialog dialog;

    public DiscoverGoodsAdapter(Context context,String blogId, List<GoodsDeatilEntity.Goods> lists,
                                boolean isCode,QuickActions quick_actions,String from,String froms,Dialog dialog) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
        this.isCode=isCode;
        this.quickActions=quick_actions;
        this.from=from;
        this.froms=froms;
        this.mBlogId = blogId;
        this.dialog=dialog;
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
                GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, goods.thumb,4);
                viewHolder.ntv_title.setText(goods.title);
                viewHolder.ntv_price.setText(getString(R.string.common_yuan)+goods.price);
                viewHolder.ntv_price1.setText(getString(R.string.common_yuan)+goods.price);
                viewHolder.ntv_priceM1.setText(getString(R.string.common_yuan)+goods.market_price);
                viewHolder.ntv_priceM1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                viewHolder.ntv_priceM.setText(getString(R.string.common_yuan)+goods.market_price);
                viewHolder.ntv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                if (isCode){
                    viewHolder.ntv_code.setVisibility(View.VISIBLE);
                    viewHolder.ntv_price1.setVisibility(View.VISIBLE);
                    viewHolder.ntv_priceM1.setVisibility(View.VISIBLE);
                    viewHolder.ntv_priceM.setVisibility(View.GONE);
                    viewHolder.ntv_price.setVisibility(View.GONE);
                    viewHolder.miv_share.setVisibility(View.GONE);
                    viewHolder.ntv_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            boolean is=false;
//                            if (!isEmpty(goods.isSuperiorProduct)&&"1".equals(goods.isSuperiorProduct)){
//                                is=true;
//                            }else {
//                                is=false;
//                            }
                            quickActions.createCode(goods.share_url,goods.title,goods.desc,goods.price,goods.goods_id,goods.thumb,
                                    1==goods.isSuperiorProduct,from,froms);
                            dialog.dismiss();
                        }
                    });
                }else {
                    viewHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            quickActions.shareDiscoverDialog(mBlogId, goods.share_url, goods.title, goods.desc, goods.price, goods.goods_id, goods.thumb,
                                    1 == goods.isSuperiorProduct, from, froms);
                            dialog.dismiss();
                        }
                    });
                }

            }
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
