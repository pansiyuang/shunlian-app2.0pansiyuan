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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/7.
 */

public class FootprintAdapter extends BaseRecyclerAdapter<FootprintEntity.MarkData> {

    public final int ITEM_TIME = 10;
    public final int Head_Layout = 13;

    private LayoutInflater inflater;
    private List<FootprintEntity.DateInfo> mDateList;
    private boolean isEdit;
    private OnChildClickListener mListener;

    private List<Integer> timeShowPosition = new ArrayList<>();
    public Map<Integer, FootprintEntity.DateInfo> timeDatas = new HashMap<>();
    private int titleCount;
    private float titleSpace = 115.5f;

    public FootprintAdapter(Context context, List<FootprintEntity.MarkData> lists, List<FootprintEntity.DateInfo> dateList) {
        super(context, true, lists);
        inflater = LayoutInflater.from(context);
        mDateList = dateList;
        initData();
    }

    public void initData() {
        timeShowPosition.clear();
        timeDatas.clear();
        timeShowPosition.add(1);
        timeDatas.put(timeShowPosition.get(timeShowPosition.size() - 1), mDateList.get(0));
        for (int i = 0; i < mDateList.size(); i++) {//计算日期显示的位置
            FootprintEntity.DateInfo dateInfo = mDateList.get(i);
            if (i + 1 != mDateList.size()) {
                int size = timeShowPosition.size() - 1;
                timeShowPosition.add(timeShowPosition.get(size) + Integer.parseInt(dateInfo.counts) + 1);
                timeDatas.put(timeShowPosition.get(timeShowPosition.size() - 1), mDateList.get(i + 1));
            }
        }
    }

    public void setTitleSpace(float f) {
        titleSpace = f;
        notifyDataSetChanged();
    }

    public void setEditMode(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    public boolean getEditMode() {
        return isEdit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TIME:
                View view = inflater.inflate(R.layout.item_foot_head, parent, false);
                return new TimeHolder(view);
            case Head_Layout:
                return new HeadHolder(inflater.inflate(R.layout.footprint_head_space, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isHeader(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isHeader(int position) {
        if (timeShowPosition.contains(position)) {
            return true;
        } else if (position + 1 == getItemCount()) {
            return true;
        }
        return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Head_Layout;
        }
        if (timeShowPosition.contains(position)) {
            return ITEM_TIME;
        }
        return super.getItemViewType(position);
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
    public int getItemCount() {
        return super.getItemCount() + timeShowPosition.size();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_foot, parent, false);
        return new FootprintHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case ITEM_TIME:
                handleTime(holder, position);
                break;
            case Head_Layout:
                handTitle(holder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handTitle(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeadHolder) {
            HeadHolder headHolder = (HeadHolder) holder;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TransformUtil.dip2px(context, titleSpace));
            headHolder.ll_space.setLayoutParams(layoutParams);
        }
    }

    private void handleTime(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TimeHolder) {
            TimeHolder mHolder = (TimeHolder) holder;
            if (timeShowPosition.contains(position)) {
                final FootprintEntity.DateInfo dateInfo = timeDatas.get(position);
                mHolder.tv_date.setText(dateInfo.date);

                if (isEdit) {
                    mHolder.miv_select.setVisibility(View.VISIBLE);
                } else {
                    mHolder.miv_select.setVisibility(View.GONE);
                }

                if (dateInfo.isSelect) {
                    mHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
                } else {
                    mHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_n));
                }

                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.OnDateSelect(position, dateInfo);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FootprintHolder) {
            FootprintHolder mHolder = (FootprintHolder) holder;
            titleCount = computeCount(position);
            int index = position - titleCount;
            if (index < lists.size()) {
                final FootprintEntity.MarkData markData = lists.get(index);

                GlideUtils.getInstance().loadImage(context, mHolder.miv_icon, markData.thumb);
                mHolder.tv_price.setText(getString(R.string.common_yuan) + markData.price);

                if (isEdit) {
                    mHolder.ll_del.setVisibility(View.VISIBLE);
                } else {
                    mHolder.ll_del.setVisibility(View.GONE);
                }

                if (markData.isSelect) {
                    mHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_h));
                } else {
                    mHolder.miv_select.setImageDrawable(getDrawable(R.mipmap.img_shoppingcar_selected_n));
                }

                mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            mListener.OnItemSelect(position, markData);
                        }
                    }
                });
            }
        }
    }

    public class HeadHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.ll_space)
        LinearLayout ll_space;

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    public class FootprintHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.ll_del)
        LinearLayout ll_del;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_price)
        TextView tv_price;

        public FootprintHolder(View itemView) {
            super(itemView);
        }
    }


    public class TimeHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.ll_foot)
        LinearLayout ll_foot;

        public TimeHolder(View itemView) {
            super(itemView);
        }
    }


    public int computeCount(int position) {
        int count = 0;
        for (int i = 0; i < timeShowPosition.size(); i++) {
            Integer integer = timeShowPosition.get(i);
            if (integer < position) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.mListener = listener;
    }

    public interface OnChildClickListener {
        void OnDateSelect(int position, FootprintEntity.DateInfo dateInfo);

        void OnItemSelect(int position, FootprintEntity.MarkData markData);
    }
}
