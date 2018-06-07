package com.shunlian.app.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlian.app.R;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.bean.CommentListEntity;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.DeviceInfoUtil;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.GridSpacingItemDecoration;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.MyLinearLayout;
import com.shunlian.app.widget.MyRelativeLayout;
import com.shunlian.app.widget.MyTextView;
import com.shunlian.app.widget.circle.CircleImageView;
import com.shunlian.app.widget.empty.NetAndEmptyInterface;
import com.shunlian.app.widget.flowlayout.FlowLayout;
import com.shunlian.app.widget.flowlayout.TagAdapter;
import com.shunlian.app.widget.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/21.
 */

public class CommentAdapter extends BaseRecyclerAdapter<CommentListEntity.Data> {

    public static final int HEAD = 2;
    private List<CommentListEntity.Label> mLabel;
    private ICommentTypeListener mCommentTypeListener;
    private int selectId = 0;
    private View mHeadView;
    private ICommentPraiseListener praiseListener;

    public CommentAdapter(Context context, boolean isShowFooter, List<CommentListEntity.Data> lists) {
        super(context, isShowFooter, lists);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case HEAD:
                View head = LayoutInflater.from(context)
                        .inflate(R.layout.head_comment, parent, false);
                return new HeadHolder(head);
            default:
                return super.onCreateViewHolder(parent, viewType);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType){
            case HEAD:
                handlerHead(holder,position);
                break;
            default:
                super.onBindViewHolder(holder, position);
                break;
        }
    }

    private void handlerHead(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder){
            final HeadHolder mHolder = (HeadHolder) holder;
            if (mLabel == null){
                return;
            }
            final TagAdapter tagAdapter = new TagAdapter<CommentListEntity.Label>(mLabel) {

                @Override
                public View getView(FlowLayout parent, int position, CommentListEntity.Label s) {
                    MyTextView textView = (MyTextView) LayoutInflater.from(context)
                            .inflate(R.layout.comment_class, mHolder.gv_section, false);
                    GradientDrawable background = (GradientDrawable) textView.getBackground();
                    textView.setText(s.name+"("+s.count+")");
                    if (position == selectId){
                        textView.setTextColor(getColor(R.color.white));
                        background.setColor(getColor(R.color.pink_color));
                    }else {
                        textView.setTextColor(getColor(R.color.new_text));
                        background.setColor(getColor(R.color.value_FEF0F3));
                    }
                    return textView;
                }
            };
            mHolder.gv_section.setAdapter(tagAdapter);
            mHolder.gv_section.setOnTagClickListener((view,posi,parent)-> {
                selectId = posi;
                tagAdapter.notifyDataChanged();
                if (mCommentTypeListener != null){
                    CommentListEntity.Label label = mLabel.get(posi);
                    mCommentTypeListener.onCommentType(label.type);
                }
                return true;

            });
        }
    }
    /**
     * 子类需要实现的holder
     *
     * @param parent
     * @return
     */
    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return HEAD;
        }else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        if (isEmpty(mLabel)){
            if (isEmpty(lists)){
                return super.getItemCount() + 1;
            }else {
                return super.getItemCount();
            }
        }else {
            if (isEmpty(lists)){
                return super.getItemCount() + 2;
            }else {
                return super.getItemCount() + 1;
            }

        }
    }

    /**
     * 处理列表
     *
     * @param holder
     * @param position
     */
    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder){
            CommentHolder mHolder = (CommentHolder) holder;
            if (isEmpty(lists)){
                gone(mHolder.mll_item);
                visible(mHolder.nei_empty);
                mHolder.nei_empty.setImageResource(R.mipmap.img_empty_common)
                        .setText("目前还没有相关评价").setButtonText("");
                return;
            }else {
                visible(mHolder.mll_item);
                gone(mHolder.nei_empty);
            }
            final CommentListEntity.Data data = lists.get(position - 1);

            if (isEmpty(data.content)){
                mHolder.mtv_content.setVisibility(View.GONE);
            }else {
                mHolder.mtv_content.setVisibility(View.VISIBLE);
                mHolder.mtv_content.setText(data.content);
            }

            GlideUtils.getInstance().loadImage(context,mHolder.civ_head,data.avatar);
            Bitmap bitmap = TransformUtil.convertVIP(context, data.level);
            mHolder.miv_vip.setImageBitmap(bitmap);
            if ("1".equals(data.member_role)){//精英
                visible(mHolder.miv_medal);
                mHolder.miv_medal.setImageResource(R.mipmap.img_jingyingdaoshi);
            }else if ("2".equals(data.member_role)){//导师
                visible(mHolder.miv_medal);
                mHolder.miv_medal.setImageResource(R.mipmap.img_chuangkejingying);
            }else {
                gone(mHolder.miv_medal);
            }
            mHolder.mtv_nickname.setText(data.nickname);
            mHolder.mtv_time.setText(data.add_time);
            mHolder.mtv_attribute.setText(data.goods_option);
            mHolder.mtv_zan_count.setText(data.praise_total);
            mHolder.mtv_buy_time.setText("购买日期:"+data.buy_time);
            if (data.is_praise){
                mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_h);
                mHolder.mtv_zan_count.setTextColor(getColor(R.color.pink_color));
            }else {
                mHolder.miv_zan.setImageResource(R.mipmap.img_pingjia_zan_n);
                mHolder.mtv_zan_count.setTextColor(getColor(R.color.line_btn));
            }
            if (!isEmpty(data.append)){
                mHolder.mtv_content1.setVisibility(View.VISIBLE);
                mHolder.mtv_content1.setText(data.append);
            }else {
                mHolder.mtv_content1.setVisibility(View.GONE);
            }

            if (isEmpty(data.reply)){
                mHolder.mrl_reply.setVisibility(View.GONE);
            }else {
                mHolder.mrl_reply.setVisibility(View.VISIBLE);
                SpannableStringBuilder spannableStringBuilder = Common.changeColor("商家回复: "
                        + data.reply, "商家回复: ", getColor(R.color.pink_color));
                mHolder.mtv_reply.setText(spannableStringBuilder);
            }

            if (isEmpty(data.append_time)){
                mHolder.mtv_append_comment.setVisibility(View.GONE);
            }else {
                mHolder.mtv_append_comment.setVisibility(View.VISIBLE);
                mHolder.mtv_append_comment.setText(data.append_time);
            }


            final List<String> pics = data.pics;
            if (isEmpty(pics)){
                mHolder.recy_view.setVisibility(View.GONE);
            }else {
                mHolder.recy_view.setVisibility(View.VISIBLE);
                PicAdapter picAdapter = new PicAdapter(context,false,pics);
                mHolder.recy_view.setAdapter(picAdapter);
                picAdapter.setOnItemClickListener((v,pos)-> {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) pics;
                    bigImgEntity.index = pos;
                    bigImgEntity.desc = data.content;
                    LookBigImgAct.startAct(context, bigImgEntity);
                });
            }


            final List<String> append_pics = data.append_pics;
            if (isEmpty(append_pics)){
                mHolder.recy_view1.setVisibility(View.GONE);
            }else {
                mHolder.recy_view1.setVisibility(View.VISIBLE);
                PicAdapter picAdapter = new PicAdapter(context,false,append_pics);
                mHolder.recy_view1.setAdapter(picAdapter);
                picAdapter.setOnItemClickListener((v,pos)-> {
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList) append_pics;
                    bigImgEntity.index = pos;
                    bigImgEntity.desc = data.append;
                    LookBigImgAct.startAct(context, bigImgEntity);
                });
            }

            visible(mHolder.mtv_comment_state);
            if ("1".equals(data.star_level)){//1差评，3中评，5好评
                mHolder.mtv_comment_state.setText("差评");
            }else if ("3".equals(data.star_level)){
                mHolder.mtv_comment_state.setText("中评");
            }else if ("5".equals(data.star_level)){
                mHolder.mtv_comment_state.setText("好评");
            }else {
                gone(mHolder.mtv_comment_state);
            }
        }
    }

    public void setLabel(List<CommentListEntity.Label> label,boolean isClear) {
        mLabel = label;
        if (isClear){
            selectId = 0;
        }
    }

    public void setPraiseTotal(String praiseTotal, int praisePosition) {
        CommentListEntity.Data data = lists.get(praisePosition - 1);
        data.praise_total = praiseTotal;
        data.is_praise = true;
        notifyItemChanged(praisePosition);
    }

    public class CommentHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.civ_head)
        CircleImageView civ_head;

        @BindView(R.id.mtv_nickname)
        MyTextView mtv_nickname;

        @BindView(R.id.miv_vip)
        MyImageView miv_vip;

        @BindView(R.id.mtv_time)
        MyTextView mtv_time;

        @BindView(R.id.mtv_content)
        MyTextView mtv_content;

        @BindView(R.id.mtv_content1)
        MyTextView mtv_content1;

        @BindView(R.id.mtv_attribute)
        MyTextView mtv_attribute;

        @BindView(R.id.mtv_buy_time)
        MyTextView mtv_buy_time;

        @BindView(R.id.mtv_zan_count)
        MyTextView mtv_zan_count;

        @BindView(R.id.miv_zan)
        MyImageView miv_zan;

        @BindView(R.id.mtv_append_comment)
        MyTextView mtv_append_comment;

        @BindView(R.id.mrl_reply)
        MyRelativeLayout mrl_reply;

        @BindView(R.id.mtv_reply)
        MyTextView mtv_reply;

        @BindView(R.id.recy_view)
        RecyclerView recy_view;

        @BindView(R.id.recy_view1)
        RecyclerView recy_view1;

        @BindView(R.id.miv_medal)
        MyImageView miv_medal;

        @BindView(R.id.mll_item)
        MyLinearLayout mll_item;

        @BindView(R.id.nei_empty)
        NetAndEmptyInterface nei_empty;

        @BindView(R.id.mtv_comment_state)
        MyTextView mtv_comment_state;

        public CommentHolder(View itemView) {
            super(itemView);
            miv_vip.setWHProportion(23,23);
            int px = TransformUtil.dip2px(context, 20);
            TransformUtil.expandViewTouchDelegate(miv_zan,px,px,px,px);
            GridLayoutManager manager = new GridLayoutManager(context,3);
            recy_view.setLayoutManager(manager);
            recy_view.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context,5),false));


            GridLayoutManager manager1 = new GridLayoutManager(context,3);
            recy_view1.addItemDecoration(new GridSpacingItemDecoration(TransformUtil.dip2px(context,5),false));
            recy_view1.setLayoutManager(manager1);

            mHeadView.post(()-> {
                int measuredHeight = mHeadView.getMeasuredHeight();
                GoodsDetailAct act = (GoodsDetailAct) context;
                ViewGroup.LayoutParams layoutParams = nei_empty.getLayoutParams();
                layoutParams.height = DeviceInfoUtil.getDeviceHeight(context) - measuredHeight - act.bottomListHeight;
                nei_empty.setLayoutParams(layoutParams);
            });

        }

        @OnClick(R.id.miv_zan)
        public void commentPraise(){
            if (praiseListener != null){
                CommentListEntity.Data data = lists.get(getAdapterPosition() - 1);
                praiseListener.onCommentPraise(data.comment_id,getAdapterPosition());
            }
        }
    }

    public class HeadHolder extends BaseRecyclerViewHolder{

        @BindView(R.id.gv_section)
        TagFlowLayout gv_section;

        public HeadHolder(View itemView) {
            super(itemView);
            mHeadView = itemView;
        }
    }

    /**
     * 评论类型监听
     * @param listener
     */
    public void setCommentTypeListener(ICommentTypeListener listener){
        mCommentTypeListener = listener;
    }

    /**
     * 评论点赞
     * @param praiseListener
     */
    public void setCommentPraiseListener(ICommentPraiseListener praiseListener){
        this.praiseListener = praiseListener;
    }

    public interface ICommentPraiseListener{
        void onCommentPraise(String comment_id,int position);
    }

    public interface ICommentTypeListener{
        void onCommentType(String type);
    }
}
