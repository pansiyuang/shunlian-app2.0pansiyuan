package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.Common.firstSmallText;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ComboAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Combo> {


    public ComboAdapter(Context context,List<GoodsDeatilEntity.Combo> lists) {
        super(context, false, lists);

    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        ComboViewHolder viewHolder = new ComboViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_dialog_combo, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ComboViewHolder viewHolder = (ComboAdapter.ComboViewHolder) holder;
        GoodsDeatilEntity.Combo combo = lists.get(position);
        String comboPrice = context.getResources().getString(R.string.rmb)+ combo.combo_price+"-"+combo.max_combo_price;
        firstSmallText(viewHolder.tv_combo_price, comboPrice, 9);
        viewHolder.tv_market_price.setText(String.format(getString(R.string.combo_original_price),
                combo.old_combo_price+"-"+combo.max_old_combo_price));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recycler_combo.setLayoutManager(linearLayoutManager);
        ComboPicAdapter comboPicAdapter = new ComboPicAdapter(context, combo.goods);
        viewHolder.recycler_combo.setAdapter(comboPicAdapter);
        comboPicAdapter.setOnItemClickListener((v,p)->{
            if (listener != null){
                listener.onItemClick(v,position);
            }
        });
        viewHolder.recycler_combo.setOnTouchListener((v,e)-> {
            if (e.getAction() == MotionEvent.ACTION_UP && listener != null){
                listener.onItemClick(v,position);
            }
            return false;
        });
    }

    public class ComboViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.recycler_combo)
        RecyclerView recycler_combo;

        public ComboViewHolder(View itemView) {
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

    public static class ComboPicAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {
        private List<GoodsDeatilEntity.Goods> goods;

        public ComboPicAdapter(Context context,List<GoodsDeatilEntity.Goods> lists) {
            super(context, false, lists);
            this.goods = lists;
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            PicViewHolder viewHolder = new PicViewHolder(LayoutInflater
                    .from(context).inflate(R.layout.item_combo_pic, parent, false));
            return viewHolder;
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            PicViewHolder mHoler = (PicViewHolder) holder;
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHoler.miv_combo, goods.get(position).thumb,160,160);
        }

        public class PicViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
            @BindView(R.id.miv_combo)
            MyImageView miv_combo;

            public PicViewHolder(View itemView) {
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
}
