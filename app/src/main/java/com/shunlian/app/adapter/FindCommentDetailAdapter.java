package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
import com.shunlian.app.presenter.FindCommentDetailPresenter;
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
 * Created by Administrator on 2018/3/15.
 */

public class FindCommentDetailAdapter extends BaseRecyclerAdapter<FindCommentListEntity.ItemComment> {

    private final LayoutInflater inflater;
    private OnPointFabulousListener mFabulousListener;
    private PromptDialog promptDialog;
    private CommentBottmDialog mDialog;
    private CommentToolBottomDialog mToolDialog;

    public FindCommentDetailAdapter(Context context, List<FindCommentListEntity.ItemComment> lists) {
        super(context, true, lists);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_comment_list, parent, false);
        return new FindCommentDetailHolder(view);
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
        if (holder instanceof FindCommentDetailHolder) {
            FindCommentDetailHolder mHolder = (FindCommentDetailHolder) holder;
            FindCommentListEntity.ItemComment lastLikesBean = lists.get(position);

            GlideUtils.getInstance().loadCircleHeadImage(context, mHolder.civ_head, lastLikesBean.avatar);

            mHolder.mtv_time.setText(lastLikesBean.create_time);
            mHolder.mtv_content.setText(lastLikesBean.content);
            mHolder.mtv_name.setText(lastLikesBean.nickname);
            mHolder.miv_vip.setVisibility(View.GONE);
            mHolder.miv_medal.setVisibility(View.GONE);

            if (lastLikesBean.like_count == 0) {
                mHolder.tv_zan.setText("点赞");
            } else {
                mHolder.tv_zan.setText(String.valueOf(lastLikesBean.like_count));
            }

            if (!isEmpty(lastLikesBean.expert_icon)) {
                visible(mHolder.miv_expert);
                GlideUtils.getInstance().loadImage(context, mHolder.miv_expert, lastLikesBean.expert_icon);
            } else {
                gone(mHolder.miv_expert);
            }

            if (!isEmpty(lastLikesBean.v_icon)) {
                visible(mHolder.miv_v);
                GlideUtils.getInstance().loadImage(context, mHolder.miv_v, lastLikesBean.v_icon);
            } else {
                gone(mHolder.miv_v);
            }

            if (position == 0) {
                visible(mHolder.view_line);
            } else {
                gone(mHolder.view_line);
            }

            mHolder.animation_zan.setAnimation("praise.json");
            mHolder.animation_zan.loop(false);
            mHolder.animation_zan.setImageAssetsFolder("images/");
            if ("1".equals(lastLikesBean.like_status)) {
                mHolder.animation_zan.setProgress(1f);
                mHolder.tv_zan.setTextColor(getColor(R.color.pink_color));
                mHolder.ll_zan.setClickable(false);
            } else {
                mHolder.animation_zan.setProgress(0f);
                mHolder.tv_zan.setTextColor(getColor(R.color.text_gray2));
                mHolder.ll_zan.setClickable(true);
            }

            mHolder.tv_reply.setText(lastLikesBean.is_self == 1 ? "删除" : "回复");

            if (lastLikesBean.check_is_show == 1) {
                mHolder.tv_verify.setText("审核");
                mHolder.tv_verify.setTextColor(getColor(R.color.pink_color));
            } else if (lastLikesBean.check_is_show == 2) {
                mHolder.tv_verify.setText("撤回");
                mHolder.tv_verify.setTextColor(getColor(R.color.text_gray2));
            }

            if (lastLikesBean.status == 3) { // 0未审核，1已审核，2已驳回，3已删除
                mHolder.mtv_content.setTextColor(getColor(R.color.color_value_6c));
                gone(mHolder.ll_zan, mHolder.tv_verify, mHolder.tv_reply);
            } else {
                mHolder.mtv_content.setTextColor(getColor(R.color.value_484848));
                visible(mHolder.ll_zan, mHolder.tv_verify, mHolder.tv_reply);
                mHolder.tv_verify.setVisibility(lastLikesBean.check_is_show == 0 ? View.GONE : View.VISIBLE);  //审核按钮是否显示，0不显示任何按钮，1显示审核按钮，2显示撤回按钮
            }

            if (isEmpty(lastLikesBean.reply_list)) {
                mHolder.ll_sub_bg.setVisibility(View.GONE);
            } else {
                reply(mHolder.ll_sub_bg, lastLikesBean.reply_list);
            }

            mHolder.mtv_content.setOnLongClickListener(v -> {
                showCommentToolDialog(lastLikesBean, true, -1);
                return false;
            });
        }
    }

    private void reply(LinearLayout ll_sub_bg, List<FindCommentListEntity.ItemComment> reply_list) {
        ll_sub_bg.removeAllViews();
        for (int j = 0; j < reply_list.size(); j++) {
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
                    .setContent(replyList.content)
                    .setMoreCount(false, 0);
            int finalJ = j;
            view.setOnCallBack(new SubCommentItemView.OnSubCallBack() {
                @Override
                public void OnPraise(String commentId, LottieAnimationView lottieAnimationView) {
                    if (FindCommentDetailPresenter.isPlaying) {
                        return;
                    }
                    if (mFabulousListener != null) {
                        mFabulousListener.onPointFabulous(false, finalJ, lottieAnimationView);
                    }
                }

                @Override
                public void OnReply() {
                    if (mFabulousListener != null) {
                        if (replyList.is_self == 1) {
                            mFabulousListener.onDel(false, finalJ);
                        } else {
                            mFabulousListener.onReply(false, finalJ);
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
                            mFabulousListener.onVerify(false, finalJ);
                        }
                    });
                    mDialog.show();
                }

                @Override
                public void onRejected() {
                    if (promptDialog == null) {
                        promptDialog = new PromptDialog((Activity) context);
                        promptDialog.setTvSureColor(R.color.white);
                        promptDialog.setTvSureBGColor(getColor(R.color.pink_color));
                        promptDialog.setSureAndCancleListener("确定撤回当前评论吗？", "确定", v1 -> {
                            if (mFabulousListener != null) {
                                mFabulousListener.onRejected(false, finalJ);
                            }
                            promptDialog.dismiss();
                        }, "取消", v12 -> {
                            promptDialog.dismiss();
                        });
                    }
                    promptDialog.show();
                }

                @Override
                public void onContentLongClick() {
                    showCommentToolDialog(replyList, false, finalJ);
                }
            });
            ll_sub_bg.addView(view);
        }
    }

    public class FindCommentDetailHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        public FindCommentDetailHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ll_zan.setOnClickListener(this);
            tv_reply.setOnClickListener(this);
            tv_verify.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_reply:
                    FindCommentListEntity.ItemComment itemComment = lists.get(0);
                    if (mFabulousListener == null) {
                        return;
                    }
                    if (itemComment.is_self == 1) {
                        mFabulousListener.onDel(true, -1);
                    } else {
                        mFabulousListener.onReply(true, -1);
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
                                mFabulousListener.onVerify(true, -1);
                            }
                        });
                        mDialog.show();
                    } else {  //撤回
                        if (promptDialog == null) {
                            promptDialog = new PromptDialog((Activity) context);
                            promptDialog.setTvSureColor(R.color.white);
                            promptDialog.setTvSureBGColor(getColor(R.color.pink_color));
                            promptDialog.setSureAndCancleListener("确定撤回当前评论吗？", "确定", v1 -> {
                                if (mFabulousListener != null) {
                                    mFabulousListener.onRejected(true, -1);
                                }
                                promptDialog.dismiss();
                            }, "取消", v12 -> {
                                promptDialog.dismiss();
                            });
                        }
                        promptDialog.show();
                    }
                    break;
                case R.id.ll_zan:
                    if (FindCommentDetailPresenter.isPlaying) {
                        return;
                    }
                    if (mFabulousListener != null) {
                        mFabulousListener.onPointFabulous(true, -1, animation_zan);
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

    public void showCommentToolDialog(FindCommentListEntity.ItemComment itemComment, boolean isParent, int childPosition) {
        if (mToolDialog == null) {
            mToolDialog = new CommentToolBottomDialog(context);
            mToolDialog.setOnToolListener(new CommentToolBottomDialog.OnToolListener() {
                @Override
                public void onReply() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onReply(isParent, childPosition);
                    }
                }

                @Override
                public void onDel() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onDel(isParent, childPosition);
                    }
                }

                @Override
                public void onVerify() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onVerify(isParent, childPosition);
                    }
                }

                @Override
                public void onRejected() {
                    if (mFabulousListener != null) {
                        mFabulousListener.onRejected(isParent, childPosition);
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
        void onPointFabulous(boolean isP, int childPosition, LottieAnimationView lottieAnimationView);

        void onReply(boolean isP, int childPosition);

        void onDel(boolean isP, int childPosition);

        void onVerify(boolean isP, int childPosition);

        void onRejected(boolean isP, int childPosition);
    }
}
