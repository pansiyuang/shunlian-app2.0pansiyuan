package com.shunlian.app.ui.register;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectRecommendView;
import com.shunlian.app.widget.MyImageView;
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

    @BindView(R.id.tv_select)
    TextView tv_select;

    @BindView(R.id.tv_notSelect)
    TextView tv_notSelect;

    @BindView(R.id.tv_nickname)
    TextView tv_nickname;

    @BindView(R.id.tv_resg_time)
    TextView tv_resg_time;

    @BindView(R.id.tv_hot)
    TextView tv_hot;

    @BindView(R.id.tv_sure)
    TextView tv_sure;

    private String recommenderId;
    private String nickname;
    private int selelctPosi = -1;//当前选择的条目

    private SelectRecommendPresenter presenter;
    private SimpleRecyclerAdapter<MemberCodeListEntity.ListBean> simpleRecyclerAdapter;


    public static void startAct(Activity context){
        Intent intent = new Intent(context,SelectRecommendAct.class);
        context.startActivityForResult(intent,201);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_recommend;
    }

    @Override
    protected void initListener() {
        super.initListener();
        tv_next.setOnClickListener(this);
        tv_notSelect.setOnClickListener(this);
        tv_select.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        frame_detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean b = inRangeOfView(ll_detail_content, event);
                if (!b){
                    dialogHidden();
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        setHeader();

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
        animation.setDuration(300);
        frame_detail.setVisibility(View.VISIBLE);
        frame_detail.setAnimation(animation);
    }

    private void dialogHidden(){
        sv_mask.setVisibility(View.GONE);
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1);
        animation.setDuration(300);
        frame_detail.setVisibility(View.GONE);
        frame_detail.setAnimation(animation);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.tv_next:
               break;
           case R.id.tv_notSelect:
               dialogHidden();
               break;
           case R.id.tv_select:
               simpleRecyclerAdapter.notifyDataSetChanged();
               dialogHidden();
               break;
           case R.id.tv_sure:
               Intent intent = new Intent();
               intent.putExtra("id",recommenderId);
               intent.putExtra("nickname",nickname);
               setResult(200,intent);
               finish();
               break;
       }
    }

    @Override
    public void selectCodeList(final List<MemberCodeListEntity.ListBean> listBeens) {
        GridLayoutManager manager = new GridLayoutManager(this,3);
        recy_view.setLayoutManager(manager);

        simpleRecyclerAdapter = new SimpleRecyclerAdapter<MemberCodeListEntity.ListBean>(this, R.layout.item_recommend_select, listBeens) {

            @Override
            public void convert(SimpleViewHolder holder, MemberCodeListEntity.ListBean s,int position) {
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
                MyImageView miv_select = holder.getView(R.id.miv_select);
                if (position == selelctPosi){
                    miv_select.setVisibility(View.VISIBLE);
                }else {
                    miv_select.setVisibility(View.GONE);
                }

            }
        };
        recy_view.setAdapter(simpleRecyclerAdapter);
        simpleRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selelctPosi = position;
                MemberCodeListEntity.ListBean listBean = listBeens.get(position);
                GlideUtils.getInstance().loadImage(SelectRecommendAct.this, miv_icon,listBean.avatar);
                tv_nickname.setText(listBean.nickname);
                tv_resg_time.setText(listBean.regtime);
                tv_hot.setText(listBean.heat);
                nickname = listBean.nickname;
                recommenderId = listBean.code;
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

    private boolean inRangeOfView(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y || ev.getY() > (y + view.getHeight())){
            return false;
        }
        return true;
    }
}
