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
import com.shunlian.app.ui.returns_order.SelectServiceActivity;
import com.shunlian.app.ui.returns_order.SubmitLogisticsInfoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
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
    private String refund_id;
    private ExchangeDetailAct exchangeDetailAct;
    private RefundDetailEntity.RefundDetail.Edit mEdit;
    private RefundDetailEntity.RefundDetail refundDetail;
    private long firstMillies;
    private int second;

    public ExchangeDetailOptAdapter(Context context, List<RefundDetailEntity.RefundDetail.Opt> opts, RefundDetailEntity.RefundDetail detail) {
        super(context, false, opts);
        exchangeDetailAct = (ExchangeDetailAct) context;
        this.refund_id = detail.refund_id;
        pink_color = getColor(R.color.pink_color);
        new_gray = getColor(R.color.new_gray);
        strokeWidth = TransformUtil.dip2px(context, 0.5f);
        this.mEdit = detail.edit;
        refundDetail = detail;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new MsgHolder(LayoutInflater.from(context).inflate(R.layout.item_exchange_detail_opt, parent, false));
    }

    public void setCurrentDate(long millies) {
        firstMillies = millies;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MsgHolder mHolder = (MsgHolder) holder;
        final RefundDetailEntity.RefundDetail.Opt opt = lists.get(position);
        GradientDrawable btGround;
        mHolder.mtv_button.setText(opt.name);
        if ("N".equals(opt.is_highlight)) {
            btGround = (GradientDrawable) mHolder.mtv_button.getBackground();
            btGround.setStroke(strokeWidth, new_gray);
            mHolder.mtv_button.setTextColor(new_gray);
        } else {
            btGround = (GradientDrawable) mHolder.mtv_button.getBackground();
            btGround.setStroke(strokeWidth, pink_color);
            mHolder.mtv_button.setTextColor(pink_color);
        }
        mHolder.mtv_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyOnClickListener.isFastClick()) {
                    return;
                }
                switch (opt.code) {
                    case "view_history_enable":
                        ConsultHistoryAct.startAct(context, refund_id);
                        break;
                    case "cancle_apply":
                        cancleapply(refund_id);
                        break;
                    case "edit_apply_enable":
                        if ("1".equals(mEdit.edit_apply_type)) {
                            SelectServiceActivity.startAct(context, mEdit.og_id);
                        } else {
                            ReturnRequestActivity.startAct(context, mEdit, true, refund_id);
                        }
                        break;
                    case "call_plat_enable":
                        if (isEmpty(getLimitTime(firstMillies))) {
                            return;
                        }
                        refundDetail.rest_second = getLimitTime(firstMillies);
                        PlatformInterventionRequestActivity.startAct(context, refundDetail, false);
                        break;
                    case "edit_call_plat_enable":
                        if (isEmpty(getLimitTime(firstMillies))) {
                            return;
                        }
                        refundDetail.rest_second = getLimitTime(firstMillies);
                        PlatformInterventionRequestActivity.startAct(context, refundDetail, true);
                        break;
                    case "add_ship_enable":
                        SubmitLogisticsInfoAct.startAct(context, refund_id, SubmitLogisticsInfoAct.APPLY);
                        break;
                    case "edit_ship_enable":
                        SubmitLogisticsInfoAct.startAct(context, refund_id, SubmitLogisticsInfoAct.MODIFY);
                        break;
                    case "check_receive_enable":
                        confirmreceipt(refund_id);
                        break;
                }
            }
        });
    }

    /**
     * 撤销售后
     */
    public void cancleapply(final String refund_id) {
        final PromptDialog promptDialog = new PromptDialog((Activity) context);
        promptDialog.setSureAndCancleListener(getString(R.string.confirm_ninjiangchexiao),
                getString(R.string.confirm), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exchangeDetailAct != null) {
                            exchangeDetailAct.cancleapply(refund_id);
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

    /**
     * 确认收货
     */
    public void confirmreceipt(final String refund_id) {
        final PromptDialog promptDialog = new PromptDialog((Activity) context);
        promptDialog.setSureAndCancleListener(getString(R.string.confirm_goods_receipt),
                getString(R.string.confirm_goods_receipt_label),
                getString(R.string.confirm_goods), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (exchangeDetailAct != null) {
                            exchangeDetailAct.confirmreceipt(refund_id);
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

    public String getLimitTime(Long beginTime) {
        String restSecond;
        LogUtil.httpLogW("ceil:" + (System.currentTimeMillis() - beginTime) / 1000);
        second = (int) Math.ceil((System.currentTimeMillis() - beginTime) / 1000);
        if (second <= 0) {
            return null;
        }
        restSecond = String.valueOf(Integer.valueOf(refundDetail.rest_second) - second);
        return restSecond;
    }
}
