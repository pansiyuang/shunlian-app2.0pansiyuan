package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/21.
 */

public class CommentAdapter extends BaseRecyclerAdapter<String> {

    public static final int HEAD = 2;

    public CommentAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View head = LayoutInflater.from(context)
                        .inflate(R.layout.head_comment, parent, false);
                return new HeadHolder(head);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case HEAD:
                handlerHead(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handlerHead(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder){
            final HeadHolder mHolder = (HeadHolder) holder;
            String[] item = {"全部(8)","追加(100)","有图评价(1920)","好评(2582)","中评(200)","差评(520)"};
            final TagAdapter tagAdapter = new TagAdapter<String>(item) {

                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    MyTextView textView = (MyTextView) LayoutInflater.from(context)
                            .inflate(R.layout.comment_class, mHolder.gv_section, false);
                    GradientDrawable background = (GradientDrawable) textView.getBackground();
                    textView.setText(s);
                    if (position == selectId){
                        textView.setTextColor(getColor(R.color.white));
                        background.setColor(getColor(R.color.pink_color));
                    }else {
                        textView.setTextColor(getColor(R.color.new_text));
                        background.setColor(getColor(R.color.value_FEF0F3));
                    }
                    return textView;
                }
            };
            mHolder.gv_section.setAdapter(tagAdapter);
            mHolder.gv_section.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    selectId = position;
                    tagAdapter.notifyDataChanged();
                    return true;
                }
            });
        }
    }
    private int selectId = 0;
    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return HEAD;
        }else {
            return super.getItemViewType(position);
        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {

    }

    public class CommentHolder extends BaseRecyclerViewHolder{

        public CommentHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeadHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.gv_section)
        TagFlowLayout gv_section;
        public HeadHolder(View itemView) {
            super(itemView);
        }
    }
}
