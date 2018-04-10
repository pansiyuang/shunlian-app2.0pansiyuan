package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.StatusEntity;
import com.shunlian.app.newchat.entity.UserInfoEntity;
import com.shunlian.app.newchat.util.TimeUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class ChatMemberAdapter extends BaseAdapter {
    private List<UserInfoEntity.Info.Friend> members;
    private Context mContext;
    private Paint paint;
    private ListView mListView;

    public ChatMemberAdapter(ListView listView, List<UserInfoEntity.Info.Friend> list, Context context) {
        this.members = list;
        this.mContext = context;
        this.mListView = listView;
        paint = new Paint();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void updataItem(BaseMessage item) {
        if (members == null) {
            return;
        }
        if (members.size() == 0) {
            return;
        }
        for (int i = 0; i < members.size(); i++) {
            if (item.from_join_id.equals(members.get(i).uid)) {
                members.get(i).unReadNum = item.getuReadNum();
                members.get(i).headurl = item.from_headurl;
                members.get(i).nickname = item.from_nickname;
                members.get(i).update_time = String.valueOf(item.getSendTime());
                notifyDataSetChanged(mListView, i);
                break;
            }
        }
    }

    public void updateItemId(String userId) {
        if (members == null) {
            return;
        }
        if (members.size() == 0) {
            return;
        }
        for (int i = 0; i < members.size(); i++) {
            if (userId.equals(members.get(i).uid)) {
                members.get(i).unReadNum = 0;
                notifyDataSetChanged(mListView, i);
                break;
            }
        }
    }


    public void updateItemStatus(StatusEntity statusEntity) {
        for (int i = 0; i < members.size(); i++) {
            if (statusEntity.id.equals(members.get(i).uid)) {
                if ("logout".equals(statusEntity.message_type)) {
                    members.get(i).line_status = "5";
                    LogUtil.httpLogW(members.get(i).nickname + "下线啦");
                } else if ("online".equals(statusEntity.message_type)) {
                    LogUtil.httpLogW(members.get(i).nickname + "上线啦");
                    members.get(i).line_status = "1";
                }
                notifyDataSetChanged(mListView, i);
                break;
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_chatlist, parent, false);
            holderView = new HolderView(convertView);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        final UserInfoEntity.Info.Friend friend = members.get(position);
//        DIOU.imageLoader.displayImage(friend.headurl, holderView.mIcon, DIOU.userPortraitImageOption, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String s, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//            }
//
//            @Override
//            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                switch (friend.line_status) {
//                    //在线状态：1在线，2空闲，3离开，4隐身，5离线
//                    case "1":
//                    case "2":
//                    case "3":
//                        setMemberLineStatus(true, bitmap, (RoundedImageView) view);
//                        break;
//                    case "4":
//                    case "5":
//                        setMemberLineStatus(false, bitmap, (RoundedImageView) view);
//                        break;
//                }
//            }
//
//            @Override
//            public void onLoadingCancelled(String s, View view) {
//
//            }
//        });
        holderView.mName.setText(friend.nickname);
        if (!friend.update_time.isEmpty()) {
            Long mDate = Long.valueOf(friend.update_time);
            holderView.mDate.setText(TimeUtil.getChatTimeStr(mDate));
        }
        if (friend.unReadNum != 0) {
            holderView.tv_numbers.setText(String.valueOf(friend.unReadNum));
            holderView.tv_numbers.setVisibility(View.VISIBLE);
        } else {
            holderView.tv_numbers.setVisibility(View.GONE);
        }

        return convertView;
    }

    public class HolderView {
        private MyImageView mIcon;
        private TextView mName;
        private TextView mDate;
        private TextView tv_numbers;

        public HolderView(View view) {
            mIcon = (MyImageView) view.findViewById(R.id.iv_icon);
            mName = (TextView) view.findViewById(R.id.tv_name);
            mDate = (TextView) view.findViewById(R.id.tv_date);
            tv_numbers = (TextView) view.findViewById(R.id.tv_numbers);
        }
    }

    public void setMemberLineStatus(boolean isOnLine, Bitmap bitmap, MyImageView iconView) {
        iconView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        bitmap = grayBitmap(isOnLine, bitmap);
        if (bitmap != null) {
            iconView.setImageBitmap(bitmap);
//            bitmap.recycle();
        }
    }

    public Bitmap grayBitmap(boolean isGray, Bitmap bitmap) {
        Bitmap faceIconGreyBitmap = null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            faceIconGreyBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(faceIconGreyBitmap);
            ColorMatrix colorMatrix = new ColorMatrix();
            if (isGray) {
                colorMatrix.setSaturation(1);
            } else {
                colorMatrix.setSaturation(0);
            }
            ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(colorMatrix);
            paint.setColorFilter(grayColorFilter);
            canvas.drawBitmap(bitmap, 0, 0, paint);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceIconGreyBitmap;
    }

    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param listView 要更新的listview
     * @param position 要更新的位置
     */
    public void notifyDataSetChanged(ListView listView, int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }
    }
}
