package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class DiscoverGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    private LayoutInflater mInflater;

    public DiscoverGoodsAdapter(Context context, List<GoodsDeatilEntity.Goods> lists) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
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
                GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_photo, goods.thumb);
                viewHolder.ntv_title.setText(goods.title);
                viewHolder.ntv_price.setText(goods.price);
                viewHolder.ntv_priceM.setText(goods.old_price);
                viewHolder.ntv_priceM.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                viewHolder.miv_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.staticToast("分享...");
                    }
                });
                viewHolder.ntv_code.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.staticToast("二维码...");
                    }
                });
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
