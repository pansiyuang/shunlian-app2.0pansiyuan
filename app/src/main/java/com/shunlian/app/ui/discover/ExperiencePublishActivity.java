package com.shunlian.app.ui.discover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.presenter.ExperiencePublishPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.myself_store.AddStoreGoodsAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.view.IExperiencePublishView;
import com.shunlian.app.widget.MyImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ExperiencePublishActivity extends BaseActivity implements IExperiencePublishView, View.OnClickListener {

    public static final String FROM_EXPERIENCE_PUBLISH = "experience_publish";
    public static final int PUBLISH_REQUEST = 10003;

    @BindView(R.id.edt_content)
    EditText edt_content;

    @BindView(R.id.tv_content_count)
    TextView tv_content_count;

    @BindView(R.id.grid_imgs)
    GridView grid_imgs;

    @BindView(R.id.rl_add_goods)
    RelativeLayout rl_add_goods;

    @BindView(R.id.ll_goods)
    LinearLayout ll_goods;

    @BindView(R.id.miv_icon)
    MyImageView miv_icon;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.tv_price)
    TextView tv_price;

    @BindView(R.id.miv_del)
    MyImageView miv_del;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.tv_goods_title)
    TextView tv_goods_title;

    private SingleImgAdapter singleImgAdapter;
    private List<ImageEntity> listExplains = new ArrayList();
    private List<ImageEntity> imgList = new ArrayList();
    private ExperiencePublishPresenter mPresenter;
    private StringBuilder picstr = new StringBuilder();
    private GoodsDeatilEntity.Goods currentGoods;
    private int index;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, ExperiencePublishActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_experience_publish;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getString(R.string.publish_experience));
        tv_title_right.setText(getString(R.string.comment_release));
        tv_title_right.setTextColor(getColorResouce(R.color.pink_color));
        tv_title_right.setVisibility(View.VISIBLE);
        mPresenter = new ExperiencePublishPresenter(this, this);
        singleImgAdapter = new SingleImgAdapter(this, listExplains);
        singleImgAdapter.setMaxSize(9);
        grid_imgs.setAdapter(singleImgAdapter);
    }

    @Override
    protected void initListener() {
        rl_add_goods.setOnClickListener(this);
        miv_del.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        super.initListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SingleImgAdapter.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            index = 0;
            imgList.clear();
            compressImgs(index, picturePaths);
        } else if (requestCode == ExperiencePublishActivity.PUBLISH_REQUEST && resultCode == Activity.RESULT_OK) {
            currentGoods = data.getParcelableExtra("goods");
            showGoodsLayout(true);
        }
    }

    public void showGoodsLayout(boolean isShow) {
        if (isShow) {
            rl_add_goods.setVisibility(View.GONE);
            ll_goods.setVisibility(View.VISIBLE);

            if (currentGoods != null) {
                GlideUtils.getInstance().loadImage(this, miv_icon, currentGoods.thumb);
                tv_goods_title.setText(currentGoods.title);
                tv_price.setText(currentGoods.price);
            }
        } else {
            rl_add_goods.setVisibility(View.VISIBLE);
            ll_goods.setVisibility(View.GONE);
            currentGoods = null;
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
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

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
    public void creatExperienctSuccess() {
        Common.staticToast("发布成功");
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_add_goods:
                Intent intent = new Intent(this, AddStoreGoodsAct.class);
                intent.putExtra("currentFrom", FROM_EXPERIENCE_PUBLISH);
                startActivityForResult(intent, PUBLISH_REQUEST);
                break;
            case R.id.miv_del:
                showGoodsLayout(false);
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
                if (isEmpty(currentGoods.goods_id)) {
                    Common.staticToast("请添加商品");
                    return;
                }
                mPresenter.createExperience(content, picstr.toString(), currentGoods.goods_id);
                break;
        }
        super.onClick(view);
    }
}
