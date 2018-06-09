package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CouponsAdapter extends BaseRecyclerAdapter<VouchercenterplEntity.MData> {
    private PGetCoupon pGetCoupon;

    public CouponsAdapter(Context context, boolean isShowFooter,
                          List<VouchercenterplEntity.MData> lists, PGetCoupon pGetCoupon) {
        super(context, isShowFooter, lists);
        this.pGetCoupon=pGetCoupon;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_get_coupons, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        VouchercenterplEntity.MData data = lists.get(position);
        mHolder.mtv_title.setText(data.title);
        SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan) + data.denomination + data.use_condition
                , data.denomination, 21);
        mHolder.mtv_price.setText(spannableStringBuilder);
        switch (data.new_or_old){
            case "1":
                mHolder.miv_tag.setImageResource(R.mipmap.img_zuixin);
                break;
            case "-1":
                mHolder.miv_tag.setImageResource(R.mipmap.img_xiajia);
                break;
            default:
                mHolder.miv_tag.setImageResource(0);
                break;
        }
        if ("0".equals(data.if_get)) {
            mHolder.mtv_yiqiang.setText(getString(R.string.first_yiqiang) + data.already_get + "%");
            mHolder.seekbar_grow.setProgress(Integer.parseInt(data.already_get));
            mHolder.mtv_yiqiang.setVisibility(View.VISIBLE);
            mHolder.seekbar_grow.setVisibility(View.VISIBLE);
            mHolder.mtv_yiling.setVisibility(View.INVISIBLE);
            mHolder.mtv_get.setBackgroundResource(R.drawable.bg_common_round_nobord);
            mHolder.mtv_get.setText(getString(R.string.chat_lijilingqu));
            mHolder.mtv_get.setTextColor(getColor(R.color.white));
            mHolder.mtv_get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pGetCoupon.getVoucher(data.id,false,position);
                }
            });
        } else {
            mHolder.mtv_yiqiang.setVisibility(View.INVISIBLE);
            mHolder.seekbar_grow.setVisibility(View.INVISIBLE);
            mHolder.mtv_yiling.setVisibility(View.VISIBLE);
            mHolder.mtv_get.setBackgroundResource(R.drawable.bg_common_pink_round);
            mHolder.mtv_get.setText(getString(R.string.first_mashangshiyong));
            mHolder.mtv_get.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StoreAct.startAct(context,data.store_id);
                }
            });
        }
    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_yiqiang)
        MyTextView mtv_yiqiang;

        @BindView(R.id.mtv_get)
        MyTextView mtv_get;

        @BindView(R.id.mtv_yiling)
        MyTextView mtv_yiling;

        @BindView(R.id.miv_tag)
        MyImageView miv_tag;

        @BindView(R.id.seekbar_grow)
        SeekBar seekbar_grow;

        public ActivityMoreHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            seekbar_grow.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return true;
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
