package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class DiscoverFlashAdapter extends BaseRecyclerAdapter<DiscoveryNavEntity.Flash> {


    public DiscoverFlashAdapter(Context context, List<DiscoveryNavEntity.Flash> datas) {
        super(context, false, datas);

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_discover_flash, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;

            DiscoveryNavEntity.Flash data = lists.get(position);
//            StorePromotionGoodsListEntity.Lable data = lists.get(position);
            twoHolder.mtv_title.setText(data.title);
            twoHolder.mtv_desc.setText(data.full_title);
            if (position == 0) {
                twoHolder.view_left.setVisibility(View.VISIBLE);
                twoHolder.view_right.setVisibility(View.GONE);
            } else if (position == getItemCount() - 1) {
                twoHolder.view_left.setVisibility(View.GONE);
                twoHolder.view_right.setVisibility(View.VISIBLE);
            }else {
                gone(twoHolder.view_left,twoHolder.view_right);
            }
//            GlideUtils.getInstance().loadImageChang(context,twoHolder.miv_photo,data.thumb);
            GlideUtils.getInstance().communityTopPic(context,twoHolder.miv_photo,data.thumb,4,false);
        }
    }


    class TwoHolder extends BaseRecyclerViewHolder implements View.OnClickListener{
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.view_left)
        View view_left;

        @BindView(R.id.view_right)
        View view_right;

        TwoHolder(View itemView) {
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
}
