package com.shunlian.app.newchat.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.StoreMsgEntity;
import com.shunlian.app.presenter.FoundCommentPresenter;
import com.shunlian.app.ui.BaseFragment;
import com.shunlian.app.view.IFoundCommentView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class CommentFragment extends BaseFragment implements IFoundCommentView {

    private FoundCommentPresenter mPresenter;

    public static CommentFragment getInstance() {
        CommentFragment commentFragment = new CommentFragment();
        return commentFragment;
    }

    @Override
    protected View getLayoutId(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.frag_list, container, false);
        return view;
    }

    @Override
    protected void initData() {
        mPresenter = new FoundCommentPresenter(getActivity(), this);
        mPresenter.getFoundCommentList(true);
    }

    @Override
    public void getFoundCommentList(List<StoreMsgEntity.StoreMsg> list, int page, int totalPage) {
        
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
