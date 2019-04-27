package com.shunlian.app.demo;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.newchat.entity.ImgEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.INewFeedBackView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2019/4/2.
 */

public class DemoNewFeedBackAct extends BaseActivity implements INewFeedBackView {
    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.et_moto)
    EditText et_moto;

    @BindView(R.id.tv_length)
    MyTextView tv_length;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.gv_proof)
    GridView gv_proof;

    @BindView(R.id.tv_submit)
    MyTextView tv_submit;
    private static final int TEXT_TOTAL = 300;
    private int index;
    private DemoNewFeedBackPresenter demoNewFeedBackPresenter;
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private DemoSingleImgAdapter demoSingleImgAdapter;
    private StringBuilder picstr = new StringBuilder();

    @Override
    protected int getLayoutId() {
        return R.layout.act_new_feedback;
    }
    protected void initListener(){
        super.initListener();
        et_moto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    tv_length.setVisibility(View.VISIBLE);
                }else {
                    tv_length.setVisibility(View.GONE);
                }
                if(charSequence.length()>TEXT_TOTAL){
                    Common.staticToasts(DemoNewFeedBackAct.this,
                            "最多输入" + TEXT_TOTAL + "个字", R.mipmap.icon_common_tanhao);
                    et_moto.setText(charSequence.subSequence(0,TEXT_TOTAL));
                }else {
                    tv_length.setText(charSequence.length()+"/"+TEXT_TOTAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>TEXT_TOTAL){
                    et_moto.setSelection(TEXT_TOTAL);
                }
            }
        });

    }
    @OnClick(R.id.tv_submit)
    public void submit(){
        String s = et_moto.getText().toString();
        if(isEmpty(s)){
            Common.staticToasts(this,"请先输入反馈内容",R.mipmap.icon_common_tanhao);
            return;
        }
        if(s.length()<5){
            Common.staticToasts(this,"反馈至少5个字",R.mipmap.icon_common_tanhao);
        }
        String phone = et_phone.getText().toString();
        if(isEmpty(phone)){
            Common.staticToasts(this,"请先输入手机号",R.mipmap.icon_common_tanhao);
        }
        if(demoNewFeedBackPresenter!=null){
            HashMap<String, String> map = new HashMap<>();
            map.put("mobile", phone);
            map.put("content", s);
            map.put("machine_info", "android");
            map.put("goods_id", 0+"");
            map.put("type", "");
            map.put("image", picstr.toString());
            map.put("app_version", SharedPrefUtil.getSharedUserString("localVersion", ""));
            demoNewFeedBackPresenter.feedback(map);
        }

    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        gone(mrlayout_toolbar_more);
        mtv_toolbar_title.setText("我要反馈");
        demoNewFeedBackPresenter = new DemoNewFeedBackPresenter(this, this);
        demoSingleImgAdapter = new DemoSingleImgAdapter(this, listExplains);
        gv_proof.setAdapter(demoSingleImgAdapter);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == SingleImgAdapter.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                index=0;
                imgList.clear();
            compressImgs(index,stringArrayListExtra);
        }

    }

    private void compressImgs(int i,List<String>list) {
        Luban.with(this).load(list.get(i)).putGear(3).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {


            }

            @Override
            public void onSuccess(File file) {
                ImageEntity imageEntity = new ImageEntity(list.get(i));
                imageEntity.file=file;
                imgList.add(imageEntity);
                index++;
                if(index>=list.size()){
                demoNewFeedBackPresenter.uploadpic(imgList,"customer_service");
                }else {
                    compressImgs(index,list);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        }).launch();

    }

    @Override
    public void setRefundPics(List<String> pics, boolean isShow) {
        if(isEmpty(pics)){
            return;
        }
        if(isShow){
            for(String picturePath :pics){
                ImageEntity imgEntity = new ImageEntity();
                imgEntity.imgUrl=picturePath;
                listExplains.add(imgEntity);
            }
            demoSingleImgAdapter.notifyDataSetChanged();
        }
        picstr.delete(0,picstr.length());
        for(int i =0;i<pics.size();i++){
            String s = pics.get(i);
            picstr.append(s);
            picstr.append(",");
        }

    }

    @Override
    public void uploadImg(UploadPicEntity picEntity) {
        for(int i =0 ;i<picEntity.relativePath.size();i++){
            imgList.get(i).imgUrl=picEntity.relativePath.get(i);
        }
        listExplains.addAll(imgList);
        demoSingleImgAdapter.setData(listExplains);

    }

    @Override
    public void uploadProgress(int progress, String tag) {

    }

    @Override
    public void submitSuccess(String msg) {
        Common.staticToasts(this,msg,R.mipmap.icon_common_duihao);
        finish();
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }
}
