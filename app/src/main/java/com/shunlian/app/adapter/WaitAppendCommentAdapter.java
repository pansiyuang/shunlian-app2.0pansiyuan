package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/12.
 */

public class WaitAppendCommentAdapter extends BaseRecyclerAdapter<CommentListEntity.Data> {


    public WaitAppendCommentAdapter(Context context, boolean isShowFooter, List<CommentListEntity.Data> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wait_comment, parent, false);
        return new WaitAppendCommentHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        WaitAppendCommentHolder mHolder = (WaitAppendCommentHolder) holder;
        CommentListEntity.Data data = lists.get(position);

        if (position == 0){
            mHolder.mtv_title.setVisibility(View.VISIBLE);
        }else {
            mHolder.mtv_title.setVisibility(View.GONE);
        }

        GlideUtils.getInstance().loadImage(context,mHolder.miv_goods_pic,data.thumb);
        mHolder.mtv_goods_detail.setText(data.title);
        mHolder.mtv_price.setText(getString(R.string.rmb)+data.price);
        mHolder.mtv_append_comment_staus.setVisibility(View.VISIBLE);
    }

    public class WaitAppendCommentHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_goods_detail)
        MyTextView mtv_goods_detail;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mtv_append_comment_staus)
        MyTextView mtv_append_comment_staus;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;


        public WaitAppendCommentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.mtv_append_comment_staus)
        public void appendComment(){
            // TODO: 2017/12/12  
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
