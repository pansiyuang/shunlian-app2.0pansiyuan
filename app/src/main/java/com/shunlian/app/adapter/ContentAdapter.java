package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.utils.SwipeMenuLayout;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/30.
 */

public class ContentAdapter extends BaseRecyclerAdapter<ArticleEntity.Article> {

    public boolean isEdit;
    private OnSelectListener mListener;

    public ContentAdapter(Context context, List<ArticleEntity.Article> lists) {
        super(context, true, lists);
    }

    public void setEditMode(boolean editMode) {
        isEdit = editMode;
        notifyItemRangeChanged(0, lists.size());
    }

    public boolean getEditMode() {
        return isEdit;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ArticleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_content, parent, false));
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


    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
        ArticleEntity.Article article = lists.get(position);
        articleViewHolder.tv_title.setText(article.title);
        articleViewHolder.tv_favorite_count.setText(article.favorites);
        articleViewHolder.tv_praise_count.setText(article.likes);
        if (isEdit) {
            articleViewHolder.ll_select.setVisibility(View.VISIBLE);
            articleViewHolder.swipe_layout.setSwipeEnable(false);
        } else {
            articleViewHolder.ll_select.setVisibility(View.GONE);
            articleViewHolder.swipe_layout.setSwipeEnable(true);
        }

        if (article.isSelect) {
            articleViewHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_h);
        } else {
            articleViewHolder.miv_select.setImageResource(R.mipmap.img_shoppingcar_selected_n);
        }

        if ("invalid".equals(article.status)) {
            articleViewHolder.miv_fail.setVisibility(View.VISIBLE);
            articleViewHolder.tv_title.setTextColor(getColor(R.color.color_value_6c));
            articleViewHolder.tv_favorite_count.setTextColor(getColor(R.color.color_value_6c));
            articleViewHolder.tv_praise_count.setTextColor(getColor(R.color.color_value_6c));
            changeImg(articleViewHolder.tv_favorite_count, R.mipmap.icon_shoucang_shoucang_h);
            changeImg(articleViewHolder.tv_praise_count, R.mipmap.icon_shoucang_zan_h);
        } else {
            articleViewHolder.miv_fail.setVisibility(View.GONE);
            articleViewHolder.tv_title.setTextColor(getColor(R.color.new_text));
            articleViewHolder.tv_favorite_count.setTextColor(getColor(R.color.new_text));
            articleViewHolder.tv_praise_count.setTextColor(getColor(R.color.new_text));
            changeImg(articleViewHolder.tv_favorite_count, R.mipmap.icon_shoucang_shoucang_n);
            changeImg(articleViewHolder.tv_praise_count, R.mipmap.icon_shoucang_zan_n);
        }
        articleViewHolder.ll_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSelect(position);
                }
            }
        });
        articleViewHolder.tv_cancel_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                articleViewHolder.swipe_layout.quickClose();
                if (mListener != null) {
                    mListener.onDel(position);
                }
            }
        });
    }

    public void changeImg(TextView textView, @DrawableRes int drawableRes) {
        Drawable nav_up = context.getResources().getDrawable(drawableRes);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        textView.setCompoundDrawables(null, null, nav_up, null);
    }

    public class ArticleViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_favorite_count)
        TextView tv_favorite_count;

        @BindView(R.id.tv_praise_count)
        TextView tv_praise_count;

        @BindView(R.id.tv_cancel_collection)
        TextView tv_cancel_collection;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

        @BindView(R.id.ll_select)
        LinearLayout ll_select;

        @BindView(R.id.miv_fail)
        MyImageView miv_fail;

        @BindView(R.id.swipe_layout)
        SwipeMenuLayout swipe_layout;

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

    public void setOnSelectListener(OnSelectListener listener) {
        this.mListener = listener;
    }

    public interface OnSelectListener {
        void onSelect(int position);

        void onDel(int position);
    }
}
