package com.shunlian.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


//                            _ooOoo_
//                           o8888888o
//                           88" . "88
//                           (| -_- |)
//                            O\ = /O
//                        ____/`---'\____
//                        . ' \\| |// `.
//                       / \\||| : |||// \
//                     / _||||| -:- |||||- \
//                       | | \\\ - /// | |
//                     | \_| ''\---/'' | |
//                      \ .-\__ `-` ___/-. /
//                   ___`. .' /--.--\ `. . __
//                ."" '< `.___\_<|>_/___.' >'"".
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |
//                 \ \ `-. \_ __\ /__ _/ .-` / /
//         ======`-.____`-.___\_____/___.-`____.-'======
//                            `=---='
//
//         .............................................
//                佛祖保佑                 永无BUG

/**
 * Created by zhang on 2017/6/22 14 : 47.
 */

public abstract class BaseFragment extends Fragment {


    protected Activity baseActivity;
    protected Context baseContext;
    private Unbinder bind;
    private Resources resources;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        baseContext = getActivity();
        resources = baseContext.getResources();
        View layoutId = getLayoutId(inflater, container);
        return layoutId;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initViews();
        initListener();
        initData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.baseActivity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.baseActivity = null;
    }

    /**
     * 设置布局id
     *
     * @param inflater
     * @param container
     * @return
     */
    protected abstract View getLayoutId(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化所有控件
     */
    protected void initViews() {
    }

    /**
     * 初始化监听器
     */
    protected void initListener() {

    }

    /**
     * 初始化数据
     */
    protected abstract void initData();


    /**
     * 显示view
     *
     * @param views
     */
    protected void visible(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * 隐藏view
     *
     * @param views
     */
    protected void gone(View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void setEdittextFocusable(boolean focusable,EditText... editText){
        for (int i = 0; i < editText.length; i++) {
            editText[i].setFocusable(focusable);
            editText[i].setFocusableInTouchMode(focusable);
            if (focusable){
                editText[i].requestFocus();
            }
        }
    }

    /**
     * 获取文字资源
     *
     * @param stringResouce
     */
    protected String getStringResouce(int stringResouce) {
        return resources.getString(stringResouce);
    }

    /**
     * 获取颜色资源
     *
     *  @param colorResouce
     */
    protected int getColorResouce(int colorResouce) {
        return resources.getColor(colorResouce);
    }

    /**
     * 获取图片资源
     *
     *  @param drawableResouce
     */
    protected Drawable getDrawableResouce(int drawableResouce) {
        return resources.getDrawable(drawableResouce);
    }

    /**
     * 判断集合内容是否为空
     * @param list
     * @return
     */
    protected boolean isEmpty(List list){
        if (list == null){
            return true;
        }

        if (list.size() == 0){
            return true;
        }else {
            return false;
        }
    }
    protected boolean isEmpty(CharSequence sequence){
        return TextUtils.isEmpty(sequence);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
    }
}
