package com.shunlian.app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.utils.GlideUtils;
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
                GlideUtils.getInstance().loadOverrideImage(context,
                        mHolder.image, imageVideo.coverPath, 176, 176);
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
            GlideUtils.getInstance().loadOverrideImage(context,
                    mHolder.image, imageVideo.path, 176, 176);
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
}
