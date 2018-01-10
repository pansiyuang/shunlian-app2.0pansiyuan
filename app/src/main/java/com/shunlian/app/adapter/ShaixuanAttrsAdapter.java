package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetListFilterEntity;
import com.shunlian.app.ui.category.CategoryLetterAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/5.
 */

public class ShaixuanAttrsAdapter extends BaseRecyclerAdapter<String> {
    public boolean isAll=false;
    private String tag;

    public ShaixuanAttrsAdapter(Context context, boolean isShowFooter, List<String> lists,String tag) {
        super(context, isShowFooter, lists);
        this.tag=tag;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_pingpai, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final String attrs = lists.get(position);
        viewHolder.mtv_name.setText(attrs);

        if (Constant.BRAND_ATTRS.get(tag).size() > 0) {
            for (int i = 0; i < Constant.BRAND_ATTRS.get(tag).size(); i++) {
                if (attrs.equals(Constant.BRAND_ATTRS.get(tag).get(i))) {
                    viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                    break;
                } else if (i >= Constant.BRAND_ATTRS.get(tag).size() - 1) {
                    viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
                    break;
                }
            }
        } else {
            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
        }
        viewHolder.mtv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickListener.isFastClick()) {
                    return;
                }
                if (Constant.BRAND_ATTRS.get(tag).size() > 0) {
                    for (int i = 0; i < Constant.BRAND_ATTRS.get(tag).size(); i++) {
                        if (attrs.equals(Constant.BRAND_ATTRS.get(tag).get(i))) {
                            viewHolder.mtv_name.setBackgroundColor(getColor(R.color.value_f5));
                            Constant.BRAND_ATTRS.get(tag).remove(i);
                            break;
                        } else if (i >= Constant.BRAND_ATTRS.get(tag).size() - 1) {
                            viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                            Constant.BRAND_ATTRS.get(tag).add(attrs);
                            break;
                        }
                    }
                } else {
                    viewHolder.mtv_name.setBackgroundResource(R.mipmap.img_dcha);
                    Constant.BRAND_ATTRS.get(tag).add(attrs);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (!isAll&&lists.size()>5){
            return 6;
        }
        return super.getItemCount();
    }

    public class ViewHolder extends BaseRecyclerViewHolder{
        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
