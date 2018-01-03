package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.ui.returns_order.RefundAfterSaleAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ExchangeDetailOptAdapter extends BaseRecyclerAdapter<RefundDetailEntity.RefundDetail.Opt> {
    private int pink_color;
    private int new_gray;
    private int strokeWidth;

    public ExchangeDetailOptAdapter(Context context, boolean isShowFooter, List<RefundDetailEntity.RefundDetail.Opt> opts) {
        super(context, isShowFooter,opts);
        pink_color = getColor(R.color.pink_color);
        new_gray = getColor(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(context, 0.5f);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MsgHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_detail_opt, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MsgHolder mHolder = (MsgHolder) holder;
        final RefundDetailEntity.RefundDetail.Opt opt=lists.get(position);
        GradientDrawable btGround;
        mHolder.mtv_button.setText(opt.name);
        if ("N".equals(opt.is_highlight)){
            btGround = (GradientDrawable) mHolder.mtv_button.getBackground();
            btGround.setStroke(strokeWidth, new_gray);
            mHolder.mtv_button.setTextColor(new_gray);
        }else {
            btGround = (GradientDrawable) mHolder.mtv_button.getBackground();
            btGround.setStroke(strokeWidth, pink_color);
            mHolder.mtv_button.setTextColor(pink_color);
        }
        mHolder.mtv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastClickListener.isFastClick()) {
                    return;
                }
                switch (opt.code){
                    case "view_history_enable":
                        RefundAfterSaleAct.startAct(context);
                        break;
                    case "edit_apply_enable":
                        break;
                    case "call_plat_enable":
                        break;
                    case "edit_call_plat_enable":
                        break;
                    case "add_ship_enable":
                        break;
                    case "edit_ship_enable":
                        break;
                }
            }
        });
    }

    public class MsgHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_button)
        MyTextView mtv_button;

        public MsgHolder(View itemView) {
            super(itemView);
        }

    }
}
