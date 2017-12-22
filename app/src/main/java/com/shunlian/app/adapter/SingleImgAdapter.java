package com.shunlian.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class SingleImgAdapter extends BaseRecyclerAdapter<ImageEntity> {

    private static final int FOOTER_IMG = 2;
    private int parentPosition;
    private int screenWidth;
    private int picWidth;

    public SingleImgAdapter(Context context, boolean isShowFooter, List<ImageEntity> lists, int position) {
        super(context, isShowFooter, lists);
        this.parentPosition = position;
        screenWidth = DeviceInfoUtil.getDeviceWidth(context);
        picWidth = (screenWidth - TransformUtil.dip2px(context, 20 + 4 * 5)) / 5;
    }

    public void setData(List<ImageEntity> list) {
        lists.clear();
        lists.addAll(list);
        notifyDataSetChanged();
    }

    public void addImages(List<ImageEntity> imageEntities) {
        lists.addAll(imageEntities);
        notifyDataSetChanged();
    }

    public void updateItemProgress(String tag, int progress) {
        if (lists == null || lists.size() == 0) {
            return;
        }
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).imgPath.equals(tag)) {
                lists.get(i).progress = progress;
                break;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER_IMG:
                View inflate = LayoutInflater.from(context).inflate(R.layout.item_single_img, parent, false);
                return new DelImagViewHolder(inflate);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount())
            return FOOTER_IMG;
        else
            return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        if (lists == null) {
            return 1;
        }
        return super.getItemCount() + 1;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case FOOTER_IMG:
                handFoot(holder);
                break;
            default:
                handleList(holder, position);
                break;
        }
    }


    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_single_img, parent, false);
        return new ImagViewHolder(inflate);
    }


    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImagViewHolder) {
            ImagViewHolder imagViewHolder = (ImagViewHolder) holder;
            imagViewHolder.miv_img_del.setVisibility(View.VISIBLE);
            GlideUtils.getInstance().loadImage(context, imagViewHolder.miv_img, lists.get(position).imgPath);
            ImageEntity imageEntity = lists.get(position);

            if (imageEntity.progress > 0 && imageEntity.progress < 100) {
                imagViewHolder.pgb_img.setVisibility(View.VISIBLE);
                imagViewHolder.pgb_img.setProgress(imageEntity.progress);
            } else if (imageEntity.progress >= 100) {
                imagViewHolder.pgb_img.setVisibility(View.GONE);
            }
        }
    }


    public void handFoot(RecyclerView.ViewHolder holder) {
        if (holder instanceof DelImagViewHolder) {
            DelImagViewHolder delImagViewHolder = (DelImagViewHolder) holder;
            delImagViewHolder.miv_img_del.setVisibility(View.GONE);
            delImagViewHolder.miv_img.setImageResource(R.mipmap.img_tupian);

            ((DelImagViewHolder) holder).miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (context instanceof CreatCommentActivity && !((CreatCommentActivity) context).isFinishing()) {
                        ((CreatCommentActivity) context).openAlbum(parentPosition);
                    }
                }
            });
        }
    }

    public class ImagViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_img_del)
        MyImageView miv_img_del;

        @BindView(R.id.pgb_img)
        ProgressBar pgb_img;

        @BindView(R.id.layout_img)
        RelativeLayout layout_img;


        public ImagViewHolder(View itemView) {
            super(itemView);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, picWidth);
            layout_img.setLayoutParams(layoutParams);
        }
    }

    public class DelImagViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_img_del)
        MyImageView miv_img_del;

        @BindView(R.id.pgb_img)
        ProgressBar pgb_img;

        @BindView(R.id.layout_img)
        RelativeLayout layout_img;

        public DelImagViewHolder(View itemView) {
            super(itemView);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(picWidth, picWidth);
            layout_img.setLayoutParams(layoutParams);
        }
    }

}
