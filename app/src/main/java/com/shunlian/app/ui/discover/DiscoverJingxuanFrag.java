package com.shunlian.app.ui.discover;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ArticleAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.ArticleEntity;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.ArticleEvent;
import com.shunlian.app.eventbus_bean.DefMessageEvent;
import com.shunlian.app.presenter.ChosenPresenter;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IChosenView;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

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


public class DiscoverJingxuanFrag extends DiscoversFrag implements IChosenView, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.recycler_article)
    RecyclerView recycler_article;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout lay_refresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.nsv_bootom)
    NestedScrollView nsv_bootom;

    private ArticleAdapter mArticleAdapter;
    private ChosenPresenter mPresenter;
    private List<ArticleEntity.Tag> mTags;
    private List<ArticleEntity.Article> mArticleList;
    private List<String> imgList;
    private int mIndex = 1;
    private LinearLayoutManager articleManager;
    private QuickActions quick_actions;
    private PromptDialog promptDialog;
    private HttpDialog httpDialog;
    private ShareInfoParam mShareInfoParam;
    private String dirName;
    private String mArticleId;
    private ArticleEntity.Article currentArticle;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_jingxuan, container, false);
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        NestedSlHeader header = new NestedSlHeader(baseContext);
        lay_refresh.setRefreshHeaderView(header);

        dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();

        articleManager = new LinearLayoutManager(getActivity());
        recycler_article.setLayoutManager(articleManager);
        ((SimpleItemAnimator) recycler_article.getItemAnimator()).setSupportsChangeAnimations(false);

        mPresenter = new ChosenPresenter(getActivity(), this);
        mPresenter.getArticleList(true);
        mTags = new ArrayList<>();
        mArticleList = new ArrayList<>();
        imgList = new ArrayList<>();
        mShareInfoParam = mPresenter.getShareInfoParam();
        httpDialog = new HttpDialog(getActivity());
        //分享
        quick_actions = new QuickActions(baseActivity);
        ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);

        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText("暂时没有用户发布精选文章")
                .setButtonText(null);
    }

    @Override
    protected void initListener() {
        lay_refresh.setOnRefreshListener(() -> {
            if (mPresenter != null) {
                mPresenter.initPage();
                mPresenter.getArticleList(true);
            }
        });

        recycler_article.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (articleManager != null) {
                    int lastPosition = articleManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == articleManager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });

        super.initListener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(ArticleEvent event) {
        if (!isEmpty(event.articleId) && !isEmpty(event.isLike)) {
            upDateLikeArticle(event.articleId);
            if ("1".equals(event.isLike)) {
            } else {
                upDateUnLikeArticle(event.articleId);
            }
        }
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void showFailureView(int request_code) {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void getNiceList(ArticleEntity articleEntity, int currentPage, int totalPage) {
        if (currentPage == 1) {
            mTags.clear();
            mArticleList.clear();
            if (!isEmpty(articleEntity.tag_list)) {
                mTags.addAll(articleEntity.tag_list);
            }

            if (!isEmpty(articleEntity.article_list)) {
                List<ArticleEntity.Topic> topicList = articleEntity.topic_list;
                int index = 0;
                if (articleEntity.article_list.size() >= 5) {
                    index = 4;
                } else if (articleEntity.article_list.size() < 5) {
                    index = articleEntity.article_list.size() - 1;
                }
                articleEntity.article_list.get(index).topic_list = topicList;
                recycler_article.setVisibility(View.VISIBLE);
                nsv_bootom.setVisibility(View.GONE);
            } else {
                recycler_article.setVisibility(View.GONE);
                nsv_bootom.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(articleEntity.article_list)) {
            mArticleList.addAll(articleEntity.article_list);
        }
        if (mArticleAdapter == null) {
            mArticleAdapter = new ArticleAdapter(getActivity(), mArticleList, this, mTags);
            mArticleAdapter.setOnItemClickListener(this);
            recycler_article.setAdapter(mArticleAdapter);
        }
        mArticleAdapter.setPageLoading(currentPage, totalPage);
        mArticleAdapter.notifyDataSetChanged();
    }

    //分享文章方法
    public void shareArticle(int position) {
        if (!Common.isAlreadyLogin()) {
            Common.goGoGo(baseActivity, "login");
            return;
        }
        currentArticle = mArticleList.get(position - 1);
        switch (currentArticle.thumb_type) {
            case "0"://小图模式
            case "1"://大图模式
                getShareInfo();
                break;
            case "2"://九宫格模式
                shareArticle();
                break;
            case "3"://小视频模式
                readToDownLoad(currentArticle.video_url);
                break;
        }
    }

    public void savePic(String shareUrl) {
        try {
            imgList.clear();
            for (String s : currentArticle.thumb_list) {
                imgList.add(s);
            }
            DownLoadImageThread thread = new DownLoadImageThread(getActivity(), (ArrayList<String>) imgList);
            thread.start();
            Common.copyText(getActivity(), shareUrl, currentArticle.title, false);
            if (promptDialog == null) {
                promptDialog = new PromptDialog(getActivity());
            }
            promptDialog.setSureAndCancleListener(getString(R.string.discover_xindewenzi),
                    getString(R.string.discover_xindetupian), "", getString(R.string.discover_quweixinfenxiang), v -> {
                        Common.openWeiXin(getActivity(), "", "");
                        promptDialog.dismiss();
                    }, getString(R.string.errcode_cancel), v -> promptDialog.dismiss());
            promptDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginRefresh(DefMessageEvent event) {
        if (event.loginSuccess && mPresenter != null) {
            mPresenter.getShareInfo(mPresenter.nice, mArticleId);
        }
    }

    private void getShareInfo() {
        if (mPresenter != null) {
            mShareInfoParam.title = currentArticle.title;
            mShareInfoParam.desc = currentArticle.full_title;
            mShareInfoParam.img = currentArticle.thumb;
            mShareInfoParam.thumb_type = currentArticle.thumb_type;
            mArticleId = currentArticle.id;
            if (!isEmpty(currentArticle.share_url)) {
                mShareInfoParam.shareLink = currentArticle.share_url;
                share();
            } else {
                mPresenter.getShareInfo(mPresenter.nice, currentArticle.id);
            }
        }
    }

    private void share() {
        if (quick_actions != null) {
            visible(quick_actions);
            quick_actions.shareInfo(mShareInfoParam);
            quick_actions.shareStyle2Dialog(true, 2, "article", mArticleId);
        }
    }

    @Override
    public void shareInfo(BaseEntity<ShareInfoParam> baseEntity) {
        mShareInfoParam.shareLink = baseEntity.data.shareLink;
        mShareInfoParam.userAvatar = baseEntity.data.userAvatar;
        mShareInfoParam.userName = baseEntity.data.userName;

        if (httpDialog.isShowing()) {
            httpDialog.dismiss();
        }
        switch (currentArticle.thumb_type) {
            case "2":
                savePic(mShareInfoParam.shareLink);
                break;
            case "3"://小视频分享
                Common.copyText(getActivity(), mShareInfoParam.shareLink, currentArticle.title, false);
                if (promptDialog == null) {
                    promptDialog = new PromptDialog(getActivity());
                }
                promptDialog.setSureAndCancleListener(getString(R.string.discover_articlevideo),
                        getString(R.string.discover_articleurl), "", getString(R.string.discover_quweixinfenxiang), v -> {
                            Common.openWeiXin(getActivity(), "", "");
                            promptDialog.dismiss();
                        }, getString(R.string.errcode_cancel), v -> promptDialog.dismiss());
                promptDialog.show();
                break;
            default:
                LogUtil.httpLogW("分享文章");
                share();
                break;
        }
    }

    public void toLikeArticle(String articleId) {
        mPresenter.articleLike(articleId);
    }

    public void toUnLikeArticle(String articleId) {
        mPresenter.articleUnLike(articleId);
    }

    public void toGetOtherTopiscList() {
        mPresenter.getOthersTopicList(String.valueOf(mIndex));
    }

    @Override
    public void likeArticle(String articleId) {
        upDateLikeArticle(articleId);
    }

    @Override
    public void unLikeArticle(String articleId) {
        upDateUnLikeArticle(articleId);
    }

    public void upDateLikeArticle(String articleId) {
        for (int i = 0; i < mArticleList.size(); i++) {
            ArticleEntity.Article article = mArticleList.get(i);
            if (articleId.equals(mArticleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) + 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mArticleAdapter.updateEvaluate(articleId, "1");
    }

    public void upDateUnLikeArticle(String articleId) {
        for (int i = 0; i < mArticleList.size(); i++) {
            ArticleEntity.Article article = mArticleList.get(i);
            if (articleId.equals(mArticleList.get(i).id)) {
                int likeCount = Integer.valueOf(article.likes) - 1;
                article.likes = likeCount + "";
                break;
            }
        }
        mArticleAdapter.updateEvaluate(articleId, "0");
    }

    @Override
    public void getOtherTopics(List<ArticleEntity.Topic> topic_list) {
        mArticleAdapter.notityTopicData(topic_list);
        mIndex++;
    }

    @Override
    public void onItemClick(View view, int position) {
        ArticleEntity.Article article = mArticleList.get(position - 1);
        ArticleH5Act.startAct(getActivity(), article.id, ArticleH5Act.MODE_SONIC);
    }

    /**
     * 刷新完成
     */
    @Override
    public void refreshFinish() {
        if (lay_refresh != null) {
            lay_refresh.setRefreshing(false);
        }
    }

    public void readToDownLoad(String videoUrl) {
        File file = new File(dirName);
        // 文件夹不存在时创建
        if (!file.exists()) {
            file.mkdir();
        }
        // 下载后的文件名
        int i = videoUrl.lastIndexOf("/"); // 取的最后一个斜杠后的字符串为名
        String fileName = dirName + videoUrl.substring(i, videoUrl.length());
        File file1 = new File(fileName);

        if (!httpDialog.isShowing()) {
            httpDialog.show();
        }
        if (file1.exists()) {
            shareArticle();
        } else {
            new Thread(() -> downLoadVideo(fileName)).start();
        }
    }

    public void downLoadVideo(String fileName) {
        try {
            URL url = new URL(currentArticle.video_url);
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

    public void shareArticle() {
        if (mPresenter != null) {
            mPresenter.getShareInfo(mPresenter.nice, currentArticle.id);
        }
    }

    public void saveVideoFile(String fileDir) {
        //strDir视频路径
        Uri localUri = Uri.parse("file://" + fileDir);
        Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
        getActivity().sendBroadcast(localIntent);
        shareArticle();
    }

    @Override
    public void onDestroyView() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        EventBus.getDefault().unregister(this);
    }
}
