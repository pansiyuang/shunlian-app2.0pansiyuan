package com.shunlian.app.widget.tab_strip;


import com.shunlian.app.ui.BaseFragment;

public class ViewPageInfo {
    private String tag;
    private BaseFragment fragment;
    private String title;

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFragment(BaseFragment fragment) {
        this.fragment = fragment;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {

        return tag;
    }

    public BaseFragment getFragment() {
        return fragment;
    }


    public String getTitle() {
        return title;
    }

    public ViewPageInfo(String _title, String _tag, BaseFragment fragment) {
        this.title = _title;
        this.tag = _tag;
        this.fragment = fragment;
    }
}