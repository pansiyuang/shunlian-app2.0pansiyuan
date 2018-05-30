package com.shunlian.app.ui.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.DiscoverFlashAdapter;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.presenter.PDiscover;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover.DiscoverGuanzhuFrag;
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.ui.discover.DiscoverQuanZiFrag;
import com.shunlian.app.ui.discover.DiscoverSucaikuFrag;
import com.shunlian.app.ui.discover.DiscoverXindeFrag;
import com.shunlian.app.ui.discover.DiscoversFrag;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.ui.discover.other.ExperiencePublishActivity;
import com.shunlian.app.ui.discover.other.SearchArticleActivity;
import com.shunlian.app.view.IDiscover;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 发现页面
 */

public class DiscoverFrag extends BaseFragment implements IDiscover, View.OnClickListener {
    public final String FLAG_JINGXUAN = "flag_jingxuan";
    public final String FLAG_GUANZHU = "flag_guanzhu";
    public final String FLAG_GOUMAIXINDE = "flag_goumaixinde";
    public final String FLAG_QUANZI = "flag_quanzi";
    public final String FLAG_SUCAIKU = "flag_sucaiku";
    @BindView(R.id.mtv_jingxuan)
    MyTextView mtv_jingxuan;
    @BindView(R.id.mtv_guanzhu)
    MyTextView mtv_guanzhu;
    @BindView(R.id.mtv_goumaixinde)
    MyTextView mtv_goumaixinde;
    @BindView(R.id.mtv_quanzi)
    MyTextView mtv_quanzi;
    @BindView(R.id.mtv_sucaiku)
    MyTextView mtv_sucaiku;
    @BindView(R.id.view_jingxuan)
    View view_jingxuan;
    @BindView(R.id.view_guanzhu)
    View view_guanzhu;
    @BindView(R.id.view_goumaixinde)
    View view_goumaixinde;
    @BindView(R.id.view_quanzi)
    View view_quanzi;
    @BindView(R.id.view_sucaiku)
    View view_sucaiku;
    @BindView(R.id.miv_search)
    MyImageView miv_search;

    @BindView(R.id.miv_experience_publish)
    MyImageView miv_experience_publish;

    @BindView(R.id.rv_flash)
    RecyclerView rv_flash;

    @BindView(R.id.mrlayout_jingxuan)
    MyRelativeLayout mrlayout_jingxuan;

    @BindView(R.id.mrlayout_guanzhu)
    MyRelativeLayout mrlayout_guanzhu;

    @BindView(R.id.mrlayout_goumaixinde)
    MyRelativeLayout mrlayout_goumaixinde;

    @BindView(R.id.mrlayout_quanzi)
    MyRelativeLayout mrlayout_quanzi;

    @BindView(R.id.mrlayout_sucaiku)
    MyRelativeLayout mrlayout_sucaiku;

    @BindView(R.id.mAppbar)
    AppBarLayout mAppbar;

    private Map<String, DiscoversFrag> fragments;
    private DiscoverGuanzhuFrag guanzhuFrag;
    private DiscoverJingxuanFrag jingxuanFrag;
    private DiscoverSucaikuFrag sucaikuFrag;
    private DiscoverXindeFrag xindeFrag;
    private DiscoverQuanZiFrag quanZiFrag;
    private DiscoverFlashAdapter flashAdapter;
    private PDiscover pDiscover;
    private String flag_jingxuan, flag_guanzhu, flag_xinde, flag_quanzi, flag_sucaiku;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_discover, null, false);
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            ImmersionBar.with(this).fitsSystemWindows(true)
                    .statusBarColor(R.color.white)
                    .statusBarDarkFont(true, 0.2f)
                    .init();
        }
    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        fragments = new HashMap<>();
        pDiscover = new PDiscover(getContext(), this);

//        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
////                rv_content.setNestedScrollingEnabled(verticalOffset!=0);
//                LogUtil.augusLogW("yxf---"+verticalOffset);
//            }
//        });
        jingXuanFrag();

    }

    @Override
    protected void initListener() {
        miv_search.setOnClickListener(this);
        miv_experience_publish.setOnClickListener(this);
        super.initListener();
    }

    public void jingXuanFrag() {
        if (jingxuanFrag == null) {
            jingxuanFrag = new DiscoverJingxuanFrag();
            fragments.put(FLAG_JINGXUAN, jingxuanFrag);
        } else {
            jingxuanFrag = (DiscoverJingxuanFrag) fragments.get(FLAG_JINGXUAN);
        }
        switchContent(jingxuanFrag);
    }

    @OnClick(R.id.mrlayout_jingxuan)
    public void jingXuan() {
        showSataus(0);
        judge(flag_jingxuan);
    }

    public void guanZhuFrag() {
        if (guanzhuFrag == null) {
            guanzhuFrag = new DiscoverGuanzhuFrag();
            fragments.put(FLAG_GUANZHU, guanzhuFrag);
        } else {
            guanzhuFrag = (DiscoverGuanzhuFrag) fragments.get(FLAG_GUANZHU);
        }
        switchContent(guanzhuFrag);
    }

    @OnClick(R.id.mrlayout_guanzhu)
    public void guanZhu() {
        showSataus(1);
        judge(flag_guanzhu);
    }

    public void xindeFrag() {
        if (xindeFrag == null) {
            xindeFrag = new DiscoverXindeFrag();
            fragments.put(FLAG_GOUMAIXINDE, xindeFrag);
        } else {
            xindeFrag = (DiscoverXindeFrag) fragments.get(FLAG_GOUMAIXINDE);
        }
        switchContent(xindeFrag);
    }

    @OnClick(R.id.mrlayout_goumaixinde)
    public void xinde() {
        showSataus(2);
        judge(flag_xinde);
    }

    public void quanziFrag() {
        if (quanZiFrag == null) {
            quanZiFrag = new DiscoverQuanZiFrag();
            fragments.put(FLAG_QUANZI, quanZiFrag);
        } else {
            quanZiFrag = (DiscoverQuanZiFrag) fragments.get(FLAG_QUANZI);
        }
        switchContent(quanZiFrag);
    }

    @OnClick(R.id.mrlayout_quanzi)
    public void quanzi() {
        showSataus(3);
        judge(flag_quanzi);
    }

    public void sucaikuFrag() {
        if (sucaikuFrag == null) {
            sucaikuFrag = new DiscoverSucaikuFrag();
            fragments.put(FLAG_SUCAIKU, sucaikuFrag);
        } else {
            sucaikuFrag = (DiscoverSucaikuFrag) fragments.get(FLAG_SUCAIKU);
        }
        switchContent(sucaikuFrag);
    }

    @OnClick(R.id.mrlayout_sucaiku)
    public void sucaiku() {
        showSataus(4);
        judge(flag_sucaiku);
    }

    public void judge(String flag) {
        switch (flag) {
            case "nice":
                jingXuanFrag();
                break;
            case "focus":
                guanZhuFrag();
                break;
            case "experience":
                xindeFrag();
                break;
            case "circle":
                quanziFrag();
                break;
            case "material":
                sucaikuFrag();
                break;
        }
    }

    private void showSataus(int status) {

        mtv_jingxuan.setTextColor(status == 0 ? getColorResouce(R.color.pink_color) : getColorResouce(R.color.new_text));
        view_jingxuan.setVisibility(status == 0 ? View.VISIBLE : View.INVISIBLE);

        mtv_guanzhu.setTextColor(status == 1 ? getColorResouce(R.color.pink_color) : getColorResouce(R.color.new_text));
        view_guanzhu.setVisibility(status == 1 ? View.VISIBLE : View.INVISIBLE);

        mtv_goumaixinde.setTextColor(status == 2 ? getColorResouce(R.color.pink_color) : getColorResouce(R.color.new_text));
        view_goumaixinde.setVisibility(status == 2 ? View.VISIBLE : View.INVISIBLE);

        mtv_quanzi.setTextColor(status == 3 ? getColorResouce(R.color.pink_color) : getColorResouce(R.color.new_text));
        view_quanzi.setVisibility(status == 3 ? View.VISIBLE : View.INVISIBLE);

        mtv_sucaiku.setTextColor(status == 4 ? getColorResouce(R.color.pink_color) : getColorResouce(R.color.new_text));
        view_sucaiku.setVisibility(status == 4 ? View.VISIBLE : View.INVISIBLE);
    }

    /*
    替换fragment内容
     */
    public void switchContent(Fragment show) {
        if (show != null) {
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            if (!show.isAdded()) {
                ft.add(R.id.flayout_content, show);
            } else {
                ft.show(show);
            }

            if (fragments != null && fragments.size() > 0) {
                Iterator<String> keys = fragments.keySet().iterator();
                while (keys.hasNext()) {
                    String next = keys.next();
                    DiscoversFrag discoversFrag = fragments.get(next);
                    if (show != discoversFrag) {
                        if (discoversFrag != null && discoversFrag.isVisible()) {
                            ft.hide(discoversFrag);
                        }
                    }
                }
                ft.commit();
            }
        }
    }

    @Override
    public void setNavData(final DiscoveryNavEntity navEntity) {
        if (flashAdapter == null) {
            flashAdapter = new DiscoverFlashAdapter(getContext(), false, navEntity.flash_list);
            LinearLayoutManager flashManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rv_flash.setLayoutManager(flashManager);
            rv_flash.setAdapter(flashAdapter);
            flashAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ArticleH5Act.startAct(getActivity(), navEntity.flash_list.get(position).id, ArticleH5Act.MODE_SONIC);
                }
            });
        } else {
            flashAdapter.notifyDataSetChanged();
        }
        if (navEntity.nav_list != null && navEntity.nav_list.size() > 0) {
            switch (navEntity.nav_list.size()) {
                case 1:
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    break;
                case 2:
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    mtv_guanzhu.setText(navEntity.nav_list.get(1).name);
                    mrlayout_guanzhu.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    flag_guanzhu = navEntity.nav_list.get(1).code;
                    break;
                case 3:
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    mtv_goumaixinde.setText(navEntity.nav_list.get(2).name);
                    mrlayout_goumaixinde.setVisibility(View.VISIBLE);
                    mtv_guanzhu.setText(navEntity.nav_list.get(1).name);
                    mrlayout_guanzhu.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    flag_guanzhu = navEntity.nav_list.get(1).code;
                    flag_xinde = navEntity.nav_list.get(2).code;
                    break;
                case 4:
                    mtv_quanzi.setText(navEntity.nav_list.get(3).name);
                    mrlayout_quanzi.setVisibility(View.VISIBLE);
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    mtv_goumaixinde.setText(navEntity.nav_list.get(2).name);
                    mrlayout_goumaixinde.setVisibility(View.VISIBLE);
                    mtv_guanzhu.setText(navEntity.nav_list.get(1).name);
                    mrlayout_guanzhu.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    flag_guanzhu = navEntity.nav_list.get(1).code;
                    flag_xinde = navEntity.nav_list.get(2).code;
                    flag_quanzi = navEntity.nav_list.get(3).code;
                    break;
                case 5:
                    mtv_sucaiku.setText(navEntity.nav_list.get(4).name);
                    mrlayout_sucaiku.setVisibility(View.VISIBLE);
                    mtv_quanzi.setText(navEntity.nav_list.get(3).name);
                    mrlayout_quanzi.setVisibility(View.VISIBLE);
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    mtv_goumaixinde.setText(navEntity.nav_list.get(2).name);
                    mrlayout_goumaixinde.setVisibility(View.VISIBLE);
                    mtv_guanzhu.setText(navEntity.nav_list.get(1).name);
                    mrlayout_guanzhu.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    flag_guanzhu = navEntity.nav_list.get(1).code;
                    flag_xinde = navEntity.nav_list.get(2).code;
                    flag_quanzi = navEntity.nav_list.get(3).code;
                    flag_sucaiku = navEntity.nav_list.get(4).code;
                    break;
            }
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.miv_search:
                SearchArticleActivity.startAct(getActivity());
                break;
            case R.id.miv_experience_publish:
                ExperiencePublishActivity.startAct(getActivity());
                break;
        }
    }
}
