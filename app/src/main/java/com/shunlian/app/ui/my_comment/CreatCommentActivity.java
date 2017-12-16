package com.shunlian.app.ui.my_comment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.CreatCommentAdapter;
import com.shunlian.app.bean.GoodsDeatilEntity;
import com.shunlian.app.bean.ReleaseCommentEntity;
import com.shunlian.app.presenter.CommentPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.utils.VerticalItemDecoration;
import com.shunlian.app.view.ICommentView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/12/12.
 */

public class CreatCommentActivity extends BaseActivity implements ICommentView, View.OnClickListener, CreatCommentAdapter.OnCommentContentCallBack {
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
    private String currentContent;
    private CommentPresenter commentPresenter;

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
                commentList.addAll((Collection<? extends ReleaseCommentEntity>) getIntent().getSerializableExtra("comment"));
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
        creatCommentAdapter.setOnCommentContentCallBack(this);
    }

    @Override
    protected void initListener() {
        tv_title_right.setOnClickListener(this);
        super.initListener();
    }

    public void openAlbum() {
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
            LogUtil.httpLogW("imagePath:" + imagePath);

            commentPresenter.uploadPic(imagePath);
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
                    if (!isEmpty(currentContent)) {
                        commentPresenter.appendComment(commentEntity.comment_id, currentContent, "");
                    }
                }
                break;

        }
    }

    @Override
    public void OnComment(String content) {
        currentContent = content;
    }

    @Override
    public void appendCommentSuccess() {
        finish();
    }

    @Override
    public void appendCommentFail(String error) {
        Common.staticToast(error);
    }
}
