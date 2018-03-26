package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.ui.discover.CommentListAct;
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.ui.discover.TagDetailActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.FastClickListener.isFastClick;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleEntity.Article> {
    private final int LAYOUT_TOP = 1004;
    private DiscoverJingxuanFrag mFragment;
    private ChangeTopicAdapter mAdapter;
    private List<ArticleEntity.Tag> mTags;
    private ChosenTagAdapter chosenTagAdapter;

    public ArticleAdapter(Context context, List<ArticleEntity.Article> lists, DiscoverJingxuanFrag frag, List<ArticleEntity.Tag> tags) {
        super(context, true, lists);
        this.mFragment = frag;
        this.mTags = tags;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LAYOUT_TOP) {
            return new TagViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_tag, parent, false));
        }
        return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chosen, parent, false));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LAYOUT_TOP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == LAYOUT_TOP) {
            handlerTag(holder);
        } else {
            handlerItem(holder, position);
        }
    }

    public void handlerTag(RecyclerView.ViewHolder holder) {
        if (holder instanceof TagViewHolder) {
            TagViewHolder tagViewHolder = (TagViewHolder) holder;
            chosenTagAdapter = new ChosenTagAdapter(context, mTags);
            tagViewHolder.recycler_tags.setAdapter(chosenTagAdapter);

            chosenTagAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TagDetailActivity.startAct(context, mTags.get(position).id);
                }
            });
        }
    }

    public void handlerItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            final ArticleEntity.Article article = lists.get(position - 1);

            if ("0".equals(article.thumb_type)) {
                articleViewHolder.rl_small.setVisibility(View.VISIBLE);
                articleViewHolder.ll_big.setVisibility(View.GONE);
            } else {
                articleViewHolder.ll_big.setVisibility(View.VISIBLE);
                articleViewHolder.rl_small.setVisibility(View.GONE);
            }

            articleViewHolder.tv_small_title.setText(article.title);
            articleViewHolder.tv_big_title.setText(article.title);
            GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_small_icon, article.thumb);
            GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_big_icon, article.thumb);
            articleViewHolder.tv_share_count.setText(article.forwards);
            articleViewHolder.tv_comment_count.setText(article.comments);
            if ("0".equals(article.had_like)) {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_n);
            } else {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_h);
            }
            articleViewHolder.tv_evaluate_count.setText(article.likes);

            if (!isEmpty(article.tags)) {
                articleViewHolder.tv_small_content.setMovementMethod(LinkMovementMethod.getInstance());
                articleViewHolder.tv_small_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
                articleViewHolder.tv_big_content.setMovementMethod(LinkMovementMethod.getInstance());
                articleViewHolder.tv_big_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
            }

            articleViewHolder.ll_evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFastClick()) {
                        return;
                    }
                    if ("0".equals(article.had_like)) {
                        mFragment.toLikeArticle(article.id);
                    } else {
                        mFragment.toUnLikeArticle(article.id);
                    }
                }
            });

            articleViewHolder.ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommentListAct.startAct(mFragment.getActivity(), article.id);
                }
            });

            articleViewHolder.miv_change.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFastClick()) {
                        return;
                    }
                    mFragment.toGetOtherTopiscList();
                }
            });

            setTopicData(article.topic_list, articleViewHolder.ll_change, articleViewHolder.recycler_change);
        }
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    public void notityTopicData(List<ArticleEntity.Topic> mTopics) {
        if (!isEmpty(mTopics) && mAdapter != null) {
            mAdapter.setData(mTopics);
        }
    }

    public void updateEvaluate(String articleId, String hadLike) {
        for (int i = 0; i < lists.size(); i++) {
            if (articleId.equals(lists.get(i).id)) {
                lists.get(i).had_like = hadLike;
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void setTopicData(List<ArticleEntity.Topic> topicData, LinearLayout linearLayout, RecyclerView recyclerView) {
        if (isEmpty(topicData)) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);

            mAdapter = new ChangeTopicAdapter(context, topicData);
            recyclerView.setAdapter(mAdapter);
        }
    }

    private SpannableStringBuilder addClickablePart(final List<ArticleEntity.Tag> mTags, String content) {
        StringBuffer sb = new StringBuffer();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < mTags.size(); i++) {
            sb.append("#" + mTags.get(i).name + "#");
            if (i != mTags.size() - 1) {
                sb.append(" ");
            }
        }
        ssb.append(sb);
        String[] str = sb.toString().split(" ");

        for (int i = 0; i < str.length; i++) {
            final int index = i;
            final String name = str[i];
            final int start = sb.toString().indexOf(name);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件
                    TagDetailActivity.startAct(context, mTags.get(index).id);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getColor(R.color.value_299FFA)); // 设置文本颜色
                    ds.setUnderlineText(false); // 去掉下划线
                }
            }, start, start + name.length(), 0);
        }
        return ssb.append(content);
    }

    public class TagViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.recycler_tags)
        RecyclerView recycler_tags;

        public TagViewHolder(View itemView) {
            super(itemView);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recycler_tags.setLayoutManager(manager);
            recycler_tags.setNestedScrollingEnabled(false);
        }

    }

    public class ArticleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_small_title)
        TextView tv_small_title;

        @BindView(R.id.miv_small_icon)
        MyImageView miv_small_icon;

        @BindView(R.id.tv_small_content)
        TextView tv_small_content;

        @BindView(R.id.tv_big_title)
        TextView tv_big_title;

        @BindView(R.id.miv_big_icon)
        MyImageView miv_big_icon;

        @BindView(R.id.tv_big_content)
        TextView tv_big_content;

        @BindView(R.id.ll_share)
        LinearLayout ll_share;

        @BindView(R.id.miv_share)
        MyImageView miv_share;

        @BindView(R.id.tv_share_count)
        TextView tv_share_count;

        @BindView(R.id.ll_comment)
        LinearLayout ll_comment;

        @BindView(R.id.miv_comment)
        MyImageView miv_comment;

        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        @BindView(R.id.ll_evaluate)
        LinearLayout ll_evaluate;

        @BindView(R.id.miv_evaluate)
        MyImageView miv_evaluate;

        @BindView(R.id.tv_evaluate_count)
        TextView tv_evaluate_count;

        @BindView(R.id.ll_change)
        LinearLayout ll_change;

        @BindView(R.id.miv_change)
        MyImageView miv_change;

        @BindView(R.id.recycler_change)
        RecyclerView recycler_change;

        @BindView(R.id.rl_small)
        RelativeLayout rl_small;

        @BindView(R.id.ll_big)
        LinearLayout ll_big;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recycler_change.setLayoutManager(manager);
            recycler_change.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(context, 0.5f), 0, 0, getColor(R.color.background_gray1)));
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
