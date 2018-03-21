package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.DiscoveryCircleEntity;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.presenter.PADiscoverSucaiku;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DiscoverNewAdapter extends BaseRecyclerAdapter<DiscoveryCircleEntity.Mdata.Content> {
    public DiscoverNewAdapter(Context context, boolean isShowFooter, List<DiscoveryCircleEntity.Mdata.Content> list) {
        super(context, isShowFooter, list);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new NewHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_new, parent, false));
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        NewHolder viewHolder = (NewHolder) holder;
        DiscoveryCircleEntity.Mdata.Content content = lists.get(position);
        if ("1".equals(content.is_likes)){
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
        }else {
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_n);
        }
        viewHolder.mtv_desc.setText(content.comments);
        viewHolder.mtv_title.setText(content.title);
        viewHolder.mtv_like.setText(content.likes);
        GlideUtils.getInstance().loadCornerImage(context,viewHolder.miv_photo,content.img,4);
    }

    public class NewHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_like)
        MyTextView mtv_like;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.miv_photo)
        MyImageView miv_photo;

        @BindView(R.id.miv_like)
        MyImageView miv_like;


        public NewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

}
