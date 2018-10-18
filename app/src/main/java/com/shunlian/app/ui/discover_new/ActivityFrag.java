package com.shunlian.app.ui.discover_new;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.BaseLazyFragment;

/**
 * Created by Administrator on 2018/10/15.
 */

public class ActivityFrag extends BaseLazyFragment {
    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.activity_frag, null, false);
        return view;
    }

    @Override
    protected void initData() {

    }
}
