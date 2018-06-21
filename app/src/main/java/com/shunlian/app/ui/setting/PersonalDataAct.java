package com.shunlian.app.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shunlian.app.R;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.photopick.ImageCaptureManager;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.presenter.PersonalDataPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SharedPrefUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IPersonalDataView;
import com.shunlian.app.widget.AvatarDialog;
import com.shunlian.app.widget.BoldTextSpan;
import com.shunlian.app.widget.BottonDialog;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.SelectDateDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shunlian.app.ui.zxing_code.ZXingDemoAct.REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY;

/**
 * Created by Administrator on 2018/4/23.
 */

public class PersonalDataAct extends BaseActivity implements IPersonalDataView{

    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtv_toolbar_title;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayout_toolbar_more;

    @BindView(R.id.llayout_nickname)
    LinearLayout llayout_nickname;

    @BindView(R.id.llayout_sex)
    LinearLayout llayout_sex;

    @BindView(R.id.mtv_sex)
    MyTextView mtv_sex;

    @BindView(R.id.llayout_autograph)
    LinearLayout llayout_autograph;

    @BindView(R.id.llayout_region)
    LinearLayout llayout_region;

    @BindView(R.id.mtv_region)
    MyTextView mtv_region;

    @BindView(R.id.llayout_avatar)
    LinearLayout llayout_avatar;

    @BindView(R.id.miv_avatar)
    MyImageView miv_avatar;

    @BindView(R.id.mtv_nickname)
    MyTextView mtv_nickname;

    @BindView(R.id.llayout_interest)
    LinearLayout llayout_interest;

    @BindView(R.id.mtv_interest)
    MyTextView mtv_interest;

    @BindView(R.id.llayout_birthday)
    LinearLayout llayout_birthday;

    @BindView(R.id.mtv_birthday)
    MyTextView mtv_birthday;

    @BindView(R.id.mtv_autograph)
    MyTextView mtv_autograph;

    private BottonDialog dialog;
    private PersonalDataPresenter presenter;
    private SelectDateDialog dateDialog;
    private ImageCaptureManager captureManager;
    private AvatarDialog avatarDialog;
    private String mInterestTag;

    public static void startAct(Context context){
        context.startActivity(new Intent(context,PersonalDataAct.class));
    }

    /**
     * 布局id
     *
     * @return
     */
    @Override
    protected int getLayoutId() {
        return R.layout.act_personal_data;
    }

    @Override
    protected void initListener() {
        super.initListener();
        llayout_nickname.setOnClickListener(this);
        llayout_sex.setOnClickListener(this);
        llayout_autograph.setOnClickListener(this);
        llayout_region.setOnClickListener(this);
        llayout_interest.setOnClickListener(this);
        llayout_birthday.setOnClickListener(this);
        llayout_avatar.setOnClickListener(this);
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtv_toolbar_title.setText("个人资料");
        gone(mrlayout_toolbar_more);

        presenter = new PersonalDataPresenter(this,this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.llayout_nickname:
                NickNameAct.startAct(this,mtv_nickname.getText().toString());
                break;
            case R.id.llayout_sex:
                if (dialog == null) {
                    dialog = new BottonDialog(this);
                    dialog.setOnClickListener((sex,id)-> {
                        mtv_sex.setText(sex);
                        if (presenter != null){
                            presenter.setInfo("sex",id);
                        }
                    });
                }
                dialog.show();
                break;
            case R.id.llayout_autograph:
                AutographAct.startAct(this,mtv_autograph.getText().toString());
                break;
            case R.id.llayout_region:
                if (presenter != null){
                    presenter.initDistrict();
                }
                break;
            case R.id.llayout_interest:
                if (isEmpty(mInterestTag)){
                    mInterestTag = "请选择";
                }
                SelectLikeAct.startAct(this,mInterestTag);
                break;
            case R.id.llayout_avatar:
                if (avatarDialog == null) {
                    avatarDialog = new AvatarDialog(this);
                    avatarDialog.setOnClickListener((id)->{
                        if (id == 1){//拍照
                            showCameraAction();
                        }else {
                            showAlbumAction();
                        }
                    });
                }
                avatarDialog.show();
                break;
            case R.id.llayout_birthday:
                if (!"设置后不可更改".equals(mtv_birthday.getText())){
                    return;//只能选择一次生日
                }
                if (dateDialog == null) {

                    dateDialog = new SelectDateDialog(this);
                    dateDialog.setOnClickListener((date)->showDialog(date));
                }
                dateDialog.show();
                break;
        }
    }

    private void showDialog(String date) {
        if (isEmpty(date)){
            return;
        }
        String src = "您的生日是%s，设置后将无法修改(生日仅自己可见)，确定要设置吗？";
        src = String.format(src,date);
        SpannableStringBuilder ssb = new SpannableStringBuilder(src);
        BoldTextSpan boldTextSpan = new BoldTextSpan();
        ssb.setSpan(boldTextSpan,5,5+date.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(12,true);
        ssb.setSpan(sizeSpan,src.indexOf("("),src.indexOf(")")+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                .setSureAndCancleListener(ssb,getString(R.string.SelectRecommendAct_sure), (v) -> {
                            if (presenter != null){
                                String s = TransformUtil.date2TimeStamp(date, "yyyy-MM-dd");
                                presenter.setInfo("birth",s);
                            }
                            mtv_birthday.setText(date);
                            mtv_birthday.setCompoundDrawables(null,null,null,null);
                            promptDialog.dismiss();
                        }, getString(R.string.errcode_cancel), (v) -> promptDialog.dismiss()
                ).show();
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        try {
            captureManager = new ImageCaptureManager(this);
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 选择相册
     */
    private void showAlbumAction() {
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(1); // 最多选择照片数量，默认为9
        startActivityForResult(intent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
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

    @Override
    public void setLocation(String district, String district_ids) {
        mtv_region.setText(district);
        //LogUtil.zhLogW(district+"===setLocation==="+district_ids);
        if (!isEmpty(district_ids)){
            if (presenter != null){
                presenter.setInfo("location",district_ids);
            }
        }
    }

    @Override
    public void setAvatar(String avatar) {
        if (!isEmpty(avatar)){
            GlideUtils.getInstance().loadCircleHeadImage(this,miv_avatar,avatar);
        }
    }

    @Override
    public void setSex(String sex) {
        if (!isEmpty(sex)){
            mtv_sex.setText(sex);
        }

    }

    @Override
    public void setNickname(String nickname) {
        if (!isEmpty(nickname)){
            mtv_nickname.setText(nickname);
        }

    }

    /**
     * 设置个性签名
     *
     * @param signature
     */
    @Override
    public void setSignature(String signature) {
        if (!isEmpty(signature)){
            mtv_autograph.setText(signature);
        }

    }

    @Override
    public void setBirth(String birth) {
        if (!isEmpty(birth)){
            mtv_birthday.setText(birth);
            mtv_birthday.setCompoundDrawables(null,null,null,null);
        }
    }

    /**
     * 设置标签
     *
     * @param tag
     */
    @Override
    public void setTag(String tag) {
        if (!isEmpty(tag)){
            mInterestTag = tag;
            mtv_interest.setText(formInterest(tag));
        }
    }

    /**
     * 设置凭证图片
     *
     * @param pic
     */
    @Override
    public void setRefundPics(String pic,String domain) {
        if (presenter != null){
            presenter.setInfo("avatar",pic);
            GlideUtils.getInstance().loadCircleImage(this,miv_avatar,domain+pic);
            SharedPrefUtil.saveSharedPrfString("avatar", domain+pic);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode//昵称
                && requestCode == NickNameAct.REQUEST_CODE){
            String nickname = data.getStringExtra("nickname");
            if (!isEmpty(nickname) && presenter != null){
                mtv_nickname.setText(nickname);
                presenter.setInfo("nickname",nickname);
            }
        }else if (RESULT_OK == resultCode//个性签名
                && requestCode == AutographAct.REQUEST_CODE){
            String signature = data.getStringExtra("signature");
            if (!isEmpty(signature) && presenter != null){
                mtv_autograph.setText(signature);
                presenter.setInfo("signature",signature);
            }
        }else if (RESULT_OK == resultCode//兴趣选择
                && requestCode == SelectLikeAct.REQUEST_CODE){
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");

            if (!isEmpty(id) && !isEmpty(name) && presenter != null){
                mtv_interest.setText(formInterest(name));
                presenter.setInfo("tag",id);
            }
        }else if (resultCode == RESULT_OK //相册
                && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = data.getStringArrayListExtra
                    (PhotoPickerActivity.EXTRA_RESULT).get(0);
            compressImgs(picturePath);
        }else if (resultCode == RESULT_OK //拍照
                && requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO) {
            if (captureManager.getCurrentPhotoPath() != null) {
                captureManager.galleryAddPic();
                compressImgs(captureManager.getCurrentPhotoPath());
            }
        }
    }

    public void compressImgs(String url) {
        Luban.with(this).load(url).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                LogUtil.httpLogW("onSuccess:" + file.length());
                List<ImageEntity> imgList = new ArrayList<>();
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.file = file;
                imgList.add(imageEntity);
                presenter.uploadPic(imgList,"customer_service");//上传图片
            }

            @Override
            public void onError(Throwable e) {
                Common.staticToast("上传图片失败");
            }
        }).launch();
    }

    private String formInterest(String src){
       if (!isEmpty(src) && src.length() > 15){
           src = src.substring(0,15)+"...";
       }
       return src;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null){
            dialog.destory();
        }
        if (dateDialog != null){
            dateDialog.destory();
        }
        if (avatarDialog != null){
            avatarDialog.destory();
        }
    }
}
