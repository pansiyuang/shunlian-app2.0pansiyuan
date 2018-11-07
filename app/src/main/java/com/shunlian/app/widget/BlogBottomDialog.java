package com.shunlian.app.widget;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.presenter.FavoBlogPresenter;
import com.shunlian.app.ui.discover_new.MyPageActivity;
import com.shunlian.app.view.IFavBlogView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/10/26.
 */

public class BlogBottomDialog extends Dialog implements BaseRecyclerAdapter.OnItemClickListener, IFavBlogView {

    @BindView(R.id.recycler_list)
    RecyclerView recycler_list;

    private Unbinder bind;
    private LinearLayoutManager manager;
    private List<String> stringList;
    private SingTextAdapter mAdapter;
    private BigImgEntity.Blog currentBlog;
    private FavoBlogPresenter mPresenter;
    private OnDialogCallBack mCallBack;

    public BlogBottomDialog(Context context) {
        this(context, R.style.popAd);
        initView();
    }

    public BlogBottomDialog(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        //设置当前dialog宽高
        Window win = getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_bottom_dialog, null, false);
        setContentView(view);
        bind = ButterKnife.bind(this, view);

        manager = new LinearLayoutManager(getContext());
        recycler_list.setLayoutManager(manager);
        stringList = new ArrayList<>();
    }

    public void setBlog(BigImgEntity.Blog blog) {
        if (blog == null) {
            return;
        }
        currentBlog = blog;
        stringList.clear();
        if (blog.is_self == 1) {
            stringList.add("删除");
        } else {
            switch (blog.is_favo) {
                case 0:
                    stringList.add("添加收藏");
                    break;
                case 1:
                    stringList.add("取消收藏");
                    break;
            }
            stringList.add("他的主页");
        }
        stringList.add("取消");
        if (mAdapter == null) {
            mAdapter = new SingTextAdapter(getContext(), stringList);
            recycler_list.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(this);
        }
        mAdapter.notifyDataSetChanged();
    }

    public void toDelBlog(String blogId) {
        if (mPresenter == null) {
            mPresenter = new FavoBlogPresenter(getContext(), this);
        }
        mPresenter.removeBlog(blogId);
    }

    public void toFavoBlog(int isFavo, String blogId) {
        if (mPresenter == null) {
            mPresenter = new FavoBlogPresenter(getContext(), this);
        }
        mPresenter.FavoBlog(isFavo, blogId);
    }

    public void destory() {
        if (isShowing()) {
            dismiss();
        }
        if (bind != null) {
            bind.unbind();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String s = stringList.get(position);
        if ("取消".equals(s) && isShowing()) {
            dismiss();
            return;
        }
        switch (stringList.get(position)) {
            case "添加收藏":
            case "取消收藏":
                toFavoBlog(currentBlog.is_favo, currentBlog.id);
                dismiss();
                break;
            case "删除":
                toDelBlog(currentBlog.id);
                dismiss();
                break;
            case "他的主页":
                MyPageActivity.startAct(getContext(), currentBlog.member_id);
                dismiss();
                break;
        }
    }

    @Override
    public void favoBlog(int type, String blogId) {
        if (mCallBack != null) {
            if (type == 0) {
                mCallBack.addFavo(1, blogId);
            } else {
                mCallBack.addFavo(0, blogId);
            }
        }
    }

    @Override
    public void removeBlog(String blogId) {
        if (mCallBack != null) {
            mCallBack.onDel(blogId);
        }
    }


    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }

    public class SingTextAdapter extends BaseRecyclerAdapter<String> {

        public SingTextAdapter(Context context, List<String> lists) {
            super(context, false, lists);
        }

        @Override
        protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
            return new SingleViewHolder(LayoutInflater.from(context).inflate(R.layout.item_botton_dialog, parent, false));
        }

        @Override
        public void handleList(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SingleViewHolder) {
                SingleViewHolder viewHolder = (SingleViewHolder) holder;
                String string = lists.get(position);
                viewHolder.tv_item.setText(string);
            }
        }

        public class SingleViewHolder extends BaseRecyclerViewHolder {

            @BindView(R.id.tv_item)
            TextView tv_item;

            public SingleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public void setOnDialogCallBack(OnDialogCallBack callBack) {
        this.mCallBack = callBack;
    }

    public interface OnDialogCallBack {
        void addFavo(int favo, String blogId);

        void onDel(String blogId);
    }
}
