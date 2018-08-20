package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.GetDataEntity;
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

public class FirstNavyAdapter extends BaseRecyclerAdapter<GetDataEntity.MData.MMData> {
    private String text_color;

    public FirstNavyAdapter(Context context, boolean isShowFooter, List<GetDataEntity.MData.MMData> lists,String text_color) {
        super(context, isShowFooter, lists);
        this.text_color=text_color;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_first_navy, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        GetDataEntity.MData.MMData data = lists.get(position);
        mHolder.ntv_nav.setText(data.title);
        if (Common.isColor(text_color)){
            mHolder.ntv_nav.setTextColor(Color.parseColor(text_color));
        }
        GlideUtils.getInstance().loadImage(context, mHolder.miv_nav, data.thumb, R.mipmap.img_default_home_nav);
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.miv_nav)
        MyImageView miv_nav;

        @BindView(R.id.ntv_nav)
        NewTextView ntv_nav;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
