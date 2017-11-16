package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class BlockStoreFirstTwoAdapter extends BaseRecyclerAdapter<StoreIndexEntity.Body.Datas> {
    private Context context;
    private List<StoreIndexEntity.Body.Datas> datas;

    public BlockStoreFirstTwoAdapter(Context context, boolean isShowFooter, List<StoreIndexEntity.Body.Datas> datas) {
        super(context, isShowFooter, datas);
        this.context=context;
        this.datas=datas;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_block_store_first_two, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoItemHolder) {
            TwoItemHolder twoItemHolder = (TwoItemHolder) holder;
            StoreIndexEntity.Body.Datas data=datas.get(position);
            twoItemHolder.mtv_desc.setText(data.title);
            twoItemHolder.mtv_price.setText(data.price);
            twoItemHolder.mtv_number.setText(data.item_id);
            GlideUtils.getInstance().loadImage(context,twoItemHolder.miv_img,data.whole_thumb);
        }
    }


    class TwoItemHolder extends RecyclerView.ViewHolder {
        private MyImageView miv_img;
        private MyTextView mtv_desc,mtv_price,mtv_number;

        public TwoItemHolder(View itemView) {
            super(itemView);
            mtv_desc= (MyTextView) itemView.findViewById(R.id.mtv_desc);
            mtv_price= (MyTextView) itemView.findViewById(R.id.mtv_price);
            mtv_number= (MyTextView) itemView.findViewById(R.id.mtv_number);
            miv_img= (MyImageView) itemView.findViewById(R.id.miv_img);
        }

    }

}
