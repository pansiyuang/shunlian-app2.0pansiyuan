package com.shunlian.app.shunlianyoupin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.h5.H5X5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.MyKanner;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/3.
 */

public class ShunlianyoupinMainFrag extends BaseFragment implements IYouPinBackView{
    @BindView(R.id.mout_youpin)
    ImageView mout_youpin;
    @BindView(R.id.iv_main)
    ImageView iv_main;
    @BindView(R.id.miv_logo)
    ImageView miv_logo;

    @BindView(R.id.kanner)
    MyKanner kanner;
    @BindView(R.id.msv_out)
    MyScrollView msv_out;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    @BindView(R.id.ntv_uuid)
    NetAndEmptyInterface ntv_uuid;
    @BindView(R.id.ll_root)
    LinearLayout ll_root;
    @BindView(R.id.sl_root)
    SwipeRefreshLayout sl_root;
    private ShunlianyoupinPresenter presenter;
    private ArrayList<String> strings;
    private ShunlianyoupinAdapter adapter;
    private LinearLayoutManager newManager;


    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_shunlianyoupin_main, container, false);
    }
    protected void initListener() {
        super.initListener();
//        rv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (newManager != null) {
//                    int lastPosition = newManager.findLastVisibleItemPosition();
//                    if (lastPosition + 1 == newManager.getItemCount()) {
//                        if (presenter != null) {
//                            presenter.refreshBaby();
//                        }
//                    }
//                }
//            }
//        });setFocusable(false);
        mout_youpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
        iv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                webview = fragmentManager.findFragmentById(R.id.fl_main).getView().findViewById(R.id.webview);

                msv_out.fullScroll(ScrollView.FOCUS_UP);
//                sv.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        miv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pullApp(getActivity(),"com.shunlian.app");
            }
        });
        msv_out.setFocusable(false);
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom){
                    if (presenter != null) {
                        presenter.refreshBaby();
                    }
                }
            }
        });
        sl_root.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter=null;

                presenter.resetBaby();
                sl_root.setRefreshing(false);
            }
        });
    }
    private void pullApp(Context context, String packageName) {
        if(isApkInstalled(context,packageName)){
            Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(packageName);
            if(intent!=null){
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else {
                Common.staticToast(getActivity(),"尚未安装应用...");
            }

        }

    }
    public  boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }





    @Override
    protected void initData() {
        presenter = new ShunlianyoupinPresenter(getActivity(), this);
        presenter.initbanner("1");
        presenter.initPingPaiData(1,10);
        adapter=null;
        strings =new ArrayList<>();
        ntv_uuid.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.day_haohuotaiduo));
        ntv_uuid.setButtonText(null);
    }

    @Override
    public void getPingPaiData(YouPingListEntity activityListEntity, int allPage, int page, List<YouPingListEntity.lists> list) {
        if (rv_list == null) {
            return;
        }
        if(adapter==null){
            adapter= new ShunlianyoupinAdapter(getActivity(), true, list);
            newManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//            rv_list.setHasFixedSize(true);
            rv_list.setNestedScrollingEnabled(false);
            rv_list.setLayoutManager(newManager);
            rv_list.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    H5X5Act.startAct(baseActivity, "http://mt-front.v2.shunliandongli.com/" +"gifts/"+list.get(position).product_id,H5X5Act.MODE_SONIC);
                }
            });
        }else {
            adapter.notifyDataSetChanged();
        }
        adapter.setPageLoading(page,allPage);


    }

    @Override
    public void getBannerData(List<YouPingbannerEntity.banner> activityListEntity) {

        if(activityListEntity!=null&&activityListEntity.size()>0){
            strings.clear();
            for(int i = 0 ;i<activityListEntity.size();i++){
                strings.add(activityListEntity.get(i).pic_url);
                if(i==activityListEntity.size()-1){
                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
                    kanner.setBanner(strings);
                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
                        @Override
                        public void onItemClick(int position) {
                            Common.goGoGo(baseActivity,activityListEntity.get(position).jump_link.type,activityListEntity.get(position).jump_link.item_id);
//                         H5X5Act.startAct(baseActivity, "http://mt-front.v2.shunliandongli.com/" +"gifts/"+activityListEntity.get(position).id,H5X5Act.MODE_SONIC);

                        }
                    });
                }
            }
        }else {
            kanner.setVisibility(View.GONE);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {
        switch (request_code){
            case 2:
            visible(ntv_uuid);
            gone(rv_list);
        }

    }

}
