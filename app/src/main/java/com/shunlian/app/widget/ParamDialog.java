package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ParamDialog extends Dialog {
    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.recycler_param)
    RecyclerView recycler_param;

    public ParamDialog(Context context) {
        super(context);
    }

    public ParamDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_param);
    }
}
