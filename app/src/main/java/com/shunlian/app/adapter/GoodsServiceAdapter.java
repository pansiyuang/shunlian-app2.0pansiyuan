package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/12/11.
 */

public class GoodsServiceAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.SimpTitle> {


    public GoodsServiceAdapter(Context context,List<GoodsDeatilEntity.SimpTitle> lists) {
        super(context, false, lists);
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.adapter_service, parent,false);
        return new GoodsServiceHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof GoodsServiceHolder){
            GoodsServiceHolder mHolder = (GoodsServiceHolder) holder;
            GoodsDeatilEntity.SimpTitle simpTitle = lists.get(position);
            mHolder.mtv_title.setText(simpTitle.title);
            mHolder.mtv_desc.setText(simpTitle.content);
        }
    }


    public class GoodsServiceHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.mtv_title)
        MyTextView mtv_title;

        @BindView(R.id.mtv_desc)
        MyTextView mtv_desc;

        public GoodsServiceHolder(View itemView) {
            super(itemView);
        }
    }
}
