package com.shunlian.app.ui.discover;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.ExperienceAdapter;
import com.shunlian.app.bean.ExperienceEntity;
import com.shunlian.app.presenter.ExperiencePresenter;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.IExperienceView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class DiscoverXindeFrag extends DiscoversFrag implements IExperienceView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private ExperiencePresenter mPresenter;
    private ExperienceAdapter mAdapter;
    private List<ExperienceEntity.Experience> experienceList;
    private String currentId;
    private String currentStatus;

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.frag_discovers_xinde, container, false);
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);
        mPresenter = new ExperiencePresenter(getActivity(), this);
        mPresenter.getExperienceList(true);
        recycler_list.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(getActivity(), 10), 0, 0));
        ((SimpleItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);

        experienceList = new ArrayList<>();
        mAdapter = new ExperienceAdapter(getActivity(), experienceList, this);
        recycler_list.setAdapter(mAdapter);
    }

    /**
     * 管理是否可点击
     *
     * @return
     */
    @Override
    public boolean isClickManage() {
        return false;
    }

    @Override
    public void getExperienceList(List<ExperienceEntity.Experience> list, int page, int totalPage) {
        if (page == 1) {
            experienceList.clear();
        }

        if (!isEmpty(list)) {
            experienceList.addAll(list);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void praiseExperience() {
        for (int i = 0; i < experienceList.size(); i++) {
            ExperienceEntity.Experience experience = experienceList.get(i);
            if (currentId.equals(experience.id)) {
                int count = Integer.valueOf(experience.praise_num);
                if ("2".equals(currentStatus)) {
                    count -= 1;
                } else {
                    count += 1;
                }
                experience.praise = currentStatus;
                experience.praise_num = String.valueOf(count);
                mAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public void toPraiseExperience(String id, String status) {
        currentId = id;
        currentStatus = status;
        mPresenter.praiseExperience(id, status);
    }
}