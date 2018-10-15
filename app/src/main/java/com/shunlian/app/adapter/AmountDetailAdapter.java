package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.AmountDetailEntity;
import com.shunlian.app.ui.balance.AlipayDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/23.
 */

public class AmountDetailAdapter extends BaseRecyclerAdapter<AmountDetailEntity.Content> {

    public AmountDetailAdapter(Context context, boolean isShowFooter,
                               List<AmountDetailEntity.Content> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_amount_detail, parent, false);
        return new ActivityMoreHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        ActivityMoreHolder mHolder = (ActivityMoreHolder) holder;
        AmountDetailEntity.Content ad = lists.get(position);
        if (isEmpty(ad.image)){
            mHolder.miv_logo.setVisibility(View.GONE);
        }else {
            mHolder.miv_logo.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadCircleImage(context,mHolder.miv_logo,ad.image);
            mHolder.miv_logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ("1".equals(ad.is_click)&&!isEmpty(ad.dialog_text)){
                        initHintDialog((Activity) context,ad.dialog_text,
                                getString(R.string.balance_fanhui),
                                getString(R.string.balance_jiebangzhanghu));
                    }
                }
            });
        }
        mHolder.mtv_desc.setText(ad.value);
        mHolder.mtv_title.setText(ad.name);
    }

    public void initHintDialog(Activity activity, String title, String left, String right) {
        PromptDialog promptDialog = new PromptDialog(activity);
        promptDialog.setSureAndCancleListener(title, right, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
                AlipayDetailAct.startAct(activity,true);
            }
        }, left, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptDialog.dismiss();
            }
        }).show();
    }
    public class ActivityMoreHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.miv_logo)
        MyImageView miv_logo;

        public ActivityMoreHolder(View itemView) {
            super(itemView);
        }
    }
}
