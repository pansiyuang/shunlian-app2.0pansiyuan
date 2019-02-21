package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CreditlogAdapter;
import com.shunlian.app.bean.CreditLogEntity;
import com.shunlian.app.presenter.ScoreRecordPresenter;
import com.shunlian.app.view.IScoreRecordView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ScoreRecordDialog extends Dialog implements IScoreRecordView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private Context mContext;
    private Unbinder bind;
    private LinearLayoutManager manager;
    private CreditlogAdapter mAdapter;
    private List<CreditLogEntity.CreditLog> mCreditLogList;
    private ScoreRecordPresenter mPresenter;

    public ScoreRecordDialog(Context context) {
        this(context, R.style.popAd);
        this.mContext = context;
    }

    public ScoreRecordDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        initView();
    }

    private void initView() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_score_record, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);
        setCanceledOnTouchOutside(false);
        manager = new LinearLayoutManager(mContext);
        recycler_list.setLayoutManager(manager);

        miv_close.setOnClickListener(v -> dismiss());
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        mPresenter.getScoreRecord(false);
                    }
                }
            }
        });
        mCreditLogList = new ArrayList<>();
        mPresenter = new ScoreRecordPresenter(getContext(), this);
        mPresenter.getScoreRecord(true);
        manager = new LinearLayoutManager(getContext());
        recycler_list.setLayoutManager(manager);
        mAdapter = new CreditlogAdapter(mContext, mCreditLogList);
        recycler_list.setAdapter(mAdapter);
    }


    public void initPage() {
        if (mPresenter != null) {
            mPresenter.initPage();
            mPresenter.getScoreRecord(true);
        }
    }

    @Override
    public void getScoreRecord(List<CreditLogEntity.CreditLog> creditLogList, int page, int totalPage) {
        if (page == 1) {
            mCreditLogList.clear();
        }
        mCreditLogList.addAll(creditLogList);
        mAdapter.setPageLoading(page, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
