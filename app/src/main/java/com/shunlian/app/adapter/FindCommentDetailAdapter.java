package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.FindCommentListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/15.
 */

public class FindCommentDetailAdapter extends BaseRecyclerAdapter<FindCommentListEntity.ItemComment> {

    private final LayoutInflater inflater;
    private OnPointFabulousListener mFabulousListener;
    private List<FindCommentListEntity.LastLikesBean> last_likes = new ArrayList<>();
    private SimpleRecyclerAdapter adapter;

    public FindCommentDetailAdapter(Context context, List<FindCommentListEntity.ItemComment> lists) {
        super(context, true, lists);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_find_comment_detail_one, parent, false);
        return new FindCommentDetailHolder(view);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FindCommentDetailHolder){
            FindCommentDetailHolder mHolder = (FindCommentDetailHolder) holder;
            FindCommentListEntity.ItemComment lastLikesBean = lists.get(position);

            GlideUtils.getInstance().loadCircleHeadImage(context,mHolder.civ_head,lastLikesBean.avatar);

            mHolder.mtv_name.setText(lastLikesBean.nickname);

            String level = lastLikesBean.level;
            Bitmap bitmap = TransformUtil.convertNewVIP(context, level);
            mHolder.miv_vip.setImageBitmap(bitmap);

            mHolder.mtv_time.setText(lastLikesBean.add_time);

            if (!isEmpty(lastLikesBean.at)){
                String source = lastLikesBean.at.concat(lastLikesBean.content);
                SpannableStringBuilder changetextbold = Common.changetextbold(source, lastLikesBean.at);
                mHolder.mtv_content.setText(changetextbold);
            }else {
                mHolder.mtv_content.setText(lastLikesBean.content);
            }

            if (position == 0){
                mHolder.itemView.setBackgroundColor(getColor(R.color.white));
                gone(mHolder.mtv_zan_count,mHolder.miv_zan);
                visible(mHolder.ll_head_portrait);
                mHolder.mtv_zan_count1.setText(lastLikesBean.likes);
                if ("1".equals(lastLikesBean.had_like)){
                    mHolder.miv_zan1.setImageResource(R.mipmap.img_pingjia_zan_h);
                    mHolder.mtv_zan_count1.setTextColor(getColor(R.color.pink_color));
                }else {
                    mHolder.mtv_zan_count1.setTextColor(getColor(R.color.share_text));
                    mHolder.miv_zan1.setImageResource(R.mipmap.img_pingjia_zan_n);
                }
                if (last_likes.size() == 0) {
                    last_likes.addAll(lastLikesBean.last_likes);
                }
                setHeadPic(mHolder);

            }else {
                mHolder.itemView.setBackgroundColor(getColor(R.color.white_ash));
                visible(mHolder.mtv_zan_count,mHolder.miv_zan);
                gone(mHolder.ll_head_portrait);
                mHolder.mtv_zan_count.setText(lastLikesBean.likes);
                if ("1".equals(lastLikesBean.had_like)){
                    mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
                    mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
                }else {
                    mHolder.mtv_zan_count.setTextColor(getColor(R.color.share_text));
                    mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
                }
            }

            if (!isEmpty(lastLikesBean.plus_role)){//大于0为plus以上等级，1PLUS店主，2主管，>=3经理
                visible(mHolder.miv_medal);
                int plusRole = Integer.parseInt(lastLikesBean.plus_role);
                if (plusRole == 1){
                    mHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_dianzhu);
                }else if (plusRole == 2){
                    mHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_zhuguan);
                }else if (plusRole >= 3){
                    mHolder.miv_medal.setImageResource(R.mipmap.img_plus_phb_jingli);
                }else {
                    gone(mHolder.miv_medal);
                }
            }else {
                gone(mHolder.miv_medal);
            }
        }
    }

    private void setHeadPic(FindCommentDetailHolder mHolder) {
        adapter = new SimpleRecyclerAdapter<FindCommentListEntity.LastLikesBean>
                (context, R.layout.item_pic_circle,last_likes) {

            @Override
            public void convert(SimpleViewHolder holder,
                                FindCommentListEntity.LastLikesBean lastLikesBean,
                                int position) {
                MyImageView miv_pic = holder.getView(R.id.civ_pic);
                int w = TransformUtil.dip2px(context, 25);
                int m = TransformUtil.dip2px(context, 2);
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) miv_pic.getLayoutParams();
                layoutParams.width = w;
                layoutParams.height = w;
                layoutParams.rightMargin = m;
                miv_pic.setLayoutParams(layoutParams);
                GlideUtils.getInstance().loadCircleHeadImage(context,miv_pic,lastLikesBean.avatar);
            }
        };
        mHolder.recy_view.setAdapter(adapter);
    }

    public void setHeadPic(List<FindCommentListEntity.LastLikesBean> last_likes) {
        if (!isEmpty(last_likes)){
            this.last_likes.clear();
            this.last_likes.addAll(last_likes);
            if (adapter != null){
                adapter.notifyDataSetChanged();
            }
        }
    }

    public class FindCommentDetailHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.civ_head)
        MyImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.ll_head_portrait)
        LinearLayout  ll_head_portrait;

        @BindView(R.id.mtv_zan_count1)
        MyTextView mtv_zan_count1;

        @BindView(R.id.miv_zan1)
        MyImageView miv_zan1;

        @BindView(R.id.miv_medal)
        MyImageView miv_medal;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;
        public FindCommentDetailHolder(View itemView) {
            super(itemView);
            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(mtv_zan_count,i,i,i,i);
            TransformUtil.expandViewTouchDelegate(mtv_zan_count1,i,i,i,i);

            LinearLayoutManager manager = new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL,false);
            recy_view.setLayoutManager(manager);
            recy_view.setNestedScrollingEnabled(false);


            itemView.setOnClickListener(this);
            mtv_zan_count.setOnClickListener(this);
            mtv_zan_count1.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.mtv_zan_count:
                case R.id.mtv_zan_count1:
                    if (mFabulousListener != null){
                        mFabulousListener.onPointFabulous(getAdapterPosition());
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

    public void setPointFabulousListener(OnPointFabulousListener fabulousListener){

        mFabulousListener = fabulousListener;
    }

    public interface OnPointFabulousListener{
        void onPointFabulous(int position);
    }
}
