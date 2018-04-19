package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.HelpSearchEntity;
import com.shunlian.app.bean.HelpcenterSolutionEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/2/27.
 */

public class SearchQAdapter extends BaseRecyclerAdapter<HelpSearchEntity.Content> {
    private String keyWord=" ";

    public SearchQAdapter(Context context,String keyWord, boolean isShowFooter, List<HelpSearchEntity.Content> lists) {
        super(context, isShowFooter, lists);
        this.keyWord=keyWord;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HolderView(LayoutInflater.from(context).inflate(R.layout.item_search_question, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HolderView) {
            HolderView viewHolder = (HolderView) holder;
            HelpSearchEntity.Content content = lists.get(position);
            SpannableStringBuilder titleBuilder = Common.changeColor(content.title, keyWord, getColor(R.color.pink_color));
            viewHolder.mtv_title.setText(titleBuilder);
            viewHolder.mtv_desc.setText(content.content);
        }
    }

    public class HolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;


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
