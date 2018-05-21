package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */

public class SingleImgAdapter extends BaseAdapter {
    public static final int REQUEST_CAMERA_CODE = 1001;
    private Context mContext;
    private List<ImageEntity> pics;
    private int parentPosition;
    private int screenWidth;
    private PhotoPickerIntent intent;
    private int MAX_SIZE = 5;

    public SingleImgAdapter(Context context, List<ImageEntity> data, int position) {
        this.mContext = context;
        this.pics = data;
        this.parentPosition = position;
        screenWidth = DeviceInfoUtil.getDeviceWidth(mContext);
    }

    public SingleImgAdapter(Context context, List<ImageEntity> data) {
        this.mContext = context;
        this.pics = data;
        screenWidth = DeviceInfoUtil.getDeviceWidth(mContext);
        intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true);
    }

    public void setMaxSize(int maxSize) {
        this.MAX_SIZE = maxSize;
    }

    @Override
    public int getCount() {
        return pics == null ? 1 : pics.size() + 1;//返回listiview数目加1
    }

    public void setData(List<ImageEntity> data) {
        this.pics = data;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return pics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_single_img, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (pics != null && position < pics.size()) {
            viewHolder.miv_del.setVisibility(View.VISIBLE);
            String imgPath = pics.get(position).imgPath;
            String imgUrl = pics.get(position).imgUrl;
            if (!TextUtils.isEmpty(imgPath)) {
                GlideUtils.getInstance().loadFileImageWithView(mContext, new File(imgPath), viewHolder.miv_img);
            } else if (!TextUtils.isEmpty(imgUrl)) {
                GlideUtils.getInstance().loadImage(mContext, viewHolder.miv_img, imgUrl);
            }
            viewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) toStringArray();
                    bigImgEntity.index = position;
                    LookBigImgAct.startAct(mContext, bigImgEntity);
                }
            });
            viewHolder.mtv_max.setVisibility(View.GONE);
        } else {
            GlideUtils.getInstance().loadLocalImageWithView(mContext, R.mipmap.img_tupian, viewHolder.miv_img);
            viewHolder.mtv_max.setVisibility(View.VISIBLE);
            viewHolder.miv_del.setVisibility(View.GONE);
            viewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof CreatCommentActivity) {
                        ((CreatCommentActivity) mContext).openAlbum(parentPosition);
                    } else {
                        intent.setMaxTotal(MAX_SIZE - pics.size()); // 最多选择照片数量，默认为9
                        ((Activity) mContext).startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                }
            });
        }
        viewHolder.miv_del.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                ImageEntity imageEntity = pics.get(position);
                pics.remove(imageEntity);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public List<String> toStringArray() {
        List<String> result = new ArrayList<>();
        for (ImageEntity imageEntity : pics) {
            if (!TextUtils.isEmpty(imageEntity.imgPath)) {
                result.add(imageEntity.imgPath);
            } else if (!TextUtils.isEmpty(imageEntity.imgUrl)) {
                result.add(imageEntity.imgUrl);
            }
        }
        return result;
    }

    public class ViewHolder {
        MyImageView miv_img;
        MyImageView miv_del;
        RelativeLayout rl_img;
        MyTextView mtv_max;

        public ViewHolder(View view) {
            miv_img = (MyImageView) view.findViewById(R.id.miv_img);
            miv_del = (MyImageView) view.findViewById(R.id.miv_img_del);
            rl_img = (RelativeLayout) view.findViewById(R.id.layout_img);
            mtv_max = (MyTextView) view.findViewById(R.id.mtv_max);

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screenWidth - TransformUtil.dip2px(mContext, 20 + (4 * 4))) / 5;
            layoutParams.height = layoutParams.width;
            rl_img.setLayoutParams(layoutParams);
        }
    }
}
