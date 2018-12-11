package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.NewUserGoodsEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.fragment.first_page.FirstPageFrag;
import com.shunlian.app.utils.Common;
import com.shunlian.app.widget.EditTextImage;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.mylibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *新人专享页面
 */

public class SearchMemberActivity extends BaseActivity{
    private List<NewUserGoodsEntity.Goods> lists;
    private MemberUserAdapter memberUserAdapter;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.edt_member_search)
    EditTextImage edt_member_search;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    LinearLayoutManager  manager;

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
        return R.layout.act_search_member;
    }
    @Override
    protected void initData() {
        lists = new ArrayList<>();
        for (int i =0;i<20;i++){
            lists.add(new NewUserGoodsEntity.Goods());
        }
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        memberUserAdapter = new MemberUserAdapter(this,lists);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        nei_empty.setImageResource(R.mipmap.img_bangzhu_sousuo).setText("暂无搜索结果").setButtonText(null);
        recy_view.setAdapter(memberUserAdapter);
        memberUserAdapter.notifyDataSetChanged();

        nei_empty.setVisibility(View.VISIBLE);
        recy_view.setVisibility(View.GONE);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchMemberActivity.class);
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
        edt_member_search.setOnEditorActionListener(new TextView.OnEditorActionListener(){
           @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if(arg1 == EditorInfo.IME_ACTION_SEARCH)
                {
                    Common.staticToast(edt_member_search.getText().toString());
                    Common.hideKeyboard(edt_member_search);

                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
