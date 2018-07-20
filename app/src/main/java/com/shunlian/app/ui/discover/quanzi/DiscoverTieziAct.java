package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverHotAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PDiscoverTiezi;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IDiscoverTiezi;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiscoverTieziAct extends BaseActivity implements View.OnClickListener, IDiscoverTiezi, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.mtv_titles)
    MyTextView mtv_titles;

    @BindView(R.id.view_bg)
    View view_bg;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;


    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.miv_photo)
    MyImageView miv_photo;
    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;
    @BindView(R.id.rv_hot)
    RecyclerView rv_hot;
    @BindView(R.id.rv_new)
    RecyclerView rv_new;
    @BindView(R.id.mtv_attend)
    MyTextView mtv_attend;

    @BindView(R.id.mtv_remen)
    MyTextView mtv_remen;

    @BindView(R.id.mtv_zuixin)
    MyTextView mtv_zuixin;

    @BindView(R.id.view_remen)
    View view_remen;

    @BindView(R.id.view_zuixin)
    View view_zuixin;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private MessageCountManager messageCountManager;

    private PDiscoverTiezi pDiscoverTiezi;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverHotAdapter newAdapter;
    private String circle_id;

    public static void startAct(Context context, String circle_id) {
        Intent intent = new Intent(context, DiscoverTieziAct.class);
        intent.putExtra("circle_id", circle_id);
        context.startActivity(intent);
    }

    @OnClick(R.id.rl_more)
    public void more() {
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.findDetail("");
        if (pDiscoverTiezi != null)
            quick_actions.shareInfo(pDiscoverTiezi.getShareInfoParam());
    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(tv_msg_count);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(tv_msg_count);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected int getLayoutId() {
//        setHideStatus();
        return R.layout.act_discover_tiezi;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attend:
                DiscoverPublishPhotoAct.startAct(DiscoverTieziAct.this, circle_id);
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attend.setOnClickListener(this);
//        rv_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (linearLayoutManager != null) {
//                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
//
//                    }
//                }
//            }
//        });
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom && pDiscoverTiezi != null) {
                    pDiscoverTiezi.refreshBaby();
                }
                float alpha = ((float) y) / 250;
                if (y > 250) {
                    mtv_titles.setAlpha(1);
                    view_bg.setAlpha(1);
                } else if (y > 150) {
                    view_bg.setAlpha(alpha);
                    mtv_titles.setAlpha(alpha);
                    miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
                    miv_more.setImageResource(R.mipmap.icon_more_n);
                    miv_close.setAlpha(alpha);
                    miv_more.setAlpha(alpha);
                } else if (y > 0) {
                    view_bg.setAlpha(0);
                    mtv_titles.setAlpha(0);
                    miv_close.setAlpha(1 - alpha);
                    miv_more.setAlpha(1 - alpha);
                    miv_close.setImageResource(R.mipmap.icon_more_fanhui);
                    miv_more.setImageResource(R.mipmap.icon_more_gengduo);
                }
            }
        });
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        setStatusBarFontDark();
        circle_id = getIntent().getStringExtra("circle_id");
        pDiscoverTiezi = new PDiscoverTiezi(this, this, circle_id);
        view_bg.setAlpha(0);
        mtv_titles.setAlpha(0);
        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText(getString(R.string.discover_wolaishuo))
                .setButtonText(null);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(final DiscoveryTieziEntity.Mdata data, final List<DiscoveryTieziEntity.Mdata.Hot> mdatas) {
        gone(nei_empty);
        if (isEmpty(mdatas)) {
            gone(view_zuixin, rv_new,mtv_zuixin);
        } else {
            visible(view_zuixin, rv_new,mtv_zuixin);
        }
        if (isEmpty(data.hot_inv)) {
            gone(view_remen, rv_hot,mtv_remen);
        } else {
            visible(view_remen, rv_hot,mtv_remen);
        }
        if (isEmpty(mdatas) && isEmpty(data.hot_inv)) {
            gone(view_remen, rv_hot,rv_new,mtv_remen);
            visible(view_zuixin,mtv_zuixin,nei_empty);
        }
        if (newAdapter == null) {
            mtv_title.setText(data.topicDetail.title);
            mtv_titles.setText(data.topicDetail.title);
            mtv_desc.setText(data.topicDetail.content);
            GlideUtils.getInstance().loadImage(getBaseContext(), miv_photo, data.topicDetail.img);
            newAdapter = new DiscoverHotAdapter(getBaseContext(), true, mdatas, this);
            linearLayoutManager = new LinearLayoutManager(getBaseContext());

            rv_new.setLayoutManager(linearLayoutManager);
            rv_new.setNestedScrollingEnabled(false);
            rv_new.setAdapter(newAdapter);
            DiscoverHotAdapter hotAdapter = new DiscoverHotAdapter(getBaseContext(), false, data.hot_inv, this);
            LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_hot.setLayoutManager(mlinearLayoutManager);
            rv_hot.setNestedScrollingEnabled(false);
            rv_hot.setAdapter(hotAdapter);
            newAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this, circle_id, mdatas.get(position).id,mdatas.get(position).imgs);
                }
            });
            hotAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this, circle_id, data.hot_inv.get(position).id,mdatas.get(position).imgs);
                }
            });

        } else {
            newAdapter.notifyDataSetChanged();
        }
        newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
