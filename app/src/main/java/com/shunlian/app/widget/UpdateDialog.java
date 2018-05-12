package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.App;
import com.shunlian.app.R;
import com.shunlian.app.bean.AdEntity;
import com.shunlian.app.bean.UpdateEntity;
import com.shunlian.app.presenter.PMain;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.utils.DownloadService;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MyOnClickListener;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IMain;

import java.io.File;


/**
 * Created by Administrator on 2016/12/13 0013.
 */

public class UpdateDialog implements IMain {
    public Dialog updateDialog;
    public Dialog forceDialog;
    //按返回键不取消dialog
    private DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };
    private Activity activity;
    private String updateLog;
    private String updateType;
    private String updateUrl;
    private String fileMd5;
    private TextView tv_checkUpdate;
    private CBProgressBar cbProgressBar;
    private PromptDialog promptDialog;
    private String cancelState;

    public UpdateDialog(Activity activity) {
        this.activity = activity;
        PMain pMain = new PMain(activity, this);
        pMain.getUpdateInfo("Android", SharedPrefUtil.getSharedPrfString("localVersion", "2.0.0"));

    }

    private void initForceDialog() {
        if (forceDialog == null) {
            forceDialog = new Dialog(activity, R.style.popAd);
            forceDialog.setContentView(R.layout.dialog_force);
            forceDialog.setOnKeyListener(keylistener);
            forceDialog.setCancelable(false);
            tv_checkUpdate = (TextView) forceDialog.findViewById(R.id.tv_checkUpdate);
            cbProgressBar = (CBProgressBar) forceDialog.findViewById(R.id.my_progress);
            tv_checkUpdate.setText("正在更新中...");
            cbProgressBar.setVisibility(View.VISIBLE);
            //自定义环形进度条
        }

        if (!activity.isFinishing()) {
            forceDialog.show();
        }
    }

    private void initUpdateDialog() {
        if (updateDialog == null) {
            updateDialog = new Dialog(activity, R.style.popAd);
            updateDialog.setContentView(R.layout.dialog_update);
            updateDialog.setOnKeyListener(keylistener);
            updateDialog.setCancelable(false);
//        TextView tv_newVersion = (TextView) updateDialog.findViewById(R.id.tv_newVersion);
//        TextView tv_oldVersion = (TextView) updateDialog.findViewById(R.id.tv_oldVersion);
            MyTextView mtv_update = (MyTextView) updateDialog.findViewById(R.id.mtv_update);
            MyTextView mtv_close = (MyTextView) updateDialog.findViewById(R.id.mtv_close);
//        TextView tv_update = (TextView) updateDialog.findViewById(R.id.tv_update);
            MyTextView mtv_content = (MyTextView) updateDialog.findViewById(R.id.mtv_content);
//        TextView tv_cancel = (TextView) updateDialog.findViewById(R.id.tv_cancel);
            mtv_content.setText(updateLog);
            mtv_content.setMovementMethod(new ScrollingMovementMethod());
//        tv_newVersion.setText("发现新版本" + newVersion);
//        tv_oldVersion.setText("（当前版本" + localVersion + "）");
            if ("force".equals(updateType)) {
                mtv_close.setVisibility(View.GONE);
            } else {
                mtv_close.setVisibility(View.VISIBLE);
            }
            mtv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateDialog.dismiss();
                    if (MyOnClickListener.isWIFIState(activity)) {
                        updataApk();
                    } else {
                        if ("force".equals(updateType)) {
                            cancelState = "退出应用";
                        } else {
                            cancelState = "取消";
                        }
                        promptDialog = new PromptDialog(activity);
                        promptDialog.setSureAndCancleListener("非WiFi状态，确定下载吗？", "确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                promptDialog.dismiss();
                                updataApk();
                            }
                        }, cancelState, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if ("force".equals(updateType)) {
                                    System.exit(0);
                                } else {
                                    promptDialog.dismiss();
                                }

                            }
                        }).show();
                    }
                }
            });
            mtv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                enter();
                    updateDialog.dismiss();
                }
            });
        }
        if (!activity.isFinishing()) {
            updateDialog.show();
        }
    }
    public void initDialogFinish(){
        //用于方法重写回调
    }
    private void updataApk() {
        Constant.IS_DOWNLOAD = true;
        Intent intent = new Intent(activity, DownloadService.class);
        intent.putExtra("url", updateUrl);
        intent.putExtra("fileMd5", fileMd5);
        activity.startService(intent);
//                    if (!"force".equals(updateType)) {
//                        enter();
//                    } else {
        if ("force".equals(updateType)) {
            //显示更新应用界面
//            updateRl.setVisibility(View.VISIBLE);
//                        cbProgressBar.setVisibility(View.VISIBLE);
            DownloadService.setOnProgressChangeListener(new DownloadService.OnProgressChangeListener() {
                @Override
                public void onStart() {
                    initForceDialog();
                }

                @Override
                public void maxProgress(float max) {
                    cbProgressBar.setMax(max);
                }

                @Override
                public void progress(float progress) {
                    cbProgressBar.setProgress(progress);
                }

                @Override
                public void downloadSuccess() {
                    LogUtil.httpLogW("----updateUtil----downloadSuccess-------");
                    tv_checkUpdate.setText("请先升级到最新版本");
                    cbProgressBar.setMax(100);
                    cbProgressBar.setProgress(100);
                }

                @Override
                public void isLoaclAPK(boolean is) {
                    LogUtil.httpLogW("----updateUtil----isLoaclAPK-------" + is);
//                    if (is) {
//                        tv_checkUpdate.setText("请先升级到最新版本");
//                        cbProgressBar.setVisibility(View.INVISIBLE);
//                    }
                }

                @Override
                public void installAPK() {
                    LogUtil.httpLogW("---updateUtil-----installAPK-------");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            forceDialog.dismiss();
                            cbProgressBar.setProgress(0);
                            updateDialog.show();
                        }
                    }, 2000);

                }

                @Override
                public void onError() {
                    forceDialog.dismiss();
                    cbProgressBar.setProgress(0);
                    updateDialog.show();
                }
            });
        }
    }

    public PromptDialog getPromptDialog() {
        return promptDialog;
    }

    @Override
    public void setAD(AdEntity data) {

    }

    @Override
    public void setUpdateInfo(UpdateEntity data) {
        updateLog = data.changeLog;
        String needUpdate = data.needUpdate;
        updateType = data.updateType;
        updateUrl = data.updateUrl;
        fileMd5 = data.fileMd5;
        SharedPrefUtil.saveSharedPrfString("updateLog", updateLog);
        SharedPrefUtil.saveSharedPrfString("newVersion", data.newVersion);
        SharedPrefUtil.saveSharedPrfString("needUpdate", needUpdate);
        SharedPrefUtil.saveSharedPrfString("updateType", updateType);
        SharedPrefUtil.saveSharedPrfString("updateUrl", updateUrl);
        SharedPrefUtil.saveSharedPrfString("fileMd5", fileMd5);
//                    updateType = "force";//调试强制更新
        if ("yes".equals(needUpdate)) {
            initUpdateDialog();
        } else {
            File file = new File(App.DOWNLOAD_PATH + DownloadService.fileName);
            if (file.exists()) {//如果存在安装包，则删除安装包。
                file.delete();
            }
//                        enter();
        }
        initDialogFinish();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
