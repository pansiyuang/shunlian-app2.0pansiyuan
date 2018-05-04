package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GetDataEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class FirstCategoryMenuAdapter extends BaseRecyclerAdapter<GetDataEntity.MData.Cate> {
    private boolean isFirst=true;
    public int selectedPosition =0;
    public FirstCategoryMenuAdapter(Context context, boolean isShowFooter,
                                    List<GetDataEntity.MData.Cate> lists,boolean isFirst) {
        super(context, isShowFooter, lists);
        this.isFirst=isFirst;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_category_menu, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        GetDataEntity.MData.Cate data = lists.get(position);
        if (isFirst){
            GradientDrawable copyBackground = (GradientDrawable) mHolder.mtv_title.getBackground();
            mHolder.mtv_title.setText(data.name);
            mHolder.mtv_title.setVisibility(View.VISIBLE);
            mHolder.mtv_titles.setVisibility(View.GONE);
            if (position==selectedPosition){
                mHolder.mtv_title.setTextColor(getColor(R.color.white));
                copyBackground.setColor(getColor(R.color.pink_color));
            }else {
                mHolder.mtv_title.setTextColor(getColor(R.color.new_text));
                copyBackground.setColor(getColor(R.color.value_F7F7F7));
            }
        }else {
            mHolder.mtv_titles.setText(data.name);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mtv_titles.setVisibility(View.VISIBLE);
            if (position==selectedPosition){
                mHolder.mtv_titles.setTextColor(getColor(R.color.white));
                mHolder.mtv_titles.setBackgroundResource(R.mipmap.bg_home_shangxin);
            }else {
                mHolder.mtv_titles.setTextColor(getColor(R.color.new_text));
                mHolder.mtv_titles.setBackgroundResource(0);
            }
        }

    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_titles)
        MyTextView mtv_titles;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
