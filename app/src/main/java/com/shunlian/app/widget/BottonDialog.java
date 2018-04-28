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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/4/23.
 */

public class BottonDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.mtv_man)
    MyTextView mtv_man;

    @BindView(R.id.mtv_woman)
    MyTextView mtv_woman;

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;

    @BindView(R.id.mtv_private)
    MyTextView mtv_private;
    private Unbinder bind;
    private OnClickListener mListener;


    public BottonDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyleBottom);
        initView();
    }


    public BottonDialog(@NonNull Context context, int themeResId) {
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
                .inflate(R.layout.dialog_sex_select, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        mtv_man.setOnClickListener(this);
        mtv_woman.setOnClickListener(this);
        mtv_private.setOnClickListener(this);
        mtv_cancel.setOnClickListener(this);
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
            case R.id.mtv_man:
                if (mListener != null){
                    mListener.onClick("男","1");
                }
                dismiss();
                break;
            case R.id.mtv_woman:
                if (mListener != null){
                    mListener.onClick("女","2");
                }
                dismiss();
                break;
            case R.id.mtv_private:
                if (mListener != null){
                    mListener.onClick("秘密","3");
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
        void onClick(String sex,String id);
    }
}
