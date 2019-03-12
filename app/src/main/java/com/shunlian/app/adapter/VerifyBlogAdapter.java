package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
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
import com.shunlian.app.ui.discover_new.ActivityDetailActivity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.discover_new.VideoGoodPlayActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.BlogGoodsShareDialog;
import com.shunlian.app.widget.FolderTextView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewLookBigImgAct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VerifyBlogAdapter extends BaseRecyclerAdapter<BigImgEntity.Blog> {

    private boolean isEdit;
    private OnItemCallBack mCallBack;

    public VerifyBlogAdapter(Context context, List<BigImgEntity.Blog> lists) {
        super(context, true, lists);
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyItemRangeChanged(0, lists.size(), lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new VerifyBlogViewholder(LayoutInflater.from(context).inflate(R.layout.item_verify_blog, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (!isEmpty(payloads) && holder instanceof HotBlogAdapter.BlogViewHolder) {
            BigImgEntity.Blog blog;
            List<BigImgEntity.Blog> blogList = (List<BigImgEntity.Blog>) payloads.get(0);
            blog = blogList.get(position);
            VerifyBlogViewholder blogViewHolder = (VerifyBlogViewholder) holder;
            GlideUtils.getInstance().loadCircleAvar(context, blogViewHolder.miv_icon, blog.avatar);
            blogViewHolder.tv_name.setText(blog.nickname);
            blogViewHolder.tv_time.setText(blog.time_desc);

            if (isEdit) {
                blogViewHolder.miv_icon.setVisibility(View.GONE);
                blogViewHolder.miv_v.setVisibility(View.GONE);
                blogViewHolder.miv_select.setVisibility(View.VISIBLE);
                blogViewHolder.rl_verify.setVisibility(View.INVISIBLE);
                blogViewHolder.tv_withdraw.setVisibility(View.GONE);
            } else {
                blogViewHolder.miv_icon.setVisibility(View.VISIBLE);
                blogViewHolder.miv_v.setVisibility(View.VISIBLE);
                blogViewHolder.miv_select.setVisibility(View.GONE);
                if (blog.status == 0) {
                    blogViewHolder.tv_withdraw.setVisibility(View.GONE);
                    blogViewHolder.rl_verify.setVisibility(View.VISIBLE);
                } else {
                    blogViewHolder.tv_withdraw.setVisibility(View.VISIBLE);
                    blogViewHolder.rl_verify.setVisibility(View.GONE);
                }
            }

            if (blog.isSelect) {
                blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_xuanzhong);
            } else {
                blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_moren);
            }

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
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VerifyBlogViewholder) {
            BigImgEntity.Blog blog = lists.get(position);
            VerifyBlogViewholder blogViewHolder = (VerifyBlogViewholder) holder;
            GlideUtils.getInstance().loadCircleAvar(context, blogViewHolder.miv_icon, blog.avatar);
            blogViewHolder.tv_name.setText(blog.nickname);
            blogViewHolder.tv_time.setText(blog.time_desc);

            if (isEdit) {
                blogViewHolder.miv_icon.setVisibility(View.GONE);
                blogViewHolder.miv_v.setVisibility(View.GONE);
                blogViewHolder.miv_select.setVisibility(View.VISIBLE);
                blogViewHolder.rl_verify.setVisibility(View.INVISIBLE);
                blogViewHolder.tv_withdraw.setVisibility(View.GONE);
            } else {
                blogViewHolder.miv_icon.setVisibility(View.VISIBLE);
                blogViewHolder.miv_v.setVisibility(View.VISIBLE);
                blogViewHolder.miv_select.setVisibility(View.GONE);
                if (blog.status == 0) {
                    blogViewHolder.tv_withdraw.setVisibility(View.GONE);
                    blogViewHolder.rl_verify.setVisibility(View.VISIBLE);
                } else {
                    blogViewHolder.tv_withdraw.setVisibility(View.VISIBLE);
                    blogViewHolder.rl_verify.setVisibility(View.GONE);
                }
            }

            if (blog.isSelect) {
                blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_xuanzhong);
            } else {
                blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_moren);
            }

            int i = TransformUtil.dip2px(context, 10);
            TransformUtil.expandViewTouchDelegate(blogViewHolder.miv_select, i, i, i, i);
            blogViewHolder.miv_select.setOnClickListener(v -> {
                if (blog.isSelect) {
                    blog.isSelect = false;
                    blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_moren);
                } else {
                    blog.isSelect = true;
                    blogViewHolder.miv_select.setImageResource(R.mipmap.ic_fukuan_xuanzhong);
                }

                if (mCallBack != null) {
                    mCallBack.OnItemSelect(position, blog.isSelect);
                }
            });

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

                blogViewHolder.rlayout_goods.setVisibility(View.VISIBLE);
            } else {
                blogViewHolder.rlayout_goods.setVisibility(View.GONE);
            }
            if (blog.type == 1) { //图文
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

            blogViewHolder.tv_attention.setOnClickListener(v -> {
                if (Common.isAlreadyLogin()) {
                    if (mCallBack != null) {
                        mCallBack.toFocusUser(blog.is_focus, blog.member_id, blog.nickname, position);
                    }
                } else {
                    Common.goGoGo(context, "login");
                }
            });

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

            blogViewHolder.tv_verify_hot.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.OnItemPassHot(position);
                }
            });

            blogViewHolder.tv_verify_main.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.OnItemPassMain(position);
                }
            });

            blogViewHolder.tv_withdraw.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.OnItemWithDraw(position);
                }
            });

            blogViewHolder.tv_verify_reject.setOnClickListener(v -> {
                if (mCallBack != null) {
                    mCallBack.OnItemReject(position);
                }
            });
        }
    }

    public void initDialog(BigImgEntity.Blog blog) {
        BlogGoodsShareDialog.startAct(context, blog);
    }

    public class VerifyBlogViewholder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_expert)
        MyImageView miv_expert;

        @BindView(R.id.miv_select)
        MyImageView miv_select;

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

        @BindView(R.id.miv_big_icon)
        MyImageView miv_big_icon;

        @BindView(R.id.miv_video)
        MyImageView miv_video;

        @BindView(R.id.rl_video)
        RelativeLayout rl_video;

        @BindView(R.id.miv_v)
        MyImageView miv_v;

        @BindView(R.id.rl_verify)
        RelativeLayout rl_verify;

        @BindView(R.id.tv_verify_main)
        TextView tv_verify_main;

        @BindView(R.id.tv_verify_hot)
        TextView tv_verify_hot;

        @BindView(R.id.tv_verify_reject)
        TextView tv_verify_reject;

        @BindView(R.id.tv_withdraw)
        TextView tv_withdraw;

        public VerifyBlogViewholder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemCallBack(OnItemCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnItemCallBack {

        void toFocusUser(int focus, String memberId, String nickName, int position);

        void OnItemSelect(int position, boolean isSelect);

        void OnItemPassHot(int position);

        void OnItemPassMain(int position);

        void OnItemReject(int position);

        void OnItemWithDraw(int position);
    }
}
