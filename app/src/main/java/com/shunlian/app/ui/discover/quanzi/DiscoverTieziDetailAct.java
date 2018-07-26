package com.shunlian.app.ui.discover.quanzi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SinglePicAdapter;
import com.shunlian.app.adapter.TieziAvarAdapter;
import com.shunlian.app.adapter.TieziCommentAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommonEntity;
import com.shunlian.app.bean.DiscoveryCommentListEntity;
import com.shunlian.app.presenter.PDiscoverTieziDetail;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.HorizonItemDecoration;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IDiscoverTieziDetail;
import com.shunlian.app.view.IFindCommentListView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyScrollView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DiscoverTieziDetailAct extends BaseActivity implements View.OnClickListener, IDiscoverTieziDetail, IFindCommentListView {
//    @BindView(R.id.kanner_tiezi)
//    TieziKanner kanner_tiezi;

    @BindView(R.id.miv_avar)
    MyImageView miv_avar;

    @BindView(R.id.miv_like)
    MyImageView miv_like;

    @BindView(R.id.miv_pic)
    MyImageView miv_pic;

    @BindView(R.id.mtv_name)
    MyTextView mtv_name;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.mtv_like)
    MyTextView mtv_like;

    @BindView(R.id.mtv_desc)
    MyTextView mtv_desc;

    @BindView(R.id.msv_out)
    MyScrollView msv_out;

    @BindView(R.id.rv_avar)
    RecyclerView rv_avar;

    @BindView(R.id.rv_pics)
    RecyclerView rv_pics;

    @BindView(R.id.rv_remark)
    RecyclerView rv_remark;

    @BindView(R.id.met_text)
    MyEditText met_text;

    @BindView(R.id.mtv_msg_count)
    MyTextView mtv_msg_count;

    @BindView(R.id.mtv_send)
    MyTextView mtv_send;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private PDiscoverTieziDetail pDiscoverTieziDetail;
    private boolean isLike;
    private String circle_id, inv_id;
    private TieziAvarAdapter avarAdapter;
    private List<String> avars;
    private int num = 0;
    private TieziCommentAdapter commentAdapter;
    private List<String> imgs;

    //    private DiscoverHotAdapter newAdapter;
    public static void startAct(Context context, String circle_id, String inv_id, List<String> imgs) {
        Intent intent = new Intent(context, DiscoverTieziDetailAct.class);
        intent.putExtra("circle_id", circle_id);
        intent.putExtra("inv_id", inv_id);
        intent.putExtra("imgs", (Serializable) imgs);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
//        setHideStatus();
        return R.layout.act_discover_tiezi_detail;
    }

    @OnClick(R.id.mtv_send)
    public void send() {
        String s = met_text.getText().toString();
        pDiscoverTieziDetail.faBu(circle_id, inv_id, s);
        met_text.setText("");
        met_text.setHint(getStringResouce(R.string.add_comments));
        setEdittextFocusable(false, met_text);
        Common.hideKeyboard(met_text);
    }

    @OnClick(R.id.met_text)
    public void onClick() {
        setEdittextFocusable(true, met_text);
        if (!isSoftShowing()) {
            Common.showKeyboard(met_text);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_like:
            case R.id.mtv_like:
                if (isLike) {
                    pDiscoverTieziDetail.dianZan(circle_id, inv_id, "2");
                } else {
                    pDiscoverTieziDetail.dianZan(circle_id, inv_id, "1");
                }
                break;
        }
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return screenHeight - rect.bottom != 0;
    }

    @Override
    public void setCommentAllCount(String count) {
        GradientDrawable background = (GradientDrawable) mtv_msg_count.getBackground();
        int w = TransformUtil.dip2px(this, 0.5f);
        background.setStroke(w, getColorResouce(R.color.white));
        mtv_msg_count.setText(count);
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {

    }

    @Override
    public void delPrompt() {

    }

    /**
     * 软键盘处理
     */
    @Override
    public void showorhideKeyboard(String hint) {
        setEdittextFocusable(true, met_text);
        met_text.setHint(hint);
        if (!isSoftShowing()) {
            Common.showKeyboard(met_text);
        } else {
            Common.hideKeyboard(met_text);
        }
    }

    @Override
    protected void initListener() {
        super.initListener();
        mtv_like.setOnClickListener(this);
        miv_like.setOnClickListener(this);
        met_text.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (!isEmpty(s)) {
                    visible(mtv_send);
                    gone(miv_icon, mtv_msg_count);
                } else {
                    gone(mtv_send);
                    visible(miv_icon, mtv_msg_count);
                }
            }
        });
        msv_out.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void scrollCallBack(boolean isScrollBottom, int height, int y, int oldy) {
                if (isScrollBottom && pDiscoverTieziDetail != null) {
                    pDiscoverTieziDetail.refreshBaby();
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        circle_id = getIntent().getStringExtra("circle_id");
        inv_id = getIntent().getStringExtra("inv_id");
        imgs = (List<String>) getIntent().getSerializableExtra("imgs");
        BitmapUtil.discoverImg(miv_pic,rv_pics,null,imgs
                ,this,0,0,20,28,20,20,0,0);
        pDiscoverTieziDetail = new PDiscoverTieziDetail(this, this, circle_id, inv_id);
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.discover_wolaishuo));
        nei_empty.setButtonText(null);
    }

    @Override
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }


    @Override
    public void setApiData(DiscoveryCommentListEntity.Mdata data, List<DiscoveryCommentListEntity.Mdata.Commentlist> mdatas) {
        if (isEmpty(mdatas)) {
            visible(nei_empty);
            gone(rv_remark);
        } else {
            gone(nei_empty);
            visible(rv_remark);
        }
        if (commentAdapter == null) {
            avars = new ArrayList<>();
            num = Integer.parseInt(data.commentcounts);
            avars.addAll(data.inv_info.five_member_likes);
            avarAdapter = new TieziAvarAdapter(getBaseContext(), false, avars);
            rv_avar.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
            rv_avar.addItemDecoration(new HorizonItemDecoration(TransformUtil.dip2px(getBaseContext(), -12)));
            rv_avar.setAdapter(avarAdapter);
//            if (data.inv_info != null && data.inv_info.img != null) {
//                kanner_tiezi.layoutRes=R.layout.layout_kanner_rectangle_indicator;
//                kanner_tiezi.setBanner(data.inv_info.img);
//                kanner_tiezi.setOnItemClickL(new BaseBanner.OnItemClickL() {
//                    @Override
//                    public void onItemClick(int position) {
//
//                    }
//                });
//
//            }
            GlideUtils.getInstance().loadCircleImage(getBaseContext(), miv_avar, data.inv_info.author_info.avatar);
            mtv_name.setText(data.inv_info.author_info.nickname);
            mtv_time.setText(data.inv_info.create_time);
            mtv_like.setText(data.inv_info.likes);
            if ("1".equals(data.inv_info.is_likes)) {
                isLike = true;
                miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
                mtv_like.setTextColor(getColorResouce(R.color.pink_color));
            } else {
                isLike = false;
                miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_ns);
                mtv_like.setTextColor(getColorResouce(R.color.value_88));
            }
            mtv_desc.setText(data.inv_info.content);
            commentAdapter = new TieziCommentAdapter(this, circle_id, inv_id, true, mdatas);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
            rv_remark.setLayoutManager(linearLayoutManager);
            rv_remark.setNestedScrollingEnabled(false);
            rv_remark.addItemDecoration(new VerticalItemDecoration(28, 0, 0));
            rv_remark.setAdapter(commentAdapter);

        } else {
            commentAdapter.notifyDataSetChanged();
        }
        commentAdapter.setPageLoading(Integer.parseInt(data.page), Integer.parseInt(data.total_page));

    }

    @Override
    public void dianZan(CommonEntity data) {
        if (!isLike) {
            isLike = true;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_h);
            mtv_like.setTextColor(getColorResouce(R.color.pink_color));
        } else {
            isLike = false;
            miv_like.setImageResource(R.mipmap.icon_found_quanzi_xin_ns);
            mtv_like.setTextColor(getColorResouce(R.color.value_88));
        }
        mtv_like.setText(String.valueOf(data.likes));
        avars.clear();
        avars.addAll(data.five_member_likes);
        avarAdapter.notifyDataSetChanged();
    }

    @Override
    public void faBu(DiscoveryCommentListEntity.Mdata.Commentlist data) {
        pDiscoverTieziDetail.mDatas.add(0, data);
        commentAdapter.notifyDataSetChanged();
        num++;
        mtv_msg_count.setText(String.valueOf(num));
    }

}
