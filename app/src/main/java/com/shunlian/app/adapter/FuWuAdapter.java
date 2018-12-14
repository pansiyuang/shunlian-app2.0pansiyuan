package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class FuWuAdapter extends BaseRecyclerAdapter<PersonalcenterEntity.MyService.Item> {


    public FuWuAdapter(Context context, boolean isShowFooter, List<PersonalcenterEntity.MyService.Item> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_fuwu, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        PersonalcenterEntity.MyService.Item ad = lists.get(position);
        GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_icon,ad.img);
        mHolder.ntv_title.setText(ad.title);
        if (!isEmpty(ad.update_title)){
            mHolder.ntv_tag.setVisibility(View.VISIBLE);
            mHolder.ntv_tag.setText(ad.update_title);
        }else {
            mHolder.ntv_tag.setVisibility(View.GONE);
            if ("0".equals(ad.update_point)){
                mHolder.view_tag.setVisibility(View.GONE);
            }else {
                mHolder.view_tag.setVisibility(View.VISIBLE);
            }
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_tag)
        NewTextView ntv_tag;

        @BindView(R.id.view_tag)
        View view_tag;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
