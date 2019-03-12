package com.shunlian.app.ui.discover_new;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.VerifyBlogAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.HotBlogsEntity;
import com.shunlian.app.presenter.VerifyBlogPresenter;
import com.shunlian.app.ui.BaseActivity;
import com.shunlian.app.ui.discover_new.comment.CommentRejectedAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.view.IVerifyBlogView;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.nestedrefresh.NestedRefreshLoadMoreLayout;
import com.shunlian.app.widget.nestedrefresh.NestedSlHeader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class VerifyBlogActivity extends BaseActivity implements IVerifyBlogView, VerifyBlogAdapter.OnItemCallBack {
    @BindView(R.id.miv_select)
    MyImageView mivSelect;

    @BindView(R.id.ll_select)
    LinearLayout llSelect;

    @BindView(R.id.miv_close)
    MyImageView mivClose;

    @BindView(R.id.tv_select)
    TextView tvSelect;

    @BindView(R.id.recycler_list)
    RecyclerView recyclerList;

    @BindView(R.id.lay_refresh)
    NestedRefreshLoadMoreLayout layRefresh;

    @BindView(R.id.nei_empty)
    NetAndEmptyInterface neiEmpty;

    @BindView(R.id.tv_verify_main)
    TextView tvVerifyMain;

    @BindView(R.id.tv_verify_hot)
    TextView tvVerifyHot;

    @BindView(R.id.tv_verify_reject)
    TextView tvVerifyReject;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.ll_verify)
    LinearLayout llVerify;

    private VerifyBlogPresenter mPresenter;
    private VerifyBlogAdapter mAdapter;
    private List<BigImgEntity.Blog> blogList;
    private LinearLayoutManager manager;
    private boolean isSelectAll; //是否选中所有
    private boolean isEdit; //是否进入选择模式
    private int currentRejectPosition = -1;
    private PromptDialog promptDialog;

    public static void startAct(Context context) {
        Intent intent = new Intent(context, VerifyBlogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_blog;
    }

    @Override
    protected void initData() {
        setStatusBarColor(R.color.white);
        setStatusBarFontDark();

        NestedSlHeader header = new NestedSlHeader(this);
        layRefresh.setRefreshHeaderView(header);
        recyclerList.setNestedScrollingEnabled(false);
        manager = new LinearLayoutManager(this);
        recyclerList.setLayoutManager(manager);

        blogList = new ArrayList<>();

        mPresenter = new VerifyBlogPresenter(this, this);
        mPresenter.getHotBlogList(true);
    }

    @Override
    protected void initListener() {
        layRefresh.setOnRefreshListener(() -> {
            mPresenter.initPage();
            mPresenter.getHotBlogList(true);
        });
        recyclerList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (manager != null) {
                    int lastPosition = manager.findLastVisibleItemPosition();
                    if (lastPosition + 1 == manager.getItemCount()) {
                        if (mPresenter != null) {
                            mPresenter.onRefresh();
                        }
                    }
                }
            }
        });
        llSelect.setOnClickListener(v -> {
            if (isSelectAll) {
                isSelectAll = false;
            } else {
                isSelectAll = true;
            }
            setSelectStatus(isSelectAll);
            selectAllBlog(isSelectAll);
            checkSelectItemCount();
        });
        tvSelect.setOnClickListener(v -> {
            selectAllBlog(false);
            if (isEdit) {
                isEdit = false;
                tv_title.setText(String.format("共%s条内容需审核", blogList.size()));
            } else {
                isEdit = true;
                tv_title.setText(String.format("已选择%s条内容", 0));
            }
            setSelectMode(isEdit);
            mAdapter.setEdit(isEdit);
        });
        tvVerifyHot.setOnClickListener(v -> {
            String ids = getSelectedBlogIds();
            if (isEmpty(ids)) {
                Common.staticToast(VerifyBlogActivity.this, "请选择需要审核的文章");
                return;
            }
            mPresenter.verifyBlog(ids, "PASS", "1", "", -1);
        });
        tvVerifyMain.setOnClickListener(v -> {
            String ids = getSelectedBlogIds();
            if (isEmpty(ids)) {
                Common.staticToast(VerifyBlogActivity.this, "请选择需要审核的文章");
                return;
            }
            mPresenter.verifyBlog(ids, "PASS", "", "", -1);
        });
        tvVerifyReject.setOnClickListener(v -> {
            CommentRejectedAct.startAct(this, "blogVerify", 1000);
        });
        super.initListener();
    }


    public void setSelectStatus(boolean b) {
        if (b) {
            mivSelect.setImageResource(R.mipmap.ic_fukuan_xuanzhong);
        } else {
            mivSelect.setImageResource(R.mipmap.ic_fukuan_moren);
        }
    }

    public void setSelectMode(boolean b) {
        if (b) {
            tvSelect.setText("取消");
            llSelect.setVisibility(View.VISIBLE);
            mivClose.setVisibility(View.GONE);
            isSelectAll = false;
            setSelectStatus(isSelectAll);
            mAdapter.setEdit(false);
            llVerify.setVisibility(View.VISIBLE);
        } else {
            tvSelect.setText("选择");
            llSelect.setVisibility(View.GONE);
            mivClose.setVisibility(View.VISIBLE);
            llVerify.setVisibility(View.GONE);
        }
    }

    @Override
    public void getBlogList(HotBlogsEntity hotBlogsEntity, int currentPage, int totalPage, int count) {
        if (currentPage == 1) {
            blogList.clear();
            if (isEmpty(hotBlogsEntity.list)) {
                neiEmpty.setVisibility(View.VISIBLE);
                recyclerList.setVisibility(View.GONE);
            } else {
                neiEmpty.setVisibility(View.GONE);
                recyclerList.setVisibility(View.VISIBLE);
            }
        }
        if (!isEmpty(hotBlogsEntity.list)) {
            for (BigImgEntity.Blog blog : hotBlogsEntity.list) {
                blog.status = 0;
            }
            blogList.addAll(hotBlogsEntity.list);
        }

        if (isEdit) {
            if (isSelectAll) {
                selectAllBlog(isSelectAll);
                tv_title.setText(String.format("已选择%s条内容", blogList.size()));
            }
        } else {
            tv_title.setText(String.format("共%s条内容需审核", count));
        }

        if (mAdapter == null) {
            mAdapter = new VerifyBlogAdapter(this, blogList);
            recyclerList.setAdapter(mAdapter);
            mAdapter.setOnItemCallBack(this);
        }
        mAdapter.setPageLoading(currentPage, totalPage);
        mAdapter.notifyDataSetChanged();
    }

    public String getSelectedBlogIds() {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < blogList.size(); i++) {
            if (blogList.get(i).isSelect) {
                buffer.append(blogList.get(i).id).append(",");
            }
        }
        if (buffer.length() > 0) {
            LogUtil.httpLogW("ids为:" + buffer.substring(0, buffer.length() - 1));
            return buffer.substring(0, buffer.length() - 1);
        }
        return null;
    }

    /**
     * 审核单个item
     *
     * @param position
     */
    @Override
    public void blogVerifyPass(int position, int count) {
        blogList.get(position).status = 1;
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
        tv_title.setText(String.format("共%s条内容需审核", count));
    }

    /**
     * 审核多个item
     */
    @Override
    public void blogVerifyPass(int count) {
        for (int i = 0; i < blogList.size(); i++) {
            if (blogList.get(i).isSelect) {
                blogList.get(i).status = 1;
            }
        }
        setSelectMode(false);
        mAdapter.setEdit(false);
        selectAllBlog(isSelectAll);
        tv_title.setText(String.format("共%s条内容需审核", count));
    }

    /**
     * @param position
     */
    @Override
    public void blogVerifyFail(int position, int count) {
        currentRejectPosition = -1;
        blogList.remove(position);
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
        tv_title.setText(String.format("共%s条内容需审核", count));
    }

    /**
     *
     */
    @Override
    public void blogVerifyFail(int count) {
        currentRejectPosition = -1;
        tv_title.setText(String.format("共%s条内容需审核", count));
        for (BigImgEntity.Blog blog : blogList) {
            if (blog.isSelect) {
                blogList.remove(blog);
            }
        }
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    /**
     * 撤回单条文章
     */
    @Override
    public void blogWithDraw(int position, int count) {
        blogList.get(position).status = 0;
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
        tv_title.setText(String.format("共%s条内容需审核", count));
    }

    @Override
    public void focusUser(int type, int position) {
        BigImgEntity.Blog blog = blogList.get(position);
        if (blog.is_focus == 0) {
            blog.is_focus = 1;
        } else {
            blog.is_focus = 0;
        }
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    /**
     * 设置所有item的选中状态
     *
     * @param selectAll
     */
    public void selectAllBlog(boolean selectAll) {
        for (int i = 0; i < blogList.size(); i++) {
            blogList.get(i).isSelect = selectAll;
        }
        mAdapter.notifyItemRangeChanged(0, blogList.size(), blogList);
    }

    /**
     * 检查所有item是否选中
     *
     * @return
     */
    public boolean checkAllBlogSelected() {
        for (int i = 0; i < blogList.size(); i++) {
            if (!blogList.get(i).isSelect) {
                return false;
            }
        }
        return true;
    }

    public void checkSelectItemCount() {
        int selectCount = 0;
        for (int i = 0; i < blogList.size(); i++) {
            if (blogList.get(i).isSelect) {
                selectCount++;
            }
        }
        tv_title.setText(String.format("已选择%s条内容", selectCount));
    }


    @Override
    public void refreshFinish() {
        if (layRefresh != null) {
            layRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    @Override
    public void toFocusUser(int focus, String memberId, String nickName, int position) {
        if (focus == 1) {
            if (promptDialog == null) {
                promptDialog = new PromptDialog(this);
                promptDialog.setTvSureBGColor(Color.WHITE);
                promptDialog.setTvSureColor(R.color.pink_color);
                promptDialog.setTvCancleIsBold(false);
                promptDialog.setTvSureIsBold(false);
            }
            promptDialog.setSureAndCancleListener(String.format(getStringResouce(R.string.ready_to_unFocus), nickName),
                    getStringResouce(R.string.unfollow), view -> {
                        mPresenter.focusUser(focus, memberId, position);
                        promptDialog.dismiss();
                    }, getStringResouce(R.string.give_up), view -> promptDialog.dismiss()
            ).show();
        } else {
            mPresenter.focusUser(focus, memberId, position);
        }
    }

    @Override
    public void OnItemSelect(int position, boolean isSelect) {
        if (isSelect) {
            boolean b = checkAllBlogSelected();
            if (b) {
                isSelectAll = true;
            }
        } else {
            isSelectAll = false;
        }
        setSelectStatus(isSelectAll);
        checkSelectItemCount();
    }

    @Override
    public void OnItemPassHot(int position) {
        BigImgEntity.Blog blog = blogList.get(position);
        mPresenter.verifyBlog(blog.id, "PASS", "1", "", position);
    }

    @Override
    public void OnItemPassMain(int position) {
        BigImgEntity.Blog blog = blogList.get(position);
        mPresenter.verifyBlog(blog.id, "PASS", "", "", position);
    }

    @Override
    public void OnItemReject(int position) {
        CommentRejectedAct.startAct(this, "blogVerify", 1000);
        currentRejectPosition = position;
    }

    @Override
    public void OnItemWithDraw(int position) {
        BigImgEntity.Blog blog = blogList.get(position);
        mPresenter.withDrawBlog(blog.id, position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 & resultCode == RESULT_OK) {
            String remark = data.getStringExtra("reason");
            if (currentRejectPosition != -1) {
                BigImgEntity.Blog blog = blogList.get(currentRejectPosition);
                mPresenter.verifyBlog(blog.id, "FAIL", "", remark, currentRejectPosition);
            } else {
                String ids = getSelectedBlogIds();
                if (isEmpty(ids)) {
                    Common.staticToast(VerifyBlogActivity.this, "请选择需要审核的文章");
                    return;
                }
                mPresenter.verifyBlog(ids, "FAIL", "", remark, -1);
            }
        }
    }
}
