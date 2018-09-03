package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.utils.TransformUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shunlian.app.utils.DeviceInfoUtil.getDeviceWidth;

/**
 * Created by Administrator on 2018/9/3.
 */

public class TurnTableDialog extends Dialog {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private Context mContext;

    public TurnTableDialog(Context context) {
        this(context, R.style.Mydialog);
        this.mContext = context;
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_turntable, null, false);
        setContentView(rootView);
        ButterKnife.bind(this, rootView);
        initViews();
    }

    public TurnTableDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    private void initViews() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = getDeviceWidth(mContext) - TransformUtil.dip2px(mContext, 90);
        win.setAttributes(lp);

        miv_close.setOnClickListener(view -> {
            if (isShowing()) {
                dismiss();
            }
        });
    }
}
