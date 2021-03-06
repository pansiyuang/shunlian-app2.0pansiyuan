package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shunlian.app.R;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/19.
 */

public class LoaderLocalImgVideoAdapter extends BaseRecyclerAdapter<ImageVideo> {


    private OnSelectionListener mSelectionListener;

    public LoaderLocalImgVideoAdapter(Context context, List<ImageVideo> lists) {
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
        View view = mInflater.inflate(R.layout.item_select_imagev2, parent, false);
        return new LoaderLocalImgVideoHolder(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        LoaderLocalImgVideoHolder mHolder = (LoaderLocalImgVideoHolder) holder;
        ImageVideo imageVideo = lists.get(position);
        if (imageVideo.videoDuration != 0) {
            gone(mHolder.checkmark);
            if (isEmpty(imageVideo.coverPath)) {
                mHolder.image.setImageBitmap(imageVideo.coverBitmap);
            }else {
                loadOverrideImage(context,
                        mHolder.image, imageVideo.coverPath, 176, 176,imageVideo);
            }
            visible(mHolder.mtv_video_duration);
            String second = String.valueOf(imageVideo.videoDuration / 1000);
            if (second.length() == 1) {
                second = "0" + second;
            }
            /*LogUtil.zhLogW(String.format("条目位置：%s  文件名:%s  时间：%s  格式化时间:%s",position+"",
                    imageVideo.name,imageVideo.videoDuration+"",second));*/
            mHolder.mtv_video_duration.setText(String.format("00:%s", second));
        } else {
            visible(mHolder.checkmark);
            loadOverrideImage(context,
                    mHolder.image, imageVideo.path, 176, 176,imageVideo);
            gone(mHolder.mtv_video_duration);
        }

        if (imageVideo.isSelect){
            mHolder.checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_h);
            visible(mHolder.mask);
        }else {
            mHolder.checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_n);
            gone(mHolder.mask);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (!isEmpty(payloads) && holder instanceof LoaderLocalImgVideoHolder) {
            ImageVideo imageVideo = null;
            LoaderLocalImgVideoHolder mHolder = (LoaderLocalImgVideoHolder) holder;
            if (payloads.get(0) instanceof ImageVideo){
                imageVideo = (ImageVideo) payloads.get(0);
            }
            if (imageVideo.isSelect){
                mHolder.checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_h);
                visible(mHolder.mask);
            }else {
                mHolder.checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_n);
                gone(mHolder.mask);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    public class LoaderLocalImgVideoHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.image)
        ImageView image;

        @BindView(R.id.mask)
        View mask;

        @BindView(R.id.checkmark)
        MyImageView checkmark;

        @BindView(R.id.frame_rootView)
        FrameLayout frameRootView;

        @BindView(R.id.mtv_video_duration)
        MyTextView mtv_video_duration;

        public LoaderLocalImgVideoHolder(View itemView) {
            super(itemView);
            ViewGroup.LayoutParams layoutParams = frameRootView.getLayoutParams();
            int width = TransformUtil.countRealWidth(context, 176);
            layoutParams.width = width;
            layoutParams.height = width;
            frameRootView.setLayoutParams(layoutParams);

            checkmark.setOnClickListener(v -> {
                ImageVideo imageVideo = lists.get(getAdapterPosition());
                if (imageVideo.isPicDamage){
                    Common.staticToast("图片损坏，请换一张");
                    return;
                }
                if (mSelectionListener != null){
                    mSelectionListener.selection(getAdapterPosition(),
                            lists.get(getAdapterPosition()).isSelect);
                }
            });

            itemView.setOnClickListener(v -> {
                if (listener != null){
                    listener.onItemClick(v,getAdapterPosition());
                }
            });
        }
    }

    public void setOnSelectionListener(OnSelectionListener selectionListener){
        mSelectionListener = selectionListener;
    }

    public interface OnSelectionListener{
        void selection(int position,boolean oldSelection);
    }

    public void loadOverrideImage(Context context, ImageView imageView, String imgUrl,
                                  int withSize, int heightSize,ImageVideo imageVideo) {
        if (imageView == null||withSize<=0||heightSize<=0) return;
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.default_error)
                .placeholder(R.mipmap.default_error)
                .crossFade()
                .dontAnimate()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .override(withSize, heightSize).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model,
                                       Target<GlideDrawable> target, boolean isFirstResource) {
                imageVideo.isPicDamage = true;
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model,
                                           Target<GlideDrawable> target, boolean isFromMemoryCache,
                                           boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }
}
