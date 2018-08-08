package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.ProbabyLikeGoodsEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/14.
 */

public class ProbabyLikeGoodsAdapter extends BaseRecyclerAdapter<ProbabyLikeGoodsEntity.Goods> {

    public static final int PARENT_LAYOUT = 100001;
    public static final int CHILD_LAYOUT = 100002;

    public ProbabyLikeGoodsAdapter(Context context, List<ProbabyLikeGoodsEntity.Goods> lists) {
        super(context, false, lists);
    }

    @Override
    public int getItemViewType(int position) {
        if (lists.get(position).isParent) {
            return PARENT_LAYOUT;
        }
        return CHILD_LAYOUT;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case PARENT_LAYOUT:
                            return gridManager.getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PARENT_LAYOUT:
                return new ParentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_probaby_like, parent, false));
            case CHILD_LAYOUT:
                return new ChildrenViewHolder(LayoutInflater.from(context).inflate(R.layout.item_store_baby, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case PARENT_LAYOUT:
                handleParent(holder, position);
                break;
            case CHILD_LAYOUT:
                handlerChild(holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
    }

    public void handleParent(RecyclerView.ViewHolder holder, int position) {
        ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
        ProbabyLikeGoodsEntity.Goods goods = lists.get(position);
        GlideUtils.getInstance().loadImage(context, parentViewHolder.miv_icon, goods.thumb);
    }

    public void handlerChild(RecyclerView.ViewHolder holder, int position) {
        try {
            ProbabyLikeGoodsEntity.Goods goods = lists.get(position);
            ChildrenViewHolder childrenViewHolder = (ChildrenViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, childrenViewHolder.miv_onel, goods.thumb);
            childrenViewHolder.mtv_descl.setText(goods.title);
            childrenViewHolder.mtv_pricel.setText(goods.price);
            childrenViewHolder.mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            childrenViewHolder.mtv_pricer.setText(getString(R.string.common_yuan) + goods.market_price);
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) childrenViewHolder.ll_rootView.getLayoutParams();

            int margin = TransformUtil.dip2px(context, 5);
            if (goods.index % 2 == 0) {//左边
                layoutParams.setMargins(0, 0, margin, margin);
            } else { //右边
                layoutParams.setMargins(0, 0, 0, margin);
            }
            childrenViewHolder.mrlayout_plus.setVisibility(View.GONE);
            childrenViewHolder.ll_rootView.setOnClickListener(v -> GoodsDetailAct.startAct(context, goods.id));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ParentViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.ll_rootView)
        LinearLayout ll_rootView;

        public ParentViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ChildrenViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.ll_rootView)
        LinearLayout ll_rootView;

        @BindView(R.id.mtv_descl)
        MyTextView mtv_descl;

        @BindView(R.id.mtv_pricel)
        MyTextView mtv_pricel;

        @BindView(R.id.mtv_pricer)
        MyTextView mtv_pricer;

        @BindView(R.id.miv_onel)
        MyImageView miv_onel;

        @BindView(R.id.mrlayout_plus)
        MyRelativeLayout mrlayout_plus;


        public ChildrenViewHolder(View itemView) {
            super(itemView);
        }
    }
}
