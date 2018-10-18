package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/17.
 */

public class HotBlogAdapter extends BaseRecyclerAdapter<HotBlogsEntity.Blog> {
    private Activity mActivity;

    public HotBlogAdapter(Context context, List<HotBlogsEntity.Blog> lists, Activity activity) {
        super(context, true, lists);
        this.mActivity = activity;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new BlogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BlogViewHolder) {
            HotBlogsEntity.Blog blog = lists.get(position);
            BlogViewHolder blogViewHolder = (BlogViewHolder) holder;
            GlideUtils.getInstance().loadCircleImage(context, blogViewHolder.miv_icon, blog.avatar);
            blogViewHolder.tv_name.setText(blog.nickname);
            blogViewHolder.tv_time.setText(blog.time_desc);

            if (isEmpty(blog.activity_title)) {
                blogViewHolder.tv_tag.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_tag.setVisibility(View.VISIBLE);
            }

            if (isEmpty(blog.place)) {
                blogViewHolder.tv_address.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_address.setVisibility(View.VISIBLE);
            }

            if (blog.add_v == 1) {
                blogViewHolder.miv_v.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.miv_v.setVisibility(View.GONE);
            }

            blogViewHolder.tv_tag.setText(blog.activity_title);
            blogViewHolder.tv_content.setText(blog.text);
            blogViewHolder.tv_address.setText(blog.place);
            blogViewHolder.tv_download.setText(blog.down_num);
            blogViewHolder.tv_zan.setText(blog.praise_num);

            if (!isEmpty(blog.related_goods)) {
                GoodsDeatilEntity.Goods goods = blog.related_goods.get(0);
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_goods_icon, goods.thumb);
                blogViewHolder.tv_goods_name.setText(goods.title);
                blogViewHolder.tv_goods_price.setText(R.string.common_yuan + goods.price);
            }
            if (blog.type == 1) {
                BitmapUtil.discoverImg(blogViewHolder.miv_big_icon, blogViewHolder.recycler_list, null, blog.pics, mActivity
                        , 0, 0, 63, 12, 16, 0, 4, 0);
                blogViewHolder.rl_video.setVisibility(View.GONE);
            } else {
                String imageWidth, imageheight;
                int width, height;
                if (!isEmpty(blog.video_thumb)) {
                    imageWidth = Common.getURLParameterValue(blog.video_thumb, "w");
                    imageheight = Common.getURLParameterValue(blog.video_thumb, "h");
                    width = Integer.valueOf(imageWidth);
                    height = Integer.valueOf(imageheight);

                    GlideUtils.getInstance().loadOverrideImage(context, blogViewHolder.miv_video, blog.video_thumb, width, height);
                }
                blogViewHolder.rl_video.setVisibility(View.VISIBLE);
            }
        }
    }

    public class BlogViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        @BindView(R.id.tv_tag)
        TextView tv_tag;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_address)
        TextView tv_address;

        @BindView(R.id.miv_goods_icon)
        MyImageView miv_goods_icon;

        @BindView(R.id.tv_goods_name)
        TextView tv_goods_name;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_share_count)
        TextView tv_share_count;

        @BindView(R.id.tv_zan)
        TextView tv_zan;

        @BindView(R.id.tv_download)
        TextView tv_download;

        @BindView(R.id.miv_more)
        MyImageView miv_more;

        @BindView(R.id.miv_big_icon)
        MyImageView miv_big_icon;

        @BindView(R.id.miv_video)
        MyImageView miv_video;

        @BindView(R.id.rl_video)
        RelativeLayout rl_video;

        @BindView(R.id.miv_v)
        MyImageView miv_v;

        public BlogViewHolder(View itemView) {
            super(itemView);
        }
    }
}
