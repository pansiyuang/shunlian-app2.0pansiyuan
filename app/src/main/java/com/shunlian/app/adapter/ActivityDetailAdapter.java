package com.shunlian.app.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.DiscoverActivityEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.ui.discover_new.ActivityDetailActivity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.VideoGoodPlayActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.DownLoadQRCodeImageUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.ShapeUtils;
import com.shunlian.app.utils.ShareGoodDialogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.download.DownLoadDialogProgress;
import com.shunlian.app.utils.download.DownloadUtils;
import com.shunlian.app.utils.download.JsDownloadListener;
import com.shunlian.app.widget.BlogBottomDialog;
import com.shunlian.app.widget.FolderTextView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.NewLookBigImgAct;
import com.shunlian.app.widget.NewTextView;
import com.shunlian.app.widget.SaveImgDialog;
import com.shunlian.app.widget.SubBlogCommentItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/23.
 */

public class ActivityDetailAdapter extends BaseRecyclerAdapter<BigImgEntity.Blog> implements BlogBottomDialog.OnDialogCallBack {
    public static final int LAYOUT_TOP = 10003;
    private OnAdapterCallBack mCallBack;
    private HotBlogsEntity.Detail mDetail;
    private TieziAvarAdapter tieziAvarAdapter;
    private BlogBottomDialog blogBottomDialog;
    private SaveImgDialog saveImgDialog;
    private DownLoadDialogProgress downLoadDialogProgress;
    private DownloadUtils downloadUtils;
    private String currentBlogId;
    private ShareGoodDialogUtil shareGoodDialogUtil;
    private ShareInfoParam mShareInfoParam;
    private HttpDialog httpDialog;
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

    public ActivityDetailAdapter(Context context, List<BigImgEntity.Blog> lists, HotBlogsEntity.Detail detail, ShareGoodDialogUtil mShareGoodDialogUtil) {
        super(context, true, lists);
        this.mDetail = detail;
        this.shareGoodDialogUtil = mShareGoodDialogUtil;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof BlogViewHolder) {
            List<BigImgEntity.Blog> blogList = (List<BigImgEntity.Blog>) payloads.get(0);
            BigImgEntity.Blog blog = blogList.get(position - 1);
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
            } else {
                blogViewHolder.animation_zan.setProgress(0f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.value_343434));
            }

            if (blog.is_self == 0) {
                blogViewHolder.miv_more.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
                blogViewHolder.miv_more.setVisibility(View.GONE);
            }

            if (blog.down_num == 0) {
                blogViewHolder.tv_download.setText("下载");
            } else {
                blogViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            }

            if (blog.comment_list == null || isEmpty(blog.comment_list.list) || blog.comment_list.total == 0) {
                blogViewHolder.tv_comment_count.setText("评论");
            } else {
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }

            if (blog.praise_num == 0) {
                blogViewHolder.tv_zan.setText("点赞");
            } else {
                blogViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));
            }

            if (blog.total_share_num == 0) {
                blogViewHolder.tv_share_count.setText("分享");
            } else {
                blogViewHolder.tv_share_count.setText(String.valueOf(blog.total_share_num));
            }

            if (blog.comment_list == null || isEmpty(blog.comment_list.list)) {
                gone(blogViewHolder.ll_comment_rootView);
            } else {
                visible(blogViewHolder.ll_comment_rootView);
                reply(blogViewHolder.ll_comment, blog.comment_list, blog.id);
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
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
        if (holder instanceof BlogViewHolder) {
            BigImgEntity.Blog blog = lists.get(position - 1);
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

            GlideUtils.getInstance().loadCircleAvar(context, blogViewHolder.miv_comment_icon, SharedPrefUtil.getSharedUserString("avatar", ""));
            blogViewHolder.tv_tag.setText(blog.activity_title);
            blogViewHolder.tv_content.setText(blog.text);
            blogViewHolder.tv_content.setBlogText(blog.text);
            blogViewHolder.tv_address.setText(blog.place);
            if (blog.down_num == 0) {
                blogViewHolder.tv_download.setText("下载");
            } else {
                blogViewHolder.tv_download.setText(String.valueOf(blog.down_num));
            }

            if (blog.praise_num == 0) {
                blogViewHolder.tv_zan.setText("点赞");
            } else {
                blogViewHolder.tv_zan.setText(String.valueOf(blog.praise_num));
            }

            if (blog.comment_list == null || isEmpty(blog.comment_list.list) || blog.comment_list.total == 0) {
                blogViewHolder.tv_comment_count.setText("评论");
            } else {
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }

            if (!isEmpty(blog.related_goods)) {
                GoodsDeatilEntity.Goods goods = blog.related_goods.get(0);
                GlideUtils.getInstance().loadImage(context, blogViewHolder.miv_goods_icon, goods.thumb);
                blogViewHolder.tv_goods_name.setText(goods.title);
                blogViewHolder.tv_goods_price.setText(getString(R.string.common_yuan) + goods.price);

                int i = TransformUtil.dip2px(context, 10);
                TransformUtil.expandViewTouchDelegate(blogViewHolder.tv_share_count, i, i, i, i);
                if (blog.total_share_num == 0) {
                    blogViewHolder.tv_share_count.setText("分享");
                } else {
                    blogViewHolder.tv_share_count.setText(String.valueOf(blog.total_share_num));
                }
                blogViewHolder.tv_share_count.setOnClickListener(v -> {
                    mShareInfoParam = new ShareInfoParam();
                    mShareInfoParam.blogId = blog.id;
                    mShareInfoParam.shareLink = goods.share_url;
                    mShareInfoParam.title = goods.title;
                    mShareInfoParam.desc = goods.desc;
                    mShareInfoParam.goods_id = goods.goods_id;
                    mShareInfoParam.price = goods.price;
                    mShareInfoParam.market_price = goods.market_price;
                    mShareInfoParam.img = goods.thumb;
                    mShareInfoParam.isSuperiorProduct = (goods.isSuperiorProduct == 1 ? true : false);
                    mShareInfoParam.userName = SharedPrefUtil.getSharedUserString("nickname", "");
                    mShareInfoParam.userAvatar = SharedPrefUtil.getSharedUserString("avatar", "");
                    shareGoodDialogUtil.shareGoodDialog(mShareInfoParam, true, true);
                    shareGoodDialogUtil.setShareGoods();
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

            blogViewHolder.miv_big_icon.setOnClickListener(v -> {
                //点击查看大图
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) blog.pics;
                bigImgEntity.index = position;
                bigImgEntity.blog = blog;
                NewLookBigImgAct.startAct(context, bigImgEntity);
            });

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
                blogViewHolder.ll_zan.setClickable(false);
            } else {
                blogViewHolder.animation_zan.setProgress(0f);
                blogViewHolder.tv_zan.setTextColor(getColor(R.color.value_343434));
                blogViewHolder.ll_zan.setClickable(true);
            }

            if (blog.is_self == 0) {
                blogViewHolder.miv_more.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.tv_attention.setVisibility(View.GONE);
                blogViewHolder.miv_more.setVisibility(View.GONE);
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

            blogViewHolder.miv_icon.setOnClickListener(v -> MyPageActivity.startAct(context, blog.member_id));

            blogViewHolder.ll_zan.setOnClickListener(v -> {
                if (Common.isAlreadyLogin()) {
                    if (mCallBack != null) {
                        mCallBack.toPraiseBlog(blog.id, blogViewHolder.animation_zan);
                    }
                } else {
                    Common.goGoGo(context, "login");
                }
            });

            blogViewHolder.ll_member.setOnClickListener(v -> {
                MyPageActivity.startAct(context, blog.member_id);
            });

            int i = TransformUtil.dip2px(context, 5);
            TransformUtil.expandViewTouchDelegate(blogViewHolder.miv_more, i, i, i, i);
            blogViewHolder.miv_more.setOnClickListener(v -> {
                showDialog(blog);
            });

            blogViewHolder.rl_video.setOnClickListener(v -> {
                VideoGoodPlayActivity.startActivity(context, blog);
            });

            blogViewHolder.rlayout_goods.setOnClickListener(view -> {
                LogUtil.augusLogW("dddd--" + blog.related_goods.size());
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

            blogViewHolder.tv_comment.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.showCommentView(blog.id);
                }
            });

            if (blog.comment_list == null || isEmpty(blog.comment_list.list)) {
                gone(blogViewHolder.ll_comment_rootView);
            } else {
                visible(blogViewHolder.ll_comment_rootView);
                reply(blogViewHolder.ll_comment, blog.comment_list, blog.id);
                blogViewHolder.tv_comment_count.setText(String.valueOf(blog.comment_list.total));
            }

        }
    }

    public void reply(LinearLayout ll_sub_bg, BigImgEntity.CommentEntity commentEntity, String blogId) {
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

    public void initDialog(BigImgEntity.Blog blog) {
        Dialog dialog_new = new Dialog(context, R.style.popAd);
        dialog_new.setContentView(R.layout.dialog_found_goods);
        Window window = dialog_new.getWindow();
//        //设置边框距离
//        window.getDecorView().setPadding(0, 0, 0, 0);
        //设置dialog位置
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置宽高
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
        MyImageView miv_icon = dialog_new.findViewById(R.id.miv_icon);
        NewTextView ntv_desc = dialog_new.findViewById(R.id.ntv_desc);
        RecyclerView rv_goods = dialog_new.findViewById(R.id.rv_goods);
        miv_close.setOnClickListener(view -> dialog_new.dismiss());
        GlideUtils.getInstance().loadCircleImage(context, miv_icon, blog.avatar);
        SpannableStringBuilder ssb = Common.changeColor(blog.nickname
                + getString(R.string.discover_fenxiangdetuijian), blog.nickname, getColor(R.color.value_007AFF));
        ntv_desc.setText(ssb);
        rv_goods.setLayoutManager(new LinearLayoutManager(context));
        miv_icon.setOnClickListener(view -> MyPageActivity.startAct(context, blog.member_id));
        ntv_desc.setOnClickListener(view -> MyPageActivity.startAct(context, blog.member_id));
        DiscoverGoodsAdapter discoverGoodsAdapter = new DiscoverGoodsAdapter(context, blog.id, blog.related_goods, false,
                SharedPrefUtil.getSharedUserString("nickname", ""), SharedPrefUtil.getSharedUserString("avatar", ""), dialog_new);
        rv_goods.setAdapter(discoverGoodsAdapter);
        discoverGoodsAdapter.setOnItemClickListener((view, position) -> GoodsDetailAct.startAct(context, blog.related_goods.get(position).goods_id));
        rv_goods.addItemDecoration(new MVerticalItemDecoration(context, 36, 38, 38));
        dialog_new.setCancelable(false);
        dialog_new.show();
    }


    public void setTextDrawable(TextView textView, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());// 设置边界
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

            int screenWidth = DeviceInfoUtil.getDeviceWidth(context);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) detailTopViewHolder.miv_icon.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            double imgHeight = screenWidth / 710d * 400;
            layoutParams.height = (int) Math.round(imgHeight);
            detailTopViewHolder.miv_icon.setLayoutParams(layoutParams);
            detailTopViewHolder.ll_top.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return LAYOUT_TOP;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void addFavo(int favo, String blogId) {
        for (int i = 0; i < lists.size(); i++) {
            BigImgEntity.Blog blog = lists.get(i);
            if (blogId.equals(blog.id)) {
                blog.is_favo = favo;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDel(String blogId) {

    }

    @Override
    public void withdrawBlog(String blogId) {

    }


    public class DetailTopViewHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder {

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        @BindView(R.id.ll_top)
        LinearLayout ll_top;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.recycler_list)
        RecyclerView recycler_list;

        @BindView(R.id.tv_join_count)
        TextView tv_join_count;

        @BindView(R.id.tv_activity_content)
        FolderTextView tv_activity_content;

        public DetailTopViewHolder(View itemView) {
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

        @BindView(R.id.ll_zan)
        LinearLayout ll_zan;

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

        @BindView(R.id.rlayout_goods)
        RelativeLayout rlayout_goods;

        @BindView(R.id.recylcer_attention)
        RecyclerView recylcer_attention;

        @BindView(R.id.tv_comment)
        TextView tv_comment;

        @BindView(R.id.tv_comment_title)
        TextView tv_comment_title;

        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        @BindView(R.id.ll_comment)
        LinearLayout ll_comment;

        @BindView(R.id.ll_comment_rootView)
        LinearLayout ll_comment_rootView;

        @BindView(R.id.miv_comment_icon)
        MyImageView miv_comment_icon;

        public BlogViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void showDialog(BigImgEntity.Blog blog) {
        if (blogBottomDialog == null) {
            blogBottomDialog = new BlogBottomDialog(context);
            blogBottomDialog.setOnDialogCallBack(this);
        }
        blogBottomDialog.setBlog(blog);
        blogBottomDialog.show();
    }

    public void setAdapterCallBack(OnAdapterCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnAdapterCallBack {
        void toFocusUser(int isFocus, String memberId, String nickName);

        void toPraiseBlog(String blogId, LottieAnimationView lottieAnimationView);

        void toDown(String blogId);

        void OnTopSize(int height);

        void showCommentView(String blogId);
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