package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.presenter.PDiscoverTieziDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.view.IDiscoverTieziDetail;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.Kanner;

import java.util.List;

import butterknife.BindView;

public class DiscoverTieziDetailAct extends BaseActivity implements View.OnClickListener, IDiscoverTieziDetail {
    @BindView(R.id.kanner)
    Kanner kanner;

    @BindView(R.id.miv_avar)
    MyImageView miv_avar;

    @BindView(R.id.miv_like)
    MyImageView miv_like;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_like)
    MyTextView mtv_like;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.mtv_attend)
    MyTextView mtv_attend;

//    @BindView(R.id.rv_hot)
//    RecyclerView rv_hot;
//
//    @BindView(R.id.rv_new)
//    RecyclerView rv_new;

    private PDiscoverTieziDetail pDiscoverTieziDetail;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLike;
    private String circle_id;

    //    private DiscoverHotAdapter newAdapter;
    public static void startAct(Context context, String circle_id, String inv_id) {
        Intent intent = new Intent(context, DiscoverTieziDetailAct.class);
        intent.putExtra("circle_id", circle_id);
        intent.putExtra("inv_id", inv_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_discover_tiezi_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attend:
                DiscoverPublishPhotoAct.startAct(DiscoverTieziDetailAct.this, circle_id);
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
//                        if (pDiscoverTiezi != null) {
//                            pDiscoverTiezi.refreshBaby();
//                        }
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void initData() {
        circle_id = getIntent().getStringExtra("circle_id");
        String inv_id = getIntent().getStringExtra("inv_id");
        pDiscoverTieziDetail = new PDiscoverTieziDetail(this, this, circle_id, inv_id);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(DiscoveryCommentListEntity.Mdata data, List<DiscoveryCommentListEntity.Mdata.Commentlist> mdatas) {
//        if (newAdapter == null) {
        if (data.inv_info != null && data.inv_info.img != null) {
            kanner.setBanner(data.inv_info.img);
            kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                @Override
                public void onItemClick(int position) {

                }
            });

        }
        GlideUtils.getInstance().loadCircleImage(getBaseContext(), miv_avar, data.inv_info.author_info.avatar);
        mtv_name.setText(data.inv_info.author_info.nickname);
        mtv_time.setText(data.inv_info.create_time);
        mtv_like.setText(data.inv_info.likes);
        if ("1".equals(data.inv_info.is_likes)) {
            isLike = true;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
            mtv_like.setTextColor(getColorResouce(R.color.pink_color));
        } else {
            isLike = false;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_n);
            mtv_like.setTextColor(getColorResouce(R.color.value_88));
        }
        mtv_desc.setText(data.inv_info.content);
//        newAdapter = new DiscoverHotAdapter(getBaseContext(), true, mdatas);
//        linearLayoutManager = new LinearLayoutManager(getBaseContext());
//        rv_new.setLayoutManager(linearLayoutManager);
//        rv_new.setNestedScrollingEnabled(false);
//        rv_new.setAdapter(newAdapter);
//        DiscoverHotAdapter hotAdapter = new DiscoverHotAdapter(getBaseContext(), false, data.hot_inv);
//        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getBaseContext());
//        rv_hot.setLayoutManager(mlinearLayoutManager);
//        rv_hot.setNestedScrollingEnabled(false);
//        rv_hot.setAdapter(hotAdapter);
//        } else {
//            newAdapter.notifyDataSetChanged();
//        }
//        newAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));

    }
}
