package com.shunlian.app.ui.more_credit;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.PhoneRecordAdapter;
import com.shunlian.app.bean.PhoneRecordEntity;
import com.shunlian.app.presenter.PPhoneRecord;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.view.IPhoneRecord;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

import butterknife.BindView;

public class PhoneRecordAct extends BaseActivity implements IPhoneRecord {

    @BindView(R.id.rv_charge)
    RecyclerView rv_charge;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private LinearLayoutManager linearLayoutManager;
    private PPhoneRecord pPhoneRecord;
    private PhoneRecordAdapter phoneRecordAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, PhoneRecordAct.class);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.act_phone_record;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pPhoneRecord = new PPhoneRecord(this, this);
        nei_empty.setImageResource(R.mipmap.img_empty_common)
                .setText(getString(R.string.phone_zanwuchongzhi))
                .setButtonText(getString(R.string.phone_lijichongzhi))
                .setOnClickListener((view) -> finish());
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_charge.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pPhoneRecord != null) {
                            pPhoneRecord.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setApiData(PhoneRecordEntity.Pager data, List<PhoneRecordEntity.MData> mdatas) {
        if (isEmpty(mdatas)) {
            visible(nei_empty);
            gone(rv_charge);
        } else if (phoneRecordAdapter == null) {
            visible(rv_charge);
            gone(nei_empty);
            phoneRecordAdapter = new PhoneRecordAdapter(baseAct, mdatas);
            linearLayoutManager = new LinearLayoutManager(baseAct, LinearLayoutManager.VERTICAL, false);
            rv_charge.setLayoutManager(linearLayoutManager);
            rv_charge.setAdapter(phoneRecordAdapter);
            rv_charge.addItemDecoration(new MVerticalItemDecoration(baseAct, 10, 15, 0));
            phoneRecordAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    PhoneOrderDetailAct.startAct(baseAct, mdatas.get(position).id);
                }
            });
        } else {
            phoneRecordAdapter.notifyDataSetChanged();
        }
        if (phoneRecordAdapter != null)
            phoneRecordAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));
    }

    @Override
    public void showFailureView(int request_code) {
        visible(nei_empty);
        gone(rv_charge);
    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nei_empty);
        gone(rv_charge);
    }
}
