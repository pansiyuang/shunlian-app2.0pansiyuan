package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionStoresEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class CollectionStoresAdapter extends BaseRecyclerAdapter<CollectionStoresEntity.Store> {
    public boolean isShowSelect;
    private IDelCollectionStoresListener mDelListener;

    public CollectionStoresAdapter(Context context, List<CollectionStoresEntity.Store> lists) {
        super(context, true, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stores_collection,
                parent, false);
        return new CollectionStoresHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setText(getString(R.string.i_have_a_bottom_line));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectionStoresHolder) {
            CollectionStoresHolder mHolder = (CollectionStoresHolder) holder;
            final CollectionStoresEntity.Store store = lists.get(position);
            GlideUtils.getInstance().loadImage(context, mHolder.miv_goods_pic, store.store_avatar);
            mHolder.mtv_title.setText(store.store_name);
            mHolder.mtv_nice.setText(getString(R.string.collection_haopinglv)+store.nice_rate);
            if (isShowSelect){
                mHolder.swipeMenuLayout.setSwipeEnable(false);
                mHolder.miv_select.setVisibility(View.VISIBLE);
            }else {
                mHolder.swipeMenuLayout.setSwipeEnable(true);
                mHolder.miv_select.setVisibility(View.GONE);
            }
            mHolder.mrlayout_new.setVisibility(View.VISIBLE);
            mHolder.miv_arrow.setVisibility(View.VISIBLE);
            mHolder.mtv_add.setVisibility(View.GONE);
            String status = store.status;
            if ("0".equals(status)){//失效
                mHolder.mtv_expired.setVisibility(View.VISIBLE);
                mHolder.mtv_title.setTextColor(getColor(R.color.light_gray_three));
                mHolder.mtv_nice.setTextColor(getColor(R.color.light_gray_three));
                mHolder.mrlayout_new.setVisibility(View.GONE);
                mHolder.miv_arrow.setVisibility(View.GONE);
            }else {
                mHolder.mtv_expired.setVisibility(View.GONE);
                mHolder.mtv_title.setTextColor(getColor(R.color.new_text));
                mHolder.mtv_nice.setTextColor(getColor(R.color.new_text));
            }
            if (store.isSelect){
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            }else {
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            }
            if (Integer.parseInt(store.new_count)>9){
                mHolder.mtv_new.setText(getString(R.string.collection_shangxin)+" 9");
                mHolder.mtv_add.setVisibility(View.VISIBLE);
                StoreGoodsAdapter storeGoodsAdapter=new StoreGoodsAdapter(context,false,store.new_goods,store.store_id,true);
                mHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                mHolder.rv_goods.setAdapter(storeGoodsAdapter);
                storeGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        GoodsDetailAct.startAct(context,store.new_goods.get(position).id);
                    }
                });
            }else if (store.new_goods.size()<=0){
                mHolder.mrlayout_new.setVisibility(View.GONE);
                mHolder.miv_arrow.setVisibility(View.GONE);
            }else {
                mHolder.mtv_new.setText(getString(R.string.collection_shangxin)+" "+store.new_count);
                StoreGoodsAdapter storeGoodsAdapter=new StoreGoodsAdapter(context,false,store.new_goods,store.store_id,false);
                mHolder.rv_goods.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                mHolder.rv_goods.setAdapter(storeGoodsAdapter);
                storeGoodsAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        GoodsDetailAct.startAct(context,store.new_goods.get(position).id);
                    }
                });
            }
        }
    }

    public class CollectionStoresHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_expired)
        MyTextView mtv_expired;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_nice)
        MyTextView mtv_nice;

        @BindView(R.id.mtv_new)
        MyTextView mtv_new;

        @BindView(R.id.mtv_add)
        MyTextView mtv_add;

        @BindView(R.id.miv_arrow)
        MyImageView miv_arrow;

        @BindView(R.id.mtv_cancel_collection)
        MyTextView mtv_cancel_collection;

        @BindView(R.id.rv_goods)
        RecyclerView rv_goods;

        @BindView(R.id.mrlayout_item)
        MyRelativeLayout mrlayout_item;

        @BindView(R.id.mrlayout_new)
        MyRelativeLayout mrlayout_new;

        @BindView(R.id.smlayout_swipe)
        SwipeMenuLayout swipeMenuLayout;

        private boolean isShow=false;

        public CollectionStoresHolder(View itemView) {
            super(itemView);
            mtv_cancel_collection.setWHProportion(175,140);
            mtv_cancel_collection.setOnClickListener(this);
            mrlayout_new.setOnClickListener(this);
            mrlayout_item.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mrlayout_new:
                    if (isShow){
                        rv_goods.setVisibility(View.GONE);
                        miv_arrow.setImageResource(R.mipmap.icon_saixuan_gd);
                        isShow=false;
                    }else {
                        rv_goods.setVisibility(View.VISIBLE);
                        miv_arrow.setImageResource(R.mipmap.icon_saixuan_sq);
                        isShow=true;
                    }
                    break;
                case R.id.mtv_cancel_collection:
                    swipeMenuLayout.quickClose();
                    if (mDelListener != null){
                        mDelListener.onDelStores(lists.get(getAdapterPosition()));
                    }
                    break;
                default:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
            }
        }

    }

    /**
     * 删除店铺
     * @param delListener
     */
    public void setDelCollectionStoresListener(IDelCollectionStoresListener delListener){
        mDelListener = delListener;
    }

    public interface IDelCollectionStoresListener{
        void onDelStores(CollectionStoresEntity.Store store);
    }
}
