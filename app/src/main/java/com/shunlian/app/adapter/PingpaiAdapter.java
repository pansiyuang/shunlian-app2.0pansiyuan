package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConfirmOrderEntity;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.utils.CenterAlignImageSpan;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class PingpaiAdapter extends BaseRecyclerAdapter<GetListFilterEntity.Recommend> {

    public PingpaiAdapter(Context context, boolean isShowFooter, List<GetListFilterEntity.Recommend> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pingpai, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        GetListFilterEntity.Recommend recommend = lists.get(position);
        viewHolder.mtv_name.setText(recommend.brand_name);
    }


    public class ViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

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
