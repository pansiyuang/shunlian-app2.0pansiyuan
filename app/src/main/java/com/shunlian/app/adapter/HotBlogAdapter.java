package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.discover.VideoPlayActivity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.VideoGoodPlayActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.FolderTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/17.
 */

public class HotBlogAdapter extends BaseRecyclerAdapter<HotBlogsEntity.Blog> {
    public static final int LAYOUT_TOP = 10003;
    private Activity mActivity;
    private List<HotBlogsEntity.RecomandFocus> recomandFocusList;
    private AttentionMemberAdapter attentionMemberAdapter;
    private OnAdapterCallBack mCallBack;
    private List<HotBlogsEntity.Ad> adList;
    private List<String> banners;

    public HotBlogAdapter(Context context, List<HotBlogsEntity.Blog> lists, Activity activity) {
        super(context, true, lists);
        this.mActivity = activity;
    }

    public HotBlogAdapter(Context context, List<HotBlogsEntity.Blog> lists, List<HotBlogsEntity.Ad> ads) {
        super(context, true, lists);
        this.adList = ads;
    }

    public HotBlogAdapter(Context context, List<HotBlogsEntity.Blog> lists, Activity activity, List<HotBlogsEntity.RecomandFocus> list) {
        super(context, true, lists);
        this.mActivity = activity;
        this.recomandFocusList = list;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new BlogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_blog, parent, false));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LAYOUT_TOP:
                return new TopViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_hot_blog_top, parent, false));
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!isEmpty(adList) && position == 0) {
            return LAYOUT_TOP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (!isEmpty(adList)) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
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

    public void handleTop(RecyclerView.ViewHolder holder) {
        if (holder instanceof TopViewHolder) {
            TopViewHolder topViewHolder = (TopViewHolder) holder;
            if (banners == null) {
                banners = new ArrayList<>();
            }
            banners.clear();
            for (HotBlogsEntity.Ad ad : adList) {
                banners.add(ad.ad_img);
            }
            topViewHolder.myKanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
            topViewHolder.myKanner.setBanner(banners);
            topViewHolder.myKanner.setOnItemClickL(position -> {
//                    Common.goGoGo(baseAct, coreHotEntity.banner_list.get(position).type, coreHotEntity.banner_list.get(position).item_id);
            });
        }
    }

    public void handleItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BlogViewHolder) {
            HotBlogsEntity.Blog blog;
            if (isEmpty(adList)) {
                blog = lists.get(position);
            } else {
                blog = lists.get(position - 1);
            }
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
                blogViewHolder.recycler_list.setVisibility(View.GONE);
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
                blogViewHolder.recycler_list.setVisibility(View.GONE);
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

            if (position == 0) {
                showAttentionList(recomandFocusList, holder);
            } else {
                blogViewHolder.rl_attention.setVisibility(View.GONE);
            }

            blogViewHolder.ll_member.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });
            blogViewHolder.miv_big_icon.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });
            blogViewHolder.miv_more.setOnClickListener(v -> {

            });

            blogViewHolder.rl_video.setOnClickListener(v -> {
                VideoGoodPlayActivity.startActivity(context, blog);
            });
        }
    }

    public void setPraiseImg(TextView textView, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置边界
        // param 左上右下
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public void showAttentionList(List<HotBlogsEntity.RecomandFocus> list, RecyclerView.ViewHolder holder) {
        BlogViewHolder blogViewHolder = (BlogViewHolder) holder;
        if (isEmpty(list)) {
            blogViewHolder.rl_attention.setVisibility(View.GONE);
        } else {
            attentionMemberAdapter = new AttentionMemberAdapter(context, list);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            blogViewHolder.recylcer_attention.setLayoutManager(manager);
            blogViewHolder.recylcer_attention.setAdapter(attentionMemberAdapter);
            blogViewHolder.rl_attention.setVisibility(View.VISIBLE);
            attentionMemberAdapter.setOnFocusListener((isFocus, memberId) -> {
                if (mCallBack != null) {
                    mCallBack.toFocusMember(isFocus, memberId);
                }
            });
        }
    }

    public void MemberAdapterNotifyDataSetChanged() {
        if (attentionMemberAdapter != null) {
            attentionMemberAdapter.notifyDataSetChanged();
        }
    }

    public class TopViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.myKanner)
        MyKanner myKanner;

        public TopViewHolder(View itemView) {
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

        void toFocusMember(int isFocus, String memberId);

        void toPraiseBlog(String blogId);
    }
}
