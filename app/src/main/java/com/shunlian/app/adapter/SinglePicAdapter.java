package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class SinglePicAdapter extends BaseRecyclerAdapter<String> {
    private int gap, border, recyclerWidth;

    public SinglePicAdapter(Context context, boolean isShowFooter, List<String> list, int gap, int border) {
        super(context, isShowFooter, list);
        this.gap = gap;
        this.border = border;
    }

    public SinglePicAdapter(Context context, List<String> list, int gap, int width) {
        super(context, false, list);
        this.gap = gap;
        this.recyclerWidth = width;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MViewHolder(LayoutInflater.from(context).inflate(R.layout.item_single_pic, null));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MViewHolder viewHolder = (MViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, viewHolder.miv_pic, lists.get(position), 4);
    }

    @Override
    public int getItemCount() {
        if (lists.size() > 9) {
            return 9;
        }
        return super.getItemCount();
    }


    public class MViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public MViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            int picWidth;
            if (recyclerWidth == 0) {
                picWidth = (Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, (gap == 0 ? 10 : gap) * 2) - TransformUtil.dip2px(context, (border == 0 ? 20 : border) * 2)) / 3;
            } else {
                picWidth = (recyclerWidth - TransformUtil.dip2px(context, (gap == 0 ? 10 : gap) * 2)) / 3;
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth, picWidth);
            miv_pic.setLayoutParams(params);
        }

    }
}
