package com.shunlian.app.yjfk;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ImageEntity;

import java.util.List;

/**
 * Created by Administrator on 2019/4/18.
 */

public class TypeAdapter extends BaseRecyclerAdapter<ComplaintTypesEntity.Title> {
    public String id="";
    public boolean isClick=false;

    public TypeAdapter(Context context, boolean isShowFooter, List lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = mInflater.inflate(R.layout.item_type_1, parent, false);
        return new type1Holder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        type1Holder holder1 = (type1Holder) holder;
        ComplaintTypesEntity.Title title=lists.get(position);
        holder1.mTextView.setText(title.title);
        RecyclerView mRvCategory = holder1.mRvCategory;
        mRvCategory.setHasFixedSize(true);
        mRvCategory.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
        typeAdapter1 typeAdapter1=new typeAdapter1(context, false, title.types);
        typeAdapter1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                isClick=true;
                id=title.types.get(position).id;
                notifyDataSetChanged();
            }
        });
        mRvCategory.setAdapter(typeAdapter1);

    }

    private class typeAdapter1 extends BaseRecyclerAdapter<ComplaintTypesEntity.Title.Typess> {


        public typeAdapter1(Context context, boolean isShowFooter, List lists) {
            super(context, isShowFooter, lists);
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            View inflate = mInflater.inflate(R.layout.item_type_2, parent, false);
            return new type2Holder(inflate);
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            type2Holder holder2 = (type2Holder) holder;
            ComplaintTypesEntity.Title.Typess title=lists.get(position);
            holder2.mTextView.setText(title.desc);
//            holder2.id=lists.get(position).id;
//            if(lists.get(position).id==)
            if (id.equals(title.id)) {
                holder2.mTextView.setBackgroundResource(R.drawable.benjin_6);
                holder2.mTextView.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                holder2.mTextView.setTextColor(Color.parseColor("#767676"));
                holder2.mTextView.setBackgroundResource(R.drawable.benjin_5);
            }

        }
        class type2Holder extends RecyclerView.ViewHolder {

            TextView mTextView;

            public type2Holder(View itemView) {
                super(itemView);
                mTextView = itemView.findViewById(R.id.mtv_type1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onItemClick(view, getAdapterPosition());
                        }
                    }
                });
            }

        }
    }

     class type1Holder extends RecyclerView.ViewHolder {

        TextView mTextView;
        RecyclerView mRvCategory;

        public type1Holder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.mtv_type1);
            mRvCategory = itemView.findViewById(R.id.mrv_type2);
        }
    }


}
