package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.DataUtil;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/11/15.
 */

public class FootprintDialog extends Dialog {

    private Unbinder bind;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.mbtn_clear)
    MyButton mbtn_clear;

    @BindView(R.id.mrl_footprint_bg)
    MyRelativeLayout mrl_footprint_bg;
    private Context context;


    public FootprintDialog(Context context) {
        this(context,R.style.MyDialogStyleRight);
        this.context = context;
    }


    public FootprintDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_footprint, null, false);
        setContentView(inflate);
        bind = ButterKnife.bind(this, inflate);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = DeviceInfoUtil.getDeviceHeight(context) -
                ImmersionBar.getStatusBarHeight((Activity) context);
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

        ViewGroup.LayoutParams layoutParams = mbtn_clear.getLayoutParams();
        layoutParams.height = TransformUtil.countRealHeight(context,86);
        mbtn_clear.setLayoutParams(layoutParams);

        initData();
    }

    private void initData() {
        List<String> fdasf = DataUtil.getListString(20, "fdasf");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recy_view.setLayoutManager(linearLayoutManager);
        SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<String>(getContext(),
                R.layout.item_footprint,fdasf){

            @Override
            public void convert(SimpleViewHolder holder, String s, int position) {

            }
        };

        recy_view.setAdapter(simpleRecyclerAdapter);
    }

    @Override
    public void dismiss() {
        super.dismiss();
//        if (bind != null){
//            bind.unbind();
//        }
        ((BaseActivity) context).immersionBar.getTag(GoodsDetailAct.class.getName()).init();
    }

    @Override
    public void show() {
        super.show();

        ((BaseActivity) context).immersionBar.statusBarColor(R.color.white).init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int width = TransformUtil.countRealWidth(getContext(), 490);
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                float x = event.getRawX();
                if (x < width){
                    dismiss();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
