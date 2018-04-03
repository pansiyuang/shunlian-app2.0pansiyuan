package com.shunlian.app.newchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shunlian.app.R;
import com.shunlian.app.newchat.entity.BaseMessage;
import com.shunlian.app.newchat.entity.ImageMessage;
import com.shunlian.app.newchat.entity.LinkMessage;
import com.shunlian.app.newchat.entity.MsgInfo;
import com.shunlian.app.newchat.entity.TextMessage;
import com.shunlian.app.newchat.util.EmoticonUtil;
import com.shunlian.app.newchat.websocket.MessageStatus;
import com.shunlian.app.utils.Common;
import com.shunlian.app.utils.GlideUtils;
import com.shunlian.app.widget.MyImageView;
import com.shunlian.app.widget.circle.CircleImageView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.shunlian.app.newchat.util.TimeUtil.getNewChatTime;
import static com.shunlian.app.utils.BitmapUtil.MAX_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MAX_WIDTH;
import static com.shunlian.app.utils.BitmapUtil.MIN_HEIGHT;
import static com.shunlian.app.utils.BitmapUtil.MIN_WIDTH;


/**
 * Created by Administrator on 2017/9/23.
 */

public class ChatAdpater extends BaseAdapter {

    private Context context;
    private List<MsgInfo> msgInfos;
    private final ObjectMapper objectMapper;
    private String current_small_url;
    private String current_bigl_url;
    private AssetManager am;
    private OnLinkClickListener listener;


    public ChatAdpater(Context context, List<MsgInfo> msgInfos) {
        this.context = context;
        this.msgInfos = msgInfos;
        objectMapper = new ObjectMapper();
        am = context.getAssets();
    }

    @Override
    public int getCount() {
        if (msgInfos != null)
            return msgInfos.size();
        else
            return 0;
    }

    @Override
    public MsgInfo getItem(int position) {
        if (msgInfos != null)
            return msgInfos.get(position);
        else
            return null;
    }

    public void itemSendComplete(String tagId, int status) {
        for (MsgInfo info : msgInfos) {
            BaseMessage baseMsg;
            ImageMessage imageMsg;
            TextMessage textMsg;
            LinkMessage linkMessage;

            baseMsg = str2Msg(info.getMessage());
            if (tagId.equals(baseMsg.tag_id)) {
                if ("image".equals(baseMsg.msg_type)) {
                    imageMsg = (ImageMessage) baseMsg;
                    imageMsg.setStatus(status);
                    info.setMessage(msg2Str(imageMsg));
                } else if ("link".equals(baseMsg.msg_type)) {
                    linkMessage = (LinkMessage) baseMsg;
                    linkMessage.setStatus(status);
                    info.setMessage(msg2Str(linkMessage));
                } else {
                    textMsg = (TextMessage) baseMsg;
                    textMsg.setStatus(status);
                    info.setMessage(msg2Str(textMsg));
                }
                break;
            }
        }
        notifyDataSetChanged();
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
        msgInfos.add(resizeImg(msgInfo));
        notifyDataSetChanged();
    }

    public void addMsgInfo(int position, MsgInfo msgInfo) {
        BaseMessage message;
        try {
            message = objectMapper.readValue(msgInfo.getMessage(), BaseMessage.class);
            if (message.getSendType() != BaseMessage.VALUE_SYSTEM) {
                addTimeMessage(true, msgInfo.getSend_time(), getLastMessageTime(true));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        msgInfos.add(position, resizeImg(msgInfo));
    }

    public void addMsgInfos(List<MsgInfo> infos) {
        //解决加载出现滑动不到最底部的bug  加载图片消息延迟加载，listView计算错误
        for (MsgInfo info : infos) {
            addMsgInfo(info);
        }
        notifyDataSetChanged();
    }

    public void addMsgInfos(int position, List<MsgInfo> infos) {
        for (MsgInfo info : infos) {
            addMsgInfo(position, info);
        }
        notifyDataSetChanged();
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        MsgInfo item = getItem(position);

        String message = item.getMessage();
        BaseMessage baseMessage = str2Msg(message);
        int sendType = baseMessage.getSendType();
        String msg_type = baseMessage.msg_type;
        TextMessage msgText;
        final ImageMessage imageMessage;
        LinkMessage linkMessage;

        ChatHolder chatHolder;
        if (convertView == null) {
            chatHolder = new ChatHolder();
        } else {
            chatHolder = (ChatHolder) convertView.getTag();
        }

        if (item == null) {
            return chatHolder.getRootView();
        }

        if (sendType == BaseMessage.VALUE_RIGHT) {//右侧
            chatHolder.ll_msg_left.setVisibility(View.GONE);
            chatHolder.ll_system.setVisibility(View.GONE);
            chatHolder.ll_system_link.setVisibility(View.GONE);
            chatHolder.rl_msg_right.setVisibility(View.VISIBLE);
            chatHolder.rl_msg_status.setVisibility(View.VISIBLE);

            GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_icon_right,baseMessage.from_headurl);

            if (baseMessage.getStatus() == MessageStatus.SendFail) {
                chatHolder.iv_status_error.setVisibility(View.VISIBLE);
                chatHolder.pb_right.setVisibility(View.GONE);
            } else if (baseMessage.getStatus() == MessageStatus.Sending) {
                chatHolder.iv_status_error.setVisibility(View.GONE);
                chatHolder.pb_right.setVisibility(View.VISIBLE);
            } else {
                chatHolder.rl_msg_status.setVisibility(View.GONE);
            }

            if ("text".equals(msg_type)) {
                msgText = (TextMessage) baseMessage;
                chatHolder.rl_msg_text_right.setVisibility(View.VISIBLE);
                chatHolder.rl_msg_img_right.setVisibility(View.GONE);
                chatHolder.ll_msg_link_right.setVisibility(View.GONE);
                chatHolder.tv_msg_text_right.setText(getEmotionContent(chatHolder.tv_msg_text_right, msgText.msg_body));
            } else if ("image".equals(msg_type)) {
                imageMessage = (ImageMessage) baseMessage;
                chatHolder.rl_msg_text_right.setVisibility(View.GONE);
                chatHolder.rl_msg_img_right.setVisibility(View.VISIBLE);
                chatHolder.ll_msg_link_right.setVisibility(View.GONE);
                ImageMessage.ImageBody msg_body = imageMessage.msg_body;
                if (imageMessage.msg_body != null) {
                    setImg(chatHolder.iv_msg_img_right, msg_body);
                    if (TextUtils.isEmpty(msg_body.localUrl)) {
                        String imagehost = msg_body.img_host;
                        if (!imagehost.startsWith("https")) {
                            imagehost = "https://" + imagehost;
                        }
                        current_small_url = imagehost + msg_body.img_small;
                        current_bigl_url = imagehost + msg_body.img_original;
                        GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_img_right,current_small_url);
                    } else {
                        current_bigl_url = "file://" + msg_body.localUrl;
                        GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_img_right,current_bigl_url);
                    }
                    chatHolder.iv_msg_img_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkBigImg(position);
                        }
                    });
                }
            } else if ("link".equals(msg_type)) {
                linkMessage = (LinkMessage) baseMessage;
                chatHolder.rl_msg_text_right.setVisibility(View.GONE);
                chatHolder.rl_msg_img_right.setVisibility(View.GONE);
                chatHolder.ll_msg_link_right.setVisibility(View.VISIBLE);

                final LinkMessage.LinkBody linkBody = linkMessage.msg_body;
                chatHolder.tv_msg_title_right.setText(linkBody.title);
                chatHolder.tv_msg_price_right.setText(linkBody.price);
                GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_link_right,linkBody.goodsImage);

                chatHolder.ll_msg_link_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!linkBody.goodsId.isEmpty()) {
//                            Common.requestGoodsDetail(context, linkBody.goodsId, null);
                        }
                    }
                });
            }
        } else if (sendType == BaseMessage.VALUE_LEFT) {//左侧

            chatHolder.ll_msg_left.setVisibility(View.VISIBLE);
            chatHolder.rl_msg_right.setVisibility(View.GONE);
            chatHolder.rl_msg_status.setVisibility(View.GONE);
            chatHolder.ll_system.setVisibility(View.GONE);
            chatHolder.ll_system_link.setVisibility(View.GONE);

            GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_icon_left,baseMessage.from_headurl);

            if ("text".equals(msg_type)) {
                msgText = (TextMessage) baseMessage;
                chatHolder.rl_msg_text_left.setVisibility(View.VISIBLE);
                chatHolder.rl_msg_img_left.setVisibility(View.GONE);
                chatHolder.ll_msg_link_left.setVisibility(View.GONE);
                chatHolder.tv_msg_text_left.setText(getEmotionContent(chatHolder.tv_msg_text_left, msgText.msg_body));
            } else if ("image".equals(msg_type)) {
                imageMessage = (ImageMessage) baseMessage;
                chatHolder.rl_msg_text_left.setVisibility(View.GONE);
                chatHolder.rl_msg_img_left.setVisibility(View.VISIBLE);
                chatHolder.ll_msg_link_left.setVisibility(View.GONE);

                ImageMessage.ImageBody msg_body = imageMessage.msg_body;
                if (imageMessage.msg_body != null) {
                    setImg(chatHolder.iv_msg_img_left, msg_body);
                    if (!msg_body.img_host.startsWith("https:")) {
                        current_small_url = "https://" + msg_body.img_host + msg_body.img_small;
                        current_bigl_url = "https://" + msg_body.img_host + msg_body.img_original;
                    } else {
                        current_small_url = msg_body.img_host + msg_body.img_small;
                        current_bigl_url = msg_body.img_host + msg_body.img_original;
                    }
                    GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_img_left,current_small_url);
                    chatHolder.iv_msg_img_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkBigImg(position);
                        }
                    });
                }
            } else if ("link".equals(msg_type)) {
                linkMessage = (LinkMessage) baseMessage;
                chatHolder.rl_msg_text_left.setVisibility(View.GONE);
                chatHolder.rl_msg_img_left.setVisibility(View.GONE);
                chatHolder.ll_msg_link_left.setVisibility(View.VISIBLE);
                final LinkMessage.LinkBody linkBody = linkMessage.msg_body;
                chatHolder.tv_msg_title_left.setText(linkBody.title);
                chatHolder.tv_msg_price_left.setText(linkBody.price);
                GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_link_left,linkBody.goodsImage);

                chatHolder.ll_msg_link_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!linkBody.goodsId.isEmpty()) {
//                            Common.requestGoodsDetail(context, linkBody.goodsId, null);
                        }
                    }
                });
            }
        } else if (sendType == BaseMessage.VALUE_SYSTEM) {
            chatHolder.ll_msg_left.setVisibility(View.GONE);
            chatHolder.rl_msg_right.setVisibility(View.GONE);
            chatHolder.rl_msg_status.setVisibility(View.GONE);

            if ("system".equals(msg_type)) {
                chatHolder.ll_system_link.setVisibility(View.GONE);
                chatHolder.ll_system.setVisibility(View.VISIBLE);
                msgText = (TextMessage) baseMessage;
                long currentTime = Long.valueOf(msgText.msg_body);
                chatHolder.tv_systemMessage.setText(getNewChatTime(currentTime));
            } else if ("sys_text".equals(msg_type)) {
                chatHolder.ll_system_link.setVisibility(View.GONE);
                chatHolder.ll_system.setVisibility(View.VISIBLE);
                msgText = (TextMessage) baseMessage;
                chatHolder.tv_systemMessage.setText(msgText.msg_body);
            } else if ("sys_link".equals(msg_type)) {
                chatHolder.ll_system_link.setVisibility(View.VISIBLE);
                chatHolder.ll_system.setVisibility(View.GONE);
                linkMessage = (LinkMessage) baseMessage;
                LinkMessage.LinkBody linkBody = linkMessage.msg_body;
                chatHolder.tv_msg_title.setText(linkBody.title);
                chatHolder.tv_msg_price.setText(context.getResources().getString(R.string.common_yuan) + linkBody.price);
                GlideUtils.getInstance().loadImage(context,chatHolder.iv_msg_link,linkBody.goodsImage);
                chatHolder.tv_send_goods.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.OnClick();
                        }
                    }
                });
            }
        }
        return chatHolder.getRootView();
    }


    public class ChatHolder {

        private final View rootView;
        private final LinearLayout ll_msg_left;
        private final LinearLayout ll_system;
        private final LinearLayout ll_msg_link_right;
        private final LinearLayout ll_msg_link_left;
        private final CircleImageView iv_msg_icon_left;
        private final TextView tv_msg_text_left;
        private final LinearLayout rl_msg_right;
        private final RelativeLayout rl_msg_text_right;
        private final RelativeLayout rl_msg_text_left;
        private final RelativeLayout rl_msg_img_left;
        private final RelativeLayout rl_msg_img_right;
        private final RelativeLayout rl_msg_status;
        private final TextView tv_systemMessage;
        private final CircleImageView iv_msg_icon_right;
        private final MyImageView iv_msg_img_left;
        private final MyImageView iv_msg_img_right;
        private final TextView tv_msg_text_right;
        private final LinearLayout ll_system_link;

        private final MyImageView iv_msg_link_right;
        private final TextView tv_msg_title_right;
        private final TextView tv_msg_price_right;

        private final MyImageView iv_msg_link_left;
        private final TextView tv_msg_title_left;
        private final TextView tv_msg_price_left;

        private final MyImageView iv_msg_link;
        private final TextView tv_msg_title;
        private final TextView tv_msg_price;
        private final TextView tv_send_goods;

        private final MyImageView iv_status_error;
        private final ProgressBar pb_right;

        public View getRootView() {
            return rootView;
        }

        public ChatHolder() {
            //左侧文本
            rootView = LayoutInflater.from(context).inflate(R.layout.item_chat, null);
            ll_msg_left = (LinearLayout) rootView.findViewById(R.id.ll_msg_left);
            rl_msg_text_left = (RelativeLayout) rootView.findViewById(R.id.rl_msg_text_left);
            rl_msg_img_left = (RelativeLayout) rootView.findViewById(R.id.rl_msg_img_left);
            ll_msg_link_left = (LinearLayout) rootView.findViewById(R.id.ll_msg_link_left);
            iv_msg_icon_left = (CircleImageView) rootView.findViewById(R.id.iv_msg_icon_left);
            iv_msg_img_left = (MyImageView) rootView.findViewById(R.id.iv_msg_img_left);
            tv_msg_text_left = (TextView) rootView.findViewById(R.id.tv_msg_text_left);
            iv_msg_link_left = (MyImageView) rootView.findViewById(R.id.iv_msg_link_left);
            tv_msg_title_left = (TextView) rootView.findViewById(R.id.tv_msg_title_left);
            tv_msg_price_left = (TextView) rootView.findViewById(R.id.tv_msg_price_left);


            //右侧文本
            rl_msg_right = (LinearLayout) rootView.findViewById(R.id.rl_msg_right);
            rl_msg_img_right = (RelativeLayout) rootView.findViewById(R.id.rl_msg_img_right);
            rl_msg_text_right = (RelativeLayout) rootView.findViewById(R.id.rl_msg_text_right);
            ll_msg_link_right = (LinearLayout) rootView.findViewById(R.id.ll_msg_link_right);
            iv_msg_icon_right = (CircleImageView) rootView.findViewById(R.id.iv_msg_icon_right);
            iv_msg_img_right = (MyImageView) rootView.findViewById(R.id.iv_msg_img_right);
            tv_msg_text_right = (TextView) rootView.findViewById(R.id.tv_msg_text_right);
            iv_msg_link_right = (MyImageView) rootView.findViewById(R.id.iv_msg_link_right);
            tv_msg_title_right = (TextView) rootView.findViewById(R.id.tv_msg_title_right);
            tv_msg_price_right = (TextView) rootView.findViewById(R.id.tv_msg_price_right);


            //系统消息
            ll_system = (LinearLayout) rootView.findViewById(R.id.ll_system);
            tv_systemMessage = (TextView) rootView.findViewById(R.id.tv_systemMessage);
            rl_msg_status = (RelativeLayout) rootView.findViewById(R.id.rl_msg_status);

            //链接消息
            ll_system_link = (LinearLayout) rootView.findViewById(R.id.ll_system_link);
            iv_msg_link = (MyImageView) rootView.findViewById(R.id.iv_msg_link);
            tv_msg_title = (TextView) rootView.findViewById(R.id.tv_msg_title);
            tv_msg_price = (TextView) rootView.findViewById(R.id.tv_msg_price);
            tv_send_goods = (TextView) rootView.findViewById(R.id.tv_send_goods);

            iv_status_error = (MyImageView) rootView.findViewById(R.id.iv_status_error);
            pb_right = (ProgressBar) rootView.findViewById(R.id.pb_right);
            rootView.setTag(this);
        }
    }


    public BaseMessage str2Msg(String msg) {
        BaseMessage message = null;
        try {
            message = objectMapper.readValue(msg, BaseMessage.class);
            if ("text".equals(message.msg_type) || "system".equals(message.msg_type) || "sys_text".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, TextMessage.class);
            } else if ("image".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, ImageMessage.class);
            } else if ("link".equals(message.msg_type) || "sys_link".equals(message.msg_type)) {
                message = objectMapper.readValue(msg, LinkMessage.class);
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

    /**
     * 查看大图
     *
     * @param position
     */
    private void checkBigImg(int position) {
        String imageUrl = null;
        String message = getItem(position).getMessage();
        BaseMessage baseMessage = str2Msg(message);
        if (baseMessage instanceof ImageMessage) {
            ImageMessage.ImageBody imageBody = ((ImageMessage) baseMessage).msg_body;
            if (TextUtils.isEmpty(imageBody.localUrl)) {
                imageUrl = imageBody.img_host + imageBody.img_original;
                if (!imageUrl.startsWith("https")) {
                    imageUrl = "https://" + imageUrl;
                }
            } else {
                imageUrl = "file://" + imageBody.localUrl;
            }
        }
//        Intent intent = new Intent(context, CommentsImageViewAct.class);
//        PublishCommentsImgsEntity publishCommentsImgsEntity = new PublishCommentsImgsEntity();
//        ArrayList<PublishCommentsImgsEntity.Item> tempImgList = new ArrayList<>();
//        PublishCommentsImgsEntity.Item item = new PublishCommentsImgsEntity.Item();
//        item.setUrl(imageUrl);
//        tempImgList.add(item);
//        publishCommentsImgsEntity.setItemList(tempImgList);
//        publishCommentsImgsEntity.setIndex(0);
//        intent.putExtra("data", publishCommentsImgsEntity);
//        context.startActivity(intent);
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
            msgInfos.add(0, info);
        } else {
            msgInfos.add(info);
        }
    }

    private long getLastMessageTime(boolean isPre) {
        long sendTime = 0;
        if (msgInfos == null || msgInfos.size() == 0) {
            return sendTime;
        } else {
            if (isPre) {
                sendTime = msgInfos.get(0).send_time;
            } else {
                sendTime = msgInfos.get(msgInfos.size() - 1).send_time;
            }
            return sendTime;
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

    public SpannableString getEmotionContent(final TextView tv, String source) {
        int position = -1;
        SpannableString spannableString = new SpannableString(source);

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            for (int i = 0; i < EmoticonUtil.emoticonData.length; i++) {
                if (key.equals(EmoticonUtil.emoticonData[i])) {
                    position = i;
                    break;
                }
            }
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            InputStream is = null;
            try {
                is = am.open(String.format("emoticon/%d.png", position));
                // 压缩表情图片
                int size = (int) tv.getTextSize() + 4;
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                int end = start + key.length();
                ImageSpan span = new ImageSpan(context, scaleBitmap);
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

    public void setOnLinkClickListener(OnLinkClickListener linkClickListener) {
        this.listener = linkClickListener;
    }

    public interface OnLinkClickListener {
        void OnClick();
    }
}