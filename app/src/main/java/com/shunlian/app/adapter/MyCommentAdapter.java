package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.bean.PicAdapter;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.CommentRank;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/11.
 */

public class MyCommentAdapter extends BaseRecyclerAdapter<CommentListEntity.Data> {

    public MyCommentAdapter(Context context, boolean isShowFooter, List<CommentListEntity.Data> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_comment, parent, false);
        return new MyCommentHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MyCommentHolder mHolder = (MyCommentHolder) holder;
        final CommentListEntity.Data data = lists.get(position);

        if ("5".equals(data.star_level)) {//好评
            mHolder.comment_rank.praiseRank();
        } else if ("3".equals(data.star_level)) {//中评
            mHolder.comment_rank.middleRank();
        } else {//差评
            mHolder.comment_rank.badRank();
        }

        String is_append = data.is_append;//追评状态  0不能追评   1可以追评 2已经追评
        if ("1".equals(is_append)) {
            GradientDrawable background = (GradientDrawable) mHolder.mtv_append_comment_staus.getBackground();
            background.setColor(getColor(R.color.white));
            mHolder.mtv_append_comment_staus.setText(getString(R.string.append_comment_write));
            mHolder.mtv_append_comment_staus.setEnabled(true);
            mHolder.mtv_append_comment_staus.setTextColor(getColor(R.color.pink_color));
        } else if ("2".equals(is_append)) {
            mHolder.mtv_append_comment_staus.setText(getString(R.string.already_comment));
            mHolder.mtv_append_comment_staus.setEnabled(false);
            GradientDrawable background = (GradientDrawable) mHolder.mtv_append_comment_staus.getBackground();
            background.setColor(getColor(R.color.pink_color));
            mHolder.mtv_append_comment_staus.setTextColor(getColor(R.color.white));
        } else {
            mHolder.mtv_append_comment_staus.setVisibility(View.GONE);
        }

        final String content = data.content;
        if (isEmpty(content)){//评价内容
            mHolder.mtv_content.setVisibility(View.GONE);
        }else {
            mHolder.mtv_content.setVisibility(View.VISIBLE);
            mHolder.mtv_content.setText(content);
        }

        String append_note = data.append_note;
        if (isEmpty(append_note)){//用户1天前追评
            mHolder.mtv_append_time.setVisibility(View.GONE);
        }else {
            mHolder.mtv_append_time.setVisibility(View.VISIBLE);
            mHolder.mtv_append_time.setText(append_note);
        }

        String append = data.append;
        if (isEmpty(append)){//追评内容
            mHolder.mtv_append_content.setVisibility(View.GONE);
        }else {
            mHolder.mtv_append_content.setVisibility(View.VISIBLE);
            mHolder.mtv_append_content.setText(append);
        }

        GlideUtils.getInstance().loadImage(context,mHolder.miv_goods_pic,data.thumb);
        mHolder.mtv_goods_detail.setText(data.title);
        mHolder.mtv_price.setText(getString(R.string.rmb)+data.price);
        mHolder.mtv_comment_time.setText(data.add_time);
        mHolder.mtv_zan_count.setText(data.praise_total);

        String reply = data.reply;
        if (isEmpty(reply)){
            mHolder.mrl_reply_content.setVisibility(View.GONE);
        }else {
            mHolder.mrl_reply_content.setVisibility(View.VISIBLE);
            mHolder.mtv_reply_content.setText(reply);
        }

        final List<String> pics = data.pics;
        if (isEmpty(pics)){
            mHolder.recy_view.setVisibility(View.GONE);
        }else {
            mHolder.recy_view.setVisibility(View.VISIBLE);
            PicAdapter picAdapter = new PicAdapter(context,false,pics);
            mHolder.recy_view.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) pics;
                    bigImgEntity.index = position;
                    bigImgEntity.desc = data.content;
                    LookBigImgAct.startAct(context, bigImgEntity);
                }
            });
        }


        final List<String> append_pics = data.append_pics;
        if (isEmpty(append_pics)){
            mHolder.recy_view_append.setVisibility(View.GONE);
        }else {
            mHolder.recy_view_append.setVisibility(View.VISIBLE);
            PicAdapter picAdapter = new PicAdapter(context,false,append_pics);
            mHolder.recy_view_append.setAdapter(picAdapter);
            picAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) append_pics;
                    bigImgEntity.index = position;
                    bigImgEntity.desc = data.append;
                    LookBigImgAct.startAct(context, bigImgEntity);
                }
            });
        }
    }

    public class MyCommentHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.comment_rank)
        CommentRank comment_rank;

        @BindView(R.id.mtv_append_comment_staus)
        MyTextView mtv_append_comment_staus;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.recy_view_append)
        RecyclerView recy_view_append;

        @BindView(R.id.mtv_append_time)
        MyTextView mtv_append_time;

        @BindView(R.id.mtv_append_content)
        MyTextView mtv_append_content;

        @BindView(R.id.mtv_goods_detail)
        MyTextView mtv_goods_detail;

        @BindView(R.id.mtv_price)
        MyTextView mtv_price;

        @BindView(R.id.mtv_comment_time)
        MyTextView mtv_comment_time;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.mtv_reply_content)
        MyTextView mtv_reply_content;

        @BindView(R.id.miv_goods_pic)
        MyImageView miv_goods_pic;

        @BindView(R.id.mrl_reply_content)
        MyRelativeLayout mrl_reply_content;

        public MyCommentHolder(View itemView) {
            super(itemView);
            GridLayoutManager manager = new GridLayoutManager(context,3);
            recy_view.setLayoutManager(manager);
            recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context,5),false));


            GridLayoutManager manager1 = new GridLayoutManager(context,3);
            recy_view_append.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context,5),false));
            recy_view_append.setLayoutManager(manager1);
            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.mtv_append_comment_staus)
        public void appendComment(){
            CommentListEntity.Data data = lists.get(getAdapterPosition());
            ReleaseCommentEntity entity = new ReleaseCommentEntity(data.thumb,data.title,data.price,data.comment_id);
        }

        @OnClick(R.id.mrl_goods)
        public void jumpGoodsDetail(){
            CommentListEntity.Data data = lists.get(getAdapterPosition());
            GoodsDetailAct.startAct(context,data.goods_id);
        }
        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
