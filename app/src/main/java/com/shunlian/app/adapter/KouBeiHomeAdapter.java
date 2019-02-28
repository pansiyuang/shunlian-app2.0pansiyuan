package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class KouBeiHomeAdapter extends BaseRecyclerAdapter<HotsaleHomeEntity.AD> {


    public KouBeiHomeAdapter(Context context, boolean isShowFooter, List<HotsaleHomeEntity.AD> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_koubei_home, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        HotsaleHomeEntity.AD ad = lists.get(position);
        GlideUtils.getInstance().loadImageZheng(context,mHolder.miv_photo,ad.thumb,9,true,false);
        mHolder.ntv_title.setText(ad.title);
        mHolder.ntv_price.setText(getString(R.string.common_yuan)+ad.price);
        mHolder.ntv_desc.setText(ad.self_buy_earn);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.ntv_title)
        NewTextView ntv_title;

        @BindView(R.id.ntv_price)
        NewTextView ntv_price;

        @BindView(R.id.ntv_desc)
        NewTextView ntv_desc;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
