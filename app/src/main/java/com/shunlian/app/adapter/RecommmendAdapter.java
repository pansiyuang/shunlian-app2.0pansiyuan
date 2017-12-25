package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.Common.firstSmallText;

/**
 * Created by Administrator on 2017/12/11.
 */

public class RecommmendAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    public OnGoodsBuyOnclickListener mListener;

    public RecommmendAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new RecommmendViewHolder(LayoutInflater.from(context).inflate(R.layout.item_meger_recommmedn, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        RecommmendViewHolder viewHolder = (RecommmendViewHolder) holder;
        final GoodsDeatilEntity.Goods goods = lists.get(position);
        GlideUtils.getInstance().loadImage(context, viewHolder.miv_meger_icon, goods.thumb);
        viewHolder.tv_meger_title.setText(goods.goods_title);

        String price = getString(R.string.common_yuan) + goods.price;
        firstSmallText(viewHolder.tv_meger_price, price, TransformUtil.sp2px(context, 5));

        viewHolder.tv_meger_sell.setText("已售：" + goods.sales);
        viewHolder.miv_meger_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnItemBuy(goods);
                }
            }
        });
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.miv_meger_icon.getLayoutParams();
        params.width = (DeviceInfoUtil.getDeviceWidth(context) - TransformUtil.dip2px(context, 5)) / 2;
        params.height = params.width;
        viewHolder.miv_meger_icon.setLayoutParams(params);
    }

    public class RecommmendViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_meger_icon)
        MyImageView miv_meger_icon;

        @BindView(R.id.tv_meger_title)
        TextView tv_meger_title;

        @BindView(R.id.tv_meger_price)
        TextView tv_meger_price;

        @BindView(R.id.tv_meger_sell)
        TextView tv_meger_sell;

        @BindView(R.id.miv_meger_buy)
        MyImageView miv_meger_buy;

        public RecommmendViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public void setOnGoodsBuyOnclickListener(OnGoodsBuyOnclickListener listener) {
        this.mListener = listener;
    }

    public interface OnGoodsBuyOnclickListener {
        void OnItemBuy(GoodsDeatilEntity.Goods goods);
    }
}
