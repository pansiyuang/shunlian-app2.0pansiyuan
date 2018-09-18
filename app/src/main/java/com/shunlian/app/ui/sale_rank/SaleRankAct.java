package com.shunlian.app.ui.sale_rank;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SaleRankAdapter;
import com.shunlian.app.bean.WeekSaleTopEntity;
import com.shunlian.app.presenter.PWeekSale;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IWeekSale;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class SaleRankAct extends BaseActivity implements View.OnClickListener, IWeekSale {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_times)
    MyTextView mtv_times;

    @BindView(R.id.rl_more)
    MyRelativeLayout rl_more;


    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private PWeekSale pWeekSale;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SaleRankAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_sale_rank;
    }

    @Override
    protected void initListener() {
        super.initListener();
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.plus_xiaoshoupaihang));
        nei_empty.setButtonText(null);
        rl_more.setVisibility(View.GONE);
        mtv_title.setText(getStringResouce(R.string.personal_youpingbenyue));
        pWeekSale = new PWeekSale(this, this);
    }


    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {
        visible(nei_empty);
        gone(rv_list);
    }


    @Override
    public void setApiData(WeekSaleTopEntity weekSaleTopEntity) {
        if (weekSaleTopEntity.list==null||weekSaleTopEntity.list.size()<=0){
            visible(nei_empty);
            gone(rv_list);
        }else {
            gone(nei_empty);
            visible(rv_list);
        }
        mtv_time.setText(weekSaleTopEntity.month_banner);
        mtv_times.setText(weekSaleTopEntity.update_time);
        rv_list.setNestedScrollingEnabled(false);
        rv_list.setAdapter(new SaleRankAdapter(baseAct,weekSaleTopEntity.list));
        rv_list.setLayoutManager(new LinearLayoutManager(baseAct,LinearLayoutManager.VERTICAL,false));
    }
}
