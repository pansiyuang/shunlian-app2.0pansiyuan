package com.shunlian.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.shunlian.app.R;
import com.shunlian.app.adapter.DrawRecordPresenter;
import com.shunlian.app.adapter.SaturdayDrawRecordAdapter;
import com.shunlian.app.bean.SaturdayDrawRecordEntity;
import com.shunlian.app.view.IDrawRecordView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DrawRecordDialog extends Dialog implements IDrawRecordView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private Unbinder bind;
    private LinearLayoutManager manager;
    private DrawRecordPresenter mPresenter;
    private SaturdayDrawRecordAdapter mAdapter;
    private List<SaturdayDrawRecordEntity.SaturdayDrawRecord> mRecordList;

    public DrawRecordDialog(Context context) {
        this(context, R.style.popAd);
    }

    public DrawRecordDialog(Context context, int themeResId) {
        super(context, themeResId);

        View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_draw_record, null, false);
        setContentView(inflate);
        bind = ButterKnife.bind(this, inflate);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(attributes);

        mPresenter = new DrawRecordPresenter(getContext(), this);
        mPresenter.getDrawRecord(true);
        mRecordList = new ArrayList<>();
        mAdapter = new SaturdayDrawRecordAdapter(context, mRecordList);
        manager = new LinearLayoutManager(context);
        recycler_list.setLayoutManager(manager);
        recycler_list.setAdapter(mAdapter);

        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        mPresenter.getDrawRecord(false);
                    }
                }
            }
        });
        miv_close.setOnClickListener(v -> dismiss());
    }

    public void initPage() {
        mPresenter.initPage();
    }

    @Override
    public void getDrawRecord(List<SaturdayDrawRecordEntity.SaturdayDrawRecord> saturdayDrawRecordList, int page, int total) {
        if (page == 1) {
            mRecordList.clear();
        }
        if (saturdayDrawRecordList != null || saturdayDrawRecordList.size() != 0) {
            mRecordList.addAll(saturdayDrawRecordList);
            mAdapter.setPageLoading(page, total);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
