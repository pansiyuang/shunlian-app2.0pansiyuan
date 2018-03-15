package com.shunlian.app.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover.DiscoverGuanzhuFrag;
import com.shunlian.app.ui.discover.DiscoverJingxuanFrag;
import com.shunlian.app.ui.discover.DiscoverQuanZiFrag;
import com.shunlian.app.ui.discover.DiscoverSucaikuFrag;
import com.shunlian.app.ui.discover.DiscoverXindeFrag;
import com.shunlian.app.ui.discover.DiscoversFrag;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.shunlian.app.R2.color.new_text;
import static com.shunlian.app.R2.color.pink_color;

/**
 * Created by Administrator on 2017/11/16.
 * <p>
 * 发现页面
 */

public class DiscoverFrag extends BaseFragment {
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
    private Map<String, DiscoversFrag> fragments;
    private DiscoverGuanzhuFrag guanzhuFrag;
    private DiscoverJingxuanFrag jingxuanFrag;
    private DiscoverSucaikuFrag sucaikuFrag;
    private DiscoverXindeFrag xindeFrag;
    private DiscoverQuanZiFrag quanZiFrag;
    private String currentFrag = FLAG_JINGXUAN;//当前frag

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

    @OnClick(R.id.mllayout_jingxuan)
    public void jingXuan() {
        currentFrag = FLAG_JINGXUAN;
        showSataus(0);
        jingXuanFrag();
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
        currentFrag = FLAG_GUANZHU;
        showSataus(1);
        guanZhuFrag();
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
        currentFrag = FLAG_GOUMAIXINDE;
        showSataus(2);
        xindeFrag();
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
        currentFrag = FLAG_QUANZI;
        showSataus(3);
        quanziFrag();
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
        currentFrag = FLAG_SUCAIKU;
        showSataus(4);
        sucaikuFrag();
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
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
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
}
