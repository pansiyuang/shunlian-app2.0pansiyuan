package com.shunlian.app.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DayDayMenuAdapter;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/3/27.
 */

public class DayDayMenuAdapterDemo extends BaseRecyclerAdapter<ActivityListEntity.Menu>{
    public int selectPosition=0;

    public DayDayMenuAdapterDemo(Context context, boolean isShowFooter, List lists,int selectPosition) {
        super(context, isShowFooter, lists);
        this.selectPosition=selectPosition;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new DayDayMenuAdapterDemo.TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_menu, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder){
            TwoHolder twoHolder = (TwoHolder) holder;
            ActivityListEntity.Menu data = lists.get(position);
            twoHolder.mtv_time.setText(data.time);
            twoHolder.mtv_desc.setText(data.content);
            twoHolder.view.setTag(R.id.tag_day_menu_id,data.id);
            if (selectPosition==position){
                twoHolder.mrlayout_rootView.setBackgroundResource(R.mipmap.bg_active_tiantiantehui_h);
                twoHolder.mllayout_tv.setBackgroundResource(0);
            }else {
                twoHolder.mrlayout_rootView.setBackgroundResource(0);
                twoHolder.mllayout_tv.setBackgroundColor(getColor(R.color.new_text));
            }

        }


    }
    class TwoHolder extends BaseRecyclerViewHolder implements View.OnClickListener{
        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mrlayout_rootView)
        MyRelativeLayout mrlayout_rootView;

        @BindView(R.id.mllayout_tv)
        MyLinearLayout mllayout_tv;

        private View view;

        TwoHolder(View itemView) {
            super(itemView);
            view=itemView;
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }

    }
}
