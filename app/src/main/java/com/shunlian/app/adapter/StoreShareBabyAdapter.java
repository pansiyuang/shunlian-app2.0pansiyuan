package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

/**
 * Created by augus on 2017/11/13 0013.
 */

public class StoreShareBabyAdapter extends BaseRecyclerAdapter<StoreGoodsListEntity.MData> {
    private Context context;
    private List<StoreGoodsListEntity.MData> datas;

    private int successCount = 0;
    private boolean isCircle = false;
    private LoadImageCount cloadImageCount;

    public StoreShareBabyAdapter(Context context, boolean isShowFooter, List<StoreGoodsListEntity.MData> datas,boolean isCircle,LoadImageCount loadImageCount) {
        super(context, isShowFooter, datas);
        this.context = context;
        this.datas = datas;
        this.isCircle = isCircle;
        successCount = 0;
        this.cloadImageCount = loadImageCount;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new OneHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_baby, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OneHolder) {
            OneHolder oneHolder = (OneHolder) holder;
            StoreGoodsListEntity.MData data=datas.get(position);
            oneHolder.mtv_descl.setText(data.title);
            oneHolder.mtv_pricel.setText(data.price);
            oneHolder.mtv_pricer.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线 市场价
            oneHolder.miv_onel.setScaleType(ImageView.ScaleType.FIT_START);
            oneHolder.mtv_pricer.setText(context.getResources().getString(R.string.common_yuan)+data.market_price);
            if(!isCircle){
                GlideUtils.getInstance().loadImage(context,oneHolder.miv_onel,data.thumb);
            }else {
                GlideUtils.getInstance().loadBitmapSync(context, data.thumb,
                        new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource,
                                                        GlideAnimation<? super Bitmap> glideAnimation) {
                                oneHolder.miv_onel.setImageBitmap(resource);
                                successCount++;
                                if(cloadImageCount!=null){
                                    cloadImageCount.imageSuccessCount(successCount);
                                }
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                            }
                        });
            }
        }
    }

    class OneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyTextView mtv_descl,mtv_pricel,mtv_pricer;
        private MyImageView miv_onel;


        OneHolder(View itemView) {
            super(itemView);
            miv_onel = (MyImageView) itemView.findViewById(R.id.miv_onel);
            int picWidth = Common.getScreenWidth((Activity) context)- TransformUtil.dip2px(context,5);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(picWidth /2, picWidth /3);
            miv_onel.setLayoutParams(params);
            mtv_descl = (MyTextView) itemView.findViewById(R.id.mtv_descl);
            mtv_pricel = (MyTextView) itemView.findViewById(R.id.mtv_pricel);
            mtv_pricer = (MyTextView) itemView.findViewById(R.id.mtv_pricer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null){
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public interface LoadImageCount{
       void imageSuccessCount(int successCount);
    }

}
