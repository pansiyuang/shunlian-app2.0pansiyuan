package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BalanceDetailAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.WithdrawDetailAdapter;
import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.WithdrawListEntity;
import com.shunlian.app.presenter.PBalanceDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Constant;
import com.shunlian.app.view.IBalanceDetail;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.List;

import butterknife.BindView;

public class BalanceDetailAct extends BaseActivity implements View.OnClickListener, IBalanceDetail {
    @BindView(R.id.rv_detai)
    RecyclerView rv_detai;

    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private PBalanceDetail pBalanceDetail;
    private LinearLayoutManager linearLayoutManager;
    private BalanceDetailAdapter balanceDetailAdapter;
    private WithdrawDetailAdapter withdrawDetailAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, BalanceDetailAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_balance_detail;
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_detai.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pBalanceDetail != null) {
                            if (Constant.ISBALANCE){
                                pBalanceDetail.refreshBaby();
                            }else {
                                pBalanceDetail.refreshBabys();
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (Constant.ISBALANCE){
            mtv_title.setText(getStringResouce(R.string.balance_yuexiangqing));
        }else {
            mtv_title.setText(getStringResouce(R.string.balance_tixianmingxi));
        }
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pBalanceDetail=new PBalanceDetail(this,this);
        if (Constant.ISBALANCE){
            pBalanceDetail.getApiData();
        }else {
            pBalanceDetail.getApiDatas();
        }
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.balance_zanwutixian));
        nei_empty.setButtonText(null);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {
        visible(nei_empty);
        gone(rv_detai);
    }


    @Override
    public void setApiData(BalanceDetailEntity data, List<BalanceDetailEntity.Balance> balanceList) {
        if (isEmpty(balanceList)){
            visible(nei_empty);
            gone(rv_detai);
        }else {
            gone(nei_empty);
            visible(rv_detai);
        }
        if (balanceDetailAdapter == null) {
            boolean isShowFooter;
            if (balanceList!=null&&balanceList.size()>5){
                isShowFooter=true;
            }else {
                isShowFooter=false;
            }
            balanceDetailAdapter = new BalanceDetailAdapter(getBaseContext(), isShowFooter, balanceList);
            linearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_detai.setLayoutManager(linearLayoutManager);
            rv_detai.setAdapter(balanceDetailAdapter);
            balanceDetailAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    AmountDetailAct.startAct(getBaseContext(),balanceList.get(position).id
                    );
                }
            });
        } else {
            balanceDetailAdapter.notifyDataSetChanged();
        }
        balanceDetailAdapter.setPageLoading(Integer.parseInt(data.page),Integer.parseInt( data.total_page));
    }

    @Override
    public void setApiDatas(WithdrawListEntity.Pager pager, List<WithdrawListEntity.Record> records) {
        if (isEmpty(records)){
            visible(nei_empty);
            gone(rv_detai);
        }else {
            gone(nei_empty);
            visible(rv_detai);
        }
        if (withdrawDetailAdapter == null) {
            boolean isShowFooter;
            if (records!=null&&records.size()>5){
                isShowFooter=true;
            }else {
                isShowFooter=false;
            }
            withdrawDetailAdapter = new WithdrawDetailAdapter(getBaseContext(), isShowFooter, records);
            linearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_detai.setLayoutManager(linearLayoutManager);
            rv_detai.setAdapter(withdrawDetailAdapter);
            withdrawDetailAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    AmountDetailAct.startAct(getBaseContext(),records.get(position).id
                    );
                }
            });
        } else {
            withdrawDetailAdapter.notifyDataSetChanged();
        }
        withdrawDetailAdapter.setPageLoading(Integer.parseInt(pager.page),Integer.parseInt( pager.total_page));

    }
}
