package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/16.
 */

public class TieziAvarAdapter extends BaseRecyclerAdapter<String> {
    private int picWidth;

    public TieziAvarAdapter(Context context, boolean isShowFooter, List<String> lists) {
        super(context, isShowFooter, lists);
    }

    public TieziAvarAdapter(Context context, List<String> lists, int width) {
        super(context, false, lists);
        this.picWidth = width;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        MivHolder viewHolder = new MivHolder(LayoutInflater.from(context).inflate(R.layout.item_tiezi_avar, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        MivHolder mivHolder = (MivHolder) holder;
        GlideUtils.getInstance().loadCircleAvar(context, mivHolder.miv_avar, lists.get(position));
        if (picWidth != 0) {
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(picWidth, picWidth);
            mivHolder.miv_avar.setLayoutParams(layoutParams);
        }
    }

    public class MivHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_avar)
        MyImageView miv_avar;


        public MivHolder(View itemView) {
            super(itemView);
        }
    }
}
