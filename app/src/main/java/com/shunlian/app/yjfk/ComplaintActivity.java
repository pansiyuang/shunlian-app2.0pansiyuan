package com.shunlian.app.yjfk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.BaseEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.widget.MyRecyclerView;
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

public class ComplaintActivity extends BaseActivity implements IOpinionbackView {
    @BindView(R.id.mrv_type)
    MyRecyclerView mrv_type;
    @BindView(R.id.gv_proof)
    GridView gv_proof;
    @BindView(R.id.et_1)
    EditText et_1;
    @BindView(R.id.tv_length)
    MyTextView tv_length;
    @BindView(R.id.tv_submit)
    MyTextView tv_submit;
    @BindView(R.id.mtv_jl)
    MyTextView mtv_jl;
    private ArrayList<ComplaintTypesEntity> lists;
    private static final int TEXT_TOTAL = 300;
    private int index;
    private StringBuilder picstr = new StringBuilder();
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private OpinionAdapter opinionAdapter;
    private OpinionPresenter opinionPresenter;
    private boolean et1length;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ComplaintActivity.class);
        context.startActivity(intent);
    }
    private TypeAdapter typeAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.act_complaint;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        opinionPresenter = new OpinionPresenter(this, this);
        opinionPresenter.getComplaintTypesEntity();
        opinionAdapter = new OpinionAdapter(this, listExplains);
        gv_proof.setAdapter(opinionAdapter);
    }

    @Override
    public void getcomplaintTypes(ComplaintTypesEntity entity) {

            typeAdapter = new TypeAdapter(this, false, entity.list);
//            typeAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
//                @Override
//                public void onItemClick(View view, int position) {
//                    typeAdapter.selectPosition = position;
//                    typeAdapter.notifyDataSetChanged();
//
//                }
//            });
//            this.id=typeAdapter.id;
            mrv_type.setLayoutManager(new LinearLayoutManager(ComplaintActivity.this,LinearLayoutManager.VERTICAL,false));
            mrv_type.setAdapter(typeAdapter);
            typeAdapter.notifyDataSetChanged();


    }

    @Override
    public void getcomplaintList(List<ComplanintListEntity.Lists> entity, int allPage, int page) {

    }


    @Override
    protected void initListener() {
        super.initListener();

        //输入提示
        et_1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5) {
                    et1length =true;
                }else {
                    et1length =false;
                }
                if(et1length&&typeAdapter.isClick){
                    Drawable drawable = getResources().getDrawable(R.drawable.bg_common_round);
                    tv_submit.setBackground(drawable);
                }else {
                    Drawable drawable = getResources().getDrawable(R.drawable.beijin_4);
                    tv_submit.setBackground(drawable);
                }
                if (s.length() > TEXT_TOTAL) {
                    Common.staticToasts(ComplaintActivity.this,
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
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String moto = et_1.getText().toString();
                if (isEmpty(moto)){
                    Common.staticToasts(ComplaintActivity.this,"请先输入反馈内容",R.mipmap.icon_common_tanhao);
                    return;
                }

                if (moto.length()<5){
                    Common.staticToasts(ComplaintActivity.this,"意见至少5个字",R.mipmap.icon_common_tanhao);
                    return;
                }
                if(typeAdapter.id==""){
                    Common.staticToasts(ComplaintActivity.this,"请选择投诉类型",R.mipmap.icon_common_tanhao);
                    return;
                }
                if (opinionPresenter != null) {
                    Map<String, String> map = new HashMap<>();
                    map.put("content", moto);
                    map.put("machine_info", "android");
                    map.put("type", typeAdapter.id);
                    map.put("image", picstr.toString());
                    map.put("app_version", SharedPrefUtil.getSharedUserString("localVersion", ""));
                    opinionPresenter.submitComplaint(map);
                }
            }
        });
        mtv_jl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ComplaninRecordActivity.startAct(ComplaintActivity.this);

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

    }

    @Override
    public void submitSuccess1(BaseEntity<Opinionfeedback1Entity> data) {
        if(data.code==1000){
            finish();
            OpinionSuccess.startAct(this,data.message,data.data.message);
        }

    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
