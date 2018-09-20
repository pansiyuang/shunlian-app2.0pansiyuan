package com.shunlian.app.ui.task;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.EggDetailAdapter;
import com.shunlian.app.bean.EggDetailEntity;
import com.shunlian.app.presenter.PEggDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.view.IEggDetail;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

import butterknife.BindView;

public class EggDetailAct extends BaseActivity implements IEggDetail {
    @BindView(R.id.rv_detail)
    RecyclerView rv_detail;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private LinearLayoutManager linearLayoutManager;
    private EggDetailAdapter eggDetailAdapter;
    private PEggDetail pEggDetail;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, EggDetailAct.class);
//        intent.putExtra("managerUrl", managerUrl);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_egg_detail;
    }


    @Override
    protected void initData() {
//        setStatusBarColor(R.color.white);
//        setStatusBarFontDark();
//        storeId = getIntent().getStringExtra("storeId");
        pEggDetail = new PEggDetail(this, this);
        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText(getString(R.string.mission_zanwumingxi))
                .setButtonText(null);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_detail.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pEggDetail != null) {
                            pEggDetail.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void getApiData(int allPage, int page, List<EggDetailEntity.Out> list) {
        if (isEmpty(list)) {
            visible(nei_empty);
            gone(rv_detail);
        } else if (eggDetailAdapter == null) {
            visible(rv_detail);
            gone(nei_empty);
            eggDetailAdapter = new EggDetailAdapter(baseAct, false, list);
            linearLayoutManager = new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false);
            rv_detail.setLayoutManager(linearLayoutManager);
            rv_detail.setAdapter(eggDetailAdapter);
            rv_detail.addItemDecoration(new MVerticalItemDecoration(baseAct, 22, 0, 22));
        } else {
            eggDetailAdapter.notifyDataSetChanged();
        }
        if (eggDetailAdapter != null)
            eggDetailAdapter.setPageLoading(page, allPage);
    }
}
