package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.PersonalDataEntity;
import com.shunlian.app.bean.PersonalcenterEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ZiChanAdapter extends BaseRecyclerAdapter<PersonalcenterEntity.MyAssets.Item> {


    public ZiChanAdapter(Context context, boolean isShowFooter, List<PersonalcenterEntity.MyAssets.Item> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_zichan, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        PersonalcenterEntity.MyAssets.Item ad = lists.get(position);
        mHolder.ntv_account.setText(ad.content.text);
        if (Common.isColor(ad.content.color)){
            mHolder.ntv_account.setTextColor(Color.parseColor(ad.content.color));
        }
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

        @BindView(R.id.ntv_account)
        NewTextView ntv_account;

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
