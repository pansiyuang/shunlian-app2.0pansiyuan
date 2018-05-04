package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class KoubeiAdapter extends BaseRecyclerAdapter<CoreHotEntity.Hot.Goods> {

    public KoubeiAdapter(Context context, boolean isShowFooter,
                         List<CoreHotEntity.Hot.Goods> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_koubei, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        CoreHotEntity.Hot.Goods data = lists.get(position);
        GlideUtils.getInstance().loadImage(context,mHolder.miv_photo,data.thumb);
        mHolder.mtv_title.setText(data.title);
        mHolder.mtv_desc.setText(data.content);
        mHolder.mtv_fu.setText(String.format(getString(R.string.first_yifukuan),data.sales));
        mHolder.mtv_hao.setText(String.format(getString(R.string.first_haopinglv),data.views)+"%");
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_fu)
        MyTextView mtv_fu;

        @BindView(R.id.mtv_hao)
        MyTextView mtv_hao;

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
