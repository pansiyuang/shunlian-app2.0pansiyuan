package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class AttributeDialog extends Dialog {


    public AttributeDialog(Context context) {
        super(context);
    }

    public AttributeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_goods_select);
    }
}
