package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class AddressManageAdapter extends BaseRecyclerAdapter<ConfirmOrderEntity.Address> {

    public AddressManageAdapter(Context context, boolean isShowFooter, List<ConfirmOrderEntity.Address> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address_manage, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final ConfirmOrderEntity.Address address = lists.get(position);
        viewHolder.tv_manage_name.setText(address.realname);
        viewHolder.tv_manage_phone.setText(address.mobile);
        viewHolder.tv_manage_detail.setText(address.detail_address);

        if ("1".equals(address.isdefault)) {
            viewHolder.tv_manage_default.setText(getString(R.string.address_default));
            viewHolder.tv_manage_default.setTextColor(getColor(R.color.pink_color));
            viewHolder.iv_manage_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        } else {
            viewHolder.tv_manage_default.setText(getString(R.string.address_sheweimoren));
            viewHolder.tv_manage_default.setTextColor(getColor(R.color.new_text));
            viewHolder.iv_manage_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }

        viewHolder.ll_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < lists.size(); i++) {
                    lists.get(i).isdefault = "0";
                }
                lists.get(position).isdefault = "1";
                notifyDataSetChanged();
            }
        });
        viewHolder.tv_manage_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //编辑
            }
        });
        viewHolder.tv_manage_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除
            }
        });
    }

    public class ViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_manage_name)
        TextView tv_manage_name;

        @BindView(R.id.tv_manage_phone)
        TextView tv_manage_phone;

        @BindView(R.id.tv_manage_detail)
        TextView tv_manage_detail;

        @BindView(R.id.ll_default)
        LinearLayout ll_default;

        @BindView(R.id.iv_manage_select)
        MyImageView iv_manage_select;

        @BindView(R.id.tv_manage_default)
        TextView tv_manage_default;

        @BindView(R.id.tv_manage_edit)
        TextView tv_manage_edit;

        @BindView(R.id.tv_manage_del)
        TextView tv_manage_del;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
