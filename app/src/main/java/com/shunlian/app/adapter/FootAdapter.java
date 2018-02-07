package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/25.
 */

public class FootAdapter extends BaseRecyclerAdapter<Object> {

    public static final int DATE_LAYOUT = 1003;
    public boolean isEdit;
    public OnChildClickListener mListener;

    public FootAdapter(Context context, List<Object> lists) {
        super(context, true, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DATE_LAYOUT) {
            return new DateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_foot_head, parent, false));
        }
        return new MarkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_foot, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        LogUtil.httpLogW("size:" + getItemCount() + "lists:" + lists.size());
        if (lists.get(position) instanceof FootprintEntity.DateInfo) {
            return DATE_LAYOUT;
        }
        return super.getItemViewType(position);
    }

    public void setEditMode(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public boolean getEditMode() {
        return isEdit;
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.no_more_footmark));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case DATE_LAYOUT:
                handleTitle(holder, position);
                break;
            default:
                handleItem(holder, position);
                break;
        }
    }

    public void handleItem(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MarkViewHolder) {
            MarkViewHolder markViewHolder = (MarkViewHolder) holder;
            final FootprintEntity.MarkData markData = (FootprintEntity.MarkData) lists.get(position);
            GlideUtils.getInstance().loadImage(context, markViewHolder.miv_icon, markData.thumb);
            markViewHolder.tv_price.setText(getString(R.string.common_yuan) + markData.price);

            if (isEdit) {
                markViewHolder.ll_del.setVisibility(View.VISIBLE);
            } else {
                markViewHolder.ll_del.setVisibility(View.GONE);
            }

            if (markData.isSelect) {
                markViewHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
            } else {
                markViewHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_n));
            }

            markViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.OnItemSelect(position, markData);
                    }
                }
            });
        }
    }

    public void handleTitle(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof DateViewHolder) {
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            final FootprintEntity.DateInfo dateInfo = (FootprintEntity.DateInfo) lists.get(position);
            dateViewHolder.tv_date.setText(dateInfo.date);

            if (isEdit) {
                dateViewHolder.miv_select.setVisibility(View.VISIBLE);
            } else {
                dateViewHolder.miv_select.setVisibility(View.GONE);
            }
            if (dateInfo.isSelect) {
                dateViewHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
            } else {
                dateViewHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_n));
            }

            if (position == 0) {
                dateViewHolder.ll_foot.setPadding(TransformUtil.dip2px(context, 10f), 0, TransformUtil.dip2px(context, 10f), TransformUtil.dip2px(context, 15f));
            } else {
                dateViewHolder.ll_foot.setPadding(TransformUtil.dip2px(context, 10f), TransformUtil.dip2px(context, 15f), TransformUtil.dip2px(context, 10f), TransformUtil.dip2px(context, 15f));
            }

            dateViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.OnDateSelect(position, dateInfo);
                    }
                }
            });
        }
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
                    if (getItemViewType(position) == DATE_LAYOUT) {
                        return gridManager.getSpanCount();
                    } else if (position + 1 == getItemCount()) {
                        return gridManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    public class DateViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.ll_foot)
        LinearLayout ll_foot;

        public DateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class MarkViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.ll_del)
        LinearLayout ll_del;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public MarkViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.mListener = listener;
    }

    public interface OnChildClickListener {
        void OnDateSelect(int position, FootprintEntity.DateInfo dateInfo);

        void OnItemSelect(int position, FootprintEntity.MarkData markData);
    }
}
