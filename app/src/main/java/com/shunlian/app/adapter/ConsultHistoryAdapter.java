package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.ConsultHistoryEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ConsultHistoryAdapter extends BaseRecyclerAdapter<ConsultHistoryEntity.HistoryList> {

    public ConsultHistoryAdapter(Context context, boolean isShowFooter,
                                 List<ConsultHistoryEntity.HistoryList> lists) {
        super(context, isShowFooter, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consult_history, parent, false);
        ConsultHistoryHolder holder = new ConsultHistoryHolder(view);
        return holder;
    }

    /**
     * 处理列表
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConsultHistoryHolder){
            ConsultHistoryHolder mHolder = (ConsultHistoryHolder) holder;
            GradientDrawable background = (GradientDrawable) mHolder.lLayout_detail.getBackground();
            background.setColor(getColor(R.color.value_f3));

            ConsultHistoryEntity.HistoryList historyList = lists.get(position);

            GlideUtils.getInstance().loadImage(context,mHolder.civ_head,historyList.user_thumb);
            mHolder.mtv_name.setText(historyList.username);
            mHolder.mtv_label.setText(historyList.progress);
            mHolder.mtv_time.setText(historyList.time);

            List<ConsultHistoryEntity.Content> content = historyList.content;
            if (isEmpty(content)){
                mHolder.lLayout_text_detail.setVisibility(View.GONE);
            }else {
                mHolder.lLayout_text_detail.setVisibility(View.VISIBLE);
                for (int i = 0; i < content.size(); i++) {
                    ConsultHistoryEntity.Content content1 = content.get(i);
                    MyTextView textView = new MyTextView(context);
                    String source = content1.label.concat(content1.title);
                    SpannableStringBuilder spannableStringBuilder = Common.
                            changeColor(source, content1.label, getColor(R.color.new_text));
                    textView.setText(spannableStringBuilder);
                    textView.setTextColor(getColor(R.color.new_gray));
                    textView.setTextSize(14);
                    mHolder.lLayout_text_detail.addView(textView);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    params.topMargin = TransformUtil.dip2px(context,10f);
                    textView.setLayoutParams(params);
                }
            }

            if (isEmpty(historyList.images)){
                mHolder.mtv_proof.setVisibility(View.GONE);
                mHolder.recy_view.setVisibility(View.GONE);
            }else {
                mHolder.mtv_proof.setVisibility(View.VISIBLE);
                mHolder.recy_view.setVisibility(View.VISIBLE);
                SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter<String>
                        (context,R.layout.item_detail,historyList.images) {

                    @Override
                    public void convert(SimpleViewHolder holder, String s, int position) {
                        MyImageView imageView = holder.getView(R.id.miv_pic);
                        imageView.setWHProportion(160,160);
                        GlideUtils.getInstance().loadImage(context,imageView,s);
                    }
                };
                mHolder.recy_view.setAdapter(adapter);
            }


            if (position == 0){
                mHolder.view_logistics_line1.setVisibility(View.INVISIBLE);
                mHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_active);
            }else {
                mHolder.view_logistics_line1.setVisibility(View.VISIBLE);
                mHolder.miv_logistics.setImageResource(R.mipmap.img_wuliu_before);
            }
        }
    }

    public class ConsultHistoryHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.view_logistics_line1)
        View view_logistics_line1;

        @BindView(R.id.miv_logistics)
        MyImageView miv_logistics;

        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_name)
        MyTextView mtv_name;

        @BindView(R.id.mtv_label)
        MyTextView mtv_label;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.lLayout_detail)
        LinearLayout lLayout_detail;

        @BindView(R.id.lLayout_text_detail)
        LinearLayout lLayout_text_detail;

        @BindView(R.id.mtv_proof)
        MyTextView mtv_proof;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;
        public ConsultHistoryHolder(View itemView) {
            super(itemView);
            recy_view.setNestedScrollingEnabled(false);
            GridLayoutManager manager = new GridLayoutManager(context,3);
            recy_view.setLayoutManager(manager);
        }
    }
}
