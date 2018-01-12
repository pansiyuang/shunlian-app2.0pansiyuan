package com.shunlian.app.ui.returns_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.SubmitLogisticsInfoPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.zxing_code.ZXingDemoAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.ISubmitLogisticsInfoView;
import com.shunlian.app.widget.MyEditText;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2017/12/28.
 */

public class SubmitLogisticsInfoAct extends BaseActivity implements ISubmitLogisticsInfoView {

    @BindView(R.id.met_logistics)
    MyEditText met_logistics;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.gv_proof)
    GridView gv_proof;

    @BindView(R.id.met_explain)
    MyEditText met_explain;

    @BindView(R.id.mtv_logistics)
    MyTextView mtv_logistics;

    private List<ImageEntity> listExplains = new ArrayList();
    private SingleImgAdapter singleImgAdapter;

    public final int LOGISTICS_CODE = 100;//物流单号
    public final int LOGISTICS_NAME = 200;//物流名字

    public static final String APPLY = "0";//申请物流信息
    public static final String MODIFY = "1";//修改物流信息
    private String refund_id;
    private SubmitLogisticsInfoPresenter presenter;
    private int index;
    private String status;
    private StringBuilder picstr = new StringBuilder();

    public static void startAct(Context context,String refund_id,String status) {
        Intent intent = new Intent(context, SubmitLogisticsInfoAct.class);
        intent.putExtra("refund_id",refund_id);
        intent.putExtra("status",status);
        context.startActivity(intent);
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_submit_logisticsinfo;
    }

    @Override
    protected void initListener() {
        super.initListener();
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.hideKeyboard(met_logistics);
                finish();
            }
        });


        met_explain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                met_explain.setSelection(s.length());

                if (!s.toString().startsWith(Common.getPlaceholder(3))){
                    String concat = Common.getPlaceholder(1).concat(s.toString());
                    met_explain.setText(concat);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        refund_id = getIntent().getStringExtra("refund_id");
        status = getIntent().getStringExtra("status");
        presenter = new SubmitLogisticsInfoPresenter(this,this,refund_id);
        if (MODIFY.equals(status)){
            presenter.getLogisticsShipInfo();
        }

        met_explain.setText(Common.getPlaceholder(3));

        singleImgAdapter = new SingleImgAdapter(this, listExplains);
        gv_proof.setAdapter(singleImgAdapter);
    }

    @OnClick(R.id.miv_code)
    public void scanCode() {
        ZXingDemoAct.startAct(this,true, LOGISTICS_CODE);
    }

    @OnClick(R.id.met_logistics)
    public void editNumber(){
        setEdittextFocusable(true,met_logistics);
        Common.showKeyboard(met_logistics);
    }

    @OnClick(R.id.met_explain)
    public void explainText(){
        setEdittextFocusable(true,met_explain);
        Common.showKeyboard(met_explain);
    }

    @OnClick(R.id.mllayout_logistics)
    public void selectLogistics(){
        SelectLogisticsAct.startAct(this,LOGISTICS_NAME);
    }

    @OnClick(R.id.mtv_submit)
    public void submit(){
        CharSequence text = mtv_logistics.getText();
        if (getStringResouce(R.string.please_select).equals(text)){
            Common.staticToast("请选择物流公司");
            return;
        }
        String logistics_code = met_logistics.getText().toString();
        if (isEmpty(logistics_code)){
            Common.staticToast("请填写物流单号");
            return;
        }
        String explain = met_explain.getText().toString();
        if (isEmpty(explain)){
            Common.staticToast("请填写申请说明");
            return;
        }

        presenter.submitLogisticsInfo(text.toString(),logistics_code,explain,picstr.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGISTICS_CODE && resultCode == ZXingDemoAct.RESULT_CODE) {
            String result = data.getStringExtra("result");
            met_logistics.setText(result);
        }else if (requestCode == SingleImgAdapter.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK){
            ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            index = 0;
            compressImgs(0, picturePaths);
        }else if (requestCode == LOGISTICS_NAME && resultCode == Activity.RESULT_OK){
            String name = data.getStringExtra("name");
            mtv_logistics.setText(name);
        }
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
                listExplains.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    singleImgAdapter.notifyDataSetChanged();
                    presenter.uploadPic(listExplains,"customer_service");//上传图片
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
    protected void onDestroy() {
        Common.hideKeyboard(met_logistics);
        super.onDestroy();
    }

    /**
     * 显示网络请求失败的界面
     *
     * @param request_code
     */
    @Override
    public void showFailureView(int request_code) {

    }

    /**
     * 显示空数据界面
     *
     * @param request_code
     */
    @Override
    public void showDataEmptyView(int request_code) {

    }

    /**
     * 设置物流公司
     *
     * @param name
     */
    @Override
    public void setLogisticsName(String name) {
        if (!isEmpty(name))
            mtv_logistics.setText(name);
    }

    /**
     * 设置物流单号
     *
     * @param code
     */
    @Override
    public void setLogisticsCode(String code) {
        if (!isEmpty(code))
            met_logistics.setText(code);
    }

    /**
     * 设置说明
     *
     * @param memo
     */
    @Override
    public void setRefundMemo(String memo) {
        if (!isEmpty(memo))
            met_explain.setText(memo);
    }

    /**
     * 设置凭证图片
     *
     * @param pics
     */
    @Override
    public void setRefundPics(List<String> pics,boolean isShow) {
        if (isEmpty(pics)){
            return;
        }
        if (isShow){
            for (String picturePath : pics) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imgUrl = picturePath;
                listExplains.add(imageEntity);
            }
            singleImgAdapter.notifyDataSetChanged();
        }
        picstr.delete(0, picstr.length());
        for (int i = 0; i < pics.size(); i++) {
            String path = pics.get(i);
            picstr.append(path);
            picstr.append(",");
        }
    }

    @Override
    public void uploadImg(UploadPicEntity picEntity) {

    }

    @Override
    public void uploadProgress(int progress, String tag) {
        // TODO: 2018/1/5 上传进度 
    }

    /**
     * 提交成功
     */
    @Override
    public void submitSuccess() {
        Common.staticToast(getStringResouce(R.string.submit_success));
        finish();
    }
}
