package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.utils.TransformUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ChosenTagAdapter extends BaseRecyclerAdapter<ArticleEntity.Tag> {

    public ChosenTagAdapter(Context context, List<ArticleEntity.Tag> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TagHolderView(LayoutInflater.from(context).inflate(R.layout.item_chosen_tag, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        TagHolderView tagHolderView = (TagHolderView) holder;
        tagHolderView.tv_tag.setText(lists.get(position).name);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, TransformUtil.dip2px(context, 24));
        if (position != lists.size() - 1) {
            params.setMargins(0, TransformUtil.dip2px(context, 12), TransformUtil.dip2px(context, 5), TransformUtil.dip2px(context, 12));
        } else {
            params.setMargins(0, TransformUtil.dip2px(context, 12), 0, TransformUtil.dip2px(context, 12));
        }
        tagHolderView.tv_tag.setLayoutParams(params);
    }

    public class TagHolderView extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_tag)
        TextView tv_tag;

        public TagHolderView(View itemView) {
            super(itemView);
        }
    }
}
