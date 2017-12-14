package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.ui.my_comment.CreatCommentActivity.APPEND_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CHANGE_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CREAT_COMMENT;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentAdapter extends BaseRecyclerAdapter<ReleaseCommentEntity> {

    private static final int FOOTER = 2;
    private List<String> imgList;
    private int commentType;
    private OnCommentContentCallBack mCallBack;

    public CreatCommentAdapter(Context context, boolean isShowFooter, List<ReleaseCommentEntity> lists, int type) {
        super(context, isShowFooter, lists);
        imgList = new ArrayList<>();
        this.commentType = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                View inflate = LayoutInflater.from(context).inflate(R.layout.foot_creat_comment, parent, false);
                return new FootViewHolder(inflate);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (commentType == CREAT_COMMENT) {
            if (position + 1 == getItemCount())
                return FOOTER;
            else
                return super.getItemViewType(position);
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        if (commentType == CREAT_COMMENT) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FOOTER:
                handleFoot(holder);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_creat_comment, parent, false);
        return new CommentViewHolder(inflate);
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentViewHolder) {
            CommentViewHolder viewHolder = (CommentViewHolder) holder;
            ReleaseCommentEntity data = lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_comment_icon, data.pic);
            viewHolder.tv_comment_title.setText(data.title);
            viewHolder.tv_comment_price.setText(getString(R.string.common_yuan) + data.price);

            SingleImgAdapter singleImgAdapter = new SingleImgAdapter(context, false, imgList);
            viewHolder.recycler_comment.setAdapter(singleImgAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            viewHolder.recycler_comment.setLayoutManager(linearLayoutManager);
            viewHolder.recycler_comment.setNestedScrollingEnabled(false);
            viewHolder.recycler_comment.addItemDecoration(new HorItemDecoration(TransformUtil.dip2px(context, 4), 0, 0));

            if (commentType == APPEND_COMMENT) {
                viewHolder.ll_comment_score.setVisibility(View.GONE);
            } else {
                viewHolder.ll_comment_score.setVisibility(View.VISIBLE);
            }

            if (position == 0) {
                if (commentType == APPEND_COMMENT) {
                    viewHolder.edt_comment.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (mCallBack != null) {
                                mCallBack.OnComment(s.toString());
                            }
                        }
                    });
                }
            }
        }
    }

    public void handleFoot(RecyclerView.ViewHolder holder) {
        if (holder instanceof FootViewHolder) {
        }
    }

    public class CommentViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_comment_icon)
        MyImageView miv_comment_icon;

        @BindView(R.id.tv_comment_title)
        TextView tv_comment_title;

        @BindView(R.id.tv_comment_price)
        TextView tv_comment_price;

        @BindView(R.id.tv_comment_high)
        TextView tv_comment_high;

        @BindView(R.id.tv_comment_middle)
        TextView tv_comment_middle;

        @BindView(R.id.tv_comment_low)
        TextView tv_comment_low;

        @BindView(R.id.recycler_comment)
        RecyclerView recycler_comment;

        @BindView(R.id.edt_comment)
        EditText edt_comment;

        @BindView(R.id.ll_comment_score)
        LinearLayout ll_comment_score;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FootViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.ratingBar_logistics)
        FiveStarBar ratingBar_logistics;

        @BindView(R.id.ratingBar_attitude)
        FiveStarBar ratingBar_attitude;

        @BindView(R.id.ratingBar_consistent)
        FiveStarBar ratingBar_consistent;

        public FootViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnCommentContentCallBack(OnCommentContentCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnCommentContentCallBack {
        void OnComment(String content);
    }
}
