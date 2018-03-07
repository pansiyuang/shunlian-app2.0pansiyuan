package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CheckInStateEntity;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class SignGoodsAdapter extends BaseRecyclerAdapter<CheckInStateEntity.GoodsList.MData> {

    public SignGoodsAdapter(Context context, boolean isShowFooter, List<CheckInStateEntity.GoodsList.MData> datas) {
        super(context, isShowFooter, datas);

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isBottom(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isBottom(int position) {
        if (position + 1 == getItemCount()){
            return true;
        }
        return false;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_goods, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            CheckInStateEntity.GoodsList.MData data=lists.get(position);
            oneHolder.mtv_title.setText(data.title);
            SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan)+ data.price, getString(R.string.common_yuan), 9);
            oneHolder.mtv_price.setText(spannableStringBuilder);
            GlideUtils.getInstance().loadImage(context,oneHolder.miv_img,data.thumb);
        }
    }

    class OneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyTextView mtv_title,mtv_price;
        private MyImageView miv_img;


        OneHolder(View itemView) {
            super(itemView);
            miv_img = (MyImageView) itemView.findViewById(R.id.miv_img);
            mtv_title = (MyTextView) itemView.findViewById(R.id.mtv_title);
            mtv_price = (MyTextView) itemView.findViewById(R.id.mtv_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

}
