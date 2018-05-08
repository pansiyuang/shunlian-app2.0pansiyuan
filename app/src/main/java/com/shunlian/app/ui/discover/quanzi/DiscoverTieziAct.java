package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverHotAdapter;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.presenter.PDiscoverTiezi;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IDiscoverTiezi;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class DiscoverTieziAct extends BaseActivity implements View.OnClickListener, IDiscoverTiezi {
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

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    private PDiscoverTiezi pDiscoverTiezi;
    private LinearLayoutManager linearLayoutManager;
    private DiscoverHotAdapter newAdapter;
    private String circle_id;

    public static void startAct(Context context, String circle_id) {
        Intent intent = new Intent(context, DiscoverTieziAct.class);
        intent.putExtra("circle_id", circle_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        setHideStatus();
        return R.layout.act_discover_tiezi;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attend:
                DiscoverPublishPhotoAct.startAct(DiscoverTieziAct.this, circle_id);
                break;
            case R.id.miv_more:
                quick_actions.setVisibility(View.VISIBLE);
                quick_actions.findDetail();
                if (pDiscoverTiezi != null)
                    quick_actions.shareInfo(pDiscoverTiezi.getShareInfoParam());
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attend.setOnClickListener(this);
        miv_more.setOnClickListener(this);
        rv_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {

                    }
                }
            }
        });
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
        circle_id = getIntent().getStringExtra("circle_id");
        pDiscoverTiezi = new PDiscoverTiezi(this, this, circle_id);
        view_bg.setAlpha(0);
        mtv_titles.setAlpha(0);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(final DiscoveryTieziEntity.Mdata data, final List<DiscoveryTieziEntity.Mdata.Hot> mdatas) {
        if (newAdapter == null) {
            mtv_title.setText(data.topicDetail.title);
            mtv_titles.setText(data.topicDetail.title);
            mtv_desc.setText(data.topicDetail.content);
            GlideUtils.getInstance().loadImage(getBaseContext(), miv_photo, data.topicDetail.img);
            newAdapter = new DiscoverHotAdapter(getBaseContext(), true, mdatas);
            linearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_new.setLayoutManager(linearLayoutManager);
            rv_new.setNestedScrollingEnabled(false);
            rv_new.setAdapter(newAdapter);
            DiscoverHotAdapter hotAdapter = new DiscoverHotAdapter(getBaseContext(), false, data.hot_inv);
            LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_hot.setLayoutManager(mlinearLayoutManager);
            rv_hot.setNestedScrollingEnabled(false);
            rv_hot.setAdapter(hotAdapter);
            newAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this, circle_id, mdatas.get(position).id);
                }
            });
            hotAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this, circle_id, data.hot_inv.get(position).id);
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
        super.onDestroy();
    }
}
