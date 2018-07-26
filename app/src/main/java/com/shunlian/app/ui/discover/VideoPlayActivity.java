package com.shunlian.app.ui.discover;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.shunlian.app.R;
import com.shunlian.app.adapter.OperateAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.VideoPlayEvent;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.HttpDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/23.
 */

public class VideoPlayActivity extends BaseActivity implements IChosenView {

    @BindView(R.id.customVideoPlayer)
    CustomVideoPlayer customVideoPlayer;

    @BindView(R.id.ll_rootView)
    RelativeLayout ll_rootView;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

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

    public static void startActivity(Context context, ArticleEntity.Article article) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra("article", article);
        context.startActivity(intent);
    }

    public List<String> itemList;
    public String currentUrl;
    public String currentPlaceHold;
    public HttpDialog httpDialog;
    private Dialog dialog_operate;
    public String dirName;
    private ArticleEntity.Article currentArticle;
    private boolean isShare;
    private ShareInfoParam mShareInfoParam;
    private ArrayList<String> imgList;
    private ChosenPresenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initData() {
        setHideStatusAndNavigation();

        mPresenter = new ChosenPresenter(this, this);

        EventBus.getDefault().register(this);
        dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        LogUtil.httpLogW("dirName:" + dirName);
        currentArticle = (ArticleEntity.Article) getIntent().getSerializableExtra("article");
        imgList = new ArrayList<>();
        if (!isEmpty(getIntent().getStringExtra("videoUrl"))) {
            currentUrl = getIntent().getStringExtra("videoUrl");
        } else if (currentArticle != null) {
            currentUrl = currentArticle.video_url;
            currentPlaceHold = currentArticle.thumb;
            imgList.add(currentArticle.thumb);
        }
        if (!isEmpty(getIntent().getStringExtra("placehold"))) {
            currentPlaceHold = getIntent().getStringExtra("placehold");
            imgList.add(currentPlaceHold);
        }

        if (!isEmpty(currentPlaceHold)) {
            GlideUtils.getInstance().loadImage(this, customVideoPlayer.thumbImageView, currentPlaceHold);
        }
        customVideoPlayer.setUp(currentUrl, CustomVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        customVideoPlayer.startVideo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        customVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(VideoPlayEvent event) {
        switch (event.action) {
            case VideoPlayEvent.FinishAction:
                finish();
                break;
            case VideoPlayEvent.MoreAction:
                initDialog();
                break;
            case VideoPlayEvent.CompleteAction:
                setHideStatusAndNavigation();
                break;
        }
    }

    public void initDialog() {
        if (itemList == null) {
            itemList = new ArrayList<>();
            itemList.add("保存视频");
            if (!isEmpty(imgList)) {
                itemList.add("图文分享");
            }
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
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    Common.staticToast("网络不可用");
                    return;
                }
                switch (itemList.get(position)) {
                    case "保存视频":
                        isShare = false;
                        readToDownLoad();
                        break;
                    case "图文分享":
                        isShare = true;
                        readToDownLoad();
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
            if (isShare) {
                shareArticle();
            } else {
                Toast.makeText(this, "已下载过该视频,请勿重复下载!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (httpDialog == null) {
                httpDialog = new HttpDialog(this);
            }
            if (!httpDialog.isShowing()) {
                httpDialog.show();
            }
            new Thread(() -> downLoadVideo(fileName)).start();
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
            saveVideoFile(fileName);
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveVideoFile(String fileDir) {
        //strDir视频路径
        Uri localUri = Uri.parse("file://" + fileDir);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        sendBroadcast(localIntent);
        Common.staticToast("视频已保存,请在手机相册中查看");
        if (isShare) {
            shareArticle();
        }
    }

    public void shareArticle() {
        if (mPresenter != null) {
            mShareInfoParam = mPresenter.getShareInfoParam();
            mShareInfoParam.title = currentArticle.title;
            mShareInfoParam.desc = currentArticle.full_title;
            mShareInfoParam.img = currentArticle.thumb;
            mShareInfoParam.thumb_type = currentArticle.thumb_type;
            if (!isEmpty(currentArticle.share_url)) {
                mShareInfoParam.shareLink = currentArticle.share_url;
                Common.copyText(this, mShareInfoParam.shareLink, mShareInfoParam.title, false);
                quick_actions.shareInfo(mShareInfoParam);
                quick_actions.saveshareGoodsPic();
            } else {
                mPresenter.getShareInfo(mPresenter.nice, currentArticle.id);
            }
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void getNiceList(ArticleEntity articleEntity, int currentPage, int totalPage) {

    }

    @Override
    public void likeArticle(String articleId) {

    }

    @Override
    public void unLikeArticle(String articleId) {

    }

    @Override
    public void getOtherTopics(List<ArticleEntity.Topic> topic_list) {

    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        if (quick_actions != null) {
            mShareInfoParam.userName = baseEntity.data.userName;
            mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
            mShareInfoParam.shareLink = baseEntity.data.shareLink;
            mShareInfoParam.desc = baseEntity.data.desc;
        }
    }
}