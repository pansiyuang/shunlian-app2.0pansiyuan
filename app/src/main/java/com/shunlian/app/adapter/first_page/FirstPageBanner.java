package com.shunlian.app.adapter.first_page;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.bean.MainPageEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.banner.BaseBanner;
import com.shunlian.app.widget.banner.RoundCornerIndicaor;

/**
 * Created by Administrator on 2018/2/1.
 */

public class FirstPageBanner extends BaseBanner<MainPageEntity.Banner, FirstPageBanner> {
    private RoundCornerIndicaor indicator;

    public FirstPageBanner(Context context) {
        super(context);
    }

    public FirstPageBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstPageBanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View onCreateItemView(final int position) {
        LinearLayout container = new LinearLayout(context);
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        final MainPageEntity.Banner banner = list.get(position);
        GlideUtils.getInstance().loadImage(getContext(),iv,banner.pic);

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodsDetailAct.startAct(getContext(),banner.item_id);
            }
        });
        container.addView(iv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return container;
    }

    @Override
    public View onCreateIndicator() {
        indicator = (RoundCornerIndicaor) LayoutInflater.from(context).inflate(layoutRes, null);
        indicator.setViewPager(vp, list.size());
        return indicator;
    }

    @Override
    public void setCurrentIndicator(int position) {
        indicator.setCurrentItem(position);
    }
}
