package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.GridView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SelectGoodsAdapter;
import com.shunlian.app.adapter.SingleImgAdapter;
import com.shunlian.app.adapter.SingleImgAdapterV2;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by zhanghe on 2018/10/12.
 */

public class FindSendPictureTextAct extends BaseActivity {


    @BindView(R.id.mtv_toolbar_title)
    MyTextView mtvToolbarTitle;

    @BindView(R.id.mrlayout_toolbar_more)
    MyRelativeLayout mrlayoutToolbarMore;

    @BindView(R.id.mtv_toolbar_right)
    MyTextView mtvToolbarRight;

    @BindView(R.id.mtv_address)
    MyTextView mtv_address;

    @BindView(R.id.mtv_topic)
    MyTextView mtv_topic;

    @BindView(R.id.recy_view)
    RecyclerView recy_view;

    @BindView(R.id.gv_proof)
    GridView gv_proof;

    private List<GoodsDeatilEntity.Goods> goodsLists;
    private SelectGoodsAdapter adapter;
    private SingleImgAdapterV2 singleImgAdapter;
    private List<ImageEntity> imgList = new ArrayList();
    private int index;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, FindSendPictureTextAct.class);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_picture_text;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();
        mtvToolbarTitle.setText("发布图文");
        gone(mrlayoutToolbarMore);
        visible(mtvToolbarRight);
        mtvToolbarRight.setText("发布");
        mtvToolbarRight.setTextColor(getColorResouce(R.color.pink_color));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        int i = TransformUtil.dip2px(this, 0.5f);
        recy_view.addItemDecoration(new MVerticalItemDecoration(this,i,
                0,0, Color.parseColor("#ECECEC")));

        SingleImgAdapterV2.BuildConfig config = new SingleImgAdapterV2.BuildConfig();
        config.max_count = 9;
        config.pictureAndVideo = false;
        singleImgAdapter = new SingleImgAdapterV2(this, imgList,config);
        gv_proof.setAdapter(singleImgAdapter);
    }

    /**
     * 添加位置
     */
    @OnClick(R.id.rlayout_address)
    public void appendAddress(){
        NearAddressAct.startAct(this,100);
    }

    /**
     * 添加话题
     */
    @OnClick(R.id.rlayout_topic)
    public void addTopic(){
        AddTopicAct.startAct(this,200);
    }

    /**
     * 选择商品
     */
    @OnClick(R.id.rlayout_select_goods)
    public void selectGoods(){
        SelectGoodsAct.startAct(this,400);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==Activity.RESULT_OK){
            String name = data.getStringExtra("name");
            //String addr = data.getStringExtra("addr");
            mtv_address.setText(name);
        }else if (requestCode==200&&resultCode==Activity.RESULT_OK){
            String title = data.getStringExtra("title");
            mtv_topic.setText(title);
        }else if (requestCode==400&&resultCode==Activity.RESULT_OK){
            if (goodsLists == null){
                goodsLists = new ArrayList<>();
            }
            GoodsDeatilEntity.Goods goods = data.getParcelableExtra("goods");
            goodsLists.add(goods);
            if (adapter == null) {
                adapter = new SelectGoodsAdapter(this,false, goodsLists);
                recy_view.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }else if (requestCode == SingleImgAdapter.REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK){
            ArrayList<String> picturePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            index = 0;
//            imgList.clear();
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
                    //presenter.uploadPic(imgList,"customer_service");//上传图片
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
}
