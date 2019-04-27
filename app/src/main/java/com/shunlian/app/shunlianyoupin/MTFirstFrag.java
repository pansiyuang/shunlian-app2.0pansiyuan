//package com.shunlian.app.shunlianyoupin;
//
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.plus.app.BuildConfig;
//import com.plus.app.R;
//import com.plus.app.adapter.BaseRecyclerAdapter;
//import com.plus.app.adapter.FirstMtAdapter;
//import com.plus.app.bean.FirstMtEntity;
//import com.plus.app.bean.FirstsMtEntity;
//import com.plus.app.presenter.PFirstMt;
//import com.plus.app.service.InterentTools;
//import com.plus.app.ui.BaseFragment;
//import com.plus.app.ui.h5.H5X5Act;
//import com.plus.app.ui.setting.SettingAct;
//import com.plus.app.utils.Common;
//import com.plus.app.utils.MVerticalItemDecoration;
//import com.plus.app.utils.MyOnClickListener;
//import com.plus.app.view.IFirstMt;
//import com.plus.app.widget.CompileScrollView;
//import com.plus.app.widget.MyImageView;
//import com.plus.app.widget.MyScrollView;
//import com.plus.app.widget.banner.BaseBanner;
//import com.plus.app.widget.banner.MyKanner;
//import com.plus.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
//import com.plus.app.widget.nestedrefresh.NestedRingHeader;
//import com.plus.app.widget.nestedrefresh.interf.onRefreshListener;
//import com.plus.mylibrary.ImmersionBar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//
//public class MTFirstFrag extends BaseFragment implements View.OnClickListener ,IFirstMt{
//
//    @BindView(R.id.kanner)
//    MyKanner kanner;
//
//    @BindView(R.id.miv_logo)
//    MyImageView miv_logo;
//
//    @BindView(R.id.miv_hint)
//    MyImageView miv_hint;
//
//    @BindView(R.id.csv_out)
//    CompileScrollView csv_out;
//
//    @BindView(R.id.lay_refresh)
//    NestedRefreshLoadMoreLayout lay_refresh;
//
//    @BindView(R.id.rv_list)
//    RecyclerView rv_list;
//    private PFirstMt pFirstMt;
//    private FirstMtAdapter pinpaiAdapter;
//
//    public static BaseFragment getInstance() {
//        MTFirstFrag fragment = new MTFirstFrag();
////        Bundle args = new Bundle();
////        args.putString("joinSign", joinSign);
////        args.putString("cateId", cateId);
////        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    protected void initListener() {
//        super.initListener();
//        miv_logo.setOnClickListener(this);
//        miv_hint.setOnClickListener(this);
//        csv_out.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                // 滚动到底
//                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
//                    if (pFirstMt != null) {
//                        pFirstMt.refreshBaby();
//                    }
//                }
//            }
//        });
//        lay_refresh.setOnRefreshListener(new onRefreshListener() {
//            @Override
//            public void onRefresh() {
//                pFirstMt.getBanners();
//                pFirstMt.reSetBaby();
//            }
//        });
//    }
//
//    public void pullApp(Context context, String packageName){
//        if (isApkInstalled(context,packageName)){
//            Intent intent = baseActivity.getPackageManager().getLaunchIntentForPackage(packageName);
//            if (intent != null) {
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        }else {
//            Common.staticToast(baseActivity,"尚未安装应用...");
//        }
//    }
//    public  boolean isApkInstalled(Context context, String packageName) {
//        if (TextUtils.isEmpty(packageName)) {
//            return false;
//        }
//        try {
//            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//    @Override
//    public void onClick(View v) {
//        if (MyOnClickListener.isFastClick()) {
//            return;
//        }
//        switch (v.getId()) {
//            case R.id.miv_logo:
//                if (BuildConfig.DEBUG) {
//                    SettingAct.startAct(baseContext);
//                }else {
//                    pullApp(baseActivity,"com.shunlian.app");
//                }
//                break;
//            case R.id.miv_hint:
//                csv_out.scrollTo(0,0);
//                break;
//        }
//    }
//
//    @Override
//    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
//        View view = inflater.inflate(R.layout.frag_first_mt, container, false);
//        return view;
//    }
//
//    @Override
//    protected void initData() {
//        ImmersionBar.with(this).fitsSystemWindows(true)
//                .statusBarColor(R.color.white)
//                .statusBarDarkFont(true, 0.2f)
//                .init();
//        pFirstMt=new PFirstMt(baseActivity,this);
//        NestedRingHeader header = new NestedRingHeader(getContext());
//        lay_refresh.setRefreshHeaderView(header);
//    }
//
//
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            ImmersionBar.with(this).fitsSystemWindows(true)
//                    .statusBarColor(R.color.white)
//                    .statusBarDarkFont(true, 0.2f)
//                    .init();
//        }
//    }
//
//    @Override
//    public void setBanners(FirstMtEntity firstMtEntity) {
//        if (!isEmpty(firstMtEntity.list)) {
//            kanner.setVisibility(View.VISIBLE);
//            List<String> strings = new ArrayList<>();
//            for (int i = 0; i < firstMtEntity.list.size(); i++) {
//                strings.add(firstMtEntity.list.get(i).pic_url);
//                if (i >= firstMtEntity.list.size() - 1) {
//                    kanner.layoutRes = R.layout.layout_kanner_rectangle_indicator;
//                    kanner.setBanner(strings);
//                    kanner.setOnItemClickL(new BaseBanner.OnItemClickL() {
//                        @Override
//                        public void onItemClick(int position) {
//                            Common.goGoGo(baseActivity,firstMtEntity.list.get(position).jump_link.type,firstMtEntity.list.get(position).jump_link.item_id);
////                            H5X5Act.startAct(baseActivity, InterentTools.H5_MENG_TIAN +"gifts/"+firstMtEntity.list.get(position).id,H5X5Act.MODE_SONIC);
//                        }
//                    });
//                }
//            }
//        } else {
//            kanner.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void setProductlist(List<FirstsMtEntity.Suspension> firstsMtEntity,String currentPage,String allPage) {
//        lay_refresh.setRefreshing(false);
//        if (pinpaiAdapter==null){
//            rv_list.setNestedScrollingEnabled(false);
//            rv_list.setLayoutManager(new LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false));
//            pinpaiAdapter = new FirstMtAdapter(baseActivity, true, firstsMtEntity);
//            rv_list.addItemDecoration(new MVerticalItemDecoration(baseActivity, 28, 0, 0));
//            rv_list.setAdapter(pinpaiAdapter);
//            pinpaiAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    H5X5Act.startAct(baseActivity, InterentTools.H5_MENG_TIAN +"gifts/"+firstsMtEntity.get(position).product_id,H5X5Act.MODE_SONIC);
//                }
//            });
//        }else {
//            pinpaiAdapter.notifyDataSetChanged();
//        }
//        pinpaiAdapter.setPageLoading(Integer.parseInt(currentPage), Integer.parseInt(allPage));
//    }
//
//    @Override
//    public void showFailureView(int request_code) {
//        if (1==request_code)
//        lay_refresh.setRefreshing(false);
//    }
//
//    @Override
//    public void showDataEmptyView(int request_code) {
//
//    }
//}
