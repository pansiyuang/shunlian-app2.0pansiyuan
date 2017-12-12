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
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShoppingCarEntity;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.RecyclerDialog;

import java.util.HashMap;
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
    private OnEnableChangeListener mListener;
    private HashMap<String, Boolean> mMap;
    private RecyclerDialog recyclerDialog;

    public ShopCarStoreAdapter(Context context, List<ShoppingCarEntity.Enabled> enableds) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(mContext);
        this.mStores = enableds;
    }

    public void setEnables(List<ShoppingCarEntity.Enabled> datas, HashMap<String, Boolean> mLink) {
        this.mStores = datas;
        this.mMap = mLink;
    }

    //  获得父类的
    @Override
    public int getGroupCount() {
        if (mStores == null || mStores.size() == 0) {
            return 0;
        }
        return mStores.size();
    }

    //  获得父项的数量
    @Override
    public int getChildrenCount(int i) {
        if (mStores.get(i).promotion == null || mStores.get(i).promotion.size() == 0) {
            return 0;
        }
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

        if (i == 0) {//第0项不显示分割线
            parentViewHolder.view_line.setVisibility(View.GONE);
        } else {
            parentViewHolder.view_line.setVisibility(View.VISIBLE);
        }

        final ShoppingCarEntity.Enabled enabled = mStores.get(i);
        if ("1".equals(enabled.all_check)) {
            parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        }
        parentViewHolder.tv_store.setText(enabled.store_name);

        parentViewHolder.tv_get_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //领劵
            }
        });
        if (mMap != null && mMap.size() != 0 && mMap.containsKey(enabled.store_id)) {
            enabled.isEditGood = mMap.get(enabled.store_id);
        }
        if (enabled.isEditAll) {
            parentViewHolder.tv_edit.setVisibility(View.GONE);
        } else {
            parentViewHolder.tv_edit.setVisibility(View.VISIBLE);
        }

        if (!enabled.isEditGood) { //完成
            parentViewHolder.tv_edit.setText(mContext.getResources().getString(R.string.edit));
        } else { //编辑
            parentViewHolder.tv_edit.setText(mContext.getResources().getString(R.string.RegisterTwoAct_finish));
        }

        parentViewHolder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enabled.isEditGood) { //完成
                    parentViewHolder.tv_edit.setText(mContext.getResources().getString(R.string.RegisterTwoAct_finish));
                    enabled.isEditGood = true;
                } else { //编辑
                    parentViewHolder.tv_edit.setText(mContext.getResources().getString(R.string.edit));
                    enabled.isEditGood = false;
                }
                if (mListener != null) {
                    mListener.OnChangeEdit(enabled.store_id, enabled.isEditGood);
                }
            }
        });

        parentViewHolder.miv_store_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(enabled.all_check)) {
                    enabled.all_check = "0";
                    parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
                } else {
                    enabled.all_check = "1";
                    parentViewHolder.miv_store_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
                }
                if (mListener != null) {
                    mListener.OnStoreCheck(enabled.store_id, enabled.all_check);
                }
            }
        });
        parentViewHolder.tv_get_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerDialog == null) {
                    recyclerDialog = new RecyclerDialog(mContext);
                }
                recyclerDialog.setVoucheres(enabled.store_voucher);
                recyclerDialog.setOnVoucherCallBack(new RecyclerDialog.OnVoucherCallBack() {
                    @Override
                    public void OnVoucherSelect(GoodsDeatilEntity.Voucher voucher) {
                        if (mListener != null) {
                            mListener.OnVoucherSelect(voucher);
                        }
                    }
                });
                recyclerDialog.show();
            }
        });
        return view;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder childViewHolder;
        ShoppingCarEntity.Enabled enabled = mStores.get(i);
        final ShoppingCarEntity.Enabled.Promotion promotion = enabled.promotion.get(i1);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.item_shoppingcar_promotion, viewGroup, false);
            childViewHolder = new ChildViewHolder(view);
            view.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) view.getTag();
        }

        childViewHolder.tv_full_min.setText(promotion.prom_label);
        childViewHolder.tv_content.setText(promotion.hint);

        goodsAdapter = new EnableGoodsAdapter(mContext, false, promotion.goods, promotion);
        linearLayoutManager = new LinearLayoutManager(mContext);
        childViewHolder.recycler_goods.setLayoutManager(linearLayoutManager);
        childViewHolder.recycler_goods.setNestedScrollingEnabled(false);
        childViewHolder.recycler_goods.setAdapter(goodsAdapter);
        childViewHolder.tv_meger_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.OnMegerOrder(promotion.need_more);
                }
            }
        });
        goodsAdapter.setOnGoodsChangeListener(new EnableGoodsAdapter.OnGoodsChangeListener() {
            @Override
            public void OnChangeCount(String goodsId, int count) {
                if (mListener != null) {
                    mListener.OnChangeCount(goodsId, count);
                }
            }

            @Override
            public void OnChangeSku(String goodsId, String skuId) {
                if (mListener != null) {
                    mListener.OnChangeSku(goodsId, skuId);
                }
            }

            @Override
            public void OnChangeCheck(String goodsId, String isCheck) {
                if (mListener != null) {
                    mListener.OnChangeCheck(goodsId, isCheck);
                }
            }

            @Override
            public void OnChangePromotion(String goods, String promoId) {
                if (mListener != null) {
                    mListener.OnChangePromotion(goods, promoId);
                }
            }

            @Override
            public void OnGoodsDel(String goodsId) {
                if (mListener != null) {
                    mListener.OnGoodsDel(goodsId);
                }
            }
        });
        if (mMap != null && mMap.size() != 0 && mMap.containsKey(enabled.store_id)) {
            enabled.isEditGood = mMap.get(enabled.store_id);
        }

        goodsAdapter.setEdit(enabled.isEditGood);
        goodsAdapter.setEditAll(enabled.isEditAll);
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

        @BindView(R.id.view_line)
        View view_line;

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

        @BindView(R.id.tv_meger_order)
        TextView tv_meger_order;


        public ChildViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnEnableChangeListener(OnEnableChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnEnableChangeListener {

        void OnChangeCount(String goodsId, int count);

        void OnChangeSku(String goodsId, String skuId);

        void OnChangeCheck(String goodsId, String isCheck);

        void OnChangeEdit(String storeId, boolean isEdit);

        void OnGoodsDel(String goodsId);

        void OnVoucherSelect(GoodsDeatilEntity.Voucher voucher);

        void OnChangePromotion(String goodsId, String promoId);

        void OnStoreCheck(String storeId, String isSelect);

        void OnMegerOrder(String needId);
    }
}
