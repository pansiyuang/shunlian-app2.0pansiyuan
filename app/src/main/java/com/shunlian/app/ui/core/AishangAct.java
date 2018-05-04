package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.AiMoreAdapter;
import com.shunlian.app.adapter.CoreNewMenuAdapter;
import com.shunlian.app.adapter.AishangHorizonAdapter;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.AllMessageCountEntity;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.eventbus_bean.NewMessageEvent;
import com.shunlian.app.newchat.util.MessageCountManager;
import com.shunlian.app.presenter.PAishang;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.MHorItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/7.
 */

public class AishangAct extends BaseActivity implements View.OnClickListener, IAishang, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.kanner)
    MyKanner kanner;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.rv_categoryMenu)
    RecyclerView rv_categoryMenu;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;

    private PAishang pAishang;
    private AiMoreAdapter aiMoreAdapter;
    private GridLayoutManager gridLayoutManager;
    private String cate_id;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AishangAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_aishang;
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (gridLayoutManager != null) {
                    int lastPosition = gridLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == gridLayoutManager.getItemCount()) {
                        if (pAishang != null) {
                            pAishang.refreshBaby("new",cate_id);
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
        EventBus.getDefault().register(this);
        mtv_title.setText(getStringResouce(R.string.first_aishangxin));
        pAishang = new PAishang(this, this);
        pAishang.getCoreNew();

        messageCountManager = MessageCountManager.getInstance(this);
        messageCountManager.setOnGetMessageListener(this);
    }

    @Override
    protected void onResume() {
        if(messageCountManager.isLoad()){
            messageCountManager.setTextCount(tv_msg_count);
        }else{
            messageCountManager.initData();
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(NewMessageEvent event) {
        messageCountManager.setTextCount(tv_msg_count);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setNewData(CoreNewEntity coreNewEntity) {
        if (coreNewEntity.banner_list != null && coreNewEntity.banner_list.size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < coreNewEntity.banner_list.size(); i++) {
                strings.add(coreNewEntity.banner_list.get(i).img);
                if (i >= coreNewEntity.banner_list.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(getBaseContext(), coreNewEntity.banner_list.get(position).type, coreNewEntity.banner_list.get(position).item_id);
                        }
                    });
                }
            }
        } else {
            kanner.setVisibility(View.GONE);
        }
        AishangHorizonAdapter aishangHorizonAdapter = new AishangHorizonAdapter(getBaseContext(), false, coreNewEntity.hot_goods);
        rv_goods.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_goods.setAdapter(aishangHorizonAdapter);
        rv_goods.addItemDecoration(new MHorItemDecoration(getBaseContext(), 10, 10, 10));
        CoreNewMenuAdapter coreNewMenuAdapter = new CoreNewMenuAdapter(getBaseContext(), false, coreNewEntity.cate_name);
        cate_id=coreNewEntity.cate_name.get(0).cate_id;
        pAishang.resetBaby("new",cate_id);
        coreNewMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                coreNewMenuAdapter.selectedPosition = position;
                coreNewMenuAdapter.notifyDataSetChanged();
                cate_id=coreNewEntity.cate_name.get(position).cate_id;
                if (rv_category.getScrollState() == 0) {
                    pAishang.resetBaby("new",cate_id);
                }
            }
        });
        rv_categoryMenu.setAdapter(coreNewMenuAdapter);
        rv_categoryMenu.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void setHotData(CoreHotEntity coreNewEntity) {

    }

    @Override
    public void setHotsData(List<CoreHotEntity.Hot.Goods> mData, String page, String total) {

    }

    @Override
    public void setNewsData(List<CoreNewsEntity.Goods> mData, String page, String total) {
        if (aiMoreAdapter==null){
            aiMoreAdapter=new AiMoreAdapter(getBaseContext(),mData);
            gridLayoutManager=new GridLayoutManager(getBaseContext(),2);
            rv_category.setLayoutManager(gridLayoutManager);
            rv_category.setAdapter(aiMoreAdapter);
            //在CoordinatorLayout中使用添加分割线失效
//            GridSpacingItemDecoration gridSpacingItemDecoration = new GridSpacingItemDecoration(TransformUtil.dip2px(getBaseContext(), 5), false);
//            rv_category.addItemDecoration(gridSpacingItemDecoration);
        }else {
            aiMoreAdapter.notifyDataSetChanged();
        }
        aiMoreAdapter.setPageLoading(Integer.parseInt(page), Integer.parseInt(total));
    }

    @Override
    public void setPushData(List<HotRdEntity.MData> mData, HotRdEntity data) {

    }

    @Override
    public void setPingData(CorePingEntity corePingEntity) {

    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {

    }

    @Override
    public void OnLoadFail() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
