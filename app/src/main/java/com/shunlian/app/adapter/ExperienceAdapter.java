package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.ExperienceEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.discover.DiscoverXindeFrag;
import com.shunlian.app.ui.discover.other.ExperienceDetailAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.utils.MyOnClickListener.isFastClick;

/**
 * Created by Administrator on 2018/3/16.
 */

public class ExperienceAdapter extends BaseRecyclerAdapter<ExperienceEntity.Experience> {
    private DiscoverXindeFrag mFragment;

    public ExperienceAdapter(Context context, List<ExperienceEntity.Experience> lists, DiscoverXindeFrag fragment) {
        super(context, true, lists);
        this.mFragment = fragment;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new ExperienceHolderView(LayoutInflater.from(context).inflate(R.layout.item_experience, parent, false));
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    @Override
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        super.setFooterHolderParams(baseFooterHolder);
        baseFooterHolder.layout_load_error.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_normal.setBackgroundColor(getColor(R.color.white_ash));
        baseFooterHolder.layout_no_more.setTextSize(12);
        baseFooterHolder.layout_load_error.setTextSize(12);
        baseFooterHolder.mtv_loading.setTextSize(12);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        if (isEmpty(payloads)) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            ExperienceHolderView holderView = (ExperienceHolderView) holder;
            ExperienceEntity.Experience experience = (ExperienceEntity.Experience) payloads.get(0);
            setPraiseImg(experience.praise, holderView.tv_evaluate_count);
            holderView.tv_evaluate_count.setText(experience.praise_num);
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ExperienceHolderView) {
            ExperienceHolderView holderView = (ExperienceHolderView) holder;
            final ExperienceEntity.Experience experience = lists.get(position);
            ExperienceEntity.MemberInfo memberInfo = experience.member_info;

            GlideUtils.getInstance().loadCircleImage(context, holderView.miv_avatar, memberInfo.avatar);
            holderView.tv_name.setText(memberInfo.nickname);
            holderView.tv_date.setText(experience.add_time);
            holderView.tv_content.setText(experience.content);
            holderView.tv_content.setMaxLines(5);
            holderView.tv_content.setEllipsize(TextUtils.TruncateAt.END);

            if (!isEmpty(experience.image)) {
                if (experience.image.size() == 1) {
                    gone(holderView.recycler_img);
                    visible(holderView.miv_pic);
                    GlideUtils.getInstance().loadImageShu(context, holderView.miv_pic, experience.image.get(0));
                    holderView.miv_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BigImgEntity bigImgEntity = new BigImgEntity();
                            bigImgEntity.itemList = (ArrayList<String>) experience.image;
                            bigImgEntity.index = 0;
                            LookBigImgAct.startAct(context, bigImgEntity);
                        }
                    });
                } else {
                    gone(holderView.miv_pic);
                    visible(holderView.recycler_img);
                    GridImageAdapter gridImageAdapter = new GridImageAdapter(context, experience.image);
                    holderView.recycler_img.setAdapter(gridImageAdapter);
                    gridImageAdapter.setOnItemClickListener((view, position1) -> {
                        BigImgEntity bigImgEntity = new BigImgEntity();
                        bigImgEntity.itemList = (ArrayList<String>) experience.image;
                        bigImgEntity.index = position1;
                        LookBigImgAct.startAct(context, bigImgEntity);
                    });
                }
            }

            final GoodsDeatilEntity.Goods goods = experience.goods;
            if (experience.goods == null) {
                holderView.ll_goods.setVisibility(View.GONE);
            } else {
                GlideUtils.getInstance().loadImage(context, holderView.miv_icon, goods.thumb);
                holderView.tv_title.setText(goods.title);
                String price = getString(R.string.common_yuan) + goods.price;
                holderView.tv_price.setText(price);
                holderView.ll_goods.setVisibility(View.VISIBLE);
            }

            holderView.tv_comment_count.setText(experience.comment_num);
            holderView.tv_evaluate_count.setText(experience.praise_num);

            holderView.ll_goods.setOnClickListener(v -> {
                if (!isEmpty(goods.id)) {
                    GoodsDetailAct.startAct(context, goods.id);
                }
            });
            holderView.tv_evaluate_count.setOnClickListener(v -> {
                if (isFastClick()) {
                    return;
                }
                if ("1".equals(experience.praise)) {
                    mFragment.toPraiseExperience(experience.id, "2");
                } else {
                    mFragment.toPraiseExperience(experience.id, "1");
                }
            });
            setPraiseImg(experience.praise, holderView.tv_evaluate_count);

            holderView.recycler_img.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ExperienceDetailAct.startAct(context, experience.id);
                }
                return false;
            });
        }
    }

    public void setPraiseImg(String status, TextView textView) {
        Drawable drawable;
        if ("2".equals(status)) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_found_zan_n);
        } else {
            drawable = context.getResources().getDrawable(R.mipmap.icon_found_zan_h);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public class ExperienceHolderView extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.miv_avatar)
        MyImageView miv_avatar;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.recycler_img)
        RecyclerView recycler_img;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_comment_count)
        TextView tv_comment_count;

        @BindView(R.id.tv_evaluate_count)
        TextView tv_evaluate_count;

        @BindView(R.id.ll_goods)
        LinearLayout ll_goods;

        public ExperienceHolderView(View itemView) {
            super(itemView);
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(context, 6f), false);
            ((DefaultItemAnimator) recycler_img.getItemAnimator()).setSupportsChangeAnimations(false);
            recycler_img.setLayoutManager(manager);
            recycler_img.addItemDecoration(gridSpacingItemDecoration);
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
