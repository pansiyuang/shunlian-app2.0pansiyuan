package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreCategoriesEntity;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.ui.store.StoreSearchAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreSortAdapter extends BaseRecyclerAdapter<StoreCategoriesEntity.MData> {
    private static final int TYPE2 = 2;//单品
    private static final int TYPE3 = 3;//多品
    private Activity context;
    private List<StoreCategoriesEntity.MData> datas;
    private OnItemClickListener mListener;
    private String storeId;

    public StoreSortAdapter(Context context, boolean isShowFooter, List<StoreCategoriesEntity.MData> datas,String storeId) {
        super(context, isShowFooter, datas);
        this.context = (Activity) context;
        this.datas = datas;
        this.storeId = storeId;
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
                    twoHolder.mrlayout_root.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StoreSearchAct.startAct(context, storeId,data.id,"",data.name);
                            context.finish();
                        }
                    });
                }
                break;
            case TYPE3:
                if (holder instanceof ThreeHolder) {
                    ThreeHolder threeHolder = (ThreeHolder) holder;
                    StoreCategoriesEntity.MData data = datas.get(position);
                    threeHolder.mtv_name.setText(data.name);
                    GridLayoutManager babyManager = new GridLayoutManager(context, 2);
                    threeHolder.rv_sort.setLayoutManager(babyManager);
//                    threeHolder.rv_sort.addItemDecoration(new GrideItemDecoration(0, 0, TransformUtil.dip2px(context, 5), TransformUtil.dip2px(context, 5), false));
                    threeHolder.rv_sort.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context, 5),false));
                    StoreSortAdapters storeSortAdapters = new StoreSortAdapters(context, false, data.children);
                    threeHolder.rv_sort.setAdapter(storeSortAdapters);
                    storeSortAdapters.setOnItemClickListener((view, position1) -> {
                        if (mListener != null) {
                            mListener.OnItemClick(view, position, position1);
                        }
                    });
                    threeHolder.mrlayout_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            StoreSearchAct.startAct(context, storeId,data.id,"",data.name);
                            context.finish();
                        }
                    });
                }
                break;
        }
    }

    class TwoHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_name;
        private MyRelativeLayout mrlayout_root;

        TwoHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
            mrlayout_root = (MyRelativeLayout) itemView.findViewById(R.id.mrlayout_root);
        }
    }

    class ThreeHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_name;
        private MyRelativeLayout mrlayout_all;
        private RecyclerView rv_sort;

        ThreeHolder(View itemView) {
            super(itemView);
            mtv_name = (MyTextView) itemView.findViewById(R.id.mtv_name);
            rv_sort = (RecyclerView) itemView.findViewById(R.id.rv_sort);
            mrlayout_all = (MyRelativeLayout) itemView.findViewById(R.id.mrlayout_all);
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
