package com.shunlian.app.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.TeamIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.zh.chartlibrary.common.DensityUtil;

import java.lang.ref.WeakReference;
import java.util.List;

public class AutoPollRecyclerView extends RecyclerView {
    private static final long TIME_AUTO_POLL = 1200;
    AutoPollTask autoPollTask;
    private boolean running; //表示是否正在自动轮询
    private boolean canRun;//表示是否可以自动轮询
    private static Context context;
    private final int mTouchSlop;
    private static boolean isAnim = false;
    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        autoPollTask = new AutoPollTask(this);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        isAnim = false;
    }

    static class AutoPollTask implements Runnable {
        private final WeakReference<AutoPollRecyclerView> mReference;

        //使用弱引用持有外部类引用->防止内存泄漏
        public AutoPollTask(AutoPollRecyclerView reference) {
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
            isAnim = false;
        }

        @Override
        public void run() {
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastItem = layoutManager.findLastVisibleItemPosition();
                recyclerView.smoothScrollBy(0, DensityUtil.dip2px(context,35));
                recyclerView.postDelayed(recyclerView.autoPollTask, TIME_AUTO_POLL);
                Log.d("layoutManage","layoutManager.getChildCount():"+layoutManager.getChildCount());
                layoutManager.getChildAt(1).setAlpha(0.36f);
                layoutManager.getChildAt(2).setAlpha(0.66f);
                isAnim = true;
//                if(layoutManager.findViewByPosition(lastItem)!=null)

                new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutManager.getChildAt(0).setAlpha(1.0f);
                }
              }, 150);
                isAnim = false;
            }
        }
    }

    //开启:如果正在运行,先停止->再开启
    public void start() {
        if (running)
            stop();
        canRun = true;
        running = true;
        isAnim = false;
        postDelayed(autoPollTask, TIME_AUTO_POLL);
    }

    public void stop() {
        running = false;
        if(autoPollTask!=null) {
            removeCallbacks(autoPollTask);
        }
        isAnim = false;
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return super.canScrollVertically(direction);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (running)
                    stop();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canRun)
                    start();
                break;
        }
        return super.onTouchEvent(e);
    }

    public static class AutoPollAdapter extends RecyclerView.Adapter<AutoPollAdapter.BaseViewHolder> {
        private final List<TeamIndexEntity.EggHistory> mData;
        private    AutoPollRecyclerView recyclerView;
        public AutoPollAdapter(List<TeamIndexEntity.EggHistory> eggHistories, AutoPollRecyclerView recyclerView) {
            this.mData = eggHistories;
            this.recyclerView = recyclerView;
        }

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_poll, parent, false);
            BaseViewHolder holder = new BaseViewHolder(view);
            Log.d("view","view:"+viewType);
            return holder;
        }

        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            if(mData!=null&&mData.size()>0) {
                holder.mllayout_root.setVisibility(VISIBLE);
                String data = mData.get(position % mData.size()).nickname+"+"+mData.get(position % mData.size()).egg+"金蛋";
                if(!TextUtils.isEmpty(mData.get(position % mData.size()).avatar)&&context!=null&&!((Activity)context).isFinishing()){
                    GlideUtils.getInstance().loadCircleImage(context,holder.image_head,mData.get(position % mData.size()).avatar);
                }else{
                    holder.image_head.setImageResource(R.mipmap.img_set_defaulthead);
                }
                holder.tv.setText(data);
            }else{
                holder.mllayout_root.setVisibility(GONE);
            }
//            if(position>2&&isAnim) {
//               recyclerView.getLayoutManager().findViewByPosition(position).setAlpha(1.0f);
//            }
        }

        @Override
        public int getItemCount() {
            return Integer.MAX_VALUE;
        }

        class BaseViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            ImageView image_head;
            MyRelativeLayout mllayout_root;
            View view;
            public BaseViewHolder(View itemView) {
                super(itemView);
                setIsRecyclable(false);
                tv = itemView.findViewById(R.id.tv_content);
                image_head= itemView.findViewById(R.id.image_head);
                mllayout_root= itemView.findViewById(R.id.mllayout_root);
            }
        }
    }
}
