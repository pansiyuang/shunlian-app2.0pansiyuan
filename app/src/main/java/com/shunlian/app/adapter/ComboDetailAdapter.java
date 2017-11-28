package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAdapter extends BaseRecyclerAdapter<String> {

    public static final int ITEM_PIC = 2;
    public static final int ITEM_GIFT_PRICE = 3;
    public static final int ITEM_OTHER = 4;
    public static final int ITEM_OTHER_COMBO = 5;
    private final LayoutInflater mInflater;

    public ComboDetailAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_PIC:
                View picView = mInflater.inflate(R.layout.item_detail, parent, false);
                return new PicHolder(picView);
            case ITEM_GIFT_PRICE:
                View giftPriceView = mInflater.inflate(R.layout.item_gift_price, parent, false);
                return new GiftPriceHolder(giftPriceView);
            case ITEM_OTHER:
                View otherView = mInflater.inflate(R.layout.item_gift_price, parent, false);
                return new OtherHolder(otherView);
            case ITEM_OTHER_COMBO:
                View comboView = mInflater.inflate(R.layout.item_dialog_combo, parent, false);
                return new OtherComboHolder(comboView);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_PIC;
        }else if (position == 1){
            return ITEM_GIFT_PRICE;
        }else if (position < lists.size() + 2){
            return super.getItemViewType(position);
        }else if (position == lists.size() + 2){
            return ITEM_OTHER;
        }else {
            return ITEM_OTHER_COMBO;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = mInflater.inflate(R.layout.item_combo, parent, false);
        return new ComboHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class PicHolder extends BaseRecyclerViewHolder{


        public PicHolder(View itemView) {
            super(itemView);
            MyImageView miv_pic = (MyImageView) itemView;
            miv_pic.setWHProportion(720,386);
        }
    }

    public class GiftPriceHolder extends BaseRecyclerViewHolder{

        public GiftPriceHolder(View itemView) {
            super(itemView);
        }
    }

    public class OtherHolder extends BaseRecyclerViewHolder{

        public OtherHolder(View itemView) {
            super(itemView);
        }
    }

    public class OtherComboHolder extends BaseRecyclerViewHolder{

        public OtherComboHolder(View itemView) {
            super(itemView);
        }
    }

    public class ComboHolder extends BaseRecyclerViewHolder{

        public ComboHolder(View itemView) {
            super(itemView);
        }
    }
}
