package com.shunlian.app.ui.register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SimpleRecyclerAdapter;
import com.shunlian.app.adapter.SimpleViewHolder;
import com.shunlian.app.bean.MemberCodeListEntity;
import com.shunlian.app.listener.OnItemClickListener;
import com.shunlian.app.presenter.SelectRecommendPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectRecommendView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.util.List;

import butterknife.BindView;

public class SelectRecommendAct extends BaseActivity implements View.OnClickListener ,ISelectRecommendView {

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.tv_next)
    TextView tv_next;

    @BindView(R.id.frame_detail)
    FrameLayout frame_detail;

    @BindView(R.id.sv_mask)
    ScrollView sv_mask;

    @BindView(R.id.ll_detail_content)
    LinearLayout ll_detail_content;

    @BindView(R.id.miv_icon)
    CircleImageView miv_icon;
    private SelectRecommendPresenter presenter;


    public static void startAct(Context context){
        Intent intent = new Intent(context,SelectRecommendAct.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_recommend;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_next.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        setHeader();
        FrameLayout.LayoutParams ll_dc_Params = (FrameLayout.LayoutParams) ll_detail_content.getLayoutParams();
        int[] ints = TransformUtil.countRealWH(this, 570, 720);
        ll_dc_Params.width = ints[0];
        ll_dc_Params.height = ints[1];
        ll_detail_content.setLayoutParams(ll_dc_Params);

        LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) miv_icon.getLayoutParams();
        int[] ints1 = TransformUtil.countRealWH(this, 200, 200);
        iconParams.width = ints1[0];
        iconParams.height = ints1[1];
        miv_icon.setLayoutParams(iconParams);

        presenter = new SelectRecommendPresenter(this,this);
    }

    private void dialogDetail() {
        sv_mask.setVisibility(View.VISIBLE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1,Animation.RELATIVE_TO_SELF,0);
        animation.setDuration(500);
        frame_detail.setVisibility(View.VISIBLE);
        frame_detail.setAnimation(animation);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_next:
               break;
       }
    }

    @Override
    public void selectCodeList(List<MemberCodeListEntity.ListBean> listBeens) {
        System.out.println("selectCodeList=="+listBeens.size());
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);

        SimpleRecyclerAdapter<MemberCodeListEntity.ListBean> simpleRecyclerAdapter = new SimpleRecyclerAdapter<MemberCodeListEntity.ListBean>(this, R.layout.item_recommend_select, listBeens) {

            @Override
            public void convert(SimpleViewHolder holder, MemberCodeListEntity.ListBean s) {
                holder.addOnClickListener(R.id.civ_head);
                CircleImageView view = holder.getView(R.id.civ_head);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                int deviceWidth = DeviceInfoUtil.getDeviceWidth(SelectRecommendAct.this);
                int v = (int) (160.0f / 750 * deviceWidth + 0.5f);
                layoutParams.width = v;
                layoutParams.height = v;
                view.setLayoutParams(layoutParams);

                GlideUtils.getInstance().loadImage(SelectRecommendAct.this, view,s.avatar);
                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                tv_nickname.setText(s.nickname);
                TextView tv_title = holder.getView(R.id.tv_title);
                tv_title.setText(s.member_role);

            }
        };
        recy_view.setAdapter(simpleRecyclerAdapter);
        simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Common.staticToast("position=="+position);
                dialogDetail();
            }
        });
    }

    @Override
    public void showFailureView() {

    }

    @Override
    public void showDataEmptyView() {

    }
}
