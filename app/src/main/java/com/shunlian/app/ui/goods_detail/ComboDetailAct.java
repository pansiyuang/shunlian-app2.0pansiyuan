package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.ComboDetailAdapter;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.bean.ComboEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.presenter.ComboDetailPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.view.IComboDetailView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAct extends SideslipBaseActivity implements IComboDetailView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;
    private Map<String, String> goods_sku;
    private int combo_size;
    private String combo_id;


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
        combo_id = getIntent().getStringExtra("combo_id");
        String goods_id = getIntent().getStringExtra("goods_id");
        ComboDetailPresenter presenter = new ComboDetailPresenter(this,this);
        presenter.getcombodetail(combo_id,goods_id);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);

        goods_sku = new HashMap<>();

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
        GoodsDeatilEntity.Combo current_combo = entity.current_combo;
        combo_size = current_combo.goods.size();
        ComboDetailAdapter adapter = new ComboDetailAdapter(this,false,current_combo.goods,entity);
        recy_view.setAdapter(adapter);
        adapter.setSelectParamsListener(new ComboDetailAdapter.ISelectParamsListener() {
            @Override
            public void selectParam(String goods_id, String sku) {
                goods_sku.put(goods_id,sku);
            }
        });
    }

    @OnClick(R.id.mtv_buy)
    public void buyCombo(){
//        if (goods_sku.size() != combo_size){
//            Common.staticToast("请选择商品属性");
//        }else {
//            ComboEntity comboEntity = new ComboEntity(combo_id,goods_sku);
//            try {
//                String s = new ObjectMapper().writeValueAsString(comboEntity);
//                System.out.println("========="+s);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }

        ComboEntity comboEntity = new ComboEntity("14",goods_sku);
        try {
            String s = new ObjectMapper().writeValueAsString(comboEntity);
//            System.out.println("========="+s);
            ConfirmOrderAct.startAct(this, s,ConfirmOrderAct.TYPE_COMBO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
