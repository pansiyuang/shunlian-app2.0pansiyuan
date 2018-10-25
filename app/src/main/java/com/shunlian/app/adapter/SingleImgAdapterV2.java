package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.ui.find_send.FindSendPictureTextAct;
import com.shunlian.app.ui.find_send.SelectPicVideoAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * Created by Administrator on 2017/12/22.
 */

public class SingleImgAdapterV2 extends BaseRecyclerAdapter<ImageVideo> {
    private BuildConfig mConfig;
    private int screenWidth;
    private final MediaMetadataRetriever mRetriever;

    public SingleImgAdapterV2(Context context, List<ImageVideo> lists, BuildConfig config) {
        super(context, false, lists);
        mConfig = config;
        screenWidth = DeviceInfoUtil.getDeviceWidth(context);
        mRetriever = new MediaMetadataRetriever();
    }

    @Override
    public int getItemCount() {
        if (!isEmpty(lists) && isMP4Path(lists.get(0).path)){
            return 1;
        }else {
            if (!isEmpty(lists) && lists.size()==mConfig.max_count){
                return lists.size();
            }else {
                return super.getItemCount() + 1;
            }
        }
    }

    public List<String> toStringArray() {
        List<String> result = new ArrayList<>();
        for (ImageVideo imageEntity : lists) {
            if (!TextUtils.isEmpty(imageEntity.path)) {
                result.add(imageEntity.path);
            } else if (!TextUtils.isEmpty(imageEntity.url)) {
                result.add(imageEntity.url);
            }
        }
        return result;
    }

    /**
     * 判断是否是MP4文件路径
     * @param path
     * @return
     */
    private boolean isMP4Path(String path){
        if (isEmpty(path))return false;
        else return path.toLowerCase().endsWith(".mp4");
    }

    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.item_single_img, parent, false);
        return new SingleImgHolderV2(view);
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        SingleImgHolderV2 viewHolder =  (SingleImgHolderV2)holder;

        if (!isEmpty(lists)&&position < lists.size()) {
            visible(viewHolder.miv_del);
            gone(viewHolder.mtv_max);
            if (position >= lists.size())return;
            ImageVideo imageEntity = lists.get(position);
            String imgPath = imageEntity.path;
            String imgUrl = imageEntity.url;
            if (!TextUtils.isEmpty(imgPath)) {
                if (imgPath.toLowerCase().endsWith(".mp4")){
                    visible(viewHolder.mtv_video_duration);
                    String second = String.valueOf(imageEntity.videoDuration / 1000);
                    if (second.length() == 1) {
                        second = "0" + second;
                    }
                    viewHolder.mtv_video_duration.setText(String.format("00:%s", second));
                    if (URLUtil.isNetworkUrl(imgPath)){
                        GlideUtils.getInstance().loadImage(context,viewHolder.miv_img,imageEntity.coverPath);
                    }else {
                        try {
                            mRetriever.setDataSource(imgPath);
                            Bitmap frameAtTime = mRetriever.getFrameAtTime();
                            viewHolder.miv_img.setImageBitmap(frameAtTime);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }else {
                    gone(viewHolder.mtv_video_duration);
                    GlideUtils.getInstance().loadFileImageWithView(context, new File(imgPath), viewHolder.miv_img);
                }
            } else if (!TextUtils.isEmpty(imgUrl)) {
                gone(viewHolder.mtv_video_duration);
                GlideUtils.getInstance().loadImage(context, viewHolder.miv_img, imgUrl);
            }
            viewHolder.miv_img.setOnClickListener(v -> {
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) toStringArray();
                bigImgEntity.index = position;
                LookBigImgAct.startAct(context, bigImgEntity);
            });
        } else {
            gone(viewHolder.miv_del,viewHolder.mtv_video_duration);
            visible(viewHolder.mtv_max);
            viewHolder.mtv_max.setText("(最多" + mConfig.max_count + "张)");
            GlideUtils.getInstance().loadLocalImageWithView(context, R.mipmap.img_tupian, viewHolder.miv_img);
            viewHolder.miv_img.setOnClickListener(v -> {
                int size = 0;
                if (lists != null) {
                    size = lists.size();
                }
                SelectPicVideoAct.startAct((Activity) context,
                        FindSendPictureTextAct.SELECT_PIC_REQUESTCODE, mConfig.max_count - size);
            });
        }
        viewHolder.miv_del.setOnClickListener(v -> {
            ImageVideo imageEntity = lists.get(position);
            lists.remove(imageEntity);
            notifyDataSetChanged();
        });
    }

    public class SingleImgHolderV2 extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.mtv_max)
        MyTextView mtv_max;

        @BindView(R.id.miv_img_del)
        MyImageView miv_del;

        @BindView(R.id.layout_img)
        RelativeLayout layoutImg;

        @BindView(R.id.mtv_video_duration)
        MyTextView mtv_video_duration;

        public SingleImgHolderV2(View itemView) {
            super(itemView);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screenWidth - TransformUtil.dip2px(context, 20 + (4 * 4))) / 5;
            layoutParams.height = layoutParams.width;
            layoutImg.setLayoutParams(layoutParams);
        }
    }


    public static class BuildConfig {
        /**
         * 最多可选数量
         */
        public int max_count;
        /**
         * 图片和视频是否可一起选择
         */
        public boolean pictureAndVideo;
    }
}
