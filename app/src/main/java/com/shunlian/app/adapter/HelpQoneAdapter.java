package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HelpcenterIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HelpQoneAdapter extends BaseRecyclerAdapter<HelpcenterIndexEntity.QuestionCategory> {

    public HelpQoneAdapter(Context context, boolean isShowFooter, List<HelpcenterIndexEntity.QuestionCategory> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HolderView(LayoutInflater.from(context).inflate(R.layout.item_help_qone, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderView) {
            HolderView viewHolder = (HolderView) holder;
            HelpcenterIndexEntity.QuestionCategory category = lists.get(position);
            if (position%2==0){
                viewHolder.view_line.setVisibility(View.VISIBLE);
            }else {
                viewHolder.view_line.setVisibility(View.GONE);
            }
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_icon, category.icon);
            viewHolder.mtv_name.setText(category.name);
            viewHolder.mtv_key.setText(category.keyword);
        }
    }

    public class HolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.mtv_key)
        MyTextView mtv_key;

        public HolderView(View itemView) {
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
