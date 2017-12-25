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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
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
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICommentView;
import com.shunlian.app.widget.FiveStarBar;
import com.shunlian.app.widget.MyImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentActivity extends BaseActivity implements ICommentView, View.OnClickListener, CreatCommentAdapter.OnCommentChangeCallBack, FiveStarBar.OnRatingBarChangeListener {
    public static final int CREAT_COMMENT = 1; //发布评论
    public static final int APPEND_COMMENT = 2; //追加评论
    public static final int CHANGE_COMMENT = 3; //修改评论

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
    private String currentCommentId;
    private String currentOrderId;
    private List<ImageEntity> paths = new ArrayList<>();
    private int currentLogisticsStar = 0;
    private int currentAttitudeStar = 0;
    private int currentConsistentStar = 0;
    private View footView;
    private PromptDialog promptDialog;


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
            commentList.addAll((Collection<? extends ReleaseCommentEntity>) getIntent().getSerializableExtra("commentList"));
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
                    if (TextUtils.isEmpty(goodsString) || TextUtils.isEmpty(currentCommentId)) {
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
        try {
            JSONArray array = new JSONArray();
            for (int i = 0; i < commentList.size(); i++) {
                Map<String, String> map = new HashMap<>();
                ReleaseCommentEntity releaseCommentEntity = commentList.get(i);

                if (!isEmpty(releaseCommentEntity.comment_id)) {
                    currentCommentId = releaseCommentEntity.comment_id;
                    map.put("comment_id", releaseCommentEntity.comment_id);
                }
                if (!isEmpty(releaseCommentEntity.goodsId)) {
                    map.put("goods_id", releaseCommentEntity.goodsId);
                }

                if (!isEmpty(releaseCommentEntity.order)) {
                    currentOrderId = releaseCommentEntity.order;
                    map.put("ordersn", releaseCommentEntity.order);
                }

                if (!isEmpty(releaseCommentEntity.starLevel)) {
                    map.put("star_level", releaseCommentEntity.starLevel);
                }

                if (!isEmpty(releaseCommentEntity.content)) {
                    map.put("content", releaseCommentEntity.content);
                } else {
                    map.put("content", "");
                }
                if (!isEmpty(releaseCommentEntity.picString)) {
                    map.put("images", releaseCommentEntity.picString);
                } else {
                    map.put("images", "");
                }
                array.put(i, map.toString());
            }
            result = array.toString();
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
            switch (msg.what) {
                case 0:
                    int progress = msg.arg1;
                    String tag = String.valueOf(msg.obj);
//            creatCommentAdapter.updateProgress(currentPosition, tag, progress);
                    break;
                case 1:
                    List<String> picStr = (List<String>) msg.obj;
                    creatCommentAdapter.addImages(paths, currentPosition);
                    ReleaseCommentEntity releaseCommentEntity = commentList.get(currentPosition);

                    StringBuffer stringBuffer;
                    if (TextUtils.isEmpty(releaseCommentEntity.picString)) {
                        stringBuffer = new StringBuffer("");
                    } else {
                        stringBuffer = new StringBuffer(releaseCommentEntity.picString);
                        stringBuffer.append(",");
                    }
                    for (int i = 0; i < picStr.size(); i++) {
                        stringBuffer.append(picStr.get(i));
                        if (i != picStr.size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                    commentList.get(currentPosition).picString = stringBuffer.toString();
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
