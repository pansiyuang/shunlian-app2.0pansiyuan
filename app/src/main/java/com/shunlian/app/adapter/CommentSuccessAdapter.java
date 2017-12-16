package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommentSuccessAdapter extends BaseRecyclerAdapter<String> {

    public static final int HEAD = 2;


    public CommentSuccessAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                View view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false);
                return new HeadHolder(view);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case HEAD:
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }

    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment_success, parent, false);
        return new CommentSuccessHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        CommentSuccessHolder mHolder = (CommentSuccessHolder) holder;
        if (position == 1){
            mHolder.line.setVisibility(View.GONE);
            mHolder.mtv_title.setText("接着评下去");
            mHolder.line_bottom.setVisibility(View.VISIBLE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.white));
        }else if (position == 2){
            mHolder.line.setVisibility(View.VISIBLE);
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_title.setText("这些商品可追加");
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.value_FEEAEA));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setText("去追评");
        }else {
            mHolder.line.setVisibility(View.GONE);
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.value_FEEAEA));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setText("去追评");
        }
    }

    public class HeadHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public HeadHolder(View itemView) {
            super(itemView);
            miv_pic.setScaleType(ImageView.ScaleType.FIT_XY);
            miv_pic.setImageResource(R.mipmap.img_pingjiachenggong);
            miv_pic.setBackgroundColor(getColor(R.color.white));
        }
    }

    public class CommentSuccessHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.line)
        View line;

        @BindView(R.id.line_bottom)
        View line_bottom;

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.mtv_go_comment)
        MyTextView mtv_go_comment;


        public CommentSuccessHolder(View itemView) {
            super(itemView);
            mtv_go_comment.setWHProportion(116,116);
        }
    }
}
