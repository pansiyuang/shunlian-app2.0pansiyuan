package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.SelectGoodsAdapter;
import com.shunlian.app.adapter.SingleImgAdapterV2;
import com.shunlian.app.bean.BlogDraftEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.ImageVideo;
import com.shunlian.app.presenter.FindSendPicPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectPicVideoView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import org.greenrobot.eventbus.EventBus;

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

public class FindSendPictureTextAct extends BaseActivity implements ISelectPicVideoView{

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

    @BindView(R.id.recy_pics)
    RecyclerView recy_pics;

    @BindView(R.id.edit)
    EditText edit;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    private List<GoodsDeatilEntity.Goods> goodsLists;
    private SelectGoodsAdapter adapter;
    private SingleImgAdapterV2 singleImgAdapter;
    private ArrayList<ImageVideo> imgList = new ArrayList();
    private int index;
    private FindSendPicPresenter presenter;
    /********打开选择图片activity的请求码**********/
    public static final int SELECT_PIC_REQUESTCODE = 800;
    private String topic_id;
    private String mVideoUrl;
    /***
     * 最大心得字数
     */
    private int MAX_TEXT_COUNT = 300;
    private String mVideoThumb;

    public static void startAct(Context context,boolean isWhiteList) {
        Intent intent = new Intent(context, FindSendPictureTextAct.class);
        intent.putExtra("isWhiteList",isWhiteList);
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

        boolean isWhiteList = getIntent().getBooleanExtra("isWhiteList", false);
        if (isWhiteList){
            MAX_TEXT_COUNT = 800;
        }

        mtvToolbarTitle.setText("发布图文");
        gone(mrlayoutToolbarMore);
        visible(mtvToolbarRight);
        mtvToolbarRight.setText("发布");
        mtvToolbarRight.setTextColor(getColorResouce(R.color.pink_color));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recy_view.setLayoutManager(manager);
        recy_view.addItemDecoration(new MVerticalItemDecoration(this,0.5f,
                0,0, Color.parseColor("#ECECEC")));

        /****上传图片***/
        SingleImgAdapterV2.BuildConfig config = new SingleImgAdapterV2.BuildConfig();
        config.max_count = 9;
        config.pictureAndVideo = false;
        GridLayoutManager picManager = new GridLayoutManager(this,5);
        singleImgAdapter = new SingleImgAdapterV2(this,imgList,config);
        recy_pics.setLayoutManager(picManager);
        int space = TransformUtil.dip2px(this, 4);
        recy_pics.addItemDecoration(new GridSpacingItemDecoration(space, false));
        recy_pics.setAdapter(singleImgAdapter);

        presenter = new FindSendPicPresenter(this,this);

        singleImgAdapter.setOnItemClickListener((view, position) -> {
            if (position >= imgList.size()){
                singleImgAdapter.selectPic();
            }else {
                EventBus.getDefault().postSticky(imgList);
                BrowseImageVideoAct.BuildConfig config1 = new BrowseImageVideoAct.BuildConfig();
                config1.position = position;
                config1.isShowImageVideo = true;
                config1.isShowSelect = false;
                BrowseImageVideoAct.startAct(this, config1, BrowseImageVideoAct.REQUEST_CODE);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (edit != null){
            edit.addTextChangedListener(new SimpleTextWatcher(){
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (s.length() > MAX_TEXT_COUNT){
                        edit.setText(s.subSequence(0,MAX_TEXT_COUNT));
                        edit.setSelection(MAX_TEXT_COUNT);
                        Common.staticToast("字数不能超过800");
                    }
                }
            });
        }

        miv_close.setOnClickListener(v -> saveDraft());
    }

    private void saveDraft(){

        if ((edit != null&&!isEmpty(edit.getText().toString())) || !isEmpty(imgList)){
            final PromptDialog promptDialog = new PromptDialog(this);
            promptDialog.setTvSureBGColor(Color.WHITE);
            promptDialog.setCancelable(true);
            promptDialog.setTvSureColor(R.color.pink_color);
            promptDialog.setTvSureIsBold(false).setTvCancleIsBold(false)
                    .setSureAndCancleListener("是否保留草稿内容？",
                            "保留", v -> send("1"),
                            "不保留", v -> {
                                promptDialog.dismiss();
                                finish();
                            }).show();
            return;
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            saveDraft();
            return true;
        }else {
            return super.onKeyDown(keyCode, event);
        }
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

    /**
     * 发布
     */
    @OnClick(R.id.mtv_toolbar_right)
    public void sendBlog(){
        send("0");
    }

    /**
     * 1表示保存为草稿 0否 默认为0
     * @param draft
     */
    private void send(String draft) {
        String edit = this.edit.getText().toString();
        if (isEmpty(edit)){
            Common.staticToast("心得不能为空");
            return;
        }
        String pics = getpicsPath();

        String goodsid = getGoodsid();

        String address = mtv_address.getText().toString();

        if (presenter != null){
            presenter.publish(edit,pics,mVideoUrl,mVideoThumb,topic_id,address,goodsid,draft);
        }
    }

    private String getpicsPath(){
        if (!isEmpty(imgList)){
            StringBuilder sb = new StringBuilder();
            for (ImageVideo e:imgList) {
                if (!isMP4Path(e.url)) {
                    sb.append(e.url);
                    sb.append(",");
                }
            }
            if (sb.length() > 1) {
                return sb.toString().substring(0, sb.length() - 1);
            }
        }
        return "";
    }

    /**
     * 判断是否是MP4文件路径
     * @param path
     * @return
     */
    private boolean isMP4Path(String path){
        if (isEmpty(path))return false;
        else return path.toLowerCase().endsWith(".mp4");
    }

    private String getGoodsid(){
        if (!isEmpty(goodsLists)){
            StringBuilder sb = new StringBuilder();
            for (GoodsDeatilEntity.Goods goods :goodsLists) {
                sb.append(goods.goods_id);
                sb.append(",");
            }
            return sb.toString().substring(0,sb.length()-1);
        }
        return "";
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
            topic_id = data.getStringExtra("id");
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
        }else if (requestCode == SELECT_PIC_REQUESTCODE && resultCode == Activity.RESULT_OK){
            ArrayList<String> picturePaths = data.getStringArrayListExtra(SelectPicVideoAct.EXTRA_RESULT);
            if (!isEmpty(picturePaths)){
                String path = picturePaths.get(0);
                if (!isEmpty(path) && isMP4Path(path)){
                    if (presenter != null){
                        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                        mRetriever.setDataSource(path);
                        Bitmap bitmap = mRetriever.getFrameAtTime();
                        presenter.uploadVideoThumb(BitmapUtil.Bitmap2Bytes(bitmap));
                        presenter.uploadVideo(path);
                    }
                }else {
                    index = 0;
                    compressImgs(index, picturePaths);
                }
            }
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
                ImageVideo imageEntity = new ImageVideo();
                imageEntity.path = list.get(index);
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
    public void uploadImg(UploadPicEntity picEntity) {
        for (int i = 0; i < picEntity.relativePath.size(); i++) {
            imgList.get(i).url = picEntity.relativePath.get(i);
        }
        if (singleImgAdapter != null)
        singleImgAdapter.notifyDataSetChanged();
    }

    /**
     * 上传视频成功
     *
     * @param url 服务端视频地址
     */
    @Override
    public void uploadViodeSuccess(String url,String local_path) {
        mVideoUrl = url;
        if (singleImgAdapter != null) {
            ImageVideo entity = new ImageVideo();
            entity.path = local_path;
            entity.url = url;
            imgList.add(entity);
            singleImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 发布成功
     */
    @Override
    public void publishSuccess() {
        finish();
    }

    /**
     * 显示草稿
     *
     * @param entity
     */
    @Override
    public void resetDraft(BlogDraftEntity entity) {
        if (edit != null && !isEmpty(entity.text)){
            edit.setText(entity.text);
        }

        if (mtv_topic != null && !isEmpty(entity.activity_title)){
            mtv_topic.setText(entity.activity_title);
            topic_id = entity.activity_id;
        }

        if (mtv_address != null && !isEmpty(entity.place)){
            mtv_address.setText(entity.place);
        }
        if (!isEmpty(entity.goods_infos)) {
            if (goodsLists == null) {
                goodsLists = new ArrayList<>();
            }
            goodsLists.addAll(entity.goods_infos);
            if (adapter == null) {
                adapter = new SelectGoodsAdapter(this,false, goodsLists);
                recy_view.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }

        if (!isEmpty(entity.video)){
            mVideoThumb = entity.video_thumb;
            mVideoUrl = entity.video;
            ImageVideo imageVideo = new ImageVideo();
            imageVideo.path = entity.video;
            imageVideo.coverPath = entity.video_thumb;
            imgList.add(imageVideo);
            singleImgAdapter.notifyDataSetChanged();
        }else if (!isEmpty(entity.pics)){
            for (String url:entity.pics) {
                ImageVideo imageVideo = new ImageVideo();
                imageVideo.path = url;
                imgList.add(imageVideo);
            }
            singleImgAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 视频封面地址
     *
     * @param s
     */
    @Override
    public void videoThumb(String s) {
        mVideoThumb = s;
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
}
