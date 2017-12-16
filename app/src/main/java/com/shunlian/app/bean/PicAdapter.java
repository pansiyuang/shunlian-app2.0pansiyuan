package com.shunlian.app.bean;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/6.
 */

public class PicAdapter extends BaseRecyclerAdapter<String> {

    public PicAdapter(Context context, boolean isShowFooter, List<String> lists) {
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
        PicHolder picHolder = new PicHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_detail, parent, false));
        return picHolder;
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        PicHolder mHolder = (PicHolder) holder;
        GlideUtils.getInstance().loadImage(context,mHolder.miv_pic, lists.get(position));
    }

    public class PicHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public PicHolder(View itemView) {
            super(itemView);
            miv_pic.setWHProportion(220,220);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
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
