package com.shunlian.app.ui.my_comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.CreatCommentAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.photopick.PhotoPickerActivity;
import com.shunlian.app.photopick.SelectModel;
import com.shunlian.app.photopick.PhotoPickerIntent;
import com.shunlian.app.presenter.CommentPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.ICommentView;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentActivity extends BaseActivity implements ICommentView, View.OnClickListener, CreatCommentAdapter.OnCommentChangeCallBack, FiveStarBar.OnRatingBarChangeListener {
    public static final int CREAT_COMMENT = 1; //发布评论
    public static final int APPEND_COMMENT = 2; //追加评论
    public static final int CHANGE_COMMENT = 3; //修改评论
    private static final int REQUEST_CAMERA_CODE = 100;

    @BindView(R.id.recycler_creat_comment)
    ListView recycler_creat_comment;

    @BindView(R.id.tv_title_left)
    TextView tv_title_left;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    @BindView(R.id.miv_close)
    MyImageView miv_close;


    FiveStarBar ratingBar_logistics, ratingBar_attitude, ratingBar_consistent;

    private List<ReleaseCommentEntity> commentList;
    private CreatCommentAdapter creatCommentAdapter;
    private int currentType;
    private CommentPresenter commentPresenter;
    private int currentPosition;
    private String currentOrderId;
    private List<ImageEntity> paths = new ArrayList<>();
    private int currentLogisticsStar = 0;
    private int currentAttitudeStar = 0;
    private int currentConsistentStar = 0;
    private View footView;
    private PromptDialog promptDialog;
    private int index;

    public static void startAct(Context context, List<ReleaseCommentEntity> list, int type) {
        Intent intent = new Intent(context, CreatCommentActivity.class);
        intent.putExtra("commentList", (Serializable) list);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    public static void startAct(Context context, ReleaseCommentEntity releaseCommentEntity, int type) {
        Intent intent = new Intent(context, CreatCommentActivity.class);
        intent.putExtra("comment", releaseCommentEntity);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_creat_comment;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        tv_title_left.setText("评价晒单");
        tv_title_left.setVisibility(View.VISIBLE);

        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText(R.string.comment_release);
        tv_title_right.setTextColor(getColorResouce(R.color.pink_color));

        commentPresenter = new CommentPresenter(this, this);
        commentList = new ArrayList<>();

        currentType = getIntent().getIntExtra("type", -1);

        if (getIntent().getSerializableExtra("commentList") != null) {
            List<ReleaseCommentEntity> list = (List<ReleaseCommentEntity>) getIntent().getSerializableExtra("commentList");
            if (currentType == APPEND_COMMENT) {
                for (ReleaseCommentEntity entity : list) {
                    if (!"0".equals(entity.is_append)) {
                        commentList.add(entity);
                    }
                }
            } else {
                commentList.addAll(list);
            }
        } else if (getIntent().getSerializableExtra("comment") != null) {
            commentList.add((ReleaseCommentEntity) getIntent().getSerializableExtra("comment"));
        }

        creatCommentAdapter = new CreatCommentAdapter(this, commentList, currentType);
        recycler_creat_comment.setAdapter(creatCommentAdapter);
        creatCommentAdapter.setOnCommentChangeCallBack(this);

        if (currentType == CREAT_COMMENT) {
            recycler_creat_comment.addFooterView(footView);
        }
    }

    @Override
    protected void initListener() {
        miv_close.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        footView = LayoutInflater.from(this).inflate(R.layout.foot_creat_comment, null, false);
        ratingBar_logistics = (FiveStarBar) footView.findViewById(R.id.ratingBar_logistics);
        ratingBar_attitude = (FiveStarBar) footView.findViewById(R.id.ratingBar_attitude);
        ratingBar_consistent = (FiveStarBar) footView.findViewById(R.id.ratingBar_consistent);
        ratingBar_consistent.setOnRatingBarChangeListener(this);
        ratingBar_attitude.setOnRatingBarChangeListener(this);
        ratingBar_logistics.setOnRatingBarChangeListener(this);

        promptDialog = new PromptDialog(this);
        promptDialog.setSureAndCancleListener(getStringResouce(R.string.ready_to_cancel_comment),
                getStringResouce(R.string.SelectRecommendAct_sure),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }, getStringResouce(R.string.errcode_cancel),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        promptDialog.dismiss();
                    }
                });
        super.initListener();
    }

    public void openAlbum(int position) {
        currentPosition = position;
        int max = 5;
        if (commentList.get(currentPosition).imgs != null) {
            max = 5 - commentList.get(currentPosition).imgs.size();
        }
        PhotoPickerIntent intent = new PhotoPickerIntent(this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(max); // 最多选择照片数量，默认为9
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            final ArrayList<String> imagePaths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
            if (commentList.get(currentPosition).imgs == null) {
                paths = new ArrayList<>();
            } else {
                paths = commentList.get(currentPosition).imgs;
            }
            index = 0;
            compressImgs(index, imagePaths);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void compressImgs(int i, final List<String> list) {
        Luban.with(this).load(list.get(i)).putGear(3).setCompressListener(new OnCompressListener() {

            @Override
            public void onStart() {
                LogUtil.httpLogW("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                ImageEntity imageEntity = new ImageEntity(list.get(index));
                imageEntity.file = file;
                paths.add(imageEntity);
                index++;
                if (index >= list.size()) {
                    commentPresenter.uploadPic(paths, "comment");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_title_right:
                if (currentType == APPEND_COMMENT) {
                    String goodsString = getGoodsString();
                    if (!isEmpty(goodsString)) {
                        commentPresenter.appendComment(goodsString);
                    }
                } else if (currentType == CHANGE_COMMENT) {
                    ReleaseCommentEntity releaseCommentEntity = commentList.get(0);
                    if (!isEmpty(releaseCommentEntity.content)) {
                        commentPresenter.changeComment(releaseCommentEntity.comment_id, releaseCommentEntity.content, releaseCommentEntity.picString);
                    }
                } else if (currentType == CREAT_COMMENT) {
                    if (currentLogisticsStar == 0) {
                        Common.staticToast("请评价物流服务");
                        return;
                    }

                    if (currentAttitudeStar == 0) {
                        Common.staticToast("请评价服务态度");
                        return;
                    }

                    if (currentConsistentStar == 0) {
                        Common.staticToast("请评价描述相符");
                        return;
                    }
                    String goodsString = getGoodsString();
                    LogUtil.httpLogW("goodsString:" + goodsString);
                    if (TextUtils.isEmpty(goodsString) || TextUtils.isEmpty(currentOrderId)) {
                        return;
                    }
                    commentPresenter.creatComment(currentOrderId, String.valueOf(currentLogisticsStar), String.valueOf(currentAttitudeStar), String.valueOf(currentConsistentStar), goodsString);
                }
                break;
            case R.id.miv_close:
                if (promptDialog != null) {
                    promptDialog.show();
                }
                break;
        }
    }

    public String getGoodsString() {
        String result = null;

        if (commentList == null || commentList.size() == 0) {
            return null;
        }
        List<Map> array = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            for (int i = 0; i < commentList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                ReleaseCommentEntity releaseCommentEntity = commentList.get(i);

                if (!isEmpty(releaseCommentEntity.comment_id)) {
                    map.put("comment_id", releaseCommentEntity.comment_id);
                }
                if (!isEmpty(releaseCommentEntity.goodsId)) {
                    map.put("goods_id", releaseCommentEntity.goodsId);
                }

                if (!isEmpty(releaseCommentEntity.order)) {
                    currentOrderId = releaseCommentEntity.order;
                    map.put("ordersn", releaseCommentEntity.order);
                }

                if (!isEmpty(releaseCommentEntity.starLevel) && currentType == CREAT_COMMENT) {
                    map.put("star_level", releaseCommentEntity.starLevel);
                }

                if (!isEmpty(releaseCommentEntity.content)) {
                    map.put("content", releaseCommentEntity.content);
                } else {
                    map.put("content", "");
                }

                if (releaseCommentEntity.imgs != null && releaseCommentEntity.imgs.size() != 0) {
                    StringBuffer stringBuffer;
                    if (TextUtils.isEmpty(releaseCommentEntity.picString)) {
                        stringBuffer = new StringBuffer("");
                    } else {
                        stringBuffer = new StringBuffer(releaseCommentEntity.picString);
                        stringBuffer.append(",");
                    }
                    for (int j = 0; j < releaseCommentEntity.imgs.size(); j++) {
                        stringBuffer.append(releaseCommentEntity.imgs.get(j));
                        if (i != releaseCommentEntity.imgs.size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                    map.put("images", stringBuffer.toString());
                } else {
                    map.put("images", "");
                }
                array.add(map);
            }
            result = mapper.writeValueAsString(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void OnComment(String content, int position) {
        commentList.get(position).content = content;
    }

    @Override
    public void OnCommentLevel(String level, int position) {
        commentList.get(position).starLevel = level;
    }

    @Override
    public void uploadImg(UploadPicEntity picEntity) {
        Message message = mHandler.obtainMessage();
        message.obj = picEntity.relativePath;
        message.what = 1;
        mHandler.sendMessage(message);
    }

    @Override
    public void uploadProgress(int progress, String tag) {
        Message message = mHandler.obtainMessage();
        message.arg1 = progress;
        message.obj = tag;
        mHandler.sendMessage(message);
    }

    @Override
    public void CommentSuccess() {
        switch (currentType) {
            case CREAT_COMMENT:
                Common.staticToast("评价成功");
                break;
            case CHANGE_COMMENT:
                Common.staticToast("修改评价成功");
                break;
            case APPEND_COMMENT:
                Common.staticToast("追加评价成功");
                break;
        }
        finish();
    }

    @Override
    public void CommentFail(String error) {
        Common.staticToast(error);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int progress = msg.arg1;
                    String tag = String.valueOf(msg.obj);
//            creatCommentAdapter.updateProgress(currentPosition, tag, progress);
                    break;
                case 1:
                    List<String> picStr = (List<String>) msg.obj;
                    creatCommentAdapter.addImages(paths, currentPosition);
                    break;
            }
        }
    };

    @Override
    public void onRatingChanged(FiveStarBar simpleRatingBar, float rating, boolean fromUser) {
        switch (simpleRatingBar.getId()) {
            case R.id.ratingBar_logistics:
                currentLogisticsStar = (int) rating;
                break;
            case R.id.ratingBar_attitude:
                currentAttitudeStar = (int) rating;
                break;
            case R.id.ratingBar_consistent:
                currentConsistentStar = (int) rating;
                break;
        }
    }
}
