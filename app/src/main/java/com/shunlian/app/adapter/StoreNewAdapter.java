package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreNewAdapter extends BaseRecyclerAdapter<StoreNewGoodsListEntity.Data> {
    private Context context;
    private List<StoreNewGoodsListEntity.Data> datas;

    public StoreNewAdapter(Context context, boolean isShowFooter, List<StoreNewGoodsListEntity.Data> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_new, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            StoreNewGoodsListEntity.Data data=datas.get(position);
            if (TextUtils.isEmpty(data.day)){
                oneHolder.mllayout_title.setVisibility(View.GONE);
                oneHolder.view_line.setVisibility(View.GONE);
            }else {
                if (0==position){
                    oneHolder.view_line.setVisibility(View.VISIBLE);
                }else {
                    oneHolder.view_line.setVisibility(View.GONE);
                }
                oneHolder.mllayout_title.setVisibility(View.VISIBLE);
                oneHolder.mtv_date.setText(data.day);
            }
            oneHolder.mtv_descl.setText(data.mDatal.title);
            oneHolder.mtv_pricel.setText(data.mDatal.price);
            oneHolder.mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            oneHolder.mtv_pricer.setText(context.getResources().getString(R.string.common_yuan)+data.mDatal.market_price);
            GlideUtils.getInstance().loadImage(context,oneHolder.miv_onel,data.mDatal.whole_thumb);
            if (TextUtils.isEmpty(data.mDatar.title)&&TextUtils.isEmpty(data.mDatar.whole_thumb)){
                oneHolder.mllayout_oner.setVisibility(View.INVISIBLE);
            }else {
                oneHolder.mllayout_oner.setVisibility(View.VISIBLE);
                oneHolder.mtv_descr.setText(data.mDatar.title);
                oneHolder.mtv_pricels.setText(data.mDatar.price);
                oneHolder.mtv_pricers.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
                oneHolder.mtv_pricers.setText(context.getResources().getString(R.string.common_yuan)+data.mDatar.market_price);
                GlideUtils.getInstance().loadImage(context,oneHolder.miv_oner,data.mDatar.whole_thumb);
            }
        }
    }

    class OneHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_descl,mtv_pricel,mtv_pricer,mtv_date,mtv_descr,mtv_pricels,mtv_pricers;
        private MyImageView miv_onel,miv_oner;
        private MyLinearLayout mllayout_title,mllayout_oner;
        private View view_line;


        OneHolder(View itemView) {
            super(itemView);
            miv_onel = (MyImageView) itemView.findViewById(R.id.miv_onel);
            mtv_descl = (MyTextView) itemView.findViewById(R.id.mtv_descl);
            mtv_pricel = (MyTextView) itemView.findViewById(R.id.mtv_pricel);
            mtv_pricer = (MyTextView) itemView.findViewById(R.id.mtv_pricer);
            miv_oner = (MyImageView) itemView.findViewById(R.id.miv_oner);
            mtv_descr = (MyTextView) itemView.findViewById(R.id.mtv_descr);
            mtv_pricels = (MyTextView) itemView.findViewById(R.id.mtv_pricels);
            mtv_pricers = (MyTextView) itemView.findViewById(R.id.mtv_pricers);
            mtv_date = (MyTextView) itemView.findViewById(R.id.mtv_date);
            mllayout_title = (MyLinearLayout) itemView.findViewById(R.id.mllayout_title);
            mllayout_oner = (MyLinearLayout) itemView.findViewById(R.id.mllayout_oner);
            view_line =itemView.findViewById(R.id.view_line);
        }
    }

}
