package com.shunlian.app.shunlianyoupin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.mylibrary.ImmersionBar;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/4.
 */

public class ShunlianyoupinPersonFrag extends MyX5Frag {
    public static BaseFragment getInstance(String h5Url, int mode, String loginVerion) {
        ShunlianyoupinPersonFrag fragment = new ShunlianyoupinPersonFrag();
        Bundle args = new Bundle();
        try {
            if (!TextUtils.isEmpty(h5Url))
                h5Url = java.net.URLDecoder.decode(h5Url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        args.putSerializable("h5Url", h5Url);
        args.putSerializable("mode", mode);
        args.putSerializable("loginVerion", loginVerion);
        fragment.setArguments(args);
        return fragment;
    }
    public void onBack() {
        mwv_h5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mwv_h5.canGoBack()) {
                        mwv_h5.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
