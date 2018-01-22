package com.shunlian.app.ui.collection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;

/**
 * Created by Administrator on 2018/1/22.
 * 足迹
 */

public class FootprintFrag extends CollectionFrag {


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_footprint,null,false);
    }

    @Override
    protected void initData() {

    }

    /**
     * 删除
     */
    @Override
    public void delete() {

    }

    /**
     * 选择所有
     */
    @Override
    public void selectAll() {

    }

    /**
     * 操作管理
     */
    @Override
    public void operationMange() {

    }

    /**
     * 完成管理
     */
    @Override
    public void finishManage() {

    }
}
