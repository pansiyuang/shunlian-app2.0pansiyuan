package com.shunlian.app.ui.goods_detail;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.ComboDetailAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.ComboDetailEntity;
import com.shunlian.app.bean.ComboEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.ComboDetailPresenter;
import com.shunlian.app.ui.SideslipBaseActivity;
import com.shunlian.app.ui.confirm_order.ConfirmOrderAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IComboDetailView;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/24.
 */

public class ComboDetailAct extends SideslipBaseActivity implements IComboDetailView, MessageCountManager.OnGetMessageListener {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.mtv_toolbar_msgCount)
    MyTextView mtv_toolbar_msgCount;

    private Map<String, String> goods_sku;
    private int combo_size;
    private String combo_id;
    private MessageCountManager messageCountManager;

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

        EventBus.getDefault().register(this);
        mtv_toolbar_title.setText(getStringResouce(R.string.package_details));
        combo_id = getIntent().getStringExtra("combo_id");
        String goods_id = getIntent().getStringExtra("goods_id");
        ComboDetailPresenter presenter = new ComboDetailPresenter(this,this);
        presenter.getcombodetail(combo_id,goods_id);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        goods_sku = new HashMap<>();

    }

    @Override
    public void onResume() {
        if (Common.isAlreadyLogin()) {
            messageCountManager = MessageCountManager.getInstance(getBaseContext());
            if (messageCountManager.isLoad()) {
                String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
                if (quick_actions != null)
                    quick_actions.setMessageCount(s);
            } else {
                messageCountManager.initData();
            }
            messageCountManager.setOnGetMessageListener(this);
        }
        super.onResume();
    }

    @OnClick(R.id.mrlayout_toolbar_more)
    public void more(){
        quick_actions.setVisibility(View.VISIBLE);
        quick_actions.order();
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
        ComboDetailAdapter adapter = new ComboDetailAdapter(this,current_combo.goods,entity);
        recy_view.setAdapter(adapter);
        adapter.setSelectParamsListener((goods_id, sku) -> goods_sku.put(goods_id,sku));
    }

    @OnClick(R.id.mtv_buy)
    public void buyCombo(){
        ComboEntity comboEntity = new ComboEntity(combo_id,goods_sku);
        try {
            String s = new ObjectMapper().writeValueAsString(comboEntity);
            ConfirmOrderAct.startAct(this, s,ConfirmOrderAct.TYPE_COMBO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (quick_actions != null)
            quick_actions.destoryQuickActions();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        String s = messageCountManager.setTextCount(mtv_toolbar_msgCount);
        if (quick_actions != null)
            quick_actions.setMessageCount(s);
    }

    @Override
    public void OnLoadFail() {

    }
}
