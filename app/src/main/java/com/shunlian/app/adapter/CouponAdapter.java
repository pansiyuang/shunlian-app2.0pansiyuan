package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.shunlian.app.R;
import com.shunlian.app.bean.VouchercenterplEntity;
import com.shunlian.app.presenter.PGetCoupon;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.store.StoreAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class CouponAdapter extends BaseRecyclerAdapter<VouchercenterplEntity.MData> {
    private PGetCoupon pGetCoupon;
    public CouponAdapter(Context context, boolean isShowFooter,
                         List<VouchercenterplEntity.MData> lists,PGetCoupon pGetCoupon) {
        super(context, isShowFooter, lists);
        this.pGetCoupon=pGetCoupon;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_get_coupon, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        VouchercenterplEntity.MData data = lists.get(position);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_photo, data.thumb);
        mHolder.mtv_title.setText(data.title);
        mHolder.mtv_desc.setText(getString(R.string.coupon_youxiaoqi)+data.valid_date);
        SpannableStringBuilder spannableStringBuilder = Common.changeTextSize(getString(R.string.common_yuan)+data.denomination+" "+data.use_condition
                , data.denomination, 21);
        mHolder.mtv_price.setText(spannableStringBuilder);
        if ("0".equals(data.if_get)) {
            mHolder.mtv_yiqiang.setText(getString(R.string.first_yiqiang) + data.already_get + "%");
            mHolder.seekbar_grow.setProgress(Integer.parseInt(data.already_get));
            mHolder.mtv_yiqiang.setVisibility(View.VISIBLE);
            mHolder.seekbar_grow.setVisibility(View.VISIBLE);
            mHolder.mtv_yiling.setVisibility(View.INVISIBLE);
            mHolder.mtv_get.setBackgroundResource(R.drawable.bg_common_round_nobord);
            GradientDrawable copyBackground = (GradientDrawable) mHolder.mtv_get.getBackground();
            copyBackground.setColor(getColor(R.color.pink_color));//设置填充色
            mHolder.mtv_get.setText(getString(R.string.chat_lijilingqu));
            mHolder.mtv_get.setTextColor(getColor(R.color.white));
            mHolder.mtv_get.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pGetCoupon.getVoucher(data.id,true,position);
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
                    Common.goGoGo(context,data.jump_type,data.lazy_id);
                }
            });
        }

    }

    public class ActivityMoreHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

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

        @BindView(R.id.seekbar_grow)
        SeekBar seekbar_grow;

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
