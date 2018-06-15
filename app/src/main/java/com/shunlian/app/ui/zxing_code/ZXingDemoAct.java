package com.shunlian.app.ui.zxing_code;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.presenter.ZXingPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IZXingView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.mylibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/12/28.
 */

public class ZXingDemoAct extends BaseActivity implements QRCodeView.Delegate,IZXingView {

    @BindView(R.id.zxingview)
    QRCodeView mQRCodeView;

    @BindView(R.id.mrlayout_title)
    MyRelativeLayout mrlayout_title;

    private boolean isOpenLight = false;
    public static final int RESULT_CODE = 200;//结果码
    public static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private boolean isResult;
    private ZXingPresenter mPresenter;


    public static void startAct(Activity activity, boolean isResult, int requestCode) {
        if (isResult) {
            Intent intent = new Intent(activity, ZXingDemoAct.class);
            intent.putExtra("isResult", isResult);
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(new Intent(activity, ZXingDemoAct.class));
        }
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_zxing_demo;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        int statusBarHeight = ImmersionBar.getStatusBarHeight(this);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                mrlayout_title.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        mrlayout_title.setLayoutParams(layoutParams);
        isResult = getIntent().getBooleanExtra("isResult", false);
        mQRCodeView.setDelegate(this);

        mPresenter = new ZXingPresenter(this,this);
    }

    @OnClick(R.id.mllayout_light)
    public void openLight() {
        if (!isOpenLight) {
            mQRCodeView.openFlashlight();
        } else {
            mQRCodeView.closeFlashlight();
        }
        isOpenLight = !isOpenLight;
    }

    @OnClick(R.id.mtv_album)
    public void openAlbum() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(1); // 最多选择照片数量，默认为1
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();

        mQRCodeView.showScanRect();

        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /**
     * 处理扫描结果
     *
     * @param result
     */
    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        mQRCodeView.startSpot();
        mQRCodeView.closeFlashlight();
        if (isResult) {
            Intent intent = new Intent();
            intent.putExtra("result", result);
            setResult(RESULT_CODE, intent);
            finish();
        } else {
            if (mPresenter != null)
                mPresenter.parseUrl(result);
        }
    }

    /**
     * 处理打开相机出错
     */
    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtil.zhLogW("打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT).get(0);
            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    mQRCodeView.closeFlashlight();
                    if (TextUtils.isEmpty(result)) {
                        Common.staticToast("未发现二维码");
                    } else {
                        if (isResult) {
                            Intent intent = new Intent();
                            intent.putExtra("result", result);
                            setResult(RESULT_CODE, intent);
                            finish();
                        } else {
                            if (mPresenter != null)
                                mPresenter.parseUrl(result);
                        }
                    }
                }
            }.execute();
        }
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }
}
