package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class KoubeiMenuAdapter extends BaseRecyclerAdapter<HotsaleHomeEntity.Cate> {
    public int selectedPosition = 0;

    public KoubeiMenuAdapter(Context context, boolean isShowFooter,
                             List<HotsaleHomeEntity.Cate> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_koubei_menu, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        HotsaleHomeEntity.Cate data = lists.get(position);
        mHolder.mtv_titles.setText(data.cate_name);
        if (position == selectedPosition) {
            mHolder.mtv_titles.setTextColor(getColor(R.color.white));
        } else {
            mHolder.mtv_titles.setTextColor(Color.parseColor("#FFD8776C"));
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_titles)
        MyTextView mtv_titles;


        public ActivityMoreHolder(View itemView) {
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
