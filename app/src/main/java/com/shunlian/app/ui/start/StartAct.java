package com.shunlian.app.ui.start;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.CommondEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.ui.MBaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.JpushUtil;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IMain;
import com.shunlian.app.widget.MyImageView;

import java.io.InputStream;
import java.util.HashSet;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/4/12 0012.
 */
public class StartAct extends MBaseActivity implements IMain {
    private AnimationDrawable flashAnimation;
    private String localVersion;
    private AdEntity data;
    private boolean isAD=false,isHave=false;
    private PMain pMain;

    @BindView(R.id.miv_bg1)
    MyImageView miv_bg1;

    @BindView(R.id.miv_bg2)
    MyImageView miv_bg2;

    @Override
    protected int getLayoutId() {
        return R.layout.act_flash;
    }

    @Override
    protected void initData() {
        setHeader();
        versionJudge();
        //假如时间间隔超过一天则自动清除图片缓存
        long lastTime = SharedPrefUtil.getCacheSharedPrfLong("lastTime", -1);
        if (lastTime != -1 && System.currentTimeMillis() - lastTime > 24 * 60 * 60 * 1000) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    LogUtil.httpLogW("-----clear cache---start----");
//                    DataCleanManager.cleanApplicationCache(baseFragActivity.getApplicationContext());
                    LogUtil.httpLogW("-----clear cache---end----");
                }
            });
            SharedPrefUtil.saveCacheSharedPrfLong("lastTime", System.currentTimeMillis());
        }
        MyImageView miv_anim= (MyImageView) findViewById(R.id.miv_anim);
        miv_anim.setBackgroundResource(R.drawable.flash_animation);
        flashAnimation = (AnimationDrawable) miv_anim.getBackground();
        flashAnimation.start();
//        loadBitmap();
        isHave=true;
        Handler handler = new Handler();
//      handler.postDelayed(new Runnable() {
//          @Override
//          public void run() {
//              flashAnimation.stop();
//          }
//      },1100);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isHave=false;
                flashAnimation.stop();
                isFirstJudge();
            }
        }, 3 * 1000);
        pMain=new PMain(this,this);
        pMain.getSplashAD();

    }

//    private void loadBitmap(){
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPurgeable=true;
//        options.inInputShareable=true;
//        //设置该属性可获得图片的长宽等信息，但是避免了不必要的提前加载动画
//        options.inJustDecodeBounds=false;
//        InputStream is=null;
//        int[] raw={
//                R.mipmap.imgss_02,R.mipmap.imgss_03,R.mipmap.imgss_05,R.mipmap.imgss_07,R.mipmap.imgss_09,
//                R.mipmap.imgss_11,R.mipmap.imgss_13,R.mipmap.imgss_16,R.mipmap.imgss_17,R.mipmap.imgss_18,
//                R.mipmap.imgss_19,R.mipmap.imgss_21,R.mipmap.imgss_23,R.mipmap.imgss_25,R.mipmap.imgss_27,
//                R.mipmap.imgss_29,R.mipmap.imgss_31,R.mipmap.imgss_32,R.mipmap.imgss_33,R.mipmap.imgss_34,
//                R.mipmap.imgss_35,R.mipmap.imgss_37,R.mipmap.imgss_39,R.mipmap.imgss_41,R.mipmap.imgss_43,
//                R.mipmap.imgss_45,R.mipmap.imgss_47,R.mipmap.imgss_49
//        };
//        Bitmap[] mbitMap=new Bitmap[5];
//        for(int i=0,j=0;i<5&&j<5;i++,j++){
//            is=getResources().openRawResource(raw[i]);
//            Bitmap bitmap =BitmapFactory.decodeStream(is, null, options);
//            mbitMap[j]=bitmap;
//        }
//
//        AnimationDrawable clipDrawable= new AnimationDrawable();
//        for(int i=0;i<5;i++){
//            clipDrawable.addFrame(new BitmapDrawable(mbitMap[i]), 50);
//        }
//        clipDrawable.setOneShot(true);
//        flashAnimation=clipDrawable;
//        MyImageView miv_anim= (MyImageView) findViewById(R.id.miv_anim);
////        miv_anim.setBackgroundResource(R.drawable.flash_animation);
//        miv_anim.setBackground(flashAnimation);
//    }
    /**
     * 删除快捷方式
     */
    public void deleteShortCut() {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        //在网上看到到的基本都是一下几句，测试的时候发现并不能删除快捷方式。
        //String appClass = activity.getPackageName()+"."+ activity.getLocalClassName();
        //ComponentName comp = new ComponentName( activity.getPackageName(), appClass);
        //shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));
        /**改成以下方式能够成功删除，估计是删除和创建需要对应才能找到快捷方式并成功删除**/
        Intent intent = new Intent();
        intent.setClass(this, getClass());
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
//        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));//对应判断快捷方式是否存在
        sendBroadcast(shortcut);
    }

    @Override
    protected void onStop() {
        tryRecycleAnimationDrawable(flashAnimation);
        super.onStop();
    }

    //创建桌面快捷方式
    private void createShortCut() {
        Intent intent = new Intent();
        intent.setClass(this, this.getClass());
        /*以下两句是为了在卸载应用的时候同时删除桌面快捷方式*/
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        //创建快捷方式的Intent
        Intent addShortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //是否允许重复创建
        addShortcut.putExtra("duplicate", false);
        //指定当前的Activity为快捷方式启动的对象: 如com.android.music.MusicBrowserActivity
        //注意: ComponentName的第二个参数必须加上点号(.)，否则快捷方式无法启动相应程序
        //此方法创建快捷方式时没有问题，但是删除快捷方式时找不到对应的快捷方式,此方法创建的快捷方式与华为创建一致
//        ComponentName comp = new ComponentName(this.getPackageName(), "." + this.getLocalClassName());
//        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(Intent.ACTION_MAIN).setComponent(comp));//对应判断快捷方式是否存在

        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);//对应删除快捷方式
        //设置快捷方式的图标
//        Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this,
//                R.drawable.ic_launcher_activity);//活动图标
        Intent.ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this,
                R.mipmap.ic_launcher);//原始图标
        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 设置快捷方式的名字
        addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        sendBroadcast(addShortcut);
    }


    public void versionJudge() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            localVersion = packageInfo.versionName;//应用现在的版本名称
            SharedPrefUtil.saveSharedPrfString("localVersion", localVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void isFirstJudge() {
        //如果是第一次启动app，或者版本更新后需要启动引导页
        if (SharedPrefUtil.getCacheSharedPrfBoolean("isFirst", true) || !SharedPrefUtil.getSharedPrfString("localVersion", "-1").equals(localVersion)) {
//            Constant.IS_GUIDE = true;
            deleteShortCut();
            createShortCut();
            SharedPrefUtil.saveSharedPrfString("localVersion", localVersion);
            SharedPrefUtil.saveCacheSharedPrfBoolean("isFirst", false);
            SharedPrefUtil.saveCacheSharedPrfLong("lastTime", System.currentTimeMillis());
//            Intent intent = new Intent(baseFragActivity, GuideAct.class);
//            startActivity(intent);
            //暂时关闭引导页
            GuideAct.startAct(this);
//            if (isAD){
//                ADAct.startAct(getBaseContext(),data);
//            }else {
////                Constant.IS_GUIDE = false;
//                MainActivity.startAct(this, "");
//            }
        } else {
            if (isAD){
                ADAct.startAct(getBaseContext(),data);
            }else {
//                Constant.IS_GUIDE = false;
                MainActivity.startAct(this, "");
            }
        }
        finish();
    }


    //禁止返回键
    @Override
    public void onBackPressed() {
        // 这里处理逻辑代码，大家注意：该方法仅适用于2.0或更新版的sdk
//        super.onBackPressed();
    }

    @Override
    public void setAD(AdEntity data) {
        if (isHave){
            if ("1".equals(data.show)){
                isAD=true;
                this.data=data;
            }
            if ("1".equals(data.is_tag)&&data.tag != null&&data.tag.size()>0){
                SharedPrefUtil.saveSharedPrfStringss("tags", new HashSet<>(data.tag));
                JpushUtil.setJPushAlias();
            }
            SharedPrefUtil.saveSharedPrfString("plus_role", data.plus_role);
        }
    }

    @Override
    public void setCommond(CommondEntity data) {

    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    @Override
    protected void onDestroy() {
        releaseImageViewResouce(miv_bg1);
        releaseImageViewResouce(miv_bg2);
//        tryRecycleAnimationDrawable(flashAnimation);
        super.onDestroy();
    }

    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }

    private void tryRecycleAnimationDrawable(AnimationDrawable animationDrawable) {
        if (animationDrawable != null) {
            animationDrawable.stop();
            for (int i = 0; i < animationDrawable.getNumberOfFrames(); i++) {
                Drawable frame = animationDrawable.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable) frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            animationDrawable.setCallback(null);
            animationDrawable = null;
            System.gc();
        }
    }
}