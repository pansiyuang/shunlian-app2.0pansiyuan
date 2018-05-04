package com.shunlian.app.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.bean.StoreIndexEntity;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;


public class StoreKanner extends BaseBanner<StoreIndexEntity.Body.Datas, StoreKanner> {
    public StoreKanner(Context context) {
        this(context, null, 0);
    }


    public StoreKanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StoreKanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public View onCreateItemView(int position) {

        LinearLayout container = new LinearLayout(context);
        ImageView iv = new ImageView(context);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        HomeAllEntity.Data.Banner banner = list.get(position);
        GlideUtils.getInstance().loadImage(getContext(),iv,list.get(position).whole_thumb);

//        iv.setTag(R.id.tag_typeId, banner.getType());
//        iv.setTag(R.id.tag_itemId, banner.getItemId());
//
//        iv.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (MyOnclickListener.isClickable(context)) {
//                    Common.startActivityFromAd(context,view.getTag(R.id.tag_typeId).toString(),view.getTag(R.id.tag_itemId).toString());
//                }
//            }
//        });
        container.addView(iv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        return container;
    }

    private RoundCornerIndicaor indicator;

    @Override
    public View onCreateIndicator() {
        indicator = (RoundCornerIndicaor) LayoutInflater.from(context).inflate(layoutRes, null);
        indicator.setViewPager(vp, list.size());
        return indicator;
    }

    @Override
    public void setCurrentIndicator(int i) {
        indicator.setCurrentItem(i);
    }
}
