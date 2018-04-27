package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreSortAdapter extends BaseRecyclerAdapter<StoreCategoriesEntity.MData> {
    private static final int TYPE2 = 2;//单品
    private static final int TYPE3 = 3;//多品
    private Context context;
    private List<StoreCategoriesEntity.MData> datas;
    private OnItemClickListener mListener;

    public StoreSortAdapter(Context context, boolean isShowFooter, List<StoreCategoriesEntity.MData> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE2:
                return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_sort_two, parent, false));
            case TYPE3:
                return new ThreeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_sort_three, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (datas.get(position).children != null && datas.get(position).children.size() > 0) {
            return TYPE3;
        } else {
            return TYPE2;
        }
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DefaultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.block_store_first_default, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE2:
                if (holder instanceof TwoHolder) {
                    TwoHolder twoHolder = (TwoHolder) holder;
                    StoreCategoriesEntity.MData data = datas.get(position);
                    twoHolder.mtv_name.setText(data.name);
                }
                break;
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;
                    StoreCategoriesEntity.MData data = datas.get(position);
                    threeHolder.mtv_name.setText(data.name);
                    GridLayoutManager babyManager = new GridLayoutManager(context, 2);
                    threeHolder.rv_sort.setLayoutManager(babyManager);
                    threeHolder.rv_sort.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(context, 5), TransformUtil.dip2px(context, 5), false));
                    StoreSortAdapters storeSortAdapters = new StoreSortAdapters(context, false, data.children);
                    threeHolder.rv_sort.setAdapter(storeSortAdapters);
                    storeSortAdapters.setOnItemClickListener((view, position1) -> {
                        LogUtil.httpLogW("222222：" + position);
                        if (mListener != null) {
                            LogUtil.httpLogW("3333333：" + position);
                            mListener.OnItemClick(view, position, position1);
                        }
                    });
                }
                break;
        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_name;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
        }
    }

    class ThreeHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_name;
        private RecyclerView rv_sort;

        ThreeHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
            rv_sort = (RecyclerView) itemView.findViewById(R.id.rv_sort);
        }
    }


    class DefaultHolder extends RecyclerView.ViewHolder {

        DefaultHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void OnItemClick(View view, int parentPosition, int childPosition);
    }
}
