package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 */

public class AttributeAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Attrs> {
    private List<GoodsDeatilEntity.Attrs> mData;
    private Context mContext;

    public AttributeAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Attrs> lists) {
        super(context, isShowFooter, lists);
        this.mData = lists;
        this.mContext = context;
    }

    public void setData(List<GoodsDeatilEntity.Attrs> attrs) {
        this.mData = attrs;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        AttributeViewHolder viewHolder = new AttributeViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_attribute, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        AttributeViewHolder attributeViewHolder = (AttributeViewHolder) holder;
        GoodsDeatilEntity.Attrs attrs = mData.get(position);
        attributeViewHolder.tv_param_name.setText(attrs.label);
        attributeViewHolder.tv_param_value.setText(attrs.value);
    }

    public class AttributeViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_param_name)
        TextView tv_param_name;

        @BindView(R.id.tv_param_value)
        TextView tv_param_value;


        public AttributeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
