package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

/**
 * Created by zhang on 2017/3/29 15 : 05.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter {

    private static final int ITEM_LIST = 0;
    private static final int ITEM_FOOTER = 1;
    public static List<Unbinder> unbinders = new ArrayList<>();
    protected Context context;
    protected final List<T> lists;
    private final boolean isShowFooter;//是否显示脚布局
    protected OnItemClickListener listener;
    protected BaseFooterHolder baseFooterHolder;
    private int currentPage;//分页时当前显示页数
    private int allPage;//总页
    private OnReloadListener reloadListener;
    private boolean isLoadFailure;
    public final LayoutInflater mInflater;

    public BaseRecyclerAdapter(Context context, boolean isShowFooter, List<T> lists) {
        if (lists == null){
           lists = new ArrayList<>();
        }
        this.context = context;
        this.lists = lists;
        this.isShowFooter = isShowFooter;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager&&isShowFooter) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isBottoms(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    protected boolean isBottoms(int position) {
        return position + 1 == getItemCount();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isShowFooter) {
            switch (viewType) {
                case ITEM_LIST:
                    return getRecyclerHolder(parent);
                case ITEM_FOOTER:
                    View footerView = LayoutInflater.from(context).inflate(R.layout.layout_competetion_listviw_footer, parent, false);
                    BaseFooterHolder baseFooterHolder = new BaseFooterHolder(footerView);
                    setFooterHolderParams(baseFooterHolder);
                    return baseFooterHolder;
            }
            return null;
        } else {
            return getRecyclerHolder(parent);
        }
    }

    /**
     * 设置baseFooterHolder  layoutparams
     *
     * @param baseFooterHolder
     */
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        this.baseFooterHolder = baseFooterHolder;
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    protected abstract RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent);

    @Override
    public int getItemViewType(int position) {
        if (isShowFooter) {
            if (position + 1 == getItemCount()) {
                return ITEM_FOOTER;
            } else {
                return ITEM_LIST;
            }
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isShowFooter) {
            int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case ITEM_LIST:
                    handleList(holder, position);
                    break;
                case ITEM_FOOTER:
                    BaseFooterHolder footerHolder = (BaseFooterHolder) holder;
                    handleFooter(footerHolder, position);
                    break;
                default:
                    handleList(holder, position);
                    break;
            }
        } else {
            handleList(holder, position);
        }
    }

    /**
     * 设置页数
     *
     * @param currentPage 当前页
     * @param allpage     总页
     */
    public void setPageLoading(int currentPage, int allpage) {
        this.currentPage = currentPage;
        this.allPage = allpage;
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    public abstract void handleList(RecyclerView.ViewHolder holder, int position);

    /**
     * 处理脚布局
     *
     * @param holder
     * @param position
     */
    public void handleFooter(BaseFooterHolder holder, int position) {
        if (isLoadFailure) {
            againLoading();
            isLoadFailure = false;
        }

        if (allPage > currentPage) {
            holder.setNormalVisibility(View.VISIBLE);
            holder.layout_no_more.setVisibility(View.GONE);
        } else {
            holder.setNormalVisibility(View.GONE);
            holder.layout_no_more.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (isShowFooter) {
            return lists.size() + 1;
        } else {
            return lists.size();
        }
    }

    /**
     * 加载失败
     */
    public void loadFailure() {
        isLoadFailure = true;
        if (baseFooterHolder != null) {
            baseFooterHolder.layout_load_error.setVisibility(View.VISIBLE);
            baseFooterHolder.layout_no_more.setVisibility(View.GONE);
            baseFooterHolder.setNormalVisibility(View.GONE);
        }
    }

    public void againLoading() {
        if (baseFooterHolder != null) {
            baseFooterHolder.layout_load_error.setVisibility(View.GONE);
            baseFooterHolder.layout_no_more.setVisibility(View.GONE);
            baseFooterHolder.setNormalVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置条目点击
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置从新加载监听
     *
     * @param reloadListener
     */
    public void setOnReloadListener(OnReloadListener reloadListener) {
        this.reloadListener = reloadListener;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
//        unbind();
    }

    public void unbind() {
        if (unbinders != null && unbinders.size() > 0) {
            for (Unbinder bind : unbinders) {
                if (bind != null) {
                    bind.unbind();
                }
            }
            unbinders.clear();
        }
    }

    protected int getColor(@ColorRes int id) {
        if (context == null){
            context = Common.getApplicationContext();
        }
        return context.getResources().getColor(id);
    }

    protected String getString(@StringRes int id) {
        if (context == null){
            context = Common.getApplicationContext();
        }
        return context.getResources().getString(id);
    }

    protected boolean isEmpty(CharSequence character){
        return TextUtils.isEmpty(character);
    }

    protected boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }
    protected Drawable getDrawable(@DrawableRes int id){
        if (context == null){
            context = Common.getApplicationContext();
        }
        return context.getResources().getDrawable(id);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnReloadListener {
        /**
         * 重新加载
         */
        void onReload();
    }

    public class BaseFooterHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout layout_normal;
        public final MyTextView layout_load_error;
        public final MyTextView layout_no_more;
        public final MyTextView mtv_loading;
        public final ProgressView spinKitView;
//        public final Circle circle;

        public BaseFooterHolder(View itemView) {
            super(itemView);
            spinKitView = (ProgressView) itemView.findViewById(R.id.progressBar);
//            circle = new Circle();
//            spinKitView.setIndeterminateDrawable(circle);
            layout_normal = (RelativeLayout) itemView.findViewById(R.id.layout_normal);
            layout_load_error = (MyTextView) itemView.findViewById(R.id.layout_load_error);
            layout_load_error.setVisibility(View.GONE);
            layout_no_more = (MyTextView) itemView.findViewById(R.id.layout_no_more);
            mtv_loading = (MyTextView) itemView.findViewById(R.id.mtv_loading);

            layout_load_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reloadListener != null) {
                        reloadListener.onReload();
                        isLoadFailure = false;
                        againLoading();
                    }
                }
            });
        }

        public void setNormalVisibility(int visibility) {
            if (visibility == View.GONE) {
                layout_normal.setVisibility(View.GONE);
//                spinKitView.unscheduleDrawable(circle);
                spinKitView.releaseAnimation();
            } else if (visibility == View.VISIBLE) {
                layout_normal.setVisibility(View.VISIBLE);
//                spinKitView.onWindowFocusChanged(true);
                spinKitView.startAnimation();
            }
        }
    }

    public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public BaseRecyclerViewHolder(View itemView) {
            super(itemView);
            Unbinder bind = ButterKnife.bind(this, itemView);
            unbinders.add(bind);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    /**
     * 显示view
     *
     * @param views
     */
    protected void visible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * 隐藏view
     *
     * @param views
     */
    protected void gone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }
}
