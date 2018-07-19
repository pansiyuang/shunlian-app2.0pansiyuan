package com.shunlian.app.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.DiscoveryMaterialEntity;
import com.shunlian.app.presenter.PADiscoverSucaiku;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DownLoadImageThread;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.PromptDialog;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.view.IADiscoverSucaiku;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/8/21.
 */

public class DiscoverSucaikuAdapter extends BaseRecyclerAdapter<DiscoveryMaterialEntity.Content> implements IADiscoverSucaiku {
    private PADiscoverSucaiku paDiscoverSucaiku;

    public DiscoverSucaikuAdapter(Context context, boolean isShowFooter, List<DiscoveryMaterialEntity.Content> list) {
        super(context, isShowFooter, list);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return new SucaikuHolder(LayoutInflater.from(context).inflate(R.layout.item_discover_sucaiku, parent, false));
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, final int position) {
        final SucaikuHolder viewHolder = (SucaikuHolder) holder;
        final DiscoveryMaterialEntity.Content content = lists.get(position);
        SpannableStringBuilder titleBuilder = Common.changeColor(content.add_time + content.content, content.add_time, getColor(R.color.value_299FFA));
        viewHolder.mtv_find_title.setText(titleBuilder);
        viewHolder.zan = Integer.parseInt(content.praise_num);
        viewHolder.mtv_zan.setText(content.praise_num);
        if ("1".equals(content.praise)) {
            viewHolder.isZan = true;
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_sucai_zan_h);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.pink_color));
        } else {
            viewHolder.isZan = false;
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_sucai_zan_n);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.value_BDBDBD));
        }
        viewHolder.miv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paDiscoverSucaiku == null)
                    paDiscoverSucaiku = new PADiscoverSucaiku(context, DiscoverSucaikuAdapter.this);
                if (viewHolder.isZan) {
                    paDiscoverSucaiku.dianZan(content.id, "2", viewHolder);
                } else {
                    paDiscoverSucaiku.dianZan(content.id, "1", viewHolder);
                }
            }
        });

        if (content.image == null || content.image.size() == 0) {
            viewHolder.rv_pics.setVisibility(View.GONE);
            viewHolder.miv_pic.setVisibility(View.GONE);
        } else {
            if (content.image.size() == 1) {
                viewHolder.rv_pics.setVisibility(View.GONE);
                viewHolder.miv_pic.setVisibility(View.VISIBLE);
                GlideUtils.getInstance().loadImageShu(context, viewHolder.miv_pic, content.image.get(0));
                viewHolder.miv_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //点击查看大图
                        BigImgEntity bigImgEntity = new BigImgEntity();
                        bigImgEntity.itemList = (ArrayList<String>) content.image;
                        bigImgEntity.index = 0;
                        bigImgEntity.items = new ArrayList<>();
                        bigImgEntity.items.add(getString(R.string.operate_baocuntupian));
                        bigImgEntity.items.add(getString(R.string.pic_text_share));
                        bigImgEntity.items.add(getString(R.string.errcode_cancel));
                        LookBigImgAct.startAct(context, bigImgEntity, new LookBigImgAct.MyCallBack() {

                            @Override
                            public void share(Context context) {
                                super.share(context);
                                DownLoadImageThread thread = new DownLoadImageThread(context,
                                        (ArrayList<String>) content.image);
                                thread.start();
                                ClipboardManager cm = (ClipboardManager) context
                                        .getSystemService(Context.CLIPBOARD_SERVICE);
                                cm.setText(content.content);

                                if (viewHolder.promptDialogs == null) {
                                    viewHolder.promptDialogs = new PromptDialog((Activity) context);
                                    viewHolder.promptDialogs.setSureAndCancleListener(getString(R.string.discover_wenzifuzhi),
                                            getString(R.string.discover_tupianbaocun), "", getString(R.string.discover_quweixinfenxiang), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Common.openWeiXin(context, "material", content.id);
                                                    viewHolder.promptDialogs.dismiss();
                                                }
                                            }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    viewHolder.promptDialogs.dismiss();
                                                }
                                            }).show();
                                } else {
                                    viewHolder.promptDialogs.show();
                                }                            }
                        });
                    }
                });
            } else {
                viewHolder.miv_pic.setVisibility(View.GONE);
                viewHolder.rv_pics.setVisibility(View.VISIBLE);
                if (viewHolder.picAdapter == null) {//此处不能复用，不能用notify，因为content.image复用导致布局混乱
                    viewHolder.picAdapter = new SinglePicAdapter(context, false, content.image);
                    viewHolder.rv_pics.setLayoutManager(new GridLayoutManager(context, 3));
                    viewHolder.rv_pics.setNestedScrollingEnabled(false);
                    viewHolder.rv_pics.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context, 9), false, 0, false));
                    viewHolder.rv_pics.setAdapter(viewHolder.picAdapter);
                } else {
                    viewHolder.picAdapter = new SinglePicAdapter(context, false, content.image);
                    viewHolder.rv_pics.setAdapter(viewHolder.picAdapter);
//                    viewHolder.picAdapter.notifyDataSetChanged();
                }
                viewHolder.picAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //点击查看大图
                        BigImgEntity bigImgEntity = new BigImgEntity();
                        bigImgEntity.itemList = (ArrayList<String>) content.image;
                        bigImgEntity.index = position;
                        LookBigImgAct.startAct(context, bigImgEntity);
                    }
                });
            }
        }
        viewHolder.mtv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownLoadImageThread thread = new DownLoadImageThread(context,
                        (ArrayList<String>) content.image);
                thread.start();
                ClipboardManager cm = (ClipboardManager) context
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(content.content);

                if (viewHolder.promptDialog == null) {
                    viewHolder.promptDialog = new PromptDialog((Activity) context);
                    viewHolder.promptDialog.setSureAndCancleListener(getString(R.string.discover_wenzifuzhi),
                            getString(R.string.discover_tupianbaocun), "", getString(R.string.discover_quweixinfenxiang), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Common.openWeiXin(context, "material", content.id);
                                    viewHolder.promptDialog.dismiss();
                                }
                            }, getString(R.string.errcode_cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    viewHolder.promptDialog.dismiss();
                                }
                            }).show();
                } else {
                    viewHolder.promptDialog.show();
                }
            }
        });
    }

    @Override
    public void dianZan(SucaikuHolder viewHolder) {
        if (viewHolder.isZan) {
            viewHolder.isZan = false;
            viewHolder.zan = viewHolder.zan - 1;
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_sucai_zan_n);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.value_BDBDBD));
        } else {
            viewHolder.isZan = true;
            viewHolder.zan = viewHolder.zan + 1;
            viewHolder.miv_zan.setImageResource(R.mipmap.icon_sucai_zan_h);
            viewHolder.mtv_zan.setTextColor(getColor(R.color.pink_color));
        }
        viewHolder.mtv_zan.setText(String.valueOf(viewHolder.zan));
    }

    @Override
    public void dianZans(DiscoverNewAdapter.NewHolder holder) {

    }

    @Override
    public void dianZanss(TieziCommentAdapter.CommentHolder holder) {

    }

    @Override
    public void showFailureView(int request_code) {

    }

    @Override
    public void showDataEmptyView(int request_code) {

    }


    public class SucaikuHolder extends BaseRecyclerViewHolder {

        @BindView(R.id.mtv_find_title)
        MyTextView mtv_find_title;

        @BindView(R.id.mtv_share)
        MyTextView mtv_share;

        @BindView(R.id.mtv_zan)
        MyTextView mtv_zan;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.miv_pic)
        MyImageView miv_pic;

        @BindView(R.id.rv_pics)
        RecyclerView rv_pics;

        private SinglePicAdapter picAdapter;
        private boolean isZan = false;
        private int zan = 0;
        private PromptDialog promptDialog,promptDialogs;

        public SucaikuHolder(View itemView) {
            super(itemView);
        }

    }

}
