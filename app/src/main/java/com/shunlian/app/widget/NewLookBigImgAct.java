package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverGoodsAdapter;
import com.shunlian.app.adapter.OperateAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.photoview.HackyViewPager;
import com.shunlian.app.widget.photoview.PhotoView;
import com.shunlian.app.widget.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/15.
 */

public class NewLookBigImgAct extends BaseActivity{
    @BindView(R.id.leftNo)
    MyTextView leftNo;
    @BindView(R.id.rightNo)
    MyTextView rightNo;
    @BindView(R.id.tv_content)
    MyTextView tv_content;
    @BindView(R.id.mtv_code)
    MyTextView mtv_code;
    @BindView(R.id.mtv_goods)
    MyTextView mtv_goods;
    @BindView(R.id.mtv_save)
    MyTextView mtv_save;
    @BindView(R.id.view_pager)
    HackyViewPager view_pager;
    @BindView(R.id.layout_content)
    View layout_content;
    @BindView(R.id.layout_top_section)
    View layout_top_section;
    private BigImgEntity entity;
    private Dialog dialog_operate;
    private Activity activity;
    private int pos = 0;
    private PromptDialog promptDialog;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    public static void startAct(Context context, BigImgEntity entity) {
        Intent intent = new Intent(context, NewLookBigImgAct.class);
        intent.putExtra("data", entity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroy();
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_look_big_img_new;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        activity = this;
        entity = getIntent().getParcelableExtra("data");
        if (entity != null) {
            if (isEmpty(entity.items))
                entity.items = new ArrayList<>();
            entity.items.add(0, getString(R.string.operate_baocuntupian));
            entity.items.add(entity.items.size(), getString(R.string.errcode_cancel));
        }
        SamplePagerAdapter adapter = new SamplePagerAdapter(entity.itemList);
        view_pager.setAdapter(adapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
        mtv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhoto();
            }
        });
        if (entity.blog==null||isEmpty(entity.blog.related_goods)){
            mtv_code.setVisibility(View.GONE);
            mtv_goods.setVisibility(View.GONE);
        }else {
            mtv_goods.setText(String.format(getStringResouce(R.string.discover_shangping),entity.blog.related_goods.size()));
            mtv_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (entity.blog.related_goods.size()==1){
                        GoodsDeatilEntity.Goods goods = entity.blog.related_goods.get(0);
                        quick_actions.createCode(goods.share_url,goods.title,goods.desc,goods.price,goods.goods_id,goods.thumb,
                                1==goods.isSuperiorProduct,SharedPrefUtil.getSharedUserString("nickname", ""),
                                SharedPrefUtil.getSharedUserString("avatar", ""));
                    }else {
                        initDialog(entity.blog,true);
                    }
                }
            });
            mtv_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (entity.blog.related_goods.size()==1){
                        GoodsDetailAct.startAct(baseAct,entity.blog.related_goods.get(0).goods_id);
                    }else {
                        initDialog(entity.blog,false);
                    }
                }
            });
        }
    }

    public void loadImg(String url, final PhotoView imageView, final ProgressBar spinner, final View layout_error) {
        if (url.startsWith("https")) {
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

    public void initDialog(BigImgEntity.Blog blog,boolean isCode) {
        Dialog dialog_new = new Dialog(baseAct, R.style.popAd);
        dialog_new.setContentView(R.layout.dialog_found_goods);
        Window window = dialog_new.getWindow();
//        //设置边框距离
//        window.getDecorView().setPadding(0, 0, 0, 0);
        //设置dialog位置
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置宽高
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
        MyImageView miv_icon = dialog_new.findViewById(R.id.miv_icon);
        NewTextView ntv_desc = dialog_new.findViewById(R.id.ntv_desc);
        RecyclerView rv_goods = dialog_new.findViewById(R.id.rv_goods);

        miv_close.setOnClickListener(view -> dialog_new.dismiss());
        GlideUtils.getInstance().loadCircleImage(baseAct, miv_icon, blog.avatar);
        SpannableStringBuilder ssb = Common.changeColor(blog.nickname
                + getString(R.string.discover_fenxiangdetuijian), blog.nickname, getResources().getColor(R.color.value_007AFF));
        ntv_desc.setText(ssb);
        rv_goods.setLayoutManager(new LinearLayoutManager(baseAct));
        DiscoverGoodsAdapter discoverGoodsAdapter = new DiscoverGoodsAdapter(baseAct, blog.related_goods,isCode,quick_actions,
                SharedPrefUtil.getSharedUserString("nickname", ""),SharedPrefUtil.getSharedUserString("avatar", ""));
        rv_goods.setAdapter(discoverGoodsAdapter);
        discoverGoodsAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsDetailAct.startAct(baseAct, blog.related_goods.get(position).goods_id);
            }
        });
        rv_goods.addItemDecoration(new MVerticalItemDecoration(baseAct,36,38,38));
        dialog_new.setCancelable(false);
        dialog_new.show();

    }

    public void savePhoto(){
        DownLoadImageThread thread = new DownLoadImageThread(activity,
                entity.itemList.get(pos), new DownLoadImageThread.MyCallBack() {
            @Override
            public void successBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Common.staticToast(getStringResouce(R.string.operate_tupianyibaocun));
                    }
                });
            }

            @Override
            public void errorBack() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Common.staticToast(getStringResouce(R.string.operate_tupianbaocunshibai));
                    }
                });
            }
        });
        thread.start();
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


        void initDialog(BigImgEntity entity) {
            if (dialog_operate == null) {
                dialog_operate = new Dialog(activity, R.style.popAd);
                dialog_operate.setContentView(R.layout.dialog_operate);
                RecyclerView rv_operate = (RecyclerView) dialog_operate.findViewById(R.id.rv_operate);
                rv_operate.setLayoutManager(new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false));
                rv_operate.addItemDecoration(new MVerticalItemDecoration(activity, 0.5f, 0, 0, getColorResouce(R.color.bg_gray_two)));
                OperateAdapter operateAdapter = new OperateAdapter(activity, entity.items);
                rv_operate.setAdapter(operateAdapter);
                operateAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        switch (entity.items.get(position)) {
                            case "保存图片":
                                savePhoto();
                                break;
                            case "图文分享":
                                DownLoadImageThread threads = new DownLoadImageThread(activity,
                                        entity.itemList);
                                threads.start();
                                ClipboardManager cm = (ClipboardManager) activity
                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(entity.content);
                                if (promptDialog == null) {
                                    promptDialog = new PromptDialog((Activity) activity);
                                    promptDialog.setSureAndCancleListener(getString(R.string.discover_wenzifuzhi),
                                            getString(R.string.discover_tupianbaocun), "", getString(R.string.discover_quweixinfenxiang), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Common.openWeiXin(activity, "material", entity.id);
                                                    promptDialog.dismiss();
                                                }
                                            }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    promptDialog.dismiss();
                                                }
                                            });
                                }
                                promptDialog.show();
                                break;
                        }
                        dialog_operate.dismiss();
                    }
                });
            }
            dialog_operate.show();
        }

        @Override
        public View instantiateItem(final ViewGroup container, int position) {
            View imageLayout = inflater.inflate(R.layout.item_pager_image_new, container, false);
            final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
            final PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
            final View layout_error = imageLayout.findViewById(R.id.layout_error);
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                    initDialog(entity);
                    return true;
                }
            });
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
                layout_error.setOnClickListener(v -> loadImg(url, imageView, spinner, layout_error));
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
}
