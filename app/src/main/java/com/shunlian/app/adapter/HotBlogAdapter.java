package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.ui.discover_new.ActivityDetailActivity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.VideoGoodPlayActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.DownLoadQRCodeImageUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.download.DownLoadDialogProgress;
import com.shunlian.app.utils.download.DownloadUtils;
import com.shunlian.app.utils.download.JsDownloadListener;
import com.shunlian.app.widget.BlogBottomDialog;
import com.shunlian.app.widget.BlogGoodsShareDialog;
import com.shunlian.app.widget.FolderTextView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewLookBigImgAct;
import com.shunlian.app.widget.SaveImgDialog;
import com.shunlian.app.widget.SubBlogCommentItemView;
import com.shunlian.app.widget.banner.MyKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/17.
 */

public class HotBlogAdapter extends BaseRecyclerAdapter<BigImgEntity.Blog> implements BlogBottomDialog.OnDialogCallBack {
    public static final int LAYOUT_TOP = 10003;
    private Activity mActivity;
    private List<HotBlogsEntity.RecomandFocus> recomandFocusList;
    private AttentionMemberAdapter attentionMemberAdapter;
    private OnAdapterCallBack mCallBack;
    private List<HotBlogsEntity.Ad> adList;
    private List<String> banners;
    private BlogBottomDialog blogBottomDialog;
    private OnDelBlogListener delBlogListener;
    private OnFavoListener favoListener;
    private boolean showAttention = true;
    private boolean isShowMore = false;
    private SaveImgDialog saveImgDialog;
    private DownLoadDialogProgress downLoadDialogProgress;
    private DownloadUtils downloadUtils;
    private HttpDialog httpDialog;
    private String currentBlogId;
    private String myImageUrl;

    private DownLoadQRCodeImageUtil downLoadQRCodeImageUtil;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (httpDialog != null) {
                        httpDialog.dismiss();
                    }
                    boolean b = false;
                    if (msg.obj != null) {
                        b = (boolean) msg.obj;
                    }
                    if (saveImgDialog == null) {
                        Activity activity = (Activity) context;
                        saveImgDialog = new SaveImgDialog(activity);
                    }
                    saveImgDialog.showQRCodeView(b);
                    saveImgDialog.show();
                    if (mCallBack != null) {
                        mCallBack.toDown(currentBlogId);
                    }
                    break;
                case 2:
                    BigImgEntity.Blog blog = (BigImgEntity.Blog) msg.obj;
                    if (downLoadQRCodeImageUtil == null) {
                        downLoadQRCodeImageUtil = new DownLoadQRCodeImageUtil(context);
                    }
                    downLoadQRCodeImageUtil.setMyCallBack(new DownLoadQRCodeImageUtil.MyCallBack() {
                        @Override
                        public void successBack() {
                            Message message = mHandler.obtainMessage(1);
                            message.obj = true;
                            mHandler.sendMessage(message);
                        }

                        @Override
                        public void errorBack() {

                        }
                    });
                    downLoadQRCodeImageUtil.saveGoodsQRImage(blog.related_goods);
                    break;
            }
        }
    };

    public HotBlogAdapter(Context context, List<BigImgEntity.Blog> lists, Activity activity) {
        super(context, true, lists);
        this.mActivity = activity;
    }

    public HotBlogAdapter(Context context, List<BigImgEntity.Blog> lists, List<HotBlogsEntity.Ad> ads) {
        super(context, true, lists);
        this.adList = ads;
    }

    public HotBlogAdapter(Context context, List<BigImgEntity.Blog> lists, Activity activity, List<HotBlogsEntity.RecomandFocus> list) {
        super(context, true, lists);
        this.mActivity = activity;
        this.recomandFocusList = list;
    }

    public void setShowAttention(boolean isShow) {
        showAttention = isShow;
    }

    public void setShowMore(boolean isShow) {
        isShowMore = isShow;
    }

    public void setMyIcon(String url) {
        myImageUrl = url;
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
                Common.goGoGo(context, adList.get(position).ad_link.type, adList.get(position).ad_link.item_id);
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof BlogViewHolder) {
            BigImgEntity.Blog blog;
            List<BigImgEntity.Blog> blogList = (List<BigImgEntity.Blog>) payloads.get(0);
            if (isEmpty(adList)) {
                blog = blogList.get(position);
            } else {
                blog = blogList.get(position - 1);
            }
            BlogViewHolder blogViewHolder = (BlogViewHolder) holder;

            if (blog.is_focus == 1) {//已经关注
                blogViewHolder.tv_attention.setBackgroundDrawable(null);
                blogViewHolder.tv_attention.setText("已关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
                blogViewHolder.tv_attention.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                blogViewHolder.tv_attention.setText("关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
                blogViewHolder.tv_attention.setVisibility(View.VISIBLE);
            }

            if (blog.is_praise == 1) {
                blogViewHolder.animation_zan.setProgress(1f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.pink_color));
                blogViewHolder.ll_zan.setClickable(false);
            } else {
                blogViewHolder.animation_zan.setProgress(0f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.value_343434));
                blogViewHolder.ll_zan.setClickable(true);
            }

            blogViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            blogViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));
            blogViewHolder.tv_share_count.setText(String.valueOf(blog.total_share_num));

            if (blog.is_self == 1) {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
                if (isShowMore) {
                    blogViewHolder.miv_more.setVisibility(View.VISIBLE);
                } else {
                    blogViewHolder.miv_more.setVisibility(View.GONE);
                }
            } else {
                blogViewHolder.miv_more.setVisibility(View.VISIBLE);
            }

            if (!showAttention) {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
            }

            if (blog.comment_list == null || isEmpty(blog.comment_list.list)) {
                blogViewHolder.tv_comment_count.setText("评论");
                gone(blogViewHolder.ll_comment, blogViewHolder.tv_comment_title);
            } else {
                visible(blogViewHolder.ll_comment);
                reply(blogViewHolder.ll_comment, blog.comment_list,blog.id);
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }


    public void handleItem(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BlogViewHolder) {
            BigImgEntity.Blog blog;
            if (isEmpty(adList)) {
                blog = lists.get(position);
            } else {
                blog = lists.get(position - 1);
            }
            BlogViewHolder blogViewHolder = (BlogViewHolder) holder;
            GlideUtils.getInstance().loadCircleAvar(context, blogViewHolder.miv_icon, blog.avatar);
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
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_v, blog.v_icon);
                blogViewHolder.miv_v.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.miv_v.setVisibility(View.GONE);
            }

            if (blog.expert == 1) {
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_expert, blog.expert_icon);
                blogViewHolder.miv_expert.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.miv_expert.setVisibility(View.GONE);
            }
            blogViewHolder.tv_tag.setText(blog.activity_title);
            blogViewHolder.tv_content.setText(blog.text);
            blogViewHolder.tv_content.setBlogText(blog.text);
            blogViewHolder.tv_address.setText(blog.place);
            blogViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            blogViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));
            GlideUtils.getInstance().loadCircleAvar(context, blogViewHolder.miv_comment_icon, myImageUrl);

            if (isEmpty(blog.activity_title)) {
                blogViewHolder.tv_tag.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_tag.setVisibility(View.VISIBLE);
            }

            if (isEmpty(blog.text)) {
                blogViewHolder.tv_content.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_content.setVisibility(View.VISIBLE);
            }

            if (!isEmpty(blog.related_goods)) {
                GoodsDeatilEntity.Goods goods = blog.related_goods.get(0);
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_goods_icon, goods.thumb);
                blogViewHolder.tv_goods_name.setText(goods.title);
                blogViewHolder.tv_goods_price.setText(getString(R.string.common_yuan) + goods.price);
                blogViewHolder.tv_share_count.setText(String.valueOf(blog.total_share_num));

                int i = TransformUtil.dip2px(context, 10);
                TransformUtil.expandViewTouchDelegate(blogViewHolder.tv_share_count, i, i, i, i);
                blogViewHolder.tv_share_count.setOnClickListener(view -> {
                    if (mCallBack != null) {
                        mCallBack.getShareInfo(blog.id, goods.goods_id);
                    }
                });
                blogViewHolder.rlayout_goods.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.rlayout_goods.setVisibility(View.GONE);
            }
            if (blog.type == 1) { //图文
                setTextDrawable(blogViewHolder.tv_download, R.mipmap.icon_imagedown_nor);
                int recyclerWidth = Common.getScreenWidth((Activity) context) - TransformUtil.dip2px(context, 79);
                SinglePicAdapter singlePicAdapter = new SinglePicAdapter(context, blog.pics, 4, recyclerWidth, false);
                BitmapUtil.discoverImg(blogViewHolder.miv_big_icon, blogViewHolder.recycler_list, singlePicAdapter, blog.pics, (Activity) context
                        , 0, 0, 63, 12, 16, 0, 4, 0);
                singlePicAdapter.setOnItemClickListener((view, position1) -> {
                    //点击查看大图
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) blog.pics;
                    bigImgEntity.index = position1;
                    bigImgEntity.blog = blog;
                    NewLookBigImgAct.startAct(context, bigImgEntity);
                });
                blogViewHolder.rl_video.setVisibility(View.GONE);
            } else {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) blogViewHolder.miv_video.getLayoutParams();
                setTextDrawable(blogViewHolder.tv_download, R.mipmap.icon_faxian_xaizai);
                if (!isEmpty(blog.video_thumb)) {
                    int[] params = BitmapUtil.imgParam(Common.getURLParameterValue(blog.video_thumb, "w"), Common.getURLParameterValue(blog.video_thumb, "h"), 190, 190);

                    if (params == null || params.length == 0) {
                        layoutParams.width = layoutParams.height = TransformUtil.dip2px(context, 95);
                    } else {
                        layoutParams.width = TransformUtil.dip2px(context, params[0]);
                        layoutParams.height = TransformUtil.dip2px(context, params[1]);
                    }
                } else {
                    layoutParams.width = layoutParams.height = TransformUtil.dip2px(context, 95);
                }
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_video, blog.video_thumb);
                blogViewHolder.miv_video.setLayoutParams(layoutParams);
                blogViewHolder.miv_big_icon.setVisibility(View.GONE);
                blogViewHolder.recycler_list.setVisibility(View.GONE);
                blogViewHolder.miv_big_icon.setVisibility(View.GONE);
                blogViewHolder.rl_video.setVisibility(View.VISIBLE);
            }

            if (blog.is_focus == 1) {//已经关注
                blogViewHolder.tv_attention.setBackgroundDrawable(null);
                blogViewHolder.tv_attention.setText("已关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.text_gray2));
                blogViewHolder.tv_attention.setVisibility(View.GONE);
            } else {
                blogViewHolder.tv_attention.setBackgroundDrawable(getDrawable(R.drawable.rounded_corner_stroke_pink_20px));
                blogViewHolder.tv_attention.setText("关注");
                blogViewHolder.tv_attention.setTextColor(getColor(R.color.pink_color));
                blogViewHolder.tv_attention.setVisibility(View.VISIBLE);
            }

            blogViewHolder.animation_zan.setAnimation("praise.json");
            blogViewHolder.animation_zan.loop(false);
            blogViewHolder.animation_zan.setImageAssetsFolder("images/");
            if (blog.is_praise == 1) {
                blogViewHolder.animation_zan.setProgress(1f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.pink_color));
            } else {
                blogViewHolder.animation_zan.setProgress(0f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.value_343434));
            }

            if (blog.is_self == 1) {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
                if (isShowMore) {
                    blogViewHolder.miv_more.setVisibility(View.VISIBLE);
                } else {
                    blogViewHolder.miv_more.setVisibility(View.GONE);
                }
            } else {
                blogViewHolder.miv_more.setVisibility(View.VISIBLE);
            }

            blogViewHolder.tv_attention.setOnClickListener(v -> {
                if (Common.isAlreadyLogin()) {
                    if (mCallBack != null) {
                        mCallBack.toFocusUser(blog.is_focus, blog.member_id, blog.nickname);
                    }
                } else {
                    Common.goGoGo(context, "login");
                }
            });

            blogViewHolder.ll_zan.setOnClickListener(v -> {
                if (Common.isAlreadyLogin()) {
                    if (mCallBack != null) {
                        mCallBack.toPraiseBlog(blog.id, blogViewHolder.animation_zan);
                    }
                } else {
                    Common.goGoGo(context, "login");
                }
            });

            if (position == 0) {
                showAttentionList(recomandFocusList, holder);
            } else {
                blogViewHolder.rl_attention.setVisibility(View.GONE);
            }

            blogViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, blog.member_id));

            blogViewHolder.ll_member.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });
            blogViewHolder.miv_big_icon.setOnClickListener(v -> {
                //点击查看大图
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) blog.pics;
                bigImgEntity.index = position;
                bigImgEntity.blog = blog;
                NewLookBigImgAct.startAct(context, bigImgEntity);
            });


            blogViewHolder.miv_more.setOnClickListener(v -> {
                showDialog(blog);
            });

            blogViewHolder.rl_video.setOnClickListener(v -> {
                VideoGoodPlayActivity.startActivity(context, blog);
            });

            blogViewHolder.rlayout_goods.setOnClickListener(view -> {
                if (blog.related_goods.size() == 1) {
                    GoodsDetailAct.startAct(context, blog.related_goods.get(0).goods_id);
                } else {
                    initDialog(blog);
                }
            });
            blogViewHolder.tv_tag.setOnClickListener(v -> {
                if (isEmpty(blog.activity_id)) {
                    return;
                }
                ActivityDetailActivity.startAct(context, blog.activity_id);
            });

            blogViewHolder.tv_download.setOnClickListener(v -> {
                if (Common.isAlreadyLogin()) {
                    readyToDownLoad(blog);
                } else {
                    Common.goGoGo(context, "login");
                }
            });
            if (!showAttention) {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
            }
            blogViewHolder.tv_comment.setOnClickListener(v -> {
                Common.showKeyboard(blogViewHolder.tv_comment);
                if (mCallBack != null) {
                    mCallBack.showCommentView(blog.id);
                }
            });
            if (blog.comment_list == null || isEmpty(blog.comment_list.list)) {
                blogViewHolder.tv_comment_count.setText("评论");
                gone(blogViewHolder.ll_comment, blogViewHolder.tv_comment_title);
            } else {
                visible(blogViewHolder.ll_comment);
                reply(blogViewHolder.ll_comment, blog.comment_list,blog.id);
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }
        }
    }

    public void reply(LinearLayout ll_sub_bg, BigImgEntity.CommentEntity commentEntity,String blogId) {
        ll_sub_bg.removeAllViews();
        int maxSize;
        if (commentEntity.list.size() >= 2) {
            maxSize = 2;
        } else {
            maxSize = commentEntity.list.size();
        }
        for (int j = 0; j < maxSize; j++) {
            SubBlogCommentItemView view = new SubBlogCommentItemView(context);
            BigImgEntity.CommentItem itemComment = commentEntity.list.get(j);
            view.setCommentData(itemComment, blogId);
            if (j == maxSize - 1) {
                view.setNumShow(true, commentEntity.total);
            } else {
                view.setNumShow(false, commentEntity.total);
            }
            ll_sub_bg.addView(view);
        }
    }

    public void setTextDrawable(TextView textView, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置边界
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public void initDialog(BigImgEntity.Blog blog) {
        BlogGoodsShareDialog.startAct(context, blog);
    }

    public void showAttentionList(List<HotBlogsEntity.RecomandFocus> list, RecyclerView.ViewHolder holder) {
        BlogViewHolder blogViewHolder = (BlogViewHolder) holder;
        if (isEmpty(list)) {
            blogViewHolder.rl_attention.setVisibility(View.GONE);
        } else {
            if (attentionMemberAdapter == null) {
                attentionMemberAdapter = new AttentionMemberAdapter(context, list);
            }
            LinearLayoutManager manager = new LinearLayoutManager(context);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            blogViewHolder.recylcer_attention.setLayoutManager(manager);
            blogViewHolder.recylcer_attention.setAdapter(attentionMemberAdapter);
            blogViewHolder.rl_attention.setVisibility(View.VISIBLE);
            attentionMemberAdapter.setOnFocusListener((isFocus, memberId, nickName) -> {
                if (Common.isAlreadyLogin()) {
                    if (mCallBack != null) {
                        mCallBack.toFocusMember(isFocus, memberId, nickName);
                    }
                } else {
                    Common.goGoGo(context, "login");
                }
            });
            attentionMemberAdapter.setOnItemClickListener((view, position) -> MyPageActivity.startAct(context, list.get(position).member_id));
        }
    }

    public void MemberAdapterNotifyDataSetChanged() {
        if (attentionMemberAdapter != null) {
            attentionMemberAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addFavo(int favo, String blogId) {
        for (int i = 0; i < lists.size(); i++) {
            BigImgEntity.Blog blog = lists.get(i);
            if (blogId.equals(blog.id)) {
                blog.is_favo = favo;
                if (favoListener != null) {
                    favoListener.OnFavo(favo, blogId);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDel(String blogId) {
        for (int i = 0; i < lists.size(); i++) {
            BigImgEntity.Blog blog = lists.get(i);
            if (blogId.equals(blog.id)) {
                lists.remove(i);
                if (delBlogListener != null) {
                    delBlogListener.onDel(blogId);
                }
            }
        }
        notifyDataSetChanged();
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

        @BindView(R.id.miv_expert)
        MyImageView miv_expert;

        @BindView(R.id.ll_member)
        LinearLayout ll_member;

        @BindView(R.id.rlayout_goods)
        RelativeLayout rlayout_goods;

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

        @BindView(R.id.ll_zan)
        LinearLayout ll_zan;

        @BindView(R.id.tv_zan)
        TextView tv_zan;

        @BindView(R.id.animation_zan)
        LottieAnimationView animation_zan;

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

        @BindView(R.id.miv_comment_icon)
        MyImageView miv_comment_icon;

        @BindView(R.id.tv_comment)
        TextView tv_comment;

        @BindView(R.id.tv_comment_title)
        TextView tv_comment_title;

        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        @BindView(R.id.ll_comment)
        LinearLayout ll_comment;

        public BlogViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAdapterCallBack(OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public void setOnDelListener(OnDelBlogListener listener) {
        this.delBlogListener = listener;
    }

    public void setOnFavoListener(OnFavoListener listener) {
        this.favoListener = listener;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId, String nickName);

        void toFocusMember(int isFocus, String memberId, String nickName);

        void toPraiseBlog(String blogId, LottieAnimationView lottieAnimationView);

        void toDown(String blogId);

        void getShareInfo(String blogId, String goodid);

        void showCommentView(String blogId);
    }

    public interface OnDelBlogListener {
        void onDel(String blogId);
    }

    public interface OnFavoListener {
        void OnFavo(int isFavo, String blogId);
    }

    public void showDialog(BigImgEntity.Blog blog) {
        if (blogBottomDialog == null) {
            blogBottomDialog = new BlogBottomDialog(context);
            blogBottomDialog.setOnDialogCallBack(this);
        }
        blogBottomDialog.setBlog(blog);
        blogBottomDialog.show();
    }

    public void readyToDownLoad(BigImgEntity.Blog blog) {
        currentBlogId = blog.id;
        switch (blog.type) {
            case 1://图文
                if (!isEmpty(blog.pics)) {
                    if (httpDialog == null) {
                        httpDialog = new HttpDialog(context);
                    }
                    httpDialog.show();
                    ArrayList arrayList = new ArrayList();
                    arrayList.addAll(blog.pics);
                    DownLoadImageThread threads = new DownLoadImageThread(context, arrayList, new DownLoadImageThread.MyCallBack() {
                        @Override
                        public void successBack() {
                            //开始保存二维码图片
                            if (isEmpty(blog.related_goods)) {
                                mHandler.sendEmptyMessage(1);
                            } else {
                                Message message = mHandler.obtainMessage(2);
                                message.obj = blog;
                                mHandler.sendMessage(message);
                            }
                        }

                        @Override
                        public void errorBack() {
                            Common.staticToast("保存图片失败");
                        }
                    });
                    threads.start();
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(blog.text);
                }
                break;
            case 2://视频
                downFileStart(blog.video);
                break;
        }
    }

    private void downFileStart(String url) {
        downLoadDialogProgress = new DownLoadDialogProgress();
        downloadUtils = new DownloadUtils(new JsDownloadListener() {
            @Override
            public void onStartDownload() {
            }

            @Override
            public void onProgress(int progress) {
                downLoadDialogProgress.showProgress(progress);
            }

            @Override
            public void onFinishDownload(String filePath, boolean isCancel) {
                downLoadDialogProgress.downLoadSuccess();
            }

            @Override
            public void onFail(String errorInfo) {
                downLoadDialogProgress.dissMissDialog();
            }

            @Override
            public void onFinishEnd() {
                if (mCallBack != null) {
                    mCallBack.toDown(currentBlogId);
                }
            }
        });
        boolean checkState = downloadUtils.checkDownLoadFileExists(url);
        if (checkState) {
            Common.staticToast("该视频已下载过!");
            return;
        }
        downLoadDialogProgress.showDownLoadDialogProgress(context, new DownLoadDialogProgress.downStateListen() {
            @Override
            public void cancelDownLoad() {
                downloadUtils.setCancel(true);
                if (mCallBack != null) {
                    mCallBack.toDown(currentBlogId);
                }
            }

            @Override
            public void fileDownLoad() {
                downloadUtils.download(url, downloadUtils.fileName);
            }
        }, !NetworkUtils.isWifiConnected(context));
    }
}
