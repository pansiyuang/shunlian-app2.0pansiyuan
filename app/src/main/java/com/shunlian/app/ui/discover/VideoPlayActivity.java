package com.shunlian.app.ui.discover;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.OperateAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.HttpDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;

/**
 * Created by Administrator on 2018/7/23.
 */

public class VideoPlayActivity extends BaseActivity {

    @BindView(R.id.customVideoPlayer)
    CustomVideoPlayer customVideoPlayer;

    @BindView(R.id.ll_rootView)
    LinearLayout ll_rootView;

    public static void startActivity(Context context, String videoUrl) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String videoUrl, String placeHold) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("videoUrl", videoUrl);
        intent.putExtra("placehold", placeHold);
        context.startActivity(intent);
    }

    public List<String> itemList;
    public String currentUrl;
    public String currentPlaceHold;
    public HttpDialog httpDialog;
    private Dialog dialog_operate;
    public String dirName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initData() {
        // 使通知栏透明化
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        dirName = Environment.getExternalStorageDirectory() + "/" + getPackageName();
        currentUrl = getIntent().getStringExtra("videoUrl");
        currentPlaceHold = getIntent().getStringExtra("placehold");
        customVideoPlayer.setUp(currentUrl, CustomVideoPlayer.SCREEN_WINDOW_FULLSCREEN);
        if (!isEmpty(currentPlaceHold)) {
            GlideUtils.getInstance().loadImage(this, customVideoPlayer.thumbImageView, currentPlaceHold);
        }
        customVideoPlayer.backButton.setOnClickListener(v -> finish());
        customVideoPlayer.fullscreenButton.setVisibility(View.GONE);
        customVideoPlayer.setVideoPlayListener(() -> initDialog());
        customVideoPlayer.startVideo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setHideStatusAndNavigation();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    public void initDialog() {
        if (itemList == null) {
            itemList = new ArrayList<>();
            itemList.add("保存视频");
            itemList.add("图文分享");
            itemList.add("取消");
        }

        if (dialog_operate == null) {
            dialog_operate = new Dialog(this, R.style.popAd);
            dialog_operate.setContentView(R.layout.dialog_operate);
            RecyclerView rv_operate = (RecyclerView) dialog_operate.findViewById(R.id.rv_operate);
            rv_operate.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false));
            rv_operate.addItemDecoration(new MVerticalItemDecoration(this, 0.5f, 0, 0, getColorResouce(R.color.bg_gray_two)));
            OperateAdapter operateAdapter = new OperateAdapter(this, itemList);
            rv_operate.setAdapter(operateAdapter);
            operateAdapter.setOnItemClickListener((view, position) -> {
                switch (itemList.get(position)) {
                    case "保存视频":
                        readToDownLoad();
                        break;
                    case "图文分享":
                        break;
                }
                dialog_operate.dismiss();
            });
        }
        dialog_operate.show();
    }

    public void readToDownLoad() {
        File file = new File(dirName);
        // 文件夹不存在时创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 下载后的文件名
        int i = currentUrl.lastIndexOf("/"); // 取的最后一个斜杠后的字符串为名
        String fileName = dirName + currentUrl.substring(i, currentUrl.length());
        LogUtil.httpLogW("文件名:" + fileName);
        File file1 = new File(fileName);
        if (file1.exists()) {
            Toast.makeText(this, "已下载过该视频,请勿重复下载!", Toast.LENGTH_SHORT).show();
        } else {
            if (httpDialog == null) {
                httpDialog = new HttpDialog(this);
            }
            if (!httpDialog.isShowing()) {
                httpDialog.show();
            }
            new Thread(() -> downLoadVideo(fileName)).start();
            LogUtil.httpLogW("下载完成");
            if (httpDialog.isShowing()) {
                httpDialog.dismiss();
            }
        }
    }

    public void downLoadVideo(String fileName) {
        try {
            URL url = new URL(currentUrl);
            // 打开连接
            URLConnection conn = url.openConnection();
            // 打开输入流
            InputStream is = conn.getInputStream();
            // 创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(fileName);
            // 写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完成后关闭流
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.httpLogW(e.getMessage());
        }
    }
}
