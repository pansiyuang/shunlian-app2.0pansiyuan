package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class AddressAdapter extends BaseRecyclerAdapter<ConfirmOrderEntity.Address> {
    private List<ConfirmOrderEntity.Address> addressList;

    public AddressAdapter(Context context, boolean isShowFooter, List<ConfirmOrderEntity.Address> lists) {
        super(context, isShowFooter, lists);
        this.addressList = lists;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_select_address, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ConfirmOrderEntity.Address address = addressList.get(position);
        viewHolder.tv_address_name.setText(address.realname);
        viewHolder.tv_address_phone.setText(address.mobile);
        viewHolder.tv_address_detail.setText(address.detail_address);
        if ("1".equals(address.isdefault)) {
            viewHolder.iv_address_select.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            viewHolder.iv_address_select.setImageDrawable(context.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        }
    }

    public class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_address_name)
        TextView tv_address_name;

        @BindView(R.id.tv_address_phone)
        TextView tv_address_phone;

        @BindView(R.id.tv_address_detail)
        TextView tv_address_detail;

        @BindView(R.id.iv_address_select)
        MyImageView iv_address_select;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
