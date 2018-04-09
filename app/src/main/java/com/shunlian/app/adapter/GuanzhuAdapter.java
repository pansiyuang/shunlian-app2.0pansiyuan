package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.ui.discover.jingxuan.TagDetailActivity;
import com.shunlian.app.ui.discover.other.CommentListAct;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.ClickableColorSpan;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GuanzhuAdapter extends BaseRecyclerAdapter<GuanzhuEntity.DynamicListBean> {

    private OnFollowShopListener mShopListener;
    private OnShareLikeListener mShareLikeListener;
    private final SpannableStringBuilder ssb;
    private final StringBuilder sb;

    public GuanzhuAdapter(Context context, List<GuanzhuEntity.DynamicListBean> lists) {
        super(context, true, lists);
        ssb = new SpannableStringBuilder();
        sb = new StringBuilder();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guanzhu, parent, false);
        return new GuanzhuHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GuanzhuHolder) {
            GuanzhuEntity.DynamicListBean dy = lists.get(position);
            GuanzhuHolder mHolder = (GuanzhuHolder) holder;
            GlideUtils.getInstance().loadImage(context, mHolder.civ_head, dy.store_logo);
            mHolder.mtv_name.setText(dy.store_name);
            String full_title = "";
            if ("new_sales".equals(dy.type)) {//上新
                gone(mHolder.miv_pic, mHolder.mtv_title, mHolder.ll_Statistics);
                visible(mHolder.recy_view, mHolder.mtv_babyNum);
                full_title = dy.add_time;
                setPicMatrix(mHolder, dy.goods_list);
            } else {
                visible(mHolder.miv_pic, mHolder.mtv_title, mHolder.ll_Statistics);
                gone(mHolder.recy_view, mHolder.mtv_babyNum);
                mHolder.mtv_title.setText(dy.title);
                GlideUtils.getInstance().loadImage(context, mHolder.miv_pic, dy.thumb);
                full_title = dy.full_title;
                mHolder.mtv_fx_count.setText(dy.forwards);
                mHolder.mtv_pl_count.setText(dy.comments);
                mHolder.mtv_zan_count.setText(dy.likes);
                if ("1".equals(dy.has_like)){//点赞
                    mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
                    mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
                }else {
                    mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
                    mHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
                }
            }
            SpannableStringBuilder tags = getTags(dy.tags,full_title,"new_sales".equals(dy.type));
            if (!isEmpty(tags)) {
                mHolder.mtv_desc.setText(tags);
                mHolder.mtv_desc.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                mHolder.mtv_desc.setText(full_title);
            }

            GradientDrawable gradientDrawable = (GradientDrawable) mHolder.mtv_follow.getBackground();
            if ("1".equals(dy.has_follow)){
                gradientDrawable.setColor(getColor(R.color.white));
                mHolder.mtv_follow.setTextColor(getColor(R.color.pink_color));
                mHolder.mtv_follow.setText(getString(R.string.discover_alear_follow));
            }else {
                gradientDrawable.setColor(getColor(R.color.pink_color));
                mHolder.mtv_follow.setTextColor(getColor(R.color.white));
                mHolder.mtv_follow.setText(getString(R.string.discover_follow));
            }
        }
    }

    private void setPicMatrix(GuanzhuHolder mHolder, final List<GuanzhuEntity.TagsBean> goods_list) {
        if (!isEmpty(goods_list)) {
            String formatNum = getString(R.string.discover_baby);
            SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<GuanzhuEntity.TagsBean>
                    (context, R.layout.item_detail, goods_list) {

                @Override
                public void convert(SimpleViewHolder holder, GuanzhuEntity.TagsBean tagsBean, int position) {
                    holder.addOnClickListener(R.id.miv_pic);
                    MyImageView miv_pic = holder.getView(R.id.miv_pic);
                    miv_pic.setWHProportion(206, 206);
                    GlideUtils.getInstance().loadImage(context, miv_pic, tagsBean.thumb);
                }
            };
            mHolder.recy_view.setAdapter(adapter);
            mHolder.mtv_babyNum.setText(String.format(formatNum, String.valueOf(goods_list.size())));
            adapter.setOnItemClickListener((view,position) ->{
                GuanzhuEntity.TagsBean tagsBean = goods_list.get(position);
                GoodsDetailAct.startAct(context, tagsBean.id);
            });
        } else {
            gone(mHolder.mtv_babyNum);
        }
    }

    private SpannableStringBuilder getTags(List<GuanzhuEntity.TagsBean> tagsBeans,
                                           String full_title, boolean isNew) {
        if (!isEmpty(tagsBeans)) {
            String format = "#%s# ";
            sb.delete(0,sb.length());
            ssb.clear();
            for (int i = 0; i < tagsBeans.size(); i++) {
                GuanzhuEntity.TagsBean tagsBean = tagsBeans.get(i);

                sb.append(String.format(format, tagsBean.name));
                ssb.append(String.format(format, tagsBean.name));

                ClickableColorSpan span = new ClickableColorSpan
                        (i,getColor(R.color.value_299FFA));
                if (!isNew) {
                    span.setOnClickItemListener((position) ->
                        TagDetailActivity.startAct(context, tagsBeans.get(position).id)
                    );
                }
                ssb.setSpan(span,sb.length()-
                                String.format(format, tagsBean.name).length(),sb.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            ssb.append(full_title);
            return ssb;
        }
        return null;
    }

    public class GuanzhuHolder extends BaseRecyclerViewHolder implements View.OnClickListener {
        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_follow)
        MyTextView mtv_follow;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.mtv_babyNum)
        MyTextView mtv_babyNum;

        @BindView(R.id.ll_Statistics)
        LinearLayout ll_Statistics;

        @BindView(R.id.mtv_fx_count)
        MyTextView mtv_fx_count;

        @BindView(R.id.mtv_pl_count)
        MyTextView mtv_pl_count;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.ll_fenxiang)
        LinearLayout ll_fenxiang;

        @BindView(R.id.ll_pinglun)
        LinearLayout ll_pinglun;

        @BindView(R.id.ll_zan)
        LinearLayout ll_zan;

        public GuanzhuHolder(View itemView) {
            super(itemView);
            mtv_follow.setWHProportion(125, 44);
            miv_pic.setWHProportion(720, 384);
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            recy_view.setLayoutManager(manager);
            recy_view.setNestedScrollingEnabled(false);
            int i = TransformUtil.dip2px(context, 6);
            GrideItemDecoration grideItemDecoration = new GrideItemDecoration(0, i, i, 0, true);
            recy_view.addItemDecoration(grideItemDecoration);
            mtv_follow.setOnClickListener(this);
            itemView.setOnClickListener(this);
            ll_zan.setOnClickListener(this);
            ll_pinglun.setOnClickListener(this);
            ll_fenxiang.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mtv_follow:
                    if (mShopListener != null){
                        mShopListener.onFollow(getAdapterPosition());
                    }
                    break;
                case R.id.ll_zan://点赞
                    if (mShopListener != null){
                        mShareLikeListener.onItemPosition(getAdapterPosition(),false);
                    }
                    break;
                case R.id.ll_pinglun://评论
                    GuanzhuEntity.DynamicListBean dy = lists.get(getAdapterPosition());
                    CommentListAct.startAct((Activity) context,dy.id);
                    break;
                case R.id.ll_fenxiang://分享
                    if (mShopListener != null){
                        mShareLikeListener.onItemPosition(getAdapterPosition(),true);
                    }
                    break;
                default:
                    if (listener != null){
                        listener.onItemClick(v,getAdapterPosition());
                    }
                    break;
            }
        }
    }

    public void setOnFollowShopListener(OnFollowShopListener shopListener){

        mShopListener = shopListener;
    }

    /**
     * 关注店铺监听
     */
    public interface OnFollowShopListener{
        void onFollow(int position);
    }

    public void setOnShareLikeListener(OnShareLikeListener shareLikeListener){

        mShareLikeListener = shareLikeListener;
    }

    public interface OnShareLikeListener{
        /**
         *
         * @param position 条目位置
         * @param isShare 点击是否是分享按钮
         */
        void onItemPosition(int position,boolean isShare);
    }
}
