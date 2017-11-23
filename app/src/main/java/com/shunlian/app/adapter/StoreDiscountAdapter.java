package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreDiscountAdapter extends BaseRecyclerAdapter<StorePromotionGoodsListEntity.Lists.Good.Data> {
    private Context context;
    private List<StorePromotionGoodsListEntity.Lists.Good.Data> datas;


    public StoreDiscountAdapter(Context context, boolean isShowFooter, List<StorePromotionGoodsListEntity.Lists.Good.Data> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
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
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_baby, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            StorePromotionGoodsListEntity.Lists.Good.Data data=datas.get(position);
            oneHolder.mtv_descl.setText(data.title);
            oneHolder.mtv_pricel.setText(data.price);
            oneHolder.mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            oneHolder.mtv_pricer.setText(context.getResources().getString(R.string.common_yuan)+data.market_price);
            GlideUtils.getInstance().loadImage(context,oneHolder.miv_onel,data.whole_thumb);
        }
    }

    class OneHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_descl,mtv_pricel,mtv_pricer;
        private MyImageView miv_onel;


        OneHolder(View itemView) {
            super(itemView);
            miv_onel = (MyImageView) itemView.findViewById(R.id.miv_onel);
            mtv_descl = (MyTextView) itemView.findViewById(R.id.mtv_descl);
            mtv_pricel = (MyTextView) itemView.findViewById(R.id.mtv_pricel);
            mtv_pricer = (MyTextView) itemView.findViewById(R.id.mtv_pricer);
        }
    }

}
