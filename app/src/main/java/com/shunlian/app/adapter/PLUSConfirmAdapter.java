package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.PLUSConfirmEntity;
import com.shunlian.app.ui.receive_adress.AddressListActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/25.
 */

public class PLUSConfirmAdapter extends BaseRecyclerAdapter<PLUSConfirmEntity.ProductBean> {

    public static final int ITEM_ADDRESS = 2;//地址条目
    private PLUSConfirmEntity.AddressBean mAddress;
    private final List<GoodsDeatilEntity.Goods> mGoods;

    public PLUSConfirmAdapter(Context context,
                              List<PLUSConfirmEntity.ProductBean> lists,
                              PLUSConfirmEntity.AddressBean address) {
        super(context, false, lists);
        mAddress = address;
        mGoods = new ArrayList<>();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return ITEM_ADDRESS;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ITEM_ADDRESS:
                View head_address = LayoutInflater.from(context)
                        .inflate(R.layout.head_address, parent, false);
                return new AddressHolder(head_address);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case ITEM_ADDRESS:
                handleAddress(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handleAddress(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddressHolder){
            AddressHolder mHolder = (AddressHolder) holder;
            if (mAddress == null){
                mHolder.mtv_add_address.setVisibility(View.VISIBLE);
                mHolder.mtv_address.setVisibility(View.GONE);
                mHolder.mtv_nickname.setVisibility(View.GONE);
                mHolder.mtv_phone.setVisibility(View.GONE);
            }else {
                mHolder.mtv_add_address.setVisibility(View.GONE);
                mHolder.mtv_address.setVisibility(View.VISIBLE);
                mHolder.mtv_nickname.setVisibility(View.VISIBLE);
                mHolder.mtv_phone.setVisibility(View.VISIBLE);
                mHolder.mtv_nickname.setText(mAddress.realname);
                mHolder.mtv_phone.setText(mAddress.mobile);
                mHolder.mtv_address.setText(String.format(getString(R.string.address),mAddress.detail_address));
            }
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View confirm_order = LayoutInflater.from(context)
                .inflate(R.layout.item_confirm_order, parent, false);
        return new BuyGoodsHolder(confirm_order);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BuyGoodsHolder){
            BuyGoodsHolder mHolder = (BuyGoodsHolder) holder;
            PLUSConfirmEntity.ProductBean enabled = lists.get(position - 1);
            gone(mHolder.mtv_store_name,mHolder.mllayout_discount,
                    mHolder.mll_promotion,mHolder.line_activity);
            String shippingFee = enabled.shipping_fee;
            if ("0".equals(shippingFee)){
                mHolder.mtv_shippingFree.setText(getString(R.string.free_shipping));
            }else {
                mHolder.mtv_shippingFree.setText(String.format(getString(R.string.express),shippingFee));
            }

            mGoods.clear();
            GoodsDeatilEntity.Goods g = new GoodsDeatilEntity.Goods();
            g.thumb = enabled.thumb;
            g.title = enabled.title;
            g.price = enabled.price;
            g.qty = enabled.qty;
            g.sku = enabled.sku;
            mGoods.add(g);

            mHolder.recy_view.setVisibility(View.VISIBLE);
            mHolder.recy_view.setAdapter(new AppointGoodsAdapter(context,
                    false, mGoods));


            mHolder.mtv_goods_price.setText(Common.dotAfterSmall(getString(R.string.rmb)
                    .concat(Common.formatFloat(enabled.price)),11));

            mHolder.mtv_goods_count.setText(String.format(getString(R.string.all_goods),enabled.qty));
        }
    }

    public class AddressHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_nickname)
        MyTextView mtv_nickname;

        @BindView(R.id.mtv_phone)
        MyTextView mtv_phone;

        @BindView(R.id.mtv_address)
        MyTextView mtv_address;

        @BindView(R.id.mtv_add_address)
        MyTextView mtv_add_address;

        @BindView(R.id.view_line)
        View view_line;

        public AddressHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (mAddress != null)
                AddressListActivity.startAct(context,mAddress.id);
            else
                AddressListActivity.startAct(context,null);
        }
    }


    public class BuyGoodsHolder extends BaseRecyclerViewHolder
            implements View.OnClickListener {

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mllayout_discount)
        MyLinearLayout mllayout_discount;

        @BindView(R.id.met_leav_msg)
        MyEditText met_leav_msg;

        @BindView(R.id.mtv_store_name)
        MyTextView mtv_store_name;

        @BindView(R.id.mtv_shippingFree)
        MyTextView mtv_shippingFree;

        @BindView(R.id.mtv_discount)
        MyTextView mtv_discount;

        @BindView(R.id.mll_promotion)
        MyLinearLayout mll_promotion;

        @BindView(R.id.mtv_promotion)
        MyTextView mtv_promotion;

        @BindView(R.id.mtv_goods_count)
        MyTextView mtv_goods_count;

        @BindView(R.id.mtv_goods_price)
        MyTextView mtv_goods_price;

        @BindView(R.id.line_activity)
        View line_activity;


        public BuyGoodsHolder(View itemView) {
            super(itemView);
            gone(met_leav_msg);
            recy_view.setNestedScrollingEnabled(false);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recy_view.setLayoutManager(manager);
            int space = TransformUtil.dip2px(context, 20);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,
                    space / 2,0));
            recy_view.setFocusable(false);

            mllayout_discount.setOnClickListener(this);
            mll_promotion.setOnClickListener(this);
        }
    }
}
