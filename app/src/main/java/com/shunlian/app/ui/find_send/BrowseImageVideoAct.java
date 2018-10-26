package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.player.SmallMediaPlayer;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.photoview.HackyViewPager;
import com.shunlian.app.widget.photoview.PhotoView;
import com.shunlian.app.widget.photoview.PhotoViewAttacher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhanghe on 2018/10/24.
 */

public class BrowseImageVideoAct extends BaseActivity {


    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.view_pager)
    HackyViewPager viewPager;

    @BindView(R.id.leftNo)
    MyTextView leftNo;

    @BindView(R.id.rightNo)
    MyTextView rightNo;

    @BindView(R.id.tv_complete)
    TextView tvComplete;

    @BindView(R.id.layout_top_section)
    RelativeLayout layoutTopSection;

    private ArrayList<ImageVideo> mImageVideos;
    private BuildConfig mConfig;
    private List<ImageVideo> editLists;
    private int mCurrentPosition;
    private int maxCount;
    private ArrayList<String> selectLists;
    private String format = "完成(%d/%d)";

    public static final int REQUEST_CODE = 8888;
    private boolean isOnlyBrowse;

    public static void startAct(Activity activity, BuildConfig config,int code) {
        activity.startActivityForResult(new Intent(activity, BrowseImageVideoAct.class)
                .putExtra("config", config),code);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_browse_image_video;
    }

    @Override
    protected void initListener() {
        super.initListener();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                leftNo.setText(String.valueOf(position+1));
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvComplete.setOnClickListener(v->{
            if (!isEmpty(editLists)){
                String path = editLists.get(mCurrentPosition).path;
                if (isMP4Path(path)){
                    Intent intent = new Intent();
                    intent.putExtra("video",path);
                    intent.putExtra("isComplete",true);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }else {
                    result(true);
                }
            }
        });
        miv_close.setOnClickListener(v->result(false));
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
    }

    /**
     * 图片视频列表
     *
     * @param imageVideos
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void recData(ArrayList<ImageVideo> imageVideos) {
        mImageVideos = imageVideos;
        mConfig = getIntent().getParcelableExtra("config");
        maxCount = mConfig.max_count;
        selectLists = mConfig.selectLists;
        isOnlyBrowse = mConfig.isOnlyBrowse;
        if (isOnlyBrowse){
            gone(tvComplete);
        }
        //LogUtil.zhLogW(mConfig.position+"=selectLists========"+selectLists.size());
        if (!mConfig.isShowImageVideo) {
            editLists = new ArrayList<>();
            String path = mImageVideos.get(mConfig.position).path;
            if (isMP4Path(path)){
                for (int i = 0; i<mImageVideos.size();i++) {
                    ImageVideo iv = mImageVideos.get(i);
                    if (isMP4Path(iv.path)){
                        editLists.add(iv);
                        if (mConfig.position == i){
                            mCurrentPosition = editLists.size()-1;
                        }
                    }
                }
            }else {
                for (int i = 0; i<mImageVideos.size();i++) {
                    ImageVideo iv = mImageVideos.get(i);
                    if (isPicFile(iv.path)){
                        editLists.add(iv);
                        if (mConfig.position == i){
                            mCurrentPosition = editLists.size()-1;
                        }
                    }
                }
            }
            SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(editLists);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(mCurrentPosition, false);
            rightNo.setText(String.valueOf(editLists.size()));
            leftNo.setText(String.valueOf(mCurrentPosition+1));
        }else {
            SamplePagerAdapter pagerAdapter = new SamplePagerAdapter(mImageVideos);
            viewPager.setAdapter(pagerAdapter);
            viewPager.setCurrentItem(mConfig.position, false);
            rightNo.setText(String.valueOf(mImageVideos.size()));
            leftNo.setText(String.valueOf(mConfig.position+1));
        }
    }


    private void result(boolean isComplete){
        if (!isEmpty(selectLists)) {
            Intent intent = new Intent();
            intent.putStringArrayListExtra("data",selectLists);
            intent.putExtra("isComplete",isComplete);
            setResult(Activity.RESULT_OK,intent);
        }
        finish();
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
     * 判断是否是图片
     *
     * @param file
     * @return
     */
    public boolean isPicFile(String file) {
        if (isEmpty(file))return false;
        else return
            (file.toLowerCase().endsWith(".jpg")
                    || file.toLowerCase().endsWith(".jpeg")
                    || file.toLowerCase().endsWith(".png"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private class SamplePagerAdapter extends PagerAdapter {
        private List<ImageVideo> list;
        private LayoutInflater inflater;
        String url = "";
        private MediaMetadataRetriever mRetriever;

        public SamplePagerAdapter(List<ImageVideo> imageMap) {
            this.list = imageMap;
            inflater = getLayoutInflater();
            mRetriever = new MediaMetadataRetriever();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, container, false);
            final ProgressBar spinner = imageLayout.findViewById(R.id.loading);
            final PhotoView imageView = imageLayout.findViewById(R.id.image);
            final View layout_error = imageLayout.findViewById(R.id.layout_error);
            final MyImageView miv_play = imageLayout.findViewById(R.id.miv_play);
            final MyImageView checkmark = imageLayout.findViewById(R.id.checkmark);
            checkmark.setVisibility(View.VISIBLE);
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    result(false);
                }

                @Override
                public void onOutsidePhotoTap() {
                    if (layoutTopSection.getVisibility() == View.VISIBLE){
                        layoutTopSection.setVisibility(View.INVISIBLE);
                    }else {
                        layoutTopSection.setVisibility(View.VISIBLE);
                    }
                }
            });

            try {
                ImageVideo imageVideo = list.get(position);
                url = imageVideo.path;
                if (isMP4Path(url)) {
                    checkmark.setVisibility(View.GONE);
                    if (!isEmpty(imageVideo.coverPath)) {
                        GlideUtils.getInstance().loadImage(baseAct,imageView,imageVideo.coverPath);
                    }else {
                        mRetriever.setDataSource(url);
                        Bitmap frameAtTime = mRetriever.getFrameAtTime();
                        imageView.setImageBitmap(frameAtTime);
                    }
                    visible(miv_play);
                } else {
                    if (isOnlyBrowse) {
                        checkmark.setVisibility(View.GONE);
                    }else {
                        checkmark.setVisibility(View.VISIBLE);
                    }
                    gone(miv_play);
                    loadImg(url, imageView, spinner, layout_error);
                    layout_error.setOnClickListener(v -> loadImg(url, imageView, spinner, layout_error));
                }

                if (imageVideo.isSelect) {
                    checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_h);
                } else {
                    checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_n);
                }

                checkmark.setOnClickListener(v -> {
                    imageVideo.isSelect = !imageVideo.isSelect;
                    boolean isSel = selectHandler(position, imageVideo.isSelect, imageVideo);
                    if (isSel) {
                        if (imageVideo.isSelect) {
                            checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_h);
                        } else {
                            checkmark.setImageResource(R.mipmap.img_shoppingcar_selected_n);
                        }
                    }else {
                        imageVideo.isSelect = !imageVideo.isSelect;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            miv_play.setOnClickListener(v -> {
                ImageVideo imageVideo = list.get(position);
                playVideo(imageVideo.path);
            });
            container.addView(imageLayout, 0);
            return imageLayout;
        }


        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public void loadImg(String url, final PhotoView imageView, final ProgressBar spinner, final View layout_error) {
        if (URLUtil.isNetworkUrl(url)) {
            GlideUtils.getInstance().loadBitmapSync(this, url, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    spinner.setVisibility(View.GONE);
                    layout_error.setVisibility(View.GONE);
                    imageView.setImageBitmap(resource);
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {
                    super.onLoadStarted(placeholder);
                    spinner.setVisibility(View.VISIBLE);
                    layout_error.setVisibility(View.GONE);
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                    spinner.setVisibility(View.GONE);
                    layout_error.setVisibility(View.VISIBLE);
                }
            });
        } else {
            GlideUtils.getInstance().loadImage(this, imageView, url);
        }
    }

    private boolean selectHandler(int position, boolean oldSelection, ImageVideo imageVideo) {
        int count;
        if (selectLists == null) {
            selectLists = new ArrayList<>();
        }
        if (oldSelection) {
            selectLists.add(imageVideo.path);
            if (isCanSelect() == -1) {
                selectLists.remove(imageVideo.path);
                Common.staticToast("图片和视频不能同时选择");
                return false;
            } else if (isCanSelect() == -2) {
                selectLists.remove(imageVideo.path);
                Common.staticToast("只能选择一个视频");
                return false;
            }
            count = selectLists.size();
            if (count > maxCount) {
                selectLists.remove(imageVideo.path);
                Common.staticToast(String.format("您最多只能选择%d张图片", maxCount));
                return false;
            }
        } else {
            selectLists.remove(imageVideo.path);
            count = selectLists.size();
        }
        tvComplete.setText(String.format(format,count,maxCount));
        return true;
    }

    /**
     * 、
     * 图片和视频不能一起选择,不能同时选这两个视频
     *
     * @return -1 图片和视频不能一起选择  -2不能同时选这两个视频
     */
    public int isCanSelect() {
        if (!isEmpty(selectLists)) {
            boolean isImage = false;
            boolean isVideo = false;
            int video_count = 0;
            for (String path : selectLists) {
                if (isPicFile(path)) {
                    isImage = true;
                }
                if (path.toLowerCase().endsWith(".mp4")) {
                    isVideo = true;
                    video_count++;
                }
                if (isImage && isVideo) {
                    return -1;
                }
                if (video_count == 2) {
                    return -2;
                }
            }
        }
        return 0;
    }


    private void playVideo(String url) {

        SmallMediaPlayer.startAct(this, url);
        /*if (URLUtil.isNetworkUrl(url)){
        }else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("file://" + url);
            intent.setDataAndType(data, "video/mp4");
            try {
                startActivity(intent);
            } catch (Exception e) {

            }
        }*/
    }



    /**
     * 开启activity的配置信息
     */
    public static class BuildConfig implements Parcelable {
        /**
         * 图片和视频是否一起选择
         */
        public boolean isShowImageVideo;

        /**
         * 是否仅预览
         */
        public boolean isOnlyBrowse;
        /**
         * 最大选择数量
         */
        public int max_count;
        /**
         * 当前已选择数量
         */
        public int count;
        /**
         * 当前列表所在下标
         */
        public int position;

        public ArrayList<String> selectLists;

        public BuildConfig() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.isShowImageVideo ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isOnlyBrowse ? (byte) 1 : (byte) 0);
            dest.writeInt(this.max_count);
            dest.writeInt(this.count);
            dest.writeInt(this.position);
            dest.writeStringList(this.selectLists);
        }

        protected BuildConfig(Parcel in) {
            this.isShowImageVideo = in.readByte() != 0;
            this.isOnlyBrowse = in.readByte() != 0;
            this.max_count = in.readInt();
            this.count = in.readInt();
            this.position = in.readInt();
            this.selectLists = in.createStringArrayList();
        }

        public static final Creator<BuildConfig> CREATOR = new Creator<BuildConfig>() {
            @Override
            public BuildConfig createFromParcel(Parcel source) {
                return new BuildConfig(source);
            }

            @Override
            public BuildConfig[] newArray(int size) {
                return new BuildConfig[size];
            }
        };
    }
}
