package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class HotExpertAdapter extends BaseRecyclerAdapter<HotBlogsEntity.Blog> {
    private OnAdapterCallBack mCallBack;

    public HotExpertAdapter(Context context, List<HotBlogsEntity.Blog> lists) {
        super(context, false, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new HotExpertViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_expert, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotExpertViewHolder) {
            HotExpertViewHolder hotExpertViewHolder = (HotExpertViewHolder) holder;
            HotBlogsEntity.Blog blog = lists.get(position);
            GlideUtils.getInstance().loadCircleAvar(context, hotExpertViewHolder.miv_icon, blog.avatar);
            hotExpertViewHolder.tv_nickname.setText(blog.nickname);
            hotExpertViewHolder.tv_fans.setText(String.valueOf(blog.fans_num));
            hotExpertViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            hotExpertViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));


            if (blog.is_focus == 1) {//已经关注
                hotExpertViewHolder.tv_attention.setBackgroundDrawable(null);
                hotExpertViewHolder.tv_attention.setText("已关注");
                hotExpertViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                hotExpertViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                hotExpertViewHolder.tv_attention.setText("关注");
                hotExpertViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            if (blog.is_praise == 1) {
                hotExpertViewHolder.tv_zan.setClickable(false);
                setPraiseImg(hotExpertViewHolder.tv_zan, R.mipmap.icon_faxian_dainzan_hong);
            } else {
                hotExpertViewHolder.tv_zan.setClickable(true);
                setPraiseImg(hotExpertViewHolder.tv_zan, R.mipmap.icon_faxian_zan);
            }

            hotExpertViewHolder.tv_attention.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toFocusUser(blog.is_focus, blog.member_id);
                }
            });
            hotExpertViewHolder.tv_zan.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toPraiseBlog(blog.id);
                }
            });

            if (blog.type == 1) {
                BitmapUtil.discoverImg(hotExpertViewHolder.miv_big_icon, hotExpertViewHolder.recycler_list, null, blog.pics, (Activity) context
                        , 0, 0, 63, 12, 16, 0, 4, 0);
                hotExpertViewHolder.rl_video.setVisibility(View.GONE);
            } else {
                String imageWidth, imageheight;
                int width, height;
                if (!isEmpty(blog.video_thumb)) {
                    imageWidth = Common.getURLParameterValue(blog.video_thumb, "w");
                    imageheight = Common.getURLParameterValue(blog.video_thumb, "h");
                    width = Integer.valueOf(imageWidth);
                    height = Integer.valueOf(imageheight);

                    GlideUtils.getInstance().loadOverrideImage(context, hotExpertViewHolder.miv_video, blog.video_thumb, width, height);
                }
                hotExpertViewHolder.rl_video.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setPraiseImg(TextView textView, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置边界
        // param 左上右下
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public class HotExpertViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_big_icon)
        MyImageView miv_big_icon;

        @BindView(R.id.rl_video)
        RelativeLayout rl_video;

        @BindView(R.id.miv_video)
        MyImageView miv_video;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_fans)
        TextView tv_fans;

        @BindView(R.id.tv_download)
        TextView tv_download;

        @BindView(R.id.tv_zan)
        TextView tv_zan;

        public HotExpertViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAdapterCallBack(HotExpertAdapter.OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId);

        void toPraiseBlog(String blogId);
    }
}
