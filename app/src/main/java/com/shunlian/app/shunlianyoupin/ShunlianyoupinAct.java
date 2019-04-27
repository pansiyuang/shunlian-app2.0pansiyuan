package com.shunlian.app.shunlianyoupin;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.shunlian.app.R;
import com.shunlian.app.photopick.Image;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyScrollView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.IntConsumer;

import butterknife.BindView;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by Administrator on 2019/4/3.
 */

public class ShunlianyoupinAct extends BaseActivity implements IBottomBarClickListener {
    private static final String[] flags = {"Main", "Mystore", "Person"};
    private static Map<String, BaseFragment> fragmentMap = new HashMap<>();
//    @BindView(R.id.mout_youpin)
//    ImageView mout_youpin;
//    @BindView(R.id.iv_main)
//    ImageView iv_main;
//    @BindView(R.id.miv_logo)
//    ImageView miv_logo;
    private FragmentManager fragmentManager;
    private BottomBar mBottomBar;
    private ShunlianyoupinMainFrag shunlianyoupinMainFrag;
    private ShunlianyoupinMystoreFrag shunlianyoupinMystoreFrag;
    private ShunlianyoupinPersonFrag shunlianyoupinPersonFrag;
    private double mExitTime;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ShunlianyoupinAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {

        return R.layout.act_shunlianyoupin;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        fragmentManager = getSupportFragmentManager();
        mainClick();
        mBottomBar = findViewById(R.id.mbottom_bar_youpin);
        mBottomBar.setIBottomBarClickListener(this);
//        mout_youpin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        iv_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                webview = fragmentManager.findFragmentById(R.id.fl_main).getView().findViewById(R.id.webview);
//                MyScrollView msv_out = shunlianyoupinMainFrag.msv_out;
//                msv_out.fullScroll(ScrollView.FOCUS_UP);
////                sv.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
//        miv_logo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                pullApp(ShunlianyoupinAct.this,"com.shunlian.app");
//            }
//        });

    }

    private void pullApp(Context context, String packageName) {
        if(isApkInstalled(context,packageName)){
            Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent!=null){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
            Common.staticToast(this,"尚未安装应用...");
        }

        }

    }

    @Override
    public void onItemClick(int action) {

        switch (action) {
            case R.id.ll_tab_main_page:
                mainClick();
                break;
            case R.id.ll_tab_main_mystore:
                mystoreClick();
                break;
            case R.id.ll_tab_person_center:
                personClick();
                break;
        }

    }

    private void personClick() {
        if (shunlianyoupinPersonFrag == null) {
            shunlianyoupinPersonFrag = (ShunlianyoupinPersonFrag) fragmentMap.get(flags[2]);
            if (shunlianyoupinPersonFrag == null) {

                shunlianyoupinPersonFrag = (ShunlianyoupinPersonFrag)ShunlianyoupinPersonFrag.getInstance("http://mt-front.v2.shunliandongli.com/app/personal", 0, "1");
                fragmentMap.put(flags[2], shunlianyoupinPersonFrag);
            }
        }
        switchContent(shunlianyoupinPersonFrag);
    }

    private void mystoreClick() {
        if (shunlianyoupinMystoreFrag == null) {
            shunlianyoupinMystoreFrag = (ShunlianyoupinMystoreFrag) fragmentMap.get(flags[1]);
            if (shunlianyoupinMystoreFrag == null) {
                shunlianyoupinMystoreFrag = (ShunlianyoupinMystoreFrag)ShunlianyoupinMystoreFrag.getInstance("http://mt-front.v2.shunliandongli.com/index", 0, "1");
                fragmentMap.put(flags[1], shunlianyoupinMystoreFrag);
            }
        }
        switchContent(shunlianyoupinMystoreFrag);
    }

    private void mainClick() {
        if (shunlianyoupinMainFrag == null) {
            shunlianyoupinMainFrag = (ShunlianyoupinMainFrag) fragmentMap.get(flags[0]);
            if (shunlianyoupinMainFrag == null) {
                shunlianyoupinMainFrag = new ShunlianyoupinMainFrag();
                fragmentMap.put(flags[0], shunlianyoupinMainFrag);
            }
        }
        switchContent(shunlianyoupinMainFrag);
    }


    public void switchContent(Fragment show) {
        if (show != null) {
            if (!show.isAdded()) {
                fragmentManager.beginTransaction().remove(show).commitAllowingStateLoss();
                fragmentManager.beginTransaction().add(R.id.fl_main, show).commitAllowingStateLoss();
            } else {
                fragmentManager.beginTransaction().show(show).commitAllowingStateLoss();
            }
            if (fragmentMap != null && fragmentMap.size() > 0) {
                Set<String> keySet = fragmentMap.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BaseFragment baseFragment = fragmentMap.get(key);
                    if (show != baseFragment) {
                        if (baseFragment != null && baseFragment.isVisible()) {
                            fragmentManager.beginTransaction().hide(baseFragment).commitAllowingStateLoss();
                        }
                    }
                }
            }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Common.staticToast("再按一次退出联动优品");
                mExitTime = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    finish();
                } catch (Exception e) {
                    finish();
                } finally {
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public  boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

}

