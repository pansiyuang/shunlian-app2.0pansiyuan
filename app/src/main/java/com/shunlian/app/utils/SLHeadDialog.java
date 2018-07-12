package com.shunlian.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
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

    @BindView(R.id.rlayout_tip1)
    RelativeLayout rlayout_tip1;

    @BindView(R.id.mtv_tip1)
    MyTextView mtv_tip1;

    @BindView(R.id.rlayout_tip2)
    RelativeLayout rlayout_tip2;

    @BindView(R.id.mtv_tip2)
    MyTextView mtv_tip2;

    @BindView(R.id.mbtn_close)
    MyButton mbtn_close;

    @BindView(R.id.miv_head)
    MyImageView miv_head;

    @BindView(R.id.llayout_content)
    LinearLayout llayout_content;

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
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        window.setAttributes(lp);


        llayout_content.post(()->{
            int measuredHeight = miv_head.getMeasuredHeight();
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    llayout_content.getLayoutParams();
            layoutParams.topMargin = measuredHeight-TransformUtil.dip2px(context,8);
        });

        if (mTips != null && mTips.size() > 0){
            if (!TextUtils.isEmpty(mTips.get(0))){
                rlayout_tip1.setVisibility(View.VISIBLE);
                mtv_tip1.setText(Common.getPlaceholder(2)+mTips.get(0));
            }else {
                rlayout_tip1.setVisibility(View.GONE);
            }

            if (mTips.size() == 2 && !TextUtils.isEmpty(mTips.get(1))){
                rlayout_tip2.setVisibility(View.VISIBLE);
                mtv_tip2.setText(Common.getPlaceholder(2)+mTips.get(1));
            }else {
                rlayout_tip2.setVisibility(View.GONE);
            }
        }
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
}
