package com.shunlian.app.newchat.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.TopicMsgAdapter;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.presenter.FoundTopicPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.ui.discover.jingxuan.ArticleH5Act;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IFoundTopicView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.refresh.turkey.SlRefreshView;
import com.shunlian.app.widget.refreshlayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/10.
 */

public class TopicFragment extends BaseFragment implements IFoundTopicView, BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    private FoundTopicPresenter mPresenter;
    private LinearLayoutManager manager;
    private List<StoreMsgEntity.StoreMsg> storeMsgList;
    private TopicMsgAdapter mAdapter;

    public static TopicFragment getInstance() {
        TopicFragment topicFragment = new TopicFragment();
        return topicFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_refresh, container, false);
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new FoundTopicPresenter(getActivity(), this);
        mPresenter.getFoundTopicList(true);
        manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);

        storeMsgList = new ArrayList<>();
        nei_empty.setImageResource(R.mipmap.img_empty_common).setText("暂无数据");
        nei_empty.setButtonText(null);
    }

    @Override
    protected void initListener() {
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
    public void getFoundTopicList(List<StoreMsgEntity.StoreMsg> list, int page, int totalPage) {
        if (page == 1) {
            storeMsgList.clear();
            if (!isEmpty(list)) {
                nei_empty.setVisibility(View.GONE);
                recycler_list.setVisibility(View.VISIBLE);
            } else {
                recycler_list.setVisibility(View.GONE);
                nei_empty.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(list)) {
            storeMsgList.addAll(list);
        }
        if (mAdapter == null) {
            mAdapter = new TopicMsgAdapter(getActivity(), storeMsgList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.setPageLoading(page, totalPage);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void onItemClick(View view, int position) {
        StoreMsgEntity.StoreMsg storeMsg = storeMsgList.get(position);
        StoreMsgEntity.Body body = storeMsg.body;
        if (1 == body.expire) {
            ArticleH5Act.startAct(getActivity(), body.id, ArticleH5Act.MODE_SONIC);
        } else {
            Common.staticToast("该文章已过期");
        }

        if (storeMsg.is_read == 0) {
            storeMsg.is_read = 1;
            mPresenter.msgRead(String.valueOf(storeMsg.type), storeMsg.id);
            mAdapter.notifyItemChanged(position, storeMsg);
        }
    }
}
