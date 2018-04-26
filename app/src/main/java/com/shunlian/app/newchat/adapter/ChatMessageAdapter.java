package com.shunlian.app.newchat.adapter;


import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.adapter.BaseRecyclerAdapter;
import com.shunlian.app.bean.BigImgEntity;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ChatGoodsEntity;
import com.shunlian.app.newchat.entity.EvaluateMessage;
import com.shunlian.app.newchat.entity.GoodsMessage;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.OrderMessage;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.newchat.ui.ChatActivity;
import com.shunlian.app.newchat.websocket.MessageStatus;
import com.shunlian.app.ui.goods_detail.GoodsDetailAct;
import com.shunlian.app.ui.my_comment.LookBigImgAct;
import com.shunlian.app.utils.CenterAlignImageSpan;
import com.shunlian.app.utils.EmojisUtils;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.utils.LogUtil;
import com.shunlian.app.utils.TransformUtil;
import com.shunlian.app.widget.MyImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

import static com.shunlian.app.newchat.util.TimeUtil.getNewChatTime;
import static com.shunlian.app.utils.BitmapUtil.MAX_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MAX_WIDTH;
import static com.shunlian.app.utils.BitmapUtil.MIN_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MIN_WIDTH;

/**
 * Created by Administrator on 2018/4/10.
 */

public class ChatMessageAdapter extends BaseRecyclerAdapter<MsgInfo> {

    public static final int LEFT_TXT = 1;
    public static final int RIGHT_TXT = 2;
    public static final int LEFT_IMG = 3;
    public static final int RIGHT_IMG = 4;
    public static final int RIGHT_GOODS = 5;
    public static final int LINK_MSG = 6;
    public static final int EVALUATE_MSG = 7;
    public static final int ORDER_MSG = 8;
    public static final int SYS_MSG = 9;

    private final ObjectMapper objectMapper;
    private RecyclerView recycler;
    private AssetManager am;

    public ChatMessageAdapter(Context context, List<MsgInfo> lists, RecyclerView recyclerView) {
        super(context, false, lists);
        objectMapper = new ObjectMapper();
        recycler = recyclerView;
        am = context.getAssets();
    }

    public void addMsgInfo(MsgInfo msgInfo) {
        BaseMessage message;
        try {
            message = objectMapper.readValue(msgInfo.getMessage(), BaseMessage.class);
            if (message.getSendType() != BaseMessage.VALUE_SYSTEM) {
                addTimeMessage(msgInfo.getSend_time(), getLastMessageTime(false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lists.add(resizeImg(msgInfo));
        notifyItemRangeChanged(0, lists.size());
        recycler.scrollToPosition(getItemCount() - 1); //滚动到最底部
    }

    @Override
    public int getItemViewType(int position) {
        String message = lists.get(position).getMessage();
        BaseMessage baseMessage = str2Msg(message);
        int sendType = baseMessage.getSendType();
        if (sendType == BaseMessage.VALUE_LEFT) { //左边消息
            switch (baseMessage.msg_type) {
                case "text":
                    return LEFT_TXT;
                case "image":
                    return LEFT_IMG;
            }
        } else if (sendType == BaseMessage.VALUE_RIGHT) {//右边消息
            switch (baseMessage.msg_type) {
                case "text":
                    return RIGHT_TXT;
                case "image":
                    return RIGHT_IMG;
                case "goods":
                    return RIGHT_GOODS;
                case "evaluate":
                    return EVALUATE_MSG;
                case "order":
                    return ORDER_MSG;
            }
        } else { //系统消息
            return SYS_MSG;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LEFT_TXT:
                return new LeftTxtViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_txt, parent, false));
            case RIGHT_TXT:
                return new RightTxtViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_right_txt, parent, false));
            case LEFT_IMG:
                return new LeftImgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_left_img, parent, false));
            case RIGHT_IMG:
                return new RightImgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_right_img, parent, false));
            case RIGHT_GOODS:
                return new RightGoodsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_goods, parent, false));
            case EVALUATE_MSG:
                return new EvaluateViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_evaluate, parent, false));
            case ORDER_MSG:
                return new OrderViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_order, parent, false));
            case LINK_MSG:
                return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_link, parent, false));
            case SYS_MSG:
                return new SysMsgViewHolder(LayoutInflater.from(context).inflate(R.layout.item_chat_msg_system, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected RecyclerView.ViewHolder getRecyclerHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void handleList(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        MsgInfo item = lists.get(position);
        String message = item.getMessage();
        BaseMessage baseMessage = str2Msg(message);
        switch (viewType) {
            case LEFT_TXT:
                handLeftTxt(holder, baseMessage);
                break;
            case RIGHT_TXT:
                handRightTxt(holder, baseMessage);
                break;
            case LEFT_IMG:
                handLeftImg(holder, baseMessage);
                break;
            case RIGHT_IMG:
                handRightImg(holder, baseMessage);
                break;
            case RIGHT_GOODS:
                handRightGoods(holder, baseMessage);
                break;
            case EVALUATE_MSG:
                handEvaluate(holder, baseMessage);
                break;
            case ORDER_MSG:
                handOrder(holder, baseMessage);
                break;
            case LINK_MSG:
                handLink(holder, baseMessage);
                break;
            case SYS_MSG:
                handSysMsg(holder, baseMessage);
                break;
        }
    }

    public void handLeftTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TextMessage textMessage = (TextMessage) baseMessage;
        LeftTxtViewHolder leftTxtViewHolder = (LeftTxtViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, leftTxtViewHolder.miv_icon, textMessage.from_headurl, 3);
        leftTxtViewHolder.tv_content.setText(getEmotionContent(textMessage.msg_body));
    }

    public void handRightTxt(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        TextMessage textMessage = (TextMessage) baseMessage;
        RightTxtViewHolder rightTxtViewHolder = (RightTxtViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, rightTxtViewHolder.miv_icon, textMessage.from_headurl, 3);
        rightTxtViewHolder.tv_content.setText(getEmotionContent(textMessage.msg_body));

        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightTxtViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightTxtViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightTxtViewHolder.miv_status_error.setVisibility(View.GONE);
            rightTxtViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightTxtViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handLeftImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handRightImg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        ImageMessage imageMessag = (ImageMessage) baseMessage;
        RightImgViewHolder rightImgViewHolder = (RightImgViewHolder) holder;
        GlideUtils.getInstance().loadCornerImage(context, rightImgViewHolder.miv_icon, imageMessag.from_headurl, 3);
        final String current_small_url, current_bigl_url;
        if (imageMessag.msg_body != null) {
            setImg(rightImgViewHolder.miv_img, imageMessag.msg_body);
            if (TextUtils.isEmpty(imageMessag.msg_body.localUrl)) {
                String imagehost = imageMessag.msg_body.img_host;
                if (!imagehost.startsWith("https")) {
                    imagehost = "https://" + imagehost;
                }
                current_small_url = imagehost + imageMessag.msg_body.img_small;
                GlideUtils.getInstance().loadImage(context, rightImgViewHolder.miv_img, current_small_url);
            } else {
                current_bigl_url = "file://" + imageMessag.msg_body.localUrl;
                GlideUtils.getInstance().loadImage(context, rightImgViewHolder.miv_img, current_bigl_url);
            }
            rightImgViewHolder.miv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> imgs = new ArrayList<>();
                    imgs.clear();

                    if (isEmpty(imageMessag.msg_body.localUrl)) {
                        if (!imageMessag.msg_body.img_host.startsWith("https")) {
                            imgs.add("https://" + imageMessag.msg_body.img_small);
                        } else {
                            imgs.add(imageMessag.msg_body.img_small);
                        }
                    } else {
                        imgs.add("file://" + imageMessag.msg_body.localUrl);
                    }
                    //查看大图
                    BigImgEntity bigImgEntity = new BigImgEntity();
                    bigImgEntity.itemList = (ArrayList<String>) imgs;
                    bigImgEntity.index = 0;
                    LookBigImgAct.startAct(context, bigImgEntity);
                }
            });
        }
        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightImgViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightImgViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightImgViewHolder.miv_status_error.setVisibility(View.GONE);
            rightImgViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightImgViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handRightGoods(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        RightGoodsViewHolder rightGoodsViewHolder = (RightGoodsViewHolder) holder;
        GoodsMessage goodsMessage = (GoodsMessage) baseMessage;
        GlideUtils.getInstance().loadCornerImage(context, rightGoodsViewHolder.miv_icon, goodsMessage.from_headurl, 3);
        if (goodsMessage.msg_body != null) {
            GoodsMessage.GoodsBody goodsBody = goodsMessage.msg_body;
            GlideUtils.getInstance().loadImage(context, rightGoodsViewHolder.miv_good_img, goodsBody.goodsImage);
            rightGoodsViewHolder.tv_goods_title.setText(goodsBody.title);
            rightGoodsViewHolder.tv_goods_price.setText(goodsBody.price);

            rightGoodsViewHolder.layout_goods.setOnClickListener(v -> {
                // TODO: 2018/4/13 点击无法跳转
                GoodsDetailAct.startAct(context, goodsBody.goodsId);
            });
        }

        if (baseMessage.getStatus() == MessageStatus.SendFail) {
            rightGoodsViewHolder.miv_status_error.setVisibility(View.VISIBLE);
            rightGoodsViewHolder.pb_right.setVisibility(View.GONE);
        } else if (baseMessage.getStatus() == MessageStatus.Sending) {
            rightGoodsViewHolder.miv_status_error.setVisibility(View.GONE);
            rightGoodsViewHolder.pb_right.setVisibility(View.VISIBLE);
        } else {
            rightGoodsViewHolder.rl_msg_status.setVisibility(View.GONE);
        }
    }

    public void handEvaluate(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        EvaluateMessage evaluateMessage = (EvaluateMessage) baseMessage;
        EvaluateViewHolder evaluateViewHolder = (EvaluateViewHolder) holder;
        EvaluateMessage.EvaluateMessageBody evaluateMessageBody;
        if (evaluateMessage.msg_body != null) {
            evaluateMessageBody = evaluateMessage.msg_body;
            initEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);

            evaluateViewHolder.tv_very_unsatisfy.setOnClickListener(v -> {
                evaluateMessageBody.score = 1;
                updateEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);
            });
            evaluateViewHolder.tv_unsatisfy.setOnClickListener(v -> {
                evaluateMessageBody.score = 2;
                updateEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);
            });
            evaluateViewHolder.tv_commonly.setOnClickListener(v -> {
                evaluateMessageBody.score = 3;
                updateEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);
            });
            evaluateViewHolder.tv_satisfy.setOnClickListener(v -> {
                evaluateMessageBody.score = 4;
                updateEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);
            });
            evaluateViewHolder.tv_very_satisfy.setOnClickListener(v -> {
                evaluateMessageBody.score = 5;
                updateEvaluteStatus(evaluateMessageBody.score, evaluateViewHolder);
            });
            evaluateViewHolder.tv_comment_status.setOnClickListener(v -> {
                ((ChatActivity) context).createEvalute(evaluateMessage);
            });
        }
    }

    public void handOrder(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        OrderMessage orderMessage = (OrderMessage) baseMessage;

        if (orderMessage.msg_body != null) {
            OrderMessage.OrderMessageBody orderMessageBody = orderMessage.msg_body;
            if (!isEmpty(orderMessageBody.orderGoods)) {
            }
        }
    }

    public void handLink(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {

    }

    public void handSysMsg(RecyclerView.ViewHolder holder, BaseMessage baseMessage) {
        SysMsgViewHolder sysMsgViewHolder = (SysMsgViewHolder) holder;
        TextMessage msgText = (TextMessage) baseMessage;
        long currentTime = Long.valueOf(msgText.msg_body);
        sysMsgViewHolder.tv_systemMessage.setText(getNewChatTime(currentTime));
    }

    public class LeftTxtViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

        public LeftTxtViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightTxtViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;

        public RightTxtViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LeftImgViewHolder extends BaseRecyclerViewHolder {

        public LeftImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightImgViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.miv_img)
        MyImageView miv_img;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;


        public RightImgViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class RightGoodsViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.layout_goods)
        RelativeLayout layout_goods;

        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;

        @BindView(R.id.tv_goods_title)
        TextView tv_goods_title;

        @BindView(R.id.miv_good_img)
        MyImageView miv_good_img;

        @BindView(R.id.miv_icon)
        MyImageView miv_icon;

        @BindView(R.id.rl_msg_status)
        RelativeLayout rl_msg_status;

        @BindView(R.id.miv_status_error)
        MyImageView miv_status_error;

        @BindView(R.id.pb_right)
        ProgressBar pb_right;

        public RightGoodsViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class EvaluateViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_very_unsatisfy)
        TextView tv_very_unsatisfy;

        @BindView(R.id.tv_unsatisfy)
        TextView tv_unsatisfy;

        @BindView(R.id.tv_commonly)
        TextView tv_commonly;

        @BindView(R.id.tv_satisfy)
        TextView tv_satisfy;

        @BindView(R.id.tv_very_satisfy)
        TextView tv_very_satisfy;

        @BindView(R.id.tv_comment_status)
        TextView tv_comment_status;

        public EvaluateViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class OrderViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_order_number)
        TextView tv_order_number;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_price)
        TextView tv_price;

        @BindView(R.id.tv_seller)
        TextView tv_seller;

        @BindView(R.id.tv_seller_id)
        TextView tv_seller_id;

        @BindView(R.id.tv_express_number)
        TextView tv_express_number;

        public OrderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class LinkViewHolder extends BaseRecyclerViewHolder {

        public LinkViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class SysMsgViewHolder extends BaseRecyclerViewHolder {
        @BindView(R.id.tv_systemMessage)
        TextView tv_systemMessage;

        public SysMsgViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void setImg(ImageView iv, ImageMessage.ImageBody imageBody) {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) iv.getLayoutParams();
        lp.width = imageBody.img_width;
        lp.height = imageBody.img_height;
        if (imageBody.img_width == 0 || imageBody.img_height == 0) {
            return;
        }
        iv.setLayoutParams(lp);
    }

    public BaseMessage str2Msg(String msg) {
        BaseMessage message = null;
        try {
            message = objectMapper.readValue(msg, BaseMessage.class);
            switch (message.msg_type) {
                case "text":
                case "system":
                case "sys_text":
                    message = objectMapper.readValue(msg, TextMessage.class);
                    break;
                case "image":
                    message = objectMapper.readValue(msg, ImageMessage.class);
                    break;
                case "link":
                case "sys_link":
                    message = objectMapper.readValue(msg, LinkMessage.class);
                    break;
                case "goods":
                    message = objectMapper.readValue(msg, GoodsMessage.class);
                    break;
                case "evaluate":
                    message = objectMapper.readValue(msg, EvaluateMessage.class);
                    break;
                case "order":
                    message = objectMapper.readValue(msg, OrderMessage.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String msg2Str(BaseMessage message) {
        String str = null;
        try {
            str = objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private void addTimeMessage(long sendTime, long lastmsgTime) {
        addTimeMessage(false, sendTime, lastmsgTime);
    }

    /**
     * @param isPre
     * @param sendTime    发送时间
     * @param lastmsgTime 上一条消息发送时间
     */
    private void addTimeMessage(boolean isPre, long sendTime, long lastmsgTime) {
        if (isPre && lastmsgTime - sendTime <= 30 && sendTime != 0) {
            return;
        }
        if (!isPre && sendTime - lastmsgTime <= 30 && lastmsgTime != 0) {
            return;
        }
        MsgInfo info = new MsgInfo();
        TextMessage textMessage = new TextMessage();
        textMessage.setSendType(BaseMessage.VALUE_SYSTEM);
        textMessage.msg_body = String.valueOf(sendTime);
        textMessage.msg_type = "system";
        try {
            info.setMessage(objectMapper.writeValueAsString(textMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isPre) {
            lists.add(0, info);
        } else {
            lists.add(info);
        }
    }

    private long getLastMessageTime(boolean isPre) {
        long sendTime = 0;
        if (isEmpty(lists)) {
            return sendTime;
        } else {
            if (isPre) {
                sendTime = lists.get(0).send_time;
            } else {
                sendTime = lists.get(lists.size() - 1).send_time;
            }
            return sendTime;
        }
    }

    private ImageMessage resizeImg(ImageMessage imageMessage) {
        int currentWidth, currentHeight;//图片的新宽高
        float width, height;//图片的宽高
        double ratio;  //图片的比例

        if (imageMessage.msg_body == null) {
            return imageMessage;
        }
        width = Float.valueOf(imageMessage.msg_body.img_width);
        height = Float.valueOf(imageMessage.msg_body.img_height);
        if (width > MAX_WIDTH && height <= MAX_WIDTH) {//假如宽度大于最大宽度
            ratio = MAX_WIDTH / width;
            currentWidth = MAX_WIDTH;
            if (height < MIN_WIDTH) { //假如宽度小于最大宽度则设置为最小宽度
                currentHeight = MIN_WIDTH;
            } else {
                currentHeight = (int) (height / ratio);
            }
        } else if (height > MAX_HEIGHT && width <= MAX_WIDTH) {//假如高度大于最大宽度
            ratio = height / MAX_WIDTH;
            currentHeight = MAX_HEIGHT;
            if (width < MIN_WIDTH) { //假如高度小于最大高度则设置为最小高度
                currentWidth = MIN_WIDTH;
            } else {
                currentWidth = (int) (width / ratio);
            }
        } else if (width <= MAX_WIDTH && height <= MAX_HEIGHT && width >= MIN_WIDTH && height >= MIN_HEIGHT) {
            //都小于等于最大值 不改变比例
            currentWidth = (int) width;
            currentHeight = (int) height;
        } else if (width >= MAX_WIDTH && height >= MAX_HEIGHT) {
            if (width > height) {
                ratio = width / MAX_WIDTH;
                currentWidth = MAX_WIDTH;
                currentHeight = (int) (height / ratio);
            } else {
                ratio = height / MAX_WIDTH;
                currentHeight = MAX_WIDTH;
                currentWidth = (int) (width / ratio);
            }
        } else {
            //都小于最小值 设为最小值
            currentWidth = MIN_WIDTH;
            currentHeight = MIN_HEIGHT;
        }
        imageMessage.msg_body.img_height = currentHeight;
        imageMessage.msg_body.img_width = currentWidth;
        return imageMessage;
    }

    private MsgInfo resizeImg(MsgInfo info) {
        MsgInfo m = info;
        String message = info.getMessage();
        BaseMessage baseMessage = str2Msg(message);
        if ("image".equals(baseMessage.msg_type)) {
            ImageMessage imageMessage = (ImageMessage) baseMessage;
            imageMessage = resizeImg(imageMessage);
            m.setMessage(msg2Str(imageMessage));
        }
        return m;
    }

    public SpannableString getEmotionContent(String source) {
        int position = -1;
        SpannableString spannableString = new SpannableString(source);

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String value = matcherEmotion.group();
            Map<Integer, String> emojiMap = EmojisUtils.emojismap();
            if (emojiMap.containsValue(value)) {
                for (int key : emojiMap.keySet()) {
                    if (emojiMap.get(key).equals(value)) {
                        position = key;
                    }
                }
            }
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            InputStream is = null;
            try {
                is = am.open(String.format("emojis/%d.png", position));
                // 压缩表情图片
                int size = TransformUtil.dip2px(context, 28);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                int end = start + value.length();
                CenterAlignImageSpan span = new CenterAlignImageSpan(scaleBitmap);
                spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                        is = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return spannableString;
    }

    public void itemSendComplete(String tagId, int status) {
        for (int i = 0; i < lists.size(); i++) {
            MsgInfo info = lists.get(i);

            BaseMessage baseMsg;
            ImageMessage imageMsg;
            TextMessage textMsg;
            LinkMessage linkMessage;
            GoodsMessage goodsMessage;
            EvaluateMessage evaluateMessage;
            OrderMessage orderMessage;

            baseMsg = str2Msg(info.getMessage());
            if (tagId.equals(baseMsg.tag_id)) {
                switch (baseMsg.msg_type) {
                    case "image":
                        imageMsg = (ImageMessage) baseMsg;
                        imageMsg.setStatus(status);
                        info.setMessage(msg2Str(imageMsg));
                        break;
                    case "link":
                        linkMessage = (LinkMessage) baseMsg;
                        linkMessage.setStatus(status);
                        info.setMessage(msg2Str(linkMessage));
                        break;
                    case "goods":
                        goodsMessage = (GoodsMessage) baseMsg;
                        goodsMessage.setStatus(status);
                        info.setMessage(msg2Str(goodsMessage));
                        break;
                    case "order ":
                        orderMessage = (OrderMessage) baseMsg;
                        orderMessage.setStatus(status);
                        info.setMessage(msg2Str(orderMessage));
                        break;
                    case "evalute ":
                        evaluateMessage = (EvaluateMessage) baseMsg;
                        evaluateMessage.setStatus(status);
                        info.setMessage(msg2Str(evaluateMessage));
                        break;
                    default:
                        textMsg = (TextMessage) baseMsg;
                        textMsg.setStatus(status);
                        info.setMessage(msg2Str(textMsg));
                        break;
                }
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void initEvaluteStatus(int score, EvaluateViewHolder evaluateViewHolder) {
        if (score == 0) {
            evaluateViewHolder.tv_very_unsatisfy.setEnabled(true);
            evaluateViewHolder.tv_unsatisfy.setEnabled(true);
            evaluateViewHolder.tv_commonly.setEnabled(true);
            evaluateViewHolder.tv_satisfy.setEnabled(true);
            evaluateViewHolder.tv_very_satisfy.setEnabled(true);
        } else {
            evaluateViewHolder.tv_very_unsatisfy.setEnabled(false);
            evaluateViewHolder.tv_unsatisfy.setEnabled(false);
            evaluateViewHolder.tv_commonly.setEnabled(false);
            evaluateViewHolder.tv_satisfy.setEnabled(false);
            evaluateViewHolder.tv_very_satisfy.setEnabled(false);

            updateEvaluteStatus(score, evaluateViewHolder);
        }
    }

    public void updateEvaluteStatus(int score, EvaluateViewHolder evaluateViewHolder) {
        setImg(false, evaluateViewHolder.tv_very_unsatisfy);
        setImg(false, evaluateViewHolder.tv_unsatisfy);
        setImg(false, evaluateViewHolder.tv_commonly);
        setImg(false, evaluateViewHolder.tv_satisfy);
        setImg(false, evaluateViewHolder.tv_very_satisfy);
        switch (score) {
            case 1:
                setImg(true, evaluateViewHolder.tv_very_unsatisfy);
                break;
            case 2:
                setImg(true, evaluateViewHolder.tv_unsatisfy);
                break;
            case 3:
                setImg(true, evaluateViewHolder.tv_commonly);
                break;
            case 4:
                setImg(true, evaluateViewHolder.tv_satisfy);
                break;
            case 5:
                setImg(true, evaluateViewHolder.tv_very_satisfy);
                break;
        }
    }

    public void setImg(boolean isSelect, TextView textView) {
        Drawable drawable;
        if (isSelect) {
            drawable = context.getResources().getDrawable(R.mipmap.icon_chat_pingjia_h);
        } else {
            drawable = context.getResources().getDrawable(R.mipmap.icon_chat_pingjia_n);
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }
}
