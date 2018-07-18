package com.shunlian.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.widget.MyButton;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zhanghe on 2018/7/12.
 */

public class SLHeadDialog extends Dialog {

    @BindView(R.id.mbtn_close)
    MyButton mbtn_close;

    @BindView(R.id.miv_head)
    MyImageView miv_head;

    @BindView(R.id.llayout_content)
    LinearLayout llayout_content;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    private Unbinder unbinder;
    private List<String> mTips;

    public SLHeadDialog(@NonNull Context context, List<String> tips) {
        this(context,R.style.MyDialogStyleBottom,tips);
        mTips = tips;
    }


    public SLHeadDialog(@NonNull Context context, int themeResId,List<String> tips) {
        super(context, themeResId);
        mTips = tips;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_slhead, null);
        setContentView(view);
        unbinder = ButterKnife.bind(this, view);


        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        llayout_content.post(()->{
            int measuredHeight = miv_head.getMeasuredHeight();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    llayout_content.getLayoutParams();
            layoutParams.topMargin = measuredHeight-TransformUtil.dip2px(context,8);
        });

        LinearLayoutManager manager = new LinearLayoutManager(context);
        recy_view.setLayoutManager(manager);

        recy_view.setAdapter(new ContentAdapter(context,mTips));
    }


    @OnClick(R.id.mbtn_close)
    public void close(){
        dismiss();
    }


    public void detachView(){
        if (isShowing()){
            dismiss();
            if (unbinder != null)unbinder.unbind();
            if (mTips != null){
                mTips.clear();
                mTips = null;
            }
        }
    }


    class ContentAdapter extends BaseRecyclerAdapter<String>{

        public ContentAdapter(Context context, List<String> lists) {
            super(context, false, lists);
        }

        /**
         * 子类需要实现的holder
         *
         * @param parent
         * @return
         */
        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            View view = mInflater.inflate(R.layout.item_sale_content, parent, false);
            return new ContentHolder(view);
        }

        /**
         * 处理列表
         *
         * @param holder
         * @param position
         */
        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ContentHolder){
                ContentHolder mHolder = (ContentHolder) holder;
                mHolder.mtv_tip2.setText(Common.getPlaceholder(2)+lists.get(position));
            }
        }

        class ContentHolder extends BaseRecyclerAdapter.BaseRecyclerViewHolder{

            @BindView(R.id.mtv_tip2)
            MyTextView mtv_tip2;

            public ContentHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
