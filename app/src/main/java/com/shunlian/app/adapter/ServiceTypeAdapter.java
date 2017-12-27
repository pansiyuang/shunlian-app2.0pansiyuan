package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.RefundInfoEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ServiceTypeAdapter extends BaseRecyclerAdapter<RefundInfoEntity.RefundChoice> {

    public ServiceTypeAdapter(Context context, boolean isShowFooter, List<RefundInfoEntity.RefundChoice> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_return_service, parent, false);
        return new ServiceViewholder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ServiceViewholder viewholder = (ServiceViewholder) holder;
        RefundInfoEntity.RefundChoice entity = lists.get(position);
        GlideUtils.getInstance().loadImage(context, viewholder.miv_service_icon, entity.icon);
        viewholder.tv_service_name.setText(entity.hint);
        viewholder.tv_service_content.setText(entity.tip);
    }

    public class ServiceViewholder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_service_icon)
        MyImageView miv_service_icon;

        @BindView(R.id.tv_service_name)
        TextView tv_service_name;

        @BindView(R.id.tv_service_content)
        TextView tv_service_content;

        public ServiceViewholder(View itemView) {
            super(itemView);
            if (listener != null) {
                listener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }
}
