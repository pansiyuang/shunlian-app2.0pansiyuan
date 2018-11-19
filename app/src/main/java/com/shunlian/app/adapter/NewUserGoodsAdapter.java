package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CollectionGoodsEntity;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/22.
 */

public class NewUserGoodsAdapter extends BaseRecyclerAdapter<NewUserGoodsEntity.Goods> {


    private IAddShoppingCarListener mCarListener;
    public boolean isShowSelect;
    public String type;
    private boolean isNew;
    public NewUserGoodsAdapter(Context context, List<NewUserGoodsEntity.Goods> lists,String type,boolean isNew) {
        super(context, true, lists);
        this.type = type;
        this.isNew = isNew;
    }


    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_goods_new_user,
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
            NewUserGoodsEntity.Goods goods = lists.get(position);
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.miv_goods_pic, goods.thumb,220,220);
            mHolder.mtv_title.setText(goods.title);
            String source = getString(R.string.rmb).concat(goods.price);
            mHolder.mtv_price.setText(Common.changeTextSize(source, getString(R.string.rmb), 12));
            if(type.equals("1")){
                if(goods.is_add_cart==1) {
                    mHolder.tv_shopping_car.setEnabled(false);
                    mHolder.tv_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_da_50px);
                    mHolder.tv_shopping_car.setText("加入购物车");
                }else{
                    mHolder.tv_shopping_car.setEnabled(true);
                    mHolder.tv_shopping_car.setBackgroundResource(R.drawable.rounded_corner_solid_pink_50px);
                    mHolder.tv_shopping_car.setText("加入购物车");
                }
            }else if(type.equals("2")){
                if(isNew) {
                    mHolder.tv_shopping_car.setText("立即购买");
                }else{
                    mHolder.tv_shopping_car.setText("立即分享");
                }
            }
            mHolder.mtv_discount_price.setText(goods.marker_price);
            if(position==0&&type.equals("1")){
                if(isNew) {
                    mHolder.tv_usew_desc.setVisibility(View.VISIBLE);
                    mHolder.tv_usew_desc.setText("0元选3件仅当前页面加入购物车有效");
                }else{
                    mHolder.tv_usew_desc.setVisibility(View.GONE);
                }
            }else{
                mHolder.tv_usew_desc.setVisibility(View.GONE);
            }
        }
    }


    public class CollectionGoodsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_discount_price)
        MyTextView mtv_discount_price;

        @BindView(R.id.tv_shopping_car)
        MyTextView tv_shopping_car;

        @BindView(R.id.mrlayout_item)
        MyRelativeLayout mrlayout_item;

        @BindView(R.id.tv_usew_desc)
        TextView tv_usew_desc;

        public CollectionGoodsHolder(View itemView) {
            super(itemView);
            mrlayout_item.setOnClickListener(this);
            tv_shopping_car.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tv_shopping_car:
                    if (mCarListener != null){
                        mCarListener.onGoodsId(v,getAdapterPosition());
                    }
                    break;
                case R.id.mrlayout_item:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
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



    public interface IAddShoppingCarListener{
        void onGoodsId(View view, int position);
    }

}
