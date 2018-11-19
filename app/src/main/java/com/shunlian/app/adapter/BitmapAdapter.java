package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

/**
 * Created by zhanghe on 2018/10/27.
 */

public class BitmapAdapter extends BaseRecyclerAdapter<Bitmap> {


    public BitmapAdapter(Context context,List<Bitmap> lists) {
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
        MyImageView imageView = new MyImageView(context);
        return new BitmapHolder(imageView);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        BitmapHolder mHolder = (BitmapHolder) holder;
        Bitmap bitmap = lists.get(position);
        mHolder.iv.setImageBitmap(bitmap);
    }

    public class BitmapHolder extends BaseRecyclerViewHolder{

        private final MyImageView iv;

        public BitmapHolder(View itemView) {
            super(itemView);
            iv = (MyImageView) itemView;
            int w = TransformUtil.dip2px(context, 50);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams
                    (w, ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
        }
    }
}
