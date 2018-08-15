package com.shunlian.app.ui.fragment;

import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverFlashAdapter;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryNavEntity;
import com.shunlian.app.presenter.PDiscover;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.MainActivity;
import com.shunlian.app.ui.discover.DiscoverGuanzhuFrag;
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.ui.discover.DiscoverQuanZiFrag;
import com.shunlian.app.ui.discover.DiscoverSucaikuFrag;
import com.shunlian.app.ui.discover.DiscoverXindeFrag;
import com.shunlian.app.ui.discover.DiscoversFrag;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.ui.discover.other.ExperiencePublishActivity;
import com.shunlian.app.ui.discover.other.SearchArticleActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IDiscover;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;

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
    @BindView(R.id.mtv_message_jingxuan)
    MyTextView mtv_message_jingxuan;
    @BindView(R.id.mtv_message_guanzhu)
    MyTextView mtv_message_guanzhu;
    @BindView(R.id.mtv_message_xinde)
    MyTextView mtv_message_xinde;
    @BindView(R.id.mtv_message_quanzi)
    MyTextView mtv_message_quanzi;
    @BindView(R.id.mtv_message_sucaiku)
    MyTextView mtv_message_sucaiku;

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

    @BindView(R.id.miv_empty)
    MyImageView miv_empty;

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

    private Map<String, DiscoversFrag> fragments = new HashMap<>();
    private DiscoverGuanzhuFrag guanzhuFrag;
    private DiscoverJingxuanFrag jingxuanFrag;
    private DiscoverSucaikuFrag sucaikuFrag;
    private DiscoverXindeFrag xindeFrag;
    private DiscoverQuanZiFrag quanZiFrag;
    private DiscoverFlashAdapter flashAdapter;
    private PDiscover pDiscover;
    //    private String flag_jingxuan = "nice", flag_guanzhu = "focus",
//            flag_xinde = "experience", flag_quanzi = "circle", flag_sucaiku = "material";
    private String flag_jingxuan = "", flag_guanzhu = "",
            flag_xinde = "", flag_quanzi = "", flag_sucaiku = "";
//    private boolean isSecond = false;
    private CommonEntity data;
    private MainActivity mainActivity;
    private String flag = "";

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

    public void initMessage(CommonEntity data) {
        this.data = data;
        if (mtv_message_jingxuan == null)
            return;
        if (data == null) {
            mtv_message_jingxuan.setVisibility(View.GONE);
            mtv_message_guanzhu.setVisibility(View.GONE);
            mtv_message_xinde.setVisibility(View.GONE);
            mtv_message_quanzi.setVisibility(View.GONE);
            mtv_message_sucaiku.setVisibility(View.GONE);
            return;
        }
        mtv_message_jingxuan.setVisibility(View.VISIBLE);
        mtv_message_guanzhu.setVisibility(View.VISIBLE);
        mtv_message_xinde.setVisibility(View.VISIBLE);
        mtv_message_quanzi.setVisibility(View.VISIBLE);
        mtv_message_sucaiku.setVisibility(View.VISIBLE);

        judges(flag_jingxuan, mtv_message_jingxuan);
        judges(flag_guanzhu, mtv_message_guanzhu);
        judges(flag_xinde, mtv_message_xinde);
        judges(flag_quanzi, mtv_message_quanzi);
        judges(flag_sucaiku, mtv_message_sucaiku);

//        if (!isSecond) {
//            mtv_message_jingxuan.setVisibility(View.GONE);
//            isSecond = true;
//        }

    }

    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        pDiscover = new PDiscover(getContext(), this);

//        mAppbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
////                rv_content.setNestedScrollingEnabled(verticalOffset!=0);
//                LogUtil.augusLogW("yxf---"+verticalOffset);
//            }
//        });
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null)
            data = mainActivity.data;
        if (getArguments() != null && !isEmpty(getArguments().getString("flag")) &&
                "nicefocusexperiencecirclematerial".contains(getArguments().getString("flag"))) {
            flag = getArguments().getString("flag");
        }
        miv_empty.setFocusable(false);
    }

    @Override
    protected void initListener() {
        miv_search.setOnClickListener(this);
        mrlayout_jingxuan.setOnClickListener(this);
        mrlayout_guanzhu.setOnClickListener(this);
        mrlayout_goumaixinde.setOnClickListener(this);
        mrlayout_quanzi.setOnClickListener(this);
        mrlayout_sucaiku.setOnClickListener(this);
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
        messageCall("nice");
    }

    public void jingXuan() {
        showSataus(0);
        judge(flag_jingxuan);
        mtv_message_jingxuan.setVisibility(View.GONE);
    }

    public void guanZhuFrag() {
        if (guanzhuFrag == null) {
            guanzhuFrag = new DiscoverGuanzhuFrag();
            fragments.put(FLAG_GUANZHU, guanzhuFrag);
        } else {
            guanzhuFrag = (DiscoverGuanzhuFrag) fragments.get(FLAG_GUANZHU);
        }
        switchContent(guanzhuFrag);
        messageCall("focus");
    }

    public void guanZhu() {
        showSataus(1);
        judge(flag_guanzhu);
        mtv_message_guanzhu.setVisibility(View.GONE);
    }

    public void xindeFrag() {
        if (xindeFrag == null) {
            xindeFrag = new DiscoverXindeFrag();
            fragments.put(FLAG_GOUMAIXINDE, xindeFrag);
        } else {
            xindeFrag = (DiscoverXindeFrag) fragments.get(FLAG_GOUMAIXINDE);
        }
        switchContent(xindeFrag);
        messageCall("experience");
    }

    public void xinde() {
        showSataus(2);
        judge(flag_xinde);
        mtv_message_xinde.setVisibility(View.GONE);
    }

    public void quanziFrag() {
        if (quanZiFrag == null) {
            quanZiFrag = new DiscoverQuanZiFrag();
            fragments.put(FLAG_QUANZI, quanZiFrag);
        } else {
            quanZiFrag = (DiscoverQuanZiFrag) fragments.get(FLAG_QUANZI);
        }
        switchContent(quanZiFrag);
        messageCall("circle");
    }

    public void quanzi() {
        showSataus(3);
        judge(flag_quanzi);
        mtv_message_quanzi.setVisibility(View.GONE);
    }

    public void sucaikuFrag() {
        if (sucaikuFrag == null) {
            sucaikuFrag = new DiscoverSucaikuFrag();
            fragments.put(FLAG_SUCAIKU, sucaikuFrag);
        } else {
            sucaikuFrag = (DiscoverSucaikuFrag) fragments.get(FLAG_SUCAIKU);
        }
        switchContent(sucaikuFrag);
        messageCall("material");
    }

    public void sucaiku() {
        showSataus(4);
        judge(flag_sucaiku);
        mtv_message_sucaiku.setVisibility(View.GONE);
    }

    public void messageCall(String flag) {
        if (data != null) {
            int count = 0;
            if (!isEmpty(flag))
                switch (flag) {
                    case "nice":
                        count = data.nice;
                        data.nice = 0;
                        break;
                    case "focus":
                        count = data.focus;
                        data.focus = 0;
                        break;
                    case "experience":
                        count = data.experience;
                        data.experience = 0;
                        break;
                    case "circle":
                        count = data.circle;
                        data.circle = 0;
                        break;
                    case "material":
                        count = data.material;
                        data.material = 0;
                        break;
                }
            data.total = data.total - count;
            if (mainActivity != null && mainActivity.mtv_message_count != null) {
                if (data.total > 99) {
                    mainActivity.mtv_message_count.setText("99+");
                } else if (data.total <= 0) {
                    mainActivity.mtv_message_count.setVisibility(View.GONE);
                } else {
                    mainActivity.mtv_message_count.setText(String.valueOf(data.total));
                }
            }
        }
    }

    public void judges(String flag, MyTextView mtv_message) {
        int count = 0;
        if (!isEmpty(flag))
            switch (flag) {
                case "nice":
                    count = data.nice;
                    break;
                case "focus":
                    count = data.focus;
                    break;
                case "experience":
                    count = data.experience;
                    break;
                case "circle":
                    count = data.circle;
                    break;
                case "material":
                    count = data.material;
                    break;
            }
        if (count > 99) {
            mtv_message.setText("99+");
        } else if (count <= 0) {
            mtv_message.setVisibility(View.GONE);
        } else {
            mtv_message.setText(String.valueOf(count));
        }
    }

    public void judge(String flag) {
        if (!isEmpty(flag))
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

//    private void setParam(int length, MyTextView mtv,MyTextView mtvs) {
//        int multiple = 4 - length;
////        LayoutParams取父布局类型,参数中的ViewGroup.LayoutParams.WRAP_CONTENT参数都是指向一个值如：RelativeLayout.LayoutParams.WRAP_CONTENT，LinearLayout.LayoutParams.WRAP_CONTENT
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(-TransformUtil.dip2px(baseContext, 4* multiple), TransformUtil.dip2px(baseContext, 6), 0, 0);
////        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
//        layoutParams.addRule(RelativeLayout.RIGHT_OF, mtvs.getId());
//        mtv.setLayoutParams(layoutParams);
//    }

    private void setParam(int length, MyTextView mtv, int tab) {//5个tab//4个
        float multiple;
        if (length > 3) {
            multiple = 0.5f;
        } else {
            multiple = 4 - length;
        }
//        LayoutParams取父布局类型,参数中的ViewGroup.LayoutParams.WRAP_CONTENT参数都是指向一个值如：RelativeLayout.LayoutParams.WRAP_CONTENT，LinearLayout.LayoutParams.WRAP_CONTENT
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, TransformUtil.dip2px(baseContext, 6), TransformUtil.dip2px(baseContext, (6 * 6 / tab) * multiple), 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mtv.setLayoutParams(layoutParams);
    }

    @Override
    public void setNavData(final DiscoveryNavEntity navEntity) {
        if (isEmpty(navEntity.flash_list)) {
            visible(miv_empty);
            gone(rv_flash);
        } else {
            LogUtil.httpLogW("navEntity:" + navEntity.flash_list.size());
            visible(rv_flash);
            gone(miv_empty);
            if (flashAdapter == null) {
                flashAdapter = new DiscoverFlashAdapter(baseActivity, navEntity.flash_list);
                LinearLayoutManager flashManager = new LinearLayoutManager(baseActivity,
                        LinearLayoutManager.HORIZONTAL, false);
                rv_flash.setLayoutManager(flashManager);
                rv_flash.setAdapter(flashAdapter);
                flashAdapter.setOnItemClickListener((view, position) ->
                        ArticleH5Act.startAct(baseActivity, navEntity.flash_list.get(position).id,
                                ArticleH5Act.MODE_SONIC));
            } else {
                flashAdapter.notifyDataSetChanged();
            }
        }

        if (!isEmpty(navEntity.nav_list)) {
            switch (navEntity.nav_list.size()) {
                case 1:
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;

                    setParam(navEntity.nav_list.get(0).name.length(), mtv_message_jingxuan, 1);
                    break;
                case 2:
                    mtv_jingxuan.setText(navEntity.nav_list.get(0).name);
                    mrlayout_jingxuan.setVisibility(View.VISIBLE);
                    mtv_guanzhu.setText(navEntity.nav_list.get(1).name);
                    mrlayout_guanzhu.setVisibility(View.VISIBLE);
                    flag_jingxuan = navEntity.nav_list.get(0).code;
                    flag_guanzhu = navEntity.nav_list.get(1).code;

                    setParam(navEntity.nav_list.get(0).name.length(), mtv_message_jingxuan, 2);
                    setParam(navEntity.nav_list.get(1).name.length(), mtv_message_guanzhu, 2);
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

                    setParam(navEntity.nav_list.get(0).name.length(), mtv_message_jingxuan, 3);
                    setParam(navEntity.nav_list.get(1).name.length(), mtv_message_guanzhu, 3);
                    setParam(navEntity.nav_list.get(2).name.length(), mtv_message_xinde, 3);
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

                    setParam(navEntity.nav_list.get(0).name.length(), mtv_message_jingxuan, 4);
                    setParam(navEntity.nav_list.get(1).name.length(), mtv_message_guanzhu, 4);
                    setParam(navEntity.nav_list.get(2).name.length(), mtv_message_xinde, 4);
                    setParam(navEntity.nav_list.get(3).name.length(), mtv_message_quanzi, 4);
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

                    setParam(navEntity.nav_list.get(0).name.length(), mtv_message_jingxuan, 5);
                    setParam(navEntity.nav_list.get(1).name.length(), mtv_message_guanzhu, 5);
                    setParam(navEntity.nav_list.get(2).name.length(), mtv_message_xinde, 5);
                    setParam(navEntity.nav_list.get(3).name.length(), mtv_message_quanzi, 5);
                    setParam(navEntity.nav_list.get(4).name.length(), mtv_message_sucaiku, 5);
                    break;
            }
            if ((flag_jingxuan+flag_guanzhu+flag_xinde+flag_quanzi+flag_sucaiku).contains("experience")){
                miv_experience_publish.setVisibility(View.VISIBLE);
            }else {
                miv_experience_publish.setVisibility(View.GONE);
            }
            if (isEmpty(flag))
                flag=navEntity.nav_list.get(0).code;
            setArgument(flag);
            initMessage(data);
        }

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void mOnClick(View view) {
        super.mOnClick(view);
        switch (view.getId()) {
            case R.id.miv_search:
                SearchArticleActivity.startAct(getActivity());
                break;
            case R.id.mrlayout_jingxuan:
                jingXuan();
                break;
            case R.id.mrlayout_guanzhu:
                guanZhu();
                break;
            case R.id.mrlayout_goumaixinde:
                xinde();
                break;
            case R.id.mrlayout_quanzi:
                quanzi();
                break;
            case R.id.mrlayout_sucaiku:
                sucaiku();
                break;
            case R.id.miv_experience_publish:
                if (!Common.isAlreadyLogin()) {
                    Common.goGoGo(baseActivity, "login");
                    return;
                }
                ExperiencePublishActivity.startAct(getActivity());
                break;
        }
    }


    public void setArgument(String flag) {
        if (!isEmpty(flag) && "nicefocusexperiencecirclematerial".contains(flag)) {
            if (flag.equals(flag_guanzhu)) {
                guanZhu();
            } else if (flag.equals(flag_xinde)) {
                xinde();
            } else if (flag.equals(flag_quanzi)) {
                quanzi();
            } else if (flag.equals(flag_sucaiku)) {
                sucaiku();
            } else {
                jingXuan();
            }
        }
//        if (!isEmpty(flag))
//            switch (flag) {
//                case "nice":
//                    jingXuan();
//                    break;
//                case "focus":
//                    guanZhu();
//                    break;
//                case "experience":
//                    xinde();
//                    break;
//                case "circle":
//                    quanzi();
//                    break;
//                case "material":
//                    sucaiku();
//                    break;
//            }
    }
}
