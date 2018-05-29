package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.WeekSaleTopEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/1.
 */

public class SaleRankAdapter extends BaseRecyclerAdapter<WeekSaleTopEntity.Cate> {

    private LayoutInflater mInflater;

    public SaleRankAdapter(Context context, List<WeekSaleTopEntity.Cate> lists) {
        super(context, false, lists);
        mInflater = LayoutInflater.from(context);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SingleViewHolder(mInflater.inflate(R.layout.item_sale_rank, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SingleViewHolder) {
            SingleViewHolder viewHolder = (SingleViewHolder) holder;
            WeekSaleTopEntity.Cate goods = lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_avar, goods.avatar);
            viewHolder.ntv_name.setText(goods.nickname);
            viewHolder.miv_level.setVisibility(View.GONE);
            viewHolder.mtv_level.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    viewHolder.miv_level.setVisibility(View.VISIBLE);
                    viewHolder.miv_level.setImageResource(R.mipmap.img_paiming_yi);
                    viewHolder.mtv_desc.setTextColor(getColor(R.color.value_EFAD45));
                    break;
                case 1:
                    viewHolder.miv_level.setVisibility(View.VISIBLE);
                    viewHolder.miv_level.setImageResource(R.mipmap.img_paiming_er);
                    viewHolder.mtv_desc.setTextColor(getColor(R.color.value_AAA7A7));
                    break;
                case 2:
                    viewHolder.miv_level.setVisibility(View.VISIBLE);
                    viewHolder.miv_level.setImageResource(R.mipmap.img_paiming_san);
                    viewHolder.mtv_desc.setTextColor(getColor(R.color.value_F86B3A));
                    break;
                default:
                    viewHolder.mtv_level.setVisibility(View.VISIBLE);
                    viewHolder.mtv_level.setText("" + (position + 1));
                    viewHolder.mtv_desc.setTextColor(getColor(R.color.my_dark_grey));
                    break;
            }
            if (!isEmpty(goods.role)) {
                int level = Integer.parseInt(goods.role);
                if (level < 2) {
                    viewHolder.miv_icon.setImageResource(R.mipmap.img_plus_phb_dianzhu);
                } else if (level == 2) {
                    viewHolder.miv_icon.setImageResource(R.mipmap.img_plus_phb_zhuguan);
                } else {
                    viewHolder.miv_icon.setImageResource(R.mipmap.img_plus_phb_jingli);
                }
            }
            viewHolder.mtv_desc.setText(goods.sales);
            viewHolder.ntv_name.setText(goods.nickname);
        }
    }


    public class SingleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_level)
        MyImageView miv_level;

        @BindView(R.id.mtv_level)
        MyTextView mtv_level;

        @BindView(R.id.miv_avar)
        MyImageView miv_avar;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.ntv_name)
        NewTextView ntv_name;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

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
