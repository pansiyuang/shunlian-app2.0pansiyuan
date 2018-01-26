package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreGoodsAdapter extends BaseRecyclerAdapter<CollectionStoresEntity.Store.NewGood> {
    private String storeId;
    private boolean isMore;

    public StoreGoodsAdapter(Context context, boolean isShowFooter, List<CollectionStoresEntity.Store.NewGood> datas, String storeId,boolean isMore) {
        super(context, isShowFooter, datas);
        this.storeId = storeId;
        this.isMore=isMore;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_goods, parent, false));
    }

    @Override
    public int getItemCount() {
        if (isMore){
            return 10;
        }
        return super.getItemCount();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            final OneHolder oneHolder = (OneHolder) holder;
            if (position >= 9) {
                oneHolder.miv_img.setVisibility(View.GONE);
                oneHolder.mtv_price.setVisibility(View.GONE);
                oneHolder.mtv_more.setVisibility(View.VISIBLE);
            } else {
                oneHolder.mtv_more.setVisibility(View.GONE);
                oneHolder.miv_img.setVisibility(View.VISIBLE);
                oneHolder.mtv_price.setVisibility(View.VISIBLE);
                CollectionStoresEntity.Store.NewGood data = lists.get(position);
                oneHolder.mtv_price.setText(getString(R.string.common_yuan) + data.price);
                GlideUtils.getInstance().loadImage(context, oneHolder.miv_img, data.thumb);
            }
        }
    }

    class OneHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_more)
        MyTextView mtv_more;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        OneHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mtv_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.mtv_more:
                    StoreAct.startAct(context, storeId);
                    break;
                default:
                    if (listener != null) {
                        listener.onItemClick(view, getAdapterPosition());
                    }
                    break;
            }
        }
    }

}
