package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.GuanzhuEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GrideItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/16.
 */

public class GuanzhuAdapter extends BaseRecyclerAdapter<GuanzhuEntity.DynamicListBean> {

    public GuanzhuAdapter(Context context, List<GuanzhuEntity.DynamicListBean> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_guanzhu, parent, false);
        return new GuanzhuHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GuanzhuHolder){
            GuanzhuEntity.DynamicListBean dy = lists.get(position);
            GuanzhuHolder mHolder = (GuanzhuHolder) holder;
            GlideUtils.getInstance().loadImage(context,mHolder.civ_head,dy.store_logo);
            mHolder.mtv_name.setText(dy.store_name);
            String full_title = "";
            if ("new_sales".equals(dy.type)){//上新
                gone(mHolder.miv_pic,mHolder.mtv_title,mHolder.ll_Statistics);
                visible(mHolder.recy_view,mHolder.mtv_babyNum);
                full_title = dy.add_time;
                setPicMatrix(mHolder,dy.goods_list);
            }else {
                visible(mHolder.miv_pic,mHolder.mtv_title,mHolder.ll_Statistics);
                gone(mHolder.recy_view,mHolder.mtv_babyNum);
                mHolder.mtv_title.setText(dy.title);
                GlideUtils.getInstance().loadImage(context,mHolder.miv_pic,dy.thumb);
                full_title = dy.full_title;
                mHolder.mtv_fx_count.setText(dy.forwards);
                mHolder.mtv_pl_count.setText(dy.comments);
                mHolder.mtv_zan_count.setText(dy.likes);
            }
            String tags = getTags(dy.tags);
            if (!isEmpty(tags)){
                SpannableStringBuilder sb = Common
                        .changeColor(tags.concat(full_title), tags, getColor(R.color.value_299FFA));
                mHolder.mtv_desc.setText(sb);
            }else {
                mHolder.mtv_desc.setText(full_title);
            }
        }
    }

    private void setPicMatrix(GuanzhuHolder mHolder, List<GuanzhuEntity.TagsBean> goods_list) {
        if (!isEmpty(goods_list)){
            String formatNum = "%s件宝贝";
            SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<GuanzhuEntity.TagsBean>
                    (context,R.layout.item_detail,goods_list) {

                @Override
                public void convert(SimpleViewHolder holder, GuanzhuEntity.TagsBean tagsBean, int position) {
                    MyImageView miv_pic = holder.getView(R.id.miv_pic);
                    miv_pic.setWHProportion(206,206);
                    GlideUtils.getInstance().loadImage(context,miv_pic,tagsBean.thumb);
                }
            };
            mHolder.recy_view.setAdapter(adapter);
            mHolder.mtv_babyNum.setText(String.format(formatNum,String.valueOf(goods_list.size())));
        }else {
            gone(mHolder.mtv_babyNum);
        }
    }

    private String getTags(List<GuanzhuEntity.TagsBean> tagsBeans){
        if (!isEmpty(tagsBeans)){
            StringBuilder sb = new StringBuilder();
            String format = "#%s#";
            for (int i = 0; i < tagsBeans.size(); i++) {
                GuanzhuEntity.TagsBean tagsBean = tagsBeans.get(i);
                sb.append(String.format(format,tagsBean.name));
            }
            return sb.toString();
        }

        return null;
    }

    public class GuanzhuHolder extends BaseRecyclerViewHolder{
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

        public GuanzhuHolder(View itemView) {
            super(itemView);
            mtv_follow.setWHProportion(125,44);
            miv_pic.setWHProportion(720,384);
            GridLayoutManager manager = new GridLayoutManager(context,3);
            recy_view.setLayoutManager(manager);
            recy_view.setNestedScrollingEnabled(false);
            int i = TransformUtil.dip2px(context, 6);
            GrideItemDecoration grideItemDecoration = new GrideItemDecoration(0,i,i,0,true);
            recy_view.addItemDecoration(grideItemDecoration);
        }
    }
}
