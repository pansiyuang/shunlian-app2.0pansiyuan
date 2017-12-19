package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.bean.CommentSuccessEntity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class CommentSuccessAdapter extends BaseRecyclerAdapter<CommentSuccessEntity.Comment> {

    public static final int HEAD = 2;
    private int mCommentSize;
    private View headView;


    public CommentSuccessAdapter(Context context, boolean isShowFooter,
                                 List<CommentSuccessEntity.Comment> lists, int commentSize) {
        super(context, isShowFooter, lists);
        mCommentSize = commentSize;
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
        if (isEmpty(lists)){
            return 2;
        }else {
            return super.getItemCount() + 1;
        }
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
        if (isEmpty(lists)){
            mHolder.nei_empty.setVisibility(View.VISIBLE);
            mHolder.line.setVisibility(View.GONE);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mll_content.setVisibility(View.GONE);
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText(getString(R.string.no_comment_goods)).setButtonText("");
            return;
        }
        CommentSuccessEntity.Comment comment = lists.get(position - 1);

        if (position == 1 && mCommentSize != 0) {
            mHolder.line.setVisibility(View.GONE);
            mHolder.mtv_title.setText(getString(R.string.comment_next));
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.white));
        } else if (position < mCommentSize) {

            mHolder.line.setVisibility(View.GONE);
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.white));
            mHolder.mtv_go_comment.setText(getString(R.string.go_comment));

        } else if (position == mCommentSize) {
            mHolder.line.setVisibility(View.GONE);
            mHolder.line_bottom.setVisibility(View.VISIBLE);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.white));
            mHolder.mtv_go_comment.setText(getString(R.string.go_comment));
        } else if (position == mCommentSize + 1) {
            if (mCommentSize == 0){
                mHolder.line.setVisibility(View.GONE);
            }else {
                mHolder.line.setVisibility(View.VISIBLE);
            }
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_title.setText(getString(R.string.this_append_comment));
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.value_FEEAEA));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setText(getString(R.string.go_append_comment));
        } else {
            mHolder.line.setVisibility(View.GONE);
            mHolder.line_bottom.setVisibility(View.GONE);
            mHolder.mtv_title.setVisibility(View.GONE);
            mHolder.mtv_go_comment.setBackgroundColor(getColor(R.color.value_FEEAEA));
            mHolder.mtv_go_comment.setTextColor(getColor(R.color.pink_color));
            mHolder.mtv_go_comment.setText(getString(R.string.go_append_comment));
        }

        mHolder.mtv_content.setText(comment.title);
        GlideUtils.getInstance().loadImage(context, mHolder.miv_pic, comment.thumb);
    }

    public class HeadHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        public HeadHolder(final View itemView) {
            super(itemView);
            miv_pic.setScaleType(ImageView.ScaleType.FIT_XY);
            miv_pic.setImageResource(R.mipmap.img_pingjiachenggong);
            miv_pic.setBackgroundColor(getColor(R.color.white));
            headView = itemView;
        }
    }

    public class CommentSuccessHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

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

        @BindView(R.id.nei_empty)
        NetAndEmptyInterface nei_empty;

        @BindView(R.id.mll_content)
        MyLinearLayout mll_content;

        public CommentSuccessHolder(View itemView) {
            super(itemView);
            mtv_go_comment.setWHProportion(116, 116);
            mtv_go_comment.setOnClickListener(this);
            if (isEmpty(lists)){
                itemView.post(new Runnable() {
                    @Override
                    public void run() {
                        int measuredHeight = headView.getMeasuredHeight();
                        ViewGroup.LayoutParams layoutParams = nei_empty.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = DeviceInfoUtil.getDeviceHeight(context)
                                - measuredHeight - TransformUtil.dip2px(context,44)
                                - ImmersionBar.getStatusBarHeight((Activity) context);
                        nei_empty.setLayoutParams(layoutParams);
                    }
                });
            }
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
