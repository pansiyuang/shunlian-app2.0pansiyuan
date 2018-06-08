package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.ExchangDetailEntity;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.ui.discover.other.CommentDetailAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SubCommentItemView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ExperienceDetailAdapter extends BaseRecyclerAdapter<FindCommentListEntity.ItemComment> {


    private ExchangDetailEntity.ExperienceInfo mExperience_info;
    private static final int ITEM_HEAD = 666;
    private static final int ITEM_TITLE = 888;
    private final LayoutInflater inflater;

    public ExperienceDetailAdapter(Context context,boolean isShowFooter,
                                   ExchangDetailEntity.ExperienceInfo experience_info,
                                   List<FindCommentListEntity.ItemComment> lists) {
        super(context, isShowFooter, lists);
        mExperience_info = experience_info;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_HEAD:
                View view = inflater.inflate(R.layout.item_experience,
                        parent, false);
                return new ExperienceDetailTitleHolder(view);
            case ITEM_TITLE:
                View view1 = inflater.inflate(R.layout.item_comment_list_title,
                        parent, false);
                return new FindTitleHolder(view1);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mExperience_info != null) {
            return ITEM_HEAD;
        } else if (position == 1) {
            return ITEM_TITLE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount()
                + (mExperience_info == null ? 0 : 1)//头部内容
                + 1 //评论条目
                + (isEmpty(lists)? 1:0);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_comment_list, parent, false);
        return new ExperienceDetailHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_HEAD:
                handleTitle(holder, position);
                break;
            case ITEM_TITLE:
                handleDivision(holder, position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handleDivision(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FindTitleHolder) {
            FindTitleHolder mHolder = (FindTitleHolder) holder;
            mHolder.mtv_title.setText(getString(R.string.comments));
        }
    }

    private void handleTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExperienceDetailTitleHolder) {
            ExperienceDetailTitleHolder mHolder = (ExperienceDetailTitleHolder) holder;
            ExchangDetailEntity.MemberInfo member_info = mExperience_info.member_info;
            if (member_info != null){
                GlideUtils.getInstance().loadCircleImage(context, mHolder.miv_avatar, member_info.avatar);
                mHolder.tv_name.setText(member_info.nickname);
            }
            mHolder.tv_date.setText(mExperience_info.add_time);
            mHolder.tv_content.setText(mExperience_info.content);
            if (mExperience_info.goods != null && !isEmpty(mExperience_info.goods.title)) {
                visible(mHolder.ll_goods);
                ExchangDetailEntity.GoodsBean goods = mExperience_info.goods;
                GlideUtils.getInstance().loadImage(context, mHolder.miv_icon, goods.thumb);
                mHolder.tv_title.setText(goods.title);
                mHolder.tv_price.setText(getString(R.string.rmb)+goods.price);
            } else {
                gone(mHolder.ll_goods);
            }
            mHolder.tv_comment_count.setText(mExperience_info.comment_num);
            mHolder.tv_evaluate_count.setText(mExperience_info.praise_num);
            Drawable drawable = null;
            if ("1".equals(mExperience_info.had_like)) {
                drawable = getDrawable(R.mipmap.icon_found_pinglun_zan_h);
                mHolder.tv_evaluate_count.setTextColor(getColor(R.color.pink_color));
            } else {
                drawable = getDrawable(R.mipmap.icon_found_pinglun_zan_n);
                mHolder.tv_evaluate_count.setTextColor(getColor(R.color.share_text));
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mHolder.tv_evaluate_count.setCompoundDrawables(drawable, null, null, null);

            if (!isEmpty(mExperience_info.image)) {
                visible(mHolder.recycler_img);
                GridImageAdapter gridImageAdapter = new GridImageAdapter(context, mExperience_info.image);
                mHolder.recycler_img.setAdapter(gridImageAdapter);
                gridImageAdapter.setOnItemClickListener((view, position1) -> {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) mExperience_info.image;
                    bigImgEntity.index = position1;
                    LookBigImgAct.startAct(context, bigImgEntity);
                });
            } else {
                gone(mHolder.recycler_img);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (isEmpty(payloads))
            super.onBindViewHolder(holder, position, payloads);
        else {
            if (payloads.get(0) instanceof ExchangDetailEntity.ExperienceInfo) {
                ExchangDetailEntity.ExperienceInfo info = (ExchangDetailEntity.ExperienceInfo) payloads.get(0);
                if (holder instanceof ExperienceDetailTitleHolder) {
                    ExperienceDetailTitleHolder mHolder = (ExperienceDetailTitleHolder) holder;
                    Drawable drawable = null;
                    if ("1".equals(info.had_like)) {
                        drawable = getDrawable(R.mipmap.icon_found_pinglun_zan_h);
                        mHolder.tv_evaluate_count.setTextColor(getColor(R.color.pink_color));
                    } else {
                        drawable = getDrawable(R.mipmap.icon_found_pinglun_zan_n);
                        mHolder.tv_evaluate_count.setTextColor(getColor(R.color.share_text));
                    }
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    mHolder.tv_evaluate_count.setCompoundDrawables(drawable, null, null, null);
                    mHolder.tv_evaluate_count.setText(info.praise_num);
                }
            }else if (payloads.get(0) instanceof FindCommentListEntity.ItemComment){
                FindCommentListEntity.ItemComment itemComment = (FindCommentListEntity.ItemComment) payloads.get(0);
                if (holder instanceof ExperienceDetailHolder) {
                    ExperienceDetailHolder mHolder = (ExperienceDetailHolder) holder;

                    mHolder.mtv_zan_count.setText(itemComment.likes);
                    if ("1".equals(itemComment.had_like)) {
                        mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
                        mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
                    } else {
                        mHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
                        mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
                    }
                }
            }
        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExperienceDetailHolder) {
            ExperienceDetailHolder mHolder = (ExperienceDetailHolder) holder;
            if (isEmpty(lists)){
                gone(mHolder.rlayout_comment);
                visible(mHolder.nei_empty);
                mHolder.nei_empty.setImageResource(R.mipmap.img_empty_common)
                        .setText("我来说两句").setButtonText("");
                return;
            }else {
                visible(mHolder.rlayout_comment);
                gone(mHolder.nei_empty);
            }

            FindCommentListEntity.ItemComment itemComment = lists.get(position - 2);

            GlideUtils.getInstance().loadImage(context, mHolder.civ_head, itemComment.avatar);

            mHolder.mtv_name.setText(itemComment.nickname);

            Bitmap bitmap = TransformUtil.convertVIP(context, itemComment.level);
            mHolder.miv_vip.setImageBitmap(bitmap);

            mHolder.mtv_time.setText(itemComment.add_time);

            mHolder.mtv_zan_count.setText(itemComment.likes);

            mHolder.mtv_content.setText(itemComment.content);

            if ("1".equals(itemComment.had_like)) {
                mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
                mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
            } else {
                mHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
                mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
            }

            List<FindCommentListEntity.ReplyList> reply_list = itemComment.reply_list;

            if (isEmpty(reply_list)) {
                gone(mHolder.ll_sub_bg, mHolder.miv_sanjiao);
            } else {
                visible(mHolder.ll_sub_bg, mHolder.miv_sanjiao);
                //回复
                reply(mHolder, itemComment, reply_list);
            }
        }
    }

    private void reply(ExperienceDetailHolder mHolder, FindCommentListEntity.ItemComment itemComment,
                       List<FindCommentListEntity.ReplyList> reply_list) {
        mHolder.ll_sub_bg.removeAllViews();
        for (int j = 0; j < reply_list.size(); j++) {
            SubCommentItemView view = new SubCommentItemView(context);
            FindCommentListEntity.ReplyList replyList = reply_list.get(j);
            if (!isEmpty(replyList.at)) {
                String source = replyList.at+replyList.reply;
                SpannableStringBuilder changetextbold = Common.changetextbold(source, replyList.at);
                view.setContent(changetextbold);
            } else {
                view.setContent(replyList.reply);
            }
            view.setHeadPic(replyList.reply_avatar)
                    .setName(replyList.reply_by)
                    .setTime(replyList.reply_time);
            if ("1".equals(itemComment.has_more_reply)) {
                if (j + 1 == reply_list.size()) {
                    view.setMoreCount(true, itemComment.reply_count);
                } else {
                    view.setMoreCount(false, null);
                }
            } else {
                view.setMoreCount(false, null);
            }
            mHolder.ll_sub_bg.addView(view);
        }
    }


    public class ExperienceDetailHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.ll_sub_bg)
        LinearLayout ll_sub_bg;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.miv_sanjiao)
        MyImageView miv_sanjiao;

        @BindView(R.id.rlayout_comment)
        RelativeLayout rlayout_comment;

        @BindView(R.id.nei_empty)
        NetAndEmptyInterface nei_empty;

        public ExperienceDetailHolder(View itemView) {
            super(itemView);
            mtv_zan_count.setOnClickListener(this);
            miv_zan.setOnClickListener(this);
            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(miv_zan, i, i, i, i);
            TransformUtil.expandViewTouchDelegate(mtv_zan_count, i, i, i, i);
            itemView.setOnClickListener(this);
            ll_sub_bg.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.miv_zan:
                case R.id.mtv_zan_count:
                    DefMessageEvent event = new DefMessageEvent();
                    event.praisePosition = getAdapterPosition();
                    EventBus.getDefault().post(event);
                    break;
                case R.id.ll_sub_bg:
                    if ("0".equals(mExperience_info.check_comment)) {
                        FindCommentListEntity.ItemComment itemComment = lists
                                .get(getAdapterPosition()-2);
                        CommentDetailAct.startAct(context,mExperience_info.id, itemComment.item_id);
                    }
                    break;
                default:
                    if (listener != null && !isEmpty(lists)){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
            }
        }
    }

    public class ExperienceDetailTitleHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.miv_avatar)
        MyImageView miv_avatar;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_add_car)
        TextView tv_add_car;

        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        @BindView(R.id.tv_evaluate_count)
        TextView tv_evaluate_count;

        @BindView(R.id.recycler_img)
        RecyclerView recycler_img;

        @BindView(R.id.ll_goods)
        LinearLayout ll_goods;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;


        public ExperienceDetailTitleHolder(View itemView) {
            super(itemView);
            tv_evaluate_count.setOnClickListener(this);
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            GridSpacingItemDecoration gridSpacingItemDecoration = new
                    GridSpacingItemDecoration(TransformUtil.dip2px(context, 6f), false);
            ((DefaultItemAnimator) recycler_img.getItemAnimator()).setSupportsChangeAnimations(false);
            recycler_img.setLayoutManager(manager);
            recycler_img.setNestedScrollingEnabled(false);
            recycler_img.addItemDecoration(gridSpacingItemDecoration);
            itemView.setOnClickListener(this);
            ll_goods.setOnClickListener(this);
            tv_add_car.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_evaluate_count:
                    DefMessageEvent event = new DefMessageEvent();
                    event.praisePosition = getAdapterPosition();
                    EventBus.getDefault().post(event);
                    break;
                case R.id.ll_goods:
                        break;
                case R.id.tv_add_car:
                    String id = mExperience_info.goods.id;
                    GoodsDetailAct.startAct(context,id);
                    break;
                default:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
            }
        }
    }

    public class FindTitleHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        public FindTitleHolder(View itemView) {
            super(itemView);
            gone(miv_icon);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    mtv_title.getLayoutParams();
            int i = TransformUtil.dip2px(context, 10);
            layoutParams.leftMargin = i;
            mtv_title.setLayoutParams(layoutParams);
        }
    }
}
