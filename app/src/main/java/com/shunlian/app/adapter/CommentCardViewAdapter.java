package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/11.
 */

public class CommentCardViewAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.Comments> {

    private static final int FOOTER = 2;
    private final int margin,space;

    public CommentCardViewAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.Comments> lists) {
        super(context, isShowFooter, lists);
        margin = TransformUtil.dip2px(context, 12);
        space = TransformUtil.dip2px(context, 5);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case FOOTER:
                View inflate = LayoutInflater.from(context).inflate(R.layout.cardview_footer, parent, false);
                return new FooterHolder(inflate);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount())
            return FOOTER;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case FOOTER:
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_comment_cardview, parent, false);
        return new CommentCardViewHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentCardViewHolder){
            CommentCardViewHolder cardViewHolder = (CommentCardViewHolder) holder;
            GoodsDeatilEntity.Comments comments = lists.get(position);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                    cardViewHolder.card_rootview.getLayoutParams();
            if (position == 0){
                layoutParams.leftMargin = margin;
            }else {
                layoutParams.leftMargin = 0;
            }
            if (Build.VERSION.SDK_INT >= 21){
                layoutParams.rightMargin = space * 2;
            }else {
                layoutParams.rightMargin = space;
            }
            cardViewHolder.card_rootview.setLayoutParams(layoutParams);

            if (!isEmpty(comments.pics)) {
                cardViewHolder.mrlayout_pic.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImage(context,
                        cardViewHolder.miv_show_pic, comments.pics.get(0));

                cardViewHolder.mtv_num.setVisibility(View.VISIBLE);
                String format = "共%s张";
                cardViewHolder.mtv_num.setText(String.format(format,comments.pics.size()));
            }else {
                cardViewHolder.mrlayout_pic.setVisibility(View.GONE);
                cardViewHolder.mtv_num.setVisibility(View.GONE);
            }

            GlideUtils.getInstance().loadCircleHeadImage(context,
                    cardViewHolder.civ_head,comments.avatar);

            cardViewHolder.mtv_content.setText(comments.content);
            cardViewHolder.mtv_nickname.setText(comments.nickname);
            Bitmap bitmap = TransformUtil.convertNewVIP(context, comments.vip_level);
            if (bitmap != null) {
                cardViewHolder.miv_vip.setVisibility(View.VISIBLE);
                cardViewHolder.miv_vip.setImageBitmap(bitmap);
            }else {
                cardViewHolder.miv_vip.setVisibility(View.GONE);
            }

            if (!isEmpty(comments.plus_role)){//大于0为plus以上等级，1PLUS店主，2主管，>=3经理
                visible(cardViewHolder.miv_medal);
                int plusRole = Integer.parseInt(comments.plus_role);
                if (plusRole == 1){
                    cardViewHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_dianzhu);
                }else if (plusRole == 2){
                    cardViewHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_zhuguan);
                }else if (plusRole >= 3){
                    cardViewHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_jingli);
                }else {
                    gone(cardViewHolder.miv_medal);
                }
            }else {
                gone(cardViewHolder.miv_medal);
            }
        }
    }

    public class FooterHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mll_bg)
        MyLinearLayout mll_bg;

        @BindView(R.id.card_rootview)
        CardView card_rootview;

        public FooterHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            RecyclerView.LayoutParams rootParams = (RecyclerView.LayoutParams)
                    card_rootview.getLayoutParams();
            rootParams.height = TransformUtil.dip2px(context,117);
            int top = TransformUtil.dip2px(context, 16);
            int bottom = TransformUtil.dip2px(context, 19);
            rootParams.topMargin = top;
            rootParams.rightMargin = margin;
            rootParams.bottomMargin = bottom;
            card_rootview.setLayoutParams(rootParams);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }

    public class CommentCardViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.card_rootview)
        CardView card_rootview;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.mtv_nickname)
        MyTextView mtv_nickname;

        @BindView(R.id.civ_head)
        MyImageView civ_head;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.miv_show_pic)
        MyImageView miv_show_pic;

        @BindView(R.id.mtv_num)
        MyTextView mtv_num;

        @BindView(R.id.mrlayout_pic)
        MyRelativeLayout mrlayout_pic;

        @BindView(R.id.miv_medal)
        MyImageView miv_medal;

        public CommentCardViewHolder(View itemView) {
            super(itemView);
            card_rootview.setOnClickListener(this);
            mtv_content.setWHProportion(261,99);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                    card_rootview.getLayoutParams();
            layoutParams.height = TransformUtil.dip2px(context,117);
            int top = TransformUtil.dip2px(context, 16);
            int bottom = TransformUtil.dip2px(context, 19);
            layoutParams.topMargin = top;
            layoutParams.bottomMargin = bottom;
            card_rootview.setLayoutParams(layoutParams);

        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
