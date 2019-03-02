package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.HotsaleAdapter;
import com.shunlian.app.adapter.KouBeiHomeAdapter;
import com.shunlian.app.adapter.KoubeiMenuAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.bean.KoubeiSecondEntity;
import com.shunlian.app.presenter.PKoubei;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IKoubei;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.NewTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class KouBeiActNew extends BaseActivity implements View.OnClickListener, IKoubei {

    @BindView(R.id.rv_categoryMenu)
    RecyclerView rv_categoryMenu;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;

    @BindView(R.id.rv_mid)
    RecyclerView rv_mid;

    @BindView(R.id.ntv_ltop)
    NewTextView ntv_ltop;

    @BindView(R.id.ntv_lmid)
    NewTextView ntv_lmid;

    @BindView(R.id.miv_ltop)
    MyImageView miv_ltop;

    @BindView(R.id.miv_lmid)
    MyImageView miv_lmid;

    @BindView(R.id.ntv_mtop)
    NewTextView ntv_mtop;

    @BindView(R.id.ntv_mmid)
    NewTextView ntv_mmid;

    @BindView(R.id.miv_mtop)
    MyImageView miv_mtop;

    @BindView(R.id.miv_mmid)
    MyImageView miv_mmid;

    @BindView(R.id.ntv_rtop)
    NewTextView ntv_rtop;

    @BindView(R.id.ntv_rmid)
    NewTextView ntv_rmid;

    @BindView(R.id.miv_rtop)
    MyImageView miv_rtop;

    @BindView(R.id.miv_rmid)
    MyImageView miv_rmid;

    @BindView(R.id.mllayout_title)
    MyLinearLayout mllayout_title;

    @BindView(R.id.store_abLayout)
    AppBarLayout store_abLayout;

    @BindView(R.id.nsv_outs)
    NestedScrollView nsv_outs;

    private PKoubei pKoubei;
    private HotsaleAdapter hotsaleAdapter;
    private List<HotsaleEntity.Suspension> midList = new ArrayList<>();


    public static void startAct(Context context) {
        Intent intent = new Intent(context, KouBeiActNew.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_kou_bei_new;
    }

    @Override
    protected void initListener() {
        super.initListener();
        store_abLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (0 <= verticalOffset) {
                    mllayout_title.setBackgroundResource(R.mipmap.img_pingjia_toutu_s);
                } else {
                    mllayout_title.setBackgroundResource(R.mipmap.img_koubei_se);
                }
            }
        });
    }

    @Override
    protected void initData() {
        immersionBar.statusBarView(R.id.view_state).init();
        pKoubei = new PKoubei(this, this);
        pKoubei.getHomedata();

    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {
//        visible(nsv_out);
//        gone(rv_category);
    }

    @Override
    public void setHomeData(HotsaleHomeEntity hotsaleHomeEntity) {
        try {
            KoubeiMenuAdapter koubeiMenuAdapter = new KoubeiMenuAdapter(baseAct, false, hotsaleHomeEntity.cate_list);
            koubeiMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    koubeiMenuAdapter.selectedPosition = position;
                    koubeiMenuAdapter.notifyDataSetChanged();
                    if (rv_mid.getScrollState() == 0 && position >= 0) {
                        pKoubei.getHotsaleCate(hotsaleHomeEntity.cate_list.get(position).cate_id);
                        nsv_outs.scrollTo(0, 0);
                    }
                }
            });
            rv_categoryMenu.setNestedScrollingEnabled(false);
            rv_categoryMenu.addItemDecoration(new MHorItemDecoration(baseAct, 20, 16, 16));
            rv_categoryMenu.setAdapter(koubeiMenuAdapter);
            rv_categoryMenu.setLayoutManager(new LinearLayoutManager(baseAct, LinearLayoutManager.HORIZONTAL, false));
            initRvMid(hotsaleHomeEntity.cate_list.get(0).list);

            if (!isEmpty(hotsaleHomeEntity.hot_list)) {
                if (hotsaleHomeEntity.hot_list.get(0) != null) {
                    HotsaleHomeEntity.Suspension left = hotsaleHomeEntity.hot_list.get(0);
                    GlideUtils.getInstance().loadImageZheng(baseAct, miv_ltop, left.list.get(0).thumb);
                    GlideUtils.getInstance().loadImageZheng(baseAct, miv_lmid, left.list.get(1).thumb);
                    ntv_ltop.setText(left.cate_name);
                    ntv_lmid.setText(left.comments);
                    miv_ltop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, left.cate_id);
                        }
                    });
                    miv_lmid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, left.cate_id);
                        }
                    });
                }
                if (hotsaleHomeEntity.hot_list.get(1) != null) {
                    HotsaleHomeEntity.Suspension mid = hotsaleHomeEntity.hot_list.get(1);
                    if (!isEmpty(mid.list) && mid.list.size() >= 2) {
                        GlideUtils.getInstance().loadImageZheng(baseAct, miv_mtop, mid.list.get(0).thumb);
                        GlideUtils.getInstance().loadImageZheng(baseAct, miv_mmid, mid.list.get(1).thumb);
                    }
                    ntv_mtop.setText(mid.cate_name);
                    ntv_mmid.setText(mid.comments);
                    miv_mtop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, mid.cate_id);
                        }
                    });
                    miv_mmid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, mid.cate_id);
                        }
                    });
                }

                if (hotsaleHomeEntity.hot_list.get(2) != null) {
                    HotsaleHomeEntity.Suspension right = hotsaleHomeEntity.hot_list.get(2);
                    GlideUtils.getInstance().loadImageZheng(baseAct, miv_rtop, right.list.get(0).thumb);
                    GlideUtils.getInstance().loadImageZheng(baseAct, miv_rmid, right.list.get(1).thumb);
                    ntv_rtop.setText(right.cate_name);
                    ntv_rmid.setText(right.comments);
                    miv_rtop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, right.cate_id);
                        }
                    });
                    miv_rmid.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            KouBeiActNews.startAct(baseAct, right.cate_id);
                        }
                    });
                }
            }
            KouBeiHomeAdapter kouBeiHomeAdapter = new KouBeiHomeAdapter(baseAct, false, hotsaleHomeEntity.like_list);
            rv_category.setLayoutManager(new GridLayoutManager(baseAct, 2));
            rv_category.setNestedScrollingEnabled(false);
            rv_category.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseAct, 9), false));
            rv_category.setAdapter(kouBeiHomeAdapter);
            kouBeiHomeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JosnSensorsDataAPI.koubeiGoodClick("口碑热销", hotsaleHomeEntity.like_list.get(position).id, hotsaleHomeEntity.like_list.get(position).title, position);
                    GoodsDetailAct.startAct(baseAct, hotsaleHomeEntity.like_list.get(position).id);
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void setHotsaleCate(HotsaleEntity hotsaleEntity) {
        initRvMid(hotsaleEntity.list);
    }

    @Override
    public void setHotsaleCates(KoubeiSecondEntity koubeiSecondEntity) {

    }

    @Override
    public void getZan(CommonEntity commonEntity) {

    }

    public void initRvMid(List<HotsaleEntity.Suspension> list) {
        midList.clear();
        midList.addAll(list);
        if (hotsaleAdapter == null) {
            hotsaleAdapter = new HotsaleAdapter(baseAct, false, midList);
            rv_mid.setNestedScrollingEnabled(false);
            rv_mid.setLayoutManager(new LinearLayoutManager(baseAct));
            rv_mid.setAdapter(hotsaleAdapter);
            hotsaleAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    KouBeiActNews.startAct(baseAct, midList.get(position).cate_id);
                }
            });
        } else {
            hotsaleAdapter.notifyDataSetChanged();
        }
    }
}
