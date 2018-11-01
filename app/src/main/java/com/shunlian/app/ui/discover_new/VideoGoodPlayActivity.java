package com.shunlian.app.ui.discover_new;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverGoodsAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ShareInfoParam;
import com.shunlian.app.eventbus_bean.VideoPlayEvent;
import com.shunlian.app.presenter.HotVideoBlogPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.IHotVideoBlogView;
import com.shunlian.app.widget.CustomVideoPlayer;
import com.shunlian.app.widget.GoodVideoPlayer;
import com.shunlian.app.widget.HttpDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.NewTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/7/23.
 */

public class VideoGoodPlayActivity extends BaseActivity implements GoodVideoPlayer.updateParseAttent,IHotVideoBlogView {

    @BindView(R.id.customVideoPlayer)
    GoodVideoPlayer customVideoPlayer;
    @BindView(R.id.ll_rootView)
    RelativeLayout ll_rootView;
    private QuickActions quick_actions;
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
        quick_actions = new QuickActions(this);
        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.addView(quick_actions);
        quick_actions.setVisibility(View.INVISIBLE);

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
        if (quick_actions != null){
            quick_actions.shareStyle1Dialog();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
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
      if(blog.related_goods!=null&&blog.related_goods.size()>0){
          GoodsDeatilEntity.Goods goods = blog.related_goods.get(0);
          quick_actions.shareDiscoverDialog(goods.share_url,goods.title,goods.desc,goods.price,goods.goods_id,goods.thumb,
                  1==goods.isSuperiorProduct,  SharedPrefUtil.getSharedUserString("nickname", ""),
                  SharedPrefUtil.getSharedUserString("avatar", ""));
      }
    }

    @Override
    public void startGoodInfo() {
        if(blog!=null&&blog.related_goods!=null&&blog.related_goods.size()>0)
        GoodsDetailAct.startAct(this, blog.related_goods.get(0).goods_id);
    }

    @Override
    public void startListGoodInfo() {
        initDialog(blog);
    }


    public void initDialog(BigImgEntity.Blog blog) {
        Dialog dialog_new = new Dialog(this, R.style.popAd);
        dialog_new.setContentView(R.layout.dialog_found_goods);
        Window window = dialog_new.getWindow();
//        //设置边框距离
//        window.getDecorView().setPadding(0, 0, 0, 0);
        //设置dialog位置
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置宽高
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LayoutInflater inflater = LayoutInflater.from(this);
        View viewDialog = inflater.inflate(R.layout.dialog_found_goods, null);
        Display display = this.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
//        int height = display.getHeight();
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_new.setContentView(viewDialog, layoutParams);

        MyImageView miv_close = dialog_new.findViewById(R.id.miv_close);
        MyImageView miv_icon = dialog_new.findViewById(R.id.miv_icon);
        NewTextView ntv_desc = dialog_new.findViewById(R.id.ntv_desc);
        RecyclerView rv_goods = dialog_new.findViewById(R.id.rv_goods);
        miv_close.setOnClickListener(view -> dialog_new.dismiss());
        GlideUtils.getInstance().loadCircleImage(this, miv_icon, blog.avatar);
        SpannableStringBuilder ssb = Common.changeColor(blog.nickname
                + getString(R.string.discover_fenxiangdetuijian), blog.nickname, this.getResources().getColor(R.color.value_007AFF));
        ntv_desc.setText(ssb);
        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        DiscoverGoodsAdapter discoverGoodsAdapter = new DiscoverGoodsAdapter(this, blog.related_goods, false, quick_actions,
                SharedPrefUtil.getSharedUserString("nickname", ""), SharedPrefUtil.getSharedUserString("avatar", ""));
        rv_goods.setAdapter(discoverGoodsAdapter);
        discoverGoodsAdapter.setOnItemClickListener((view, position) -> GoodsDetailAct.startAct(this, blog.related_goods.get(position).goods_id));
        rv_goods.addItemDecoration(new MVerticalItemDecoration(this, 36, 38, 38));
        dialog_new.setCancelable(false);
        dialog_new.show();
    }

    @Override
    public void destoryVideo() {
        onBackPressed();
    }

}
