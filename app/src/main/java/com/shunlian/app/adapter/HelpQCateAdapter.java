package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HelpcenterQuestionEntity;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class HelpQCateAdapter extends BaseRecyclerAdapter<HelpcenterQuestionEntity.Question> {
    private boolean isArrow=false;
    public HelpQCateAdapter(Context context, boolean isArrow,boolean isShowFooter, List<HelpcenterQuestionEntity.Question> lists) {
        super(context, isShowFooter, lists);
        this.isArrow=isArrow;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HolderView(LayoutInflater.from(context).inflate(R.layout.item_help_qcate, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderView) {
            HolderView viewHolder = (HolderView) holder;
            HelpcenterQuestionEntity.Question category = lists.get(position);
            if (isArrow){
                viewHolder.mtv_desc.setText(category.title);
                viewHolder.miv_arrow.setVisibility(View.VISIBLE);
            }else {
                viewHolder.mtv_desc.setText(category.name);
                viewHolder.miv_arrow.setVisibility(View.GONE);
            }
        }
    }

    public class HolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.miv_arrow)
        MyImageView miv_arrow;


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
