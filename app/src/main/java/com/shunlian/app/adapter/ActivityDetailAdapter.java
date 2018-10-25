package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.FolderTextView;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class ActivityDetailAdapter extends BaseRecyclerAdapter<HotBlogsEntity.Blog> {
    public static final int LAYOUT_TOP = 10003;
    private OnAdapterCallBack mCallBack;
    private HotBlogsEntity.Detail mDetail;
    private TieziAvarAdapter tieziAvarAdapter;

    public ActivityDetailAdapter(Context context, List<HotBlogsEntity.Blog> lists, HotBlogsEntity.Detail detail) {
        super(context, true, lists);
        this.mDetail = detail;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LAYOUT_TOP:
                return new DetailTopViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_activity_detail_top, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new BlogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == LAYOUT_TOP) {
            handleTop(holder);
        } else {
            if (!isEmpty(lists)) {
                handleItem(holder, position);
            }
        }
    }

    private void handleItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HotBlogAdapter.BlogViewHolder) {
            HotBlogsEntity.Blog blog = lists.get(position - 1);
            HotBlogAdapter.BlogViewHolder blogViewHolder = (HotBlogAdapter.BlogViewHolder) holder;
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
            blogViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            blogViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));

            if (!isEmpty(blog.related_goods)) {
                GoodsDeatilEntity.Goods goods = blog.related_goods.get(0);
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_goods_icon, goods.thumb);
                blogViewHolder.tv_goods_name.setText(goods.title);
                blogViewHolder.tv_goods_price.setText(getString(R.string.common_yuan) + goods.price);
                blogViewHolder.tv_share_count.setText(String.valueOf(goods.share_num));
            }
            if (blog.type == 1) {
                int recyclerWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 79);
                BitmapUtil.discoverImg(blogViewHolder.miv_big_icon, blogViewHolder.recycler_list, new SinglePicAdapter(context, blog.pics, 4, recyclerWidth), blog.pics, (Activity) context
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

            if (blog.is_focus == 1) {//已经关注
                blogViewHolder.tv_attention.setBackgroundDrawable(null);
                blogViewHolder.tv_attention.setText("已关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
            } else {
                blogViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                blogViewHolder.tv_attention.setText("关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
            }

            if (blog.is_praise == 1) {
                blogViewHolder.tv_zan.setClickable(false);
                setPraiseImg(blogViewHolder.tv_zan, R.mipmap.icon_faxian_dainzan_hong);
            } else {
                blogViewHolder.tv_zan.setClickable(true);
                setPraiseImg(blogViewHolder.tv_zan, R.mipmap.icon_faxian_zan);
            }

            if (blog.is_self == 0) {
                blogViewHolder.miv_more.setVisibility(View.GONE);
            } else {
                blogViewHolder.miv_more.setVisibility(View.VISIBLE);
            }

            blogViewHolder.tv_attention.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toFocusUser(blog.is_focus, blog.member_id);
                }
            });

            blogViewHolder.tv_zan.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.toPraiseBlog(blog.id);
                }
            });

            blogViewHolder.ll_member.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });
            blogViewHolder.miv_big_icon.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });
            blogViewHolder.miv_more.setOnClickListener(v -> {

            });

            blogViewHolder.rl_attention.setVisibility(View.GONE);
        }
    }

    public void setPraiseImg(TextView textView, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置边界
        // param 左上右下
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    private void handleTop(RecyclerView.ViewHolder holder) {
        if (holder instanceof DetailTopViewHolder) {
            DetailTopViewHolder detailTopViewHolder = (DetailTopViewHolder) holder;
            GlideUtils.getInstance().loadImage(context, detailTopViewHolder.miv_icon, mDetail.thumb);
            detailTopViewHolder.tv_title.setText(mDetail.title);
            detailTopViewHolder.tv_activity_content.setText(mDetail.content);
            detailTopViewHolder.tv_join_count.setText(mDetail.refer_member_num + "人在参与");

            ViewTreeObserver vto = detailTopViewHolder.rl_rootView.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(() -> {
                int height = detailTopViewHolder.rl_rootView.getHeight();

                if (mCallBack != null) {
                    mCallBack.OnTopSize(height);
                }
            });

            if (!isEmpty(mDetail.members)) {
                List<String> pics = new ArrayList<>();
                for (DiscoverActivityEntity.Member member : mDetail.members) {
                    pics.add(member.avatar);
                }
                if (tieziAvarAdapter == null) {
                    tieziAvarAdapter = new TieziAvarAdapter(context, pics, TransformUtil.dip2px(context, 20));
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    detailTopViewHolder.recycler_list.addItemDecoration(new HorizonItemDecoration(TransformUtil.dip2px(context, -8)));
                    detailTopViewHolder.recycler_list.setLayoutManager(linearLayoutManager);
                    detailTopViewHolder.recycler_list.setAdapter(tieziAvarAdapter);
                }
                detailTopViewHolder.recycler_list.setVisibility(View.VISIBLE);
            } else {
                detailTopViewHolder.recycler_list.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LAYOUT_TOP;
        }
        return super.getItemViewType(position);
    }


    public class DetailTopViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder {

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_join_count)
        TextView tv_join_count;

        @BindView(R.id.tv_activity_content)
        TextView tv_activity_content;

        public DetailTopViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class BlogViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.ll_member)
        LinearLayout ll_member;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_attention)
        TextView tv_attention;

        @BindView(R.id.tv_tag)
        TextView tv_tag;

        @BindView(R.id.tv_content)
        FolderTextView tv_content;

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

        @BindView(R.id.rl_attention)
        RelativeLayout rl_attention;

        @BindView(R.id.recylcer_attention)
        RecyclerView recylcer_attention;

        public BlogViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAdapterCallBack(OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId);

        void toPraiseBlog(String blogId);

        void OnTopSize(int height);
    }
}