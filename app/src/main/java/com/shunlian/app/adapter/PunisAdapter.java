package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

import java.util.List;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PunisAdapter extends BaseRecyclerAdapter<String> {

    public PunisAdapter(Context context, boolean isShowFooter, List<String> lists) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_punish, parent, false);
        return new PunishHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        PunishHolder mHolder = (PunishHolder) holder;

    }


    public class PunishHolder extends BaseRecyclerViewHolder{

        public PunishHolder(View itemView) {
            super(itemView);
            GradientDrawable background = (GradientDrawable) itemView.getBackground();
            background.setColor(getColor(R.color.white));
        }
    }
}
