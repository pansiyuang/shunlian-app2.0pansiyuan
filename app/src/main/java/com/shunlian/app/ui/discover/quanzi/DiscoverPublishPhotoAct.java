package com.shunlian.app.ui.discover.quanzi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CommonImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.ExperiencePublishPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IExperiencePublishView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.shunlian.app.adapter.CommonImgAdapter.REQUEST_CAMERA_CODE;

public class DiscoverPublishPhotoAct extends BaseActivity implements IExperiencePublishView, View.OnClickListener, TextWatcher {
    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.tv_content_count)
    TextView tv_content_count;

    @BindView(R.id.recycler_imgs)
    RecyclerView recycler_imgs;

    private CommonImgAdapter mImgAdapter;
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private ExperiencePublishPresenter mPresenter;
    private StringBuilder picstr = new StringBuilder();
    private int index;
    private String circle_id;

    public static void startAct(Context context, String circle_id) {
        Intent intent = new Intent(context, DiscoverPublishPhotoAct.class);
        intent.putExtra("circle_id", circle_id);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_discover_publish_photo;
    }

    @Override
    protected void initListener() {
        super.initListener();
        edt_content.addTextChangedListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        circle_id = getIntent().getStringExtra("circle_id");
        tv_title.setText(getStringResouce(R.string.discover_fabiaozhaopian));
        tv_title_right.setText(getString(R.string.comment_release));
        tv_title_right.setTextColor(getColorResouce(R.color.pink_color));
        tv_title_right.setVisibility(View.VISIBLE);
        mPresenter = new ExperiencePublishPresenter(this, this);

        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mImgAdapter = new CommonImgAdapter(this, listExplains, 9);
        recycler_imgs.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(this, 5), false));
        recycler_imgs.setLayoutManager(manager);
        ((DefaultItemAnimator) recycler_imgs.getItemAnimator()).setSupportsChangeAnimations(false);
        recycler_imgs.setAdapter(mImgAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            index = 0;
            imgList.clear();
            compressImgs(index, picturePaths);
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
                    mPresenter.uploadPic(imgList, "experience_publish");//上传图片
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
    public void showFailureView(int rquest_code) {

    }

    @Override
    public void showDataEmptyView(int rquest_code) {

    }

    @Override
    public void setPics(List<String> pics, boolean isShow) {
        if (isEmpty(pics)) {
            return;
        }
        if (isShow) {
            for (String picturePath : pics) {
                ImageEntity imageEntity = new ImageEntity();
                imageEntity.imgUrl = picturePath;
                listExplains.add(imageEntity);
            }
            mImgAdapter.notifyDataSetChanged();
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
        mImgAdapter.setData(listExplains);
    }

    @Override
    public void uploadProgress(int progress, String tag) {

    }

    @Override
    public void creatExperienctSuccess() {
        Common.staticToasts(this, "发布成功", R.mipmap.icon_common_duihao);
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.miv_close:
                backSelect();
                break;
            case R.id.tv_title_right:
                String content = edt_content.getText().toString();
                if (content.length() < 10) {
                    Common.staticToast("请至少输入10个字");
                    return;
                }
                if (isEmpty(picstr.toString())) {
                    Common.staticToast("请添加图片");
                    return;
                }
                mPresenter.createCircle(content, picstr.toString(), circle_id);
                break;
        }
        super.onClick(view);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = edt_content.getText().toString();
        tv_content_count.setText(str.length() + "/300");
    }

    private void backSelect() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                .setSureAndCancleListener(getStringResouce(R.string.ready_to_cancel_comment),
                        getStringResouce(R.string.continue_to_publish), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                promptDialog.dismiss();
                            }
                        }, getStringResouce(R.string.SelectRecommendAct_sure), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                promptDialog.dismiss();
                                finish();
                            }
                        }).show();
    }

    @Override
    protected void onStop() {
        Common.hideKeyboard(edt_content);
        super.onStop();
    }
}
