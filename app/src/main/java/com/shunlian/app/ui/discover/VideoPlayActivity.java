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
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.SaveAlbumDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.circle.RoundRectImageView;

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
        dirName = Environment.getExternalStorageDirectory() + "/" + getPackageName();
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
            Common.staticToast("视频保存失败");
        }
    }

    public void saveVideoFile(String fileDir) {
        //strDir视频路径
        Uri localUri = Uri.parse("file://" + fileDir);
        Intent localIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        localIntent.setData(localUri);
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
                saveshareFindPic();
            } else {
                mPresenter.getShareInfo(mPresenter.nice, currentArticle.id);
            }
        }
    }

    private void saveshareFindPic() {
        final View inflate = LayoutInflater.from(this).inflate(R.layout.share_find, null, false);
        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        inflate.setLayoutParams(layoutParams1);

        CircleImageView miv_user_head = (CircleImageView) inflate.findViewById(R.id.miv_user_head);

        MyTextView mtv_nickname = (MyTextView) inflate.findViewById(R.id.mtv_nickname);
        mtv_nickname.setText("来自" + mShareInfoParam.userName + "的分享");

        int i = TransformUtil.countRealWidth(this, 320);
        Bitmap qrImage = BitmapUtil.createQRImage(mShareInfoParam.shareLink, null, i);
        MyImageView miv_code = (MyImageView) inflate.findViewById(R.id.miv_code);
        miv_code.setImageBitmap(qrImage);


        MyTextView mtv_title = (MyTextView) inflate.findViewById(R.id.mtv_title);
        mtv_title.setText(mShareInfoParam.title);

        MyTextView mtv_desc = (MyTextView) inflate.findViewById(R.id.mtv_desc);
        if (TextUtils.isEmpty(mShareInfoParam.desc)) {
            mtv_desc.setVisibility(View.GONE);
        } else {
            mtv_desc.setVisibility(View.VISIBLE);
            mtv_desc.setText(mShareInfoParam.desc);
        }

        RoundRectImageView miv_bigpic = (RoundRectImageView) inflate.findViewById(R.id.miv_bigpic);
        MyImageView miv_smallpic = (MyImageView) inflate.findViewById(R.id.miv_smallpic);

        int[] ints = TransformUtil.countRealWH(this, 640, 300);
        ViewGroup.LayoutParams layoutParams = miv_bigpic.getLayoutParams();
        layoutParams.width = ints[0];
        layoutParams.height = ints[1];

        GlideUtils.getInstance().loadBitmapSync(this, mShareInfoParam.userAvatar,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        miv_user_head.setImageBitmap(resource);
                        findPic(inflate, miv_bigpic, miv_smallpic);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        miv_user_head.setImageResource(R.mipmap.img_set_defaulthead);
                        findPic(inflate, miv_bigpic, miv_smallpic);
                    }
                });
    }

    private void findPic(View inflate, RoundRectImageView miv_bigpic, MyImageView miv_smallpic) {
        GlideUtils.getInstance().loadBitmapSync(this, mShareInfoParam.img,
                new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if ("1".equals(mShareInfoParam.thumb_type)) {//显示大图
                            miv_bigpic.setVisibility(View.VISIBLE);
                            miv_smallpic.setVisibility(View.GONE);
                            miv_bigpic.setImageBitmap(resource);
                        } else {
                            miv_bigpic.setVisibility(View.GONE);
                            miv_smallpic.setVisibility(View.VISIBLE);
                            miv_smallpic.setImageBitmap(resource);
                        }
                        savePic(inflate);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Common.staticToast("分享失败");
                    }
                });
    }

    private void savePic(View inflate) {
        inflate.postDelayed(() -> {
            Bitmap bitmapByView = getBitmapByView(inflate);
            boolean isSuccess = BitmapUtil.saveImageToAlbumn(this, bitmapByView);
            if (isSuccess) {
                SaveAlbumDialog dialog = new SaveAlbumDialog((Activity) this, "article", currentArticle.id);
                dialog.show();
            } else {
                Common.staticToast("分享失败");
            }
        }, 100);
    }

    public Bitmap getBitmapByView(View view) {
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
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
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
        mShareInfoParam.userName = baseEntity.data.userName;
        saveshareFindPic();
    }
}
