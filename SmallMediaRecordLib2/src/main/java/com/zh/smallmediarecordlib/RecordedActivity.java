package com.zh.smallmediarecordlib;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zh.smallmediarecordlib.api.IMediaRecorder;
import com.zh.smallmediarecordlib.util.Utils;
import com.zh.smallmediarecordlib.widget.FocusSurfaceView;
import com.zh.smallmediarecordlib.widget.MyVideoView;
import com.zh.smallmediarecordlib.widget.RecordedButton;

import java.io.File;

/**
 * Created by zhanghe on 2018/10/17.
 * 处理录制时长
 * 权限申请
 */

public class RecordedActivity extends BaseActivity implements View.OnClickListener, IMediaRecorder {

    private final int PERMISSION_REQUEST_CODE = 0x001;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //UI
    private RecordedButton mRecordControl;
    private ImageView iv_change_flash;
    private ImageView iv_change_camera;
    private RelativeLayout rl_bottom;
    private FocusSurfaceView surfaceView;
    private ImageView iv_back;
    private ImageView iv_close;
    private ImageView iv_finish;
    private TextView dialogTextView;
    private MyVideoView vv_play;
    private RelativeLayout rl_top;
    private TextView tv_hint;


    private String mUrl;
    private CountDownTimer mDownTimer;
    private SurfaceHolder mSurfaceHolder;
    private final int maxDuration = 15900;//最大时长
    private MediaRecorderBase mediaRecorderBase;

    private SurfaceHolder.Callback mSurfaceCallBack = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            mediaRecorderBase = new MediaRecorderBase(RecordedActivity.this, surfaceView,
                    RecordedActivity.this);
        }

        @Override
        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (mSurfaceHolder != null && mSurfaceHolder.getSurface() == null) {
                return;
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (mediaRecorderBase != null)
                mediaRecorderBase.releaseCamera();
        }
    };

    public static void startAct(Activity activity, int code) {
        Intent intent = new Intent(activity, RecordedActivity.class);
        activity.startActivityForResult(intent, code);
    }

    private void permissionCheck() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean permissionState = true;
            for (String permission : permissionManifest) {
                int i = ContextCompat.checkSelfPermission(this, permission);
                if (i == PackageManager.PERMISSION_DENIED) {
                    permissionState = false;
                }
            }
            if (!permissionState) {
                ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (Manifest.permission.CAMERA.equals(permissions[i])) {
                        if (mediaRecorderBase != null) {
                            mediaRecorderBase.initRecord();
                        }
                    } else if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_record);
        if (!Utils.checkCameraHardware(this)) {
            Toast.makeText(this, "相机不存在或者被别的应用占用", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        initView();
        permissionCheck();
        initData();
        initListener();
    }


    private void initView() {
        surfaceView = findViewById(R.id.record_surfaceView);
        mRecordControl = findViewById(R.id.record_control);
        iv_change_flash = findViewById(R.id.iv_change_flash);
        iv_change_camera = findViewById(R.id.iv_change_camera);
        rl_bottom = findViewById(R.id.rl_bottom);
        rl_top = findViewById(R.id.rl_top);
        vv_play = findViewById(R.id.vv_play);
        tv_hint = findViewById(R.id.tv_hint);
        iv_back = findViewById(R.id.iv_back);
        iv_finish = findViewById(R.id.iv_finish);
        iv_close = findViewById(R.id.iv_close);
    }

    private void initData() {
        //配置SurfaceHolder
        mSurfaceHolder = surfaceView.getHolder();
        // 设置Surface不需要维护自己的缓冲区
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 设置分辨率
        mSurfaceHolder.setFixedSize(320, 280);
        // 设置该组件不会让屏幕自动关闭
        mSurfaceHolder.setKeepScreenOn(true);
        //回调接口
        mSurfaceHolder.addCallback(mSurfaceCallBack);

        UIState(1);
        mRecordControl.setMax(maxDuration);

        if (MediaRecorderBase.isSupportCameraLedFlash(getPackageManager())){
            visible(iv_change_flash);
        }else {
            gone(iv_change_flash);
        }

        if (MediaRecorderBase.isSupportFrontCamera()){
            visible(iv_change_camera);
        }else {
            gone(iv_change_camera);
        }
    }

    private void initListener() {
        iv_close.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_finish.setOnClickListener(this);

        mRecordControl.setOnGestureListener(new RecordedButton.OnGestureListener() {
            private boolean start_state;

            @Override
            public void onLongClick() {
                start_state = startRecord();
                if (start_state) {
                    progress();
                }
            }

            @Override
            public void onClick() {
            }

            @Override
            public void onLift() {
                if (start_state && completeRecord()) {
                    mRecordControl.closeButton();
                    UIState(2);
                }
                if (mDownTimer != null) {
                    mDownTimer.cancel();
                }
            }

            @Override
            public void onOver() {
                if (start_state) {
                    mRecordControl.closeButton();
                    completeRecord();
                    UIState(2);
                }
                if (mDownTimer != null) {
                    mDownTimer.cancel();
                }
            }
        });

        iv_change_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeFlash()) {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_open);
                } else {
                    iv_change_flash.setImageResource(R.mipmap.video_flash_close);
                }
            }
        });

        iv_change_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
                iv_change_flash.setImageResource(R.mipmap.video_flash_close);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_finish) {
            if (!TextUtils.isEmpty(mUrl)) {
                Intent intent = new Intent();
                intent.putExtra("video_path", mUrl);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.iv_back) {
            new File(mediaRecorderBase.currentVideoFilePath).delete();
            finish();
        } else if (v.getId() == R.id.iv_close) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDownTimer != null){
            mDownTimer.cancel();
            mDownTimer = null;
        }
        if (mSurfaceHolder != null){
            mSurfaceHolder.removeCallback(mSurfaceCallBack);
            mSurfaceCallBack = null;
            mSurfaceHolder = null;
        }

        if (mediaRecorderBase != null){
            mediaRecorderBase.destory();
        }
    }

    /**
     * ui状态
     * 1 预览状态
     * 2 预览视频状态
     *
     * @param state_code
     */
    private void UIState(int state_code) {
        if (state_code == 1) {
            visible(tv_hint, mRecordControl, rl_top, iv_close);
            gone(rl_bottom, vv_play);
            mRecordControl.cleanSplit();
            mRecordControl.setProgress(0);
        } else if (state_code == 2) {
            gone(rl_top, mRecordControl, tv_hint, iv_close);
            visible(vv_play, rl_bottom);
            mRecordControl.cleanSplit();
            mRecordControl.setProgress(0);
        }
    }

    /**
     * 改变灯光
     *
     * @return
     */
    public boolean changeFlash() {
        if (mediaRecorderBase != null) {
            return mediaRecorderBase.changeFlash();
        }
        return false;
    }

    /**
     * 切换前后相机
     */
    public void switchCamera() {
        if (mediaRecorderBase != null) {
            mediaRecorderBase.switchCamera();
        }
    }

    /**
     * 初始化录制
     */
    private void initRecord() {
        //staticToast("初始化录制");
        if (mediaRecorderBase != null) {
            mediaRecorderBase.initRecord();
        }
    }

    /**
     * 开始录制
     */
    private boolean startRecord() {
        //staticToast("开始录制");
        if (mediaRecorderBase != null) {
            return mediaRecorderBase.startRecord();
        }
        return false;
    }

    /**
     * 暂停录制
     */
    private void pauseRecord() {
        //staticToast("暂停录制");
        if (mediaRecorderBase != null) {
            mediaRecorderBase.pauseRecord();
        }
    }

    /**
     * 完成录制
     */
    private boolean completeRecord() {
        //staticToast("完成录制");
        if (mediaRecorderBase != null) {
            return mediaRecorderBase.completeRecord();
        }
        return false;
    }

    /**
     * 开始合成视频
     */
    @Override
    public void startMergeRecordVideo() {
        dialogTextView = showProgressDialog();
        dialogTextView.setText("视频合成中");
    }

    /**
     * 完成合成
     *
     * @param url
     */
    @Override
    public void completeMergeRecordVideo(String url) {
        closeProgressDialog();
        if (url == null) {
            UIState(1);
        } else if ("".equals(url)) {
            Toast.makeText(mContext, "视频合成失败", Toast.LENGTH_SHORT).show();
        } else {
            mUrl = url;
            visible(vv_play, rl_bottom);
            gone(rl_top, tv_hint, mRecordControl, iv_close);
            playVideo(url);
            Uri localUri = Uri.parse("file://" + url);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
        }
    }

    private void playVideo(String url) {
        vv_play.setVideoPath(url);
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                vv_play.start();
            }
        });
        if (vv_play.isPrepared()) {
            vv_play.setLooping(true);
            vv_play.start();
        }
    }

    public void progress() {
        if (mDownTimer == null) {
            mDownTimer = new CountDownTimer(maxDuration, 50) {
                @Override
                public void onTick(long l) {
                    if (mRecordControl != null)
                        mRecordControl.setProgress(maxDuration - l);
                }

                @Override
                public void onFinish() {
                    if (mRecordControl != null) {
                        mRecordControl.setProgress(maxDuration);
                        mRecordControl.closeButton();
                    }
                }
            };
        }
        mDownTimer.cancel();
        mDownTimer.start();
    }
}

