package com.shunlian.app.ui.my_comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.photoview.HackyViewPager;
import com.shunlian.app.widget.photoview.PhotoView;
import com.shunlian.app.widget.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class LookBigImgAct extends BaseActivity {

    @BindView(R.id.leftNo)
    MyTextView leftNo;

    @BindView(R.id.rightNo)
    MyTextView rightNo;

    @BindView(R.id.tv_content)
    MyTextView tv_content;

    @BindView(R.id.view_pager)
    HackyViewPager view_pager;

    @BindView(R.id.layout_content)
    View layout_content;

    @BindView(R.id.layout_top_section)
    View layout_top_section;
    private BigImgEntity entity;

    public static void startAct(Context context, BigImgEntity entity) {
        Intent intent = new Intent(context, LookBigImgAct.class);
        intent.putExtra("data", entity);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_look_big_img;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        entity = getIntent().getParcelableExtra("data");
        SamplePagerAdapter adapter = new SamplePagerAdapter(entity.itemList);
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int pos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
                leftNo.setText(String.valueOf(position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        rightNo.setText(String.valueOf(entity.itemList.size()));
        view_pager.setCurrentItem(entity.index);
        if (isEmpty(entity.desc)) {
            layout_content.setVisibility(View.INVISIBLE);
        } else {
            layout_content.setVisibility(View.VISIBLE);
            tv_content.setText(entity.desc);
        }
    }

    private class SamplePagerAdapter extends PagerAdapter {
        private List<String> list;
        private LayoutInflater inflater;

        public SamplePagerAdapter(List<String> imageMap) {
            this.list = imageMap;
            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image, container, false);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
            final View layout_error = imageLayout.findViewById(R.id.layout_error);
            imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    finish();
                }

                @Override
                public void onOutsidePhotoTap() {
                    if (layout_top_section.getVisibility() == View.VISIBLE) {
                        layout_top_section.setVisibility(View.INVISIBLE);
                        layout_content.setVisibility(View.INVISIBLE);
                    } else {
                        layout_top_section.setVisibility(View.VISIBLE);
                        layout_content.setVisibility(View.VISIBLE);
                    }
                }
            });

            try {
                final String url = list.get(position);
                loadImg(url, imageView, spinner, layout_error);
                layout_error.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadImg(url, imageView, spinner, layout_error);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

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
        if (url.startsWith("http")) {
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
            GlideUtils.getInstance().loadFileImageWithView(this, new File(url),imageView);
        }
    }
}
