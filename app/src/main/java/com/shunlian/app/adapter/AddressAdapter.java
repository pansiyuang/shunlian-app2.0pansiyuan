package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.utils.CenterAlignImageSpan;
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

    public void OnItemSelect(String addressId) {
        for (int i = 0; i < addressList.size(); i++) {
            if (addressId.equals(addressList.get(i).id)) {
                addressList.get(i).isSelect = true;
            } else {
                addressList.get(i).isSelect = false;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ConfirmOrderEntity.Address address = addressList.get(position);
        viewHolder.tv_address_name.setText(address.realname);
        viewHolder.tv_address_phone.setText(address.mobile);
        if ("1".equals(address.isdefault)) {
            addImageSpan(true, ((ViewHolder) holder).tv_address_detail, address.detail_address);
        } else {
            addImageSpan(false, ((ViewHolder) holder).tv_address_detail, address.detail_address);
        }
        if (address.isSelect) {
            viewHolder.iv_address_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        } else {
            viewHolder.iv_address_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }
    }

    /**
     * 图片
     */
    private void addImageSpan(boolean isDefault, TextView textView, String address) {
        textView.setText("");
        if (isDefault) {
            SpannableString spanString = new SpannableString("图 " + address);
            Drawable d = getDrawable(R.mipmap.img_address_address_n);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            CenterAlignImageSpan span = new CenterAlignImageSpan(d);
            spanString.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.append(spanString);
        } else {
            textView.setText(address);
        }
    }

    public class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
