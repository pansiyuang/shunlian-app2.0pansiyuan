package com.shunlian.app.ui.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;



public class DiscoverSucaikuFrag extends DiscoversFrag {


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_sucaiku,null,false);
    }

    @Override
    protected void initData() {

    }


    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }
}
