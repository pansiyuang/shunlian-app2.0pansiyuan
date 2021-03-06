package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
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
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.ui.discover.VideoPlayActivity;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.ui.discover.jingxuan.TagDetailActivity;
import com.shunlian.app.ui.discover_new.comment.CommentListAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.MyOnClickListener.isFastClick;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleEntity.Article> {
    private final int LAYOUT_TOP = 100004;
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
        return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chosen, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LAYOUT_TOP) {
            return new TagViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_tag, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemCount() {
        return lists.size() + 2;
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

            if (!isEmpty(mTags)) {
                chosenTagAdapter = new ChosenTagAdapter(context, mTags);
                tagViewHolder.recycler_tags.setAdapter(chosenTagAdapter);
                chosenTagAdapter.setOnItemClickListener((view, position) -> TagDetailActivity.startAct(context, mTags.get(position).id));
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, TransformUtil.dip2px(context, 10));
                tagViewHolder.itemView.setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            ArticleEntity.Article article = (ArticleEntity.Article) payloads.get(0);

            articleViewHolder.tv_share_count.setText(article.forwards);
            articleViewHolder.tv_evaluate_count.setText(article.likes);
            if ("0".equals(article.had_like)) {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_zan_n);
            } else {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_zan_h);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public void handlerItem(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            final ArticleEntity.Article article = lists.get(position - 1);

            if (!isEmpty(article.title)) {
                articleViewHolder.tv_small_title.setText(article.title);
                articleViewHolder.tv_big_title.setText(article.title);
            }

            GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_small_icon, article.thumb, false);
            GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_big_icon, article.thumb, false);
            articleViewHolder.tv_share_count.setText(article.forwards);
            articleViewHolder.tv_comment_count.setText(article.comments);
            if ("0".equals(article.had_like)) {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_zan_n);
            } else {
                articleViewHolder.miv_evaluate.setImageResource(R.mipmap.icon_found_zan_h);
            }
            articleViewHolder.tv_evaluate_count.setText(article.likes);

            if (isEmpty(article.tags)) {
                articleViewHolder.tv_small_content.setText(article.full_title);
                articleViewHolder.tv_big_content.setText(article.full_title);
            } else {
                articleViewHolder.tv_small_content.setMovementMethod(LinkMovementMethod.getInstance());
                articleViewHolder.tv_big_content.setMovementMethod(LinkMovementMethod.getInstance());
                articleViewHolder.tv_small_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
                articleViewHolder.tv_big_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
            }
            articleViewHolder.ll_evaluate.setOnClickListener(v -> {
                if (isFastClick()) {
                    return;
                }
                if ("0".equals(article.had_like)) {
                    mFragment.toLikeArticle(article.id);
                } else {
                    mFragment.toUnLikeArticle(article.id);
                }
            });
            articleViewHolder.ll_share.setOnClickListener(v -> {
                if (isFastClick()) {
                    return;
                }
                mFragment.shareArticle(position);
            });
            articleViewHolder.ll_comment.setOnClickListener(v -> CommentListAct.startAct(mFragment.getActivity(), article.id));

            articleViewHolder.miv_change.setOnClickListener(v -> {
                if (isFastClick()) {
                    return;
                }
                mFragment.toGetOtherTopiscList();
            });

            setTopicData(article.topic_list, articleViewHolder.ll_change, articleViewHolder.recycler_change);

            if ("0".equals(article.thumb_type)) { //0小图（左右布局，图在右侧），1大图（上下布局，图是通栏显示）, 2九宫格，3小视频
                visible(articleViewHolder.rl_small);
                gone(articleViewHolder.ll_big);
                GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_small_icon, article.thumb);
            } else {
                showViewType(article.thumb_type, articleViewHolder, article);
            }
        }
    }

    public void showViewType(String type, ArticleViewHolder articleViewHolder, ArticleEntity.Article a) {
        visible(articleViewHolder.ll_big);
        gone(articleViewHolder.rl_small);
        switch (type) {
            case "1": //大图模式
                visible(articleViewHolder.miv_big_icon);
                gone(articleViewHolder.recycler_nine);
                gone(articleViewHolder.rl_video);

                int screenWidth = DeviceInfoUtil.getDeviceWidth(context) - TransformUtil.dip2px(context, 20);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) articleViewHolder.miv_big_icon.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                double height = screenWidth / 710d * 350;
                layoutParams.height = (int) Math.round(height);
                articleViewHolder.miv_big_icon.setLayoutParams(layoutParams);
                GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_big_icon, a.thumb);

                articleViewHolder.miv_big_icon.setOnClickListener(view -> {
                    if (!isEmpty(a.id)) {
                        ArticleH5Act.startAct(context, a.id, ArticleH5Act.MODE_SONIC);
                    }
                });
                break;
            case "2"://九宫格模式
                gone(articleViewHolder.miv_big_icon);
                visible(articleViewHolder.recycler_nine);
                gone(articleViewHolder.rl_video);
                if (a.thumb_list != null && a.thumb_list.length != 0) {
                    List<String> imgList = new ArrayList<>();
                    for (String s : a.thumb_list) {
                        imgList.add(s);
                    }
                    BitmapUtil.discoverImg(articleViewHolder.miv_big_icon, articleViewHolder.recycler_nine, null, imgList, mFragment.getActivity()
                            , 0, 0, 10, 12, 10, 12, 6, 20);
                }
                break;
            case "3"://视频模式
                gone(articleViewHolder.miv_big_icon);
                gone(articleViewHolder.recycler_nine);
                visible(articleViewHolder.rl_video);
                GlideUtils.getInstance().loadImage(context, articleViewHolder.miv_video, a.thumb);
                articleViewHolder.miv_video.setOnClickListener(v -> {
                    if (isEmpty(a.video_url)) {
                        return;
                    }
                    VideoPlayActivity.startActivity(context, a);
                });
                break;
        }
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
                notifyItemChanged(i + 1, lists.get(i));
                break;
            }
        }
    }

    public void setTopicData(final List<ArticleEntity.Topic> topicData, LinearLayout linearLayout, RecyclerView recyclerView) {
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

        @BindView(R.id.rl_video)
        RelativeLayout rl_video;

        @BindView(R.id.miv_video)
        MyImageView miv_video;

        @BindView(R.id.recycler_nine)
        RecyclerView recycler_nine;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recycler_change.setLayoutManager(manager);
            recycler_change.setNestedScrollingEnabled(false);
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
