package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CommentBottmDialog;
import com.shunlian.app.widget.CommentToolBottomDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SubCommentItemView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class FindCommentListAdapter extends BaseRecyclerAdapter<FindCommentListEntity.ItemComment> {

    //    private final int ITEM_TITLE = 10;
    public int mHotCommentCount;
    private String mCommentType;
    private CommentBottmDialog mDialog;
    private CommentToolBottomDialog mToolDialog;
    private PromptDialog mPromptDialog;
    private OnPointFabulousListener mFabulousListener;

    public FindCommentListAdapter(Context context, List<FindCommentListEntity.ItemComment> lists, int hotCommentCount, String comment_type) {
        super(context, true, lists);
        mHotCommentCount = hotCommentCount;
        mCommentType = comment_type;
    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case ITEM_TITLE:
//                View view = LayoutInflater.from(context).inflate(R.layout.item_comment_list_title, parent, false);
//                return new FindTitleHolder(view);
//            default:
//                return super.onCreateViewHolder(parent, viewType);
//        }
//    }

//    @Override
//    public int getItemViewType(int position) {
//        if (mHotCommentCount == 0){
//            if (position == 0){
//                return ITEM_TITLE;
//            }
//        }else {
//            if (position == 0 || position == mHotCommentCount + 1) {
//                return ITEM_TITLE;
//            }
//        }
//        return super.getItemViewType(position);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        int itemViewType = getItemViewType(position);
//        switch (itemViewType) {
//            case ITEM_TITLE:
//                handleTitle(holder,position);
//                break;
//            default:
//                super.onBindViewHolder(holder, position);
//                break;
//        }
//
//    }
//
//    private void handleTitle(RecyclerView.ViewHolder holder, int position) {
//        if (holder instanceof FindTitleHolder){
//            FindTitleHolder mHolder = (FindTitleHolder) holder;
//
//            if ("all".equals(mCommentType)){
//                if (mHotCommentCount != 0){
//                    int icon = 0;
//                    String title = "";
//                    if (position == 0){
//                        icon = R.mipmap.icon_zuixin;
//                        title = getString(R.string.hot_comment);
//                    }else {
//                        icon = R.mipmap.icon_pinglun;
//                        title = getString(R.string.new_comment);
//                    }
//                    mHolder.miv_icon.setImageResource(icon);
//                    mHolder.mtv_title.setText(title);
//                }else {
//                    mHolder.miv_icon.setImageResource(R.mipmap.icon_zuixin);
//                    mHolder.mtv_title.setText(getString(R.string.new_comment));
//                }
//            }else {
//                mHolder.miv_icon.setImageResource(R.mipmap.icon_found_jingxuan);
//                mHolder.mtv_title.setText(getString(R.string.select_comment));
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return super.getItemCount() + (mHotCommentCount == 0 ? 1 : 2);
//    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_list, parent, false);
        return new FindCommentListHolder(view);
    }

    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white));
        baseFooterHolder.layout_no_more.setText("~没有更多~");
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        FindCommentListHolder mHolder = (FindCommentListHolder) holder;
        FindCommentListEntity.ItemComment itemComment = lists.get(position);
        if (itemComment != null) {
            GlideUtils.getInstance().loadCircleHeadImage(context, mHolder.civ_head, itemComment.avatar);
            mHolder.mtv_name.setText(itemComment.nickname);
//        if (!isEmpty(itemComment.level)) {
//            Bitmap bitmap = TransformUtil.convertNewVIP(context, itemComment.level);
//            mHolder.miv_vip.setImageBitmap(bitmap);
//            mHolder.miv_vip.setVisibility(View.VISIBLE);
//        } else {
            mHolder.miv_vip.setVisibility(View.GONE);
//        }

            mHolder.mtv_time.setText(itemComment.create_time);

            if (itemComment.like_count == 0) {
                mHolder.tv_zan.setText("点赞");
            } else {
                mHolder.tv_zan.setText(String.valueOf(itemComment.like_count));
            }

            List<FindCommentListEntity.ItemComment> reply_list = itemComment.reply_list;

            if (isEmpty(reply_list)) {
                gone(mHolder.ll_sub_bg);
            } else {
                visible(mHolder.ll_sub_bg);
                //回复
                reply(mHolder, position, itemComment, reply_list);
            }

            if (position == 1 || position == mHotCommentCount + (mHotCommentCount == 0 ? 1 : 2)) {
                gone(mHolder.view_line);
            } else {
                visible(mHolder.view_line);
            }

            if (!isEmpty(itemComment.expert_icon)) {
                visible(mHolder.miv_expert);
                GlideUtils.getInstance().loadImage(context, mHolder.miv_expert, itemComment.expert_icon);
            } else {
                gone(mHolder.miv_expert);
            }

            if (!isEmpty(itemComment.v_icon)) {
                visible(mHolder.miv_v);
                GlideUtils.getInstance().loadImage(context, mHolder.miv_v, itemComment.v_icon);
            } else {
                gone(mHolder.miv_v);
            }

            mHolder.animation_zan.setAnimation("praise.json");
            mHolder.animation_zan.loop(false);
            mHolder.animation_zan.setImageAssetsFolder("images/");
            if ("1".equals(itemComment.like_status)) {
                mHolder.animation_zan.setProgress(1f);
                mHolder.tv_zan.setTextColor(getColor(R.color.pink_color));
                mHolder.ll_zan.setClickable(false);
            } else {
                mHolder.animation_zan.setProgress(0f);
                mHolder.tv_zan.setTextColor(getColor(R.color.text_gray2));
                mHolder.ll_zan.setClickable(true);
            }

            mHolder.tv_reply.setText(itemComment.is_self == 1 ? "删除" : "回复");
            gone(mHolder.miv_medal);
            if (itemComment.check_is_show == 1) {
                mHolder.tv_verify.setText("审核");
                mHolder.tv_verify.setTextColor(getColor(R.color.pink_color));
            } else if (itemComment.check_is_show == 2) {
                mHolder.tv_verify.setText("撤回");
                mHolder.tv_verify.setTextColor(getColor(R.color.text_gray2));
            }

            if (itemComment.status == 3) { // 0未审核，1已审核，2已驳回，3已删除
                mHolder.mtv_content.setTextColor(getColor(R.color.color_value_6c));
                mHolder.mtv_content.setText("该条评论已被删除");
                mHolder.mtv_content.setLongClickable(false);
                gone(mHolder.ll_zan, mHolder.tv_verify, mHolder.tv_reply);
            } else {
                mHolder.mtv_content.setTextColor(getColor(R.color.value_484848));
                visible(mHolder.ll_zan, mHolder.tv_verify, mHolder.tv_reply);
                mHolder.mtv_content.setText(itemComment.content);
                mHolder.mtv_content.setLongClickable(true);
                mHolder.tv_verify.setVisibility(itemComment.check_is_show == 0 ? View.GONE : View.VISIBLE);  //审核按钮是否显示，0不显示任何按钮，1显示审核按钮，2显示撤回按钮
            }

            mHolder.mtv_content.setOnLongClickListener(v -> {
                showCommentToolDialog(itemComment, position, -1);
                return false;
            });
        }
    }

    private void reply(FindCommentListHolder mHolder, int position, FindCommentListEntity.ItemComment itemComment, final List<FindCommentListEntity.ItemComment> reply_list) {
        mHolder.ll_sub_bg.removeAllViews();
        int maxSize;
        if (reply_list.size() < 3) {
            maxSize = reply_list.size();
        } else {
            maxSize = 3;
        }
        for (int j = 0; j < maxSize; j++) {
            SubCommentItemView view = new SubCommentItemView(context);
            FindCommentListEntity.ItemComment replyList = reply_list.get(j);
            view.setCommentData(replyList);
            if (isEmpty(replyList.reply_member)) {
                view.setName(replyList.nickname + "说：");
            } else {
                String content = replyList.nickname + " 回复 " + replyList.reply_member;
                SpannableString spannableString = new SpannableString(content);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#484848"));
                spannableString.setSpan(colorSpan, replyList.nickname.length(), spannableString.length() - replyList.reply_member.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                view.setName(spannableString);
            }
            view.setHeadPic(replyList.avatar)
                    .setTime(replyList.create_time)
                    .setContent(replyList.content);
            if (j == maxSize - 1) {
                view.setMoreCount(true, itemComment.reply_count);
            } else {
                view.setMoreCount(false, itemComment.reply_count);
            }

            int finalJ = j;
            view.setOnCallBack(new SubCommentItemView.OnSubCallBack() {
                @Override
                public void OnPraise(String commentId, LottieAnimationView lottieAnimationView) {
                    if (mFabulousListener != null) {
                        mFabulousListener.onPointFabulous(position, finalJ, lottieAnimationView);
                    }
                }

                @Override
                public void OnReply() {
                    if (mFabulousListener != null) {
                        if (replyList.is_self == 1) {
                            mFabulousListener.onDel(position, finalJ);
                        } else {
                            mFabulousListener.onReply(position, finalJ);
                        }
                    }
                }

                @Override
                public void onVerify() {
                    if (mDialog == null) {
                        mDialog = new CommentBottmDialog(context);
                    }
                    mDialog.setCommentData(replyList);
                    mDialog.setOnPassListener(() -> {
                        if (mFabulousListener != null) {
                            mFabulousListener.onVerify(position, finalJ);
                        }
                    });
                    mDialog.show();
                }

                @Override
                public void onRejected() {
                    if (mPromptDialog == null) {
                        mPromptDialog = new PromptDialog((Activity) context);
                        mPromptDialog.setTvSureColor(R.color.white);
                        mPromptDialog.setTvSureBGColor(getColor(R.color.pink_color));
                        mPromptDialog.setSureAndCancleListener("确定撤回当前评论吗？", "确定", v1 -> {
                            if (mFabulousListener != null) {
                                mFabulousListener.onRejected(position, finalJ);
                            }
                            mPromptDialog.dismiss();
                        }, "取消", v12 -> {
                            mPromptDialog.dismiss();
                        });
                    }
                    mPromptDialog.show();
                }

                @Override
                public void onContentLongClick() {
                    showCommentToolDialog(replyList, position, finalJ);
                }
            });
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
        MyImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.ll_sub_bg)
        LinearLayout ll_sub_bg;

        @BindView(R.id.view_line)
        View view_line;

        @BindView(R.id.miv_medal)
        MyImageView miv_medal;

        @BindView(R.id.miv_expert)
        MyImageView miv_expert;

        @BindView(R.id.ll_zan)
        LinearLayout ll_zan;

        @BindView(R.id.animation_zan)
        LottieAnimationView animation_zan;

        @BindView(R.id.tv_zan)
        TextView tv_zan;

        @BindView(R.id.tv_reply)
        TextView tv_reply;

        @BindView(R.id.tv_verify)
        TextView tv_verify;

        @BindView(R.id.miv_v)
        MyImageView miv_v;

        public FindCommentListHolder(View itemView) {
            super(itemView);
            GradientDrawable background = (GradientDrawable) ll_sub_bg.getBackground();
            background.setColor(getColor(R.color.white_ash));
            itemView.setOnClickListener(this);
            ll_sub_bg.setOnClickListener(this);
            ll_zan.setOnClickListener(this);
            tv_reply.setOnClickListener(this);
            tv_verify.setOnClickListener(this);

            //返回键扩大点击范围
            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(tv_reply, i, i, i, i);
            TransformUtil.expandViewTouchDelegate(tv_verify, i, i, i, i);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_zan:
                    if (mFabulousListener != null) {
                        mFabulousListener.onPointFabulous(getAdapterPosition(), -1, animation_zan);
                    }
                    break;
                case R.id.tv_reply:
                    FindCommentListEntity.ItemComment itemComment = lists.get(getAdapterPosition());
                    if (mFabulousListener == null) {
                        return;
                    }
                    if (itemComment.is_self == 1) {
                        mFabulousListener.onDel(getAdapterPosition(), -1);
                    } else {
                        mFabulousListener.onReply(getAdapterPosition(), -1);
                    }
                    break;
                case R.id.tv_verify:
                    FindCommentListEntity.ItemComment comment = lists.get(getAdapterPosition());
                    if (comment.check_is_show == 1) { //审核
                        if (mDialog == null) {
                            mDialog = new CommentBottmDialog(context);
                        }
                        mDialog.setCommentData(comment);
                        mDialog.setOnPassListener(() -> {
                            if (mFabulousListener != null) {
                                mFabulousListener.onVerify(getAdapterPosition(), -1);
                            }
                        });
                        mDialog.show();
                    } else {  //撤回
                        if (mPromptDialog == null) {
                            mPromptDialog = new PromptDialog((Activity) context);
                            mPromptDialog.setTvSureColor(R.color.white);
                            mPromptDialog.setTvSureBGColor(getColor(R.color.pink_color));
                            mPromptDialog.setSureAndCancleListener("确定撤回当前评论吗？", "确定", v1 -> {
                                if (mFabulousListener != null) {
                                    mFabulousListener.onRejected(getAdapterPosition(), -1);
                                }
                                mPromptDialog.dismiss();
                            }, "取消", v12 -> {
                                mPromptDialog.dismiss();
                            });
                        }
                        mPromptDialog.show();
                    }
                    break;
                default:
                    if (listener != null) {
                        listener.onItemClick(v, getAdapterPosition());
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

    public void showCommentToolDialog(FindCommentListEntity.ItemComment itemComment, int parentPosition, int childPosition) {
        if (mToolDialog == null) {
            mToolDialog = new CommentToolBottomDialog(context);
            mToolDialog.setOnToolListener(new CommentToolBottomDialog.OnToolListener() {
                @Override
                public void onReply() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onReply(parentPosition, childPosition);
                    }
                }

                @Override
                public void onDel() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onDel(parentPosition, childPosition);
                    }
                }

                @Override
                public void onVerify() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onVerify(parentPosition, childPosition);
                    }
                }

                @Override
                public void onRejected() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onRejected(parentPosition, childPosition);
                    }
                }
            });
        }
        mToolDialog.setCommentData(itemComment);
        mToolDialog.show();
    }


    public void setPointFabulousListener(OnPointFabulousListener fabulousListener) {
        mFabulousListener = fabulousListener;
    }

    public interface OnPointFabulousListener {
        void onPointFabulous(int position, int childPosition, LottieAnimationView lottieAnimationView);

        void onReply(int position, int childPosition);

        void onDel(int position, int childPosition);

        void onVerify(int position, int childPosition);

        void onRejected(int position, int childPosition);
    }
}
