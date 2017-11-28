package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class DisabledGoodsAdapter extends BaseRecyclerAdapter<ShoppingCarEntity.Disabled> {
    private Context mContext;
    private List<ShoppingCarEntity.Disabled> mDisables;
    private LayoutInflater layoutInflater;

    public DisabledGoodsAdapter(Context context, boolean isShowFooter, List<ShoppingCarEntity.Disabled> lists) {
        super(context, isShowFooter, lists);
        this.mContext = context;
        this.mDisables = lists;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        DisableGoodsViewholder disableGoodsViewholder = new DisableGoodsViewholder(layoutInflater.inflate(R.layout.item_shoppoingcar_disable, parent, false));
        return disableGoodsViewholder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        DisableGoodsViewholder mHolder = (DisableGoodsViewholder) holder;
        ShoppingCarEntity.Disabled disabled = mDisables.get(position);

        GlideUtils.getInstance().loadImage(mContext, mHolder.miv_icon_disable, disabled.thumb);
        mHolder.tv_disable_title.setText(disabled.goods_title);

        if (!TextUtils.isEmpty(disabled.sku)) {
            mHolder.tv_disable_param.setText(disabled.sku);
        }
        if ("2".equals(disabled.status)) { // 2为卖完  3为下架
            mHolder.tv_disable_status.setText(getString(R.string.goods_sell_out));
        } else if ("3".equals(disabled.status)) {
            mHolder.tv_disable_status.setText(getString(R.string.goods_off_shelf));
        }
    }

    public class DisableGoodsViewholder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon_disable)
        MyImageView miv_icon_disable;

        @BindView(R.id.tv_disable_title)
        TextView tv_disable_title;

        @BindView(R.id.tv_disable_param)
        TextView tv_disable_param;

        @BindView(R.id.tv_disable_status)
        TextView tv_disable_status;

        public DisableGoodsViewholder(View itemView) {
            super(itemView);
        }
    }
}
