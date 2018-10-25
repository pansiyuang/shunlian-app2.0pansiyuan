package com.zh.smallmediarecordlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.zh.smallmediarecordlib.api.IMediaRecorder;
import com.zh.smallmediarecordlib.util.VideoUtils;
import com.zh.smallmediarecordlib.widget.FocusSurfaceView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhanghe on 2018/10/16.
 */

public class MediaRecorderBase {

    public static final String TAG = MediaRecorderBase.class.getName();

    private Camera mCamera;
    private Context mContext;
    private FocusSurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private IMediaRecorder iMediaRecorder;
    private MediaRecorder mediaRecorder;
    public String currentVideoFilePath = "";
    private String saveVideoPath = "";
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private MediaRecorder.OnErrorListener OnErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int what, int extra) {
            try {
                if (mediaRecorder != null) {
                    mediaRecorder.reset();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public static final String CACHE_PATH_EXTERNAL = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "shunlian";

    public MediaRecorderBase(Context context, FocusSurfaceView surfaceView, IMediaRecorder iMediaRecorder) {
        mContext = context;
        mSurfaceView = surfaceView;
        mSurfaceHolder = surfaceView.getHolder();
        this.iMediaRecorder = iMediaRecorder;
        initCamera();
    }

    public Camera getCamera() {
        return mCamera;
    }

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            int numberOfCameras = Camera.getNumberOfCameras();
            if (numberOfCameras == 2) {
                c = Camera.open(mCameraId); // attempt to get a Camera instance
            } else {
                c = Camera.open(); // attempt to get a Camera instance
            }

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    /**
     * 初始化摄像头
     */
    private void initCamera() {
        if (mCamera != null) {
            releaseCamera();
        }
        saveVideoPath = "";
        mCamera = getCameraInstance();
        if (mCamera == null) {
            Toast.makeText(mContext, "未能获取到相机！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mSurfaceView != null) {
            mSurfaceView.setTouchFocus(mCamera);
        }

        try {
            //将相机与SurfaceHolder绑定
            mCamera.setPreviewDisplay(mSurfaceHolder);
            //配置CameraParams
            configCameraParams();
            //启动相机预览
            mCamera.startPreview();
        } catch (IOException e) {
            //有的手机会因为兼容问题报错，这就需要开发者针对特定机型去做适配了
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }


    /**
     * 设置摄像头为竖屏
     *
     * @author lip
     * @date 2015-3-16
     */
    private void configCameraParams() {
        Camera.Parameters params = mCamera.getParameters();
        //设置相机的横竖屏(竖屏需要旋转90°)
        if (mContext.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            params.set("orientation", "portrait");
            mCamera.setDisplayOrientation(90);
        } else {
            params.set("orientation", "landscape");
            mCamera.setDisplayOrientation(0);
        }
        //设置聚焦模式
        params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        //缩短Recording启动时间
        params.setRecordingHint(true);
        //影像稳定能力
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            if (params.isVideoStabilizationSupported())
                params.setVideoStabilization(true);
        }
        mCamera.setParameters(params);
    }


    /**
     * 释放摄像头资源
     *
     * @author liuzhongjun
     * @date 2016-2-5
     */
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 开始录制视频
     */
    private boolean start_Record() {
        //录制视频前必须先解锁Camera
        if (mCamera == null) return false;
        mCamera.unlock();
        configMediaRecorder();
        try {
            mediaRecorder.start();
        } catch (Exception e) {
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    /**
     * 停止录制视频
     */
    private boolean releaseMediaRecorder() {
        try {
            if (mediaRecorder != null) {
                // 设置后不会崩
                mediaRecorder.setOnErrorListener(null);
                mediaRecorder.setPreviewDisplay(null);
                mediaRecorder.setOnInfoListener(null);
                //停止录制
                mediaRecorder.stop();
                mediaRecorder.reset();
                //释放资源
                mediaRecorder.release();
                mediaRecorder = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "录制时间太短", Toast.LENGTH_SHORT).show();
            new File(currentVideoFilePath).delete();
            if (mediaRecorder != null){
                mediaRecorder.reset();
                //释放资源
                mediaRecorder.release();
                mediaRecorder = null;
            }
            return false;
        }
        return true;
    }

    private void pause_Record() {
        //正在录制的视频，点击后暂停
        //取消自动对焦
        cancelAutoFocus();
        releaseMediaRecorder();
    }

    /**
     * 初始化录制
     */
    public void initRecord() {
        initCamera();
        //configMediaRecorder();
    }

    /**
     * 开始录制
     */
    public boolean startRecord() {
        if (getSDPath(mContext) == null) return false;
        //视频文件保存路径，configMediaRecorder方法中会设置
        currentVideoFilePath = getSDPath(mContext) + getVideoName();
        //开始录制视频
        return start_Record();
    }

    /**
     * 暂停录制
     */
    public void pauseRecord() {
        pause_Record();
        //判断是否进行视频合并
        if ("".equals(saveVideoPath)) {
            saveVideoPath = currentVideoFilePath;
        } else {
            mergeRecordVideoFile();
        }
    }

    /**
     * 完成录制
     */
    public boolean completeRecord() {
        //停止视频录制
        if (!releaseMediaRecorder()){
            if (iMediaRecorder != null) {
                iMediaRecorder.completeMergeRecordVideo(null);
                return false;
            }
        }else{
            //先给Camera加锁后再释放相机
            if (mCamera != null) {
                mCamera.lock();
            }
            releaseCamera();
            //判断是否进行视频合并
            mergeRecordVideoFile();
        }
        return true;
    }

    /**
     * 配置MediaRecorder()
     */
    private void configMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.reset();
        mediaRecorder.setCamera(mCamera);

        mediaRecorder.setOnErrorListener(OnErrorListener);

        //使用SurfaceView预览
        mediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

        //1.设置采集声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置采集图像
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        //2.设置视频，音频的输出格式 mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        //3.设置音频的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //设置图像的编码格式
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        //设置立体声
//        mediaRecorder.setAudioChannels(2);
        //设置最大录像时间 单位：毫秒
//        mediaRecorder.setMaxDuration(60 * 1000);
        //设置最大录制的大小 单位，字节
//        mediaRecorder.setMaxFileSize(1024 * 1024);
        //音频一秒钟包含多少数据位
        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        mediaRecorder.setAudioEncodingBitRate(44100);
        if (mProfile.videoBitRate > 10 * 1024 * 1024)
            mediaRecorder.setVideoEncodingBitRate(10 * 1024 * 1024);
        else
            mediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);

        //设置选择角度，顺时针方向，因为默认是逆向90度的，这样图像就是正常显示了,这里设置的是观看保存后的视频的角度
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mediaRecorder.setOrientationHint(270);
        } else {
            mediaRecorder.setOrientationHint(90);
        }

        //设置录像的分辨率
        mediaRecorder.setVideoSize(720, 480);

        //设置录像视频输出地址
        mediaRecorder.setOutputFile(currentVideoFilePath);
        //开始录制
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变闪光灯
     *
     * @return
     */
    public boolean changeFlash() {
        boolean flashOn = false;
        if (flashEnable() && mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            if (Camera.Parameters.FLASH_MODE_TORCH.equals(params.getFlashMode())) {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                flashOn = false;
            } else {
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                flashOn = true;
            }
            mCamera.setParameters(params);
        }
        return flashOn;
    }

    public boolean flashEnable() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
                && mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK;

    }

    /**
     * 切换前置/后置摄像头
     */
    public void switchCamera() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);
        } else {
            switchCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        }
    }

    /**
     * 切换前置/后置摄像头
     */
    public void switchCamera(int cameraFacingFront) {
        mCameraId = cameraFacingFront;
        releaseCamera();
        initCamera();
    }

    public void cancelAutoFocus() {
        if (mCamera != null)
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success && mCamera != null)
                    mCamera.cancelAutoFocus();
            }
        });
    }


    /**
     * 合并录像视频方法
     */
    private void mergeRecordVideoFile() {
        if (TextUtils.isEmpty(saveVideoPath) || saveVideoPath.equals(currentVideoFilePath)) {
            if (iMediaRecorder != null) {
                iMediaRecorder.completeMergeRecordVideo(currentVideoFilePath);
            }
        } else {
            new AsyncTask<Void, Void, String>() {
                @Override
                protected void onPreExecute() {
                    if (iMediaRecorder != null) {
                        iMediaRecorder.startMergeRecordVideo();
                    }
                }

                @Override
                protected String doInBackground(Void... voids) {
                    try {
                        String[] str = new String[]{saveVideoPath, currentVideoFilePath};
                        //将2个视频文件合并到 append.mp4文件下
                        VideoUtils.appendVideo(mContext, getSDPath(mContext) + "append.mp4", str);
                        File reName = new File(saveVideoPath);
                        File f = new File(getSDPath(mContext) + "append.mp4");
                        //再将合成的append.mp4视频文件 移动到 saveVideoPath 路径下
                        f.renameTo(reName);
                        if (reName.exists()) {
                            f.delete();
                            new File(currentVideoFilePath).delete();
                        }
                        return reName.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(String s) {
                    if (iMediaRecorder != null) {
                        iMediaRecorder.completeMergeRecordVideo(s);
                    }
                }
            }.execute();
        }
    }


    /**
     * 创建视频文件保存路径
     */
    public static String getSDPath(Context context) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(context, "请查看您的SD卡是否存在！", Toast.LENGTH_SHORT).show();
            return null;
        }

        File eis = new File(CACHE_PATH_EXTERNAL + "/RecordVideo/");
        if (!eis.exists()) {
            eis.mkdir();
        }
        return eis.getAbsolutePath() + File.separator;
    }

    private String getVideoName() {
        return "VID_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".mp4";
    }
}
