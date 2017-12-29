package com.shunlian.app.ui.returns_order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.RefundAfterSaleAdapter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.DataUtil;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RefundAfterSaleAct extends BaseActivity {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;


    public static void startAct(Context context){
        context.startActivity(new Intent(context,RefundAfterSaleAct.class));
    }
    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_refund_after_sale;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        RefundAfterSaleAdapter afterSaleAdapter = new RefundAfterSaleAdapter
                (this,true, DataUtil.getListString(10,"dd"));
        recy_view.setAdapter(afterSaleAdapter);
    }

}
