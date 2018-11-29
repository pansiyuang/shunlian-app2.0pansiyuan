package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.NoticeMsgEntity;
import com.shunlian.app.utils.HighLightKeyWordUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/25.
 */

public class NoticeMsgAdapter extends BaseRecyclerAdapter<NoticeMsgEntity.Notice> {
    public NoticeMsgAdapter(Context context, List<NoticeMsgEntity.Notice> lists) {
        super(context, true, lists);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new NoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notice_msg, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NoticeViewHolder) {
            NoticeMsgEntity.Notice notice = lists.get(position);
            NoticeViewHolder noticeViewHolder = (NoticeViewHolder) holder;

            if (notice.opt == 1) {//已通过
                noticeViewHolder.tv_content.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_00B97A), notice.content, "审核已通过"));
            } else {
                noticeViewHolder.tv_content.setText(HighLightKeyWordUtil.getHighLightKeyWord(context.getResources().getColor(R.color.value_E70101), notice.content, "审核未通过"));
            }
        }
    }

    public class NoticeViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.tv_content)
        TextView tv_content;

        public NoticeViewHolder(View itemView) {
            super(itemView);
        }
    }
}
