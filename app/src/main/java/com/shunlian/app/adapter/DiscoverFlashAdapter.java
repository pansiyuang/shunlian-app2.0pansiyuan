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


    public DiscoverFlashAdapter(Context context, boolean isShowFooter, List<DiscoveryNavEntity.Flash> datas) {
        super(context, isShowFooter, datas);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_flash, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;

            DiscoveryNavEntity.Flash data = lists.get(position);
//            StorePromotionGoodsListEntity.Lable data = lists.get(position);
            twoHolder.mtv_title.setText(data.title);
            twoHolder.mtv_desc.setText(data.full_title);
            if (position==0){
                twoHolder.view_left.setVisibility(View.VISIBLE);
                twoHolder.view_right.setVisibility(View.GONE);
            }else if (position==lists.size()-1){
                twoHolder.view_left.setVisibility(View.GONE);
                twoHolder.view_right.setVisibility(View.VISIBLE);
            }else {
                twoHolder.view_left.setVisibility(View.GONE);
                twoHolder.view_right.setVisibility(View.GONE);
            }
            data.thumb="https://img01.sldlcdn.com/attachment/channelimg/2018/03/P599bAK9k5Ql5iAbiotqqIx7B99Hyy.jpg";
            GlideUtils.getInstance().loadCornerImage(context,twoHolder.miv_photo,data.thumb,4);
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

        private View view;

        TwoHolder(View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
