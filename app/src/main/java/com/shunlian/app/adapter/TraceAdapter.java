package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.OrderLogisticsEntity;
import com.shunlian.app.presenter.OrderLogisticsPresenter;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/14.
 */

public class TraceAdapter extends BaseRecyclerAdapter<OrderLogisticsEntity.Trace> {

    public static final int HEAD_VIEW = 1;
    public static final int FOOT_VIEW = 2;
    private OrderLogisticsEntity orderLogisticsEntity;
    private boolean showMore;
    private List<OrderLogisticsEntity.FootMark> footMarkList;
    private GridSpacingItemDecoration gridSpacingItemDecoration;
    private FootMarkAdapter footMarkAdapter;
    private GridLayoutManager gridLayoutManager;
    private int currentPage;
    public OrderLogisticsPresenter mPresenter;

    public TraceAdapter(Context context, List<OrderLogisticsEntity.Trace> lists, OrderLogisticsEntity entity) {
        super(context, false, lists);
        this.orderLogisticsEntity = entity;
        gridSpacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(context, 5), false);
        OrderLogisticsEntity.History history = entity.history;
        footMarkList = new ArrayList<>();
        if (history.page == 1) {
            footMarkList.clear();
        }
        if (!isEmpty(history.mark_data)) {
            footMarkList.addAll(history.mark_data);
        }

        footMarkAdapter = new FootMarkAdapter(context, true, footMarkList);
    }

    public void setData(OrderLogisticsEntity entity) {
        OrderLogisticsEntity.History history = entity.history;
        if (history.page == 1) {
            footMarkList.clear();
        }
        if (!isEmpty(history.mark_data)) {
            footMarkList.addAll(history.mark_data);
            footMarkAdapter.setData(footMarkList);
        }
    }

    public void setPresenter(OrderLogisticsPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TraceViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order_logistics, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TraceViewHolder) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ((TraceViewHolder) holder).miv_logistics.getLayoutParams();
            OrderLogisticsEntity.Trace trace = lists.get(position - 1);
            TraceViewHolder traceViewHolder = (TraceViewHolder) holder;
            traceViewHolder.tv_logistics_detail.setText(trace.AcceptStation);
            traceViewHolder.tv_logistics_time.setText(trace.AcceptTime);

            if (position == 1) {
                traceViewHolder.rl_logistics.setPadding(0, TransformUtil.dip2px(context, 15), 0, 0);
            } else {
                traceViewHolder.rl_logistics.setPadding(0, 0, 0, 0);
            }

            if (position == 1) {
                params.width = TransformUtil.dip2px(context, 14);
                params.height = TransformUtil.dip2px(context, 14);
                traceViewHolder.miv_logistics.setLayoutParams(params);
                traceViewHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_active);
                traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
                traceViewHolder.tv_logistics_detail.setTextColor(getColor(R.color.pink_color));
            } else {
                params.width = TransformUtil.dip2px(context, 10);
                params.height = TransformUtil.dip2px(context, 10);
                traceViewHolder.miv_logistics.setLayoutParams(params);
                traceViewHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_before);
                traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
                traceViewHolder.tv_logistics_detail.setTextColor(getColor(R.color.new_text));
            }

            if (showMore) {
                traceViewHolder.tv_logistics_more.setVisibility(View.GONE);
                traceViewHolder.setVisibility(true);
            } else {
                if (getItemCount() == 2) {
                    traceViewHolder.setVisibility(true);
                    traceViewHolder.tv_logistics_more.setVisibility(View.GONE);
                    traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
                } else {
                    if (position == 1) {
                        traceViewHolder.setVisibility(true);
                        traceViewHolder.rl_logistics.setPadding(0, TransformUtil.dip2px(context, 15), 0, 0);
                        traceViewHolder.tv_logistics_more.setVisibility(View.VISIBLE);
                        traceViewHolder.view_logistics_line.setVisibility(View.VISIBLE);
                    } else {
                        traceViewHolder.setVisibility(false);
                    }
                }
            }

            traceViewHolder.tv_logistics_more.setOnClickListener(v -> {
                if (!showMore) {
                    setVisibility(true);
                }
            });
        }
    }

    public void handleHead(RecyclerView.ViewHolder holder) {
        HeadViewHolder headViewHolder = (HeadViewHolder) holder;
        GlideUtils.getInstance().loadImage(context, headViewHolder.miv_order_icon, orderLogisticsEntity.thumb);
        headViewHolder.tv_order_count.setText(orderLogisticsEntity.qty + "件商品");
        headViewHolder.tv_order_status.setText(orderLogisticsEntity.now_status.AcceptStation);
        headViewHolder.tv_order_logistics_company.setText(orderLogisticsEntity.express_com + "：");
        headViewHolder.tv_order_logistics_num.setText(orderLogisticsEntity.express_sn);
        headViewHolder.tv_order_official_phone.setText("官方电话：" + orderLogisticsEntity.express_phone);
    }

    public void handleFoot(RecyclerView.ViewHolder holder) {
        FootViewHolder footViewHolder = (FootViewHolder) holder;
        if (gridLayoutManager == null) {
            gridLayoutManager = new GridLayoutManager(context, 2);
            footViewHolder.recycler_footmark.addItemDecoration(gridSpacingItemDecoration);
        }
        footViewHolder.recycler_footmark.setNestedScrollingEnabled(false);
        footViewHolder.recycler_footmark.setLayoutManager(gridLayoutManager);
        footViewHolder.recycler_footmark.setAdapter(footMarkAdapter);
        footMarkAdapter.setOnItemClickListener((view, position) -> {
            OrderLogisticsEntity.FootMark footMark = footMarkList.get(position);
            GoodsDetailAct.startAct(context, footMark.goods_id);
        });
        footViewHolder.recycler_footmark.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager != null) {
                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                    LogUtil.httpLogW("lastPosition:" + lastPosition + "getItemCount():" + gridLayoutManager.getItemCount());
                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_VIEW) {
            return new HeadViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_order_title, parent, false));
        } else if (viewType == FOOT_VIEW) {
            return new FootViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_recycle_footmark, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case HEAD_VIEW:
                handleHead(holder);
                break;
            case FOOT_VIEW:
                handleFoot(holder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_VIEW;
        } else if (position == getItemCount() - 1) {
            return FOOT_VIEW;
        }
        return super.getItemViewType(position);
    }

    public void setVisibility(boolean visibility) {
        this.showMore = visibility;
        notifyDataSetChanged();
    }

    public class HeadViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_order_icon)
        MyImageView miv_order_icon;

        @BindView(R.id.tv_order_count)
        TextView tv_order_count;

        @BindView(R.id.tv_order_status)
        TextView tv_order_status;

        @BindView(R.id.tv_order_logistics_company)
        TextView tv_order_logistics_company;

        @BindView(R.id.tv_order_logistics_num)
        TextView tv_order_logistics_num;

        @BindView(R.id.tv_order_official_phone)
        TextView tv_order_official_phone;

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TraceViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_logistics)
        MyImageView miv_logistics;

        @BindView(R.id.view_logistics_line)
        View view_logistics_line;

        @BindView(R.id.tv_logistics_detail)
        TextView tv_logistics_detail;

        @BindView(R.id.tv_logistics_time)
        TextView tv_logistics_time;

        @BindView(R.id.rl_logistics)
        RelativeLayout rl_logistics;

        @BindView(R.id.tv_logistics_more)
        TextView tv_logistics_more;


        public TraceViewHolder(View itemView) {
            super(itemView);
        }

        public void setVisibility(boolean isVisible) {
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams) itemView.getLayoutParams();
            if (isVisible) {
                param.height = RelativeLayout.LayoutParams.WRAP_CONTENT;// 这里注意使用自己布局的根布局类型
                param.width = RelativeLayout.LayoutParams.MATCH_PARENT;// 这里注意使用自己布局的根布局类型
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
                param.height = 0;
                param.width = 0;
            }
            itemView.setLayoutParams(param);
        }
    }

    public class FootViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.recycler_footmark)
        RecyclerView recycler_footmark;

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }
}
