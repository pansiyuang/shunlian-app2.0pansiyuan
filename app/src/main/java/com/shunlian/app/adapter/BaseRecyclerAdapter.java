package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.load.Circle;
import com.shunlian.app.utils.load.SpinKitView;

import java.util.List;


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

    protected final Context context;
    protected final List<T> lists;
    private static final int ITEM_LIST = 0;
    private static final int ITEM_FOOTER = 1;
    private final boolean isShowFooter;//是否显示脚布局
    private int currentPage;//分页时当前显示页数
    private int allPage;//总页
    protected OnItemClickListener listener;
    private OnReloadListener reloadListener;
    protected BaseFooterHolder baseFooterHolder;
    private boolean isLoadFailure;

    public BaseRecyclerAdapter(Context context, boolean isShowFooter, List<T> lists) {
        this.context = context;
        this.lists = lists;
        this.isShowFooter = isShowFooter;
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
        }else{
            return getRecyclerHolder(parent);
        }
    }

    /**
     * 设置baseFooterHolder  layoutparams
     * @param baseFooterHolder
     */
    public void setFooterHolderParams(BaseFooterHolder baseFooterHolder) {
        this.baseFooterHolder = baseFooterHolder;
    }

    /**
     * 子类需要实现的holder
     * @return
     * @param parent
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
        }else {
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
            }
        }else {
            handleList(holder, position);
        }
    }

    /**
     * 设置页数
     * @param currentPage 当前页
     * @param allpage 总页
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
        if (isLoadFailure){
            againLoading();
            isLoadFailure = false;
        }

        if (allPage >= currentPage) {
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
            return lists.size()+1;
        }else{
            return lists.size();
        }
    }

    /**
     * 加载失败
     */
    public void loadFailure() {
        isLoadFailure = true;
        if (baseFooterHolder != null){
            baseFooterHolder.layout_load_error.setVisibility(View.VISIBLE);
            baseFooterHolder.layout_no_more.setVisibility(View.GONE);
            baseFooterHolder.setNormalVisibility(View.GONE);
        }
    }

    public void againLoading(){
        if (baseFooterHolder != null){
            baseFooterHolder.layout_load_error.setVisibility(View.GONE);
            baseFooterHolder.layout_no_more.setVisibility(View.GONE);
            baseFooterHolder.setNormalVisibility(View.VISIBLE);
        }
    }


    public class BaseFooterHolder extends RecyclerView.ViewHolder {
        public final RelativeLayout layout_normal;
        public final TextView layout_load_error;
        public final TextView layout_no_more;
        public final SpinKitView spinKitView;
        public final Circle circle;

        public BaseFooterHolder(View itemView) {
            super(itemView);
            spinKitView = (SpinKitView) itemView.findViewById(R.id.progressBar);
            circle = new Circle();
            spinKitView.setIndeterminateDrawable(circle);
            layout_normal = (RelativeLayout) itemView.findViewById(R.id.layout_normal);
            layout_load_error = (TextView) itemView.findViewById(R.id.layout_load_error);
            layout_load_error.setVisibility(View.GONE);
            layout_no_more = (TextView) itemView.findViewById(R.id.layout_no_more);

            layout_load_error.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reloadListener != null){
                        reloadListener.onReload();
                        isLoadFailure = false;
                        againLoading();
                    }
                }
            });
        }

        public void setNormalVisibility(int visibility){
            if (visibility == View.GONE){
                layout_normal.setVisibility(View.GONE);
                spinKitView.unscheduleDrawable(circle);
            }else if (visibility == View.VISIBLE){
                layout_normal.setVisibility(View.VISIBLE);
                spinKitView.onWindowFocusChanged(true);
            }
        }
    }

    /**
     * 设置条目点击
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    /**
     * 设置从新加载监听
     * @param reloadListener
     */
    public void setOnReloadListener(OnReloadListener reloadListener){
        this.reloadListener = reloadListener;
    }
    public interface OnReloadListener{
        /**
         * 重新加载
         */
        void onReload();
    }
}
