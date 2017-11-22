package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.bean.StoreNewGoodsListEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreEvaluateAdapter extends BaseRecyclerAdapter<StoreIntroduceEntity.Evaluate.Pj> {
    private Context context;
    private List<StoreIntroduceEntity.Evaluate.Pj> pjs;

    public StoreEvaluateAdapter(Context context, boolean isShowFooter, List<StoreIntroduceEntity.Evaluate.Pj> pjs) {
        super(context, isShowFooter, pjs);
        this.context = context;
        this.pjs = pjs;
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_evaluate, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            StoreIntroduceEntity.Evaluate.Pj data=pjs.get(position);
            oneHolder.mtv_evaluate.setText(data.name);
            oneHolder.mtv_pinjiafen.setText(data.score);
            oneHolder.mtv_pingjia.setText(data.explain);
        }
    }

    class OneHolder extends RecyclerView.ViewHolder {
        private MyTextView mtv_evaluate,mtv_pinjiafen,mtv_pingjia;

        OneHolder(View itemView) {
            super(itemView);
            mtv_evaluate = (MyTextView) itemView.findViewById(R.id.mtv_evaluate);
            mtv_pinjiafen = (MyTextView) itemView.findViewById(R.id.mtv_pinjiafen);
            mtv_pingjia = (MyTextView) itemView.findViewById(R.id.mtv_pingjia);
        }
    }

}
