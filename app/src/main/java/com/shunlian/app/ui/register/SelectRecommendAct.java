//package com.shunlian.app.ui.register;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.TranslateAnimation;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.shunlian.app.R;
//import com.shunlian.app.adapter.SimpleRecyclerAdapter;
//import com.shunlian.app.adapter.SimpleViewHolder;
//import com.shunlian.app.bean.MemberCodeListEntity;
//import com.shunlian.app.eventbus_bean.SelectMemberID;
//import com.shunlian.app.presenter.SelectRecommendPresenter;
//import com.shunlian.app.ui.BaseActivity;
//import com.shunlian.app.ui.h5.H5X5Act;
//import com.shunlian.app.utils.DeviceInfoUtil;
//import com.shunlian.app.utils.GlideUtils;
//import com.shunlian.app.utils.MyOnClickListener;
//import com.shunlian.app.utils.TransformUtil;
//import com.shunlian.app.view.ISelectRecommendView;
//import com.shunlian.app.widget.MyImageView;
//import com.shunlian.app.widget.MyTextView;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.List;
//
//import butterknife.BindView;
//
//public class SelectRecommendAct extends BaseActivity implements View.OnClickListener ,ISelectRecommendView {
//
//    @BindView(R.id.recy_view)
//    RecyclerView recy_view;
//
//    @BindView(R.id.tv_help)
//    TextView tv_help;
//
//    @BindView(R.id.frame_detail)
//    RelativeLayout frame_detail;
//
//    @BindView(R.id.ll_detail_content)
//    LinearLayout ll_detail_content;
//
//    @BindView(R.id.miv_icon)
//    MyImageView miv_icon;
//
//    @BindView(R.id.tv_select)
//    MyTextView tv_select;
//
//    @BindView(R.id.miv_cancale)
//    MyImageView miv_cancale;
//
//    @BindView(R.id.mtv_label)
//    MyTextView mtv_label;
//
//
//    @BindView(R.id.tv_nickname)
//    TextView tv_nickname;
//
//    @BindView(R.id.mtv_id)
//    TextView mtv_id;
//
//    @BindView(R.id.mtv_vip)
//    TextView mtv_vip;
//
//    @BindView(R.id.tv_sure)
//    TextView tv_sure;
//
//    @BindView(R.id.miv_vip)
//    MyImageView miv_vip;
//
//    @BindView(R.id.mtv_synopsis)
//    TextView mtv_synopsis;
//
//    private String recommenderId;
//    private String member_id;
//    private String nickname;
//    private boolean isSelect;//是否选择
//    private int selelctPosi = -1;//当前选择的条目
//
//    private SelectRecommendPresenter presenter;
//    private SimpleRecyclerAdapter<MemberCodeListEntity.ListBean> simpleRecyclerAdapter;
//    private String mHelpUrl;
//    private boolean isFocus;
//    private String id;
//
//
//    public static void startAct(Activity context,String id,boolean isFocus){
//        Intent intent = new Intent(context,SelectRecommendAct.class);
//        intent.putExtra("id",id);
//        intent.putExtra("isFocus",isFocus);
//        context.startActivityForResult(intent,201);
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_select_recommend;
//    }
//
//    @Override
//    protected void initListener() {
//        super.initListener();
//        tv_help.setOnClickListener(this);
//        miv_cancale.setOnClickListener(this);
//        tv_select.setOnClickListener(this);
//        tv_sure.setOnClickListener(this);
//    }
//
//    @Override
//    protected void initData() {
//        setStatusBarColor(R.color.white);
//        setStatusBarFontDark();
//
//        id = getIntent().getStringExtra("id");
//        isFocus = getIntent().getBooleanExtra("isFocus", false);
//
//        if (isFocus){
//            tv_select.setVisibility(View.INVISIBLE);
//        }
//
//        LinearLayout.LayoutParams iconParams = (LinearLayout.LayoutParams) miv_icon.getLayoutParams();
//        int[] ints1 = TransformUtil.countRealWH(this, 200, 200);
//        iconParams.width = ints1[0];
//        iconParams.height = ints1[1];
//        miv_icon.setLayoutParams(iconParams);
//
//        presenter = new SelectRecommendPresenter(this,this, id);
//        if (!isEmpty(id) && isFocus){
//            presenter.codeDetail(id);
//        }else {
//            presenter.initApi();
//        }
//
//        GridLayoutManager manager = new GridLayoutManager(this,3);
//        recy_view.setLayoutManager(manager);
//    }
//
//    private void dialogDetail() {
//        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
//                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,1,
//                Animation.RELATIVE_TO_SELF,0);
//        animation.setDuration(300);
//        frame_detail.setVisibility(View.VISIBLE);
//        frame_detail.setAnimation(animation);
//    }
//
//    private void dialogHidden(){
//        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0,
//                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,
//                0,Animation.RELATIVE_TO_SELF,1);
//        animation.setDuration(300);
//        frame_detail.setVisibility(View.GONE);
//        frame_detail.setAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//
//                if (isSelect) {
//                    EventBus.getDefault().post(recommenderId);
//                    SelectMemberID memberID = new SelectMemberID();
//                    memberID.code = recommenderId;
//                    memberID.member_id = member_id;
//                    EventBus.getDefault().post(memberID);
//                    finish();
//                    isSelect = false;
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (MyOnClickListener.isFastClick()){
//            return;
//        }
//       switch (v.getId()){
//           case R.id.tv_help://帮助
//               if (!isEmpty(mHelpUrl)){
//                   H5X5Act.startAct(this,mHelpUrl,H5X5Act.MODE_SONIC);
//               }
//               break;
//           case R.id.miv_cancale:
//               if (isFocus){
//                   dialogHidden();
//                   finish();
//               }else {
//                   dialogHidden();
//               }
//
//               break;
//           case R.id.tv_select:
//               isSelect = true;
//               simpleRecyclerAdapter.notifyDataSetChanged();
//               dialogHidden();
//               break;
//           case R.id.tv_sure:
//               presenter.initApi();
//               selelctPosi = -1;
//               break;
//       }
//    }
//
//    @Override
//    public void selectCodeList(final List<MemberCodeListEntity.ListBean> listBeens) {
//
//        simpleRecyclerAdapter = new SimpleRecyclerAdapter<MemberCodeListEntity.ListBean>(
//                this, R.layout.item_recommend_select, listBeens) {
//
//            @Override
//            public void convert(SimpleViewHolder holder, MemberCodeListEntity.ListBean s,int position) {
//                holder.addOnClickListener(R.id.civ_head);
//                //头像
//                MyImageView view = holder.getView(R.id.civ_head);
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
//                int deviceWidth = DeviceInfoUtil.getDeviceWidth(SelectRecommendAct.this);
//                int v = (int) (160.0f / 750 * deviceWidth + 0.5f);
//                layoutParams.width = v;
//                layoutParams.height = v;
//                view.setLayoutParams(layoutParams);
//
//                GlideUtils.getInstance().loadCircleHeadImage(SelectRecommendAct.this, view,s.avatar);
//                //昵称
//                TextView tv_nickname = holder.getView(R.id.tv_nickname);
//                tv_nickname.setText(s.nickname);
//
//                MyImageView miv_select = holder.getView(R.id.miv_select);
//                if (position == selelctPosi){
//                    visible(miv_select);
//                }else {
//                    gone(miv_select);
//                }
//
//                if (s.member_id.equals(id)){
//                    visible(miv_select);
//                }else {
//                    gone(miv_select);
//                }
//
//                MyImageView miv_large_vip = holder.getView(R.id.miv_large_vip);
//                int memberRole = Integer.parseInt(isEmpty(s.role)?"0":s.role);
//                if (memberRole == 1){//店主
//                    miv_large_vip.setImageResource(R.mipmap.img_plus_phb_dianzhu);
//                }else if (memberRole == 2){//主管
//                    miv_large_vip.setImageResource(R.mipmap.img_plus_phb_zhuguan);
//                }else if (memberRole >= 3){//经理
//                    miv_large_vip.setImageResource(R.mipmap.img_plus_phb_jingli);
//                }else {
//                    if (!isEmpty(s.level)) {
//                        Bitmap bitmap = TransformUtil.convertNewVIP(SelectRecommendAct.this, s.level);
//                        miv_large_vip.setImageBitmap(bitmap);
//                        miv_large_vip.setVisibility(View.VISIBLE);
//                    } else {
//                        miv_large_vip.setVisibility(View.GONE);
//                    }
//                }
//            }
//        };
//        recy_view.setAdapter(simpleRecyclerAdapter);
//
//        simpleRecyclerAdapter.setOnItemClickListener((view, position) -> {
//            if (isFocus)return;
//            selelctPosi = position;
//
//            MemberCodeListEntity.ListBean listBean = listBeens.get(position);
//            showDialog(listBean);
//        });
//    }
//
//    private void showDialog(MemberCodeListEntity.ListBean listBean) {
//        GlideUtils.getInstance()
//                .loadCircleHeadImage(SelectRecommendAct.this, miv_icon,listBean.avatar);
//        tv_nickname.setText(listBean.nickname);
//
//        nickname = listBean.nickname;
//        recommenderId = listBean.code;
//        member_id = listBean.member_id;
//
//        //等级描述
//        mtv_vip.setText(listBean.member_role_msg);
//        //等级
//        int memberRole = Integer.parseInt(isEmpty(listBean.role)?"0":listBean.role);
//        if (memberRole == 1){//店主
//            miv_vip.setImageResource(R.mipmap.img_plus_phb_dianzhu);
//        }else if (memberRole == 2){//主管
//            miv_vip.setImageResource(R.mipmap.img_plus_phb_zhuguan);
//        }else if (memberRole >= 3){//经理
//            miv_vip.setImageResource(R.mipmap.img_plus_phb_jingli);
//        }else {
//            if(!isEmpty(listBean.level)){
//                Bitmap bitmap = TransformUtil.convertNewVIP(SelectRecommendAct.this, listBean.level);
//                miv_vip.setImageBitmap(bitmap);
//                //等级描述
//                mtv_vip.setText(listBean.level);
//                mtv_vip.setVisibility(View.VISIBLE);
//                miv_vip.setVisibility(View.VISIBLE);
//            } else {
//                miv_vip.setVisibility(View.GONE);
//                mtv_vip.setVisibility(View.GONE);
//            }
//        }
//        //推荐人id
//        mtv_id.setText(recommenderId);
//        //标签
//        mtv_label.setText(listBean.tag_val);
//        //简介
//        mtv_synopsis.setText(listBean.signature);
//
//        dialogDetail();
//    }
//
//    @Override
//    public void help(String url) {
//        mHelpUrl = url;
//    }
//
//    /**
//     * 详情
//     *
//     * @param bean
//     */
//    @Override
//    public void codeInfo(MemberCodeListEntity bean) {
//        if (bean.info != null)
//            showDialog(bean.info);
//    }
//
//    @Override
//    public void showFailureView(int rquest_code) {
//
//    }
//
//    @Override
//    public void showDataEmptyView(int rquest_code) {
//
//    }
//
//    private boolean inRangeOfView(View view, MotionEvent ev){
//        int[] location = new int[2];
//        view.getLocationOnScreen(location);
//        int x = location[0];
//        int y = location[1];
//        if(ev.getX() < x || ev.getX() > (x + view.getWidth())
//                || ev.getY() < y || ev.getY() > (y + view.getHeight())){
//            return false;
//        }
//        return true;
//    }
//}
