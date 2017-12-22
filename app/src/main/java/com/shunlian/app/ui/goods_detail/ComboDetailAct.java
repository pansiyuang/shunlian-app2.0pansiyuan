package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ComboDetailAdapter;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.ComboDetailPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.view.IComboDetailView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAct extends SideslipBaseActivity implements IComboDetailView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;


    public static void startAct(Context context,String combo_id,String goods_id){
        Intent intent = new Intent(context,ComboDetailAct.class);
        intent.putExtra("combo_id",combo_id);
        intent.putExtra("goods_id",goods_id);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_combo_detail;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        String combo_id = getIntent().getStringExtra("combo_id");
        String goods_id = getIntent().getStringExtra("goods_id");
        ComboDetailPresenter presenter = new ComboDetailPresenter(this,this);
        presenter.getcombodetail(combo_id,goods_id);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void comboDetailData(ComboDetailEntity entity) {
        List<GoodsDeatilEntity.Goods> goods = entity.goods;
        ComboDetailAdapter adapter = new ComboDetailAdapter(this,false,goods,entity);
        recy_view.setAdapter(adapter);
    }
}
