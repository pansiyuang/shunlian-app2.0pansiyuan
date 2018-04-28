package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.widget.pick_time.PickTimeView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/23.
 */

public class SelectDateDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;

    @BindView(R.id.mtv_sure)
    MyTextView mtv_sure;

    @BindView(R.id.ptv)
    PickTimeView ptv;

    private Unbinder bind;
    private OnClickListener mListener;
    private String date;


    public SelectDateDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyleBottom);
        initView();
    }


    public SelectDateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView(){
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);

        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.item_select_date, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);
        ptv.setViewType(PickTimeView.TYPE_PICK_DATE);
        mtv_sure.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        ptv.setOnSelectedChangeListener((v,timeMillis)->
            date = sdfDate.format(timeMillis)
        );
    }


    public void destory(){
        if (isShowing()) {
            dismiss();
        }
        if (bind != null) {
            bind.unbind();
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mtv_sure:
                if (mListener != null){
                    mListener.onClick(date);
                }
                dismiss();
                break;
            case R.id.mtv_cancel:
                dismiss();
                break;
        }
    }

    public void setOnClickListener(OnClickListener listener){
        mListener = listener;
    }

    public interface OnClickListener{
        void onClick(String date);
    }
}
