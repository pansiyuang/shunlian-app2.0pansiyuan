package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/11/22.
 */

public class ShopCarStoreAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<ShoppingCarEntity.Enabled> mStores;
    private EnableGoodsAdapter goodsAdapter;
    private LinearLayoutManager linearLayoutManager;

    public ShopCarStoreAdapter(Context context, List<ShoppingCarEntity.Enabled> enableds) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.mStores = enableds;
    }

    //  获得父类的
    @Override
    public int getGroupCount() {
        return mStores.size();
    }

    //  获得父项的数量
    @Override
    public int getChildrenCount(int i) {
        return mStores.get(i).promotion.size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int i) {
        return mStores.get(i);
    }

    //  获得某个父项的某个子项的id
    @Override
    public Object getChild(int i, int i1) {
        return mStores.get(i).promotion.get(i1);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int i) {
        return i;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        final ParentViewHolder parentViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_shoppingcar_store, viewGroup, false);
            parentViewHolder = new ParentViewHolder(view);
            view.setTag(parentViewHolder);
        } else {
            parentViewHolder = (ParentViewHolder) view.getTag();
        }

        final ShoppingCarEntity.Enabled enabled = mStores.get(i);
        if ("1".equals(enabled.all_check)) {
            parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        } else {
            parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        }
        parentViewHolder.tv_store.setText(enabled.store_name);

        parentViewHolder.tv_get_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //领劵
            }
        });
        parentViewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = mContext.getResources().getString(R.string.edit);
                if (!enabled.isEdit) { //完成
                    parentViewHolder.tv_edit.setText(mContext.getResources().getString(R.string.RegisterTwoAct_finish));
                    enabled.isEdit = true;
                } else { //编辑
                    parentViewHolder.tv_edit.setText(str);
                    enabled.isEdit = false;
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_shoppingcar_promotion, viewGroup, false);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }
        ShoppingCarEntity.Enabled.Promotion promotion = mStores.get(i).promotion.get(i1);
        childViewHolder.tv_full_min.setText(promotion.prom_label);
        childViewHolder.tv_content.setText(promotion.hint);
        childViewHolder.tv_prefer.setText(promotion.title_label);
        childViewHolder.tv_discount.setText(promotion.prom_title);

        goodsAdapter = new EnableGoodsAdapter(mContext, false, promotion.goods);
        linearLayoutManager = new LinearLayoutManager(mContext);
        childViewHolder.recycler_goods.setLayoutManager(linearLayoutManager);
        childViewHolder.recycler_goods.setNestedScrollingEnabled(false);
        childViewHolder.recycler_goods.setAdapter(goodsAdapter);

        goodsAdapter.setEdit(mStores.get(i).isEdit);
        return view;
    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public class ParentViewHolder {
        @BindView(R.id.miv_store_select)
        MyImageView miv_store_select;

        @BindView(R.id.tv_store)
        TextView tv_store;

        @BindView(R.id.tv_get_voucher)
        TextView tv_get_voucher;

        @BindView(R.id.tv_edit)
        TextView tv_edit;

        public ParentViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public class ChildViewHolder {
        @BindView(R.id.rl_merge)
        RelativeLayout rl_merge;

        @BindView(R.id.tv_full_min)
        TextView tv_full_min;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.recycler_goods)
        RecyclerView recycler_goods;

        @BindView(R.id.tv_prefer)
        TextView tv_prefer;

        @BindView(R.id.tv_discount)
        TextView tv_discount;

        @BindView(R.id.rl_prefer)
        RelativeLayout rl_prefer;

        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
