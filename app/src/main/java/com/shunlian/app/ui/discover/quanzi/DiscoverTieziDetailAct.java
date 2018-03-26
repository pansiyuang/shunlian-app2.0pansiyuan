package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.TieziAvarAdapter;
import com.shunlian.app.adapter.TieziCommentAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.presenter.PDiscoverTieziDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IDiscoverTieziDetail;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.TieziKanner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoverTieziDetailAct extends BaseActivity implements View.OnClickListener, IDiscoverTieziDetail {
    @BindView(R.id.kanner_tiezi)
    TieziKanner kanner_tiezi;

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

    @BindView(R.id.rv_avar)
    RecyclerView rv_avar;

    @BindView(R.id.rv_remark)
    RecyclerView rv_remark;

    private PDiscoverTieziDetail pDiscoverTieziDetail;
    private LinearLayoutManager linearLayoutManager;
    private boolean isLike;
    private String circle_id, inv_id;
    private TieziAvarAdapter avarAdapter;
    private List<String> avars;
    private TieziCommentAdapter commentAdapter;

    //    private DiscoverHotAdapter newAdapter;
    public static void startAct(Context context, String circle_id, String inv_id) {
        Intent intent = new Intent(context, DiscoverTieziDetailAct.class);
        intent.putExtra("circle_id", circle_id);
        intent.putExtra("inv_id", inv_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        setHideStatus();
        return R.layout.act_discover_tiezi_detail;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mtv_attend:
                DiscoverPublishPhotoAct.startAct(DiscoverTieziDetailAct.this, circle_id);
                break;
            case R.id.miv_like:
            case R.id.mtv_like:
                if (isLike) {
                    pDiscoverTieziDetail.dianZan(circle_id, inv_id, "2");
                } else {
                    pDiscoverTieziDetail.dianZan(circle_id, inv_id, "1");
                }
                break;
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_attend.setOnClickListener(this);
        mtv_like.setOnClickListener(this);
        miv_like.setOnClickListener(this);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom && pDiscoverTieziDetail != null) {
                    pDiscoverTieziDetail.refreshBaby();
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
        inv_id = getIntent().getStringExtra("inv_id");
        pDiscoverTieziDetail = new PDiscoverTieziDetail(this, this, circle_id, inv_id);
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
    public void setApiData(DiscoveryCommentListEntity.Mdata data, List<DiscoveryCommentListEntity.Mdata.Commentlist> mdatas) {
        if (commentAdapter == null) {
            avars = new ArrayList<>();
            avars.addAll(data.inv_info.five_member_likes);
            avarAdapter = new TieziAvarAdapter(getBaseContext(), false, avars);
            rv_avar.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_avar.addItemDecoration(new HorizonItemDecoration(TransformUtil.dip2px(getBaseContext(), -12)));
            rv_avar.setAdapter(avarAdapter);
            if (data.inv_info != null && data.inv_info.img != null) {
                kanner_tiezi.setBanner(data.inv_info.img);
                kanner_tiezi.setOnItemClickL(new BaseBanner.OnItemClickL() {
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
            commentAdapter = new TieziCommentAdapter(getBaseContext(),circle_id,inv_id, true, mdatas);
            linearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_remark.setLayoutManager(linearLayoutManager);
            rv_remark.setNestedScrollingEnabled(false);
            rv_remark.setAdapter(commentAdapter);
        } else {
            commentAdapter.notifyDataSetChanged();
        }
        commentAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));

    }

    @Override
    public void dianZan(CommonEntity data) {
        if (!isLike) {
            isLike = true;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
            mtv_like.setTextColor(getColorResouce(R.color.pink_color));
        } else {
            isLike = false;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_n);
            mtv_like.setTextColor(getColorResouce(R.color.value_88));
        }
        mtv_like.setText(String.valueOf(data.likes));
        avars.clear();
        avars.addAll(data.five_member_likes);
        avarAdapter.notifyDataSetChanged();
    }

}
