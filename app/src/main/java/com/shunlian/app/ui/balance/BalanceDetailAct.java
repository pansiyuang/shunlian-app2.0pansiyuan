package com.shunlian.app.ui.balance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BalanceDetailAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.BalanceDetailEntity;
import com.shunlian.app.bean.BalanceInfoEntity;
import com.shunlian.app.presenter.PBalanceDetail;
import com.shunlian.app.presenter.PBalanceMain;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IBalanceDetail;
import com.shunlian.app.view.IBalanceMain;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.List;

import butterknife.BindView;

public class BalanceDetailAct extends BaseActivity implements View.OnClickListener, IBalanceDetail {
    @BindView(R.id.rv_detai)
    RecyclerView rv_detai;

    private PBalanceDetail pBalanceDetail;
    private LinearLayoutManager linearLayoutManager;
    private BalanceDetailAdapter balanceDetailAdapter;

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
                            pBalanceDetail.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        pBalanceDetail=new PBalanceDetail(this,this);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(BalanceDetailEntity data, List<BalanceDetailEntity.Balance> balanceList) {
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
}
