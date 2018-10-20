package com.shunlian.app.adapter;

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

/**
 * Created by Administrator on 2017/12/22.
 */

public class SingleImgAdapterV2 extends BaseAdapter {
    public static final int REQUEST_CAMERA_CODE = 1001;
    private Context mContext;
    private List<ImageEntity> pics;
    private BuildConfig mConfig;
    private int screenWidth;

    public SingleImgAdapterV2(Context context, List<ImageEntity> data,BuildConfig config) {
        this.mContext = context;
        this.pics = data;
        mConfig = config;
        screenWidth = DeviceInfoUtil.getDeviceWidth(mContext);
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
            viewHolder.miv_img.setOnClickListener(v -> {
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) toStringArray();
                bigImgEntity.index = position;
                LookBigImgAct.startAct(mContext, bigImgEntity);
            });
        } else {
            GlideUtils.getInstance().loadLocalImageWithView(mContext, R.mipmap.img_tupian, viewHolder.miv_img);
            viewHolder.miv_del.setVisibility(View.GONE);
            viewHolder.miv_img.setOnClickListener(v -> {
                SelectPicVideoAct.startAct(mContext,mConfig.max_count);
            });
        }
        viewHolder.miv_del.setOnClickListener(v -> {
            ImageEntity imageEntity = pics.get(position);
            pics.remove(imageEntity);
            notifyDataSetChanged();
        });
        if (pics != null && pics.size() < mConfig.max_count){
            viewHolder.mtv_max.setVisibility(View.VISIBLE);
            viewHolder.mtv_max.setText("(最多"+mConfig.max_count+"张)");
        }else {
            viewHolder.mtv_max.setVisibility(View.GONE);
        }
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
            miv_img = view.findViewById(R.id.miv_img);
            miv_del = view.findViewById(R.id.miv_img_del);
            rl_img = view.findViewById(R.id.layout_img);
            mtv_max = view.findViewById(R.id.mtv_max);

            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screenWidth - TransformUtil.dip2px(mContext, 20 + (4 * 4))) / 5;
            layoutParams.height = layoutParams.width;
            rl_img.setLayoutParams(layoutParams);
        }
    }


    public static class BuildConfig{
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
