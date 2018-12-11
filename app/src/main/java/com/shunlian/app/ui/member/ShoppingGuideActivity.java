package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.listener.ICallBackResult;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.CommonDialogUtil;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class ShoppingGuideActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.relt_empty_guide)
    RelativeLayout relt_empty_guide;

    @BindView(R.id.tv_add_guide)
    TextView tv_add_guide;

    private CommonDialogUtil commonDialogUtil;
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_shop_guide;
    }
    @Override
    protected void initData() {
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        commonDialogUtil = new CommonDialogUtil(this);
        nei_empty.setImageResource(R.mipmap.icon_login_logo).setText("您还没有导购专员，快去绑定吧！").setButtonText(null);
        relt_empty_guide.setVisibility(View.VISIBLE);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ShoppingGuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_add_guide.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.tv_add_guide){
            commonDialogUtil.inputGuideCommonDialog(new ICallBackResult<String>() {
                @Override
                public void onTagClick(String data) {

                }
            });
        }
    }

    /**
     * 设置引导用户的信息
     */
    private void showGuideUserInfo(){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
