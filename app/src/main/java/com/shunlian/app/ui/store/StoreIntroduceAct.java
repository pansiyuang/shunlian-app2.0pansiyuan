package com.shunlian.app.ui.store;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.StoreEvaluateAdapter;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.bean.StoreIntroduceEntity;
import com.shunlian.app.presenter.StoreIntroducePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.StoreIntroduceView;
import com.shunlian.app.widget.MyRelativeLayout;

import java.util.List;

import butterknife.BindView;

public class StoreIntroduceAct extends BaseActivity implements View.OnClickListener,StoreIntroduceView {
    @BindView(R.id.mtv_storeName)
    TextView mtv_storeName;

    @BindView(R.id.mtv_storeScore)
    TextView mtv_storeScore;

    @BindView(R.id.mtv_number)
    TextView mtv_number;

    @BindView(R.id.mtv_haopinglv)
    TextView mtv_haopinglv;

    @BindView(R.id.mtv_dianhua)
    TextView mtv_dianhua;

    @BindView(R.id.mtv_weixin)
    TextView mtv_weixin;

    @BindView(R.id.mtv_baozhen)
    TextView mtv_baozhen;

    @BindView(R.id.mtv_kaidian)
    TextView mtv_kaidian;

    @BindView(R.id.rv_haopin)
    RecyclerView rv_haopin;




    private String storeId;
    private StoreIntroducePresenter storeIntroducePresenter;

    public static void startAct(Context context,String storeId) {
        Intent intent = new Intent(context, StoreIntroduceAct.class);
        intent.putExtra("storeId", storeId);//店铺id
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_introduce;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void initListener() {
        super.initListener();

    }

    @Override
    protected void initData() {
        storeId = getIntent().getStringExtra("storeId");
        storeIntroducePresenter=new StoreIntroducePresenter(this,this,storeId);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void introduceInfo(StoreIntroduceEntity storeIntroduceEntity) {
        mtv_storeName.setText(storeIntroduceEntity.store_name);
        mtv_storeScore.setText("店铺分null");
        mtv_number.setText(storeIntroduceEntity.store_collect+"人");
        mtv_haopinglv.setText(storeIntroduceEntity.evaluate.praise_rate);
        mtv_dianhua.setText(storeIntroduceEntity.store_phone);
        mtv_weixin.setText(storeIntroduceEntity.store_wx);
        mtv_baozhen.setText(storeIntroduceEntity.paying_amount);
        mtv_kaidian.setText(storeIntroduceEntity.store_time);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_haopin.setLayoutManager(manager);
        rv_haopin.setAdapter(new StoreEvaluateAdapter(this,false,storeIntroduceEntity.evaluate.pj));
    }
}
