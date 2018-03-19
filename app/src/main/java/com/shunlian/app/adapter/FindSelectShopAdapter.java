package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.FindSelectShopEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class FindSelectShopAdapter extends BaseRecyclerAdapter<FindSelectShopEntity.StoreList> {


    public FindSelectShopAdapter(Context context, List<FindSelectShopEntity.StoreList> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_shop, parent, false);
        return new FindSelectShopHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        FindSelectShopHolder mHolder = (FindSelectShopHolder) holder;
        FindSelectShopEntity.StoreList storeList = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHolder.civ_head,storeList.logo);
        mHolder.mtv_shop_name.setText(storeList.name);
        mHolder.mtv_shop_name.setText(storeList.desc);
        if (storeList.isSelect){
            mHolder.miv_select.setImageResource(R.mipmap.img_found_choiceshop_h);
        }else {
            mHolder.miv_select.setImageResource(R.mipmap.img_found_choiceshop_n);
        }
    }

    public class FindSelectShopHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_shop_name)
        MyTextView mtv_shop_name;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        public FindSelectShopHolder(View itemView) {
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
