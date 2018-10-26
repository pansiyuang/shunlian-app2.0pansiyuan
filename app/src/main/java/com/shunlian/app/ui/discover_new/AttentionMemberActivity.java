package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.MemberListAdapter;
import com.shunlian.app.bean.MemberEntity;
import com.shunlian.app.presenter.AttentionMemberPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.view.IAttentionMemberView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/22.
 */

public class AttentionMemberActivity extends BaseActivity implements IAttentionMemberView, MemberListAdapter.OnAdapterCallBack {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.refreshview)
    SlRefreshView refreshview;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    @BindView(R.id.tv_title)
    TextView tv_title;

    private AttentionMemberPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<MemberEntity.Member> memberList;
    private MemberListAdapter mAdapter;
    private String currentMemberId;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, AttentionMemberActivity.class);
        context.startActivity(intent);
    }

    public static void startAct(Context context, String memberId) {
        Intent intent = new Intent(context, AttentionMemberActivity.class);
        intent.putExtra("member_id", memberId);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_my_fans;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        currentMemberId = getIntent().getStringExtra("member_id");

        mPresenter = new AttentionMemberPresenter(this, this);

        if (isEmpty(currentMemberId)) {
            tv_title.setText("我的关注");
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("你还没有关注的人哟")
                    .setButtonText(null);

            mPresenter.getAttentionList(true);
        } else {
            tv_title.setText("Ta的关注");
            nei_empty.setImageResource(R.mipmap.img_empty_common)
                    .setText("Ta还没有还没有关注的人哟")
                    .setButtonText(null);

            mPresenter.getTaAttentionList(true, currentMemberId);
        }

        refreshview.setCanRefresh(true);
        refreshview.setCanLoad(false);
        recycler_list.setNestedScrollingEnabled(false);

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        memberList = new ArrayList<>();

    }

    @Override
    protected void initListener() {
        refreshview.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mPresenter != null) {
                    mPresenter.initPage();
                    if (isEmpty(currentMemberId)) {
                        mPresenter.getAttentionList(true);
                    } else {
                        mPresenter.getTaAttentionList(true, currentMemberId);
                    }
                }
            }

            @Override
            public void onLoadMore() {

            }
        });
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        super.initListener();
    }

    @Override
    public void getAttentionList(List<MemberEntity.Member> list, int page, int totalPage) {
        refreshview.stopRefresh(true);
        if (page == 1) {
            memberList.clear();
        }
        if (!isEmpty(list)) {
            memberList.addAll(list);
        }

        if (page == 1 && isEmpty(memberList)) {
            nei_empty.setVisibility(View.VISIBLE);
            recycler_list.setVisibility(View.GONE);
        } else {
            nei_empty.setVisibility(View.GONE);
            recycler_list.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new MemberListAdapter(this, memberList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setAdapterCallBack(this);
        }
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (MemberEntity.Member member : memberList) {
            if (memberId.equals(member.member_id)) {
                if (member.focus_status == 0) {
                    member.focus_status = 1;
                } else {
                    member.focus_status = 0;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void toFocusUser(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }
}
