package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.ui.order.ExchangeDetailAct;
import com.shunlian.app.ui.order.PlatformInterventionRequestActivity;
import com.shunlian.app.ui.returns_order.ConsultHistoryAct;
import com.shunlian.app.ui.returns_order.ReturnRequestActivity;
import com.shunlian.app.ui.returns_order.SubmitLogisticsInfoAct;
import com.shunlian.app.utils.FastClickListener;
import com.shunlian.app.utils.PromptDialog;
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
    private String refund_id,order_id;
    private ExchangeDetailAct exchangeDetailAct;
    private RefundDetailEntity.RefundDetail.Edit mEdit;

    public ExchangeDetailOptAdapter(Context context, boolean isShowFooter, List<RefundDetailEntity.RefundDetail.Opt> opts,String refund_id,String order_id,RefundDetailEntity.RefundDetail.Edit edit) {
        super(context, isShowFooter,opts);
        exchangeDetailAct= (ExchangeDetailAct) context;
        this.refund_id=refund_id;
        this.order_id=order_id;
        pink_color = getColor(R.color.pink_color);
        new_gray = getColor(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(context, 0.5f);
        this.mEdit = edit;
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
                        ConsultHistoryAct.startAct(context,refund_id);
                        break;
                    case "edit_apply_enable":
                        ReturnRequestActivity.startAct(context,mEdit,true,refund_id);
                        break;
                    case "call_plat_enable":
                        PlatformInterventionRequestActivity.startAct(context,refund_id,mEdit,false);
                        break;
                    case "edit_call_plat_enable":
                        PlatformInterventionRequestActivity.startAct(context,refund_id,mEdit,true);
                        break;
                    case "add_ship_enable":
                        SubmitLogisticsInfoAct.startAct(context,refund_id,SubmitLogisticsInfoAct.APPLY);
                        break;
                    case "edit_ship_enable":
                        SubmitLogisticsInfoAct.startAct(context,refund_id,SubmitLogisticsInfoAct.MODIFY);
                        break;
                    case "check_receive_enable":
                        confirmreceipt(order_id);
                        break;
                }
            }
        });
    }

    /**
     * 确认收货
     */
    public void confirmreceipt(final String order_id) {
        final PromptDialog promptDialog = new PromptDialog((Activity) context);
        promptDialog.setSureAndCancleListener(getString(R.string.confirm_goods_receipt),
                getString(R.string.confirm_goods_receipt_label),
                getString(R.string.confirm_goods), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exchangeDetailAct != null){
                            exchangeDetailAct.confirmreceipt(order_id);
                        }
                        promptDialog.dismiss();
                    }
                }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.dismiss();
                    }
                }).show();
    }

    public class MsgHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_button)
        MyTextView mtv_button;

        public MsgHolder(View itemView) {
            super(itemView);
        }

    }
}
