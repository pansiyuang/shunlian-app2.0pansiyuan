package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.ui.my_comment.CreatCommentActivity;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.ui.returns_order.ReturnRequestActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

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
            GlideUtils.getInstance().loadFileImageWithView(mContext, new File(imgPath), viewHolder.miv_img);
            viewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) toStringArray();
                    bigImgEntity.index = position;
                    LookBigImgAct.startAct(mContext, bigImgEntity);
                }
            });
        } else {
            GlideUtils.getInstance().loadLocalImageWithView(mContext, R.mipmap.img_tupian, viewHolder.miv_img);
            viewHolder.miv_del.setVisibility(View.GONE);
            viewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof CreatCommentActivity) {
                        ((CreatCommentActivity) mContext).openAlbum(parentPosition);
                    } else {
                        intent.setMaxTotal(5 - pics.size()); // 最多选择照片数量，默认为9
                        ((Activity) mContext).startActivityForResult(intent, REQUEST_CAMERA_CODE);
                    }
                }
            });
        }
        viewHolder.miv_del.setOnClickListener(new View.OnClickListener() {
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
            result.add(imageEntity.imgPath);
        }
        return result;
    }

    public class ViewHolder {
        MyImageView miv_img;
        MyImageView miv_del;
        RelativeLayout rl_img;

        public ViewHolder(View view) {
            miv_img = (MyImageView) view.findViewById(R.id.miv_img);
            miv_del = (MyImageView) view.findViewById(R.id.miv_img_del);
            rl_img = (RelativeLayout) view.findViewById(R.id.layout_img);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = (screenWidth - TransformUtil.dip2px(mContext, 20 + (4 * 4))) / 5;
            layoutParams.height = layoutParams.width;
            rl_img.setLayoutParams(layoutParams);
        }
    }
}
