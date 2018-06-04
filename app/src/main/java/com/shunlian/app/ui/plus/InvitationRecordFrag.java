package com.shunlian.app.ui.plus;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.adapter.InvitationRecordAdapter;
import com.shunlian.app.bean.InvitationEntity;
import com.shunlian.app.presenter.InvitationRecordPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.view.IInvitationRecordeView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/5/28.
 * 邀请记录界面
 */

public class InvitationRecordFrag extends BaseFragment implements IInvitationRecordeView {

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface nei_empty;

    RecyclerView recycler_list;
    private InvitationRecordPresenter mPresenter;
    private InvitationRecordAdapter mAdapter;
    private List<InvitationEntity.Invitation> mList;

    public static InvitationRecordFrag getInstance() {
        InvitationRecordFrag invitationRecordFrag = new InvitationRecordFrag();
        return invitationRecordFrag;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_invitation_record, container, false);
        recycler_list = (RecyclerView) view.findViewById(R.id.recycler_list);
        return view;
    }

    @Override
    protected void initData() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycler_list.setLayoutManager(manager);

        nei_empty.setImageResource(R.mipmap.img_empty_common).setText(getString(R.string.plus_ninhaimei));
        nei_empty.setButtonText(null);

        mList = new ArrayList<>();
        mPresenter = new InvitationRecordPresenter(getActivity(), this);
        mPresenter.getInviteHistory(true);
    }


    @Override
    public void getInvitationRecord(int page, int totalPage, List<InvitationEntity.Invitation> invitationList) {

        if (page == 1) {
            mList.clear();
        }
        if (!isEmpty(invitationList)) {
            mList.addAll(invitationList);
        }

        if (mAdapter == null) {
            mAdapter = new InvitationRecordAdapter(getActivity(), mList);
            recycler_list.setNestedScrollingEnabled(false);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        if (isEmpty(mList)){
            visible(nei_empty);
            gone(recycler_list);
        }else {
            visible(recycler_list);
            gone(nei_empty);
        }
    }

    @Override
    public void showFailureView(int request_code) {
        visible(nei_empty);
        gone(recycler_list);
    }

    @Override
    public void showDataEmptyView(int request_code) {
       visible(nei_empty);
       gone(recycler_list);
    }
}
