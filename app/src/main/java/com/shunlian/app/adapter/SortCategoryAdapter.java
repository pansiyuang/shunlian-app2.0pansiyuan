package com.shunlian.app.adapter;

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

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.SortFragEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by zhang on 2017/7/13 09 : 56.
 */

public class SortCategoryAdapter extends BaseRecyclerAdapter<SortFragEntity.ItemList> {


    private SortFragEntity.Toplist mToplist;
    public List<Integer> counts = new ArrayList<>();//二级分类显示条目位置
    private int titleCount = 1;
    //二级分类显示条目位置对应的k:V
    public Map<Integer,SortFragEntity.SubList> titleData = new HashMap<>();
    private final LayoutInflater mInflater;
    private ViewGroup parent;

    public SortCategoryAdapter(Context context, List<SortFragEntity.ItemList> children,
                               SortFragEntity.Toplist toplist) {
        super(context, false, children);
        mInflater = LayoutInflater.from(context);
        filterData(toplist,false);
    }

    public void filterData(SortFragEntity.Toplist toplist, boolean b) {
        counts.clear();
        titleData.clear();
        titleCount = 1;
        mToplist = toplist;
        counts.add(0);
        List<SortFragEntity.SubList> subLists = mToplist.children;//二级分类
        if (!isEmpty(subLists)) {//统计title位置
            titleData.put(counts.get(counts.size() - 1), subLists.get(0));
            for (int i = 0; i < subLists.size(); i++) {//遍历二级分类
                SortFragEntity.SubList subList = subLists.get(i);
                if (i + 1 != subLists.size()) {//非最后一个列表
                    //counts最后一次了的位置加上 上一个二级类目下的数量再加1 = 当前二级类目的位置
                    int count = 0;
                    if (!isEmpty(subList.children))
                        count = subList.children.size();

                    counts.add(counts.get(counts.size() - 1) + count + 1);
                    titleData.put(counts.get(counts.size() - 1), subLists.get(i + 1));
                }
            }
        }
        if (b)notifyDataSetChanged();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) layoutManager;
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isHeader(position) ? manager.getSpanCount() : 1;
                }
            });
        }
    }

    private boolean isHeader(int position) {
        if (counts.contains(position)) {
            return true;
        }
        return false;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return super.onCreateViewHolder(parent, viewType);
            case 2:
                View inflate = mInflater.inflate(R.layout.sort_sub_title, parent, false);
                return new TitleHolder(inflate);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        if (counts.contains(position)) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case 1:
                super.onBindViewHolder(holder, position);
                break;
            case 2:
                handleTitle(holder, position);
                break;
        }

    }


    private void handleTitle(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            TitleHolder mHolder = (TitleHolder) holder;
            if (counts.contains(position)) {//有title
                SortFragEntity.SubList subList = titleData.get(position);
                if (subList == null) {
                    return;
                }
                if (isEmpty(subList.name)){
                    gone(mHolder.sort_tv_title,mHolder.view_line1,mHolder.view_line2);
                }else {
                    visible(mHolder.sort_tv_title,mHolder.view_line1,mHolder.view_line2);
                    mHolder.sort_tv_title.setText(subList.name);
                }

                String on_ranking = subList.on_ranking;
                if ("1".equalsIgnoreCase(on_ranking)) {
                    visible(mHolder.mtv_ranking);
                } else {
                    gone(mHolder.mtv_ranking);
                }
            } else {
                mHolder.sort_tv_title.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 子类需要实现的holder
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        this.parent = parent;
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_sort, parent, false);
        return new SortCategoryHolder(view);
    }


    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof SortCategoryHolder) {
            SortCategoryHolder mHolder = (SortCategoryHolder) holder;
            titleCount = computeCount(position);
            int index = position - titleCount;
            if (index < lists.size()) {
                String name = lists.get(index).name;
                if (!TextUtils.isEmpty(name)) {
                    visible(mHolder.tv_name,mHolder.iv_thumb);
                    if (mHolder.tv_name == null || mHolder.iv_thumb == null)mHolder.againBind();
                    GlideUtils.getInstance().loadOverrideImage(context,
                            mHolder.iv_thumb,lists.get(index).thumb,115,115);
                    mHolder.tv_name.setText(name);
                } else {
                    gone(mHolder.tv_name,mHolder.iv_thumb);
                }
            }
        }

    }

    public int computeCount(int position) {
        int count = 0;
        for (int i = 0; i < counts.size(); i++) {
            Integer integer = counts.get(i);
            if (integer < position) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }



    @Override
    public int getItemCount() {
        return lists.size() + counts.size();
    }


    public class SortCategoryHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_thumb)
        MyImageView iv_thumb;
        @BindView(R.id.tv_name)
        TextView tv_name;

        public SortCategoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public class TitleHolder extends BaseRecyclerViewHolder implements View.OnClickListener {

        @BindView(R.id.sort_tv_title)
        TextView sort_tv_title;

        @BindView(R.id.mtv_ranking)
        MyTextView mtv_ranking;

        @BindView(R.id.view_line1)
        View view_line1;

        @BindView(R.id.view_line2)
        View view_line2;

        public TitleHolder(View itemView) {
            super(itemView);
            int i = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(mtv_ranking,i,i,i,i);
            mtv_ranking.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public void detachView() {
        if (counts != null){
            counts.clear();
            counts = null;
        }
        if (titleData != null){
            titleData.clear();
            titleData = null;
        }
        unbind();
    }
}
