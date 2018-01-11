package com.shunlian.app.ui.order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundDetailEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.PlatformInterventionPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPlatformInterventionRequestView;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.ReturnGoodsDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shunlian.app.adapter.SingleImgAdapter.REQUEST_CAMERA_CODE;

/**
 * Created by Administrator on 2018/1/8.
 */

public class PlatformInterventionRequestActivity extends BaseActivity implements View.OnClickListener, IPlatformInterventionRequestView, ReturnGoodsDialog.ISelectListener {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.rl_title_more)
    RelativeLayout rl_title_more;

    @BindView(R.id.mtv_time)
    MyTextView mtv_time;

    @BindView(R.id.tv_select_status)
    TextView tv_select_status;

    @BindView(R.id.tv_request_complete)
    TextView tv_request_complete;

    @BindView(R.id.edt_remark)
    EditText edt_remark;

    @BindView(R.id.grid_imgs)
    GridView grid_imgs;

    public RefundDetailEntity.RefundDetail.Edit mEdit;
    private ReturnGoodsDialog dialog;
    private SingleImgAdapter singleImgAdapter;
    private List<ImageEntity> mImageList = new ArrayList<>();
    private List<ImageEntity> imageEntityList = new ArrayList<>();
    private PlatformInterventionPresenter presenter;
    private String refundId;
    private String currentStatusId;
    private boolean isEdit;
    private int index;

    public static void startAct(Context context, String refundId, RefundDetailEntity.RefundDetail.Edit edit, boolean isEdit) {
        Intent intent = new Intent(context, PlatformInterventionRequestActivity.class);
        intent.putExtra("edit", edit);
        intent.putExtra("refundId", refundId);
        intent.putExtra("isEdit", isEdit);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_platform_intervention_request;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.platform_intervention_request));
        rl_title_more.setVisibility(View.VISIBLE);

        refundId = getIntent().getStringExtra("refundId");
        mEdit = (RefundDetailEntity.RefundDetail.Edit) getIntent().getSerializableExtra("edit");
        isEdit = getIntent().getBooleanExtra("isEdit", false);

        mtv_time.setText(mEdit.time_desc);
        mtv_time.setVisibility(View.VISIBLE);

        if (isEdit) {
            currentStatusId = mEdit.current_user_status.id;
            tv_select_status.setText(mEdit.current_user_status.desc);
            tv_select_status.setTextColor(getColorResouce(R.color.new_text));
            edt_remark.setText(mEdit.refund_remark_admin);
            List<ImageEntity> list = getImageEntityList();
            if (list != null && list.size() != 0) {
                mImageList.addAll(list);
            }
        }

        if (mEdit.user_status != null && mEdit.user_status.size() != 0) {
            dialog = new ReturnGoodsDialog(this);
            dialog.setGoodsStatus(mEdit.user_status, currentStatusId);
            dialog.setTitle(getStringResouce(R.string.goods_status));
            dialog.setSelectListener(this);
        }

        singleImgAdapter = new SingleImgAdapter(this, mImageList);
        grid_imgs.setAdapter(singleImgAdapter);

        presenter = new PlatformInterventionPresenter(this, this);
    }

    @Override
    protected void initListener() {
        tv_select_status.setOnClickListener(this);
        tv_request_complete.setOnClickListener(this);
        TransformUtil.expandViewTouchDelegate(tv_select_status, TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f), TransformUtil.dip2px(this, 9f));
        super.initListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select_status:
                if (dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                break;
            case R.id.tv_request_complete:
                if (TextUtils.isEmpty(currentStatusId)) {
                    Common.staticToast("请选择到货状态");
                    return;
                }
                String remark = edt_remark.getText().toString();
                presenter.platformRequest(refundId, currentStatusId, remark, getImageString(), isEdit);
                break;
        }
        super.onClick(view);
    }

    private List<ImageEntity> getImageEntityList() {
        if (mEdit.member_evidence_admin == null || mEdit.member_evidence_admin.size() == 0) {
            return null;
        }
        List<ImageEntity> list = new ArrayList<>();
        for (String str : mEdit.member_evidence_admin) {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.imgUrl = str;
            list.add(imageEntity);
        }
        return list;
    }

    public String getImageString() {
        StringBuffer result = new StringBuffer();
        if (mImageList == null || mImageList.size() == 0) {
            return null;
        }
        for (int i = 0; i < mImageList.size(); i++) {
            result.append(mImageList.get(i).imgUrl);
            if (i != mImageList.size() - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }


    @Override
    public void requestSuccess(String msg) {
        //申请成功
        Common.staticToast(msg);
    }

    @Override
    public void requestFail(String errorMsg) {
        Common.staticToast(errorMsg);
    }

    @Override
    public void uploadImg(UploadPicEntity entity) {
        Message message = mHandler.obtainMessage();
        message.obj = entity.relativePath;
        message.what = 1;
        mHandler.sendMessage(message);
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            imageEntityList.clear();
            index = 0;
            compressImgs(index, imagePaths);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void compressImgs(int i, final List<String> list) {
        Luban.with(this).load(list.get(i)).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                ImageEntity imageEntity = new ImageEntity(list.get(index));
                imageEntity.file = file;
                imageEntityList.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    presenter.uploadPic(imageEntityList, "refund");
                } else {
                    compressImgs(index, list);
                }
            }

            @Override
            public void onError(Throwable e) {
                Common.staticToast("上传图片失败");
            }
        }).launch();
    }

    @Override
    public void onSelect(int position) {
        RefundDetailEntity.RefundDetail.Edit.Reason reason = mEdit.user_status.get(position);
        currentStatusId = reason.reason_id;
        tv_select_status.setText(reason.reason_info);
        tv_select_status.setTextColor(getColorResouce(R.color.new_text));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = msg.arg1;
                    String tag = String.valueOf(msg.obj);
//            creatCommentAdapter.updateProgress(currentPosition, tag, progress);
                    break;
                case 1:
                    List<String> imgs = (List<String>) msg.obj;
                    for (int i = 0; i < imageEntityList.size(); i++) {
                        imageEntityList.get(i).imgUrl = imgs.get(i);
                    }
                    mImageList.addAll(imageEntityList);
                    singleImgAdapter.setData(mImageList);
                    break;
            }
        }
    };
}
