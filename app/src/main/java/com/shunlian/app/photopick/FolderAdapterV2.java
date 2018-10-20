package com.shunlian.app.photopick;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件夹Adapter
 */
public class FolderAdapterV2 extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    private List<FolderV2> mFolders = new ArrayList<>();

    int lastSelected = 0;

    public FolderAdapterV2(Context context) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 设置数据集
     *
     * @param folders
     */
    public void setData(List<FolderV2> folders) {
        if (folders != null && folders.size() > 0) {
            mFolders = folders;
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolders.size();
    }


    @Override
    public Object getItem(int position) {
        return mFolders.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = mInflater.inflate(R.layout.item_folder, viewGroup, false);
            holder = new ViewHolder(view);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (holder != null) {
            holder.bindData(mFolders.get(i));
            if (lastSelected == i) {
                holder.indicator.setVisibility(View.VISIBLE);
            } else {
                holder.indicator.setVisibility(View.INVISIBLE);
            }
        }
        return view;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) return;
        lastSelected = i;
        notifyDataSetChanged();
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    class ViewHolder {
        ImageView cover;
        TextView name;
        TextView size;
        ImageView indicator;

        ViewHolder(View view) {
            cover = view.findViewById(R.id.cover);
            cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            name = view.findViewById(R.id.name);
            size = view.findViewById(R.id.size);
            indicator = view.findViewById(R.id.indicator);
            view.setTag(this);
        }

        void bindData(FolderV2 data) {
            name.setText(data.name);
            size.setText(data.images.size() + "张");
            if (data.cover.videoDuration != 0){
                if (TextUtils.isEmpty(data.cover.coverPath)){
                    cover.setImageBitmap(data.cover.coverBitmap);
                }else {
                    loadImage(data.cover.coverPath);
                }
            }else {
                loadImage(data.cover.path);
            }
        }

        private void loadImage(String url) {
            // 显示图片
            GlideUtils.getInstance().loadOverrideImage(mContext,cover,url,144,144);
        }
    }

    public void destory(){
        if (mFolders != null){
            mFolders.clear();
            mFolders = null;
        }
    }
}
