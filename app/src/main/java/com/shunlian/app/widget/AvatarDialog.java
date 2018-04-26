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

public class AvatarDialog extends Dialog implements View.OnClickListener {

    @BindView(R.id.mtv_photo)
    MyTextView mtv_photo;

    @BindView(R.id.mtv_album)
    MyTextView mtv_album;

    @BindView(R.id.mtv_cancel)
    MyTextView mtv_cancel;


    private Unbinder bind;
    private OnClickListener mListener;


    public AvatarDialog(@NonNull Context context) {
        this(context, R.style.MyDialogStyleBottom);
        initView();
    }


    public AvatarDialog(@NonNull Context context, int themeResId) {
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
                .inflate(R.layout.dialog_avatar, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        mtv_photo.setOnClickListener(this);
        mtv_album.setOnClickListener(this);
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
            case R.id.mtv_photo:
                if (mListener != null){
                    mListener.onClick(1);
                }
                dismiss();
                break;
            case R.id.mtv_album:
                if (mListener != null){
                    mListener.onClick(2);
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
        void onClick(int id);
    }
}
