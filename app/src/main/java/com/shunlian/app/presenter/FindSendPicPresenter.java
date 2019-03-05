package com.shunlian.app.presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapterV2;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BlogDraftEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.listener.SimpleNetDataCallback;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.ui.find_send.BrowseImageVideoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.ISelectPicVideoView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by zhanghe on 2018/10/22.
 */

public class FindSendPicPresenter extends BasePresenter<ISelectPicVideoView> {

    private Call<BaseEntity<CommonEntity>> sendBlogCall;
    private Call<BaseEntity<UploadPicEntity>> uploadMultiPicsCall;
    private Call<BaseEntity<UploadPicEntity>> uploadPicCall;
    private Call<BaseEntity<CommonEntity>> uploadVideoCall;
    private ArrayList<ImageVideo> mImgList = new ArrayList();
    private ArrayList<ImageVideo> mTempList = new ArrayList();
    private SingleImgAdapterV2 mImgAdapter;
    private int index = 0;//递归压缩图片下标
    private byte[] mBitmapBytes;
    private PromptDialog promptDialog;

    public FindSendPicPresenter(Context context, ISelectPicVideoView iView) {
        super(context, iView);
        attachView();
    }

    /**
     * 加载view
     */
    @Override
    public void attachView() {
        SingleImgAdapterV2.BuildConfig config = new SingleImgAdapterV2.BuildConfig();
        config.max_count = 9;
        config.pictureAndVideo = false;
        mImgAdapter = new SingleImgAdapterV2(context, mImgList, config);
        if (iView != null)iView.setAdapter(mImgAdapter);
        mImgAdapter.setOnItemClickListener((view, position) -> {
            if (position >= mImgList.size()) {
                mImgAdapter.selectPic();
            } else {
                EventBus.getDefault().postSticky(mImgList);
                BrowseImageVideoAct.BuildConfig config1 = new BrowseImageVideoAct.BuildConfig();
                config1.position = position;
                config1.isShowImageVideo = true;
                config1.isOnlyBrowse = true;
                BrowseImageVideoAct.startAct((Activity) context, config1, BrowseImageVideoAct.REQUEST_CODE);
            }
        });
    }

    /**
     * 卸载view
     */
    @Override
    public void detachView() {
        if (sendBlogCall != null && sendBlogCall.isExecuted()) {
            sendBlogCall.cancel();
        }
        sendBlogCall = null;

        if (uploadMultiPicsCall != null && uploadMultiPicsCall.isExecuted()) {
            uploadMultiPicsCall.cancel();
        }
        uploadMultiPicsCall = null;

        if (uploadPicCall != null && uploadPicCall.isExecuted()) {
            uploadPicCall.cancel();
        }
        uploadPicCall = null;

        if (uploadVideoCall != null && uploadVideoCall.isExecuted()) {
            uploadVideoCall.cancel();
        }
        uploadVideoCall = null;

        if (mImgAdapter != null){
            mImgAdapter.destory();
            mImgAdapter.unbind();
            mImgAdapter = null;
        }

        if (mImgList != null){
            mImgList.clear();
            mImgList = null;
        }
        if (mBitmapBytes != null){
            mBitmapBytes = null;
        }
        if (promptDialog != null){
            promptDialog.dismiss();
        }
    }

    /**
     * 处理网络请求
     * 获取草稿
     */
    @Override
    public void initApi() {
        Map<String, String> map = new HashMap<>();
        sortAndMD5(map);
        Call<BaseEntity<BlogDraftEntity>> getDraftCall = getApiService().getDraft(map);
        getNetData(true, getDraftCall, new SimpleNetDataCallback<BaseEntity<BlogDraftEntity>>() {
            @Override
            public void onSuccess(BaseEntity<BlogDraftEntity> entity) {
                super.onSuccess(entity);
                BlogDraftEntity data = entity.data;
                promptDialog = new PromptDialog((Activity) context);
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setCancelable(true);
                promptDialog.setTvSureColor(R.color.pink_color);
                promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                        .setSureAndCancleListener("上次有未发布的内容，是否加载？",
                                "确定", v -> loadDraft(data),
                                "取消", v ->
                                    promptDialog.dismiss()).show();
            }
        });

    }
    //加载草稿
    private void loadDraft(BlogDraftEntity data) {
        if (promptDialog != null)promptDialog.dismiss();
        iView.resetDraft(data);

        if (!isEmpty(data.video)) {
            ImageVideo imageVideo = new ImageVideo();
            imageVideo.path = data.video;
            imageVideo.coverPath = data.video_thumb;
            mImgList.add(imageVideo);
            mImgAdapter.notifyDataSetChanged();
        } else if (!isEmpty(data.pics)) {
            for (String url : data.pics) {
                ImageVideo imageVideo = new ImageVideo();
                imageVideo.path = url;
                imageVideo.url = url;
                mImgList.add(imageVideo);
            }
            mImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 上传多张图片
     *
     * @param filePath
     * @param uploadPath
     */
    public void uploadPic(List<ImageVideo> filePath, final String uploadPath) {
        if (isEmpty(filePath)) {
            return;
        }
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (int i = 0; i < filePath.size(); i++) {
            File file = filePath.get(i).file;
            if (file == null) continue;
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file[]", file.getName(), requestBody);
            parts.add(part);
        }


        Map<String, String> map = new HashMap<>();
        map.put("path_name", uploadPath);
        sortAndMD5(map);

        uploadMultiPicsCall = getAddCookieApiService().uploadPic(parts, map);
        getNetData(true, uploadMultiPicsCall, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                if (uploadPicEntity != null) {
                    for (int i = 0; i < uploadPicEntity.relativePath.size(); i++) {
                        mTempList.get(i).url = uploadPicEntity.relativePath.get(i);
                    }
                    mImgList.addAll(mTempList);
                    if (mImgAdapter != null)
                        mImgAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onErrorCode(int code, String message) {
                super.onErrorCode(code, message);
                if (mTempList != null) mTempList.clear();
                if (mImgAdapter != null)
                    mImgAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 上传视频封面
     *
     * @param bytes
     */
    public void uploadVideoThumb(byte[] bytes) {
        mBitmapBytes = bytes;
        List<MultipartBody.Part> parts = new ArrayList<>();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), bytes);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", "video_thumb.png", requestBody);
        parts.add(part);

        Map<String, String> map = new HashMap<>();
        map.put("path_name", "video_thumb");
        sortAndMD5(map);

        uploadPicCall = getAddCookieApiService().uploadPic(parts, map);
        getNetData(true, uploadPicCall, new SimpleNetDataCallback<BaseEntity<UploadPicEntity>>() {
            @Override
            public void onSuccess(BaseEntity<UploadPicEntity> entity) {
                super.onSuccess(entity);
                UploadPicEntity uploadPicEntity = entity.data;
                if (!isEmpty(uploadPicEntity.relativePath)) {
                    iView.videoThumb(uploadPicEntity.relativePath.get(0));
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                uploadVideoThumb(mBitmapBytes);
            }
        });
    }

    /**
     * 上传视频
     * @param videoPath
     */
    public void uploadVideo(String videoPath) {
        File file = new File(videoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("video/*"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        uploadVideoCall = getAddCookieApiService().uploadVideo(part);

        getNetData(true, uploadVideoCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                String url = entity.data.name;
                iView.uploadViodeSuccess(url, videoPath);
                if (mImgAdapter != null) {
                    ImageVideo imageVideo = new ImageVideo();
                    imageVideo.path = videoPath;
                    imageVideo.url = url;
                    mImgList.add(imageVideo);
                    mImgAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 发布
     */
    public void publish(String text, String pics, String video, String video_thumb,
                        String activity_id, String place,
                        String related_goods, String draft) {
        Map<String, String> map = new HashMap<>();
        map.put("text", text);
        if (!isEmpty(pics)) {
            map.put("type", "1");
            map.put("pics", pics);
        } else if (!isEmpty(video)) {
            map.put("type", "2");
            map.put("video_thumb", video_thumb);
            map.put("video", video);
        }
        if (!isEmpty(activity_id)) {
            map.put("activity_id", activity_id);
        }
        if (!isEmpty(place)) {
            map.put("place", place);
        }
        if (!isEmpty(related_goods)) {
            map.put("related_goods", related_goods);
        }
        if (!isEmpty(draft)) {
            map.put("draft", draft);
        }
        sortAndMD5(map);

        sendBlogCall = getAddCookieApiService().pubishBlog(getRequestBody(map));

        getNetData(true, sendBlogCall, new SimpleNetDataCallback<BaseEntity<CommonEntity>>() {
            @Override
            public void onSuccess(BaseEntity<CommonEntity> entity) {
                super.onSuccess(entity);
                iView.publishSuccess(draft);
                Common.staticToast(context,entity.message);
            }
        });
    }




    /***
     * 压缩图片
     */
    public void reducePics(List<String> list){
        if (isEmpty(list))return;
        index = 0;
        if (mTempList == null)
            mTempList = new ArrayList<>();
        mTempList.clear();
        compress(index,list);
    }

    /**
     * 递归压缩图片
     * @param post
     * @param list
     */
    private void compress(int post,List<String> list){
        Luban.with(context).load(list.get(post)).putGear(3).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                LogUtil.httpLogW("递归  开始压缩图片"+list);
            }
            @Override
            public void onSuccess(File file) {
                ImageVideo imageEntity = new ImageVideo();
                imageEntity.path = list.get(index);
                imageEntity.file = file;
                //mImgList.add(imageEntity);
                mTempList.add(imageEntity);
                index++;
                if (index < list.size()){
                    compress(index,list);
                }else {
                    uploadPic(mTempList, "find_send");//上传图片
                }
            }
            @Override
            public void onError(Throwable e) {
                Common.staticToast(context,"上传图片失败");
            }
        }).launch();
    }

    public String getpicsPath() {
        if (!isEmpty(mImgList)) {
            StringBuilder sb = new StringBuilder();
            for (ImageVideo e : mImgList) {
                if (!isMP4Path(e.url)) {
                    sb.append(e.url);
                    sb.append(",");
                }
            }
            if (sb.length() > 1) {
                return sb.toString().substring(0, sb.length() - 1);
            }
        }
        return "";
    }

    /**
     * 判断是否是MP4文件路径
     *
     * @param path
     * @return
     */
    private boolean isMP4Path(String path) {
        if (isEmpty(path)) return false;
        else return path.toLowerCase().endsWith(".mp4");
    }

    public ArrayList<ImageVideo> getmImgList() {
        return mImgList;
    }
}
