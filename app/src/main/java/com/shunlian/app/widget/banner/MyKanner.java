package com.shunlian.app.widget.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.shunlian.app.R;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;


public class MyKanner extends BaseBanner<String, MyKanner> {
    public MyKanner(Context context) {
        this(context, null, 0);
    }


    public MyKanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyKanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public View onCreateItemView(int position) {

//        RelativeLayout container = new RelativeLayout(context);
//        ImageView iv = new ImageView(context);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);
//
////        HomeAllEntity.Data.Banner banner = list.get(position);
//        GlideUtils.getInstance().communityBanner(context,iv,list.get(position));

        RelativeLayout container = new RelativeLayout(context);
        ImageView iv = new ImageView(context);
        if (isCorp){
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }else {
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        int deviceWidth = DeviceInfoUtil.getDeviceWidth(context);
        GlideUtils.getInstance().loadOverrideImage(context,iv,list.get(position),deviceWidth,(int)(deviceWidth*scale));
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
        if (dimColor!=-1){
            View view = new View(context);
            view.setBackgroundColor(dimColor);
            container.addView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        }
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
