package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DiscoverHotAdapter extends BaseRecyclerAdapter<DiscoveryTieziEntity.Mdata.Hot>  {
    private Activity activity;
    public DiscoverHotAdapter(Context context, boolean isShowFooter, List<DiscoveryTieziEntity.Mdata.Hot> list,Activity activity) {
        super(context, isShowFooter, list);
        this.activity=activity;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new TieziHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_tiezi, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        TieziHolder viewHolder = (TieziHolder) holder;
        final DiscoveryTieziEntity.Mdata.Hot hot = lists.get(position);
        viewHolder.mtv_title.setText(hot.content);
        viewHolder.mtv_name.setText(hot.nickname);
        viewHolder.mtv_time.setText(hot.create_time);
        viewHolder.mtv_pinlun.setText(hot.comments);
        viewHolder.mtv_like.setText(hot.likes);
        if ("1".equals(hot.is_likes)) {
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
            viewHolder.mtv_like.setTextColor(getColor(R.color.pink_color));
        } else {
            viewHolder.miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_n);
            viewHolder.mtv_like.setTextColor(getColor(R.color.value_878B8A));
        }
        GlideUtils.getInstance().loadCircleImage(context,viewHolder.miv_avar,hot.avatar);
        if (hot.imgs == null || hot.imgs.size() == 0) {
            viewHolder.rv_pics.setVisibility(View.GONE);
            viewHolder.miv_pic.setVisibility(View.GONE);
        } else {
            if (hot.imgs.size() == 1) {
                viewHolder.rv_pics.setVisibility(View.GONE);
                viewHolder.miv_pic.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImageShu(context,viewHolder.miv_pic,hot.imgs.get(0));
                viewHolder.miv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击查看大图
                        BigImgEntity bigImgEntity = new BigImgEntity();
                        bigImgEntity.itemList = (ArrayList<String>) hot.imgs;
                        bigImgEntity.index = 0;
                        LookBigImgAct.startAct(context, bigImgEntity);
                    }
                });
            } else {
                viewHolder.miv_pic.setVisibility(View.GONE);
                viewHolder.rv_pics.setVisibility(View.VISIBLE);
                if (viewHolder.picAdapter==null){
                    viewHolder.picAdapter  = new SinglePicAdapter(activity, false,  hot.imgs);
                    viewHolder.rv_pics.setLayoutManager(new GridLayoutManager(context, 3));
                    viewHolder.rv_pics.setNestedScrollingEnabled(false);
                    viewHolder.rv_pics.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context,9),false));
                    viewHolder.rv_pics.setAdapter(viewHolder.picAdapter);
                }else {
                    viewHolder.picAdapter  = new SinglePicAdapter(activity, false, hot.imgs);
                    viewHolder.rv_pics.setAdapter(viewHolder.picAdapter);
                    //viewHolder.picAdapter.notifyDataSetChanged();
                }
                viewHolder.picAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //点击查看大图
                        BigImgEntity bigImgEntity = new BigImgEntity();
                        bigImgEntity.itemList = (ArrayList<String>)  hot.imgs;
                        bigImgEntity.index = position;
                        LookBigImgAct.startAct(context, bigImgEntity);
                    }
                });
            }
        }

    }



    public class TieziHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_pinlun)
        MyTextView mtv_pinlun;

        @BindView(R.id.mtv_like)
        MyTextView mtv_like;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.miv_avar)
        MyImageView miv_avar;

        @BindView(R.id.miv_like)
        MyImageView miv_like;

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.rv_pics)
        RecyclerView rv_pics;


        private SinglePicAdapter picAdapter;
        public TieziHolder(View itemView) {
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
