package com.shunlian.app.newchat.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseLazyFragment;

/**
 * Created by Administrator on 2018/4/8.
 */

public class LittleStoreFragment extends BaseLazyFragment {

    public static LittleStoreFragment getInstance() {
        LittleStoreFragment littleStoreFragment = new LittleStoreFragment();
        return littleStoreFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        return view;
    }

    @Override
    protected void initData() {

    }
}
