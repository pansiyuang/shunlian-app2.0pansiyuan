//package com.shunlian.app.shunlianyoupin;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.plus.app.R;
//import com.plus.app.bean.H5CallEntity;
//import com.plus.app.ui.BaseFragment;
//import com.plus.app.ui.h5.H5X5Frag;
//import com.plus.app.utils.LogUtil;
//import com.plus.app.utils.SharedPrefUtil;
//import com.plus.mylibrary.ImmersionBar;
//
///**
// * Created by Administrator on 2017/12/26.
// */
//
//public class MTH5MyStoreFrag extends H5X5Frag {
//    private String loginVerion="0";
//
//    public static BaseFragment getInstance(String h5Url, int mode, String loginVerion) {
//        MTH5MyStoreFrag fragment = new MTH5MyStoreFrag();
//        Bundle args = new Bundle();
//        try {
//            if (!TextUtils.isEmpty(h5Url))
//                h5Url = java.net.URLDecoder.decode(h5Url);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        args.putSerializable("h5Url", h5Url);
//        args.putSerializable("mode", mode);
//        args.putSerializable("loginVerion", loginVerion);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    protected void jsCallback(H5CallEntity h5CallEntity) {
//
//    }
//
//    @Override
//    protected void initData() {
//        super.initData();
//        loginVerion = (String) getArguments().getSerializable("loginVerion");
//    }
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            ImmersionBar.with(this).fitsSystemWindows(true)
//                    .statusBarColor(R.color.white)
//                    .statusBarDarkFont(true, 0.2f)
//                    .init();
//            if (!loginVerion.equals(SharedPrefUtil.getSharedUserString("loginVerion","0"))) {
//                mengtianRefresh();
//                loginVerion = SharedPrefUtil.getSharedUserString("loginVerion","0");
//                LogUtil.augusLogW("dfdf--dddd");
//            }
//
//        }
//    }
//
//    @Override
//    public void onBack() {
//        super.onBack();
//        ((MTFirstAct) activity).mainPageClick();
//    }
//
//}
