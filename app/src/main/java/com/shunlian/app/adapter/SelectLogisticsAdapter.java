package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.Contact;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/1/4.
 */

public class SelectLogisticsAdapter extends BaseRecyclerAdapter<Contact> {

    public SelectLogisticsAdapter(Context context, List<Contact> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_logistics, parent, false);
        return new SelectLogisticsHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelectLogisticsHolder){
            SelectLogisticsHolder mHolder = (SelectLogisticsHolder) holder;
            Contact contact = lists.get(position);

            mHolder.mtv_name.setText(contact.getName());
            if (position != 0){
                Contact contact1 = lists.get(position - 1);
                if (!contact.getIndex().equals(contact1.getIndex())){
                    mHolder.view_line.setVisibility(View.VISIBLE);
                    mHolder.mtv_initials.setVisibility(View.VISIBLE);
                    mHolder.mtv_initials.setText(contact.getIndex());
                }else {
                    mHolder.view_line.setVisibility(View.GONE);
                    mHolder.mtv_initials.setVisibility(View.GONE);
                }
            }else {
                mHolder.view_line.setVisibility(View.GONE);
                mHolder.mtv_initials.setVisibility(View.VISIBLE);
                mHolder.mtv_initials.setText(contact.getIndex());
            }
        }
    }

    public class SelectLogisticsHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_initials)
        MyTextView mtv_initials;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.view_line)
        View view_line;

        public SelectLogisticsHolder(View itemView) {
            super(itemView);
            mtv_name.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
