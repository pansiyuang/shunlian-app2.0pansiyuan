package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/11.
 */

public class CommentCardViewAdapter extends BaseRecyclerAdapter<String> {

    private static final int FOOTER = 2;
    private int cardViewHeight;

    public CommentCardViewAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case FOOTER:
                View inflate = LayoutInflater.from(context).inflate(R.layout.cardview_footer, parent, false);
                return new FooterHolder(inflate);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount())
            return FOOTER;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case FOOTER:
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_comment_cardview, parent, false);
        return new CommentCardViewHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class FooterHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mll_bg)
        MyLinearLayout mll_bg;

        public FooterHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = mll_bg.getLayoutParams();
            layoutParams.height = cardViewHeight;
            mll_bg.setLayoutParams(layoutParams);
        }
    }

    public class CommentCardViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.card_rootview)
        CardView card_rootview;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        public CommentCardViewHolder(View itemView) {
            super(itemView);
            card_rootview.setOnClickListener(this);
            card_rootview.post(new Runnable() {
                @Override
                public void run() {
                    cardViewHeight = card_rootview.getMeasuredHeight();
                }
            });
            mtv_content.setWHProportion(261,99);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
