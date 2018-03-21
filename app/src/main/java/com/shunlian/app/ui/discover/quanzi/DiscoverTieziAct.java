package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverHotAdapter;
import com.shunlian.app.adapter.StoreEvaluateAdapter;
import com.shunlian.app.bean.DiscoveryTieziEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.presenter.PDiscoverTiezi;
import com.shunlian.app.presenter.StoreIntroducePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.store.StoreLicenseAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IDiscoverTiezi;
import com.shunlian.app.view.StoreIntroduceView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class DiscoverTieziAct extends BaseActivity implements View.OnClickListener, IDiscoverTiezi {
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
        return R.layout.act_discover_tiezi;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attention:

                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_new.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pDiscoverTiezi != null) {
                            pDiscoverTiezi.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        circle_id=getIntent().getStringExtra("circle_id");
        pDiscoverTiezi = new PDiscoverTiezi(this, this, circle_id);
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
            mtv_desc.setText(data.topicDetail.content);
            GlideUtils.getInstance().loadImage(getBaseContext(),miv_photo,data.topicDetail.img);
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
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this,circle_id,data.new_inv.get(position).id);
                }
            });
            hotAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    DiscoverTieziDetailAct.startAct(DiscoverTieziAct.this,circle_id,mdatas.get(position).id);
                }
            });

        } else {
            newAdapter.notifyDataSetChanged();
        }
        newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));

    }
}
