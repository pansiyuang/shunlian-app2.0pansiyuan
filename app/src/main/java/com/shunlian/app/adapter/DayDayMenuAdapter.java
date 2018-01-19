package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.ActivityListEntity;
import com.shunlian.app.bean.StorePromotionGoodsListEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class DayDayMenuAdapter extends BaseRecyclerAdapter<ActivityListEntity.Menu> {
    private Context context;
    private List<ActivityListEntity.Menu> datas;
    public int selectPosition=0;

    public DayDayMenuAdapter(Context context, boolean isShowFooter, List<ActivityListEntity.Menu> datas) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TwoHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_menu, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TwoHolder) {
            TwoHolder twoHolder = (TwoHolder) holder;

            ActivityListEntity.Menu data = datas.get(position);
//            StorePromotionGoodsListEntity.Lable data = lists.get(position);
            twoHolder.mtv_time.setText(data.time);
            twoHolder.mtv_desc.setText(data.content);
            twoHolder.view.setTag(R.id.tag_day_menu_id,data.id);
            if (selectPosition==position){
                twoHolder.mrlayout_rootView.setBackgroundResource(R.mipmap.bg_active_tiantiantehui_h);
                twoHolder.mllayout_tv.setBackgroundResource(0);
            }else {
                twoHolder.mrlayout_rootView.setBackgroundResource(0);
                twoHolder.mllayout_tv.setBackgroundResource(getColor(R.color.new_text));
            }
        }
    }


    class TwoHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mrlayout_rootView)
        MyTextView mrlayout_rootView;

        @BindView(R.id.mllayout_tv)
        MyTextView mllayout_tv;

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
