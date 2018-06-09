package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.SystemMsgEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/9.
 */

public class SysMsgAdapter extends BaseRecyclerAdapter<SystemMsgEntity.MsgType> {


    public SysMsgAdapter(Context context, List<SystemMsgEntity.MsgType> lists) {
        super(context, true, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_sys_msg, parent, false);
        return new SysMsgHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        SysMsgHolder mHolder = (SysMsgHolder) holder;
        SystemMsgEntity.MsgType msgType = lists.get(position);
        //LogUtil.zhLogW("==系统消息==========" + msgType.type);
        switch (msgType.type) {
            case "100":
                couponType(mHolder, msgType);
                break;
            case "101":
            case "102":
            case "103":
            case "104":
                goodsTemple(mHolder, msgType);
                break;
            case "105":
                activityNotify(mHolder, msgType);
                break;
            case "106":
                specailNotify(mHolder, msgType);
                break;
            case "107":
                punishNotify(mHolder, msgType);
                break;
            case "108":
            case "109":
                articleNotify(mHolder, msgType);
                break;
            case "110":
                platformNotify(mHolder, msgType);
                break;

        }


    }

    /*
    优惠券类型
     */
    private void couponType(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_special, mHolder.rlayout_activity, mHolder.rlayout_article,
                mHolder.rlayout_goods, mHolder.rlayout_goods_notify, mHolder.rlayout_punish);
        visible(mHolder.rlayout_coupon);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_coupon_title.setText(content.title);
        mHolder.mtv_coupon_time.setText(msgType.create_time);
        mHolder.mtv_desc.setText(content.content);
        String concat = getString(R.string.rmb).concat(content.money);
        mHolder.mtv_coupon_den.setText(concat);
        mHolder.mtv_full_cut.setText("满" + content.condition + "可用");
        mHolder.mtv_vail_time.setText("有效期：" + content.expire);
        mHolder.mtv_remark.setText(content.remark);
    }

    /*
   商品模板
    */
    private void goodsTemple(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_special, mHolder.rlayout_activity, mHolder.rlayout_article,
                mHolder.rlayout_coupon, mHolder.rlayout_goods_notify, mHolder.rlayout_punish);
        visible(mHolder.rlayout_goods);

        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_goods_time.setText(msgType.create_time);
        mHolder.mtv_goods_title.setText(content.title);
        mHolder.mtv_goods_desc.setText(content.content);
        mHolder.mtv_source.setText(content.remark);
        GlideUtils.getInstance().loadOverrideImage(context, mHolder.miv_icon, content.thumb,122,118);
        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_goods);
        }else {
            visible(mHolder.red_dot_goods);
        }
    }

    /*
    活动通知
     */
    private void activityNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_coupon, mHolder.rlayout_special, mHolder.rlayout_article,
                mHolder.rlayout_goods, mHolder.rlayout_goods_notify, mHolder.rlayout_punish);
        visible(mHolder.rlayout_activity);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_act_title.setText(content.title);
        mHolder.mtv_act_desc.setText(content.content);
        mHolder.mtv_act_time.setText(content.remark);
        mHolder.mtv_act_msg_time.setText(msgType.create_time);
        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_act);
        }else {
            visible(mHolder.red_dot_act);
        }
    }

    /*
    处罚通知
     */
    private void punishNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_special, mHolder.rlayout_activity, mHolder.rlayout_article,
                mHolder.rlayout_coupon, mHolder.rlayout_goods_notify, mHolder.rlayout_goods);

        SystemMsgEntity.ContentBean content = msgType.body;

        mHolder.mtv_punish.setText(content.title);
        mHolder.mtv_punish_msg_time.setText(msgType.create_time);
        if ("1072".equals(content.opt)){
            visible(mHolder.rlayout_punish, mHolder.mtv_punish_time);
            mHolder.mtv_punish.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_punish_time.setText(content.content);
            mHolder.mtv_punish_due.setText(content.remark);
        }else {
            visible(mHolder.rlayout_punish);
            gone(mHolder.mtv_punish_time);
            mHolder.mtv_punish.setTextColor(getColor(R.color.value_03C780));
            mHolder.mtv_punish_due.setText(content.content);
        }

        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_punish);
        }else {
            visible(mHolder.red_dot_punish);
        }
    }

    /*
   文章审核通知
    */
    private void articleNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_special, mHolder.rlayout_activity, mHolder.rlayout_goods_notify,
                mHolder.rlayout_coupon, mHolder.rlayout_punish, mHolder.rlayout_goods);
        visible(mHolder.rlayout_article);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_Article_title.setText(content.title);
        mHolder.mtv_Article_desc.setText(content.content);
        mHolder.mtv_article_time.setText(msgType.create_time);
        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_article);
        }else {
            visible(mHolder.red_dot_article);
        }
    }

    /*
    商品推送通知
     */
    private void goodsPushNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_special, mHolder.rlayout_activity, mHolder.rlayout_article,
                mHolder.rlayout_coupon, mHolder.rlayout_punish, mHolder.rlayout_goods);
        visible(mHolder.rlayout_goods_notify);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_goods_notify.setText(content.title);
        mHolder.mtv_goods_notify_time.setText(msgType.create_time);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_goods_notif, content.thumb);
    }

    /*
    专题通知
     */
    private void specailNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_activity, mHolder.rlayout_article, mHolder.rlayout_goods_notify,
                mHolder.rlayout_coupon, mHolder.rlayout_punish, mHolder.rlayout_goods);
        visible(mHolder.rlayout_special);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_special_title.setText(content.title);
        mHolder.mtv_special_time.setText(msgType.create_time);
        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_special);
        }else {
            visible(mHolder.red_dot_special);
        }
    }

    /*
   平台通知
    */
    private void platformNotify(SysMsgHolder mHolder, SystemMsgEntity.MsgType msgType) {
        gone(mHolder.rlayout_activity, mHolder.rlayout_article, mHolder.rlayout_goods_notify,
                mHolder.rlayout_coupon, mHolder.rlayout_punish, mHolder.rlayout_goods);
        visible(mHolder.rlayout_special);
        SystemMsgEntity.ContentBean content = msgType.body;
        mHolder.mtv_special_title.setText(content.title);
        mHolder.mtv_special_time.setText(content.time);
        if ("1".equals(msgType.is_read)){
            gone(mHolder.red_dot_special);
        }else {
            visible(mHolder.red_dot_special);
        }
    }


    public class SysMsgHolder extends BaseRecyclerViewHolder {
        /************优惠券*****************/
        @BindView(R.id.rlayout_coupon)
        RelativeLayout rlayout_coupon;

        @BindView(R.id.mtv_coupon_time)
        MyTextView mtv_coupon_time;

        @BindView(R.id.mtv_coupon_title)
        MyTextView mtv_coupon_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_coupon_den)
        MyTextView mtv_coupon_den;

        @BindView(R.id.mtv_full_cut)
        MyTextView mtv_full_cut;

        @BindView(R.id.mtv_vail_time)
        MyTextView mtv_vail_time;

        @BindView(R.id.mtv_remark)
        MyTextView mtv_remark;

        @BindView(R.id.llayout_coupon_title)
        LinearLayout llayout_coupon_title;

        /*************商品模版******************/
        @BindView(R.id.rlayout_goods)
        RelativeLayout rlayout_goods;

        @BindView(R.id.mtv_goods_time)
        MyTextView mtv_goods_time;

        @BindView(R.id.mtv_goods_title)
        MyTextView mtv_goods_title;

        @BindView(R.id.mtv_goods_desc)
        MyTextView mtv_goods_desc;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.mtv_source)
        MyTextView mtv_source;

        @BindView(R.id.red_dot_goods)
        View red_dot_goods;

        /****************促销活动********************/
        @BindView(R.id.rlayout_activity)
        RelativeLayout rlayout_activity;

        @BindView(R.id.mtv_act_title)
        MyTextView mtv_act_title;

        @BindView(R.id.mtv_act_desc)
        MyTextView mtv_act_desc;

        @BindView(R.id.mtv_act_time)
        MyTextView mtv_act_time;

        @BindView(R.id.mtv_act_msg_time)
        MyTextView mtv_act_msg_time;

        @BindView(R.id.red_dot_act)
        View red_dot_act;
        /*****************处罚通知***********************/
        @BindView(R.id.rlayout_punish)
        RelativeLayout rlayout_punish;

        @BindView(R.id.mtv_punish)
        MyTextView mtv_punish;

        @BindView(R.id.mtv_punish_msg_time)
        MyTextView mtv_punish_msg_time;

        @BindView(R.id.mtv_punish_time)
        MyTextView mtv_punish_time;

        @BindView(R.id.mtv_punish_due)
        MyTextView mtv_punish_due;

        @BindView(R.id.red_dot_punish)
        View red_dot_punish;

        /*******************文章审核通知***********************/
        @BindView(R.id.rlayout_article)
        RelativeLayout rlayout_article;

        @BindView(R.id.mtv_Article_title)
        MyTextView mtv_Article_title;

        @BindView(R.id.mtv_Article_desc)
        MyTextView mtv_Article_desc;

        @BindView(R.id.mtv_article_time)
        MyTextView mtv_article_time;

        @BindView(R.id.red_dot_article)
        View red_dot_article;

        /****************商品推送模板********************/
        @BindView(R.id.rlayout_goods_notify)
        RelativeLayout rlayout_goods_notify;

        @BindView(R.id.mtv_goods_notify)
        MyTextView mtv_goods_notify;

        @BindView(R.id.mtv_goods_notify_time)
        MyTextView mtv_goods_notify_time;

        @BindView(R.id.miv_goods_notif)
        MyImageView miv_goods_notif;
        /************专题活动********************/
        @BindView(R.id.rlayout_special)
        RelativeLayout rlayout_special;

        @BindView(R.id.mtv_special_title)
        MyTextView mtv_special_title;

        @BindView(R.id.mtv_special_time)
        MyTextView mtv_special_time;

        @BindView(R.id.red_dot_special)
        View red_dot_special;

        public SysMsgHolder(View itemView) {
            super(itemView);
            GradientDrawable d = new GradientDrawable();
            d.setShape(GradientDrawable.RECTANGLE);
            d.setColor(getColor(R.color.pink_color));
            int i = TransformUtil.dip2px(context, 5);
            float[] f = {i, i, i, i, 0, 0, 0, 0};
            d.setCornerRadii(f);
            d.mutate();
            llayout_coupon_title.setBackgroundDrawable(d);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }
}
