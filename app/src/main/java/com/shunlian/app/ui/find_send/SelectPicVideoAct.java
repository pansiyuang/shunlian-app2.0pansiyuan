package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.adapter.LoaderLocalImgVideoAdapter;
import com.shunlian.app.photopick.FolderAdapterV2;
import com.shunlian.app.photopick.FolderV2;
import com.shunlian.app.photopick.ImageCaptureManager;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.photoview.HackyViewPager;
import com.zh.smallmediarecordlib.RecordedActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/10/15.
 */

public class SelectPicVideoAct extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnAlbum)
    MyTextView btnAlbum;

    @BindView(R.id.tv_complete)
    MyTextView tv_complete;

    @BindView(R.id.mtv_album)
    MyTextView mtv_album;

    @BindView(R.id.mtv_camera)
    MyTextView mtv_camera;

    @BindView(R.id.mtv_video)
    MyTextView mtv_video;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.rlayout_root)
    RelativeLayout rlayout_root;

    @BindView(R.id.view_pager)
    HackyViewPager view_pager;

    private int value_484848;
    private int pink_color;

    private ArrayList<ImageVideo> mImageVideos = new ArrayList<>();

    // 文件夹数据
    private ArrayList<FolderV2> mResultFolder = new ArrayList<>();
    /**
     * 选择文件结果地址列表
     */
    private ArrayList<String> mSelectResultList = new ArrayList<>();

    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";

    // 全部图片
    private static final int LOADER_IMAGE_ALL = 0;
    // 全部视频
    private static final int LOADER_VIDEO_ALL = 2;
    //录制视频请求码
    private static final int RECORD_REQUEST_CODE = 1000;

    private boolean isImage;
    private boolean isVideo;
    private HttpDialog httpDialog;
    private int maxCount;
    private String format = "完成(%d/%d)";
    private ListPopupWindow mFolderPopupWindow;
    private FolderAdapterV2 mFolderAdapter;
    private LoaderLocalImgVideoAdapter mImageVideoAdapter;
    private ImageCaptureManager captureManager;
    private AsyncTask<List<ImageVideo>, Void, List<ImageVideo>> asyncTask;

    public static void startAct(Activity context, int code, int maxCount) {
        Intent intent = new Intent(context, SelectPicVideoAct.class);
        intent.putExtra("maxCount", maxCount);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_pic_video;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        maxCount = getIntent().getIntExtra("maxCount", 0);
        //初始化
        pink_color = getColorResouce(R.color.pink_color);
        value_484848 = getColorResouce(R.color.value_484848);
        tv_complete.setText(String.format(format, 0, maxCount));
        httpDialog = new HttpDialog(this);
        httpDialog.show();
        state(1);
        // 首次加载所有图片
        getSupportLoaderManager().initLoader(LOADER_IMAGE_ALL, null, mLoaderImageCallback);
        getSupportLoaderManager().initLoader(LOADER_VIDEO_ALL, null, mLoaderVideoCallback);


        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 2);
        recy_view.addItemDecoration(new GridSpacingItemDecoration(i, false));


        mFolderAdapter = new FolderAdapterV2(this);
        mSelectResultList.clear();
    }

    @OnClick(R.id.btnAlbum)
    public void clickSelectAlbum() {
        if (mFolderPopupWindow == null) {
            createPopupFolderList();
        }
        if (mFolderPopupWindow.isShowing()) {
            mFolderPopupWindow.dismiss();
        } else {
            mFolderPopupWindow.show();
            btnAlbum.setSelected(true);
            int index = mFolderAdapter.getSelectIndex();
            index = index == 0 ? index : index - 1;
            mFolderPopupWindow.getListView().setSelection(index);
        }
    }

    @OnClick(R.id.tv_complete)
    public void complete() {
        if (!isEmpty(mSelectResultList)) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_RESULT, mSelectResultList);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Common.staticToast("请选择图片或者视频");
        }
    }

    @OnClick(R.id.mtv_album)
    public void clickAlbum() {
        state(1);
    }

    @OnClick(R.id.mtv_camera)
    public void clickCamera() {
        state(1);
        try {
            captureManager = new ImageCaptureManager(this);
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @OnClick(R.id.mtv_video)
    public void clickVideo() {
        state(1);
        RecordedActivity.startAct(this, RECORD_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager != null && captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        String currentPhotoPath = captureManager.getCurrentPhotoPath();
                        resultLocalUrl(currentPhotoPath);
                    }
                    break;
                case RECORD_REQUEST_CODE:
                    String video_path = data.getStringExtra("video_path");
                    resultLocalUrl(video_path);
                    break;
                case BrowseImageVideoAct.REQUEST_CODE:
                    ArrayList<String> pics_path = data.getStringArrayListExtra("data");
                    String video = data.getStringExtra("video");
                    //是否确认选择
                    boolean isComplete = data.getBooleanExtra("isComplete", false);
                    if (isComplete) {//图片
                        if (pics_path != null) {
                            if (mSelectResultList == null) {
                                mSelectResultList = new ArrayList<>();
                            }
                            mSelectResultList.addAll(pics_path);
                            complete();
                        } else {//视频
                            if (mSelectResultList == null) {
                                mSelectResultList = new ArrayList<>();
                            }
                            mSelectResultList.clear();
                            mSelectResultList.add(video);
                            complete();
                        }
                    } else {
                        if (pics_path != null) {
                            mSelectResultList.clear();
                            mSelectResultList.addAll(pics_path);
                            tv_complete.setText(String.format(format, mSelectResultList.size(), maxCount));
                        }
                    }
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isImage = isVideo = false;
        if (httpDialog != null && httpDialog.isShowing()) {
            httpDialog.dismiss();
        }
        httpDialog = null;
        if (mImageVideoAdapter != null) {
            mImageVideoAdapter.unbind();
            mImageVideoAdapter = null;
        }
        if (mFolderAdapter != null) {
            mFolderAdapter.destory();
            mFolderAdapter = null;
        }
        if (mImageVideos != null) {
            mImageVideos.clear();
            mImageVideos = null;
        }

        if (mResultFolder != null) {
            for (FolderV2 v2 : mResultFolder) {
                v2.cover = null;
                v2.name = null;
                v2.path = null;
                if (v2.images != null) {
                    v2.images.clear();
                    v2.images = null;
                }
            }
            mResultFolder.clear();
            mResultFolder = null;
        }
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        if (mFolderPopupWindow != null){
            mFolderPopupWindow.dismiss();
            mFolderPopupWindow.clearListSelection();
            mFolderPopupWindow = null;
        }
    }


    /**
     * 图片或者视频地址返回
     *
     * @param currentPhotoPath
     */
    private void resultLocalUrl(String currentPhotoPath) {
        if (mSelectResultList == null) {
            mSelectResultList = new ArrayList<>();
        }
        mSelectResultList.clear();
        mSelectResultList.add(currentPhotoPath);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_RESULT, mSelectResultList);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }




    private LoaderManager.LoaderCallbacks<Cursor> mLoaderImageCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {     //查询图片需要的数据列
                MediaStore.Images.Media.DATA,           //图片的真实路径  /storage/emulated/0/pp/downloader/wallpaper/aaa.jpg
                MediaStore.Images.Media.DATE_ADDED,      //图片被添加的时间，long型  1450518608
                MediaStore.Images.Media.DISPLAY_NAME,   //图片的显示名称  aaa.jpg
                //MediaStore.Images.Media.SIZE,           //图片的大小，long型  132492
                //MediaStore.Images.Media.WIDTH,          //图片的宽度，int型  1920
                //MediaStore.Images.Media.HEIGHT,         //图片的高度，int型  1080
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            isImage = false;
            //扫描所有图片
            CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    null, null, IMAGE_PROJECTION[1] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                while (data.moveToNext()) {
                    //查询数据
                    String imagePath = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    if (!isPicFile(imagePath)) {
                        continue;
                    }
                    File file = new File(imagePath);
                    if (!file.exists() || file.length() <= 0) {
                        continue;
                    }
                    String imageName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    long imageAddTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    //封装实体
                    ImageVideo imageItem = new ImageVideo();
                    imageItem.creatTime = imageAddTime;
                    imageItem.path = imagePath;
                    imageItem.name = imageName;
                    mImageVideos.add(imageItem);

                    mediaStoreCategory(imagePath, imageItem);
                }
                isImage = true;
                mergerMedia();

            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderVideoCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] VIDEO_PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DISPLAY_NAME,
                //MediaStore.Video.Media.WIDTH,
                //MediaStore.Video.Media.HEIGHT,
                //MediaStore.Video.Media.MIME_TYPE,
        };
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA};

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            isVideo = false;
            //扫描所有视频
            CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                    null, null, VIDEO_PROJECTION[4] + " DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            List<ImageVideo> mVideoList = new ArrayList<>();
            if (data != null) {
                while (data.moveToNext()) {

                    String id = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[0]));
                    String thumb_video = coverPath(id, thumbColumns);
                    //查询数据
                    String videoPath = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[1]));
                    long videoDuration = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[3]));
                    long video_size = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[2]));
                    if (!isEmpty(videoPath) && isMP4Path(videoPath)
                            && videoDuration < 16000 && video_size <= 20 * 1024 * 1024) {
                        String videoName = data.getString(data.getColumnIndexOrThrow(VIDEO_PROJECTION[5]));
                        long imageAddTime = data.getLong(data.getColumnIndexOrThrow(VIDEO_PROJECTION[4]));
                        ImageVideo imageVideo = new ImageVideo();
                        imageVideo.name = videoName;
                        imageVideo.videoDuration = videoDuration;
                        imageVideo.path = videoPath;
                        imageVideo.coverPath = thumb_video;
                        imageVideo.creatTime = imageAddTime;
                        mVideoList.add(imageVideo);
                        mImageVideos.add(imageVideo);
                        //mediaStoreCategory(videoPath, imageVideo);
                    }
                }
                loadCoverThread(mVideoList);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        }
    };


    /**
     * 相册列表
     */
    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(this);
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

        mFolderPopupWindow.setAnchorView(rlayout_root);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnDismissListener(() -> {
            if (btnAlbum != null) {
                btnAlbum.setSelected(false);
            }
        });
        mFolderPopupWindow.setOnItemClickListener((parent, view, position, id) -> {

            mFolderAdapter.setSelectIndex(position);

            new Handler().postDelayed(() -> {
                mFolderPopupWindow.dismiss();
                FolderV2 folder = mResultFolder.get(position);
                if (null != folder) {
                    if (mImageVideos != null) {
                        mImageVideos.clear();
                        mImageVideos.addAll(folder.images);
                    }
                    btnAlbum.setText(folder.name);
                    if (mImageVideoAdapter != null)
                        mImageVideoAdapter.notifyDataSetChanged();
                }
                // 滑动到最初始位置
                recy_view.smoothScrollToPosition(0);
            }, 100);
        });
    }

    /**
     * 媒体文件分类
     *
     * @param imagePath
     * @param imageItem
     */
    private void mediaStoreCategory(String imagePath, ImageVideo imageItem) {
        //根据父路径分类存放图片
        File imageFile = new File(imagePath);
        File imageParentFile = imageFile.getParentFile();
        FolderV2 folder = new FolderV2();
        folder.name = imageParentFile.getName();
        folder.path = imageParentFile.getAbsolutePath();

        if (!mResultFolder.contains(folder)) {
            ArrayList<ImageVideo> images = new ArrayList<>();
            images.add(imageItem);
            folder.cover = imageItem;
            folder.images = images;
            mResultFolder.add(folder);
        } else {
            mResultFolder.get(mResultFolder.indexOf(folder)).images.add(imageItem);
        }
    }

    /**
     * 合并媒体文件
     */
    private void mergerMedia() {
        if (isImage && isVideo) {
            if (httpDialog != null) {
                httpDialog.dismiss();
            }
            Collections.sort(mImageVideos);
            isVideo = isImage = false;

            /********将所有图片保存到一个类目******/
            if (!"图片和视频".equals(mResultFolder.get(0).name)) {
                List<ImageVideo> allFile = new ArrayList<>();
                allFile.addAll(mImageVideos);

                FolderV2 allFolder = new FolderV2();
                allFolder.name = "图片和视频";
                allFolder.images = allFile;
                allFolder.cover = allFile.get(0);
                mResultFolder.add(0, allFolder);
            }

            mFolderAdapter.setData(mResultFolder);

            if (mImageVideoAdapter == null) {
                mImageVideoAdapter = new LoaderLocalImgVideoAdapter(this, mImageVideos);
                recy_view.setAdapter(mImageVideoAdapter);
                mImageVideoAdapter.setOnItemClickListener((view, position) -> {
                    EventBus.getDefault().postSticky(mImageVideos);
                    BrowseImageVideoAct.BuildConfig config = new BrowseImageVideoAct.BuildConfig();
                    config.max_count = maxCount;
                    config.count = mSelectResultList.size();
                    config.position = position;
                    config.isShowImageVideo = false;
                    config.isOnlyBrowse = false;
                    config.selectResultList = mSelectResultList;
                    BrowseImageVideoAct.startAct(this, config, BrowseImageVideoAct.REQUEST_CODE);
                });
                mImageVideoAdapter.setOnSelectionListener((position, oldSelection) -> {
                    ImageVideo imageVideo = mImageVideos.get(position);
                    imageVideo.isSelect = !oldSelection;
                    selectHandler(position, !oldSelection, imageVideo);
                });
            } else {
                mImageVideoAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 处理选择
     * @return
     */
    private boolean selectHandler(int position, boolean oldSelection, ImageVideo imageVideo) {
        int count;
        if (mSelectResultList == null) {
            mSelectResultList = new ArrayList<>();
        }
        if (oldSelection) {
            mSelectResultList.add(imageVideo.path);
            if (isCanSelect() == -1) {
                mSelectResultList.remove(imageVideo.path);
                Common.staticToast("图片和视频不能同时选择");
                return false;
            } else if (isCanSelect() == -2) {
                mSelectResultList.remove(imageVideo.path);
                Common.staticToast("只能选择一个视频");
                return false;
            }
            count = mSelectResultList.size();
            if (count > maxCount) {
                mSelectResultList.remove(imageVideo.path);
                Common.staticToast(String.format("您最多只能选择%d张图片", maxCount));
                return false;
            }
        } else {
            mSelectResultList.remove(imageVideo.path);
            count = mSelectResultList.size();
        }
        if (count == 0) {
            tv_complete.setTextColor(getColorResouce(R.color.value_484848));
        } else {
            tv_complete.setTextColor(getColorResouce(R.color.pink_color));
        }
        tv_complete.setText(String.format(format, count, maxCount));

        mImageVideoAdapter.notifyItemChanged(position, imageVideo);
        return true;
    }

    /**
     * 加载系统数据库缩略图
     *
     * @param video_id
     * @param project
     * @return
     */
    private String coverPath(String video_id, String[] project) {
        /*********获取视频缩略图***********/
        Cursor thumbCursor = getContentResolver().query(
                MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                project, MediaStore.Video.Thumbnails.VIDEO_ID
                        + "=" + video_id, null, null);
        if (thumbCursor != null && thumbCursor.moveToNext()) {
            return thumbCursor.getString(thumbCursor.getColumnIndex(project[0]));
        }
        /****************end*********************/
        return "";
    }

    /**
     * 加工视频封面
     *
     * @param mVideoList
     */
    private void loadCoverThread(List<ImageVideo> mVideoList) {
        asyncTask = new AsyncTask<List<ImageVideo>, Void, List<ImageVideo>>() {
            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<ImageVideo> doInBackground(List<ImageVideo>[] mVideoList) {
                try {
                    for (ImageVideo iv : mVideoList[0]) {
                        if (iv.videoDuration != 0 && isEmpty(iv.coverPath)) {
                            mRetriever.setDataSource(iv.path);
                            Bitmap frameBitmap = mRetriever.getFrameAtTime();
                            iv.coverBitmap = frameBitmap;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return mVideoList[0];
            }

            @Override
            protected void onPostExecute(List<ImageVideo> aVoid) {
                super.onPostExecute(aVoid);
                //LogUtil.zhLogW("=====onPostExecute=========");
                if (!isEmpty(aVoid)) {
                    ImageVideo imageVideo = aVoid.get(0);
                    FolderV2 folder = new FolderV2();
                    folder.name = "所有视频";
                    folder.cover = imageVideo;
                    folder.images = aVoid;
                    if (mResultFolder != null)
                        mResultFolder.add(0, folder);
                }
                if (mRetriever != null) mRetriever.release();
                isVideo = true;
                if (mResultFolder != null)
                    mergerMedia();
            }
        };
        asyncTask.execute(mVideoList);
    }

    private void state(int state) {
        mtv_album.setTextColor(state == 1 ? pink_color : value_484848);
        mtv_album.setSelected(state == 1);

        mtv_camera.setTextColor(state == 2 ? pink_color : value_484848);
        mtv_camera.setSelected(state == 2);

        mtv_video.setTextColor(state == 3 ? pink_color : value_484848);
        mtv_video.setSelected(state == 3);
    }

    /**
     * 图片和视频不能一起选择,不能同时选这两个视频
     * @return -1 图片和视频不能一起选择  -2不能同时选这两个视频
     */
    public int isCanSelect() {
        if (!isEmpty(mSelectResultList)) {
            boolean isImage = false;
            boolean isVideo = false;
            int video_count = 0;
            for (String path : mSelectResultList) {
                if (isPicFile(path)) {
                    isImage = true;
                }
                if (isMP4Path(path)) {
                    isVideo = true;
                    video_count++;
                }
                if (isImage && isVideo) {
                    return -1;
                }
                if (video_count >= 2) {
                    return -2;
                }
            }
        }
        return 0;
    }

    /**
     * 判断是否是图片
     *
     * @param file
     * @return
     */
    public boolean isPicFile(String file) {
        if (isEmpty(file))return false;
        else return (file.toLowerCase().endsWith(".jpg")
                || file.toLowerCase().endsWith(".jpeg")
                || file.toLowerCase().endsWith(".png"));
    }

    /**
     * 判断是否是MP4文件路径
     * @param path
     * @return
     */
    private boolean isMP4Path(String path){
        if (isEmpty(path))return false;
        else return path.toLowerCase().endsWith(".mp4");
    }
}
