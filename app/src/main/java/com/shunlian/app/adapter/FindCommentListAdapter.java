package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.ui.discover.CommentDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SubCommentItemView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FindCommentListAdapter extends BaseRecyclerAdapter<FindCommentListEntity.ItemComment> {

    private final int ITEM_TITLE = 10;
    public int mHotCommentCount;
    private String mCommentType;
    private OnPointFabulousListener mFabulousListener;

    public FindCommentListAdapter(Context context, List<FindCommentListEntity.ItemComment> lists,
                                  int hotCommentCount, String comment_type) {
        super(context, true, lists);
        mHotCommentCount = hotCommentCount;
        mCommentType = comment_type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TITLE:
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.item_comment_list_title, parent, false);
                return new FindTitleHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHotCommentCount == 0){
            if (position == 0){
                return ITEM_TITLE;
            }
        }else {
            if (position == 0 || position == mHotCommentCount + 1) {
                return ITEM_TITLE;
            }
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case ITEM_TITLE:
                handleTitle(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    private void handleTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FindTitleHolder){
            FindTitleHolder mHolder = (FindTitleHolder) holder;

            if ("all".equals(mCommentType)){
                if (mHotCommentCount != 0){
                    int icon = 0;
                    String title = "";
                    if (position == 0){
                        icon = R.mipmap.icon_pinglun;
                        title = getString(R.string.hot_comment);
                    }else {
                        icon = R.mipmap.icon_zuixin;
                        title = getString(R.string.new_comment);
                    }
                    mHolder.miv_icon.setImageResource(icon);
                    mHolder.mtv_title.setText(title);
                }else {
                    mHolder.miv_icon.setImageResource(R.mipmap.icon_zuixin);
                    mHolder.mtv_title.setText(getString(R.string.new_comment));
                }
            }else {
                mHolder.miv_icon.setImageResource(R.mipmap.icon_found_jingxuan);
                mHolder.mtv_title.setText(getString(R.string.select_comment));
            }
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (mHotCommentCount == 0 ? 1 : 2);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_list, parent, false);
        return new FindCommentListHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

        FindCommentListHolder mHolder = (FindCommentListHolder) holder;
        position = getPosition(position);

        FindCommentListEntity.ItemComment itemComment = lists.get(position);

        GlideUtils.getInstance().loadImage(context,mHolder.civ_head,itemComment.avatar);

        mHolder.mtv_name.setText(itemComment.nickname);

        Bitmap bitmap = TransformUtil.convertVIP(context, itemComment.level);
        mHolder.miv_vip.setImageBitmap(bitmap);

        mHolder.mtv_time.setText(itemComment.add_time);

        mHolder.mtv_zan_count.setText(itemComment.likes);

        mHolder.mtv_content.setText(itemComment.content);

        if ("1".equals(itemComment.had_like)){
            mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
            mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
        }else {
            mHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
            mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
        }

        List<FindCommentListEntity.ReplyList> reply_list = itemComment.reply_list;

        if (isEmpty(reply_list)){
            mHolder.ll_sub_bg.setVisibility(View.GONE);
            mHolder.miv_sanjiao.setVisibility(View.GONE);
        }else {
            mHolder.ll_sub_bg.setVisibility(View.VISIBLE);
            mHolder.miv_sanjiao.setVisibility(View.VISIBLE);
            //回复
            reply(mHolder, itemComment, reply_list);
        }

        if (position==1 || position == mHotCommentCount + (mHotCommentCount == 0 ? 1 : 2)){
            mHolder.view_line.setVisibility(View.GONE);
        }else {
            mHolder.view_line.setVisibility(View.VISIBLE);
        }
    }

    private void reply(FindCommentListHolder mHolder, FindCommentListEntity.ItemComment itemComment,
                       List<FindCommentListEntity.ReplyList> reply_list) {
        mHolder.ll_sub_bg.removeAllViews();
        for (int j = 0; j < reply_list.size(); j++) {
            SubCommentItemView view = new SubCommentItemView(context);
            FindCommentListEntity.ReplyList replyList = reply_list.get(j);
            if (!isEmpty(replyList.at)){
                String source = replyList.at.concat(replyList.reply);
                SpannableStringBuilder changetextbold = Common.changetextbold(source, replyList.at);
                view.setContent(changetextbold);
            }else {
                view.setContent(replyList.reply);
            }
            view.setHeadPic(replyList.reply_avatar)
                    .setName(replyList.reply_by)
                    .setTime(replyList.reply_time);
            if ("1".equals(itemComment.has_more_reply)){
                if (j+1 == reply_list.size()){
                    view.setMoreCount(true,itemComment.reply_count);
                }else {
                    view.setMoreCount(false,null);
                }
            }else {
                view.setMoreCount(false,null);
            }
            mHolder.ll_sub_bg.addView(view);
        }
    }

    public int getPosition(int position) {
        if (mHotCommentCount != 0) {
            if (position < mHotCommentCount + 1) {
                position = position - 1;
            } else {
                position = position - 2;
            }
        } else {
            position = position - 1;
        }
        return position;
    }


    public class FindCommentListHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        public FindCommentListHolder(View itemView) {
            super(itemView);
            GradientDrawable background = (GradientDrawable) ll_sub_bg.getBackground();
            background.setColor(getColor(R.color.white_ash));
            itemView.setOnClickListener(this);
            ll_sub_bg.setOnClickListener(this);
            mtv_zan_count.setOnClickListener(this);
            miv_zan.setOnClickListener(this);
            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(miv_zan,i,i,i,i);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.miv_zan:
                case R.id.mtv_zan_count:
                    if (mFabulousListener != null){
                        mFabulousListener.onPointFabulous(getAdapterPosition());
                    }
                    break;
                case R.id.ll_sub_bg:
                    if ("all".equals(mCommentType)) {
                        FindCommentListEntity.ItemComment itemComment = lists
                                .get(FindCommentListAdapter.this.getPosition(getAdapterPosition()));
                        CommentDetailAct.startAct(context,null, itemComment.item_id);
                    }
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
        }
    }


    public void setPointFabulousListener(OnPointFabulousListener fabulousListener){

        mFabulousListener = fabulousListener;
    }

    public interface OnPointFabulousListener{
        void onPointFabulous(int position);
    }
}
