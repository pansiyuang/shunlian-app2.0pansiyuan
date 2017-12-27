package com.shunlian.app.ui.returns_order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.RefundInfoEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.widget.CustomerGoodsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/27.
 */

public class ReturnRequestActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.customer_goods)
    CustomerGoodsView customer_goods;

    @BindView(R.id.rl_return_reason)
    RelativeLayout rl_return_reason;

    @BindView(R.id.edt_return_money)
    EditText edt_return_money;

    @BindView(R.id.edt_refunds)
    EditText edt_refunds;

    @BindView(R.id.recycler_imgs)
    GridView recycler_imgs;

    private static final int REQUEST_CAMERA_CODE = 100;

    private RefundInfoEntity currentInfoEntity;
    private List<ImageEntity> imageEntityList;
    private PhotoPickerIntent intent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_request;
    }

    public static void startAct(Context context, RefundInfoEntity infoEntity) {
        Intent intent = new Intent(context, ReturnRequestActivity.class);
        intent.putExtra("infoEntity", infoEntity);
        context.startActivity(intent);
    }


    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title.setText(getStringResouce(R.string.return_request));
        currentInfoEntity = (RefundInfoEntity) getIntent().getSerializableExtra("infoEntity");
        customer_goods.setLabelName(getStringResouce(R.string.return_goods), false);
        customer_goods.setGoodsTitle(currentInfoEntity.title);
        customer_goods.setGoodsCount("x" + currentInfoEntity.qty);
        customer_goods.setGoodsParams(currentInfoEntity.sku_desc);
        customer_goods.setGoodsPrice(getStringResouce(R.string.common_yuan) + currentInfoEntity.price);
        customer_goods.selectCount(Integer.valueOf(currentInfoEntity.qty));

        intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照

        imageEntityList = new ArrayList<>();
        SingleImgAdapter singleImgAdapter = new SingleImgAdapter(this, imageEntityList);
    }

    public void openAlbum() {
        int max = 5;
        if (imageEntityList != null) {
            max = 5 - imageEntityList.size();
        }

        intent.setMaxTotal(max); // 最多选择照片数量，默认为9
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);


            for (String s : imagePaths) {
                ImageEntity imageEntity = new ImageEntity(s);
                imageEntityList.add(imageEntity);
            }
//            commentPresenter.uploadPic(paths, "comment");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
