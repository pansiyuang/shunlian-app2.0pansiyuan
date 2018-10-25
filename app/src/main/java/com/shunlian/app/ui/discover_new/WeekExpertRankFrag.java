package com.shunlian.app.ui.discover_new;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.WeekExpertAdapter;
import com.shunlian.app.bean.ExpertEntity;
import com.shunlian.app.presenter.WeekExpertPresenter;
import com.shunlian.app.ui.BaseLazyFragment;
import com.shunlian.app.view.IWeekExpertView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/10/19.
 */

public class WeekExpertRankFrag extends BaseLazyFragment implements IWeekExpertView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private WeekExpertPresenter mPresenter;
    private List<ExpertEntity.Expert> experts;
    private WeekExpertAdapter weekExpertAdapter;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_week_expert_rank, null, false);
        return view;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();

        mPresenter = new WeekExpertPresenter(getActivity(), this);
        mPresenter.getWeekExpertList();
        experts = new ArrayList<>();
    }

    @Override
    public void expertList(List<ExpertEntity.Expert> expertList) {
        if (!isEmpty(expertList)) {
            experts.addAll(expertList);
        }
        if (weekExpertAdapter == null) {
            weekExpertAdapter = new WeekExpertAdapter(getActivity(), experts, this);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_list.setLayoutManager(manager);
            recycler_list.setAdapter(weekExpertAdapter);
        }
        weekExpertAdapter.notifyDataSetChanged();
    }

    @Override
    public void focusUser(int isFocus, String memberId) {
        for (ExpertEntity.Expert expert : experts) {
            if (memberId.equals(expert.member_id)) {
                if (expert.focus_status == 0) {
                    expert.focus_status = 1;
                } else {
                    expert.focus_status = 0;
                }
            }
            weekExpertAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void toFocus(int isFocus, String memberId) {
        mPresenter.focusUser(isFocus, memberId);
    }
}
