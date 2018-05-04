package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class PinpaiAdapter extends BaseRecyclerAdapter<CorePingEntity.MData> {
    public int selectedPosition = 0;

    public PinpaiAdapter(Context context, boolean isShowFooter,
                         List<CorePingEntity.MData> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ping_pai, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        CorePingEntity.MData data = lists.get(position);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_photo, data.bg_img);
        mHolder.mtv_title.setText(data.content);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;


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
