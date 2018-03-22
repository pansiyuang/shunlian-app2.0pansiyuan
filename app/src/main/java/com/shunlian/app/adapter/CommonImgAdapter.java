package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/22.
 */

public class CommonImgAdapter extends BaseRecyclerAdapter<ImageEntity> {
    public static final int REQUEST_CAMERA_CODE = 1001;
    private static final int TYPE_CAMERA = 1003;
    public int MAX_SIZE = 5;
    private PhotoPickerIntent intent;
    private List<ImageEntity> mList;
    private int picWidth;

    public CommonImgAdapter(Context context, List<ImageEntity> lists, int maxSize) {
        super(context, false, lists);
        this.MAX_SIZE = maxSize;
        this.mList = lists;
        intent = new PhotoPickerIntent(context);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true);
    }

    public void setData(List<ImageEntity> data) {
        mList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mList.size() < MAX_SIZE) {
            return mList.size() + 1;// 注意这里,这里会做判断,若图片集合大小小于最多显示几张那么就让count+1
        } else {
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddItem(position)) {
            return TYPE_CAMERA;
        }
        return super.getItemViewType(position);
    }

    private boolean isShowAddItem(int position) {
        int size = mList.size() == 0 ? 0 : mList.size();// 第0个位置,集合为0 返回为true; 第一个位置position=1,list.size=3,size=3 返回false
        return position == size;
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CAMERA:
                return new AddViewHolder(LayoutInflater.from(context).inflate(R.layout.img_common_add, parent, false));
            default:
                return new ImagViewholder(LayoutInflater.from(context).inflate(R.layout.item_common_img, parent, false));
        }
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case TYPE_CAMERA:
                handlerAdd(holder);
                break;
            default:
                handlerItem(holder, position);
                break;
        }
    }

    public List<String> toStringArray() {
        List<String> result = new ArrayList<>();
        for (ImageEntity imageEntity : mList) {
            if (!TextUtils.isEmpty(imageEntity.imgPath)) {
                result.add(imageEntity.imgPath);
            } else if (!TextUtils.isEmpty(imageEntity.imgUrl)) {
                result.add(imageEntity.imgUrl);
            }
        }
        return result;
    }

    public void handlerItem(RecyclerView.ViewHolder holder, final int position) {
        final ImagViewholder viewHolder = (ImagViewholder) holder;

        String imgPath = mList.get(position).imgPath;
        String imgUrl = mList.get(position).imgUrl;
        if (!TextUtils.isEmpty(imgPath)) {
            GlideUtils.getInstance().loadFileImageWithView(context, new File(imgPath), viewHolder.miv_img);
        } else if (!TextUtils.isEmpty(imgUrl)) {
            GlideUtils.getInstance().loadImage(context, viewHolder.miv_img, imgUrl);
        }
        viewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BigImgEntity bigImgEntity = new BigImgEntity();
                bigImgEntity.itemList = (ArrayList<String>) toStringArray();
                bigImgEntity.index = position;
                LookBigImgAct.startAct(context, bigImgEntity);
            }
        });
        viewHolder.miv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageEntity imageEntity = mList.get(position);
                mList.remove(imageEntity);
                notifyDataSetChanged();
            }
        });
    }

    public void handlerAdd(RecyclerView.ViewHolder holder) {
        final AddViewHolder addViewHolder = (AddViewHolder) holder;
        addViewHolder.tv_max_count.setText(String.format(getString(R.string.max_pic_size), MAX_SIZE));
        addViewHolder.miv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setMaxTotal(MAX_SIZE - mList.size()); // 最多选择照片数量，默认为9
                ((Activity) context).startActivityForResult(intent, REQUEST_CAMERA_CODE);
            }
        });
    }

    public class ImagViewholder extends BaseRecyclerViewHolder {

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.miv_img_del)
        MyImageView miv_del;

        public ImagViewholder(View itemView) {
            super(itemView);
            miv_img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (picWidth != 0) {
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) miv_img.getLayoutParams();
                        params.height = params.width = picWidth;
                        miv_img.setLayoutParams(params);
                    }
                }
            });
        }
    }

    public class AddViewHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.rl_rootView)
        RelativeLayout rl_rootView;

        @BindView(R.id.miv_add)
        MyImageView miv_add;

        @BindView(R.id.tv_max_count)
        TextView tv_max_count;

        public AddViewHolder(View itemView) {
            super(itemView);
            rl_rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    picWidth = rl_rootView.getWidth();
                    RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) rl_rootView.getLayoutParams();
                    params.height = picWidth;
                    rl_rootView.setLayoutParams(params);
                }
            });
        }
    }
}
