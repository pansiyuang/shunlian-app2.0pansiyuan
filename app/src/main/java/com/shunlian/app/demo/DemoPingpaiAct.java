package com.shunlian.app.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.CoreHotEntity;
import com.shunlian.app.bean.CoreNewEntity;
import com.shunlian.app.bean.CoreNewsEntity;
import com.shunlian.app.bean.CorePingEntity;
import com.shunlian.app.bean.HotRdEntity;
import com.shunlian.app.demo.demo1.DemoPAishang;
import com.shunlian.app.presenter.PAishang;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.core.PingpaiAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.QuickActions;
import com.shunlian.app.view.IAishang;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/1.
 */

public class DemoPingpaiAct extends BaseActivity implements IAishang {
    @BindView(R.id.mtv_title)
    MyTextView mtv_title;

    @BindView(R.id.kanner)
    MyKanner kanner;


    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    @BindView(R.id.rl_more)
    RelativeLayout rl_more;

    @BindView(R.id.quick_actions)
    QuickActions quick_actions;

    @BindView(R.id.tv_msg_count)
    MyTextView tv_msg_count;
    private DemoPAishang pAishang;
    private ArrayList<String> strings;
    private DemoPingpaiAdapter pinpaiAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, DemoPingpaiAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.domeact_pingpai;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_title.setText(getStringResouce(R.string.first_pingpaite));
        strings =new ArrayList<>();
        pAishang = new DemoPAishang(this, this);
        pAishang.getCorePing();
    }

    @Override
    public void setPingData(CorePingEntity corePingEntity) {
        if(corePingEntity.banner!=null&&corePingEntity.banner.size()>0){
            for(int i = 0 ;i<corePingEntity.banner.size();i++){
                strings.add(corePingEntity.banner.get(i).img);
                if(i==corePingEntity.banner.size()-1){
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(baseAct, corePingEntity.banner.get(position).type, corePingEntity.banner.get(position).item_id);
                        }
                    });
                }
            }
        }else {
            kanner.setVisibility(View.GONE);
        }
        if(isEmpty(corePingEntity.brand_list)){
            visible(nei_empty);
            gone(rv_list);
        }else {
            visible(rv_list);
            gone(nei_empty);
        }
        List<CorePingEntity.MData> pingList=corePingEntity.brand_list;
        rv_list.setNestedScrollingEnabled(false);
        rv_list.setLayoutManager(new LinearLayoutManager(baseAct,LinearLayoutManager.VERTICAL,false));
        pinpaiAdapter = new DemoPingpaiAdapter(this,true,pingList);
        rv_list.setAdapter(pinpaiAdapter);
        rv_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void setNewData(CoreNewEntity coreNewEntity) {

    }

    @Override
    public void setHotData(CoreHotEntity coreHotEntity) {

    }

    @Override
    public void setHotsData(List<CoreHotEntity.Hot.Goods> mData, String page, String total) {

    }

    @Override
    public void setNewsData(List<CoreNewsEntity.Goods> mData, String page, String total) {

    }

    @Override
    public void setPushData(List<HotRdEntity.MData> mData, HotRdEntity data) {

    }



    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {
        visible(nei_empty);
        gone(rv_list);
    }
}
