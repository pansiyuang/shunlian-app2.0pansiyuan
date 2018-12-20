package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class CollectionGoodsAdapter extends BaseRecyclerAdapter<CollectionGoodsEntity.Goods> {


    private IAddShoppingCarListener mCarListener;
    public boolean isShowSelect;
    private IDelCollectionGoodsListener mDelListener;

    public CollectionGoodsAdapter(Context context,List<CollectionGoodsEntity.Goods> lists) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_collection,
                parent, false);
        return new CollectionGoodsHolder(view);
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

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof CollectionGoodsHolder){
            CollectionGoodsHolder mHolder = (CollectionGoodsHolder) holder;
            CollectionGoodsEntity.Goods goods = null;
            if (payloads.get(0) instanceof CollectionGoodsEntity.Goods){
                goods = (CollectionGoodsEntity.Goods) payloads.get(0);
            }else {
                List<CollectionGoodsEntity.Goods>
                        goodsList = (List<CollectionGoodsEntity.Goods>) payloads.get(0);
                goods = goodsList.get(position);
            }
            if (goods.isSelect){
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            }else {
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            }
        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectionGoodsHolder) {
            CollectionGoodsHolder mHolder = (CollectionGoodsHolder) holder;
            CollectionGoodsEntity.Goods goods = lists.get(position);
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.miv_goods_pic, goods.thumb,220,220);
            mHolder.mtv_title.setText(goods.title);
            String source = getString(R.string.rmb).concat(goods.price);
            mHolder.mtv_price.setText(Common.changeTextSize(source, getString(R.string.rmb), 12));
            if ("1".equals(goods.free_shipping)) {
                visible(mHolder.mtv_shippingFree);
            } else {
                gone(mHolder.mtv_shippingFree);
            }
            mHolder.mtv_discount_price.setText(goods.max_dis);
            String favo_num = goods.favo_num;
            if (isEmpty(favo_num)) {
                gone(mHolder.mtv_collection_count);
            } else {
                visible(mHolder.mtv_collection_count);
                mHolder.mtv_collection_count.setText(favo_num.concat("人收藏"));
            }

            mHolder.mtv_voucher.setVisibility("1".equals(goods.has_coupon)?View.VISIBLE : View.GONE);
            mHolder.mtv_fold.setVisibility("1".equals(goods.has_discount)?View.VISIBLE : View.GONE);
            mHolder.mtv_gift.setVisibility("1".equals(goods.has_gift)?View.VISIBLE : View.GONE);

            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(mHolder.miv_shopping_car, i, i, i, i);

            String status = goods.status;
            if ("0".equals(status)){//失效
                visible(mHolder.mtv_expired);
                gone(mHolder.miv_shopping_car,
                        mHolder.mtv_shippingFree,
                        mHolder.mtv_discount_price,
                        mHolder.mtv_collection_count,
                        mHolder.mrlayout_discount_title);
                mHolder.mtv_title.setTextColor(getColor(R.color.color_value_6c));
                mHolder.mtv_price.setTextColor(getColor(R.color.color_value_6c));
            }else {
                gone(mHolder.mtv_expired);
                visible(mHolder.miv_shopping_car,
                        mHolder.mtv_shippingFree,
                        mHolder.mtv_discount_price,
                        mHolder.mtv_collection_count,
                        mHolder.mrlayout_discount_title);
                mHolder.mtv_title.setTextColor(getColor(R.color.new_text));
                mHolder.mrlayout_discount_title.setVisibility(View.VISIBLE);
                mHolder.mtv_price.setTextColor(getColor(R.color.pink_color));
            }
            if (isShowSelect){
                mHolder.swipeMenuLayout.setSwipeEnable(false);
                visible(mHolder.miv_select);
            }else {
                mHolder.swipeMenuLayout.setSwipeEnable(true);
                gone(mHolder.miv_select);
            }
            /*if (goods.isSelect){
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            }else {
                mHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            }*/
        }
    }

    public class CollectionGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_expired)
        MyTextView mtv_expired;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_shippingFree)
        MyTextView mtv_shippingFree;

        @BindView(R.id.mtv_discount_price)
        MyTextView mtv_discount_price;

        @BindView(R.id.mtv_collection_count)
        MyTextView mtv_collection_count;

        @BindView(R.id.mtv_cancel_collection)
        MyTextView mtv_cancel_collection;

        @BindView(R.id.mtv_voucher)
        MyTextView mtv_voucher;

        @BindView(R.id.mtv_fold)
        MyTextView mtv_fold;

        @BindView(R.id.mtv_gift)
        MyTextView mtv_gift;

        @BindView(R.id.miv_shopping_car)
        MyImageView miv_shopping_car;

        @BindView(R.id.mrlayout_item)
        MyRelativeLayout mrlayout_item;

        @BindView(R.id.mrlayout_discount_title)
        MyLinearLayout mrlayout_discount_title;

        @BindView(R.id.mtv_earn_much)
        MyTextView mtv_earn_much;
        private final SwipeMenuLayout swipeMenuLayout;

        public CollectionGoodsHolder(View itemView) {
            super(itemView);
            swipeMenuLayout = (SwipeMenuLayout) itemView;
            int strokeWidth = TransformUtil.dip2px(context, 0.5f);
            GradientDrawable background = (GradientDrawable) mtv_shippingFree.getBackground();
            background.setColor(getColor(R.color.white));
            background.setStroke(strokeWidth,getColor(R.color.value_c9));

            GradientDrawable gVoucher = (GradientDrawable) mtv_voucher.getBackground();
            gVoucher.setColor(getColor(R.color.white));
            gVoucher.setStroke(strokeWidth,getColor(R.color.value_f46c6f));

            GradientDrawable gFold = (GradientDrawable) mtv_fold.getBackground();
            gFold.setColor(getColor(R.color.white));
            gFold.setStroke(strokeWidth,getColor(R.color.value_f46c6f));

            GradientDrawable gGift = (GradientDrawable) mtv_gift.getBackground();
            gGift.setColor(getColor(R.color.white));
            gGift.setStroke(strokeWidth,getColor(R.color.value_f46c6f));

            mrlayout_item.setOnClickListener(this);
            miv_shopping_car.setOnClickListener(this);
            mtv_cancel_collection.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.miv_shopping_car:
                    if (mCarListener != null && !isShowSelect){
                        mCarListener.onGoodsId(v,getAdapterPosition());
                    }
                    if (isShowSelect){
                        if (listener != null){
                            listener.onItemClick(v,getAdapterPosition());
                        }
                    }
                    break;
                case R.id.mrlayout_item:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
                case R.id.mtv_cancel_collection:
                    swipeMenuLayout.quickClose();
                    if (mDelListener != null){
                        mDelListener.onDelGoods(lists.get(getAdapterPosition()));
                    }
                    break;
            }
        }


    }

    /**
     * 添加购物车
     * @param carListener
     */
    public void setAddShoppingCarListener(IAddShoppingCarListener carListener){
        mCarListener = carListener;
    }

    /**
     * 删除商品
     * @param delListener
     */
    public void setDelCollectionGoodsListener(IDelCollectionGoodsListener delListener){
        mDelListener = delListener;
    }



    public interface IAddShoppingCarListener{
        void onGoodsId(View view,int position);
    }

    public interface IDelCollectionGoodsListener{
        void onDelGoods(CollectionGoodsEntity.Goods goods);
    }
}
