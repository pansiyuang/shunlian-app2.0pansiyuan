package com.shunlian.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;

import java.io.File;

public class GlideUtils {

    /**
     * Glide默认加载RGB_565
     * context可传 context、activity、fragment
     */

    private static GlideUtils mInstance;

    private GlideUtils() {
    }

    public static GlideUtils getInstance() {
        if (mInstance == null) {
            synchronized (GlideUtils.class) {
                if (mInstance == null) {
                    mInstance = new GlideUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 常量
     */
    static class Contants {
        public static final int BLUR_VALUE = 20; //模糊
        public static final int CORNER_RADIUS = 20; //圆角
        public static final float THUMB_SIZE = 0.1f; //0-1之间
    }


    public void loadImage(Context context, ImageView imageView, String imgUrl) {
        loadImage(context, imageView, imgUrl, true);
    }

    public void loadImage(Context context, ImageView imageView, String imgUrl, @DrawableRes int placeholder) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                    .error(R.mipmap.error)
                .placeholder(placeholder)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(imageView);
    }

    public void loadImageZheng(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                    .error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_common)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(imageView);
    }

    public void loadImageChang(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                    .error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_home_retui)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(imageView);
    }

    public void loadImageShu(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                    .error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_home_promotion3)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(imageView);
    }

    /**
     * 常规加载图片
     *
     * @param context
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     * @param isFade    是否需要动画
     */

    public void loadImage(Context context, ImageView imageView, String imgUrl, boolean isFade) {
        if (imageView == null) return;
        if (isFade) {
            Glide.with(context)
                    .load(imgUrl)
//                    .error(R.mipmap.error)
                    .placeholder(R.mipmap.img_default_common)
                    .crossFade()
                    .priority(Priority.NORMAL) //下载的优先级
                    //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                    //source:缓存源资源   result：缓存转换后的资源
                    .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(imgUrl)
//                    .error(R.mipmap.error)
                    .dontAnimate()
                    .placeholder(imageView.getDrawable()) //解决点击图片加载闪动的bug
                    .priority(Priority.NORMAL) //下载的优先级
                    //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                    //source:缓存源资源   result：缓存转换后的资源
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .bitmapTransform(new CenterCrop(context))
                    .into(imageView);
        }
    }

    /**
     * 加载缩略图
     *
     * @param context
     * @param imageView 图片容器
     * @param imgUrl    图片地址
     */
    public void loadThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    /**
     * 加载图片并设置为指定大小
     *
     * @param context
     * @param imageView
     * @param imgUrl
     * @param withSize
     * @param heightSize
     */
    public void loadOverrideImage(Context context, ImageView imageView, String imgUrl, int withSize, int heightSize) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.img_default_common)
                .placeholder(R.mipmap.img_default_common)
                .crossFade()
                .dontAnimate()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .override(withSize, heightSize)
                .into(imageView);
    }

    /**
     * 加载图片并对其进行模糊处理
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
                .placeholder(R.mipmap.img_guige_moren)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new GlideBlurTransformation(context, Contants.BLUR_VALUE))
                .into(imageView);
    }

    /**
     * 加载圆图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCircleImage(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
                .placeholder(R.mipmap.img_zhanweitu)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new GlideCircleTransform(context))
                .into(imageView);
    }


    public void loadCircleAvar(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
                .placeholder(R.mipmap.img_set_defaulthead)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new GlideCircleTransform(context))
                .into(imageView);
    }

    public void loadCircleImage(Context context, ImageView imageView, String imgUrl, @DrawableRes int placeholder) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
                .placeholder(placeholder)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载模糊的圆图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurCircleImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new GlideBlurTransformation(context, Contants.BLUR_VALUE),
                        new GlideCircleTransform(context))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCornerImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new GlideRoundTransform(context, Contants.CORNER_RADIUS))
                .into(imageView);
    }

    /**
     * 加载圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCornerImage(Context context, ImageView imageView, String imgUrl, int radius) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .crossFade()
                .placeholder(R.mipmap.img_guige_moren)
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new CenterCrop(context),
                        new GlideRoundTransform(context, radius))
                .into(imageView);
    }

    /**
     * 加载模糊的圆角图片
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadBlurCornerImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(
                        new GlideBlurTransformation(context, Contants.BLUR_VALUE),
                        new GlideRoundTransform(context, Contants.CORNER_RADIUS))
                .into(imageView);
    }

    /**
     * 同步加载图片
     *
     * @param context
     * @param imgUrl
     * @param target
     */
    public void loadBitmapSync(Context context, String imgUrl, SimpleTarget<Bitmap> target) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(target);
    }

    /**
     * 加载gif
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadGifImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .into(imageView);
    }

    /**
     * 加载gif的缩略图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadGifThumbnailImage(Context context, ImageView imageView, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asGif()
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
//                .error(R.mipmap.error)
//                .placeholder(R.mipmap.error)
                .thumbnail(Contants.THUMB_SIZE)
                .into(imageView);
    }

    /**
     * 控件放置背景图片
     */

    public void loadBgImage(Context context, final View view, String imgUrl, @DrawableRes int placeholder) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .placeholder(placeholder)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN&&view!=null) {
                            view.setBackground(drawable);
                        }
                    }
                });
    }


    public void loadBgImageZheng(Context context, final View view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .placeholder(R.mipmap.img_default_common)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(drawable);
                        }
                    }
                });
    }

    public void loadBgImageChang(Context context, final View view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .placeholder(R.mipmap.img_default_home_retui)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(drawable);
                        }
                    }
                });
    }

    public void loadBgImageShu(Context context, final View view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .placeholder(R.mipmap.img_default_home_promotion3)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(drawable);
                        }
                    }
                });
    }

    /**
     * 控件放置背景图片
     */

    public void loadBgImage(Context context, final View view, String imgUrl) {
        Glide.with(context)
                .load(imgUrl)
                .asBitmap()
                .placeholder(R.mipmap.img_guige_moren)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Drawable drawable = new BitmapDrawable(resource);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            view.setBackground(drawable);
                        }
                    }
                });
    }

    /**
     * 加载资源文件方法
     */

    public void loadLocalImageWithView(Context context, @DrawableRes int resourceId, ImageView view) {
        if (view == null) return;
        Glide.with(context).
                load(resourceId).
                into(view);
    }

    /**
     * 加载图片文件方法
     */

    public void loadFileImageWithView(Context context, File file, ImageView view) {
        Glide.with(context).
                load(file).
                into(view);
    }


    /**
     * 恢复请求，一般在停止滚动的时候
     *
     * @param context
     */
    public void resumeRequests(Context context) {
        Glide.with(context).resumeRequests();
    }

    /**
     * 暂停请求 正在滚动的时候
     *
     * @param context
     */
    public void pauseRequests(Context context) {
        Glide.with(context).pauseRequests();
    }

    //Glide保存图片
    public void savePicture(final Context context, String url) {
        Glide.with(context).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                BitmapUtil.saveImageToAlbumn(context, resource);
                Common.staticToasts(context, "保存成功", R.mipmap.icon_common_duihao);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Common.staticToasts(context, "保存失败", R.mipmap.icon_common_tanhao);
            }
        });
    }

    /**
     * 发现关注列表
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void findFollowList(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
                //.error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_find_followlist)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.SOURCE) //缓存策略
                .into(imageView);
    }

    /**
     * 圈子的轮播
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void communityBanner(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
                //.error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_find_circlebanner)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                //all:缓存源资源和转换后的资源 none:不作任何磁盘缓存
                //source:缓存源资源   result：缓存转换后的资源
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .into(imageView);
    }

    /**
     * 圈子顶部图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void communityTopPic(Context context, ImageView imageView, String imgUrl, int radius,boolean isCorp) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
                //.error(R.mipmap.error)
                .placeholder(R.mipmap.img_default_find_circletopic)
                .crossFade()
                .placeholder(R.mipmap.img_guige_moren)
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(isCorp?new CenterCrop(context):new FitCenter(context),
                        new GlideRoundTransform(context, radius))
//                .bitmapTransform(new CenterCrop(context),
//                        new GlideRoundTransform(context, radius))
                .into(imageView);
    }


    /**
     * 加载圆图
     *
     * @param context
     * @param imageView
     * @param imgUrl
     */
    public void loadCircleHeadImage(Context context, ImageView imageView, String imgUrl) {
        if (imageView == null) return;
        Glide.with(context)
                .load(imgUrl)
                .error(R.mipmap.img_set_defaulthead)
                .placeholder(R.mipmap.img_set_defaulthead)
                .crossFade()
                .priority(Priority.NORMAL) //下载的优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL) //缓存策略
                .bitmapTransform(new GlideCircleTransform(context))
                .into(imageView);
    }

    public void clearMemory(){
        Glide.get(Common.getApplicationContext()).clearMemory();
    }
}