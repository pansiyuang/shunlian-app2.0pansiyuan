package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/15.
 */

public class ComboAdapter extends BaseRecyclerAdapter {
    private Context context;
    private List<GoodsDeatilEntity.Combo> combos;

    public ComboAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Combo> lists) {
        super(context, isShowFooter, lists);
        this.context = context;
        this.combos = lists;
    }

    public void setData(List<GoodsDeatilEntity.Combo> lists) {
        this.combos = lists;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog_combo, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ComboAdapter.ViewHolder) holder;
        GoodsDeatilEntity.Combo combo = combos.get(position);
        viewHolder.tv_combo_price.setText(combo.combo_price);
        viewHolder.tv_market_price.setText(combo.old_combo_price);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        viewHolder.recycler_combo.setLayoutManager(linearLayoutManager);
        viewHolder.recycler_combo.setAdapter(new ComboPicAdapter(context, false, combo.goods));
    }

    public class ViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_combo_price)
        TextView tv_combo_price;

        @BindView(R.id.tv_market_price)
        TextView tv_market_price;

        @BindView(R.id.recycler_combo)
        RecyclerView recycler_combo;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ComboPicAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Combo.Goods> {
        private List<GoodsDeatilEntity.Combo.Goods> goods;

        public ComboPicAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Combo.Goods> lists) {
            super(context, isShowFooter, lists);
            this.goods = lists;
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_combo_pic, parent, false));
            return viewHolder;
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            PicViewHolder mHoler = (PicViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, mHoler.miv_combo, goods.get(position).thumb);
        }

        public class PicViewHolder extends BaseRecyclerViewHolder {
            @BindView(R.id.miv_combo)
            MyImageView miv_combo;

            public PicViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
