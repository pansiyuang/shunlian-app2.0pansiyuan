package com.shunlian.app.ui.discover_new;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.adapter.OperateAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.VideoPlayEvent;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.presenter.HotBlogPresenter;
import com.shunlian.app.presenter.HotVideoBlogPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.NetworkUtils;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SaveAlbumDialog;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.view.IHotBlogView;
import com.shunlian.app.view.IHotVideoBlogView;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.GoodVideoPlayer;
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

public class VideoGoodPlayActivity extends BaseActivity implements GoodVideoPlayer.updateParseAttent,IHotVideoBlogView {

    @BindView(R.id.customVideoPlayer)
    GoodVideoPlayer customVideoPlayer;

    @BindView(R.id.ll_rootView)
    RelativeLayout ll_rootView;

    public static void startActivity(Context context, BigImgEntity.Blog blog) {
        Intent intent = new Intent(context, VideoGoodPlayActivity.class);
        intent.putExtra("blog", blog);
        context.startActivity(intent);
    }
    public HttpDialog httpDialog;
    private  BigImgEntity.Blog blog;

    private HotVideoBlogPresenter hotBlogPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_video_play;
    }

    @Override
    protected void initData() {
        setHideStatusAndNavigation();

        hotBlogPresenter = new HotVideoBlogPresenter(this, this);
        EventBus.getDefault().register(this);
        blog = getIntent().getParcelableExtra("blog");

        if(blog!=null){
            customVideoPlayer.setGoodUserInfo(blog,this);
        }
        customVideoPlayer.setUp(blog.video, CustomVideoPlayer.SCREEN_WINDOW_NORMAL, "");
        customVideoPlayer.startVideo();
        httpDialog = new HttpDialog(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setHideStatusAndNavigation();
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
                break;
            case VideoPlayEvent.CompleteAction:
                setHideStatusAndNavigation();
                break;
        }
    }

    public void shareArticle() {
        if (!Common.isAlreadyLogin()) {
//            if(blog!=null&&blog.related_goods!=null&&blog.related_goods.size()>0) {
//                Common.goGoGo(this, "HTMLShare", "2","http://www.baidu.com", blog.related_goods.get(0).title,"",blog.related_goods.get(0).thumb);
//            }
        } else {
//            isShare = true;
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Override
    public void focusUser(int isFocus, String memberId) {
        blog.is_focus = isFocus==0?1:0;
        customVideoPlayer.setAttentStateView();
    }

    @Override
    public void parseBlog(int isAttent, String memberId) {
        blog.is_praise = isAttent;
        blog.praise_num =blog.praise_num+1;
        customVideoPlayer.setParseStateView();
    }

    @Override
    public void downCountSuccess() {
        blog.down_num =blog.down_num+1;
        customVideoPlayer.setDownLoadSuccess();
    }

    @Override
    public void showFailureView(int request_code) {
    }

    @Override
    public void showDataEmptyView(int request_code) {
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
    }

    @Override
    public void updateParse(boolean isParse) {
        //取消点赞和点赞(点赞目前不能取消)
        hotBlogPresenter.praiseBlos(blog.id);
    }

    @Override
    public void updateAttent(boolean isAttent) {
        //取消关注和关注
        hotBlogPresenter.focusUser(isAttent?1:0,blog.member_id);
    }

    @Override
    public void downVideo() {
        //上传下载成功
        hotBlogPresenter.downCount(blog.id);
    }

    @Override
    public void shareBolg() {
        //分享
        if(customVideoPlayer.checkDownLoadFileExists()) {
            shareArticle();
        }else{
            Common.staticToast("视频未下载，请先下载视频再分享！");
        }
    }

    @Override
    public void startGoodInfo() {
        if(blog!=null&&blog.related_goods!=null&&blog.related_goods.size()>0)
        GoodsDetailAct.startAct(this, blog.related_goods.get(0).goods_id);
    }

}
