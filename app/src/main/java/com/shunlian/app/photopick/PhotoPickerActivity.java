package com.shunlian.app.photopick;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

public class PhotoPickerActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = PhotoPickerActivity.class.getName();

    private Context mCxt;

    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 默认最大照片数量
     */
    public static final int DEFAULT_MAX_TOTAL = 9;
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 筛选照片配置信息
     */
    public static final String EXTRA_IMAGE_CONFIG = "image_config";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";

    // 结果数据
    private ArrayList<String> resultList = new ArrayList<>();

    //已选默认数据数据
    private ArrayList<String> savaList = new ArrayList<>();

    // 文件夹数据
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    @BindView(R.id.grid)
    GridView mGridView;

    @BindView(R.id.btnAlbum)
    Button btnAlbum;

    @BindView(R.id.tv_send)
    TextView tv_send;

    @BindView(R.id.photo_picker_footer)
    View mPopupAnchorView;

    // 最大照片数量
    private ImageCaptureManager captureManager;
    private int mDesireImageCount;
    private ImageConfig imageConfig; // 照片配置

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;
    private boolean isNeedLoad = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photopicker;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        initViews();

        // 照片属性
        imageConfig = getIntent().getParcelableExtra(EXTRA_IMAGE_CONFIG);
        // 首次加载所有图片
        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        // 选择图片数量
        mDesireImageCount = getIntent().getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_MAX_TOTAL);
        // 图片选择模式
        final int mode = getIntent().getExtras().getInt(EXTRA_SELECT_MODE, MODE_SINGLE);
        // 默认选择
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getIntent().getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList.addAll(tmp);
                savaList.addAll(resultList);
            }
        }
        // 是否显示照相机
        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mImageAdapter = new ImageGridAdapter(mCxt, mIsShowCamera, getItemImageWidth());
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        if (mode == MODE_MULTI) {
                            // 判断选择数量问题
                            if (mDesireImageCount == resultList.size()) {
                                Common.staticToast(getStringResouce(R.string.msg_amount_limit));
                                return;
                            }
                        }
                        showCameraAction();
                    } else {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        if (checkPhoto(image.path)) {
                            selectImageFromGrid(image, mode);
                        }
                    }
                } else {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    if (checkPhoto(image.path)) {
                        selectImageFromGrid(image, mode);
                    }
                }
            }
        });
        mFolderAdapter = new FolderAdapter(mCxt);

        refreshActionStatus();
    }

    private void initViews() {
        mCxt = this;
        captureManager = new ImageCaptureManager(mCxt);
        // ActionBar Setting
        mGridView = (GridView) findViewById(R.id.grid);
        mGridView.setNumColumns(getNumColnums());
        mPopupAnchorView = findViewById(R.id.photo_picker_footer);

        GradientDrawable sendBG = (GradientDrawable) tv_send.getBackground();
        sendBG.setColor(getColorResouce(R.color.pink_color));
    }

    @Override
    protected void initListener() {
        // 打开相册列表
        btnAlbum.setOnClickListener(this);
        // 预览
        tv_send.setOnClickListener(this);
        super.initListener();
    }

    public boolean checkPhoto(String photoPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(photoPath, options); //filePath代表图片路径
        if (options.mCancel || options.outWidth == -1 || options.outHeight == -1) {
            //说明图片已经损坏
            Common.staticToast("图片已经损坏,请换一张图片");
            return false;
        }
        return true;
    }

    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(mCxt);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);

        // 计算ListPopupWindow内容的高度(忽略mPopupAnchorView.height)，R.layout.item_foloer
        int folderItemViewHeight = TransformUtil.dip2px(this, 92);
        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;

        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        if (folderViewHeight >= screenHeigh) {
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        } else {
            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        }

        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            isNeedLoad = true;
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            btnAlbum.setText(R.string.all_image);
                            mImageAdapter.setShowCamera(mIsShowCamera);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                btnAlbum.setText(folder.name);
                                // 设定默认选择
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        resultList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onImageSelected(String path) {
        if (!resultList.contains(path)) {
            resultList.add(path);
        }
        refreshActionStatus();
    }

    public void onImageUnselected(String path) {
        if (resultList.contains(path)) {
            resultList.remove(path);
        }
        refreshActionStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        resultList.add(captureManager.getCurrentPhotoPath());
                    }
                    complete();
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        // 重置列数
        mGridView.setNumColumns(getNumColnums());
        // 重置Item宽度
        mImageAdapter.setItemSize(getItemImageWidth());

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }

            // 重置PopupWindow高度
            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        }

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mCxt, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    onImageUnselected(image.path);
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == resultList.size()) {
                        Common.staticToast(getStringResouce(R.string.msg_amount_limit));
                        return;
                    }
                    resultList.add(image.path);
                    onImageSelected(image.path);
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                onSingleImageSelected(image.path);
            }
        }
    }

    /**
     * 刷新操作按钮状态
     */
    private void refreshActionStatus() {
        String text = getString(R.string.done_with_count, resultList.size(), mDesireImageCount);
        tv_send.setText(text);
        boolean hasSelected = resultList.size() > 0;
        tv_send.setEnabled(hasSelected);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
                MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
                MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
                MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
                MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
                MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
                MediaStore.Images.Media.MIME_TYPE,      //图片的类型     image/jpeg
                MediaStore.Images.Media.DATE_ADDED};    //图片被添加的时间，long型  1450518608

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            //扫描所有图片
            if (id == LOADER_ALL)
                cursorLoader = new CursorLoader(getApplicationContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[6] + " DESC");
            //扫描某个图片文件夹
            if (id == LOADER_CATEGORY)
                cursorLoader = new CursorLoader(getApplicationContext(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, IMAGE_PROJECTION[1] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[6] + " DESC");

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (isNeedLoad) {
                isNeedLoad = false;
            } else {
                return;
            }
            mResultFolder.clear();
            if (data != null) {
                ArrayList<Image> allImages = new ArrayList<>();   //所有图片的集合,不分文件夹
                while (data.moveToNext()) {
                    //查询数据
                    String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));

                    if (!isPicFile(imagePath)) {
                        continue;
                    }
                    File file = new File(imagePath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }
                    long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                    //封装实体
                    Image imageItem = new Image(imagePath, imageName, imageAddTime);
                    allImages.add(imageItem);
                    //根据父路径分类存放图片
                    File imageFile = new File(imagePath);
                    File imageParentFile = imageFile.getParentFile();
                    Folder folder = new Folder();
                    folder.name = imageParentFile.getName();
                    folder.path = imageParentFile.getAbsolutePath();

                    if (!mResultFolder.contains(folder)) {
                        ArrayList<Image> images = new ArrayList<>();
                        images.add(imageItem);
                        folder.cover = imageItem;
                        folder.images = images;
                        mResultFolder.add(folder);
                    } else {
                        mResultFolder.get(mResultFolder.indexOf(folder)).images.add(imageItem);
                    }
                }
                mImageAdapter.setData(allImages);
                // 设定默认选择
                if (resultList != null && resultList.size() > 0) {
                    mImageAdapter.setDefaultSelected(resultList);
                }
                mFolderAdapter.setData(mResultFolder);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 获取GridView Item宽度
     *
     * @return
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = TransformUtil.dip2px(this, 2);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列
     *
     * @return
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }

    // 返回已选择的图片数据
    private void complete() {
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, resultList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAlbum:
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
                break;
//            case R.id.btnPreview:
//                PhotoPreviewIntent intent = new PhotoPreviewIntent(mCxt);
//                intent.setCurrentItem(0);
//                intent.setPhotoPaths(resultList);
//                startActivityForResult(intent, PhotoPreviewActivity.REQUEST_PREVIEW);
//                break;
            case R.id.tv_send:
                complete();
                break;
        }
    }

    public boolean isPicFile(String file) {
        if (file.toLowerCase().endsWith(".jpg") || file.toLowerCase().endsWith(".jpeg") || file.toLowerCase().endsWith(".png")) {
            return true;
        } else {
            return false;
        }
    }

}
