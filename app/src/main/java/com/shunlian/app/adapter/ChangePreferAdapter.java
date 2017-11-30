package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/28.
 */

public class ChangePreferAdapter extends BaseRecyclerAdapter<GoodsDeatilEntity.AllProm> {
    private List<GoodsDeatilEntity.AllProm> allPromList;
    private OnItemPreferSelectListener mListener;
    private Context mContext;

    public ChangePreferAdapter(Context context, boolean isShowFooter, List<GoodsDeatilEntity.AllProm> lists) {
        super(context, isShowFooter, lists);
        this.allPromList = lists;
        this.mContext = context;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        ChangeViewHolder viewHolder = new ChangeViewHolder(LayoutInflater.from(context).inflate(R.layout.item_changeprefer, parent, false));
        return viewHolder;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        ChangeViewHolder changeViewHolder = (ChangeViewHolder) holder;

        final GoodsDeatilEntity.AllProm allProm = allPromList.get(position);
        changeViewHolder.tv_prefer.setText(allProm.prom_title);

        if (allProm.isSelect) {
            changeViewHolder.miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_h));
        } else {
            changeViewHolder.miv_prefer_select.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.img_shoppingcar_selected_n));
        }

        changeViewHolder.miv_prefer_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(allProm.prom_id) && !allProm.isSelect) {
                    selectPrefer(allProm.prom_id);
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.OnPreferSelect(allProm);
                    }
                }
            }
        });
    }

    public void selectPrefer(String promId) {
        if (allPromList != null && allPromList.size() != 0) {
            for (int i = 0; i < allPromList.size(); i++) {
                if (allPromList.get(i).prom_id.equals(promId)) {
                    allPromList.get(i).isSelect = true;
                } else {
                    allPromList.get(i).isSelect = false;
                }
            }
        }
    }

    public class ChangeViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_prefer_select)
        MyImageView miv_prefer_select;

        @BindView(R.id.tv_prefer)
        TextView tv_prefer;

        public ChangeViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setOnItemPreferSelectListener(OnItemPreferSelectListener listener) {
        this.mListener = listener;
    }

    public interface OnItemPreferSelectListener {
        void OnPreferSelect(GoodsDeatilEntity.AllProm prom);
    }
}
