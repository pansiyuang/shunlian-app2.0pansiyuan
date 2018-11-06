package com.shunlian.app.ui.find_send;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.adapter.SelectGoodsAdapter;
import com.shunlian.app.bean.BlogDraftEntity;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.presenter.FindSendPicPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.utils.BitmapUtil;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.MVerticalItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.SimpleTextWatcher;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.ISelectPicVideoView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhanghe on 2018/10/12.
 */

public class FindSendPictureTextAct extends BaseActivity implements ISelectPicVideoView, View.OnTouchListener {

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
    RecyclerView recyGoods;

    @BindView(R.id.recy_pics)
    RecyclerView recy_pics;

    @BindView(R.id.edit)
    EditText edit;

    @BindView(R.id.miv_close)
    MyImageView miv_close;

    @BindView(R.id.mtv_associated_goods)
    MyTextView mtv_associated_goods;

    @BindView(R.id.mtv_count)
    MyTextView mtv_count;

    @BindView(R.id.miv_tip)
    MyImageView miv_tip;

    private List<GoodsDeatilEntity.Goods> mGoodsLists;
    private SelectGoodsAdapter mGoodsAdapter;
    private FindSendPicPresenter presenter;
    /********打开选择图片activity的请求码**********/
    public static final int SELECT_PIC_REQUESTCODE = 800;
    /*********添加地址************/
    public static final int ADDRESS_REQUEST_CODE = 100;
    /***********添加话题******************/
    public static final int ADDTOPIC_REQUEST_CODE = 200;
    /************添加商品********************/
    public static final int ADDGOODS_REQUEST_CODE = 400;
    private String topic_id;//话题id
    private String mVideoUrl;//视频地址
    private String mVideoThumb;//视频封面地址
    /***
     * 最大心得字数
     */
    private int MAX_TEXT_COUNT = 300;
    private final String text_format = "%d/%d";
    /***最多关联商品数量***/
    private final int MAX_ASSOCIATED_GOODS = 5;
    private SendConfig mConfig;
    private PromptDialog promptDialog;

    /**
     *
     * @param context
     * @param config 配置信息
     */
    public static void startAct(Context context, SendConfig config) {
        Intent intent = new Intent(context, FindSendPictureTextAct.class);
        intent.putExtra("config", config);
        if (!(context instanceof Activity))
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_picture_text;
    }

    @Override
    protected void initListener() {
        super.initListener();
        if (edit != null) {
            edit.setOnTouchListener(this);
            edit.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    super.afterTextChanged(s);
                    if (s.length() > MAX_TEXT_COUNT) {
                        edit.setText(s.subSequence(0, MAX_TEXT_COUNT));
                        edit.setSelection(MAX_TEXT_COUNT);
                        Common.staticToast("字数不能超过" + MAX_TEXT_COUNT);
                    }
                    mtv_count.setText(String.format(text_format,edit.getText().length(),MAX_TEXT_COUNT));
                }
            });
        }

        miv_close.setOnClickListener(v -> saveDraft());
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        mConfig = getIntent().getParcelableExtra("config");
        if (mConfig != null && mConfig.isWhiteList) {
            MAX_TEXT_COUNT = 800;
        }
        mtv_count.setText(String.format(text_format,0,MAX_TEXT_COUNT));
        presenter = new FindSendPicPresenter(this, this);

        mtvToolbarTitle.setText("发布图文");
        if (mConfig != null && !isEmpty(mConfig.activityTitle)){
            mtv_topic.setText(mConfig.activityTitle);
            topic_id = mConfig.activityID;
            miv_tip.setVisibility(View.INVISIBLE);
            mtvToolbarTitle.setText("发布心得");
        }else {//从活动进发布页不加载草稿
            presenter.initApi();
        }

        gone(mrlayoutToolbarMore);
        visible(mtvToolbarRight);
        mtvToolbarRight.setText("发布");
        mtvToolbarRight.setTextColor(getColorResouce(R.color.pink_color));

        /********商品列表*********/
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyGoods.setLayoutManager(manager);
        recyGoods.addItemDecoration(new MVerticalItemDecoration(this, 0.5f,
                0, 0, Color.parseColor("#ECECEC")));
        recyGoods.setNestedScrollingEnabled(false);
        /********end*********/




        /****上传图片***/
        GridLayoutManager picManager = new GridLayoutManager(this, 5);
        recy_pics.setLayoutManager(picManager);
        int space = TransformUtil.dip2px(this, 4);
        recy_pics.addItemDecoration(new GridSpacingItemDecoration(space, false));
        /****end***/

    }

    /**
     * 添加位置
     */
    @OnClick(R.id.rlayout_address)
    public void appendAddress() {
        NearAddressAct.startAct(this, ADDRESS_REQUEST_CODE);
    }

    /**
     * 添加话题
     */
    @OnClick(R.id.rlayout_topic)
    public void addTopic() {
        if (!(mConfig != null && !isEmpty(mConfig.activityID))){
            AddTopicAct.startAct(this, ADDTOPIC_REQUEST_CODE);
        }
    }

    /**
     * 选择商品
     */
    @OnClick(R.id.rlayout_select_goods)
    public void selectGoods() {
        if (!isEmpty(mGoodsLists) && mGoodsLists.size() >= MAX_ASSOCIATED_GOODS){
            Common.staticToast("共联商品最多可选5个哦~");
            return;
        }
        String goodsid = getGoodsid();
        SelectGoodsAct.startAct(this,goodsid, ADDGOODS_REQUEST_CODE);
    }

    /**
     * 发布
     */
    @OnClick(R.id.mtv_toolbar_right)
    public void sendBlog() {
        send("0");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveDraft();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String name = data.getStringExtra("name");
            //String addr = data.getStringExtra("addr");
            mtv_address.setText(name);
        } else if (requestCode == ADDTOPIC_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String title = data.getStringExtra("title");
            topic_id = data.getStringExtra("id");
            mtv_topic.setText(title);
        } else if (requestCode == ADDGOODS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (mGoodsLists == null) {
                mGoodsLists = new ArrayList<>();
            }
            GoodsDeatilEntity.Goods goods = data.getParcelableExtra("goods");
            if (mGoodsLists.size() >= MAX_ASSOCIATED_GOODS) {
                //Common.staticToast(String.format("最多关联%d个商品",MAX_ASSOCIATED_GOODS));
                return;
            }else {
                mGoodsLists.add(goods);
                associated();
            }
            if (mGoodsAdapter == null) {
                mGoodsAdapter = new SelectGoodsAdapter(this, false, mGoodsLists, null);
                recyGoods.setAdapter(mGoodsAdapter);
                mGoodsAdapter.setOnDelGoodsListener(position -> {
                    if (!isEmpty(mGoodsLists)) {
                        mGoodsLists.remove(position);
                        mGoodsAdapter.notifyDataSetChanged();
                        associated();
                    }
                });
            } else {
                mGoodsAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == SELECT_PIC_REQUESTCODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> picturePaths = data.getStringArrayListExtra(SelectPicVideoAct.EXTRA_RESULT);
            if (!isEmpty(picturePaths)) {
                String path = picturePaths.get(0);
                if (!isEmpty(path) && isMP4Path(path)) {
                    if (presenter != null) {
                        try {
                            MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
                            mRetriever.setDataSource(path);
                            Bitmap bitmap = mRetriever.getFrameAtTime();
                            presenter.uploadVideoThumb(BitmapUtil.Bitmap2Bytes(bitmap));
                            presenter.uploadVideo(path);
                        }catch (Exception e){}
                    }
                } else {
                    if (presenter != null){
                        presenter.reducePics(picturePaths);
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null){
            presenter.detachView();
            presenter = null;
        }

        if (mGoodsLists != null){
            mGoodsLists.clear();
            mGoodsLists = null;
        }
        if (mGoodsAdapter != null){
            mGoodsAdapter.unbind();
            mGoodsAdapter = null;
        }

        if (promptDialog != null){
            promptDialog.dismiss();
        }
    }

    @Override
    public void uploadImg(UploadPicEntity picEntity) {
    }

    /**
     * 上传视频成功
     *
     * @param url 服务端视频地址
     */
    @Override
    public void uploadViodeSuccess(String url, String local_path) {
        mVideoUrl = url;
    }

    @Override
    public void setAdapter(BaseRecyclerAdapter adapter) {
        if (recy_pics != null)
        recy_pics.setAdapter(adapter);
    }

    /**
     * 发布成功
     */
    @Override
    public void publishSuccess() {
        finish();
        MyPageActivity.startAct(this,mConfig.memberId);
    }

    /**
     * 显示草稿
     *
     * @param entity
     */
    @Override
    public void resetDraft(BlogDraftEntity entity) {
        if (edit != null && !isEmpty(entity.text)) {
            edit.setText(entity.text);
        }

        if (mtv_topic != null && !isEmpty(entity.activity_title)) {
            mtv_topic.setText(entity.activity_title);
            topic_id = entity.activity_id;
        }

        if (mtv_address != null && !isEmpty(entity.place)) {
            mtv_address.setText(entity.place);
        }
        if (!isEmpty(entity.goods_infos)) {
            if (mGoodsLists == null) {
                mGoodsLists = new ArrayList<>();
            }
            mGoodsLists.addAll(entity.goods_infos);
            if (mGoodsAdapter == null) {
                mGoodsAdapter = new SelectGoodsAdapter(this, false, mGoodsLists, null);
                recyGoods.setAdapter(mGoodsAdapter);
            } else {
                mGoodsAdapter.notifyDataSetChanged();
            }
        }

        if (!isEmpty(entity.video)) {
            mVideoThumb = entity.video_thumb;
            mVideoUrl = entity.video;
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


    /**
     * 1表示保存为草稿 0否 默认为0
     *
     * @param draft
     */
    private void send(String draft) {
        String edit = this.edit.getText().toString();
        if (isEmpty(edit)) {
            Common.staticToast("请输入内容~");
            return;
        }
        String pics = presenter.getpicsPath();
        if (isEmpty(pics) && isEmpty(mVideoUrl)) {
            Common.staticToast("请添加图片或视频~");
            return;
        }
        String goodsid = getGoodsid();

        String address = mtv_address.getText().toString();
        //过滤空地址
        if (getStringResouce(R.string.find_add_addr).equals(address)){address = null;}

        if (presenter != null) {
            presenter.publish(edit, pics, mVideoUrl, mVideoThumb, topic_id, address, goodsid, draft);
        }
    }

    /**
     * 询问是否保存草稿
     */
    private void saveDraft() {
        if ((edit != null && !isEmpty(edit.getText().toString()))
                && presenter != null && !isEmpty(presenter.getmImgList())) {
            promptDialog = new PromptDialog(this);
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

    /**
     * 关联商品数量标识
     */
    private void associated(){
        if (isEmpty(mGoodsLists)){
            mtv_associated_goods.setText(getStringResouce(R.string.find_associated_goods));
        }else {
            String format = getStringResouce(R.string.find_associated_goods) + "(%d/%d)";
            mtv_associated_goods.setText(String.format(format, mGoodsLists.size(), MAX_ASSOCIATED_GOODS));
        }
    }

    /**
     * 判断是否是MP4文件路径
     *
     * @param path
     * @return
     */
    private boolean isMP4Path(String path) {
        if (isEmpty(path)) return false;
        else return path.toLowerCase().endsWith(".mp4");
    }

    private String getGoodsid() {
        if (!isEmpty(mGoodsLists)) {
            StringBuilder sb = new StringBuilder();
            for (GoodsDeatilEntity.Goods goods : mGoodsLists) {
                if (!isEmpty(goods.goods_id)) {
                    sb.append(goods.goods_id);
                }else if (!isEmpty(goods.id)){
                    sb.append(goods.id);
                }
                sb.append(",");
            }
            return sb.toString().substring(0, sb.length() - 1);
        }
        return null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.edit && canVerticalScroll(edit))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }

    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    private boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop()
                - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }

    public static class SendConfig implements Parcelable {
        /******是否是白名单***必传*****/
        public boolean isWhiteList;//true白名单用户  否则普通用户
        /*********活动id******非必传******/
        public String activityID;
        /*********活动标题******非必传******/
        public String activityTitle;
        /*********用户Id******必传******/
        public String memberId;

        public SendConfig() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.isWhiteList ? (byte) 1 : (byte) 0);
            dest.writeString(this.activityID);
            dest.writeString(this.activityTitle);
            dest.writeString(this.memberId);
        }

        protected SendConfig(Parcel in) {
            this.isWhiteList = in.readByte() != 0;
            this.activityID = in.readString();
            this.activityTitle = in.readString();
            this.memberId = in.readString();
        }

        public static final Creator<SendConfig> CREATOR = new Creator<SendConfig>() {
            @Override
            public SendConfig createFromParcel(Parcel source) {
                return new SendConfig(source);
            }

            @Override
            public SendConfig[] newArray(int size) {
                return new SendConfig[size];
            }
        };
    }
}
