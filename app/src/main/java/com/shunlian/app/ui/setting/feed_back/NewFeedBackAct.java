package com.shunlian.app.ui.setting.feed_back;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.NewFeedBackPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.view.INewFeedBackView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2016/6/8 0008.
 */
public class NewFeedBackAct extends BaseActivity implements View.OnClickListener ,INewFeedBackView{

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

    private static final int TEXT_TOTAL = 300;//文字总数
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private String goodsid, type;
    private int index;
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private SingleImgAdapter singleImgAdapter;
    private NewFeedBackPresenter presenter;
    private StringBuilder picstr = new StringBuilder();

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_new_feedback;
    }

    @Override
    protected void initListener() {
        super.initListener();
        et_moto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    tv_length.setVisibility(View.VISIBLE);
                } else {
                    tv_length.setVisibility(View.GONE);
                }

                if (s.length() > TEXT_TOTAL) {
                    Common.staticToasts(NewFeedBackAct.this,
                            "最多输入" + TEXT_TOTAL + "个字", R.mipmap.icon_common_tanhao);
                    et_moto.setText(s.subSequence(0, TEXT_TOTAL));
                } else {
                    tv_length.setText(s.length() + "/" + TEXT_TOTAL);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > TEXT_TOTAL) {
                    et_moto.setSelection(TEXT_TOTAL);
                }
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
        gone(mrlayout_toolbar_more);
        mtv_toolbar_title.setText("我要反馈");
        presenter = new NewFeedBackPresenter(this,this);

        singleImgAdapter = new SingleImgAdapter(this, listExplains);
        gv_proof.setAdapter(singleImgAdapter);

        if (!TextUtils.isEmpty(getIntent().getStringExtra("goodsid"))) {
            goodsid = getIntent().getStringExtra("goodsid");
            et_moto.setHint("请您填写商品或商家的意见！");
        }
        type = getIntent().getStringExtra("type");

    }


    @OnClick(R.id.tv_submit)
    public void submit() {
        String moto = et_moto.getText().toString();
        if (isEmpty(moto)){
            Common.staticToasts(this,"请先输入反馈内容",R.mipmap.icon_common_tanhao);
            return;
        }

        if (moto.length()<5){
            Common.staticToasts(this,"意见至少5个字",R.mipmap.icon_common_tanhao);
            return;
        }

        String phone = et_phone.getText().toString();
        if (isEmpty(phone)){
            Common.staticToasts(this,"请先输入手机号",R.mipmap.icon_common_tanhao);
            return;
        }
        if (presenter != null) {
            Map<String, String> map = new HashMap<>();
            map.put("mobile", phone);
            map.put("content", moto);
            map.put("machine_info", "android");
            map.put("goods_id", goodsid);
            map.put("type", type);
            map.put("image", picstr.toString());
            map.put("app_version", SharedPrefUtil.getSharedUserString("localVersion", ""));
            presenter.feedback(map);
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
                LogUtil.httpLogW("onSuccess:" + file.length());
                ImageEntity imageEntity = new ImageEntity(list.get(index));
                imageEntity.file = file;
                imgList.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    presenter.uploadPic(imgList,"customer_service");//上传图片
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
    public void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
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
     * 设置凭证图片
     *
     * @param pics
     * @param isShow
     */
    @Override
    public void setRefundPics(List<String> pics, boolean isShow) {
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
        for (int i = 0; i < picEntity.relativePath.size(); i++) {
            imgList.get(i).imgUrl = picEntity.relativePath.get(i);
        }
        listExplains.addAll(imgList);
        singleImgAdapter.setData(listExplains);
    }

    @Override
    public void uploadProgress(int progress, String tag) {

    }

    @Override
    public void submitSuccess(String msg) {
        Common.staticToasts(this,msg,R.mipmap.icon_common_duihao);
        finish();
    }
}