package com.shunlian.app.ui.my_comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CreatCommentAdapter;
import com.shunlian.app.bean.ImageEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.bean.UploadPicEntity;
import com.shunlian.app.presenter.CommentPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICommentView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentActivity extends BaseActivity implements ICommentView, View.OnClickListener, CreatCommentAdapter.OnCommentChangeCallBack {
    public static final int CREAT_COMMENT = 1; //发布评论
    public static final int APPEND_COMMENT = 2; //追加评论
    public static final int CHANGE_COMMENT = 3; //修改评论

    @BindView(R.id.recycler_creat_comment)
    RecyclerView recycler_creat_comment;

    @BindView(R.id.tv_title_left)
    TextView tv_title_left;

    @BindView(R.id.tv_title_right)
    TextView tv_title_right;

    private List<ReleaseCommentEntity> commentList;
    private ReleaseCommentEntity commentEntity;
    private CreatCommentAdapter creatCommentAdapter;
    private int currentType;
    private CommentPresenter commentPresenter;
    private int currentPosition;
    private List<ImageEntity> paths = new ArrayList<>();
    private int currentLogisticsStar = 0;
    private int currentAttitudeStar = 0;
    private int currentConsistentStar = 0;

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
        switch (currentType) {
            //创建评论
            case CREAT_COMMENT:
                commentList.addAll((Collection<? extends ReleaseCommentEntity>) getIntent().getSerializableExtra("commentList"));
                commentList.addAll((Collection<? extends ReleaseCommentEntity>) getIntent().getSerializableExtra("commentList"));
                break;
            //追评评论
            case APPEND_COMMENT:
                commentEntity = (ReleaseCommentEntity) getIntent().getSerializableExtra("comment");
                commentList.add(commentEntity);
                break;
            //修改评论
            case CHANGE_COMMENT:
                commentEntity = (ReleaseCommentEntity) getIntent().getSerializableExtra("comment");
                commentList.add(commentEntity);
                break;
            default:
                finish();
                break;
        }
        creatCommentAdapter = new CreatCommentAdapter(this, false, commentList, currentType);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_creat_comment.setNestedScrollingEnabled(false);
        recycler_creat_comment.setLayoutManager(linearLayoutManager);
        recycler_creat_comment.setAdapter(creatCommentAdapter);
        recycler_creat_comment.addItemDecoration(new VerticalItemDecoration(TransformUtil.dip2px(this, 7), 0, 0));
        creatCommentAdapter.setOnCommentChangeCallBack(this);
    }

    @Override
    protected void initListener() {
        tv_title_right.setOnClickListener(this);
        super.initListener();
    }

    public void openAlbum(int position) {
        currentPosition = position;
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            paths.clear();
            paths.add(new ImageEntity(imagePath));

            creatCommentAdapter.addImages(paths, currentPosition);
            commentPresenter.uploadPic(paths, "comment");
            c.close();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    ReleaseCommentEntity releaseCommentEntity = commentList.get(0);
                    if (!isEmpty(releaseCommentEntity.content)) {
                        commentPresenter.appendComment(commentEntity.comment_id, releaseCommentEntity.content, releaseCommentEntity.pic);
                    }
                } else if (currentType == CHANGE_COMMENT) {
                    ReleaseCommentEntity releaseCommentEntity = commentList.get(0);
                    if (!isEmpty(releaseCommentEntity.content)) {
                        commentPresenter.changeComment(commentEntity.comment_id, releaseCommentEntity.content, releaseCommentEntity.pic);
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
                    getGoodsString();
//                    commentPresenter.creatComment(commentEntity.comment_id, currentLogisticsStar, currentAttitudeStar, currentConsistentStar, releaseCommentEntity.pic);
                }
                break;
        }
    }

    public String getGoodsString() {
        String result = null;
        if (commentList == null || commentList.size() == 0) {
            return null;
        }
        try {
            for (int i = 0; i < commentList.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                ReleaseCommentEntity releaseCommentEntity = commentList.get(i);
                jsonObject.put("goods_id", releaseCommentEntity.goodsId);
                jsonObject.put("star_level", releaseCommentEntity.starLevel);
                jsonObject.put("content", releaseCommentEntity.content);
                jsonObject.put("images", releaseCommentEntity.pic);
                LogUtil.httpLogW("jsonObject:" + jsonObject.toString());
            }
        } catch (JSONException e) {
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
    public void OnLogisticsStar(float logistics) {
        currentLogisticsStar = ((int) logistics);
    }

    @Override
    public void OnAttitudeStar(float attitude) {
        currentAttitudeStar = ((int) attitude);
    }

    @Override
    public void OnConsistent(float consistent) {
        currentConsistentStar = ((int) consistent);
    }

    @Override
    public void uploadImg(UploadPicEntity picEntity) {
        List<ImageEntity> imageEntities;
        if (commentList.get(currentPosition).imgs == null) {
            imageEntities = new ArrayList<>();
        } else {
            imageEntities = commentList.get(currentPosition).imgs;
        }
        List<String> pics = picEntity.relativePath;
        for (String string : pics) {
            imageEntities.add(new ImageEntity(string));
        }
        commentList.get(currentPosition).imgs = imageEntities;
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < imageEntities.size(); i++) {
            if (i != imageEntities.size() - 1) {
                stringBuffer.append(imageEntities.get(i).imgPath + ",");
            } else {
                stringBuffer.append(imageEntities.get(i).imgPath);
            }
        }
        commentList.get(currentPosition).pic = stringBuffer.toString();
    }

    @Override
    public void uploadProgress(int progress, String tag) {
        Message message = mHandler.obtainMessage();
        message.arg1 = progress;
        message.obj = tag;
        mHandler.sendMessage(message);
    }

    @Override
    public void appendCommentSuccess() {
        Common.staticToast("追评成功");
        finish();
    }

    @Override
    public void appendCommentFail(String error) {
        Common.staticToast(error);
    }

    @Override
    public void changeCommentSuccess() {
        Common.staticToast("修改好评成功");
        finish();
    }

    @Override
    public void changeCommtFail(String errorstr) {
        Common.staticToast(errorstr);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int progress = msg.arg1;
            String tag = String.valueOf(msg.obj);
//            creatCommentAdapter.updateProgress(currentPosition, tag, progress);
        }
    };
}
