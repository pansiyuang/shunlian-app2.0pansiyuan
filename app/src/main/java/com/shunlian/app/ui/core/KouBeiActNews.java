package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.KouBeiHomeAdapter;
import com.shunlian.app.adapter.KouBeiSecondAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.HotsaleEntity;
import com.shunlian.app.bean.HotsaleHomeEntity;
import com.shunlian.app.bean.KoubeiSecondEntity;
import com.shunlian.app.presenter.PKoubei;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.JosnSensorsDataAPI;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IKoubei;
import com.shunlian.app.widget.NewTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class KouBeiActNews extends BaseActivity implements View.OnClickListener, IKoubei {

    @BindView(R.id.rv_categorys)
    RecyclerView rv_categorys;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;

    @BindView(R.id.ntv_title)
    NewTextView ntv_title;

    @BindView(R.id.ntv_desc)
    NewTextView ntv_desc;

    private PKoubei pKoubei;
    private KouBeiSecondAdapter kouBeiSecondAdapter;
    private KouBeiHomeAdapter kouBeiHomeAdapter;
    private List<KoubeiSecondEntity.Content> contents = new ArrayList<>();


    public static void startAct(Context context, String cate3) {
        Intent intent = new Intent(context, KouBeiActNews.class);
        intent.putExtra("cate3", cate3);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_kou_bei_news;
    }

    @Override
    protected void initData() {
        immersionBar.statusBarView(R.id.view_state).init();
        pKoubei = new PKoubei(this, this);
        pKoubei.getHotsaleCates(getIntent().getStringExtra("cate3"));
    }

    @Override
    public void setHotsaleCates(KoubeiSecondEntity koubeiSecondEntity) {
        ntv_title.setText(koubeiSecondEntity.cate_name);
        ntv_desc.setText(koubeiSecondEntity.cate_rate);
        ntv_desc.setVisibility(View.VISIBLE);
        int divide = -1;
        contents.clear();
        if (!isEmpty(koubeiSecondEntity.commend_list)) {
            divide = koubeiSecondEntity.commend_list.size() - 1;
            contents.addAll(koubeiSecondEntity.commend_list);
        }
        contents.addAll(koubeiSecondEntity.hot_list);
        if (kouBeiSecondAdapter == null) {
            kouBeiSecondAdapter = new KouBeiSecondAdapter(baseAct, false, contents, divide);
            rv_categorys.setNestedScrollingEnabled(false);
            rv_categorys.setLayoutManager(new LinearLayoutManager(baseAct));
            rv_categorys.addItemDecoration(new MVerticalItemDecoration(baseAct, 16, 0, 16));
            rv_categorys.setAdapter(kouBeiSecondAdapter);
            kouBeiSecondAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(baseAct, contents.get(position).id);
                }
            });
        } else {
            kouBeiSecondAdapter.notifyDataSetChanged();
        }

        if (kouBeiHomeAdapter == null) {
            kouBeiHomeAdapter = new KouBeiHomeAdapter(baseAct, false, koubeiSecondEntity.like_list);
            rv_category.setLayoutManager(new GridLayoutManager(baseAct, 2));
            rv_category.setNestedScrollingEnabled(false);
            rv_category.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(baseAct, 9), false));
            rv_category.setAdapter(kouBeiHomeAdapter);
            kouBeiHomeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JosnSensorsDataAPI.koubeiGoodClick("口碑热销", koubeiSecondEntity.like_list.get(position).id, koubeiSecondEntity.like_list.get(position).title, position);
                    GoodsDetailAct.startAct(baseAct, koubeiSecondEntity.like_list.get(position).id);
                }
            });
        } else {
            kouBeiHomeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void getZan(CommonEntity commonEntity) {

    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void setHomeData(HotsaleHomeEntity hotsaleHomeEntity) {

    }

    @Override
    public void setHotsaleCate(HotsaleEntity hotsaleEntity) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        pKoubei.getHotsaleCates(getIntent().getStringExtra("cate3"));
    }
}
