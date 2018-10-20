package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/18.
 */

public class SelectGoodsAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Goods> {


    public SelectGoodsAdapter(Context context,boolean isShowFooter, List<GoodsDeatilEntity.Goods> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_select_goods, parent, false);
        return new SelectGoodsHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        SelectGoodsHolder mHolder = (SelectGoodsHolder) holder;
        GoodsDeatilEntity.Goods goods = lists.get(position);
        GlideUtils.getInstance().loadOverrideImage(context,mHolder.mivPic,goods.thumb,132,132);
        mHolder.mtvTitle.setText(goods.title);
        mHolder.mtvPrice.setText("￥"+goods.price);
    }


    public class SelectGoodsHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_pic)
        MyImageView mivPic;

        @BindView(R.id.mtv_title)
        MyTextView mtvTitle;

        @BindView(R.id.mtv_price)
        MyTextView mtvPrice;

        public SelectGoodsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }
}
