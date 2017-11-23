package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreDiscountMenuAdapter extends BaseRecyclerAdapter<StorePromotionGoodsListEntity.Lable> {
    private Context context;
    private List<StorePromotionGoodsListEntity.Lable> datas;
    public int selectPosition=0;

    public StoreDiscountMenuAdapter(Context context, boolean isShowFooter, List<StorePromotionGoodsListEntity.Lable> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_discount_menu, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;
            StorePromotionGoodsListEntity.Lable data = datas.get(position);
            twoHolder.mtv_name.setText(data.name);
            twoHolder.view.setTag(R.id.tag_store_discount_menu,data.promotionId);
            if (selectPosition==position){
                LogUtil.augusLogW("yxfdf--"+position);
                twoHolder.mtv_name.setTextColor(context.getResources().getColor(R.color.white));
                twoHolder.mtv_name.setBackgroundResource(R.drawable.shape_store_discount_menu);
            }else {
                LogUtil.augusLogW("yxfdfkl--"+position);
                twoHolder.mtv_name.setTextColor(context.getResources().getColor(R.color.pink_color));
                twoHolder.mtv_name.setBackgroundResource(R.drawable.shape_store_discount_menus);
            }
        }
    }


    class TwoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MyTextView mtv_name;
        private View view;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
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
