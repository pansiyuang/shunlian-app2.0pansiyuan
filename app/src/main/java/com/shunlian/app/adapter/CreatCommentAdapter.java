package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static com.shunlian.app.ui.my_comment.CreatCommentActivity.APPEND_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CHANGE_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CREAT_COMMENT;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentAdapter extends BaseRecyclerAdapter<ReleaseCommentEntity> {

    private static final int FOOTER_COMMENT = 3;
    private int commentType;
    private OnCommentChangeCallBack mCallBack;
    private HashMap<Integer, SingleImgAdapter> mAdapters;
    private int parentIndex;

    public void addImages(List<ImageEntity> pathes, int position) {
        if (mAdapters.size() != 0) {
            SingleImgAdapter imgAdapter = mAdapters.get(position);
            parentIndex = position;
            imgAdapter.addImages(pathes);
        }
    }

//    public void updateProgress(int p1, String tag, int progress) {
//        if (mAdapters.size() != 0) {
//            SingleImgAdapter imgAdapter = mAdapters.get(p1);
//            imgAdapter.updateItemProgress(tag, progress);
//        }
//    }

    public CreatCommentAdapter(Context context, boolean isShowFooter, List<ReleaseCommentEntity> lists, int type) {
        super(context, isShowFooter, lists);
        this.commentType = type;
        mAdapters = new HashMap<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER_COMMENT:
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
                return FOOTER_COMMENT;
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
            case FOOTER_COMMENT:
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
    public void handleList(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CommentViewHolder) {
            final CommentViewHolder viewHolder = (CommentViewHolder) holder;
            final ReleaseCommentEntity data = lists.get(position);
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_comment_icon, data.pic);
            viewHolder.tv_comment_title.setText(data.title);
            viewHolder.tv_comment_price.setText(getString(R.string.common_yuan) + data.price);

            SingleImgAdapter singleImgAdapter;

            if (mAdapters.size() < lists.size()) {
                List<ImageEntity> list = new ArrayList<>();
                singleImgAdapter = new SingleImgAdapter(context, false, list, position);
                viewHolder.recycler_comment.setAdapter(singleImgAdapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 5);
                viewHolder.recycler_comment.setLayoutManager(gridLayoutManager);
                viewHolder.recycler_comment.setNestedScrollingEnabled(false);
                viewHolder.recycler_comment.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context, 4), false));
                mAdapters.put(position, singleImgAdapter);
            } else {
                if (data.imgs == null) {
                    ((SingleImgAdapter) viewHolder.recycler_comment.getAdapter()).setData(new ArrayList<ImageEntity>());
                } else {
                    ((SingleImgAdapter) viewHolder.recycler_comment.getAdapter()).setData(data.imgs);
                }
            }

            if (commentType == APPEND_COMMENT || commentType == CHANGE_COMMENT) {
                viewHolder.ll_comment_score.setVisibility(View.GONE);
            } else {
                viewHolder.ll_comment_score.setVisibility(View.VISIBLE);
            }

            viewHolder.recycler_comment.setTag(position);
            if (viewHolder.edt_comment.getTag() instanceof TextWatcher) {
                viewHolder.edt_comment.removeTextChangedListener((TextWatcher) viewHolder.edt_comment.getTag());
            }
            viewHolder.edt_comment.setText(data.content);
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    data.content = viewHolder.edt_comment.getText().toString();
                    if (mCallBack != null) {
                        mCallBack.OnComment(viewHolder.edt_comment.getText().toString(), position);
                    }
                }
            };
            viewHolder.edt_comment.addTextChangedListener(textWatcher);
            viewHolder.edt_comment.setTag(textWatcher);

            if (isEmpty(data.starLevel)) {
                data.starLevel = "10";
            }

            changeStatus(viewHolder, data.starLevel, position);

            viewHolder.ll_comment_high.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatus(viewHolder, "10", position);
                }
            });

            viewHolder.ll_comment_middle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatus(viewHolder, "6", position);
                }
            });

            viewHolder.ll_comment_low.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeStatus(viewHolder, "2", position);
                }
            });
        }
    }

    private void changeStatus(CommentViewHolder viewHolder, String type, int position) {
        viewHolder.miv_comment_high.setImageDrawable(getDrawable(R.mipmap.icon_haoping_n));
        viewHolder.miv_comment_middle.setImageDrawable(getDrawable(R.mipmap.icon_zhongping_n));
        viewHolder.miv_comment_low.setImageDrawable(getDrawable(R.mipmap.icon_chaping_n));
        switch (type) {
            case "2":
                viewHolder.miv_comment_low.setImageDrawable(getDrawable(R.mipmap.icon_chaping_h));
                break;
            case "6":
                viewHolder.miv_comment_middle.setImageDrawable(getDrawable(R.mipmap.icon_zhongping_h));
                break;
            case "10":
                viewHolder.miv_comment_high.setImageDrawable(getDrawable(R.mipmap.icon_haoping_h));
                break;
        }
        if (mCallBack != null) {
            lists.get(position).starLevel = String.valueOf(type);
            mCallBack.OnCommentLevel(String.valueOf(type), position);
        }
    }


    public void handleFoot(RecyclerView.ViewHolder holder) {
        if (holder instanceof FootViewHolder) {
            FootViewHolder viewHolder = (FootViewHolder) holder;
            viewHolder.ratingBar_logistics.setOnRatingBarChangeListener(new FiveStarBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(FiveStarBar simpleRatingBar, float rating, boolean fromUser) {
                    if (mCallBack != null) {
                        mCallBack.OnLogisticsStar(rating);
                    }
                }
            });
            viewHolder.ratingBar_attitude.setOnRatingBarChangeListener(new FiveStarBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(FiveStarBar simpleRatingBar, float rating, boolean fromUser) {
                    if (mCallBack != null) {
                        mCallBack.OnAttitudeStar(rating);
                    }
                }
            });
            viewHolder.ratingBar_consistent.setOnRatingBarChangeListener(new FiveStarBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(FiveStarBar simpleRatingBar, float rating, boolean fromUser) {
                    if (mCallBack != null) {
                        mCallBack.OnConsistent(rating);
                    }
                }
            });
        }
    }

    public class CommentViewHolder extends BaseRecyclerViewHolder implements TextWatcher {
        @BindView(R.id.miv_comment_icon)
        MyImageView miv_comment_icon;

        @BindView(R.id.tv_comment_title)
        TextView tv_comment_title;

        @BindView(R.id.tv_comment_price)
        TextView tv_comment_price;

        @BindView(R.id.ll_comment_high)
        LinearLayout ll_comment_high;

        @BindView(R.id.ll_comment_middle)
        LinearLayout ll_comment_middle;

        @BindView(R.id.ll_comment_low)
        LinearLayout ll_comment_low;

        @BindView(R.id.miv_comment_high)
        MyImageView miv_comment_high;

        @BindView(R.id.miv_comment_middle)
        MyImageView miv_comment_middle;

        @BindView(R.id.miv_comment_low)
        MyImageView miv_comment_low;

        @BindView(R.id.recycler_comment)
        RecyclerView recycler_comment;

        @BindView(R.id.edt_comment)
        EditText edt_comment;

        @BindView(R.id.ll_comment_score)
        LinearLayout ll_comment_score;

        public CommentViewHolder(View itemView) {
            super(itemView);

            edt_comment.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

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

    public void setOnCommentChangeCallBack(OnCommentChangeCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnCommentChangeCallBack {
        void OnComment(String content, int position);

        void OnCommentLevel(String level, int position);

        void OnLogisticsStar(float logistics);

        void OnAttitudeStar(float attitude);

        void OnConsistent(float consistent);
    }
}
