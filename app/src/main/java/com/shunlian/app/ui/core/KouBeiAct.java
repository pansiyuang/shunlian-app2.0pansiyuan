package com.shunlian.app.ui.core;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.CoreHotMenuAdapter;
import com.shunlian.app.adapter.KoubeiAdapter;
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
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.MHorItemDecoration;
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

public class KouBeiAct extends BaseActivity implements View.OnClickListener, IAishang, MessageCountManager.OnGetMessageListener {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;

    @BindView(R.id.kanner)
    MyKanner kanner;


    @BindView(R.id.rv_categoryMenu)
    RecyclerView rv_categoryMenu;

    @BindView(R.id.rv_category)
    RecyclerView rv_category;

    private PAishang pAishang;
    private KoubeiAdapter koubeiAdapter;
    private LinearLayoutManager linearLayoutManager;
    private String cate_id;
    private MessageCountManager messageCountManager;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, KouBeiAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_kou_bei;
    }

    @Override
    protected void initListener() {
        super.initListener();
        rv_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (pAishang != null) {
                            pAishang.refreshBaby("hot",cate_id);
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
        mtv_title.setText(getStringResouce(R.string.first_pingpaite));
        mtv_title.setText(getStringResouce(R.string.first_koubeire));
        pAishang = new PAishang(this, this);
        pAishang.getCoreHot();

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

    }

    @Override
    public void setHotData(CoreHotEntity coreHotEntity) {
        if (coreHotEntity.banner_list != null && coreHotEntity.banner_list.size() > 0) {
            kanner.setVisibility(View.VISIBLE);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < coreHotEntity.banner_list.size(); i++) {
                strings.add(coreHotEntity.banner_list.get(i).img);
                if (i >= coreHotEntity.banner_list.size() - 1) {
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(getBaseContext(), coreHotEntity.banner_list.get(position).type, coreHotEntity.banner_list.get(position).item_id);
                        }
                    });
                }
            }
        }else {
            kanner.setVisibility(View.GONE);
        }
        CoreHotMenuAdapter coreHotMenuAdapter = new CoreHotMenuAdapter(getBaseContext(), false, coreHotEntity.cate_name);
        cate_id=coreHotEntity.cate_name.get(0).cate_id;
        pAishang.resetBaby("hot",cate_id);
        coreHotMenuAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                coreHotMenuAdapter.selectedPosition = position;
                coreHotMenuAdapter.notifyDataSetChanged();
                cate_id=coreHotEntity.cate_name.get(position).cate_id;
                if (rv_category.getScrollState() == 0) {
                    pAishang.resetBaby("hot",cate_id);
                }
            }
        });
        rv_categoryMenu.addItemDecoration(new MHorItemDecoration(getBaseContext(),25,15,15));
        rv_categoryMenu.setAdapter(coreHotMenuAdapter);
        rv_categoryMenu.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setHotsData(List<CoreHotEntity.Hot.Goods> mData, String page, String total) {
        if (koubeiAdapter==null){
            koubeiAdapter=new KoubeiAdapter(getBaseContext(),true,mData);
            linearLayoutManager=new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false);
            rv_category.setLayoutManager(linearLayoutManager);
            rv_category.setAdapter(koubeiAdapter);
            koubeiAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsDetailAct.startAct(getBaseContext(),mData.get(position).id);
                }
            });
        }else {
            koubeiAdapter.notifyDataSetChanged();
        }
        koubeiAdapter.setPageLoading(Integer.parseInt(page), Integer.parseInt(total));
    }

    @Override
    public void setNewsData(List<CoreNewsEntity.Goods> mData, String page, String total) {

    }

    @Override
    public void setPushData(List<HotRdEntity.MData> mData, HotRdEntity data) {

    }

    @Override
    public void setPingData(CorePingEntity corePingEntity) {

    }

    @Override
    public void OnLoadSuccess(AllMessageCountEntity messageCountEntity) {
        messageCountManager.setTextCount(tv_msg_count);
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
