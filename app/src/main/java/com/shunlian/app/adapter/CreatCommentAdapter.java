package com.shunlian.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.shunlian.app.ui.my_comment.CreatCommentActivity.APPEND_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CHANGE_COMMENT;
import static com.shunlian.app.ui.my_comment.CreatCommentActivity.CREAT_COMMENT;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentAdapter extends BaseAdapter {

    private int commentType;
    private List<ReleaseCommentEntity> lists;
    private OnCommentChangeCallBack mCallBack;
    private HashMap<Integer, SingleImgAdapter> mAdapters;
    private Context mContext;
//    public void updateProgress(int p1, String tag, int progress) {
//        if (mAdapters.size() != 0) {
//            SingleImgAdapter imgAdapter = mAdapters.get(p1);
//            imgAdapter.updateItemProgress(tag, progress);
//        }
//    }

    public CreatCommentAdapter(Context context, List<ReleaseCommentEntity> list, int type) {
        this.commentType = type;
        this.mContext = context;
        this.lists = list;
        mAdapters = new HashMap<>();
    }

    public void addImages(List<ImageEntity> pathes, int position) {
        if (mAdapters.size() != 0) {
            SingleImgAdapter imgAdapter = mAdapters.get(position);

//            List<ImageEntity> imageEntityList;
//            if (lists.get(position).imgs == null) {
//                imageEntityList = new ArrayList<>();
//                imageEntityList.addAll(pathes);
//                lists.get(position).imgs = imageEntityList;
//            } else {
            lists.get(position).imgs = pathes;
//            }
            imgAdapter.setData(pathes);
        }
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public ReleaseCommentEntity getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyImageView miv_comment_icon;
        MyImageView miv_select;
        TextView tv_comment_title;
        TextView tv_comment_price;
        LinearLayout ll_comment_high;
        LinearLayout ll_comment_middle;
        LinearLayout ll_comment_low;
        RelativeLayout layout_anonymous;
        final MyImageView miv_comment_high;
        final MyImageView miv_comment_middle;
        final MyImageView miv_comment_low;
        GridView recycler_comment;
        final EditText edt_comment;
        LinearLayout ll_comment_score;
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_creat_comment, parent, false);

        tv_comment_title = (TextView) convertView.findViewById(R.id.tv_comment_title);
        tv_comment_price = (TextView) convertView.findViewById(R.id.tv_comment_price);
        ll_comment_high = (LinearLayout) convertView.findViewById(R.id.ll_comment_high);
        ll_comment_middle = (LinearLayout) convertView.findViewById(R.id.ll_comment_middle);
        ll_comment_low = (LinearLayout) convertView.findViewById(R.id.ll_comment_low);
        miv_comment_icon = (MyImageView) convertView.findViewById(R.id.miv_comment_icon);
        miv_select = (MyImageView) convertView.findViewById(R.id.miv_select);
        miv_comment_high = (MyImageView) convertView.findViewById(R.id.miv_comment_high);
        miv_comment_middle = (MyImageView) convertView.findViewById(R.id.miv_comment_middle);
        miv_comment_low = (MyImageView) convertView.findViewById(R.id.miv_comment_low);
        recycler_comment = (GridView) convertView.findViewById(R.id.recycler_comment);
        edt_comment = (EditText) convertView.findViewById(R.id.edt_comment);
        ll_comment_score = (LinearLayout) convertView.findViewById(R.id.ll_comment_score);
        layout_anonymous = convertView.findViewById(R.id.layout_anonymous);

        final ReleaseCommentEntity data = lists.get(position);
        GlideUtils.getInstance().loadImage(mContext, miv_comment_icon, data.pic);
        tv_comment_title.setText(data.title);
        tv_comment_price.setText(mContext.getString(R.string.common_yuan) + data.price);

        SingleImgAdapter singleImgAdapter;
        List<ImageEntity> list;
        if (data.imgs == null) {
            list = new ArrayList<>();
        } else {
            list = data.imgs;
        }
        singleImgAdapter = new SingleImgAdapter(mContext, list, position);
        recycler_comment.setAdapter(singleImgAdapter);
        mAdapters.put(position, singleImgAdapter);

        if (commentType == APPEND_COMMENT) {
            ll_comment_score.setVisibility(View.GONE);
            layout_anonymous.setVisibility(View.GONE);
        } else if (commentType == CREAT_COMMENT) {
            ll_comment_score.setVisibility(View.VISIBLE);
            layout_anonymous.setVisibility(View.VISIBLE);

            if (data.anonymous == 0) {
                miv_select.setImageResource(R.mipmap.img_niming_n);
            } else {
                miv_select.setImageResource(R.mipmap.img_niming_h);
            }
        } else if (commentType == CHANGE_COMMENT) {
            ll_comment_middle.setEnabled(false);
            ll_comment_low.setEnabled(false);
            ll_comment_score.setVisibility(View.VISIBLE);
            layout_anonymous.setVisibility(View.GONE);
        }

        edt_comment.setText(data.content);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    data.content = "";
                } else {
                    data.content = edt_comment.getText().toString();
                }
                if (mCallBack != null) {
                    mCallBack.OnComment(edt_comment.getText().toString(), position);
                }
            }
        };
        edt_comment.addTextChangedListener(watcher);
        if (TextUtils.isEmpty(data.starLevel)) {
            data.starLevel = "5";
        }
        miv_comment_high.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_haoping_n));
        miv_comment_middle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_zhongping_n));
        miv_comment_low.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_chaping_n));
        switch (data.starLevel) {
            case "1":
                miv_comment_low.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_chaping_h));
                break;
            case "3":
                miv_comment_middle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_zhongping_h));
                break;
            case "5":
                miv_comment_high.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_haoping_h));
                break;
        }
        ll_comment_high.setOnClickListener(v -> {
            miv_comment_high.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_haoping_h));
            miv_comment_middle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_zhongping_n));
            miv_comment_low.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_chaping_n));
            if (mCallBack != null) {
                mCallBack.OnCommentLevel("5", position);
            }
        });

        ll_comment_middle.setOnClickListener(v -> {
            miv_comment_high.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_haoping_n));
            miv_comment_middle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_zhongping_h));
            miv_comment_low.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_chaping_n));
            if (mCallBack != null) {
                mCallBack.OnCommentLevel("3", position);
            }
        });

        ll_comment_low.setOnClickListener(v -> {
            miv_comment_high.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_haoping_n));
            miv_comment_middle.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_zhongping_n));
            miv_comment_low.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_chaping_h));
            if (mCallBack != null) {
                mCallBack.OnCommentLevel("1", position);
            }
        });

        edt_comment.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        //返回键扩大点击范围
        int i = TransformUtil.dip2px(mContext, 20);
        TransformUtil.expandViewTouchDelegate(miv_select, i, i, i, i);

        miv_select.setOnClickListener(v -> {
            if (0 == data.anonymous) {
                miv_select.setImageResource(R.mipmap.img_niming_h);
                data.anonymous = 1;
            } else {
                miv_select.setImageResource(R.mipmap.img_niming_n);
                data.anonymous = 0;
            }
            mCallBack.OnCommentAnonymous(data.anonymous, position);
        });

        return convertView;
    }

    public void setOnCommentChangeCallBack(OnCommentChangeCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnCommentChangeCallBack {
        void OnComment(String content, int position);

        void OnCommentLevel(String level, int position);

        void OnCommentAnonymous(int anonymous, int position);
    }
}
