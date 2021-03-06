package com.shunlian.app.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.FootprintEntity;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.mylibrary.ImmersionBar;

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

//    @BindView(R.id.mbtn_clear)
//    MyButton mbtn_clear;

    @BindView(R.id.mrl_footprint_bg)
    MyRelativeLayout mrl_footprint_bg;

    @BindView(R.id.mll_empty)
    MyLinearLayout mll_empty;
    private Context context;
    private FootprintEntity mFootprintEntity;


    public FootprintDialog(Context context, FootprintEntity footprintEntity) {
        this(context,footprintEntity,R.style.MyDialogStyleRight);
    }


    public FootprintDialog(Context context,FootprintEntity footprintEntity, int themeResId) {
        super(context, themeResId);
        this.context = context;
        mFootprintEntity = footprintEntity;
        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_footprint, null, false);
        setContentView(inflate);
        bind = ButterKnife.bind(this, inflate);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = DeviceInfoUtil.getDeviceHeight(context) -
                ImmersionBar.getStatusBarHeight((Activity) context);
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(attributes);

//        ViewGroup.LayoutParams layoutParams = mbtn_clear.getLayoutParams();
//        layoutParams.height = TransformUtil.countRealHeight(context,86);
//        mbtn_clear.setLayoutParams(layoutParams);

        initData();
    }

   /* public void refreshData(){
        mFootprintEntity = null;
        initData();
    }*/

    private void initData() {
        if (mFootprintEntity == null){
            mll_empty.setVisibility(View.VISIBLE);
            recy_view.setVisibility(View.GONE);
//            mbtn_clear.setVisibility(View.GONE);
            return;
        }

        if (mFootprintEntity.mark_data != null && mFootprintEntity.mark_data.size() > 0) {
            mll_empty.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
//            mbtn_clear.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recy_view.setLayoutManager(linearLayoutManager);
            int space = TransformUtil.dip2px(context, 8.5f);
            recy_view.addItemDecoration(new VerticalItemDecoration(space,0,0));
            SimpleRecyclerAdapter simpleRecyclerAdapter = new SimpleRecyclerAdapter<FootprintEntity.MarkData>(getContext(),
                    R.layout.item_footprint, mFootprintEntity.mark_data) {

                @Override
                public void convert(SimpleViewHolder holder, FootprintEntity.MarkData s, int position) {
                    holder.addOnClickListener(R.id.rootview);
                    MyImageView miv_icon = holder.getView(R.id.miv_icon);
                    MyTextView mtv_title = holder.getView(R.id.mtv_title);
                    MyTextView mtv_price = holder.getView(R.id.mtv_price);
                    GlideUtils.getInstance().loadImage(context,miv_icon,s.thumb);
                    mtv_title.setText(s.title);
                    mtv_price.setText(context.getResources().getString(R.string.rmb)+s.price);
                }
            };

            recy_view.setAdapter(simpleRecyclerAdapter);
            simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    dismiss();
                    FootprintEntity.MarkData markData = mFootprintEntity.mark_data.get(position);
                    GoodsDetailAct.startAct(context, markData.goods_id);
                }
            });
        }else {
            mll_empty.setVisibility(View.VISIBLE);
            recy_view.setVisibility(View.GONE);
//            mbtn_clear.setVisibility(View.GONE);
        }
    }

    /*@OnClick(R.id.mbtn_clear)
    public void clearFootprint(){
        if (context instanceof GoodsDetailAct){
            GoodsDetailAct act = (GoodsDetailAct) context;
            act.clearFootprint();
        }
    }*/

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
