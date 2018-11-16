package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/11/7.
 */

public class SaveImgDialog extends Dialog {
    @BindView(R.id.miv_share_wechat)
    MyImageView miv_wechat;

    @BindView(R.id.tv_cancel)
    TextView tv_cancel;

    private Unbinder bind;
    private Activity act;

    public SaveImgDialog(Activity activity) {
        this(activity, R.style.popAd);
        this.act = activity;
        initView();
    }

    public SaveImgDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_save_imgs, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);
        setCancelable(false);

        miv_wechat.setOnClickListener((v) -> {
            Common.openWeiXin(act, "", "");
            dismiss();
        });
        tv_cancel.setOnClickListener(v -> dismiss());
    }

    public void destory() {
        if (isShowing()) {
            dismiss();
        }
        if (bind != null) {
            bind.unbind();
        }
    }
}
