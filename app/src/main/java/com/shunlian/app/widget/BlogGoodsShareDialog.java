package com.shunlian.app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DiscoverGoodsAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.mylibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BlogGoodsShareDialog extends AppCompatActivity {

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.rv_goods)
    RecyclerView rv_goods;

    @BindView(R.id.ntv_desc)
    NewTextView ntv_desc;

    private BigImgEntity.Blog mBlog;
    private ImmersionBar immersionBar;
    private Unbinder unbinder;
    private Window window;

    public static void startAct(Context context, BigImgEntity.Blog blog) {
        Intent intent = new Intent(context, BlogGoodsShareDialog.class);
        intent.putExtra("blog", blog);
        context.startActivity(intent);
        ((Activity)context).overridePendingTransition(R.anim.push_bottom_in,R.anim.push_bottom_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyCompat();
        setContentView(R.layout.dialog_found_goods);
        unbinder = ButterKnife.bind(this);
        initView();
        mBlog = getIntent().getParcelableExtra("blog");
        setBlogData(mBlog);

        setFinishOnTouchOutside(true);
    }

    private void initView() {
        Window window = getWindow();
//        //设置边框距离
//        window.getDecorView().setPadding(0, 0, 0, 0);
        //设置dialog位置
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置宽高
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        window.getDecorView().setOnTouchListener((v, event) -> {
            finish();
            return false;
        });
    }

    public void setBlogData(BigImgEntity.Blog blog) {
        this.mBlog = blog;

        miv_close.setOnClickListener(v -> {
            finish();
        });
        miv_icon.setOnClickListener(v -> MyPageActivity.startAct(this, mBlog.member_id));
        ntv_desc.setOnClickListener(v -> MyPageActivity.startAct(this, mBlog.member_id));
        GlideUtils.getInstance().loadCircleImage(this, miv_icon, mBlog.avatar);
        SpannableStringBuilder ssb = Common.changeColor(mBlog.nickname
                + this.getResources().getString(R.string.discover_fenxiangdetuijian), mBlog.nickname, this.getResources().getColor(R.color.value_007AFF));
        ntv_desc.setText(ssb);
        rv_goods.setLayoutManager(new LinearLayoutManager(this));
        DiscoverGoodsAdapter discoverGoodsAdapter = new DiscoverGoodsAdapter(this, mBlog.id, mBlog.related_goods, false,
                SharedPrefUtil.getSharedUserString("nickname", ""), SharedPrefUtil.getSharedUserString("avatar", ""), this);
        rv_goods.setAdapter(discoverGoodsAdapter);
        discoverGoodsAdapter.setOnItemClickListener((v, position) -> {
            GoodsDetailAct.startAct(this, mBlog.related_goods.get(position).goods_id);
        });
        rv_goods.addItemDecoration(new MVerticalItemDecoration(this, 36, 38, 38));
    }

    private void applyCompat() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarDarkFont(true, 1).init();

        window = getWindow();
        window.setGravity(Gravity.TOP);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_bottom_out);
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }
}
