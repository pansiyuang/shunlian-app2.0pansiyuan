package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.FastClickListener.isFastClick;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ArticleAdapter extends BaseRecyclerAdapter<ArticleEntity.Article> {
    public static final int LAYOUT_SMALL = 10004;
    public static final int LAYOUT_BIG = 10005;
    private DiscoverJingxuanFrag mFragment;

    public ArticleAdapter(Context context, List<ArticleEntity.Article> lists, DiscoverJingxuanFrag frag) {
        super(context, false, lists);
        this.mFragment = frag;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LAYOUT_SMALL:
                return new SmallHolderView(LayoutInflater.from(context).inflate(R.layout.item_chosen_small, parent, false));
            case LAYOUT_BIG:
                return new BigHolderView(LayoutInflater.from(context).inflate(R.layout.item_chosen_big, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == LAYOUT_SMALL) {
            handleSmall(holder, position);
        } else if (getItemViewType(position) == LAYOUT_BIG) {
            handleBig(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ArticleEntity.Article article = lists.get(position);
        if ("0".equals(article.thumb_type)) {
            return LAYOUT_SMALL;
        } else {
            return LAYOUT_BIG;
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

    public void handleSmall(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SmallHolderView) {
            SmallHolderView smallHolderView = (SmallHolderView) holder;
            final ArticleEntity.Article article = lists.get(position);
            smallHolderView.tv_title.setText(article.title);
            GlideUtils.getInstance().loadImage(context, smallHolderView.miv_icon, article.thumb);
            smallHolderView.tv_share_count.setText(article.forwards);
            smallHolderView.tv_comment_count.setText(article.comments);
            if ("0".equals(article.had_like)) {
                smallHolderView.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_n);
            } else {
                smallHolderView.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_h);
            }
            smallHolderView.tv_evaluate_count.setText(article.likes);

            if (!isEmpty(article.tags)) {
                smallHolderView.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                smallHolderView.tv_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
            }
            smallHolderView.ll_evaluate.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    public void handleBig(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BigHolderView) {
            BigHolderView bigHolderView = (BigHolderView) holder;
            final ArticleEntity.Article article = lists.get(position);
            bigHolderView.tv_title.setText(article.title);
            GlideUtils.getInstance().loadImage(context, bigHolderView.miv_icon, article.thumb);
            bigHolderView.tv_share_count.setText(article.forwards);
            bigHolderView.tv_comment_count.setText(article.comments);
            if ("0".equals(article.had_like)) {
                bigHolderView.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_n);
            } else {
                bigHolderView.miv_evaluate.setImageResource(R.mipmap.icon_found_pinglun_zan_h);
            }
            bigHolderView.tv_evaluate_count.setText(article.likes);

            if (!isEmpty(article.tags)) {
                bigHolderView.tv_content.setMovementMethod(LinkMovementMethod.getInstance());
                bigHolderView.tv_content.setText(addClickablePart(article.tags, article.full_title), TextView.BufferType.SPANNABLE);
            }

            bigHolderView.ll_evaluate.setOnClickListener(new View.OnClickListener() {
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
        }
    }

    private SpannableStringBuilder addClickablePart(List<ArticleEntity.Tag> mTags, String content) {
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
            final String name = str[i];
            final int start = sb.toString().indexOf(name);
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件
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

    public class SmallHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

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


        public SmallHolderView(View itemView) {
            super(itemView);
        }
    }

    public class BigHolderView extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

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

        public BigHolderView(View itemView) {
            super(itemView);
        }
    }
}
