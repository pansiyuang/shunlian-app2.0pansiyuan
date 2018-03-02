package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/2.
 */

public class QrcodeStoreAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {

    public static final int FOOT_VIEW = 10003;

    public QrcodeStoreAdapter(Context context, List<GoodsDeatilEntity.Goods> lists) {
        super(context, false, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOT_VIEW) {
            return new FootHolderView(LayoutInflater.from(context).inflate(R.layout.bottom_qrcode_store, parent, false));
        } else {
            return new GoodsHolderView(LayoutInflater.from(context).inflate(R.layout.item_qrcode_goods, parent, false));
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType = getItemViewType(position);
                    if (itemType == FOOT_VIEW) {
                        return ((GridLayoutManager) manager).getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOT_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == FOOT_VIEW) {
            handFoot(holder);
        } else {
            handItem(holder, position);
        }
    }

    public void handFoot(RecyclerView.ViewHolder holder) {
        if (holder instanceof FootHolderView) {
            FootHolderView footHolderView = (FootHolderView) holder;
        }
    }

    public void handItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsHolderView) {
            GoodsHolderView goodsHolderView = (GoodsHolderView) holder;
            GoodsDeatilEntity.Goods goods = lists.get(position);

            int space = TransformUtil.dip2px(context, 12);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((DeviceInfoUtil.getDeviceWidth(context) - (space * 3)) / 2, TransformUtil.dip2px(context, 168.5f));
            if (position % 2 == 1) { //右边item
                layoutParams.setMargins(0, space, space, 0);
            } else {//左边item
                layoutParams.setMargins(space, space, space, 0);
            }
            goodsHolderView.miv_icon.setLayoutParams(layoutParams);

            GlideUtils.getInstance().loadImage(context, goodsHolderView.miv_icon, goods.thumb);
            goodsHolderView.tv_goods_title.setText(goods.title);
            if (!isEmpty(goods.price)) {
                goodsHolderView.tv_price.setText(getString(R.string.common_yuan) + " " + goods.price);
            }
        }
    }

    public class GoodsHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public GoodsHolderView(View itemView) {
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

    public class FootHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_qrcode)
        MyImageView miv_qrcode;

        public FootHolderView(View itemView) {
            super(itemView);
        }
    }
}
