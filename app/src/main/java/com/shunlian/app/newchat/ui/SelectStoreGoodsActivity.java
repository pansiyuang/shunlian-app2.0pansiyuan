package com.shunlian.app.newchat.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.GoodsItemAdapter;
import com.shunlian.app.bean.StoreGoodsListEntity;
import com.shunlian.app.presenter.SelectStoreGoodsPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.store.StoreSortAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISelectStoreGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SelectStoreGoodsActivity extends BaseActivity implements ISelectStoreGoodsView, BaseRecyclerAdapter.OnItemClickListener {

    public static final int REQUEST_CODE = 10001;

    @BindView(R.id.edt_keyword)
    EditText edt_keyword;

    @BindView(R.id.tv_sort)
    TextView tv_sort;

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    private SelectStoreGoodsPresenter mPresenter;
    private GoodsItemAdapter mAdapter;
    private String currentStoreId;
    private LinearLayoutManager manager;
    private List<StoreGoodsListEntity.MData> mDataList;
    private String currentKeyWord;
    private String currentSrc1, currentSrc2;
    private StoreGoodsListEntity.MData mCurrentData;

    public static void startActForResult(Activity activity, String storeId, int requestCode) {
        Intent intent = new Intent(activity, SelectStoreGoodsActivity.class);
        intent.putExtra("store_id", storeId);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getLayoutId() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        return R.layout.act_select_store_goods;
    }

    @Override
    protected void initData() {
        currentStoreId = getIntent().getStringExtra("store_id");

        mDataList = new ArrayList<>();
        mPresenter = new SelectStoreGoodsPresenter(this, this);
        mPresenter.getBabyList(true, currentStoreId, currentKeyWord, currentSrc1, currentSrc2);

        manager = new LinearLayoutManager(this);
        recycler_list.setLayoutManager(manager);
        ((DefaultItemAnimator) recycler_list.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    protected void initListener() {
        edt_keyword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentKeyWord = edt_keyword.getText().toString();
                if (!isEmpty(currentKeyWord)) {
                    mPresenter.initPage();
                    currentSrc1 = "";
                    currentSrc2 = "";
                    mPresenter.getBabyList(true, currentStoreId, currentKeyWord, currentSrc1, currentSrc2);
                }
            }
            return false;
        });
        tv_sort.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sort:
                StoreSortAct.startActForResult(this, currentStoreId, REQUEST_CODE);
                break;
            case R.id.btn_submit:
                if (mCurrentData == null) {
                    Common.staticToast("请选择要选择的商品");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("select_goods", mCurrentData);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        super.onClick(view);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    @Override
    public void getBabyList(List<StoreGoodsListEntity.MData> list, int page, int totalPage) {
        if (page == 1) {
            mDataList.clear();
        }
        if (!isEmpty(list)) {
            mDataList.addAll(list);
        }

        if (mAdapter == null) {
            mAdapter = new GoodsItemAdapter(this, mDataList);
            mAdapter.setOnItemClickListener(this);
            recycler_list.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refreshFinish() {

    }

    @Override
    public void onItemClick(View view, int position) {
        mCurrentData = mDataList.get(position);
        boolean isSelect = mCurrentData.isSelect;

        for (StoreGoodsListEntity.MData mData : mDataList) {
            mData.isSelect = false;
        }

        if (isSelect) {
            mDataList.get(position).isSelect = false;
            mCurrentData = null;
            btn_submit.setText("确定0/1");
        } else {
            mDataList.get(position).isSelect = true;
            btn_submit.setText("确定1/1");
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            Intent intent = data;
            currentSrc1 = intent.getStringExtra("parentSrc");
            currentSrc2 = intent.getStringExtra("childSrc");
            edt_keyword.setText(intent.getStringExtra("keyword"));
            mPresenter.initPage();
            mPresenter.getBabyList(true, currentStoreId, currentKeyWord, currentSrc1, currentSrc2);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
