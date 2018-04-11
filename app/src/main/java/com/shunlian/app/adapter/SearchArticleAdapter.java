package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.utils.HighLightKeyWordUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/19.
 */

public class SearchArticleAdapter extends BaseRecyclerAdapter<ArticleEntity.Article> {

    public SearchArticleAdapter(Context context, List<ArticleEntity.Article> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_search_artice, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder viewHolder = (ArticleViewHolder) holder;
            ArticleEntity.Article article = lists.get(position);
            viewHolder.tv_title.setText(HighLightKeyWordUtil.getHighLightKeyWord(R.color.value_40A5FF, article.title, article.full_title_keywords));
            viewHolder.tv_content.setText(HighLightKeyWordUtil.getHighLightKeyWord(R.color.value_40A5FF, article.full_title, article.full_title_keywords));
            viewHolder.tv_from.setText("来源：" + article.pub_by);
            viewHolder.tv_date.setText(article.pub_time);
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


    public class ArticleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_from)
        TextView tv_from;

        @BindView(R.id.tv_date)
        TextView tv_date;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
