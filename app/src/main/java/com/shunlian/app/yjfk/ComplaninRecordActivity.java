package com.shunlian.app.yjfk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shunlian.app.R;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.MyRecyclerView;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2019/4/19.
 */

public class ComplaninRecordActivity extends BaseActivity implements IOpinionbackView {
    @BindView(R.id.mrv_record)
    MyRecyclerView mrv_record;
    private LinearLayoutManager linearLayoutManager;
    private OpinionPresenter opinionPresenter;
    private ComplaninRecordActivityAdapter complaninRecordActivityAdapter;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ComplaninRecordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_record;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mrv_record.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (linearLayoutManager != null) {
                    int lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == linearLayoutManager.getItemCount()) {
                        if (opinionPresenter != null) {
                            opinionPresenter.refreshBaby();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        opinionPresenter = new OpinionPresenter(this, this);
        opinionPresenter.getcomplaintList(1, 20);
    }

    @Override
    public void getcomplaintList(List<ComplanintListEntity.Lists> entity, int allPage, int page) {
//        if (complaninRecordActivityAdapter == null) {
            complaninRecordActivityAdapter = new ComplaninRecordActivityAdapter(ComplaninRecordActivity.this, true, entity);
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mrv_record.setLayoutManager(linearLayoutManager);
            mrv_record.setAdapter(complaninRecordActivityAdapter);
//        }else {
//            complaninRecordActivityAdapter.notifyDataSetChanged();
//        }
        complaninRecordActivityAdapter.setPageLoading(page, allPage);
    }

    @Override
    public void getOpinionfeedback(OpinionfeedbackEntity entity) {

    }

    @Override
    public void uploadImg(UploadPicEntity uploadPicEntity) {

    }

    @Override
    public void setRefundPics(List<String> relativePath, boolean b) {

    }

    @Override
    public void submitSuccess(String message) {

    }

    @Override
    public void submitSuccess1(BaseEntity<Opinionfeedback1Entity> data) {

    }

    @Override
    public void getcomplaintTypes(ComplaintTypesEntity entity) {

    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
