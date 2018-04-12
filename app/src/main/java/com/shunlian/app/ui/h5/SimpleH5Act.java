package com.shunlian.app.ui.h5;

import android.content.Context;
import android.content.Intent;

import com.shunlian.app.bean.H5CallEntity;

/**
 * Created by Administrator on 2018/4/4.
 */

public class SimpleH5Act extends H5Act {


    public static void startAct(Context context, String url, int mode) {
        Intent intentH5 = new Intent(context, SimpleH5Act.class);
        intentH5.putExtra("url", url);
        intentH5.putExtra("mode", mode);
        context.startActivity(intentH5);
    }
    /**
     * 布局id
     *
     * @param h5CallEntity
     * @return
     */
    @Override
    protected void jsCallback(H5CallEntity h5CallEntity) {

    }
}
