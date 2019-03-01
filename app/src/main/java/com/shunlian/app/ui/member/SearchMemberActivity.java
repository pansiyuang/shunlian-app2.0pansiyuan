package com.shunlian.app.ui.member;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberUserAdapter;
import com.shunlian.app.bean.MemberInfoEntity;
import com.shunlian.app.presenter.MemberPagePresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.view.IMemberPageView;
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

public class SearchMemberActivity extends BaseActivity  implements IMemberPageView {
    private List<MemberInfoEntity.MemberList> lists;
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
    MemberPagePresenter memberPagePresenter;
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
        ImmersionBar.with(this).fitsSystemWindows(true)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true, 0.2f)
                .init();
        memberPagePresenter = new MemberPagePresenter(this,this);
        memberUserAdapter = new MemberUserAdapter(this,lists,false);
        manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        nei_empty.setImageResource(R.mipmap.img_bangzhu_sousuo).setText("暂无搜索结果").setButtonText(null);
        recy_view.setAdapter(memberUserAdapter);
        memberUserAdapter.notifyDataSetChanged();

        nei_empty.setVisibility(View.GONE);
        recy_view.setVisibility(View.GONE);
    }

    public static void startAct(Context context) {
        Intent intent = new Intent(context, SearchMemberActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initListener() {
        recy_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (memberPagePresenter != null) {
                            memberPagePresenter.onRefresh();
                        }
                    }
                }
            }
        });
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
                    Common.hideKeyboard(edt_member_search);
                    if(memberPagePresenter!=null){
                        memberPagePresenter.initApiMemberKey(edt_member_search.getText().toString());
                    }
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

    @Override
    public void memberListInfo(List<MemberInfoEntity.MemberList> memberLists, int currentPage) {
        if(memberLists.size()==0&&currentPage==1){
            lists.clear();
            memberUserAdapter.notifyDataSetChanged();
            nei_empty.setVisibility(View.VISIBLE);
            recy_view.setVisibility(View.GONE);
        }else{
            nei_empty.setVisibility(View.GONE);
            recy_view.setVisibility(View.VISIBLE);
            if(currentPage==1){
                lists.clear();
            }
            lists.addAll(memberLists);
            memberUserAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void memberDetail(MemberInfoEntity memberInfoEntity, String total_num) {

    }

    @Override
    public void setWeixin(String weixin) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
