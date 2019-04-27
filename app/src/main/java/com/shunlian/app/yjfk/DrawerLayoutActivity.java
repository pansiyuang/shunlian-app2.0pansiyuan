//package com.shunlian.app.yjfk;
//
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import com.shunlian.app.R;
//import com.shunlian.app.ui.BaseActivity;
//
///**
// * Created by Administrator on 2019/4/23.
// */
//
//public class DrawerLayoutActivity extends BaseActivity {
//    @Override
//    protected int getLayoutId() {
//        return R.layout.drawerlayout11 ;
//    }
//
//    @Override
//    protected void initData() {
//        Toolbar toolbar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 8,R.string.close){
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                getActionBar().setTitle("aaaaaa");
//                invalidateOptionsMenu(); // creates call to
//                // onPrepareOptionsMenu()
//            }
//
//        };
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//    }
//
//}
