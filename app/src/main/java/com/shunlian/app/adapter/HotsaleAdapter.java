package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.SaleProgressView;
import com.shunlian.app.widget.SaleProgressViews;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class HotsaleAdapter extends BaseRecyclerAdapter<HotsaleEntity.Suspension> {


    public HotsaleAdapter(Context context, boolean isShowFooter, List<HotsaleEntity.Suspension> lists) {
        super(context, isShowFooter, lists);
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hotsale, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        HotsaleEntity.Suspension ad = lists.get(position);
        mHolder.ntv_ttitle.setText(ad.cate_name);
        if (!isEmpty(ad.list)) {
            if (ad.list.get(0) != null) {
                HotsaleEntity.Suspension.Link left = ad.list.get(0);
                GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_tleft, left.thumb);
                mHolder.ntv_tleft.setText(left.title);
                mHolder.ntv_tlefts.setText(left.praise);
                try {
                    if (isEmpty(left.star_rate)){
                        mHolder.progress_view_salel.setVisibility(View.GONE);
                    }else {
                        mHolder.progress_view_salel.setVisibility(View.VISIBLE);
                        mHolder.progress_view_salel.setTotalAndCurrentCount(100,Integer.parseInt(left.star_rate));
                    }
                }catch (Exception e){
                    mHolder.progress_view_salel.setVisibility(View.GONE);
                }
            }
            if (2 <= ad.list.size() && ad.list.get(1) != null) {
                HotsaleEntity.Suspension.Link mid = ad.list.get(1);
                GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_tmid, mid.thumb);
                mHolder.ntv_tmid.setText(mid.title);
                mHolder.ntv_tmids.setText(mid.praise);
                try {
                    if (isEmpty(mid.star_rate)){
                        mHolder.progress_view_salem.setVisibility(View.GONE);
                    }else {
                        mHolder.progress_view_salem.setVisibility(View.VISIBLE);
                        mHolder.progress_view_salem.setTotalAndCurrentCount(100,Integer.parseInt(mid.star_rate));
                    }
                }catch (Exception e){
                    mHolder.progress_view_salem.setVisibility(View.GONE);
                }
            }
            if (3 <= ad.list.size() && ad.list.get(2) != null) {
                HotsaleEntity.Suspension.Link right = ad.list.get(2);
                GlideUtils.getInstance().loadImageZheng(context, mHolder.miv_tright, right.thumb);
                mHolder.ntv_tright.setText(right.title);
                mHolder.ntv_trights.setText(right.praise);
                try {
                    if (isEmpty(right.star_rate)){
                        mHolder.progress_view_saler.setVisibility(View.GONE);
                    }else {
                        mHolder.progress_view_saler.setVisibility(View.VISIBLE);
                        mHolder.progress_view_saler.setTotalAndCurrentCount(100,Integer.parseInt(right.star_rate));
                    }
                }catch (Exception e){
                    mHolder.progress_view_saler.setVisibility(View.GONE);
                }
            }
        }

    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.progress_view_saler)
        SaleProgressView progress_view_saler;

        @BindView(R.id.progress_view_salem)
        SaleProgressView progress_view_salem;

        @BindView(R.id.progress_view_salel)
        SaleProgressView progress_view_salel;

        @BindView(R.id.ntv_ttitle)
        NewTextView ntv_ttitle;


        @BindView(R.id.miv_tleft)
        MyImageView miv_tleft;

        @BindView(R.id.ntv_tleft)
        NewTextView ntv_tleft;

        @BindView(R.id.ntv_tlefts)
        NewTextView ntv_tlefts;

        @BindView(R.id.miv_tmid)
        MyImageView miv_tmid;

        @BindView(R.id.ntv_tmid)
        NewTextView ntv_tmid;

        @BindView(R.id.ntv_tmids)
        NewTextView ntv_tmids;

        @BindView(R.id.miv_tright)
        MyImageView miv_tright;

        @BindView(R.id.ntv_tright)
        NewTextView ntv_tright;

        @BindView(R.id.ntv_trights)
        NewTextView ntv_trights;


        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }

    }
}
