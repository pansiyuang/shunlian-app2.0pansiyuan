package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreTypeAdapter extends BaseRecyclerAdapter<String> {

    public StoreTypeAdapter(Context context, boolean isShowFooter, List<String> datas) {
        super(context, isShowFooter, datas);

    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DefaultHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_type, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DefaultHolder) {
            DefaultHolder defaultHolder = (DefaultHolder) holder;
            defaultHolder.mtv_content.setText(lists.get(position));
            //设置圆角背景
            GradientDrawable copyBackground = (GradientDrawable) defaultHolder.mtv_content.getBackground();
            switch (lists.get(position)){
                case "新品":
                    copyBackground.setColor(getColor(R.color.store_tagone));
                    break;
                case "爆款":
                    copyBackground.setColor(getColor(R.color.store_tagtwo));
                    break;
                case "热卖":
                    copyBackground.setColor(getColor(R.color.store_tagthree));
                    break;
                case "推荐":
                    copyBackground.setColor(getColor(R.color.store_tagfour));
                    break;
                default:
                    copyBackground.setColor(getColor(R.color.store_tagfour));
                    break;
            }
        }

    }


    class DefaultHolder extends RecyclerView.ViewHolder {
        MyTextView mtv_content;

        DefaultHolder(View itemView) {
            super(itemView);
            mtv_content= (MyTextView) itemView.findViewById(R.id.mtv_content);
        }
    }
}
