package com.shunlian.app.yjfk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2019/4/17.
 */

public class Opinion1Activity extends BaseActivity implements IOpinionbackView{
    @BindView(R.id.et_1)
    EditText et_1;
    @BindView(R.id.et_2)
    EditText et_2;
    @BindView(R.id.tv_length)
    MyTextView tv_length;
    @BindView(R.id.miv_close)
    MyImageView miv_close;
    @BindView(R.id.gv_proof)
    GridView gv_proof;
    @BindView(R.id.tv_submit)
    MyTextView tv_submit;


    private static final int TEXT_TOTAL = 300;
    private int index;
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private OpinionPresenter opinionPresenter;
    private OpinionAdapter opinionAdapter;
    private StringBuilder picstr = new StringBuilder();
    private boolean et1length=false;
    private boolean et2length=false;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, Opinion1Activity.class);
        context.startActivity(intent);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.act_opinion_tx;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        opinionPresenter = new OpinionPresenter(this, this);
        opinionAdapter = new OpinionAdapter(this, listExplains);
        gv_proof.setAdapter(opinionAdapter);

    }
    @Override
    protected void initListener() {
        super.initListener();

        //输入提示
        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    et1length=true;
                }else {
                    et1length=false;
                }
                if(et1length&&et2length){
                  Drawable drawable = getResources().getDrawable(R.drawable.bg_common_round);
                    tv_submit.setBackground(drawable);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.beijin_4);
                    tv_submit.setBackground(drawable);
                }

                if (s.length() > TEXT_TOTAL) {
                    Common.staticToasts(Opinion1Activity.this,
                            "最多输入" + TEXT_TOTAL + "个字", R.mipmap.icon_common_tanhao);
                    et_1.setText(s.subSequence(0, TEXT_TOTAL));
                } else {
                    tv_length.setText(s.length() + "/" + TEXT_TOTAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > TEXT_TOTAL) {
                    et_1.setSelection(TEXT_TOTAL);
                }
            }
        });
        et_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5) {
                    et2length=true;
                }else {
                    et2length=false;
                }
                if(et1length&&et2length){
                    Drawable drawable = getResources().getDrawable(R.drawable.bg_common_round);
//                    tv_submit.setBackground(R.drawable.bg_common_round);
                    tv_submit.setBackground(drawable);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.beijin_4);
                    tv_submit.setBackground(drawable);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        //关闭
        miv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Opinion1Activity.this,"wwwwwww",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //提交
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moto = et_1.getText().toString();
                if (isEmpty(moto)){
                    Common.staticToasts(Opinion1Activity.this,"请先输入反馈内容",R.mipmap.icon_common_tanhao);
                    return;
                }

                if (moto.length()<5){
                    Common.staticToasts(Opinion1Activity.this,"意见至少5个字",R.mipmap.icon_common_tanhao);
                    return;
                }

                String phone = et_1.getText().toString();
                if (isEmpty(phone)){
                    Common.staticToasts(Opinion1Activity.this,"请先输入手机号",R.mipmap.icon_common_tanhao);
                    return;
                }
                if (opinionPresenter != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("mobile", phone);
                    map.put("content", moto);
                    map.put("machine_info", "android");
                    map.put("type", "2");
                    map.put("ref_val", "2");
                    map.put("from_type", "2");
                    map.put("image", picstr.toString());
                    map.put("app_version", SharedPrefUtil.getSharedUserString("localVersion", ""));
                    opinionPresenter.submitComplaint(map);
                }
            }
        });

    }
    public void compressImgs(int i,   List<String> list) {
        Luban.with(this).load(list.get(i)).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                LogUtil.httpLogW("onSuccess:" + file.length());
                ImageEntity imageEntity = new ImageEntity(list.get(index));
                imageEntity.file = file;
                imgList.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    opinionPresenter.uploadPic(imgList,"customer_service");//上传图片
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SingleImgAdapter.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            index = 0;
            imgList.clear();
            compressImgs(index, picturePaths);
        }
    }

    @Override
    public void getOpinionfeedback(OpinionfeedbackEntity entity) {


    }

    @Override
    public void uploadImg(UploadPicEntity uploadPicEntity) {
        for (int i = 0; i < uploadPicEntity.relativePath.size(); i++) {
            imgList.get(i).imgUrl = uploadPicEntity.relativePath.get(i);
        }
        listExplains.addAll(imgList);
        opinionAdapter.setData(listExplains);

    }

    @Override
    public void setRefundPics(List<String> relativePath, boolean b) {
        if (isEmpty(relativePath)){
            return;
        }
        if (b){
            for (String picturePath : relativePath) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imgUrl = picturePath;
                listExplains.add(imageEntity);
            }
            opinionAdapter.notifyDataSetChanged();
        }
        picstr.delete(0, picstr.length());
        for (int i = 0; i < relativePath.size(); i++) {
            String path = relativePath.get(i);
            picstr.append(path);
            picstr.append(",");
        }

    }

    @Override
    public void submitSuccess(String message) {
        Common.staticToasts(this,message,R.mipmap.icon_common_duihao);
        finish();
    }

    @Override
    public void submitSuccess1(BaseEntity<Opinionfeedback1Entity> data) {
        if(data.code==1000){
            OpinionSuccess.startAct(this,data.message,data.data.message);
        }


    }

    @Override
    public void getcomplaintTypes(ComplaintTypesEntity entity) {

    }



    @Override
    public void getcomplaintList(List<ComplanintListEntity.Lists> entity, int allPage, int page) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
