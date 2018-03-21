package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.shunlian.app.R;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.banner.Kanner;
import com.shunlian.mylibrary.ImmersionBar;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/3/19.
 */

public class TagListActivity extends BaseActivity {

    @BindView(R.id.miv_is_fav)
    MyImageView miv_is_fav;

    @BindView(R.id.miv_more)
    MyImageView miv_more;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mll_item)
    MyLinearLayout mll_item;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.line)
    View line;

    @BindView(R.id.recycler_tags)
    RecyclerView recycler_tags;

    private int bannerHeight;
    public int offset;
    private String favId;
    private LinearLayoutManager manager;
    private int totalDy;
    private int screenWidth;

    @Override
    protected int getLayoutId() {
        return R.layout.act_tag_list;
    }

    @Override
    protected void initData() {
        defToolbar();
    }

    @Override
    protected void initListener() {
        super.initListener();
        recycler_tags.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int firstPosition = manager.findFirstVisibleItemPosition();
                    View firstView = manager.findViewByPosition(firstPosition);
                    if (firstView instanceof Kanner) {
                        totalDy += dy;
                        setBgColor(firstPosition, totalDy);
                    } else {
                        setToolbar();
                        totalDy = screenWidth;
                    }
                }
            }
        });
    }

    public void defToolbar() {
        if (immersionBar == null) {
            immersionBar = ImmersionBar.with(this);
        }
        immersionBar.titleBar(toolbar, false)
                .statusBarDarkFont(true, 0.2f)
                .addTag(GoodsDetailAct.class.getName())
                .init();
    }

    public void setBgColor(int position, int totalDy) {
        ImmersionBar immersionBar = ImmersionBar.with(this)
                .addViewSupportTransformColor(toolbar, R.color.white);
        if (totalDy <= bannerHeight) {
            if (totalDy <= 0) {
                totalDy = 0;
            }
            float alpha = (float) totalDy / bannerHeight;
            immersionBar.statusBarAlpha(alpha)
                    .addTag(GoodsDetailAct.class.getName())
                    .init();
            mll_item.setAlpha(alpha);

            float v = 1.0f - alpha * 2;
            if (v <= 0) {
                v = alpha * 2 - 1;
                setImg(2, 1);
            } else {
                setImg(1, 2);
            }
            miv_close.setAlpha(v);
            miv_is_fav.setAlpha(v);
            miv_more.setAlpha(v);
            if (alpha < 1.0f)
                line.setVisibility(View.INVISIBLE);
            else
                line.setVisibility(View.VISIBLE);
        } else {
            setToolbar();
        }
    }

    public void setToolbar() {
        setImg(2, 1);
        immersionBar.statusBarAlpha(1.0f)
                .addTag(GoodsDetailAct.class.getName())
                .init();
        mll_item.setAlpha(1.0f);
        miv_close.setAlpha(1.0f);
        miv_is_fav.setAlpha(1.0f);
        miv_more.setAlpha(1.0f);
        line.setVisibility(View.VISIBLE);
    }

    private void setImg(int status, int oldStatus) {
        if (status != oldStatus) {
            if (status == 1) {
                miv_close.setImageResource(R.mipmap.icon_more_fanhui);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_more_souchag_h);
                }
                miv_more.setImageResource(R.mipmap.icon_more_gengduo);
            } else {
                miv_close.setImageResource(R.mipmap.img_more_fanhui_n);
                if (TextUtils.isEmpty(favId) || "0".equals(favId)) {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_n);
                } else {
                    miv_is_fav.setImageResource(R.mipmap.icon_xiangqingye_souchag_h);
                }

                miv_more.setImageResource(R.mipmap.icon_more_n);
            }
        }
    }
}
