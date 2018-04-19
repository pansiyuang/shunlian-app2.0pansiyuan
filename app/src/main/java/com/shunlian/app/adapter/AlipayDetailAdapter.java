package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.AmountDetailEntity;
import com.shunlian.app.bean.GetRealInfoEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class AlipayDetailAdapter extends BaseRecyclerAdapter<GetRealInfoEntity.Content> {

    public AlipayDetailAdapter(Context context, boolean isShowFooter,
                               List<GetRealInfoEntity.Content> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_amount_detail, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        GetRealInfoEntity.Content ad = lists.get(position);
        mHolder.mtv_desc.setText(ad.value);
        mHolder.mtv_title.setTextColor(getColor(R.color.new_text));
        mHolder.mtv_title.setText(ad.name);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }
    }
}
